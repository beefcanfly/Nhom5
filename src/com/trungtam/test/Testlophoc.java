package com.trungtam.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Testlophoc {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/quanly";
        String user = "root";
        String password = "root"; 

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();

            String sql = "SELECT maLop, tenLop, caHoc, lichHoc FROM LopHoc WHERE lichHoc = '2-4-6'";

            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("=== DANH SÁCH LỚP SÁNG NGÀY CHẴN ===");

            while (rs.next()) {
                String maLop = rs.getString("maLop");
                String tenLop = rs.getString("tenLop");
                String caHoc = rs.getString("caHoc");
                String lichHoc = rs.getString("lichHoc");

                System.out.println(
                        maLop + " | " +
                        tenLop + " | " +
                        caHoc + " | " +
                        lichHoc
                );
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}