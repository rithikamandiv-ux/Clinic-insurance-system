package com.rithika.clinicsystem.ui.policy;

import com.rithika.clinicsystem.api.ApiException;
import com.rithika.clinicsystem.api.InsurancePolicyApiService;
import com.rithika.clinicsystem.api.PatientApiService;
import com.rithika.clinicsystem.dto.InsurancePolicyRequest;
import com.rithika.clinicsystem.dto.InsurancePolicyResponse;
import com.rithika.clinicsystem.dto.PatientResponse;
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

public class InsurancePolicyView {

    private final InsurancePolicyApiService insurancePolicyApiService;
    private final PatientApiService patientApiService;

    private final BorderPane root;

    private final TableView<InsurancePolicyResponse> policyTable;

    private final ObservableList<InsurancePolicyResponse> policies;
    private final FilteredList<InsurancePolicyResponse> filteredPolicies;

    private final ObservableList<PatientResponse> patients;

    private Button editButton;
    private Button deleteButton;


    public InsurancePolicyView(
            InsurancePolicyApiService insurancePolicyApiService,
            PatientApiService patientApiService
    ) {

        this.insurancePolicyApiService =
                insurancePolicyApiService;

        this.patientApiService =
                patientApiService;

        this.root =
                new BorderPane();

        this.policyTable =
                new TableView<>();

        this.policies =
                FXCollections.observableArrayList();

        this.filteredPolicies =
                new FilteredList<>(
                        policies,
                        policy -> true
                );

        this.patients =
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


        Label titleLabel =
                new Label("Insurance Policies");

        titleLabel
                .getStyleClass()
                .add("page-title");


        Label subtitleLabel =
                new Label(
                        "Manage patient insurance policies and coverage"
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


        Button addPolicyButton =
                new Button("+ Add Policy");

        addPolicyButton
                .getStyleClass()
                .add("accent-button");

        addPolicyButton.setOnAction(
                event -> showAddPolicyDialog()
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
                        addPolicyButton
                );

        header.setAlignment(
                Pos.CENTER_LEFT
        );


        TextField searchField =
                new TextField();

        searchField.setPromptText(
                "Search by policy ID, patient, provider or policy type"
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
                        ) -> filterPolicies(newValue)
                );


        configureTable();

        policyTable.setItems(
                filteredPolicies
        );

        policyTable.setPlaceholder(
                new Label("No insurance policies found")
        );


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

        editButton.setDisable(true);

        editButton.setOnAction(
                event -> editSelectedPolicy()
        );


        deleteButton =
                new Button("Delete");

        deleteButton
                .getStyleClass()
                .add("danger-button");

        deleteButton.setDisable(true);

        deleteButton.setOnAction(
                event -> deleteSelectedPolicy()
        );


        policyTable
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (
                                observable,
                                oldSelection,
                                newSelection
                        ) -> {

                            boolean noSelection =
                                    newSelection == null;

                            editButton.setDisable(
                                    noSelection
                            );

                            deleteButton.setDisable(
                                    noSelection
                            );
                        }
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
                        editButton,
                        deleteButton
                );

        actions.setAlignment(
                Pos.CENTER_LEFT
        );


        VBox content =
                new VBox(
                        20,
                        header,
                        searchField,
                        policyTable,
                        actions
                );

        VBox.setVgrow(
                policyTable,
                Priority.ALWAYS
        );


