package com.rithika.clinicinsurance.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PatientRequest {

    @NotBlank(message = "Patient ID is required")
    @Size(max = 20, message = "Patient ID cannot exceed 20 characters")
    private String patientId;

    @NotBlank(message = "Patient name is required")
    @Size(max = 100, message = "Patient name cannot exceed 100 characters")
    private String patientName;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 130, message = "Age cannot exceed 130")
    private Integer age;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "\\d{10}",
            message = "Phone number must contain exactly 10 digits"
    )
    private String phoneNumber;

    @NotNull(message = "Insurance status is required")
    private Boolean insuranceStatus;

    public PatientRequest() {
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