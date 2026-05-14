package com.trungtam.dao;

import com.trungtam.model.NguoiDung;
import com.trungtam.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NguoiDungDAO {

	public NguoiDung findById(String maNguoiDung) {
	    try (Connection conn = DBConnection.getConnection()) {

	        String sql = "SELECT * FROM nguoidung WHERE maNguoiDung = ?";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, maNguoiDung);

	        ResultSet rs = ps.executeQuery();

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

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
    // READ ALL
    public List<NguoiDung> getAll() {
        List<NguoiDung> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM nguoidung";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                NguoiDung nd = new NguoiDung();
                nd.setMaNguoiDung(rs.getString("maNguoiDung"));
                nd.setHoTen(rs.getString("hoTen"));
                nd.setEmail(rs.getString("email"));
                nd.setNgaySinh(rs.getString("ngaySinh")); // FIX
                nd.setGioiTinh(rs.getString("gioiTinh"));
                nd.setSoDienThoai(rs.getString("soDienThoai"));
                nd.setQueQuan(rs.getString("queQuan"));

                list.add(nd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // INSERT
    public boolean insert(NguoiDung nd) {
        String sql = "INSERT INTO nguoidung(hoTen, email, ngaySinh, gioiTinh, soDienThoai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nd.getHoTen());
            ps.setString(2, nd.getEmail());
            ps.setString(3, nd.getNgaySinh()); // FIX
            ps.setString(4, nd.getGioiTinh());
            ps.setString(5, nd.getSoDienThoai());
            ps.setString(6, nd.getQueQuan());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // UPDATE
    public boolean update(NguoiDung nd) {
        String sql = "UPDATE nguoidung SET hoTen=?, email=?, ngaySinh=?, gioiTinh=?, soDienThoai=? WHERE maNguoiDung=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nd.getHoTen());
            ps.setString(2, nd.getEmail());
            ps.setString(3, nd.getNgaySinh()); // FIX
            ps.setString(4, nd.getGioiTinh());
            ps.setString(5, nd.getSoDienThoai());
            ps.setString(6, nd.getMaNguoiDung());
            ps.setString(7, nd.getQueQuan());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // DELETE
    public boolean delete(String maNguoiDung) {
        String sql = "DELETE FROM nguoidung WHERE maNguoiDung=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNguoiDung);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}