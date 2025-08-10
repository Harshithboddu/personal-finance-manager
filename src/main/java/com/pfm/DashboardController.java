package com.pfm.controller;

import com.pfm.dao.TransactionDAO;
import com.pfm.model.Transaction;
import com.pfm.model.User;
import com.pfm.util.CSVExporter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class DashboardController {
    private User user;

    @FXML private Label welcomeLabel;
    @FXML private Label balanceLabel;
    @FXML private TableView<Transaction> table;
    @FXML private TableColumn<Transaction, String> dateCol;
    @FXML private TableColumn<Transaction, String> descCol;
    @FXML private TableColumn<Transaction, String> catCol;
    @FXML private TableColumn<Transaction, Double> amtCol;
    @FXML private TableColumn<Transaction, String> typeCol;
    @FXML private TextField descField;
    @FXML private TextField categoryField;
    @FXML private TextField amountField;
    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox<String> typeChoice;
    @FXML private PieChart pieChart;

    public void initUser(User user) {
        this.user = user;
        welcomeLabel.setText("Welcome, " + user.getUsername());
        typeChoice.getItems().addAll("INCOME", "EXPENSE");
        typeChoice.setValue("EXPENSE");
        setupTable();
        refresh();
    }

    private void setupTable() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        catCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        amtCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    @FXML
    private void addTransaction() {
        try {
            String date = datePicker.getValue().toString();
            String desc = descField.getText();
            String cat = categoryField.getText();
            double amt = Double.parseDouble(amountField.getText());
            String type = typeChoice.getValue();
            Transaction t = new Transaction(0, user.getId(), date, desc, cat, amt, type);
            boolean ok = TransactionDAO.addTransaction(t);
            if (ok) {
                clearInputs();
                refresh();
            } else {
                showAlert("Error", "Could not add transaction");
            }
        } catch (Exception e) {
            showAlert("Input error", "Please check your inputs");
        }
    }

    @FXML
    private void deleteSelected() {
        Transaction sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        boolean ok = TransactionDAO.deleteTransaction(sel.getId());
        if (ok) refresh();
    }

    @FXML
    private void exportCSV() {
        List<Transaction> list = TransactionDAO.getTransactionsForUser(user.getId());
        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName("transactions.csv");
        File file = chooser.showSaveDialog(table.getScene().getWindow());
        if (file != null) {
            boolean ok = CSVExporter.exportToCSV(list, file);
            showAlert(ok ? "Success" : "Error", ok ? "Exported CSV" : "Failed to export");
        }
    }

    private void refresh() {
        List<Transaction> list = TransactionDAO.getTransactionsForUser(user.getId());
        ObservableList<Transaction> obs = FXCollections.observableArrayList(list);
        table.setItems(obs);
        double bal = TransactionDAO.getBalanceForUser(user.getId());
        balanceLabel.setText(String.format("Balance: %.2f", bal));
        updateChart(list);
    }

    private void updateChart(List<Transaction> list) {
        // simple breakdown by category (expenses only)
        javafx.collections.ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        java.util.Map<String, Double> catSum = new java.util.HashMap<>();
        for (Transaction t : list) {
            if ("EXPENSE".equals(t.getType())) {
                catSum.put(t.getCategory(), catSum.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
            }
        }
        for (var e : catSum.entrySet()) {
            data.add(new PieChart.Data(e.getKey(), e.getValue()));
        }
        pieChart.setData(data);
    }

    private void clearInputs() {
        descField.clear();
        categoryField.clear();
        amountField.clear();
        datePicker.setValue(null);
    }

    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
