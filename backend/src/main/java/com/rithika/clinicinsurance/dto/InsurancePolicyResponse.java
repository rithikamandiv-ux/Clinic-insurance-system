package com.rithika.clinicinsurance.dto;

import com.rithika.clinicinsurance.model.InsurancePolicy;

import java.math.BigDecimal;

public class InsurancePolicyResponse {

    private String policyId;
    private String patientId;
    private String providerName;
    private BigDecimal coverageAmount;
    private String policyType;

    public InsurancePolicyResponse(
            String policyId,
            String patientId,
            String providerName,
            BigDecimal coverageAmount,
            String policyType
    ) {
        this.policyId = policyId;
        this.patientId = patientId;
        this.providerName = providerName;
        this.coverageAmount = coverageAmount;
        this.policyType = policyType;
    }

    public static InsurancePolicyResponse from(
            InsurancePolicy insurancePolicy
    ) {
        return new InsurancePolicyResponse(
                insurancePolicy.getPolicyId(),
                insurancePolicy.getPatient().getPatientId(),
                insurancePolicy.getProviderName(),
                insurancePolicy.getCoverageAmount(),
                insurancePolicy.getPolicyType()
        );
    }

    public String getPolicyId() {
        return policyId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getProviderName() {
        return providerName;
    }

    public BigDecimal getCoverageAmount() {
        return coverageAmount;
    }

    public String getPolicyType() {
        return policyType;
    }
}