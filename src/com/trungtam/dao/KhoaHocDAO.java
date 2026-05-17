package com.trungtam.dao;
 
import com.trungtam.model.KhoaHoc;
import com.trungtam.utils.DBConnection;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
public class KhoaHocDAO {
 
    public void create(KhoaHoc khoaHoc) {
        // Sửa tên cột thành maKhoaHoc, tenKhoaHoc, hocPhi, thoiLuong, trangThai
        String sql = "INSERT INTO khoahoc (maKhoaHoc, tenKhoaHoc, hocPhi, thoiLuong, trangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, khoaHoc.getMaKhoaHoc());
            pstmt.setString(2, khoaHoc.getTenKhoaHoc());
            pstmt.setDouble(3, khoaHoc.getHocPhi());
            pstmt.setInt(4, khoaHoc.getThoiLuong());
            pstmt.setString(5, khoaHoc.getTrangThai());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    public List<KhoaHoc> getAll() {
        List<KhoaHoc> khoaHocList = new ArrayList<>();
        String sql = "SELECT * FROM khoahoc";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                // Sửa chính xác tên các cột lấy từ ResultSet ở đây
                KhoaHoc kh = new KhoaHoc(
                        rs.getString("maKhoaHoc"),
                        rs.getString("tenKhoaHoc"),
                        rs.getDouble("hocPhi"),
                        rs.getInt("thoiLuong"),
                        rs.getString("trangThai")
                );
                khoaHocList.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return khoaHocList;
    }
 
    public KhoaHoc getById(String maKhoaHoc) {
        String sql = "SELECT * FROM khoahoc WHERE maKhoaHoc = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKhoaHoc);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new KhoaHoc(
                            rs.getString("maKhoaHoc"),
                            rs.getString("tenKhoaHoc"),
                            rs.getDouble("hocPhi"),
                            rs.getInt("thoiLuong"),
                            rs.getString("trangThai")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    public void update(KhoaHoc khoaHoc) {
        String sql = "UPDATE khoahoc SET tenKhoaHoc = ?, hocPhi = ?, thoiLuong = ?, trangThai = ? WHERE maKhoaHoc = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, khoaHoc.getTenKhoaHoc());
            pstmt.setDouble(2, khoaHoc.getHocPhi());
            pstmt.setInt(3, khoaHoc.getThoiLuong());
            pstmt.setString(4, khoaHoc.getTrangThai());
            pstmt.setString(5, khoaHoc.getMaKhoaHoc());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    public void delete(String maKhoaHoc) {
        String sql = "DELETE FROM khoahoc WHERE maKhoaHoc = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKhoaHoc);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Bạn có thể giữ lại hoặc xóa hàm getList() cũ này đi vì hàm getAll() ở trên đã bao phủ hoàn toàn cấu trúc mới rồi.
    public List<KhoaHoc> getList() {
        return getAll(); 
    }
}