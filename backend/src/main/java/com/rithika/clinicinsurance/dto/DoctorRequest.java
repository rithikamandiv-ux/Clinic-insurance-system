package com.rithika.clinicinsurance.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public class DoctorRequest {

    @NotBlank(message = "Doctor ID is required")
    @Size(max = 20, message = "Doctor ID cannot exceed 20 characters")
    @Pattern(
            regexp = "^D\\d{3}$",
            message = "Doctor ID must follow the format D###, for example D001."
    )
    private String doctorId;

    @NotBlank(message = "Doctor name is required")
    @Size(max = 100, message = "Doctor name cannot exceed 100 characters")
    private String doctorName;

    @NotBlank(message = "Specialization is required")
    @Size(max = 100, message = "Specialization cannot exceed 100 characters")
    private String specialization;

    @NotNull(message = "Consultation fee is required")
    @DecimalMin(
            value = "0.00",
            inclusive = true,
            message = "Consultation fee cannot be negative"
    )
    @Digits(
            integer = 8,
            fraction = 2,
            message = "Consultation fee must have at most 8 integer digits and 2 decimal places"
    )
    private BigDecimal consultationFee;

    public DoctorRequest() {
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