package com.pfm.controller;

import com.pfm.dao.UserDAO;
import com.pfm.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Button signupBtn;
    @FXML private Label messageLabel;

    @FXML
    private void initialize() {
        messageLabel.setText("");
    }

    @FXML
    private void onLogin(ActionEvent e) {
        String u = usernameField.getText().trim();
        String p = passwordField.getText().trim();
        if (u.isEmpty() || p.isEmpty()) {
            messageLabel.setText("Enter credentials");
            return;
        }
        User user = UserDAO.authenticate(u, p);
        if (user != null) {
            openDashboard(user);
            ((Stage)loginBtn.getScene().getWindow()).close();
        } else {
            messageLabel.setText("Invalid username/password");
        }
    }

    @FXML
    private void onSignup(ActionEvent e) {
        String u = usernameField.getText().trim();
        String p = passwordField.getText().trim();
        if (u.isEmpty() || p.isEmpty()) {
            messageLabel.setText("Enter values");
            return;
        }
        boolean ok = UserDAO.createUser(u, p);
        messageLabel.setText(ok ? "Registered — login now" : "Username already exists");
    }

    private void openDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = loader.load();
            // pass user id to controller
            DashboardController ctrl = loader.getController();
            ctrl.initUser(user);
            Stage stage = new Stage();
            stage.setTitle("Dashboard - " + user.getUsername());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
