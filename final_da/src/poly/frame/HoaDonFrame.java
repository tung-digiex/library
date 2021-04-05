/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.frame;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import poly.dao.HoaDonDAO;
import poly.dao.SachDAO;
import poly.helper.DateHelper;
import poly.helper.DialogHelper;
import poly.helper.ShareHelper;
import poly.model.HoaDon;
import poly.model.Sach;
import javax.swing.JFileChooser;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import poly.helper.jdbcHelper;

/**
 *
 * @author admin
 */
public class HoaDonFrame extends javax.swing.JFrame {
    ResultSet rs;
    PreparedStatement ps;
    /**
     * Creates new form HoaDonFrame
     */
    public HoaDonFrame() {
        init();
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        try {
            String url = "Select DISTINCT tensach from sach Join loaisach on sach.maloai=loaisach.maloai";
            ps = jdbcHelper.prepareStatement(url);
            rs = ps.executeQuery();           
            while (rs.next()) {
                cboSach.addItem(rs.getString(1));
            }        
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi : Không thể kết nối đến máy chủ");
        }
        
        if(ShareHelper.USER.isChucVu()==false){
            tblHD.setVisible(false);
           
        }
        else{
          tblHD.setVisible(true);
             
        }
    }

    int index = 0;
    HoaDonDAO dao = new HoaDonDAO();
    SachDAO sachdao = new SachDAO();
    DefaultTableModel tblModel;
    
    void init() {
        setIconImage(ShareHelper.APP_ICON);
        
    }
    public boolean check() {
        if ( txtTenKH.getText().equals("") || txtGia.getText().equals("") ) {
            JOptionPane.showMessageDialog(rootPane, "Hãy nhập đủ dữ liệu sau đó ấn Save", "Error", 1);
            return false;
        } 
        return true;
    }
    
    
    void updateSL(){
        index = tblHD1.getRowCount();
        for(int i =0 ; i <index ;i++){
            String name = tblHD1.getValueAt(i, 0).toString();
            Sach s = sachdao.selectByName(name);
            int soluongmua = Integer.parseInt(tblHD1.getValueAt(i, 1).toString());
            int soluongcu = s.getSoLuong();
            int soluong = soluongcu - soluongmua;
            s.setSoLuong(soluong);
            sachdao.updateSL(s);
        }      
    }
    
