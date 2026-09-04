module com.rithika.clinicsystem {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.rithika.clinicsystem.ui to javafx.fxml;
    exports com.rithika.clinicsystem.ui;
}