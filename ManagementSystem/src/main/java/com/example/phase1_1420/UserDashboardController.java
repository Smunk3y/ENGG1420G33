package com.example.phase1_1420;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;

public class UserDashboardController implements Initializable {
    @FXML private Pane sidebarPane;
    @FXML private Button toggleButton;
    @FXML private StackPane contentArea;
    @FXML private Text studentNameText;
    @FXML private Text studentIdText;
    @FXML private Text academicLevelText;
    @FXML private Text currentSemText;
    @FXML private Text graduationDateText;
    @FXML private TableView<Event> eventsTable;
    @FXML private TableColumn<Event, String> eventNameColumn;
    @FXML private TableColumn<Event, String> eventDateColumn;
    @FXML private TableColumn<Event, String> eventLocationColumn;
    @FXML private Text UserName;
    @FXML private Text UserID;

    private boolean sidebarVisible = false;
    private final ExcelFile excelReader = new ExcelFile();
    private final ObservableList<Event> registeredEvents = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Load data from Excel
            excelReader.ReadingNameExcelFile();
            
            // Initialize sidebar
            sidebarPane.setTranslateX(-200);
            toggleButton.setText("☰");
            
            // Load initial content
            loadContent("user-dashboard-view.fxml");
            
            // Set up user info
            if (UserDatabase.CurrentUser != null) {
                UserName.setText(UserDatabase.CurrentUser.getUsername());
                UserID.setText(UserDatabase.CurrentUser.getId());
            }
        } catch (Exception e) {
            System.err.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateUserInfo() {
        if (UserDatabase.CurrentUser != null) {
            Student currentStudent = (Student) UserDatabase.CurrentUser;
            studentNameText.setText(currentStudent.getUsername());
            studentIdText.setText(currentStudent.getId());
            academicLevelText.setText(currentStudent.getAcademicLevel());
            currentSemText.setText(currentStudent.getCurrentSem());
            graduationDateText.setText("Expected: " + currentStudent.getCurrentSem());
        }
    }

    private void setupRegisteredEventsTable() {
        eventNameColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEventName()));
        eventDateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(extractDate(cellData.getValue().getDateTime())));
        eventLocationColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getLocation()));
    }

    private void loadRegisteredEvents() {
        registeredEvents.clear();
        String currentUserId = UserDatabase.CurrentUser.getId();
        
        for (Event event : excelReader.eventList) {
            String registeredStudents = event.getRegisteredStudents();
            if (registeredStudents != null && registeredStudents.contains(currentUserId)) {
                registeredEvents.add(event);
            }
        }
        
        eventsTable.setItems(registeredEvents);
    }

    private String extractDate(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return "TBA";
        }
        String[] parts = dateTime.split(" ");
        return parts[0];
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
    private void handleDashboard() { loadContent("user-dashboard-view.fxml"); }

    @FXML
    private void handleCourses() { loadContent("course-management-view.fxml"); }

    @FXML
    private void handleSubjects() { loadContent("subject-management-view.fxml"); }

    @FXML
    private void handleEvents() {
        loadContent("event-management-view.fxml");
    }

    @FXML
    private void handleProfile() { loadContent("user-profile-view.fxml"); }

    @FXML
    private void handleFaculty() {
        loadContent("faculty-management-view.fxml");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/phase1_1420/" + fxmlFile));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}