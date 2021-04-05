/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.model;

/**
 *
 * @author Admin
 */
public class Sach {
    private int MaSach;
    private String TenSach;
    private int MaTG;
    private String MaLoai;
    private int SoLuong;
    private String NamXB;
    private String NXB;
    private int Gia;
    
   @Override
    public String toString(){
        return this.TenSach;
    }

    public int getMaSach() {
        return MaSach;
    }

    public void setMaSach(int MaSach) {
        this.MaSach = MaSach;
    }

    public String getTenSach() {
        return TenSach;
    }

    public void setTenSach(String TenSach) {
        this.TenSach = TenSach;
    }

    public int getMaTG() {
        return MaTG;
    }

    public void setMaTG(int MaTG) {
        this.MaTG = MaTG;
    }

    public String getMaLoai() {
        return MaLoai;
    }

    public void setMaLoai(String MaLoai) {
        this.MaLoai = MaLoai;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    public String getNamXB() {
        return NamXB;
    }

    public void setNamXB(String NamXB) {
        this.NamXB = NamXB;
    }

    public String getNXB() {
        return NXB;
    }

    public void setNXB(String NXB) {
        this.NXB = NXB;
    }

    public int getGia() {
        return Gia;
    }

    public void setGia(int Gia) {
        this.Gia = Gia;
    }
    
}
   