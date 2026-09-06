package com.rithika.clinicinsurance.exception;

public class InsuranceClaimNotFoundException extends RuntimeException {

    public InsuranceClaimNotFoundException(String claimId) {
        super("Insurance claim with ID " + claimId + " was not found");
    }
}