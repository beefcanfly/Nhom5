package com.trungtam.model;

public class LopHoc {
    private String maLop;
    private String tenLop;
    private String maKhoaHoc;
    private String maGV;
    private String caHoc;
    private String lichHoc;
    private String trangThai;
    private double hocPhi;
    private int thoiLuong;

    public LopHoc() {}

    public LopHoc(String maLop, String tenLop, String maKhoaHoc,
                  String maGV, String caHoc, String lichHoc, String trangThai) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.maKhoaHoc = maKhoaHoc;
        this.maGV = maGV;
        this.caHoc = caHoc;
        this.lichHoc = lichHoc;
        this.trangThai = trangThai;
    }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }

    public String getMaKhoaHoc() { return maKhoaHoc; }
    public void setMaKhoaHoc(String maKhoaHoc) { this.maKhoaHoc = maKhoaHoc; }

    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }

    public String getCaHoc() { return caHoc; }
    public void setCaHoc(String caHoc) { this.caHoc = caHoc; }

    public String getLichHoc() { return lichHoc; }
    public void setLichHoc(String lichHoc) { this.lichHoc = lichHoc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

}
