package com.rithika.clinicsystem.model;

public class MedicalRecord {
    private String recordId;
    private String patientId;
    private String doctorId;
    private String appointmentId;
    private String diagnosis;
    private String treatment;
    private double treatmentCost;

    public MedicalRecord(String recordId, String patientId, String doctorId,
                         double treatmentCost){
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.treatmentCost = treatmentCost;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getAppointmentId() {

        return appointmentId;
    }

    public String getDiagnosis() {

        return diagnosis;
    }

    public String getTreatment() {

        return treatment;
    }

    public double getTreatmentCost() {

        return treatmentCost;
    }

    public void setDiagnosis(String diagnosis) {

        this.diagnosis = diagnosis;
    }

    public void setTreatment(String treatment) {

        this.treatment = treatment;
    }

    public void setTreatmentCost(double treatmentCost) {

        this.treatmentCost = treatmentCost;
    }

    public void displayRecord() {
        System.out.println("Record ID: " + recordId);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Doctor ID: " + doctorId);
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Diagnosis: " + diagnosis);
        System.out.println("Treatment: " + treatment);
        System.out.println("Treatment Cost: " + treatmentCost);
    }
}
