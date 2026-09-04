package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.model.Patient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private final List<Patient> patients = new ArrayList<>();

    public List<Patient> getAllPatients() {
        return patients;
    }

    public Patient addPatient(Patient patient) {
        patients.add(patient);
        return patient;
    }

    public Patient findPatientById(String patientId) {

        for (Patient patient : patients) {

            if (patient.getPatientId().equals(patientId)) {
                return patient;
            }
        }

        return null;
    }
}