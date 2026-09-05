package com.rithika.clinicinsurance.exception;

public class PatientAlreadyExistsException extends RuntimeException {

    public PatientAlreadyExistsException(String patientId) {
        super("Patient with ID " + patientId + " already exists");
    }
}
