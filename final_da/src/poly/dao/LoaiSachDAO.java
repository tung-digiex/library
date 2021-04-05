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
import poly.model.LoaiSach;

/**
 *
 * @author admin
 */
public class LoaiSachDAO {
    public void insert(LoaiSach model) {
        String sql = "insert into loaisach(MaLoai, TenLoai) values(?,?)";
        jdbcHelper.excuteUpdate(sql,
                model.getMaLoai(),
                model.getTenLoai()                
        );
    }

    public void update(LoaiSach model) {
        String sql = "update loaisach set TenLoai=? where MaLoai=?";
        jdbcHelper.excuteUpdate(sql,  
                
                model.getTenLoai(),
                model.getMaLoai()
        );
    }
    
    public void delete(String MaLoai){
        String sql ="delete from loaisach where MaLoai=?";
        jdbcHelper.excuteUpdate(sql,MaLoai);
    }
    
    public List<LoaiSach> select(){
        String sql ="select* from loaisach";
        return select(sql);
    }
    
    public LoaiSach findById(String maloai){
        String sql = "select* from loaisach where MaLoai=?";
        List<LoaiSach> list = select(sql,maloai);
        return list.size() > 0 ? list.get(0): null;
    }
    
    private List<LoaiSach> select(String sql, Object...args){
        List<LoaiSach> list = new ArrayList<>();
        try{
            ResultSet rs = null;
            try{
                rs = jdbcHelper.excuteQuery(sql, args);
                while(rs.next()){
                    LoaiSach model = readFromResultSet(rs);
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

    private LoaiSach readFromResultSet(ResultSet rs) throws SQLException{
        LoaiSach model = new LoaiSach();
        model.setMaLoai(rs.getString("MaLoai"));
        model.setTenLoai(rs.getString("TenLoai"));
        
        return model;
    }
}
