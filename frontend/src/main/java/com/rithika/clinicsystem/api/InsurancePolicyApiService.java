package com.rithika.clinicsystem.api;

import com.rithika.clinicsystem.dto.InsurancePolicyCreateRequest;
import com.rithika.clinicsystem.dto.InsurancePolicyUpdateRequest;
import com.rithika.clinicsystem.dto.InsurancePolicyResponse;

import java.util.List;

public class InsurancePolicyApiService {

    private final ApiClient apiClient;

    public InsurancePolicyApiService(
            ApiClient apiClient
    ) {
        this.apiClient = apiClient;
    }

    public List<InsurancePolicyResponse> getAllPolicies() {

        return apiClient.getList(
                "/insurance-policies",
                InsurancePolicyResponse.class
        );
    }

    public InsurancePolicyResponse getPolicyById(
            String policyId
    ) {

        return apiClient.get(
                "/insurance-policies/" + policyId,
                InsurancePolicyResponse.class
        );
    }

    public InsurancePolicyResponse addPolicy(
            InsurancePolicyCreateRequest request
    ) {

        return apiClient.post(
                "/insurance-policies",
                request,
                InsurancePolicyResponse.class
        );
    }

    public InsurancePolicyResponse updatePolicy(
            String policyId,
            InsurancePolicyUpdateRequest request
    ) {

        return apiClient.put(
                "/insurance-policies/" + policyId,
                request,
                InsurancePolicyResponse.class
        );
    }

    public void deletePolicy(
            String policyId
    ) {

        apiClient.delete(
                "/insurance-policies/" + policyId
        );
    }
}