package com.pfm.model;

public class Transaction {
    private int id;
    private int userId;
    private String date; // "YYYY-MM-DD"
    private String description;
    private String category;
    private double amount;
    private String type; // INCOME or EXPENSE

    public Transaction() {}

    // full constructor
    public Transaction(int id, int userId, String date, String description,
                       String category, double amount, String type) {
        this.id = id; this.userId = userId; this.date = date;
        this.description = description; this.category = category;
        this.amount = amount; this.type = type;
    }

    // getters & setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
