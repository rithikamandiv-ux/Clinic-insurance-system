package com.rithika.clinicsystem.api;

import com.rithika.clinicsystem.dto.DoctorRequest;
import com.rithika.clinicsystem.dto.DoctorResponse;

import java.util.List;

public class DoctorApiService {

    private final ApiClient apiClient;

    public DoctorApiService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<DoctorResponse> getAllDoctors() {

        return apiClient.getList(
                "/doctors",
                DoctorResponse.class
        );
    }

    public DoctorResponse getDoctorById(
            String doctorId
    ) {

        return apiClient.get(
                "/doctors/" + doctorId,
                DoctorResponse.class
        );
    }

    public DoctorResponse addDoctor(
            DoctorRequest request
    ) {

        return apiClient.post(
                "/doctors",
                request,
                DoctorResponse.class
        );
    }

    public DoctorResponse updateDoctor(
            String doctorId,
            DoctorRequest request
    ) {

        return apiClient.put(
                "/doctors/" + doctorId,
                request,
                DoctorResponse.class
        );
    }

    public void deleteDoctor(
            String doctorId
    ) {

        apiClient.delete(
                "/doctors/" + doctorId
        );
    }
}