package com.trungtam.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/manager";
            String user = "root";
            String password = "root"; // 

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Kết nối thành công!");
            return conn;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Kết nối thất bại!");
            return null;
        }
    }
}
