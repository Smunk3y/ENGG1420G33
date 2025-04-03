package com.example.phase1_1420;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

public class FacultyProfileController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField departmentField;
    @FXML private TextField researchField;
    @FXML private PasswordField passwordField;

    private Faculty currentFaculty;

    @FXML
    public void initialize() {
        currentFaculty = (Faculty) UserDatabase.CurrentUser;

        nameField.setText(currentFaculty.getUsername());
        emailField.setText(currentFaculty.getEmail());
        departmentField.setText(currentFaculty.getDepartment());
        researchField.setText(currentFaculty.getResearchArea());
        passwordField.setText(currentFaculty.getPassword());
    }

    @FXML
    private void handleUpdate() {
        currentFaculty.setUsername(nameField.getText().trim());
        currentFaculty.setEmail(emailField.getText().trim());
        currentFaculty.setDepartment(departmentField.getText().trim());
        currentFaculty.setResearchArea(researchField.getText().trim());
        currentFaculty.setPassword(passwordField.getText().trim());

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Profile Updated");
        alert.setHeaderText(null);
        alert.setContentText("Your profile has been successfully updated.");
        alert.showAndWait();
    }
}

