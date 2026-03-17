package com.example.studentapp.controller;

import com.example.studentapp.dto.EnrollmentDTO;
import com.example.studentapp.service.EnrollmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/enroll")
@CrossOrigin(origins = "*")
public class EnrollmentController {
    private static final Logger logger = LogManager.getLogger(EnrollmentController.class);

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService){ // change autowire
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/{courseId}")
    public ResponseEntity<?> enrollStudent(@PathVariable Long courseId,@RequestParam Long studentId){
        logger.info("Student ID {} is attempting to enroll in Course ID {}", studentId, courseId);
        EnrollmentDTO enrollment = enrollmentService.enrollStudent(studentId, courseId);
        logger.info("Successfully enrolled Student ID {} into Course ID {}", studentId, courseId);
        return ResponseEntity.ok(enrollment);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> unenrollStudent(@PathVariable Long courseId,@RequestParam Long studentId){
        logger.info("Student ID {} is attempting to unenroll from Course ID {}",studentId, courseId);
        enrollmentService.unenrollStudent(studentId,courseId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successfully unenrolled from the course!");
        logger.info("Successfully unenrolled Student ID {} from Course ID {}",studentId ,courseId);
        return ResponseEntity.ok(response);
    }
}