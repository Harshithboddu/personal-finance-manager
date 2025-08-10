package com.pfm.util;

import java.sql.*;

public class DBUtil {
    private static final String DB_URL = "jdbc:sqlite:pfm.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initDB() {
        // create users and transactions tables if not exists
        String users = "CREATE TABLE IF NOT EXISTS users (" +
                       "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                       "username TEXT UNIQUE NOT NULL," +
                       "password TEXT NOT NULL" +
                       ");";
        String transactions = "CREATE TABLE IF NOT EXISTS transactions (" +
                              "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                              "user_id INTEGER NOT NULL," +
                              "date TEXT NOT NULL," + // ISO date "YYYY-MM-DD"
                              "description TEXT," +
                              "category TEXT," +
                              "amount REAL NOT NULL," +
                              "type TEXT NOT NULL," + // INCOME or EXPENSE
                              "FOREIGN KEY(user_id) REFERENCES users(id)" +
                              ");";
        try (Connection c = getConnection();
             Statement s = c.createStatement()) {
            s.execute(users);
            s.execute(transactions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
