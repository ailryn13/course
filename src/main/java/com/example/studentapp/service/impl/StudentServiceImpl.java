package com.example.studentapp.service.impl;

import com.example.studentapp.service.StudentService;
import com.example.studentapp.dto.StudentDTO;
import com.example.studentapp.dto.StudentResponse;
import com.example.studentapp.entity.StudentBean;
import com.example.studentapp.exception.AppExceptions;
import com.example.studentapp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Notice we don't need BCryptPasswordEncoder import here anymore
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    private final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Autowired
    // We now ask Spring to inject the PasswordEncoder here!
    public StudentServiceImpl(StudentRepository studentRepository, PasswordEncoder passwordEncoder){
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder; // No more 'new' keyword!
    }

    @Override
    public StudentDTO registerStudent(StudentBean student){
        String cleanEmail = student.getEmail() != null ? student.getEmail().trim().toLowerCase() : null;
        String cleanName = student.getName() != null ? student.getName().trim() : null;

        student.setEmail(cleanEmail);
        student.setName(cleanName);

        if(cleanName==null || !cleanName.matches("[A-Za-z ]+$")){
            throw new AppExceptions.BadRequestException("Registration failed: Name can only contain letters and spaces!");
        }

        if(cleanEmail == null || !cleanEmail.matches(EMAIL_REGEX)){
            throw new AppExceptions.BadRequestException("Registration failed: Invalid email format!");
        }

        if(student.getPassword() == null || student.getPassword().length()<6){
            throw new AppExceptions.BadRequestException("Registration failed: Password must be at least 6 characters!");
        }

        Optional<StudentBean> existingStudent = studentRepository.findByEmail(cleanEmail);
        if(existingStudent.isPresent()){
            throw new AppExceptions.DuplicateResourceException("Email is already Registered!");
        }

        // The injected encoder works perfectly here
        String hashedPassword = passwordEncoder.encode(student.getPassword());
        student.setPassword(hashedPassword);

        StudentBean savedStudent = studentRepository.save(student);

        return new StudentResponse(savedStudent.getId(), savedStudent.getName(), savedStudent.getEmail());
    }

    @Override
    public StudentDTO loginStudent(String email, String rawPassword){
        String cleanEmail = email != null ? email.trim() : null;

        if(cleanEmail == null || !cleanEmail.matches(EMAIL_REGEX)){
            throw new AppExceptions.BadRequestException("Invalid email format!");
        }

        StudentBean student = studentRepository.findByEmail(cleanEmail)
                .orElseThrow(() -> new AppExceptions.ResourceNotFoundException("User does not exist!"));

        // The injected encoder works perfectly here
        if(!passwordEncoder.matches(rawPassword, student.getPassword())){
            throw new AppExceptions.UnauthorizedException("Invalid email or Password!");
        }

        return new StudentResponse(student.getId(), student.getName(), student.getEmail());
    }
}