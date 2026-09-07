package com.rithika.clinicsystem.ui.layout;

import com.rithika.clinicsystem.ui.dashboard.DashboardView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

import com.rithika.clinicsystem.api.PatientApiService;
import com.rithika.clinicsystem.ui.patient.PatientView;
import com.rithika.clinicsystem.api.DoctorApiService;
import com.rithika.clinicsystem.ui.doctor.DoctorView;
import com.rithika.clinicsystem.api.AppointmentApiService;
import com.rithika.clinicsystem.ui.appointment.AppointmentView;
import com.rithika.clinicsystem.api.MedicalRecordApiService;
import com.rithika.clinicsystem.ui.medicalrecord.MedicalRecordView;
import com.rithika.clinicsystem.api.InsurancePolicyApiService;
import com.rithika.clinicsystem.ui.policy.InsurancePolicyView;
import com.rithika.clinicsystem.api.InsuranceClaimApiService;
import com.rithika.clinicsystem.ui.claim.InsuranceClaimView;

public class MainLayout {

    private final BorderPane root;
    private final StackPane contentArea;

    private final PatientApiService patientApiService;
    private final DoctorApiService doctorApiService;
    private final AppointmentApiService appointmentApiService;
    private final MedicalRecordApiService medicalRecordApiService;
    private final InsurancePolicyApiService insurancePolicyApiService;
    private final InsuranceClaimApiService insuranceClaimApiService;

    private final List<Button> navigationButtons;

    public MainLayout(
            PatientApiService patientApiService,
            DoctorApiService doctorApiService,
            AppointmentApiService appointmentApiService,
            MedicalRecordApiService medicalRecordApiService,
            InsurancePolicyApiService insurancePolicyApiService,
            InsuranceClaimApiService insuranceClaimApiService
    ) {

        this.patientApiService =
                patientApiService;

        this.doctorApiService =
                doctorApiService;

        this.appointmentApiService =
                appointmentApiService;

        this.medicalRecordApiService =
                medicalRecordApiService;

        this.insurancePolicyApiService =
                insurancePolicyApiService;

        this.insuranceClaimApiService =
                insuranceClaimApiService;

        root =
                new BorderPane();

        root
                .getStyleClass()
                .add("app-root");


        contentArea =
                new StackPane();

        contentArea
                .getStyleClass()
                .add("content-area");


        navigationButtons =
                new ArrayList<>();


        root.setTop(
                createTopNavigation()
        );

        root.setCenter(
                contentArea
        );


        showDashboard();
    }

    private void showPatients() {

        PatientView patientView =
                new PatientView(
                        patientApiService
                );

        setContent(
                patientView.getView()
        );
    }

    private void showDoctors() {

        DoctorView doctorView =
                new DoctorView(
                        doctorApiService
                );

        setContent(
                doctorView.getView()
        );
    }

    private void showAppointments() {

        AppointmentView appointmentView =
                new AppointmentView(
                        appointmentApiService,
                        patientApiService,
                        doctorApiService
                );

        setContent(
                appointmentView.getView()
        );
    }

    private void showMedicalRecords() {

        MedicalRecordView medicalRecordView =
                new MedicalRecordView(
                        medicalRecordApiService,
                        appointmentApiService
                );

        setContent(
                medicalRecordView.getView()
        );
    }

    private void showPolicies() {

        InsurancePolicyView insurancePolicyView =
                new InsurancePolicyView(
                        insurancePolicyApiService,
                        patientApiService
                );

        setContent(
                insurancePolicyView.getView()
        );
    }

    private void showClaims() {

        InsuranceClaimView insuranceClaimView =
                new InsuranceClaimView(
                        insuranceClaimApiService,
                        medicalRecordApiService
                );

        setContent(
                insuranceClaimView.getView()
        );
    }


