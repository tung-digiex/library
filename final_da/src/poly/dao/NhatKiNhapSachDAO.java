/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.dao;

import poly.model.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import poly.helper.DateHelper;
import poly.helper.jdbcHelper;
import poly.model.NhatKiNhapSach;

/**
 *
 * @author Administrator
 */
public class NhatKiNhapSachDAO {

    public void insert(NhatKiNhapSach model) {
        String sql = "insert into nhatkinhapsach ( MaSach, SoLuong, NgayNhap) values (?, ?, ?)";
        jdbcHelper.excuteUpdate(sql,
                model.getMaSach(),
                model.getSoLuong(),
                DateHelper.toString(model.getNgayNhap(), "MM-dd-yyyy"));
    }

//    public void update(NhatKiNhapSach model) {
//        String sql = "update nhatkinhapsach set MaSach=?, SoLuong=?, NgayNhap=? Where STT=?";
//        jdbcHelper.excuteUpdate(sql,
//                model.getMaSach(),
//                model.getSoLuong(),
//                model.getNgayNhap(),
//                model.getStt());
//    }
    
    public void update(NhatKiNhapSach model) {
        String sql = "update nhatkinhapsach set MaSach=?, SoLuong=?, NgayNhap=? where STT=?";
        jdbcHelper.excuteUpdate(sql,
                model.getMaSach(),
                model.getSoLuong(),
                DateHelper.toString(model.getNgayNhap()),
                model.getStt());
    }

    public void delete(String maSach) {
        String sql = "delete from nhatkinhapsach where MaSach=?";
        jdbcHelper.excuteUpdate(sql, maSach);
    }

    public List<NhatKiNhapSach> select() {
        String sql = "select * from nhatkinhapsach";
        return select(sql);
    }

    private List<NhatKiNhapSach> select(String sql, Object... args) {
        List<NhatKiNhapSach> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = jdbcHelper.excuteQuery(sql, args);
                while (rs.next()) {
                    NhatKiNhapSach model = readFromResultSet(rs);
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
                throw new RuntimeException(ex);
        }
        return list;
    }

    private NhatKiNhapSach readFromResultSet(ResultSet rs) throws SQLException {
        NhatKiNhapSach model = new NhatKiNhapSach();
        model.setStt(rs.getInt("STT"));
        model.setMaSach(rs.getString("MaSach"));
        model.setSoLuong(rs.getInt("SoLuong"));
        model.setNgayNhap(rs.getDate("NgayNhap"));
        return model;
    }
}
