package com.example.socialmedia;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow; // <-- This was likely missing
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginScreen {

    private HelloApplication app;
    private Stage stage;
    private Label messageLabel;

    // --- INSTAGRAM-STYLE DARK BLUE THEME ---
    private final String CSS_STYLES = """
        .root { -fx-background-color: #0B1437; }
        
        .login-card { 
            -fx-background-color: #111C44; 
            -fx-border-color: #2B3674; 
            -fx-border-width: 1;
            -fx-border-radius: 3;
            -fx-background-radius: 3;
        }

        .logo-text { 
            -fx-font-family: 'Segoe UI', cursive; 
            -fx-font-size: 32px; 
            -fx-font-weight: bold; 
            -fx-text-fill: white; 
            -fx-padding: 10 0 20 0;
        }
        
        .divider-text { -fx-text-fill: #A3AED0; -fx-font-weight: bold; -fx-font-size: 12px; }

        .input-field { 
            -fx-background-color: #0B1437; 
            -fx-border-color: #2B3674; 
            -fx-border-radius: 5; 
            -fx-padding: 9; 
            -fx-text-fill: white; 
            -fx-font-size: 13px;
        }
        .input-field:focused { -fx-border-color: #4318FF; }

        .primary-btn { 
            -fx-background-color: #4318FF; 
            -fx-text-fill: white; 
            -fx-font-weight: bold; 
            -fx-font-size: 13px;
            -fx-cursor: hand; 
            -fx-background-radius: 5; 
            -fx-padding: 8 10;
        }
        .primary-btn:hover { -fx-background-color: #3311CC; }

        .link-text { -fx-fill: #4318FF; -fx-font-weight: bold; -fx-cursor: hand; }
        .link-text:hover { -fx-fill: white; }
        
        .plain-text { -fx-fill: white; -fx-font-size: 13px; }

        .error-msg { -fx-text-fill: #FF5B5B; -fx-font-size: 12px; -fx-padding: 10 0; }
        .success-msg { -fx-text-fill: #01B574; -fx-font-size: 12px; -fx-padding: 10 0; }
    """;

    public LoginScreen(HelloApplication app) {
        this.app = app;
    }

    public void show(Stage stage) {
        this.stage = stage;
        showLoginView();
    }

    // --- VIEW 1: LOGIN ---
    private void showLoginView() {
        Label logo = new Label("Socialify");
        logo.getStyleClass().add("logo-text");

        TextField userField = new TextField(); userField.setPromptText("Username"); userField.getStyleClass().add("input-field");
        PasswordField passField = new PasswordField(); passField.setPromptText("Password"); passField.getStyleClass().add("input-field");

        Button loginBtn = new Button("Log In");
        loginBtn.getStyleClass().add("primary-btn");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnAction(e -> handleLogin(userField.getText(), passField.getText()));

        messageLabel = new Label();

        HBox divider = createDivider();

        Hyperlink forgotPass = new Hyperlink("Forgot password?");
        forgotPass.setStyle("-fx-text-fill: #A3AED0; -fx-font-size: 11px;");

        VBox topBox = new VBox(15, logo, messageLabel, userField, passField, loginBtn, divider, forgotPass);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(30, 40, 30, 40));
        topBox.getStyleClass().add("login-card");
        topBox.setMaxWidth(350);

        Text noAccount = new Text("Don't have an account? ");
        noAccount.getStyleClass().add("plain-text");

        Text signUpLink = new Text("Sign up");
        signUpLink.getStyleClass().add("link-text");
        signUpLink.setOnMouseClicked(e -> showRegisterView());

        TextFlow signUpFlow = new TextFlow(noAccount, signUpLink);
        signUpFlow.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        VBox bottomBox = new VBox(signUpFlow);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));
        bottomBox.getStyleClass().add("login-card");
        bottomBox.setMaxWidth(350);

        VBox mainLayout = new VBox(15, topBox, bottomBox);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getStyleClass().add("root");

        Scene scene = new Scene(mainLayout, 900, 800);
        scene.getStylesheets().add("data:text/css," + CSS_STYLES.replaceAll("\n", ""));
        stage.setScene(scene);
        stage.setTitle("Socialify • Login");
        stage.show();
    }

    // --- VIEW 2: REGISTER ---
    private void showRegisterView() {
        Label logo = new Label("Socialify");
        logo.getStyleClass().add("logo-text");

        Label sub = new Label("Sign up to see photos from your friends.");
        sub.setStyle("-fx-text-fill: #A3AED0; -fx-font-weight: bold; -fx-font-size: 14px; -fx-wrap-text: true; -fx-text-alignment: center;");

        TextField userField = new TextField(); userField.setPromptText("Username"); userField.getStyleClass().add("input-field");
        PasswordField passField = new PasswordField(); passField.setPromptText("Password"); passField.getStyleClass().add("input-field");
        TextField bioField = new TextField(); bioField.setPromptText("Full Name / Bio"); bioField.getStyleClass().add("input-field");

        Button regBtn = new Button("Sign Up");
        regBtn.getStyleClass().add("primary-btn");
        regBtn.setMaxWidth(Double.MAX_VALUE);
        regBtn.setOnAction(e -> handleRegister(userField.getText(), passField.getText(), bioField.getText()));

        messageLabel = new Label();

        Label terms = new Label("By signing up, you agree to our Terms.");
        terms.setStyle("-fx-text-fill: #A3AED0; -fx-font-size: 11px;");

        VBox topBox = new VBox(15, logo, sub, userField, passField, bioField, regBtn, messageLabel, terms);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(30, 40, 30, 40));
        topBox.getStyleClass().add("login-card");
        topBox.setMaxWidth(350);

        Text haveAccount = new Text("Have an account? ");
        haveAccount.getStyleClass().add("plain-text");

        Text loginLink = new Text("Log in");
        loginLink.getStyleClass().add("link-text");
        loginLink.setOnMouseClicked(e -> showLoginView());

        TextFlow loginFlow = new TextFlow(haveAccount, loginLink);
        loginFlow.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        VBox bottomBox = new VBox(loginFlow);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20));
        bottomBox.getStyleClass().add("login-card");
        bottomBox.setMaxWidth(350);

        VBox mainLayout = new VBox(15, topBox, bottomBox);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getStyleClass().add("root");

        stage.getScene().setRoot(mainLayout);
    }

    private HBox createDivider() {
        Separator l1 = new Separator(); HBox.setHgrow(l1, Priority.ALWAYS);
        Label or = new Label("OR"); or.getStyleClass().add("divider-text");
        Separator l2 = new Separator(); HBox.setHgrow(l2, Priority.ALWAYS);
        HBox div = new HBox(10, l1, or, l2);
        div.setAlignment(Pos.CENTER);
        return div;
    }

    private void handleLogin(String user, String pass) {
        if(user.isEmpty() || pass.isEmpty()) { showError("Please fill in all fields."); return; }
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement p = c.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            p.setString(1, user); p.setString(2, pass);
            if (p.executeQuery().next()) {
                app.showDashboard(user); // <--- ERROR HERE? CHECK HelloApplication.java below
            } else {
                showError("Sorry, your password was incorrect.");
            }
        } catch (Exception e) { e.printStackTrace(); showError("Connection error."); }
    }

    private void handleRegister(String user, String pass, String bio) {
        if(user.isEmpty() || pass.isEmpty()) { showError("Username/Password required."); return; }
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement check = c.prepareStatement("SELECT user_id FROM users WHERE username = ?")) {
            check.setString(1, user);
            if(check.executeQuery().next()) { showError("Username already taken."); return; }
            try(PreparedStatement insert = c.prepareStatement("INSERT INTO users (username, password, bio) VALUES (?, ?, ?)")) {
                insert.setString(1, user); insert.setString(2, pass); insert.setString(3, bio);
                insert.executeUpdate();
                showSuccess("Account created! Please Log In.");
            }
        } catch (Exception e) { e.printStackTrace(); showError("Registration failed."); }
    }

    private void showError(String msg) {
        messageLabel.setText(msg);
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add("error-msg");
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
    }

    private void showSuccess(String msg) {
        messageLabel.setText(msg);
        messageLabel.getStyleClass().clear();
        messageLabel.getStyleClass().add("success-msg");
    }
}