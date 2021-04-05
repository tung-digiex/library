/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.helper;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author admin
 */
public class DialogHelper {
    
    /**
     * Hien thi thong bao nguoi dung
     * @param parent la cua so chua thong bao
     * @param message la thong bao
     */
    public static void alert(Component parent, String message){
        JOptionPane.showMessageDialog(parent, message, "Hệ thống quản lý nhà sách", JOptionPane.INFORMATION_MESSAGE);
               
    }
    
    /**
     * Hien thi thong bao ve yeu cau nguoi dung xac nhan
     * @param parent la cua so chua thong bao
     * @param message la ket qua nhan duoc true/false
     * @return 
     */
    public static boolean confirm(Component parent, String message){
        int result = JOptionPane.showConfirmDialog( parent,message, "Hệ thống quản lý nhà sách", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * hien thi thong bao yeu cau nhap du lieu
     * @param parent la cua so chua thong bao
     * @param message la ket qua nhan duoc tu nguoi su dung nhap vao
     * @return 
     */
    public static String prompt(Component parent, String message){
        return JOptionPane.showInputDialog(parent, message, "Hệ thống quản lý nhà sách", JOptionPane.INFORMATION_MESSAGE);
    }
}
