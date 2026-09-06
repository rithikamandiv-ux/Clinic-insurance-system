package com.rithika.clinicinsurance.model;

import com.rithika.clinicinsurance.enums.ClaimStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "insurance_claims")
public class InsuranceClaim {

    @Id
    @Column(name = "claim_id")
    private String claimId;

    @OneToOne(optional = false)
    @JoinColumn(
            name = "medical_record_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_insurance_claim_medical_record")
    )
    private MedicalRecord medicalRecord;

    @Column(
            name = "claim_amount",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal claimAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ClaimStatus status;

    public InsuranceClaim() {
    }

    public InsuranceClaim(
            String claimId,
            MedicalRecord medicalRecord,
            BigDecimal claimAmount,
            ClaimStatus status
    ) {
        this.claimId = claimId;
        this.medicalRecord = medicalRecord;
        this.claimAmount = claimAmount;
        this.status = status;
    }

    public String getClaimId() {
        return claimId;
    }

    public void setClaimId(String claimId) {
        this.claimId = claimId;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }
}