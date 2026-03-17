package com.example.studentapp.repository;

import com.example.studentapp.entity.StudentBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentBean, Long> {
    Optional<StudentBean> findByEmail(String email);

}