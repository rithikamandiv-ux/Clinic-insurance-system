package com.rithika.clinicsystem.dto;

import java.time.LocalDate;

public class AppointmentUpdateRequest {

    private String patientId;
    private String doctorId;
    private LocalDate appointmentDate;

    public AppointmentUpdateRequest() {
    }

    public AppointmentUpdateRequest(
            String patientId,
            String doctorId,
            LocalDate appointmentDate
    ) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
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
