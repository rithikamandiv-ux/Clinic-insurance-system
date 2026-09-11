package com.rithika.clinicsystem.dto;

import java.math.BigDecimal;

public class DoctorUpdateRequest {

    private String doctorName;
    private String specialization;
    private BigDecimal consultationFee;

    public DoctorUpdateRequest() {
    }

    public DoctorUpdateRequest(
            String doctorName,
            String specialization,
            BigDecimal consultationFee
    ) {
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.consultationFee = consultationFee;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }
}
