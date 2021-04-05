/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.dao;


import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import poly.helper.jdbcHelper;
import poly.model.NhanVien;

/**
 *
 * @author admin
 */
public class NhanVienDAO {

    public void insert(NhanVien model) {
        String sql = "insert into nhanvien(MaNV, MatKhau, HoTen, Chuc) values(?,?,?,?)";
        jdbcHelper.excuteUpdate(sql,
                model.getMaNV(),
                model.getMatKhau(),
                model.getHoTen(),
                model.isChucVu()
        );
    }

    public void update(NhanVien model) {
        String sql = "update nhanvien set MatKhau=?, HoTen=?, ChucVu=? where MaNV=?";
        jdbcHelper.excuteUpdate(sql,                
                model.getMatKhau(),
                model.getHoTen(),
                model.isChucVu(),
                model.getMaNV()
        );
    }
    
    public void updateMK(NhanVien model) {
        String sql = "update nhanvien set MatKhau=? where MaNV=?";
        jdbcHelper.excuteUpdate(sql,                
                model.getMatKhau(),              
                model.getMaNV()
        );
    }
    
    public void delete(String MaNV){
        String sql ="delete from nhanvien where MaNV=?";
        jdbcHelper.excuteUpdate(sql,MaNV);
    }
    
    public List<NhanVien> select(){
        String sql ="select* from nhanvien";
        return select(sql);
    }
    
    public NhanVien findById(String manv){
        String sql = "select* from nhanvien where MaNV=?";
        List<NhanVien> list = select(sql,manv);
        return list.size() > 0 ? list.get(0): null;
    }
    
    public NhanVien TimMK(String manv){
        String sql = "select MatKhau from nhanvien where MaNV=?";
        List<NhanVien> list = select(sql,manv);
        return list.size() > 0 ? list.get(0): null;
    }
    private List<NhanVien> select(String sql, Object...args){
        List<NhanVien> list = new ArrayList<>();
        try{
            ResultSet rs = null;
            try{
                rs = jdbcHelper.excuteQuery(sql, args);
                while(rs.next()){
                    NhanVien model = readFromResultSet(rs);
                    list.add(model);
                }
                
            }
            finally{
                rs.getStatement().getConnection().close();
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
        return list;
    }

    private NhanVien readFromResultSet(ResultSet rs) throws SQLException{
        NhanVien model = new NhanVien();
        model.setMaNV(rs.getString("MaNV"));
        model.setMatKhau(rs.getString("MatKhau"));
        model.setHoTen(rs.getString("HoTen"));
        model.setChucVu(rs.getBoolean("ChucVu"));
        //model.setHinh(rs.getString("Hinh"));
        return model;
    }
    
   
   
}

    

