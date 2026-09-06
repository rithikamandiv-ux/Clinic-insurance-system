package com.rithika.clinicsystem.api;

import com.rithika.clinicsystem.dto.InsuranceClaimRequest;
import com.rithika.clinicsystem.dto.InsuranceClaimResponse;

import java.util.List;

public class InsuranceClaimApiService {

    private final ApiClient apiClient;

    public InsuranceClaimApiService(
            ApiClient apiClient
    ) {
        this.apiClient = apiClient;
    }

    public List<InsuranceClaimResponse> getAllClaims() {

        return apiClient.getList(
                "/insurance-claims",
                InsuranceClaimResponse.class
        );
    }

    public InsuranceClaimResponse getClaimById(
            String claimId
    ) {

        return apiClient.get(
                "/insurance-claims/" + claimId,
                InsuranceClaimResponse.class
        );
    }

    public InsuranceClaimResponse createClaim(
            InsuranceClaimRequest request
    ) {

        return apiClient.post(
                "/insurance-claims",
                request,
                InsuranceClaimResponse.class
        );
    }

    public InsuranceClaimResponse processClaim(
            String claimId
    ) {

        return apiClient.patch(
                "/insurance-claims/"
                        + claimId
                        + "/process",
                InsuranceClaimResponse.class
        );
    }
}