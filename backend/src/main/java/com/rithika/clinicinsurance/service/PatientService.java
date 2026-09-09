package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.exception.PatientAlreadyExistsException;
import com.rithika.clinicinsurance.exception.PatientNotFoundException;
import com.rithika.clinicinsurance.model.Patient;
import com.rithika.clinicinsurance.repository.PatientRepository;
import org.springframework.stereotype.Service;
import com.rithika.clinicinsurance.exception.ResourceInUseException;
import com.rithika.clinicinsurance.repository.AppointmentRepository;
import com.rithika.clinicinsurance.repository.InsurancePolicyRepository;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;

    public PatientService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            InsurancePolicyRepository insurancePolicyRepository
    ) {

        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.insurancePolicyRepository = insurancePolicyRepository;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(String patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
    }

    public Patient addPatient(Patient patient) {

        if (patientRepository.existsById(patient.getPatientId())) {
            throw new PatientAlreadyExistsException(patient.getPatientId());
        }

        return patientRepository.save(patient);
    }

    public Patient updatePatient(String patientId, Patient updatedPatient) {

        Patient existingPatient = getPatientById(patientId);

        existingPatient.setPatientName(updatedPatient.getPatientName());
        existingPatient.setAge(updatedPatient.getAge());
        existingPatient.setPhoneNumber(updatedPatient.getPhoneNumber());
        existingPatient.setInsuranceStatus(updatedPatient.isInsuranceStatus());

        return patientRepository.save(existingPatient);
    }

    public void deletePatient(String patientId) {

        Patient patient = getPatientById(patientId);

        if (
                appointmentRepository
                        .existsByPatient_PatientId(patientId)
        ) {

            throw new ResourceInUseException(
                    "Patient "
                            + patientId
                            + " cannot be deleted because existing appointments reference this patient."
            );
        }

        if (
                insurancePolicyRepository
                        .existsByPatient_PatientId(patientId)
        ) {

            throw new ResourceInUseException(
                    "Patient "
                            + patientId
                            + " cannot be deleted because an insurance policy references this patient."
            );
        }

        patientRepository.delete(patient);
    }
}