package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.model.InsurancePolicy;
import com.rithika.clinicinsurance.model.Patient;
import com.rithika.clinicinsurance.exception.InsurancePolicyAlreadyExistsException;
import com.rithika.clinicinsurance.exception.InsurancePolicyNotFoundException;
import com.rithika.clinicinsurance.exception.PatientAlreadyHasPolicyException;
import com.rithika.clinicinsurance.exception.PatientNotFoundException;
import com.rithika.clinicinsurance.repository.InsurancePolicyRepository;
import com.rithika.clinicinsurance.repository.PatientRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InsurancePolicyServiceTest {

    @Mock
    private InsurancePolicyRepository insurancePolicyRepository;

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private InsurancePolicyService insurancePolicyService;

    @Test
    void getPolicyByIdReturnsExistingInsurancePolicy() {

        InsurancePolicy existing = policy();
        when(insurancePolicyRepository.findById("POL001")).thenReturn(Optional.of(existing));

        InsurancePolicy result = insurancePolicyService.getPolicyById("POL001");

        assertSame(existing, result);
    }

    @Test
    void getPolicyByIdThrowsExceptionWhenInsurancePolicyDoesNotExist() {

        when(insurancePolicyRepository.findById("POL001")).thenReturn(Optional.empty());

        assertThrows(InsurancePolicyNotFoundException.class,
                () -> insurancePolicyService.getPolicyById("POL001"));
    }

    @Test
    void addPolicyThrowsExceptionWhenPolicyAlreadyExists() {

        when(insurancePolicyRepository.existsById("POL001")).thenReturn(true);

        assertThrows(InsurancePolicyAlreadyExistsException.class,
                () -> insurancePolicyService.addPolicy("POL001", "P001", "Provider", new BigDecimal("5000.00"), "Basic"));

        verify(insurancePolicyRepository, never()).save(any(InsurancePolicy.class));
    }

    @Test
    void addPolicyThrowsExceptionWhenPatientAlreadyHasPolicy() {

        when(insurancePolicyRepository.existsByPatient_PatientId("P001")).thenReturn(true);

        assertThrows(PatientAlreadyHasPolicyException.class,
                () -> insurancePolicyService.addPolicy("POL001", "P001", "Provider", new BigDecimal("5000.00"), "Basic"));

        verify(insurancePolicyRepository, never()).save(any(InsurancePolicy.class));
    }

    @Test
    void addPolicyThrowsExceptionWhenPatientDoesNotExist() {

        when(patientRepository.findById("P001")).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> insurancePolicyService.addPolicy("POL001", "P001", "Provider", new BigDecimal("5000.00"), "Basic"));

        verify(insurancePolicyRepository, never()).save(any(InsurancePolicy.class));
    }

    @Test
    void addPolicyCreatesPolicyForPatient() {

        Patient patient = new Patient("P001", "Test Patient", 25, "0771234567", true);
        when(patientRepository.findById("P001")).thenReturn(Optional.of(patient));
        when(insurancePolicyRepository.save(any(InsurancePolicy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InsurancePolicy result = insurancePolicyService.addPolicy("POL001", "P001", "Provider", new BigDecimal("5000.00"), "Basic");

        assertEquals("POL001", result.getPolicyId());
        assertSame(patient, result.getPatient());
        assertEquals("Provider", result.getProviderName());
        assertEquals(0, new BigDecimal("5000.0").compareTo(result.getCoverageAmount()));
        assertEquals("Basic", result.getPolicyType());
        verify(insurancePolicyRepository).save(result);
    }

    @Test
    void updatePolicyUpdatesProviderCoverageAndType() {

        InsurancePolicy existing = policy();
        Patient originalPatient = existing.getPatient();
        when(insurancePolicyRepository.findById("POL001")).thenReturn(Optional.of(existing));
        when(insurancePolicyRepository.save(existing)).thenReturn(existing);

        InsurancePolicy result = insurancePolicyService.updatePolicy(
                "POL001", "New Provider", new BigDecimal("10000.00"), "Premium");

        assertSame(existing, result);
        assertSame(originalPatient, result.getPatient());
        assertEquals("New Provider", result.getProviderName());
        assertEquals(0, new BigDecimal("10000").compareTo(result.getCoverageAmount()));
        assertEquals("Premium", result.getPolicyType());
        verify(insurancePolicyRepository).save(existing);
    }

    @Test
    void deletePolicyDeletesUnreferencedInsurancePolicy() {

        InsurancePolicy existing = policy();
        when(insurancePolicyRepository.findById("POL001")).thenReturn(Optional.of(existing));

        insurancePolicyService.deletePolicy("POL001");

        verify(insurancePolicyRepository).delete(existing);
    }

    private InsurancePolicy policy() {
        Patient patient = new Patient("P001", "Test Patient", 25, "0771234567", true);
        return new InsurancePolicy("POL001", patient, "Provider", new BigDecimal("5000"), "Basic");
    }
}
