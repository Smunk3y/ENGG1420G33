package com.example.phase1_1420;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class LoginController {
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Label errorLabel;

    private int failedAttempts = 0;
    private static final int MAX_ATTEMPTS = 5;

    @FXML
    private void handleLogin() throws IOException {
        String username = userField.getText();
        String password = passField.getText();

        // Create a new Excel file reader and load all data
        ExcelFile excelFile = new ExcelFile();
        excelFile.ReadingNameExcelFile();
        
        // Ensure the UserDatabase has the latest data
        UserDatabase.refreshUserData(excelFile);

        // Try to authenticate the user
        String role = UserDatabase.authenticate(username, password);

        if (role != null) {
            failedAttempts = 0; // Reset on success
            loadDashboard(role);
        } else {
            failedAttempts++;
            errorLabel.setText("Invalid Login! - (" + failedAttempts + "/" + MAX_ATTEMPTS + " Login Attempts Used)");
            errorLabel.setStyle("-fx-text-fill: red;");

            if (failedAttempts >= MAX_ATTEMPTS) {
                errorLabel.setText("Too many failed Login attempts! Please Try again later. Closing login screen.");

                PauseTransition delay = new PauseTransition(Duration.seconds(5));
                delay.setOnFinished(e -> System.exit(0));
                delay.play();
            }
        }
    }

    private void loadDashboard(String role) {
        try {
            String fxmlFile;
            if( "ADMIN".equals(role)){
                fxmlFile = "admin-dashboard.fxml";
            }else if ("FACULTY".equals(role)){
                fxmlFile = "faculty-dashboard.fxml";
            }else{
                fxmlFile = "user-dashboard.fxml";
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/phase1_1420/" + fxmlFile));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) userField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(role + " Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}