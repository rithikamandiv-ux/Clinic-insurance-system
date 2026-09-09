package com.rithika.clinicinsurance.service;

import com.rithika.clinicinsurance.exception.DoctorAlreadyExistsException;
import com.rithika.clinicinsurance.exception.DoctorNotFoundException;
import com.rithika.clinicinsurance.model.Doctor;
import com.rithika.clinicinsurance.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import com.rithika.clinicinsurance.exception.ResourceInUseException;
import com.rithika.clinicinsurance.repository.AppointmentRepository;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    public DoctorService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository
    ) {

        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
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

        if (
                appointmentRepository
                        .existsByDoctor_DoctorId(doctorId)
        ) {

            throw new ResourceInUseException(
                    "Doctor "
                            + doctorId
                            + " cannot be deleted because existing appointments reference this doctor."
            );
        }

        doctorRepository.delete(doctor);
    }
}