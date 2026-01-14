package com.example.socialmedia;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SignUpScreen {

    private HelloApplication app;

    // --- DARK THEME STYLES ---
    private final String DARK_BG = "-fx-background-color: #000000;";
    private final String INPUT_STYLE = "-fx-background-color: #121212; -fx-text-fill: white; -fx-border-color: #363636; -fx-border-radius: 3; -fx-padding: 8;";
    private final String BTN_BLUE = "-fx-background-color: #0095f6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 4; -fx-cursor: hand;";
    private final String LINK_STYLE = "-fx-text-fill: #0095f6; -fx-background-color: transparent; -fx-cursor: hand;";
    private final String TEXT_WHITE = "-fx-text-fill: white;";

    public SignUpScreen(HelloApplication app) {
        this.app = app;
    }

    public Scene getScene() {
        // 1. Header
        Label title = new Label("Sign Up");
        title.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 24px; -fx-font-weight: bold; " + TEXT_WHITE);

        Label subtitle = new Label("Sign up to see photos from your friends.");
        subtitle.setStyle("-fx-text-fill: #a8a8a8; -fx-font-size: 14px;");

        // 2. Input Fields
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setStyle(INPUT_STYLE);
        emailField.setMaxWidth(300);

        TextField userField = new TextField();
        userField.setPromptText("Username");
        userField.setStyle(INPUT_STYLE);
        userField.setMaxWidth(300);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setStyle(INPUT_STYLE);
        passField.setMaxWidth(300);

        // 3. Register Button
        Button registerBtn = new Button("Sign Up");
        registerBtn.setStyle(BTN_BLUE);
        registerBtn.setMaxWidth(300);
        registerBtn.setPadding(new Insets(8));
        registerBtn.setOnAction(e -> handleRegistration(userField.getText(), passField.getText(), emailField.getText()));

        // 4. Back to Login
        Button loginLink = new Button("Have an account? Log in");
        loginLink.setStyle(LINK_STYLE);
        loginLink.setOnAction(e -> app.showLoginScreen());

        VBox layout = new VBox(15, title, subtitle, emailField, userField, passField, registerBtn, new Separator(), loginLink);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle(DARK_BG);

        return new Scene(layout, 400, 550);
    }

    private void handleRegistration(String username, String password, String email) {
        if(username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        String query = "INSERT INTO users (username, password, email, bio) VALUES (?, ?, ?, 'New to Social Media')";

        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement(query)) {

            p.setString(1, username);
            p.setString(2, password);
            p.setString(3, email);
            p.executeUpdate();

            // On success, show alert then go to login
            showAlert("Success", "Account created successfully!");
            app.showLoginScreen();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Username or Email likely already exists.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
