package com.rithika.clinicinsurance.dto;

import com.rithika.clinicinsurance.model.MedicalRecord;

import java.math.BigDecimal;

public class MedicalRecordResponse {

    private String recordId;
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String diagnosis;
    private String treatment;
    private BigDecimal treatmentCost;

    public MedicalRecordResponse(
            String recordId,
            String appointmentId,
            String patientId,
            String doctorId,
            String diagnosis,
            String treatment,
            BigDecimal treatmentCost
    ) {
        this.recordId = recordId;
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.treatmentCost = treatmentCost;
    }

    public static MedicalRecordResponse from(MedicalRecord medicalRecord) {

        return new MedicalRecordResponse(
                medicalRecord.getRecordId(),
                medicalRecord.getAppointment().getAppointmentId(),
                medicalRecord.getAppointment().getPatient().getPatientId(),
                medicalRecord.getAppointment().getDoctor().getDoctorId(),
                medicalRecord.getDiagnosis(),
                medicalRecord.getTreatment(),
                medicalRecord.getTreatmentCost()
        );
    }

    public String getRecordId() {
        return recordId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public BigDecimal getTreatmentCost() {
        return treatmentCost;
    }
}