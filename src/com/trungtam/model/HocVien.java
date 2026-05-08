package com.trungtam.model;

public class HocVien {
    private String maHV;
    private String maNguoiDung;
    private String trangThaiHoc;

    public HocVien() {}

    public HocVien(String maHV, String maNguoiDung, String trangThaiHoc) {
        this.maHV = maHV;
        this.maNguoiDung = maNguoiDung;
        this.trangThaiHoc = trangThaiHoc;
    }

    public String getMaHV() { return maHV; }
    public void setMaHV(String maHV) { this.maHV = maHV; }

    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(String maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getTrangThaiHoc() { return trangThaiHoc; }
    public void setTrangThaiHoc(String trangThaiHoc) { this.trangThaiHoc = trangThaiHoc; }
}
