package com.rithika.clinicinsurance.repository;

import com.rithika.clinicinsurance.model.InsuranceClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceClaimRepository
        extends JpaRepository<InsuranceClaim, String> {

    boolean existsByMedicalRecord_RecordId(String recordId);
}