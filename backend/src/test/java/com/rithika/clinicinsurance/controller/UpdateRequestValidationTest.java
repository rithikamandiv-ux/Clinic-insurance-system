package com.rithika.clinicinsurance.controller;

import com.rithika.clinicinsurance.service.PatientService;
import com.rithika.clinicinsurance.service.DoctorService;
import com.rithika.clinicinsurance.service.AppointmentService;
import com.rithika.clinicinsurance.service.MedicalRecordService;
import com.rithika.clinicinsurance.service.InsurancePolicyService;
import com.rithika.clinicinsurance.exception.GlobalExceptionHandler;
import com.rithika.clinicinsurance.enums.AppointmentStatus;
import com.rithika.clinicinsurance.model.Appointment;
import com.rithika.clinicinsurance.model.Doctor;
import com.rithika.clinicinsurance.model.Patient;
import com.rithika.clinicinsurance.model.MedicalRecord;
import com.rithika.clinicinsurance.model.InsurancePolicy;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

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
class UpdateRequestValidationTest {

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

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                        patientController,
                        doctorController,
                        appointmentController,
                        medicalRecordController,
                        insurancePolicyController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void updatePatientRejectsMissingPatientName() throws Exception {

        String request = """
                {"age": 31, "phoneNumber": "0779999999", "insuranceStatus": true}
                """;

        assertInvalidUpdate("/api/patients/P001", request, "patientName", "Patient name is required");

        verifyNoInteractions(patientService);
    }

    @Test
    void updateDoctorRejectsConsultationFeeWithTooManyDecimalPlaces() throws Exception {

        String request = """
                {"doctorName": "Updated Doctor", "specialization": "General", "consultationFee": 2500.001}
                """;

        assertInvalidUpdate("/api/doctors/D001", request, "consultationFee",
                "Consultation fee must have at most 8 integer digits and 2 decimal places");

        verifyNoInteractions(doctorService);
    }

    @Test
    void updateMedicalRecordRejectsNegativeTreatmentCost() throws Exception {

        String request = """
                {"diagnosis": "Updated", "treatment": "Updated", "treatmentCost": -1.00}
                """;

        assertInvalidUpdate("/api/medical-records/MR001", request, "treatmentCost",
                "Treatment cost cannot be negative");

        verifyNoInteractions(medicalRecordService);
    }

    @Test
    void updatePatientDoesNotRequirePatientIdInRequestBody() throws Exception {

        String request = """
                {"patientName": "Updated Example", "age": 31,
                 "phoneNumber": "0779999999", "insuranceStatus": true}
                """;
        when(patientService.updatePatient(eq("P001"), any(Patient.class)))
                .thenAnswer(invocation -> invocation.getArgument(1));

        mockMvc.perform(put("/api/patients/P001")
                        .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value("P001"))
                .andExpect(jsonPath("$.patientName").value("Updated Example"))
                .andExpect(jsonPath("$.age").value(31))
                .andExpect(jsonPath("$.phoneNumber").value("0779999999"))
                .andExpect(jsonPath("$.insuranceStatus").value(true));

        ArgumentCaptor<Patient> patient = ArgumentCaptor.forClass(Patient.class);
        verify(patientService).updatePatient(eq("P001"), patient.capture());
        assertEquals("P001", patient.getValue().getPatientId());
    }

    @Test
    void updateDoctorDoesNotRequireDoctorIdInRequestBody() throws Exception {

        String request = """
                {"doctorName": "Updated Doctor", "specialization": "General", "consultationFee": 2500.00}
                """;
        when(doctorService.updateDoctor(eq("D001"), any(Doctor.class)))
                .thenAnswer(invocation -> invocation.getArgument(1));

        mockMvc.perform(put("/api/doctors/D001")
                        .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value("D001"))
                .andExpect(jsonPath("$.doctorName").value("Updated Doctor"))
                .andExpect(jsonPath("$.specialization").value("General"))
                .andExpect(jsonPath("$.consultationFee").value(2500.00));

        ArgumentCaptor<Doctor> doctor = ArgumentCaptor.forClass(Doctor.class);
        verify(doctorService).updateDoctor(eq("D001"), doctor.capture());
        assertEquals("D001", doctor.getValue().getDoctorId());
    }

