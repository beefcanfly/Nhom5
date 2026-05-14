package com.trungtam.services;

import com.trungtam.dao.*;
import com.trungtam.model.*;
import com.trungtam.utils.SessionManager;

public class AuthService {

    private TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    private NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();
    private GiangVienDAO giangVienDAO = new GiangVienDAO();
    private HocVienDAO hocVienDAO = new HocVienDAO();

    // =========================
    // LOGIN FULL
    // =========================
    public SessionUser login(String username, String password) {

        // 1. validate cơ bản
        if (username == null || password == null) return null;

        username = username.trim();
        password = password.trim();

        if (username.isEmpty() || password.isEmpty()) return null;

        // 2. check tài khoản
        TaiKhoan tk = taiKhoanDAO.login(username, password);

        if (tk == null) return null;

        // 3. check trạng thái
        if (tk.getTrangThai() != null &&
            tk.getTrangThai().equalsIgnoreCase("inactive")) {
            return null;
        }

        // =========================
        // 4. TẠO SESSION
        // =========================
        SessionUser session = new SessionUser();
        session.setTaiKhoan(tk);

        // 5. lấy thông tin người dùng (chung)
        NguoiDung nd = nguoiDungDAO.findById(tk.getMaNguoiDung());
        session.setNguoiDung(nd);

        // 6. phân quyền dữ liệu riêng
        String role = tk.getVaiTro().toLowerCase();

        if (role.equals("giangvien")) {

            GiangVien gv = giangVienDAO.findById(tk.getUsername());
            session.setGiangVien(gv);

        } else if (role.equals("hocvien")) {

            HocVien hv = hocVienDAO.findById(tk.getUsername());
            session.setHocVien(hv);

        }

        // =========================
        // 7. LƯU SESSION GLOBAL
        // =========================
        SessionManager.setSession(session);

        return session;
    }
}