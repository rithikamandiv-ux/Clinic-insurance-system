package com.rithika.clinicsystem.dto;

public class PatientRequest {

    private String patientId;
    private String patientName;
    private Integer age;
    private String phoneNumber;
    private Boolean insuranceStatus;

    public PatientRequest() {
    }

    public PatientRequest(
            String patientId,
            String patientName,
            Integer age,
            String phoneNumber,
            Boolean insuranceStatus
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getInsuranceStatus() {
        return insuranceStatus;
    }

    public void setInsuranceStatus(Boolean insuranceStatus) {
        this.insuranceStatus = insuranceStatus;
    }
}