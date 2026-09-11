package com.rithika.clinicinsurance.controller;

import com.rithika.clinicinsurance.dto.DoctorCreateRequest;
import com.rithika.clinicinsurance.dto.DoctorUpdateRequest;
import com.rithika.clinicinsurance.dto.DoctorResponse;
import com.rithika.clinicinsurance.model.Doctor;
import com.rithika.clinicinsurance.service.DoctorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {

        List<DoctorResponse> doctors = doctorService
                .getAllDoctors()
                .stream()
                .map(DoctorResponse::from)
                .toList();

        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> getDoctorById(
            @PathVariable("doctorId")
            @Pattern(
                    regexp = "^D\\d{3}$",
                    message = "Doctor ID must follow the format D###, for example D001."
            )
            String doctorId
    ) {

        Doctor doctor = doctorService.getDoctorById(doctorId);

        return ResponseEntity.ok(
                DoctorResponse.from(doctor)
        );
    }

    @PostMapping
    public ResponseEntity<DoctorResponse> addDoctor(
            @Valid @RequestBody DoctorCreateRequest request
    ) {

        Doctor doctor = new Doctor(
                request.getDoctorId(),
                request.getDoctorName(),
                request.getSpecialization(),
                request.getConsultationFee()
        );

        Doctor savedDoctor = doctorService.addDoctor(doctor);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(DoctorResponse.from(savedDoctor));
    }

    @PutMapping("/{doctorId}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable("doctorId")
            @Pattern(
                    regexp = "^D\\d{3}$",
                    message = "Doctor ID must follow the format D###, for example D001."
            )
            String doctorId,
            @Valid @RequestBody DoctorUpdateRequest request
    ) {

        Doctor updatedDoctor = new Doctor(
                doctorId,
                request.getDoctorName(),
                request.getSpecialization(),
                request.getConsultationFee()
        );

        Doctor savedDoctor = doctorService.updateDoctor(
                doctorId,
                updatedDoctor
        );

        return ResponseEntity.ok(
                DoctorResponse.from(savedDoctor)
        );
    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable("doctorId")
            @Pattern(
                    regexp = "^D\\d{3}$",
                    message = "Doctor ID must follow the format D###, for example D001."
            )
            String doctorId
    ) {

        doctorService.deleteDoctor(doctorId);

        return ResponseEntity.noContent().build();
    }
}