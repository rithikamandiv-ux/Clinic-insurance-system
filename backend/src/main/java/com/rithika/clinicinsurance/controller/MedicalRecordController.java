package com.rithika.clinicinsurance.controller;

import com.rithika.clinicinsurance.dto.MedicalRecordCreateRequest;
import com.rithika.clinicinsurance.dto.MedicalRecordUpdateRequest;
import com.rithika.clinicinsurance.dto.MedicalRecordResponse;
import com.rithika.clinicinsurance.model.MedicalRecord;
import com.rithika.clinicinsurance.service.MedicalRecordService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
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
            @PathVariable("recordId")
            @Pattern(
                    regexp = "^MR\\d{3}$",
                    message = "Medical Record ID must follow the format MR###, for example MR001."
            )
            String recordId
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
            @Valid @RequestBody MedicalRecordCreateRequest request
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
            @PathVariable("recordId")
            @Pattern(
                    regexp = "^MR\\d{3}$",
                    message = "Medical Record ID must follow the format MR###, for example MR001."
            )
            String recordId,
            @Valid @RequestBody MedicalRecordUpdateRequest request
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
            @PathVariable("recordId")
            @Pattern(
                    regexp = "^MR\\d{3}$",
                    message = "Medical Record ID must follow the format MR###, for example MR001."
            )
            String recordId
    ) {

        medicalRecordService.deleteMedicalRecord(recordId);

        return ResponseEntity.noContent().build();
    }
}