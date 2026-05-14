package com.trungtam.utils;

import javax.swing.JOptionPane;

import com.trungtam.model.SessionUser;

import view.AdminUI;
import view.GiangVienUI;
import view.HocVienUI;

public class RoleRouter {

    public static void openUI(SessionUser session) {

        String role = session.getTaiKhoan().getVaiTro().toLowerCase();

        switch (role) {

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
                JOptionPane.showMessageDialog(null,
                        "Vai trò không hợp lệ: " + role);
        }
    }
}