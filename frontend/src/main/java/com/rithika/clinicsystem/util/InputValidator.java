package com.rithika.clinicsystem.util;

import java.util.regex.Pattern;

public final class InputValidator {

    /*
     * ----------------------------------------------------
     * CARENEXUS ID PATTERNS
     * ----------------------------------------------------
     */

    private static final Pattern PATIENT_ID_PATTERN =
            Pattern.compile("^P\\d{3}$");

    private static final Pattern DOCTOR_ID_PATTERN =
            Pattern.compile("^D\\d{3}$");

    private static final Pattern APPOINTMENT_ID_PATTERN =
            Pattern.compile("^A\\d{3}$");

    private static final Pattern MEDICAL_RECORD_ID_PATTERN =
            Pattern.compile("^MR\\d{3}$");

    private static final Pattern POLICY_ID_PATTERN =
            Pattern.compile("^POL\\d{3}$");

    private static final Pattern CLAIM_ID_PATTERN =
            Pattern.compile("^CL\\d{3}$");


    /*
     * Utility class.
     *
     * We do not create InputValidator objects because
     * every validation method is static.
     */
    private InputValidator() {
    }


    /*
     * ----------------------------------------------------
     * GENERAL VALIDATION
     * ----------------------------------------------------
     */

    // Check if string is empty or null
    public static boolean isEmpty(String input) {

        return input == null
                || input.trim().isEmpty();
    }


    // Check if valid integer
    public static boolean isValidInteger(String input) {

        if (input == null) {
            return false;
        }

        try {

            Integer.parseInt(input);

            return true;

        } catch (NumberFormatException exception) {

            return false;
        }
    }


    // Check if valid double
    public static boolean isValidDouble(String input) {

        if (input == null) {
            return false;
        }

        try {

            Double.parseDouble(input);

            return true;

        } catch (NumberFormatException exception) {

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

        return phone != null
                && phone.matches("\\d{10}");
    }


    // Check simple date format (yyyy-mm-dd)
    public static boolean isValidDate(String date) {

        return date != null
                && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }


    /*
     * ----------------------------------------------------
     * CARENEXUS ENTITY ID VALIDATION
     * ----------------------------------------------------
     */

    // Patient ID format: P###
    // Example: P001
    public static boolean isValidPatientId(String patientId) {

        return matchesPattern(
                patientId,
                PATIENT_ID_PATTERN
        );
    }


    // Doctor ID format: D###
    // Example: D001
    public static boolean isValidDoctorId(String doctorId) {

        return matchesPattern(
                doctorId,
                DOCTOR_ID_PATTERN
        );
    }


    // Appointment ID format: A###
    // Example: A001
    public static boolean isValidAppointmentId(String appointmentId) {

        return matchesPattern(
                appointmentId,
                APPOINTMENT_ID_PATTERN
        );
    }


    // Medical Record ID format: MR###
    // Example: MR001
    public static boolean isValidMedicalRecordId(String medicalRecordId) {

        return matchesPattern(
                medicalRecordId,
                MEDICAL_RECORD_ID_PATTERN
        );
    }


    // Insurance Policy ID format: POL###
    // Example: POL001
    public static boolean isValidPolicyId(String policyId) {

        return matchesPattern(
                policyId,
                POLICY_ID_PATTERN
        );
    }


    // Insurance Claim ID format: CL###
    // Example: CL001
    public static boolean isValidClaimId(String claimId) {

        return matchesPattern(
                claimId,
                CLAIM_ID_PATTERN
        );
    }


    /*
     * ----------------------------------------------------
     * PRIVATE HELPER
     * ----------------------------------------------------
     */

    private static boolean matchesPattern(
            String input,
            Pattern pattern
    ) {

        return input != null
                && pattern
                .matcher(input)
                .matches();
    }
}