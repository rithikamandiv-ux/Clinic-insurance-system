package com.rithika.clinicsystem.dto;

import java.math.BigDecimal;

public class InsurancePolicyUpdateRequest {

    private String providerName;
    private BigDecimal coverageAmount;
    private String policyType;

    public InsurancePolicyUpdateRequest() {
    }

    public InsurancePolicyUpdateRequest(
            String providerName,
            BigDecimal coverageAmount,
            String policyType
    ) {
        this.providerName = providerName;
        this.coverageAmount = coverageAmount;
        this.policyType = policyType;
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
