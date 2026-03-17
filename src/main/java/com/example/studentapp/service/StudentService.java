package com.example.studentapp.service;

import com.example.studentapp.dto.StudentDTO;
import com.example.studentapp.entity.StudentBean;

public interface StudentService {

    StudentDTO registerStudent(StudentBean student);

    StudentDTO loginStudent(String email, String rawPassword);

}