package com.trungtam.dao;

import com.trungtam.model.GiangVien;
import com.trungtam.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GiangVienDAO {

    // Lấy toàn bộ thông tin Giảng viên gộp với bảng cha NguoiDung để đổ lên lưới ProfileCard
    // ĐÃ SỬA LỖI: Cú pháp kết hợp bảng (JOIN) chuẩn xác để không mất dữ liệu
    public List<Object[]> getGiangVienFullInfo() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT gv.maGV, nd.hoTen, nd.email, nd.soDienThoai, nd.ngaySinh, gv.chuyenMon, gv.hocVi, nd.gioiTinh, nd.queQuan, gv.maNguoiDung " +
                     "FROM giangvien gv INNER JOIN nguoidung nd ON gv.maNguoiDung = nd.maNguoiDung ORDER BY gv.maGV DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("maGV"),
                    rs.getString("hoTen"),
                    rs.getString("email"),
                    rs.getString("soDienThoai"),
                    rs.getString("ngaySinh"),
                    rs.getString("chuyenMon"),
                    rs.getString("hocVi"),
                    rs.getString("gioiTinh"),
                    rs.getString("queQuan"),
                    rs.getString("maNguoiDung")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm chèn mới giảng viên vào lớp con (maNguoiDung phải tồn tại ở bảng cha trước)
    public boolean insert(GiangVien gv) {
        String sql = "INSERT INTO giangvien(maGV, maNguoiDung, hocVi, chuyenMon) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
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

    // Hàm cập nhật thông tin chuyên môn của giảng viên
    public boolean update(GiangVien gv) {
        String sql = "UPDATE giangvien SET hocVi = ?, chuyenMon = ? WHERE maGV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, gv.getHocVi());
            ps.setString(2, gv.getChuyenMon());
            ps.setString(3, gv.getMaGV());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm xóa giảng viên ở lớp con
    public boolean delete(String maGV) {
        String sql = "DELETE FROM giangvien WHERE maGV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maGV);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm tìm kiếm Giảng viên theo mã
    public GiangVien findById(String maGV) {
        String sql = "SELECT * FROM giangvien WHERE maGV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
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

    // HÀM BỔ SUNG: LẤY DANH SÁCH GIẢNG VIÊN THEO MÃ KHÓA HỌC 
    public List<Object[]> getGiangVienByKhoaHoc(String maKH) {
        List<Object[]> list = new ArrayList<>();
        // Truy vấn lấy giảng viên có chuyên môn chứa tên hoặc mã khóa học tương ứng
        String sql = "SELECT gv.maGV, nd.hoTen FROM giangvien gv " +
                     "INNER JOIN nguoidung nd ON gv.maNguoiDung = nd.maNguoiDung " +
                     "WHERE gv.chuyenMon LIKE (SELECT CONCAT('%', tenKhoaHoc, '%') FROM khoahoc WHERE maKhoaHoc = ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maKH);
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