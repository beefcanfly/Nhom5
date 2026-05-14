package com.trungtam.dao;

import java.sql.*;
import com.trungtam.utils.DBConnection;

public class AdminDAO {
    public int[] getDashboardStats() {
        int[] stats = new int[4];
        // Query gom tất cả các chỉ số quan trọng
        String sql = "SELECT " +
                     "(SELECT COUNT(*) FROM hocvien) AS total_hv, " +
                     "(SELECT COUNT(DISTINCT maHV) FROM dangky) AS reg_hv, " +
                     "(SELECT COUNT(*) FROM giangvien) AS total_gv, " +
                     "(SELECT COUNT(*) FROM giangvien WHERE trangThai LIKE N'%active%') AS active_gv";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                stats[0] = rs.getInt("total_hv");    // Tổng học viên
                stats[1] = rs.getInt("reg_hv");      // Học viên đã đăng ký môn
                stats[2] = rs.getInt("total_gv");    // Tổng giảng viên
                stats[3] = rs.getInt("active_gv");   // Giảng viên đang dạy
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }
}