package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.model.Appointment;
import com.rithika.clinicinsurance.model.Doctor;
import com.rithika.clinicinsurance.model.InsuranceClaim;
import com.rithika.clinicinsurance.model.InsurancePolicy;
import com.rithika.clinicinsurance.model.MedicalRecord;
import com.rithika.clinicinsurance.model.Patient;
import com.rithika.clinicinsurance.exception.InsuranceClaimAlreadyExistsException;
import com.rithika.clinicinsurance.exception.InsuranceClaimNotFoundException;
import com.rithika.clinicinsurance.exception.InvalidInsuranceClaimStatusTransitionException;
import com.rithika.clinicinsurance.exception.MedicalRecordAlreadyHasClaimException;
import com.rithika.clinicinsurance.exception.MedicalRecordNotFoundException;
import com.rithika.clinicinsurance.enums.AppointmentStatus;
import com.rithika.clinicinsurance.enums.ClaimStatus;
import com.rithika.clinicinsurance.repository.InsuranceClaimRepository;
import com.rithika.clinicinsurance.repository.MedicalRecordRepository;
import com.rithika.clinicinsurance.repository.InsurancePolicyRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsuranceClaimServiceTest {

    @Mock
    private InsuranceClaimRepository insuranceClaimRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private InsurancePolicyRepository insurancePolicyRepository;

    @InjectMocks
    private InsuranceClaimService insuranceClaimService;

    @Test
    void getClaimByIdReturnsExistingInsuranceClaim() {

        InsuranceClaim existing = pendingClaim();
        when(insuranceClaimRepository.findById("CL001")).thenReturn(Optional.of(existing));

        InsuranceClaim result = insuranceClaimService.getClaimById("CL001");

        assertSame(existing, result);
    }

    @Test
    void getClaimByIdThrowsExceptionWhenInsuranceClaimDoesNotExist() {

        when(insuranceClaimRepository.findById("CL001")).thenReturn(Optional.empty());

        assertThrows(InsuranceClaimNotFoundException.class,
                () -> insuranceClaimService.getClaimById("CL001"));
    }

    @Test
    void createClaimThrowsExceptionWhenClaimAlreadyExists() {

        when(insuranceClaimRepository.existsById("CL001")).thenReturn(true);

        assertThrows(InsuranceClaimAlreadyExistsException.class,
                () -> insuranceClaimService.createClaim("CL001", "MR001"));

        verify(insuranceClaimRepository, never()).save(any(InsuranceClaim.class));
    }

    @Test
    void createClaimThrowsExceptionWhenMedicalRecordAlreadyHasClaim() {

        when(insuranceClaimRepository.existsByMedicalRecord_RecordId("MR001")).thenReturn(true);

        assertThrows(MedicalRecordAlreadyHasClaimException.class,
                () -> insuranceClaimService.createClaim("CL001", "MR001"));

        verify(insuranceClaimRepository, never()).save(any(InsuranceClaim.class));
    }

    @Test
    void createClaimThrowsExceptionWhenMedicalRecordDoesNotExist() {

        when(medicalRecordRepository.findById("MR001")).thenReturn(Optional.empty());

        assertThrows(MedicalRecordNotFoundException.class,
                () -> insuranceClaimService.createClaim("CL001", "MR001"));

        verify(insuranceClaimRepository, never()).save(any(InsuranceClaim.class));
    }

    @Test
    void createClaimUsesTreatmentCostAsClaimAmountAndPendingStatus() {

        MedicalRecord record = medicalRecord();
        record.setTreatmentCost(new BigDecimal("3750.50"));
        when(medicalRecordRepository.findById("MR001")).thenReturn(Optional.of(record));
        when(insuranceClaimRepository.save(any(InsuranceClaim.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InsuranceClaim result = insuranceClaimService.createClaim("CL001", "MR001");

        assertEquals("CL001", result.getClaimId());
        assertSame(record, result.getMedicalRecord());
        assertEquals(0, new BigDecimal("3750.5").compareTo(result.getClaimAmount()));
        assertEquals(ClaimStatus.PENDING, result.getStatus());
        verify(insuranceClaimRepository).save(result);
    }

    @Test
    void processClaimThrowsExceptionWhenClaimAlreadyApproved() {

        InsuranceClaim claim = pendingClaim();
        claim.setStatus(ClaimStatus.APPROVED);
        when(insuranceClaimRepository.findById("CL001")).thenReturn(Optional.of(claim));

        assertThrows(InvalidInsuranceClaimStatusTransitionException.class,
                () -> insuranceClaimService.processClaim("CL001"));

        verify(insuranceClaimRepository, never()).save(any(InsuranceClaim.class));
    }

    @Test
    void processClaimThrowsExceptionWhenClaimAlreadyRejected() {

        InsuranceClaim claim = pendingClaim();
        claim.setStatus(ClaimStatus.REJECTED);
        when(insuranceClaimRepository.findById("CL001")).thenReturn(Optional.of(claim));

        assertThrows(InvalidInsuranceClaimStatusTransitionException.class,
                () -> insuranceClaimService.processClaim("CL001"));

        verify(insuranceClaimRepository, never()).save(any(InsuranceClaim.class));
    }

    @Test
    void processClaimRejectsClaimWhenPatientHasNoPolicy() {

        InsuranceClaim claim = pendingClaim();
        when(insuranceClaimRepository.findById("CL001")).thenReturn(Optional.of(claim));
        when(insurancePolicyRepository.findByPatient_PatientId("P001")).thenReturn(Optional.empty());
        when(insuranceClaimRepository.save(claim)).thenReturn(claim);

        InsuranceClaim result = insuranceClaimService.processClaim("CL001");

        assertSame(claim, result);
        assertEquals(ClaimStatus.REJECTED, result.getStatus());
        assertEquals(0, new BigDecimal("2500").compareTo(result.getClaimAmount()));
        verify(insuranceClaimRepository).save(claim);
    }

    @Test
    void processClaimApprovesClaimWhenCoverageExceedsClaimAmount() {

        InsuranceClaim claim = pendingClaim();
        when(insuranceClaimRepository.findById("CL001")).thenReturn(Optional.of(claim));
        InsurancePolicy policy = new InsurancePolicy("POL001",
                claim.getMedicalRecord().getAppointment().getPatient(),
                "Provider", new BigDecimal("3000"), "Basic");
        when(insurancePolicyRepository.findByPatient_PatientId("P001")).thenReturn(Optional.of(policy));
        when(insuranceClaimRepository.save(claim)).thenReturn(claim);

        InsuranceClaim result = insuranceClaimService.processClaim("CL001");

        assertSame(claim, result);
        assertEquals(ClaimStatus.APPROVED, result.getStatus());
        assertEquals(0, new BigDecimal("2500").compareTo(result.getClaimAmount()));
        verify(insuranceClaimRepository).save(claim);
    }

    @Test
    void processClaimApprovesClaimWhenCoverageEqualsClaimAmount() {

        InsuranceClaim claim = pendingClaim();
        when(insuranceClaimRepository.findById("CL001")).thenReturn(Optional.of(claim));
        InsurancePolicy policy = new InsurancePolicy("POL001",
                claim.getMedicalRecord().getAppointment().getPatient(),
                "Provider", new BigDecimal("2500.0"), "Basic");
        when(insurancePolicyRepository.findByPatient_PatientId("P001")).thenReturn(Optional.of(policy));
        when(insuranceClaimRepository.save(claim)).thenReturn(claim);

        InsuranceClaim result = insuranceClaimService.processClaim("CL001");

        assertSame(claim, result);
        assertEquals(ClaimStatus.APPROVED, result.getStatus());
        assertEquals(0, new BigDecimal("2500").compareTo(result.getClaimAmount()));
        verify(insuranceClaimRepository).save(claim);
    }

    @Test
    void processClaimRejectsClaimWhenCoverageIsLessThanClaimAmount() {

        InsuranceClaim claim = pendingClaim();
        when(insuranceClaimRepository.findById("CL001")).thenReturn(Optional.of(claim));
        InsurancePolicy policy = new InsurancePolicy("POL001",
                claim.getMedicalRecord().getAppointment().getPatient(),
                "Provider", new BigDecimal("2000"), "Basic");
        when(insurancePolicyRepository.findByPatient_PatientId("P001")).thenReturn(Optional.of(policy));
        when(insuranceClaimRepository.save(claim)).thenReturn(claim);

        InsuranceClaim result = insuranceClaimService.processClaim("CL001");

        assertSame(claim, result);
        assertEquals(ClaimStatus.REJECTED, result.getStatus());
        assertEquals(0, new BigDecimal("2500").compareTo(result.getClaimAmount()));
        verify(insuranceClaimRepository).save(claim);
    }

    private MedicalRecord medicalRecord() {
        Patient patient = new Patient("P001", "Test Patient", 25, "0771234567", true);
        Doctor doctor = new Doctor("D001", "Test Doctor", "General", new BigDecimal("1500.00"));
        Appointment appointment = new Appointment("A001", LocalDate.of(2026, 10, 1), AppointmentStatus.COMPLETED, patient, doctor);
        return new MedicalRecord("MR001", appointment, "Diagnosis", "Treatment", new BigDecimal("2500.00"));
    }

    private InsuranceClaim pendingClaim() {
        return new InsuranceClaim("CL001", medicalRecord(), new BigDecimal("2500.00"), ClaimStatus.PENDING);
    }
}
