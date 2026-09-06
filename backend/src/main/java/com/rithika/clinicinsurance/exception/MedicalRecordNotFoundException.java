package com.rithika.clinicinsurance.exception;

public class MedicalRecordNotFoundException extends RuntimeException {

    public MedicalRecordNotFoundException(String recordId) {
        super("Medical record with ID " + recordId + " was not found");
    }
}