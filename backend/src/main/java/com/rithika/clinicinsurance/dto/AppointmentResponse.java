package com.rithika.clinicinsurance.dto;

import com.rithika.clinicinsurance.enums.AppointmentStatus;
import com.rithika.clinicinsurance.model.Appointment;

import java.time.LocalDate;

public class AppointmentResponse {

    private String appointmentId;
    private LocalDate appointmentDate;
    private AppointmentStatus status;
    private String patientId;
    private String doctorId;

    public AppointmentResponse(
            String appointmentId,
            LocalDate appointmentDate,
            AppointmentStatus status,
            String patientId,
            String doctorId
    ) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getAppointmentId(),
                appointment.getAppointmentDate(),
                appointment.getStatus(),
                appointment.getPatient().getPatientId(),
                appointment.getDoctor().getDoctorId()
        );
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }
}