package com.rithika.clinicsystem.dto;

import java.math.BigDecimal;

public class InsurancePolicyRequest {

    private String policyId;
    private String patientId;
    private String providerName;
    private BigDecimal coverageAmount;
    private String policyType;

    public InsurancePolicyRequest() {
    }

    public InsurancePolicyRequest(
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

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public BigDecimal getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(
            BigDecimal coverageAmount
    ) {
        this.coverageAmount = coverageAmount;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }
}