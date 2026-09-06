package com.rithika.clinicinsurance.exception;

public class InsurancePolicyAlreadyExistsException extends RuntimeException {

    public InsurancePolicyAlreadyExistsException(String policyId) {
        super("Insurance policy with ID " + policyId + " already exists");
    }
}