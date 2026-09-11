package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.model.Appointment;
import com.rithika.clinicinsurance.model.Doctor;
import com.rithika.clinicinsurance.model.MedicalRecord;
import com.rithika.clinicinsurance.model.Patient;
import com.rithika.clinicinsurance.exception.AppointmentNotFoundException;
import com.rithika.clinicinsurance.exception.InvalidMedicalRecordAppointmentException;
import com.rithika.clinicinsurance.exception.MedicalRecordAlreadyExistsException;
import com.rithika.clinicinsurance.exception.MedicalRecordNotFoundException;
import com.rithika.clinicinsurance.exception.ResourceInUseException;
import com.rithika.clinicinsurance.enums.AppointmentStatus;
import com.rithika.clinicinsurance.repository.MedicalRecordRepository;
import com.rithika.clinicinsurance.repository.AppointmentRepository;
import com.rithika.clinicinsurance.repository.InsuranceClaimRepository;

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
class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private InsuranceClaimRepository insuranceClaimRepository;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @Test
    void getMedicalRecordByIdReturnsExistingMedicalRecord() {

        MedicalRecord existing = medicalRecord();
        when(medicalRecordRepository.findById("MR001")).thenReturn(Optional.of(existing));

        MedicalRecord result = medicalRecordService.getMedicalRecordById("MR001");

        assertSame(existing, result);
    }

    @Test
    void getMedicalRecordByIdThrowsExceptionWhenMedicalRecordDoesNotExist() {

        when(medicalRecordRepository.findById("MR001")).thenReturn(Optional.empty());

        assertThrows(MedicalRecordNotFoundException.class,
                () -> medicalRecordService.getMedicalRecordById("MR001"));
    }

    @Test
    void addMedicalRecordThrowsExceptionWhenRecordAlreadyExists() {

        when(medicalRecordRepository.existsById("MR001")).thenReturn(true);

        assertThrows(MedicalRecordAlreadyExistsException.class,
                () -> medicalRecordService.addMedicalRecord("MR001", "A001", "Diagnosis", "Treatment", new BigDecimal("2500.00")));

        verify(medicalRecordRepository, never()).save(any(MedicalRecord.class));
    }

    @Test
    void addMedicalRecordThrowsExceptionWhenAppointmentDoesNotExist() {

        when(appointmentRepository.findById("A001")).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class,
                () -> medicalRecordService.addMedicalRecord("MR001", "A001", "Diagnosis", "Treatment", new BigDecimal("2500.00")));

        verify(medicalRecordRepository, never()).save(any(MedicalRecord.class));
    }

    @Test
    void addMedicalRecordThrowsExceptionWhenAppointmentIsBooked() {

        Appointment appointment = completedAppointment();
        appointment.setStatus(AppointmentStatus.BOOKED);
        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(appointment));

        assertThrows(InvalidMedicalRecordAppointmentException.class,
                () -> medicalRecordService.addMedicalRecord("MR001", "A001", "Diagnosis", "Treatment", new BigDecimal("2500.00")));

        verify(medicalRecordRepository, never()).save(any(MedicalRecord.class));
    }

    @Test
    void addMedicalRecordThrowsExceptionWhenAppointmentIsCancelled() {

        Appointment appointment = completedAppointment();
        appointment.setStatus(AppointmentStatus.CANCELLED);
        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(appointment));

        assertThrows(InvalidMedicalRecordAppointmentException.class,
                () -> medicalRecordService.addMedicalRecord("MR001", "A001", "Diagnosis", "Treatment", new BigDecimal("2500.00")));

        verify(medicalRecordRepository, never()).save(any(MedicalRecord.class));
    }

    @Test
    void addMedicalRecordThrowsExceptionWhenAppointmentAlreadyHasRecord() {

        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(completedAppointment()));
        when(medicalRecordRepository.existsByAppointment_AppointmentId("A001")).thenReturn(true);

        assertThrows(InvalidMedicalRecordAppointmentException.class,
                () -> medicalRecordService.addMedicalRecord("MR001", "A001", "Diagnosis", "Treatment", new BigDecimal("2500.00")));

        verify(medicalRecordRepository, never()).save(any(MedicalRecord.class));
    }

    @Test
    void addMedicalRecordPreservesDetailsForCompletedAppointment() {

        Appointment appointment = completedAppointment();
        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(appointment));
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MedicalRecord result = medicalRecordService.addMedicalRecord("MR001", "A001", "Diagnosis", "Treatment", new BigDecimal("2500.00"));

        assertEquals("MR001", result.getRecordId());
        assertSame(appointment, result.getAppointment());
        assertEquals("Diagnosis", result.getDiagnosis());
        assertEquals("Treatment", result.getTreatment());
        assertEquals(0, new BigDecimal("2500.0").compareTo(result.getTreatmentCost()));
        verify(medicalRecordRepository).save(result);
    }

    @Test
    void updateMedicalRecordUpdatesDiagnosisTreatmentAndCost() {

        MedicalRecord existing = medicalRecord();
        Appointment originalAppointment = existing.getAppointment();
        when(medicalRecordRepository.findById("MR001")).thenReturn(Optional.of(existing));
        when(medicalRecordRepository.save(existing)).thenReturn(existing);

        MedicalRecord result = medicalRecordService.updateMedicalRecord(
                "MR001", "Updated diagnosis", "Updated treatment", new BigDecimal("3200.50"));

        assertSame(existing, result);
        assertSame(originalAppointment, result.getAppointment());
        assertEquals("Updated diagnosis", result.getDiagnosis());
        assertEquals("Updated treatment", result.getTreatment());
        assertEquals(0, new BigDecimal("3200.5").compareTo(result.getTreatmentCost()));
        verify(medicalRecordRepository).save(existing);
    }

    @Test
    void deleteMedicalRecordThrowsExceptionWhenResourceIsReferenced() {

        MedicalRecord existing = medicalRecord();
        when(medicalRecordRepository.findById("MR001")).thenReturn(Optional.of(existing));
        when(insuranceClaimRepository.existsByMedicalRecord_RecordId("MR001")).thenReturn(true);

        assertThrows(ResourceInUseException.class,
                () -> medicalRecordService.deleteMedicalRecord("MR001"));

        verify(medicalRecordRepository, never()).delete(any(MedicalRecord.class));
    }

    @Test
    void deleteMedicalRecordDeletesUnreferencedMedicalRecord() {

        MedicalRecord existing = medicalRecord();
        when(medicalRecordRepository.findById("MR001")).thenReturn(Optional.of(existing));
        when(insuranceClaimRepository.existsByMedicalRecord_RecordId("MR001")).thenReturn(false);

        medicalRecordService.deleteMedicalRecord("MR001");

        verify(medicalRecordRepository).delete(existing);
    }

    private Appointment completedAppointment() {
        Patient patient = new Patient("P001", "Test Patient", 25, "0771234567", true);
        Doctor doctor = new Doctor("D001", "Test Doctor", "General", new BigDecimal("1500.00"));
        return new Appointment("A001", LocalDate.of(2026, 10, 1), AppointmentStatus.COMPLETED, patient, doctor);
    }

    private MedicalRecord medicalRecord() {
        Appointment appointment = completedAppointment();
        return new MedicalRecord("MR001", appointment, "Diagnosis", "Treatment", new BigDecimal("2500.00"));
    }
}
