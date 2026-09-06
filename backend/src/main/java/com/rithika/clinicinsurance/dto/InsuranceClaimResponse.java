package com.rithika.clinicinsurance.dto;

import com.rithika.clinicinsurance.enums.ClaimStatus;
import com.rithika.clinicinsurance.model.InsuranceClaim;

import java.math.BigDecimal;

public class InsuranceClaimResponse {

    private String claimId;
    private String medicalRecordId;
    private String patientId;
    private BigDecimal claimAmount;
    private ClaimStatus status;

    public InsuranceClaimResponse(
            String claimId,
            String medicalRecordId,
            String patientId,
            BigDecimal claimAmount,
            ClaimStatus status
    ) {
        this.claimId = claimId;
        this.medicalRecordId = medicalRecordId;
        this.patientId = patientId;
        this.claimAmount = claimAmount;
        this.status = status;
    }

    public static InsuranceClaimResponse from(
            InsuranceClaim insuranceClaim
    ) {
        return new InsuranceClaimResponse(
                insuranceClaim.getClaimId(),
                insuranceClaim
                        .getMedicalRecord()
                        .getRecordId(),
                insuranceClaim
                        .getMedicalRecord()
                        .getAppointment()
                        .getPatient()
                        .getPatientId(),
                insuranceClaim.getClaimAmount(),
                insuranceClaim.getStatus()
        );
    }

    public String getClaimId() {
        return claimId;
    }

    public String getMedicalRecordId() {
        return medicalRecordId;
    }

    public String getPatientId() {
        return patientId;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public ClaimStatus getStatus() {
        return status;
    }
}