    @Test
    void updateAppointmentDoesNotRequireAppointmentIdInRequestBody() throws Exception {

        LocalDate date = LocalDate.now().plusDays(1);
        String request = """
                {"patientId": "P001", "doctorId": "D001", "appointmentDate": "%s"}
                """.formatted(date);
        Appointment appointment = appointment(date);
        when(appointmentService.updateAppointment("A001", "P001", "D001", date))
                .thenReturn(appointment);

        mockMvc.perform(put("/api/appointments/A001")
                        .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentId").value("A001"))
                .andExpect(jsonPath("$.patientId").value("P001"))
                .andExpect(jsonPath("$.doctorId").value("D001"));

        verify(appointmentService).updateAppointment("A001", "P001", "D001", date);
    }

    @Test
    void updateMedicalRecordDoesNotRequireRecordOrAppointmentIdInRequestBody() throws Exception {

        String request = """
                {"diagnosis": "Updated diagnosis", "treatment": "Updated treatment", "treatmentCost": 6000.00}
                """;
        BigDecimal cost = new BigDecimal("6000.00");
        MedicalRecord record = new MedicalRecord("MR001", appointment(LocalDate.now()),
                "Updated diagnosis", "Updated treatment", cost);
        when(medicalRecordService.updateMedicalRecord("MR001", "Updated diagnosis", "Updated treatment", cost))
                .thenReturn(record);

        mockMvc.perform(put("/api/medical-records/MR001")
                        .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recordId").value("MR001"))
                .andExpect(jsonPath("$.appointmentId").value("A001"))
                .andExpect(jsonPath("$.diagnosis").value("Updated diagnosis"))
                .andExpect(jsonPath("$.treatment").value("Updated treatment"))
                .andExpect(jsonPath("$.treatmentCost").value(6000.00));

        verify(medicalRecordService).updateMedicalRecord("MR001", "Updated diagnosis", "Updated treatment", cost);
    }

    @Test
    void updatePolicyDoesNotRequirePolicyOrPatientIdInRequestBody() throws Exception {

        String request = """
                {"providerName": "Updated Provider", "coverageAmount": 150000.00, "policyType": "Premium"}
                """;
        BigDecimal coverage = new BigDecimal("150000.00");
        InsurancePolicy policy = new InsurancePolicy("POL001", patient(), "Updated Provider", coverage, "Premium");
        when(insurancePolicyService.updatePolicy("POL001", "Updated Provider", coverage, "Premium"))
                .thenReturn(policy);

        mockMvc.perform(put("/api/insurance-policies/POL001")
                        .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyId").value("POL001"))
                .andExpect(jsonPath("$.patientId").value("P001"))
                .andExpect(jsonPath("$.providerName").value("Updated Provider"))
                .andExpect(jsonPath("$.coverageAmount").value(150000.00))
                .andExpect(jsonPath("$.policyType").value("Premium"));

        verify(insurancePolicyService).updatePolicy("POL001", "Updated Provider", coverage, "Premium");
    }

    private void assertInvalidUpdate(String path, String request, String field, String message) throws Exception {

        mockMvc.perform(put(path).contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.path").value(path))
                .andExpect(jsonPath("$.fieldErrors.length()").value(1))
                .andExpect(jsonPath("$.fieldErrors." + field).value(message));
    }

    private Patient patient() {
        return new Patient("P001", "Example", 30, "0771234567", true);
    }

    private Appointment appointment(LocalDate date) {
        Doctor doctor = new Doctor("D001", "Doctor", "General", new BigDecimal("2500.00"));
        return new Appointment("A001", date, AppointmentStatus.COMPLETED, patient(), doctor);
    }
}
