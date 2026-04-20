package com.rithika.clinicsystem.ui;

import com.rithika.clinicsystem.model.MedicalRecord;
import com.rithika.clinicsystem.service.ClinicService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MedicalRecordUI {

    private final ClinicService clinicService;

    public MedicalRecordUI(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    public void show() {
        Stage stage = new Stage();

        Label titleLabel = new Label("Medical Record Management");
        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        TextField recordIdField = new TextField();
        recordIdField.setPromptText("Enter Record ID");

        TextField patientIdField = new TextField();
        patientIdField.setPromptText("Enter Patient ID");

        TextField diagnosisField = new TextField();
        diagnosisField.setPromptText("Enter Diagnosis");

        TextField costField = new TextField();
        costField.setPromptText("Enter Treatment Cost");

        Button addButton = new Button("Add Medical Record");
        Button viewButton = new Button("View Records");

        addButton.setPrefWidth(220);
        viewButton.setPrefWidth(220);

        String buttonStyle =
                "-fx-background-color: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 1.5;";

        addButton.setStyle(buttonStyle);
        viewButton.setStyle(buttonStyle);

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(250);
        outputArea.setStyle(
                "-fx-control-inner-background: #111111;" +
                        "-fx-text-fill: white;"
        );

        addButton.setOnAction(e -> {

            String recordId = recordIdField.getText().trim();
            String patientId = patientIdField.getText().trim();
            String diagnosis = diagnosisField.getText().trim();
            String costText = costField.getText().trim();

            if (recordId.isEmpty() || patientId.isEmpty() || diagnosis.isEmpty() || costText.isEmpty()) {
                outputArea.setText("Please fill in all fields.");
                return;
            }

            if (clinicService.findMedicalRecordById(recordId) != null) {
                outputArea.setText("Record ID already exists.");
                return;
            }

            if (clinicService.findPatienById(patientId) == null) {
                outputArea.setText("Patient not found.");
                return;
            }

            double cost;
            try {
                cost = Double.parseDouble(costText);
            } catch (NumberFormatException ex) {
                outputArea.setText("Cost must be a valid number.");
                return;
            }

            MedicalRecord record = new MedicalRecord(
                    recordId,
                    patientId,
                    diagnosis,
                    cost
            );

            clinicService.addMedicalRecord(record);

            outputArea.setText("Medical record added successfully.");

            recordIdField.clear();
            patientIdField.clear();
            diagnosisField.clear();
            costField.clear();
        });

        viewButton.setOnAction(e -> {

            if (clinicService.getAllMedicalRecords().isEmpty()) {
                outputArea.setText("No records found.");
                return;
            }

            StringBuilder builder = new StringBuilder();

            for (MedicalRecord record : clinicService.getAllMedicalRecords()) {
                builder.append("Record ID: ").append(record.getRecordId()).append("\n");
                builder.append("Patient ID: ").append(record.getPatientId()).append("\n");
                builder.append("Diagnosis: ").append(record.getDiagnosis()).append("\n");
                builder.append("Treatment Cost: Rs. ").append(record.getTreatmentCost()).append("\n");
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
        recordIdField.setMaxWidth(fieldWidth);
        diagnosisField.setMaxWidth(fieldWidth);
        costField.setMaxWidth(fieldWidth);

        layout.getChildren().addAll(
                titleLabel,
                recordIdField,
                patientIdField,
                diagnosisField,
                costField,
                addButton,
                viewButton,
                outputArea
        );

        Scene scene = new Scene(layout, 550, 700);
        stage.setTitle("Medical Record Management");
        stage.setScene(scene);
        stage.show();
    }
}