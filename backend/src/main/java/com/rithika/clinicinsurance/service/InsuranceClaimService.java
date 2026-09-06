package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.enums.ClaimStatus;
import com.rithika.clinicinsurance.exception.InsuranceClaimAlreadyExistsException;
import com.rithika.clinicinsurance.exception.InsuranceClaimNotFoundException;
import com.rithika.clinicinsurance.exception.InvalidInsuranceClaimStatusTransitionException;
import com.rithika.clinicinsurance.exception.MedicalRecordAlreadyHasClaimException;
import com.rithika.clinicinsurance.exception.MedicalRecordNotFoundException;
import com.rithika.clinicinsurance.model.InsuranceClaim;
import com.rithika.clinicinsurance.model.InsurancePolicy;
import com.rithika.clinicinsurance.model.MedicalRecord;
import com.rithika.clinicinsurance.repository.InsuranceClaimRepository;
import com.rithika.clinicinsurance.repository.InsurancePolicyRepository;
import com.rithika.clinicinsurance.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InsuranceClaimService {

    private final InsuranceClaimRepository insuranceClaimRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;

    public InsuranceClaimService(
            InsuranceClaimRepository insuranceClaimRepository,
            MedicalRecordRepository medicalRecordRepository,
            InsurancePolicyRepository insurancePolicyRepository
    ) {
        this.insuranceClaimRepository = insuranceClaimRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.insurancePolicyRepository = insurancePolicyRepository;
    }

    public List<InsuranceClaim> getAllClaims() {
        return insuranceClaimRepository.findAll();
    }

    public InsuranceClaim getClaimById(String claimId) {
        return insuranceClaimRepository.findById(claimId)
                .orElseThrow(
                        () -> new InsuranceClaimNotFoundException(claimId)
                );
    }

    public InsuranceClaim createClaim(
            String claimId,
            String medicalRecordId
    ) {

        if (insuranceClaimRepository.existsById(claimId)) {
            throw new InsuranceClaimAlreadyExistsException(claimId);
        }

        if (insuranceClaimRepository
                .existsByMedicalRecord_RecordId(medicalRecordId)) {

            throw new MedicalRecordAlreadyHasClaimException(
                    medicalRecordId
            );
        }

        MedicalRecord medicalRecord = medicalRecordRepository
                .findById(medicalRecordId)
                .orElseThrow(
                        () -> new MedicalRecordNotFoundException(
                                medicalRecordId
                        )
                );

        InsuranceClaim claim = new InsuranceClaim(
                claimId,
                medicalRecord,
                medicalRecord.getTreatmentCost(),
                ClaimStatus.PENDING
        );

        return insuranceClaimRepository.save(claim);
    }

    public InsuranceClaim processClaim(String claimId) {

        InsuranceClaim claim = getClaimById(claimId);

        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new InvalidInsuranceClaimStatusTransitionException(
                    claimId,
                    claim.getStatus()
            );
        }

        String patientId = claim
                .getMedicalRecord()
                .getAppointment()
                .getPatient()
                .getPatientId();

        Optional<InsurancePolicy> policyOptional =
                insurancePolicyRepository
                        .findByPatient_PatientId(patientId);

        if (policyOptional.isEmpty()) {

            claim.setStatus(ClaimStatus.REJECTED);

            return insuranceClaimRepository.save(claim);
        }

        InsurancePolicy policy = policyOptional.get();

        if (claim.getClaimAmount()
                .compareTo(policy.getCoverageAmount()) <= 0) {

            claim.setStatus(ClaimStatus.APPROVED);

        } else {

            claim.setStatus(ClaimStatus.REJECTED);
        }

        return insuranceClaimRepository.save(claim);
    }
}