        root.setCenter(
                content
        );
    }


    private void configureTable() {

        policyTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN
        );


        TableColumn<InsurancePolicyResponse, String>
                policyIdColumn =
                new TableColumn<>("Policy ID");

        policyIdColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getPolicyId()
                        )
        );


        TableColumn<InsurancePolicyResponse, String>
                patientColumn =
                new TableColumn<>("Patient");

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


        TableColumn<InsurancePolicyResponse, String>
                providerColumn =
                new TableColumn<>("Provider");

        providerColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getProviderName()
                        )
        );


        TableColumn<InsurancePolicyResponse, String>
                coverageColumn =
                new TableColumn<>("Coverage Amount");

        coverageColumn.setCellValueFactory(
                cellData -> {

                    BigDecimal coverage =
                            cellData
                                    .getValue()
                                    .getCoverageAmount();

                    return new SimpleStringProperty(
                            "Rs. "
                                    + coverage.toPlainString()
                    );
                }
        );


        TableColumn<InsurancePolicyResponse, String>
                typeColumn =
                new TableColumn<>("Policy Type");

        typeColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData
                                        .getValue()
                                        .getPolicyType()
                        )
        );


        policyTable
                .getColumns()
                .addAll(
                        policyIdColumn,
                        patientColumn,
                        providerColumn,
                        coverageColumn,
                        typeColumn
                );
    }


    private void loadData() {

        try {

            List<PatientResponse> loadedPatients =
                    patientApiService
                            .getAllPatients();

            patients.setAll(
                    loadedPatients
            );


            List<InsurancePolicyResponse> loadedPolicies =
                    insurancePolicyApiService
                            .getAllPolicies();

            policies.setAll(
                    loadedPolicies
            );


            policyTable
                    .getSelectionModel()
                    .clearSelection();

        } catch (ApiException exception) {

            showError(
                    "Unable to Load Insurance Policies",
                    exception.getMessage()
            );
        }
    }


    private void filterPolicies(
            String searchText
    ) {

        String search =
                searchText == null
                        ? ""
                        : searchText
                        .trim()
                        .toLowerCase();


        if (search.isEmpty()) {

            filteredPolicies.setPredicate(
                    policy -> true
            );

            return;
        }


        filteredPolicies.setPredicate(
                policy -> {

                    String policyId =
                            safeLower(
                                    policy.getPolicyId()
                            );

                    String patient =
                            safeLower(
                                    getPatientDisplay(
                                            policy.getPatientId()
                                    )
                            );

                    String provider =
                            safeLower(
                                    policy.getProviderName()
                            );

                    String policyType =
                            safeLower(
                                    policy.getPolicyType()
                            );


                    return policyId.contains(search)
                            || patient.contains(search)
                            || provider.contains(search)
                            || policyType.contains(search);
                }
        );
    }


    private void showAddPolicyDialog() {

        List<PatientResponse> eligiblePatients =
                getEligiblePatients(
                        null
                );


        if (eligiblePatients.isEmpty()) {

            showWarning(
                    "No Eligible Patients",
                    "Every patient currently already has an insurance policy."
            );

            return;
        }


        PolicyFormResult formResult =
                showPolicyForm(
                        "Add Insurance Policy",
                        null
                );


        if (formResult == null) {
            return;
        }


        InsurancePolicyRequest request =
                new InsurancePolicyRequest(
                        formResult.policyId(),
                        formResult
                                .patient()
                                .getPatientId(),
                        formResult.providerName(),
                        formResult.coverageAmount(),
                        formResult.policyType()
                );


        try {

            insurancePolicyApiService
                    .addPolicy(
                            request
                    );

            loadData();

            showInformation(
                    "Policy Added",
                    "Insurance policy was added successfully."
            );

        } catch (ApiException exception) {

            showError(
                    "Unable to Add Policy",
                    exception.getMessage()
            );
        }
    }


    private void editSelectedPolicy() {

        InsurancePolicyResponse selectedPolicy =
                policyTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedPolicy == null) {
            return;
        }


        PolicyFormResult formResult =
                showPolicyForm(
                        "Edit Insurance Policy",
                        selectedPolicy
                );


        if (formResult == null) {
            return;
        }


        InsurancePolicyRequest request =
                new InsurancePolicyRequest(
                        selectedPolicy
                                .getPolicyId(),
                        selectedPolicy
                                .getPatientId(),
                        formResult.providerName(),
                        formResult.coverageAmount(),
                        formResult.policyType()
                );


        try {

            insurancePolicyApiService
                    .updatePolicy(
                            selectedPolicy
                                    .getPolicyId(),
                            request
                    );

            loadData();

            showInformation(
                    "Policy Updated",
                    "Insurance policy was updated successfully."
            );

        } catch (ApiException exception) {

            showError(
                    "Unable to Update Policy",
                    exception.getMessage()
            );
        }
    }


    private void deleteSelectedPolicy() {

        InsurancePolicyResponse selectedPolicy =
                policyTable
                        .getSelectionModel()
                        .getSelectedItem();


        if (selectedPolicy == null) {
            return;
        }


        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Insurance Policy"
        );

        confirmation.setHeaderText(
                "Delete policy "
                        + selectedPolicy
                        .getPolicyId()
                        + "?"
        );

        confirmation.setContentText(
                "Patient: "
                        + selectedPolicy
                        .getPatientId()
                        + "\nProvider: "
                        + selectedPolicy
                        .getProviderName()
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


        try {

            insurancePolicyApiService
                    .deletePolicy(
                            selectedPolicy
                                    .getPolicyId()
                    );

            loadData();

            showInformation(
                    "Policy Deleted",
                    "Insurance policy was deleted successfully."
            );

        } catch (ApiException exception) {

            showError(
                    "Unable to Delete Policy",
                    exception.getMessage()
            );
        }
    }


    private PolicyFormResult showPolicyForm(
            String title,
            InsurancePolicyResponse existingPolicy
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


        TextField policyIdField =
                new TextField();

        policyIdField.setPromptText(
                "POL001"
        );


        ComboBox<PatientResponse> patientComboBox =
                createPatientComboBox(
                        existingPolicy
                );


        TextField providerNameField =
                new TextField();

        providerNameField.setPromptText(
                "Insurance provider"
        );


        TextField coverageAmountField =
                new TextField();

        coverageAmountField.setPromptText(
                "0.00"
        );


        TextField policyTypeField =
                new TextField();

        policyTypeField.setPromptText(
                "Policy type"
        );


        if (existingPolicy != null) {

            policyIdField.setText(
                    existingPolicy
                            .getPolicyId()
            );

            policyIdField.setDisable(
                    true
            );


            providerNameField.setText(
                    existingPolicy
                            .getProviderName()
            );


            coverageAmountField.setText(
                    existingPolicy
                            .getCoverageAmount()
                            .toPlainString()
            );


            policyTypeField.setText(
                    existingPolicy
                            .getPolicyType()
            );
        }


        GridPane form =
                new GridPane();

        form.setHgap(14);
        form.setVgap(14);

        form.setPadding(
                new Insets(20)
        );


        form.add(
                new Label("Policy ID"),
                0,
                0
        );

        form.add(
                policyIdField,
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
                new Label("Provider Name"),
                0,
                2
        );

        form.add(
                providerNameField,
                1,
                2
        );


        form.add(
                new Label("Coverage Amount"),
                0,
                3
        );

        form.add(
                coverageAmountField,
                1,
                3
        );


        form.add(
                new Label("Policy Type"),
                0,
                4
        );

        form.add(
                policyTypeField,
                1,
                4
        );


        policyIdField.setPrefWidth(
                320
        );

        patientComboBox.setPrefWidth(
                320
        );

        providerNameField.setPrefWidth(
                320
        );

        coverageAmountField.setPrefWidth(
                320
        );

        policyTypeField.setPrefWidth(
                320
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

                return null;
            }


            String policyId =
                    policyIdField
                            .getText()
                            .trim();


            PatientResponse patient =
                    patientComboBox
                            .getValue();


            String providerName =
                    providerNameField
                            .getText()
                            .trim();


            String coverageAmountText =
                    coverageAmountField
                            .getText()
                            .trim();


            String policyType =
                    policyTypeField
                            .getText()
                            .trim();


            String validationError =
                    validatePolicyForm(
                            policyId,
                            patient,
                            providerName,
                            coverageAmountText,
                            policyType
                    );


            if (validationError != null) {

                showWarning(
                        "Invalid Policy Information",
                        validationError
                );

                continue;
            }


            BigDecimal coverageAmount =
                    new BigDecimal(
                            coverageAmountText
                    );


            return new PolicyFormResult(
                    policyId,
                    patient,
                    providerName,
                    coverageAmount,
                    policyType
            );
        }
    }


    private ComboBox<PatientResponse> createPatientComboBox(
            InsurancePolicyResponse existingPolicy
    ) {

        String currentPatientId =
                existingPolicy == null
                        ? null
                        : existingPolicy
                        .getPatientId();


        ObservableList<PatientResponse>
                eligiblePatients =
                FXCollections.observableArrayList(
                        getEligiblePatients(
                                currentPatientId
                        )
                );


        ComboBox<PatientResponse> comboBox =
                new ComboBox<>(
                        eligiblePatients
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


                        return patient
                                .getPatientId()
                                + " — "
                                + patient
                                .getPatientName();
                    }


                    @Override
                    public PatientResponse fromString(
                            String string
                    ) {

                        return null;
                    }
                }
        );


        if (existingPolicy != null) {

            PatientResponse patient =
                    findPatientById(
                            existingPolicy
                                    .getPatientId()
                    );


            comboBox.setValue(
                    patient
            );

            comboBox.setDisable(
                    true
            );
        }


        return comboBox;
    }


    private List<PatientResponse> getEligiblePatients(
            String currentPatientId
    ) {

        return patients
                .stream()
                .filter(
                        patient -> {

                            String patientId =
                                    patient
                                            .getPatientId();


                            if (
                                    currentPatientId != null
                                            && currentPatientId
                                            .equals(
                                                    patientId
                                            )
                            ) {

                                return true;
                            }


                            return !hasPolicyForPatient(
                                    patientId
                            );
                        }
                )
                .toList();
    }


    private boolean hasPolicyForPatient(
            String patientId
    ) {

        return policies
                .stream()
                .anyMatch(
                        policy ->
                                policy
                                        .getPatientId()
                                        .equals(
                                                patientId
                                        )
                );
    }


    private PatientResponse findPatientById(
            String patientId
    ) {

        return patients
                .stream()
                .filter(
                        patient ->
                                patient
                                        .getPatientId()
                                        .equals(
                                                patientId
                                        )
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


        return patient
                .getPatientId()
                + " — "
                + patient
                .getPatientName();
    }


    private String validatePolicyForm(
            String policyId,
            PatientResponse patient,
            String providerName,
            String coverageAmountText,
            String policyType
    ) {

        if (policyId.isBlank()) {

            return "Policy ID is required.";
        }


        if (policyId.length() > 20) {

            return "Policy ID cannot exceed 20 characters.";
        }


        if (patient == null) {

            return "Select a patient.";
        }


        if (providerName.isBlank()) {

            return "Provider name is required.";
        }


        if (providerName.length() > 100) {

            return "Provider name cannot exceed 100 characters.";
        }


        if (coverageAmountText.isBlank()) {

            return "Coverage amount is required.";
        }


        try {

            BigDecimal coverageAmount =
                    new BigDecimal(
                            coverageAmountText
                    );


            if (
                    coverageAmount.compareTo(
                            BigDecimal.ZERO
                    ) <= 0
            ) {

                return "Coverage amount must be greater than zero.";
            }


            if (coverageAmount.scale() > 2) {

                return "Coverage amount can have at most 2 decimal places.";
            }


            int integerDigits =
                    coverageAmount
                            .precision()
                            - coverageAmount
                            .scale();


            if (integerDigits > 10) {

                return "Coverage amount can have at most 10 integer digits.";
            }

        } catch (NumberFormatException exception) {

            return "Coverage amount must be a valid number.";
        }


        if (policyType.isBlank()) {

            return "Policy type is required.";
        }


        if (policyType.length() > 100) {

            return "Policy type cannot exceed 100 characters.";
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


    private record PolicyFormResult(
            String policyId,
            PatientResponse patient,
            String providerName,
            BigDecimal coverageAmount,
            String policyType
    ) {
    }
}