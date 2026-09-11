package com.rithika.clinicinsurance.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class AppointmentUpdateRequest {

    @NotBlank(message = "Patient ID is required")
    @Pattern(
            regexp = "^P\\d{3}$",
            message = "Patient ID must follow the format P###, for example P001."
    )
    private String patientId;

    @NotBlank(message = "Doctor ID is required")
    @Pattern(
            regexp = "^D\\d{3}$",
            message = "Doctor ID must follow the format D###, for example D001."
    )
    private String doctorId;

    @NotNull(message = "Appointment date is required")
    @FutureOrPresent(message = "Appointment date cannot be in the past")
    private LocalDate appointmentDate;

    public AppointmentUpdateRequest() {
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
