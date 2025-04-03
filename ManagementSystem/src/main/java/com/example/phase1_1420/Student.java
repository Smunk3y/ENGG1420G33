package com.example.phase1_1420;


import java.io.*;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Student extends User {


    private String address;
    private String telephone;
    private String academicLevel;
    private String currentSem;
    private String subjects;
    private String thesisTitle;
    private double progress;
    private String email;
    private String thesis;


    public Student(String id, String password, String username, String email, String address, String telephone,
                   String academicLevel, String currentSem, String subjects, String thesisTitle, double progress, String thesis) {
        super(id, password, username, "USER", email);
        this.address = address;
        this.telephone = telephone;
        this.academicLevel = academicLevel;
        this.currentSem = currentSem;
        this.subjects = subjects;
        this.thesisTitle = thesisTitle;
        this.progress = progress * 100;
        this.email = email;
        this.thesis = thesis;
    }


    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }


    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }


    public String getAcademicLevel() { return academicLevel; }
    public void setAcademicLevel(String academicLevel) { this.academicLevel = academicLevel; }


    public String getCurrentSem() { return currentSem; }
    public void setCurrentSem(String currentSem) { this.currentSem = currentSem; }


    public String getSubjects() { return subjects; }
    public void setSubjects(String subjects) { this.subjects = subjects; }


    public String getThesisTitle() { return thesisTitle; }
    public void setThesisTitle(String thesisTitle) { this.thesisTitle = thesisTitle; }


    public double getProgress() { return progress; }
    public void setProgress(double progress) { this.progress = progress * 100; }


    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }


    public String getThesis() { return thesis; }
    public void setThesis(String thesis) { this.thesis = thesis; }


    @Override
    public String toString() {
        return "Username: " + username +
                " | StudentID: " + id +
                " | Email: " + email +
                " | Address: " + address +
                " | Telephone: " + telephone +
                " | Academic Level: " + academicLevel +
                " | Current Semester: " + currentSem +
                " | Subjects: " + subjects +
                " | Thesis Title: " + thesisTitle +
                " | Progress: " + progress + "%";
    }


    // --- Excel File Management Methods ---
    public static List<Student> loadStudentsFromExcel(String filePath) {
        List<Student> students = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);


            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row


                Student student = new Student(
                        row.getCell(0).getStringCellValue(),
                        "", // Password not stored in Excel for security
                        row.getCell(1).getStringCellValue(),
                        row.getCell(2).getStringCellValue(),
                        "", "", "",
                        row.getCell(3).getStringCellValue(),
                        "", "", row.getCell(4).getNumericCellValue(), ""
                );
                students.add(student);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }


    public static void saveStudentsToExcel(String filePath, List<Student> students) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Students");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Name", "Email", "Semester", "Progress"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }


            int rowNum = 1;
            for (Student student : students) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(student.getId());
                row.createCell(1).setCellValue(student.getUsername());
                row.createCell(2).setCellValue(student.getEmail());
                row.createCell(3).setCellValue(student.getCurrentSem());
                row.createCell(4).setCellValue(student.getProgress());
            }


            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
