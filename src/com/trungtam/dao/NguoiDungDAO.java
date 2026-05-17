package com.trungtam.dao;

import com.trungtam.model.NguoiDung;
import com.trungtam.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NguoiDungDAO {

    // Hàm chèn mới tài khoản gốc (Đầy đủ 7 tham số đầu vào)
    public boolean insert(NguoiDung nd) {
        String sql = "INSERT INTO nguoidung(maNguoiDung, hoTen, email, ngaySinh, gioiTinh, soDienThoai, queQuan) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nd.getMaNguoiDung());
            ps.setString(2, nd.getHoTen());
            ps.setString(3, nd.getEmail());
            ps.setString(4, nd.getNgaySinh());
            ps.setString(5, nd.getGioiTinh());
            ps.setString(6, nd.getSoDienThoai());
            ps.setString(7, nd.getQueQuan());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm cập nhật thông tin chi tiết (Đã sửa vị trí maNguoiDung xuống cuối cùng của mệnh đề WHERE)
    public boolean update(NguoiDung nd) {
        String sql = "UPDATE nguoidung SET hoTen=?, email=?, ngaySinh=?, gioiTinh=?, soDienThoai=?, queQuan=? WHERE maNguoiDung=?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, nd.getHoTen());
            ps.setString(2, nd.getEmail());
            ps.setString(3, nd.getNgaySinh());
            ps.setString(4, nd.getGioiTinh());
            ps.setString(5, nd.getSoDienThoai());
            ps.setString(6, nd.getQueQuan());
            ps.setString(7, nd.getMaNguoiDung()); // Dấu ? số 7 đại diện cho WHERE
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm xóa bỏ vĩnh viễn tài khoản người dùng
    public boolean delete(String maND) {
        String sql = "DELETE FROM nguoidung WHERE maNguoiDung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maND);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hàm tìm kiếm đối tượng theo mã người dùng phục vụ trang chi tiết (Detail Popup)
    public NguoiDung findById(String maND) {
        String sql = "SELECT * FROM nguoidung WHERE maNguoiDung = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, maND);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    NguoiDung nd = new NguoiDung();
                    nd.setMaNguoiDung(rs.getString("maNguoiDung"));
                    nd.setHoTen(rs.getString("hoTen"));
                    nd.setEmail(rs.getString("email"));
                    nd.setNgaySinh(rs.getString("ngaySinh"));
                    nd.setGioiTinh(rs.getString("gioiTinh"));
                    nd.setSoDienThoai(rs.getString("soDienThoai"));
                    nd.setQueQuan(rs.getString("queQuan"));
                    return nd;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}