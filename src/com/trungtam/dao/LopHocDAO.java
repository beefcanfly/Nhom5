package com.trungtam.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.trungtam.model.LopHoc;
import com.trungtam.utils.DBConnection;

public class LopHocDAO {

    // 1. Lấy danh sách tất cả lớp học
    public List<LopHoc> getAll() {
        List<LopHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM LopHoc";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LopHoc lh = new LopHoc();
                lh.setMaLop(rs.getString("maLop"));
                lh.setTenLop(rs.getString("tenLop"));
                lh.setHocPhi(rs.getDouble("hocPhi"));    
                lh.setThoiLuong(rs.getInt("thoiLuong")); 
                list.add(lh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm mới lớp học
    public boolean insert(LopHoc lh) {
        String sql = "INSERT INTO LopHoc (maLop, tenLop, hocPhi, thoiLuong) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, lh.getMaLop());
            ps.setString(2, lh.getTenLop());
            ps.setDouble(3, lh.getHocPhi());
            ps.setInt(4, lh.getThoiLuong());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Cập nhật thông tin lớp học
    public boolean update(LopHoc lh) {
        String sql = "UPDATE LopHoc SET tenLop = ?, hocPhi = ?, thoiLuong = ? WHERE maLop = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, lh.getTenLop());
            ps.setDouble(2, lh.getHocPhi());
            ps.setInt(3, lh.getThoiLuong());
            ps.setString(4, lh.getMaLop());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Xóa lớp học theo mã
    public boolean delete(String maLop) {
        String sql = "DELETE FROM LopHoc WHERE maLop = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maLop);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Tìm kiếm lớp học theo tên
    public List<LopHoc> findByName(String name) {
        List<LopHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM LopHoc WHERE tenLop LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LopHoc lh = new LopHoc();
                lh.setMaLop(rs.getString("maLop"));
                lh.setTenLop(rs.getString("tenLop"));
                lh.setHocPhi(rs.getDouble("hocPhi"));
                lh.setThoiLuong(rs.getInt("thoiLuong"));
                list.add(lh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Object[]> getLopHocFullInfo() {
        List<Object[]> list = new ArrayList<>();
        // SQL Join 3 bảng để lấy tên Khóa học và tên Giảng viên
        String sql = "SELECT l.maLop, l.tenLop, k.tenKhoaHoc, n.hoTen, l.caHoc, l.lichHoc, l.trangThai " +
                     "FROM lophoc l " +
                     "JOIN khoahoc k ON l.maKhoaHoc = k.maKhoaHoc " +
                     "JOIN giangvien g ON l.maGV = g.maGV " +
                     "JOIN nguoidung n ON g.maNguoiDung = n.maNguoiDung";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}