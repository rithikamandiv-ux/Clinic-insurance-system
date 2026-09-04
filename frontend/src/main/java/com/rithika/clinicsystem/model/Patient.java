package com.rithika.clinicsystem.model;

public class Patient {
    private String patientId;
    private String patientName;
    private int age;
    private String phoneNumber;
    private boolean insuranceStatus;

    public Patient(String patientId, String patientName, int age, String phoneNumber, boolean insuranceStatus){
        this.patientId = patientId;
        this.patientName = patientName;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.insuranceStatus = insuranceStatus;
    }

    public String getPatientId(){
        return patientId;
    }

    public String getPatientName(){
        return patientName;
    }

    public int getAge(){
        return age;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public boolean InsuranceStatus(){
        return insuranceStatus;
    }

    public void setPatientId(String patientId){
        this.patientId = patientId;
    }

    public void setPatientName(String patientName){
        this.patientName = patientName;
    }

    public void setAge(int age){
        this.age = age;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setInsuranceStatus(boolean insuranceStatus){
        this.insuranceStatus = insuranceStatus;
    }

    public void displayPatient(){
        System.out.println("patient ID : " + patientId);
        System.out.println("patient name : " + patientName);
        System.out.println("patient age : " + patientId);
        System.out.println("patient phone number : " + phoneNumber);
        System.out.println("Insurance status : " + insuranceStatus);
    }
}
