package com.example.studentapp.service.impl;

import com.example.studentapp.dto.*;
import com.example.studentapp.entity.CourseBean;
import com.example.studentapp.entity.EnrollmentBean;
import com.example.studentapp.entity.StudentBean;
import com.example.studentapp.exception.AppExceptions;
import com.example.studentapp.repository.CourseRepository;
import com.example.studentapp.repository.EnrollmentRepository;
import com.example.studentapp.repository.StudentRepository;
import com.example.studentapp.service.EnrollmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final Logger logger = LogManager.getLogger(EnrollmentServiceImpl.class);

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository, CourseRepository courseRepository){
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    private EnrollmentDTO convertToDto(EnrollmentBean enrollment) {
        EnrollmentResponse dto = new EnrollmentResponse();
        BeanUtils.copyProperties(enrollment, dto, "student", "course");

        if (enrollment.getStudent() != null) {
            StudentResponse studentDto = new StudentResponse(
                    enrollment.getStudent().getId(),
                    enrollment.getStudent().getName(),
                    enrollment.getStudent().getEmail()
            );
            dto.setStudent(studentDto);
        }

        if (enrollment.getCourse() != null) {
            CourseResponse courseDto = new CourseResponse();
            BeanUtils.copyProperties(enrollment.getCourse(), courseDto);
            dto.setCourse(courseDto);
        }
        return dto;
    }

    @Override
    @Transactional
    public EnrollmentDTO enrollStudent(Long studentId, Long courseId) {
        logger.info("Attempting to enroll Student ID {} in Course ID {}", studentId, courseId);

        StudentBean student = studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.warn("Enrollment failed: Student ID {} not found", studentId);
                    return new AppExceptions.ResourceNotFoundException("Student not found!");
                });

        CourseBean course = courseRepository.findById(courseId)
                .orElseThrow(() -> {
                    logger.warn("Enrollment failed: Course ID {} not found", courseId);
                    return new AppExceptions.ResourceNotFoundException("Course not found!");
                });

        if(enrollmentRepository.existsByStudentAndCourse(student,course)){
            logger.warn("Enrollment failed: Student ID {} is already enrolled in Course ID {}", studentId, courseId);
            throw new AppExceptions.DuplicateResourceException("Student is already Enrolled in this course!");
        }

        if(course.getAvailableSeats() <= 0){
            logger.warn("Enrollment failed: Course ID {} is fully booked", courseId);
            throw new AppExceptions.CourseFullException("Sorry, this course is fully booked!");
        }

        try {
            EnrollmentBean enrollment = new EnrollmentBean();
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            EnrollmentBean savedEnrollment = enrollmentRepository.save(enrollment);

            course.setAvailableSeats(course.getAvailableSeats() - 1);
            courseRepository.save(course);

            logger.info("Successfully enrolled Student ID {} in Course ID {}", studentId, courseId);
            return convertToDto(savedEnrollment);

        } catch (DataIntegrityViolationException ex) {
            logger.error("Database constraint violation during enrollment: {}", ex.getMessage());
            throw new AppExceptions.BadRequestException("Could not complete enrollment due to a database error.");
        }
    }

    @Override
    @Transactional
    public void unenrollStudent(Long studentId, Long courseId){
        logger.info("Attempting to unenroll Student ID {} from Course ID {}", studentId, courseId);

        StudentBean student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Student not found!"));
        CourseBean course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Course not found!"));

        EnrollmentBean enrollment = enrollmentRepository.findByStudentAndCourse(student,course)
                .orElseThrow(() -> {
                    logger.warn("Unenrollment failed: Record not found for Student ID {} in Course ID {}", studentId, courseId);
                    return new AppExceptions.ResourceNotFoundException("Enrollment record not found");
                });

        try {
            enrollmentRepository.delete(enrollment);
            course.setAvailableSeats(course.getAvailableSeats() + 1);
            courseRepository.save(course);
            logger.info("Successfully unenrolled Student ID {} from Course ID {}", studentId, courseId);
        } catch (Exception ex) {
            logger.error("Error during unenrollment process: {}", ex.getMessage());
            throw new AppExceptions.BadRequestException("Failed to unenroll student.");
        }
    }

    @Override
    public List<EnrollmentDTO> getStudentEnrollments(Long studentId){
        logger.debug("Fetching enrollments for Student ID {}", studentId);
        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}