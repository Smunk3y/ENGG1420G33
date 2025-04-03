package com.example.phase1_1420;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseManagementController {

    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> courseCodeColumn;
    @FXML private TableColumn<Course, String> courseNameColumn;
    @FXML private TableColumn<Course, String> subjectCodeColumn;
    @FXML private TableColumn<Course, String> sectionColumn;
    @FXML private TableColumn<Course, String> facultyColumn;

    @FXML private TextField courseCodeField;
    @FXML private TextField courseNameField;
    @FXML private ComboBox<String> subjectDropdown;
    @FXML private TextField sectionField;
    @FXML private TextField locationField;
    @FXML private TextField lectureTimeField;
    @FXML private TextField finalExamField;
    @FXML private TextField capacityField;
    @FXML private ComboBox<String> facultyDropdown;

    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button manageEnrollmentsButton;

    private final ObservableList<Course> allCourses = FXCollections.observableArrayList();
    private final ExcelFile excelReader = new ExcelFile();

    @FXML
    public void initialize() {
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<>("courseCode"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        subjectCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        sectionColumn.setCellValueFactory(new PropertyValueFactory<>("sectionNumber"));
        facultyColumn.setCellValueFactory(new PropertyValueFactory<>("teacherName"));

        courseTable.setItems(allCourses);

        boolean isAdmin = UserDatabase.CurrentUser.getRole().equals("ADMIN");
        setAdminMode(isAdmin);

        loadCoursesFromExcel();
        loadDropdowns();

        courseTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> populateFields(newSelection));

        if (!isAdmin) {
            showOnlyUserCourses();
        }
    }

    private void showOnlyUserCourses() {
        String username = UserDatabase.CurrentUser.getUsername();
        String id = UserDatabase.CurrentUser.getId();
        ObservableList<Course> userCourses = FXCollections.observableArrayList();
        
        // Reload courses from Excel to ensure we have the latest data
        try {
            excelReader.ReadingNameExcelFile();
            allCourses.setAll(excelReader.courseList);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Get the student's subjects
        Student currentStudent = null;
        for (Student student : excelReader.studentList) {
            if (student.getId().equals(id)) {
                currentStudent = student;
                break;
            }
        }

        if (currentStudent == null) {
            courseTable.setItems(userCourses);
            return;
        }

        String studentSubjectsStr = currentStudent.getSubjects();
        if (studentSubjectsStr == null || studentSubjectsStr.trim().isEmpty()) {
            courseTable.setItems(userCourses);
            return;
        }

        // Split and clean subjects
        String[] studentSubjects = studentSubjectsStr.split(",");
        for (int i = 0; i < studentSubjects.length; i++) {
            studentSubjects[i] = studentSubjects[i].trim().toUpperCase();
        }

        // Create a set to track processed subjects
        java.util.Set<String> processedSubjects = new java.util.HashSet<>();

        // First pass: Match existing courses
        for (Course course : excelReader.courseList) {  // Use excelReader.courseList directly
            String courseSubjectCode = course.getCode().trim().toUpperCase();
            
            // Check if this course belongs to the faculty
            if (course.getTeacherName().equals(username)) {
                userCourses.add(course);
                processedSubjects.add(courseSubjectCode);
                continue;
            }

            // Check if this course matches any of the student's subjects
            for (String studentSubject : studentSubjects) {
                if (studentSubject.isEmpty()) continue;
                
                if (courseSubjectCode.equals(studentSubject)) {
                    if (!processedSubjects.contains(courseSubjectCode)) {
                        userCourses.add(course);
                        processedSubjects.add(courseSubjectCode);
                        break;
                    }
                }
            }
        }

        // Second pass: Add basic entries for subjects without corresponding courses
        for (String studentSubject : studentSubjects) {
            if (studentSubject.trim().isEmpty()) continue;
            
            if (!processedSubjects.contains(studentSubject)) {
                // Use the 3-parameter constructor
                Course basicCourse = new Course(
                    studentSubject,  // Use subject code as course code
                    "No Course Found - See Advisor",  // Course name
                    studentSubject  // Subject code
                );
                
                // Set additional properties using setters
                basicCourse.setSectionNumber("N/A");
                basicCourse.setCapacity(0.0);
                basicCourse.setSchedule("N/A");
                basicCourse.setFinalExamDateTime("N/A");
                basicCourse.setLocation("N/A");
                basicCourse.setInstructor("N/A");
                
                userCourses.add(basicCourse);
                processedSubjects.add(studentSubject);
            }
        }

        courseTable.setItems(userCourses);
    }

    private void setAdminMode(boolean isAdmin) {
        addButton.setDisable(!isAdmin);
        editButton.setDisable(!isAdmin);
        deleteButton.setDisable(!isAdmin);
        manageEnrollmentsButton.setDisable(!isAdmin);

        //Make it a readable box
        facultyDropdown.setEditable(!isAdmin);
        subjectDropdown.setEditable(!isAdmin);
    }

    private void loadCoursesFromExcel() {
        try {
            excelReader.ReadingNameExcelFile();
            allCourses.setAll(excelReader.courseList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDropdowns() {
        subjectDropdown.getItems().clear();
        facultyDropdown.getItems().clear();

        for (Subject s : excelReader.subjectList) {
            subjectDropdown.getItems().add(s.getCode());
        }

        for (Faculty f : excelReader.facultyList) {
            facultyDropdown.getItems().add(f.getUsername());
        }
    }

    private void populateFields(Course course) {
        if (course != null) {
            courseCodeField.setText(course.getCourseCode());
            courseNameField.setText(course.getCourseName());
            subjectDropdown.setValue(course.getCode());
            sectionField.setText(course.getSectionNumber());
            locationField.setText(course.getLocation());
            lectureTimeField.setText(course.getSchedule());
            finalExamField.setText(course.getFinalExamDateTime());
            capacityField.setText(String.valueOf(course.getCapacity()));
            facultyDropdown.setValue(course.getInstructor());
        }
    }

    @FXML
    private void handleAddCourse() {
        try {
            double capacity = Double.parseDouble(capacityField.getText());
            String code = courseCodeField.getText();

            for (Course c : allCourses) {
                if (c.getCourseCode().equals(code)) {
                    showAlert("Duplicate", "Course code already exists.", Alert.AlertType.WARNING);
                    return;
                }
                if(c.getSchedule().equals(lectureTimeField.getText()) && c.getLocation().equals(locationField.getText())){
                    showAlert("Duplicate", "Lecture Time & Location already exists ", Alert.AlertType.WARNING);
                    return;
                }
            }

            // Create course with the 3-parameter constructor
            Course newCourse = new Course(
                    code,
                    courseNameField.getText(),
                    subjectDropdown.getValue()
            );
            
            // Set additional properties using setters
            newCourse.setSectionNumber(sectionField.getText());
            newCourse.setCapacity(capacity);
            newCourse.setSchedule(lectureTimeField.getText());
            newCourse.setFinalExamDateTime(finalExamField.getText());
            newCourse.setLocation(locationField.getText());
            newCourse.setInstructor(facultyDropdown.getValue());

            // Add course to faculty object if found
            for (Faculty faculty : excelReader.facultyList) {
                if (faculty.getUsername().equals(newCourse.getInstructor())) {
                    String existing = faculty.getCoursesOffered();
                    if (!existing.contains(newCourse.getCourseName())) {
                        if (existing.isEmpty()) {
                            faculty.setCoursesOffered(newCourse.getCourseName());
                        } else {
                            faculty.setCoursesOffered(existing + "," + newCourse.getCourseName());
                        }
                    }
                }
            }

            allCourses.add(newCourse);
            excelReader.courseList.add(newCourse);
            excelReader.writeFacultyToExcel(excelReader.facultyList);
            excelReader.writeCoursesToExcel(excelReader.courseList);
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Invalid input: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleEditCourse() {
        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a course to edit.", Alert.AlertType.WARNING);
            return;
        }
        try {
            selected.setCourseCode(courseCodeField.getText());
            selected.setCourseName(courseNameField.getText());
            selected.setSubjectCode(subjectDropdown.getValue());
            selected.setSectionNumber(sectionField.getText());
            selected.setLocation(locationField.getText());
            selected.setSchedule(lectureTimeField.getText());
            selected.setFinalExamDateTime(finalExamField.getText());
            selected.setCapacity(Double.parseDouble(capacityField.getText()));
            selected.setInstructor(facultyDropdown.getValue());

            // Update faculty's offered courses
            for (Faculty faculty : excelReader.facultyList) {
                if (faculty.getUsername().equals(selected.getInstructor())) {
                    String existing = faculty.getCoursesOffered();
                    if (!existing.contains(selected.getCourseName())) {
                        if (existing.isEmpty()) {
                            faculty.setCoursesOffered(selected.getCourseName());
                        } else {
                            faculty.setCoursesOffered(existing + "," + selected.getCourseName());
                        }
                    }
                }
            }

            courseTable.refresh();
            excelReader.writeCoursesToExcel(allCourses);
            excelReader.writeFacultyToExcel(excelReader.facultyList);
            clearFields();
        } catch (Exception e) {
            showAlert("Error", "Invalid input: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteCourse() {
        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a course to delete.", Alert.AlertType.WARNING);
            return;
        }
        allCourses.remove(selected);
        excelReader.courseList.remove(selected);
        courseTable.refresh();
        clearFields();
        try {
            excelReader.writeCoursesToExcel(allCourses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        courseCodeField.clear();
        courseNameField.clear();
        sectionField.clear();
        locationField.clear();
        lectureTimeField.clear();
        finalExamField.clear();
        capacityField.clear();
        subjectDropdown.setValue(null);
        facultyDropdown.setValue(null);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleViewMyCourses() {
        showOnlyUserCourses();
    }

    @FXML
    private void handleManageEnrollments() {
        Course selected = courseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No selection", "Please select a course to manage enrollments.", Alert.AlertType.WARNING);
            return;
        }

        EnrollmentManager.showForCourse(selected, excelReader.studentList, excelReader, selected.getCapacity());
    }

}

