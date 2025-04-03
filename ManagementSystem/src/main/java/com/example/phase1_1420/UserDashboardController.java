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
        System.out.println("\n=== Loading Registered Events ===");
        
        try {
            if (eventsTable == null) {
                System.err.println("ERROR: eventsTable is null, cannot load registered events");
                return;
            }
            
            if (UserDatabase.CurrentUser == null) {
                System.err.println("ERROR: CurrentUser is null, cannot load registered events");
                return;
            }
            
            System.out.println("Current Student: " + UserDatabase.CurrentUser.getUsername());
            
            registeredEvents.clear();
            String currentUsername = UserDatabase.CurrentUser.getUsername();
            
            System.out.println("Total events to check: " + excelReader.eventList.size());
            
            for (Event event : excelReader.eventList) {
                System.out.println("\nChecking event: " + event.getEventName());
                
                // Check if this registration was explicitly removed during this session
                if (UserDatabase.wasRegistrationRemoved(event.getEventID(), currentUsername)) {
                    System.out.println("× Registration was explicitly removed for this event during this session");
                    continue; // Skip this event
                }
                
                String registeredStudents = event.getRegisteredStudents();
                System.out.println("Registered students string: '" + registeredStudents + "'");
                
                if (registeredStudents != null && !registeredStudents.isEmpty()) {
                    String[] students = registeredStudents.split(",");
                    System.out.println("Split students:");
                    for (String student : students) {
                        String trimmedStudent = student.trim();
                        System.out.println("- Checking against: '" + trimmedStudent + "'");
                        if (trimmedStudent.equals(currentUsername)) {
                            System.out.println("✓ Match found! Adding event: " + event.getEventName());
                            registeredEvents.add(event);
                            break;
                        }
                    }
                }
            }
            
            System.out.println("\nTotal registered events found: " + registeredEvents.size());
            System.out.println("Setting table items...");
            
            eventsTable.setItems(registeredEvents);
            
            System.out.println("Table items set successfully");
        } catch (Exception e) {
            System.err.println("Error in loadRegisteredEvents: " + e.getMessage());
            e.printStackTrace();
        }
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
    private void handleFaculty() { loadContent("faculty-view.fxml");}

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
            Parent content = loader.load();
            contentArea.getChildren().add(content);
            
            // If loading the dashboard view, initialize its elements
            if (fxmlFile.equals("user-dashboard-view.fxml")) {
                System.out.println("Initializing dashboard content...");
                
                // Find and retrieve UI elements
                studentNameText = (Text) content.lookup("#studentNameText");
                studentIdText = (Text) content.lookup("#studentIdText");
                academicLevelText = (Text) content.lookup("#academicLevelText");
                currentSemText = (Text) content.lookup("#currentSemText");
                graduationDateText = (Text) content.lookup("#graduationDateText");
                eventsTable = (TableView<Event>) content.lookup("#eventsTable");
                
                if (eventsTable != null) {
                    eventNameColumn = (TableColumn<Event, String>) eventsTable.getColumns().get(0);
                    eventDateColumn = (TableColumn<Event, String>) eventsTable.getColumns().get(1);
                    eventLocationColumn = (TableColumn<Event, String>) eventsTable.getColumns().get(2);
                    
                    // Set up tables and load data
                    setupRegisteredEventsTable();
                    loadRegisteredEvents();
                } else {
                    System.err.println("ERROR: Could not find eventsTable in the loaded FXML");
                }
                
                // Find courses table if it exists
                TableView<Course> coursesTable = (TableView<Course>) content.lookup("#coursesTable");
                if (coursesTable != null) {
                    System.out.println("Found courses table, setting up courses...");
                    loadEnrolledCourses(coursesTable);
                } else {
                    System.err.println("ERROR: Could not find coursesTable in the loaded FXML");
                }
                
                // Update user information display
                updateUserInfo();
                
                System.out.println("Dashboard content initialized");
            }
        } catch (IOException e) {
            System.err.println("Error loading content: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadEnrolledCourses(TableView<Course> coursesTable) {
        try {
            if (UserDatabase.CurrentUser == null) {
                System.err.println("ERROR: CurrentUser is null, cannot load enrolled courses");
                return;
            }
            
            Student currentStudent = (Student) UserDatabase.CurrentUser;
            System.out.println("Current Student: " + currentStudent.getUsername());
            String studentSubjectsStr = currentStudent.getSubjects();
            
            System.out.println("Student Subjects: " + studentSubjectsStr);
            
            ObservableList<Course> enrolledCourses = FXCollections.observableArrayList();
            
            // Check if student has any subjects
            if (studentSubjectsStr == null || studentSubjectsStr.trim().isEmpty()) {
                System.out.println("Student has no registered subjects");
                return;
            }
            
            // Parse the student's subjects
            String[] studentSubjects = studentSubjectsStr.split(",");
            for (int i = 0; i < studentSubjects.length; i++) {
                studentSubjects[i] = studentSubjects[i].trim().toUpperCase();
                System.out.println("Subject " + (i+1) + ": " + studentSubjects[i]);
            }
            
            // Create a set to track processed subjects (to avoid duplicates)
            java.util.Set<String> processedSubjects = new java.util.HashSet<>();
            
            // First pass: Match existing courses with student's subjects
            for (Course course : excelReader.courseList) {
                String courseSubjectCode = course.getSubjectCode().trim().toUpperCase();
                System.out.println("\nChecking course: " + course.getCourseName());
                System.out.println("Subject code: '" + courseSubjectCode + "'");
                
                // Check each of the student's subjects
                for (String studentSubject : studentSubjects) {
                    if (studentSubject.isEmpty()) continue;
                    
                    System.out.println("Comparing with student subject: '" + studentSubject + "'");
                    
                    // Check for exact match
                    if (courseSubjectCode.equals(studentSubject)) {
                        // Only add if we haven't processed this subject yet
                        if (!processedSubjects.contains(courseSubjectCode)) {
                            System.out.println("✓ Match found! Adding course: " + course.getCourseName());
                            enrolledCourses.add(course);
                            processedSubjects.add(courseSubjectCode);
                            break;
                        }
                    }
                }
            }
            
            // Second pass: Add placeholders for subjects without courses
            for (String studentSubject : studentSubjects) {
                if (studentSubject.trim().isEmpty()) continue;
                
                if (!processedSubjects.contains(studentSubject)) {
                    System.out.println("Creating placeholder for subject: " + studentSubject);
                    
                    // Create a basic course entry
                    Course basicCourse = new Course(
                        studentSubject,
                        "No Course Found - See Advisor",
                        "Not Assigned",
                        "TBA"
                    );
                    basicCourse.setSubjectCode(studentSubject);
                    
                    enrolledCourses.add(basicCourse);
                    processedSubjects.add(studentSubject);
                }
            }
            
            // Set up the table columns if not already set
            if (coursesTable.getColumns().size() >= 4) {
                TableColumn<Course, String> codeCol = (TableColumn<Course, String>) coursesTable.getColumns().get(0);
                TableColumn<Course, String> nameCol = (TableColumn<Course, String>) coursesTable.getColumns().get(1);
                TableColumn<Course, String> instrCol = (TableColumn<Course, String>) coursesTable.getColumns().get(2);
                TableColumn<Course, String> schedCol = (TableColumn<Course, String>) coursesTable.getColumns().get(3);
                
                codeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseCode()));
                nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseName()));
                instrCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstructor()));
                schedCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSchedule()));
            }
            
            // Set the courses in the table
            coursesTable.setItems(enrolledCourses);
            System.out.println("Total courses loaded: " + enrolledCourses.size());
        } catch (Exception e) {
            System.err.println("Error loading courses: " + e.getMessage());
            e.printStackTrace();
        }
    }
}