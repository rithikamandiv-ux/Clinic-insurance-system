package com.rithika.clinicsystem.model;

public class InsurancePolicy {
    private String policyId;
    private String patientId;
    private String providerName;
    private double coverageAmount;
    private String policyType;


    public InsurancePolicy(String policyId, String patientId, String providerName,
                           double coverageAmount, String policyType){
        this.policyId = policyId;
        this.patientId = patientId;
        this.providerName = providerName;
        this.coverageAmount = coverageAmount;
        this.policyType = policyType;
    }

    public String getPolicyId() {
        return policyId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getProviderName(){
        return providerName;
    }

    public Double getCoverageAmount() {
        return coverageAmount;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setProviderName( String providerName){
        this.providerName = providerName;
    }

    public void setCoverageAmount(double coverageAmount){
        this.coverageAmount = coverageAmount;
    }

    public void setPolicyType( String policyType){
        this.policyType = policyType;
    }

    public void displayPolicy(){
        System.out.println("Policy ID: " + policyId);
        System.out.println("Patient ID: " + patientId);
        System.out.println("Provider Name: " + providerName);
        System.out.println("Coverage Amount: " + coverageAmount);
        System.out.println("Policy Type: " + policyType);
        }
}

