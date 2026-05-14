package com.trungtam.model;

public class ThongBao {
    private int maThongBao;
    private String tieuDe;
    private String noiDung;
    private int doiTuongNhan; // 0: Tất cả, 1: Học viên, 2: Giảng viên


    // Constructor không tham số (Dùng khi lấy dữ liệu từ DB)
    public ThongBao() {
    }

    // Constructor có tham số (Dùng khi Admin tạo thông báo mới)
    public ThongBao(String tieuDe, String noiDung, int doiTuongNhan) {
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.doiTuongNhan = doiTuongNhan;
    }

    // --- GETTER VÀ SETTER ---

    public int getMaThongBao() {
        return maThongBao;
    }

    public void setMaThongBao(int maThongBao) {
        this.maThongBao = maThongBao;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public int getDoiTuongNhan() {
        return doiTuongNhan;
    }

    public void setDoiTuongNhan(int doiTuongNhan) {
        this.doiTuongNhan = doiTuongNhan;
    }


    // Ghi đè phương thức toString để hỗ trợ debug/test nếu cần
    @Override
    public String toString() {
        return "ThongBao{" +
                "maThongBao=" + maThongBao +
                ", tieuDe='" + tieuDe + '\'' +
                ", doiTuongNhan=" + doiTuongNhan +              
                '}';
    }
}