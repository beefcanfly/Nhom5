package com.trungtam.model;
import java.util.Date;

public class TaiKhoan {
    private String username;
    private String password;
    private String vaiTro;
    private String trangThai;
    private String maNguoiDung;
    private Date lanDangNhapCuoi;

    public TaiKhoan() {}

    public TaiKhoan(String username, String password, String vaiTro,
                    String trangThai, String maNguoiDung, Date lanDangNhapCuoi) {
        this.username = username;
        this.password = password;
        this.vaiTro = vaiTro;
        this.trangThai = trangThai;
        this.maNguoiDung = maNguoiDung;
        this.lanDangNhapCuoi = lanDangNhapCuoi;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(String maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public Date getLanDangNhapCuoi() { return lanDangNhapCuoi; }
    public void setLanDangNhapCuoi(Date lanDangNhapCuoi) { this.lanDangNhapCuoi = lanDangNhapCuoi; }
}
