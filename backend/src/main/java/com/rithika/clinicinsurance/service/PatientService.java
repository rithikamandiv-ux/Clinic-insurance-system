package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.exception.PatientAlreadyExistsException;
import com.rithika.clinicinsurance.exception.PatientNotFoundException;
import com.rithika.clinicinsurance.model.Patient;
import com.rithika.clinicinsurance.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
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

        patientRepository.delete(patient);
    }
}