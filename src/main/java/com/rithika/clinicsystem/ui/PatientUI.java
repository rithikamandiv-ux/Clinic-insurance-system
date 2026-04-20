package com.rithika.clinicsystem.ui;

import com.rithika.clinicsystem.model.Patient;
import com.rithika.clinicsystem.service.ClinicService;
import com.rithika.clinicsystem.util.FileUtil;
import com.rithika.clinicsystem.util.InputValidator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PatientUI {

    private final ClinicService clinicService;

    public PatientUI(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    public void show() {
        Stage stage = new Stage();

        Label titleLabel = new Label("Patient Management");
        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Enter Patient ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter Patient Name");

        TextField ageField = new TextField();
        ageField.setPromptText("Enter Age");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter Phone Number");

        CheckBox insuranceCheckBox = new CheckBox("Has Insurance");
        insuranceCheckBox.setStyle("-fx-text-fill: white;");

        Button addButton = new Button("Add Patient");
        Button viewButton = new Button("View Patients");

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
        outputArea.setPrefHeight(200);
        outputArea.setStyle(
                "-fx-control-inner-background: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;"
        );

        addButton.setOnAction(e -> {
            String patientId = patientIdField.getText().trim();
            String name = nameField.getText().trim();
            String ageText = ageField.getText().trim();
            String phone = phoneField.getText().trim();
            boolean hasInsurance = insuranceCheckBox.isSelected();

            if (InputValidator.isEmpty(patientId)) {
                outputArea.setText("Patient ID is required.");
                return;
            }

            if (InputValidator.isEmpty(name)) {
                outputArea.setText("Name is required.");
                return;
            }

            if (!InputValidator.isPositiveInteger(ageText)) {
                outputArea.setText("Age must be a positive number.");
                return;
            }

            if (!InputValidator.isValidPhoneNumber(phone)) {
                outputArea.setText("Phone number must be 10 digits.");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageText);
            } catch (NumberFormatException ex) {
                outputArea.setText("Age must be a valid number.");
                return;
            }

            if (clinicService.findPatienById(patientId) != null) {
                outputArea.setText("Patient ID already exists.");
                return;
            }

            Patient patient = new Patient(patientId, name, age, phone, hasInsurance);
            clinicService.addPatient(patient);
            FileUtil.savePatients(clinicService.getAllPatients());

            outputArea.setText("Patient added successfully.");

            patientIdField.clear();
            nameField.clear();
            ageField.clear();
            phoneField.clear();
            insuranceCheckBox.setSelected(false);
        });

        viewButton.setOnAction(e -> {
            if (clinicService.getAllPatients().isEmpty()) {
                outputArea.setText("No patients found.");
                return;
            }

            StringBuilder builder = new StringBuilder();

            for (Patient patient : clinicService.getAllPatients()) {
                builder.append("Patient ID: ").append(patient.getPatientId()).append("\n");
                builder.append("Name: ").append(patient.getPatientName()).append("\n");
                builder.append("Age: ").append(patient.getAge()).append("\n");
                builder.append("Phone Number: ").append(patient.getPhoneNumber()).append("\n");
                builder.append("Has Insurance: ").append(patient.InsuranceStatus()).append("\n");
                builder.append("-----------------------------\n");
            }

            outputArea.setText(builder.toString());
        });

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #664C36;");
        double fieldWidth = 300;

        patientIdField.setMaxWidth(fieldWidth);
        nameField.setMaxWidth(fieldWidth);
        ageField.setMaxWidth(fieldWidth);
        phoneField.setMaxWidth(fieldWidth);

        layout.getChildren().addAll(
                titleLabel,
                patientIdField,
                nameField,
                ageField,
                phoneField,
                insuranceCheckBox,
                addButton,
                viewButton,
                outputArea
        );

        Scene scene = new Scene(layout, 500, 650);
        stage.setTitle("Patient Management");
        stage.setScene(scene);
        stage.show();
    }

}