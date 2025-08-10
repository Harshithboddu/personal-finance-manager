package com.pfm.util;

import com.pfm.model.Transaction;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class CSVExporter {
    public static boolean exportToCSV(List<Transaction> list, File file) {
        try (FileWriter fw = new FileWriter(file)) {
            fw.append("id,user_id,date,description,category,amount,type\n");
            for (Transaction t : list) {
                fw.append(String.valueOf(t.getId())).append(",");
                fw.append(String.valueOf(t.getUserId())).append(",");
                fw.append(t.getDate()).append(",");
                fw.append("\"").append(t.getDescription() == null ? "" : t.getDescription()).append("\"").append(",");
                fw.append("\"").append(t.getCategory() == null ? "" : t.getCategory()).append("\"").append(",");
                fw.append(String.valueOf(t.getAmount())).append(",");
                fw.append(t.getType()).append("\n");
            }
            fw.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
