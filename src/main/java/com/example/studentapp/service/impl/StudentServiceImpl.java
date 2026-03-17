package com.example.studentapp.service.impl;

import com.example.studentapp.service.StudentService;
import com.example.studentapp.dto.StudentDTO;
import com.example.studentapp.dto.StudentResponse;
import com.example.studentapp.entity.StudentBean;
import com.example.studentapp.exception.AppExceptions;
import com.example.studentapp.repository.StudentRepository;
import com.example.studentapp.util.ValidationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LogManager.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, PasswordEncoder passwordEncoder){
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public StudentDTO registerStudent(StudentBean student){
        logger.info("Starting registration process for student name: {}", student.getName());

        String cleanEmail = student.getEmail() != null ? student.getEmail().trim().toLowerCase() : null;
        String cleanName = student.getName() != null ? student.getName().trim() : null;

        student.setEmail(cleanEmail);
        student.setName(cleanName);

        validateStudentInputs(student, cleanName, cleanEmail);

        Optional<StudentBean> existingStudent = studentRepository.findByEmail(cleanEmail);
        if(existingStudent.isPresent()){
            logger.warn("Registration failed: Email {} is already registered.", cleanEmail);
            throw new AppExceptions.DuplicateResourceException("Email is already Registered!");
        }

        String hashedPassword = passwordEncoder.encode(student.getPassword());
        student.setPassword(hashedPassword);

        // 2. Targeted Try-Catch: Catching DB constraints and wrapping them in a business exception
        try {
            logger.debug("Attempting to save student to the database.");
            StudentBean savedStudent = studentRepository.save(student);
            logger.info("Successfully registered student with ID: {}", savedStudent.getId());

            return new StudentResponse(savedStudent.getId(), savedStudent.getName(), savedStudent.getEmail());
        } catch (DataIntegrityViolationException ex) {
            logger.error("Database constraint violation while saving student: {}", ex.getMessage());
            throw new AppExceptions.DuplicateResourceException("Failed to register student due to a database conflict.");
        }
    }

    @Override
    public StudentDTO loginStudent(String email, String rawPassword){
        logger.info("Starting login process for email: {}", email);
        String cleanEmail = email != null ? email.trim() : null;

        // Utilizing ValidationUtils here as well
        if(!ValidationUtils.isValidEmail(cleanEmail)){
            logger.warn("Login failed: Invalid email format for input: {}", cleanEmail);
            throw new AppExceptions.BadRequestException("Invalid email format!");
        }

        StudentBean student = studentRepository.findByEmail(cleanEmail)
                .orElseThrow(() -> {
                    logger.warn("Login failed: User with email {} does not exist.", cleanEmail);
                    return new AppExceptions.ResourceNotFoundException("User does not exist!");
                });

        if(!passwordEncoder.matches(rawPassword, student.getPassword())){
            logger.warn("Login failed: Invalid password provided for email: {}", cleanEmail);
            throw new AppExceptions.UnauthorizedException("Invalid email or Password!");
        }

        logger.info("Login successful for student ID: {}", student.getId());
        return new StudentResponse(student.getId(), student.getName(), student.getEmail());
    }

    // Extracted helper method for readability (DRY Principle)
    private void validateStudentInputs(StudentBean student, String cleanName, String cleanEmail) {
        if(!ValidationUtils.isValidName(cleanName)) {
            logger.warn("Validation failed: Invalid name format '{}'", cleanName);
            throw new AppExceptions.BadRequestException("Registration failed: Name can only contain letters and spaces!");
        }
        if(!ValidationUtils.isValidEmail(cleanEmail)) {
            logger.warn("Validation failed: Invalid email format '{}'", cleanEmail);
            throw new AppExceptions.BadRequestException("Registration failed: Invalid email format!");
        }
        if(!ValidationUtils.isValidPassword(student.getPassword())) {
            logger.warn("Validation failed: Password length is less than 6 characters.");
            throw new AppExceptions.BadRequestException("Registration failed: Password must be at least 6 characters!");
        }
    }
}