package com.rithika.clinicinsurance.repository;

import com.rithika.clinicinsurance.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository
        extends JpaRepository<Appointment, String> {
}