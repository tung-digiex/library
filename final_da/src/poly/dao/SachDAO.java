/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.dao;

import poly.helper.jdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import poly.model.Sach;

/**
 *
 * @author Admin
 */
public class SachDAO {
    //Câu lệnh insert
    public void insert(Sach model){ 
        String sql="INSERT INTO sach  VALUES (?, ?, ?, ?,?, ?, ?)";
        jdbcHelper.excuteUpdate(sql,                
                model.getTenSach(),
                model.getMaTG(),
                model.getMaLoai(),
                model.getSoLuong(),
                model.getNamXB(),
                model.getNXB(),
                model.getGia());
    }
    //câu lệnh update 
    public void update(Sach model){
        String sql="UPDATE sach SET TenSach=?, MaTG=?, MaLoai=?, SoLuong=?, NamXB=?, NXB=?, Gia=? WHERE MaSach=?";
        jdbcHelper.excuteUpdate(sql,
                model.getTenSach(),
                model.getMaTG(),
                model.getMaLoai(),
                model.getSoLuong(),
                model.getGia(),
                model.getNamXB(),
                model.getNXB(),
                model.getMaSach());
    }
    public void updateSL(Sach model){
        String sql ="update sach set SoLuong=? where MaSach = ?";
        jdbcHelper.excuteUpdate(sql,
                model.getSoLuong(),
                model.getMaSach());    
    }
    //câu lệnh delete
    public void delete(int MaSach){
        String sql="DELETE FROM sach WHERE MaSach=?";
        jdbcHelper.excuteUpdate(sql, MaSach);
    }
    //
    
    public Sach selectByName(String name){
          String sql = "SELECT * FROM sach WHERE TenSach LIKE ?";
          List<Sach> list = select(sql, name);
          return list.size() > 0 ? list.get(0) : null;
    }
    
    public List<Sach> select(){ 
        String sql="SELECT * FROM sach";
        return select(sql);
    }
    
    public List<Sach> selectByKeyword(String keyword) {
        String sql = "SELECT * FROM sach WHERE TenSach LIKE ?";
        return select(sql, "%" + keyword + "%");
    }

    
    public Sach findById(int macd){
          String sql="SELECT * FROM sach WHERE MaSach=?";
          List<Sach> list = select(sql, macd);
          return list.size() > 0 ? list.get(0) : null;
    }
    
    private List<Sach> select(String sql, Object...args){
            List<Sach> list=new ArrayList<>();  
        try {
            ResultSet rs = null;
            try {
                rs = jdbcHelper.excuteQuery(sql, args);
                while(rs.next()){ 
                    Sach model=readFromResultSet(rs);
                    list.add(model);
                }
            }
            finally{
                rs.getStatement().getConnection().close();
            }
        } 
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }
    
    private Sach readFromResultSet(ResultSet rs) throws SQLException{ 
          Sach model=new Sach();
          model.setMaSach(rs.getInt("MaSach"));
          model.setTenSach(rs.getString("TenSach"));
          model.setMaTG(rs.getInt("MaTG"));
          model.setMaLoai(rs.getString("MaLoai")); 
          model.setSoLuong(rs.getInt("SoLuong"));
          model.setNamXB(rs.getString("NamXB"));       
          model.setNXB(rs.getString("NXB"));
          model.setGia(rs.getInt("Gia"));
          return model; 
    }
}
