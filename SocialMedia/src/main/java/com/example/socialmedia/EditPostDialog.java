package com.example.socialmedia;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditPostDialog {

    private final int postId;
    private final String currentCaption;
    private final Runnable onSuccess; // Callback to refresh the feed

    // --- DARK THEME STYLES ---
    private final String DARK_BG = "-fx-background-color: #1c1c1c;"; // Slightly lighter than main bg
    private final String TEXT_WHITE = "-fx-text-fill: white;";
    private final String TEXT_AREA_STYLE = "-fx-control-inner-background: #262626; -fx-background-color: #262626; -fx-text-fill: white; -fx-highlight-fill: #0095f6; -fx-highlight-text-fill: white;";
    private final String BTN_BLUE = "-fx-background-color: #0095f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 4;";
    private final String BTN_CANCEL = "-fx-background-color: transparent; -fx-text-fill: #ed4956; -fx-font-weight: bold; -fx-cursor: hand;";

    public EditPostDialog(int postId, String currentCaption, Runnable onSuccess) {
        this.postId = postId;
        this.currentCaption = currentCaption;
        this.onSuccess = onSuccess;
    }

    public void show() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
        window.setTitle("Edit Post");

        // 1. Header Label
        Label headerLabel = new Label("Edit Caption");
        headerLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18px; -fx-font-weight: bold; " + TEXT_WHITE);

        // 2. Input Area
        TextArea captionInput = new TextArea(currentCaption);
        captionInput.setPromptText("Write a caption...");
        captionInput.setWrapText(true);
        captionInput.setPrefRowCount(5);
        captionInput.setStyle(TEXT_AREA_STYLE);

        // 3. Action Buttons
        Button saveBtn = new Button("Done");
        saveBtn.setStyle(BTN_BLUE);
        saveBtn.setPrefWidth(80);
        saveBtn.setOnAction(e -> {
            if (updatePostInDatabase(captionInput.getText())) {
                window.close();
                if (onSuccess != null) onSuccess.run(); // Refresh the main feed
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle(BTN_CANCEL);
        cancelBtn.setOnAction(e -> window.close());

        HBox buttonBox = new HBox(15, cancelBtn, saveBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        // 4. Layout
        VBox layout = new VBox(20, headerLabel, captionInput, buttonBox);
        layout.setPadding(new Insets(20));
        layout.setStyle(DARK_BG);

        Scene scene = new Scene(layout, 400, 250);
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();
    }

    // Database Logic
    private boolean updatePostInDatabase(String newCaption) {
        String query = "UPDATE post SET caption = ? WHERE post_id = ?";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(query)) {

            p.setString(1, newCaption);
            p.setInt(2, postId);

            int rowsAffected = p.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}