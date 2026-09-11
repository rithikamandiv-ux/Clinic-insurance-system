package com.rithika.clinicsystem.ui.appointment;

import com.rithika.clinicsystem.api.ApiException;
import com.rithika.clinicsystem.api.AppointmentApiService;
import com.rithika.clinicsystem.api.DoctorApiService;
import com.rithika.clinicsystem.api.PatientApiService;
import com.rithika.clinicsystem.dto.AppointmentRequest;
import com.rithika.clinicsystem.dto.AppointmentResponse;
import com.rithika.clinicsystem.dto.DoctorResponse;
import com.rithika.clinicsystem.dto.PatientResponse;
import com.rithika.clinicsystem.ui.ThemeManager;
import com.rithika.clinicsystem.util.AsyncTaskRunner;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import com.rithika.clinicsystem.util.InputValidator;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AppointmentView {

    private final AppointmentApiService appointmentApiService;
    private final PatientApiService patientApiService;
    private final DoctorApiService doctorApiService;

    private final BorderPane root;

    private final BooleanProperty busy;

    private final TableView<AppointmentResponse> appointmentTable;

    private final ObservableList<AppointmentResponse> appointments;
    private final FilteredList<AppointmentResponse> filteredAppointments;

    private final ObservableList<PatientResponse> patientOptions;
    private final ObservableList<DoctorResponse> doctorOptions;

    private Button editButton;
    private Button completeButton;
    private Button cancelButton;
    private Button deleteButton;


    public AppointmentView(
            AppointmentApiService appointmentApiService,
            PatientApiService patientApiService,
            DoctorApiService doctorApiService
    ) {

        this.appointmentApiService =
                appointmentApiService;

        this.patientApiService =
                patientApiService;

        this.doctorApiService =
                doctorApiService;

        this.root =
                new BorderPane();

        this.appointmentTable =
                new TableView<>();

        this.appointments =
                FXCollections.observableArrayList();

        this.filteredAppointments =
                new FilteredList<>(
                        appointments,
                        appointment -> true
                );

        this.patientOptions =
                FXCollections.observableArrayList();

        this.doctorOptions =
                FXCollections.observableArrayList();

        this.busy =
                new SimpleBooleanProperty(false);

        buildView();
        loadAppointments();
    }


    private void buildView() {

        ProgressIndicator loadingIndicator =
                new ProgressIndicator();

        loadingIndicator.setAccessibleText("Appointment request in progress");

        loadingIndicator.setPrefSize(18, 18);
        loadingIndicator.setMinSize(18, 18);
        loadingIndicator.setMaxSize(18, 18);
        loadingIndicator.visibleProperty().bind(busy);
        loadingIndicator.managedProperty().bind(busy);


        root
                .getStyleClass()
                .add("page-content");

        root.setPadding(
                new Insets(32)
        );


        /*
         * ----------------------------------------------------
         * HEADER
         * ----------------------------------------------------
         */

        Label titleLabel =
                new Label("Appointments");

        titleLabel
                .getStyleClass()
                .add("page-title");


        Label subtitleLabel =
                new Label(
                        "Manage clinic appointments and appointment lifecycle"
                );

        subtitleLabel
                .getStyleClass()
                .add("secondary-text");


        VBox titleSection =
                new VBox(
                        4,
                        titleLabel,
                        subtitleLabel
                );


        Button addAppointmentButton =
                new Button("+ Book Appointment");

        addAppointmentButton
                .getStyleClass()
                .add("accent-button");

        addAppointmentButton.setOnAction(
                event -> showAddAppointmentDialog()
        );


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
                        addAppointmentButton
                );

        header.setAlignment(
                Pos.CENTER_LEFT
        );


        /*
         * ----------------------------------------------------
         * SEARCH
         * ----------------------------------------------------
         */

        TextField searchField =
                new TextField();

        searchField.setAccessibleText("Search appointments");

        searchField.setPromptText(
                "Search by appointment ID, patient, doctor or status"
        );

        searchField
                .getStyleClass()
                .add("search-field");

        searchField.setMaxWidth(
                460
        );


        searchField
                .textProperty()
                .addListener(
                        (
                                observable,
                                oldValue,
                                newValue
                        ) -> filterAppointments(newValue)
                );


        /*
         * ----------------------------------------------------
         * TABLE
         * ----------------------------------------------------
         */

        configureTable();

        appointmentTable.setItems(
                filteredAppointments
        );

        appointmentTable.setPlaceholder(
                new Label(
                        "No appointments found"
                )
        );


        /*
         * ----------------------------------------------------
         * ACTION BUTTONS
         * ----------------------------------------------------
         */

        Button refreshButton =
                new Button("Refresh");

        refreshButton
                .getStyleClass()
                .add("secondary-button");

        refreshButton.setOnAction(event -> {
            loadAppointments();
        });


        editButton =
                new Button("Edit");

        editButton
                .getStyleClass()
                .add("primary-button");


        editButton.setOnAction(
                event -> editSelectedAppointment()
        );


        completeButton =
                new Button("Complete");

        completeButton
                .getStyleClass()
                .add("success-button");


        completeButton.setOnAction(
                event -> completeSelectedAppointment()
        );


        cancelButton =
                new Button("Cancel");

        cancelButton
                .getStyleClass()
                .add("warning-button");


        cancelButton.setOnAction(
                event -> cancelSelectedAppointment()
        );


        deleteButton =
                new Button("Delete");

        deleteButton
                .getStyleClass()
                .add("danger-button");


        deleteButton.setOnAction(
                event -> deleteSelectedAppointment()
        );


        addAppointmentButton.disableProperty().bind(busy);
        refreshButton.disableProperty().bind(busy);
        editButton.disableProperty().bind(
                busy.or(appointmentTable.getSelectionModel().selectedItemProperty().isNull())
        );
        deleteButton.disableProperty().bind(
                busy.or(appointmentTable.getSelectionModel().selectedItemProperty().isNull())
        );
        completeButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> {
                            var selection = appointmentTable.getSelectionModel().getSelectedItem();
                            return busy.get() || selection == null
                                    || !"BOOKED".equalsIgnoreCase(selection.getStatus());
                        },
                        busy,
                        appointmentTable.getSelectionModel().selectedItemProperty()
                )
        );
        cancelButton.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> {
                            var selection = appointmentTable.getSelectionModel().getSelectedItem();
                            return busy.get() || selection == null
                                    || !"BOOKED".equalsIgnoreCase(selection.getStatus());
                        },
                        busy,
                        appointmentTable.getSelectionModel().selectedItemProperty()
                )
        );

        Region actionSpacer =
                new Region();

        HBox.setHgrow(
                actionSpacer,
                Priority.ALWAYS
        );


        HBox actions =
                new HBox(
                        10,
                        refreshButton,
                        loadingIndicator,
                        actionSpacer,
                        editButton,
                        completeButton,
                        cancelButton,
                        deleteButton
                );

        actions.setAlignment(
                Pos.CENTER_LEFT
        );


        /*
         * ----------------------------------------------------
         * MAIN CONTENT
         * ----------------------------------------------------
         */

        VBox content =
                new VBox(
                        20,
                        header,
                        searchField,
                        appointmentTable,
                        actions
                );

        VBox.setVgrow(
                appointmentTable,
                Priority.ALWAYS
        );


        root.setCenter(
                content
        );
    }


    private void configureTable() {

        appointmentTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );


        /*
         * APPOINTMENT ID
         */

        TableColumn<AppointmentResponse, String>
                appointmentIdColumn =
                new TableColumn<>(
                        "Appointment ID"
                );

        appointmentIdColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getAppointmentId()
                        )
        );


        /*
         * PATIENT
         */

        TableColumn<AppointmentResponse, String>
                patientColumn =
                new TableColumn<>(
                        "Patient"
                );

        patientColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                getPatientDisplay(
                                        cellData
                                                .getValue()
                                                .getPatientId()
                                )
                        )
        );


        /*
         * DOCTOR
         */

        TableColumn<AppointmentResponse, String>
                doctorColumn =
                new TableColumn<>(
                        "Doctor"
                );

        doctorColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                getDoctorDisplay(
                                        cellData
                                                .getValue()
                                                .getDoctorId()
                                )
                        )
        );


        /*
         * DATE
         */

        TableColumn<AppointmentResponse, LocalDate>
                dateColumn =
                new TableColumn<>(
                        "Date"
                );

        dateColumn.setCellValueFactory(
                cellData ->
                        new SimpleObjectProperty<>(
                                cellData
                                        .getValue()
                                        .getAppointmentDate()
                        )
        );


        /*
         * STATUS
         */

        TableColumn<AppointmentResponse, String>
                statusColumn =
                new TableColumn<>(
                        "Status"
                );

        statusColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getStatus()
                        )
        );


        statusColumn.setCellFactory(
                column ->
                        new TableCell<>() {

                            @Override
                            protected void updateItem(
                                    String status,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        status,
                                        empty
                                );

                                if (
                                        empty
                                                || status == null
                                ) {

                                    setText(null);
                                    setGraphic(null);

                                    return;
                                }


                                Label badge =
                                        new Label(status);

                                badge
                                        .getStyleClass()
                                        .add(
                                                "status-label"
                                        );


                                switch (
                                        status.toUpperCase()
                                ) {

                                    case "COMPLETED" ->
                                            badge
                                                    .getStyleClass()
                                                    .add(
                                                            "status-success"
                                                    );

                                    case "CANCELLED" ->
                                            badge
                                                    .getStyleClass()
                                                    .add(
                                                            "status-danger"
                                                    );

                                    default ->
                                            badge
                                                    .getStyleClass()
                                                    .add(
                                                            "status-info"
                                                    );
                                }


                                setText(null);
                                setGraphic(badge);
                                setAlignment(
                                        Pos.CENTER_LEFT
                                );
                            }
                        }
        );


        appointmentTable
                .getColumns()
                .addAll(
                        appointmentIdColumn,
                        patientColumn,
                        doctorColumn,
                        dateColumn,
                        statusColumn
                );
    }


    private void loadAppointments() {

        if (busy.get()) {
            return;
        }

        busy.set(true);

        AsyncTaskRunner.run(
                () -> new AppointmentPageData(
                        appointmentApiService.getAllAppointments(),
                        patientApiService.getAllPatients(),
                        doctorApiService.getAllDoctors()
                ),
                data -> {
                    try {
                        appointments.setAll(data.appointments());
                        patientOptions.setAll(data.patientOptions());
                        doctorOptions.setAll(data.doctorOptions());
                        appointmentTable.getSelectionModel().clearSelection();
                    } finally {
                        busy.set(false);
                    }
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Load Appointments",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Load Appointments",
                                "An unexpected error occurred while loading appointments."
                        );
                    }
                }
        );
    }


    private void filterAppointments(
            String searchText
    ) {

        String search =
                searchText == null
                        ? ""
                        : searchText
                        .trim()
                        .toLowerCase();


        if (search.isEmpty()) {

            filteredAppointments.setPredicate(
                    appointment -> true
            );

            return;
        }


        filteredAppointments.setPredicate(
                appointment -> {

                    String appointmentId =
                            appointment
                                    .getAppointmentId()
                                    .toLowerCase();

                    String patient =
                            getPatientDisplay(
                                    appointment
                                            .getPatientId()
                            )
                                    .toLowerCase();

                    String doctor =
                            getDoctorDisplay(
                                    appointment
                                            .getDoctorId()
                            )
                                    .toLowerCase();

                    String status =
                            appointment
                                    .getStatus()
                                    .toLowerCase();

                    String date =
                            appointment
                                    .getAppointmentDate()
                                    .toString()
                                    .toLowerCase();


                    return appointmentId.contains(search)
                            || patient.contains(search)
                            || doctor.contains(search)
                            || status.contains(search)
                            || date.contains(search);
                }
        );
    }


    private void showAddAppointmentDialog() {

        if (busy.get()) {
            return;
        }

        AppointmentFormResult formResult =
                showAppointmentForm(
                        "Book Appointment",
                        null
                );


        if (formResult == null) {
            return;
        }


        AppointmentRequest request =
                new AppointmentRequest(
                        formResult.appointmentId(),
                        formResult
                                .patient()
                                .getPatientId(),
                        formResult
                                .doctor()
                                .getDoctorId(),
                        formResult.appointmentDate()
                );


        busy.set(true);

        AsyncTaskRunner.run(
                () -> appointmentApiService.addAppointment(request),
                response -> {
                    try {
                        appointments.add(response);
                    } finally {
                        busy.set(false);
                    }

                    showInformation(
                            "Appointment Booked",
                            "Appointment was booked successfully."
                    );
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Book Appointment",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Book Appointment",
                                "An unexpected error occurred while adding the appointment."
                        );
                    }
                }
        );
    }


    private void editSelectedAppointment() {

        if (busy.get()) {
            return;
        }

        AppointmentResponse selectedAppointment =
                appointmentTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedAppointment == null) {
            return;
        }


        AppointmentFormResult formResult =
                showAppointmentForm(
                        "Edit Appointment",
                        selectedAppointment
                );


        if (formResult == null) {
            return;
        }


        AppointmentRequest request =
                new AppointmentRequest(
                        selectedAppointment
                                .getAppointmentId(),
                        formResult
                                .patient()
                                .getPatientId(),
                        formResult
                                .doctor()
                                .getDoctorId(),
                        formResult.appointmentDate()
                );


        busy.set(true);

        AsyncTaskRunner.run(
                () -> appointmentApiService.updateAppointment(selectedAppointment.getAppointmentId(), request),
                response -> {
                    try {
                        int index = appointments.indexOf(selectedAppointment);
                        if (index >= 0) {
                            appointments.set(index, response);
                        }
                    } finally {
                        busy.set(false);
                    }

                    showInformation(
                            "Appointment Updated",
                            "Appointment was updated successfully."
                    );
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Update Appointment",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Update Appointment",
                                "An unexpected error occurred while updating the appointment."
                        );
                    }
                }
        );
    }


    private void completeSelectedAppointment() {

        if (busy.get()) {
            return;
        }

        AppointmentResponse selectedAppointment =
                appointmentTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedAppointment == null) {
            return;
        }


        if (
                !showConfirmation(
                        "Complete Appointment",
                        "Complete appointment "
                                + selectedAppointment
                                .getAppointmentId()
                                + "?",
                        "A completed appointment cannot return to BOOKED."
                )
        ) {

            return;
        }


        busy.set(true);

        AsyncTaskRunner.run(
                () -> appointmentApiService.completeAppointment(selectedAppointment.getAppointmentId()),
                response -> {
                    try {
                        int index = appointments.indexOf(selectedAppointment);
                        if (index >= 0) {
                            appointments.set(index, response);
                        }
                    } finally {
                        busy.set(false);
                    }

                    showInformation(
                            "Appointment Completed",
                            "Appointment was marked as completed."
                    );
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Complete Appointment",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Complete Appointment",
                                "An unexpected error occurred while completing the appointment."
                        );
                    }
                }
        );
    }


    private void cancelSelectedAppointment() {

        if (busy.get()) {
            return;
        }

        AppointmentResponse selectedAppointment =
                appointmentTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedAppointment == null) {
            return;
        }


        if (
                !showConfirmation(
                        "Cancel Appointment",
                        "Cancel appointment "
                                + selectedAppointment
                                .getAppointmentId()
                                + "?",
                        "A cancelled appointment cannot return to BOOKED."
                )
        ) {

            return;
        }


        busy.set(true);

        AsyncTaskRunner.run(
                () -> appointmentApiService.cancelAppointment(selectedAppointment.getAppointmentId()),
                response -> {
                    try {
                        int index = appointments.indexOf(selectedAppointment);
                        if (index >= 0) {
                            appointments.set(index, response);
                        }
                    } finally {
                        busy.set(false);
                    }

                    showInformation(
                            "Appointment Cancelled",
                            "Appointment was cancelled successfully."
                    );
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Cancel Appointment",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Cancel Appointment",
                                "An unexpected error occurred while cancelling the appointment."
                        );
                    }
                }
        );
    }


    private void deleteSelectedAppointment() {

        if (busy.get()) {
            return;
        }

        AppointmentResponse selectedAppointment =
                appointmentTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedAppointment == null) {
            return;
        }


        if (
                !showConfirmation(
                        "Delete Appointment",
                        "Delete appointment "
                                + selectedAppointment
                                .getAppointmentId()
                                + "?",
                        "This action cannot be undone."
                )
        ) {

            return;
        }


        busy.set(true);

        AsyncTaskRunner.run(
                () -> {
                    appointmentApiService.deleteAppointment(selectedAppointment.getAppointmentId());
                    return null;
                },
                response -> {
                    try {
                        appointments.remove(selectedAppointment);
                    } finally {
                        busy.set(false);
                    }

                    showInformation(
                            "Appointment Deleted",
                            "Appointment was deleted successfully."
                    );
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Delete Appointment",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Delete Appointment",
                                "An unexpected error occurred while deleting the appointment."
                        );
                    }
                }
        );
    }


    private AppointmentFormResult showAppointmentForm(
            String title,
            AppointmentResponse appointment
    ) {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(title);

        dialog
                .getDialogPane()
                .getButtonTypes()
                .addAll(
                        ButtonType.CANCEL,
                        ButtonType.OK
                );


        ThemeManager.apply(
                dialog.getDialogPane()
        );


        /*
         * APPOINTMENT ID
         */

        TextField appointmentIdField =
                new TextField();

        appointmentIdField.setPromptText(
                "A001"
        );


        /*
         * PATIENT
         */

        ComboBox<PatientResponse> patientComboBox =
                createPatientComboBox();


        /*
         * DOCTOR
         */

        ComboBox<DoctorResponse> doctorComboBox =
                createDoctorComboBox();


        /*
         * DATE
         */

        DatePicker appointmentDatePicker =
                new DatePicker();

        appointmentDatePicker.setPromptText(
                "Select appointment date"
        );


        appointmentDatePicker.setDayCellFactory(
                datePicker ->
                        new DateCell() {

                            @Override
                            public void updateItem(
                                    LocalDate date,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        date,
                                        empty
                                );

                                if (
                                        date.isBefore(
                                                LocalDate.now()
                                        )
                                ) {

                                    setDisable(true);
                                }
                            }
                        }
        );


        /*
         * EDIT MODE
         */

        if (appointment != null) {

            appointmentIdField.setText(
                    appointment
                            .getAppointmentId()
            );

            appointmentIdField.setDisable(
                    true
            );


            patientComboBox.setValue(
                    findPatientById(
                            appointment
                                    .getPatientId()
                    )
            );


            doctorComboBox.setValue(
                    findDoctorById(
                            appointment
                                    .getDoctorId()
                    )
            );


            appointmentDatePicker.setValue(
                    appointment
                            .getAppointmentDate()
            );
        }


        /*
         * FORM LAYOUT
         */

        GridPane form =
                new GridPane();

        form.setHgap(14);
        form.setVgap(14);

        form.setPadding(
                new Insets(20)
        );


        appointmentIdField.setAccessibleText("Appointment ID");
        patientComboBox.setAccessibleText("Patient");
        doctorComboBox.setAccessibleText("Doctor");
        appointmentDatePicker.setAccessibleText("Appointment Date");

        form.add(
                new Label("Appointment ID"),
                0,
                0
        );

        form.add(
                appointmentIdField,
                1,
                0
        );


        form.add(
                new Label("Patient"),
                0,
                1
        );

        form.add(
                patientComboBox,
                1,
                1
        );


        form.add(
                new Label("Doctor"),
                0,
                2
        );

        form.add(
                doctorComboBox,
                1,
                2
        );


        form.add(
                new Label("Appointment Date"),
                0,
                3
        );

        form.add(
                appointmentDatePicker,
                1,
                3
        );


        patientComboBox.setPrefWidth(
                300
        );

        doctorComboBox.setPrefWidth(
                300
        );

        appointmentDatePicker.setPrefWidth(
                300
        );


        dialog
                .getDialogPane()
                .setContent(form);


        while (true) {

            Optional<ButtonType> result =
                    dialog.showAndWait();


            if (
                    result.isEmpty()
                            || result.get()
                            != ButtonType.OK
            ) {

                return null;
            }


            String appointmentId =
                    appointmentIdField
                            .getText()
                            .trim();

            PatientResponse patient =
                    patientComboBox
                            .getValue();

            DoctorResponse doctor =
                    doctorComboBox
                            .getValue();

            LocalDate appointmentDate =
                    appointmentDatePicker
                            .getValue();


            String validationError =
                    validateAppointmentForm(
                            appointmentId,
                            patient,
                            doctor,
                            appointmentDate
                    );


            if (validationError != null) {

                showWarning(
                        "Invalid Appointment Information",
                        validationError
                );

                continue;
            }


            return new AppointmentFormResult(
                    appointmentId,
                    patient,
                    doctor,
                    appointmentDate
            );
        }
    }


    private ComboBox<PatientResponse> createPatientComboBox() {

        ComboBox<PatientResponse> comboBox =
                new ComboBox<>(
                        patientOptions
                );

        comboBox.setPromptText(
                "Select patient"
        );


        comboBox.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(
                            PatientResponse patient
                    ) {

                        if (patient == null) {
                            return "";
                        }

                        return patient.getPatientId()
                                + " — "
                                + patient.getPatientName();
                    }


                    @Override
                    public PatientResponse fromString(
                            String string
                    ) {

                        return null;
                    }
                }
        );


        return comboBox;
    }


    private ComboBox<DoctorResponse> createDoctorComboBox() {

        ComboBox<DoctorResponse> comboBox =
                new ComboBox<>(
                        doctorOptions
                );

        comboBox.setPromptText(
                "Select doctor"
        );


        comboBox.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(
                            DoctorResponse doctor
                    ) {

                        if (doctor == null) {
                            return "";
                        }

                        return doctor.getDoctorId()
                                + " — "
                                + doctor.getDoctorName()
                                + " — "
                                + doctor.getSpecialization();
                    }


                    @Override
                    public DoctorResponse fromString(
                            String string
                    ) {

                        return null;
                    }
                }
        );


        return comboBox;
    }


    private PatientResponse findPatientById(
            String patientId
    ) {

        return patientOptions
                .stream()
                .filter(
                        patient ->
                                patient
                                        .getPatientId()
                                        .equals(patientId)
                )
                .findFirst()
                .orElse(null);
    }


    private DoctorResponse findDoctorById(
            String doctorId
    ) {

        return doctorOptions
                .stream()
                .filter(
                        doctor ->
                                doctor
                                        .getDoctorId()
                                        .equals(doctorId)
                )
                .findFirst()
                .orElse(null);
    }


    private String getPatientDisplay(
            String patientId
    ) {

        PatientResponse patient =
                findPatientById(
                        patientId
                );


        if (patient == null) {
            return patientId;
        }


        return patient.getPatientId()
                + " — "
                + patient.getPatientName();
    }


    private String getDoctorDisplay(
            String doctorId
    ) {

        DoctorResponse doctor =
                findDoctorById(
                        doctorId
                );


        if (doctor == null) {
            return doctorId;
        }


        return doctor.getDoctorId()
                + " — "
                + doctor.getDoctorName();
    }


    private String validateAppointmentForm(
            String appointmentId,
            PatientResponse patient,
            DoctorResponse doctor,
            LocalDate appointmentDate
    ) {

        if (appointmentId.isBlank()) {

            return "Appointment ID is required.";
        }


        if (
                !InputValidator
                        .isValidAppointmentId(appointmentId)
        ) {

            return "Appointment ID must follow the format A###, for example A001.";
        }


        if (patient == null) {

            return "Select a patient.";
        }


        if (doctor == null) {

            return "Select a doctor.";
        }


        if (appointmentDate == null) {

            return "Select an appointment date.";
        }


        if (
                appointmentDate.isBefore(
                        LocalDate.now()
                )
        ) {

            return "Appointment date cannot be in the past.";
        }


        return null;
    }


    private boolean showConfirmation(
            String title,
            String header,
            String message
    ) {

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                title
        );

        confirmation.setHeaderText(
                header
        );

        confirmation.setContentText(
                message
        );


        ThemeManager.apply(
                confirmation.getDialogPane()
        );


        Optional<ButtonType> result =
                confirmation.showAndWait();


        return result.isPresent()
                && result.get()
                == ButtonType.OK;
    }


    private void showInformation(
            String title,
            String message
    ) {

        showAlert(
                Alert.AlertType.INFORMATION,
                title,
                message
        );
    }


    private void showWarning(
            String title,
            String message
    ) {

        showAlert(
                Alert.AlertType.WARNING,
                title,
                message
        );
    }


    private void showError(
            String title,
            String message
    ) {

        showAlert(
                Alert.AlertType.ERROR,
                title,
                message
        );
    }


    private void showAlert(
            Alert.AlertType type,
            String title,
            String message
    ) {

        Alert alert =
                new Alert(type);

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


    private record AppointmentFormResult(
            String appointmentId,
            PatientResponse patient,
            DoctorResponse doctor,
            LocalDate appointmentDate
    ) {
    }
    private record AppointmentPageData(
            List<AppointmentResponse> appointments,
            List<PatientResponse> patientOptions,
            List<DoctorResponse> doctorOptions
    ) {
    }
}
