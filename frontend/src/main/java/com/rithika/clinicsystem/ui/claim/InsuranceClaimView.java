package com.rithika.clinicsystem.ui.claim;

import com.rithika.clinicsystem.api.ApiException;
import com.rithika.clinicsystem.api.InsuranceClaimApiService;
import com.rithika.clinicsystem.api.MedicalRecordApiService;
import com.rithika.clinicsystem.dto.InsuranceClaimRequest;
import com.rithika.clinicsystem.dto.InsuranceClaimResponse;
import com.rithika.clinicsystem.dto.MedicalRecordResponse;
import com.rithika.clinicsystem.ui.ThemeManager;


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

public class InsuranceClaimView {

    private final InsuranceClaimApiService insuranceClaimApiService;
    private final MedicalRecordApiService medicalRecordApiService;

    private final BorderPane root;

    private final TableView<InsuranceClaimResponse> claimTable;

    private final ObservableList<InsuranceClaimResponse> claims;

    private final FilteredList<InsuranceClaimResponse> filteredClaims;

    private final ObservableList<MedicalRecordResponse> medicalRecords;

    private Button processButton;


    public InsuranceClaimView(
            InsuranceClaimApiService insuranceClaimApiService,
            MedicalRecordApiService medicalRecordApiService
    ) {

        this.insuranceClaimApiService =
                insuranceClaimApiService;

        this.medicalRecordApiService =
                medicalRecordApiService;

        this.root =
                new BorderPane();

        this.claimTable =
                new TableView<>();

        this.claims =
                FXCollections.observableArrayList();

        this.filteredClaims =
                new FilteredList<>(
                        claims,
                        claim -> true
                );

        this.medicalRecords =
                FXCollections.observableArrayList();

        buildView();

        loadData();
    }


