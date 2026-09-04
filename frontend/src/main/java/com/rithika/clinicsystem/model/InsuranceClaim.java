package com.rithika.clinicsystem.model;

public class InsuranceClaim {

    public static final String status_pending = "pending";
    public static final String status_approved = "approved";
    public static final String status_rejected = "rejected";

    private String claimId;
    private String patientId;
    private String recordId;
    private double claimAmount;
    private String claimStatus;

    public InsuranceClaim(String claimId, String patientId, String recordId, double claimAmount, String claimStatus){
        this.claimId = claimId;
        this.patientId = patientId;
        this.recordId = recordId;
        this.claimAmount = claimAmount;
        this.claimStatus = claimStatus;
    }

    public String getClaimId() {
        return claimId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getRecordId() {
        return recordId;
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus){
        this.claimStatus = claimStatus;
    }

    public void displayClaim(){
        System.out.println("Claim ID: " + claimId);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Record ID: " + recordId);
        System.out.println("Claim Amount: " + claimAmount);
        System.out.println("Claim Status: " + claimStatus);
    }
}
