package com.example.phase1_1420;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AdminDashboardViewController {

    @FXML private Button myButton;

    @FXML
    private void buttontest() {
      myButton.setText("CLICKED!");
    }
}