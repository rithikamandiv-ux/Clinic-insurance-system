package com.rithika.clinicinsurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class InsuranceClaimRequest {

    @NotBlank(message = "Claim ID is required")
    @Size(max = 20, message = "Claim ID cannot exceed 20 characters")
    private String claimId;

    @NotBlank(message = "Medical record ID is required")
    private String medicalRecordId;

    public InsuranceClaimRequest() {
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(String medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }
}