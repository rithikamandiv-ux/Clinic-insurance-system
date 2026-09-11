package com.rithika.clinicinsurance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class MedicalRecordUpdateRequest {

    @NotBlank(message = "Diagnosis is required")
    @Size(max = 500, message = "Diagnosis cannot exceed 500 characters")
    private String diagnosis;

    @NotBlank(message = "Treatment is required")
    @Size(max = 1000, message = "Treatment cannot exceed 1000 characters")
    private String treatment;

    @NotNull(message = "Treatment cost is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Treatment cost cannot be negative"
    )
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Treatment cost must have at most 10 integer digits and 2 decimal places"
    )
    private BigDecimal treatmentCost;

    public MedicalRecordUpdateRequest() {
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public BigDecimal getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(BigDecimal treatmentCost) {
        this.treatmentCost = treatmentCost;
    }
}
