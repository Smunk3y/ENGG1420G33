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

    //Write the subject's back to the excel file after edit in GUI
    public void writeSubjectsToExcel(List<Subject> updatedSubjects) throws IOException {
        File file = new File("src/main/resources/UMS_Data.xlsx");
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
        File file = new File("UMS_Data.xlsx");
        FileInputStream fis = new FileInputStream(file);
        Workbook wb = WorkbookFactory.create(fis);
        Sheet sheet = wb.getSheetAt(1); // Subjects in sheet 0

        // Clear old data (except header)
        for (int i = sheet.getLastRowNum(); i > 0; i--) {
            Row row = sheet.getRow(i);
            if (row != null) sheet.removeRow(row);
        }

        // Write updated subject list starting from row 1
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
        File file = new File("UMS_Data.xlsx");
        FileInputStream fis = new FileInputStream(file);
        Workbook wb = WorkbookFactory.create(fis);
        Sheet sheet = wb.getSheetAt(2); // Subjects in sheet 0

        // Clear old data (except header)
        for (int i = sheet.getLastRowNum(); i > 0; i--) {
            Row row = sheet.getRow(i);
            if (row != null) sheet.removeRow(row);
        }

        // Write updated subject list starting from row 1
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
        File file = new File("UMS_Data.xlsx");
        FileInputStream fis = new FileInputStream(file);
        Workbook wb = WorkbookFactory.create(fis);
        Sheet sheet = wb.getSheetAt(3);

        // Clear old data (except header)
        for (int i = sheet.getLastRowNum(); i > 0; i--) {
            Row row = sheet.getRow(i);
            if (row != null) sheet.removeRow(row);
        }

        // Write updated subject list starting from row 1
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
            researchCell.setCellValue(faculty.getResearchInterest());
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
        File file = new File("UMS_Data.xlsx");
        FileInputStream fis = new FileInputStream(file);
        Workbook wb = WorkbookFactory.create(fis);
        Sheet sheet = wb.getSheetAt(4); // Subjects in sheet 0

        // Clear old data (except header)
        for (int i = sheet.getLastRowNum(); i > 0; i--) {
            Row row = sheet.getRow(i);
            if (row != null) sheet.removeRow(row);
        }

        // Write updated subject list starting from row 1
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

            idCell.setCellValue(event.getEventID());
            nameCell.setCellValue(event.getEventID());
            descriptionCell.setCellValue(event.getDescription());
            locationCell.setCellValue(event.getLocation());
            dateTimeCell.setCellValue(event.getDateTime()); // Assumed as string or formatted
            capacityCell.setCellValue(event.getCapacity());
            costCell.setCellValue(event.getCost());
            photoCell.setCellValue("default"); //PHOTO
            registeredStudentsCell.setCellValue(event.getRegisteredStudents());
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
        File file = new File("/Users/keanurangayah/Downloads/JAVALAbs/ENGG1420G33/ManagementSystem/src/main/java/com/example/phase1_1420/UMS_Data.xlsx");
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
                System.out.println("\nReading student from Excel:");
                System.out.println("- ID: " + idCell.toString());
                System.out.println("- Name: " + userCell.toString());
                System.out.println("- Raw Subjects Cell: '" + (subjectsCell != null ? subjectsCell.toString() : "null") + "'");
                
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
                        telephoneCell.toString(), academicLevelCell.toString(),semesterCell.toString(), subjectsCell.toString(), thesisTitleCell.toString(), progress);
                studentList.add(student);
                
                // Debug: Print the student's subjects after creation
                System.out.println("- Processed Subjects: '" + student.getSubjects() + "'");
            }
        }

        // Finding Students In sheet 2
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
                Faculty faculty = new Faculty(idCell.toString(),passCell.toString(),userCell.toString(),emailCell.toString(), degreeCell.toString()
                , researchCell.toString(), officeCell.toString(), coursesCell.toString());
                facultyList.add(faculty);
            }
        }

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

            if (idCell != null) {
                Event event = new Event(idCell.toString(), nameCell.toString(), descriptionCell.toString(), locationCell.toString(),
                        dateTimeCell.toString(),Double.parseDouble(capacityCell.toString()), costCell.toString(), registeredStudentsCell.toString());
                eventList.add(event);
            }
        }

        sheet = wb.getSheetAt(0);
        subjectList.clear();
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;

            //Wont enter Blank Cells into the object
            Cell idCell = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            Cell passCell = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);


            if (idCell != null) {
                Subject subject = new Subject(idCell.toString(),passCell.toString());
                subjectList.add(subject);
            }
        }

        sheet = wb.getSheetAt(1);
        courseList.clear();
        System.out.println("Reading courses from Excel...");
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) continue;

            //Wont enter Blank Cells into the object
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
                Course course = new Course(courseCodecell.toString(), courseNameCell.toString(), subjectCodeCell.toString(), sectionNumberCell.toString(), Double.parseDouble(CapacityCell.toString()),
                        lectureTimeCell.toString(), finalTimeCell.toString(), locationCell.toString(), teacherCell.toString());
                courseList.add(course);
            }
        }
        System.out.println("Total courses read from Excel: " + courseList.size());

        wb.close();
        fis.close();
    }
}
