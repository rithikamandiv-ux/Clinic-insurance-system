package com.rithika.clinicinsurance.controller;

import com.rithika.clinicinsurance.service.PatientService;
import com.rithika.clinicinsurance.service.DoctorService;
import com.rithika.clinicinsurance.service.AppointmentService;
import com.rithika.clinicinsurance.service.MedicalRecordService;
import com.rithika.clinicinsurance.service.InsurancePolicyService;
import com.rithika.clinicinsurance.service.InsuranceClaimService;
import com.rithika.clinicinsurance.exception.GlobalExceptionHandler;
import com.rithika.clinicinsurance.exception.PatientNotFoundException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PathIdValidationTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private DoctorController doctorController;

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private MedicalRecordController medicalRecordController;

    @Mock
    private InsurancePolicyService insurancePolicyService;

    @InjectMocks
    private InsurancePolicyController insurancePolicyController;

    @Mock
    private InsuranceClaimService insuranceClaimService;

    @InjectMocks
    private InsuranceClaimController insuranceClaimController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                        patientController,
                        doctorController,
                        appointmentController,
                        medicalRecordController,
                        insurancePolicyController,
                        insuranceClaimController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getPatientRejectsMalformedPathId() throws Exception {

        assertMalformedPath(get("/api/patients/banana"),
                "/api/patients/banana", "patientId",
                "Patient ID must follow the format P###, for example P001.");
    }

    @Test
    void deleteDoctorRejectsMalformedPathId() throws Exception {

        assertMalformedPath(delete("/api/doctors/123"),
                "/api/doctors/123", "doctorId",
                "Doctor ID must follow the format D###, for example D001.");
    }

    @Test
    void completeAppointmentRejectsMalformedPathId() throws Exception {

        assertMalformedPath(patch("/api/appointments/not-valid/complete"),
                "/api/appointments/not-valid/complete", "appointmentId",
                "Appointment ID must follow the format A###, for example A001.");
    }

    @Test
    void getMedicalRecordRejectsMalformedPathId() throws Exception {

        assertMalformedPath(get("/api/medical-records/M1"),
                "/api/medical-records/M1", "recordId",
                "Medical Record ID must follow the format MR###, for example MR001.");
    }

    @Test
    void getPolicyRejectsMalformedPathId() throws Exception {

        assertMalformedPath(get("/api/insurance-policies/POL01"),
                "/api/insurance-policies/POL01", "policyId",
                "Policy ID must follow the format POL###, for example POL001.");
    }

    @Test
    void processClaimRejectsMalformedPathId() throws Exception {

        assertMalformedPath(patch("/api/insurance-claims/CL0001/process"),
                "/api/insurance-claims/CL0001/process", "claimId",
                "Claim ID must follow the format CL###, for example CL001.");
    }

    @Test
    void getPatientReturnsNotFoundForValidButMissingId() throws Exception {

        when(patientService.getPatientById("P999"))
                .thenThrow(new PatientNotFoundException("P999"));

        mockMvc.perform(get("/api/patients/P999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Patient with ID P999 was not found"))
                .andExpect(jsonPath("$.path").value("/api/patients/P999"));

        verify(patientService).getPatientById("P999");
    }

    private void assertMalformedPath(
            MockHttpServletRequestBuilder request,
            String path,
            String field,
            String message
    ) throws Exception {

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(
                        HandlerMethodValidationException.class, result.getResolvedException()))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.path").value(path))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.fieldErrors.length()").value(1))
                .andExpect(jsonPath("$.fieldErrors." + field).value(message));

        verifyNoInteractions(patientService, doctorService, appointmentService,
                medicalRecordService, insurancePolicyService, insuranceClaimService);
    }
}
