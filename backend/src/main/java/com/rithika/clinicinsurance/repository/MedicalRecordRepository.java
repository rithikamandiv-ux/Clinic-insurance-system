package com.rithika.clinicinsurance.repository;

import com.rithika.clinicinsurance.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordRepository
        extends JpaRepository<MedicalRecord, String> {

    boolean existsByAppointment_AppointmentId(String appointmentId);
}