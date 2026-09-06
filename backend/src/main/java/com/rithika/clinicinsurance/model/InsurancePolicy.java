package com.rithika.clinicinsurance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "insurance_policies")
public class InsurancePolicy {

    @Id
    @Column(name = "policy_id")
    private String policyId;

    @OneToOne(optional = false)
    @JoinColumn(
            name = "patient_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_insurance_policy_patient")
    )
    private Patient patient;

    @Column(name = "provider_name", nullable = false, length = 100)
    private String providerName;

    @Column(
            name = "coverage_amount",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal coverageAmount;

    @Column(name = "policy_type", nullable = false, length = 100)
    private String policyType;

    public InsurancePolicy() {
    }

    public InsurancePolicy(
            String policyId,
            Patient patient,
            String providerName,
            BigDecimal coverageAmount,
            String policyType
    ) {
        this.policyId = policyId;
        this.patient = patient;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
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