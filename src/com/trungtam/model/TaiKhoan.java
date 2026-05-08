package com.trungtam.model;

public class TaiKhoan {
    // Các thuộc tính bám sát Database của bạn
    private String username;
    private String password;
    private String vaiTro;      
    private String trangThai;   
    private String maNguoiDung; 

    // ================= CONSTRUCTORS =================
    
    // 1. Constructor rỗng (Khắc phục lỗi: The constructor TaiKhoan() is undefined)
    public TaiKhoan() {
    }

    // 2. Constructor 3 tham số (Dành cho AuthService)
    public TaiKhoan(String username, String password, String vaiTro) {
        this.username = username;
        this.password = password;
        this.vaiTro = vaiTro;
    }

    // 3. Constructor đầy đủ tham số
    public TaiKhoan(String username, String password, String vaiTro, String trangThai, String maNguoiDung) {
        this.username = username;
        this.password = password;
        this.vaiTro = vaiTro;
        this.trangThai = trangThai;
        this.maNguoiDung = maNguoiDung;
    }

    // ================= GETTERS & SETTERS =================
    // Khắc phục toàn bộ lỗi: The method ... is undefined

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }
    
    // Hàm phụ trợ nếu các file khác quen dùng chữ "Role" thay vì "VaiTro"
    public String getRole() { 
        return vaiTro; 
    }
}