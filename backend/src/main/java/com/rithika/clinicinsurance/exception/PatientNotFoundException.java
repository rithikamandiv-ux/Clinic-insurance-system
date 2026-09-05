package com.rithika.clinicinsurance.exception;

public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(String patientId) {
        super("Patient with ID " + patientId + " was not found");
    }
}