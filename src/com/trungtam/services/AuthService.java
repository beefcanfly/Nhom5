package com.trungtam.services;

import com.trungtam.dao.TaiKhoanDAO;
import com.trungtam.model.TaiKhoan;

public class AuthService {

    private TaiKhoanDAO dao = new TaiKhoanDAO();

    public TaiKhoan login(String username, String password) {

        // validate cơ bản
        if (username == null || password == null) return null;

        username = username.trim();
        password = password.trim();

        if (username.isEmpty() || password.isEmpty()) return null;

        // lấy tài khoản từ DB
        TaiKhoan tk = dao.login(username, password);

        if (tk == null) return null;

        // check trạng thái 
        if (tk.getTrangThai() != null && tk.getTrangThai().equalsIgnoreCase("inactive")) {
            return null;
        }

        return tk;
    }
}