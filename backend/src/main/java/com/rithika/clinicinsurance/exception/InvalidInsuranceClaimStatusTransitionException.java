package com.rithika.clinicinsurance.exception;

import com.rithika.clinicinsurance.enums.ClaimStatus;

public class InvalidInsuranceClaimStatusTransitionException
        extends RuntimeException {

    public InvalidInsuranceClaimStatusTransitionException(
            String claimId,
            ClaimStatus currentStatus
    ) {
        super(
                "Insurance claim with ID " + claimId
                        + " cannot be processed because its current status is "
                        + currentStatus
        );
    }
}