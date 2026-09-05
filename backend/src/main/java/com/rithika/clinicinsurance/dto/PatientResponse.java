package com.rithika.clinicinsurance.dto;

import com.rithika.clinicinsurance.model.Patient;

public class PatientResponse {

    private String patientId;
    private String patientName;
    private int age;
    private String phoneNumber;
    private boolean insuranceStatus;

    public PatientResponse(
            String patientId,
            String patientName,
            int age,
            String phoneNumber,
            boolean insuranceStatus
    ) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.insuranceStatus = insuranceStatus;
    }

    public static PatientResponse from(Patient patient) {
        return new PatientResponse(
                patient.getPatientId(),
                patient.getPatientName(),
                patient.getAge(),
                patient.getPhoneNumber(),
                patient.isInsuranceStatus()
        );
    }

    public String getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public int getAge() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isInsuranceStatus() {
        return insuranceStatus;
    }
}