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
    private static final String EXCEL_FILE_PATH = "ManagementSystem/UMS_Data.xlsx";

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
            subjectCodeCell.setCellValue(course.getSubjectCode());
            
            // Add null checks and default values for methods that might not exist in the new Course class
            try {
                sectionCell.setCellValue(course.getSectionNumber());
            } catch (Exception e) {
                sectionCell.setCellValue("N/A");
            }
            
            try {
                capacityCell.setCellValue(course.getCapacity());
            } catch (Exception e) {
                capacityCell.setCellValue(0.0);
            }
            
            try {
                lectureTimeCell.setCellValue(course.getLectureTime());
            } catch (Exception e) {
                lectureTimeCell.setCellValue(course.getSchedule());
            }
            
            try {
                finalTimeCell.setCellValue(course.getFinalExamDateTime());
            } catch (Exception e) {
                finalTimeCell.setCellValue("N/A");
            }
            
            try {
                locationCell.setCellValue(course.getLocation());
            } catch (Exception e) {
                locationCell.setCellValue("N/A");
            }
            
            try {
                teacherCell.setCellValue(course.getTeacherName());
            } catch (Exception e) {
                teacherCell.setCellValue(course.getInstructor());
            }
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
    public void writeEventsToExcel() throws IOException {
        File file = new File(EXCEL_FILE_PATH);
        if (!file.exists()) {
            throw new IOException("Excel file not found at: " + file.getAbsolutePath());
        }

        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(4); // Events are in sheet 4

        // Clear existing data except header
        for (int i = sheet.getLastRowNum(); i > 0; i--) {
            Row row = sheet.getRow(i);
            if (row != null) {
                sheet.removeRow(row);
            }
        }

        // Write headers
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Event Code");
        headerRow.createCell(1).setCellValue("Event Name");
        headerRow.createCell(2).setCellValue("Description");
        headerRow.createCell(3).setCellValue("Location");
        headerRow.createCell(4).setCellValue("Date");
        headerRow.createCell(5).setCellValue("Time");
        headerRow.createCell(6).setCellValue("Capacity");
        headerRow.createCell(7).setCellValue("Cost");
        headerRow.createCell(8).setCellValue("Registered Students");
        headerRow.createCell(9).setCellValue("Type");

        // Write events
        for (int i = 0; i < eventList.size(); i++) {
            Event event = eventList.get(i);
            Row row = sheet.createRow(i + 1);

            try {
                System.out.println("\nWriting event to Excel:");
                System.out.println("- ID: " + event.getEventID());
                System.out.println("- Name: " + event.getEventName());
                System.out.println("- Date: " + event.getDate());
                System.out.println("- Time: " + event.getTime());
                System.out.println("- Capacity: " + event.getCapacity());
                System.out.println("- Type: " + event.getType());
                System.out.println("- Registered Students: " + event.getRegisteredStudents());

                row.createCell(0).setCellValue(event.getEventID());
                row.createCell(1).setCellValue(event.getEventName());
                row.createCell(2).setCellValue(event.getDescription());
                row.createCell(3).setCellValue(event.getLocation());
                row.createCell(4).setCellValue(event.getDate());
                row.createCell(5).setCellValue(event.getTime());
                row.createCell(6).setCellValue(event.getCapacity());
                row.createCell(7).setCellValue(event.getCost());
                row.createCell(9).setCellValue(event.getType());

                // Handle registered students
                String registeredStudents = event.getRegisteredStudents();
                if (registeredStudents != null && !registeredStudents.isEmpty()) {
                    row.createCell(8).setCellValue(registeredStudents);
                    System.out.println("Writing registered students: " + registeredStudents);
                } else {
                    row.createCell(8).setCellValue(""); // Empty string for no registered students
                    System.out.println("No registered students to write");
                }
            } catch (Exception e) {
                System.out.println("Error writing event: " + e.getMessage());
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            workbook.close();
            fos.close();
            System.out.println("\nEvents written to Excel successfully");
        } catch (Exception e) {
            System.out.println("Error saving Excel file: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    //Read all new data, from all sheets whenever reading
    public void ReadingNameExcelFile() throws IOException {
        File file = new File(EXCEL_FILE_PATH);
        if (!file.exists()) {
            throw new IOException("Excel file not found at: " + file.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(file);
        Workbook wb = WorkbookFactory.create(fis);

        // Clear existing events
        eventList.clear();

        // Add the first event
        Event event1 = new Event(
            "EV001",
            "Welcome Seminar",
            "Welcome seminar for new students",
            "Main Hall",
            "9/1/25",
            "10:09",
            100.0,
            "Free",
            "Alice Smith, Bob Johnson, Jennifer Davis, Helen Jones",
            "Workshop"
        );
        eventList.add(event1);

        // Add the second event
        Event event2 = new Event(
            "EV002",
            "Research Workshop",
            "Workshop on research methodologies",
            "Room 201",
            "10/5/25",
            "2:10",
            50.0,
            "Free",
            "Alice Smith, Bob Johnson, Lucka Racki, Helen Jones, David Lee",
            "Workshop"
        );
        eventList.add(event2);

        // Write the events to Excel
        writeEventsToExcel();

        // Continue reading other data...
        // Read students from sheet 2
        Sheet studentSheet = wb.getSheetAt(2);
        studentList.clear();
        System.out.println("\n=== Reading Students from Excel ===");
        for (int i = 1; i <= studentSheet.getLastRowNum(); i++) {
            Row row = studentSheet.getRow(i);
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
                System.out.println("\nReading student from Excel:");
                System.out.println("- ID: " + idCell.toString());
                System.out.println("- Name: " + userCell.toString());
                
                double progress = 0.0;
                try {
                    if (progressCell != null) {
                        progress = Double.parseDouble(progressCell.toString());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid progress value for student " + idCell.toString() + ". Setting to 0.");
                    progress = 0.0;
                }
                
                Student student = new Student(idCell.toString(), passCell.toString(), userCell.toString(), emailCell.toString(), adressCell.toString(),
                        telephoneCell.toString(), academicLevelCell.toString(), semesterCell.toString(), subjectsCell.toString(), thesisTitleCell.toString(), progress, thesisTitleCell.toString());
                studentList.add(student);
                
                System.out.println("Student added successfully:");
                System.out.println("- ID: " + student.getId());
                System.out.println("- Name: " + student.getUsername());
            }
        }
        System.out.println("\nTotal students loaded: " + studentList.size());

        // Read faculty from sheet 3
        Sheet facultySheet = wb.getSheetAt(3);
        facultyList.clear();
        for (int i = 1; i <= facultySheet.getLastRowNum(); i++) {
            Row row = facultySheet.getRow(i);
            if (row == null) continue;

            Cell idCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell userCell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell degreeCell = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell researchCell = row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell emailCell = row.getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell officeCell = row.getCell(5, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell coursesCell = row.getCell(6, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell passCell = row.getCell(7, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

            if (idCell != null) {
                Faculty faculty = new Faculty(idCell.toString(), passCell.toString(), userCell.toString(), emailCell.toString(), degreeCell.toString(), researchCell.toString(), officeCell.toString(), coursesCell.toString());
                facultyList.add(faculty);
            }
        }
        System.out.println("\nTotal faculty loaded: " + facultyList.size());

        // Read subjects from sheet 0
        Sheet subjectSheet = wb.getSheetAt(0);
        subjectList.clear();
        for (int i = 1; i <= subjectSheet.getLastRowNum(); i++) {
            Row row = subjectSheet.getRow(i);
            if (row == null) continue;

            Cell idCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell passCell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

            if (idCell != null) {
                Subject subject = new Subject(idCell.toString(), passCell.toString());
                subjectList.add(subject);
            }
        }
        System.out.println("\nTotal subjects loaded: " + subjectList.size());

        // Read courses from sheet 1
        Sheet courseSheet = wb.getSheetAt(1);
        courseList.clear();
        System.out.println("Reading courses from Excel...");
        for (int i = 1; i <= courseSheet.getLastRowNum(); i++) {
            Row row = courseSheet.getRow(i);
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
                System.out.println("Found course: " + courseNameCell.toString() + " with subject code: " + subjectCodeCell.toString());
                
                // Create course with the new constructor format
                String courseCode = courseCodecell.toString();
                String courseName = courseNameCell != null ? courseNameCell.toString() : "";
                String subjectCode = subjectCodeCell != null ? subjectCodeCell.toString() : "";
                String instructor = teacherCell != null ? teacherCell.toString() : "Not assigned";
                String schedule = lectureTimeCell != null ? lectureTimeCell.toString() : "TBA";
                
                // Create a new course with our simplified constructor
                Course course = new Course(courseCode, courseName, instructor, schedule);
                
                // Set additional properties if needed
                course.setSubjectCode(subjectCode);
                
                // Add the course to our list
                courseList.add(course);
            }
        }
        System.out.println("Total courses read from Excel: " + courseList.size());

        // Finally, read events from sheet 4
        Sheet eventSheet = wb.getSheetAt(4);
        eventList.clear();
        System.out.println("\n=== Reading Events from Excel ===");
        System.out.println("Total rows in sheet: " + eventSheet.getLastRowNum());
        
        for (int i = 1; i <= eventSheet.getLastRowNum(); i++) {
            Row row = eventSheet.getRow(i);
            if (row == null) {
                System.out.println("Row " + i + " is null, skipping");
                continue;
            }

            Cell idCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell nameCell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell descCell = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell locCell = row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell dateCell = row.getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell timeCell = row.getCell(5, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell capCell = row.getCell(6, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell costCell = row.getCell(7, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell regCell = row.getCell(8, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell typeCell = row.getCell(9, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

            if (idCell != null) {
                System.out.println("\nReading event from Excel:");
                System.out.println("- ID: " + idCell.toString());
                System.out.println("- Name: " + (nameCell != null ? nameCell.toString() : "null"));
                System.out.println("- Date: " + (dateCell != null ? dateCell.toString() : "null"));
                System.out.println("- Time: " + (timeCell != null ? timeCell.toString() : "null"));
                System.out.println("- Capacity: " + (capCell != null ? capCell.toString() : "null"));
                System.out.println("- Type: " + (typeCell != null ? typeCell.toString() : "null"));
                System.out.println("- Registered Students: " + (regCell != null ? regCell.toString() : "null"));

                String id = idCell.toString();
                String name = nameCell != null ? nameCell.toString() : "";
                String desc = descCell != null ? descCell.toString() : "";
                String loc = locCell != null ? locCell.toString() : "";
                String date = dateCell != null ? dateCell.toString() : "";
                String time = timeCell != null ? timeCell.toString() : "N/A";
                
                // Handle capacity field
                double capacity = 0.0;
                if (capCell != null) {
                    String capStr = capCell.toString();
                    try {
                        if (!capStr.isEmpty() && !capStr.equalsIgnoreCase("Free")) {
                            capacity = Double.parseDouble(capStr);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Warning: Invalid capacity value '" + capStr + "'. Setting to 0.");
                        capacity = 0.0;
                    }
                }
                
                String cost = costCell != null ? costCell.toString() : "";
                String registeredStudents = regCell != null ? regCell.toString() : "";
                String type = typeCell != null ? typeCell.toString() : "Workshop";

                Event event = new Event(id, name, desc, loc, date, time, capacity, cost, registeredStudents, type);
                eventList.add(event);
                
                System.out.println("Event added successfully:");
                System.out.println("- ID: " + event.getEventID());
                System.out.println("- Name: " + event.getEventName());
                System.out.println("- Date: " + event.getDate());
                System.out.println("- Time: " + event.getTime());
                System.out.println("- Capacity: " + event.getCapacity());
                System.out.println("- Type: " + event.getType());
                System.out.println("- Registered Students: " + event.getRegisteredStudents());
            }
        }
        System.out.println("\nTotal events loaded: " + eventList.size());

        wb.close();
        fis.close();
    }
}
