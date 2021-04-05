
package poly.model;

import poly.frame.TrangChuJPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import poly.frame.HoaDonFrame;
import poly.frame.NhanVienFrame;
import poly.frame.NhatKiNhapSachJFrame;
import poly.frame.TacGiaFrame;
import poly.frame.sach;
import poly.helper.ShareHelper;


/**
 *
 * @author Admin
 */
public class Main_ManHinhController {
    
    private JPanel root;
    private String kindSelected = "";
    
    private List<DanhMucMain> listItem = null;

    public Main_ManHinhController(JPanel jpnView) {
        this.root = jpnView;
    }
    
    public void setView(JPanel jpnItem, JLabel jlbItem){
        kindSelected = "Main_2";
        jpnItem.setBackground(new Color(255, 255, 0));
        jlbItem.setBackground(new Color(255, 255, 0));
       
        
        root.removeAll();
        root.setLayout(new BorderLayout());
        root.add(new TrangChuJPanel());
        root.validate();
        root.repaint();
        
    }
    
    public void setEvent(List<DanhMucMain> listItem){
        this.listItem = listItem;
        for(DanhMucMain item : listItem){
            item.getJlb().addMouseListener(new LabelEvent(item.getKind(), item.getJlb()));
        }
        
    }
    
    class LabelEvent implements MouseListener {
        
        private JFrame node;
        
        private String kind;
        private JPanel jpnItem;
        private JLabel jlbItem;

        public LabelEvent(String kind, JLabel jlbItem) {
            this.kind = kind;
            
            this.jlbItem = jlbItem;
        }
        
        

        @Override
        public void mouseClicked(MouseEvent e) {
            switch(kind){
                case "Main_Sach":
                    node = new sach();
                    break;
                case "Main_NhanVien":
                    node = new NhanVienFrame();
                  
                    break;
                case "Main_NhatKi":
                    node = new NhatKiNhapSachJFrame();
                    break;
                case "Main_HoaDon":
                    node = new HoaDonFrame();
                    break;
                case "Main_TacGia":
                    node = new TacGiaFrame();
                    break;                  
                default:
                    break;
            }
            root.removeAll();
            root.setLayout(new BorderLayout());
            root.add(node);
            root.validate();
            root.repaint();
            setChangeBackground(kind);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            kindSelected = kind;
            
            jlbItem.setBackground(new Color(255, 255, 0));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
            jlbItem.setBackground(new Color(255, 255, 0));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if(kindSelected.equalsIgnoreCase(kind)){
                
                jlbItem.setBackground(new Color(76, 175, 80));
            }
        }
        
    }
    
    private void setChangeBackground(String kind){
        for(DanhMucMain item : listItem){
            if(item.getKind().equalsIgnoreCase(kind)){
                
                item.getJlb().setBackground(new Color(96, 100, 191));
            }else{
                
                item.getJlb().setBackground(new Color(76, 175, 80));
            }
        }
    }
    
}
