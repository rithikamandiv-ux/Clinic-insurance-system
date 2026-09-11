package com.rithika.clinicsystem.ui.dashboard;

import com.rithika.clinicsystem.api.ApiException;
import com.rithika.clinicsystem.api.AppointmentApiService;
import com.rithika.clinicsystem.api.DoctorApiService;
import com.rithika.clinicsystem.api.InsuranceClaimApiService;
import com.rithika.clinicsystem.api.InsurancePolicyApiService;
import com.rithika.clinicsystem.api.MedicalRecordApiService;
import com.rithika.clinicsystem.api.PatientApiService;

import com.rithika.clinicsystem.dto.AppointmentResponse;
import com.rithika.clinicsystem.dto.DoctorResponse;
import com.rithika.clinicsystem.dto.InsuranceClaimResponse;
import com.rithika.clinicsystem.dto.InsurancePolicyResponse;
import com.rithika.clinicsystem.dto.MedicalRecordResponse;
import com.rithika.clinicsystem.dto.PatientResponse;

import com.rithika.clinicsystem.ui.ThemeManager;
import com.rithika.clinicsystem.util.AsyncTaskRunner;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.text.DecimalFormat;

public class DashboardView {

    private static final DecimalFormat CURRENCY_FORMAT =
            new DecimalFormat(
                    "#,##0.00"
            );

    private final PatientApiService patientApiService;
    private final DoctorApiService doctorApiService;
    private final AppointmentApiService appointmentApiService;
    private final MedicalRecordApiService medicalRecordApiService;
    private final InsurancePolicyApiService insurancePolicyApiService;
    private final InsuranceClaimApiService insuranceClaimApiService;

    private final BorderPane root;

    private final BooleanProperty busy;


    /*
     * ----------------------------------------------------
     * SUMMARY VALUES
     * ----------------------------------------------------
     */

    private final Label totalPatientsValue;
    private final Label totalDoctorsValue;
    private final Label totalAppointmentsValue;
    private final Label totalMedicalRecordsValue;
    private final Label totalPoliciesValue;
    private final Label pendingClaimsValue;


    /*
     * ----------------------------------------------------
     * APPOINTMENT VALUES
     * ----------------------------------------------------
     */

    private final Label bookedAppointmentsValue;
    private final Label completedAppointmentsValue;
    private final Label cancelledAppointmentsValue;
    private final Label todayAppointmentsValue;


    /*
     * ----------------------------------------------------
     * CLAIM VALUES
     * ----------------------------------------------------
     */

    private final Label pendingClaimOverviewValue;
    private final Label approvedClaimsValue;
    private final Label rejectedClaimsValue;
    private final Label totalClaimValue;


    public DashboardView(
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


        totalPatientsValue =
                createValueLabel();

        totalDoctorsValue =
                createValueLabel();

        totalAppointmentsValue =
                createValueLabel();

        totalMedicalRecordsValue =
                createValueLabel();

        totalPoliciesValue =
                createValueLabel();

        pendingClaimsValue =
                createValueLabel();


        bookedAppointmentsValue =
                createOverviewValueLabel();

        completedAppointmentsValue =
                createOverviewValueLabel();

        cancelledAppointmentsValue =
                createOverviewValueLabel();

        todayAppointmentsValue =
                createOverviewValueLabel();


        pendingClaimOverviewValue =
                createOverviewValueLabel();

        approvedClaimsValue =
                createOverviewValueLabel();

        rejectedClaimsValue =
                createOverviewValueLabel();

        totalClaimValue =
                createOverviewValueLabel();


        this.busy =
                new SimpleBooleanProperty(false);

        buildView();

        loadDashboardData();
    }


    private void buildView() {

        ProgressIndicator loadingIndicator =
                new ProgressIndicator();

        loadingIndicator.setAccessibleText("Dashboard request in progress");

        loadingIndicator.setPrefSize(18, 18);
        loadingIndicator.setMinSize(18, 18);
        loadingIndicator.setMaxSize(18, 18);
        loadingIndicator.visibleProperty().bind(busy);
        loadingIndicator.managedProperty().bind(busy);


        root
                .getStyleClass()
                .add(
                        "page-content"
                );


        /*
         * ----------------------------------------------------
         * PAGE TITLE
         * ----------------------------------------------------
         */

        Label titleLabel =
                new Label(
                        "Dashboard"
                );

        titleLabel
                .getStyleClass()
                .add(
                        "page-title"
                );


        Label subtitleLabel =
                new Label(
                        "Healthcare Operations & Insurance Management"
                );

        subtitleLabel
                .getStyleClass()
                .add(
                        "secondary-text"
                );


        VBox titleSection =
                new VBox(
                        4,
                        titleLabel,
                        subtitleLabel
                );


        Button refreshButton =
                new Button(
                        "Refresh Dashboard"
                );

        refreshButton
                .getStyleClass()
                .add(
                        "secondary-button"
                );

        refreshButton.setOnAction(
                event ->
                        loadDashboardData()
        );


        refreshButton.disableProperty().bind(busy);

        Region headerSpacer =
                new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );


