package com.example.phase1_1420;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FacultyDashboardViewController implements Initializable {
    @FXML private Text studentNameText;
    @FXML private Text studentIdText;
    @FXML private Text DegreeLevelText;
    @FXML private Text CoursesOffText;
    @FXML private ListView<String> facultyCoursesList;

    private final ExcelFile excelReader = new ExcelFile();
    private Faculty currentFaculty;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("\n=== Initializing UserDashboardViewController ===");

            // Load data from Excel
            excelReader.ReadingNameExcelFile();

            // Get current faculty from UserDatabase
            currentFaculty = (Faculty) UserDatabase.CurrentUser;
            System.out.println("Current Faculty:");
            System.out.println("- ID: " + currentFaculty.getId());
            System.out.println("- Name: " + currentFaculty.getUsername());
            System.out.println("- Degree Level: " + currentFaculty.getDegree());
            System.out.println("- Courses Offered: " + currentFaculty.getCoursesOffered());

            // Update UI with faculty information
            updateStudentInfo();
            loadCoursesTaught();

        } catch (Exception e) {
            System.err.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateStudentInfo() {
        studentNameText.setText(currentFaculty.getUsername());
        studentIdText.setText(currentFaculty.getId());
        DegreeLevelText.setText(currentFaculty.getDegree());
        CoursesOffText.setText(currentFaculty.getCoursesOffered());
    }

    private void loadCoursesTaught() {
        List<String> taughtCourses = new ArrayList<>();
        for (Course course : excelReader.courseList) {
            if (course.getTeacherName().equals(currentFaculty.getUsername())) {
                taughtCourses.add(course.getCourseCode() + " - " + course.getCourseName());
            }
        }
        facultyCoursesList.setItems(FXCollections.observableArrayList(taughtCourses));
    }
}
