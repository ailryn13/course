package com.example.studentapp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "COURSE")
public class CourseBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String name;
    @Column(length = 500)
    private String description;
    @Column(length = 50)
    private String duration;
    @Column(name = "TOTAL_SEATS", nullable = false)
    private Integer totalSeats;
    @Column(name = "AVAILABLE_SEATS", nullable = false)
    private Integer availableSeats;

    public CourseBean(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }
}