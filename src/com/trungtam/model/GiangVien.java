package com.trungtam.model;

public class GiangVien {
    private String maGV;
    private String maNguoiDung;
    private String hocVi;
    private String chuyenMon;

    public GiangVien() {}

    public GiangVien(String maGV, String maNguoiDung, String hocVi, String chuyenMon) {
        this.maGV = maGV;
        this.maNguoiDung = maNguoiDung;
        this.hocVi = hocVi;
        this.chuyenMon = chuyenMon;
    }

    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }

    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(String maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getHocVi() { return hocVi; }
    public void setHocVi(String hocVi) { this.hocVi = hocVi; }

    public String getChuyenMon() { return chuyenMon; }
    public void setChuyenMon(String chuyenMon) { this.chuyenMon = chuyenMon; }
}