package com.rithika.clinicsystem.api;

import com.rithika.clinicsystem.dto.MedicalRecordRequest;
import com.rithika.clinicsystem.dto.MedicalRecordResponse;

import java.util.List;

public class MedicalRecordApiService {

    private final ApiClient apiClient;

    public MedicalRecordApiService(
            ApiClient apiClient
    ) {
        this.apiClient = apiClient;
    }

    public List<MedicalRecordResponse> getAllMedicalRecords() {

        return apiClient.getList(
                "/medical-records",
                MedicalRecordResponse.class
        );
    }

    public MedicalRecordResponse getMedicalRecordById(
            String recordId
    ) {

        return apiClient.get(
                "/medical-records/" + recordId,
                MedicalRecordResponse.class
        );
    }

    public MedicalRecordResponse addMedicalRecord(
            MedicalRecordRequest request
    ) {

        return apiClient.post(
                "/medical-records",
                request,
                MedicalRecordResponse.class
        );
    }

    public MedicalRecordResponse updateMedicalRecord(
            String recordId,
            MedicalRecordRequest request
    ) {

        return apiClient.put(
                "/medical-records/" + recordId,
                request,
                MedicalRecordResponse.class
        );
    }

    public void deleteMedicalRecord(
            String recordId
    ) {

        apiClient.delete(
                "/medical-records/" + recordId
        );
    }
}