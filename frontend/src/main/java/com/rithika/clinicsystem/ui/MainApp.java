package com.rithika.clinicsystem.ui;

import com.rithika.clinicsystem.model.Doctor;
import com.rithika.clinicsystem.service.ClinicService;
import com.rithika.clinicsystem.service.InsuranceService;
import com.rithika.clinicsystem.util.FileUtil;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        ClinicService clinicService = new ClinicService();
        InsuranceService insuranceService = new InsuranceService();
        // Load saved data into memory
        clinicService.setPatients(FileUtil.loadPatients());
        clinicService.setDoctors(FileUtil.loadDoctors());
        clinicService.setAppointments(FileUtil.loadAppointments());

        //UI starting point
        Label titleLabel = new Label("Clinic Insurance System");
        titleLabel.setStyle(
                        "-fx-text-fill: #EAEAEA;" +
                        "-fx-font-size: 30px;" +
                        "-fx-font-style: italic;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: 'Georgia';"
        );

        Button patientButton = new Button("Manage Patients");
        patientButton.setPrefWidth(280);
        patientButton.setPrefHeight(45);
        patientButton.setStyle(
                "-fx-background-color: #FFD3AC;" +
                        "-fx-text-fill: #3B2A1A;" +
                        "-fx-border-color: transparent;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );
        patientButton.setOnAction(e -> {
            PatientUI patientUI = new PatientUI(clinicService);
            patientUI.show();
        });

        Button doctorButton = new Button("Manage Doctors");
        doctorButton.setPrefWidth(280);
        doctorButton.setPrefHeight(45);
        doctorButton.setStyle(
                "-fx-background-color: #FFD3AC;" +
                        "-fx-text-fill: #3B2A1A;" +
                        "-fx-border-color: transparent;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );
        doctorButton.setOnAction(actionEvent -> {
            DoctorUI doctorUI = new DoctorUI(clinicService);
            doctorUI.show();
        });


        Button appointmentButton = new Button("Manage Appointments");
        appointmentButton.setPrefWidth(280);
        appointmentButton.setPrefHeight(45);
        appointmentButton.setStyle(
                "-fx-background-color: #FFD3AC;" +
                        "-fx-text-fill: #3B2A1A;" +
                        "-fx-border-color: transparent;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );
        appointmentButton.setOnAction(actionEvent -> {
            AppointmentUI appointmentUI = new AppointmentUI(clinicService);
            appointmentUI.show();
        });


        Button claimButton = new Button("Manage Claims");
        claimButton.setPrefWidth(280);
        claimButton.setPrefHeight(45);
        claimButton.setStyle(
                "-fx-background-color: #FFD3AC;" +
                        "-fx-text-fill: #3B2A1A;" +
                        "-fx-border-color: transparent;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );
        claimButton.setOnAction(actionEvent -> {
            ClaimUI claimUI = new ClaimUI(clinicService, new InsuranceService());
            claimUI.show();
        });


        Button recordButton = new Button("Manage Records");
        recordButton.setPrefWidth(280);
        recordButton.setPrefHeight(45);
        recordButton.setStyle(
                "-fx-background-color: #FFD3AC;" +
                        "-fx-text-fill: #3B2A1A;" +
                        "-fx-border-color: transparent;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );
        recordButton.setOnAction(e -> {
            MedicalRecordUI recordUI = new MedicalRecordUI(clinicService);
            recordUI.show();
        });


        VBox layout = new VBox(28);
        VBox.setMargin(titleLabel, new Insets(0, 0, 40, 0));
        layout.setStyle("-fx-background-color: #331C08;");
        titleLabel.setStyle(
                "-fx-text-fill: #E0E0E0;" +
                        "-fx-font-size: 34px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255,255,255,0.25), 10, 0.5, 0, 0);"
        );
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(
                titleLabel,
                patientButton,
                doctorButton,
                appointmentButton,
                claimButton,
                recordButton
        );

        Scene scene = new Scene(layout, 900, 650);

        primaryStage.setTitle("Clinic Insurance System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}