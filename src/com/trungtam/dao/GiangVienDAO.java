package com.trungtam.dao;

import com.trungtam.model.GiangVien;
import com.trungtam.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GiangVienDAO {

    // =========================
    // THÊM GIẢNG VIÊN
    // =========================
    public boolean insert(GiangVien gv) {

        String sql = "INSERT INTO GiangVien("
                + "maGV, maNguoiDung, hocVi, chuyenMon"
                + ") VALUES (?, ?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, gv.getMaGV());
            ps.setString(2, gv.getMaNguoiDung());
            ps.setString(3, gv.getHocVi());
            ps.setString(4, gv.getChuyenMon());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // CẬP NHẬT GIẢNG VIÊN
    // =========================
    public boolean update(GiangVien gv) {

        String sql = "UPDATE GiangVien SET "
                + "maNguoiDung=?, "
                + "hocVi=?, "
                + "chuyenMon=? "
                + "WHERE maGV=?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, gv.getMaNguoiDung());
            ps.setString(2, gv.getHocVi());
            ps.setString(3, gv.getChuyenMon());
            ps.setString(4, gv.getMaGV());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // XÓA GIẢNG VIÊN
    // =========================
    public boolean delete(String maGV) {

        String sql = "DELETE FROM GiangVien WHERE maGV=?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, maGV);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // TÌM GIẢNG VIÊN THEO MÃ
    // =========================
    public GiangVien findById(String maGV) {

        String sql = "SELECT * FROM GiangVien WHERE maGV=?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, maGV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    GiangVien gv = new GiangVien();
                    gv.setMaGV(rs.getString("maGV"));
                    gv.setMaNguoiDung(rs.getString("maNguoiDung"));
                    gv.setHocVi(rs.getString("hocVi"));
                    gv.setChuyenMon(rs.getString("chuyenMon"));
                    
                    return gv;
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
    public List<GiangVien> getAll() {

        List<GiangVien> list = new ArrayList<>();

        String sql = "SELECT * FROM GiangVien";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                GiangVien gv = new GiangVien();

                gv.setMaGV(rs.getString("maGV"));
                gv.setMaNguoiDung(rs.getString("maNguoiDung"));
                gv.setHocVi(rs.getString("hocVi"));
                gv.setChuyenMon(rs.getString("chuyenMon"));

                list.add(gv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // TÌM KIẾM GIẢNG VIÊN
    // =========================
    public List<GiangVien> search(String keyword) {

        List<GiangVien> list = new ArrayList<>();

        String sql = "SELECT * FROM GiangVien "
                + "WHERE maGV LIKE ? "
                + "OR maNguoiDung LIKE ? "
                + "OR hocVi LIKE ? "
                + "OR chuyenMon LIKE ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            String key = "%" + keyword + "%";

            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);
            ps.setString(4, key);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    GiangVien gv = new GiangVien();

                    gv.setMaGV(rs.getString("maGV"));
                    gv.setMaNguoiDung(rs.getString("maNguoiDung"));
                    gv.setHocVi(rs.getString("hocVi"));
                    gv.setChuyenMon(rs.getString("chuyenMon"));

                    list.add(gv);
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
    public boolean exists(String maGV) {

        String sql = "SELECT COUNT(*) FROM GiangVien WHERE maGV=?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setString(1, maGV);

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

    // ============================================
    // LẤY TOÀN BỘ THÔNG TIN CHI TIẾT (JOIN 2 BẢNG)
    // ============================================
    public List<Object[]> getGiangVienFullInfo() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT g.maGV, n.hoTen, n.email, n.soDienThoai, g.hocVi, g.chuyenMon, n.ngaySinh, n.gioiTinh, n.queQuan, n.maNguoiDung " +
                     "FROM GiangVien g JOIN NguoiDung n ON g.maNguoiDung = n.maNguoiDung";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("maGV"),          // 0
                    rs.getString("hoTen"),         // 1
                    rs.getString("email"),         // 2
                    rs.getString("soDienThoai"),   // 3
                    rs.getString("hocVi"),         // 4
                    rs.getString("chuyenMon"),     // 5
                    rs.getString("ngaySinh"),      // 6
                    rs.getString("gioiTinh"),      // 7
                    rs.getString("queQuan"),       // 8
                    rs.getString("maNguoiDung")    // 9
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ====================================================
    // LỌC DANH SÁCH GIẢNG VIÊN THEO CHUYÊN MÔN KHÓA HỌC
    // ====================================================
    public List<Object[]> getGiangVienByKhoaHoc(String maKhoaHoc) {
        List<Object[]> list = new ArrayList<>();
        // Truy vấn tìm những giảng viên có chuyên môn chứa chuỗi tên khóa học được chọn
        String sql = "SELECT g.maGV, n.hoTen " +
                     "FROM GiangVien g " +
                     "JOIN NguoiDung n ON g.maNguoiDung = n.maNguoiDung " +
                     "WHERE g.chuyenMon LIKE (SELECT CONCAT('%', tenKhoaHoc, '%') FROM khoahoc WHERE maKhoaHoc = ?)";
                     
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maKhoaHoc);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{ 
                        rs.getString("maGV"), 
                        rs.getString("hoTen") 
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}