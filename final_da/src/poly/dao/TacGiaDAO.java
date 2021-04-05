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
import poly.model.Sach;
import poly.model.TacGia;

/**
 *
 * @author admin
 */
public class TacGiaDAO {
    public void insert(TacGia model) {
        String sql = "insert into tacgia( TenTG, GioiThieu) values(?,?)";
        jdbcHelper.excuteUpdate(sql,
                
                model.getTenTG(),
                model.getGioiThieu()
                
        );
    }

    public void update(TacGia model) {
        String sql = "update tacgia set TenTG=?, TenTG=? where MaTG=?";
        jdbcHelper.excuteUpdate(sql,  
                
                model.getTenTG(),
                model.getGioiThieu(),
                model.getMaTG()
        );
    }
    
    public void delete(int MaTG){
        String sql ="delete from tacgia where MaTG=?";
        jdbcHelper.excuteUpdate(sql,MaTG);
    }
    
    public List<TacGia> select(){
        String sql ="select* from tacgia";
        return select(sql);
    }
    
    public TacGia findById(int matg){
        String sql = "select* from tacgia where MaTG=?";
        List<TacGia> list = select(sql,matg);
        return list.size() > 0 ? list.get(0): null;
    }
    public TacGia selectByName(String name){
          String sql = "SELECT * FROM TacGia WHERE TenTG LIKE ?";
          List<TacGia> list = select(sql, name);
          return list.size() > 0 ? list.get(0) : null;
    }
    
    public List<TacGia> selectByKeyword(String keyword) {
        String sql = "SELECT * FROM tacgia WHERE TenTG LIKE ?";
        return select(sql, "%" + keyword + "%");
    }
    
    private List<TacGia> select(String sql, Object...args){
        List<TacGia> list = new ArrayList<>();
        try{
            ResultSet rs = null;
            try{
                rs = jdbcHelper.excuteQuery(sql, args);
                while(rs.next()){
                    TacGia model = readFromResultSet(rs);
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

    private TacGia readFromResultSet(ResultSet rs) throws SQLException{
        TacGia model = new TacGia();
        model.setMaTG(rs.getInt("MaTG"));
        model.setTenTG(rs.getString("TenTG"));
        model.setGioiThieu(rs.getString("GioiThieu"));
        
        return model;
    }
}
