/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author admin
 */
public class jdbcHelper {
    private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String dburl = "jdbc:sqlserver://localhost:1433;databaseName=QLY_SACH";
    private static String username ="sa";
    private static String password ="123456";
    
    /*
    * Nap driver
    */
    static{
        try{
            Class.forName(driver);
        }
        catch(ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }
    /**
     * Xay dung PreparedStatement
     * @param sql la cau lenh SQL chua co the chua tham so, co the la mot loi goi thu tuc luu
     * @param args la danh sach cac gia tri duoc cung cap cho cac tham so trong cau lenh sql
     * @return PreparedStatement tao duoc
     * @throws SQLException loi sai cu phap
     */
    public static PreparedStatement prepareStatement(String sql, Object...args) throws SQLException{
        Connection con = DriverManager.getConnection(dburl, username, password);
        PreparedStatement pstmt = null;
        
        if(sql.trim().startsWith("{")){
            pstmt = con.prepareCall(sql);
        }
        else{
            pstmt = con.prepareStatement(sql);
        }
        for(int i=0; i<args.length; i++){
            pstmt.setObject(i+1,args[i]);
        }
        return pstmt;
    }
    
    /**
     * Thuc hien cau lenh SQL thao tac (insert, update,delete) hoac thu tuc luu thao tac du lieu
     * @param sql la cau lenh SQL chua co the chua tham so, co the la mot loi goi thu tuc luu
     * @param args la danh sach cac gia tri duoc cung cap cho cac tham so trong cau lenh sql
     */
    public static void excuteUpdate(String sql, Object...args){
        try{
            PreparedStatement stmt = prepareStatement(sql, args);
            try{
                stmt.executeUpdate();
            }
            finally{
                stmt.getConnection().close();
            }
        }
        catch(Exception e){
            
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Thuc hien cau lenh SQL truy van(SELECT) hoac thu tuc luu truy van du lieu
     * @param sql 
     * @param args
     * @return 
     */
    public static ResultSet excuteQuery(String sql, Object...args){
        try{
            PreparedStatement stmt = prepareStatement(sql, args);
            return stmt.executeQuery();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
