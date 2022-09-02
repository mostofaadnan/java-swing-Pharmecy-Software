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
import java.util.regex.PatternSyntaxException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
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

/**
 *
 * @author adnan
 */
public class PurchaseEntry extends javax.swing.JFrame {
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
    String itemcode;
    String proitemcode;
    String itemname;
    
    
    
    float itemmrp;
    String itemUnit;
    
    // definitions
 //private DaemonThread myThread = null;
    int count = 0;
    
    String pcode=null;
    String GRNsuplyer=null;
    
   // VideoCapture webSource = null;

   // Mat frame = new Mat();admin
   // MatOfByte mem = new MatOfByte();
    /**
     * Creates new form NewPurchase
     * @throws java.io.IOException
     * @throws java.sql.SQLException
    /**
     * Creates new form PurchaseEntry
     */
    public PurchaseEntry() throws IOException, SQLException {
        initComponents();
         conn = Java_Connect.conectrDB();
        userkey();
        AutoCompleteDecorator.decorate(itemnamesearch);
        AutoCompleteDecorator.decorate(unibox);
        AutoCompleteDecorator.decorate(supliyerbox);
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
        purchanobox();
    }
    ////Active GRn////
    
     private void selectsupliyerGRN() {
        try {

            String sql = "Select  supliyer from purchase where purchaseCode='" + pcode + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            if (rs.next()) {

                GRNsuplyer = rs.getString("supliyer");

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    //END GRN//
    
    
    
    
    
    
    
    
    
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
  
     private void purchanobox(){
         purchasebox.removeAllItems();
         purchasebox.addItem("Select");
         purchasebox.setSelectedIndex(0);
     try {
            String sql = "Select purchaseCode from purchase where GRNstatus=0";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("purchaseCode");
                purchasebox.addItem(name);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
     
     
     
     }
     
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
                allcolorbox2.addItem(name);
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
         
        if (itemnametext.getText().isEmpty() || categorybox.getSelectedIndex() == 0 || supliyerbox.getSelectedIndex() == 0 || unibox2.getSelectedIndex() == 0 || unitrateText2.getText().isEmpty() || mrptext2.getText().isEmpty()) {
JOptionPane.showMessageDialog(null, "Requirment Error");
        } else {
        
            try {

                String sql = "Insert into item(itemcode,itemName,category,supliyer,stockunit,openingDate,tp,mrp,includevate,vat,inputuser,updateuser,lastupdate,status) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
               // pst = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
                pst = conn.prepareStatement(sql);
                pst.setString(1, itemcodetext.getText());
               
                pst.setString(2, itemnametext.getText());
                pst.setString(3, catid);

                pst.setInt(4, supid);
                pst.setInt(5, unitid);

                pst.setString(6, ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText());
                pst.setString(7, unitrateText2.getText());
                pst.setString(8, mrptext2.getText());
                if(vatcheck.isSelected()){
                pst.setInt(9,1);
                pst.setString(10,vattext.getText());
                }else{
                pst.setInt(9,0);
                pst.setDouble(10,0);
                }
                pst.setInt(11, userid);
                pst.setInt(12, userid);
                pst.setString(13, ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText());
                pst.setString(14, "0");

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
                   String unit = rs.getString("unitshorts");
                   unibox.setSelectedItem(unit);
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
                unibox.addItem(name);
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
        unibox.setSelectedIndex(0);
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
             String itemcodeformat=String.format("%05d",itemcodei);
             String countrycod="1";
             String barcodebefor=itemcodeformat+colorformat+sizeformat+supidformaat;
             
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

            if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0) {
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
        unibox.setSelectedIndex(0);
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
   /* if(modifiaction==0){
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
    
    */
    
    
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
       /*String table_click = parchaseText.getText();
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
    */

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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
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
        jPanel9 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        purchasebox = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        itempanel = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        unitrateText = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        discounttext = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        unibox = new javax.swing.JComboBox<>();
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
        jLabel21 = new javax.swing.JLabel();
        allcolorbox2 = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        sizetext2 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        camerapanel = new javax.swing.JPanel();
        startbtn = new javax.swing.JButton();
        stopbtn = new javax.swing.JButton();
        snapbtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(6);

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

        jPanel9.setBackground(new java.awt.Color(67, 86, 86));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Purchase No:");

        jButton4.setText("View");

        purchasebox.setEditable(true);
        purchasebox.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        purchasebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        jButton3.setText("GRN Execution");

        jButton7.setText("Load Barcode");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(purchasebox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton7)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchasebox, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                .addGap(5, 5, 5)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel10)
                        .addGap(19, 19, 19)
                        .addComponent(lottext, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addGap(5, 5, 5)
                        .addComponent(proftetext, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(parchasedate, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(parchaseText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(parchasedate, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(supliyerbox)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lottext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(proftetext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 121, Short.MAX_VALUE)
        );

        itempanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itempanelMouseClicked(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(67, 86, 86));

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

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Unit ");

        unibox.setEditable(true);

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
                .addContainerGap(17, Short.MAX_VALUE))
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
                    .addComponent(allcolorbox, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel1)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24))
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(discounttext)
                    .addComponent(unibox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(unitrateText)
                    .addComponent(itemnamesearch, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(codetext)
                    .addComponent(mrptext, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comentbox)
                    .addComponent(barcodetext)
                    .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(codetext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(barcodetext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addGap(1, 1, 1))
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comentbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        itempanel.addTab("Item Entry", jPanel3);

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
        unitrateText2.setBorder(null);

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("MRP");

        mrptext2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        mrptext2.setForeground(new java.awt.Color(102, 0, 0));
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

        jLabel57.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(51, 51, 51));
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
        unibox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        unibox2.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                unibox2PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Color");

        allcolorbox2.setEditable(true);
        allcolorbox2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        allcolorbox2.setBorder(null);
        allcolorbox2.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                allcolorbox2PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Size:");

        sizetext2.setText("1");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(63, 63, 63)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(allcolorbox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(sizetext2)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(vatcheck, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel56)
                                .addGap(18, 18, 18)
                                .addComponent(vattext, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(mrptext2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(unitrateText2)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel50)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemnametext)
                            .addComponent(itemcodetext)
                            .addComponent(categorybox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(unibox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(10, 10, 10))
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
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(allcolorbox2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sizetext2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(187, Short.MAX_VALUE))
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

        jScrollPane1.setPreferredSize(new java.awt.Dimension(1502, 472));

        jPanel4.setBackground(new java.awt.Color(0, 51, 51));

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
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(180);
            dataTable.getColumnModel().getColumn(3).setPreferredWidth(300);
        }

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Search");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel19)
                .addGap(2, 2, 2)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1000, Short.MAX_VALUE))
            .addComponent(jScrollPane4)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jScrollPane1.setViewportView(jPanel4);

        jPanel8.setBackground(new java.awt.Color(67, 86, 86));

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(netdiscounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(99, 99, 99)))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nettotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(netdiscounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nettotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 17, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deletebtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(svbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1362, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(itempanel, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGap(0, 447, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 915, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itempanel)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(128, 128, 128)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(123, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("tab1", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseClicked
        try {

            supliyerbox.removeAllItems();
            supliyerbox.setSelectedItem("Select");

            itemnamesearch.removeAllItems();
            itemnamesearch.setSelectedItem("Select");
            unibox.removeAllItems();
            unibox.setSelectedItem("Select");
            Item();
            unit();
            suppliyer();
        } catch (SQLException ex) {
            Logger.getLogger(Purchase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jCheckBox1MouseClicked

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
                String unit = rs.getString("unitshorts");
                unibox.setSelectedItem(unit);
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
                    String unit = rs.getString("unitshorts");
                    unibox.setSelectedItem(unit);
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

    private void unitrateTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateTextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();
            qtytext.setBackground(Color.YELLOW);

        }
    }//GEN-LAST:event_unitrateTextKeyPressed

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

    private void mrptextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrptextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();
            qtytext.setBackground(Color.YELLOW);

        }
    }//GEN-LAST:event_mrptextKeyPressed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        pics_lbl.setIcon(null);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        imageupload();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void comentboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_comentboxKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();
            qtytext.setBackground(Color.YELLOW);

        }
    }//GEN-LAST:event_comentboxKeyPressed

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
                String unit = rs.getString("unitshorts");
                unibox.setSelectedItem(unit);
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
        try {
            String category = (String) categorybox.getSelectedItem();
            String sql = "Select * from category where name='" + category + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                catid = rs.getString("id");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

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

    private void allcolorbox2PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_allcolorbox2PopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_allcolorbox2PopupMenuWillBecomeInvisible

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

    private void itempanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itempanelMouseClicked

    }//GEN-LAST:event_itempanelMouseClicked

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        pics_lbl.setIcon(null);
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        int selectedRowIndex = dataTable.getSelectedRow();

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
                String unit = rs.getString("unitshorts");
                unibox.setSelectedItem(unit);
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
    }//GEN-LAST:event_dataTableMouseClicked

    private void dataTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMousePressed

    }//GEN-LAST:event_dataTableMousePressed

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
        deleterow();
    }//GEN-LAST:event_dataTableKeyPressed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        TableModel myTableModel = dataTable.getModel();
        final TableRowSorter<TableModel> sorter = new TableRowSorter<>(myTableModel);
        dataTable.setRowSorter(sorter);
        String text = jTextField1.getText();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
            dataTable.clearSelection();
        } else {
            try {
                // sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1));
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 3));
                dataTable.clearSelection();
            } catch (PatternSyntaxException pse) {
                JOptionPane.showMessageDialog(null, "Bad regex pattern",
                    "Bad regex pattern", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_jTextField1KeyReleased

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
               /* Purchase filte = null;

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
                
                */

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
                /*
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
                */

            }

        }
    }//GEN-LAST:event_deletebtnActionPerformed

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
            java.util.logging.Logger.getLogger(PurchaseEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PurchaseEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PurchaseEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PurchaseEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new PurchaseEntry().setVisible(true);
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(PurchaseEntry.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> allcolorbox;
    private javax.swing.JComboBox<String> allcolorbox2;
    private javax.swing.JTextField barcodetext;
    private javax.swing.JPanel camerapanel;
    private javax.swing.JComboBox<String> categorybox;
    private javax.swing.JTextField codetext;
    private javax.swing.JTextField comentbox;
    private javax.swing.JTable dataTable;
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField discounttext;
    private javax.swing.JTextField itemcodetext;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JTextField itemnametext;
    private javax.swing.JTabbedPane itempanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
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
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lottext;
    private javax.swing.JTextField mrptext;
    private javax.swing.JTextField mrptext2;
    private javax.swing.JTextField netdiscounttext;
    private javax.swing.JLabel nettotaltext;
    private javax.swing.JLabel parchaseText;
    private com.toedter.calendar.JDateChooser parchasedate;
    private javax.swing.JLabel pics_lbl;
    private javax.swing.JLabel proftetext;
    private javax.swing.JComboBox<String> purchasebox;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JComboBox<String> sizebox;
    private javax.swing.JTextField sizetext2;
    private javax.swing.JButton snapbtn;
    private javax.swing.JButton startbtn;
    private javax.swing.JButton stopbtn;
    private javax.swing.JComboBox<String> supliyerbox;
    private javax.swing.JButton svbtn;
    private javax.swing.JLabel totalrate;
    private javax.swing.JLabel totalvattext;
    private javax.swing.JComboBox<String> unibox;
    private javax.swing.JComboBox<String> unibox2;
    private javax.swing.JTextField unitrateText;
    private javax.swing.JTextField unitrateText2;
    private javax.swing.JCheckBox vatcheck;
    private javax.swing.JTextField vattext;
    // End of variables declaration//GEN-END:variables
String filename = null;
    int s = 0;
    byte[] person_image = null;

    private ImageIcon format = null;
}
