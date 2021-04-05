/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.model;

/**
 *
 * @author admin
 */
public class ThongKeDoanhThu {
    private String tenSach;
    private int maSach;
    private int maHD;
    private int soLuong;
    private int doanhThu;

    public ThongKeDoanhThu() {
    }

    public ThongKeDoanhThu(String tenSach, int maSach, int maHD, int soLuong, int doanhThu) {
        this.tenSach = tenSach;
        this.maSach = maSach;
        this.maHD = maHD;
        this.soLuong = soLuong;
        this.doanhThu = doanhThu;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public int getMaSach() {
        return maSach;
    }

    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getDoanhThu() {
        return doanhThu;
    }

    public void setDoanhThu(int doanhThu) {
        this.doanhThu = doanhThu;
    }
    
    
}
