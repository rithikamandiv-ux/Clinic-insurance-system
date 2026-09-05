package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.exception.DoctorAlreadyExistsException;
import com.rithika.clinicinsurance.exception.DoctorNotFoundException;
import com.rithika.clinicinsurance.model.Doctor;
import com.rithika.clinicinsurance.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor getDoctorById(String doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException(doctorId));
    }

    public Doctor addDoctor(Doctor doctor) {

        if (doctorRepository.existsById(doctor.getDoctorId())) {
            throw new DoctorAlreadyExistsException(doctor.getDoctorId());
        }

        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(String doctorId, Doctor updatedDoctor) {

        Doctor existingDoctor = getDoctorById(doctorId);

        existingDoctor.setDoctorName(updatedDoctor.getDoctorName());
        existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
        existingDoctor.setConsultationFee(updatedDoctor.getConsultationFee());

        return doctorRepository.save(existingDoctor);
    }

    public void deleteDoctor(String doctorId) {

        Doctor doctor = getDoctorById(doctorId);

        doctorRepository.delete(doctor);
    }
}