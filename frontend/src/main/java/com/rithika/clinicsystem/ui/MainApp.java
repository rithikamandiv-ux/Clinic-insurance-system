package com.rithika.clinicsystem.ui;

import com.rithika.clinicsystem.api.ApiClient;
import com.rithika.clinicsystem.api.PatientApiService;
import com.rithika.clinicsystem.ui.layout.MainLayout;
import com.rithika.clinicsystem.api.DoctorApiService;
import com.rithika.clinicsystem.api.AppointmentApiService;
import com.rithika.clinicsystem.api.MedicalRecordApiService;
import com.rithika.clinicsystem.api.InsurancePolicyApiService;
import com.rithika.clinicsystem.api.InsuranceClaimApiService;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(
            Stage primaryStage
    ) {

        /*
         * SHARED API CLIENT
         */

        ApiClient apiClient =
                new ApiClient();


        /*
         * PATIENT API SERVICE
         */

        PatientApiService patientApiService =
                new PatientApiService(
                        apiClient
                );

        DoctorApiService doctorApiService =
                new DoctorApiService(
                        apiClient
                );

        AppointmentApiService appointmentApiService =
                new AppointmentApiService(
                        apiClient
                );

        MedicalRecordApiService medicalRecordApiService =
                new MedicalRecordApiService(
                        apiClient
                );

        InsurancePolicyApiService insurancePolicyApiService =
                new InsurancePolicyApiService(
                        apiClient
                );

        InsuranceClaimApiService insuranceClaimApiService =
                new InsuranceClaimApiService(
                        apiClient
                );


        /*
         * MAIN APPLICATION LAYOUT
         */

        MainLayout mainLayout =
                new MainLayout(
                        patientApiService,
                        doctorApiService,
                        appointmentApiService,
                        medicalRecordApiService,
                        insurancePolicyApiService,
                        insuranceClaimApiService

                );


        /*
         * SCENE
         */

        Scene scene =
                new Scene(
                        mainLayout.getRoot(),
                        1280,
                        800
                );

        ThemeManager.apply(
                scene
        );


        /*
         * STAGE
         */

        primaryStage.setTitle(
                "CareNexus"
        );

        primaryStage.setMinWidth(
                1100
        );

        primaryStage.setMinHeight(
                700
        );

        primaryStage.setScene(
                scene
        );

        primaryStage.show();
    }


    public static void main(
            String[] args
    ) {

        launch(args);
    }
}