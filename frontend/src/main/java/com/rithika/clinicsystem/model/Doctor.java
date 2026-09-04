package com.rithika.clinicsystem.model;

public class Doctor {
    private String doctorId;
    private String doctorName;
    private String specialization;
    private double consultationFee;

    public Doctor(String doctorId, String doctorName, String specialization, double consultationFee){
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.consultationFee = consultationFee;
    }

    public String getDoctorId(){
        return doctorId;
    }

    public String getDoctorName(){
        return doctorName;
    }

    public String getSpecialization(){
        return specialization;
    }

    public double getConsultationFee(){
        return consultationFee;
    }

    public void setDoctorId(String doctorId){
        this.doctorId = doctorId;
    }

    public void setDoctorName(String doctorName){
        this.doctorName = doctorName;
    }

    public void setSpecialization(String specialization){
        this.specialization = specialization;
    }

    public void setConsultationFee(int consultationFee){
        this.consultationFee = consultationFee;
    }

    public void displayDoctor(){
        System.out.println("Doctor ID : " + doctorId);
        System.out.println("Doctor name : " + doctorName);
        System.out.println("Specialization : " + specialization);
        System.out.println("Consultation fee : " + consultationFee);
    }
}

