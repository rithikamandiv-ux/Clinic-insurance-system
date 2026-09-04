package com.rithika.clinicinsurance.model;

public class Patient {

    private String patientId;
    private String patientName;
    private int age;
    private String phoneNumber;
    private boolean insuranceStatus;

    public Patient() {
    }

    public Patient(
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

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isInsuranceStatus() {
        return insuranceStatus;
    }

    public void setInsuranceStatus(boolean insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
    }
}