package com.example.studentapp.service.impl;

import com.example.studentapp.dto.CourseDTO;
import com.example.studentapp.dto.CourseResponse;
import com.example.studentapp.entity.CourseBean;
import com.example.studentapp.exception.AppExceptions;
import com.example.studentapp.repository.CourseRepository;
import com.example.studentapp.service.CourseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger logger = LogManager.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    private CourseDTO convertToDto(CourseBean course) {
        CourseResponse courseDto = new CourseResponse();
        BeanUtils.copyProperties(course, courseDto);
        return courseDto;
    }

    @Override
    public CourseDTO createCourse(CourseBean courseBean){
        logger.info("Attempting to create a new course: {}", courseBean.getName());

        try {
            courseBean.setAvailableSeats(courseBean.getTotalSeats());
            CourseBean savedCourse = courseRepository.save(courseBean);

            logger.info("Successfully created course with ID: {}", savedCourse.getId());
            return convertToDto(savedCourse);
        } catch (DataIntegrityViolationException ex) {
            logger.error("Database constraint violation while creating course '{}': {}", courseBean.getName(), ex.getMessage());
            throw new AppExceptions.BadRequestException("Failed to create course due to invalid data or a database conflict.");
        }
    }

    @Override
    public List<CourseDTO> getAllCourses(){
        logger.debug("Fetching all courses from the database.");
        return courseRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDTO getCourseById(Long id){
        logger.debug("Fetching course by ID: {}", id);
        CourseBean course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Course lookup failed: ID {} not found", id);
                    return new AppExceptions.ResourceNotFoundException("Course not found with ID: " + id);
                });
        return convertToDto(course);
    }
}