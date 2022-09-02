/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
/**
 *
 * @author adnan
 */
public class NewPurchase extends javax.swing.JInternalFrame {
int tree = 0;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int unitid=0;
    int supid=0;
    //  Dashboard dashboard = new Dashboard();
    int userid = 0;
    int updatekey=0;
    int view=0;
    
   int updatedeletekey=0;
   int modifiaction=0;
   float minusAmount;
   
   //supplier
   
    double openingbalance;
    double consighnmentamnt;
    double Balancedue;
    String suplyer = null;
    float includevat;
     
    String colorid="1";
    String sizeid="1";
    String barcode=null;
    String unitrate;
    String mrpvalue; 
    String catid;
    String scatid;
    String itemcode;
    String proitemcode;
    String itemname;
    
    
    
    float itemmrp;
    String itemUnit;
    
    // definitions
 //private DaemonThread myThread = null;
    int count = 0;
    
    
    
    String barcodenamefromat=null;
    String barcodemrp=null;
    String barcodecomment=null;
            
   // VideoCapture webSource = null;

   // Mat frame = new Mat();admin
   // MatOfByte mem = new MatOfByte();
    /**
     * Creates new form NewPurchase
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public NewPurchase() throws IOException, SQLException {
        initComponents();
        
        conn = Java_Connect.conectrDB();
        userkey();
        AutoCompleteDecorator.decorate(itemnamesearch);
        AutoCompleteDecorator.decorate(sizebox);
        AutoCompleteDecorator.decorate(allcolorbox);
        
        AutoCompleteDecorator.decorate(supliyerbox);
        AutoCompleteDecorator.decorate(categorybox);
        AutoCompleteDecorator.decorate(subcategorybox);
        Item();
        unit();
        color();
        Itemsize();
        Item_code();
        category();
        suppliyer();
        parchase_code();
        currentDate();
        accsessModification();
        Deletebtnactivate();
        ItemCode();
        ItementryactiveDiactive();
        printaccess.setSelected(true);
        
    }
  public NewPurchase(String table_click,int activemodification) throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
         userkey();
         AutoCompleteDecorator.decorate(itemnamesearch);
        AutoCompleteDecorator.decorate(sizebox);
        AutoCompleteDecorator.decorate(allcolorbox);
        
        AutoCompleteDecorator.decorate(supliyerbox);
        AutoCompleteDecorator.decorate(categorybox);
        AutoCompleteDecorator.decorate(subcategorybox);
       
        Item();
        unit();
        color();
         Itemsize();
        category();
        currentDate();
        PurchaseDetailsPR(table_click);
        parchaseText.setText(table_click);
        view=1;
        modifiaction=activemodification;
        suppliyer();
        accsessModification();
        
        updatedeletekey=1;
        Deletebtnactivate();
       ItemCode();
       ItementryactiveDiactive();
        
    }
     private void ItemCode() {
        try {
           
            int new_inv = 1;
            String NewParchaseCode = (+new_inv + "");
            String sql = "Select id from item";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = (ParchaseCode);
            }
           // String finelcode = NewParchaseCode + Itemcodedate;
            String finelcode = NewParchaseCode;
            itemcodetext.setText(finelcode);
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
  
  /////////////////////////////////////////////////////////////////////
  /*class DaemonThread implements Runnable
    {
    protected volatile boolean runnable = false;

    @Override
    public  void run()
    {
        synchronized(this)
        {
            while(runnable)
            {
                if(webSource.grab())
                {
		    	try
                        {
                            webSource.retrieve(frame);
			    Highgui.imencode(".bmp", frame, mem);
			    Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));

			    BufferedImage buff = (BufferedImage) im;
			    Graphics g=camerapanel.getGraphics();

			    if (g.drawImage(buff, 0, 0, 450, 392 , 0, 0, buff.getWidth(), buff.getHeight(), null))
			    
			    if(runnable == false)
                            {
			    	//System.out.println("Going to wait()");
			    	this.wait();
			    }
			 }
			 catch(IOException | InterruptedException ex)
                         {
			   // System.out.println("Error");
                         }
                }
            }
        }
     }
   }
     
     */
  
