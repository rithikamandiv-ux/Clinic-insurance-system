package com.rithika.clinicsystem.api;

import com.rithika.clinicsystem.dto.AppointmentCreateRequest;
import com.rithika.clinicsystem.dto.AppointmentUpdateRequest;
import com.rithika.clinicsystem.dto.AppointmentResponse;

import java.util.List;

public class AppointmentApiService {

    private final ApiClient apiClient;

    public AppointmentApiService(
            ApiClient apiClient
    ) {
        this.apiClient = apiClient;
    }

    public List<AppointmentResponse> getAllAppointments() {

        return apiClient.getList(
                "/appointments",
                AppointmentResponse.class
        );
    }

    public AppointmentResponse getAppointmentById(
            String appointmentId
    ) {

        return apiClient.get(
                "/appointments/" + appointmentId,
                AppointmentResponse.class
        );
    }

    public AppointmentResponse addAppointment(
            AppointmentCreateRequest request
    ) {

        return apiClient.post(
                "/appointments",
                request,
                AppointmentResponse.class
        );
    }

    public AppointmentResponse updateAppointment(
            String appointmentId,
            AppointmentUpdateRequest request
    ) {

        return apiClient.put(
                "/appointments/" + appointmentId,
                request,
                AppointmentResponse.class
        );
    }

    public AppointmentResponse completeAppointment(
            String appointmentId
    ) {

        return apiClient.patch(
                "/appointments/"
                        + appointmentId
                        + "/complete",
                AppointmentResponse.class
        );
    }

    public AppointmentResponse cancelAppointment(
            String appointmentId
    ) {

        return apiClient.patch(
                "/appointments/"
                        + appointmentId
                        + "/cancel",
                AppointmentResponse.class
        );
    }

    public void deleteAppointment(
            String appointmentId
    ) {

        apiClient.delete(
                "/appointments/" + appointmentId
        );
    }
}