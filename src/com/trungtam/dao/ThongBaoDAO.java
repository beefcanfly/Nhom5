package com.trungtam.dao;

import com.trungtam.model.ThongBao;
import com.trungtam.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ThongBaoDAO {

    // Hàm lấy danh sách thông báo động rút gọn trường ngày tháng (Dùng cho cả AdminUI và FullUI)
    public List<ThongBao> getThongBaoByRole(int role) {
        List<ThongBao> ds = new ArrayList<>();
        // Lấy thông báo riêng cho vai trò đó HOẶC thông báo dùng chung cho tất cả (0)
        String sql = "SELECT * FROM thongbao WHERE doiTuongNhan = ? OR doiTuongNhan = 0 ORDER BY maThongBao DESC";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, role);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongBao tb = new ThongBao();
                    tb.setMaThongBao(rs.getInt("maThongBao"));
                    tb.setTieuDe(rs.getString("tieuDe"));
                    tb.setNoiDung(rs.getString("noiDung"));
                    tb.setDoiTuongNhan(rs.getInt("doiTuongNhan"));
                    ds.add(tb);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Hàm xử lý đẩy dữ liệu thông báo mới từ cổng Admin lên CSDL
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