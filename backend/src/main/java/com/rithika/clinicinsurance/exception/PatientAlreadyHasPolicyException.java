package com.rithika.clinicinsurance.exception;

public class PatientAlreadyHasPolicyException extends RuntimeException {

    public PatientAlreadyHasPolicyException(String patientId) {
        super("Patient with ID " + patientId + " already has an insurance policy");
    }
}