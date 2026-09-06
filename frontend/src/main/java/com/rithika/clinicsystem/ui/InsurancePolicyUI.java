package com.rithika.clinicsystem.ui;

import com.rithika.clinicsystem.api.ApiException;
import com.rithika.clinicsystem.api.InsurancePolicyApiService;
import com.rithika.clinicsystem.dto.InsurancePolicyRequest;
import com.rithika.clinicsystem.dto.InsurancePolicyResponse;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.List;

public class InsurancePolicyUI {

    private final InsurancePolicyApiService insurancePolicyApiService;

    public InsurancePolicyUI(
            InsurancePolicyApiService insurancePolicyApiService
    ) {
        this.insurancePolicyApiService =
                insurancePolicyApiService;
    }

    public void show() {

        Stage stage = new Stage();

        Label titleLabel =
                new Label("Insurance Policy Management");

        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        TextField policyIdField =
                new TextField();

        policyIdField.setPromptText(
                "Enter Policy ID"
        );

        TextField patientIdField =
                new TextField();

        patientIdField.setPromptText(
                "Enter Patient ID"
        );

        TextField providerNameField =
                new TextField();

        providerNameField.setPromptText(
                "Enter Insurance Provider"
        );

        TextField coverageAmountField =
                new TextField();

        coverageAmountField.setPromptText(
                "Enter Coverage Amount"
        );

        TextField policyTypeField =
                new TextField();

        policyTypeField.setPromptText(
                "Enter Policy Type"
        );

        Button addButton =
                new Button("Add Policy");

        Button viewButton =
                new Button("View Policies");

        Button updateButton =
                new Button("Update Policy");

        Button deleteButton =
                new Button("Delete Policy");

        addButton.setPrefWidth(220);
        viewButton.setPrefWidth(220);
        updateButton.setPrefWidth(220);
        deleteButton.setPrefWidth(220);

        String buttonStyle =
                "-fx-background-color: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 1.5;";

        addButton.setStyle(buttonStyle);
        viewButton.setStyle(buttonStyle);
        updateButton.setStyle(buttonStyle);
        deleteButton.setStyle(buttonStyle);

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
         * ADD POLICY
         */
        addButton.setOnAction(event -> {

            InsurancePolicyRequest request =
                    buildRequest(
                            policyIdField,
                            patientIdField,
                            providerNameField,
                            coverageAmountField,
                            policyTypeField,
                            outputArea
                    );

            if (request == null) {
                return;
            }

            try {

                InsurancePolicyResponse response =
                        insurancePolicyApiService
                                .addPolicy(request);

                outputArea.setText(
                        "Insurance policy added successfully.\n\n"
                                + formatPolicy(response)
                );

                clearFields(
                        policyIdField,
                        patientIdField,
                        providerNameField,
                        coverageAmountField,
                        policyTypeField
                );

            } catch (ApiException exception) {

                outputArea.setText(
                        "Unable to add insurance policy.\n\n"
                                + exception.getMessage()
                );
            }
        });

        /*
         * VIEW POLICIES
         */
        viewButton.setOnAction(event -> {

            try {

                List<InsurancePolicyResponse> policies =
                        insurancePolicyApiService
                                .getAllPolicies();

                if (policies.isEmpty()) {

                    outputArea.setText(
                            "No insurance policies found."
                    );

                    return;
                }

                StringBuilder builder =
                        new StringBuilder();

                for (
                        InsurancePolicyResponse policy
                        : policies
                ) {

                    builder
                            .append(
                                    formatPolicy(policy)
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
                        "Unable to load insurance policies.\n\n"
                                + exception.getMessage()
                );
            }
        });

        /*
         * UPDATE POLICY
         */
        updateButton.setOnAction(event -> {

            String policyId =
                    policyIdField
                            .getText()
                            .trim();

            if (policyId.isEmpty()) {

                outputArea.setText(
                        "Policy ID is required for update."
                );

                return;
            }

            InsurancePolicyRequest request =
                    buildRequest(
                            policyIdField,
                            patientIdField,
                            providerNameField,
                            coverageAmountField,
                            policyTypeField,
                            outputArea
                    );

            if (request == null) {
                return;
            }

            try {

                InsurancePolicyResponse response =
                        insurancePolicyApiService
                                .updatePolicy(
                                        policyId,
                                        request
                                );

                outputArea.setText(
                        "Insurance policy updated successfully.\n\n"
                                + formatPolicy(response)
                );

                clearFields(
                        policyIdField,
                        patientIdField,
                        providerNameField,
                        coverageAmountField,
                        policyTypeField
                );

            } catch (ApiException exception) {

                outputArea.setText(
                        "Unable to update insurance policy.\n\n"
                                + exception.getMessage()
                );
            }
        });

        /*
         * DELETE POLICY
         */
        deleteButton.setOnAction(event -> {

            String policyId =
                    policyIdField
                            .getText()
                            .trim();

            if (policyId.isEmpty()) {

                outputArea.setText(
                        "Enter the Policy ID to delete."
                );

                return;
            }

            try {

                insurancePolicyApiService
                        .deletePolicy(policyId);

                outputArea.setText(
                        "Insurance policy deleted successfully.\n\n"
                                + "Policy ID: "
                                + policyId
                );

                clearFields(
                        policyIdField,
                        patientIdField,
                        providerNameField,
                        coverageAmountField,
                        policyTypeField
                );

            } catch (ApiException exception) {

                outputArea.setText(
                        "Unable to delete insurance policy.\n\n"
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

        policyIdField.setMaxWidth(fieldWidth);
        patientIdField.setMaxWidth(fieldWidth);
        providerNameField.setMaxWidth(fieldWidth);
        coverageAmountField.setMaxWidth(fieldWidth);
        policyTypeField.setMaxWidth(fieldWidth);

        layout.getChildren().addAll(
                titleLabel,
                policyIdField,
                patientIdField,
                providerNameField,
                coverageAmountField,
                policyTypeField,
                addButton,
                viewButton,
                updateButton,
                deleteButton,
                outputArea
        );

        Scene scene =
                new Scene(
                        layout,
                        560,
                        780
                );

        stage.setTitle(
                "Insurance Policy Management"
        );

        stage.setScene(scene);
        stage.show();
    }

    private InsurancePolicyRequest buildRequest(
            TextField policyIdField,
            TextField patientIdField,
            TextField providerNameField,
            TextField coverageAmountField,
            TextField policyTypeField,
            TextArea outputArea
    ) {

        String policyId =
                policyIdField
                        .getText()
                        .trim();

        String patientId =
                patientIdField
                        .getText()
                        .trim();

        String providerName =
                providerNameField
                        .getText()
                        .trim();

        String coverageAmountText =
                coverageAmountField
                        .getText()
                        .trim();

        String policyType =
                policyTypeField
                        .getText()
                        .trim();

        if (
                policyId.isEmpty()
                        || patientId.isEmpty()
                        || providerName.isEmpty()
                        || coverageAmountText.isEmpty()
                        || policyType.isEmpty()
        ) {

            outputArea.setText(
                    "Please fill in all fields."
            );

            return null;
        }

        BigDecimal coverageAmount;

        try {

            coverageAmount =
                    new BigDecimal(
                            coverageAmountText
                    );

        } catch (NumberFormatException exception) {

            outputArea.setText(
                    "Coverage amount must be a valid number."
            );

            return null;
        }

        if (
                coverageAmount.compareTo(
                        BigDecimal.ZERO
                ) <= 0
        ) {

            outputArea.setText(
                    "Coverage amount must be greater than zero."
            );

            return null;
        }

        return new InsurancePolicyRequest(
                policyId,
                patientId,
                providerName,
                coverageAmount,
                policyType
        );
    }

    private String formatPolicy(
            InsurancePolicyResponse policy
    ) {

        return "Policy ID: "
                + policy.getPolicyId()
                + "\n"
                + "Patient ID: "
                + policy.getPatientId()
                + "\n"
                + "Provider: "
                + policy.getProviderName()
                + "\n"
                + "Coverage Amount: Rs. "
                + policy.getCoverageAmount()
                + "\n"
                + "Policy Type: "
                + policy.getPolicyType()
                + "\n";
    }

    private void clearFields(
            TextField policyIdField,
            TextField patientIdField,
            TextField providerNameField,
            TextField coverageAmountField,
            TextField policyTypeField
    ) {

        policyIdField.clear();
        patientIdField.clear();
        providerNameField.clear();
        coverageAmountField.clear();
        policyTypeField.clear();
    }
}