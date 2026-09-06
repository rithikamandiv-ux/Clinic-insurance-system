package com.rithika.clinicinsurance.exception;

public class InsurancePolicyNotFoundException extends RuntimeException {

    public InsurancePolicyNotFoundException(String policyId) {
        super("Insurance policy with ID " + policyId + " was not found");
    }
}