package com.rithika.clinicsystem.dto;

public class PatientUpdateRequest {

    private String patientName;
    private Integer age;
    private String phoneNumber;
    private Boolean insuranceStatus;

    public PatientUpdateRequest() {
    }

    public PatientUpdateRequest(
            String patientName,
            Integer age,
            String phoneNumber,
            Boolean insuranceStatus
    ) {
        this.patientName = patientName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.insuranceStatus = insuranceStatus;
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
