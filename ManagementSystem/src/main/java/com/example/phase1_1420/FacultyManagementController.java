
package com.example.phase1_1420;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FacultyManagementController {

    @FXML private TableView<Faculty> facultyTable;
    @FXML private TableColumn<Faculty, String> idColumn;
    @FXML private TableColumn<Faculty, String> nameColumn;
    @FXML private TableColumn<Faculty, String> emailColumn;
    @FXML private TableColumn<Faculty, String> degreeColumn;
    @FXML private TableColumn<Faculty, String> researchAreaColumn;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField degreeField;
    @FXML private TextField researchAreaField;
    @FXML private TextField passwordField;

    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button clearSelectionButton;

    private final ObservableList<Faculty> facultyList = FXCollections.observableArrayList();
    private final ExcelFile excelFile = new ExcelFile();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        degreeColumn.setCellValueFactory(new PropertyValueFactory<>("degree"));
        researchAreaColumn.setCellValueFactory(new PropertyValueFactory<>("researchArea"));

        facultyTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getUsername());
                emailField.setText(newSelection.getEmail());
                degreeField.setText(newSelection.getDegree());
                researchAreaField.setText(newSelection.getResearchArea());
                passwordField.setText(newSelection.getPassword());
            }
        });

        try {
            excelFile.ReadingNameExcelFile();
            if (UserDatabase.CurrentUser.getRole().equals("USER") && UserDatabase.CurrentUser instanceof Student) {
                Student currentStudent = (Student) UserDatabase.CurrentUser;
                Set<String> teacherUsernames = new HashSet<>();
                String[] enrolledCodes = currentStudent.getSubjects().split(",");
                for (Course course : excelFile.courseList) {
                    if (Arrays.asList(enrolledCodes).contains(course.getCode())) {
                        teacherUsernames.add(course.getTeacherName());
                    }
                }
                for (Faculty f : excelFile.facultyList) {
                    if (teacherUsernames.contains(f.getUsername())) {
                        facultyList.add(f);
                    }
                }
            } else {
                facultyList.addAll(excelFile.facultyList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to load faculty data.");
        }

        setAdminMode(UserDatabase.CurrentUser.getRole().equals("ADMIN"));
        facultyTable.setItems(facultyList);
    }

    private void setAdminMode(boolean isAdmin) {
        addButton.setDisable(!isAdmin);
        editButton.setDisable(!isAdmin);
        deleteButton.setDisable(!isAdmin);
        passwordField.setVisible(isAdmin);
    }

    private String generateNextFacultyID() {
        int max = 0;
        for (Faculty f : facultyList) {
            String id = f.getId().replaceAll("[^\\d]", "");
            if (!id.isEmpty()) {
                int num = Integer.parseInt(id);
                if (num > max) {
                    max = num;
                }
            }
        }
        return String.format("F%04d", max + 1);
    }

    @FXML
    private void handleAdd() {
        String id = generateNextFacultyID();

        for (Faculty f : facultyList) {
            if (f.getId().equals(id)) {
                showAlert(Alert.AlertType.ERROR, "Duplicate ID", "A faculty member with this ID already exists.");
                return;
            }
        }

        for (Faculty f : facultyList) {
            if (f.getEmail().equals(emailField.getText().trim())) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Email", "A faculty member with this Email already exists.");
                return;
            }
        }

        Faculty newFaculty = new Faculty(
                id,
                passwordField.getText().trim(),
                nameField.getText().trim(),
                emailField.getText().trim(),
                degreeField.getText().trim(),
                researchAreaField.getText().trim(),
                "RM ###",
                ""
        );

        facultyList.add(newFaculty);
        saveFacultyToExcel();
        clearFields();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Faculty added successfully.");
    }

    public void saveFacultyToExcel() {
        try {
            excelFile.writeFacultyToExcel(facultyList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEdit() {
        Faculty selected = facultyTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a faculty member to edit.");
            return;
        }

        selected.setUsername(nameField.getText().trim());
        selected.setEmail(emailField.getText().trim());
        selected.setDegree(degreeField.getText().trim());
        selected.setResearchArea(researchAreaField.getText().trim());
        selected.setPassword(passwordField.getText().trim());

        facultyTable.refresh();
        saveFacultyToExcel();
        clearFields();
        showAlert(Alert.AlertType.INFORMATION, "Updated", "Faculty updated successfully.");
    }

    @FXML
    private void clearSelections() {
        clearFields();
    }

    @FXML
    private void handleDelete() {
        Faculty selected = facultyTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            facultyList.remove(selected);
            clearFields();
            saveFacultyToExcel();
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Faculty deleted successfully.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a faculty member to delete.");
        }
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        degreeField.clear();
        researchAreaField.clear();
        passwordField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
