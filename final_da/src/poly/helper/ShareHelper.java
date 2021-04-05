/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.helper;


import java.awt.Image;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;
import poly.model.NhanVien;

/**
 *
 * @author admin
 */
public class ShareHelper {
    
    /**
     * Anh bieu tuong cua ung dung, xuat hien tren moi cua so
     */
    public static final Image APP_ICON;
    static{
        //Tai bieu tuong ung dung
        String file = "/poly/images/bg.jpg";
        APP_ICON = new ImageIcon(ShareHelper.class.getResource(file)).getImage();
    }
    
    /**
     * Sao chep file logo chuyen de vao thu muc logo
     * @param file la doi tuong file anh
     * @return chep duoc hay khong
     */
    public static boolean saveLogo(File file){
        File dir = new File("logos");
        //Tao thu muc neu chua ton tai
        if(!dir.exists()){
            dir.mkdirs();
        }
        File newFile = new File(dir,file.getName());
        try{
            //Copy vao thu muc logos(de neu da ton tai)
            Path source = Paths.get(file.getAbsolutePath());
            Path destination = Paths.get(newFile.getAbsolutePath());
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
    
    /**
     * Doc hinh anh logo chuyen de
     * @param fileName la ten file logo
     * @return anh duoc doc
     */
    public static ImageIcon readLogo(String fileName){
        File path  = new File("logos", fileName);
        return new ImageIcon(path.getAbsolutePath());
    }
    
    /**
     * Doi tuong nay chua thong tin nguoi su dung sau khi dang nhap
     */
    public static NhanVien USER = null;
    
    /**
     * Xoa thong tin cua nguoi su dung khi co yeu cau dang xuat
     */
    public static void logoff(){
        ShareHelper.USER = null;
    }
    
    /**
     * Kiem tra xem dang nhap chua
     * @return 
     */
    public static boolean authenticated(){
        return ShareHelper.USER != null;
    }
}
