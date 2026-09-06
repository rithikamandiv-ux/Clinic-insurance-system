package com.rithika.clinicsystem.dto;

import java.math.BigDecimal;

public class DoctorResponse {

    private String doctorId;
    private String doctorName;
    private String specialization;
    private BigDecimal consultationFee;

    public DoctorResponse() {
    }

    public DoctorResponse(
            String doctorId,
            String doctorName,
            String specialization,
            BigDecimal consultationFee
    ) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.consultationFee = consultationFee;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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