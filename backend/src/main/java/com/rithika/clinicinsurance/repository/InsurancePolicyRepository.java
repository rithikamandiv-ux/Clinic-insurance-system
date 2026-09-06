package com.rithika.clinicinsurance.repository;

import com.rithika.clinicinsurance.model.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InsurancePolicyRepository
        extends JpaRepository<InsurancePolicy, String> {

    boolean existsByPatient_PatientId(String patientId);

    Optional<InsurancePolicy> findByPatient_PatientId(String patientId);
}