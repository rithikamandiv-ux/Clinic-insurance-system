package com.rithika.clinicsystem.ui.doctor;

import com.rithika.clinicsystem.api.ApiException;
import com.rithika.clinicsystem.api.DoctorApiService;
import com.rithika.clinicsystem.dto.DoctorCreateRequest;
import com.rithika.clinicsystem.dto.DoctorUpdateRequest;
import com.rithika.clinicsystem.dto.DoctorResponse;
import com.rithika.clinicsystem.ui.ThemeManager;
import com.rithika.clinicsystem.util.InputValidator;
import com.rithika.clinicsystem.util.AsyncTaskRunner;

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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class DoctorView {

    private final DoctorApiService doctorApiService;

    private final BorderPane root;

    private final TableView<DoctorResponse> doctorTable;

    private final ObservableList<DoctorResponse> doctors;

    private final FilteredList<DoctorResponse> filteredDoctors;

    private final BooleanProperty busy;


    public DoctorView(
            DoctorApiService doctorApiService
    ) {

        this.doctorApiService =
                doctorApiService;

        this.root =
                new BorderPane();

        this.doctorTable =
                new TableView<>();

        this.doctors =
                FXCollections.observableArrayList();

        this.filteredDoctors =
                new FilteredList<>(
                        doctors,
                        doctor -> true
                );

        this.busy =
                new SimpleBooleanProperty(false);

        buildView();

        loadDoctors();
    }


    private void buildView() {

        root
                .getStyleClass()
                .add("page-content");

        root.setPadding(
                new Insets(32)
        );

        ProgressIndicator loadingIndicator =
                new ProgressIndicator();

        loadingIndicator.setAccessibleText("Doctor request in progress");

        loadingIndicator.setPrefSize(
                18,
                18
        );

        loadingIndicator.setMinSize(
                18,
                18
        );

        loadingIndicator.setMaxSize(
                18,
                18
        );

        loadingIndicator
                .visibleProperty()
                .bind(busy);

        loadingIndicator
                .managedProperty()
                .bind(busy);


        /*
         * ----------------------------------------------------
         * PAGE HEADER
         * ----------------------------------------------------
         */

        Label titleLabel =
                new Label("Doctors");

        titleLabel
                .getStyleClass()
                .add("page-title");


        Label subtitleLabel =
                new Label(
                        "Manage doctors, specializations and consultation fees"
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


        Button addDoctorButton =
                new Button("+ Add Doctor");

        addDoctorButton
                .getStyleClass()
                .add("accent-button");

        addDoctorButton.setOnAction(
                event -> showAddDoctorDialog()
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
                        addDoctorButton
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

        searchField.setAccessibleText("Search doctors");

        searchField.setPromptText(
                "Search by doctor ID, name or specialization"
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
                        ) -> filterDoctors(newValue)
                );


        /*
         * ----------------------------------------------------
         * TABLE
         * ----------------------------------------------------
         */

        configureTable();

        doctorTable.setItems(
                filteredDoctors
        );

        doctorTable.setPlaceholder(
                new Label(
                        "No doctors found"
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
                event -> loadDoctors()
        );


        Button editButton =
                new Button("Edit");

        editButton
                .getStyleClass()
                .add("primary-button");

        editButton.setOnAction(
                event -> editSelectedDoctor()
        );


        Button deleteButton =
                new Button("Delete");

        deleteButton
                .getStyleClass()
                .add("danger-button");

        deleteButton.setOnAction(
                event -> deleteSelectedDoctor()
        );

        addDoctorButton
                .disableProperty()
                .bind(busy);

        refreshButton
                .disableProperty()
                .bind(busy);

        editButton
                .disableProperty()
                .bind(busy);

        deleteButton
                .disableProperty()
                .bind(busy);


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
                        doctorTable,
                        actions
                );

        VBox.setVgrow(
                doctorTable,
                Priority.ALWAYS
        );


        root.setCenter(
                content
        );
    }


    private void configureTable() {

        doctorTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );


        /*
         * DOCTOR ID
         */

        TableColumn<DoctorResponse, String>
                doctorIdColumn =
                new TableColumn<>("Doctor ID");

        doctorIdColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getDoctorId()
                        )
        );


        /*
         * NAME
         */

        TableColumn<DoctorResponse, String>
                nameColumn =
                new TableColumn<>("Name");

        nameColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getDoctorName()
                        )
        );


        /*
         * SPECIALIZATION
         */

        TableColumn<DoctorResponse, String>
                specializationColumn =
                new TableColumn<>(
                        "Specialization"
                );

        specializationColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getSpecialization()
                        )
        );


        /*
         * CONSULTATION FEE
         */

        TableColumn<DoctorResponse, String>
                consultationFeeColumn =
                new TableColumn<>(
                        "Consultation Fee"
                );

        consultationFeeColumn.setCellValueFactory(
                cellData -> {

                    BigDecimal fee =
                            cellData
                                    .getValue()
                                    .getConsultationFee();

                    return new SimpleStringProperty(
                            "Rs. " + fee.toPlainString()
                    );
                }
        );


        doctorTable
                .getColumns()
                .addAll(
                        doctorIdColumn,
                        nameColumn,
                        specializationColumn,
                        consultationFeeColumn
                );
    }


    private void loadDoctors() {

        if (busy.get()) {
            return;
        }

        busy.set(true);

        AsyncTaskRunner.run(

                () ->
                        doctorApiService
                                .getAllDoctors(),

                result -> {

                    doctors.setAll(result);

                    busy.set(false);
                },

                throwable -> {

                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {

                        showError(
                                "Unable to Load Doctors",
                                apiException.getMessage()
                        );

                    } else {

                        showError(
                                "Unable to Load Doctors",
                                "An unexpected error occurred while loading doctors."
                        );
                    }
                }
        );
    }


    private void filterDoctors(
            String searchText
    ) {

        String search =
                searchText == null
                        ? ""
                        : searchText
                        .trim()
                        .toLowerCase();


        if (search.isEmpty()) {

            filteredDoctors.setPredicate(
                    doctor -> true
            );

            return;
        }


        filteredDoctors.setPredicate(
                doctor -> {

                    String doctorId =
                            doctor
                                    .getDoctorId()
                                    .toLowerCase();

                    String doctorName =
                            doctor
                                    .getDoctorName()
                                    .toLowerCase();

                    String specialization =
                            doctor
                                    .getSpecialization()
                                    .toLowerCase();


                    return doctorId.contains(search)
                            || doctorName.contains(search)
                            || specialization.contains(search);
                }
        );
    }


    private void showAddDoctorDialog() {

        DoctorFormResult formResult =
                showDoctorForm(
                        "Add Doctor",
                        null
                );


        if (formResult == null) {
            return;
        }


        DoctorCreateRequest request =
                new DoctorCreateRequest(
                        formResult.doctorId(),
                        formResult.doctorName(),
                        formResult.specialization(),
                        formResult.consultationFee()
                );


        busy.set(true);

        AsyncTaskRunner.run(

                () ->
                        doctorApiService
                                .addDoctor(request),

                response -> {

                    doctors.add(response);

                    busy.set(false);

                    showInformation(
                            "Doctor Added",
                            "Doctor was added successfully."
                    );
                },

                throwable -> {

                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {

                        showError(
                                "Unable to Add Doctor",
                                apiException.getMessage()
                        );

                    } else {

                        showError(
                                "Unable to Add Doctor",
                                "An unexpected error occurred while adding the doctor."
                        );
                    }
                }
        );
    }


    private void editSelectedDoctor() {

        DoctorResponse selectedDoctor =
                doctorTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedDoctor == null) {

            showWarning(
                    "No Doctor Selected",
                    "Select a doctor from the table first."
            );

            return;
        }


        DoctorFormResult formResult =
                showDoctorForm(
                        "Edit Doctor",
                        selectedDoctor
                );


        if (formResult == null) {
            return;
        }


        DoctorUpdateRequest request =
                new DoctorUpdateRequest(
                        formResult.doctorName(),
                        formResult.specialization(),
                        formResult.consultationFee()
                );


        busy.set(true);

        AsyncTaskRunner.run(

                () ->
                        doctorApiService
                                .updateDoctor(
                                        selectedDoctor.getDoctorId(),
                                        request
                                ),

                response -> {

                    int index =
                            doctors.indexOf(selectedDoctor);

                    if (index >= 0) {
                        doctors.set(
                                index,
                                response
                        );
                    }

                    busy.set(false);

                    showInformation(
                            "Doctor Updated",
                            "Doctor information was updated successfully."
                    );
                },

                throwable -> {

                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {

                        showError(
                                "Unable to Update Doctor",
                                apiException.getMessage()
                        );

                    } else {

                        showError(
                                "Unable to Update Doctor",
                                "An unexpected error occurred while updating the doctor."
                        );
                    }
                }
        );
    }


    private void deleteSelectedDoctor() {

        DoctorResponse selectedDoctor =
                doctorTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedDoctor == null) {

            showWarning(
                    "No Doctor Selected",
                    "Select a doctor from the table first."
            );

            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Doctor"
        );

        confirmation.setHeaderText(
                "Delete "
                        + selectedDoctor
                        .getDoctorName()
                        + "?"
        );

        confirmation.setContentText(
                "Doctor ID: "
                        + selectedDoctor
                        .getDoctorId()
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

                () ->
                        doctorApiService.deleteDoctor(
                                selectedDoctor.getDoctorId()
                        ),

                () -> {

                    doctors.removeIf(
                            doctor ->
                                    doctor.getDoctorId().equals(
                                            selectedDoctor.getDoctorId()
                                    )
                    );

                    busy.set(false);

                    showInformation(
                            "Doctor Deleted",
                            "Doctor was deleted successfully."
                    );
                },

                throwable -> {

                    busy.set(false);

                    if (throwable instanceof ApiException apiException) {

                        showError(
                                "Unable to Delete Doctor",
                                apiException.getMessage()
                        );

                    } else {

                        showError(
                                "Unable to Delete Doctor",
                                "An unexpected error occurred while deleting the doctor."
                        );
                    }
                }
        );
    }


    private DoctorFormResult showDoctorForm(
            String title,
            DoctorResponse doctor
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


        TextField doctorIdField =
                new TextField();

        doctorIdField.setPromptText(
                "D001"
        );


        TextField doctorNameField =
                new TextField();

        doctorNameField.setPromptText(
                "Doctor name"
        );


        TextField specializationField =
                new TextField();

        specializationField.setPromptText(
                "Specialization"
        );


        TextField consultationFeeField =
                new TextField();

        consultationFeeField.setPromptText(
                "Consultation fee"
        );


        /*
         * If we're editing, load the selected
         * doctor's current data into the form.
         */

        if (doctor != null) {

            doctorIdField.setText(
                    doctor.getDoctorId()
            );

            doctorIdField.setDisable(
                    true
            );

            doctorNameField.setText(
                    doctor.getDoctorName()
            );

            specializationField.setText(
                    doctor.getSpecialization()
            );

            consultationFeeField.setText(
                    doctor
                            .getConsultationFee()
                            .toPlainString()
            );
        }


        GridPane form =
                new GridPane();

        form.setHgap(14);
        form.setVgap(14);

        form.setPadding(
                new Insets(20)
        );


        doctorIdField.setAccessibleText("Doctor ID");
        doctorNameField.setAccessibleText("Name");
        specializationField.setAccessibleText("Specialization");
        consultationFeeField.setAccessibleText("Consultation Fee");

        form.add(
                new Label("Doctor ID"),
                0,
                0
        );

        form.add(
                doctorIdField,
                1,
                0
        );


        form.add(
                new Label("Name"),
                0,
                1
        );

        form.add(
                doctorNameField,
                1,
                1
        );


        form.add(
                new Label("Specialization"),
                0,
                2
        );

        form.add(
                specializationField,
                1,
                2
        );


        form.add(
                new Label("Consultation Fee"),
                0,
                3
        );

        form.add(
                consultationFeeField,
                1,
                3
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


            String doctorId =
                    doctorIdField
                            .getText()
                            .trim();

            String doctorName =
                    doctorNameField
                            .getText()
                            .trim();

            String specialization =
                    specializationField
                            .getText()
                            .trim();

            String consultationFeeText =
                    consultationFeeField
                            .getText()
                            .trim();


            String validationError =
                    validateDoctorForm(
                            doctorId,
                            doctorName,
                            specialization,
                            consultationFeeText
                    );


            if (validationError != null) {

                showWarning(
                        "Invalid Doctor Information",
                        validationError
                );

                continue;
            }


            BigDecimal consultationFee =
                    new BigDecimal(
                            consultationFeeText
                    );


            return new DoctorFormResult(
                    doctorId,
                    doctorName,
                    specialization,
                    consultationFee
            );
        }
    }


    private String validateDoctorForm(
            String doctorId,
            String doctorName,
            String specialization,
            String consultationFeeText
    ) {

        if (doctorId.isBlank()) {

            return "Doctor ID is required.";
        }


        if (
                !InputValidator
                        .isValidDoctorId(doctorId)
        ) {

            return "Doctor ID must follow the format D###, for example D001.";
        }


        if (doctorName.isBlank()) {

            return "Doctor name is required.";
        }


        if (specialization.isBlank()) {

            return "Specialization is required.";
        }


        if (consultationFeeText.isBlank()) {

            return "Consultation fee is required.";
        }


        try {

            BigDecimal consultationFee =
                    new BigDecimal(
                            consultationFeeText
                    );


            if (
                    consultationFee.compareTo(
                            BigDecimal.ZERO
                    ) <= 0
            ) {

                return "Consultation fee must be greater than zero.";
            }

        } catch (NumberFormatException exception) {

            return "Consultation fee must be a valid number.";
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


    private record DoctorFormResult(
            String doctorId,
            String doctorName,
            String specialization,
            BigDecimal consultationFee
    ) {
    }
}