package com.rithika.clinicinsurance.controller;

import com.rithika.clinicinsurance.dto.InsurancePolicyCreateRequest;
import com.rithika.clinicinsurance.dto.InsurancePolicyUpdateRequest;
import com.rithika.clinicinsurance.dto.InsurancePolicyResponse;
import com.rithika.clinicinsurance.model.InsurancePolicy;
import com.rithika.clinicinsurance.service.InsurancePolicyService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurance-policies")
public class InsurancePolicyController {

    private final InsurancePolicyService insurancePolicyService;

    public InsurancePolicyController(
            InsurancePolicyService insurancePolicyService
    ) {
        this.insurancePolicyService = insurancePolicyService;
    }

    @GetMapping
    public ResponseEntity<List<InsurancePolicyResponse>>
    getAllPolicies() {

        List<InsurancePolicyResponse> policies =
                insurancePolicyService
                        .getAllPolicies()
                        .stream()
                        .map(InsurancePolicyResponse::from)
                        .toList();

        return ResponseEntity.ok(policies);
    }

    @GetMapping("/{policyId}")
    public ResponseEntity<InsurancePolicyResponse>
    getPolicyById(
            @PathVariable("policyId")
            @Pattern(
                    regexp = "^POL\\d{3}$",
                    message = "Policy ID must follow the format POL###, for example POL001."
            )
            String policyId
    ) {

        InsurancePolicy policy =
                insurancePolicyService.getPolicyById(policyId);

        return ResponseEntity.ok(
                InsurancePolicyResponse.from(policy)
        );
    }

    @PostMapping
    public ResponseEntity<InsurancePolicyResponse>
    addPolicy(
            @Valid @RequestBody InsurancePolicyCreateRequest request
    ) {

        InsurancePolicy policy =
                insurancePolicyService.addPolicy(
                        request.getPolicyId(),
                        request.getPatientId(),
                        request.getProviderName(),
                        request.getCoverageAmount(),
                        request.getPolicyType()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(InsurancePolicyResponse.from(policy));
    }

    @PutMapping("/{policyId}")
    public ResponseEntity<InsurancePolicyResponse>
    updatePolicy(
            @PathVariable("policyId")
            @Pattern(
                    regexp = "^POL\\d{3}$",
                    message = "Policy ID must follow the format POL###, for example POL001."
            )
            String policyId,
            @Valid @RequestBody InsurancePolicyUpdateRequest request
    ) {

        InsurancePolicy policy =
                insurancePolicyService.updatePolicy(
                        policyId,
                        request.getProviderName(),
                        request.getCoverageAmount(),
                        request.getPolicyType()
                );

        return ResponseEntity.ok(
                InsurancePolicyResponse.from(policy)
        );
    }

    @DeleteMapping("/{policyId}")
    public ResponseEntity<Void> deletePolicy(
            @PathVariable("policyId")
            @Pattern(
                    regexp = "^POL\\d{3}$",
                    message = "Policy ID must follow the format POL###, for example POL001."
            )
            String policyId
    ) {

        insurancePolicyService.deletePolicy(policyId);

        return ResponseEntity.noContent().build();
    }
}