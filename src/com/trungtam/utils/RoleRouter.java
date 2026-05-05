package com.trungtam.utils;

import com.trungtam.model.TaiKhoan;
import view.AdminUI;
import view.GiangVienUI;
import view.HocVienUI;

public class RoleRouter {

    public static void openUI(TaiKhoan tk) {

        switch (tk.getVaiTro().toLowerCase()) {
            case "admin":
                new AdminUI().setVisible(true);
                break;

            case "giangvien":
                new GiangVienUI().setVisible(true);
                break;

            case "hocvien":
                new HocVienUI().setVisible(true);
                break;

            default:
                System.out.println("Vai trò không hợp lệ!");
        }
    }
}
