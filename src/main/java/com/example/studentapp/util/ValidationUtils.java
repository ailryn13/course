package com.example.studentapp.util;

public class ValidationUtils {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

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