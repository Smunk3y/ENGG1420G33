package com.example.phase1_1420;

import java.util.HashMap;
import java.util.Map;

/**
 * User Database that handles authentication and user role management (admin, or user / student).
 */
public class UserDatabase {

    private static final Map<String, User> userMap = new HashMap<>();

    public static User CurrentUser = null;

    // Track changes to event registrations during the application session
    private static final java.util.Map<String, java.util.Set<String>> removedRegistrations = new java.util.HashMap<>();

    static {

        //Read Excel Sheet for Students & populate into map
        try {
            ExcelFile reader = new ExcelFile();
            reader.ReadingNameExcelFile();

            for (Student student : reader.studentList) {
                userMap.put(student.getId(), student);
            }
            for (Faculty faculty : reader.facultyList){
                userMap.put(faculty.getId(), faculty);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Admin
        userMap.put("ADMIN", new User("ADMIN001", "admin123", "admin", "ADMIN", "admin@uofg.com"));

        // User -- Delete default user because it's useless for devlopment now
        //userMap.put("USER", new User("USER001", "user123", "user", "USER", "user@uofg.com"));
    }

    public static String authenticate(String username, String password) {
        //TO upper case to remove case sensitivty for login id
        User user = userMap.get(username.toUpperCase());
        if (user != null && user.getPassword().equals(password)) {
            CurrentUser = user;
            return (CurrentUser.role);
        }
        return null;
    }

    public static String getDisplayName(String username) {
        User user = userMap.get(username);
        return (user != null) ? user.getId() : "Unknown";
    }

    /**
     * Refreshes the user database with the latest data from the Excel file.
     * This ensures that any changes made during the session are reflected when users log in.
     *
     * @param excelFile The Excel file reader with the latest data
     */
    public static void refreshUserData(ExcelFile excelFile) {
        // Clear the existing user map (except admin)
        User adminUser = userMap.get("ADMIN");
        userMap.clear();
        
        // Re-add the admin user
        if (adminUser != null) {
            userMap.put("ADMIN", adminUser);
        } else {
            // Create default admin if it doesn't exist
            userMap.put("ADMIN", new User("ADMIN001", "admin123", "admin", "ADMIN", "admin@uofg.com"));
        }
        
        // Add all students from the Excel file
        for (Student student : excelFile.studentList) {
            userMap.put(student.getId(), student);
        }
        
        // Add all faculty from the Excel file
        for (Faculty faculty : excelFile.facultyList) {
            userMap.put(faculty.getId(), faculty);
        }
        
        System.out.println("User database refreshed:");
        System.out.println("- Students: " + excelFile.studentList.size());
        System.out.println("- Faculty: " + excelFile.facultyList.size());
        System.out.println("- Total users: " + userMap.size());
    }

    /**
     * Records when a student's registration is removed from an event during the current session
     * @param eventId The ID of the event
     * @param username The username of the student removed from the event
     */
    public static void trackRemovedRegistration(String eventId, String username) {
        if (!removedRegistrations.containsKey(eventId)) {
            removedRegistrations.put(eventId, new java.util.HashSet<>());
        }
        removedRegistrations.get(eventId).add(username);
        System.out.println("Tracking removed registration: " + username + " from event " + eventId);
    }
    
    /**
     * Checks if a student was removed from an event during the current session
     * @param eventId The ID of the event
     * @param username The username of the student
     * @return true if the student was removed from the event during the current session
     */
    public static boolean wasRegistrationRemoved(String eventId, String username) {
        return removedRegistrations.containsKey(eventId) && 
               removedRegistrations.get(eventId).contains(username);
    }
    
    /**
     * Clears the tracking of removed registrations for a student
     * This would be called when a student re-registers for an event
     * @param eventId The ID of the event
     * @param username The username of the student
     */
    public static void clearRemovedRegistration(String eventId, String username) {
        if (removedRegistrations.containsKey(eventId)) {
            removedRegistrations.get(eventId).remove(username);
            if (removedRegistrations.get(eventId).isEmpty()) {
                removedRegistrations.remove(eventId);
            }
            System.out.println("Cleared removed registration tracking: " + username + " for event " + eventId);
        }
    }
}