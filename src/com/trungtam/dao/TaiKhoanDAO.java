package com.trungtam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.trungtam.model.TaiKhoan;
import com.trungtam.utils.DBConnection;

public class TaiKhoanDAO {

    public TaiKhoan login(String username, String password) {
        TaiKhoan tk = null;

        String sql = "SELECT * FROM taikhoan WHERE username=? AND password=? AND trangThai='active'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tk = new TaiKhoan();
                tk.setUsername(rs.getString("username"));
                tk.setPassword(rs.getString("password"));
                tk.setVaiTro(rs.getString("vaiTro"));
                tk.setTrangThai(rs.getString("trangThai"));
                tk.setMaNguoiDung(rs.getString("maNguoiDung"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tk;
    }
}