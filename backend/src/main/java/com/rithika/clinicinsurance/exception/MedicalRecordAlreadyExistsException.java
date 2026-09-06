package com.rithika.clinicinsurance.exception;

public class MedicalRecordAlreadyExistsException extends RuntimeException {

    public MedicalRecordAlreadyExistsException(String recordId) {
        super("Medical record with ID " + recordId + " already exists");
    }
}