package com.example.studentapp.controller;

import com.example.studentapp.dto.StudentDTO;
import com.example.studentapp.entity.StudentBean;
import com.example.studentapp.service.EnrollmentService;
import com.example.studentapp.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private static final Logger logger = LogManager.getLogger(StudentController.class);

    // Injecting the Interface! Spring automatically uses StudentServiceImpl
    private final StudentService studentService;
    private final EnrollmentService enrollmentService;

    public StudentController(StudentService studentService, EnrollmentService enrollmentService){
        this.studentService = studentService;
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody StudentBean student){
        logger.info("Attempting to register a new student with email: {}", student.getEmail());

        // Using the DTO
        StudentDTO savedStudent = studentService.registerStudent(student);

        Map<String,Object> response = new HashMap<>();
        response.put("message","Registration Successful!");
        response.put("studentId", savedStudent.getId());

        logger.info("Registration successful for student ID: {}", savedStudent.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginStudent(@RequestBody Map<String,String> credentials){
        String email = credentials.get("email");
        String password = credentials.get("password");

        logger.info("login attempt for email: {}", email);

        // Using the DTO
        StudentDTO loggedInStudent = studentService.loginStudent(email, password);

        Map<String, Object> response = new HashMap<>();
        response.put("message","login Successful!");
        response.put("studentId", loggedInStudent.getId());
        response.put("name", loggedInStudent.getName());

        logger.info("login successful for Student ID: {}", loggedInStudent.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/enrollments")
    public ResponseEntity<?> getStudentEnrollments(@PathVariable Long id){
        logger.info("Fetching enrollments for student ID: {}", id);
        return ResponseEntity.ok(enrollmentService.getStudentEnrollments(id));
    }
}