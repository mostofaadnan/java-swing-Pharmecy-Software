/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


/**
 *
 * @author adnan
 */
public final class CreditPos extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    
    float tpprice;
    float bankbalance1;
    int tree = 0;
    String Invoiceno;

    String cusId;

    float openingbalance;
    float dipositamt;
   
    float salesamt;
    float balancedue;
    float paidamt;
    float totaldiscount;
    String Paymenttype="Credit";
     int new_inv;
    float afterbalancedue;
     float balanced;
     java.sql.Date date;
     int updatekey=0;
     int id=1;
     double creditAmnt;
    // Dashboard dashboard = new Dashboard();
    int userid =0;
  int offer;
  String offerid; 
  String username=null;
  int orderupdate=0;
   float includevat=0;
    /**
     * Creates new form CreditPos
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public CreditPos() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0,screenSize.width, screenSize.height);
         userkey();
        // executebtn.setEnabled(false);
        intilize();
        currentDate();
        Item();
        customer();
        AutoCompleteDecorator.decorate(unibox);
        AutoCompleteDecorator.decorate(itemnamesearch);
        AutoCompleteDecorator.decorate(customerbox);
        unit();
        currency();
        
        invoicetext.setEnabled(false);
        boxcheck();
        paneldactive();
      //  editinvoiceno();
      //  deleterow();
       // entrucesure();
       creditumber();
      // uInformation();
       parchase_code();
       offer(); 
       username();
        order();
        AutoCompleteDecorator.decorate(orderNobox);
    }
    private void updateSystemProcess(){
    if(updatekey==1){
    
    codetext.setEditable(false);
    itemnamesearch.setEditable(false);
     itemnamesearch.setEnabled(false);
    }else{
    codetext.setEditable(true);
    itemnamesearch.setEditable(true);
    itemnamesearch.setEnabled(true);
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
    private void intilize() {

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("company.png")));
    }
     private void username() throws SQLException {

        try {
            String sql = "Select name from admin where id='"+userid+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                username = rs.getString("name");


            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
     
       private void order() {
        orderNobox.removeAllItems();
        orderNobox.addItem("Select");
        orderNobox.setSelectedItem("Select");
        String status = "0";
        try {
            String sql = "Select id from creditsaleorder where status='" + status + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("id");
                orderNobox.addItem(name);
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    
      private void OderInsert() {
  
        try {

            String sql = "Insert into creditsaleorder(orderdate,cusid,inputuserid,status) values (?,?,?,?)";
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
           
            pst.setDate(1, date);
            pst.setString(2, cusId);
            pst.setInt(3, userid);
            pst.setInt(4, 0);
            
            pst.execute();
             ResultSet rshere = pst.getGeneratedKeys();
            int generatedKey = 0;
            if (rshere.next()) {
                generatedKey = rshere.getInt(1);
                orderDetailsInsert(generatedKey);
                // PrintOrder(generatedKey);
            }
           

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
      
      private void orderDetailsInsert(int id) throws SQLException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String procode = (String) dataTable.getValueAt(row, 1);
             

                float rate = (Float) dataTable.getValueAt(row, 3);

                float qty = (Float) dataTable.getValueAt(row, 4);

                String unit = (String) dataTable.getValueAt(row, 5);
                float discount = (Float) dataTable.getValueAt(row, 6);
                float totalamount = (Float) dataTable.getValueAt(row, 7);

                float vat = (Float) dataTable.getValueAt(row, 8);
                float totalvat = (Float) dataTable.getValueAt(row, 9);
                float neTotal = (Float) dataTable.getValueAt(row, 10);

                try {

                    String sql = "Insert into creditorderdetails(orderNo,prcode,unitrate,qty,unit,discount,totalamount,vat,Totalvat,NetTotal) values (?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1, id);
                    pst.setString(2, procode);
                    pst.setFloat(3, rate);
                    pst.setFloat(4, qty);
                    pst.setString(5, unit);
                    pst.setFloat(6, discount);
                  
                    pst.setFloat(7, totalamount);
                    pst.setFloat(8, vat);
                    pst.setFloat(9, totalvat);
                    pst.setFloat(10, neTotal);
                    
                    pst.execute();

///    examperticipationinsert(id,examcode,exam);
                    // 
                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }

            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }   
             private void OrderStatus() {
        try {
            int status=1;
            String orderid =(String) orderNobox.getSelectedItem();

            String sql = "Update creditsaleorder set status='" + status + "'AND orderdate='"+date+"' where id='" + orderid + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
           

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        
            try {
                String sql = "Delete from creditsaleorder where id=? ";
                pst = conn.prepareStatement(sql);

                pst.setString(1, (String) orderNobox.getSelectedItem());

                pst.execute();
                

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            try {
                String sql = "Delete from creditorderDetails where orderNo=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, (String) orderNobox.getSelectedItem());

                pst.execute();
                

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

    } 
        
        private void orderUpdate() throws SQLException{
            try {
                String sql = "Delete from creditorderDetails where orderNo=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, (String) orderNobox.getSelectedItem());

                pst.execute();
                

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            String oderids=(String)orderNobox.getSelectedItem();
            int oderid=Integer.parseInt(oderids);
            orderDetailsInsert(oderid);
           // PrintOrder(oderid);
        
        }
        private void offer() throws SQLException {

        try {
            String sql = "Select id,startdate,enddate from offerlist where  startdate='"+date+"' AND enddate='"+date+"'";
            pst = conn.prepareStatement(sql);
           
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

               offer=1;
               offerid=rs.getString("id");
            }
            else{
            offer=0;
            offerid=null;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
      /*  public void uInformation() {

        try {

            String sql = "Select * from admin where id='" + id + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                uname.setText(username);
                String Name = rs.getString("name");
                usernametext.setText(Name);

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        // usernametext.setText(LoginUserid);
        // loginu=usernametext.getText();
    }
        
        */
     private void creditumber() throws SQLException {

        try {
            String sql = "Select number	 from numberformat where id=2";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                new_inv = rs.getInt("number");
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
     
  
    private void panelactive(){
    codetext.setEnabled(true);
    itemnamesearch.setEnabled(true);
    unitrateText.setEnabled(true);
    qtytext.setEnabled(true);
    unibox.setEnabled(true);
    discounttext.setEnabled(true);
    addbtn.setEnabled(true);        

    }
    private void paneldactive(){
    codetext.setEnabled(false);
    itemnamesearch.setEnabled(false);
    unitrateText.setEnabled(false);
    qtytext.setEnabled(false);
    unibox.setEnabled(false);
    discounttext.setEnabled(false);
    addbtn.setEnabled(false);        

    }
      
      private void entrucesure(){
          
          
      if(customerbox.getSelectedIndex()==0){
      
      paneldactive();
      }else{
      panelactive();
      
      }
      
      
      }
    private void boxcheck() {

        if (netstext.getText().isEmpty()) {

            customerbox.setEnabled(true);
        } else {
            customerbox.setEnabled(false);

        }
    }
/*
    final private static String[] units = {"Zero", "One", "Two", "Three", "Four",
        "Five", "Six", "Seven", "Eight", "Nine", "Ten",
        "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen",
        "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    final private static String[] tens = {"", "", "Twenty", "Thirty", "Forty", "Fifty",
        "Sixty", "Seventy", "Eighty", "Ninety"};

    public static String convert(Integer i) {
        //
        if (i < 20) {
            return units[i];
        }
        if (i < 100) {
            return tens[i / 10] + ((i % 10 > 0) ? " " + convert(i % 10) : "");
        }
        if (i < 1000) {
            return units[i / 100] + " Hundred" + ((i % 100 > 0) ? " and " + convert(i % 100) : "");
        }
        if (i < 1000000) {
            return convert(i / 1000) + " Thousand " + ((i % 1000 > 0) ? " " + convert(i % 1000) : "");
        }
        return convert(i / 1000000) + " Million " + ((i % 1000000 > 0) ? " " + convert(i % 1000000) : "");
    }
*/
    public void balance() throws SQLException {

        try {
            String sql = "Select sum(cashin),sum(cashout) from CashDrower";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {

                float cashin = rs.getFloat("sum(cashin)");
                float cashout = rs.getFloat("sum(cashout)");
                bankbalance1 = (cashin - cashout);

            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void parchase_code() {
        
       
        try {
           
            String NewParchaseCode = ("DI-" + new_inv + "");
            String sql = "Select id from sale";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = ("DI-" + ParchaseCode + "");
            }

            invoicetext.setText(NewParchaseCode);
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

     private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        inputdate.setDate(date);

    }
     
         private void inputDate(){
    date =convertUtilToSql(inputdate.getDate());
    
    }
private static java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());

        return sDate;
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

   private void customer() throws SQLException {
       String type="Credit";
       String type2="Credit And Cash";
       
        try {
            String sql = "Select customername from customerInfo where balanceType='"+type+"' OR balanceType='"+type2+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("customername");
                customerbox.addItem(name);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

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

                unibox.addItem(name);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
   private void country() throws SQLException {

        try {
            String sql = "Select * from country";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("name");

               // countrybox.addItem(name);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void currency() throws SQLException {

        try {
            String sql = "Select * from currency";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("currency");

                currencybox.addItem(name);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void AfterExecute() {

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);
        totalrate.setText(null);
        totalvattext.setText(null);
        totalinvoiceamount.setText(null);
        
        customerbox.setSelectedIndex(0);
        customeridtext.setText(null);
       
        previseamonmttext.setText(null);
        netstext.setText(null);
        
        netdiscount.setText(null);
        parchase_code();
        //tablenobox.setSelectedIndex(0);
        order();
        orderupdate=0;
      //  executebtn.setEnabled(false);
        orderbtn.setText("Hold Order");
        codetext.requestFocusInWindow(); 
        currentDate();
        paneldactive();
               
    }

    private void checkentry() {

        String s = "";
        boolean exists = false;
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            s = dataTable.getValueAt(i, 1).toString().trim();

            if (codetext.getText().equals("")) {
                // JOptionPane.showMessageDialog(null, "Enter Invoice Details First");
            } else if (codetext.getText().equals(s)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            entryData();
        } else {
            JOptionPane.showMessageDialog(null, "This Data Already Exist.");
            clear();

        }

    }
private static final DecimalFormat df2 = new DecimalFormat("#.00");
    private void entryData() {
        float discount;
        String discounttype;
        float vat = includevat / 100;
        float totalvat, productvat;

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        tree++;

        float tpd = Float.parseFloat(unitrateText.getText());

          if (discounttext.getText().isEmpty()) {

                discount = 0;

            } else {
                discount = Float.parseFloat(discounttext.getText());

            }

        float tpwdisc = tpd - discount;
        productvat = (tpwdisc * vat);
        float productvatfloat = Float.parseFloat(df2.format(productvat));

        float qty = Float.parseFloat(qtytext.getText());
        float totaldics = discount * qty;
        float totaldiscountformat = Float.parseFloat(df2.format(totaldics));

        float nettotaltp = (tpd * qty);
        totalvat = (productvat * qty);

        float totalvatfloat = Float.parseFloat(df2.format(totalvat));
        float totalall = nettotaltp + totalvatfloat - totaldiscountformat;
       
        //String gpper = String.format("%.2f", gp);
        model2.addRow(new Object[]{tree, codetext.getText(), itemnamesearch.getSelectedItem(), tpd, qty, unibox.getSelectedItem(), discount, nettotaltp, productvatfloat, totalvatfloat, totalall});
        ///  totalrate.setText(Integer.toString(total_action_rate()));
        
        
        double inputotalrate =total_action_mrp();
        totalrate.setText(df2.format(inputotalrate));
       
        double inputvat =total_action_vat();
        totalvattext.setText(df2.format(inputvat));
       
        double input = total_invoice_amount();

        totalinvoiceamount.setText(df2.format(input));
       
        
        
        netdiscount.setText(Float.toString(total_discount_amount()));
       
        

        float totalinvoce = total_invoice_amount();
        float prebamt = Float.parseFloat(previseamonmttext.getText());
        float nettotalin = totalinvoce + prebamt;
        String nets = String.format("%.2f", nettotalin);
        netstext.setText(nets);

        clear();
    }
    private void deleterow() {

        int[] toDelete = dataTable.getSelectedRows();
        Arrays.sort(toDelete); // be shure to have them in ascending order.
        DefaultTableModel myTableModel = (DefaultTableModel) dataTable.getModel();
        for (int ii = toDelete.length - 1; ii >= 0; ii--) {
            myTableModel.removeRow(toDelete[ii]); // beginning at the largest.
        }
        double inputotalrate =total_action_mrp();
        totalrate.setText(df2.format(inputotalrate));
       
        double inputvat =total_action_vat();
        totalvattext.setText(df2.format(inputvat));
       
        double input = total_invoice_amount();
        totalinvoiceamount.setText(df2.format(input));
      
        netdiscount.setText(Float.toString(total_discount_amount()));
        
        
      float totalinvoce = total_invoice_amount();
      float prebamt = Float.parseFloat(previseamonmttext.getText());
      float nettotalin = totalinvoce + prebamt;
       String nets = String.format("%.2f", nettotalin);
       netstext.setText(nets);
       updatekey=0;
        clear();
    }
    private float total_action_mrp() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 7).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_vat() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 9).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_invoice_amount() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 10).toString());
        }

        return (float) totaltpmrp;

    }
    
      private float total_discount_amount() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 6).toString());
        }

        return (float) totaltpmrp;

    }

    private void clear() {
        codetext.setText(null);
        itemnamesearch.setSelectedIndex(0);
        unitrateText.setText(null);
        qtytext.setText(null);
        unibox.setSelectedIndex(0);
        discounttext.setText(null);
       updatekey=0;
       updateSystemProcess();
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
if(model2.getRowCount()==0){
AfterExecute();
}  
if(model2.getRowCount()==0 && orderNobox.getSelectedIndex()>0){
OrderStatus();
AfterExecute();
orderbtn.setText("Hold Order");
}
    }

    private void invoicecheck(){
    
        try {
            String sql = "Select id from sale";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
        if(rs.next()==false){
        saleInsertcheck();
        
        }else{
        saleInsert();
        
        
        }
        } catch (SQLException | JRException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        
       
    }
    
    
 
    
    private void saleInsertcheck() throws JRException {
     //   float chasin = Float.parseFloat(totalinvoiceamount.getText());
     
   
        try {
                  float totaltd = totaltrade();
            float totalamt=Float.parseFloat(totalrate.getText());
            float totalprofite=totalamt-totaltd;
            float discount=Float.parseFloat(netdiscount.getText());
            float profite=totalprofite-discount;
            String sql = "Insert into sale(id,invoiceNo,invoicedate,paymentCurency,paymentType,netdiscount,TotalAmount,TotalVat,Totalinvoice,customerid,Ref_No, totaltraprice,totalprofite,inputuserid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            
            pst.setInt(1, new_inv);
            pst.setString(2, invoicetext.getText());
            pst.setDate(3, date);
            pst.setString(4, (String) currencybox.getSelectedItem());
            pst.setString(5, (String) Paymenttype);
            pst.setString(6, netdiscount.getText());
            pst.setString(7, totalrate.getText());
            pst.setString(8, totalvattext.getText());
            pst.setString(9, totalinvoiceamount.getText());
            pst.setString(10, cusId);
            pst.setString(11, reftext.getText());
            pst.setFloat(12, totaltd);
            pst.setFloat(13, profite);
            pst.setInt(14, userid);

            pst.execute();
             SaleDetailsInsert();
           // statementinsert();
            Stockpdate();
            customerbalanceupdate();
            vatcollection();
            itemforcastInsert();
             if(orderNobox.getSelectedIndex()>0){
             OrderStatus();
                 }
           
            JOptionPane.showMessageDialog(null, "Invoice Data Saved");
            printInvoice();
            AfterExecute();
            // cashInDrwaer(chasin);
           

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void saleInsert() throws JRException {
     //   float chasin = Float.parseFloat(totalinvoiceamount.getText());
        try {
          float totaltd = totaltrade();
            float totalamt=Float.parseFloat(totalrate.getText());
            float totalprofite=totalamt-totaltd;
            float discount=Float.parseFloat(netdiscount.getText());
            float profite=totalprofite-discount;
            String sql = "Insert into sale(invoiceNo,invoicedate,paymentCurency,paymentType,netdiscount,TotalAmount,TotalVat,Totalinvoice,customerid,Ref_No ,totaltraprice,totalprofite,inputuserid) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, invoicetext.getText());
            pst.setDate(2, date);
            pst.setString(3, (String) currencybox.getSelectedItem());
            pst.setString(4, (String) Paymenttype);
            pst.setString(5, netdiscount.getText());
            pst.setString(6, totalrate.getText());
            pst.setString(7, totalvattext.getText());
            pst.setString(8, totalinvoiceamount.getText());
            pst.setString(9, cusId);
            pst.setString(10,reftext.getText());
            pst.setFloat(11, totaltd);
            pst.setFloat(12, profite);
            pst.setInt(13, userid);
            pst.execute();
            
             SaleDetailsInsert();
           // statementinsert();
            Stockpdate();
            customerbalanceupdate();
            vatcollection();
            itemforcastInsert();
            if(orderNobox.getSelectedIndex()>0){
             OrderStatus();
            
            }
           
            JOptionPane.showMessageDialog(null, "Invoice Data Saved");
            printInvoice();
            AfterExecute();
            // cashInDrwaer(chasin);
           

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    
    
     private void vatcollection() {
        //   float chasin = Float.parseFloat(totalinvoiceamount.getText());
        try {

            String sql = "Insert into vatcollection(invoiceNopurchaseNo,invoicedate,paymenttype,vattype,TotalAmount,totaldiscount,TotalVat,Totalinvoice,customerid) values (?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, invoicetext.getText());
            pst.setDate(2, date);

            pst.setString(3, (String) Paymenttype);
            pst.setString(4, "Input");
            pst.setString(5, totalrate.getText());
            pst.setString(6, netdiscount.getText());
            pst.setString(7, totalvattext.getText());
            pst.setString(8, totalinvoiceamount.getText());
            pst.setString(9, cusId);

            pst.execute();
            // cashInDrwaer(chasin);
            //JOptionPane.showMessageDialog(null, "Invoice Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
      
        private float totaltrade(){
    
      int rows = dataTable.getRowCount();
          float totaltradep = 0;
            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String procode = (String) dataTable.getValueAt(row, 1);
                try {
                    String sql = "Select * from item where id='" + procode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {

                        tpprice = rs.getFloat("tp");

                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }

               
                float qty = (Float) dataTable.getValueAt(row, 4);
                float totaltrade=qty*tpprice;
                totaltradep=totaltradep+totaltrade;
                
            }
      return (float) totaltradep;
    
    }
    private void statementinsert(float balancedue) throws SQLException {
 String afterdue=df2.format(balancedue);
        try {

            String sql = "Insert into CraditStatement(customerid,Orderno,type,invoiceno,invoicedate,totalInvoice,Received_amt,remark,balance) values(?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, customeridtext.getText());
            pst.setString(2, "0");
            pst.setString(3, "Invoice");
            pst.setString(4, invoicetext.getText());
            pst.setDate(5, date);
            pst.setString(6, totalinvoiceamount.getText());
            pst.setDouble(7, 0.00);
            pst.setString(8, "Credit Invoice");
            pst.setString(9, afterdue);
            pst.execute();
            ResultSet rshere = pst.getGeneratedKeys();
            int generatedKey = 0;
            if (rshere.next()) {
                generatedKey = rshere.getInt(1);
                creditpaymentinsert(generatedKey);

            }
           // JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void customerbalanceupdate() throws SQLException {
      
        float todaysale=Float.parseFloat(totalinvoiceamount.getText());
        float customer_amt=paidamt;
        float aftersaleamt=salesamt+todaysale;
        float dicount=Float.parseFloat(netdiscount.getText());
        float todicount=totaldiscount+dicount;
        float aftercreditamt=(float) (creditAmnt+todaysale);
       
        
           balanced=balancedue+todaysale; 
          
             String afterdue=df2.format(balanced);
              String creditamt=df2.format(aftercreditamt);
             String saleamt=df2.format(aftersaleamt);
       
        
   
 
    
        try {

            String sql = "Update customerInfo set creditAmnt='"+creditamt+"',Balancedue='" + afterdue + "',saleamount='" + saleamt + "',totaldiscount='" + todicount + "' where id='" + cusId + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
         //   JOptionPane.showMessageDialog(null, "Data Update");
          statementinsert(balanced);
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
       
    }

    private void creditpaymentinsert(int creditid) throws SQLException {

        try {

            String sql = "Insert into creditpayment(custid,creditid,amounttype,ceditamt,paymentamt,inputdate) values(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, cusId);
            pst.setInt(2, creditid);
            pst.setString(3, "credit");
            pst.setFloat(4, Float.parseFloat(totalinvoiceamount.getText()));
              pst.setDouble(5, 0.00);
            pst.setDate(6, date);
            pst.execute();

            //JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void SaleDetailsInsert() throws SQLException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String procode = (String) dataTable.getValueAt(row, 1);
                try {
                    String sql = "Select * from item where id='" + procode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {

                        tpprice = rs.getFloat("tp");

                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                float rate = (Float) dataTable.getValueAt(row, 3);

                float qty = (Float) dataTable.getValueAt(row, 4);

                String unit = (String) dataTable.getValueAt(row, 5);
                float discount = (Float) dataTable.getValueAt(row, 6);
                float totalamount = (Float) dataTable.getValueAt(row, 7);

                float vat = (Float) dataTable.getValueAt(row, 8);
                float totalvat = (Float) dataTable.getValueAt(row, 9);
                float neTotal = (Float) dataTable.getValueAt(row, 10);
                try {

                    String sql = "Insert into saleDetails(invoiceNo,prcode,unitrate,qty,unit,discount,commesion,totalamount,vat,Totalvat,NetTotal,tradprice,invoicedate,cusid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, invoicetext.getText());
                    pst.setString(2, procode);
                    pst.setFloat(3, rate);
                    pst.setFloat(4, qty);
                    pst.setString(5, unit);
                    pst.setFloat(6, discount);
                    pst.setFloat(7, 0);
                    pst.setFloat(8, totalamount);
                    pst.setFloat(9, vat);
                    pst.setFloat(10, totalvat);
                    pst.setFloat(11, neTotal);
                    pst.setFloat(12, tpprice);
                    pst.setDate(13, date);
                    pst.setString(14, cusId);
                    pst.execute();

///    examperticipationinsert(id,examcode,exam);
                    // 
                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }

            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
  private void itemforcastInsert() throws SQLException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String procode = (String) dataTable.getValueAt(row, 1);
                try {
                    String sql = "Select * from item where id='" + procode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {

                        tpprice = rs.getFloat("tp");

                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                float rate = (Float) dataTable.getValueAt(row, 3);

                float qty = (Float) dataTable.getValueAt(row, 4);

                String unit = (String) dataTable.getValueAt(row, 5);
                float discount = (Float) dataTable.getValueAt(row, 6);
                float totalamount = (Float) dataTable.getValueAt(row, 7);

                float vat = (Float) dataTable.getValueAt(row, 8);
                float totalvat = (Float) dataTable.getValueAt(row, 9);
                float neTotal = (Float) dataTable.getValueAt(row, 10);
                try {

                    String sql = "Insert into itemforcast(invoiceNo,prcode,unitrate,qty,unit,discount,commesion,totalamount,vat,Totalvat,NetTotal,tradprice,invoicedate) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, invoicetext.getText());
                    pst.setString(2, procode);
                    pst.setFloat(3, rate);
                    pst.setFloat(4, qty);
                    pst.setString(5, unit);
                    pst.setFloat(6, discount);
                    pst.setFloat(7, 0);
                    pst.setFloat(8, totalamount);
                    pst.setFloat(9, vat);
                    pst.setFloat(10, totalvat);
                    pst.setFloat(11, neTotal);
                    pst.setFloat(12, tpprice);
                    pst.setDate(13, date);
                    pst.execute();

///    examperticipationinsert(id,examcode,exam);
                    // 
                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }

            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    private void cashInDrwaer(float cashin) {
        Float balanc = bankbalance1 + cashin;
        try {

            String sql = "Insert into CashDrower(cashin,cashout,balance,type,upatedate,tranid) values(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setFloat(1, cashin);
            pst.setFloat(2, 0);
            pst.setFloat(3, balanc);
            pst.setString(4, "Sale Product");
            pst.setDate(5, date);
            pst.setString(6, invoicetext.getText());
            pst.execute();

            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void printInvoice() throws JRException {
            int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Invoice", "Invoice Print", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
        
        Invoiceno = invoicetext.getText();
        String User;

       // String Paymenttype = (String) paymenttypebox.getSelectedItem();
        // int i=1; 
        if (customerbox.getSelectedIndex() == 0) {
            User = " ";

        } else {
            User = (String) customerbox.getSelectedItem();
        }

        try {

            String currency = (String) currencybox.getSelectedItem();
            Double d = Double.parseDouble(totalinvoiceamount.getText());

            String num = totalinvoiceamount.getText();

            //String arebic = convertNumberToArabicWords(num);
            String inwords = convertToIndianCurrency(num);
            String convert = inwords;
            
            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/CreditInvoice.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();
            para.put("invoiceno", Invoiceno);
            para.put("paymenttype", Paymenttype);

            para.put("inwords", convert);
            para.put("customer", User);
            para.put("customerid", cusId);

            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

            JasperPrintManager.printReport(jp, true);
            //JasperViewer.viewReport(jp, true);

        } catch (NumberFormatException | JRException ex) {
            JOptionPane.showMessageDialog(null, ex);

        }
        }
        
        /*    int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Invoice", "Invoice Print", JOptionPane.YES_NO_OPTION);
                if (p == 0) {
        Invoiceno = invoicetext.getText();
          
        
        try {

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/BilRecieptCredit.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();
            para.put("invoiceno", Invoiceno);
           para.put("cusid", cusId);
           para.put("cashier", username);
           para.put("discount", netdiscount.getText());
           para.put("prevoiusdue", previseamonmttext.getText());
            para.put("currentdue", totalinvoiceamount.getText());
            para.put("totaldue", netstext.getText());
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

            JasperPrintManager.printReport(jp, true);
            //JasperViewer.viewReport(jp, true);

        } catch (NumberFormatException | JRException ex) {
            JOptionPane.showMessageDialog(null, ex);

        }
                   }
    */
        }

    private void Stockpdate() {
        // int cat=(Integer) categoryBox.getSelectedIndex();
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            String cat = dataTable.getValueAt(i, 8).toString().trim();

            String s = dataTable.getValueAt(i, 1).toString().trim();
            float qty = (Float) dataTable.getValueAt(i, 4);

            Stockpdateumuse(s, qty);

        }

    }

    private void Stockpdateumuse(String s, float qtyint) {

        try {

            String sql = "Select * from Stock where procode=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, s);
            rs = pst.executeQuery();

            while (rs.next()) {

                float stock = rs.getFloat("qty");

                float update_qty = stock - qtyint;

                String sql1 = "Update stock set qty='" + update_qty + "' where procode='" + s + "'";

                pst = conn.prepareStatement(sql1);

                pst.execute();

            }  //JOptionPane.showMessageDialog(null, add1);

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void checkUpdate() {

        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int i = dataTable.getSelectedRow();
        if (i >= 0) {
            float discount;
            float vat =includevat/100;
            float totalvat;

            float tpd = Float.parseFloat(unitrateText.getText());
            
            if (discounttext.getText().isEmpty()) {

                discount = 0;

            } else {
                discount = Float.parseFloat(discounttext.getText());

            }
            float tpwdisc = tpd - discount;
           float productvat = tpwdisc * vat;
             String prorductvatfromat=df2.format(productvat);
           float productvatfloat=Float.parseFloat(prorductvatfromat);
            float qty = Float.parseFloat(qtytext.getText());
            float nettotaltp = (tpwdisc * qty);
            totalvat = (productvatfloat * qty);
             String totalvatformat=df2.format(totalvat);
            float totalvatfloat=Float.parseFloat(totalvatformat);
            
            float totalall = nettotaltp + totalvatfloat;

            model.setValueAt(tpd, i, 3);
            model.setValueAt(discount, i, 6);
            model.setValueAt(qty, i, 4);
            model.setValueAt(nettotaltp, i, 7);
            model.setValueAt(totalvatfloat, i, 9);
            model.setValueAt(totalall, i, 10);

        }

      
        
      double inputotalrate =total_action_mrp();
        totalrate.setText(df2.format(inputotalrate));
       
        double inputvat =total_action_vat();
        totalvattext.setText(df2.format(inputvat));
       
        double input = total_invoice_amount();

        totalinvoiceamount.setText(df2.format(input));
        totalinvoiceamount.setText(df2.format(input));
        
        
        netdiscount.setText(Float.toString(total_discount_amount()));
         
        float totalinvoce = total_invoice_amount();
        float prebamt = Float.parseFloat(previseamonmttext.getText());
        float nettotalin = totalinvoce + prebamt;
        String nets = String.format("%.2f", nettotalin);
        netstext.setText(nets);
        updatekey=0;
        
        clear();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        inputdate = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        unitrateText = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        discounttext = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        qtytext = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        unibox = new javax.swing.JComboBox<>();
        jPanel18 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        totaltext = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        addbtn = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        invoicetext = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        orderNobox = new javax.swing.JComboBox<>();
        reftext = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        customerbox = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        customeridtext = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        previseamonmttext = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        netstext = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        snametext = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        addresstext = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        contactnotext = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        msgtext = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        currencybox = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        executebtn = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        totalrate = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        totalvattext = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        totalinvoiceamount = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        netdiscount = new javax.swing.JTextField();
        orderbtn = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        neworderbtn = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Credit Invoice");
        setExtendedState(6);

        jPanel4.setBackground(new java.awt.Color(67, 86, 86));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Date");

        inputdate.setForeground(new java.awt.Color(102, 102, 102));
        inputdate.setDateFormatString("yyyy-MM-dd");

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Product Code");

        codetext.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        codetext.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        codetext.setBorder(null);
        codetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codetextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetextcodetextKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel11)
                .addGap(1, 1, 1)
                .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel3.setBackground(new java.awt.Color(67, 86, 86));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Item Description");

        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchitemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel10)
                .addGap(1, 1, 1)
                .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel14.setBackground(new java.awt.Color(67, 86, 86));
        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Price");

        unitrateText.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        unitrateText.setForeground(new java.awt.Color(153, 0, 0));
        unitrateText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        unitrateText.setBorder(null);
        unitrateText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                unitrateTextKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unitrateText)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(0, 58, Short.MAX_VALUE)))
                .addGap(3, 3, 3))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel15.setBackground(new java.awt.Color(67, 86, 86));
        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Discount");

        discounttext.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        discounttext.setBorder(null);
        discounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                discounttextKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel16.setBackground(new java.awt.Color(67, 86, 86));
        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Quantity");

        qtytext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        qtytext.setForeground(new java.awt.Color(153, 0, 0));
        qtytext.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        qtytext.setBorder(null);
        qtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                qtytextqtytextKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel14)
                .addGap(1, 1, 1)
                .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel17.setBackground(new java.awt.Color(67, 86, 86));
        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Unit");

        unibox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel16))
                    .addComponent(unibox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel16)
                .addGap(1, 1, 1)
                .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel18.setBackground(new java.awt.Color(67, 86, 86));
        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Stock");

        totaltext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totaltext.setBorder(null);
        totaltext.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(3, 3, 3))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel15)
                .addGap(1, 1, 1)
                .addComponent(totaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel19.setBackground(new java.awt.Color(67, 86, 86));
        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        addbtn.setBackground(new java.awt.Color(255, 255, 255));
        addbtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addbtn.setText("Add");
        addbtn.setBorder(null);
        addbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addbtnjButton12ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(153, 0, 0));
        jButton16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setText("CLEAR");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(addbtn, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(533, 533, 533))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(inputdate, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inputdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        jPanel5.setBackground(new java.awt.Color(67, 86, 86));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Invoice No:");

        invoicetext.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        invoicetext.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Ref No:");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Holde No");

        orderNobox.setEditable(true);
        orderNobox.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        orderNobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        orderNobox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                orderNoboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(orderNobox, 0, 291, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(reftext))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(invoicetext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(17, 17, 17))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(invoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(reftext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(orderNobox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));
        jLabel18.setText("Customer Name");

        customerbox.setEditable(true);
        customerbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        customerbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                customerboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setText("Customer Code");

        customeridtext.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        customeridtext.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        customeridtext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        customeridtext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        customeridtext.setEnabled(false);
        customeridtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                customeridtextKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                customeridtextKeyTyped(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Due");

        previseamonmttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        previseamonmttext.setForeground(new java.awt.Color(204, 0, 0));
        previseamonmttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        previseamonmttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        previseamonmttext.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        previseamonmttext.setEnabled(false);
        previseamonmttext.setSelectedTextColor(new java.awt.Color(102, 0, 0));
        previseamonmttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previseamonmttextActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("Net Total");

        netstext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        netstext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netstext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        netstext.setDisabledTextColor(new java.awt.Color(102, 0, 0));
        netstext.setEnabled(false);
        netstext.setSelectedTextColor(new java.awt.Color(102, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(previseamonmttext)
                    .addComponent(netstext)
                    .addComponent(customeridtext)
                    .addComponent(customerbox, 0, 236, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(customerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(customeridtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(previseamonmttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(netstext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Customer Information", jPanel1);

        jLabel1.setText("Customer Name*");

        jLabel3.setText("Address:");

        addresstext.setColumns(20);
        addresstext.setRows(5);
        jScrollPane3.setViewportView(addresstext);

        jLabel4.setText("Mobile*:");

        jButton13.setBackground(new java.awt.Color(0, 51, 51));
        jButton13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("Save");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        msgtext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(msgtext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addComponent(contactnotext)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addComponent(snametext))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(msgtext, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(snametext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactnotext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("New Customer", jPanel10);

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setText("Curency Type");

        currencybox.setEditable(true);
        currencybox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(currencybox, 0, 279, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currencybox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(140, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Curremcy", jPanel13);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setViewportBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        dataTable.setAutoCreateRowSorter(true);
        dataTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        dataTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL.No", "Item Code", "Descripion", "Unit Rate", "Qty", "Unit", "Discount", "Total Amount", "Vat(5%)", "Total Vat", "Net Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTable.setRowHeight(40);
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
        jScrollPane2.setViewportView(dataTable);
        if (dataTable.getColumnModel().getColumnCount() > 0) {
            dataTable.getColumnModel().getColumn(0).setResizable(false);
            dataTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            dataTable.getColumnModel().getColumn(1).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(150);
            dataTable.getColumnModel().getColumn(3).setResizable(false);
            dataTable.getColumnModel().getColumn(3).setPreferredWidth(100);
            dataTable.getColumnModel().getColumn(4).setResizable(false);
            dataTable.getColumnModel().getColumn(4).setPreferredWidth(40);
            dataTable.getColumnModel().getColumn(5).setResizable(false);
            dataTable.getColumnModel().getColumn(6).setResizable(false);
            dataTable.getColumnModel().getColumn(7).setResizable(false);
            dataTable.getColumnModel().getColumn(8).setResizable(false);
            dataTable.getColumnModel().getColumn(9).setResizable(false);
            dataTable.getColumnModel().getColumn(10).setResizable(false);
        }

        executebtn.setBackground(new java.awt.Color(67, 86, 86));
        executebtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        executebtn.setForeground(new java.awt.Color(255, 255, 255));
        executebtn.setText("Execution");
        executebtn.setBorder(null);
        executebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executebtnActionPerformed(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        totalrate.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        totalrate.setForeground(new java.awt.Color(204, 0, 0));
        totalrate.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(67, 86, 86));
        jLabel7.setText("Total Amount");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalrate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        totalvattext.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        totalvattext.setForeground(new java.awt.Color(204, 0, 0));
        totalvattext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(67, 86, 86));
        jLabel25.setText("Total Vat");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalvattext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(67, 86, 86));
        jLabel5.setText("Total Invoice");

        totalinvoiceamount.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        totalinvoiceamount.setForeground(new java.awt.Color(204, 0, 0));
        totalinvoiceamount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalinvoiceamount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalinvoiceamount, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(67, 86, 86));
        jLabel6.setText("Discount");

        netdiscount.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        netdiscount.setForeground(new java.awt.Color(153, 0, 0));
        netdiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                netdiscountKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(netdiscount))
                .addGap(5, 5, 5))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(netdiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        orderbtn.setBackground(new java.awt.Color(51, 153, 0));
        orderbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        orderbtn.setForeground(new java.awt.Color(255, 255, 255));
        orderbtn.setText("Hold Order");
        orderbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderbtnActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(153, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Check Invoice");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        neworderbtn.setBackground(new java.awt.Color(0, 102, 102));
        neworderbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        neworderbtn.setForeground(new java.awt.Color(255, 255, 255));
        neworderbtn.setText("New Order");
        neworderbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                neworderbtnActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 153, 153));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Cancel Hold Order");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 955, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(neworderbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(orderbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(executebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(orderbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(neworderbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(executebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        setSize(new java.awt.Dimension(1370, 644));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void codetextcodetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextcodetextKeyReleased

        if (codetext.getText().isEmpty() || codetext.getText().matches("^[a-zA-Z]+$")) {

            itemnamesearch.setSelectedIndex(0);
            unitrateText.setText(null);
            discounttext.setText(null);
            qtytext.setText(null);
            unibox.setSelectedIndex(0);
            totaltext.setText(null);

        } else {
            String SearchText = (String) codetext.getText();

            try {

                String sql = "Select itemcode,itemName,(select un.unitshort from unit un where un.id=ita.stockunit) as 'stockunit',mrp,vat,(select st.Qty from stock st where st.procode=ita.itemcode) as 'stock'  from item ita where ita.itemcode='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                //  pst.setString(1, SearchText);

                rs = pst.executeQuery();
                if (rs.next()) {

                    String name = rs.getString("itemName");
                    itemnamesearch.setSelectedItem(name);
                    String unitrate = rs.getString("mrp");
                    unitrateText.setText(unitrate);
                    String stock = rs.getString("stock");
                    totaltext.setText(stock);
                    String unit = rs.getString("stockunit");
                    unibox.setSelectedItem(unit);
                    includevat=rs.getFloat("vat");
                      if(offer==1){
                    String sql1="Select offerprice from offerdetails where procode='" + SearchText + "' AND offerid='"+offerid+"' ";
                    pst = conn.prepareStatement(sql1);
                //  pst.setString(1, SearchText);

                rs = pst.executeQuery();
                if (rs.next()) {
                String offerprice = rs.getString("offerprice");
                    unitrateText.setText(offerprice);
                
                }
                    }
                    
                    
                    
                    //qtytext.setText("1");
                  //  checkentry();
                  

                } else {

                    itemnamesearch.setSelectedIndex(0);
                    unitrateText.setText(null);
                    discounttext.setText(null);
                    qtytext.setText(null);
                    unibox.setSelectedIndex(0);
                    totaltext.setText(null);

                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
         
        }
    }//GEN-LAST:event_codetextcodetextKeyReleased

    private void itemnamesearchitemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchitemnamesearchPopupMenuWillBecomeInvisible
        if (itemnamesearch.getSelectedIndex() == 0) {
            codetext.setText(null);

            unitrateText.setText(null);
            discounttext.setText(null);
            qtytext.setText(null);
            unibox.setSelectedIndex(0);
            totaltext.setText(null);

        } else {

            String SearchText = (String) itemnamesearch.getSelectedItem();
            try {

                String sql = "Select itemcode,itemName,(select un.unitshort from unit un where un.id=ita.stockunit) as 'stockunit',mrp,vat,(select st.Qty from stock st where st.procode=ita.itemcode) as 'stock' from item ita where ita.itemName='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                //  pst.setString(1, SearchText);

                rs = pst.executeQuery();
                if (rs.next()) {

                    String proid = rs.getString("itemcode");
                    codetext.setText(proid);
                    String stock = rs.getString("stock");
                    totaltext.setText(stock);
                    String unitrate = rs.getString("mrp");
                    unitrateText.setText(unitrate);
                    String unit = rs.getString("stockunit");
                    unibox.setSelectedItem(unit);
                    includevat=rs.getFloat("vat");
                     if(offer==1){
                    String sql1="Select offerprice from offerdetails where procode='" + proid + "' AND offerid='"+offerid+"' ";
                    pst = conn.prepareStatement(sql1);
                //  pst.setString(1, SearchText);

                rs = pst.executeQuery();
                if (rs.next()) {
                String offerprice = rs.getString("offerprice");
                    unitrateText.setText(offerprice);
                
                }
                    }
                     
                     qtytext.requestFocusInWindow();
                    
                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

          
              
        }
    }//GEN-LAST:event_itemnamesearchitemnamesearchPopupMenuWillBecomeInvisible

    private void qtytextqtytextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextqtytextKeyReleased
        /*
        if (qtytext.getText().isEmpty() || qtytext.getText().matches("^[a-zA-Z]+$")) {
            totaltext.setText(null);
        } else if (unitrateText.getText().isEmpty()) {
            totaltext.setText(null);

        } else {
            float discount;
            float Qty = Float.parseFloat(qtytext.getText());
            float mrp = Float.parseFloat(unitrateText.getText());
            if (discounttext.getText().isEmpty()) {

                discount = 0;

            } else {
                discount = Float.parseFloat(discounttext.getText());
            }

            float price = mrp - discount;
            float total = Qty * price;
            totaltext.setText(String.format("%.2f", total));

        }
        */
    }//GEN-LAST:event_qtytextqtytextKeyReleased

    private void addbtnjButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbtnjButton12ActionPerformed
         if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0 || customerbox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
        } else {
             if (updatekey==0) {
       try {
            
            checkentry();
            
            } catch (NumberFormatException e) {
        JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Quantity Validation", JOptionPane.CANCEL_OPTION);
            qtytext.setText(null);
            }
        } else {
             try {
            checkUpdate();
             } catch (NumberFormatException e) {
        JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Quantity Validation", JOptionPane.CANCEL_OPTION);
            qtytext.setText(null);
            }
        }
         codetext.requestFocusInWindow();
         
         }
          
    }//GEN-LAST:event_addbtnjButton12ActionPerformed

    private void customerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_customerboxPopupMenuWillBecomeInvisible
      
        
        String Customername = (String) customerbox.getSelectedItem();
        try {
            String sql = "Select * from customerInfo  where customername='" + Customername + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {

                cusId = rs.getString("id");
                customeridtext.setText(cusId);

                String Name = rs.getString("customername");
               

                openingbalance=rs.getFloat("OpenigBalance");
                dipositamt=rs.getFloat("DipositAmt");
                creditAmnt=rs.getFloat("creditAmnt");
                salesamt=rs.getFloat("saleamount");
                paidamt=rs.getFloat("paidamount");
                balancedue=rs.getFloat("Balancedue");
                totaldiscount=rs.getFloat("totaldiscount");
                String trans = String.format("%s", balancedue);
                previseamonmttext.setText(trans);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
         entrucesure();
        codetext.requestFocusInWindow();
        /*
        try {
            String sql = "Select sum(ps.ceditamt) as 'netcredit',sum(ps.paymentamt) as 'netpayment' from creditpayment ps where  custid='" + cusId + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {

                float netcredit = rs.getFloat("netcredit");

                float netpayment = rs.getFloat("netpayment");

                float remain = netcredit - netpayment;
                previseamonmttext.setText(String.format("%.2f", remain));

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        */
    }//GEN-LAST:event_customerboxPopupMenuWillBecomeInvisible

    private void customeridtextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customeridtextKeyPressed

     
    }//GEN-LAST:event_customeridtextKeyPressed

    private void customeridtextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customeridtextKeyTyped

    }//GEN-LAST:event_customeridtextKeyTyped

    private void previseamonmttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previseamonmttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_previseamonmttextActionPerformed

    private void executebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executebtnActionPerformed
            parchase_code();
            inputDate();
     DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
     int emptytbl=model.getRowCount();
        
        if(invoicetext.getText().isEmpty()){
      
       JOptionPane.showMessageDialog(null, "Please Check Invoice No");
      }else{
      if(emptytbl>0){
      
           // saleInsert();
            
           invoicecheck();
        /*   SaleDetailsInsert();
           // statementinsert();
            Stockpdate();
            customerbalanceupdate();
            vatcollection();
            itemforcastInsert();
            printInvoice();
            AfterExecute();
           
          */ 

        
      }
      }
      

    }//GEN-LAST:event_executebtnActionPerformed

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        int selectedRowIndex = dataTable.getSelectedRow();
       
        codetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        unitrateText.setText(model.getValueAt(selectedRowIndex, 3).toString());
        qtytext.setText(model.getValueAt(selectedRowIndex, 4).toString());
      //  discounttext.setText(model.getValueAt(selectedRowIndex, 6).toString());
        unibox.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());
        float qty=Float.parseFloat((model.getValueAt(selectedRowIndex, 4).toString()));
        float discount=Float.parseFloat((model.getValueAt(selectedRowIndex, 6).toString()));
        float indiscount=discount/qty;
        float itemvat=Float.parseFloat((model.getValueAt(selectedRowIndex, 8).toString()));
        float productprice=Float.parseFloat((model.getValueAt(selectedRowIndex, 3).toString()))-indiscount;
        includevat=itemvat/productprice;
        discounttext.setText(df2.format(indiscount));
        updatekey=1;
        updateSystemProcess();

    }//GEN-LAST:event_dataTableMouseClicked

    private void dataTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMousePressed

    }//GEN-LAST:event_dataTableMousePressed

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
        deleterow();
    }//GEN-LAST:event_dataTableKeyPressed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        if(snametext.getText().isEmpty() || contactnotext.getText().isEmpty()){

        }else{
            try {

                String sql = "Insert into customerinfo(customername,address,poboxno,city,country,vatReNo,remark,status,customerType,balanceType,OpeningDate,OpenigBalance,DipositAmt,creditAmnt,cashamt,saleamount,paidamount,totaldiscount,Balancedue,contactname,contactdesignation,TelephoneNo,ContactNo,inputuser) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);

                pst.setString(1, snametext.getText());
                pst.setString(2, addresstext.getText());
                pst.setString(3, " ");
                pst.setString(4, " ");
                pst.setString(5, " ");
                pst.setString(6, " ");
                pst.setString(7, " ");
                pst.setString(8, " ");
                pst.setString(9, "");
                pst.setString(10, "Credit And Cash");
                pst.setDate(11, date);
                pst.setString(12, "0");
                pst.setString(13, "0");
                pst.setString(14, "0");
                pst.setString(15, "0");
                pst.setString(16, "0");
                pst.setString(17, "0");
                pst.setString(18, "0");
                pst.setString(19, "0");
                pst.setString(20, " ");
                pst.setString(21, " ");
                pst.setString(22, " ");
                pst.setString(23, contactnotext.getText());
                pst.setInt(24, userid);
                pst.execute();

                customerbox.removeAllItems();
                customerbox.addItem("Select");
                customerbox.setSelectedItem("Select");
                 customer();
                snametext.setText(null);
                addresstext.setText(null);
                contactnotext.setText(null);
                msgtext.setText("Successfully Save");

                // JOptionPane.showMessageDialog(null, "Data Saved");

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
       clear();
        
    }//GEN-LAST:event_jButton16ActionPerformed

    private void qtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0 || customerbox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
        } else {
             if (updatekey==0) {
       try {
            
            checkentry();
            
            } catch (NumberFormatException e) {
        JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Quantity Validation", JOptionPane.CANCEL_OPTION);
            qtytext.setText(null);
            }
        } else {
             try {
            checkUpdate();
             } catch (NumberFormatException e) {
        JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Quantity Validation", JOptionPane.CANCEL_OPTION);
            qtytext.setText(null);
            }
        }
         codetext.requestFocusInWindow();
         
         }
          
        }
    }//GEN-LAST:event_qtytextKeyPressed

    private void codetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
        qtytext.requestFocusInWindow(); 
        
        }
    }//GEN-LAST:event_codetextKeyPressed
private void PrintOrder(int orderno){
 int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Order", "Kitchen Order", JOptionPane.YES_NO_OPTION);
                    if (p == 0) {
 DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        if(model2.getRowCount()==0){

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        }else{

            try {
                
               String type=null;
               if(orderupdate==0){
               type="Order";
               }else{
               type="Order Update";
               
               }
                String ordersNo=String.format("%d", orderno);
                String total=totalrate.getText();
                String vat=totalvattext.getText();
                String netTotal=totalinvoiceamount.getText();

                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/Bill.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDataSource", ItemJRBean);
                para.put("invoiceno", ordersNo);
                para.put("tableNo", reftext.getText());
                para.put("total", total);
                para.put("vat", vat);
                para.put("discount", netdiscount.getText());
                para.put("netTotal", netTotal);
                para.put("Type", type);
                para.put("cashier", username);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                // JasperViewer.viewReport(jp, false);
                JasperPrintManager.printReport(jp, true);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
        
                    }
}
    private void orderbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_orderbtnActionPerformed

inputDate();
DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
if( model2.getRowCount()==0){
JOptionPane.showMessageDialog(null, "Please Select Table No Or Table Data Is Empty");
}else{
if(orderupdate==0){
//order Insert
  OderInsert();
}else{
    try {
        //Order Update

        orderUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(PointOfSale.class.getName()).log(Level.SEVERE, null, ex);
    }

}

AfterExecute();

}

    }//GEN-LAST:event_orderbtnActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
      /*  DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        if(model2.getRowCount()==0){

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        }else{

            try {
                String total=totalrate.getText();
                String vat=totalvattext.getText();
                String netTotal=totalinvoiceamount.getText();

                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/Bill.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDataSource", ItemJRBean);
                para.put("invoiceno", orderNobox.getSelectedItem());
                para.put("tableNo", reftext.getText());
                para.put("total", total);
                para.put("vat", vat);
                para.put("discount", netdiscount.getText());
                para.put("netTotal", netTotal);
                para.put("Type", "Bill Reciept");
                para.put("cashier", username);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                // JasperViewer.viewReport(jp, false);
                JasperPrintManager.printReport(jp, true);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
        */
      
        PrintInvoice printinvoice = null;

        try {
             printinvoice= new PrintInvoice();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(CreditPos.class.getName()).log(Level.SEVERE, null, ex);
        }
        printinvoice.setVisible(true);
      
    }//GEN-LAST:event_jButton3ActionPerformed
private void orderview(){
    tree=0;
            try {
                DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
                model2.setRowCount(0);
               // int slNo = 0;
                String sql = "Select prcode ,(select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item', sl.unitrate as 'UnitRate', sl.qty as 'Qty',sl.unit as 'Unit', sl.discount as 'Discount', sl.totalamount as 'Amount', sl.vat as 'Vat', sl.Totalvat as 'TotalVat',sl.NetTotal  as 'NetTotal' from creditorderdetails sl where sl.orderNo='" + orderNobox.getSelectedItem() + "'";
                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();
                // datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {
                    String prcode = rs.getString("prcode");
                    String Item = rs.getString("Item");
                    float UnitRate = rs.getFloat("UnitRate");
                    float Qty = rs.getFloat("Qty");
                    String Unit = rs.getString("Unit");
                    float Discount = rs.getFloat("Discount");
                    float Amount = rs.getFloat("Amount");
                    float Vat = rs.getFloat("Vat");
                    float TotalVat=rs.getFloat("TotalVat");
                    float NetTotal = rs.getFloat("NetTotal");
                    tree++;
                    model2.addRow(new Object[]{tree, prcode, Item, UnitRate, Qty, Unit, Discount, Amount, Vat,TotalVat ,NetTotal});

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
                  try {
                String sql = "Select cusid from creditsaleorder sl  where sl.id='" + orderNobox.getSelectedItem() + "'";
                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();
                if (rs.next()) {

                   
                  cusId = rs.getString("cusid");

                    //  currency=rs.getString("paymentCurency");
                } 
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
                  
                   try{
            String sql = "Select orderdate from creditsaleorder where id='" + orderNobox.getSelectedItem() + "'";
                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();
          if(rs.next()){
               date=rs.getDate("orderdate");
               inputdate.setDate(date);
          }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
       double inputotalrate = total_action_mrp();
        totalrate.setText(df2.format(inputotalrate));

        double inputvat = total_action_vat();
        totalvattext.setText(df2.format(inputvat));

        double input = total_invoice_amount();

        totalinvoiceamount.setText(df2.format(input));

        netdiscount.setText(Float.toString(total_discount_amount()));
           

}

private void customerinfobyorder(){
    try {
            String sql = "Select * from customerInfo  where id='" + cusId + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {

                cusId = rs.getString("id");
                customeridtext.setText(cusId);

                String Name = rs.getString("customername");
               customerbox.setSelectedItem(Name);

                openingbalance=rs.getFloat("OpenigBalance");
                dipositamt=rs.getFloat("DipositAmt");
                creditAmnt=rs.getFloat("creditAmnt");
                salesamt=rs.getFloat("saleamount");
                paidamt=rs.getFloat("paidamount");
                balancedue=rs.getFloat("Balancedue");
                totaldiscount=rs.getFloat("totaldiscount");
                String trans = String.format("%s", balancedue);
                previseamonmttext.setText(trans);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }


}


private void tableNoAction(){
   if(orderNobox.getSelectedIndex()==0){
            AfterExecute();
            paneldactive();

        }else{
            orderview();
            customerinfobyorder();
            panelactive();
            orderupdate=1;
            orderbtn.setText("Update Kitchen Order");
            executebtn.setEnabled(true);
        float totalinvoce = total_invoice_amount();
        float prebamt = Float.parseFloat(previseamonmttext.getText());
        float nettotalin = totalinvoce + prebamt;
        String nets = String.format("%.2f", nettotalin);
        netstext.setText(nets);

        }



}
    private void orderNoboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_orderNoboxPopupMenuWillBecomeInvisible
        if(orderNobox.getSelectedIndex()==0){
            AfterExecute();
            paneldactive();

        }else{
            orderview();
            customerinfobyorder();
            panelactive();
            orderupdate=1;
            orderbtn.setText("Update Order");
            executebtn.setEnabled(true);
        float totalinvoce = total_invoice_amount();
        float prebamt = Float.parseFloat(previseamonmttext.getText());
        float nettotalin = totalinvoce + prebamt;
        String nets = String.format("%.2f", nettotalin);
        netstext.setText(nets);

        }
    }//GEN-LAST:event_orderNoboxPopupMenuWillBecomeInvisible

    private void neworderbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_neworderbtnActionPerformed
       AfterExecute();
    }//GEN-LAST:event_neworderbtnActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
         if(orderupdate==1 ||orderNobox.getSelectedIndex()>0){
           try {
                String sql = "Delete from saleorder where id=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, (String) orderNobox.getSelectedItem());

                pst.execute();
                

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            try {
                String sql = "Delete from orderDetails where orderNo=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, (String) orderNobox.getSelectedItem());

                pst.execute();
                

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            AfterExecute();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void netdiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_netdiscountKeyPressed
          if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            if (totalinvoiceamount.getText().isEmpty()|| netdiscount.getText().isEmpty()) {
          //  changetext.setText(null);

        } else {
            float discount = Float.parseFloat(netdiscount.getText());
            float amount = total_invoice_amount();

            if (amount < discount) {
               // changetext.setText(null);
                JOptionPane.showMessageDialog(null, "Discount Amount is Bigger than total Invoice Amount");

            } else {

                float change = amount-discount;
                //  String gpper = String.format("%.2f", productvat);
                totalinvoiceamount.setBackground(Color.YELLOW);
                totalinvoiceamount.setText(String.format("%.2f", change));
                //customer
                float prebamt = Float.parseFloat(previseamonmttext.getText());
        float nettotalin = change + prebamt;
        String nets = String.format("%.2f", nettotalin);
        netstext.setText(nets);
            }

        }

        }
    }//GEN-LAST:event_netdiscountKeyPressed

    private void discounttextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discounttextKeyPressed
          if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
          qtytext.requestFocusInWindow(); 
          
          }
    }//GEN-LAST:event_discounttextKeyPressed

    private void unitrateTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateTextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
          qtytext.requestFocusInWindow(); 
          
          }
    }//GEN-LAST:event_unitrateTextKeyPressed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreditPos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addbtn;
    private javax.swing.JTextArea addresstext;
    private javax.swing.JTextField codetext;
    private javax.swing.JTextField contactnotext;
    private javax.swing.JComboBox<String> currencybox;
    private javax.swing.JComboBox<String> customerbox;
    private javax.swing.JTextField customeridtext;
    private javax.swing.JTable dataTable;
    private javax.swing.JTextField discounttext;
    private javax.swing.JButton executebtn;
    private com.toedter.calendar.JDateChooser inputdate;
    private javax.swing.JLabel invoicetext;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
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
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel msgtext;
    private javax.swing.JTextField netdiscount;
    private javax.swing.JTextField netstext;
    private javax.swing.JButton neworderbtn;
    private javax.swing.JComboBox<String> orderNobox;
    private javax.swing.JButton orderbtn;
    private javax.swing.JTextField previseamonmttext;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextField reftext;
    private javax.swing.JTextField snametext;
    private javax.swing.JLabel totalinvoiceamount;
    private javax.swing.JLabel totalrate;
    private javax.swing.JTextField totaltext;
    private javax.swing.JLabel totalvattext;
    private javax.swing.JComboBox<String> unibox;
    private javax.swing.JTextField unitrateText;
    // End of variables declaration//GEN-END:variables
 public static String convertToIndianCurrency(String num) {
        BigDecimal bd = new BigDecimal(num);
        long number = bd.longValue();
        long no = bd.longValue();
        int decimal = (int) (bd.remainder(BigDecimal.ONE).doubleValue() * 100);
        int digits_length = String.valueOf(no).length();
        int i = 0;
        ArrayList<String> str = new ArrayList<>();
        HashMap<Integer, String> words = new HashMap<>();
        words.put(0, "");
        words.put(1, "One");
        words.put(2, "Two");
        words.put(3, "Three");
        words.put(4, "Four");
        words.put(5, "Five");
        words.put(6, "Six");
        words.put(7, "Seven");
        words.put(8, "Eight");
        words.put(9, "Nine");
        words.put(10, "Ten");
        words.put(11, "Eleven");
        words.put(12, "Twelve");
        words.put(13, "Thirteen");
        words.put(14, "Fourteen");
        words.put(15, "Fifteen");
        words.put(16, "Sixteen");
        words.put(17, "Seventeen");
        words.put(18, "Eighteen");
        words.put(19, "Nineteen");
        words.put(20, "Twenty");
        words.put(30, "Thirty");
        words.put(40, "Forty");
        words.put(50, "Fifty");
        words.put(60, "Sixty");
        words.put(70, "Seventy");
        words.put(80, "Eighty");
        words.put(90, "Ninety");
        String digits[] = {"", "Hundred", "Thousand", "Hundred Thousand", "Million" , "10 Million"};
        while (i < digits_length) {
            int divider = (i == 2) ? 10 : 100;
            number = no % divider;
            no = no / divider;
            i += divider == 10 ? 1 : 2;
            if (number > 0) {
                int counter = str.size();
                String plural = (counter > 0 && number > 9) ? "s" : "";
                String tmp = (number < 21) ? words.get((int) number) + " " + digits[counter] + plural : words.get((int) Math.floor(number / 10) * 10) + " " + words.get((int) (number % 10)) + " " + digits[counter] + plural;
                str.add(tmp);
            } else {
                str.add("");
            }
        }

        Collections.reverse(str);
        // ArrayList<String> Rupees =str;
//String Rupees = String.join(" ", str).trim();

        String Rupees = String.join(" ", str).trim();

        String paise = (decimal) > 0 ? " And " + words.get((int) (decimal - decimal % 10)) + " Files " + words.get((int) (decimal % 10)) : "";
        return Rupees + paise + " " + "AED Only";
    }
//Arebic

    public static String convertNumberToArabicWords(String number) throws NumberFormatException {

// check if the input string is number or not
        Double.parseDouble(number);

// check if its floating point number or not
        if (number.contains(".")) {// yes
// the number
            String theNumber = number.substring(0, number.indexOf('.'));
// the floating point number
            String theFloat = number.substring(number.indexOf('.') + 1);
// check how many digits in the number 1:x 2:xx 3:xxx 4:xxxx 5:xxxxx
// 6:xxxxxx
            switch (theNumber.length()) {
                case 1:
                    return convertOneDigits(theNumber) + " ?????????? " + convertTwoDigits(theFloat);
                case 2:
                    return convertTwoDigits(theNumber) + " ?????????? " + convertTwoDigits(theFloat);
                case 3:
                    return convertThreeDigits(theNumber) + " ?????????? " + convertTwoDigits(theFloat);
                case 4:
                    return convertFourDigits(theNumber) + " ?????????? " + convertTwoDigits(theFloat);
                case 5:
                    return convertFiveDigits(theNumber) + " ?????????? " + convertTwoDigits(theFloat);
                case 6:
                    return convertSixDigits(theNumber) + " ?????????? " + convertTwoDigits(theFloat);
                default:
                    return "";
            }
        } else {
            switch (number.length()) {
                case 1:
                    return convertOneDigits(number);
                case 2:
                    return convertTwoDigits(number);
                case 3:
                    return convertThreeDigits(number);
                case 4:
                    return convertFourDigits(number);
                case 5:
                    return convertFiveDigits(number);
                case 6:
                    return convertSixDigits(number);
                default:
                    return "";
            }

        }
    }

// -------------------------------------------
    private static String convertOneDigits(String oneDigit) {
        switch (Integer.parseInt(oneDigit)) {
            case 1:
                return "????????";
            case 2:
                return "??????????";
            case 3:
                return "??????????";
            case 4:
                return "??????????";
            case 5:
                return "????????";
            case 6:
                return "??????";
            case 7:
                return "????????";
            case 8:
                return "????????????";
            case 9:
                return "????????";
            default:
                return "";
        }
    }

    private static String convertTwoDigits(String twoDigits) {
        String returnAlpha = "00";
// check if the first digit is 0 like 0x
        if (twoDigits.charAt(0) == '0' && twoDigits.charAt(1) != '0') {// yes
// convert two digits to one
            return convertOneDigits(String.valueOf(twoDigits.charAt(1)));
        } else {// no
// check the first digit 1x 2x 3x 4x 5x 6x 7x 8x 9x
            switch (getIntVal(twoDigits.charAt(0))) {
                case 1: {// 1x
                    if (getIntVal(twoDigits.charAt(1)) == 1) {
                        return "??????????????";
                    }
                    if (getIntVal(twoDigits.charAt(1)) == 2) {
                        return "??????????????";
                    } else {
                        return convertOneDigits(String.valueOf(twoDigits.charAt(1))) + " " + "??????";
                    }
                }
                case 2:// 2x x:not 0
                    returnAlpha = "??????????";
                    break;
                case 3:// 3x x:not 0
                    returnAlpha = "????????????";
                    break;
                case 4:// 4x x:not 0
                    returnAlpha = "????????????";
                    break;
                case 5:// 5x x:not 0
                    returnAlpha = "??????????";
                    break;
                case 6:// 6x x:not 0
                    returnAlpha = "????????";
                    break;
                case 7:// 7x x:not 0
                    returnAlpha = "??????????";
                    break;
                case 8:// 8x x:not 0
                    returnAlpha = "????????????";
                    break;
                case 9:// 9x x:not 0
                    returnAlpha = "??????????";
                    break;
                default:
                    returnAlpha = "";
                    break;
            }
        }

// 20 - 99
// x0 x:not 0,1
        if (convertOneDigits(String.valueOf(twoDigits.charAt(1))).length() == 0) {
            return returnAlpha;
        } else {// xx x:not 0
            return convertOneDigits(String.valueOf(twoDigits.charAt(1))) + " ?? " + returnAlpha;
        }
    }

    private static String convertThreeDigits(String threeDigits) {

// check the first digit x00
        switch (getIntVal(threeDigits.charAt(0))) {

            case 1: {// 100 - 199
                if (getIntVal(threeDigits.charAt(1)) == 0) {// 10x
                    if (getIntVal(threeDigits.charAt(2)) == 0) {// 100
                        return "????????";
                    } else {// 10x x: is not 0
                        return "????????" + " ?? " + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else {// 1xx x: is not 0
                    return "????????" + " ?? " + convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            case 2: {// 200 - 299
                if (getIntVal(threeDigits.charAt(1)) == 0) {// 20x
                    if (getIntVal(threeDigits.charAt(2)) == 0) {// 200
                        return "????????????";
                    } else {// 20x x:not 0
                        return "????????????" + " ?? " + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else {// 2xx x:not 0
                    return "????????????" + " ?? " + convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: {// 300 - 999
                if (getIntVal(threeDigits.charAt(1)) == 0) {// x0x x:not 0
                    if (getIntVal(threeDigits.charAt(2)) == 0) {// x00 x:not 0
                        return convertOneDigits(String.valueOf(threeDigits.charAt(1) + "????????"));
                    } else {// x0x x:not 0
                        return convertOneDigits(String.valueOf(threeDigits.charAt(0))) + "????????" + " ?? "
                                + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else {// xxx x:not 0
                    return convertOneDigits(String.valueOf(threeDigits.charAt(0))) + "????????" + " ?? "
                            + convertTwoDigits(threeDigits.substring(1, 3));
                }
            }

            case 0: {// 000 - 099
                if (threeDigits.charAt(1) == '0') {// 00x
                    if (threeDigits.charAt(2) == '0') {// 000
                        return "";
                    } else {// 00x x:not 0
                        return convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else {// 0xx x:not 0
                    return convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            default:
                return "";
        }
    }

    private static String convertFourDigits(String fourDigits) {
// xxxx
        switch (getIntVal(fourDigits.charAt(0))) {

            case 1: {// 1000 - 1999
                if (getIntVal(fourDigits.charAt(1)) == 0) {// 10xx x:not 0
                    if (getIntVal(fourDigits.charAt(2)) == 0) {// 100x x:not 0
                        if (getIntVal(fourDigits.charAt(3)) == 0) {// 1000
                            return "??????";
                        } else {// 100x x:not 0
                            return "??????" + " ?? " + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else {// 10xx x:not 0
                        return "??????" + " ?? " + convertTwoDigits(fourDigits.substring(2, 3));
                    }
                } else {// 1xxx x:not 0
                    return "??????" + " ?? " + convertThreeDigits(fourDigits.substring(1, 4));
                }
            }
            case 2: {// 2000 - 2999
                if (getIntVal(fourDigits.charAt(1)) == 0) {// 20xx
                    if (getIntVal(fourDigits.charAt(2)) == 0) {// 200x
                        if (getIntVal(fourDigits.charAt(3)) == 0) {// 2000
                            return "??????????";
                        } else {// 200x x:not 0
                            return "??????????" + " ?? " + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else {// 20xx x:not 0
                        return "??????????" + " ?? " + convertTwoDigits(fourDigits.substring(2, 3));
                    }
                } else {// 2xxx x:not 0
                    return "??????????" + " ?? " + convertThreeDigits(fourDigits.substring(1, 4));
                }
            }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: {// 3000 - 9999
                if (getIntVal(fourDigits.charAt(1)) == 0) {// x0xx x:not 0
                    if (getIntVal(fourDigits.charAt(2)) == 0) {// x00x x:not 0
                        if (getIntVal(fourDigits.charAt(3)) == 0) {// x000 x:not 0
                            return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ????????";
                        } else {// x00x x:not 0
                            return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ????????" + " ?? "
                                    + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else {// x0xx x:not 0
                        return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ????????" + " ?? "
                                + convertTwoDigits(fourDigits.substring(2, 3));
                    }
                } else {// xxxx x:not 0
                    return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ????????" + " ?? "
                            + convertThreeDigits(fourDigits.substring(1, 4));
                }
            }

            default:
                return "";
        }
    }

    private static String convertFiveDigits(String fiveDigits) {
        if (convertThreeDigits(fiveDigits.substring(2, 5)).length() == 0) {// xx000
// x:not
// 0
            return convertTwoDigits(fiveDigits.substring(0, 2)) + " ?????? ";
        } else {// xxxxx x:not 0
            return convertTwoDigits(fiveDigits.substring(0, 2)) + " ???????? " + " ?? "
                    + convertThreeDigits(fiveDigits.substring(2, 5));
        }
    }

    private static String convertSixDigits(String sixDigits) {

        if (convertThreeDigits(sixDigits.substring(2, 5)).length() == 0) {// xxx000
// x:not
// 0
            return convertThreeDigits(sixDigits.substring(0, 3)) + " ?????? ";
        } else {// xxxxxx x:not 0
            return convertThreeDigits(sixDigits.substring(0, 3)) + " ???????? " + " ?? "
                    + convertThreeDigits(sixDigits.substring(3, 6));
        }
    }

    private static int getIntVal(char c) {
        return Integer.parseInt(String.valueOf(c));
    }


}
