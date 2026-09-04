package com.rithika.clinicsystem.service;

import com.rithika.clinicsystem.model.InsuranceClaim;
import com.rithika.clinicsystem.model.InsurancePolicy;
import com.rithika.clinicsystem.model.MedicalRecord;

import java.util.ArrayList;
import java.util.List;

public class InsuranceService {

    private List <InsurancePolicy> policies;
    private List <InsuranceClaim> claims;

    public InsuranceService(){
        policies = new ArrayList<>();
        claims = new ArrayList<>();
    }

    public void addPolicy(InsurancePolicy policy){
        policies.add(policy);
    }

    public void addClaim(InsuranceClaim claim){
        claims.add(claim);
    }

    public InsurancePolicy findPolicyById(String policyId){
        for (InsurancePolicy insurancePolicy : policies){
            if (insurancePolicy.getPolicyId().equals(policyId)){
                return insurancePolicy;
            }
        }
        return null;
    }

    public InsurancePolicy findPolicyByPatientId(String patientId){
        for (InsurancePolicy insurancePolicy : policies){
            if (insurancePolicy.getPatientId().equals(patientId)){
                return insurancePolicy;
            }
        }
        return null;
    }

    public InsuranceClaim findClaimById(String claimId){
        for (InsuranceClaim insuranceClaim : claims){
            if (insuranceClaim.getClaimId().equals(claimId)){
                return insuranceClaim;
            }
        }
        return null;
    }

    public List<InsuranceClaim> getClaims() {
        return claims;
    }

    public List<InsurancePolicy> getPolicies() {
        return policies;
    }

    // claims processing method
    public void processClaim(String claimId) {

        InsuranceClaim claim = findClaimById(claimId);

        if (claim == null) {
            System.out.println("Claim not found.");
            return;
        }

        InsurancePolicy policy = findPolicyByPatientId(claim.getPatientId());

        if (policy == null) {
            claim.setClaimStatus(InsuranceClaim.status_rejected);
            System.out.println("No policy found. Claim rejected.");
            return;
        }

        if (claim.getClaimAmount() <= policy.getCoverageAmount()) {
            claim.setClaimStatus(InsuranceClaim.status_approved);
            System.out.println("Claim approved.");
        } else {
            claim.setClaimStatus(InsuranceClaim.status_rejected);
            System.out.println("Claim rejected. Amount exceeds coverage.");
        }
    }

    //This is useful before creating claims
    public boolean hasPolicy(String patientId) {
        InsurancePolicy policy = findPolicyByPatientId(patientId);
        return policy != null;
    }

    //	read treatment cost from a MedicalRecord and create a new claim automatically
    public InsuranceClaim createClaimFromRecord(String claimId, MedicalRecord record) {

        if (record == null) {
            System.out.println("Medical record not found.");
            return null;
        }

        InsuranceClaim claim = new InsuranceClaim(
                claimId,
                record.getPatientId(),
                record.getRecordId(),
                record.getTreatmentCost(),
                InsuranceClaim.status_pending
        );

        claims.add(claim);

        return claim;
    }
}
