package com.rithika.clinicinsurance.repository;

import com.rithika.clinicinsurance.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, String> {

    boolean existsByPatient_PatientId(String patientId);

    boolean existsByDoctor_DoctorId(String doctorId);
}