package com.rithika.clinicsystem.util;

import com.rithika.clinicsystem.model.Appointment;
import com.rithika.clinicsystem.model.Doctor;
import com.rithika.clinicsystem.model.Patient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    //patient data file saving
    public static void savePatients(List<Patient> patients) {
        File folder = new File("data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/patients.txt"))) {

            for (Patient patient : patients) {
                writer.write(
                        patient.getPatientId() + "," +
                                patient.getPatientName() + "," +
                                patient.getAge() + "," +
                                patient.getPhoneNumber() + "," +
                                patient.InsuranceStatus()
                );
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving patients to file.");
            e.printStackTrace();
        }
    }

    //load patient data
    public static List<Patient> loadPatients() {
        List<Patient> patients = new ArrayList<>();

        File file = new File("data/patients.txt");

        if (!file.exists()) {
            return patients;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String id = parts[0];
                String name = parts[1];
                int age = Integer.parseInt(parts[2]);
                String phone = parts[3];
                boolean hasInsurance = Boolean.parseBoolean(parts[4]);

                Patient patient = new Patient(id, name, age, phone, hasInsurance);
                patients.add(patient);
            }

        } catch (IOException e) {
            System.out.println("Error loading patients.");
            e.printStackTrace();
        }

        return patients;
    }

    //doctor data file saving
    public static void saveDoctors(List<Doctor> doctors) {
        File folder = new File("data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/doctors.txt"))) {

            for (Doctor doctor : doctors) {
                writer.write(
                        doctor.getDoctorId() + "," +
                                doctor.getDoctorName() + "," +
                                doctor.getSpecialization() + "," +
                                doctor.getConsultationFee()
                );
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving doctors to file.");
            e.printStackTrace();
        }
    }

    //load doctor data
    public static List<Doctor> loadDoctors() {
        List<Doctor> doctors = new ArrayList<>();

        File file = new File("data/doctors.txt");

        if (!file.exists()) {
            return doctors;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String id = parts[0];
                String name = parts[1];
                String specialization = parts[2];
                double fee = Double.parseDouble(parts[3]);

                Doctor doctor = new Doctor(id, name, specialization, fee);
                doctors.add(doctor);
            }

        } catch (IOException e) {
            System.out.println("Error loading doctors.");
            e.printStackTrace();
        }

        return doctors;
    }

    //appointment data file saving
    public static void saveAppointments(List<Appointment> appointments) {
        File folder = new File("data");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/appointments.txt"))) {

            for (Appointment appointment : appointments) {
                writer.write(
                        appointment.getAppointmentId() + "," +
                                appointment.getPatientId() + "," +
                                appointment.getDoctorId() + "," +
                                appointment.getDate() + "," +
                                appointment.getStatus()
                );
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving appointments to file.");
            e.printStackTrace();
        }
    }

    //load appointment data
    public static List<Appointment> loadAppointments() {
        List<Appointment> appointments = new ArrayList<>();

        File file = new File("data/appointments.txt");

        if (!file.exists()) {
            return appointments;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String id = parts[0];
                String patientId = parts[1];
                String doctorId = parts[2];
                String date = parts[3];
                String status = parts[4];

                Appointment appointment = new Appointment(id, date, status, doctorId, patientId);
                appointments.add(appointment);
            }

        } catch (IOException e) {
            System.out.println("Error loading appointments.");
            e.printStackTrace();
        }

        return appointments;
    }


}