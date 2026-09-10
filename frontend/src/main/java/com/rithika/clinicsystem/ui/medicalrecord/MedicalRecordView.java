package com.rithika.clinicsystem.ui.medicalrecord;

import com.rithika.clinicsystem.api.ApiException;
import com.rithika.clinicsystem.api.AppointmentApiService;
import com.rithika.clinicsystem.api.MedicalRecordApiService;
import com.rithika.clinicsystem.dto.AppointmentResponse;
import com.rithika.clinicsystem.dto.MedicalRecordRequest;
import com.rithika.clinicsystem.dto.MedicalRecordResponse;
import com.rithika.clinicsystem.ui.ThemeManager;
import com.rithika.clinicsystem.util.AsyncTaskRunner;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import com.rithika.clinicsystem.util.InputValidator;

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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class MedicalRecordView {

    private final MedicalRecordApiService medicalRecordApiService;
    private final AppointmentApiService appointmentApiService;

    private final BorderPane root;

    private final BooleanProperty busy;

    private final TableView<MedicalRecordResponse> medicalRecordTable;

    private final ObservableList<MedicalRecordResponse> medicalRecords;
    private final FilteredList<MedicalRecordResponse> filteredMedicalRecords;

    private final ObservableList<AppointmentResponse> appointments;

    private Button editButton;
    private Button deleteButton;


    public MedicalRecordView(
            MedicalRecordApiService medicalRecordApiService,
            AppointmentApiService appointmentApiService
    ) {

        this.medicalRecordApiService =
                medicalRecordApiService;

        this.appointmentApiService =
                appointmentApiService;

        this.root =
                new BorderPane();

        this.medicalRecordTable =
                new TableView<>();

        this.medicalRecords =
                FXCollections.observableArrayList();

        this.filteredMedicalRecords =
                new FilteredList<>(
                        medicalRecords,
                        record -> true
                );

        this.appointments =
                FXCollections.observableArrayList();

        this.busy =
                new SimpleBooleanProperty(false);

        buildView();

        loadData();
    }


    private void buildView() {

        ProgressIndicator loadingIndicator =
                new ProgressIndicator();

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
                new Label("Medical Records");

        titleLabel
                .getStyleClass()
                .add("page-title");


        Label subtitleLabel =
                new Label(
                        "Manage patient diagnoses, treatments and treatment costs"
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


        Button addRecordButton =
                new Button("+ Add Medical Record");

        addRecordButton
                .getStyleClass()
                .add("accent-button");

        addRecordButton.setOnAction(
                event -> showAddMedicalRecordDialog()
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
                        addRecordButton
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

        searchField.setPromptText(
                "Search by record, appointment, patient, doctor, diagnosis or treatment"
        );

        searchField
                .getStyleClass()
                .add("search-field");

        searchField.setMaxWidth(
                520
        );


        searchField
                .textProperty()
                .addListener(
                        (
                                observable,
                                oldValue,
                                newValue
                        ) -> filterMedicalRecords(
                                newValue
                        )
                );


        /*
         * ----------------------------------------------------
         * TABLE
         * ----------------------------------------------------
         */

        configureTable();

        medicalRecordTable.setItems(
                filteredMedicalRecords
        );

        medicalRecordTable.setPlaceholder(
                new Label(
                        "No medical records found"
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

        refreshButton.setOnAction(
                event -> loadData()
        );


        editButton =
                new Button("Edit");

        editButton
                .getStyleClass()
                .add("primary-button");


        editButton.setOnAction(
                event -> editSelectedMedicalRecord()
        );


        deleteButton =
                new Button("Delete");

        deleteButton
                .getStyleClass()
                .add("danger-button");


        deleteButton.setOnAction(
                event -> deleteSelectedMedicalRecord()
        );


        addRecordButton.disableProperty().bind(busy);
        refreshButton.disableProperty().bind(busy);
        editButton.disableProperty().bind(
                busy.or(medicalRecordTable.getSelectionModel().selectedItemProperty().isNull())
        );
        deleteButton.disableProperty().bind(
                busy.or(medicalRecordTable.getSelectionModel().selectedItemProperty().isNull())
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
                        medicalRecordTable,
                        actions
                );

        VBox.setVgrow(
                medicalRecordTable,
                Priority.ALWAYS
        );


        root.setCenter(
                content
        );
    }


    private void configureTable() {

        medicalRecordTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );


        /*
         * RECORD ID
         */

        TableColumn<MedicalRecordResponse, String>
                recordIdColumn =
                new TableColumn<>("Record ID");

        recordIdColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getRecordId()
                        )
        );


        /*
         * APPOINTMENT ID
         */

        TableColumn<MedicalRecordResponse, String>
                appointmentIdColumn =
                new TableColumn<>(
                        "Appointment"
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
         * PATIENT ID
         */

        TableColumn<MedicalRecordResponse, String>
                patientIdColumn =
                new TableColumn<>(
                        "Patient"
                );

        patientIdColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getPatientId()
                        )
        );


        /*
         * DOCTOR ID
         */

        TableColumn<MedicalRecordResponse, String>
                doctorIdColumn =
                new TableColumn<>(
                        "Doctor"
                );

        doctorIdColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getDoctorId()
                        )
        );


        /*
         * DIAGNOSIS
         */

        TableColumn<MedicalRecordResponse, String>
                diagnosisColumn =
                new TableColumn<>(
                        "Diagnosis"
                );

        diagnosisColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getDiagnosis()
                        )
        );


        /*
         * TREATMENT
         */

        TableColumn<MedicalRecordResponse, String>
                treatmentColumn =
                new TableColumn<>(
                        "Treatment"
                );

        treatmentColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getTreatment()
                        )
        );


        /*
         * TREATMENT COST
         */

        TableColumn<MedicalRecordResponse, String>
                treatmentCostColumn =
                new TableColumn<>(
                        "Treatment Cost"
                );

        treatmentCostColumn.setCellValueFactory(
                cellData -> {

                    BigDecimal cost =
                            cellData
                                    .getValue()
                                    .getTreatmentCost();

                    return new SimpleStringProperty(
                            "Rs. "
                                    + cost.toPlainString()
                    );
                }
        );


        medicalRecordTable
                .getColumns()
                .addAll(
                        recordIdColumn,
                        appointmentIdColumn,
                        patientIdColumn,
                        doctorIdColumn,
                        diagnosisColumn,
                        treatmentColumn,
                        treatmentCostColumn
                );
    }


    private void loadData() {

        if (busy.get()) {
            return;
        }

        busy.set(true);

        AsyncTaskRunner.run(
                () -> new MedicalRecordPageData(
                        appointmentApiService.getAllAppointments(),
                        medicalRecordApiService.getAllMedicalRecords()
                ),
                data -> {
                    try {
                        appointments.setAll(data.appointments());
                        medicalRecords.setAll(data.medicalRecords());
                        medicalRecordTable.getSelectionModel().clearSelection();
                    } finally {
                        busy.set(false);
                    }
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Load Medical Records",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Load Medical Records",
                                "An unexpected error occurred while loading medical records."
                        );
                    }
                }
        );
    }


    private void filterMedicalRecords(
            String searchText
    ) {

        String search =
                searchText == null
                        ? ""
                        : searchText
                        .trim()
                        .toLowerCase();


        if (search.isEmpty()) {

            filteredMedicalRecords.setPredicate(
                    record -> true
            );

            return;
        }


        filteredMedicalRecords.setPredicate(
                record -> {

                    String recordId =
                            safeLower(
                                    record.getRecordId()
                            );

                    String appointmentId =
                            safeLower(
                                    record.getAppointmentId()
                            );

                    String patientId =
                            safeLower(
                                    record.getPatientId()
                            );

                    String doctorId =
                            safeLower(
                                    record.getDoctorId()
                            );

                    String diagnosis =
                            safeLower(
                                    record.getDiagnosis()
                            );

                    String treatment =
                            safeLower(
                                    record.getTreatment()
                            );


                    return recordId.contains(search)
                            || appointmentId.contains(search)
                            || patientId.contains(search)
                            || doctorId.contains(search)
                            || diagnosis.contains(search)
                            || treatment.contains(search);
                }
        );
    }


    private void showAddMedicalRecordDialog() {

        if (busy.get()) {
            return;
        }

        List<AppointmentResponse>
                eligibleAppointments =
                getEligibleAppointments(
                        null
                );


        if (eligibleAppointments.isEmpty()) {

            showWarning(
                    "No Eligible Appointments",
                    "A medical record can only be created for a completed "
                            + "appointment that does not already have a medical record."
            );

            return;
        }


        MedicalRecordFormResult formResult =
                showMedicalRecordForm(
                        "Add Medical Record",
                        null
                );


        if (formResult == null) {
            return;
        }


        MedicalRecordRequest request =
                new MedicalRecordRequest(
                        formResult.recordId(),
                        formResult.appointmentId(),
                        formResult.diagnosis(),
                        formResult.treatment(),
                        formResult.treatmentCost()
                );


        busy.set(true);

        AsyncTaskRunner.run(
                () -> medicalRecordApiService.addMedicalRecord(request),
                response -> {
                    try {
                        medicalRecords.add(response);
                    } finally {
                        busy.set(false);
                    }

                    showInformation(
                            "Medical Record Added",
                            "Medical record was added successfully."
                    );
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Add Medical Record",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Add Medical Record",
                                "An unexpected error occurred while adding the medical record."
                        );
                    }
                }
        );
    }


    private void editSelectedMedicalRecord() {

        if (busy.get()) {
            return;
        }

        MedicalRecordResponse selectedRecord =
                medicalRecordTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedRecord == null) {
            return;
        }


        MedicalRecordFormResult formResult =
                showMedicalRecordForm(
                        "Edit Medical Record",
                        selectedRecord
                );


        if (formResult == null) {
            return;
        }


        /*
         * The current backend uses the same validated request
         * DTO for both POST and PUT.
         *
         * Therefore recordId and appointmentId are still sent
         * during an update even though the backend service only
         * changes diagnosis, treatment and treatment cost.
         */

        MedicalRecordRequest request =
                new MedicalRecordRequest(
                        selectedRecord
                                .getRecordId(),
                        selectedRecord
                                .getAppointmentId(),
                        formResult.diagnosis(),
                        formResult.treatment(),
                        formResult.treatmentCost()
                );


        busy.set(true);

        AsyncTaskRunner.run(
                () -> medicalRecordApiService.updateMedicalRecord(selectedRecord.getRecordId(), request),
                response -> {
                    try {
                        int index = medicalRecords.indexOf(selectedRecord);
                        if (index >= 0) {
                            medicalRecords.set(index, response);
                        }
                    } finally {
                        busy.set(false);
                    }

                    showInformation(
                            "Medical Record Updated",
                            "Medical record was updated successfully."
                    );
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Update Medical Record",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Update Medical Record",
                                "An unexpected error occurred while updating the medical record."
                        );
                    }
                }
        );
    }


    private void deleteSelectedMedicalRecord() {

        if (busy.get()) {
            return;
        }

        MedicalRecordResponse selectedRecord =
                medicalRecordTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedRecord == null) {
            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Medical Record"
        );

        confirmation.setHeaderText(
                "Delete medical record "
                        + selectedRecord
                        .getRecordId()
                        + "?"
        );

        confirmation.setContentText(
                "Appointment: "
                        + selectedRecord
                        .getAppointmentId()
                        + "\nPatient: "
                        + selectedRecord
                        .getPatientId()
                        + "\n\nThis action cannot be undone."
        );


        ThemeManager.apply(
                confirmation.getDialogPane()
        );


        Optional<ButtonType> result =
                confirmation.showAndWait();


        if (
                result.isEmpty()
                        || result.get()
                        != ButtonType.OK
        ) {

            return;
        }


        busy.set(true);

        AsyncTaskRunner.run(
                () -> {
                    medicalRecordApiService.deleteMedicalRecord(selectedRecord.getRecordId());
                    return null;
                },
                response -> {
                    try {
                        medicalRecords.remove(selectedRecord);
                    } finally {
                        busy.set(false);
                    }

                    showInformation(
                            "Medical Record Deleted",
                            "Medical record was deleted successfully."
                    );
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Delete Medical Record",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Delete Medical Record",
                                "An unexpected error occurred while deleting the medical record."
                        );
                    }
                }
        );
    }


    private MedicalRecordFormResult showMedicalRecordForm(
            String title,
            MedicalRecordResponse existingRecord
    ) {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                title
        );

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
         * ----------------------------------------------------
         * RECORD ID
         * ----------------------------------------------------
         */

        TextField recordIdField =
                new TextField();

        recordIdField.setPromptText(
                "MR001"
        );


        /*
         * ----------------------------------------------------
         * APPOINTMENT
         * ----------------------------------------------------
         */

        ComboBox<AppointmentResponse>
                appointmentComboBox =
                createAppointmentComboBox(
                        existingRecord
                );


        /*
         * ----------------------------------------------------
         * DIAGNOSIS
         * ----------------------------------------------------
         */

        TextArea diagnosisArea =
                new TextArea();

        diagnosisArea.setPromptText(
                "Enter diagnosis"
        );

        diagnosisArea.setWrapText(
                true
        );

        diagnosisArea.setPrefRowCount(
                3
        );


        /*
         * ----------------------------------------------------
         * TREATMENT
         * ----------------------------------------------------
         */

        TextArea treatmentArea =
                new TextArea();

        treatmentArea.setPromptText(
                "Enter treatment information"
        );

        treatmentArea.setWrapText(
                true
        );

        treatmentArea.setPrefRowCount(
                4
        );


        /*
         * ----------------------------------------------------
         * TREATMENT COST
         * ----------------------------------------------------
         */

        TextField treatmentCostField =
                new TextField();

        treatmentCostField.setPromptText(
                "0.00"
        );


        /*
         * ----------------------------------------------------
         * EDIT MODE
         * ----------------------------------------------------
         */

        if (existingRecord != null) {

            recordIdField.setText(
                    existingRecord
                            .getRecordId()
            );

            recordIdField.setDisable(
                    true
            );


            diagnosisArea.setText(
                    existingRecord
                            .getDiagnosis()
            );


            treatmentArea.setText(
                    existingRecord
                            .getTreatment()
            );


            treatmentCostField.setText(
                    existingRecord
                            .getTreatmentCost()
                            .toPlainString()
            );
        }


        /*
         * ----------------------------------------------------
         * FORM
         * ----------------------------------------------------
         */

        GridPane form =
                new GridPane();

        form.setHgap(
                14
        );

        form.setVgap(
                14
        );

        form.setPadding(
                new Insets(20)
        );


        form.add(
                new Label("Record ID"),
                0,
                0
        );

        form.add(
                recordIdField,
                1,
                0
        );


        form.add(
                new Label("Completed Appointment"),
                0,
                1
        );

        form.add(
                appointmentComboBox,
                1,
                1
        );


        form.add(
                new Label("Diagnosis"),
                0,
                2
        );

        form.add(
                diagnosisArea,
                1,
                2
        );


        form.add(
                new Label("Treatment"),
                0,
                3
        );

        form.add(
                treatmentArea,
                1,
                3
        );


        form.add(
                new Label("Treatment Cost"),
                0,
                4
        );

        form.add(
                treatmentCostField,
                1,
                4
        );


        recordIdField.setPrefWidth(
                360
        );

        appointmentComboBox.setPrefWidth(
                360
        );

        diagnosisArea.setPrefWidth(
                360
        );

        treatmentArea.setPrefWidth(
                360
        );

        treatmentCostField.setPrefWidth(
                360
        );


        dialog
                .getDialogPane()
                .setContent(
                        form
                );

        dialog
                .getDialogPane()
                .setPrefWidth(
                        620
                );


        /*
         * ----------------------------------------------------
         * SUBMIT LOOP
         * ----------------------------------------------------
         */

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


            String recordId =
                    recordIdField
                            .getText()
                            .trim();


            AppointmentResponse selectedAppointment =
                    appointmentComboBox
                            .getValue();


            String appointmentId =
                    selectedAppointment == null
                            ? null
                            : selectedAppointment
                            .getAppointmentId();


            String diagnosis =
                    diagnosisArea
                            .getText()
                            .trim();


            String treatment =
                    treatmentArea
                            .getText()
                            .trim();


            String treatmentCostText =
                    treatmentCostField
                            .getText()
                            .trim();


            String validationError =
                    validateMedicalRecordForm(
                            recordId,
                            appointmentId,
                            diagnosis,
                            treatment,
                            treatmentCostText
                    );


            if (validationError != null) {

                showWarning(
                        "Invalid Medical Record Information",
                        validationError
                );

                continue;
            }


            BigDecimal treatmentCost =
                    new BigDecimal(
                            treatmentCostText
                    );


            return new MedicalRecordFormResult(
                    recordId,
                    appointmentId,
                    diagnosis,
                    treatment,
                    treatmentCost
            );
        }
    }


    private ComboBox<AppointmentResponse>
    createAppointmentComboBox(
            MedicalRecordResponse existingRecord
    ) {

        String currentAppointmentId =
                existingRecord == null
                        ? null
                        : existingRecord
                        .getAppointmentId();


        ObservableList<AppointmentResponse>
                eligibleAppointments =
                FXCollections.observableArrayList(
                        getEligibleAppointments(
                                currentAppointmentId
                        )
                );


        ComboBox<AppointmentResponse> comboBox =
                new ComboBox<>(
                        eligibleAppointments
                );


        comboBox.setPromptText(
                "Select completed appointment"
        );


        comboBox.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(
                            AppointmentResponse appointment
                    ) {

                        if (appointment == null) {
                            return "";
                        }


                        return appointment
                                .getAppointmentId()
                                + " — "
                                + appointment
                                .getPatientId()
                                + " — "
                                + appointment
                                .getDoctorId()
                                + " — "
                                + appointment
                                .getAppointmentDate();
                    }


                    @Override
                    public AppointmentResponse fromString(
                            String string
                    ) {

                        return null;
                    }
                }
        );


        if (existingRecord != null) {

            AppointmentResponse currentAppointment =
                    findAppointmentById(
                            existingRecord
                                    .getAppointmentId()
                    );


            comboBox.setValue(
                    currentAppointment
            );


            /*
             * Appointment association cannot be changed
             * after the record has been created.
             */

            comboBox.setDisable(
                    true
            );
        }


        return comboBox;
    }


    private List<AppointmentResponse>
    getEligibleAppointments(
            String currentAppointmentId
    ) {

        return appointments
                .stream()
                .filter(
                        appointment ->
                                "COMPLETED"
                                        .equalsIgnoreCase(
                                                appointment
                                                        .getStatus()
                                        )
                )
                .filter(
                        appointment -> {

                            String appointmentId =
                                    appointment
                                            .getAppointmentId();


                            /*
                             * During edit, keep the appointment
                             * belonging to the current record.
                             */

                            if (
                                    currentAppointmentId != null
                                            && currentAppointmentId
                                            .equals(
                                                    appointmentId
                                            )
                            ) {

                                return true;
                            }


                            return !hasMedicalRecordForAppointment(
                                    appointmentId
                            );
                        }
                )
                .toList();
    }


    private boolean hasMedicalRecordForAppointment(
            String appointmentId
    ) {

        return medicalRecords
                .stream()
                .anyMatch(
                        record ->
                                record
                                        .getAppointmentId()
                                        .equals(
                                                appointmentId
                                        )
                );
    }


    private AppointmentResponse findAppointmentById(
            String appointmentId
    ) {

        return appointments
                .stream()
                .filter(
                        appointment ->
                                appointment
                                        .getAppointmentId()
                                        .equals(
                                                appointmentId
                                        )
                )
                .findFirst()
                .orElse(
                        null
                );
    }


    private String validateMedicalRecordForm(
            String recordId,
            String appointmentId,
            String diagnosis,
            String treatment,
            String treatmentCostText
    ) {

        if (recordId.isBlank()) {

            return "Medical record ID is required.";
        }


        if (
                !InputValidator
                        .isValidMedicalRecordId(recordId)
        ) {

            return "Medical Record ID must follow the format MR###, for example MR001.";
        }


        if (recordId.length() > 20) {

            return "Medical record ID cannot exceed 20 characters.";
        }


        if (appointmentId == null) {

            return "Select a completed appointment.";
        }


        if (diagnosis.isBlank()) {

            return "Diagnosis is required.";
        }


        if (diagnosis.length() > 500) {

            return "Diagnosis cannot exceed 500 characters.";
        }


        if (treatment.isBlank()) {

            return "Treatment is required.";
        }


        if (treatment.length() > 1000) {

            return "Treatment cannot exceed 1000 characters.";
        }


        if (treatmentCostText.isBlank()) {

            return "Treatment cost is required.";
        }


        try {

            BigDecimal treatmentCost =
                    new BigDecimal(
                            treatmentCostText
                    );


            if (
                    treatmentCost.compareTo(
                            BigDecimal.ZERO
                    ) < 0
            ) {

                return "Treatment cost cannot be negative.";
            }


            if (
                    treatmentCost.scale() > 2
            ) {

                return "Treatment cost can have at most 2 decimal places.";
            }


            int integerDigits =
                    treatmentCost
                            .precision()
                            - treatmentCost
                            .scale();


            if (integerDigits > 10) {

                return "Treatment cost can have at most 10 integer digits.";
            }

        } catch (NumberFormatException exception) {

            return "Treatment cost must be a valid number.";
        }


        return null;
    }


    private String safeLower(
            String value
    ) {

        return value == null
                ? ""
                : value.toLowerCase();
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
                new Alert(
                        type
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


    private record MedicalRecordFormResult(
            String recordId,
            String appointmentId,
            String diagnosis,
            String treatment,
            BigDecimal treatmentCost
    ) {
    }
    private record MedicalRecordPageData(
            List<AppointmentResponse> appointments,
            List<MedicalRecordResponse> medicalRecords
    ) {
    }
}
