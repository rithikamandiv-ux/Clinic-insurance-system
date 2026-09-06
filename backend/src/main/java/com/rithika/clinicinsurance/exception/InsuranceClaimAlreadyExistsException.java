package com.rithika.clinicinsurance.exception;

public class InsuranceClaimAlreadyExistsException extends RuntimeException {

    public InsuranceClaimAlreadyExistsException(String claimId) {
        super("Insurance claim with ID " + claimId + " already exists");
    }
}