package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.exception.InsurancePolicyAlreadyExistsException;
import com.rithika.clinicinsurance.exception.InsurancePolicyNotFoundException;
import com.rithika.clinicinsurance.exception.PatientAlreadyHasPolicyException;
import com.rithika.clinicinsurance.exception.PatientNotFoundException;
import com.rithika.clinicinsurance.model.InsurancePolicy;
import com.rithika.clinicinsurance.model.Patient;
import com.rithika.clinicinsurance.repository.InsurancePolicyRepository;
import com.rithika.clinicinsurance.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InsurancePolicyService {

    private final InsurancePolicyRepository insurancePolicyRepository;
    private final PatientRepository patientRepository;

    public InsurancePolicyService(
            InsurancePolicyRepository insurancePolicyRepository,
            PatientRepository patientRepository
    ) {
        this.insurancePolicyRepository = insurancePolicyRepository;
        this.patientRepository = patientRepository;
    }

    public List<InsurancePolicy> getAllPolicies() {
        return insurancePolicyRepository.findAll();
    }

    public InsurancePolicy getPolicyById(String policyId) {
        return insurancePolicyRepository.findById(policyId)
                .orElseThrow(
                        () -> new InsurancePolicyNotFoundException(policyId)
                );
    }

    public InsurancePolicy addPolicy(
            String policyId,
            String patientId,
            String providerName,
            BigDecimal coverageAmount,
            String policyType
    ) {

        if (insurancePolicyRepository.existsById(policyId)) {
            throw new InsurancePolicyAlreadyExistsException(policyId);
        }

        if (insurancePolicyRepository
                .existsByPatient_PatientId(patientId)) {
            throw new PatientAlreadyHasPolicyException(patientId);
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(
                        () -> new PatientNotFoundException(patientId)
                );

        InsurancePolicy policy = new InsurancePolicy(
                policyId,
                patient,
                providerName,
                coverageAmount,
                policyType
        );

        return insurancePolicyRepository.save(policy);
    }

    public InsurancePolicy updatePolicy(
            String policyId,
            String providerName,
            BigDecimal coverageAmount,
            String policyType
    ) {

        InsurancePolicy existingPolicy =
                getPolicyById(policyId);

        existingPolicy.setProviderName(providerName);
        existingPolicy.setCoverageAmount(coverageAmount);
        existingPolicy.setPolicyType(policyType);

        return insurancePolicyRepository.save(existingPolicy);
    }

    public void deletePolicy(String policyId) {

        InsurancePolicy policy =
                getPolicyById(policyId);

        insurancePolicyRepository.delete(policy);
    }
}