    private void buildView() {

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
                new Label("Insurance Claims");

        titleLabel
                .getStyleClass()
                .add("page-title");


        Label subtitleLabel =
                new Label(
                        "Create and process insurance claims from medical records"
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


        Button createClaimButton =
                new Button("+ Create Claim");

        createClaimButton
                .getStyleClass()
                .add("accent-button");

        createClaimButton.setOnAction(
                event -> showCreateClaimDialog()
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
                        createClaimButton
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
                "Search by claim ID, medical record, patient or status"
        );

        searchField
                .getStyleClass()
                .add("search-field");

        searchField.setMaxWidth(
                480
        );


        searchField
                .textProperty()
                .addListener(
                        (
                                observable,
                                oldValue,
                                newValue
                        ) -> filterClaims(
                                newValue
                        )
                );


        /*
         * ----------------------------------------------------
         * TABLE
         * ----------------------------------------------------
         */

        configureTable();

        claimTable.setItems(
                filteredClaims
        );

        claimTable.setPlaceholder(
                new Label(
                        "No insurance claims found"
                )
        );


        /*
         * ----------------------------------------------------
         * ACTIONS
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


        processButton =
                new Button("Process Claim");

        processButton
                .getStyleClass()
                .add("primary-button");

        processButton.setDisable(
                true
        );

        processButton.setOnAction(
                event -> processSelectedClaim()
        );


        claimTable
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (
                                observable,
                                oldSelection,
                                newSelection
                        ) -> updateProcessButton(
                                newSelection
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
                        actionSpacer,
                        processButton
                );

        actions.setAlignment(
                Pos.CENTER_LEFT
        );


        /*
         * ----------------------------------------------------
         * CONTENT
         * ----------------------------------------------------
         */

        VBox content =
                new VBox(
                        20,
                        header,
                        searchField,
                        claimTable,
                        actions
                );

        VBox.setVgrow(
                claimTable,
                Priority.ALWAYS
        );


        root.setCenter(
                content
        );
    }


    private void configureTable() {

        claimTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );


        /*
         * CLAIM ID
         */

        TableColumn<InsuranceClaimResponse, String>
                claimIdColumn =
                new TableColumn<>(
                        "Claim ID"
                );

        claimIdColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getClaimId()
                        )
        );


        /*
         * MEDICAL RECORD
         */

        TableColumn<InsuranceClaimResponse, String>
                recordColumn =
                new TableColumn<>(
                        "Medical Record"
                );

        recordColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getMedicalRecordId()
                        )
        );


        /*
         * PATIENT
         */

        TableColumn<InsuranceClaimResponse, String>
                patientColumn =
                new TableColumn<>(
                        "Patient"
                );

        patientColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getPatientId()
                        )
        );


        /*
         * CLAIM AMOUNT
         */

        TableColumn<InsuranceClaimResponse, String>
                amountColumn =
                new TableColumn<>(
                        "Claim Amount"
                );

        amountColumn.setCellValueFactory(
                cellData -> {

                    BigDecimal amount =
                            cellData
                                    .getValue()
                                    .getClaimAmount();

                    return new SimpleStringProperty(
                            "Rs. "
                                    + amount.toPlainString()
                    );
                }
        );


        /*
         * STATUS
         */

        TableColumn<InsuranceClaimResponse, String>
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
                                        new Label(
                                                status
                                        );

                                badge
                                        .getStyleClass()
                                        .add(
                                                "status-label"
                                        );


                                switch (
                                        status.toUpperCase()
                                ) {

                                    case "APPROVED" ->
                                            badge
                                                    .getStyleClass()
                                                    .add(
                                                            "status-success"
                                                    );


                                    case "REJECTED" ->
                                            badge
                                                    .getStyleClass()
                                                    .add(
                                                            "status-danger"
                                                    );


                                    default ->
                                            badge
                                                    .getStyleClass()
                                                    .add(
                                                            "status-warning"
                                                    );
                                }


                                setText(null);

                                setGraphic(
                                        badge
                                );

                                setAlignment(
                                        Pos.CENTER_LEFT
                                );
                            }
                        }
        );


        claimTable
                .getColumns()
                .addAll(
                        claimIdColumn,
                        recordColumn,
                        patientColumn,
                        amountColumn,
                        statusColumn
                );
    }


    private void loadData() {

        try {

            List<MedicalRecordResponse> loadedRecords =
                    medicalRecordApiService
                            .getAllMedicalRecords();

            medicalRecords.setAll(
                    loadedRecords
            );


            List<InsuranceClaimResponse> loadedClaims =
                    insuranceClaimApiService
                            .getAllClaims();

            claims.setAll(
                    loadedClaims
            );


            claimTable
                    .getSelectionModel()
                    .clearSelection();

        } catch (ApiException exception) {

            showError(
                    "Unable to Load Insurance Claims",
                    exception.getMessage()
            );
        }
    }


    private void filterClaims(
            String searchText
    ) {

        String search =
                searchText == null
                        ? ""
                        : searchText
                        .trim()
                        .toLowerCase();


        if (search.isEmpty()) {

            filteredClaims.setPredicate(
                    claim -> true
            );

            return;
        }


        filteredClaims.setPredicate(
                claim -> {

                    String claimId =
                            safeLower(
                                    claim.getClaimId()
                            );

                    String recordId =
                            safeLower(
                                    claim.getMedicalRecordId()
                            );

                    String patientId =
                            safeLower(
                                    claim.getPatientId()
                            );

                    String status =
                            safeLower(
                                    claim.getStatus()
                            );


                    return claimId.contains(search)
                            || recordId.contains(search)
                            || patientId.contains(search)
                            || status.contains(search);
                }
        );
    }


    private void updateProcessButton(
            InsuranceClaimResponse claim
    ) {

        boolean pending =
                claim != null
                        && "PENDING"
                        .equalsIgnoreCase(
                                claim.getStatus()
                        );


        processButton.setDisable(
                !pending
        );
    }


    private void showCreateClaimDialog() {

        List<MedicalRecordResponse> eligibleRecords =
                getEligibleMedicalRecords();


        if (eligibleRecords.isEmpty()) {

            showWarning(
                    "No Eligible Medical Records",
                    "Every current medical record already has an insurance claim."
            );

            return;
        }


        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Create Insurance Claim"
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
         * CLAIM ID
         */

        TextField claimIdField =
                new TextField();

        claimIdField.setPromptText(
                "CL001"
        );


        /*
         * MEDICAL RECORD
         */

        ComboBox<MedicalRecordResponse>
                medicalRecordComboBox =
                createMedicalRecordComboBox(
                        eligibleRecords
                );


        /*
         * INFORMATION
         */

        Label amountInformation =
                new Label(
                        "Claim amount is automatically taken from "
                                + "the medical record treatment cost."
                );

        amountInformation
                .getStyleClass()
                .add(
                        "secondary-text"
                );

        amountInformation.setWrapText(
                true
        );


        /*
         * FORM
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
                new Label("Claim ID"),
                0,
                0
        );

        form.add(
                claimIdField,
                1,
                0
        );


        form.add(
                new Label("Medical Record"),
                0,
                1
        );

        form.add(
                medicalRecordComboBox,
                1,
                1
        );


        form.add(
                amountInformation,
                1,
                2
        );


        claimIdField.setPrefWidth(
                380
        );

        medicalRecordComboBox.setPrefWidth(
                380
        );


        dialog
                .getDialogPane()
                .setContent(
                        form
                );


        while (true) {

            Optional<ButtonType> result =
                    dialog.showAndWait();


            if (
                    result.isEmpty()
                            || result.get()
                            != ButtonType.OK
            ) {

                return;
            }


            String claimId =
                    claimIdField
                            .getText()
                            .trim();


            MedicalRecordResponse medicalRecord =
                    medicalRecordComboBox
                            .getValue();


            String validationError =
                    validateClaimForm(
                            claimId,
                            medicalRecord
                    );


            if (validationError != null) {

                showWarning(
                        "Invalid Claim Information",
                        validationError
                );

                continue;
            }


            InsuranceClaimRequest request =
                    new InsuranceClaimRequest(
                            claimId,
                            medicalRecord
                                    .getRecordId()
                    );


            try {

                insuranceClaimApiService
                        .createClaim(
                                request
                        );

                loadData();


                showInformation(
                        "Claim Created",
                        "Insurance claim was created successfully "
                                + "with PENDING status."
                );


                return;

            } catch (ApiException exception) {

                showError(
                        "Unable to Create Claim",
                        exception.getMessage()
                );

                return;
            }
        }
    }


    private ComboBox<MedicalRecordResponse>
    createMedicalRecordComboBox(
            List<MedicalRecordResponse> eligibleRecords
    ) {

        ComboBox<MedicalRecordResponse> comboBox =
                new ComboBox<>(
                        FXCollections.observableArrayList(
                                eligibleRecords
                        )
                );


        comboBox.setPromptText(
                "Select medical record"
        );


        comboBox.setConverter(
                new StringConverter<>() {

                    @Override
                    public String toString(
                            MedicalRecordResponse record
                    ) {

                        if (record == null) {
                            return "";
                        }


                        return record
                                .getRecordId()
                                + " — "
                                + record
                                .getPatientId()
                                + " — Rs. "
                                + record
                                .getTreatmentCost()
                                .toPlainString();
                    }


                    @Override
                    public MedicalRecordResponse fromString(
                            String string
                    ) {

                        return null;
                    }
                }
        );


        return comboBox;
    }


    private List<MedicalRecordResponse>
    getEligibleMedicalRecords() {

        return medicalRecords
                .stream()
                .filter(
                        record ->
                                !hasClaimForMedicalRecord(
                                        record.getRecordId()
                                )
                )
                .toList();
    }


    private boolean hasClaimForMedicalRecord(
            String recordId
    ) {

        return claims
                .stream()
                .anyMatch(
                        claim ->
                                claim
                                        .getMedicalRecordId()
                                        .equals(
                                                recordId
                                        )
                );
    }


    private String validateClaimForm(
            String claimId,
            MedicalRecordResponse medicalRecord
    ) {

        /*
         * Strict CL### validation will be added later
         * together with all other ID formats.
         */

        if (claimId.isBlank()) {

            return "Claim ID is required.";
        }


        if (claimId.length() > 20) {

            return "Claim ID cannot exceed 20 characters.";
        }


        if (medicalRecord == null) {

            return "Select a medical record.";
        }


        return null;
    }


    private void processSelectedClaim() {

        InsuranceClaimResponse selectedClaim =
                claimTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedClaim == null) {
            return;
        }


        if (
                !"PENDING"
                        .equalsIgnoreCase(
                                selectedClaim.getStatus()
                        )
        ) {

            showWarning(
                    "Claim Already Processed",
                    "Only PENDING claims can be processed."
            );

            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Process Insurance Claim"
        );

        confirmation.setHeaderText(
                "Process claim "
                        + selectedClaim
                        .getClaimId()
                        + "?"
        );

        confirmation.setContentText(
                "Patient: "
                        + selectedClaim
                        .getPatientId()
                        + "\nClaim amount: Rs. "
                        + selectedClaim
                        .getClaimAmount()
                        .toPlainString()
                        + "\n\n"
                        + "The system will determine whether "
                        + "the claim should be approved or rejected."
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


        try {

            InsuranceClaimResponse processedClaim =
                    insuranceClaimApiService
                            .processClaim(
                                    selectedClaim
                                            .getClaimId()
                            );


            loadData();


            showInformation(
                    "Claim Processed",
                    "Claim "
                            + processedClaim
                            .getClaimId()
                            + " is now "
                            + processedClaim
                            .getStatus()
                            + "."
            );

        } catch (ApiException exception) {

            showError(
                    "Unable to Process Claim",
                    exception.getMessage()
            );
        }
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
}