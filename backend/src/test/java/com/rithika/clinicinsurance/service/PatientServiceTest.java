package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.model.Patient;
import com.rithika.clinicinsurance.repository.AppointmentRepository;
import com.rithika.clinicinsurance.repository.InsurancePolicyRepository;
import com.rithika.clinicinsurance.repository.PatientRepository;
import com.rithika.clinicinsurance.exception.PatientNotFoundException;
import com.rithika.clinicinsurance.exception.PatientAlreadyExistsException;
import com.rithika.clinicinsurance.exception.ResourceInUseException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private InsurancePolicyRepository insurancePolicyRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void getPatientByIdReturnsPatientWhenPatientExists() {

        Patient patient =
                new Patient(
                        "P001",
                        "Test Patient",
                        25,
                        "0771234567",
                        true
                );

        when(
                patientRepository.findById("P001")
        ).thenReturn(
                Optional.of(patient)
        );

        Patient result =
                patientService.getPatientById("P001");

        assertSame(
                patient,
                result
        );

        verify(
                patientRepository
        ).findById("P001");
    }

    @Test
    void getPatientByIdThrowsExceptionWhenPatientDoesNotExist() {

        when(
                patientRepository.findById("P999")
        ).thenReturn(
                Optional.empty()
        );

        assertThrows(
                PatientNotFoundException.class,
                () -> patientService.getPatientById("P999")
        );

        verify(
                patientRepository
        ).findById("P999");
    }

    @Test
    void addPatientThrowsExceptionWhenPatientAlreadyExists() {

        Patient patient =
                new Patient(
                        "P001",
                        "Test Patient",
                        25,
                        "0771234567",
                        true
                );

        when(
                patientRepository.existsById("P001")
        ).thenReturn(true);

        assertThrows(
                PatientAlreadyExistsException.class,
                () -> patientService.addPatient(patient)
        );

        verify(
                patientRepository
        ).existsById("P001");

        verify(
                patientRepository,
                never()
        ).save(patient);
    }

    @Test
    void addPatientSavesAndReturnsPatientWhenPatientDoesNotExist() {

        Patient patient =
                new Patient(
                        "P002",
                        "New Patient",
                        30,
                        "0712345678",
                        false
                );

        when(
                patientRepository.existsById("P002")
        ).thenReturn(false);

        when(
                patientRepository.save(patient)
        ).thenReturn(patient);

        Patient result =
                patientService.addPatient(patient);

        assertSame(
                patient,
                result
        );

        verify(
                patientRepository
        ).existsById("P002");

        verify(
                patientRepository
        ).save(patient);
    }

    @Test
    void updatePatientUpdatesFieldsAndReturnsSavedPatient() {

        Patient existingPatient =
                new Patient(
                        "P001",
                        "Old Name",
                        25,
                        "0771111111",
                        false
                );

        Patient updatedPatient =
                new Patient(
                        "P001",
                        "Updated Name",
                        30,
                        "0772222222",
                        true
                );

        when(
                patientRepository.findById("P001")
        ).thenReturn(
                Optional.of(existingPatient)
        );

        when(
                patientRepository.save(existingPatient)
        ).thenReturn(existingPatient);

        Patient result =
                patientService.updatePatient(
                        "P001",
                        updatedPatient
                );

        assertSame(
                existingPatient,
                result
        );

        assertEquals(
                "Updated Name",
                result.getPatientName()
        );

        assertEquals(
                30,
                result.getAge()
        );

        assertEquals(
                "0772222222",
                result.getPhoneNumber()
        );

        assertTrue(
                result.isInsuranceStatus()
        );

        verify(
                patientRepository
        ).findById("P001");

        verify(
                patientRepository
        ).save(existingPatient);
    }

    @Test
    void deletePatientThrowsExceptionWhenPatientHasAppointments() {

        Patient patient =
                new Patient(
                        "P001",
                        "Test Patient",
                        25,
                        "0771234567",
                        true
                );

        when(
                patientRepository.findById("P001")
        ).thenReturn(
                Optional.of(patient)
        );

        when(
                appointmentRepository
                        .existsByPatient_PatientId("P001")
        ).thenReturn(true);

        assertThrows(
                ResourceInUseException.class,
                () -> patientService.deletePatient("P001")
        );

        verify(
                appointmentRepository
        ).existsByPatient_PatientId("P001");

        verify(
                patientRepository,
                never()
        ).delete(patient);
    }

    @Test
    void deletePatientThrowsExceptionWhenPatientHasInsurancePolicy() {

        Patient patient =
                new Patient(
                        "P001",
                        "Test Patient",
                        25,
                        "0771234567",
                        true
                );

        when(
                patientRepository.findById("P001")
        ).thenReturn(
                Optional.of(patient)
        );

        when(
                appointmentRepository
                        .existsByPatient_PatientId("P001")
        ).thenReturn(false);

        when(
                insurancePolicyRepository
                        .existsByPatient_PatientId("P001")
        ).thenReturn(true);

        assertThrows(
                ResourceInUseException.class,
                () -> patientService.deletePatient("P001")
        );

        verify(
                appointmentRepository
        ).existsByPatient_PatientId("P001");

        verify(
                insurancePolicyRepository
        ).existsByPatient_PatientId("P001");

        verify(
                patientRepository,
                never()
        ).delete(patient);
    }

    @Test
    void deletePatientDeletesPatientWhenPatientIsNotReferenced() {

        Patient patient =
                new Patient(
                        "P001",
                        "Test Patient",
                        25,
                        "0771234567",
                        true
                );

        when(
                patientRepository.findById("P001")
        ).thenReturn(
                Optional.of(patient)
        );

        when(
                appointmentRepository
                        .existsByPatient_PatientId("P001")
        ).thenReturn(false);

        when(
                insurancePolicyRepository
                        .existsByPatient_PatientId("P001")
        ).thenReturn(false);

        patientService.deletePatient("P001");

        verify(
                appointmentRepository
        ).existsByPatient_PatientId("P001");

        verify(
                insurancePolicyRepository
        ).existsByPatient_PatientId("P001");

        verify(
                patientRepository
        ).delete(patient);
    }
}