package com.rithika.clinicsystem.ui.dashboard;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardView {

    public VBox getView() {

        Label titleLabel =
                new Label("Dashboard");

        titleLabel
                .getStyleClass()
                .add("page-title");


        Label subtitleLabel =
                new Label(
                        "Healthcare Operations & Insurance Management"
                );

        subtitleLabel
                .getStyleClass()
                .add("secondary-text");


        VBox content =
                new VBox(10);

        content.setPadding(
                new Insets(32)
        );

        content
                .getStyleClass()
                .add("page-content");

        content.getChildren().addAll(
                titleLabel,
                subtitleLabel
        );

        return content;
    }
}