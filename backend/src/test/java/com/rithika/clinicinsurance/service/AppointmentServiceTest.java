package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.model.Appointment;
import com.rithika.clinicinsurance.model.Doctor;
import com.rithika.clinicinsurance.model.Patient;
import com.rithika.clinicinsurance.exception.AppointmentAlreadyExistsException;
import com.rithika.clinicinsurance.exception.AppointmentNotFoundException;
import com.rithika.clinicinsurance.exception.DoctorNotFoundException;
import com.rithika.clinicinsurance.exception.InvalidAppointmentStatusTransitionException;
import com.rithika.clinicinsurance.exception.PatientNotFoundException;
import com.rithika.clinicinsurance.exception.ResourceInUseException;
import com.rithika.clinicinsurance.enums.AppointmentStatus;
import com.rithika.clinicinsurance.repository.AppointmentRepository;
import com.rithika.clinicinsurance.repository.PatientRepository;
import com.rithika.clinicinsurance.repository.DoctorRepository;
import com.rithika.clinicinsurance.repository.MedicalRecordRepository;

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
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    void getAppointmentByIdReturnsExistingAppointment() {

        Appointment existing = bookedAppointment();
        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(existing));

        Appointment result = appointmentService.getAppointmentById("A001");

        assertSame(existing, result);
    }

    @Test
    void getAppointmentByIdThrowsExceptionWhenAppointmentDoesNotExist() {

        when(appointmentRepository.findById("A001")).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class,
                () -> appointmentService.getAppointmentById("A001"));
    }

    @Test
    void addAppointmentThrowsExceptionWhenAppointmentAlreadyExists() {

        when(appointmentRepository.existsById("A001")).thenReturn(true);

        assertThrows(AppointmentAlreadyExistsException.class,
                () -> appointmentService.addAppointment("A001", "P001", "D001", LocalDate.of(2026, 10, 1)));

        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void addAppointmentThrowsExceptionWhenPatientDoesNotExist() {

        when(patientRepository.findById("P001")).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> appointmentService.addAppointment("A001", "P001", "D001", LocalDate.of(2026, 10, 1)));

        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void addAppointmentThrowsExceptionWhenDoctorDoesNotExist() {

        Patient patient = new Patient("P001", "Test Patient", 25, "0771234567", true);
        when(patientRepository.findById("P001")).thenReturn(Optional.of(patient));
        when(doctorRepository.findById("D001")).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class,
                () -> appointmentService.addAppointment("A001", "P001", "D001", LocalDate.of(2026, 10, 1)));

        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void updateAppointmentThrowsExceptionWhenPatientDoesNotExist() {

        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(bookedAppointment()));
        when(patientRepository.findById("P001")).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class,
                () -> appointmentService.updateAppointment("A001", "P001", "D001", LocalDate.of(2026, 10, 1)));

        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void updateAppointmentThrowsExceptionWhenDoctorDoesNotExist() {

        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(bookedAppointment()));
        Patient patient = new Patient("P001", "Test Patient", 25, "0771234567", true);
        when(patientRepository.findById("P001")).thenReturn(Optional.of(patient));
        when(doctorRepository.findById("D001")).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class,
                () -> appointmentService.updateAppointment("A001", "P001", "D001", LocalDate.of(2026, 10, 1)));

        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void updateAppointmentThrowsExceptionWhenAppointmentDoesNotExist() {

        when(appointmentRepository.findById("A001")).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class,
                () -> appointmentService.updateAppointment("A001", "P001", "D001", LocalDate.of(2026, 10, 1)));

        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void addAppointmentCreatesBookedAppointment() {

        Patient patient = new Patient("P001", "Test Patient", 25, "0771234567", true);
        Doctor doctor = new Doctor("D001", "Test Doctor", "General", new BigDecimal("1500.00"));
        when(patientRepository.findById("P001")).thenReturn(Optional.of(patient));
        when(doctorRepository.findById("D001")).thenReturn(Optional.of(doctor));
        when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Appointment result = appointmentService.addAppointment("A001", "P001", "D001", LocalDate.of(2026, 10, 1));

        assertEquals("A001", result.getAppointmentId());
        assertEquals(LocalDate.of(2026, 10, 1), result.getAppointmentDate());
        assertSame(patient, result.getPatient());
        assertSame(doctor, result.getDoctor());
        assertEquals(AppointmentStatus.BOOKED, result.getStatus());
        verify(appointmentRepository).save(result);
    }

    @Test
    void updateAppointmentUpdatesPatientDoctorAndDate() {

        Appointment existing = bookedAppointment();
        Patient patient = new Patient("P002", "New Patient", 30, "0772222222", false);
        Doctor doctor = new Doctor("D002", "New Doctor", "Cardiology", new BigDecimal("3000"));
        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(existing));
        when(patientRepository.findById("P002")).thenReturn(Optional.of(patient));
        when(doctorRepository.findById("D002")).thenReturn(Optional.of(doctor));
        when(appointmentRepository.save(existing)).thenReturn(existing);

        Appointment result = appointmentService.updateAppointment(
                "A001", "P002", "D002", LocalDate.of(2026, 10, 2));

        assertSame(existing, result);
        assertSame(patient, result.getPatient());
        assertSame(doctor, result.getDoctor());
        assertEquals(LocalDate.of(2026, 10, 2), result.getAppointmentDate());
        assertEquals(AppointmentStatus.BOOKED, result.getStatus());
        verify(appointmentRepository).save(existing);
    }

    @Test
    void completeAppointmentChangesBookedAppointmentToCompleted() {

        Appointment appointment = bookedAppointment();
        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        Appointment result = appointmentService.completeAppointment("A001");

        assertSame(appointment, result);
        assertEquals(AppointmentStatus.COMPLETED, result.getStatus());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void completeAppointmentThrowsExceptionWhenAppointmentAlreadyCompleted() {

        Appointment appointment = bookedAppointment();
        appointment.setStatus(AppointmentStatus.COMPLETED);
        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(appointment));

        assertThrows(InvalidAppointmentStatusTransitionException.class,
                () -> appointmentService.completeAppointment("A001"));

        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void completeAppointmentThrowsExceptionWhenAppointmentAlreadyCancelled() {

        Appointment appointment = bookedAppointment();
        appointment.setStatus(AppointmentStatus.CANCELLED);
        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(appointment));

        assertThrows(InvalidAppointmentStatusTransitionException.class,
                () -> appointmentService.completeAppointment("A001"));

        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void cancelAppointmentChangesBookedAppointmentToCancelled() {

        Appointment appointment = bookedAppointment();
        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        Appointment result = appointmentService.cancelAppointment("A001");

        assertSame(appointment, result);
        assertEquals(AppointmentStatus.CANCELLED, result.getStatus());
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void cancelAppointmentThrowsExceptionWhenAppointmentAlreadyCompleted() {

        Appointment appointment = bookedAppointment();
        appointment.setStatus(AppointmentStatus.COMPLETED);
        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(appointment));

        assertThrows(InvalidAppointmentStatusTransitionException.class,
                () -> appointmentService.cancelAppointment("A001"));

        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void cancelAppointmentThrowsExceptionWhenAppointmentAlreadyCancelled() {

        Appointment appointment = bookedAppointment();
        appointment.setStatus(AppointmentStatus.CANCELLED);
        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(appointment));

        assertThrows(InvalidAppointmentStatusTransitionException.class,
                () -> appointmentService.cancelAppointment("A001"));

        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void deleteAppointmentThrowsExceptionWhenResourceIsReferenced() {

        Appointment existing = bookedAppointment();
        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(existing));
        when(medicalRecordRepository.existsByAppointment_AppointmentId("A001")).thenReturn(true);

        assertThrows(ResourceInUseException.class,
                () -> appointmentService.deleteAppointment("A001"));

        verify(appointmentRepository, never()).delete(any(Appointment.class));
    }

    @Test
    void deleteAppointmentDeletesUnreferencedAppointment() {

        Appointment existing = bookedAppointment();
        when(appointmentRepository.findById("A001")).thenReturn(Optional.of(existing));
        when(medicalRecordRepository.existsByAppointment_AppointmentId("A001")).thenReturn(false);

        appointmentService.deleteAppointment("A001");

        verify(appointmentRepository).delete(existing);
    }

    private Appointment bookedAppointment() {
        Patient patient = new Patient("P001", "Test Patient", 25, "0771234567", true);
        Doctor doctor = new Doctor("D001", "Test Doctor", "General", new BigDecimal("1500.00"));
        return new Appointment("A001", LocalDate.of(2026, 10, 1),
                AppointmentStatus.BOOKED, patient, doctor);
    }
}
