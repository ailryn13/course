package com.example.studentapp.service;

import com.example.studentapp.dto.EnrollmentDTO;
import java.util.List;

public interface EnrollmentService {
    EnrollmentDTO enrollStudent(Long studentId, Long courseId);
    void unenrollStudent(Long studentId, Long courseId);
    List<EnrollmentDTO> getStudentEnrollments(Long studentId);
}