package com.rithika.clinicsystem.dto;

import java.time.LocalDate;

public class AppointmentResponse {

    private String appointmentId;
    private LocalDate appointmentDate;
    private String status;
    private String patientId;
    private String doctorId;

    public AppointmentResponse() {
    }

    public AppointmentResponse(
            String appointmentId,
            LocalDate appointmentDate,
            String status,
            String patientId,
            String doctorId
    ) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}