package com.trungtam.dao;

import com.trungtam.model.KhoaHoc;
import com.trungtam.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class KhoaHocDAO {

    // Hàm lấy danh sách khóa học gốc 
    public List<KhoaHoc> getList() {
        List<KhoaHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM khoahoc";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                KhoaHoc kh = new KhoaHoc();
                kh.setMaKhoaHoc(rs.getString("maKhoaHoc")); 
                kh.setTenKhoaHoc(rs.getString("tenKhoaHoc"));
                kh.setHocPhi(rs.getDouble("hocPhi"));
                kh.setThoiLuong(rs.getInt("thoiLuong"));
                kh.setTrangThai(rs.getString("trangThai"));
                list.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm alias dự phòng cho AdminUI gọi không bị đỏ code
    public List<KhoaHoc> getAll() {
        return this.getList();
    }

    // Hàm tìm kiếm khóa học theo mã để phục vụ liên kết lớp học
    public KhoaHoc getById(String maKH) {
        String sql = "SELECT * FROM khoahoc WHERE maKhoaHoc = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KhoaHoc kh = new KhoaHoc();
                    kh.setMaKhoaHoc(rs.getString("maKhoaHoc"));
                    kh.setTenKhoaHoc(rs.getString("tenKhoaHoc"));
                    kh.setHocPhi(rs.getDouble("hocPhi"));
                    kh.setThoiLuong(rs.getInt("thoiLuong"));
                    kh.setTrangThai(rs.getString("trangThai"));
                    return kh;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Hàm thêm khóa học mới
    public boolean create(KhoaHoc kh) {
        String sql = "INSERT INTO khoahoc (maKhoaHoc, tenKhoaHoc, hocPhi, thoiLuong, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, kh.getMaKhoaHoc());
            ps.setString(2, kh.getTenKhoaHoc());
            ps.setDouble(3, kh.getHocPhi());
            ps.setInt(4, kh.getThoiLuong());
            ps.setString(5, kh.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm cập nhật khóa học
    public boolean update(KhoaHoc kh) {
        String sql = "UPDATE khoahoc SET tenKhoaHoc = ?, hocPhi = ?, thoiLuong = ?, trangThai = ? WHERE maKhoaHoc = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, kh.getTenKhoaHoc());
            ps.setDouble(2, kh.getHocPhi());
            ps.setInt(3, kh.getThoiLuong());
            ps.setString(4, kh.getTrangThai());
            ps.setString(5, kh.getMaKhoaHoc());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm xóa khóa học
    public boolean delete(String maKH) {
        String sql = "DELETE FROM khoahoc WHERE maKhoaHoc = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
