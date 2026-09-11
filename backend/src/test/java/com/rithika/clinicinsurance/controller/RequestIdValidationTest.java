package com.rithika.clinicinsurance.controller;

import com.rithika.clinicinsurance.exception.GlobalExceptionHandler;
import com.rithika.clinicinsurance.service.AppointmentService;
import com.rithika.clinicinsurance.service.InsuranceClaimService;
import com.rithika.clinicinsurance.service.PatientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RequestIdValidationTest {

    @Mock
    private PatientService patientService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private InsuranceClaimService insuranceClaimService;

    @InjectMocks
    private PatientController patientController;

    @InjectMocks
    private AppointmentController appointmentController;

    @InjectMocks
    private InsuranceClaimController insuranceClaimController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Real MVC validation and exception handling, with no database or Boot context.
        mockMvc = MockMvcBuilders.standaloneSetup(
                        patientController, appointmentController, insuranceClaimController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void addPatientRejectsLowercasePatientId() throws Exception {

        String request = """
                {
                    "patientId": "p001",
                    "patientName": "Test Patient",
                    "age": 25,
                    "phoneNumber": "0771234567",
                    "insuranceStatus": true
                }
                """;

        assertInvalidId("/api/patients", request, "patientId",
                "Patient ID must follow the format P###, for example P001.");

        verifyNoInteractions(patientService);
    }

    @Test
    void addAppointmentRejectsIdWithTooFewDigits() throws Exception {

        String request = """
                {
                    "appointmentId": "A01",
                    "patientId": "P001",
                    "doctorId": "D001",
                    "appointmentDate": "%s"
                }
                """.formatted(LocalDate.now().plusDays(1));

        assertInvalidId("/api/appointments", request, "appointmentId",
                "Appointment ID must follow the format A###, for example A001.");

        verifyNoInteractions(appointmentService);
    }

    @Test
    void createClaimRejectsIdWithTooManyDigits() throws Exception {

        String request = """
                {
                    "claimId": "CL0001",
                    "medicalRecordId": "MR001"
                }
                """;

        assertInvalidId("/api/insurance-claims", request, "claimId",
                "Claim ID must follow the format CL###, for example CL001.");

        verifyNoInteractions(insuranceClaimService);
    }

    private void assertInvalidId(
            String path,
            String request,
            String field,
            String message
    ) throws Exception {

        mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.path").value(path))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.fieldErrors.length()").value(1))
                .andExpect(jsonPath("$.fieldErrors." + field).value(message));
    }
}
