package com.rithika.clinicinsurance.controller;

import com.rithika.clinicinsurance.dto.PatientRequest;
import com.rithika.clinicinsurance.dto.PatientResponse;
import com.rithika.clinicinsurance.model.Patient;
import com.rithika.clinicinsurance.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {

        List<PatientResponse> patients = patientService
                .getAllPatients()
                .stream()
                .map(PatientResponse::from)
                .toList();

        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponse> getPatientById(
            @PathVariable String patientId
    ) {

        Patient patient = patientService.getPatientById(patientId);

        return ResponseEntity.ok(
                PatientResponse.from(patient)
        );
    }

    @PostMapping
    public ResponseEntity<PatientResponse> addPatient(
            @Valid @RequestBody PatientRequest request
    ) {

        Patient patient = new Patient(
                request.getPatientId(),
                request.getPatientName(),
                request.getAge(),
                request.getPhoneNumber(),
                request.getInsuranceStatus()
        );

        Patient savedPatient = patientService.addPatient(patient);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PatientResponse.from(savedPatient));
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable String patientId,
            @Valid @RequestBody PatientRequest request
    ) {

        Patient updatedPatient = new Patient(
                patientId,
                request.getPatientName(),
                request.getAge(),
                request.getPhoneNumber(),
                request.getInsuranceStatus()
        );

        Patient savedPatient = patientService.updatePatient(
                patientId,
                updatedPatient
        );

        return ResponseEntity.ok(
                PatientResponse.from(savedPatient)
        );
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(
            @PathVariable String patientId
    ) {

        patientService.deletePatient(patientId);

        return ResponseEntity.noContent().build();
    }
}