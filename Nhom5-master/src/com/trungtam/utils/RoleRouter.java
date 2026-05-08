package com.trungtam.utils;

import com.trungtam.model.TaiKhoan;
import view.HocVienUI;
import view.GiangVienUI;
import view.AdminUI;

public class RoleRouter {
    
    public static void openUI(TaiKhoan tk) {
        String quyen = tk.getRole().toUpperCase();

        if (quyen.equals("SINH_VIEN")) {
            // Mở giao diện Sinh Viên
            new HocVienUI().setVisible(true);
            
        } else if (quyen.equals("GIANG_VIEN")) {
            // Mở giao diện Giảng Viên
            new GiangVienUI().setVisible(true);
            
        } else if (quyen.equals("ADMIN")) {
            // Mở giao diện Giảng Viên
            new AdminUI().setVisible(true);
        } else {
            // Trường hợp lỗi role
            System.out.println("Tài khoản không có quyền truy cập hợp lệ!");
        }
    }
}
