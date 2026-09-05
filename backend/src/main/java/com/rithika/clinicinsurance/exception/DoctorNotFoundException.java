package com.rithika.clinicinsurance.exception;

public class DoctorNotFoundException extends RuntimeException {

    public DoctorNotFoundException(String doctorId) {
        super("Doctor with ID " + doctorId + " was not found");
    }
}