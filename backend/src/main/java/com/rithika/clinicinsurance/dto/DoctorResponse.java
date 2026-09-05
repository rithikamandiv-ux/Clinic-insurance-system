package com.rithika.clinicinsurance.dto;

import com.rithika.clinicinsurance.model.Doctor;

import java.math.BigDecimal;

public class DoctorResponse {

    private String doctorId;
    private String doctorName;
    private String specialization;
    private BigDecimal consultationFee;

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

    public static DoctorResponse from(Doctor doctor) {
        return new DoctorResponse(
                doctor.getDoctorId(),
                doctor.getDoctorName(),
                doctor.getSpecialization(),
                doctor.getConsultationFee()
        );
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }
}