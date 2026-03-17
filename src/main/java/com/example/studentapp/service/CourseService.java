package com.example.studentapp.service;

import com.example.studentapp.dto.CourseDTO;
import com.example.studentapp.entity.Course;
import com.example.studentapp.entity.CourseBean;
import com.example.studentapp.exception.AppExceptions;
import com.example.studentapp.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository){
        this.courseRepository = courseRepository;
    }

    // Creating a course still uses the Entity (or a Request DTO in more advanced setups)
    public Course createCourse(CourseBean course){
        course.setAvailableSeats(course.getTotalSeats());
        return courseRepository.save(course);
    }

    // Updated to return the new DTO Interface!
    public List<CourseDTO> getAllCourses(){
        return courseRepository.findAllProjectedBy();
    }

    public Course getCourseById(Long id){
        return courseRepository.findById(id)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("Course not found with ID: " + id));
    }
}