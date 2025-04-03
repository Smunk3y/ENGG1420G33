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

public class EventManagementController implements Initializable {
    // Common fields
    private final ExcelFile excelReader = new ExcelFile();
    private Event selectedEvent;
    private final ObservableList<Event> allEvents = FXCollections.observableArrayList();
    private final ObservableList<Event> filteredEvents = FXCollections.observableArrayList();
    private YearMonth currentYearMonth;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    
    // View containers
    @FXML private VBox listView;
    @FXML private VBox calendarView;
    @FXML private VBox eventDetailsView;
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
    @FXML private Label registrationStatusText;
    @FXML private Button registerButton;
    @FXML private Button cancelRegistrationButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            System.out.println("\n=== Initializing EventManagementController ===");
            
            // Load data from Excel
            excelReader.ReadingNameExcelFile();
            allEvents.setAll(excelReader.eventList);
            filteredEvents.setAll(allEvents);
            
            // Initialize calendar
            currentYearMonth = YearMonth.now();
            updateCalendar();
            
            // Initialize filter options
            initializeFilters();
            
            // Initialize table columns
            initializeColumns();
            
            // Set up event handlers
            setupEventHandlers();
            
            // Show list view by default
            showListView();
        } catch (Exception e) {
            System.err.println("Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateCalendar() {
        // Clear previous calendar cells
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
        
        // Get the first day of the month
        LocalDate firstDay = currentYearMonth.atDay(1);
        int firstDayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // Convert to 0-6 (Sun-Sat)
        
        // Fill in the calendar grid
        int row = 1;
        int col = firstDayOfWeek;
        
        for (int day = 1; day <= currentYearMonth.lengthOfMonth(); day++) {
            LocalDate date = currentYearMonth.atDay(day);
            VBox dateCell = createDateCell(date);
            calendarGrid.add(dateCell, col, row);
            
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

    @FXML
    private void handlePrevMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendar();
    }

    @FXML
    private void handleNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendar();
    }

    private void initializeFilters() {
        // Initialize event type filter
        List<String> eventTypes = new ArrayList<>();
        for (Event event : allEvents) {
            if (!eventTypes.contains(event.getType())) {
                eventTypes.add(event.getType());
            }
        }
        eventTypeFilter.setItems(FXCollections.observableArrayList(eventTypes));
        
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
        eventNameColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getEventName()));
        eventDateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(extractDate(cellData.getValue().getDateTime())));
        eventTimeColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(extractTime(cellData.getValue().getDateTime())));
        eventLocationColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getLocation()));
        eventTypeColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getType()));
        eventCapacityColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacity())));
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

        // Registration button handlers
        registerButton.setOnAction(e -> registerForEvent());
        cancelRegistrationButton.setOnAction(e -> cancelRegistration());
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
        updateListTable();
    }

    private void showCalendarView() {
        listView.setVisible(false);
        listView.setManaged(false);
        calendarView.setVisible(true);
        calendarView.setManaged(true);
        eventDetailsView.setVisible(false);
        eventDetailsView.setManaged(false);
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
            updateEventDetails(selectedEvent);
        }
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
            
            // Update registration status
            String currentUserId = UserDatabase.CurrentUser.getId();
            String registeredStudents = event.getRegisteredStudents();
            boolean isRegistered = registeredStudents != null && registeredStudents.contains(currentUserId);
            
            registrationStatusText.setText(isRegistered ? "You are registered for this event" : "You are not registered for this event");
            registerButton.setDisable(isRegistered);
            cancelRegistrationButton.setDisable(!isRegistered);
        }
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

    // Registration methods
    @FXML
    private void registerForEvent() {
        if (selectedEvent != null) {
            String currentStudents = selectedEvent.getRegisteredStudents();
            String newStudents = currentStudents.isEmpty() ? 
                UserDatabase.CurrentUser.getId() : 
                currentStudents + "," + UserDatabase.CurrentUser.getId();
            
            selectedEvent.setRegisteredStudents(newStudents);
            
            try {
                excelReader.writeEventsToExcel();
            } catch (IOException e) {
                showAlert("Error", "Failed to save registration: " + e.getMessage(), 
                    Alert.AlertType.ERROR);
                return;
            }
            
            updateEventDetails(selectedEvent);
            showAlert("Success", "Successfully registered for the event!", 
                Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void cancelRegistration() {
        if (selectedEvent != null) {
            String currentStudents = selectedEvent.getRegisteredStudents();
            String[] students = currentStudents.split(",");
            StringBuilder newStudents = new StringBuilder();
            
            for (String student : students) {
                if (!student.equals(UserDatabase.CurrentUser.getId())) {
                    if (newStudents.length() > 0) {
                        newStudents.append(",");
                    }
                    newStudents.append(student);
                }
            }
            
            selectedEvent.setRegisteredStudents(newStudents.toString());
            
            try {
                excelReader.writeEventsToExcel();
            } catch (IOException e) {
                showAlert("Error", "Failed to cancel registration: " + e.getMessage(), 
                    Alert.AlertType.ERROR);
                return;
            }
            
            updateEventDetails(selectedEvent);
            showAlert("Success", "Successfully cancelled registration!", 
                Alert.AlertType.INFORMATION);
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

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 