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
import poly.helper.DateHelper;
import poly.helper.jdbcHelper;
import poly.model.HoaDon;

/**
 *
 * @author admin
 */
public class HoaDonDAO {
    public void insert(HoaDon model){
        String sql = "insert into hoadon values(?,?)";        
        jdbcHelper.excuteUpdate(sql,                  
                model.getTenKH(),                                 
                model.getMaNV()                
        );
    }    
    public void insert1(HoaDon model){
        String sql1 = "insert into chitiethoadon values(?,?,?,?,?,?)";
        jdbcHelper.excuteUpdate(sql1,
                model.getMaHD(),                
                model.getMaSach(),
                model.getSoLuong(),
                model.getGia(),
                model.getThanhTien(),
                DateHelper.toString(model.getNgayLap(), "MM-dd-yyyy")
                );
    }
    
    public void update(HoaDon model){
        String sql = "update hoadon set TenKH=?, MaSach=?, SoLuong=?, Gia=?, MaNV=?, ThanhTien=?, NgayLap=? where MaHD=?";
        jdbcHelper.excuteUpdate(sql,                                                 
                model.getTenKH(), 
                model.getMaSach(),
                model.getSoLuong(),
                model.getGia(),
                model.getMaNV(),
                model.getThanhTien(),
                DateHelper.toString(model.getNgayLap()),
                model.getMaHD()                
        );
    }
    
    public void delete(Integer MaHD){
        String sql = "delete from hoadon where MaHD=?";
        jdbcHelper.excuteUpdate(sql, MaHD);
    }
    
    public List<HoaDon> select(){
        String sql =" select* from hoadon";
        return select(sql);
    }
    
    public List<HoaDon> select1(){
        String sql = "select chitiethoadon.mahd,tenkh,chitiethoadon.masach,soluong,gia,hoadon.manv,thanhtien,ngaylap from chitiethoadon join hoadon on hoadon.mahd=chitiethoadon.mahd";
        return select(sql);
    }    
    
    public HoaDon findById(Integer mahd){
        String sql = "select* from hoadon where MaHD=?";
        List<HoaDon> list = select(sql,mahd);
        return list.size() > 0 ? list.get(0): null;
    }
    
    private List<HoaDon> select(String sql, Object...args){
        List<HoaDon> list = new ArrayList<>();
        try{
            ResultSet rs = null;
            try{
                rs = jdbcHelper.excuteQuery(sql, args);
                while(rs.next()){
                    HoaDon model = readFromResultSet(rs);
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

    private HoaDon readFromResultSet(ResultSet rs) throws SQLException{
        HoaDon model = new HoaDon();
        model.setMaHD(rs.getInt("MaHD"));
        model.setTenKH(rs.getString("TenKH"));  
        model.setMaSach(rs.getInt("MaSach"));
        model.setSoLuong(rs.getInt("SoLuong"));
        model.setGia(rs.getInt("Gia"));
        model.setMaNV(rs.getString("MaNV"));
        model.setNgayLap(rs.getDate("ngaylap"));
        model.setThanhTien(rs.getInt("ThanhTien"));
        return model;
    }
}
