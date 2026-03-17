package com.example.studentapp.repository;

import com.example.studentapp.entity.StudentBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface StudentRepository extends JpaRepository<StudentBean, Long> {
    Optional<StudentBean> findByEmail(String email);

}