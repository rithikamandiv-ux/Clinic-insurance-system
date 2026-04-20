package com.rithika.clinicsystem.ui;

import com.rithika.clinicsystem.model.Doctor;
import com.rithika.clinicsystem.service.ClinicService;
import com.rithika.clinicsystem.util.FileUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DoctorUI {

    private final ClinicService clinicService;

    public DoctorUI(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    public void show() {
        Stage stage = new Stage();

        Label titleLabel = new Label("Doctor Management");
        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        TextField doctorIdField = new TextField();
        doctorIdField.setPromptText("Enter Doctor ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter Doctor Name");

        TextField specializationField = new TextField();
        specializationField.setPromptText("Enter Specialization");

        TextField feeField = new TextField();
        feeField.setPromptText("Enter Consultation Fee");

        Button addButton = new Button("Add Doctor");
        Button viewButton = new Button("View Doctors");

        addButton.setPrefWidth(200);
        viewButton.setPrefWidth(200);

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
        outputArea.setPrefHeight(220);
        outputArea.setStyle(
                "-fx-control-inner-background: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;"
        );

        addButton.setOnAction(e -> {
            String doctorId = doctorIdField.getText().trim();
            String name = nameField.getText().trim();
            String specialization = specializationField.getText().trim();
            String feeText = feeField.getText().trim();

            if (doctorId.isEmpty() || name.isEmpty() || specialization.isEmpty() || feeText.isEmpty()) {
                outputArea.setText("Please fill in all fields.");
                return;
            }

            double consultationFee;
            try {
                consultationFee = Double.parseDouble(feeText);
            } catch (NumberFormatException ex) {
                outputArea.setText("Consultation fee must be a valid number.");
                return;
            }

            if (clinicService.findDoctorById(doctorId) != null) {
                outputArea.setText("Doctor ID already exists.");
                return;
            }

            Doctor doctor = new Doctor(doctorId, name, specialization, consultationFee);
            clinicService.addDoctor(doctor);
            FileUtil.saveDoctors(clinicService.getAllDoctors());

            outputArea.setText("Doctor added successfully.");

            doctorIdField.clear();
            nameField.clear();
            specializationField.clear();
            feeField.clear();
        });

        viewButton.setOnAction(e -> {
            if (clinicService.getAllDoctors().isEmpty()) {
                outputArea.setText("No doctors found.");
                return;
            }

            StringBuilder builder = new StringBuilder();

            for (Doctor doctor : clinicService.getAllDoctors()) {
                builder.append("Doctor ID: ").append(doctor.getDoctorId()).append("\n");
                builder.append("Name: ").append(doctor.getDoctorName()).append("\n");
                builder.append("Specialization: ").append(doctor.getSpecialization()).append("\n");
                builder.append("Consultation Fee: ").append(doctor.getConsultationFee()).append("\n");
                builder.append("-----------------------------\n");
            }

            outputArea.setText(builder.toString());
        });

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #664C36;");
        double fieldWidth = 300;

        doctorIdField.setMaxWidth(fieldWidth);
        nameField.setMaxWidth(fieldWidth);
        specializationField.setMaxWidth(fieldWidth);
        feeField.setMaxWidth(fieldWidth);

        layout.getChildren().addAll(
                titleLabel,
                doctorIdField,
                nameField,
                specializationField,
                feeField,
                addButton,
                viewButton,
                outputArea
        );

        Scene scene = new Scene(layout, 500, 620);
        stage.setTitle("Doctor Management");
        stage.setScene(scene);
        stage.show();
    }
}