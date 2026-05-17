package com.trungtam.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.trungtam.model.LopHoc;
import com.trungtam.utils.DBConnection;

public class LopHocDAO {

    // 1. Lấy danh sách tất cả lớp học cơ bản (Đã dọn sạch hocPhi và thoiLuong)
    public List<LopHoc> getAll() {
        List<LopHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM lophoc";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                LopHoc lh = new LopHoc();
                lh.setMaLop(rs.getString("maLop"));
                lh.setTenLop(rs.getString("tenLop"));
                lh.setMaKhoaHoc(rs.getString("maKhoaHoc"));
                lh.setMaGV(rs.getString("maGV"));
                lh.setCaHoc(rs.getString("caHoc"));
                lh.setLichHoc(rs.getString("lichHoc"));
                lh.setTrangThai(rs.getString("trangThai"));
                list.add(lh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm mới lớp học chuẩn chỉ
    public boolean insert(LopHoc lh) {
        String sql = "INSERT INTO lophoc (maLop, tenLop, maKhoaHoc, maGV, caHoc, lichHoc, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, lh.getMaLop());
            ps.setString(2, lh.getTenLop());
            ps.setString(3, lh.getMaKhoaHoc());
            ps.setString(4, lh.getMaGV());
            ps.setString(5, lh.getCaHoc());
            ps.setString(6, lh.getLichHoc());
            ps.setString(7, lh.getTrangThai());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Cập nhật thông tin lớp học
    public boolean update(LopHoc lh) {
        String sql = "UPDATE lophoc SET tenLop = ?, maKhoaHoc = ?, maGV = ?, caHoc = ?, lichHoc = ?, trangThai = ? WHERE maLop = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, lh.getTenLop());
            ps.setString(2, lh.getMaKhoaHoc());
            ps.setString(3, lh.getMaGV());
            ps.setString(4, lh.getCaHoc());
            ps.setString(5, lh.getLichHoc());
            ps.setString(6, lh.getTrangThai());
            ps.setString(7, lh.getMaLop());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Xóa lớp học theo mã
    public boolean delete(String maLop) {
        String sql = "DELETE FROM lophoc WHERE maLop = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maLop);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Tìm kiếm lớp học theo Mã lớp phục vụ sửa dữ liệu hoặc bẫy trùng mã
    public LopHoc getById(String maLop) {
        String sql = "SELECT * FROM lophoc WHERE maLop = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maLop);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LopHoc lh = new LopHoc();
                    lh.setMaLop(rs.getString("maLop"));
                    lh.setTenLop(rs.getString("tenLop"));
                    lh.setMaKhoaHoc(rs.getString("maKhoaHoc"));
                    lh.setMaGV(rs.getString("maGV"));
                    lh.setCaHoc(rs.getString("caHoc"));
                    lh.setLichHoc(rs.getString("lichHoc"));
                    lh.setTrangThai(rs.getString("trangThai"));
                    return lh;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 6. Lấy dữ liệu đầy đủ kết nối 3 bảng để render chuẩn lên JTable
    public List<Object[]> getLopHocFullInfo() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT l.maLop, l.tenLop, k.tenKhoaHoc, n.hoTen, l.caHoc, l.lichHoc, l.trangThai " +
                     "FROM lophoc l " +
                     "INNER JOIN khoahoc k ON l.maKhoaHoc = k.maKhoaHoc " +
                     "INNER JOIN giangvien g ON l.maGV = g.maGV " +
                     "INNER JOIN nguoidung n ON g.maNguoiDung = n.maNguoiDung ORDER BY l.maLop DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString(1), 
                    rs.getString(2), 
                    rs.getString(3),
                    rs.getString(4), 
                    rs.getString(5), 
                    rs.getString(6), 
                    rs.getString(7)
                });
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return list;
    }
}