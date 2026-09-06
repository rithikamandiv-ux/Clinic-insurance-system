package com.rithika.clinicinsurance.model;

import com.rithika.clinicinsurance.enums.AppointmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @Column(name = "appointment_id")
    private String appointmentId;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "patient_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_appointment_patient")
    )
    private Patient patient;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "doctor_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_appointment_doctor")
    )
    private Doctor doctor;

    public Appointment() {
    }

    public Appointment(
            String appointmentId,
            LocalDate appointmentDate,
            AppointmentStatus status,
            Patient patient,
            Doctor doctor
    ) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.patient = patient;
        this.doctor = doctor;
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

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}