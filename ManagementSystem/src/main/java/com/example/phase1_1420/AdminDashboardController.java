package com.example.phase1_1420;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class AdminDashboardController {
    @FXML private Pane sidebarPane;
    @FXML private Button toggleButton;
    @FXML private StackPane contentArea;
    @FXML private Text UserName;
    @FXML private Text UserID;
    private boolean sidebarVisible = false;

    @FXML
    private void initialize() {
        sidebarPane.setTranslateX(-200);
        toggleButton.setText("☰");
        loadContent("/com/example/phase1_1420/admin-dashboard-view.fxml");
        UserName.setText(UserDatabase.CurrentUser.getUsername());
        UserID.setText(UserDatabase.CurrentUser.getId());
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
    private void handleDashboard() { loadContent("/com/example/phase1_1420/admin-dashboard-view.fxml"); }

    @FXML
    private void handleSubjects() { loadContent("/com/example/phase1_1420/subject-management-view.fxml"); }

    @FXML
    private void handleCourses() { loadContent("/com/example/phase1_1420/course-management-view.fxml"); }

    @FXML
    private void handleStudents() { loadContent("/com/example/phase1_1420/admin-students-view.fxml"); }

    @FXML
    private void handleFaculty() { loadContent("/com/example/phase1_1420/admin-faculty-view.fxml"); }

    @FXML
    private void handleEvents() { 
        loadContent("/com/example/phase1_1420/admin-event-management-view.fxml"); 
    }

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
            System.out.println("Loading content: " + fxmlFile);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            
            if (loader.getLocation() == null) {
                System.err.println("ERROR: Could not find resource: " + fxmlFile);
                showAlert("Resource Not Found", 
                          "Could not load: " + fxmlFile, 
                          Alert.AlertType.ERROR);
                return;
            }
            
            contentArea.getChildren().clear();
            Parent content = loader.load();
            contentArea.getChildren().add(content);
            System.out.println("Successfully loaded: " + fxmlFile);
        } catch (IOException e) {
            System.err.println("Error loading content: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to load content: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

