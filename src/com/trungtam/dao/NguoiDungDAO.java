package com.trungtam.dao;

import com.trungtam.model.NguoiDung;
import com.trungtam.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NguoiDungDAO {

    // ==========================================
    // TÌM THEO ID (Sửa lỗi dùng Try-with-resources thiếu)
    // ==========================================
    public NguoiDung findById(String maNguoiDung) {
        String sql = "SELECT * FROM nguoidung WHERE maNguoiDung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maNguoiDung);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NguoiDung nd = new NguoiDung();
                    nd.setMaNguoiDung(rs.getString("maNguoiDung"));
                    nd.setHoTen(rs.getString("hoTen"));
                    nd.setEmail(rs.getString("email"));
                    nd.setNgaySinh(rs.getString("ngaySinh"));
                    nd.setGioiTinh(rs.getString("gioiTinh"));
                    nd.setSoDienThoai(rs.getString("soDienThoai"));
                    nd.setQueQuan(rs.getString("queQuan"));
                    return nd;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // ==========================================
    // ĐỌC TOÀN BỘ DANH SÁCH
    // ==========================================
    public List<NguoiDung> getAll() {
        List<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT * FROM nguoidung";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NguoiDung nd = new NguoiDung();
                nd.setMaNguoiDung(rs.getString("maNguoiDung"));
                nd.setHoTen(rs.getString("hoTen"));
                nd.setEmail(rs.getString("email"));
                nd.setNgaySinh(rs.getString("ngaySinh"));
                nd.setGioiTinh(rs.getString("gioiTinh"));
                nd.setSoDienThoai(rs.getString("soDienThoai"));
                nd.setQueQuan(rs.getString("queQuan"));
                list.add(nd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ==========================================
    // THÊM MỚI NGƯỜI DÙNG (ĐÃ SỬA: Đủ 7 cột, Đủ 7 dấu ?)
    // ==========================================
    public boolean insert(NguoiDung nd) {
        String sql = "INSERT INTO nguoidung(maNguoiDung, hoTen, email, ngaySinh, gioiTinh, soDienThoai, queQuan) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nd.getMaNguoiDung());
            ps.setString(2, nd.getHoTen());
            ps.setString(3, nd.getEmail());
            ps.setString(4, nd.getNgaySinh());
            ps.setString(5, nd.getGioiTinh());
            ps.setString(6, nd.getSoDienThoai());
            ps.setString(7, nd.getQueQuan());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================
    // CẬP NHẬT NGƯỜI DÙNG (ĐÃ SỬA: Chuẩn hóa thứ tự dấu ?)
    // ==========================================
    public boolean update(NguoiDung nd) {
        String sql = "UPDATE nguoidung SET hoTen=?, email=?, ngaySinh=?, gioiTinh=?, soDienThoai=?, queQuan=? WHERE maNguoiDung=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nd.getHoTen());
            ps.setString(2, nd.getEmail());
            ps.setString(3, nd.getNgaySinh());
            ps.setString(4, nd.getGioiTinh());
            ps.setString(5, nd.getSoDienThoai());
            ps.setString(6, nd.getQueQuan());
            ps.setString(7, nd.getMaNguoiDung()); // maNguoiDung nằm ở WHERE nên phải là tham số số 7

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================
    // XÓA NGƯỜI DÙNG
    // ==========================================
    public boolean delete(String maNguoiDung) {
        String sql = "DELETE FROM nguoidung WHERE maNguoiDung=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNguoiDung);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}