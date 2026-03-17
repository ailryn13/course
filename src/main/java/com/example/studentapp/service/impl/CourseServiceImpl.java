package com.example.studentapp.service.impl;

import com.example.studentapp.dto.CourseDTO;
import com.example.studentapp.dto.CourseResponse; // Imported the concrete class!
import com.example.studentapp.entity.CourseBean;
import com.example.studentapp.exception.AppExceptions;
import com.example.studentapp.repository.CourseRepository;
import com.example.studentapp.service.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    // --- Private Helper Method ---
    private CourseDTO convertToDto(CourseBean course) {
        // We instantiate the Response class, but return the Interface!
        CourseResponse courseDto = new CourseResponse();
        BeanUtils.copyProperties(course, courseDto);
        return courseDto;
    }

    // --- Interface Methods ---

    @Override
    // 1. Updated parameter to CourseBean to match the interface!
    public CourseDTO createCourse(CourseBean courseBean){

        // 2. We don't need to convert to an entity anymore, it's already an entity!
        courseBean.setAvailableSeats(courseBean.getTotalSeats());
        CourseBean savedCourse = courseRepository.save(courseBean);

        return convertToDto(savedCourse);
    }

    @Override
    public List<CourseDTO> getAllCourses(){
        return courseRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDTO getCourseById(Long id){
        CourseBean course = courseRepository.findById(id)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Course not found with ID: " + id));
        return convertToDto(course);
    }
}