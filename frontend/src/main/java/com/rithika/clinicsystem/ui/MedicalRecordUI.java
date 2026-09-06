package com.rithika.clinicsystem.ui;

import com.rithika.clinicsystem.api.ApiException;
import com.rithika.clinicsystem.api.MedicalRecordApiService;
import com.rithika.clinicsystem.dto.MedicalRecordRequest;
import com.rithika.clinicsystem.dto.MedicalRecordResponse;
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

public class MedicalRecordUI {

    private final MedicalRecordApiService medicalRecordApiService;

    public MedicalRecordUI(
            MedicalRecordApiService medicalRecordApiService
    ) {
        this.medicalRecordApiService = medicalRecordApiService;
    }

    public void show() {

        Stage stage = new Stage();

        Label titleLabel =
                new Label("Medical Record Management");

        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        TextField recordIdField =
                new TextField();

        recordIdField.setPromptText(
                "Enter Record ID"
        );

        TextField appointmentIdField =
                new TextField();

        appointmentIdField.setPromptText(
                "Enter Appointment ID"
        );

        TextField diagnosisField =
                new TextField();

        diagnosisField.setPromptText(
                "Enter Diagnosis"
        );

        TextField treatmentField =
                new TextField();

        treatmentField.setPromptText(
                "Enter Treatment"
        );

        TextField costField =
                new TextField();

        costField.setPromptText(
                "Enter Treatment Cost"
        );

        Button addButton =
                new Button("Add Medical Record");

        Button viewButton =
                new Button("View Records");

        addButton.setPrefWidth(220);
        viewButton.setPrefWidth(220);

        String buttonStyle =
                "-fx-background-color: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 1.5;";

        addButton.setStyle(buttonStyle);
        viewButton.setStyle(buttonStyle);

        TextArea outputArea =
                new TextArea();

        outputArea.setEditable(false);
        outputArea.setPrefHeight(260);

        outputArea.setStyle(
                "-fx-control-inner-background: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;"
        );

        /*
         * ADD MEDICAL RECORD
         */
        addButton.setOnAction(event -> {

            String recordId =
                    recordIdField
                            .getText()
                            .trim();

            String appointmentId =
                    appointmentIdField
                            .getText()
                            .trim();

            String diagnosis =
                    diagnosisField
                            .getText()
                            .trim();

            String treatment =
                    treatmentField
                            .getText()
                            .trim();

            String costText =
                    costField
                            .getText()
                            .trim();

            if (
                    recordId.isEmpty()
                            || appointmentId.isEmpty()
                            || diagnosis.isEmpty()
                            || treatment.isEmpty()
                            || costText.isEmpty()
            ) {

                outputArea.setText(
                        "Please fill in all fields."
                );

                return;
            }

            BigDecimal treatmentCost;

            try {

                treatmentCost =
                        new BigDecimal(costText);

            } catch (NumberFormatException exception) {

                outputArea.setText(
                        "Treatment cost must be a valid number."
                );

                return;
            }

            if (
                    treatmentCost.compareTo(
                            BigDecimal.ZERO
                    ) < 0
            ) {

                outputArea.setText(
                        "Treatment cost cannot be negative."
                );

                return;
            }

            MedicalRecordRequest request =
                    new MedicalRecordRequest(
                            recordId,
                            appointmentId,
                            diagnosis,
                            treatment,
                            treatmentCost
                    );

            try {

                MedicalRecordResponse response =
                        medicalRecordApiService
                                .addMedicalRecord(
                                        request
                                );

                outputArea.setText(
                        "Medical record added successfully.\n\n" +
                                formatMedicalRecord(response)
                );

                recordIdField.clear();
                appointmentIdField.clear();
                diagnosisField.clear();
                treatmentField.clear();
                costField.clear();

            } catch (ApiException exception) {

                outputArea.setText(
                        "Unable to add medical record.\n\n" +
                                exception.getMessage()
                );
            }
        });

        /*
         * VIEW MEDICAL RECORDS
         */
        viewButton.setOnAction(event -> {

            try {

                List<MedicalRecordResponse> records =
                        medicalRecordApiService
                                .getAllMedicalRecords();

                if (records.isEmpty()) {

                    outputArea.setText(
                            "No medical records found."
                    );

                    return;
                }

                StringBuilder builder =
                        new StringBuilder();

                for (
                        MedicalRecordResponse record
                        : records
                ) {

                    builder
                            .append(
                                    formatMedicalRecord(
                                            record
                                    )
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
                        "Unable to load medical records.\n\n" +
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

        recordIdField
                .setMaxWidth(fieldWidth);

        appointmentIdField
                .setMaxWidth(fieldWidth);

        diagnosisField
                .setMaxWidth(fieldWidth);

        treatmentField
                .setMaxWidth(fieldWidth);

        costField
                .setMaxWidth(fieldWidth);

        layout.getChildren().addAll(
                titleLabel,
                recordIdField,
                appointmentIdField,
                diagnosisField,
                treatmentField,
                costField,
                addButton,
                viewButton,
                outputArea
        );

        Scene scene =
                new Scene(
                        layout,
                        550,
                        730
                );

        stage.setTitle(
                "Medical Record Management"
        );

        stage.setScene(scene);
        stage.show();
    }

    private String formatMedicalRecord(
            MedicalRecordResponse record
    ) {

        return "Record ID: "
                + record.getRecordId()
                + "\n"
                + "Appointment ID: "
                + record.getAppointmentId()
                + "\n"
                + "Patient ID: "
                + record.getPatientId()
                + "\n"
                + "Doctor ID: "
                + record.getDoctorId()
                + "\n"
                + "Diagnosis: "
                + record.getDiagnosis()
                + "\n"
                + "Treatment: "
                + record.getTreatment()
                + "\n"
                + "Treatment Cost: Rs. "
                + record.getTreatmentCost()
                + "\n";
    }
}