package com.example.studentapp.service;

import com.example.studentapp.dto.CourseDTO;
import com.example.studentapp.entity.CourseBean;
import java.util.List;

// CHANGE 'class' TO 'interface' RIGHT HERE:
public interface CourseService {

    // Notice that these methods don't have bodies {}, just a semicolon at the end!
    CourseDTO createCourse(CourseBean courseBean);

    List<CourseDTO> getAllCourses();

    CourseDTO getCourseById(Long id);

}