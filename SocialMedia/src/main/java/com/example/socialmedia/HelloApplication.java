package com.example.socialmedia;

import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showLoginScreen(); // Start with Login
    }

    public void showLoginScreen() {
        // Instead of building the UI here, just call the new class
        new LoginScreen(this).show(primaryStage);
    }

    // --- ADD THIS NEW METHOD ---
    public void showSignUpScreen() {
        SignUpScreen signUp = new SignUpScreen(this);
        primaryStage.setScene(signUp.getScene());
        primaryStage.setTitle("Create Account");
        primaryStage.show();
    }

    public void showDashboard(String username) {
        Dashboard dashboard = new Dashboard(this);
        dashboard.show(primaryStage, username);
    }

    public static void main(String[] args) {
        launch();
    }
}