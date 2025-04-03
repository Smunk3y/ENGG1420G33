package com.example.phase1_1420;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelFile {
    public List<Student> studentList = new ArrayList<>();
    public List<Faculty> facultyList = new ArrayList<>();
    public List<Event> eventList = new ArrayList<>();
    public List<Subject> subjectList = new ArrayList<>();
    public List<Course> courseList = new ArrayList<>();

    // Common file path for all Excel operations
    private static final String EXCEL_FILE_PATH = "UMS_Data.xlsx";

    //Write the subject's back to the excel file after edit in GUI
    public void writeSubjectsToExcel(List<Subject> updatedSubjects) throws IOException {
        File file = new File(EXCEL_FILE_PATH);
        if (!file.exists()) {
            throw new IOException("Excel file not found at: " + file.getAbsolutePath());
        }

        FileInputStream fis = new FileInputStream(file);
        Workbook wb = WorkbookFactory.create(fis);
        Sheet sheet = wb.getSheetAt(0); // Subjects in sheet 0

        // Clear old data (except header)
        for (int i = sheet.getLastRowNum(); i > 0; i--) {
            Row row = sheet.getRow(i);
            if (row != null) sheet.removeRow(row);
        }

        // Write updated subject list starting from row 1
        int rowIndex = 1;
        for (Subject subject : updatedSubjects) {
            Row row = sheet.createRow(rowIndex++);
            Cell codeCell = row.createCell(0);
            Cell nameCell = row.createCell(1);

            codeCell.setCellValue(subject.getCode());
            nameCell.setCellValue(subject.getName());
        }

        fis.close();

        // Save changes
        FileOutputStream fos = new FileOutputStream(file);
        wb.write(fos);
        fos.close();
        wb.close();
    }

    //Write Courses to excel
    public void writeCoursesToExcel(List<Course> updatedCourses) throws IOException {
        File file = new File(EXCEL_FILE_PATH);
        if (!file.exists()) {
            throw new IOException("Excel file not found at: " + file.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(file);
        Workbook wb = WorkbookFactory.create(fis);
        Sheet sheet = wb.getSheetAt(1); // Courses in sheet 1

        // Clear old data (except header)
        for (int i = sheet.getLastRowNum(); i > 0; i--) {
            Row row = sheet.getRow(i);
            if (row != null) sheet.removeRow(row);
        }

        // Write updated course list starting from row 1
        int rowIndex = 1;
        for (Course course : updatedCourses) {
            Row row = sheet.createRow(rowIndex++);

            Cell codeCell = row.createCell(0);
            Cell nameCell = row.createCell(1);
            Cell subjectCodeCell = row.createCell(2);
            Cell sectionCell = row.createCell(3);
            Cell capacityCell = row.createCell(4);
            Cell lectureTimeCell = row.createCell(5);
            Cell finalTimeCell = row.createCell(6);
            Cell locationCell = row.createCell(7);
            Cell teacherCell = row.createCell(8);

            codeCell.setCellValue(course.getCourseCode());
            nameCell.setCellValue(course.getCourseName());
            subjectCodeCell.setCellValue(course.getCode());
            sectionCell.setCellValue(course.getSectionNumber());
            capacityCell.setCellValue(course.getCapacity());
            lectureTimeCell.setCellValue(course.getLectureTime());
            finalTimeCell.setCellValue(course.getFinalExamDateTime());
            locationCell.setCellValue(course.getLocation());
            teacherCell.setCellValue(course.getTeacherName());
        }

        fis.close();

        // Save changes
        FileOutputStream fos = new FileOutputStream(file);
        wb.write(fos);
        fos.close();
        wb.close();
    }

    //Write Students to excel
    public void writeStudentsToExcel(List<Student> updatedStudents) throws IOException {
        File file = new File(EXCEL_FILE_PATH);
        if (!file.exists()) {
            throw new IOException("Excel file not found at: " + file.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(file);
        Workbook wb = WorkbookFactory.create(fis);
        Sheet sheet = wb.getSheetAt(2); // Students in sheet 2

        // Clear old data (except header)
        for (int i = sheet.getLastRowNum(); i > 0; i--) {
            Row row = sheet.getRow(i);
            if (row != null) sheet.removeRow(row);
        }

        // Write updated student list starting from row 1
        int rowIndex = 1;
        for (Student student : updatedStudents) {
            Row row = sheet.createRow(rowIndex++);

            Cell idCell = row.createCell(0);
            Cell userCell = row.createCell(1);
            Cell addressCell = row.createCell(2);
            Cell telephoneCell = row.createCell(3);
            Cell emailCell = row.createCell(4);
            Cell academicLevelCell = row.createCell(5);
            Cell semesterCell = row.createCell(6);
            Cell photoCell = row.createCell(7);
            Cell subjectsCell = row.createCell(8);
            Cell thesisTitleCell = row.createCell(9);
            Cell progressCell = row.createCell(10);
            Cell passCell = row.createCell(11);

            idCell.setCellValue(student.getId());
            userCell.setCellValue(student.getUsername());
            addressCell.setCellValue(student.getAddress());
            telephoneCell.setCellValue(student.getTelephone());
            emailCell.setCellValue(student.getEmail());
            academicLevelCell.setCellValue(student.getAcademicLevel());
            semesterCell.setCellValue(student.getCurrentSem());
            photoCell.setCellValue("default");
            subjectsCell.setCellValue(student.getSubjects());
            thesisTitleCell.setCellValue(student.getThesisTitle());
            progressCell.setCellValue(student.getProgress());
            passCell.setCellValue(student.getPassword());
        }

        fis.close();

        // Save changes
        FileOutputStream fos = new FileOutputStream(file);
        wb.write(fos);
        fos.close();
        wb.close();
    }

    //Write Faculty to excel
    public void writeFacultyToExcel(List<Faculty> updatedFaculty) throws IOException {
        File file = new File(EXCEL_FILE_PATH);
        if (!file.exists()) {
            throw new IOException("Excel file not found at: " + file.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(file);
        Workbook wb = WorkbookFactory.create(fis);
        Sheet sheet = wb.getSheetAt(3); // Faculty in sheet 3

        // Clear old data (except header)
        for (int i = sheet.getLastRowNum(); i > 0; i--) {
            Row row = sheet.getRow(i);
            if (row != null) sheet.removeRow(row);
        }

        // Write updated faculty list starting from row 1
        int rowIndex = 1;
        for (Faculty faculty : updatedFaculty) {
            Row row = sheet.createRow(rowIndex++);

            Cell idCell = row.createCell(0);
            Cell userCell = row.createCell(1);
            Cell degreeCell = row.createCell(2);
            Cell researchCell = row.createCell(3);
            Cell emailCell = row.createCell(4);
            Cell officeCell = row.createCell(5);
            Cell coursesCell = row.createCell(6);
            Cell passCell = row.createCell(7);

            idCell.setCellValue(faculty.getId());
            userCell.setCellValue(faculty.getUsername());
            degreeCell.setCellValue(faculty.getDegree());
            researchCell.setCellValue(faculty.getResearchArea());
            emailCell.setCellValue(faculty.getEmail());
            officeCell.setCellValue(faculty.getOfficeLocation());
            coursesCell.setCellValue(faculty.getCoursesOffered());
            passCell.setCellValue(faculty.getPassword());
        }

        fis.close();

        // Save changes
        FileOutputStream fos = new FileOutputStream(file);
        wb.write(fos);
        fos.close();
        wb.close();
    }

    //Write the Event's back to the excel file after edit in GUI
    public void writeEventsToExcel(List<Event> updatedEvents) throws IOException {
        File file = new File(EXCEL_FILE_PATH);
        if (!file.exists()) {
            throw new IOException("Excel file not found at: " + file.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(file);
        Workbook wb = WorkbookFactory.create(fis);
        Sheet sheet = wb.getSheetAt(4); // Events in sheet 4

        // Clear old data (except header)
        for (int i = sheet.getLastRowNum(); i > 0; i--) {
            Row row = sheet.getRow(i);
            if (row != null) sheet.removeRow(row);
        }

        // Write updated event list starting from row 1
        int rowIndex = 1;
        for (Event event : updatedEvents) {
            Row row = sheet.createRow(rowIndex++);

            Cell idCell = row.createCell(0);
            Cell nameCell = row.createCell(1);
            Cell descriptionCell = row.createCell(2);
            Cell locationCell = row.createCell(3);
            Cell dateTimeCell = row.createCell(4);
            Cell capacityCell = row.createCell(5);
            Cell costCell = row.createCell(6);
            Cell photoCell = row.createCell(7);
            Cell registeredStudentsCell = row.createCell(8);
            Cell typeCell = row.createCell(9);

            idCell.setCellValue(event.getEventID());
            nameCell.setCellValue(event.getEventName());
            descriptionCell.setCellValue(event.getDescription());
            locationCell.setCellValue(event.getLocation());
            dateTimeCell.setCellValue(event.getDateTime());
            capacityCell.setCellValue(event.getCapacity());
            costCell.setCellValue(event.getCost());
            photoCell.setCellValue("default");
            registeredStudentsCell.setCellValue(event.getRegisteredStudents());
            typeCell.setCellValue(event.getType());
        }

        fis.close();

        // Save changes
        FileOutputStream fos = new FileOutputStream(file);
        wb.write(fos);
        fos.close();
        wb.close();
    }

    //Read all new data, from all sheets whenever reading
    public void ReadingNameExcelFile() throws IOException {
        File file = new File(EXCEL_FILE_PATH);
        if (!file.exists()) {
            throw new IOException("Excel file not found at: " + file.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(file);
        Workbook wb = WorkbookFactory.create(fis);

        // Finding Students In sheet 2
        Sheet sheet = wb.getSheetAt(2);
        studentList.clear();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell idCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell userCell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell adressCell = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell telephoneCell = row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell emailCell = row.getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell academicLevelCell = row.getCell(5, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell semesterCell = row.getCell(6, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell subjectsCell = row.getCell(8, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell thesisTitleCell = row.getCell(9, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell progressCell = row.getCell(10, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell passCell = row.getCell(11, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

            if (idCell != null) {
                Student student = new Student(idCell.toString(), passCell.toString(), userCell.toString(), emailCell.toString(), adressCell.toString(),
                        telephoneCell.toString(), academicLevelCell.toString(),semesterCell.toString(), subjectsCell.toString(), thesisTitleCell.toString(), Double.parseDouble(progressCell.toString()), thesisTitleCell.toString());
                studentList.add(student);

                System.out.println("- Processed Subjects: '" + student.getSubjects() + "'");
            }
        }

        // Finding Faculty In sheet 3
        sheet = wb.getSheetAt(3);
        facultyList.clear();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell idCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell userCell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell degreeCell = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell researchCell =row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell emailCell =row.getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell officeCell = row.getCell(5, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell coursesCell = row.getCell(6, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell passCell = row.getCell(7, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

            if (idCell != null) {
                // 👇 This line needs to pass 9 arguments
                Faculty faculty = new Faculty(
                        idCell != null ? idCell.toString() : "",
                        passCell != null ? passCell.toString() : "",
                        userCell != null ? userCell.toString() : "",
                        emailCell != null ? emailCell.toString() : "",
                        degreeCell != null ? degreeCell.toString() : "",
                        researchCell != null ? researchCell.toString() : "",
                        officeCell != null ? officeCell.toString() : "",
                        coursesCell != null ? coursesCell.toString() : ""
                );
                facultyList.add(faculty);
            }
        }

        // Finding Events In sheet 4
        sheet = wb.getSheetAt(4);
        eventList.clear();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell idCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell nameCell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell descriptionCell = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell locationCell =row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell dateTimeCell =row.getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell capacityCell = row.getCell(5, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell costCell = row.getCell(6, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell registeredStudentsCell = row.getCell(8, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell typeCell = row.getCell(9, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

            if (idCell != null) {
                // Provide default values for null cells
                String eventType = typeCell != null ? typeCell.toString() : "General";
                String eventName = nameCell != null ? nameCell.toString() : "Unnamed Event";
                String eventDescription = descriptionCell != null ? descriptionCell.toString() : "";
                String eventLocation = locationCell != null ? locationCell.toString() : "TBA";
                String eventDateTime = dateTimeCell != null ? dateTimeCell.toString() : "TBA";
                double eventCapacity = capacityCell != null ? Double.parseDouble(capacityCell.toString()) : 0.0;
                String eventCost = costCell != null ? costCell.toString() : "Free";
                String eventRegisteredStudents = registeredStudentsCell != null ? registeredStudentsCell.toString() : "";

                Event event = new Event(idCell.toString(), eventName, eventDescription, eventLocation,
                        eventDateTime, eventCapacity, eventCost, eventRegisteredStudents, eventType);
                eventList.add(event);
            }
        }

        // Finding Subjects In sheet 0
        sheet = wb.getSheetAt(0);
        subjectList.clear();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell idCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell passCell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

            if (idCell != null) {
                Subject subject = new Subject(idCell.toString(),passCell.toString());
                subjectList.add(subject);
            }
        }

        // Finding Courses In sheet 1
        sheet = wb.getSheetAt(1);
        courseList.clear();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell courseCodecell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell courseNameCell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell subjectCodeCell = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell sectionNumberCell = row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell CapacityCell = row.getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell lectureTimeCell = row.getCell(5, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell finalTimeCell = row.getCell(6, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell locationCell = row.getCell(7, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell teacherCell = row.getCell(8, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

            if (courseCodecell != null) {
                Course course = new Course(courseCodecell.toString(), courseNameCell.toString(), subjectCodeCell.toString(), sectionNumberCell.toString(), Double.parseDouble(CapacityCell.toString()),
                        lectureTimeCell.toString(), finalTimeCell.toString(), locationCell.toString(), teacherCell.toString());
                courseList.add(course);
            }
        }

        wb.close();
        fis.close();
    }
}