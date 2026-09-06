module com.rithika.clinicsystem {

    requires javafx.controls;
    requires javafx.fxml;

    requires java.net.http;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.rithika.clinicsystem.dto
            to com.fasterxml.jackson.databind;

    opens com.rithika.clinicsystem.ui to javafx.fxml;

    exports com.rithika.clinicsystem.ui;
}