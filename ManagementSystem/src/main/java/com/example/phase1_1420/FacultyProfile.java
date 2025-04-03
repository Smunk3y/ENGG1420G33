package com.example.phase1_1420;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class FacultyProfile {

    @FXML private TextField nameBox;
    @FXML private TextField emailBox;
    @FXML private TextField researchBox;
    @FXML private TextField degreeBox;
    @FXML private Label idText;

    @FXML private TextField oldPasswordField;
    @FXML private TextField newPasswordField;

    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> courseCodeColumn;
    @FXML private TableColumn<Course, String> courseNameColumn;
    @FXML private TableColumn<Course, String> instructorColumn;
    @FXML private TableColumn<Course, String> scheduleColumn;

    private Faculty currentFaculty;
    private final ExcelFile excelFile = new ExcelFile();
    private final ObservableList<Faculty> allFaculty = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (UserDatabase.CurrentUser instanceof Faculty) {
            currentFaculty = (Faculty) UserDatabase.CurrentUser;

            try {
                excelFile.ReadingNameExcelFile();
                allFaculty.setAll(excelFile.facultyList);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Could not load faculty data.");
                return;
            }

            initializeColumns();
            populateFacultyInfo();
            loadCoursesTable();
        } else {
            System.err.println("Current user is not a Faculty.");
        }
    }

    private void populateFacultyInfo() {
        idText.setText(currentFaculty.getId());
        nameBox.setText(currentFaculty.getUsername());
        researchBox.setText(currentFaculty.getResearchArea());
        emailBox.setText(currentFaculty.getEmail());
        degreeBox.setText(currentFaculty.getDegree());
    }

    private void initializeColumns() {
        courseCodeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseCode()));
        courseNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourseName()));
        instructorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTeacherName()));
        scheduleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLectureTime()));
    }

    @FXML
    private void handleSaveChanges() {
        try {
            for (Faculty f : allFaculty) {
                if (f.getId().equals(currentFaculty.getId())) {
                    f.setUsername(nameBox.getText().trim());
                    f.setEmail(emailBox.getText().trim());
                    f.setResearchArea(researchBox.getText().trim());
                    f.setDegree(degreeBox.getText().trim());
                    break;
                }
            }

            excelFile.writeFacultyToExcel(allFaculty);
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

        if (!currentFaculty.getPassword().equals(oldPass)) {
            showAlert(Alert.AlertType.ERROR, "Incorrect Password", "Old password does not match our records.");
            return;
        }

        for (Faculty f : allFaculty) {
            if (f.getId().equals(currentFaculty.getId())) {
                f.setPassword(newPass);
                break;
            }
        }

        try {
            excelFile.writeFacultyToExcel(allFaculty);
            currentFaculty.setPassword(newPass);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully.");
            oldPasswordField.clear();
            newPasswordField.clear();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update password in file.");
        }
    }

    private void loadCoursesTable() {
        ObservableList<Course> teachingCourses = FXCollections.observableArrayList();

        for (Course c : excelFile.courseList) {
            if (c.getTeacherName().equalsIgnoreCase(currentFaculty.getUsername())) {
                teachingCourses.add(c);
            }
        }

        coursesTable.setItems(teachingCourses);
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}