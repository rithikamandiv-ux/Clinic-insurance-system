package com.rithika.clinicsystem.ui;

import com.rithika.clinicsystem.api.ApiException;
import com.rithika.clinicsystem.api.PatientApiService;
import com.rithika.clinicsystem.dto.PatientRequest;
import com.rithika.clinicsystem.dto.PatientResponse;
import com.rithika.clinicsystem.util.InputValidator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class PatientUI {

    private final PatientApiService patientApiService;

    public PatientUI(
            PatientApiService patientApiService
    ) {
        this.patientApiService = patientApiService;
    }

    public void show() {

        Stage stage = new Stage();

        Label titleLabel =
                new Label("Patient Management");

        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        TextField patientIdField =
                new TextField();

        patientIdField.setPromptText(
                "Enter Patient ID"
        );

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Enter Patient Name"
        );

        TextField ageField =
                new TextField();

        ageField.setPromptText(
                "Enter Age"
        );

        TextField phoneField =
                new TextField();

        phoneField.setPromptText(
                "Enter Phone Number"
        );

        CheckBox insuranceCheckBox =
                new CheckBox("Has Insurance");

        insuranceCheckBox.setStyle(
                "-fx-text-fill: white;"
        );

        Button addButton =
                new Button("Add Patient");

        Button viewButton =
                new Button("View Patients");

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

        TextArea outputArea =
                new TextArea();

        outputArea.setEditable(false);
        outputArea.setPrefHeight(200);

        outputArea.setStyle(
                "-fx-control-inner-background: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;"
        );

        /*
         * ADD PATIENT
         */
        addButton.setOnAction(event -> {

            String patientId =
                    patientIdField
                            .getText()
                            .trim();

            String name =
                    nameField
                            .getText()
                            .trim();

            String ageText =
                    ageField
                            .getText()
                            .trim();

            String phone =
                    phoneField
                            .getText()
                            .trim();

            boolean hasInsurance =
                    insuranceCheckBox
                            .isSelected();

            if (InputValidator.isEmpty(patientId)) {

                outputArea.setText(
                        "Patient ID is required."
                );

                return;
            }

            if (InputValidator.isEmpty(name)) {

                outputArea.setText(
                        "Name is required."
                );

                return;
            }

            if (!InputValidator
                    .isPositiveInteger(ageText)) {

                outputArea.setText(
                        "Age must be a positive number."
                );

                return;
            }

            if (!InputValidator
                    .isValidPhoneNumber(phone)) {

                outputArea.setText(
                        "Phone number must be 10 digits."
                );

                return;
            }

            int age;

            try {

                age = Integer.parseInt(ageText);

            } catch (NumberFormatException exception) {

                outputArea.setText(
                        "Age must be a valid number."
                );

                return;
            }

            PatientRequest request =
                    new PatientRequest(
                            patientId,
                            name,
                            age,
                            phone,
                            hasInsurance
                    );

            try {

                PatientResponse response =
                        patientApiService
                                .addPatient(request);

                outputArea.setText(
                        "Patient added successfully.\n\n" +
                                "Patient ID: "
                                + response.getPatientId()
                                + "\n" +
                                "Name: "
                                + response.getPatientName()
                                + "\n" +
                                "Age: "
                                + response.getAge()
                                + "\n" +
                                "Phone Number: "
                                + response.getPhoneNumber()
                                + "\n" +
                                "Has Insurance: "
                                + response.isInsuranceStatus()
                );

                patientIdField.clear();
                nameField.clear();
                ageField.clear();
                phoneField.clear();

                insuranceCheckBox
                        .setSelected(false);

            } catch (ApiException exception) {

                outputArea.setText(
                        "Unable to add patient.\n\n" +
                                exception.getMessage()
                );
            }
        });

        /*
         * VIEW PATIENTS
         */
        viewButton.setOnAction(event -> {

            try {

                List<PatientResponse> patients =
                        patientApiService
                                .getAllPatients();

                if (patients.isEmpty()) {

                    outputArea.setText(
                            "No patients found."
                    );

                    return;
                }

                StringBuilder builder =
                        new StringBuilder();

                for (PatientResponse patient : patients) {

                    builder
                            .append("Patient ID: ")
                            .append(
                                    patient.getPatientId()
                            )
                            .append("\n");

                    builder
                            .append("Name: ")
                            .append(
                                    patient.getPatientName()
                            )
                            .append("\n");

                    builder
                            .append("Age: ")
                            .append(
                                    patient.getAge()
                            )
                            .append("\n");

                    builder
                            .append("Phone Number: ")
                            .append(
                                    patient.getPhoneNumber()
                            )
                            .append("\n");

                    builder
                            .append("Has Insurance: ")
                            .append(
                                    patient.isInsuranceStatus()
                            )
                            .append("\n");

                    builder.append(
                            "-----------------------------\n"
                    );
                }

                outputArea.setText(
                        builder.toString()
                );

            } catch (ApiException exception) {

                outputArea.setText(
                        "Unable to load patients.\n\n" +
                                exception.getMessage()
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

        patientIdField
                .setMaxWidth(fieldWidth);

        nameField
                .setMaxWidth(fieldWidth);

        ageField
                .setMaxWidth(fieldWidth);

        phoneField
                .setMaxWidth(fieldWidth);

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

        Scene scene =
                new Scene(
                        layout,
                        500,
                        650
                );

        stage.setTitle(
                "Patient Management"
        );

        stage.setScene(scene);

        stage.show();
    }
}