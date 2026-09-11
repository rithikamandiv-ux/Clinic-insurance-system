package com.rithika.clinicinsurance.controller;

import com.rithika.clinicinsurance.dto.InsuranceClaimRequest;
import com.rithika.clinicinsurance.dto.InsuranceClaimResponse;
import com.rithika.clinicinsurance.model.InsuranceClaim;
import com.rithika.clinicinsurance.service.InsuranceClaimService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance-claims")
public class InsuranceClaimController {

    private final InsuranceClaimService insuranceClaimService;

    public InsuranceClaimController(
            InsuranceClaimService insuranceClaimService
    ) {
        this.insuranceClaimService = insuranceClaimService;
    }

    @GetMapping
    public ResponseEntity<List<InsuranceClaimResponse>>
    getAllClaims() {

        List<InsuranceClaimResponse> claims =
                insuranceClaimService
                        .getAllClaims()
                        .stream()
                        .map(InsuranceClaimResponse::from)
                        .toList();

        return ResponseEntity.ok(claims);
    }

    @GetMapping("/{claimId}")
    public ResponseEntity<InsuranceClaimResponse>
    getClaimById(
            @PathVariable("claimId")
            @Pattern(
                    regexp = "^CL\\d{3}$",
                    message = "Claim ID must follow the format CL###, for example CL001."
            )
            String claimId
    ) {

        InsuranceClaim claim =
                insuranceClaimService.getClaimById(claimId);

        return ResponseEntity.ok(
                InsuranceClaimResponse.from(claim)
        );
    }

    @PostMapping
    public ResponseEntity<InsuranceClaimResponse>
    createClaim(
            @Valid @RequestBody InsuranceClaimRequest request
    ) {

        InsuranceClaim claim =
                insuranceClaimService.createClaim(
                        request.getClaimId(),
                        request.getMedicalRecordId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(InsuranceClaimResponse.from(claim));
    }

    @PatchMapping("/{claimId}/process")
    public ResponseEntity<InsuranceClaimResponse>
    processClaim(
            @PathVariable("claimId")
            @Pattern(
                    regexp = "^CL\\d{3}$",
                    message = "Claim ID must follow the format CL###, for example CL001."
            )
            String claimId
    ) {

        InsuranceClaim claim =
                insuranceClaimService.processClaim(claimId);

        return ResponseEntity.ok(
                InsuranceClaimResponse.from(claim)
        );
    }
}