package com.rithika.clinicinsurance.exception;

public class InvalidMedicalRecordAppointmentException
        extends RuntimeException {

    public InvalidMedicalRecordAppointmentException(
            String appointmentId,
            String message
    ) {
        super(
                "Appointment with ID " + appointmentId + ": " + message
        );
    }
}