package com.rithika.clinicsystem.ui;

import com.rithika.clinicsystem.model.InsuranceClaim;
import com.rithika.clinicsystem.model.MedicalRecord;
import com.rithika.clinicsystem.service.ClinicService;
import com.rithika.clinicsystem.service.InsuranceService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClaimUI {

    private final ClinicService clinicService;
    private final InsuranceService insuranceService;

    public ClaimUI(ClinicService clinicService, InsuranceService insuranceService) {
        this.clinicService = clinicService;
        this.insuranceService = insuranceService;
    }

    public void show() {
        Stage stage = new Stage();

        Label titleLabel = new Label("Claim Management");
        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        TextField claimIdField = new TextField();
        claimIdField.setPromptText("Enter Claim ID");

        TextField recordIdField = new TextField();
        recordIdField.setPromptText("Enter Medical Record ID");

        TextField processClaimIdField = new TextField();
        processClaimIdField.setPromptText("Enter Claim ID to Process");

        Button createClaimButton = new Button("Create Claim");
        Button processClaimButton = new Button("Process Claim");
        Button viewClaimsButton = new Button("View Claims");

        createClaimButton.setPrefWidth(220);
        processClaimButton.setPrefWidth(220);
        viewClaimsButton.setPrefWidth(220);

        String buttonStyle =
                "-fx-background-color: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 1.5;";

        createClaimButton.setStyle(buttonStyle);
        processClaimButton.setStyle(buttonStyle);
        viewClaimsButton.setStyle(buttonStyle);

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(250);
        outputArea.setStyle(
                "-fx-control-inner-background: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;"
        );

        createClaimButton.setOnAction(e -> {
            String claimId = claimIdField.getText().trim();
            String recordId = recordIdField.getText().trim();

            if (claimId.isEmpty() || recordId.isEmpty()) {
                outputArea.setText("Please fill in all required fields.");
                return;
            }

            if (insuranceService.findClaimById(claimId) != null) {
                outputArea.setText("Claim ID already exists.");
                return;
            }

            MedicalRecord record = clinicService.findMedicalRecordById(recordId);

            if (record == null) {
                outputArea.setText("Medical record not found.");
                return;
            }

            if (!insuranceService.hasPolicy(record.getPatientId())) {
                outputArea.setText("This patient does not have an insurance policy.");
                return;
            }

            InsuranceClaim claim = insuranceService.createClaimFromRecord(claimId, record);

            if (claim != null) {
                outputArea.setText(
                        "Claim created successfully.\n" +
                                "Claim ID: " + claim.getClaimId() + "\n" +
                                "Patient ID: " + claim.getPatientId() + "\n" +
                                "Record ID: " + claim.getRecordId() + "\n" +
                                "Claim Amount: Rs. " + claim.getClaimAmount() + "\n" +
                                "Status: " + claim.getClaimStatus()
                );
            }

            claimIdField.clear();
            recordIdField.clear();
        });

        processClaimButton.setOnAction(e -> {
            String claimId = processClaimIdField.getText().trim();

            if (claimId.isEmpty()) {
                outputArea.setText("Please enter a claim ID to process.");
                return;
            }

            InsuranceClaim claim = insuranceService.findClaimById(claimId);

            if (claim == null) {
                outputArea.setText("Claim not found.");
                return;
            }

            insuranceService.processClaim(claimId);

            outputArea.setText(
                    "Claim processed successfully.\n" +
                            "Claim ID: " + claim.getClaimId() + "\n" +
                            "Updated Status: " + claim.getClaimStatus()
            );

            processClaimIdField.clear();
        });

        viewClaimsButton.setOnAction(e -> {
            if (insuranceService.getClaims().isEmpty()) {
                outputArea.setText("No claims found.");
                return;
            }

            StringBuilder builder = new StringBuilder();

            for (InsuranceClaim claim : insuranceService.getClaims()) {
                builder.append("Claim ID: ").append(claim.getClaimId()).append("\n");
                builder.append("Patient ID: ").append(claim.getPatientId()).append("\n");
                builder.append("Record ID: ").append(claim.getRecordId()).append("\n");
                builder.append("Claim Amount: Rs. ").append(claim.getClaimAmount()).append("\n");
                builder.append("Claim Status: ").append(claim.getClaimStatus()).append("\n");
                builder.append("-----------------------------\n");
            }

            outputArea.setText(builder.toString());
        });

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #664C36;");

        double fieldWidth = 300;
        claimIdField.setMaxWidth(fieldWidth);
        recordIdField.setMaxWidth(fieldWidth);

        layout.getChildren().addAll(
                titleLabel,
                claimIdField,
                recordIdField,
                createClaimButton,
                processClaimIdField,
                processClaimButton,
                viewClaimsButton,
                outputArea
        );

        Scene scene = new Scene(layout, 550, 700);
        stage.setTitle("Claim Management");
        stage.setScene(scene);
        stage.show();
    }
}