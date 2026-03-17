package com.example.studentapp.dto;

// Make sure this says 'interface', not 'class'!
public interface EnrollmentDTO {

    Long getId();

    StudentDTO getStudent();

    CourseDTO getCourse();

}