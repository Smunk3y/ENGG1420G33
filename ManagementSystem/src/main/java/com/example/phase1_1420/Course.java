package com.example.phase1_1420;

public class Course {
    private String courseCode;
    private String courseName;
    private String instructor;
    private String schedule;
    private String subjectCode;
    private String enrolledStudents;
    
    // Additional fields for backward compatibility
    private String sectionNumber = "N/A";
    private double capacity = 0.0;
    private String location = "N/A";
    private String finalExamDateTime = "N/A";

    public Course(String courseCode, String courseName, String instructor, String schedule) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.instructor = instructor;
        this.schedule = schedule;
        this.enrolledStudents = "";
    }

    public Course(String courseCode, String courseName, String subjectCode) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.subjectCode = subjectCode;
        this.instructor = "Not assigned";
        this.schedule = "TBA";
        this.enrolledStudents = "";
    }

    public String getCourseCode() {
        return courseCode != null ? courseCode : "";
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName != null ? courseName : "";
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructor() {
        return instructor != null ? instructor : "";
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getSchedule() {
        return schedule != null ? schedule : "";
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSubjectCode() {
        return subjectCode != null ? subjectCode : "";
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getEnrolledStudents() {
        return enrolledStudents != null ? enrolledStudents : "";
    }

    public void setEnrolledStudents(String enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public boolean isStudentEnrolled(String username) {
        if (enrolledStudents == null || enrolledStudents.isEmpty()) {
            return false;
        }
        
        String[] students = enrolledStudents.split(",");
        for (String student : students) {
            if (student.trim().equals(username)) {
                return true;
            }
        }
        return false;
    }
    
    // Backwards compatibility methods
    
    public String getSectionNumber() {
        return sectionNumber;
    }
    
    public void setSectionNumber(String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }
    
    public double getCapacity() {
        return capacity;
    }
    
    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getLectureTime() {
        return schedule;
    }
    
    public String getFinalExamDateTime() {
        return finalExamDateTime;
    }
    
    public void setFinalExamDateTime(String finalExamDateTime) {
        this.finalExamDateTime = finalExamDateTime;
    }
    
    public String getTeacherName() {
        return instructor;
    }
    
    // Alias for getSubjectCode() for backward compatibility
    public String getCode() {
        return getSubjectCode();
    }

    @Override
    public String toString() {
        return courseName;
    }
}