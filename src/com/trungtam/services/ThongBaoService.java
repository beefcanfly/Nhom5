package com.trungtam.services;

import com.trungtam.dao.ThongBaoDAO;
import com.trungtam.model.ThongBao;
import java.util.List;

public class ThongBaoService {
    private ThongBaoDAO thongBaoDAO;

    public ThongBaoService() {
        this.thongBaoDAO = new ThongBaoDAO();
    }

    // Lấy thông báo cho Học viên (Role = 1)
    public List<ThongBao> getThongBaoChoHocVien() {
        return thongBaoDAO.getThongBaoByRole(1);
    }

    // Lấy thông báo cho Giảng viên (Role = 2)
    public List<ThongBao> getThongBaoChoGiangVien() {
        return thongBaoDAO.getThongBaoByRole(2);
    }

    // Admin đăng thông báo
    public String taoThongBaoMoi(String tieuDe, String noiDung, int doiTuong) {
        if (tieuDe.isEmpty() || noiDung.isEmpty()) {
            return "Tiêu đề và nội dung không được để trống!";
        }
        
        ThongBao tb = new ThongBao();
        tb.setTieuDe(tieuDe);
        tb.setNoiDung(noiDung);
        tb.setDoiTuongNhan(doiTuong);

        if (thongBaoDAO.insertThongBao(tb)) {
            return "Đăng thông báo thành công!";
        }
        return "Lỗi khi lưu vào cơ sở dữ liệu.";
    }
}