    public void LoadTblFromDB1() {
        DefaultTableModel model = (DefaultTableModel) tblHD1.getModel();
        String name = cboSach.getSelectedItem().toString();        
        int price,quantity,total;
        try {
        Sach s = sachdao.selectByName(name);
        price = s.getGia();        
        quantity = Integer.parseInt(txtSoLuong.getText());
        total = price * quantity;
        Object[] row = {
            cboSach.getSelectedItem().toString(),
            txtSoLuong.getText(),
            s.getGia(),
            total
            };
            model.addRow(row);           
        }
        catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        } 
    }    
    
    public void tong(){
        int line = tblHD1.getRowCount();   
        int tong =0,tong1;
        for (int i = 0; i < line; i++) {
            tong1 = Integer.parseInt(tblHD1.getValueAt(i, 3).toString()); 
            tong = tong + tong1;
            lblThanhTien.setText(String.valueOf(tong));
            }
    }
    
    void load() {
        DefaultTableModel model = (DefaultTableModel) tblHD.getModel();
        model.setRowCount(0);
        try {
            List<HoaDon> list = dao.select1();            
            for (HoaDon hd : list) {
                Object[] row = {
                    hd.getMaHD(),
                    hd.getTenKH(),
                    hd.getMaSach(),
                    hd.getSoLuong(),
                    hd.getGia(),
                    hd.getMaNV(),
                    hd.getThanhTien(),
                    hd.getNgayLap()
                };
                model.addRow(row);
            }
     } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void insert() {
        HoaDon model = getModel();
            dao.insert(model);            
            String sql = "select top 1 MaHD from hoadon order by mahd desc";
            ResultSet rs = jdbcHelper.excuteQuery(sql);
        try {
            while(rs.next()){
                lblMaHD.setText(String.valueOf(rs.getInt("mahd")));                 
                index = tblHD1.getRowCount();
                    List<HoaDon> list = getModel1();
                    for(HoaDon hd : list){ 
                        dao.insert1(hd);
                    }                    
                }
            DialogHelper.alert(this, "Thêm mới thành công!");            
        load();   
        clear();
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Thêm mới thất bại!");
        }        
    }

    void update() {
        HoaDon model = getModel();
        try {
            dao.update(model);
            this.load();
            DialogHelper.alert(this, "Cập nhật thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert(this, "Cập nhật thất bại!");
        }
    }

    void delete() {
        if (DialogHelper.confirm(this, "Bạn thực sự muốn xóa hoá đơn này?")) {
            Integer mahd = Integer.parseInt(lblMaHD.getText());
            try {
                dao.delete(mahd);
                this.load();
                this.clear();
                DialogHelper.alert(this, "Xóa thành công!");
            } catch (Exception e) {
                DialogHelper.alert(this, "Xóa thất bại!");
            }
        }
    }

    void clear() {
        DefaultTableModel model = (DefaultTableModel) tblHD1.getModel();
        model.setRowCount(0);
        lblMaHD.setText("");
        txtTenKH.setText("");
        txtSoLuong.setText("");            
        lblMaV.setText(ShareHelper.USER.getMaNV());
        txtNgayLap.setText("");
        lblThanhTien.setText("0");
        txtTienNhan.setText("");
        txtTienThoi.setText("");
                
    }

    void edit() {
        try {
            Integer mahd= (Integer) tblHD.getValueAt(this.index, 0);
            HoaDon model = dao.findById(mahd);
            if (model != null) {
                this.setModel(model);
//                this.setStatus(false);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
        }
    }

    void setModel(HoaDon model) {
        
//      lblMaHD.setText(String.valueOf(model.getMaHD()));
        txtTenKH.setText(model.getTenKH());
        cboSach.setSelectedItem(String.valueOf(sachdao.findById(model.getMaSach())));      
        lblSoLuongCon.setText(String.valueOf(sachdao.findById(model.getSoLuong())));
        txtGia.setText(String.valueOf(model.getGia()));
        lblMaV.setText(ShareHelper.USER.getMaNV());
        lblThanhTien.setText(String.valueOf(model.getThanhTien()));        
    }
    
    HoaDon getModel() {
        HoaDon model = new HoaDon();        
        model.setTenKH(txtTenKH.getText());
        model.setMaNV(ShareHelper.USER.getMaNV());
        return model;
    }
    List<HoaDon> getModel1() {                
        index = tblHD1.getRowCount();
        List<HoaDon> list = new ArrayList<>(); 
        for(int i =0; i < index; i++){  
            HoaDon model = new HoaDon();
            String name = tblHD1.getValueAt(i, 0).toString();
            Sach s = sachdao.selectByName(name);
            model.setMaHD(Integer.parseInt(lblMaHD.getText()));
            model.setTenKH(txtTenKH.getText());
            model.setMaSach(s.getMaSach());                        
            model.setSoLuong(Integer.parseInt(tblHD1.getValueAt(i, 1).toString()));
            model.setGia(Integer.valueOf(tblHD1.getValueAt(i, 2).toString()));            
            model.setNgayLap(DateHelper.toDate(txtNgayLap.getText()));
            model.setThanhTien(Integer.parseInt(tblHD1.getValueAt(i, 3).toString()));
            list.add(model);
        }
       return list; 
    }
   

    void clickM(){
        index =tblHD.getSelectedRow();
        lblMaHD.setText(tblHD.getValueAt(index, 0).toString());//lấy hàng được chọn, lấy dữ liệu hàng hiện tại đưa vào cột thứ 0 set lên textfield
        txtTenKH.setText(tblHD.getValueAt(index,1).toString());
        cboSach.setSelectedItem(tblHD.getValueAt(index, 2).toString());
        txtSoLuong.setText(tblHD.getValueAt(index,3).toString());
        txtGia.setText(tblHD.getValueAt(index,4).toString());        
        lblMaV.setText(tblHD.getValueAt(index, 5).toString());
        lblThanhTien.setText(tblHD.getValueAt(index, 6).toString());       
    }
     
     void selectComboBox() {
        String name = cboSach.getSelectedItem().toString();
        Sach s = sachdao.selectByName(name);       
        txtGia.setText(String.valueOf(s.getGia()));
        txtSoLuong.setText("");
        lblSoLuongCon.setText(String.valueOf(s.getSoLuong()));
        int line = tblHD1.getRowCount();
        for (int i = 0; i < line; i++) {                
            if (tblHD1.getValueAt(i, 0).toString().equals(cboSach.getSelectedItem().toString())) {                
                int soluong = Integer.parseInt(tblHD1.getValueAt(i, 1).toString());
                int soluongcon = Integer.parseInt(lblSoLuongCon.getText())-soluong;
                lblSoLuongCon.setText(String.valueOf(soluongcon));
            }           
        }    
    }
    

//    void fillComboBox() {
//        DefaultComboBoxModel model = (DefaultComboBoxModel) cboSach.getModel();
//        model.removeAllElements();
//        try {
//            List<Sach> list = sachdao.select();
//            for (Sach s : list) {
//                model.addElement(s);
//            }
//        } catch (Exception e) {
//            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
//        }
//    }


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        txtTenKH = new javax.swing.JTextField();
        lblMaHD = new javax.swing.JLabel();
        lblMa = new javax.swing.JLabel();
        lblTen = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cboSach = new javax.swing.JComboBox<>();
        lblSoLuong = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        lblGia = new javax.swing.JLabel();
        lblMaNV = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblThanhTien = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        lblSoLuongCon = new javax.swing.JLabel();
        lblNgayLap = new javax.swing.JLabel();
        txtNgayLap = new javax.swing.JTextField();
        lblTienNhan = new javax.swing.JLabel();
        txtTienNhan = new javax.swing.JTextField();
        lblTienThoi = new javax.swing.JLabel();
        txtTienThoi = new javax.swing.JTextField();
        lblMaV = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnInsert = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblHD1 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        lblTD2 = new javax.swing.JLabel();
        btnIn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHD = new javax.swing.JTable();
        btnFirst = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jPanel3.setBackground(new java.awt.Color(51, 0, 102));

        jPanel4.setBackground(new java.awt.Color(0, 102, 51));

        lblMaHD.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblMaHD.setForeground(new java.awt.Color(255, 255, 255));

        lblMa.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblMa.setForeground(new java.awt.Color(255, 255, 255));
        lblMa.setText("Mã HD");

        lblTen.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblTen.setForeground(new java.awt.Color(255, 255, 255));
        lblTen.setText("Tên KH");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Mã sách");

        cboSach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSachActionPerformed(evt);
            }
        });

        lblSoLuong.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblSoLuong.setForeground(new java.awt.Color(255, 255, 255));
        lblSoLuong.setText("Số lượng");

        lblGia.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblGia.setForeground(new java.awt.Color(255, 255, 255));
        lblGia.setText("Giá");

        lblMaNV.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblMaNV.setForeground(new java.awt.Color(255, 255, 255));
        lblMaNV.setText("Mã NV");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Thành Tiền");

        lblThanhTien.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblThanhTien.setForeground(new java.awt.Color(255, 255, 255));
        lblThanhTien.setText("0");
        lblThanhTien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThanhTienMouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Số lượng còn");

        lblSoLuongCon.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        lblSoLuongCon.setForeground(new java.awt.Color(255, 255, 255));
        lblSoLuongCon.setText("0");

        lblNgayLap.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblNgayLap.setForeground(new java.awt.Color(255, 255, 255));
        lblNgayLap.setText("Ngày lập");

        lblTienNhan.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblTienNhan.setForeground(new java.awt.Color(255, 255, 255));
        lblTienNhan.setText("Tiền khách đưa");

        txtTienNhan.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtTienNhanInputMethodTextChanged(evt);
            }
        });

        lblTienThoi.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblTienThoi.setForeground(new java.awt.Color(255, 255, 255));
        lblTienThoi.setText("Tiền trả lại");

        lblMaV.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblMaV.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblMa, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTen, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblGia, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtGia, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cboSach, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtSoLuong, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblSoLuongCon, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addComponent(lblTienThoi, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(txtTienThoi, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(lblTienNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(txtTienNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addComponent(lblNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(txtNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                            .addComponent(lblMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblMaV, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(140, 140, 140)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(lblThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(40, 40, 40))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblMaV, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTen, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboSach, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblThanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblSoLuongCon, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTienNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTienNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTienThoi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTienThoi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblGia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(0, 0, 204));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/images/a_delete.png"))); // NOI18N
        btnDelete.setText("Xoá");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(0, 0, 204));
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/images/a_up.png"))); // NOI18N
        btnUpdate.setText("Cập nhật");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnInsert.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnInsert.setForeground(new java.awt.Color(0, 0, 204));
        btnInsert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/images/a_save.png"))); // NOI18N
        btnInsert.setText("Lưu");
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        btnThem.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnThem.setForeground(new java.awt.Color(0, 0, 204));
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/images/a_add.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        tblHD1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Sách", "Số Lượng", "Giá", "Thành Tiền"
            }
        ));
        tblHD1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHD1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblHD1);

        jPanel8.setBackground(new java.awt.Color(204, 0, 0));

        lblTD2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTD2.setForeground(new java.awt.Color(255, 255, 255));
        lblTD2.setText("QUẢN LÝ HOÁ ĐƠN");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(lblTD2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTD2)
                .addGap(13, 13, 13))
        );

        btnIn.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnIn.setForeground(new java.awt.Color(0, 0, 204));
        btnIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/images/a_print.png"))); // NOI18N
        btnIn.setText("In HĐ");
        btnIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 204));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/images/a_cal.png"))); // NOI18N
        jButton2.setText("Tính tiền");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnMoi.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnMoi.setForeground(new java.awt.Color(0, 0, 204));
        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/images/a_rs.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(339, 339, 339)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnIn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Hoá đơn", jPanel1);

        jPanel2.setBackground(new java.awt.Color(51, 0, 102));

        tblHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã HĐ", "Tên KH", "Mã Sách", "Số Lượng", "Giá", "Mã NV  ", "Thành Tiền", "Ngày Lập"
            }
        ));
        tblHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHDMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHD);

        btnFirst.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/images/firstt.png"))); // NOI18N
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/images/backk.png"))); // NOI18N
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/images/nextt.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/images/lastt.png"))); // NOI18N
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(287, 287, 287))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(310, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Chi tiết", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
                                 
        try {
            if (check()) {
                this.updateSL();
                this.insert();  
            }
        } catch (Exception e) {
            e.printStackTrace();       
        }
    }//GEN-LAST:event_btnInsertActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblHD1.getModel();
        int line = tblHD1.getSelectedRow();
        tblHD1.setValueAt(cboSach.getSelectedItem(), line, 0);
        tblHD1.setValueAt(txtSoLuong.getText(), line, 1);
        tblHD1.setValueAt(txtGia.getText(), line, 2);
        String name = cboSach.getSelectedItem().toString();
        Sach s = sachdao.selectByName(name); 
        lblSoLuongCon.setText(String.valueOf(s.getSoLuong()));
        int soluongcon1 = Integer.parseInt(lblSoLuongCon.getText());
        int soluongcon = soluongcon1 - Integer.parseInt(tblHD1.getValueAt(line, 1).toString());
        lblSoLuongCon.setText(String.valueOf(soluongcon));
        LoadTblFromDB1();
        model.removeRow(line);
        tong();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblHD1.getModel();
        int line = tblHD1.getSelectedRow();
        model.removeRow(line);        
        tong();
        this.selectComboBox();
        txtSoLuong.setText("0");
        if (tblHD1.getRowCount() > 0) {
            tong();
            this.selectComboBox();
            txtSoLuong.setText("0");
        } else {
            lblThanhTien.setText("0");
        }                
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        try{
            index=0;
            tblHD.setRowSelectionInterval(index, index);
            clickM();        
        }
        catch(Exception e){
           e.printStackTrace();
        }
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        if(index >0){ 
            index --;
            tblHD.setRowSelectionInterval(index, index);
            clickM();
        }
        else{
            JOptionPane.showMessageDialog(null,"Đầu danh sách rồi");
        }
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        if(index < tblHD.getRowCount() - 1){
            index++;
            tblHD.setRowSelectionInterval(index, index);
            clickM();
            
        }
        else{
            JOptionPane.showMessageDialog(null,"Cuối danh sách rồi");
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // TODO add your handling code here:
        index = tblHD.getRowCount() - 1;
        tblHD.setRowSelectionInterval(index, index);
        clickM();
    }//GEN-LAST:event_btnLastActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        //fillComboBox();
        this.load();
        clear();
        
    }//GEN-LAST:event_formWindowOpened

    private void tblHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHDMouseClicked
        // TODO add your handling code here:
        index = tblHD.getSelectedRow();          
         clickM();
    }//GEN-LAST:event_tblHDMouseClicked

    private void cboSachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSachActionPerformed
        // TODO add your handling code here:
        this.selectComboBox();
    }//GEN-LAST:event_cboSachActionPerformed

    private void lblThanhTienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThanhTienMouseClicked
        // TODO add your handling code here:
         int tong;
        int gia = Integer.parseInt(txtGia.getText());
        int sl = Integer.parseInt(txtSoLuong.getText());              
            tong = gia*sl;        
        lblThanhTien.setText(String.valueOf(tong));
    }//GEN-LAST:event_lblThanhTienMouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        int soluongcon; 
        if(Integer.parseInt(txtSoLuong.getText())<=Integer.parseInt(lblSoLuongCon.getText())){
            int line = tblHD1.getRowCount();
            int quanCu,quanMoi,quanTotal;
            int thanhtien,gia;                       
                for (int i = 0; i < line; i++) { 
                    if (tblHD1.getValueAt(i, 0).toString().equals(cboSach.getSelectedItem().toString())) {                
                        quanCu = Integer.parseInt(tblHD1.getValueAt(i, 1).toString());
                        quanMoi = Integer.parseInt(txtSoLuong.getText());
                        quanTotal=quanCu+quanMoi;
                        txtSoLuong.setText(String.valueOf(quanTotal));                
                        int soluongmua = quanTotal;
                        soluongcon = Integer.parseInt(lblSoLuongCon.getText());
                        if(quanMoi <= soluongcon){                            
                            tblHD1.setValueAt(quanTotal, i, 1);                
                            gia=Integer.parseInt(txtGia.getText());
                            thanhtien = gia*quanTotal;
                            tblHD1.setValueAt(thanhtien, i, 3);
                            DefaultTableModel model = (DefaultTableModel) tblHD1.getModel();
                            model.removeRow(i);
                            break;
                        }else{
                            DialogHelper.alert(this, "Không đủ số lượng yêu cầu!");                            }              
                        }                                                 
                        }
                        soluongcon = Integer.parseInt(lblSoLuongCon.getText())-Integer.parseInt(txtSoLuong.getText());
                        if(soluongcon >=0){
                            lblSoLuongCon.setText(String.valueOf(soluongcon)) ;
                        }else{
                            lblSoLuongCon.setText("0");
                        }                        
                        LoadTblFromDB1();
                        tong();                        
                        txtSoLuong.setText("");                        
        }else{
            DialogHelper.alert(this, "Không đủ số lượng yêu cầu!");
            txtSoLuong.setText("");
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInActionPerformed
        // TODO add your handling code here:
        
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            Date now = new Date();
            JFileChooser fc= new JFileChooser();
            fc.showSaveDialog(fc);
            File f = fc.getSelectedFile();
            
            // Tạo đối tượng PdfWriter
            PdfWriter.getInstance(document, new FileOutputStream(f));

            // Mở file để thực hiện ghi
            document.open();
            Font font = new Font(BaseFont.createFont("C:\\Users\\Admin\\Desktop/vuArial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
            font.setSize(10);
            font.setStyle(Font.NORMAL);
            // Thêm nội dung sử dụng add function
            document.add(new Paragraph("   Nhà sách Thanh An       ",font));
            document.add(new Paragraph("   Phòng 504, Innovation, CVPM Quang Trung",font));
            document.add(new Paragraph("   SĐT: 0123456789",font));
            document.add(new Paragraph("                        HÓA ĐƠN BÁN HÀNG",font));
            document.add(new Paragraph("   Mã hóa đơn: " + lblMaHD.getText(),font));
            document.add(new Paragraph("   Thời gian: " + DateHelper.now(),font));
            document.add(new Paragraph("   Nhân viên: " + lblMaV.getText(),font));
            document.add(new Paragraph("   ------------------------------------------------------------",font));
            document.add(new Paragraph("   Mã Sách     Số lượng   Đơn giá   Thành tiền",font));
            document.add(new Paragraph("   ------------------------------------------------------------",font));
            //Ghi sản phẩm
            index = tblHD1.getRowCount();
            int quantotal = 0;
            for (int i = 0; i < index; i++) {                
                String name = (String) tblHD1.getValueAt(i, 0);
                Sach s = sachdao.selectByName(name);
                int masach = s.getMaSach();
                int soluong = Integer.parseInt(tblHD1.getValueAt(i, 1).toString());
                int gia = Integer.parseInt(tblHD1.getValueAt(i, 2).toString());                
                int thanhtien = Integer.parseInt(tblHD1.getValueAt(i, 3).toString());
                System.out.println(masach+" "+soluong+" "+gia+" "+thanhtien);
                document.add(new Paragraph("   "+(i + 1) + ".  " + name,font));
                
       document.add(new Paragraph("   "+masach + "    " + soluong + "       " + gia + "       "+thanhtien ,font));
               
            }
            document.add(new Paragraph("   -------------------------------------------------------------\r\n",font));
            document.add(new Paragraph("   Tổng cộng: " + lblThanhTien.getText() + "  VNĐ",font));           
            document.add(new Paragraph("   -------------------------------------------------------------",font));
            document.add(new Paragraph("   Thành tiền: " + lblThanhTien.getText() + "  VNĐ",font));
            document.add(new Paragraph("   -------------------------------------------------------------",font));
            document.add(new Paragraph("   Tiền khách đưa: " + txtTienNhan.getText() + "  VNĐ",font));
            document.add(new Paragraph("   Tiền trả lại: " + txtTienThoi.getText() +  " VNĐ",font));
            document.add(new Paragraph("   -------------------------------------------------------------",font));         
            document.add(new Paragraph("   -------------------------------------------------------------",font));
            document.add(new Paragraph("   ---------------------CẢM ƠN QUÝ KHÁCH!-----------------------",font));
              // Đóng File
            document.close();
            JOptionPane.showMessageDialog(null, "In hóa đơn thành công");
            String cmds[] = new String[] {"cmd", "/c", "C:\\Users\\Admin\\Desktop\\HoaDon\\"+ lblMaHD.getText()+".pdf"};///// ////
                try {
                    Runtime.getRuntime().exec(cmds);
                }
                catch(Exception e){
                    
                }            
        } catch (IOException ex) {
            Logger.getLogger(HoaDonFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(HoaDonFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
                   
    }//GEN-LAST:event_btnInActionPerformed

    private void tblHD1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHD1MouseClicked
        // TODO add your handling code here:
        this.selectComboBox();        
        int line = tblHD1.getSelectedRow();
        int soluong,soluongcon,soluongcu;        
        cboSach.setSelectedItem(String.valueOf(tblHD1.getValueAt(line, 0)));
        txtSoLuong.setText(String.valueOf(tblHD1.getValueAt(line, 1)));
        soluong = Integer.parseInt(txtSoLuong.getText());
        soluongcon = Integer.parseInt(lblSoLuongCon.getText())-soluong;
        if(soluongcon > 0){
            lblSoLuongCon.setText(String.valueOf(soluongcon));
        }else{
            lblSoLuongCon.setText("0");
        }
    }//GEN-LAST:event_tblHD1MouseClicked

    private void txtTienNhanInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtTienNhanInputMethodTextChanged
        // TODO add your handling code here:        
        
    }//GEN-LAST:event_txtTienNhanInputMethodTextChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int tiennhan = Integer.parseInt(txtTienNhan.getText());
        int thanhtien = Integer.parseInt(lblThanhTien.getText());
        int tienthoi = tiennhan - thanhtien;
        txtTienThoi.setText(String.valueOf(tienthoi));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_btnMoiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HoaDonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HoaDonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HoaDonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HoaDonFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HoaDonFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnIn;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cboSach;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblGia;
    private javax.swing.JLabel lblMa;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblMaV;
    private javax.swing.JLabel lblNgayLap;
    private javax.swing.JLabel lblSoLuong;
    private javax.swing.JLabel lblSoLuongCon;
    private javax.swing.JLabel lblTD2;
    private javax.swing.JLabel lblTen;
    private javax.swing.JLabel lblThanhTien;
    private javax.swing.JLabel lblTienNhan;
    private javax.swing.JLabel lblTienThoi;
    private javax.swing.JTable tblHD;
    private javax.swing.JTable tblHD1;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtNgayLap;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtTienNhan;
    private javax.swing.JTextField txtTienThoi;
    // End of variables declaration//GEN-END:variables
}