        HBox header =
                new HBox(
                        20,
                        titleSection,
                        headerSpacer,
                        loadingIndicator,
                        refreshButton
                );

        header.setAlignment(
                Pos.CENTER_LEFT
        );


        /*
         * ----------------------------------------------------
         * SUMMARY SECTION
         * ----------------------------------------------------
         */

        Label summaryTitle =
                new Label(
                        "System Overview"
                );

        summaryTitle
                .getStyleClass()
                .addAll(
                        "section-title",
                        "dashboard-section-title"
                );


        GridPane summaryGrid =
                createSummaryGrid();


        /*
         * ----------------------------------------------------
         * OPERATIONAL OVERVIEW
         * ----------------------------------------------------
         */

        Label operationalTitle =
                new Label(
                        "Operational Overview"
                );

        operationalTitle
                .getStyleClass()
                .addAll(
                        "section-title",
                        "dashboard-section-title"
                );


        HBox overviewSection =
                createOverviewSection();


        /*
         * ----------------------------------------------------
         * PAGE CONTENT
         * ----------------------------------------------------
         */

        VBox content =
                new VBox(
                        24,
                        header,
                        summaryTitle,
                        summaryGrid,
                        operationalTitle,
                        overviewSection
                );

        content.setPadding(
                new Insets(
                        32
                )
        );


        /*
         * ScrollPane keeps the dashboard usable if the
         * application window becomes smaller.
         */

        ScrollPane scrollPane =
                new ScrollPane(
                        content
                );

        scrollPane.setFitToWidth(
                true
        );

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane
                .getStyleClass()
                .add(
                        "dashboard-scroll-pane"
                );


