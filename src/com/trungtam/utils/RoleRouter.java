package com.trungtam.utils;

import com.trungtam.model.TaiKhoan;
import com.trungtam.test.TestGiangVienUI;
import com.trungtam.test.TestHocVienUI;

public class RoleRouter {
    
    public static void openUI(TaiKhoan tk) {
        if (tk == null || tk.getRole() == null) {
            System.out.println("Lỗi: Đối tượng tài khoản hoặc quyền bị null!");
            return;
        }

        // Lấy quyền, chuyển thành chữ hoa và xóa khoảng trắng thừa
        String quyen = tk.getRole().trim().toUpperCase();

        // Kiểm tra đúng theo giá trị thực tế trong DB của bạn
        if (quyen.equals("SINHVIEN") || quyen.equals("SINH_VIEN") || quyen.equals("HOCVIEN")) {
            // Mở giao diện Sinh Viên / Học Viên
            new TestHocVienUI().setVisible(true);
            
        } else if (quyen.equals("GIANGVIEN") || quyen.equals("GIANG_VIEN")) {
            // Mở giao diện Giảng Viên (Khớp chính xác với [GIANGVIEN] từ DB)
            new TestGiangVienUI().setVisible(true);
            
        } else {
            // Trường hợp lỗi role
            System.out.println("Tài khoản không có quyền truy cập hợp lệ!");
        }
    }
}