package com.rithika.clinicinsurance.exception;

public class MedicalRecordAlreadyHasClaimException extends RuntimeException {

    public MedicalRecordAlreadyHasClaimException(String recordId) {
        super(
                "Medical record with ID " + recordId
                        + " already has an insurance claim"
        );
    }
}