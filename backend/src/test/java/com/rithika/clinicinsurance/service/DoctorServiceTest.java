package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.model.Doctor;
import com.rithika.clinicinsurance.exception.DoctorAlreadyExistsException;
import com.rithika.clinicinsurance.exception.DoctorNotFoundException;
import com.rithika.clinicinsurance.exception.ResourceInUseException;
import com.rithika.clinicinsurance.repository.DoctorRepository;
import com.rithika.clinicinsurance.repository.AppointmentRepository;

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
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    void getDoctorByIdReturnsExistingDoctor() {

        Doctor existing = new Doctor("D001", "Test Doctor", "General", new BigDecimal("1500.00"));
        when(doctorRepository.findById("D001")).thenReturn(Optional.of(existing));

        Doctor result = doctorService.getDoctorById("D001");

        assertSame(existing, result);
    }

    @Test
    void getDoctorByIdThrowsExceptionWhenDoctorDoesNotExist() {

        when(doctorRepository.findById("D001")).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class,
                () -> doctorService.getDoctorById("D001"));
    }

    @Test
    void addDoctorThrowsExceptionWhenDoctorAlreadyExists() {

        Doctor doctor = new Doctor("D001", "Test Doctor", "General", new BigDecimal("1500.00"));
        when(doctorRepository.existsById("D001")).thenReturn(true);

        assertThrows(DoctorAlreadyExistsException.class,
                () -> doctorService.addDoctor(doctor));

        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void addDoctorSavesAndReturnsNewDoctor() {

        Doctor doctor = new Doctor("D001", "Test Doctor", "General", new BigDecimal("1500.00"));
        when(doctorRepository.existsById("D001")).thenReturn(false);
        when(doctorRepository.save(doctor)).thenReturn(doctor);

        Doctor result = doctorService.addDoctor(doctor);

        assertSame(doctor, result);
        verify(doctorRepository).save(doctor);
    }

    @Test
    void updateDoctorUpdatesFieldsAndReturnsSavedDoctor() {

        Doctor existing = new Doctor("D001", "Test Doctor", "General", new BigDecimal("1500.00"));
        Doctor updated = new Doctor("D001", "Updated Doctor", "Cardiology", new BigDecimal("3000.0"));
        when(doctorRepository.findById("D001")).thenReturn(Optional.of(existing));
        when(doctorRepository.save(existing)).thenReturn(existing);

        Doctor result = doctorService.updateDoctor("D001", updated);

        assertSame(existing, result);
        assertEquals("D001", result.getDoctorId());
        assertEquals("Updated Doctor", result.getDoctorName());
        assertEquals("Cardiology", result.getSpecialization());
        assertEquals(0, new BigDecimal("3000.00").compareTo(result.getConsultationFee()));
        verify(doctorRepository).save(existing);
    }

    @Test
    void deleteDoctorThrowsExceptionWhenResourceIsReferenced() {

        Doctor existing = new Doctor("D001", "Test Doctor", "General", new BigDecimal("1500.00"));
        when(doctorRepository.findById("D001")).thenReturn(Optional.of(existing));
        when(appointmentRepository.existsByDoctor_DoctorId("D001")).thenReturn(true);

        assertThrows(ResourceInUseException.class,
                () -> doctorService.deleteDoctor("D001"));

        verify(doctorRepository, never()).delete(any(Doctor.class));
    }

    @Test
    void deleteDoctorDeletesUnreferencedDoctor() {

        Doctor existing = new Doctor("D001", "Test Doctor", "General", new BigDecimal("1500.00"));
        when(doctorRepository.findById("D001")).thenReturn(Optional.of(existing));
        when(appointmentRepository.existsByDoctor_DoctorId("D001")).thenReturn(false);

        doctorService.deleteDoctor("D001");

        verify(doctorRepository).delete(existing);
    }
}
