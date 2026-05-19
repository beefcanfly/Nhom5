package com.trungtam.model;


public class DangKy {
    private int maDangKy;
    private String maHV;
    private String maLop;
    private String trangThai;
    
    // Các thuộc tính bổ sung để hỗ trợ hiển thị giao diện (Optional)
    private String tenHocVien;
    private String tenLop;
    private String tenKhoaHoc;

    // Constructor không tham số
    public DangKy() {
    }

    // Constructor đầy đủ tham số (Dùng khi lấy dữ liệu từ Database)
    public DangKy(int maDangKy, String maHV, String maLop, String trangThai) {
        this.maDangKy = maDangKy;
        this.maHV = maHV;
        this.maLop = maLop;
        this.trangThai = trangThai;
    }

    public int getMaDangKy() {
        return maDangKy;
    }

    public void setMaDangKy(int maDangKy) {
        this.maDangKy = maDangKy;
    }

    public String getMaHV() {
        return maHV;
    }

    public void setMaHV(String maHV) {
        this.maHV = maHV;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenHocVien() {
        return tenHocVien;
    }

    public void setTenHocVien(String tenHocVien) {
        this.tenHocVien = tenHocVien;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getTenKhoaHoc() {
        return tenKhoaHoc;
    }

    public void setTenKhoaHoc(String tenKhoaHoc) {
        this.tenKhoaHoc = tenKhoaHoc;
    }

    // Hàm toString để hỗ trợ debug/kiểm tra dữ liệu nhanh
    @Override
    public String toString() {
        return "DangKy{" +
                "maDangKy=" + maDangKy +
                ", maHV='" + maHV + '\'' +
                ", maLop='" + maLop + '\'' +
                ", trangThai='" + trangThai + '\'' +
                '}';
    }
}
