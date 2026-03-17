package com.example.studentapp.dto;

public class CourseResponse implements CourseDTO {
    private Long id;
    private String name;
    private String description;
    private String duration;
    private Integer availableSeats;

    // --- Interface Getters ---
    @Override public Long getId() { return id; }
    @Override public String getName() { return name; }
    @Override public String getDescription() { return description; }
    @Override public String getDuration() { return duration; }
    @Override public Integer getAvailableSeats() { return availableSeats; }

    // --- Setters (Needed for BeanUtils) ---
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setDuration(String duration) { this.duration = duration; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }
}