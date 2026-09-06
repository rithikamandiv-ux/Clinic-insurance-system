package com.rithika.clinicsystem.dto;

import java.math.BigDecimal;

public class InsuranceClaimResponse {

    private String claimId;
    private String medicalRecordId;
    private String patientId;
    private BigDecimal claimAmount;
    private String status;

    public InsuranceClaimResponse() {
    }

    public InsuranceClaimResponse(
            String claimId,
            String medicalRecordId,
            String patientId,
            BigDecimal claimAmount,
            String status
    ) {
        this.claimId = claimId;
        this.medicalRecordId = medicalRecordId;
        this.patientId = patientId;
        this.claimAmount = claimAmount;
        this.status = status;
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

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(
            BigDecimal claimAmount
    ) {
        this.claimAmount = claimAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}