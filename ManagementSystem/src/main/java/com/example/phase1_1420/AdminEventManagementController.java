package com.example.phase1_1420;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;

public class AdminEventManagementController implements Initializable {
    // Common fields
    private final ExcelFile excelReader = new ExcelFile();
    private Event selectedEvent;
    private final ObservableList<Event> allEvents = FXCollections.observableArrayList();
    private final ObservableList<Event> filteredEvents = FXCollections.observableArrayList();
    private YearMonth currentYearMonth = YearMonth.now();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    
    // View containers
    @FXML private VBox listView;
    @FXML private VBox calendarView;
    @FXML private VBox eventDetailsView;
    @FXML private VBox eventFormView;
    @FXML private GridPane calendarGrid;
    
    // Calendar View fields
    @FXML private Label monthYearLabel;
    @FXML private Button prevMonthButton;
    @FXML private Button nextMonthButton;
    
    // List View fields
    @FXML private ComboBox<String> eventTypeFilter;
    @FXML private ComboBox<String> dateFilter;
    @FXML private Button applyFilterButton;
    @FXML private Button clearFilterButton;
    @FXML private TableView<Event> eventsTable;
    @FXML private TableColumn<Event, String> eventNameColumn;
    @FXML private TableColumn<Event, String> eventDateColumn;
    @FXML private TableColumn<Event, String> eventTimeColumn;
    @FXML private TableColumn<Event, String> eventLocationColumn;
    @FXML private TableColumn<Event, String> eventTypeColumn;
    @FXML private TableColumn<Event, String> eventCapacityColumn;
    @FXML private TableColumn<Event, String> eventRegisteredColumn;
    
    // Event Details View fields
    @FXML private Label selectedEventNameText;
    @FXML private Label selectedEventDateTimeText;
    @FXML private Label selectedEventLocationText;
    @FXML private Label selectedEventDescriptionText;
    @FXML private Label selectedEventTypeText;
    @FXML private Label selectedEventCapacityText;
    @FXML private Label selectedEventCostText;
    @FXML private TableView<Student> registeredStudentsTable;
    @FXML private TableColumn<Student, String> studentIdColumn;
    @FXML private TableColumn<Student, String> studentNameColumn;
    @FXML private TableColumn<Student, String> studentEmailColumn;
    
    // Event Form fields
    @FXML private Label formTitleLabel;
    @FXML private TextField eventNameField;
    @FXML private TextField dateField;
    @FXML private TextField timeField;
    @FXML private TextField eventLocationField;
    @FXML private TextArea eventDescriptionField;
    @FXML private ComboBox<String> eventTypeField;
    @FXML private TextField eventCapacityField;
    @FXML private TextField eventCostField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("\n=== Initializing AdminEventManagementController ===");
            
            // Check if user is admin
            if (!UserDatabase.CurrentUser.getRole().equals("ADMIN")) {
                showAlert("Access Denied", "You do not have permission to access this page.", Alert.AlertType.ERROR);
                return;
            }
            
            // Load data from Excel
            excelReader.ReadingNameExcelFile();
            allEvents.setAll(excelReader.eventList);
            filteredEvents.setAll(allEvents);
            
            // Initialize calendar
            updateCalendar();
            
            // Initialize filter options
            initializeFilters();
            
            // Initialize table columns
            initializeColumns();
            
            // Initialize registered students table
            initializeRegisteredStudentsTable();
            
            // Set up event handlers
            setupEventHandlers();
            
            // Show list view by default
            showListView();
            
