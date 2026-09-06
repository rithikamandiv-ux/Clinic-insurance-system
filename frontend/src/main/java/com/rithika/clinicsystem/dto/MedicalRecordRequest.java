package com.rithika.clinicsystem.dto;

import java.math.BigDecimal;

public class MedicalRecordRequest {

    private String recordId;
    private String appointmentId;
    private String diagnosis;
    private String treatment;
    private BigDecimal treatmentCost;

    public MedicalRecordRequest() {
    }

    public MedicalRecordRequest(
            String recordId,
            String appointmentId,
            String diagnosis,
            String treatment,
            BigDecimal treatmentCost
    ) {
        this.recordId = recordId;
        this.appointmentId = appointmentId;
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

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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