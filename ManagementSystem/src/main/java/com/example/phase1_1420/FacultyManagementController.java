package com.example.phase1_1420;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class FacultyManagementController {

    @FXML private TableView<Faculty> facultyTable;
    @FXML private TableColumn<Faculty, String> idColumn;
    @FXML private TableColumn<Faculty, String> nameColumn;
    @FXML private TableColumn<Faculty, String> emailColumn;
    @FXML private TableColumn<Faculty, String> departmentColumn;
    @FXML private TableColumn<Faculty, String> researchAreaColumn;

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField departmentField;
    @FXML private TextField researchAreaField;
    @FXML private TextField passwordField;

    private final ObservableList<Faculty> facultyList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        researchAreaColumn.setCellValueFactory(new PropertyValueFactory<>("researchArea"));

        try {
            ExcelFile excelFile = new ExcelFile();  // or use your singleton if needed
            excelFile.ReadingNameExcelFile(); // loads from file
            facultyList.addAll(excelFile.facultyList); // populate observable list
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Error", "Failed to load faculty data.");
        }

        facultyTable.setItems(facultyList);
    }


    @FXML
    private void handleAdd() {
        String id = idField.getText().trim();

        // Check for duplicate
        for (Faculty f : facultyList) {
            if (f.getId().equals(id)) {
                showAlert(Alert.AlertType.ERROR, "Duplicate ID", "A faculty member with this ID already exists.");
                return;
            }
        }

        Faculty newFaculty = new Faculty(
                id,
                passwordField.getText().trim(),
                nameField.getText().trim(),
                emailField.getText().trim(),
                departmentField.getText().trim(),
                researchAreaField.getText().trim(),
                "N/A",    // default office location
                "N/A",    // default courses offered
                "N/A"     // default degree
        );

        facultyList.add(newFaculty);
        clearFields();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Faculty added successfully.");
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
        selected.setDepartment(departmentField.getText().trim());
        selected.setResearchArea(researchAreaField.getText().trim());
        selected.setPassword(passwordField.getText().trim());

        facultyTable.refresh();
        clearFields();
        showAlert(Alert.AlertType.INFORMATION, "Updated", "Faculty updated successfully.");
    }

    @FXML
    private void handleDelete() {
        Faculty selected = facultyTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            facultyList.remove(selected);
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Deleted", "Faculty deleted successfully.");
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a faculty member to delete.");
        }
    }

    private void clearFields() {
        idField.clear();
        nameField.clear();
        emailField.clear();
        departmentField.clear();
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
