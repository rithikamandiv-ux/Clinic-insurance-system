package com.rithika.clinicsystem.dto;

import java.math.BigDecimal;

public class MedicalRecordUpdateRequest {

    private String diagnosis;
    private String treatment;
    private BigDecimal treatmentCost;

    public MedicalRecordUpdateRequest() {
    }

    public MedicalRecordUpdateRequest(
            String diagnosis,
            String treatment,
            BigDecimal treatmentCost
    ) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.treatmentCost = treatmentCost;
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