       private void userkey() throws  IOException, SQLException {
        FileInputStream fis = new FileInputStream("src\\userkey.properties");
        Properties p = new Properties();
        p.load(fis);

        String userids = (String) p.get("userid");
        userid=Integer.parseInt(userids);
        // this.dispose();
        //LoginAccess desboard=new LoginAccess();
    }
      private void Deletebtnactivate(){
      if(updatedeletekey==1){
      
      deletebtn.setEnabled(true);
      
      
      
      }else{
      
       deletebtn.setEnabled(false);
      
      }
      
      
      
      }
       private void accsessModification() {
        try {
            String sql = "Select purcedit,purcdelete from modificationaccsess where userid='" + userid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                int purcedit = rs.getInt("purcedit");
                int purcdelete = rs.getInt("purcdelete");

                if (	purcedit == 1) {

                    svbtn.setEnabled(true);

                } else {

                    svbtn.setEnabled(false);

                }
                if (purcdelete == 1) {

                    deletebtn.setEnabled(true);

                } else {

                    deletebtn.setEnabled(false);

                }

            } else {
                svbtn.setEnabled(false);
                deletebtn.setEnabled(false);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
       
        private void color() throws SQLException {

        try {
            String sql = "Select name from color";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("name");
                allcolorbox.addItem(name);
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
        
          private void Itemsize() throws SQLException {

        try {
            String sql = "Select size from itemsize";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("size");
                sizebox.addItem(name);
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
         private void category() throws SQLException {

        try {
            String sql = "Select name from category";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                
                String category = rs.getString("name");
                categorybox.addItem(category);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

       private static final DecimalFormat df2 = new DecimalFormat("#.00");
       private void PurchaseDetailsPR(String table_click) throws SQLException {
       DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
          
          try {

            String sql = "Select pr.prcode as 'id',barcode,color,size,(select ita.nameformat from barcodeproduct ita where ita.barcode=pr.barcode) as 'Itemname',unitrate, qty,unit,total,discount,vat,totalvat,nettotal,picture,comment from purchasedetails pr  where pr.purchaseCode='" + table_click + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            
           while(rs.next()){
               tree++;
           String id=rs.getString("id");
           itemname=rs.getString("Itemname");
           float unitrates=rs.getFloat("unitrate");
           float qty=rs.getFloat("qty");
           
           float total=rs.getFloat("total");
           float discount=rs.getFloat("discount");
           
           float totalvat=rs.getFloat("totalvat");
           float nettotal=rs.getFloat("nettotal");
           String probarcode=rs.getString("barcode");
           String color=rs.getString("color");
           String size=rs.getString("size");
           String comment=rs.getString("comment");
            byte[] ImageData = rs.getBytes("picture");
            
           
           model2.addRow(new Object[]{tree,id,probarcode,itemname,color,size,unitrates,qty,discount,total,totalvat,nettotal,ImageData,comment});
           
           
           
           }
          //  dataTable.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {

            String sql = "Select purchaseCode,Totalrate,totalvat,nettotal,supliyer,pdate,remark,(select sup.supliyername from suplyierinfo sup where sup.id=par.supliyer) as'supliyerinfo'  from purchase par  where par.purchaseCode='" + table_click + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            if (rs.next()) {
 /*
                String TotalRate = rs.getString("Totalrate");
                totalrate.setText(TotalRate);
                String totalvat = rs.getString("totalvat");
                totalvattext.setText(totalvat);
                String nettotal = rs.getString("nettotal");
                nettotaltext.setText(nettotal);
               
                */
                
                String ParchseNo = rs.getString("purchaseCode");
                parchaseText.setText(ParchseNo);
                supid=rs.getInt("supliyer");
                String suppliyer = rs.getString("supliyerinfo");
                supliyerbox.setSelectedItem(suppliyer);
                
                Date inutDate=rs.getDate("pdate");
                parchasedate.setDate(inutDate);
                String remark=rs.getString("remark");
                 remarktext.setText(remark);       
                

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        totalrate.setText(df2.format(total_action_mrp()));
        netdiscounttext.setText(df2.format(total_action_discount()));
        totalvattext.setText(df2.format(total_action_vat()));
        nettotaltext.setText(df2.format(total_action_nettotal()));
    }
    
        private void deleterow() {

        int[] toDelete = dataTable.getSelectedRows();
        Arrays.sort(toDelete); // be shure to have them in ascending order.
        DefaultTableModel myTableModel = (DefaultTableModel) dataTable.getModel();
        for (int ii = toDelete.length - 1; ii >= 0; ii--) {
            myTableModel.removeRow(toDelete[ii]); // beginning at the largest.
        }
        totalrate.setText(df2.format(total_action_mrp()));
        netdiscounttext.setText(df2.format(total_action_discount()));
        totalvattext.setText(df2.format(total_action_vat()));
        nettotaltext.setText(df2.format(total_action_nettotal()));
  
        clear();
        updatekey=0;
    }
   private void enter() {
       java.awt.event.KeyEvent evt = null;
      if (evt.getKeyCode() != KeyEvent.VK_F5) {

        }else{
    
          finelEntry();
        
        }
   
   
   }
  private void currentDate(){
  java.util.Date now = new java.util.Date();
  java.sql.Date date = new java.sql.Date(now.getTime());
  parchasedate.setDate(date);
  
  }

    
    private void parchase_code() {
        try {
            int new_inv = 1;
            String NewParchaseCode = ("PR-1" + new_inv + "");
           // String ParchaseCode =String.format("%08d",new_inv);
            String sql = "Select id from purchase";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                //String ParchaseCode = Integer.toString(new_inv);
                
            }
String ParchaseCode =String.format("%08d",new_inv);
                NewParchaseCode = ("PR-1" + ParchaseCode + "");
                parchaseText.setText(NewParchaseCode);
            
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void parchaseInsert() {

        try {

            String sql = "Insert into purchase(purchaseCode,pdate,supliyer,Totalrate,totalvat,nettotal,	totaldiscount,remark,status,GRNstatus,inputuser) values (?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, parchaseText.getText());
            pst.setString(2, ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText());
            pst.setInt(3, supid);
            pst.setString(4, totalrate.getText());
            pst.setString(5, totalvattext.getText());
            pst.setString(6, nettotaltext.getText());
            pst.setString(7, netdiscounttext.getText());
            pst.setString(8, remarktext.getText());
            pst.setString(9, "1");
            pst.setInt(10, 0);
            pst.setInt(11, userid);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Saved");
            
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void parcheDetailsInsert() throws SQLException, IOException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String procode = (String) dataTable.getValueAt(row, 1);
                 try {
                    String sql = "Select itemcode,mrp,stockunit,includevate from item where itemcode='" + procode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {
                         
                        itemmrp=rs.getFloat("mrp");
                        itemUnit=rs.getString("stockunit");
                        includevat=rs.getFloat("includevate");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                String probarcode=(String) dataTable.getValueAt(row, 2);
                String color=(String) dataTable.getValueAt(row, 4);
                String size=(String) dataTable.getValueAt(row, 5);
                float rate = (Float) dataTable.getValueAt(row, 6);
                //float mrp = (Float) dataTable.getValueAt(row, 7);
                float qty = (Float) dataTable.getValueAt(row, 7);
                //String unit = (String) dataTable.getValueAt(row, 9);
                float discount =(Float) dataTable.getValueAt(row, 8); 
                float total =  (Float) dataTable.getValueAt(row, 9);
                float totalvat =  (Float) dataTable.getValueAt(row, 10);
                float nettotal =  (Float) dataTable.getValueAt(row, 11);
                byte[] ImageData  = (byte[]) dataTable.getValueAt(row, 12);
                float itemvat=totalvat/qty;
                String comment=(String) dataTable.getValueAt(row, 13);
                try {

                    String sql = "Insert into purchaseDetails(purchaseCode,prcode,barcode,color,size,unitrate,mrp,qty,unit,discount,total,vat,totalvat,nettotal,supid,picture,comment) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, parchaseText.getText());
                    pst.setString(2, procode);
                    pst.setString(3, probarcode);
                    pst.setString(4, color);
                    pst.setString(5, size);
                    pst.setFloat(6, rate);
                    pst.setFloat(7, itemmrp);
                    pst.setFloat(8, qty);
                    pst.setString(9, itemUnit);
                    pst.setFloat(10, discount);
                    pst.setFloat(11, total);
                    pst.setFloat(12, itemvat);
                    pst.setFloat(13, totalvat);
                    pst.setFloat(14, nettotal);
                    pst.setInt(15,supid);
                    pst.setBytes(16, ImageData);
                    pst.setString(17, comment);
                    pst.execute();
                    ///    examperticipationinsert(id,examcode,exam);

                     // 
                } catch (SQLException | HeadlessException e) {
                   JOptionPane.showMessageDialog(null, e);

                }

            }
            //  loadGRN();
            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
   
    
        private void Barcodeinsert() throws SQLException, IOException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String procode = (String) dataTable.getValueAt(row, 1);
                   try {
                    String sql = "Select itemcode,mrp,stockunit,includevate from item where itemcode='" + procode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {
                         
                        itemmrp=rs.getFloat("mrp");
                        //itemUnit=rs.getString("stockunit");
                        //includevat=rs.getFloat("includevate");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                String probarcode=(String) dataTable.getValueAt(row, 2);
                String itemnameformat=(String) dataTable.getValueAt(row, 3);
                String color=(String) dataTable.getValueAt(row, 4);
                String size=(String) dataTable.getValueAt(row, 5);
                float rate = (Float) dataTable.getValueAt(row, 6);
               // float mrp = (Float) dataTable.getValueAt(row, 7);
                byte[] ImageData  = (byte[]) dataTable.getValueAt(row, 12);
                String comment=(String) dataTable.getValueAt(row, 13);
                try {

                  //  String sql = "Insert into barcodeproduct(itemcode,barcode,color,size,tp,picture) values (?,?,?,?,?,?)";
                    String sql="Insert into barcodeproduct(itemcode,barcode,nameformat,color,size,tp,mrp,inputdate,inputuser,picture,comment) values (?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE tp='"+rate+"',nameformat='"+itemnameformat+"',size='"+size+"',mrp='"+itemmrp+"',comment='"+comment+"'";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, procode);
                    pst.setString(2, probarcode);
                    pst.setString(3, itemnameformat);
                    pst.setString(4, color);
                    pst.setString(5, size);
                    pst.setFloat(6, rate);
                    pst.setFloat(7, itemmrp);
                    pst.setString(8, ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText());
                    pst.setInt(9, userid);
                    pst.setBytes(10, ImageData);
                    pst.setString(11, comment);
                    
                    pst.execute();
                    ///    examperticipationinsert(id,examcode,exam);

                     // 
                } catch (SQLException | HeadlessException e) {
                   JOptionPane.showMessageDialog(null, e);

                }

            }
            //  loadGRN();
            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
        
      
        private void StockDetailsinsert() throws SQLException, IOException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String procode = (String) dataTable.getValueAt(row, 1);
                String probarcode=(String) dataTable.getValueAt(row, 2);
                String color=(String) dataTable.getValueAt(row, 4);
                String size=(String) dataTable.getValueAt(row, 5);
                
               
                try {

                    String sql = "Insert into stockdetails(itemcode,barcode,color,size,Qty) values (?,?,?,?,?) ON DUPLICATE KEY UPDATE color='"+color+"'";
                    pst = conn.prepareStatement(sql);
                   
                    pst.setString(1, procode);
                    pst.setString(2, probarcode);
                    pst.setString(3, color);
                    pst.setString(4, size);
                    pst.setFloat(5, (float) 0);
                    pst.execute();
                    ///    examperticipationinsert(id,examcode,exam);

                     // 
                } catch (SQLException | HeadlessException e) {
                   JOptionPane.showMessageDialog(null, e);

                }

            }
            //  loadGRN();
            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }     
    
       private void ItemClear(){
       itemnametext.setText(null);
       categorybox.setSelectedIndex(0);
       unibox2.setSelectedIndex(0);
       unitrateText2.setText(null);
       mrptext2.setText(null);
       if(vatcheck.isSelected()){
       vatcheck.setSelected(false);
       vattext.setText(null);
       }
       ItemCode();
       }
       private void addStock_insert() throws SQLException {
         
        if (itemnametext.getText().isEmpty() || categorybox.getSelectedIndex() == 0 || supliyerbox.getSelectedIndex() == 0 || unitrateText2.getText().isEmpty() || mrptext2.getText().isEmpty()) {
JOptionPane.showMessageDialog(null, "Requirment Error");
        } else {
        
            try {

                String sql = "Insert into item(itemcode,itemName,category,subcategory,supliyer,stockunit,openingDate,tp,mrp,includevate,vat,inputuser,updateuser,lastupdate,status) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
               // pst = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
                pst = conn.prepareStatement(sql);
                pst.setString(1, itemcodetext.getText());
               
                pst.setString(2, itemnametext.getText());
                pst.setString(3, catid);
                pst.setString(4, scatid);

                pst.setInt(5, supid);
                pst.setInt(6, unitid);

                pst.setString(7, ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText());
                pst.setString(8, unitrateText2.getText());
                pst.setString(9, mrptext2.getText());
                if(vatcheck.isSelected()){
                pst.setInt(10,1);
                pst.setString(11,vattext.getText());
                }else{
                pst.setInt(10,0);
                pst.setDouble(11,0);
                }
                pst.setInt(12, userid);
                pst.setInt(13, userid);
                pst.setString(14, ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText());
                pst.setString(15, "0");

                pst.execute();
                
                viewItemAfterInsert();
                ItemClear();
                qtytext.requestFocusInWindow();
                //finelEntry();
                /*
                
                ResultSet rshere = pst.getGeneratedKeys();
                int generatedKey = 0;
                if (rshere.next()) {
                    generatedKey = rshere.getInt(1);
                   itemcodeupdate(generatedKey);
                }
              */   
                //JOptionPane.showMessageDialog(null, "Data Saved");
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }   
       
       private void viewItemAfterInsert() throws SQLException{
       
   itemnamesearch.removeAllItems();
   itemnamesearch.addItem(" ");
    Item();
         try {

             

                String sql = "Select itemcode,itemName,tp,mrp,vat,(select un.unitshort from unit un where un.id=it.stockunit) as 'unitshorts' from item it where it.itemcode='" + itemcodetext.getText() + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {

                   itemcode = rs.getString("itemcode");
                   codetext.setText(itemcode);
                   itemname=rs.getString("itemName");
                   itemnamesearch.setSelectedItem(itemname);
                   unitrate = rs.getString("tp");
                   unitrateText.setText(unitrate);
                   mrpvalue = rs.getString("mrp");
                   mrptext.setText(mrpvalue);
                   includevat=rs.getFloat("vat");
                   
                   float tprice = rs.getFloat("tp");
                   float mrprice = rs.getFloat("mrp");                
                   float profite=(mrprice-tprice/100*tprice);
                   String pro=String.format("%.2f", profite);
                   proftetext.setText(pro +"%");

                    // qtytext.requestFocusInWindow();
                     //qtytext.setBackground(Color.YELLOW);
                     itempanel.setSelectedIndex(0);
                     barcodeManagment();
                     barcodetext.setText(barcode);
                    // qtytext.requestFocusInWindow();
                     qtytext.setBackground(Color.YELLOW);
                } 

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

}
      private void itemcodeupdate() {
        try {

           

            String sql = "Update item set tp='" + unitrateText.getText() + "',mrp='" + mrptext.getText() + "' where itemcode='" + itemcode + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    } 
      private void Item_code() {
        try {
             int new_inv = 1;
                itemcode = (+new_inv + "");
            
            String sql = "Select id from item";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
               
               
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                itemcode = (ParchaseCode);
              
                
            }
           // String finelcode = NewParchaseCode + Itemcodedate;
           // String finelcode = NewParchaseCode;
          //  codetext.setText(finelcode);
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }   
   private void purchaseupdate() {
        try {

           String date = ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText();

            String sql = "Update purchase set pdate='" +date + "',supliyer='" + supid + "',Totalrate='" + totalrate.getText() + "', totalvat='" + totalvattext.getText() + "', nettotal='" + nettotaltext.getText() + "',totaldiscount='"+netdiscounttext.getText()+"',inputuser='"+userid+"' where purchaseCode='" + parchaseText.getText() + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void checkentry() {
       // barcodeManagment();
        String s = "";
        boolean exists = false;
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            s = dataTable.getValueAt(i, 2).toString().trim();
            //filtere
            float tpd = Float.parseFloat(unitrateText.getText());
 
float qty=(Float)dataTable.getValueAt(i, 7);
float applyqty=Float.parseFloat(qtytext.getText());
float totalqty=qty+applyqty;


float totaldiscountget=(Float)dataTable.getValueAt(i, 8);
float discount=totaldiscountget/qty;              
float tpwdisc = tpd - discount;

float nettotaltp = (tpd * totalqty);
float nettotaltpformat=Float.parseFloat(df2.format(nettotaltp));
               
float totaldiscount=totalqty*discount;
float totaldiscountformat=Float.parseFloat(df2.format(totaldiscount));
             
/*
float vats=(Float)dataTable.getValueAt(i, 12);
float itemvat=tpwdisc*vats;
float itemvatfromat=Float.parseFloat(df2.format(itemvat));
*/
        float vat=includevat;
        float vats=vat/100;
        float itemvat=(tpwdisc*vats);
        float itemvatformat=Float.parseFloat(df2.format(itemvat));



float totalvat=itemvatformat*totalqty;
float totalvatformat=Float.parseFloat(df2.format(totalvat));
               
float nettotal=nettotaltpformat+totalvat-totaldiscountformat;
float nettotalformat=Float.parseFloat(df2.format(nettotal));
            
            if (barcode.equals("")) {
                JOptionPane.showMessageDialog(null, "Enter Invoice Details First");
            } else if (barcode.equals(s)) {
                exists = true;
                
//updtae
dataTable.setValueAt(totalqty, i, 7);
dataTable.setValueAt(totaldiscountformat, i, 8);
dataTable.setValueAt(nettotaltpformat, i, 9);
dataTable.setValueAt(totalvatformat, i, 10);
dataTable.setValueAt(nettotalformat, i, 11);
totalrate.setText(df2.format(total_action_mrp()));
netdiscounttext.setText(df2.format(total_action_discount()));
totalvattext.setText(df2.format(total_action_vat()));
nettotaltext.setText(df2.format(total_action_nettotal()));
                break;
            }
        }
        if (!exists) {
            entryData();
        } else {
           // JOptionPane.showMessageDialog(null, "This Data Already Exist.");
 
            clear();
        }

    }

    private void unit() throws SQLException {

        try {
            String sql = "Select * from unit";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("unitshort");
                int id = rs.getInt("id");
                
                unibox2.addItem(name);
                //unibox.setSelectedIndex(id);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void suppliyer() throws SQLException {

        try {
            String sql = "Select * from suplyierInfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("supliyername");
                int id = rs.getInt("id");
                supliyerbox.addItem(name);
                //  supliyerbox.setSelectedIndex(id);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void Item() throws SQLException {

        try {
            String sql = "Select itemName from item";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("itemName");
                itemnamesearch.addItem(name);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
   private float total_action_discount() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 8).toString());
        }

        return (float) totaltpmrp;

    }
    private float total_action_mrp() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 9).toString());
        }

        return (float) totaltpmrp;

    }
    private float total_action_vat() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 10).toString());
        }

        return (float) totaltpmrp;

    }
    
    
    private float total_action_nettotal() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 11).toString());
        }

        return (float) totaltpmrp;

    }
    private void clear() {
        //codetext.setText(null);
        //itemnamesearch.setSelectedIndex(0);
       /// unitrateText.setText(null);
        qtytext.setText(null);
        //unibox.setSelectedIndex(0);
       // discounttext.setText(null);
        //pics_lbl.setIcon(null);
       // person_image=null;
        //allcolorbox.setSelectedIndex(0);
        sizebox.setSelectedIndex(0);
        //mrptext.setText(null);
        //comentbox.setText(null);
       // barcodetext.setText(null);
    }
     private void clearone() {
        codetext.setText(null);
        itemnamesearch.setSelectedIndex(0);
        unitrateText.setText(null);
        qtytext.setText(null);
       
        discounttext.setText(null);
        pics_lbl.setIcon(null);
        person_image=null;
        allcolorbox.setSelectedIndex(0);
        sizebox.setSelectedIndex(0);
        mrptext.setText(null);
        comentbox.setText(null);
        barcodetext.setText(null);
    }
      public static String checkSum (String ean) throws Exception {
	
        long tot = 0;
 
        for (int i = 0; i < 12; i++) {
            tot = tot + (Long.parseLong(String.valueOf(ean.charAt(i))) * (i % 2 == 0 ? 1 : 3));
        }
        return tot % 10 == 0 ? "0" : "" +(10-(tot % 10));
	}  
      
 
private void barcodeManagment() {
    
            // int size=Integer.parseInt(sizetext.getText());
            //String sizeformat=String.format("%02d",size);
            int coloridin=Integer.parseInt(colorid);
            String colorformat=String.format("%02d",coloridin);
             int sizein=Integer.parseInt(sizeid);
              String sizeformat=String.format("%02d",sizein);
              String supidformaat=String.format("%03d",supid);
             
             
             
             int itemcodei=Integer.parseInt(itemcode);
             String itemcodeformat=String.format("%04d",itemcodei);
             String countrycod="8";
             String barcodebefor=countrycod+itemcodeformat+colorformat+sizeformat+supidformaat;
             
              //String test="977147396801";
             String sumcheck=null;
    try {
       sumcheck = checkSum (barcodebefor);
   } catch (Exception ex) {
       Logger.getLogger(NewPurchase.class.getName()).log(Level.SEVERE, null, ex);
    }
                   // codetext.setText(NewParchaseCode+sumcheck);
             barcode=barcodebefor+sumcheck;
            // barcode=itemcode+supidformaat+size+supid;
             
             
   }

public String firstTwo(String str) {
    return str.length() < 2 ? str : str.substring(0, 2);
}
    private void entryData() {
        //barcodeManagment();
        float discount;
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        tree++;

        float tpd = Float.parseFloat(unitrateText.getText());
        float mrp = Float.parseFloat(mrptext.getText());
        if (discounttext.getText().isEmpty()) {

            discount = 0;

        } else {
            discount = Float.parseFloat(discounttext.getText());

        }
        float tpwdisc = tpd - discount;
        float vat=includevat;
        float vats=vat/100;
        float itemvat=(tpwdisc*vats);
        float itemvatformat=Float.parseFloat(df2.format(itemvat));
        
        float qty = Float.parseFloat(qtytext.getText());
        float totaldiscount=qty*discount;
        float nettotaltp = (tpd * qty);
        float totalvat=(itemvatformat*qty);
       
        float totalvatformat=Float.parseFloat(df2.format(totalvat));
        float nettotal=nettotaltp+totalvat-totaldiscount;
        float nettotalformat=Float.parseFloat(df2.format(nettotal));
        String color=(String)allcolorbox.getSelectedItem();
        String colortwoformat=firstTwo(color);
        String size=(String) sizebox.getSelectedItem();
        String Itemformat=itemname+"("+colortwoformat+"/"+size+")";
        String comment=comentbox.getText();
        
        model2.addRow(new Object[]{tree,itemcode,barcode,Itemformat,allcolorbox.getSelectedItem(),size,tpd, qty,totaldiscount,nettotaltp,totalvatformat,nettotalformat,person_image,comment});
        totalrate.setText(df2.format(total_action_mrp()));
        netdiscounttext.setText(df2.format(total_action_discount()));
        totalvattext.setText(df2.format(total_action_vat()));
        nettotaltext.setText(df2.format(total_action_nettotal()));
  
       
        clear();
    }

    private void finelEntry() {
        

        if (updatekey == 0) {

            if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() ) {
                JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");

            } else {
                checkentry();

            }

        } else if (unitrateText.getText().isEmpty() || qtytext.getText().isEmpty()) {

        } else {

            DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
            int i = dataTable.getSelectedRow();
            if (i >= 0) {
                float discount;
                float tpd = Float.parseFloat(unitrateText.getText());
                if (discounttext.getText().isEmpty()) {

                    discount = 0;

                } else {
                    discount = Float.parseFloat(discounttext.getText());

                }
                float tpwdisc = tpd - discount;

                float qty = Float.parseFloat(qtytext.getText());
                float nettotaltp = (tpd * qty);
                float nettotaltpformat=Float.parseFloat(df2.format(nettotaltp));
                float totaldiscount=qty*discount;
                float totaldiscountformat=Float.parseFloat(df2.format(totaldiscount));
               // float vat=includevat;
                float vats=includevat;
                float itemvat=tpwdisc*vats;
                float itemvatfromat=Float.parseFloat(df2.format(itemvat));
                float totalvat=itemvatfromat*qty;
                float totalvatformat=Float.parseFloat(df2.format(totalvat));
                float nettotal=nettotaltpformat+totalvat-totaldiscountformat;
                float nettotalformat=Float.parseFloat(df2.format(nettotal));
                String procode=codetext.getText();
                String color=(String) allcolorbox.getSelectedItem();
                String size=(String) sizebox.getSelectedItem();
                gettpvalue(procode);
                getColorid(color);
                barcodeManagment();
                
       
               String colortwoformat=firstTwo(color);
               itemname=(String) itemnamesearch.getSelectedItem();
               String Itemformat=itemname+"("+colortwoformat+"/"+size+")";
               String comment=comentbox.getText();
                if(pics_lbl.getIcon()==null){
                
                }else{
                GetImage();
                }
              
                model.setValueAt(barcode, i, 2);
                model.setValueAt(Itemformat, i, 3);
                model.setValueAt(color, i, 4);
                model.setValueAt(size, i, 5);
                model.setValueAt(qty, i, 7);
                model.setValueAt(tpd, i, 6);
                model.setValueAt(totaldiscountformat, i, 8);
                model.setValueAt(nettotaltpformat, i, 9);
                //model.setValueAt(itemvatfromat, i, 12);
                model.setValueAt(totalvatformat, i, 10);
                model.setValueAt(nettotalformat, i, 11);
                model.setValueAt(person_image, i, 12);
                model.setValueAt(comment, i, 13);
                totalrate.setText(df2.format(total_action_mrp()));
                netdiscounttext.setText(df2.format(total_action_discount()));
                totalvattext.setText(df2.format(total_action_vat()));
                nettotaltext.setText(df2.format(total_action_nettotal()));
                clear();
                 updatekey=0;
                codetext.setText(null);
                itemnamesearch.setSelectedIndex(0);
            }

        }

    }

private void gettpvalue(String procode){
    
        try {

            String sql = "Select ita.mrp as 'mrp',ita.tp as 'tprice'  from item ita where ita.itemcode='" + procode + "'";
            pst = conn.prepareStatement(sql);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            if (rs.next()) {

               
                unitrate = rs.getString("tprice");
                mrpvalue = rs.getString("mrp");
               

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }


}
    private void getColorid(String colorname){
    
      try {
            String sql = "Select id from color where name='"+colorname+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

               colorid=rs.getString("id");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    
    
    
    }
    
    private void GetImage(){
    
    try {
        Icon icons = pics_lbl.getIcon();
        BufferedImage bi = new BufferedImage(icons.getIconWidth(), icons.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        icons.paintIcon(null, g, 0, 0);
        g.setColor(Color.WHITE);
        g.drawString(pics_lbl.getText(), 10, 20);
        g.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", os);
        InputStream fis = new ByteArrayInputStream(os.toByteArray());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        for (int readNum; (readNum = fis.read(buf)) != -1;) {
            bos.write(buf, 0, readNum);
            //System.out.println("read " + readNum + " bytes,");
            }
         person_image = bos.toByteArray();
        //photo = bytes;
    } catch (IOException d) {
        JOptionPane.showMessageDialog(rootPane, d);
    }
    
    }
    private void reload() throws SQLException{
    
         codetext.setText(null);
        itemnamesearch.setSelectedIndex(0);
        unitrateText.setText(null);
        qtytext.setText(null);
       
        totalrate.setText(null);
        discounttext.setText(null);
        parchasedate.setDate(null);
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);
        codetext.enable(true);
        itemnamesearch.enable(true);
       
        
         parchase_code();
        suppliyer();
    
    }

    
    private void loadGRN() throws SQLException, IOException{
    if(modifiaction==0){
        String table_click = parchaseText.getText();
        PurchaseDetails filte = null;
        
        filte = new PurchaseDetails(table_click);
      

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
          
        //Homedesktop.add(It);
        filte.setVisible(true);

        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
        this.dispose();
    }else{
    GRNframe filte = null;
        
        filte = new GRNframe();
      

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
          
        //Homedesktop.add(It);
        filte.setVisible(true);

        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
        this.dispose();
    
    
    
    
    }
    
    
    
    
    }
     private void purchasedeletehistory(){
    
        try {
                String sql = "Delete from purchaseDetails where purchaseCode=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, parchaseText.getText());

                pst.execute();
                

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
    
    
    }
     
        private void imageupload() {
        JFileChooser choser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "gif", "png");
        choser.addChoosableFileFilter(filter);
        int result = choser.showSaveDialog(null);
        // choser.showOpenDialog(null);
        File f = choser.getSelectedFile();
        filename = f.getAbsolutePath();
        //file_path.setText(filename);

        try {
            File image = new File(filename);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1;) {

                bos.write(buf, 0, readNum);

            }
            person_image = bos.toByteArray();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = choser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            pics_lbl.setIcon(ResizeImage(path));
        } //if the user click on save in Jfilechooser
        else if (result == JFileChooser.CANCEL_OPTION) {
            //   System.out.println("No File Select");
        }

    }

    public ImageIcon ResizeImage(String ImagePath) {
        ImageIcon MyImage = new ImageIcon(ImagePath);
        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(pics_lbl.getWidth(), pics_lbl.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }

    private Image ScaleImage(byte[] img, int w, int h) throws IOException {

        BufferedImage resizeIamge = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizeIamge.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        ByteArrayInputStream in = new ByteArrayInputStream(img);
        BufferedImage bImageFromConvert = ImageIO.read(in);
        g2.drawImage(bImageFromConvert, 0, 0, w, h, null);
        g2.dispose();
        return resizeIamge;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        parchaseText = new javax.swing.JLabel();
        parchasedate = new com.toedter.calendar.JDateChooser();
        lottext = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        proftetext = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarktext = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        supliyerbox = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        barcodebtn = new javax.swing.JButton();
        printaccess = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        svbtn = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        totalrate = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        netdiscounttext = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        totalvattext = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        nettotaltext = new javax.swing.JLabel();
        itempanel = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        unitrateText = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        discounttext = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        mrptext = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        pics_lbl = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        comentbox = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        barcodetext = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        qtytext = new javax.swing.JTextField();
        allcolorbox = new javax.swing.JComboBox<>();
        sizebox = new javax.swing.JComboBox<>();
        jPanel15 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        itemcodetext = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        itemnametext = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        categorybox = new javax.swing.JComboBox<>();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        unitrateText2 = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        mrptext2 = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        vatcheck = new javax.swing.JCheckBox();
        jLabel56 = new javax.swing.JLabel();
        vattext = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        unibox2 = new javax.swing.JComboBox<>();
        jLabel58 = new javax.swing.JLabel();
        subcategorybox = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        camerapanel = new javax.swing.JPanel();
        startbtn = new javax.swing.JButton();
        stopbtn = new javax.swing.JButton();
        snapbtn = new javax.swing.JButton();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        parchaseText1 = new javax.swing.JLabel();
        parchasedate1 = new com.toedter.calendar.JDateChooser();
        lottext1 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        proftetext1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        remarktext1 = new javax.swing.JTextArea();
        jLabel29 = new javax.swing.JLabel();
        supliyerbox1 = new javax.swing.JComboBox<>();
        jLabel30 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        dataTable1 = new javax.swing.JTable();
        jTextField2 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        svbtn1 = new javax.swing.JButton();
        deletebtn1 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        totalrate1 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        netdiscounttext1 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        totalvattext1 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        nettotaltext1 = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel12 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        codetext1 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        itemnamesearch1 = new javax.swing.JComboBox<>();
        jLabel38 = new javax.swing.JLabel();
        unitrateText1 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        discounttext1 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        qtytext1 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        unibox1 = new javax.swing.JComboBox<>();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        allcolorbox1 = new javax.swing.JComboBox<>();
        jLabel43 = new javax.swing.JLabel();
        sizetext1 = new javax.swing.JTextField();
        mrptext1 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        vatcheck1 = new javax.swing.JCheckBox();
        jLabel46 = new javax.swing.JLabel();
        vattext1 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        categorybox1 = new javax.swing.JComboBox<>();
        jLabel48 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        pics_lbl1 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        camerapanel1 = new javax.swing.JPanel();
        startbtn1 = new javax.swing.JButton();
        stopbtn1 = new javax.swing.JButton();
        snapbtn1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Purchase");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));
        jPanel2.setPreferredSize(new java.awt.Dimension(1500, 95));

        jPanel6.setBackground(new java.awt.Color(67, 86, 86));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Date");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Ref.No:");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Profite:");

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("Load Items");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseClicked(evt);
            }
        });

        parchaseText.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        parchaseText.setForeground(new java.awt.Color(255, 255, 255));
        parchaseText.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        parchasedate.setDateFormatString("yyyy-MM-dd ");

        lottext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lottext.setForeground(new java.awt.Color(255, 255, 255));
        lottext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Purchase No");

        proftetext.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        proftetext.setForeground(new java.awt.Color(0, 204, 51));
        proftetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        remarktext.setColumns(20);
        remarktext.setRows(5);
        remarktext.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jScrollPane2.setViewportView(remarktext);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Lot");

        supliyerbox.setEditable(true);
        supliyerbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        supliyerbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                supliyerboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Suppliyer");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supliyerbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(3, 3, 3)
                        .addComponent(parchaseText, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel10)
                        .addGap(19, 19, 19)
                        .addComponent(lottext, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addGap(5, 5, 5)
                        .addComponent(proftetext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(parchasedate, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(parchaseText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(parchasedate, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(supliyerbox)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lottext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(proftetext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel3.setBackground(new java.awt.Color(0, 51, 51));
        jPanel3.setPreferredSize(new java.awt.Dimension(1200, 470));

        dataTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        dataTable.setFont(new java.awt.Font("Tahoma", 0, 15)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Code", "Barcode", "Item Name", "Color", "Size", "TP", "Qty", "Discount", "Amount", "Vat", "Net Total", "ImageData", "Comment"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTable.setRowHeight(35);
        dataTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dataTableMousePressed(evt);
            }
        });
        dataTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dataTableKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(dataTable);
        if (dataTable.getColumnModel().getColumnCount() > 0) {
            dataTable.getColumnModel().getColumn(0).setResizable(false);
            dataTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            dataTable.getColumnModel().getColumn(1).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            dataTable.getColumnModel().getColumn(3).setResizable(false);
            dataTable.getColumnModel().getColumn(3).setPreferredWidth(200);
            dataTable.getColumnModel().getColumn(4).setResizable(false);
            dataTable.getColumnModel().getColumn(5).setResizable(false);
            dataTable.getColumnModel().getColumn(5).setPreferredWidth(50);
            dataTable.getColumnModel().getColumn(6).setResizable(false);
            dataTable.getColumnModel().getColumn(7).setResizable(false);
            dataTable.getColumnModel().getColumn(7).setPreferredWidth(30);
            dataTable.getColumnModel().getColumn(8).setResizable(false);
            dataTable.getColumnModel().getColumn(9).setResizable(false);
            dataTable.getColumnModel().getColumn(10).setResizable(false);
            dataTable.getColumnModel().getColumn(11).setResizable(false);
            dataTable.getColumnModel().getColumn(12).setResizable(false);
        }

        barcodebtn.setBackground(new java.awt.Color(255, 255, 255));
        barcodebtn.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        barcodebtn.setForeground(new java.awt.Color(102, 0, 0));
        barcodebtn.setText("Barcode");
        barcodebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barcodebtnActionPerformed(evt);
            }
        });

        printaccess.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        printaccess.setForeground(new java.awt.Color(255, 255, 255));
        printaccess.setText("Print");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(barcodebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(printaccess, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1200, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(barcodebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(printaccess, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel3);

        jPanel4.setBackground(new java.awt.Color(67, 86, 86));

        svbtn.setBackground(new java.awt.Color(0, 102, 51));
        svbtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        svbtn.setForeground(new java.awt.Color(255, 255, 255));
        svbtn.setText("Execute");
        svbtn.setBorder(null);
        svbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svbtnActionPerformed(evt);
            }
        });

        deletebtn.setBackground(new java.awt.Color(153, 0, 0));
        deletebtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deletebtn.setForeground(new java.awt.Color(255, 255, 255));
        deletebtn.setText("Delete");
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Total Rate");

        totalrate.setBackground(new java.awt.Color(255, 255, 255));
        totalrate.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalrate.setForeground(new java.awt.Color(255, 255, 255));
        totalrate.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Discount");

        netdiscounttext.setBackground(new java.awt.Color(67, 86, 86));
        netdiscounttext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        netdiscounttext.setForeground(new java.awt.Color(255, 255, 255));
        netdiscounttext.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Total Vat");

        totalvattext.setBackground(new java.awt.Color(255, 255, 255));
        totalvattext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalvattext.setForeground(new java.awt.Color(255, 255, 255));
        totalvattext.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("NetTotal");

        nettotaltext.setBackground(new java.awt.Color(255, 255, 255));
        nettotaltext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        nettotaltext.setForeground(new java.awt.Color(255, 255, 255));
        nettotaltext.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(netdiscounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nettotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(netdiscounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nettotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deletebtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(svbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        itempanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itempanelMouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Item Code");

        codetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        codetext.setForeground(new java.awt.Color(153, 0, 0));
        codetext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        codetext.setBorder(null);
        codetext.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        codetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codetextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetextKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Item Name");

        itemnamesearch.setBackground(new java.awt.Color(255, 249, 248));
        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        itemnamesearch.setBorder(null);
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        itemnamesearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                itemnamesearchKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("TP");

        unitrateText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        unitrateText.setForeground(new java.awt.Color(102, 0, 0));
        unitrateText.setBorder(null);
        unitrateText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                unitrateTextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                unitrateTextKeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Discount");

        discounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        discounttext.setForeground(new java.awt.Color(0, 102, 0));
        discounttext.setBorder(null);
        discounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                discounttextKeyPressed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 0, 0));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("ADD");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 153, 0));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        mrptext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        mrptext.setForeground(new java.awt.Color(102, 0, 0));
        mrptext.setBorder(null);
        mrptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mrptextKeyPressed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("MRP");

        jPanel5.setBackground(new java.awt.Color(67, 86, 86));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton6.setBackground(new java.awt.Color(204, 204, 255));
        jButton6.setText("Clear");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        pics_lbl.setBackground(new java.awt.Color(255, 255, 255));
        pics_lbl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pics_lbl.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButton5.setBackground(new java.awt.Color(0, 153, 153));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Browse");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pics_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pics_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Comment");

        comentbox.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        comentbox.setForeground(new java.awt.Color(0, 102, 0));
        comentbox.setBorder(null);
        comentbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                comentboxKeyPressed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Barcode");

        barcodetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        barcodetext.setForeground(new java.awt.Color(153, 0, 0));
        barcodetext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        barcodetext.setBorder(null);
        barcodetext.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        barcodetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                barcodetextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                barcodetextKeyReleased(evt);
            }
        });

        jPanel16.setBackground(new java.awt.Color(67, 86, 86));
        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Color");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Size:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Qty");

        qtytext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        qtytext.setForeground(new java.awt.Color(153, 0, 0));
        qtytext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        qtytext.setBorder(null);
        qtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytextKeyPressed(evt);
            }
        });

        allcolorbox.setEditable(true);
        allcolorbox.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        allcolorbox.setBorder(null);
        allcolorbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                allcolorboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        sizebox.setEditable(true);
        sizebox.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        sizebox.setBorder(null);
        sizebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                sizeboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(allcolorbox, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sizebox, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(qtytext))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel18)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(allcolorbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sizebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comentbox)
                    .addComponent(discounttext)
                    .addComponent(mrptext)
                    .addComponent(unitrateText)
                    .addComponent(itemnamesearch, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(barcodetext)
                    .addComponent(codetext)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(codetext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(barcodetext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addGap(1, 1, 1))
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comentbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        itempanel.addTab("Item Entry", jPanel1);

        jPanel15.setBackground(new java.awt.Color(0, 102, 102));

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Product Code");

        itemcodetext.setEditable(false);
        itemcodetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        itemcodetext.setForeground(new java.awt.Color(153, 0, 0));
        itemcodetext.setBorder(null);
        itemcodetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemcodetextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                itemcodetextKeyReleased(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("Item Name");

        itemnametext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        itemnametext.setForeground(new java.awt.Color(153, 0, 0));
        itemnametext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        itemnametext.setBorder(null);
        itemnametext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemnametextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                itemnametextKeyReleased(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Category");

        categorybox.setBackground(new java.awt.Color(255, 249, 248));
        categorybox.setEditable(true);
        categorybox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        categorybox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        categorybox.setBorder(null);
        categorybox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                categoryboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        categorybox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                categoryboxKeyTyped(evt);
            }
        });

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setText("Stock Unit");

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("TP");

        unitrateText2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        unitrateText2.setForeground(new java.awt.Color(102, 0, 0));
        unitrateText2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        unitrateText2.setBorder(null);

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("MRP");

        mrptext2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        mrptext2.setForeground(new java.awt.Color(102, 0, 0));
        mrptext2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        mrptext2.setBorder(null);

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Vat Req.");

        vatcheck.setBackground(new java.awt.Color(255, 255, 255));
        vatcheck.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        vatcheck.setForeground(new java.awt.Color(255, 255, 255));
        vatcheck.setText("Include ");
        vatcheck.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vatcheckMouseClicked(evt);
            }
        });

        jLabel56.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 255, 255));
        jLabel56.setText("Vat");

        vattext.setEditable(false);
        vattext.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel57.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("%");

        jButton13.setBackground(new java.awt.Color(102, 0, 0));
        jButton13.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("New Entry");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        unibox2.setEditable(true);
        unibox2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        unibox2.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                unibox2PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Category Head");

        subcategorybox.setBackground(new java.awt.Color(255, 249, 248));
        subcategorybox.setEditable(true);
        subcategorybox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        subcategorybox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        subcategorybox.setBorder(null);
        subcategorybox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                subcategoryboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        subcategorybox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                subcategoryboxKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(110, 110, 110))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(mrptext2, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel15Layout.createSequentialGroup()
                                        .addComponent(vatcheck, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel56)
                                        .addGap(18, 18, 18)
                                        .addComponent(vattext, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(unitrateText2))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel50)
                                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel58, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(11, 11, 11)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(subcategorybox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(categorybox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(itemnametext, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(unibox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(itemcodetext, javax.swing.GroupLayout.Alignment.LEADING))))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(itemcodetext)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(itemnametext)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(categorybox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subcategorybox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unibox2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitrateText2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mrptext2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vatcheck, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(vattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57))
                .addGap(18, 18, 18)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(245, Short.MAX_VALUE))
        );

        itempanel.addTab("New Item", jPanel15);

        jPanel7.setBackground(new java.awt.Color(67, 86, 86));

        camerapanel.setBackground(new java.awt.Color(0, 0, 0));
        camerapanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        camerapanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                camerapanelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout camerapanelLayout = new javax.swing.GroupLayout(camerapanel);
        camerapanel.setLayout(camerapanelLayout);
        camerapanelLayout.setHorizontalGroup(
            camerapanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );
        camerapanelLayout.setVerticalGroup(
            camerapanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 392, Short.MAX_VALUE)
        );

        startbtn.setText("Start");
        startbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startbtnActionPerformed(evt);
            }
        });

        stopbtn.setText("Pause");
        stopbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopbtnActionPerformed(evt);
            }
        });

        snapbtn.setText("Scren Shot");
        snapbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                snapbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(camerapanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(startbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(snapbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(camerapanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stopbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(snapbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        itempanel.addTab("Camera Capture", jPanel7);

        jInternalFrame1.setClosable(true);
        jInternalFrame1.setIconifiable(true);
        jInternalFrame1.setMaximizable(true);
        jInternalFrame1.setResizable(true);
        jInternalFrame1.setTitle("Purchase");
        jInternalFrame1.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel8.setBackground(new java.awt.Color(67, 86, 86));

        jPanel9.setBackground(new java.awt.Color(67, 86, 86));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Date");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Ref.No:");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Profite:");

        jCheckBox2.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jCheckBox2.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setText("Load Items");
        jCheckBox2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox2MouseClicked(evt);
            }
        });

        parchaseText1.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        parchaseText1.setForeground(new java.awt.Color(255, 255, 255));
        parchaseText1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        parchasedate1.setDateFormatString("yyyy-MM-dd ");

        lottext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lottext1.setForeground(new java.awt.Color(255, 255, 255));
        lottext1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Purchase No");

        proftetext1.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        proftetext1.setForeground(new java.awt.Color(0, 204, 51));
        proftetext1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        remarktext1.setColumns(20);
        remarktext1.setRows(5);
        remarktext1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jScrollPane3.setViewportView(remarktext1);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Lot");

        supliyerbox1.setEditable(true);
        supliyerbox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        supliyerbox1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                supliyerbox1PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Suppliyer");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supliyerbox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(3, 3, 3)
                        .addComponent(parchaseText1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(parchasedate1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel29)
                        .addGap(19, 19, 19)
                        .addComponent(lottext1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel27)
                        .addGap(5, 5, 5)
                        .addComponent(proftetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(parchaseText1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(parchasedate1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(supliyerbox1)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lottext1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(proftetext1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(0, 51, 51));
        jPanel10.setPreferredSize(new java.awt.Dimension(1500, 438));

        dataTable1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        dataTable1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dataTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Item Code", "Barcode", "Item Name", "Color", "Size", "Unit Rate", "Qty", "Unit", "Total Discount", "Total ", "Vat", "Total Vat", "Net Total", "ImageData"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTable1.setRowHeight(30);
        dataTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataTable1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dataTable1MousePressed(evt);
            }
        });
        dataTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dataTable1KeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(dataTable1);
        if (dataTable1.getColumnModel().getColumnCount() > 0) {
            dataTable1.getColumnModel().getColumn(0).setResizable(false);
            dataTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
            dataTable1.getColumnModel().getColumn(1).setResizable(false);
            dataTable1.getColumnModel().getColumn(2).setResizable(false);
            dataTable1.getColumnModel().getColumn(2).setPreferredWidth(200);
            dataTable1.getColumnModel().getColumn(3).setResizable(false);
            dataTable1.getColumnModel().getColumn(3).setPreferredWidth(300);
            dataTable1.getColumnModel().getColumn(4).setResizable(false);
            dataTable1.getColumnModel().getColumn(5).setResizable(false);
            dataTable1.getColumnModel().getColumn(6).setResizable(false);
            dataTable1.getColumnModel().getColumn(7).setResizable(false);
            dataTable1.getColumnModel().getColumn(7).setPreferredWidth(30);
            dataTable1.getColumnModel().getColumn(8).setResizable(false);
            dataTable1.getColumnModel().getColumn(9).setResizable(false);
            dataTable1.getColumnModel().getColumn(10).setResizable(false);
            dataTable1.getColumnModel().getColumn(11).setResizable(false);
            dataTable1.getColumnModel().getColumn(12).setResizable(false);
            dataTable1.getColumnModel().getColumn(13).setResizable(false);
            dataTable1.getColumnModel().getColumn(14).setResizable(false);
        }

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Search");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1490, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel31)
                .addGap(2, 2, 2)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane5.setViewportView(jPanel10);

        jPanel11.setBackground(new java.awt.Color(67, 86, 86));

        svbtn1.setBackground(new java.awt.Color(0, 102, 51));
        svbtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        svbtn1.setForeground(new java.awt.Color(255, 255, 255));
        svbtn1.setText("Execute");
        svbtn1.setBorder(null);
        svbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svbtn1ActionPerformed(evt);
            }
        });

        deletebtn1.setBackground(new java.awt.Color(153, 0, 0));
        deletebtn1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deletebtn1.setForeground(new java.awt.Color(255, 255, 255));
        deletebtn1.setText("Delete");
        deletebtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtn1ActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Total Rate");

        totalrate1.setBackground(new java.awt.Color(255, 255, 255));
        totalrate1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalrate1.setForeground(new java.awt.Color(255, 255, 255));
        totalrate1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Discount");

        netdiscounttext1.setBackground(new java.awt.Color(67, 86, 86));
        netdiscounttext1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        netdiscounttext1.setForeground(new java.awt.Color(255, 255, 255));
        netdiscounttext1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Total Vat");

        totalvattext1.setBackground(new java.awt.Color(255, 255, 255));
        totalvattext1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalvattext1.setForeground(new java.awt.Color(255, 255, 255));
        totalvattext1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("NetTotal");

        nettotaltext1.setBackground(new java.awt.Color(255, 255, 255));
        nettotaltext1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        nettotaltext1.setForeground(new java.awt.Color(255, 255, 255));
        nettotaltext1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalrate1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(netdiscounttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(totalvattext1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99)))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nettotaltext1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(svbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(deletebtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalrate1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(netdiscounttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalvattext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nettotaltext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deletebtn1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(svbtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        jTabbedPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane2MouseClicked(evt);
            }
        });

        jPanel12.setBackground(new java.awt.Color(67, 86, 86));

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Product Code");

        codetext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        codetext1.setForeground(new java.awt.Color(153, 0, 0));
        codetext1.setBorder(null);
        codetext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codetext1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetext1KeyReleased(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("Item Name");

        itemnamesearch1.setBackground(new java.awt.Color(255, 249, 248));
        itemnamesearch1.setEditable(true);
        itemnamesearch1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        itemnamesearch1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        itemnamesearch1.setBorder(null);
        itemnamesearch1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearch1PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        itemnamesearch1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                itemnamesearch1KeyTyped(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("TP");

        unitrateText1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        unitrateText1.setForeground(new java.awt.Color(102, 0, 0));
        unitrateText1.setBorder(null);

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Discount");

        discounttext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        discounttext1.setForeground(new java.awt.Color(0, 102, 0));
        discounttext1.setBorder(null);

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Qty");

        qtytext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        qtytext1.setForeground(new java.awt.Color(153, 0, 0));
        qtytext1.setBorder(null);
        qtytext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytext1KeyPressed(evt);
            }
        });

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setText("Unit ");

        unibox1.setEditable(true);
        unibox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        jButton7.setBackground(new java.awt.Color(255, 0, 0));
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("ADD");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(0, 153, 0));
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Clear");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setText("Color");

        allcolorbox1.setEditable(true);
        allcolorbox1.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        allcolorbox1.setBorder(null);
        allcolorbox1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                allcolorbox1PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Size:");

        sizetext1.setText("1");

        mrptext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        mrptext1.setForeground(new java.awt.Color(102, 0, 0));
        mrptext1.setBorder(null);

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("MRP");

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("MRP");

        vatcheck1.setBackground(new java.awt.Color(255, 255, 255));
        vatcheck1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        vatcheck1.setForeground(new java.awt.Color(255, 255, 255));
        vatcheck1.setText("Include ");
        vatcheck1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vatcheck1MouseClicked(evt);
            }
        });

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setText("Vat");

        vattext1.setEditable(false);

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("%");

        categorybox1.setBackground(new java.awt.Color(255, 249, 248));
        categorybox1.setEditable(true);
        categorybox1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        categorybox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        categorybox1.setBorder(null);
        categorybox1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                categorybox1PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        categorybox1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                categorybox1KeyTyped(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Category");

        jPanel13.setBackground(new java.awt.Color(67, 86, 86));
        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton9.setBackground(new java.awt.Color(204, 204, 255));
        jButton9.setText("Clear");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        pics_lbl1.setBackground(new java.awt.Color(255, 255, 255));
        pics_lbl1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pics_lbl1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButton10.setBackground(new java.awt.Color(0, 153, 153));
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Browse");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setText("jButton3");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pics_lbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton11))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pics_lbl1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );

        jButton12.setText("New Entry");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41)
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36)
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(discounttext1)
                            .addComponent(unibox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(qtytext1)
                            .addComponent(unitrateText1)
                            .addComponent(allcolorbox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(itemnamesearch1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(codetext1)
                            .addGroup(jPanel12Layout.createSequentialGroup()
                                .addComponent(vatcheck1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(vattext1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(mrptext1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(sizetext1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(categorybox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(codetext1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemnamesearch1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(categorybox1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(allcolorbox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sizetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitrateText1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vatcheck1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vattext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(mrptext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(qtytext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(unibox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(discounttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        jTabbedPane2.addTab("Item Entry", jPanel12);

        jPanel14.setBackground(new java.awt.Color(67, 86, 86));

        camerapanel1.setBackground(new java.awt.Color(0, 0, 0));
        camerapanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        camerapanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                camerapanel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout camerapanel1Layout = new javax.swing.GroupLayout(camerapanel1);
        camerapanel1.setLayout(camerapanel1Layout);
        camerapanel1Layout.setHorizontalGroup(
            camerapanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );
        camerapanel1Layout.setVerticalGroup(
            camerapanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 392, Short.MAX_VALUE)
        );

        startbtn1.setText("Start");
        startbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startbtn1ActionPerformed(evt);
            }
        });

        stopbtn1.setText("Pause");
        stopbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopbtn1ActionPerformed(evt);
            }
        });

        snapbtn1.setText("Scren Shot");
        snapbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                snapbtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(camerapanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(startbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(snapbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(camerapanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stopbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(snapbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        jTabbedPane2.addTab("Camera Capture", jPanel14);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jTabbedPane2)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(itempanel, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 915, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 680, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 680, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(itempanel)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 299, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 299, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void codetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();
            qtytext.setBackground(Color.YELLOW);

        }
    }//GEN-LAST:event_codetextKeyPressed

    private void codetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyReleased

        String SearchText = (String) codetext.getText();
              try {

             

                String sql = "Select itemcode,itemName,tp,mrp,vat,(select un.unitshort from unit un where un.id=it.stockunit) as 'unitshorts' from item it where it.itemcode='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {

                   itemcode = rs.getString("itemcode");
                   codetext.setText(itemcode);
                   itemname=rs.getString("itemName");
                   itemnamesearch.setSelectedItem(itemname);
                   unitrate = rs.getString("tp");
                   unitrateText.setText(unitrate);
                   mrpvalue = rs.getString("mrp");
                   mrptext.setText(mrpvalue);
                   includevat=rs.getFloat("vat");
                   
                   float tprice = rs.getFloat("tp");
                   float mrprice = rs.getFloat("mrp");                
                   float profite=(mrprice-tprice/100*tprice);
                   String pro=String.format("%.2f", profite);
                   proftetext.setText(pro +"%");

                    // qtytext.requestFocusInWindow();
                     //qtytext.setBackground(Color.YELLOW);
                     //barcodeManagment();
                     //barcodetext.setText(barcode);
                   
                }else{
                clear();
                
                } 

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
              
             // barcodeManagment();
              //barcodetext.setText(barcode);
    }//GEN-LAST:event_codetextKeyReleased

    private void qtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        }else{
         if(unitrateText.getText().isEmpty() || mrptext.getText().isEmpty() )  {


}else{
float tp=Float.parseFloat(unitrateText.getText());
float mrp=Float.parseFloat(mrptext.getText());

if(tp>mrp){
JOptionPane.showMessageDialog(null, "TP Price Grater Than MRP Price");

}else{

 barcodeManagment();
 itemcodeupdate();
 finelEntry();
 codetext.requestFocusInWindow();
 qtytext.setBackground(Color.WHITE);

}


}
            

        }
    }//GEN-LAST:event_qtytextKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
if(unitrateText.getText().isEmpty() || mrptext.getText().isEmpty() )  {


}else{
float tp=Float.parseFloat(unitrateText.getText());
float mrp=Float.parseFloat(mrptext.getText());

if(tp>mrp){
JOptionPane.showMessageDialog(null, "TP Price Grater Than MRP Price");

}else{

 barcodeManagment();
 itemcodeupdate();
 finelEntry();
 codetext.requestFocusInWindow();
 qtytext.setBackground(Color.WHITE);
}


}  
   
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clearone();
        updatekey=0;
        codetext.enable(true);
        itemnamesearch.enable(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
  
        
           if (itemnamesearch.getSelectedIndex() > 0) {
             String SearchText = (String) itemnamesearch.getSelectedItem();
              try {

             

                String sql = "Select itemcode,itemName,tp,mrp,vat,(select un.unitshort from unit un where un.id=it.stockunit) as 'unitshorts' from item it where it.itemName='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {

                   itemcode = rs.getString("itemcode");
                   codetext.setText(itemcode);
                   itemname=rs.getString("itemName");
                   itemnamesearch.setSelectedItem(itemname);
                   unitrate = rs.getString("tp");
                   unitrateText.setText(unitrate);
                   mrpvalue = rs.getString("mrp");
                   mrptext.setText(mrpvalue);
                   includevat=rs.getFloat("vat");
                   
                   float tprice = rs.getFloat("tp");
                   float mrprice = rs.getFloat("mrp");                
                   float profite=(mrprice-tprice/100*tprice);
                   String pro=String.format("%.2f", profite);
                   proftetext.setText(pro +"%");

                    // qtytext.requestFocusInWindow();
                     //qtytext.setBackground(Color.YELLOW);
                     //barcodeManagment();
                     //barcodetext.setText(barcode);
                     qtytext.requestFocusInWindow();
                     qtytext.setBackground(Color.YELLOW);
                   
                } 

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }else{
            clear();
           }
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void itemnamesearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyTyped
        try {
            Item();
        } catch (SQLException ex) {
            Logger.getLogger(Purchase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_itemnamesearchKeyTyped

    private void jCheckBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseClicked
        try {

            supliyerbox.removeAllItems();
            supliyerbox.setSelectedItem("Select");

            itemnamesearch.removeAllItems();
            itemnamesearch.setSelectedItem("Select");
            
            Item();
            unit();
            suppliyer();
        } catch (SQLException ex) {
            Logger.getLogger(Purchase.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jCheckBox1MouseClicked
private void ItementryactiveDiactive(){
if(supid>0){
codetext.setEditable(true);
barcodetext.setEditable(true);
itemnamesearch.setEnabled(true);

}else{
codetext.setEditable(false);
barcodetext.setEditable(false);
itemnamesearch.setEnabled(false);
}



}
    private void supliyerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supliyerboxPopupMenuWillBecomeInvisible
        
        if(supliyerbox.getSelectedIndex()>0){
        try {
            String sql = "Select id from suplyierInfo where supliyername='"+supliyerbox.getSelectedItem()+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                supid = rs.getInt("id");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }}else{
        supid=0;
        
        }
        ItementryactiveDiactive();
    }//GEN-LAST:event_supliyerboxPopupMenuWillBecomeInvisible

    private void allcolorboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_allcolorboxPopupMenuWillBecomeInvisible
      String colorname=(String) allcolorbox.getSelectedItem();
        
        try {
            String sql = "Select id from color where name='"+colorname+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

               colorid=rs.getString("id");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        barcodeManagment();
        barcodetext.setText(barcode);
         sizebox.requestFocusInWindow();
        
    }//GEN-LAST:event_allcolorboxPopupMenuWillBecomeInvisible

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        pics_lbl.setIcon(null);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        imageupload();
    }//GEN-LAST:event_jButton5ActionPerformed
private void Minusamount(){
 try {
            String sql = "Select nettotal as 'minusamount'  from purchase where purchaseCode='"+parchaseText.getText()+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
           while(rs.next()){
           
           minusAmount=rs.getFloat("minusamount");
           
           
           
           }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }



}
private void selectsupliyer() {
        try {

            String sql = "Select  supliyer from purchase where purchaseCode='" + parchaseText.getText() + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            if (rs.next()) {

                suplyer = rs.getString("supliyer");

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void suplyerinfo() {
        try {

            String sql = "Select consighnmentamnt,Balancedue from suplyierInfo where id='" + suplyer + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            if (rs.next()) {

               
                consighnmentamnt = rs.getDouble("consighnmentamnt");
                Balancedue = rs.getDouble("Balancedue");

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

private void UpdateMunusSupplier(){
selectsupliyer();
suplyerinfo();
 try {
            double preamt = consighnmentamnt;
            double presntamt = minusAmount;
            double prentconsigntotal = preamt - presntamt;
            double afterblncedue = Balancedue - presntamt;

            String sql = "Update suplyierInfo set consighnmentamnt='" + prentconsigntotal + "',Balancedue='" + afterblncedue + "' where id='" + suplyer + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }


}

private void StockloadMinus() {

        try {
            String sql1 = "Select st.Qty as 'stockqty' from stockdetails st where st.barcode=(Select pc.barcode from purchasedetails pc where pc.barcode=st.barcode AND pc.purchaseCode='" + parchaseText.getText() + "' )";
            pst = conn.prepareStatement(sql1);

            rs = pst.executeQuery();

            while (rs.next()) {
                Float updateqty = rs.getFloat("stockqty");
                //String sql1 = "Select prcode,qty,unit , count(id)  from purchasedetails pc where pc.purchaseCode='" + parchasetext.getText() + "'.Update  stock st set st.Qty=pc.qty where st.procode=pc.prcode";
                String sql = "Update stockdetails sta set sta.Qty=(Select pc.qty-'" + updateqty + "'  from purchasedetails pc where pc.barcode=sta.barcode AND pc.purchaseCode='" + parchaseText.getText() + "') where (select pc.barcode from purchasedetails pc where pc.barcode=sta.barcode AND pc.purchaseCode='" + parchaseText.getText() + "')=sta.barcode";
                //  String sql="update stock sta set Qty=(select sum(pc.qty+(select st.Qty from stock st where st.procode=pc.prcode))from purchasedetails pc where sta.procode=pc.prcode AND pc.purchaseCode='"+parchasetext.getText()+"')";

                pst = conn.prepareStatement(sql);
                pst.execute();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
private void StockloadPlus() {

       try {
            String sql1 = "Select st.Qty as 'stockqty' from stockdetails st where st.barcode=(Select pc.barcode from purchasedetails pc where pc.barcode=st.barcode AND pc.purchaseCode='" + parchaseText.getText() + "' )";
            pst = conn.prepareStatement(sql1);

            rs = pst.executeQuery();

            while (rs.next()) {
                Float updateqty = rs.getFloat("stockqty");
                //String sql1 = "Select prcode,qty,unit , count(id)  from purchasedetails pc where pc.purchaseCode='" + parchasetext.getText() + "'.Update  stock st set st.Qty=pc.qty where st.procode=pc.prcode";
                String sql = "Update stockdetails sta set sta.Qty=(Select pc.qty+'" + updateqty + "'  from purchasedetails pc where pc.barcode=sta.barcode AND pc.purchaseCode='" + parchaseText.getText() + "') where (select pc.barcode from purchasedetails pc where pc.barcode=sta.barcode AND pc.purchaseCode='" + parchaseText.getText() + "')=sta.barcode";
                //  String sql="update stock sta set Qty=(select sum(pc.qty+(select st.Qty from stock st where st.procode=pc.prcode))from purchasedetails pc where sta.procode=pc.prcode AND pc.purchaseCode='"+parchasetext.getText()+"')";

                pst = conn.prepareStatement(sql);
                pst.execute();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

private void UpdatePlusSupplier(){
selectsupliyer();
suplyerinfo();
 try {
            double preamt = consighnmentamnt;
            double presntamt = minusAmount;
            double prentconsigntotal = preamt + presntamt;
            double afterblncedue = Balancedue + presntamt;

            String sql = "Update suplyierInfo set consighnmentamnt='" + prentconsigntotal + "',Balancedue='" + afterblncedue + "' where id='" + suplyer + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
           // JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }


}

private void loadPurchaseDetails() throws SQLException, IOException{
       String table_click = parchaseText.getText();
        PurchaseDetails filte = null;
        
        filte = new PurchaseDetails(table_click);
      

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
          
        //Homedesktop.add(It);
        filte.setVisible(true);

        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
        this.dispose();

}
private void purchasenewInsert(){

                parchase_code();
                parchaseInsert();
            try {
                parcheDetailsInsert();
                  loadGRN();
            } catch (SQLException | IOException ex) {
                Logger.getLogger(Purchase.class.getName()).log(Level.SEVERE, null, ex);
            }
           

}
private void purchaseVoidupdate(){

    purchasedeletehistory();
             purchaseupdate();
            try {
                parcheDetailsInsert();
                 //reload();
            } catch (SQLException | IOException ex) {
                Logger.getLogger(Purchase.class.getName()).log(Level.SEVERE, null, ex);
            }
           
                try {
                    loadPurchaseDetails();
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(Purchase.class.getName()).log(Level.SEVERE, null, ex);
                }


}

private void purchaseModification(){

             Minusamount();   
             UpdateMunusSupplier();
             StockloadMinus();
             purchasedeletehistory();
            // parchaseInsert();
            purchaseupdate();
            try {
                parcheDetailsInsert();
                 //reload();
            } catch (SQLException | IOException ex) {
                Logger.getLogger(Purchase.class.getName()).log(Level.SEVERE, null, ex);
            }
            Minusamount();
           UpdatePlusSupplier();
           StockloadPlus();
                try {
                    loadGRN();
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(Purchase.class.getName()).log(Level.SEVERE, null, ex);
                }


}
    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
         TableModel myTableModel = dataTable.getModel();
        final TableRowSorter<TableModel> sorter = new TableRowSorter<>(myTableModel);
        dataTable.setRowSorter(sorter);
        if (supliyerbox.getSelectedIndex() == 0 || totalrate.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Execute Faild, Due to Data Box Empty");
        } else {
            if(view==0){
                purchasenewInsert();
                try {
                     Barcodeinsert();
                     StockDetailsinsert();
                  // StockCheck();
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(NewPurchase.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                if(modifiaction==0)
                {
                    purchaseVoidupdate();
                }else{

                    purchaseModification();

                }

            }

        }

    }//GEN-LAST:event_svbtnActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        if(modifiaction==0)
        {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from purchase where purchaseCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, parchaseText.getText());

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                purchasedeletehistory();
                Purchase filte = null;

                try {
                    filte = new Purchase();
                } catch (SQLException ex) {
                    Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Purchase.class.getName()).log(Level.SEVERE, null, ex);
                }

                filte.setVisible(true);
                this.getDesktopPane().add(filte);

                //Homedesktop.add(It);
                filte.setVisible(true);

                Dimension desktopSize = getDesktopPane().getSize();
                Dimension jInternalFrameSize = filte.getSize();
                filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                    (desktopSize.height - jInternalFrameSize.height) / 2);
                filte.moveToFront();
                this.dispose();

            }

        }else{

            //delete Grn

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Grn Information", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                Minusamount();
                UpdateMunusSupplier();
                StockloadMinus();
                try {
                    String sql = "Delete from purchase where purchaseCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, parchaseText.getText());

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                purchasedeletehistory();

                try {
                    String sql = "Delete from grninfo where purchaseCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, parchaseText.getText());

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                GRNframe filte = null;

                try {
                    filte = new GRNframe();
                } catch (SQLException ex) {
                    Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Purchase.class.getName()).log(Level.SEVERE, null, ex);
                }

                filte.setVisible(true);
                this.getDesktopPane().add(filte);

                //Homedesktop.add(It);
                filte.setVisible(true);

                Dimension desktopSize = getDesktopPane().getSize();
                Dimension jInternalFrameSize = filte.getSize();
                filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                    (desktopSize.height - jInternalFrameSize.height) / 2);
                filte.moveToFront();
                this.dispose();

            }

        }
    }//GEN-LAST:event_deletebtnActionPerformed

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
//int cellselectioncellls=dataTable.getSelectedColumns();
int selectedRowIndex = dataTable.getSelectedRow();
if(dataTable.getSelectedColumn()==2){
  
   String pcode=model.getValueAt(selectedRowIndex, 1).toString();
          try {
                    String sql = "Select mrp from item it where it.itemcode='" + pcode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    if (rs.next()) {
                         
                        barcodemrp=rs.getString("mrp");
                       
                        //itemUnit=rs.getString("stockunit");
                        //includevat=rs.getFloat("includevate");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }   
    
barcodebtn.setText(model.getValueAt(selectedRowIndex, 2).toString());
barcodenamefromat=model.getValueAt(selectedRowIndex, 3).toString();
barcodecomment=model.getValueAt(selectedRowIndex, 13).toString();
}else{
    barcodebtn.setText("Barcode");
        pics_lbl.setIcon(null);
       

        //int selectedRowIndex = dataTable.getSelectedRow();
      
        updatekey=1;
        String pcode=model.getValueAt(selectedRowIndex, 1).toString();
        codetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        
        itemcode=model.getValueAt(selectedRowIndex, 1).toString();
          try {
                    String sql = "Select mrp,itemname,(select un.unitshort from unit un where un.id=it.stockunit) as 'unitshorts',vat from item it where it.itemcode='" + pcode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    if (rs.next()) {
                         
                        String mrp=rs.getString("mrp");
                        mrptext.setText(mrp);
                        includevat=rs.getFloat("vat");
                        
                        String name=rs.getString("itemName");
                        itemnamesearch.setSelectedItem(name);
                        
                        //itemUnit=rs.getString("stockunit");
                        //includevat=rs.getFloat("includevate");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }
        barcodetext.setText(model.getValueAt(selectedRowIndex, 2).toString());
       // itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 3).toString());
        allcolorbox.setSelectedItem(model.getValueAt(selectedRowIndex, 4).toString());
        sizebox.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());
        unitrateText.setText(model.getValueAt(selectedRowIndex, 6).toString());
        qtytext.setText(model.getValueAt(selectedRowIndex, 7).toString());
       // unibox.setSelectedItem(model.getValueAt(selectedRowIndex, 9).toString());
        float qty=Float.parseFloat((model.getValueAt(selectedRowIndex, 7).toString()));
        float discount=Float.parseFloat((model.getValueAt(selectedRowIndex, 8).toString()));
        float indiscount=discount/qty;
        discounttext.setText(df2.format(indiscount));
        qtytext.setBackground(Color.YELLOW);
        byte[] ImageData  = (byte[]) model.getValueAt(selectedRowIndex, 12);
        comentbox.setText(model.getValueAt(selectedRowIndex, 13).toString());
      // byte[] ImageData =Table1.getColumnModel().getColumn(12).setCellRenderer(new ImageRenderer()); 
                if (ImageData == null) {

                } else {

                    try {
                        //format = new ImageIcon(ImageData);
                        format = new ImageIcon(ScaleImage(ImageData, 150, 150));
                    } catch (IOException ex) {
                        Logger.getLogger(NewItem.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    pics_lbl.setIcon(format);

                }
                
}
    }//GEN-LAST:event_dataTableMouseClicked

    private void dataTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMousePressed
      
    }//GEN-LAST:event_dataTableMousePressed

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
      if (evt.getKeyCode() != KeyEvent.VK_DELETE) {

        } else {
           deleterow();
          }
    }//GEN-LAST:event_dataTableKeyPressed
 
    private void startbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startbtnActionPerformed
        /*    webSource =new VideoCapture(0);
            myThread = new DaemonThread();
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            myThread.runnable = true;
            t.start();
			 startbtn.setEnabled(false);  //start button
            stopbtn.setEnabled(true);  // stop button
        
        */
    }//GEN-LAST:event_startbtnActionPerformed

    private void stopbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopbtnActionPerformed
      /*  myThread.runnable = false;
            stopbtn.setEnabled(false);   
            startbtn.setEnabled(true);
            
            webSource.release();
        
        */
    }//GEN-LAST:event_stopbtnActionPerformed

    private void snapbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_snapbtnActionPerformed
        /*   //int returnVal = jFileChooser1.showSaveDialog(this);
       // if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = jFileChooser1.getSelectedFile();
       //String path= new File("").getAbsolutePath()+ "/src/Report/Purchasebydate.jrxml"
        Highgui.imwrite(new File("").getAbsolutePath()+"/src/snap/take.jpg", frame);
    // ImageIcon image = new ImageIcon("C:\\Users\\adnan\\Desktop\\utpol\\take.jpg");    
     String path=new File("").getAbsolutePath()+"/src/snap/take.jpg";
             pics_lbl.setIcon(ResizeImage(path));
    // pics_lbl.setIcon(image); 
   // } else {
        //System.out.println("File access cancelled by user.");
    //}
           myThread.runnable = false;
            stopbtn.setEnabled(false);   
            startbtn.setEnabled(true);
            
            webSource.release();
            itempanel.setSelectedIndex(0);
        
        
        */
    }//GEN-LAST:event_snapbtnActionPerformed

    private void camerapanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_camerapanelMouseClicked
      /*     if(startbtn.isEnabled()==false){
        myThread.runnable = false;
            stopbtn.setEnabled(false);   
            startbtn.setEnabled(true);
            
            webSource.release();
           
           
           }else{
            webSource =new VideoCapture(0);
            myThread = new DaemonThread();
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            myThread.runnable = true;
            t.start();
			 startbtn.setEnabled(false);  //start button
            stopbtn.setEnabled(true); 
           
           }

*/
    }//GEN-LAST:event_camerapanelMouseClicked

    private void itempanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itempanelMouseClicked
        
    }//GEN-LAST:event_itempanelMouseClicked

    private void jCheckBox2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox2MouseClicked

    private void supliyerbox1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supliyerbox1PopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_supliyerbox1PopupMenuWillBecomeInvisible

    private void dataTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTable1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_dataTable1MouseClicked

    private void dataTable1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTable1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dataTable1MousePressed

    private void dataTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTable1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dataTable1KeyPressed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2KeyReleased

    private void svbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_svbtn1ActionPerformed

    private void deletebtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deletebtn1ActionPerformed

    private void codetext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetext1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_codetext1KeyPressed

    private void codetext1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetext1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_codetext1KeyReleased

    private void itemnamesearch1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearch1PopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_itemnamesearch1PopupMenuWillBecomeInvisible

    private void itemnamesearch1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearch1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_itemnamesearch1KeyTyped

    private void qtytext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytext1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_qtytext1KeyPressed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void allcolorbox1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_allcolorbox1PopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_allcolorbox1PopupMenuWillBecomeInvisible

    private void vatcheck1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vatcheck1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_vatcheck1MouseClicked

    private void categorybox1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_categorybox1PopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_categorybox1PopupMenuWillBecomeInvisible

    private void categorybox1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_categorybox1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_categorybox1KeyTyped

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void camerapanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_camerapanel1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_camerapanel1MouseClicked

    private void startbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startbtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startbtn1ActionPerformed

    private void stopbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopbtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stopbtn1ActionPerformed

    private void snapbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_snapbtn1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_snapbtn1ActionPerformed

    private void jTabbedPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPane2MouseClicked

    private void itemcodetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemcodetextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemcodetextKeyPressed

    private void itemcodetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemcodetextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_itemcodetextKeyReleased

    private void itemnametextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnametextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemnametextKeyPressed

    private void itemnametextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnametextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_itemnametextKeyReleased

    private void categoryboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_categoryboxPopupMenuWillBecomeInvisible
         if(categorybox.getSelectedIndex()>0){
            subcategorybox.removeAllItems();
             subcategorybox.addItem("Select");
        subcategorybox.setSelectedIndex(0);
            try {
            String category = (String) categorybox.getSelectedItem();
            String sql = "Select id from category where name='" + category + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                catid = rs.getString("id");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
       
        try {
            String sql = "Select name from subcategory where catid='" + catid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String subcategory = rs.getString("name");
                subcategorybox.addItem(subcategory);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        } 
       
       }
    }//GEN-LAST:event_categoryboxPopupMenuWillBecomeInvisible

    private void categoryboxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_categoryboxKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_categoryboxKeyTyped

    private void vatcheckMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vatcheckMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_vatcheckMouseClicked

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
     ItemCode();
        try {
      addStock_insert();
     
    } catch (SQLException ex) {
        Logger.getLogger(NewPurchase.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    }//GEN-LAST:event_jButton13ActionPerformed

    private void barcodetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barcodetextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();
            qtytext.setBackground(Color.YELLOW);

        }
    }//GEN-LAST:event_barcodetextKeyPressed

    private void barcodetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barcodetextKeyReleased
     String SearchText = (String) barcodetext.getText();
              try {
                String sql = "Select it.Itemcode as 'itemcode',itemName as 'itemName',st.tp as 'tp',st.mrp as 'mrp',it.vat as 'vat',(select un.unitshort from unit un where un.id=it.stockunit) as 'unitshorts',st.color as 'color',st.size as 'size',comment from item it inner join barcodeproduct st ON it.Itemcode=st.itemcode where st.barcode='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {

                   itemcode = rs.getString("itemcode");
                   codetext.setText(itemcode);
                   itemname=rs.getString("itemName");
                   itemnamesearch.setSelectedItem(itemname);
                   unitrate = rs.getString("tp");
                   unitrateText.setText(unitrate);
                   mrpvalue = rs.getString("mrp");
                   mrptext.setText(mrpvalue);
                   includevat=rs.getFloat("vat");
                   
                   String size=rs.getString("size");
                   sizebox.setSelectedItem(size);
                   String color=rs.getString("color");
                   allcolorbox.setSelectedItem(color);
                   float tprice = rs.getFloat("tp");
                   float mrprice = rs.getFloat("mrp");
                 
                    float profite=(mrprice-tprice/100*tprice);
                     String pro=String.format("%.2f", profite);
                     proftetext.setText(pro +"%");

                    // qtytext.requestFocusInWindow();
                     //qtytext.setBackground(Color.YELLOW);
                   
                }else{
                clear();
                } 

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
    }//GEN-LAST:event_barcodetextKeyReleased

    private void unitrateTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateTextKeyReleased
     
        if(unitrateText.getText().isEmpty()){
        
        }else{
        barcodeManagment();
        barcodetext.setText(barcode);
        
        }
        
    }//GEN-LAST:event_unitrateTextKeyReleased

    private void discounttextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discounttextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();
            qtytext.setBackground(Color.YELLOW);

        }
    }//GEN-LAST:event_discounttextKeyPressed

    private void comentboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comentboxKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();
            qtytext.setBackground(Color.YELLOW);

        }
    }//GEN-LAST:event_comentboxKeyPressed

    private void unibox2PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_unibox2PopupMenuWillBecomeInvisible
         try {
            String sql = "Select id from unit where unitshort='"+unibox2.getSelectedItem()+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                
               unitid = rs.getInt("id");
             

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_unibox2PopupMenuWillBecomeInvisible

    private void unitrateTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateTextKeyPressed
       if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();
            qtytext.setBackground(Color.YELLOW);

        }
    }//GEN-LAST:event_unitrateTextKeyPressed

    private void mrptextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrptextKeyPressed
      if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();
            qtytext.setBackground(Color.YELLOW);

        }
    }//GEN-LAST:event_mrptextKeyPressed

    private void sizeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_sizeboxPopupMenuWillBecomeInvisible
        String sizename=(String) sizebox.getSelectedItem();
        
        try {
            String sql = "Select id from itemsize where size='"+sizename+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

               sizeid=rs.getString("id");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        barcodeManagment();
        barcodetext.setText(barcode);
        qtytext.requestFocusInWindow();
    }//GEN-LAST:event_sizeboxPopupMenuWillBecomeInvisible

    private void subcategoryboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_subcategoryboxPopupMenuWillBecomeInvisible
       if(subcategorybox.getSelectedIndex()>0){
        
        try {
            String category = (String) subcategorybox.getSelectedItem();
            String sql = "Select id from subcategory where name='" + category + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                scatid = rs.getString("id");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        
        
        }
    }//GEN-LAST:event_subcategoryboxPopupMenuWillBecomeInvisible

    private void subcategoryboxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_subcategoryboxKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_subcategoryboxKeyTyped

    private void barcodebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barcodebtnActionPerformed
       if("Barcode".equals(barcodebtn.getText())){
       
       
       
       }else{
    String Imei=barcodebtn.getText();
          try {
JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/Barcode.jrxml");
  
               // String Imei=barcodetext.getText();
              HashMap para = new HashMap();
              para.put("imeinumber", Imei);
              para.put("formatname", barcodenamefromat);
              para.put("mrp", barcodemrp);
              para.put("comment", barcodecomment);
             
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

                if(printaccess.isSelected()==true){
                
                JasperPrintManager.printReport(jp, true);
                
                
                }else{
                JasperViewer.viewReport(jp, false);
                
                }
                
                
              barcodebtn.setText("Barcode");

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }
    
    }
    }//GEN-LAST:event_barcodebtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> allcolorbox;
    private javax.swing.JComboBox<String> allcolorbox1;
    private javax.swing.JButton barcodebtn;
    private javax.swing.JTextField barcodetext;
    private javax.swing.JPanel camerapanel;
    private javax.swing.JPanel camerapanel1;
    private javax.swing.JComboBox<String> categorybox;
    private javax.swing.JComboBox<String> categorybox1;
    private javax.swing.JTextField codetext;
    private javax.swing.JTextField codetext1;
    private javax.swing.JTextField comentbox;
    private javax.swing.JTable dataTable;
    private javax.swing.JTable dataTable1;
    private javax.swing.JButton deletebtn;
    private javax.swing.JButton deletebtn1;
    private javax.swing.JTextField discounttext;
    private javax.swing.JTextField discounttext1;
    private javax.swing.JTextField itemcodetext;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JComboBox<String> itemnamesearch1;
    private javax.swing.JTextField itemnametext;
    private javax.swing.JTabbedPane itempanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lottext;
    private javax.swing.JLabel lottext1;
    private javax.swing.JTextField mrptext;
    private javax.swing.JTextField mrptext1;
    private javax.swing.JTextField mrptext2;
    private javax.swing.JTextField netdiscounttext;
    private javax.swing.JTextField netdiscounttext1;
    private javax.swing.JLabel nettotaltext;
    private javax.swing.JLabel nettotaltext1;
    private javax.swing.JLabel parchaseText;
    private javax.swing.JLabel parchaseText1;
    private com.toedter.calendar.JDateChooser parchasedate;
    private com.toedter.calendar.JDateChooser parchasedate1;
    private javax.swing.JLabel pics_lbl;
    private javax.swing.JLabel pics_lbl1;
    private javax.swing.JCheckBox printaccess;
    private javax.swing.JLabel proftetext;
    private javax.swing.JLabel proftetext1;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextField qtytext1;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JTextArea remarktext1;
    private javax.swing.JComboBox<String> sizebox;
    private javax.swing.JTextField sizetext1;
    private javax.swing.JButton snapbtn;
    private javax.swing.JButton snapbtn1;
    private javax.swing.JButton startbtn;
    private javax.swing.JButton startbtn1;
    private javax.swing.JButton stopbtn;
    private javax.swing.JButton stopbtn1;
    private javax.swing.JComboBox<String> subcategorybox;
    private javax.swing.JComboBox<String> supliyerbox;
    private javax.swing.JComboBox<String> supliyerbox1;
    private javax.swing.JButton svbtn;
    private javax.swing.JButton svbtn1;
    private javax.swing.JLabel totalrate;
    private javax.swing.JLabel totalrate1;
    private javax.swing.JLabel totalvattext;
    private javax.swing.JLabel totalvattext1;
    private javax.swing.JComboBox<String> unibox1;
    private javax.swing.JComboBox<String> unibox2;
    private javax.swing.JTextField unitrateText;
    private javax.swing.JTextField unitrateText1;
    private javax.swing.JTextField unitrateText2;
    private javax.swing.JCheckBox vatcheck;
    private javax.swing.JCheckBox vatcheck1;
    private javax.swing.JTextField vattext;
    private javax.swing.JTextField vattext1;
    // End of variables declaration//GEN-END:variables

 String filename = null;
    int s = 0;
    byte[] person_image = null;

    private ImageIcon format = null;
}
