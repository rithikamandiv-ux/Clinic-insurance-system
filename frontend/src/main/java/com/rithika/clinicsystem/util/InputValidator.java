package com.rithika.clinicsystem.util;

public class InputValidator {

    // Check if string is empty or null
    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    // Check if valid integer
    public static boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Check if valid double
    public static boolean isValidDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Check if positive integer
    public static boolean isPositiveInteger(String input) {
        if (!isValidInteger(input)) {
            return false;
        }
        return Integer.parseInt(input) > 0;
    }

    // Check if positive double
    public static boolean isPositiveDouble(String input) {
        if (!isValidDouble(input)) {
            return false;
        }
        return Double.parseDouble(input) > 0;
    }

    // Check if phone number is valid (10 digits)
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    // Optional: check simple date format (yyyy-mm-dd)
    public static boolean isValidDate(String date) {
        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}