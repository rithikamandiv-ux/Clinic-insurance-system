package com.rithika.clinicinsurance.exception;

import com.rithika.clinicinsurance.enums.AppointmentStatus;

public class InvalidAppointmentStatusTransitionException
        extends RuntimeException {

    public InvalidAppointmentStatusTransitionException(
            String appointmentId,
            AppointmentStatus currentStatus,
            AppointmentStatus requestedStatus
    ) {
        super(
                "Appointment with ID " + appointmentId
                        + " cannot change status from "
                        + currentStatus
                        + " to "
                        + requestedStatus
        );
    }
}