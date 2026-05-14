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
        String sql = "INSERT INTO KHOAHOC (MAKH, TENKH, HOCPHI, THOILUONG, TRANGTHAI) VALUES (?, ?, ?, ?, ?)";
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
        String sql = "SELECT * FROM KHOAHOC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                KhoaHoc kh = new KhoaHoc(
                        rs.getString("MAKH"),
                        rs.getString("TENKH"),
                        rs.getDouble("HOCPHI"),
                        rs.getInt("THOILUONG"),
                        rs.getString("TRANGTHAI")
                );
                khoaHocList.add(kh);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return khoaHocList;
    }
 
    public KhoaHoc getById(String maKhoaHoc) {
        String sql = "SELECT * FROM KHOAHOC WHERE MAKH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKhoaHoc);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new KhoaHoc(
                            rs.getString("MAKH"),
                            rs.getString("TENKH"),
                            rs.getDouble("HOCPHI"),
                            rs.getInt("THOILUONG"),
                            rs.getString("TRANGTHAI")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    public void update(KhoaHoc khoaHoc) {
        String sql = "UPDATE KHOAHOC SET TENKH = ?, HOCPHI = ?, THOILUONG = ?, TRANGTHAI = ? WHERE MAKH = ?";
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
        String sql = "DELETE FROM KHOAHOC WHERE MAKH = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKhoaHoc);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
}

