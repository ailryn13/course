package com.example.studentapp.dto;

import java.time.LocalDateTime;

public class EnrollmentDTO {
    private Long id;
    private StudentDTO student;
    private CourseDTO course;
    private LocalDateTime enrollmentDate;

    public EnrollmentDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StudentDTO getStudent() { return student; }
    public void setStudent(StudentDTO student) { this.student = student; }

    public CourseDTO getCourse() { return course; }
    public void setCourse(CourseDTO course) { this.course = course; }

    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDateTime enrollmentDate) { this.enrollmentDate = enrollmentDate; }
}