package com.rithika.clinicsystem.ui.patient;

import com.rithika.clinicsystem.api.ApiException;
import com.rithika.clinicsystem.api.PatientApiService;
import com.rithika.clinicsystem.dto.PatientCreateRequest;
import com.rithika.clinicsystem.dto.PatientUpdateRequest;
import com.rithika.clinicsystem.dto.PatientResponse;
import com.rithika.clinicsystem.ui.ThemeManager;
import com.rithika.clinicsystem.util.AsyncTaskRunner;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import com.rithika.clinicsystem.util.InputValidator;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class PatientView {

    private final PatientApiService patientApiService;

    private final BorderPane root;

    private final BooleanProperty busy;

    private final TableView<PatientResponse> patientTable;

    private final ObservableList<PatientResponse> patients;

    private final FilteredList<PatientResponse> filteredPatients;


    public PatientView(
            PatientApiService patientApiService
    ) {

        this.patientApiService =
                patientApiService;

        this.root =
                new BorderPane();

        this.patientTable =
                new TableView<>();

        this.patients =
                FXCollections.observableArrayList();

        this.filteredPatients =
                new FilteredList<>(
                        patients,
                        patient -> true
                );

        this.busy =
                new SimpleBooleanProperty(false);

        buildView();

        loadPatients();
    }


    private void buildView() {

        ProgressIndicator loadingIndicator =
                new ProgressIndicator();

        loadingIndicator.setAccessibleText("Patient request in progress");

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
         * PAGE HEADER
         * ----------------------------------------------------
         */

        Label titleLabel =
                new Label("Patients");

        titleLabel
                .getStyleClass()
                .add("page-title");


        Label subtitleLabel =
                new Label(
                        "Manage patient information and insurance status"
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


        Button addPatientButton =
                new Button("+ Add Patient");

        addPatientButton
                .getStyleClass()
                .add("accent-button");


        addPatientButton.setOnAction(
                event -> showAddPatientDialog()
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
                        addPatientButton
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

        searchField.setAccessibleText("Search patients");

        searchField.setPromptText(
                "Search by patient ID, name or phone number"
        );

        searchField
                .getStyleClass()
                .add("search-field");

        searchField.setMaxWidth(
                420
        );


        searchField
                .textProperty()
                .addListener(
                        (
                                observable,
                                oldValue,
                                newValue
                        ) -> filterPatients(newValue)
                );


        /*
         * ----------------------------------------------------
         * TABLE
         * ----------------------------------------------------
         */

        configureTable();

        patientTable.setItems(
                filteredPatients
        );

        patientTable.setPlaceholder(
                new Label(
                        "No patients found"
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
                event -> loadPatients()
        );


        Button editButton =
                new Button("Edit");

        editButton
                .getStyleClass()
                .add("primary-button");

        editButton.setOnAction(
                event -> editSelectedPatient()
        );


        Button deleteButton =
                new Button("Delete");

        deleteButton
                .getStyleClass()
                .add("danger-button");

        deleteButton.setOnAction(
                event -> deleteSelectedPatient()
        );


        addPatientButton.disableProperty().bind(busy);
        refreshButton.disableProperty().bind(busy);
        editButton.disableProperty().bind(
                busy
        );
        deleteButton.disableProperty().bind(
                busy
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
                        patientTable,
                        actions
                );

        VBox.setVgrow(
                patientTable,
                Priority.ALWAYS
        );


        root.setCenter(
                content
        );
    }


    private void configureTable() {

        patientTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );


        /*
         * PATIENT ID
         */

        TableColumn<PatientResponse, String>
                patientIdColumn =
                new TableColumn<>("Patient ID");

        patientIdColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getPatientId()
                        )
        );


        /*
         * NAME
         */

        TableColumn<PatientResponse, String>
                nameColumn =
                new TableColumn<>("Name");

        nameColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getPatientName()
                        )
        );


        /*
         * AGE
         */

        TableColumn<PatientResponse, Number>
                ageColumn =
                new TableColumn<>("Age");

        ageColumn.setCellValueFactory(
                cellData ->
                        new SimpleIntegerProperty(
                                cellData
                                        .getValue()
                                        .getAge()
                        )
        );


        /*
         * PHONE
         */

        TableColumn<PatientResponse, String>
                phoneColumn =
                new TableColumn<>("Phone Number");

        phoneColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getPhoneNumber()
                        )
        );


        /*
         * INSURANCE
         */

        TableColumn<PatientResponse, String>
                insuranceColumn =
                new TableColumn<>("Insurance");

        insuranceColumn.setCellValueFactory(
                cellData -> {

                    boolean insured =
                            cellData
                                    .getValue()
                                    .isInsuranceStatus();

                    return new SimpleStringProperty(
                            insured
                                    ? "Insured"
                                    : "Not Insured"
                    );
                }
        );


        patientTable
                .getColumns()
                .addAll(
                        patientIdColumn,
                        nameColumn,
                        ageColumn,
                        phoneColumn,
                        insuranceColumn
                );
    }


    private void loadPatients() {

        if (busy.get()) {
            return;
        }

        busy.set(true);

        AsyncTaskRunner.run(
                () -> patientApiService.getAllPatients(),
                data -> {
                    try {
                        patients.setAll(data);
                    } finally {
                        busy.set(false);
                    }
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Load Patients",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Load Patients",
                                "An unexpected error occurred while loading patients."
                        );
                    }
                }
        );
    }


    private void filterPatients(
            String searchText
    ) {

        String search =
                searchText == null
                        ? ""
                        : searchText
                        .trim()
                        .toLowerCase();


        if (search.isEmpty()) {

            filteredPatients.setPredicate(
                    patient -> true
            );

            return;
        }


        filteredPatients.setPredicate(
                patient -> {

                    String patientId =
                            patient
                                    .getPatientId()
                                    .toLowerCase();

                    String name =
                            patient
                                    .getPatientName()
                                    .toLowerCase();

                    String phone =
                            patient
                                    .getPhoneNumber()
                                    .toLowerCase();


                    return patientId.contains(search)
                            || name.contains(search)
                            || phone.contains(search);
                }
        );
    }


    private void showAddPatientDialog() {

        if (busy.get()) {
            return;
        }

        PatientFormResult formResult =
                showPatientForm(
                        "Add Patient",
                        null
                );

        if (formResult == null) {
            return;
        }


        PatientCreateRequest request =
                new PatientCreateRequest(
                        formResult.patientId(),
                        formResult.patientName(),
                        formResult.age(),
                        formResult.phoneNumber(),
                        formResult.insuranceStatus()
                );


        busy.set(true);

        AsyncTaskRunner.run(
                () -> patientApiService.addPatient(request),
                response -> {
                    try {
                        patients.add(response);
                    } finally {
                        busy.set(false);
                    }

                    showInformation(
                            "Patient Added",
                            "Patient was added successfully."
                    );
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Add Patient",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Add Patient",
                                "An unexpected error occurred while adding the patient."
                        );
                    }
                }
        );
    }


    private void editSelectedPatient() {

        if (busy.get()) {
            return;
        }

        PatientResponse selectedPatient =
                patientTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedPatient == null) {

            showWarning(
                    "No Patient Selected",
                    "Select a patient from the table first."
            );

            return;
        }


        PatientFormResult formResult =
                showPatientForm(
                        "Edit Patient",
                        selectedPatient
                );


        if (formResult == null) {
            return;
        }


        PatientUpdateRequest request =
                new PatientUpdateRequest(
                        formResult.patientName(),
                        formResult.age(),
                        formResult.phoneNumber(),
                        formResult.insuranceStatus()
                );


        busy.set(true);

        AsyncTaskRunner.run(
                () -> patientApiService.updatePatient(selectedPatient.getPatientId(), request),
                response -> {
                    try {
                        int index = patients.indexOf(selectedPatient);
                        if (index >= 0) {
                            patients.set(index, response);
                        }
                    } finally {
                        busy.set(false);
                    }

                    showInformation(
                            "Patient Updated",
                            "Patient information was updated successfully."
                    );
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Update Patient",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Update Patient",
                                "An unexpected error occurred while updating the patient."
                        );
                    }
                }
        );
    }


    private void deleteSelectedPatient() {

        if (busy.get()) {
            return;
        }

        PatientResponse selectedPatient =
                patientTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedPatient == null) {

            showWarning(
                    "No Patient Selected",
                    "Select a patient from the table first."
            );

            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Patient"
        );

        confirmation.setHeaderText(
                "Delete "
                        + selectedPatient
                        .getPatientName()
                        + "?"
        );

        confirmation.setContentText(
                "Patient ID: "
                        + selectedPatient
                        .getPatientId()
                        + "\n\n"
                        + "This action cannot be undone."
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
                    patientApiService.deletePatient(selectedPatient.getPatientId());
                    return null;
                },
                response -> {
                    try {
                        patients.remove(selectedPatient);
                    } finally {
                        busy.set(false);
                    }

                    showInformation(
                            "Patient Deleted",
                            "Patient was deleted successfully."
                    );
                },
                throwable -> {
                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {
                        showError(
                                "Unable to Delete Patient",
                                apiException.getMessage()
                        );
                    } else {
                        showError(
                                "Unable to Delete Patient",
                                "An unexpected error occurred while deleting the patient."
                        );
                    }
                }
        );
    }


    private PatientFormResult showPatientForm(
            String title,
            PatientResponse patient
    ) {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(title);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        ButtonType.CANCEL,
                        ButtonType.OK
                );

        ThemeManager.apply(
                dialog.getDialogPane()
        );


        TextField patientIdField =
                new TextField();

        patientIdField.setPromptText(
                "P001"
        );


        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Patient name"
        );


        TextField ageField =
                new TextField();

        ageField.setPromptText(
                "Age"
        );


        TextField phoneField =
                new TextField();

        phoneField.setPromptText(
                "10 digit phone number"
        );


        CheckBox insuranceCheckBox =
                new CheckBox(
                        "Patient has insurance"
                );


        if (patient != null) {

            patientIdField.setText(
                    patient.getPatientId()
            );

            patientIdField.setDisable(
                    true
            );

            nameField.setText(
                    patient.getPatientName()
            );

            ageField.setText(
                    String.valueOf(
                            patient.getAge()
                    )
            );

            phoneField.setText(
                    patient.getPhoneNumber()
            );

            insuranceCheckBox.setSelected(
                    patient.isInsuranceStatus()
            );
        }


        GridPane form =
                new GridPane();

        form.setHgap(14);
        form.setVgap(14);

        form.setPadding(
                new Insets(20)
        );


        patientIdField.setAccessibleText("Patient ID");
        nameField.setAccessibleText("Name");
        ageField.setAccessibleText("Age");
        phoneField.setAccessibleText("Phone Number");

        form.add(
                new Label("Patient ID"),
                0,
                0
        );

        form.add(
                patientIdField,
                1,
                0
        );


        form.add(
                new Label("Name"),
                0,
                1
        );

        form.add(
                nameField,
                1,
                1
        );


        form.add(
                new Label("Age"),
                0,
                2
        );

        form.add(
                ageField,
                1,
                2
        );


        form.add(
                new Label("Phone Number"),
                0,
                3
        );

        form.add(
                phoneField,
                1,
                3
        );


        form.add(
                insuranceCheckBox,
                1,
                4
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


            String patientId =
                    patientIdField
                            .getText()
                            .trim();

            String patientName =
                    nameField
                            .getText()
                            .trim();

            String ageText =
                    ageField
                            .getText()
                            .trim();

            String phoneNumber =
                    phoneField
                            .getText()
                            .trim();


            String validationError =
                    validatePatientForm(
                            patientId,
                            patientName,
                            ageText,
                            phoneNumber
                    );


            if (validationError != null) {

                showWarning(
                        "Invalid Patient Information",
                        validationError
                );

                continue;
            }


            int age =
                    Integer.parseInt(
                            ageText
                    );


            return new PatientFormResult(
                    patientId,
                    patientName,
                    age,
                    phoneNumber,
                    insuranceCheckBox
                            .isSelected()
            );
        }
    }


    private String validatePatientForm(
            String patientId,
            String patientName,
            String age,
            String phoneNumber
    ) {

        if (
                InputValidator
                        .isEmpty(patientId)
        ) {

            return "Patient ID is required.";
        }


        if (
                !InputValidator
                        .isValidPatientId(patientId)
        ) {

            return "Patient ID must follow the format P###, for example P001.";
        }


        if (
                InputValidator
                        .isEmpty(patientName)
        ) {

            return "Patient name is required.";
        }


        if (
                !InputValidator
                        .isPositiveInteger(age)
        ) {

            return "Age must be a positive number.";
        }


        if (
                !InputValidator
                        .isValidPhoneNumber(
                                phoneNumber
                        )
        ) {

            return "Phone number must contain 10 digits.";
        }


        return null;
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

        alert.setTitle(title);

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


    private record PatientFormResult(
            String patientId,
            String patientName,
            int age,
            String phoneNumber,
            boolean insuranceStatus
    ) {
    }
}