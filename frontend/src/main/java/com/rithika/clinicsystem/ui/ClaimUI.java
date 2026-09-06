package com.rithika.clinicsystem.ui;

import com.rithika.clinicsystem.api.ApiException;
import com.rithika.clinicsystem.api.InsuranceClaimApiService;
import com.rithika.clinicsystem.dto.InsuranceClaimRequest;
import com.rithika.clinicsystem.dto.InsuranceClaimResponse;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ClaimUI {

    private final InsuranceClaimApiService insuranceClaimApiService;

    public ClaimUI(
            InsuranceClaimApiService insuranceClaimApiService
    ) {
        this.insuranceClaimApiService =
                insuranceClaimApiService;
    }

    public void show() {

        Stage stage = new Stage();

        Label titleLabel =
                new Label("Claim Management");

        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        TextField claimIdField =
                new TextField();

        claimIdField.setPromptText(
                "Enter Claim ID"
        );

        TextField medicalRecordIdField =
                new TextField();

        medicalRecordIdField.setPromptText(
                "Enter Medical Record ID"
        );

        TextField processClaimIdField =
                new TextField();

        processClaimIdField.setPromptText(
                "Enter Claim ID to Process"
        );

        Button createClaimButton =
                new Button("Create Claim");

        Button processClaimButton =
                new Button("Process Claim");

        Button viewClaimsButton =
                new Button("View Claims");

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

        TextArea outputArea =
                new TextArea();

        outputArea.setEditable(false);
        outputArea.setPrefHeight(270);

        outputArea.setStyle(
                "-fx-control-inner-background: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;"
        );

        /*
         * CREATE CLAIM
         */
        createClaimButton.setOnAction(event -> {

            String claimId =
                    claimIdField
                            .getText()
                            .trim();

            String medicalRecordId =
                    medicalRecordIdField
                            .getText()
                            .trim();

            if (
                    claimId.isEmpty()
                            || medicalRecordId.isEmpty()
            ) {

                outputArea.setText(
                        "Please fill in all required fields."
                );

                return;
            }

            InsuranceClaimRequest request =
                    new InsuranceClaimRequest(
                            claimId,
                            medicalRecordId
                    );

            try {

                InsuranceClaimResponse response =
                        insuranceClaimApiService
                                .createClaim(request);

                outputArea.setText(
                        "Claim created successfully.\n\n"
                                + formatClaim(response)
                );

                claimIdField.clear();
                medicalRecordIdField.clear();

            } catch (ApiException exception) {

                outputArea.setText(
                        "Unable to create claim.\n\n"
                                + exception.getMessage()
                );
            }
        });

        /*
         * PROCESS CLAIM
         */
        processClaimButton.setOnAction(event -> {

            String claimId =
                    processClaimIdField
                            .getText()
                            .trim();

            if (claimId.isEmpty()) {

                outputArea.setText(
                        "Please enter a Claim ID to process."
                );

                return;
            }

            try {

                InsuranceClaimResponse response =
                        insuranceClaimApiService
                                .processClaim(
                                        claimId
                                );

                outputArea.setText(
                        "Claim processed successfully.\n\n"
                                + formatClaim(response)
                );

                processClaimIdField.clear();

            } catch (ApiException exception) {

                outputArea.setText(
                        "Unable to process claim.\n\n"
                                + exception.getMessage()
                );
            }
        });

        /*
         * VIEW CLAIMS
         */
        viewClaimsButton.setOnAction(event -> {

            try {

                List<InsuranceClaimResponse> claims =
                        insuranceClaimApiService
                                .getAllClaims();

                if (claims.isEmpty()) {

                    outputArea.setText(
                            "No insurance claims found."
                    );

                    return;
                }

                StringBuilder builder =
                        new StringBuilder();

                for (
                        InsuranceClaimResponse claim
                        : claims
                ) {

                    builder
                            .append(
                                    formatClaim(claim)
                            )
                            .append(
                                    "-----------------------------\n"
                            );
                }

                outputArea.setText(
                        builder.toString()
                );

            } catch (ApiException exception) {

                outputArea.setText(
                        "Unable to load claims.\n\n"
                                + exception.getMessage()
                );
            }
        });

        VBox layout =
                new VBox(15);

        layout.setPadding(
                new Insets(20)
        );

        layout.setAlignment(
                Pos.CENTER
        );

        layout.setStyle(
                "-fx-background-color: #664C36;"
        );

        double fieldWidth = 300;

        claimIdField.setMaxWidth(fieldWidth);
        medicalRecordIdField.setMaxWidth(fieldWidth);
        processClaimIdField.setMaxWidth(fieldWidth);

        layout.getChildren().addAll(
                titleLabel,
                claimIdField,
                medicalRecordIdField,
                createClaimButton,
                processClaimIdField,
                processClaimButton,
                viewClaimsButton,
                outputArea
        );

        Scene scene =
                new Scene(
                        layout,
                        550,
                        700
                );

        stage.setTitle(
                "Claim Management"
        );

        stage.setScene(scene);
        stage.show();
    }

    private String formatClaim(
            InsuranceClaimResponse claim
    ) {

        return "Claim ID: "
                + claim.getClaimId()
                + "\n"
                + "Medical Record ID: "
                + claim.getMedicalRecordId()
                + "\n"
                + "Patient ID: "
                + claim.getPatientId()
                + "\n"
                + "Claim Amount: Rs. "
                + claim.getClaimAmount()
                + "\n"
                + "Status: "
                + claim.getStatus()
                + "\n";
    }
}