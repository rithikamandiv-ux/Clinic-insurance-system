package com.rithika.clinicinsurance.controller;

import com.rithika.clinicinsurance.dto.AppointmentCreateRequest;
import com.rithika.clinicinsurance.dto.AppointmentUpdateRequest;
import com.rithika.clinicinsurance.dto.AppointmentResponse;
import com.rithika.clinicinsurance.model.Appointment;
import com.rithika.clinicinsurance.service.AppointmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(
            AppointmentService appointmentService
    ) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<AppointmentResponse>>
    getAllAppointments() {

        List<AppointmentResponse> appointments =
                appointmentService
                        .getAllAppointments()
                        .stream()
                        .map(AppointmentResponse::from)
                        .toList();

        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse>
    getAppointmentById(
            @PathVariable("appointmentId")
            @Pattern(
                    regexp = "^A\\d{3}$",
                    message = "Appointment ID must follow the format A###, for example A001."
            )
            String appointmentId
    ) {

        Appointment appointment =
                appointmentService
                        .getAppointmentById(appointmentId);

        return ResponseEntity.ok(
                AppointmentResponse.from(appointment)
        );
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse>
    addAppointment(
            @Valid @RequestBody AppointmentCreateRequest request
    ) {

        Appointment appointment =
                appointmentService.addAppointment(
                        request.getAppointmentId(),
                        request.getPatientId(),
                        request.getDoctorId(),
                        request.getAppointmentDate()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AppointmentResponse.from(appointment));
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse>
    updateAppointment(
            @PathVariable("appointmentId")
            @Pattern(
                    regexp = "^A\\d{3}$",
                    message = "Appointment ID must follow the format A###, for example A001."
            )
            String appointmentId,
            @Valid @RequestBody AppointmentUpdateRequest request
    ) {

        Appointment appointment =
                appointmentService.updateAppointment(
                        appointmentId,
                        request.getPatientId(),
                        request.getDoctorId(),
                        request.getAppointmentDate()
                );

        return ResponseEntity.ok(
                AppointmentResponse.from(appointment)
        );
    }

    @PatchMapping("/{appointmentId}/complete")
    public ResponseEntity<AppointmentResponse> completeAppointment(
            @PathVariable("appointmentId")
            @Pattern(
                    regexp = "^A\\d{3}$",
                    message = "Appointment ID must follow the format A###, for example A001."
            )
            String appointmentId
    ) {

        Appointment appointment =
                appointmentService.completeAppointment(appointmentId);

        return ResponseEntity.ok(
                AppointmentResponse.from(appointment)
        );
    }

    @PatchMapping("/{appointmentId}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(
            @PathVariable("appointmentId")
            @Pattern(
                    regexp = "^A\\d{3}$",
                    message = "Appointment ID must follow the format A###, for example A001."
            )
            String appointmentId
    ) {

        Appointment appointment =
                appointmentService.cancelAppointment(appointmentId);

        return ResponseEntity.ok(
                AppointmentResponse.from(appointment)
        );
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable("appointmentId")
            @Pattern(
                    regexp = "^A\\d{3}$",
                    message = "Appointment ID must follow the format A###, for example A001."
            )
            String appointmentId
    ) {

        appointmentService.deleteAppointment(appointmentId);

        return ResponseEntity.noContent().build();
    }
}