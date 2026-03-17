package com.example.studentapp.controller;

import com.example.studentapp.dto.CourseDTO; // <-- Added DTO import
import com.example.studentapp.entity.CourseBean;
import com.example.studentapp.service.CourseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private static final Logger logger = LogManager.getLogger(CourseController.class);

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @PostMapping
    // Change parameter back to CourseBean!
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseBean courseBean){
        logger.info("Attempting to create a new Course: {}", courseBean.getName());
        CourseDTO savedCourse = courseService.createCourse(courseBean);
        return ResponseEntity.ok(savedCourse);
    }

    @GetMapping
    // <-- Changed Course to CourseDto in the return type
    public ResponseEntity<List<CourseDTO>> getAllCourses(){
        logger.info("Fetching All available courses");

        // <-- Changed Course to CourseDto here
        List<CourseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
}