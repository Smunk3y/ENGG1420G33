package com.example.phase1_1420;


import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
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
        loadContent("admin-dashboard-view.fxml");
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
    private void handleDashboard() { loadContent("admin-dashboard-view.fxml"); }


    @FXML
    private void handleSubjects() {loadContent("subject-management-view.fxml");}


    @FXML
    private void handleCourses() { loadContent("course-management-view.fxml"); }


    @FXML
    private void handleStudents(ActionEvent event) { loadContent("StudentManagement.fxml"); }


    @FXML
    private void handleFaculty() { loadContent("admin-faculty-view.fxml"); }


    @FXML
    private void handleEvents() { loadContent("event-management-view.fxml"); }


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


