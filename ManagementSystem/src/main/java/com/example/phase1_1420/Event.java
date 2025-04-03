package com.example.phase1_1420;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {

    protected String eventID;
    protected String eventName;
    protected String description;
    protected String location;
    protected String date;
    protected String time;
    protected double capacity;
    protected String cost;
    
    
    protected String registeredStudents;
    protected String type;

    public Event(String eventID, String eventName, String description, String location, String date, double capacity,
                 String cost, String registeredStudents, String type) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.date = formatDate(date);
        this.time = "N/A";
        this.capacity = capacity;
        this.cost = cost;
        this.registeredStudents = registeredStudents;
        this.type = type != null && !type.equals("default") ? type : "Workshop";
    }

    public Event(String eventID, String eventName, String description, String location, String date, String time, double capacity, String cost, String registeredStudents, String type) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.description = description;
        this.location = location;
        this.date = formatDate(date);
        this.time = formatTime(time);
        this.capacity = capacity;
        this.cost = cost;
        this.registeredStudents = registeredStudents;
        this.type = type != null && !type.equals("default") ? type : "Workshop";
    }

    private String formatDate(String inputDate) {
        if (inputDate == null || inputDate.isEmpty()) {
            return "";
        }
        try {
            // Try different date formats - add more formats to handle "09/01/0025" style dates
            SimpleDateFormat[] formats = {
                new SimpleDateFormat("yyyy-MM-dd"),
                new SimpleDateFormat("MM/dd/yyyy"),
                new SimpleDateFormat("MM/dd/00yy"), // Special format for 2-digit years with leading zeros
                new SimpleDateFormat("dd/MM/yyyy")
            };
            
            // First try the exact input without parsing to preserve existing data
            if (inputDate.matches("\\d{2}/\\d{2}/\\d{4}")) {
                return inputDate; // Return as-is if it's already in MM/dd/yyyy format
            }
            
            // Otherwise try parsing with different formats
            for (SimpleDateFormat format : formats) {
                try {
                    format.setLenient(true); // Be more forgiving with date parsing
                    Date date = format.parse(inputDate);
                    return format.format(date);
                } catch (ParseException e) {
                    // Try next format
                    continue;
                }
            }
            
            // If none of the formats work, return the original date
            return inputDate;
        } catch (Exception e) {
            System.out.println("Keeping original date format: " + inputDate);
            return inputDate;
        }
    }

    private String formatTime(String inputTime) {
        if (inputTime == null || inputTime.isEmpty() || inputTime.equals("N/A")) {
            return "N/A";
        }
        try {
            // Try to parse the time in various formats
            SimpleDateFormat[] formats = {
                new SimpleDateFormat("HH:mm"),
                new SimpleDateFormat("h:mm"),
                new SimpleDateFormat("H:mm"),
                new SimpleDateFormat("HH:mm a")
            };
            
            for (SimpleDateFormat format : formats) {
                try {
                    Date time = format.parse(inputTime);
                    // Format the time consistently as HH:mm
                    return new SimpleDateFormat("HH:mm").format(time);
                } catch (ParseException e) {
                    continue;
                }
            }
            return inputTime; // Return original if no format matches
        } catch (Exception e) {
            return inputTime; // Return original if parsing fails
        }
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = formatDate(date);
    }

    public String getTime() {
        return time != null && !time.isEmpty() ? time : "N/A";
    }

    public void setTime(String time) {
        this.time = formatTime(time);
    }

    public String getDateTime() {
        return date + (time.equals("N/A") ? "" : " " + time);
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getRegisteredStudents() {
        return registeredStudents != null ? registeredStudents : "";
    }

    public boolean isStudentRegistered(String username) {
        if (registeredStudents == null || registeredStudents.isEmpty()) {
            return false;
        }
        // Split by comma and trim each name
        String[] students = registeredStudents.split(",");
        for (String student : students) {
            if (student.trim().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void setRegisteredStudents(String registeredStudents) {
        this.registeredStudents = registeredStudents;
    }

    public String getType() {
        return type != null && !type.equals("default") ? type : "Workshop";
    }

    public void setType(String type) {
        this.type = type != null && !type.equals("default") ? type : "Workshop";
    }

    @Override
    public String toString() {
        return "Event ID: " + eventID +
                " | Name: " + eventName +
                " | Description: " + description +
                " | Location: " + location +
                " | Date: " + date +
                " | Time: " + time +
                " | Capacity: " + capacity +
                " | Cost: " + cost +
                " | Students Registered: " + registeredStudents +
                " | Type: " + type;
    }

}
