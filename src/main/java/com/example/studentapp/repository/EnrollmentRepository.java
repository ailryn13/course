package com.example.studentapp.repository;

import com.example.studentapp.entity.CourseBean;
import com.example.studentapp.entity.EnrollmentBean;
import com.example.studentapp.entity.StudentBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<EnrollmentBean, Long> {
    boolean existsByStudentAndCourse(StudentBean student, CourseBean course);

    List<EnrollmentBean> findByStudentId(Long studentID);

    Optional<EnrollmentBean> findByStudentAndCourse(StudentBean student, CourseBean course);
}