package com.trungtam.model;

public class HocVien {
    private String maHV;
    private String maNguoiDung;
    private String trangThai;

    public HocVien() {}

    public HocVien(String maHV, String maNguoiDung, String trangThai) {
        this.maHV = maHV;
        this.maNguoiDung = maNguoiDung;
        this.trangThai = trangThai;
    }

    public String getMaHV() { return maHV; }
    public void setMaHV(String maHV) { this.maHV = maHV; }

    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(String maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
