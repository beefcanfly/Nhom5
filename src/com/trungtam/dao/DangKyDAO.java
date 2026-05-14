package com.trungtam.dao;

import com.trungtam.utils.DBConnection;
import com.trungtam.model.DangKy;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DangKyDAO {

    // 1. Lấy danh sách Khóa học (Trả về String[] gồm {maKhoaHoc, tenKhoaHoc})
    public List<String[]> getListKhoaHoc() {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT maKhoaHoc, tenKhoaHoc FROM khoahoc WHERE trangThai = 'open'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{rs.getString("maKhoaHoc"), rs.getString("tenKhoaHoc")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Lấy danh sách Lớp học dựa trên mã Khóa học
    public List<String[]> getLopByKhoaHoc(String maKhoaHoc) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT maLop, tenLop, caHoc, lichHoc FROM lophoc WHERE maKhoaHoc = ? AND trangThai = 'open'";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhoaHoc);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("maLop"), 
                    rs.getString("tenLop"), 
                    rs.getString("caHoc"),
                    rs.getString("lichHoc")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Thực hiện đăng ký (Insert vào bảng dangky)
    public boolean insert(DangKy dk) {
        String sql = "INSERT INTO dangky (maHV, maLop, trangThai) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dk.getMaHV());
            ps.setString(2, dk.getMaLop());
            ps.setString(3, dk.getTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Kiểm tra xem học viên đã đăng ký lớp này chưa để tránh lỗi Duplicate
    public boolean checkTonTai(String maHV, String maLop) {
        String sql = "SELECT COUNT(*) FROM dangky WHERE maHV = ? AND maLop = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHV);
            ps.setString(2, maLop);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // 5. Xóa đăng ký không mong muốn
    public boolean delete(String maHV, String maLop) {
        String sql = "DELETE FROM dangky WHERE maHV = ? AND maLop = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHV);
            ps.setString(2, maLop);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}