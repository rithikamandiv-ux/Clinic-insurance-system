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
@Table(name = "medical_records")
public class MedicalRecord {

    @Id
    @Column(name = "record_id")
    private String recordId;

    @OneToOne(optional = false)
    @JoinColumn(
            name = "appointment_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_medical_record_appointment")
    )
    private Appointment appointment;

    @Column(nullable = false, length = 500)
    private String diagnosis;

    @Column(nullable = false, length = 1000)
    private String treatment;

    @Column(
            name = "treatment_cost",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal treatmentCost;

    public MedicalRecord() {
    }

    public MedicalRecord(
            String recordId,
            Appointment appointment,
            String diagnosis,
            String treatment,
            BigDecimal treatmentCost
    ) {
        this.recordId = recordId;
        this.appointment = appointment;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.treatmentCost = treatmentCost;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
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