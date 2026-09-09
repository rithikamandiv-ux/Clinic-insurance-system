package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.enums.AppointmentStatus;
import com.rithika.clinicinsurance.exception.AppointmentAlreadyExistsException;
import com.rithika.clinicinsurance.exception.AppointmentNotFoundException;
import com.rithika.clinicinsurance.exception.DoctorNotFoundException;
import com.rithika.clinicinsurance.exception.InvalidAppointmentStatusTransitionException;
import com.rithika.clinicinsurance.exception.PatientNotFoundException;
import com.rithika.clinicinsurance.model.Appointment;
import com.rithika.clinicinsurance.model.Doctor;
import com.rithika.clinicinsurance.model.Patient;
import com.rithika.clinicinsurance.repository.AppointmentRepository;
import com.rithika.clinicinsurance.repository.DoctorRepository;
import com.rithika.clinicinsurance.repository.PatientRepository;
import org.springframework.stereotype.Service;
import com.rithika.clinicinsurance.exception.ResourceInUseException;
import com.rithika.clinicinsurance.repository.MedicalRecordRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            MedicalRecordRepository medicalRecordRepository
    ) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointmentById(String appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(
                        () -> new AppointmentNotFoundException(appointmentId)
                );
    }

    public Appointment addAppointment(
            String appointmentId,
            String patientId,
            String doctorId,
            LocalDate appointmentDate
    ) {

        if (appointmentRepository.existsById(appointmentId)) {
            throw new AppointmentAlreadyExistsException(appointmentId);
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(
                        () -> new PatientNotFoundException(patientId)
                );

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(
                        () -> new DoctorNotFoundException(doctorId)
                );

        Appointment appointment = new Appointment(
                appointmentId,
                appointmentDate,
                AppointmentStatus.BOOKED,
                patient,
                doctor
        );

        return appointmentRepository.save(appointment);
    }

    public Appointment updateAppointment(
            String appointmentId,
            String patientId,
            String doctorId,
            LocalDate appointmentDate
    ) {

        Appointment existingAppointment =
                getAppointmentById(appointmentId);

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(
                        () -> new PatientNotFoundException(patientId)
                );

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(
                        () -> new DoctorNotFoundException(doctorId)
                );

        existingAppointment.setPatient(patient);
        existingAppointment.setDoctor(doctor);
        existingAppointment.setAppointmentDate(appointmentDate);

        return appointmentRepository.save(existingAppointment);
    }

    public Appointment completeAppointment(String appointmentId) {

        Appointment appointment = getAppointmentById(appointmentId);

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            throw new InvalidAppointmentStatusTransitionException(
                    appointmentId,
                    appointment.getStatus(),
                    AppointmentStatus.COMPLETED
            );
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);

        return appointmentRepository.save(appointment);
    }

    public Appointment cancelAppointment(String appointmentId) {

        Appointment appointment = getAppointmentById(appointmentId);

        if (appointment.getStatus() != AppointmentStatus.BOOKED) {
            throw new InvalidAppointmentStatusTransitionException(
                    appointmentId,
                    appointment.getStatus(),
                    AppointmentStatus.CANCELLED
            );
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        return appointmentRepository.save(appointment);
    }

    public void deleteAppointment(String appointmentId) {

        Appointment appointment = getAppointmentById(appointmentId);

        if (
                medicalRecordRepository
                        .existsByAppointment_AppointmentId(appointmentId)
        ) {

            throw new ResourceInUseException(
                    "Appointment "
                            + appointmentId
                            + " cannot be deleted because a medical record references this appointment."
            );
        }

        appointmentRepository.delete(appointment);
    }
}