package com.example.phase1_1420;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class StudentProfile {

    @FXML private TextField nameBox;
    @FXML private TextField emailBox;
    @FXML private TextField adressBox;
    @FXML private TextField telephoneBox;
    @FXML private Label idText;

    @FXML private TextField oldPasswordField;
    @FXML private TextField newPasswordField;

    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> courseCodeColumn;
    @FXML private TableColumn<Course, String> courseNameColumn;
    @FXML private TableColumn<Course, String> instructorColumn;
    @FXML private TableColumn<Course, String> scheduleColumn;

    private Student currentStudent;
    private final ExcelFile excelFile = new ExcelFile();
    private final ObservableList<Student> allStudents = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (UserDatabase.CurrentUser instanceof Student) {
            currentStudent = (Student) UserDatabase.CurrentUser;

            try {
                excelFile.ReadingNameExcelFile();
                allStudents.setAll(excelFile.studentList);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Could not load student data.");
                return;
            }

            initializeColumns();
            populateStudentInfo();
            loadCoursesTable();
        } else {
            System.err.println("Current user is not a Student.");
        }
    }

    private void populateStudentInfo() {
        idText.setText(currentStudent.getId());
        nameBox.setText(currentStudent.getUsername());
        adressBox.setText(currentStudent.getAddress());
        emailBox.setText(currentStudent.getEmail());
        telephoneBox.setText(currentStudent.getTelephone());
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
    }


    @FXML
    private void handleSaveChanges() {
        try {
            // Update the student object in the list
            for (Student s : allStudents) {
                if (s.getId().equals(currentStudent.getId())) {
                    s.setUsername(nameBox.getText().trim());
                    s.setEmail(emailBox.getText().trim());
                    s.setTelephone(telephoneBox.getText().trim());
                    s.setAddress(adressBox.getText().trim());
                    break;
                }
            }

            // Write updated list to Excel
            excelFile.writeStudentsToExcel(allStudents);

            showAlert(Alert.AlertType.INFORMATION, "Saved", "Changes saved successfully.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save changes to file.");
        }
    }

    @FXML
    private void handleChangePassword() {
        String oldPass = oldPasswordField.getText().trim();
        String newPass = newPasswordField.getText().trim();

        if (oldPass.isEmpty() || newPass.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Fields", "Please enter both old and new passwords.");
            return;
        }

        if (!currentStudent.getPassword().equals(oldPass)) {
            showAlert(Alert.AlertType.ERROR, "Incorrect Password", "Old password does not match our records.");
            return;
        }

        // Update in observable list
        for (Student s : allStudents) {
            if (s.getId().equals(currentStudent.getId())) {
                s.setPassword(newPass);
                break;
            }
        }

        try {
            excelFile.writeStudentsToExcel(allStudents);
            currentStudent.setPassword(newPass); // Update the in-session object
            showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully.");
            oldPasswordField.clear();
            newPasswordField.clear();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update password in file.");
        }
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
        for (Course course : excelFile.courseList) {
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
                    "N/A",          // No course name available
                    studentSubject, // Use subject code
                    "N/A",         // No section number
                    0.0,           // Default capacity
                    "N/A",         // No lecture time
                    "N/A",         // No final exam time
                    "N/A",         // No location
                    "N/A"          // No instructor
            );
            enrolledCourses.add(basicCourse);
            processedSubjects.add(studentSubject);
        }

        // Set the items in the table
        coursesTable.setItems(enrolledCourses);
        System.out.println("Total courses loaded: " + enrolledCourses.size());
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}