        root.setCenter(
                scrollPane
        );
    }


    private GridPane createSummaryGrid() {

        GridPane grid =
                new GridPane();

        grid.setHgap(
                18
        );

        grid.setVgap(
                18
        );


        /*
         * Three equal columns.
         */

        for (
                int i = 0;
                i < 3;
                i++
        ) {

            ColumnConstraints column =
                    new ColumnConstraints();

            column.setPercentWidth(
                    33.33
            );

            column.setHgrow(
                    Priority.ALWAYS
            );

            grid
                    .getColumnConstraints()
                    .add(
                            column
                    );
        }


        VBox patientCard =
                createSummaryCard(
                        "Total Patients",
                        totalPatientsValue,
                        "Registered patients"
                );


        VBox doctorCard =
                createSummaryCard(
                        "Total Doctors",
                        totalDoctorsValue,
                        "Available doctors"
                );


        VBox appointmentCard =
                createSummaryCard(
                        "Total Appointments",
                        totalAppointmentsValue,
                        "All appointment records"
                );


        VBox medicalRecordCard =
                createSummaryCard(
                        "Medical Records",
                        totalMedicalRecordsValue,
                        "Completed medical records"
                );


        VBox policyCard =
                createSummaryCard(
                        "Insurance Policies",
                        totalPoliciesValue,
                        "Registered patient policies"
                );


        VBox pendingClaimCard =
                createSummaryCard(
                        "Pending Claims",
                        pendingClaimsValue,
                        "Claims awaiting processing"
                );


        grid.add(
                patientCard,
                0,
                0
        );

        grid.add(
                doctorCard,
                1,
                0
        );

        grid.add(
                appointmentCard,
                2,
                0
        );


        grid.add(
                medicalRecordCard,
                0,
                1
        );

        grid.add(
                policyCard,
                1,
                1
        );

        grid.add(
                pendingClaimCard,
                2,
                1
        );


        return grid;
    }


    private VBox createSummaryCard(
            String title,
            Label valueLabel,
            String description
    ) {

        Label titleLabel =
                new Label(
                        title
                );

        titleLabel
                .getStyleClass()
                .add(
                        "dashboard-stat-title"
                );


        Label descriptionLabel =
                new Label(
                        description
                );

        descriptionLabel
                .getStyleClass()
                .add(
                        "dashboard-stat-description"
                );


        VBox card =
                new VBox(
                        8,
                        titleLabel,
                        valueLabel,
                        descriptionLabel
                );


        card
                .getStyleClass()
                .addAll(
                        "card",
                        "dashboard-stat-card"
                );


        card.setMaxWidth(
                Double.MAX_VALUE
        );


        GridPane.setHgrow(
                card,
                Priority.ALWAYS
        );


        return card;
    }


    private HBox createOverviewSection() {

        VBox appointmentCard =
                createAppointmentOverviewCard();


        VBox claimCard =
                createClaimOverviewCard();


        HBox.setHgrow(
                appointmentCard,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                claimCard,
                Priority.ALWAYS
        );


        appointmentCard.setMaxWidth(
                Double.MAX_VALUE
        );

        claimCard.setMaxWidth(
                Double.MAX_VALUE
        );


        HBox section =
                new HBox(
                        18,
                        appointmentCard,
                        claimCard
                );


        return section;
    }


    private VBox createAppointmentOverviewCard() {

        Label title =
                new Label(
                        "Appointment Overview"
                );

        title
                .getStyleClass()
                .add(
                        "section-title"
                );


        VBox card =
                new VBox(
                        14,
                        title,
                        createOverviewRow(
                                "Booked",
                                bookedAppointmentsValue
                        ),
                        createOverviewRow(
                                "Completed",
                                completedAppointmentsValue
                        ),
                        createOverviewRow(
                                "Cancelled",
                                cancelledAppointmentsValue
                        ),
                        createOverviewRow(
                                "Scheduled Today",
                                todayAppointmentsValue
                        )
                );


        card
                .getStyleClass()
                .addAll(
                        "card",
                        "dashboard-overview-card"
                );


        return card;
    }


    private VBox createClaimOverviewCard() {

        Label title =
                new Label(
                        "Insurance Claim Overview"
                );

        title
                .getStyleClass()
                .add(
                        "section-title"
                );


        VBox card =
                new VBox(
                        14,
                        title,
                        createOverviewRow(
                                "Pending",
                                pendingClaimOverviewValue
                        ),
                        createOverviewRow(
                                "Approved",
                                approvedClaimsValue
                        ),
                        createOverviewRow(
                                "Rejected",
                                rejectedClaimsValue
                        ),
                        createOverviewRow(
                                "Total Claim Value",
                                totalClaimValue
                        )
                );


        card
                .getStyleClass()
                .addAll(
                        "card",
                        "dashboard-overview-card"
                );


        return card;
    }


    private HBox createOverviewRow(
            String title,
            Label valueLabel
    ) {

        Label titleLabel =
                new Label(
                        title
                );

        titleLabel
                .getStyleClass()
                .add(
                        "dashboard-overview-label"
                );


        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        HBox row =
                new HBox(
                        12,
                        titleLabel,
                        spacer,
                        valueLabel
                );

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row
                .getStyleClass()
                .add(
                        "dashboard-overview-row"
                );


        return row;
    }


    private Label createValueLabel() {

        Label label =
                new Label(
                        "0"
                );

        label
                .getStyleClass()
                .add(
                        "dashboard-stat-value"
                );

        return label;
    }


    private Label createOverviewValueLabel() {

        Label label =
                new Label(
                        "0"
                );

        label
                .getStyleClass()
                .add(
                        "dashboard-overview-value"
                );

        return label;
    }


    private void loadDashboardData() {

        if (busy.get()) {
            return;
        }

        busy.set(true);

        AsyncTaskRunner.run(
                () -> new DashboardPageData(
                        patientApiService.getAllPatients(),
                        doctorApiService.getAllDoctors(),
                        appointmentApiService.getAllAppointments(),
                        medicalRecordApiService.getAllMedicalRecords(),
                        insurancePolicyApiService.getAllPolicies(),
                        insuranceClaimApiService.getAllClaims()
                ),
                data -> {
                    try {
                        updateDashboard(data);
                    } finally {
                        busy.set(false);
                    }
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Load Dashboard",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Load Dashboard",
                                "An unexpected error occurred while loading dashboard data."
                        );
                    }
                }
        );
    }


    private void updateDashboard(DashboardPageData data) {
        List<PatientResponse> patients = data.patients();
        List<DoctorResponse> doctors = data.doctors();
        List<AppointmentResponse> appointments = data.appointments();
        List<MedicalRecordResponse> medicalRecords = data.medicalRecords();
        List<InsurancePolicyResponse> policies = data.policies();
        List<InsuranceClaimResponse> claims = data.claims();

        /*
         * ------------------------------------------------
         * SUMMARY METRICS
         * ------------------------------------------------
         */

        totalPatientsValue.setText(
                String.valueOf(
                        patients.size()
                )
        );


        totalDoctorsValue.setText(
                String.valueOf(
                        doctors.size()
                )
        );


        totalAppointmentsValue.setText(
                String.valueOf(
                        appointments.size()
                )
        );


        totalMedicalRecordsValue.setText(
                String.valueOf(
                        medicalRecords.size()
                )
        );


        totalPoliciesValue.setText(
                String.valueOf(
                        policies.size()
                )
        );


        /*
         * ------------------------------------------------
         * APPOINTMENT STATUS COUNTS
         * ------------------------------------------------
         */

        long bookedAppointments =
                appointments
                        .stream()
                        .filter(
                                appointment ->
                                        "BOOKED"
                                                .equalsIgnoreCase(
                                                        appointment
                                                                .getStatus()
                                                )
                        )
                        .count();


        long completedAppointments =
                appointments
                        .stream()
                        .filter(
                                appointment ->
                                        "COMPLETED"
                                                .equalsIgnoreCase(
                                                        appointment
                                                                .getStatus()
                                                )
                        )
                        .count();


        long cancelledAppointments =
                appointments
                        .stream()
                        .filter(
                                appointment ->
                                        "CANCELLED"
                                                .equalsIgnoreCase(
                                                        appointment
                                                                .getStatus()
                                                )
                        )
                        .count();


        LocalDate today =
                LocalDate.now();


        long appointmentsToday =
                appointments
                        .stream()
                        .filter(
                                appointment ->
                                        today.equals(
                                                appointment
                                                        .getAppointmentDate()
                                        )
                        )
                        .count();


        bookedAppointmentsValue.setText(
                String.valueOf(
                        bookedAppointments
                )
        );


        completedAppointmentsValue.setText(
                String.valueOf(
                        completedAppointments
                )
        );


        cancelledAppointmentsValue.setText(
                String.valueOf(
                        cancelledAppointments
                )
        );


        todayAppointmentsValue.setText(
                String.valueOf(
                        appointmentsToday
                )
        );


        /*
         * ------------------------------------------------
         * CLAIM STATUS COUNTS
         * ------------------------------------------------
         */

        long pendingClaims =
                claims
                        .stream()
                        .filter(
                                claim ->
                                        "PENDING"
                                                .equalsIgnoreCase(
                                                        claim
                                                                .getStatus()
                                                )
                        )
                        .count();


        long approvedClaims =
                claims
                        .stream()
                        .filter(
                                claim ->
                                        "APPROVED"
                                                .equalsIgnoreCase(
                                                        claim
                                                                .getStatus()
                                                )
                        )
                        .count();


        long rejectedClaims =
                claims
                        .stream()
                        .filter(
                                claim ->
                                        "REJECTED"
                                                .equalsIgnoreCase(
                                                        claim
                                                                .getStatus()
                                                )
                        )
                        .count();


        BigDecimal totalClaims =
                claims
                        .stream()
                        .map(
                                InsuranceClaimResponse::getClaimAmount
                        )
                        .filter(
                                amount ->
                                        amount != null
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );


        pendingClaimsValue.setText(
                String.valueOf(
                        pendingClaims
                )
        );


        pendingClaimOverviewValue.setText(
                String.valueOf(
                        pendingClaims
                )
        );


        approvedClaimsValue.setText(
                String.valueOf(
                        approvedClaims
                )
        );


        rejectedClaimsValue.setText(
                String.valueOf(
                        rejectedClaims
                )
        );


        totalClaimValue.setText(
                "Rs. "
                        + CURRENCY_FORMAT.format(
                        totalClaims
                )
        );
    }


    private void showError(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );


        ThemeManager.apply(
                alert.getDialogPane()
        );


        alert.showAndWait();
    }


    public Parent getView() {

        return root;
    }
    private record DashboardPageData(
            List<PatientResponse> patients,
            List<DoctorResponse> doctors,
            List<AppointmentResponse> appointments,
            List<MedicalRecordResponse> medicalRecords,
            List<InsurancePolicyResponse> policies,
            List<InsuranceClaimResponse> claims
    ) {
    }
}
