package com.rithika.clinicsystem.api;

import com.rithika.clinicsystem.dto.PatientRequest;
import com.rithika.clinicsystem.dto.PatientResponse;

import java.util.List;

public class PatientApiService {

    private final ApiClient apiClient;

    public PatientApiService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<PatientResponse> getAllPatients() {

        return apiClient.getList(
                "/patients",
                PatientResponse.class
        );
    }

    public PatientResponse getPatientById(
            String patientId
    ) {

        return apiClient.get(
                "/patients/" + patientId,
                PatientResponse.class
        );
    }

    public PatientResponse addPatient(
            PatientRequest request
    ) {

        return apiClient.post(
                "/patients",
                request,
                PatientResponse.class
        );
    }

    public PatientResponse updatePatient(
            String patientId,
            PatientRequest request
    ) {

        return apiClient.put(
                "/patients/" + patientId,
                request,
                PatientResponse.class
        );
    }

    public void deletePatient(
            String patientId
    ) {

        apiClient.delete(
                "/patients/" + patientId
        );
    }
}