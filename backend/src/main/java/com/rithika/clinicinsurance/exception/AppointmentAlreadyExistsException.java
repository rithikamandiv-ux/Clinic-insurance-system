package com.rithika.clinicinsurance.exception;

public class AppointmentAlreadyExistsException extends RuntimeException {

    public AppointmentAlreadyExistsException(String appointmentId) {
        super("Appointment with ID " + appointmentId + " already exists");
    }
}