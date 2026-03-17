package com.example.studentapp.util;

public class ValidationUtils {

    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String NAME_REGEX = "^[A-Za-z ]+$";
    public static final int MIN_PASSWORD_LENGTH=6;

    public static boolean isValidName(String name) {
        return name != null && name.trim().matches("[A-Za-z ]+$");
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.trim().matches(EMAIL_REGEX);
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

}