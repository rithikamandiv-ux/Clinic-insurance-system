package com.rithika.clinicinsurance.exception;

public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException(String appointmentId) {
        super("Appointment with ID " + appointmentId + " was not found");
    }
}