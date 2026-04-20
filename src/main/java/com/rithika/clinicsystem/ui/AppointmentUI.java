package com.rithika.clinicsystem.ui;

import com.rithika.clinicsystem.model.Appointment;
import com.rithika.clinicsystem.service.ClinicService;
import com.rithika.clinicsystem.util.FileUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppointmentUI {

    private final ClinicService clinicService;

    public AppointmentUI(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    public void show() {
        Stage stage = new Stage();

        Label titleLabel = new Label("Appointment Management");
        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        TextField appointmentIdField = new TextField();
        appointmentIdField.setPromptText("Enter Appointment ID");

        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Enter Patient ID");

        TextField doctorIdField = new TextField();
        doctorIdField.setPromptText("Enter Doctor ID");

        TextField dateField = new TextField();
        dateField.setPromptText("Enter Date (YYYY-MM-DD)");

        Button addButton = new Button("Book Appointment");
        Button viewButton = new Button("View Appointments");

        addButton.setPrefWidth(220);
        viewButton.setPrefWidth(220);

        addButton.setStyle(
                "-fx-background-color: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 1.5;"
        );

        viewButton.setStyle(
                "-fx-background-color: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 1.5;"
        );

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(230);
        outputArea.setStyle(
                "-fx-control-inner-background: #111111;" +
                        "-fx-text-fill: white;"
        );

        addButton.setOnAction(e -> {

            String appointmentId = appointmentIdField.getText().trim();
            String patientId = patientIdField.getText().trim();
            String doctorId = doctorIdField.getText().trim();
            String date = dateField.getText().trim();

            if (appointmentId.isEmpty() || patientId.isEmpty() || doctorId.isEmpty() || date.isEmpty()) {
                outputArea.setText("Please fill in all fields.");
                return;
            }

            if (clinicService.findAppointmentById(appointmentId) != null) {
                outputArea.setText("Appointment ID already exists.");
                return;
            }

            if (clinicService.findPatienById(patientId) == null) {
                outputArea.setText("Patient not found.");
                return;
            }

            if (clinicService.findDoctorById(doctorId) == null) {
                outputArea.setText("Doctor not found.");
                return;
            }

            Appointment appointment = new Appointment(
                    appointmentId,
                    date,
                    Appointment.STATUS_BOOKED,
                    doctorId,
                    patientId
            );

            clinicService.addAppointment(appointment);
            FileUtil.saveAppointments(clinicService.getAllAppointments());

            outputArea.setText("Appointment booked successfully.");

            appointmentIdField.clear();
            patientIdField.clear();
            doctorIdField.clear();
            dateField.clear();
        });

        viewButton.setOnAction(e -> {

            if (clinicService.getAllAppointments().isEmpty()) {
                outputArea.setText("No appointments found.");
                return;
            }

            StringBuilder builder = new StringBuilder();

            for (Appointment appointment : clinicService.getAllAppointments()) {
                builder.append("Appointment ID: ").append(appointment.getAppointmentId()).append("\n");
                builder.append("Patient ID: ").append(appointment.getPatientId()).append("\n");
                builder.append("Doctor ID: ").append(appointment.getDoctorId()).append("\n");
                builder.append("Date: ").append(appointment.getDate()).append("\n");
                builder.append("Status: ").append(appointment.getStatus()).append("\n");
                builder.append("-----------------------------\n");
            }

            outputArea.setText(builder.toString());
        });

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #664C36;");

        double fieldWidth = 300;
        appointmentIdField.setMaxWidth(fieldWidth);
        patientIdField.setMaxWidth(fieldWidth);
        doctorIdField.setMaxWidth(fieldWidth);
        dateField.setMaxWidth(fieldWidth);

        layout.getChildren().addAll(
                titleLabel,
                appointmentIdField,
                patientIdField,
                doctorIdField,
                dateField,
                addButton,
                viewButton,
                outputArea
        );

        Scene scene = new Scene(layout, 520, 650);
        stage.setTitle("Appointment Management");
        stage.setScene(scene);
        stage.show();
    }
}