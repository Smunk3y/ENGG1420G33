package com.example.phase1_1420;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class FacultyDashboardController {
    @FXML private Pane sidebarPane;
    @FXML private Button toggleButton;
    @FXML private StackPane contentArea;
    @FXML private Text UserName;
    @FXML private Text UserID;
    private boolean sidebarVisible = false;
    private final ExcelFile excelReader = new ExcelFile();

    @FXML
    private void initialize() {
        try {
            // Load data from Excel
            excelReader.ReadingNameExcelFile();
            
            // Get current Faculty from UserDatabase
            Faculty currentFaculty = (Faculty) UserDatabase.CurrentUser;
            
            // Set user information
            UserName.setText(currentFaculty.getUsername());
            UserID.setText(currentFaculty.getId());
            
            // Initialize sidebar
            sidebarPane.setTranslateX(-200);
            toggleButton.setText("☰");
            
            // Load initial content
            loadContent("faculty-dashboard-view.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleSidebar() {
        TranslateTransition slide = new TranslateTransition(Duration.millis(300), sidebarPane);

        if (sidebarVisible) {
            slide.setToX(-200);
            toggleButton.setText("☰");
            sidebarVisible = false;
        } else {
            slide.setToX(0);
            toggleButton.setText("≡");
            sidebarVisible = true;
        }
        slide.play();
    }

    @FXML
    private void handleDashboard() { loadContent("faculty-dashboard-view.fxml"); }

    @FXML
    private void handleCourses() { loadContent("course-management-view.fxml"); }

    @FXML
    private void handleSubjects() { loadContent("subject-management-view.fxml"); }

    @FXML
    private void handleEvents() { loadContent("user-events-view.fxml"); }

    @FXML
    private void handleProfileView() {
        loadContent("faculty-profile-view.fxml");
    }


    @FXML
    private void handleStudents() { loadContent("students-view.fxml"); }


    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/phase1_1420/login-view.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) sidebarPane.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login Page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}