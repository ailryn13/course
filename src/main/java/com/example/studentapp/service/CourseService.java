package com.example.studentapp.service;

import com.example.studentapp.dto.CourseDTO;
import com.example.studentapp.entity.CourseBean;
import java.util.List;

public interface CourseService {

    CourseDTO createCourse(CourseBean courseBean);

    List<CourseDTO> getAllCourses();

    CourseDTO getCourseById(Long id);

}