    private HBox createTopNavigation() {

        HBox navigationBar =
                new HBox(8);

        navigationBar
                .getStyleClass()
                .add("top-navigation");

        navigationBar.setAlignment(
                Pos.CENTER_LEFT
        );

        navigationBar.setPadding(
                new Insets(
                        0,
                        24,
                        0,
                        24
                )
        );


        /*
         * BRAND
         */

        Label brandLabel =
                new Label(
                        "CareNexus"
                );

        brandLabel
                .getStyleClass()
                .add("top-navigation-brand");


        /*
         * SPACING AFTER BRAND
         */

        Region brandSpacer =
                new Region();

        brandSpacer.setPrefWidth(24);


        /*
         * NAVIGATION BUTTONS
         */

        Button dashboardButton =
                createNavigationButton(
                        "Dashboard"
                );

        Button patientsButton =
                createNavigationButton(
                        "Patients"
                );

        Button doctorsButton =
                createNavigationButton(
                        "Doctors"
                );

        Button appointmentsButton =
                createNavigationButton(
                        "Appointments"
                );

        Button recordsButton =
                createNavigationButton(
                        "Medical Records"
                );

        Button policiesButton =
                createNavigationButton(
                        "Policies"
                );

        Button claimsButton =
                createNavigationButton(
                        "Claims"
                );


        /*
         * RIGHT SIDE SPACER
         */

        Region rightSpacer =
                new Region();

        HBox.setHgrow(
                rightSpacer,
                Priority.ALWAYS
        );


        /*
         * TEMPORARY SYSTEM STATUS
         */

        Label statusLabel =
                new Label(
                        "API Connected"
                );

        statusLabel
                .getStyleClass()
                .add("api-status");


        /*
         * NAVIGATION EVENTS
         */

        dashboardButton.setOnAction(event -> {

            setActiveButton(
                    dashboardButton
            );

            showDashboard();
        });


        patientsButton.setOnAction(event -> {

            setActiveButton(
                    patientsButton
            );

            showPatients();
        });


        doctorsButton.setOnAction(event -> {

            setActiveButton(
                    doctorsButton
            );

            showDoctors();
        });


        appointmentsButton.setOnAction(event -> {

            setActiveButton(
                    appointmentsButton
            );

            showAppointments();
        });


        recordsButton.setOnAction(event -> {

            setActiveButton(
                    recordsButton
            );

            showMedicalRecords();
        });


        policiesButton.setOnAction(event -> {

            setActiveButton(
                    policiesButton
            );

            showPolicies();
        });


        claimsButton.setOnAction(event -> {

            setActiveButton(
                    claimsButton
            );

            showClaims();
        });


        navigationBar
                .getChildren()
                .addAll(
                        brandLabel,
                        brandSpacer,
                        dashboardButton,
                        patientsButton,
                        doctorsButton,
                        appointmentsButton,
                        recordsButton,
                        policiesButton,
                        claimsButton,
                        rightSpacer,
                        statusLabel
                );


        setActiveButton(
                dashboardButton
        );


        return navigationBar;
    }


    private Button createNavigationButton(
            String text
    ) {

        Button button =
                new Button(text);

        button
                .getStyleClass()
                .add("top-nav-button");

        navigationButtons.add(
                button
        );

        return button;
    }


    private void setActiveButton(
            Button activeButton
    ) {

        for (
                Button button
                : navigationButtons
        ) {

            button
                    .getStyleClass()
                    .remove(
                            "top-nav-button-active"
                    );
        }


        if (
                !activeButton
                        .getStyleClass()
                        .contains(
                                "top-nav-button-active"
                        )
        ) {

            activeButton
                    .getStyleClass()
                    .add(
                            "top-nav-button-active"
                    );
        }
    }


    private void showDashboard() {

        DashboardView dashboardView =
                new DashboardView();

        setContent(
                dashboardView.getView()
        );
    }


    private void showTemporaryPage(
            String title,
            String message
    ) {

        Label titleLabel =
                new Label(title);

        titleLabel
                .getStyleClass()
                .add("page-title");


        Label messageLabel =
                new Label(message);

        messageLabel
                .getStyleClass()
                .add("secondary-text");


        HBox heading =
                new HBox();

        heading.getChildren().add(
                titleLabel
        );


        javafx.scene.layout.VBox page =
                new javafx.scene.layout.VBox(
                        12
                );

        page.setPadding(
                new Insets(32)
        );

        page
                .getStyleClass()
                .add("page-content");

        page.getChildren().addAll(
                heading,
                messageLabel
        );


        setContent(page);
    }


    public void setContent(
            Node node
    ) {

        contentArea
                .getChildren()
                .setAll(node);
    }


    public BorderPane getRoot() {
        return root;
    }
}