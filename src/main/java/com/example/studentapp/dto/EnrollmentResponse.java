package com.example.studentapp.dto;

public class EnrollmentResponse implements EnrollmentDTO {
    private Long id;
    private StudentDTO student;
    private CourseDTO course;

    @Override public Long getId() { return id; }
    @Override public StudentDTO getStudent() { return student; }
    @Override public CourseDTO getCourse() { return course; }

    public void setId(Long id) { this.id = id; }
    public void setStudent(StudentDTO student) { this.student = student; }
    public void setCourse(CourseDTO course) { this.course = course; }
}