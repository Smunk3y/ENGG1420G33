package com.example.phase1_1420;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserDashboardViewController implements Initializable {
    @FXML private Text studentNameText;
    @FXML private Text studentIdText;
    @FXML private Text academicLevelText;
    @FXML private Text currentSemText;
    @FXML private Text graduationDateText;
    @FXML private ProgressBar progressBar;
    @FXML private Label progressLabel;
    
    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> courseCodeColumn;
    @FXML private TableColumn<Course, String> courseNameColumn;
    @FXML private TableColumn<Course, String> instructorColumn;
    @FXML private TableColumn<Course, String> scheduleColumn;
    
    @FXML private TableView<Event> eventsTable;
    @FXML private TableColumn<Event, String> eventNameColumn;
    @FXML private TableColumn<Event, String> eventDateColumn;
    @FXML private TableColumn<Event, String> eventLocationColumn;

    private final ExcelFile excelReader = new ExcelFile();
    private Student currentStudent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("\n=== Initializing UserDashboardViewController ===");
            
            // Load data from Excel
            excelReader.ReadingNameExcelFile();
            
            // Get current student from UserDatabase
            currentStudent = (Student) UserDatabase.CurrentUser;
            System.out.println("Current Student:");
            System.out.println("- ID: " + currentStudent.getId());
            System.out.println("- Name: " + currentStudent.getUsername());
            System.out.println("- Academic Level: " + currentStudent.getAcademicLevel());
            System.out.println("- Current Semester: " + currentStudent.getCurrentSem());
            System.out.println("- Subjects: " + currentStudent.getSubjects());
            
            // Initialize table columns
            initializeColumns();
            
            // Update UI with student information
            updateStudentInfo();
            
            // Load tables with data
            loadCoursesTable();
            loadEventsTable();
        } catch (Exception e) {
            System.err.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeColumns() {
        // Courses table
        courseCodeColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getCourseCode()));
        courseNameColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getCourseName()));
        instructorColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getTeacherName()));
        scheduleColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getLectureTime()));

        // Events table
        eventNameColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEventName()));
        eventDateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getDateTime()));
        eventLocationColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getLocation()));
    }

    private void updateStudentInfo() {
        studentNameText.setText(currentStudent.getUsername());
        studentIdText.setText(currentStudent.getId());
        academicLevelText.setText(currentStudent.getAcademicLevel());
        currentSemText.setText(currentStudent.getCurrentSem());
        graduationDateText.setText(calculateGraduationDate());
        

        progressBar.setProgress((double)currentStudent.getProgress() / 100 );
        progressLabel.setText(currentStudent.getProgress() + "% Complete");
    }

    private String calculateGraduationDate() {
        String academicLevel = currentStudent.getAcademicLevel();
        int currentLevel;
        
        // Convert academic level text to number
        switch (academicLevel.toLowerCase()) {
            case "freshman":
                currentLevel = 1;
                break;
            case "sophomore":
                currentLevel = 2;
                break;
            case "junior":
                currentLevel = 3;
                break;
            case "senior":
                currentLevel = 4;
                break;
            default:
                currentLevel = 1; // Default to freshman if unknown
        }
        
        // Parse semester string (e.g., "Fall 2025")
        String currentSemStr = currentStudent.getCurrentSem();
        int currentSem;
        if (currentSemStr.toLowerCase().contains("fall")) {
            currentSem = 1;
        } else if (currentSemStr.toLowerCase().contains("spring")) {
            currentSem = 2;
        } else {
            currentSem = 1; // Default to Fall if unknown
        }
        
        int remainingSemesters = (4 - currentLevel) * 2 + (2 - currentSem);
        
        // Assuming 4 months per semester
        int monthsUntilGraduation = remainingSemesters * 4;
        LocalDate graduationDate = LocalDate.now().plusMonths(monthsUntilGraduation);
        
        return graduationDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
    }

    private void loadCoursesTable() {
        // Get the student's subjects string from the current student object
        String studentSubjectsStr = currentStudent.getSubjects();
        System.out.println("\n=== Course Loading Debug ===");
        System.out.println("Student ID: " + currentStudent.getId());
        System.out.println("Raw Subjects String: '" + studentSubjectsStr + "'");
        
        // Create an observable list to hold the enrolled courses
        ObservableList<Course> enrolledCourses = FXCollections.observableArrayList();
        
        // Check if the student has any subjects registered
        if (studentSubjectsStr == null || studentSubjectsStr.trim().isEmpty()) {
            System.out.println("WARNING: Student has no subjects registered!");
            coursesTable.setItems(enrolledCourses);
            return;
        }
        
        // Split the subjects string into an array and clean each subject
        String[] studentSubjects = studentSubjectsStr.split(",");
        for (int i = 0; i < studentSubjects.length; i++) {
            studentSubjects[i] = studentSubjects[i].trim().toUpperCase();
        }
        
        // Create a set to keep track of processed subjects to avoid duplicates
        java.util.Set<String> processedSubjects = new java.util.HashSet<>();
        
        // First pass: Match existing courses with student's subjects
        for (Course course : excelReader.courseList) {
            String courseSubjectCode = course.getCode().trim().toUpperCase();
            System.out.println("\nChecking course: " + course.getCourseName());
            System.out.println("Subject code: '" + courseSubjectCode + "'");
            
            // Check each of the student's subjects
            for (String studentSubject : studentSubjects) {
                if (studentSubject.isEmpty()) continue;
                
                System.out.println("Comparing with student subject: '" + studentSubject + "'");
                
                // Check for exact match (after converting to uppercase)
                if (courseSubjectCode.equals(studentSubject)) {
                    // Only add the course if we haven't processed this subject code before
                    if (!processedSubjects.contains(courseSubjectCode)) {
                        System.out.println("✓ Match found! Adding course: " + course.getCourseName());
                        enrolledCourses.add(course);
                        processedSubjects.add(courseSubjectCode);
                        break;
                    }
                }
            }
        }
        
        // Second pass: Create basic entries for subjects without corresponding courses
        for (String studentSubject : studentSubjects) {
            if (studentSubject.trim().isEmpty()) continue;
            
            // Skip if we already have a course for this subject
            if (processedSubjects.contains(studentSubject)) continue;
            
            // Create a basic course entry for subjects without corresponding courses
            Course basicCourse = new Course(
                studentSubject,  // Use subject code as course code
                "No Course Found - See Advisor",  // Course name
                studentSubject   // Subject code
            );
            
            // Set additional properties using setters
            basicCourse.setSectionNumber("N/A");
            basicCourse.setCapacity(0.0);
            basicCourse.setSchedule("N/A");
            basicCourse.setFinalExamDateTime("N/A");
            basicCourse.setLocation("N/A");
            basicCourse.setInstructor("N/A");
            
            enrolledCourses.add(basicCourse);
            processedSubjects.add(studentSubject);
        }
        
        // Set the items in the table
        coursesTable.setItems(enrolledCourses);
        System.out.println("Total courses loaded: " + enrolledCourses.size());
    }

    private void loadEventsTable() {
        // Filter events based on student's registration
        ObservableList<Event> registeredEvents = FXCollections.observableArrayList();
        String username = currentStudent.getUsername();
        
        System.out.println("Checking events for student: " + username);
        
        for (Event event : excelReader.eventList) {
            System.out.println("Checking event: " + event.getEventName());
            System.out.println("Registered students: " + event.getRegisteredStudents());
            
            // First check if the registration was removed during this session
            if (UserDatabase.wasRegistrationRemoved(event.getEventID(), username)) {
                System.out.println("Registration was removed for this event during this session - skipping");
                continue;
            }
            
            // Check if the student is registered by username
            if (event.isStudentRegistered(username)) {
                System.out.println("Student is registered for this event - adding to table");
                registeredEvents.add(event);
            } else {
                System.out.println("Student is not registered for this event");
            }
        }
        
        System.out.println("Total registered events found: " + registeredEvents.size());
        eventsTable.setItems(registeredEvents);
    }
}
