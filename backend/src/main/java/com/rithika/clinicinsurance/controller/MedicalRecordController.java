package com.rithika.clinicinsurance.controller;

import com.rithika.clinicinsurance.dto.MedicalRecordRequest;
import com.rithika.clinicinsurance.dto.MedicalRecordResponse;
import com.rithika.clinicinsurance.model.MedicalRecord;
import com.rithika.clinicinsurance.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(
            MedicalRecordService medicalRecordService
    ) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecordResponse>>
    getAllMedicalRecords() {

        List<MedicalRecordResponse> records =
                medicalRecordService
                        .getAllMedicalRecords()
                        .stream()
                        .map(MedicalRecordResponse::from)
                        .toList();

        return ResponseEntity.ok(records);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<MedicalRecordResponse>
    getMedicalRecordById(
            @PathVariable String recordId
    ) {

        MedicalRecord medicalRecord =
                medicalRecordService.getMedicalRecordById(recordId);

        return ResponseEntity.ok(
                MedicalRecordResponse.from(medicalRecord)
        );
    }

    @PostMapping
    public ResponseEntity<MedicalRecordResponse>
    addMedicalRecord(
            @Valid @RequestBody MedicalRecordRequest request
    ) {

        MedicalRecord medicalRecord =
                medicalRecordService.addMedicalRecord(
                        request.getRecordId(),
                        request.getAppointmentId(),
                        request.getDiagnosis(),
                        request.getTreatment(),
                        request.getTreatmentCost()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MedicalRecordResponse.from(medicalRecord));
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<MedicalRecordResponse>
    updateMedicalRecord(
            @PathVariable String recordId,
            @Valid @RequestBody MedicalRecordRequest request
    ) {

        MedicalRecord medicalRecord =
                medicalRecordService.updateMedicalRecord(
                        recordId,
                        request.getDiagnosis(),
                        request.getTreatment(),
                        request.getTreatmentCost()
                );

        return ResponseEntity.ok(
                MedicalRecordResponse.from(medicalRecord)
        );
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteMedicalRecord(
            @PathVariable String recordId
    ) {

        medicalRecordService.deleteMedicalRecord(recordId);

        return ResponseEntity.noContent().build();
    }
}