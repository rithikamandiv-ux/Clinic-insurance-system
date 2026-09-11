package com.rithika.clinicinsurance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class InsurancePolicyUpdateRequest {

    @NotBlank(message = "Provider name is required")
    @Size(max = 100, message = "Provider name cannot exceed 100 characters")
    private String providerName;

    @NotNull(message = "Coverage amount is required")
    @DecimalMin(
            value = "0.01",
            inclusive = true,
            message = "Coverage amount must be greater than zero"
    )
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Coverage amount must have at most 10 integer digits and 2 decimal places"
    )
    private BigDecimal coverageAmount;

    @NotBlank(message = "Policy type is required")
    @Size(max = 100, message = "Policy type cannot exceed 100 characters")
    private String policyType;

    public InsurancePolicyUpdateRequest() {
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

    public void setCoverageAmount(BigDecimal coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }
}
