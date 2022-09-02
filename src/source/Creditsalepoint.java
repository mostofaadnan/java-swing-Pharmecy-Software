/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class Creditsalepoint extends javax.swing.JFrame {
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
    /**
     * Creates new form Creditsalepoint
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.sql.SQLException
     */
    public Creditsalepoint() throws IOException, FileNotFoundException, SQLException {
        initComponents();
       conn = Java_Connect.conectrDB();
        userkey();
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
            String sql = "Select name from admin";
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
     
   private void editinvoiceno(){
   if(editinvoicetext.isSelected()==true){
   invoicetext.setEnabled(true);
   invoicetext.setText(null);
  
   }else{
   invoicetext.setEnabled(false);
   parchase_code();
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
        //  inputdate.setDate(date);

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
        refnotext.setText(null);
        netdiscount.setText(null);
        parchase_code();

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
        float vat = (float) 0.00;
        float totalvat;

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        tree++;

        float tpd = Float.parseFloat(unitrateText.getText());
        float productvat = tpd * vat;

        if (discounttext.getText().isEmpty()) {

            discount = 0;

        } else {
            discount = Float.parseFloat(discounttext.getText());

        }
        float tpwdisc = tpd - discount;

        float qty = Float.parseFloat(qtytext.getText());
        float nettotaltp = (tpwdisc * qty);
        totalvat = (productvat * qty);
        float totalall = nettotaltp + totalvat;

        //String gpper = String.format("%.2f", gp);
        model2.addRow(new Object[]{tree, codetext.getText(), itemnamesearch.getSelectedItem(), tpd, qty, unibox.getSelectedItem(), discount, nettotaltp, productvat, totalvat, totalall});
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
            pst.setString(11, refnotext.getText());
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
            pst.setString(10, refnotext.getText());
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

            String sql = "Insert into vatcollection(invoiceNopurchaseNo,invoicedate,paymenttype,vattype,TotalAmount,TotalVat,Totalinvoice,customerid) values (?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, invoicetext.getText());
            pst.setDate(2, date);
            
            pst.setString(3, (String) Paymenttype);
            pst.setString(4, "Input");
            pst.setString(5, totalrate.getText());
            pst.setString(6, totalvattext.getText());
            pst.setString(7, totalinvoiceamount.getText());
            pst.setString(8, cusId);
           

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

                    String sql = "Insert into saleDetails(invoiceNo,prcode,unitrate,qty,unit,discount,commesion,totalamount,vat,Totalvat,NetTotal,tradprice,invoicedate) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
          
        
        try {

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/BilRecieptCredit.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();
            para.put("invoiceno", Invoiceno);
           para.put("cusid", cusId);
           para.put("cashier", username);

            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

            JasperPrintManager.printReport(jp, true);
            //JasperViewer.viewReport(jp, true);

        } catch (NumberFormatException | JRException ex) {
            JOptionPane.showMessageDialog(null, ex);

        }
                   }
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
            float vat = (float) 0.00;
            float totalvat;

            float tpd = Float.parseFloat(unitrateText.getText());
            float productvat = tpd * vat;

            if (discounttext.getText().isEmpty()) {

                discount = 0;

            } else {
                discount = Float.parseFloat(discounttext.getText());

            }
            float tpwdisc = tpd - discount;

            float qty = Float.parseFloat(qtytext.getText());
            float nettotaltp = (tpwdisc * qty);
            totalvat = (productvat * qty);
            float totalall = nettotaltp + totalvat;

            model.setValueAt(qty, i, 4);
            model.setValueAt(nettotaltp, i, 7);
            model.setValueAt(totalvat, i, 9);
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
        jLabel10 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        unitrateText = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        discounttext = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        qtytext = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        totaltext = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        unibox = new javax.swing.JComboBox<>();
        addbtn = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
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
        jPanel5 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        invoicetext = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        editinvoicetext = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        refnotext = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
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
        netdiscount = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Credit Pos");
        setExtendedState(6);

        jPanel4.setBackground(new java.awt.Color(67, 86, 86));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Item Description");

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

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Product Code");

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

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Price");

        unitrateText.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        unitrateText.setForeground(new java.awt.Color(153, 0, 0));
        unitrateText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        unitrateText.setBorder(null);
        unitrateText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                unitrateTextunitrateTextKeyPressed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Discount");

        discounttext.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        discounttext.setBorder(null);

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

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Stock");

        totaltext.setEditable(false);
        totaltext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totaltext.setBorder(null);
        totaltext.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        totaltext.setEnabled(false);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Unit");

        unibox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));

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
        jButton16.setBorder(null);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(itemnamesearch, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(totaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(addbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codetext)
                    .addComponent(itemnamesearch)
                    .addComponent(unitrateText)
                    .addComponent(discounttext)
                    .addComponent(qtytext)
                    .addComponent(totaltext)
                    .addComponent(unibox)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4))
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
        customerbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerboxActionPerformed(evt);
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
        jLabel20.setText("Total Credit Amt");

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
        jLabel21.setText("Net Tota(After Inv)");

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
                .addComponent(msgtext, javax.swing.GroupLayout.DEFAULT_SIZE, 15, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(snametext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactnotext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addComponent(currencybox, 0, 257, Short.MAX_VALUE)
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

        jPanel5.setBackground(new java.awt.Color(67, 86, 86));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Invoice No:");

        invoicetext.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        invoicetext.setForeground(new java.awt.Color(255, 255, 255));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("Check ");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        editinvoicetext.setText("Edit Invoice No");
        editinvoicetext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editinvoicetextMouseClicked(evt);
            }
        });
        editinvoicetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editinvoicetextActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Ref No:");

        refnotext.setBorder(null);
        refnotext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refnotextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(refnotext))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(invoicetext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editinvoicetext)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(invoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(editinvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refnotext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

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

        jButton8.setBackground(new java.awt.Color(67, 86, 86));
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Execution");
        jButton8.setBorder(null);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

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
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalrate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

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
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

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
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(totalinvoiceamount, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(totalinvoiceamount, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        netdiscount.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        netdiscount.setForeground(new java.awt.Color(204, 0, 0));
        netdiscount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(67, 86, 86));
        jLabel6.setText("Discount");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(netdiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(netdiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
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
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton2.setBackground(new java.awt.Color(51, 153, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Kitchen Order");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(153, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Bill Reciept");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void codetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();

        }
    }//GEN-LAST:event_codetextKeyPressed

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

                String sql = "Select itemcode,itemName,(select un.unitshort from unit un where un.id=ita.stockunit) as 'stockunit',mrp,(select st.Qty from stock st where st.procode=ita.itemcode) as 'stock'  from item ita where ita.itemcode='" + SearchText + "'";
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

                String sql = "Select itemcode,itemName,(select un.unitshort from unit un where un.id=ita.stockunit) as 'stockunit',mrp,(select st.Qty from stock st where st.procode=ita.itemcode) as 'stock' from item ita where ita.itemName='" + SearchText + "'";
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

    private void unitrateTextunitrateTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateTextunitrateTextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_F5) {
            qtytext.requestFocusInWindow();
        } else {
            qtytext.requestFocusInWindow();

        }
    }//GEN-LAST:event_unitrateTextunitrateTextKeyPressed

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

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        clear();

    }//GEN-LAST:event_jButton16ActionPerformed

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

    private void customerboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerboxActionPerformed
        entrucesure();
        codetext.requestFocusInWindow();
    }//GEN-LAST:event_customerboxActionPerformed

    private void customeridtextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customeridtextKeyPressed

    }//GEN-LAST:event_customeridtextKeyPressed

    private void customeridtextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customeridtextKeyTyped

    }//GEN-LAST:event_customeridtextKeyTyped

    private void previseamonmttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previseamonmttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_previseamonmttextActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        if(snametext.getText().isEmpty() || contactnotext.getText().isEmpty()){

        }else{
            try {

                String sql = "Insert into customerinfo(customername,address,poboxno,city,country,vatReNo,remark,status,customerType,balanceType,OpeningDate,OpenigBalance,DipositAmt,creditAmnt,saleamount,paidamount,totaldiscount,Balancedue,contactname,contactdesignation,TelephoneNo,ContactNo) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);

                pst.setString(1, snametext.getText());
                pst.setString(2, addresstext.getText());
                pst.setString(3, " ");
                pst.setString(4, " ");
                pst.setString(5, " ");
                pst.setString(6, " ");
                pst.setString(7, " ");
                pst.setString(8, " ");
                pst.setString(9, " ");
                pst.setString(10, " ");
                pst.setDate(11, date);
                pst.setString(12, "0");
                pst.setString(13, "0");
                pst.setString(14, "0");
                pst.setString(15, "0");
                pst.setString(16, "0");
                pst.setString(17, "0");
                pst.setString(18, "0");
                pst.setString(19, "0");
                pst.setString(20, "0");
                pst.setString(21, "0");
                pst.setString(22, contactnotext.getText());
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        parchase_code();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void editinvoicetextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editinvoicetextMouseClicked
        editinvoiceno();
    }//GEN-LAST:event_editinvoicetextMouseClicked

    private void editinvoicetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editinvoicetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editinvoicetextActionPerformed

    private void refnotextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refnotextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_refnotextActionPerformed

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        int selectedRowIndex = dataTable.getSelectedRow();

        codetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        unitrateText.setText(model.getValueAt(selectedRowIndex, 3).toString());
        qtytext.setText(model.getValueAt(selectedRowIndex, 4).toString());
        unibox.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());
        updatekey=1;
    }//GEN-LAST:event_dataTableMouseClicked

    private void dataTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMousePressed

    }//GEN-LAST:event_dataTableMousePressed

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
        deleterow();
    }//GEN-LAST:event_dataTableKeyPressed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        parchase_code();
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

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
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
                para.put("invoiceno",  invoicetext.getText());
                para.put("total", total);
                para.put("vat", vat);
                para.put("netTotal", netTotal);
                para.put("Type", "Order");
                para.put("cashier", username);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                // JasperViewer.viewReport(jp, false);
                JasperPrintManager.printReport(jp, true);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
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
                para.put("invoiceno",  invoicetext.getText());
                para.put("total", total);
                para.put("vat", vat);
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
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(Creditsalepoint.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Creditsalepoint.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Creditsalepoint.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Creditsalepoint.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Creditsalepoint().setVisible(true);
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(Creditsalepoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
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
    private javax.swing.JCheckBox editinvoicetext;
    private javax.swing.JLabel invoicetext;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton8;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
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
    private javax.swing.JLabel netdiscount;
    private javax.swing.JTextField netstext;
    private javax.swing.JTextField previseamonmttext;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextField refnotext;
    private javax.swing.JTextField snametext;
    private javax.swing.JLabel totalinvoiceamount;
    private javax.swing.JLabel totalrate;
    private javax.swing.JTextField totaltext;
    private javax.swing.JLabel totalvattext;
    private javax.swing.JComboBox<String> unibox;
    private javax.swing.JTextField unitrateText;
    // End of variables declaration//GEN-END:variables
}
