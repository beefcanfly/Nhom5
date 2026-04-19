package com.trungtam.test;

import com.trungtam.dao.HocVienDAO;
import com.trungtam.model.HocVien;
import java.util.List;

public class TestHocVien {
    public static void main(String[] args) {
        HocVienDAO dao = new HocVienDAO();
        List<HocVien> list = dao.getAll();

        for (HocVien hv : list) {
            System.out.println(hv.getMaHocVien() + " - " + hv.getHoTen());
        }
    }
}
