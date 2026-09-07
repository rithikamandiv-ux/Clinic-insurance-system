package com.rithika.clinicsystem.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;
import java.util.Objects;

public final class ThemeManager {

    private static final String STYLESHEET =
            "/styles/app.css";


    private ThemeManager() {
    }


    public static void apply(
            Scene scene
    ) {

        String stylesheet =
                getStylesheet();

        if (
                !scene
                        .getStylesheets()
                        .contains(stylesheet)
        ) {

            scene
                    .getStylesheets()
                    .add(stylesheet);
        }
    }


    public static void apply(
            Parent parent
    ) {

        String stylesheet =
                getStylesheet();

        if (
                !parent
                        .getStylesheets()
                        .contains(stylesheet)
        ) {

            parent
                    .getStylesheets()
                    .add(stylesheet);
        }
    }


    private static String getStylesheet() {

        URL stylesheet =
                Objects.requireNonNull(
                        ThemeManager.class
                                .getResource(
                                        STYLESHEET
                                ),
                        "Application stylesheet not found: "
                                + STYLESHEET
                );

        return stylesheet
                .toExternalForm();
    }
}