            System.out.println("AdminEventManagementController initialized successfully");
            System.out.println("Total events loaded: " + allEvents.size());
        } catch (IOException e) {
            showAlert("Error", "Failed to initialize: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateCalendar() {
        if (currentYearMonth == null) {
            currentYearMonth = YearMonth.now();
        }
        
        // Clear existing calendar cells
        calendarGrid.getChildren().clear();
        
        // Add day headers
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-weight: bold;");
            calendarGrid.add(dayLabel, i, 0);
        }
        
        // Update month/year label
        monthYearLabel.setText(currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        
        // Get first day of month and adjust to start of week
        LocalDate firstDay = currentYearMonth.atDay(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // Convert to 0-6 (Sun-Sat)
        
        // Add empty cells for days before the first day of the month
        for (int i = 0; i < dayOfWeek; i++) {
            calendarGrid.add(new Label(""), i, 1);
        }
        
        // Add cells for each day of the month
        int row = 1;
        int col = dayOfWeek;
        for (int day = 1; day <= currentYearMonth.lengthOfMonth(); day++) {
            LocalDate date = currentYearMonth.atDay(day);
            calendarGrid.add(createDateCell(date), col, row);
            
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createDateCell(LocalDate date) {
        VBox cell = new VBox(2);
        cell.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-padding: 5;");
        cell.setPrefSize(100, 80);
        
        // Add date label
        Label dateLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dateLabel.setStyle("-fx-font-weight: bold;");
        cell.getChildren().add(dateLabel);
        
        // Add events for this date
        List<Event> dayEvents = getEventsForDate(date);
        for (Event event : dayEvents) {
            Label eventLabel = new Label(event.getEventName());
            eventLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #4CAF50;");
            eventLabel.setWrapText(true);
            cell.getChildren().add(eventLabel);
        }
        
        return cell;
    }

    private List<Event> getEventsForDate(LocalDate date) {
        List<Event> dayEvents = new ArrayList<>();
        for (Event event : allEvents) {
            try {
                String dateStr = extractDate(event.getDateTime());
                LocalDate eventDate = LocalDate.parse(dateStr, DATE_FORMATTER);
                if (eventDate.equals(date)) {
                    dayEvents.add(event);
                }
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing date: " + e.getMessage());
            }
        }
        return dayEvents;
    }

    private void initializeFilters() {
        // Initialize event type filter with predefined types
        ObservableList<String> eventTypes = FXCollections.observableArrayList(
            "Workshop",
            "Seminar",
            "Conference",
            "Meeting",
            "Social",
            "Other"
        );
        
        // Set items for both filter and form dropdowns
        eventTypeFilter.setItems(eventTypes);
        eventTypeField.setItems(eventTypes);
        
        // Set default value to ensure dropdown is visible
        eventTypeField.setValue("Workshop");
        
        // Initialize date range filter
        dateFilter.setItems(FXCollections.observableArrayList(
            "All Dates",
            "Today",
            "This Week",
            "This Month",
            "Next Month"
        ));
    }

    private void initializeColumns() {
        // Initialize table columns with proper cell value factories
        eventNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEventName()));
        eventDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        eventTimeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        eventLocationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        eventTypeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        eventCapacityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacity())));
        eventRegisteredColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRegisteredStudents()));

        // Set up the event details view
        eventsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedEvent = newSelection;
                selectedEventNameText.setText(selectedEvent.getEventName());
                selectedEventDateTimeText.setText(selectedEvent.getDate() + " " + selectedEvent.getTime());
                selectedEventLocationText.setText(selectedEvent.getLocation());
                selectedEventDescriptionText.setText(selectedEvent.getDescription());
                selectedEventTypeText.setText(selectedEvent.getType());
                selectedEventCapacityText.setText(String.valueOf(selectedEvent.getCapacity()));
                selectedEventCostText.setText(selectedEvent.getCost());
                
                // Update registered students table
                updateRegisteredStudentsTable(selectedEvent.getRegisteredStudents());
            }
        });

        // Set the items
        eventsTable.setItems(filteredEvents);
    }

    private void initializeRegisteredStudentsTable() {
        studentIdColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getId()));
        studentNameColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getUsername()));
        studentEmailColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEmail()));
    }

    private void setupEventHandlers() {
        // Table selection handlers
        eventsTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                selectedEvent = newSelection;
            }
        );

        // Filter button handlers
        applyFilterButton.setOnAction(e -> applyFilters());
        clearFilterButton.setOnAction(e -> clearFilters());
    }

    // Navigation methods
    @FXML
    private void handleViewCalendar() {
        showCalendarView();
    }

    @FXML
    private void handleBackToList() {
        showListView();
    }

    @FXML
    private void handleViewEventDetails() {
        if (selectedEvent != null) {
            showEventDetails();
        } else {
            showAlert("No Selection", "Please select an event first.", Alert.AlertType.WARNING);
        }
    }

    private void showListView() {
        listView.setVisible(true);
        listView.setManaged(true);
        calendarView.setVisible(false);
        calendarView.setManaged(false);
        eventDetailsView.setVisible(false);
        eventDetailsView.setManaged(false);
        eventFormView.setVisible(false);
        eventFormView.setManaged(false);
        updateListTable();
    }

    private void showCalendarView() {
        listView.setVisible(false);
        listView.setManaged(false);
        calendarView.setVisible(true);
        calendarView.setManaged(true);
        eventDetailsView.setVisible(false);
        eventDetailsView.setManaged(false);
        eventFormView.setVisible(false);
        eventFormView.setManaged(false);
        updateCalendar();
    }

    private void showEventDetails() {
        if (selectedEvent != null) {
            listView.setVisible(false);
            listView.setManaged(false);
            calendarView.setVisible(false);
            calendarView.setManaged(false);
            eventDetailsView.setVisible(true);
            eventDetailsView.setManaged(true);
            eventFormView.setVisible(false);
            eventFormView.setManaged(false);
            updateEventDetails(selectedEvent);
        }
    }

    private void showEventForm(boolean isEdit) {
        formTitleLabel.setText(isEdit ? "Edit Event" : "Add New Event");
        
        if (isEdit && selectedEvent != null) {
            eventNameField.setText(selectedEvent.getEventName());
            dateField.setText(selectedEvent.getDate());
            timeField.setText(selectedEvent.getTime());
            eventLocationField.setText(selectedEvent.getLocation());
            eventDescriptionField.setText(selectedEvent.getDescription());
            eventTypeField.setValue(selectedEvent.getType());
            eventCapacityField.setText(String.valueOf(selectedEvent.getCapacity()));
            eventCostField.setText(selectedEvent.getCost());
        } else {
            eventNameField.clear();
            dateField.clear();
            timeField.clear();
            eventLocationField.clear();
            eventDescriptionField.clear();
            eventTypeField.setValue(null);
            eventCapacityField.clear();
            eventCostField.clear();
        }
        
        eventFormView.setVisible(true);
        eventFormView.setManaged(true);
        listView.setVisible(false);
        listView.setManaged(false);
        calendarView.setVisible(false);
        calendarView.setManaged(false);
        eventDetailsView.setVisible(false);
        eventDetailsView.setManaged(false);
    }

    // Data update methods
    private void updateListTable() {
        eventsTable.setItems(filteredEvents);
    }

    private void updateEventDetails(Event event) {
        if (event != null) {
            selectedEventNameText.setText(event.getEventName());
            selectedEventDateTimeText.setText(event.getDateTime());
            selectedEventLocationText.setText(event.getLocation());
            selectedEventDescriptionText.setText(event.getDescription());
            selectedEventTypeText.setText(event.getType());
            selectedEventCapacityText.setText(String.valueOf(event.getCapacity()));
            selectedEventCostText.setText(event.getCost());
            
            // Update registered students table
            updateRegisteredStudentsTable(event.getRegisteredStudents());
        }
    }

    private void updateRegisteredStudentsTable(String registeredStudentsStr) {
        ObservableList<Student> registeredStudents = FXCollections.observableArrayList();
        if (registeredStudentsStr != null && !registeredStudentsStr.isEmpty()) {
            String[] studentNames = registeredStudentsStr.split(",");
            for (String name : studentNames) {
                name = name.trim();
                // Find the student in the student list
                for (Student student : excelReader.studentList) {
                    if (student.getUsername().equals(name)) {
                        registeredStudents.add(student);
                        break;
                    }
                }
            }
        }
        registeredStudentsTable.setItems(registeredStudents);
    }

    // Filter methods
    @FXML
    private void applyFilters() {
        filteredEvents.clear();
        String selectedType = eventTypeFilter.getValue();
        String selectedDateRange = dateFilter.getValue();

        for (Event event : allEvents) {
            boolean typeMatch = selectedType == null || event.getType().equals(selectedType);
            boolean dateMatch = isDateInRange(event.getDateTime(), selectedDateRange);

            if (typeMatch && dateMatch) {
                filteredEvents.add(event);
            }
        }
        updateListTable();
    }

    @FXML
    private void clearFilters() {
        eventTypeFilter.setValue(null);
        dateFilter.setValue(null);
        filteredEvents.setAll(allEvents);
        updateListTable();
    }

    private boolean isDateInRange(String eventDateTime, String dateRange) {
        if (dateRange == null || dateRange.equals("All Dates")) {
            return true;
        }

        try {
            String dateStr = extractDate(eventDateTime);
            LocalDate eventDate = LocalDate.parse(dateStr, DATE_FORMATTER);
            LocalDate today = LocalDate.now();

            switch (dateRange) {
                case "Today":
                    return eventDate.equals(today);
                case "This Week":
                    return eventDate.isAfter(today.minusDays(1)) && 
                           eventDate.isBefore(today.plusWeeks(1));
                case "This Month":
                    return eventDate.getMonth() == today.getMonth() && 
                           eventDate.getYear() == today.getYear();
                case "Next Month":
                    return eventDate.getMonth() == today.getMonth().plus(1) && 
                           eventDate.getYear() == today.getYear();
                default:
                    return true;
            }
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            return false;
        }
    }

    // Event management methods
    @FXML
    private void handleAddEvent() {
        System.out.println("\n=== Adding New Event ===");
        
        String eventName = eventNameField.getText();
        String description = eventDescriptionField.getText();
        String location = eventLocationField.getText();
        String date = dateField.getText();
        String time = timeField.getText();
        String capacityText = eventCapacityField.getText();
        String cost = eventCostField.getText();
        String type = eventTypeField.getValue();

        if (eventName.isEmpty() || description.isEmpty() || location.isEmpty() || 
            date.isEmpty() || capacityText.isEmpty() || cost.isEmpty() || type == null) {
            showAlert("Error", "Please fill in all required fields", Alert.AlertType.ERROR);
            return;
        }

        try {
            double capacity = Double.parseDouble(capacityText);
            String eventID = "EV" + String.format("%03d", excelReader.eventList.size() + 1);
            
            System.out.println("Creating new event:");
            System.out.println("- ID: " + eventID);
            System.out.println("- Name: " + eventName);
            System.out.println("- Date: " + date);
            System.out.println("- Time: " + time);
            System.out.println("- Type: " + type);

            Event newEvent = new Event(eventID, eventName, description, location, date, time, capacity, cost, "", type);
            excelReader.eventList.add(newEvent);
            excelReader.writeEventsToExcel();
            
            System.out.println("Event added successfully. Total events: " + excelReader.eventList.size());
            
            clearFields();
            showAlert("Success", "Event added successfully", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for capacity", Alert.AlertType.ERROR);
        } catch (IOException e) {
            showAlert("Error", "Failed to save event: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleEditEvent() {
        if (selectedEvent != null) {
            showEventForm(true);
        } else {
            showAlert("No Selection", "Please select an event first.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleSaveEvent() {
        try {
            // Check admin permission
            if (!UserDatabase.CurrentUser.getRole().equals("ADMIN")) {
                showAlert("Access Denied", "You do not have permission to save events.", Alert.AlertType.ERROR);
                return;
            }

            String name = eventNameField.getText();
            String date = dateField.getText();
            String time = timeField.getText();
            String location = eventLocationField.getText();
            String description = eventDescriptionField.getText();
            String type = eventTypeField.getValue();
            int capacity = Integer.parseInt(eventCapacityField.getText());
            String cost = eventCostField.getText();

            // Validate required fields
            if (name.isEmpty() || date.isEmpty() || location.isEmpty() || type == null) {
                showAlert("Error", "Please fill in all required fields.", Alert.AlertType.ERROR);
                return;
            }

            if (selectedEvent == null) {
                // Add new event
                String eventID = "EVT" + System.currentTimeMillis();
                Event newEvent = new Event(
                    eventID,
                    name,
                    description,
                    location,
                    date,
                    time,
                    capacity,
                    cost,
                    "",  // Empty string for registered students
                    type
                );
                excelReader.eventList.add(newEvent);
                allEvents.add(newEvent);
                filteredEvents.add(newEvent);
            } else {
                // Update existing event
                selectedEvent.setEventName(name);
                selectedEvent.setDate(date);
                selectedEvent.setTime(time);
                selectedEvent.setLocation(location);
                selectedEvent.setDescription(description);
                selectedEvent.setType(type);
                selectedEvent.setCapacity(capacity);
                selectedEvent.setCost(cost);
            }

            excelReader.writeEventsToExcel();
            eventsTable.setItems(filteredEvents);
            clearFields();
            showAlert("Success", "Event saved successfully!", Alert.AlertType.INFORMATION);
            showListView();
        } catch (Exception e) {
            showAlert("Error", "Failed to save event: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteEvent() {
        // Check admin permission
        if (!UserDatabase.CurrentUser.getRole().equals("ADMIN")) {
            showAlert("Access Denied", "You do not have permission to delete events.", Alert.AlertType.ERROR);
            return;
        }

        if (selectedEvent != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Delete");
            confirmDialog.setHeaderText("Delete Event");
            confirmDialog.setContentText("Are you sure you want to delete this event? This action cannot be undone.");
            
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        excelReader.eventList.remove(selectedEvent);
                        excelReader.writeEventsToExcel();
                        allEvents.remove(selectedEvent);
                        filteredEvents.remove(selectedEvent);
                        eventsTable.setItems(filteredEvents);
                        showAlert("Success", "Event deleted successfully.", Alert.AlertType.INFORMATION);
                    } catch (IOException e) {
                        showAlert("Error", "Failed to delete event: " + e.getMessage(), Alert.AlertType.ERROR);
                    }
                }
            });
        } else {
            showAlert("No Selection", "Please select an event first.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleCancelForm() {
        showListView();
    }

    @FXML
    private void handleExportRegistrations() {
        if (selectedEvent != null) {
            try {
                File file = new File(selectedEvent.getEventName() + "_registrations.csv");
                FileWriter writer = new FileWriter(file);
                
                // Write header
                writer.write("Student ID,Name,Email\n");
                
                // Write student data
                for (Student student : registeredStudentsTable.getItems()) {
                    writer.write(String.format("%s,%s,%s\n",
                        student.getId(),
                        student.getUsername(),
                        student.getEmail()
                    ));
                }
                
                writer.close();
                showAlert("Success", "Registrations exported to " + file.getName(), Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("Error", "Failed to export registrations: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("No Selection", "Please select an event first.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleRemoveStudent() {
        // Check admin permission
        if (!UserDatabase.CurrentUser.getRole().equals("ADMIN")) {
            showAlert("Access Denied", "You do not have permission to manage registrations.", Alert.AlertType.ERROR);
            return;
        }

        Student selectedStudent = registeredStudentsTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null && selectedEvent != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirm Removal");
            confirmDialog.setHeaderText("Remove Student");
            confirmDialog.setContentText("Are you sure you want to remove " + selectedStudent.getUsername() + " from the event?");
            
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    String currentStudents = selectedEvent.getRegisteredStudents();
                    if (currentStudents == null || currentStudents.isEmpty()) {
                        return;
                    }
                    
                    System.out.println("Removing student: " + selectedStudent.getUsername());
                    System.out.println("Current registered students: " + currentStudents);
                    
                    // Split by comma and process each student
                    String[] students = currentStudents.split(",");
                    StringBuilder newStudents = new StringBuilder();
                    boolean found = false;
                    
                    for (String student : students) {
                        String trimmedStudent = student.trim();
                        // Skip the selected student's name
                        if (!trimmedStudent.equals(selectedStudent.getUsername())) {
                            // Add other students to the new list
                            if (newStudents.length() > 0) {
                                newStudents.append(", ");
                            }
                            newStudents.append(trimmedStudent);
                        } else {
                            found = true;
                        }
                    }
                    
                    if (!found) {
                        showAlert("Warning", "Student not found in registration list.", Alert.AlertType.WARNING);
                        return;
                    }
                    
                    System.out.println("Updated registered students: " + newStudents);
                    
                    // Update the event with the new list of students
                    selectedEvent.setRegisteredStudents(newStudents.toString());
                    
                    try {
                        // Track this removal in the UserDatabase to ensure it persists during the session
                        UserDatabase.trackRemovedRegistration(selectedEvent.getEventID(), selectedStudent.getUsername());
                        
                        // Ensure changes are immediately saved to Excel
                        excelReader.writeEventsToExcel();
                        System.out.println("Successfully saved student removal to Excel");
                        
                        // Update the UI
                        updateEventDetails(selectedEvent);
                        updateRegisteredStudentsTable(newStudents.toString());
                        
                        // Show success message with additional feedback
                        showAlert("Success", 
                                 "Student '" + selectedStudent.getUsername() + "' has been removed from the event.\n" +
                                 "This change will be reflected when the student logs in.", 
                                 Alert.AlertType.INFORMATION);
                    } catch (IOException e) {
                        showAlert("Error", "Failed to remove student: " + e.getMessage(), Alert.AlertType.ERROR);
                        e.printStackTrace();
                    }
                }
            });
        } else {
            showAlert("No Selection", "Please select a student first.", Alert.AlertType.WARNING);
        }
    }

    private String extractDate(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return "TBA";
        }
        String[] parts = dateTime.split(" ");
        return parts[0];
    }

    private String extractTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return "TBA";
        }
        String[] parts = dateTime.split(" ");
        return parts.length > 1 ? parts[1] : "TBA";
    }

    @FXML
    private void handlePrevMonth() {
        if (currentYearMonth == null) {
            currentYearMonth = YearMonth.now();
        }
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendar();
    }

    @FXML
    private void handleNextMonth() {
        if (currentYearMonth == null) {
            currentYearMonth = YearMonth.now();
        }
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendar();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateEventsTable() {
        eventsTable.setItems(FXCollections.observableArrayList(excelReader.eventList));
    }

    private void clearFields() {
        eventNameField.clear();
        dateField.clear();
        timeField.clear();
        eventLocationField.clear();
        eventDescriptionField.clear();
        eventTypeField.setValue(null);
        eventCapacityField.clear();
        eventCostField.clear();
    }
} 