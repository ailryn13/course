package com.example.studentapp.repository;

import com.example.studentapp.dto.CourseDTO; // Import the new DTO
import com.example.studentapp.entity.CourseBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseBean, Long> {
    List<CourseDTO> findAllProjectedBy();
}