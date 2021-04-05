/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import poly.helper.jdbcHelper;

/**
 *
 * @author admin
 */
public class ThongKeDAO {
    public List<Object[]>getDoanhThu(int nam){
        List<Object[]> list = new ArrayList<>();
        try{
            ResultSet rs = null;
            try{
                String sql ="{call sp_ThongKeDoanhThu(?)}";
                rs = jdbcHelper.excuteQuery(sql, nam);
                while(rs.next()){
                    Object[] model ={
                        rs.getString("TenSach"),
                        rs.getInt("MaSach"),
                        rs.getInt("MaHD"),
                        rs.getInt("SoLuong"),
                        rs.getDouble("DoanhThu")
                        
                    };
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
}
