package com.example.studentapp.dto;

public class StudentResponse implements StudentDTO {
    private final Long id;
    private final String name;
    private final String email;

    public StudentResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public Long getId() { return id; }
    @Override
    public String getName() { return name; }
    @Override
    public String getEmail() { return email; }
}