package com.rithika.clinicinsurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public class InsuranceClaimRequest {

    @NotBlank(message = "Claim ID is required")
    @Size(max = 20, message = "Claim ID cannot exceed 20 characters")
    @Pattern(
            regexp = "^CL\\d{3}$",
            message = "Claim ID must follow the format CL###, for example CL001."
    )
    private String claimId;


    @NotBlank(message = "Medical record ID is required")
    @Pattern(
            regexp = "^MR\\d{3}$",
            message = "Medical Record ID must follow the format MR###, for example MR001."
    )
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