package com.example.socialmedia;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Toast {
    public static void show(String message, Stage stage, boolean isSuccess) {
        Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        Label label = new Label(message);
        label.setStyle("-fx-background-color: " + (isSuccess ? "#01B574" : "#FF5B5B") + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10px 20px;" +
                "-fx-background-radius: 20px;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 2);");

        popup.getContent().add(label);

        popup.setOnShown(e -> {
            popup.setX(stage.getX() + stage.getWidth() / 2 - label.getWidth() / 2);
            popup.setY(stage.getY() + stage.getHeight() - 100);
        });

        popup.show(stage);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(label.opacityProperty(), 0.0)),
                new KeyFrame(Duration.millis(200), new KeyValue(label.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(2.5), new KeyValue(label.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(3.0), new KeyValue(label.opacityProperty(), 0.0))
        );
        timeline.setOnFinished(e -> popup.hide());
        timeline.play();
    }
}