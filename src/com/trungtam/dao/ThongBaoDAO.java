package com.trungtam.dao;

import com.trungtam.model.ThongBao;
import com.trungtam.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ThongBaoDAO {

    // Hàm lấy danh sách thông báo dựa trên vai trò (role)
    public List<ThongBao> getThongBaoByRole(int role) {
        List<ThongBao> ds = new ArrayList<>();
        // Query: Lấy thông báo dành riêng cho role đó HOẶC thông báo cho tất cả (0)
        String sql = "SELECT * FROM thongbao WHERE doiTuongNhan = ? OR doiTuongNhan = 0 ORDER BY maThongBao DESC";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, role);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ThongBao tb = new ThongBao();
                tb.setMaThongBao(rs.getInt("maThongBao"));
                tb.setTieuDe(rs.getString("tieuDe"));
                tb.setNoiDung(rs.getString("noiDung"));
                tb.setDoiTuongNhan(rs.getInt("doiTuongNhan"));

                ds.add(tb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Hàm dành cho Admin để đăng thông báo mới
    public boolean insertThongBao(ThongBao tb) {
        String sql = "INSERT INTO thongbao (tieuDe, noiDung, doiTuongNhan) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, tb.getTieuDe());
            ps.setString(2, tb.getNoiDung());
            ps.setInt(3, tb.getDoiTuongNhan());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}