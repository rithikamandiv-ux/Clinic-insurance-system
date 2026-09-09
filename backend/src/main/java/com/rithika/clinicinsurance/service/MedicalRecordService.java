package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.enums.AppointmentStatus;
import com.rithika.clinicinsurance.exception.AppointmentNotFoundException;
import com.rithika.clinicinsurance.exception.InvalidMedicalRecordAppointmentException;
import com.rithika.clinicinsurance.exception.MedicalRecordAlreadyExistsException;
import com.rithika.clinicinsurance.exception.MedicalRecordNotFoundException;
import com.rithika.clinicinsurance.model.Appointment;
import com.rithika.clinicinsurance.model.MedicalRecord;
import com.rithika.clinicinsurance.repository.AppointmentRepository;
import com.rithika.clinicinsurance.repository.MedicalRecordRepository;
import org.springframework.stereotype.Service;
import com.rithika.clinicinsurance.exception.ResourceInUseException;
import com.rithika.clinicinsurance.repository.InsuranceClaimRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;
    private final InsuranceClaimRepository insuranceClaimRepository;

    public MedicalRecordService(
            MedicalRecordRepository medicalRecordRepository,
            AppointmentRepository appointmentRepository,
            InsuranceClaimRepository insuranceClaimRepository
    ) {

        this.medicalRecordRepository = medicalRecordRepository;
        this.appointmentRepository = appointmentRepository;
        this.insuranceClaimRepository = insuranceClaimRepository;
    }

    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordRepository.findAll();
    }

    public MedicalRecord getMedicalRecordById(String recordId) {
        return medicalRecordRepository.findById(recordId)
                .orElseThrow(
                        () -> new MedicalRecordNotFoundException(recordId)
                );
    }

    public MedicalRecord addMedicalRecord(
            String recordId,
            String appointmentId,
            String diagnosis,
            String treatment,
            BigDecimal treatmentCost
    ) {

        if (medicalRecordRepository.existsById(recordId)) {
            throw new MedicalRecordAlreadyExistsException(recordId);
        }

        Appointment appointment = appointmentRepository
                .findById(appointmentId)
                .orElseThrow(
                        () -> new AppointmentNotFoundException(appointmentId)
                );

        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new InvalidMedicalRecordAppointmentException(
                    appointmentId,
                    "medical record can only be created for a completed appointment"
            );
        }

        if (medicalRecordRepository
                .existsByAppointment_AppointmentId(appointmentId)) {

            throw new InvalidMedicalRecordAppointmentException(
                    appointmentId,
                    "a medical record already exists for this appointment"
            );
        }

        MedicalRecord medicalRecord = new MedicalRecord(
                recordId,
                appointment,
                diagnosis,
                treatment,
                treatmentCost
        );

        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord updateMedicalRecord(
            String recordId,
            String diagnosis,
            String treatment,
            BigDecimal treatmentCost
    ) {

        MedicalRecord existingRecord =
                getMedicalRecordById(recordId);

        existingRecord.setDiagnosis(diagnosis);
        existingRecord.setTreatment(treatment);
        existingRecord.setTreatmentCost(treatmentCost);

        return medicalRecordRepository.save(existingRecord);
    }

    public void deleteMedicalRecord(String recordId) {

        MedicalRecord medicalRecord =
                getMedicalRecordById(recordId);

        if (
                insuranceClaimRepository
                        .existsByMedicalRecord_RecordId(recordId)
        ) {

            throw new ResourceInUseException(
                    "Medical record "
                            + recordId
                            + " cannot be deleted because an insurance claim references this medical record."
            );
        }

        medicalRecordRepository.delete(medicalRecord);
    }
}