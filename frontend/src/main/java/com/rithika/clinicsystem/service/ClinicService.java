package com.rithika.clinicsystem.service;

import com.rithika.clinicsystem.model.Appointment;
import com.rithika.clinicsystem.model.Doctor;
import com.rithika.clinicsystem.model.MedicalRecord;
import com.rithika.clinicsystem.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class ClinicService {

    private List <Patient> patients;
    private List <Doctor> doctors;
    private List <Appointment> appointments;
    private List <MedicalRecord> medicalRecords;

    public ClinicService(){
        patients = new ArrayList<>();
        doctors = new ArrayList<>();
        appointments = new ArrayList<>();
        medicalRecords = new ArrayList<>();
    }

    //add methods
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void addDoctor(Doctor doctor) {
        doctors.add(doctor);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    //find methods
    public Patient findPatienById (String patienId){
        for (Patient patient : patients){
            if(patient.getPatientId().equals(patienId)){
                return patient;
            }
        }
        return null;
    }

    public Doctor findDoctorById (String doctorId){
        for (Doctor doctor : doctors){
            if (doctor.getDoctorId().equals(doctorId)){
                return doctor;
            }
        }
        return null;
    }

    public Appointment findAppointmentById (String appointmentId){
        for (Appointment appointment : appointments){
            if (appointment.getAppointmentId().equals(appointmentId)){
                return appointment;
            }
        }
        return null;
    }

    public MedicalRecord findMedicalRecordById (String recordId){
        for (MedicalRecord medicalRecord : medicalRecords){
            if (medicalRecord.getRecordId().equals(recordId)){
                return medicalRecord;
            }
        }
        return null;
    }

    //get all methods
    public List <Patient> getAllPatients() {
        return patients;
    }

    public List <Doctor> getAllDoctors() {
        return doctors;
    }

    public List <Appointment> getAllAppointments() {
        return appointments;
    }

    public List <MedicalRecord> getAllMedicalRecords() {
        return medicalRecords;
    }
}
