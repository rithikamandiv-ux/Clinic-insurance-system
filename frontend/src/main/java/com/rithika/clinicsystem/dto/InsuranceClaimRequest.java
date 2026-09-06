package com.rithika.clinicsystem.dto;

public class InsuranceClaimRequest {

    private String claimId;
    private String medicalRecordId;

    public InsuranceClaimRequest() {
    }

    public InsuranceClaimRequest(
            String claimId,
            String medicalRecordId
    ) {
        this.claimId = claimId;
        this.medicalRecordId = medicalRecordId;
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