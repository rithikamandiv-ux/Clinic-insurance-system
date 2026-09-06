package com.rithika.clinicsystem.ui;

import com.rithika.clinicsystem.api.ApiException;
import com.rithika.clinicsystem.api.DoctorApiService;
import com.rithika.clinicsystem.dto.DoctorRequest;
import com.rithika.clinicsystem.dto.DoctorResponse;
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

public class DoctorUI {

    private final DoctorApiService doctorApiService;

    public DoctorUI(
            DoctorApiService doctorApiService
    ) {
        this.doctorApiService = doctorApiService;
    }

    public void show() {

        Stage stage = new Stage();

        Label titleLabel =
                new Label("Doctor Management");

        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        TextField doctorIdField =
                new TextField();

        doctorIdField.setPromptText(
                "Enter Doctor ID"
        );

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Enter Doctor Name"
        );

        TextField specializationField =
                new TextField();

        specializationField.setPromptText(
                "Enter Specialization"
        );

        TextField feeField =
                new TextField();

        feeField.setPromptText(
                "Enter Consultation Fee"
        );

        Button addButton =
                new Button("Add Doctor");

        Button viewButton =
                new Button("View Doctors");

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
        outputArea.setPrefHeight(220);

        outputArea.setStyle(
                "-fx-control-inner-background: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;"
        );

        /*
         * ADD DOCTOR
         */
        addButton.setOnAction(event -> {

            String doctorId =
                    doctorIdField
                            .getText()
                            .trim();

            String name =
                    nameField
                            .getText()
                            .trim();

            String specialization =
                    specializationField
                            .getText()
                            .trim();

            String feeText =
                    feeField
                            .getText()
                            .trim();

            if (
                    doctorId.isEmpty()
                            || name.isEmpty()
                            || specialization.isEmpty()
                            || feeText.isEmpty()
            ) {

                outputArea.setText(
                        "Please fill in all fields."
                );

                return;
            }

            BigDecimal consultationFee;

            try {

                consultationFee =
                        new BigDecimal(feeText);

            } catch (NumberFormatException exception) {

                outputArea.setText(
                        "Consultation fee must be a valid number."
                );

                return;
            }

            if (consultationFee.compareTo(BigDecimal.ZERO) < 0) {

                outputArea.setText(
                        "Consultation fee cannot be negative."
                );

                return;
            }

            DoctorRequest request =
                    new DoctorRequest(
                            doctorId,
                            name,
                            specialization,
                            consultationFee
                    );

            try {

                DoctorResponse response =
                        doctorApiService
                                .addDoctor(request);

                outputArea.setText(
                        "Doctor added successfully.\n\n" +
                                "Doctor ID: "
                                + response.getDoctorId()
                                + "\n" +
                                "Name: "
                                + response.getDoctorName()
                                + "\n" +
                                "Specialization: "
                                + response.getSpecialization()
                                + "\n" +
                                "Consultation Fee: "
                                + response.getConsultationFee()
                );

                doctorIdField.clear();
                nameField.clear();
                specializationField.clear();
                feeField.clear();

            } catch (ApiException exception) {

                outputArea.setText(
                        "Unable to add doctor.\n\n" +
                                exception.getMessage()
                );
            }
        });

        /*
         * VIEW DOCTORS
         */
        viewButton.setOnAction(event -> {

            try {

                List<DoctorResponse> doctors =
                        doctorApiService
                                .getAllDoctors();

                if (doctors.isEmpty()) {

                    outputArea.setText(
                            "No doctors found."
                    );

                    return;
                }

                StringBuilder builder =
                        new StringBuilder();

                for (DoctorResponse doctor : doctors) {

                    builder
                            .append("Doctor ID: ")
                            .append(
                                    doctor.getDoctorId()
                            )
                            .append("\n");

                    builder
                            .append("Name: ")
                            .append(
                                    doctor.getDoctorName()
                            )
                            .append("\n");

                    builder
                            .append("Specialization: ")
                            .append(
                                    doctor.getSpecialization()
                            )
                            .append("\n");

                    builder
                            .append("Consultation Fee: ")
                            .append(
                                    doctor.getConsultationFee()
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
                        "Unable to load doctors.\n\n" +
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

        doctorIdField
                .setMaxWidth(fieldWidth);

        nameField
                .setMaxWidth(fieldWidth);

        specializationField
                .setMaxWidth(fieldWidth);

        feeField
                .setMaxWidth(fieldWidth);

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

        Scene scene =
                new Scene(
                        layout,
                        500,
                        620
                );

        stage.setTitle(
                "Doctor Management"
        );

        stage.setScene(scene);

        stage.show();
    }
}