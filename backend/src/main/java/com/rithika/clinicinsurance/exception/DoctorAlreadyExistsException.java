package com.rithika.clinicinsurance.exception;

public class DoctorAlreadyExistsException extends RuntimeException {

    public DoctorAlreadyExistsException(String doctorId) {
        super("Doctor with ID " + doctorId + " already exists");
    }
}