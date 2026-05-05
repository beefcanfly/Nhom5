package com.trungtam.model;

public class NguoiDung {

    private String maNguoiDung;
    private String hoTen;
    private String email;
    private String ngaySinh;
    private String gioiTinh;
    private String soDienThoai;

    // Constructor rỗng
    public NguoiDung() {
    }

    // Constructor đầy đủ
    public NguoiDung(String maNguoiDung, String hoTen, String email,
                     String ngaySinh, String gioiTinh, String soDienThoai) {
        this.maNguoiDung = maNguoiDung;
        this.hoTen = hoTen;
        this.email = email;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
    }

    // Getter & Setter
    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    // toString 
    @Override
    public String toString() {
        return "NguoiDung{" +
                "maNguoiDung=" + maNguoiDung +
                ", hoTen='" + hoTen + '\'' +
                ", email='" + email + '\'' +
                ", ngaySinh=" + ngaySinh +
                ", gioiTinh='" + gioiTinh + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                '}';
    }
}