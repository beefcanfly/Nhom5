package com.trungtam.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.trungtam.model.LopHoc;
import com.trungtam.utils.DBConnection;

public class LopHocDAO {

    // 1. Lấy danh sách tất cả lớp học (Bỏ đọc cột hocPhi, thoiLuong từ bảng lophoc)
    public List<LopHoc> getAll() {
        List<LopHoc> list = new ArrayList<>();
        String sql = "SELECT * FROM LopHoc";
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

    // 2. Tìm kiếm lớp học theo Mã lớp 
    public LopHoc getById(String maLop) {
        String sql = "SELECT * FROM LopHoc WHERE maLop = ?";
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

    // 3. Mở lớp học mới (Chỉ INSERT 7 cột thực tế của bảng LopHoc)
    public boolean insert(LopHoc lh) {
        String sql = "INSERT INTO LopHoc (maLop, tenLop, maKhoaHoc, maGV, caHoc, lichHoc, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?)";
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

    // 4. Cập nhật thông tin lớp học (Chỉ UPDATE các cột thực tế)
    public boolean update(LopHoc lh) {
        String sql = "UPDATE LopHoc SET tenLop = ?, maKhoaHoc = ?, maGV = ?, caHoc = ?, lichHoc = ?, trangThai = ? WHERE maLop = ?";
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

    // 5. Xóa lớp học theo mã
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

    // 6. Tìm kiếm lớp học theo tên
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

    // 7. Lấy thông tin hiển thị lên bảng Admin (Hàm này giữ nguyên vì kết hợp từ câu lệnh JOIN)
    public List<Object[]> getLopHocFullInfo() {
        List<Object[]> list = new ArrayList<>();
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