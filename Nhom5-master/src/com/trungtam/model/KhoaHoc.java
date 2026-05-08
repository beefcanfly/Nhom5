package com.trungtam.model;

public class KhoaHoc {
    private String maKhoaHoc;
    private String tenKhoaHoc;
    private double hocPhi;
    private int thoiLuong;
    private String trangThai;

    public KhoaHoc() {}

    public KhoaHoc(String maKhoaHoc, String tenKhoaHoc, double hocPhi, int thoiLuong, String trangThai) {
        this.maKhoaHoc = maKhoaHoc;
        this.tenKhoaHoc = tenKhoaHoc;
        this.hocPhi = hocPhi;
        this.thoiLuong = thoiLuong;
        this.trangThai = trangThai;
    }

    public String getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(String maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }

    public String getTenKhoaHoc() { return tenKhoaHoc; }
    public void setTenKhoaHoc(String tenKhoaHoc) { this.tenKhoaHoc = tenKhoaHoc; }

    public double getHocPhi() { return hocPhi; }
    public void setHocPhi(double hocPhi) { this.hocPhi = hocPhi; }

    public int getThoiLuong() { return thoiLuong; }
    public void setThoiLuong(int thoiLuong) { this.thoiLuong = thoiLuong; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}