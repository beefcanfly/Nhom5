package com.trungtam.dao;

import com.trungtam.model.HocVien;
import com.trungtam.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HocVienDAO {

    // =========================
    // THÊM HỌC VIÊN
    // =========================
    public boolean insert(HocVien hv) {
        String sql = "INSERT INTO HocVien(maHV, maNguoiDung, trangThai) VALUES (?, ?, ?)";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, hv.getMaHV());
            ps.setString(2, hv.getMaNguoiDung());
            ps.setString(3, hv.getTrangThai());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================
    // CẬP NHẬT HỌC VIÊN
    // =========================
    public boolean update(HocVien hv) {
        String sql = "UPDATE HocVien SET maNguoiDung=?, trangThai=? WHERE maHV=?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, hv.getMaNguoiDung());
            ps.setString(2, hv.getTrangThai());
            ps.setString(3, hv.getMaHV());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================
    // XÓA HỌC VIÊN
    // =========================
    public boolean delete(String maHV) {
        String sql = "DELETE FROM HocVien WHERE maHV=?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maHV);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================
    // TÌM HỌC VIÊN THEO MÃ
    // =========================
    public HocVien findById(String maHV) {
        String sql = "SELECT * FROM HocVien WHERE maHV=?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maHV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HocVien hv = new HocVien();
                    hv.setMaHV(rs.getString("maHV"));
                    hv.setMaNguoiDung(rs.getString("maNguoiDung"));
                    hv.setTrangThai(rs.getString("trangThai"));
                    return hv;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // =========================
    // LẤY TOÀN BỘ DANH SÁCH
    // =========================
    public List<HocVien> getAll() {
        List<HocVien> list = new ArrayList<>();
        String sql = "SELECT * FROM HocVien";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                HocVien hv = new HocVien();
                hv.setMaHV(rs.getString("maHV"));
                hv.setMaNguoiDung(rs.getString("maNguoiDung"));
                hv.setTrangThai(rs.getString("trangThai"));
                list.add(hv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // ====================================================
    // TÌM KIẾM HỌC VIÊN (Đã sửa đổi tên biến cột trangThai)
    // ====================================================
    public List<HocVien> search(String keyword) {
        List<HocVien> list = new ArrayList<>();
        String sql = "SELECT * FROM HocVien WHERE maHV LIKE ? OR maNguoiDung LIKE ? OR trangThai LIKE ?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HocVien hv = new HocVien();
                    hv.setMaHV(rs.getString("maHV"));
                    hv.setMaNguoiDung(rs.getString("maNguoiDung"));
                    hv.setTrangThai(rs.getString("trangThai"));
                    list.add(hv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // =========================
    // LỌC THEO TRẠNG THÁI HỌC
    // =========================
    public List<HocVien> findByTrangThai(String trangThai) {
        List<HocVien> list = new ArrayList<>();
        String sql = "SELECT * FROM HocVien WHERE trangThai=?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, trangThai);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HocVien hv = new HocVien();
                    hv.setMaHV(rs.getString("maHV"));
                    hv.setMaNguoiDung(rs.getString("maNguoiDung"));
                    hv.setTrangThai(rs.getString("trangThai"));
                    list.add(hv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // =========================
    // KIỂM TRA TỒN TẠI
    // =========================
    public boolean exists(String maHV) {
        String sql = "SELECT COUNT(*) FROM HocVien WHERE maHV=?";
        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, maHV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================
    // LẤY TOÀN BỘ THÔNG TIN CHI TIẾT (JOIN 2 BẢNG)
    // ==========================================
    public List<Object[]> getHocVienFullInfo() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT h.maHV, n.hoTen, n.email, n.soDienThoai, h.trangThai, " +
                     "n.ngaySinh, n.gioiTinh, n.queQuan, n.maNguoiDung " +
                     "FROM HocVien h JOIN nguoidung n ON h.maNguoiDung = n.maNguoiDung";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("maHV"),         // 0
                    rs.getString("hoTen"),        // 1
                    rs.getString("email"),        // 2
                    rs.getString("soDienThoai"),  // 3
                    rs.getString("trangThai"),    // 4
                    rs.getString("ngaySinh"),     // 5
                    rs.getString("gioiTinh"),     // 6
                    rs.getString("queQuan"),      // 7
                    rs.getString("maNguoiDung")   // 8
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}