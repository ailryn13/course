package com.example.studentapp.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilsTest {

    @Test
    public void isValidEmail_ValidEmail_ReturnsTrue() {
        String email = "test@example.com";

        boolean result = ValidationUtils.isValidEmail(email);

        assertTrue(result, "A standard email format should return true");
    }

    @Test
    public void isValidEmail_InvalidEmail_ReturnsFalse() {
        String email = "test@.com";

        boolean result = ValidationUtils.isValidEmail(email);

        assertFalse(result, "An email missing a proper domain should return false");
    }

    @Test
    public void isValidEmail_NullEmail_ReturnsFalse() {
        boolean result = ValidationUtils.isValidEmail(null);
        assertFalse(result, "A null email should safely return false without crashing");
    }

    @Test
    public void isValidPassword_ValidPassword_ReturnsTrue() {
        boolean result = ValidationUtils.isValidPassword("Secure123");
        assertTrue(result);
    }

    @Test
    public void isValidPassword_ShortPassword_ReturnsFalse() {
        boolean result = ValidationUtils.isValidPassword("12345");
        assertFalse(result, "A password with less than 6 characters should return false");
    }

    @Test
    public void isValidPassword_NullPassword_ReturnsFalse() {
        boolean result = ValidationUtils.isValidPassword(null);
        assertFalse(result, "A null password should safely return false");
    }

    @Test
    public void isValidName_ValidName_ReturnsTrue() {
        boolean result = ValidationUtils.isValidName("John Doe");
        assertTrue(result);
    }

    @Test
    public void isValidName_InvalidNameWithNumbers_ReturnsFalse() {
        boolean result = ValidationUtils.isValidName("John123");
        assertFalse(result, "A name containing numbers should return false");
    }
}