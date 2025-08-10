package com.pfm.dao;

import com.pfm.model.Transaction;
import com.pfm.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public static boolean addTransaction(Transaction t) {
        String sql = "INSERT INTO transactions(user_id, date, description, category, amount, type) VALUES(?,?,?,?,?,?)";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, t.getUserId());
            ps.setString(2, t.getDate());
            ps.setString(3, t.getDescription());
            ps.setString(4, t.getCategory());
            ps.setDouble(5, t.getAmount());
            ps.setString(6, t.getType());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Transaction> getTransactionsForUser(int userId) {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY date DESC";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Transaction t = new Transaction(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("date"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("type")
                    );
                    list.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static double getBalanceForUser(int userId) {
        String sql = "SELECT SUM(CASE WHEN type='INCOME' THEN amount ELSE -amount END) as bal FROM transactions WHERE user_id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("bal");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static boolean deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // additional methods: updateTransaction, filter by month, category, etc.
}
