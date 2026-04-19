package com.trungtam.dao;

import java.sql.*;
import java.util.*;
import com.trungtam.model.HocVien;
import com.trungtam.utils.DBConnection;

public class HocVienDAO {

    public List<HocVien> getAll() {
        List<HocVien> list = new ArrayList<>();

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM hocvien";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                HocVien hv = new HocVien();
                hv.setMaHocVien(rs.getString("maHocVien"));
                hv.setHoTen(rs.getString("hoTen"));

                list.add(hv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}