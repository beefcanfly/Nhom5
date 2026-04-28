package com.trungtam.test;

import com.trungtam.dao.TaiKhoanDAO;
import com.trungtam.model.TaiKhoan;

public class TestLogin {
    public static void main(String[] args) {

        TaiKhoanDAO dao = new TaiKhoanDAO();

        TaiKhoan tk = dao.login("hv01", "123"); // đổi theo dữ liệu bạn

        if (tk != null) {
            System.out.println("Đăng nhập thành công!");
            System.out.println("Vai trò: " + tk.getVaiTro());

            // phân quyền
            if (tk.getVaiTro().equalsIgnoreCase("admin")) {
                System.out.println("Mở giao diện ADMIN");
            } else if (tk.getVaiTro().equalsIgnoreCase("giangvien")) {
                System.out.println("Mở giao diện GIẢNG VIÊN");
            } else if (tk.getVaiTro().equalsIgnoreCase("hocvien")) {
                System.out.println("Mở giao diện HỌC VIÊN");
            }

        } else {
            System.out.println("Sai tài khoản hoặc mật khẩu!");
        }
    }
}
