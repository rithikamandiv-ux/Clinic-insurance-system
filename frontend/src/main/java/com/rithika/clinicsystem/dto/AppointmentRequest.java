package com.rithika.clinicsystem.dto;

import java.time.LocalDate;

public class AppointmentRequest {

    private String appointmentId;
    private String patientId;
    private String doctorId;
    private LocalDate appointmentDate;

    public AppointmentRequest() {
    }

    public AppointmentRequest(
            String appointmentId,
            String patientId,
            String doctorId,
            LocalDate appointmentDate
    ) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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