package com.example.studentapp.service.impl;

import com.example.studentapp.dto.CourseDTO;
import com.example.studentapp.dto.EnrollmentDTO;
import com.example.studentapp.dto.StudentDTO;
import com.example.studentapp.entity.CourseBean;
import com.example.studentapp.entity.EnrollmentBean;
import com.example.studentapp.entity.StudentBean;
import com.example.studentapp.exception.AppExceptions;
import com.example.studentapp.repository.CourseRepository;
import com.example.studentapp.repository.EnrollmentRepository;
import com.example.studentapp.repository.StudentRepository;
import com.example.studentapp.service.EnrollmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository, CourseRepository courseRepository){
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    // --- Private DTO Mapping Helper ---
    private EnrollmentDTO convertToDto(EnrollmentBean enrollment) {
        EnrollmentDTO dto = new EnrollmentDTO();
        BeanUtils.copyProperties(enrollment, dto, "student", "course");

        if (enrollment.getStudent() != null) {
            StudentDTO studentDto = new StudentDTO();
            BeanUtils.copyProperties(enrollment.getStudent(), studentDto);
            studentDto.setPassword(null);
            dto.setStudent(studentDto);
        }

        if (enrollment.getCourse() != null) {
            CourseDTO courseDto = new CourseDTO();
            BeanUtils.copyProperties(enrollment.getCourse(), courseDto);
            dto.setCourse(courseDto);
        }

        return dto;
    }

    // --- Interface Methods ---
    @Override
    @Transactional
    public EnrollmentDTO enrollStudent(Long studentId, Long courseId) {
        StudentBean student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Student not found!"));
        CourseBean course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Course not found!"));

        if(enrollmentRepository.existsByStudentAndCourse(student,course)){
            throw new AppExceptions.DuplicateResourceException("Student is already Enrolled in this course!");
        }
        if(course.getAvailableSeats() <= 0){
            throw new AppExceptions.CourseFullException("Sorry, this course is fully booked!");
        }

        course.setAvailableSeats(course.getAvailableSeats()-1);
        courseRepository.save(course);

        EnrollmentBean enrollment = new EnrollmentBean();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        EnrollmentBean savedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDto(savedEnrollment);
    }

    @Override
    @Transactional
    public void unenrollStudent(Long studentId, Long courseId){
        StudentBean student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Student not found!"));
        CourseBean course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Course not found!"));

        EnrollmentBean enrollment = enrollmentRepository.findByStudentAndCourse(student,course)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Enrollment record not found"));

        enrollmentRepository.delete(enrollment);

        course.setAvailableSeats(course.getAvailableSeats()+1);
        courseRepository.save(course);
    }

    @Override
    public List<EnrollmentDTO> getStudentEnrollments(Long studentId){
        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}