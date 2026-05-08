package com.trungtam.dao;

import com.trungtam.model.TaiKhoan;
import com.trungtam.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {

    // 🔹 READ ALL
    public List<TaiKhoan> getAll() {
        List<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT * FROM taikhoan";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setUsername(rs.getString("username"));
                tk.setPassword(rs.getString("password"));
                tk.setVaiTro(rs.getString("vaiTro"));
                tk.setTrangThai(rs.getString("trangThai"));
                tk.setMaNguoiDung(rs.getString("maNguoiDung"));

                list.add(tk);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 🔹 GET BY USERNAME (rất quan trọng cho login)
    public TaiKhoan getByUsername(String username) {
        String sql = "SELECT * FROM taikhoan WHERE username=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setUsername(rs.getString("username"));
                tk.setPassword(rs.getString("password"));
                tk.setVaiTro(rs.getString("vaiTro"));
                tk.setTrangThai(rs.getString("trangThai"));
                tk.setMaNguoiDung(rs.getString("maNguoiDung"));
                return tk;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 🔹 INSERT
    public boolean insert(TaiKhoan tk) {
        String sql = "INSERT INTO taikhoan(username, password, vaiTro, trangThai, maNguoiDung) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getUsername());
            ps.setString(2, tk.getPassword());
            ps.setString(3, tk.getVaiTro());
            ps.setString(4, tk.getTrangThai());
            ps.setString(5, tk.getMaNguoiDung());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 UPDATE
    public boolean update(TaiKhoan tk) {
        String sql = "UPDATE taikhoan SET password=?, vaiTro=?, trangThai=?, maNguoiDung=? WHERE username=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tk.getPassword());
            ps.setString(2, tk.getVaiTro());
            ps.setString(3, tk.getTrangThai());
            ps.setString(4, tk.getMaNguoiDung());
            ps.setString(5, tk.getUsername());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 🔹 DELETE
    public boolean delete(String username) {
        String sql = "DELETE FROM taikhoan WHERE username=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public TaiKhoan login(String username, String password) {
        String sql = "SELECT * FROM taikhoan WHERE username=? AND password=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setUsername(rs.getString("username"));
                tk.setPassword(rs.getString("password"));
                tk.setVaiTro(rs.getString("vaiTro"));
                tk.setTrangThai(rs.getString("trangThai"));
                tk.setMaNguoiDung(rs.getString("maNguoiDung"));

                return tk;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}