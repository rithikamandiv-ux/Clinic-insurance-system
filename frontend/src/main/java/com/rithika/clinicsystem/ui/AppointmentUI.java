package com.rithika.clinicsystem.ui;

import com.rithika.clinicsystem.api.ApiException;
import com.rithika.clinicsystem.api.AppointmentApiService;
import com.rithika.clinicsystem.dto.AppointmentRequest;
import com.rithika.clinicsystem.dto.AppointmentResponse;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AppointmentUI {

    private final AppointmentApiService appointmentApiService;

    public AppointmentUI(
            AppointmentApiService appointmentApiService
    ) {
        this.appointmentApiService = appointmentApiService;
    }

    public void show() {

        Stage stage = new Stage();

        Label titleLabel =
                new Label("Appointment Management");

        titleLabel.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 24px;" +
                        "-fx-font-weight: bold;"
        );

        TextField appointmentIdField =
                new TextField();

        appointmentIdField.setPromptText(
                "Enter Appointment ID"
        );

        TextField patientIdField =
                new TextField();

        patientIdField.setPromptText(
                "Enter Patient ID"
        );

        TextField doctorIdField =
                new TextField();

        doctorIdField.setPromptText(
                "Enter Doctor ID"
        );

        TextField dateField =
                new TextField();

        dateField.setPromptText(
                "Enter Date (YYYY-MM-DD)"
        );

        Button addButton =
                new Button("Book Appointment");

        Button viewButton =
                new Button("View Appointments");

        Button completeButton =
                new Button("Complete Appointment");

        Button cancelButton =
                new Button("Cancel Appointment");

        addButton.setPrefWidth(220);
        viewButton.setPrefWidth(220);
        completeButton.setPrefWidth(220);
        cancelButton.setPrefWidth(220);

        String buttonStyle =
                "-fx-background-color: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 1.5;";

        addButton.setStyle(buttonStyle);
        viewButton.setStyle(buttonStyle);
        completeButton.setStyle(buttonStyle);
        cancelButton.setStyle(buttonStyle);

        TextArea outputArea =
                new TextArea();

        outputArea.setEditable(false);
        outputArea.setPrefHeight(230);

        outputArea.setStyle(
                "-fx-control-inner-background: #111111;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 13px;"
        );

        /*
         * BOOK APPOINTMENT
         */
        addButton.setOnAction(event -> {

            String appointmentId =
                    appointmentIdField
                            .getText()
                            .trim();

            String patientId =
                    patientIdField
                            .getText()
                            .trim();

            String doctorId =
                    doctorIdField
                            .getText()
                            .trim();

            String dateText =
                    dateField
                            .getText()
                            .trim();

            if (
                    appointmentId.isEmpty()
                            || patientId.isEmpty()
                            || doctorId.isEmpty()
                            || dateText.isEmpty()
            ) {

                outputArea.setText(
                        "Please fill in all fields."
                );

                return;
            }

            LocalDate appointmentDate;

            try {

                appointmentDate =
                        LocalDate.parse(dateText);

            } catch (DateTimeParseException exception) {

                outputArea.setText(
                        "Date must use the format YYYY-MM-DD."
                );

                return;
            }

            AppointmentRequest request =
                    new AppointmentRequest(
                            appointmentId,
                            patientId,
                            doctorId,
                            appointmentDate
                    );

            try {

                AppointmentResponse response =
                        appointmentApiService
                                .addAppointment(request);

                outputArea.setText(
                        "Appointment booked successfully.\n\n" +
                                formatAppointment(response)
                );

                appointmentIdField.clear();
                patientIdField.clear();
                doctorIdField.clear();
                dateField.clear();

            } catch (ApiException exception) {

                outputArea.setText(
                        "Unable to book appointment.\n\n" +
                                exception.getMessage()
                );
            }
        });

        /*
         * VIEW APPOINTMENTS
         */
        viewButton.setOnAction(event -> {

            try {

                List<AppointmentResponse> appointments =
                        appointmentApiService
                                .getAllAppointments();

                if (appointments.isEmpty()) {

                    outputArea.setText(
                            "No appointments found."
                    );

                    return;
                }

                StringBuilder builder =
                        new StringBuilder();

                for (AppointmentResponse appointment :
                        appointments) {

                    builder
                            .append(
                                    formatAppointment(
                                            appointment
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
                        "Unable to load appointments.\n\n" +
                                exception.getMessage()
                );
            }
        });

        /*
         * COMPLETE APPOINTMENT
         *
         * For these lifecycle actions we only need
         * the Appointment ID.
         */
        completeButton.setOnAction(event -> {

            String appointmentId =
                    appointmentIdField
                            .getText()
                            .trim();

            if (appointmentId.isEmpty()) {

                outputArea.setText(
                        "Enter the Appointment ID to complete."
                );

                return;
            }

            try {

                AppointmentResponse response =
                        appointmentApiService
                                .completeAppointment(
                                        appointmentId
                                );

                outputArea.setText(
                        "Appointment completed successfully.\n\n" +
                                formatAppointment(response)
                );

                appointmentIdField.clear();

            } catch (ApiException exception) {

                outputArea.setText(
                        "Unable to complete appointment.\n\n" +
                                exception.getMessage()
                );
            }
        });

        /*
         * CANCEL APPOINTMENT
         */
        cancelButton.setOnAction(event -> {

            String appointmentId =
                    appointmentIdField
                            .getText()
                            .trim();

            if (appointmentId.isEmpty()) {

                outputArea.setText(
                        "Enter the Appointment ID to cancel."
                );

                return;
            }

            try {

                AppointmentResponse response =
                        appointmentApiService
                                .cancelAppointment(
                                        appointmentId
                                );

                outputArea.setText(
                        "Appointment cancelled successfully.\n\n" +
                                formatAppointment(response)
                );

                appointmentIdField.clear();

            } catch (ApiException exception) {

                outputArea.setText(
                        "Unable to cancel appointment.\n\n" +
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

        appointmentIdField
                .setMaxWidth(fieldWidth);

        patientIdField
                .setMaxWidth(fieldWidth);

        doctorIdField
                .setMaxWidth(fieldWidth);

        dateField
                .setMaxWidth(fieldWidth);

        layout.getChildren().addAll(
                titleLabel,
                appointmentIdField,
                patientIdField,
                doctorIdField,
                dateField,
                addButton,
                viewButton,
                completeButton,
                cancelButton,
                outputArea
        );

        Scene scene =
                new Scene(
                        layout,
                        520,
                        760
                );

        stage.setTitle(
                "Appointment Management"
        );

        stage.setScene(scene);

        stage.show();
    }

    private String formatAppointment(
            AppointmentResponse appointment
    ) {

        return "Appointment ID: "
                + appointment.getAppointmentId()
                + "\n"
                + "Patient ID: "
                + appointment.getPatientId()
                + "\n"
                + "Doctor ID: "
                + appointment.getDoctorId()
                + "\n"
                + "Date: "
                + appointment.getAppointmentDate()
                + "\n"
                + "Status: "
                + appointment.getStatus()
                + "\n";
    }
}