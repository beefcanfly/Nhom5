package com.trungtam.dao;

import com.trungtam.model.HocVien;
import com.trungtam.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HocVienDAO {

    // Lấy toàn bộ thông tin Học viên gộp với bảng cha NguoiDung để đổ lên lưới ProfileCard
    public List<Object[]> getHocVienFullInfo() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT hv.maHV, nd.hoTen, nd.email, nd.soDienThoai, hv.trangThai, nd.ngaySinh, nd.gioiTinh, nd.queQuan, hv.maNguoiDung " +
                     "FROM hocvien hv JOIN nguoidung nd ON hv.maNguoiDung = nd.maNguoiDung ORDER BY hv.maHV DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("maHV"),
                    rs.getString("hoTen"),
                    rs.getString("email"),
                    rs.getString("soDienThoai"),
                    rs.getString("trangThai"),
                    rs.getString("ngaySinh"),
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

    // Hàm chèn mới học viên vào lớp con
    public boolean insert(HocVien hv) {
        String sql = "INSERT INTO hocvien(maHV, maNguoiDung, trangThai) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, hv.getMaHV());
            ps.setString(2, hv.getMaNguoiDung());
            ps.setString(3, hv.getTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm cập nhật trạng thái học viên
    public boolean update(HocVien hv) {
        String sql = "UPDATE hocvien SET trangThai = ? WHERE maHV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, hv.getTrangThai());
            ps.setString(2, hv.getMaHV());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm xóa học viên ở lớp con
    public boolean delete(String maHV) {
        String sql = "DELETE FROM hocvien WHERE maHV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maHV);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm tìm kiếm Học viên theo mã
    public HocVien findById(String maHV) {
        String sql = "SELECT * FROM hocvien WHERE maHV = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
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

    // Hàm tìm kiếm nâng cao (Đã sửa lỗi đổi từ cột trangThaiHoc về trangThai chuẩn)
    public List<HocVien> search(String keyword) {
        List<HocVien> list = new ArrayList<>();
        String sql = "SELECT * FROM hocvien WHERE maHV LIKE ? OR trangThai LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
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
}