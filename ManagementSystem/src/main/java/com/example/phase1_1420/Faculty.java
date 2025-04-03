package com.example.phase1_1420;


public class Faculty extends User {
    private String department;
    private String researchArea;
    private String officeLocation;
    private String coursesOffered;
    private String degree;

    public Faculty(String id, String password, String username, String email,
                   String department, String researchArea,
                   String officeLocation, String coursesOffered, String degree) {
        super(id, password, username, "FACULTY", email);
        this.department = department;
        this.researchArea = researchArea;
        this.officeLocation = officeLocation;
        this.coursesOffered = coursesOffered;
        this.degree = degree;
    }

    // Getters and Setters
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getResearchArea() {
        return researchArea;
    }

    public void setResearchArea(String researchArea) {
        this.researchArea = researchArea;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public String getCoursesOffered() {
        return coursesOffered;
    }

    public void setCoursesOffered(String coursesOffered) {
        this.coursesOffered = coursesOffered;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @Override
    public String toString() {
        return "Faculty ID: " + id +
                " | Name: " + username +
                " | Email: " + email +
                " | Department: " + department +
                " | Research Area: " + researchArea +
                " | Office Location: " + officeLocation +
                " | Courses Offered: " + coursesOffered +
                " | Degree: " + degree;
    }

    // Optional: if these fields might change dynamically
    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
