package com.example.phase1_1420;

public class Course extends Subject {
    private String courseCode;
    private String courseName;
    private String sectionNumber;
    private double capacity;
    private String lectureTime;
    private String finalExamDateTime;
    private String location;
    private String teacherName;

    public Course(String courseCode, String courseName, String subjectCode,
                  String sectionNumber, double capacity, String lectureTime,
                  String finalExamDateTime, String location, String teacherName) {

        super(subjectCode, subjectCode); // Use subjectCode for both code and name
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.sectionNumber = sectionNumber;
        this.capacity = capacity;
        this.lectureTime = lectureTime;
        this.finalExamDateTime = finalExamDateTime;
        this.location = location;
        this.teacherName = teacherName;
    }

    //Setters and getters
    public String getCourseCode(){
       return this.courseCode;
    }

    public void setCourseCode(String Input){
        this.courseCode = Input;
    }

    public String getCourseName(){
        return this.courseName;
    }

    public void setCourseName(String Input){
        this.courseName = Input;
    }

    public String getSectionNumber(){
        return this.sectionNumber;
    }

    public void setSectionNumber(String Input){
        this.sectionNumber = Input;
    }

    public String getCapacity(){
        return ( "" + this.capacity);
    }

    public void setCapacity(Double Input){
        this.capacity = Input;
    }

    public String getLectureTime(){
        return this.lectureTime;
    }

    public void setLectureTime(String Input){
        this.lectureTime = Input;
    }

    public String getFinalExamDateTime(){
        return this.finalExamDateTime;
    }

    public void setFinalExamDateTime(String Input){
        this.finalExamDateTime = Input;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String Input){
        this.location = Input;
    }

    public String getTeacherName(){
        return this.teacherName;
    }

    public void setTeacherName(String Input){
        this.teacherName = Input;
    }

    @Override
    public String toString() {
        return "Course: " + courseName +
                " | Subject: " + subjectName +
                " | Section: " + sectionNumber +
                " | Teacher: " + teacherName;
    }
}