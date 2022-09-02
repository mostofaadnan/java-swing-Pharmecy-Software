/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.File;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


/**
 *
 * @author adnan
 */
public class CraditInvoice extends javax.swing.JInternalFrame {

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
    /**
     * Creates new form CraditInvoice
     *
     * @throws java.sql.SQLException
     */
    CraditInvoice() throws SQLException, IOException{
        initComponents();
        conn = Java_Connect.conectrDB();
        currentDate();
        Item();
        customer();
        AutoCompleteDecorator.decorate(unibox);
        AutoCompleteDecorator.decorate(itemnamesearch);
        AutoCompleteDecorator.decorate(customerbox);
        unit();
        currency();
        parchase_code();
        invoicetext.setEnabled(false);
        boxcheck();
        paneldactive();
      //  editinvoiceno();
      //  deleterow();
       // entrucesure();
       creditumber();
       updarerext.hide();
    }
    
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
        java.sql.Date date = new java.sql.Date(now.getTime());
        inputdate.setDate(date);

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
            String cash="Cash Customer";
        try {
            String sql = "Select customername from customerInfo where NOT customername ='"+cash+"'";
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
        customernametext.setText(null);
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
        float vat = (float) 0.05;
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
    
    
 
    
    private void saleInsertcheck() {
     //   float chasin = Float.parseFloat(totalinvoiceamount.getText());
     
   
        try {
                  float totaltd = totaltrade();
            float totalamt=Float.parseFloat(totalrate.getText());
            float totalprofite=totalamt-totaltd;
            float discount=Float.parseFloat(netdiscount.getText());
            float profite=totalprofite-discount;
            String sql = "Insert into sale(id,invoiceNo,invoicedate,paymentCurency,paymentType,netdiscount,TotalAmount,TotalVat,Totalinvoice,customerid,Ref_No, totaltraprice,totalprofite) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            
            pst.setInt(1, new_inv);
            pst.setString(2, invoicetext.getText());
            pst.setString(3, ((JTextField) inputdate.getDateEditor().getUiComponent()).getText());
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

            pst.execute();
            // cashInDrwaer(chasin);
           JOptionPane.showMessageDialog(null, "Invoice Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void saleInsert() {
     //   float chasin = Float.parseFloat(totalinvoiceamount.getText());
        try {
          float totaltd = totaltrade();
            float totalamt=Float.parseFloat(totalrate.getText());
            float totalprofite=totalamt-totaltd;
            float discount=Float.parseFloat(netdiscount.getText());
            float profite=totalprofite-discount;
            String sql = "Insert into sale(invoiceNo,invoicedate,paymentCurency,paymentType,netdiscount,TotalAmount,TotalVat,Totalinvoice,customerid,Ref_No ,totaltraprice,totalprofite) values (?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, invoicetext.getText());
            pst.setString(2, ((JTextField) inputdate.getDateEditor().getUiComponent()).getText());
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
            pst.execute();
            // cashInDrwaer(chasin);
           JOptionPane.showMessageDialog(null, "Invoice Data Saved");

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
            pst.setString(2, ((JTextField) inputdate.getDateEditor().getUiComponent()).getText());
            
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
    private void statementinsert(String balancedue) throws SQLException {

        try {

            String sql = "Insert into CraditStatement(customerid,Orderno,type,invoiceno,invoicedate,totalInvoice,Received_amt,remark,balance) values(?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, customeridtext.getText());
            pst.setString(2, "0");
            pst.setString(3, "Invoice");
            pst.setString(4, invoicetext.getText());
            pst.setString(5, ((JTextField) inputdate.getDateEditor().getUiComponent()).getText());
            pst.setString(6, totalinvoiceamount.getText());
            pst.setDouble(7, 0.00);
            pst.setString(8, "Credit Invoice");
            pst.setString(9, balancedue);
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
        float customer_amt=dipositamt+paidamt;
        float aftersaleamt=salesamt+todaysale+openingbalance;
        float dicount=Float.parseFloat(netdiscount.getText());
        float todicount=totaldiscount+dicount;
        afterbalancedue= Math.abs(customer_amt-aftersaleamt);
        if(customer_amt>=aftersaleamt){
        
         balanced=0;
        }else{
        
           balanced=afterbalancedue; 
          
            
        }
       
        String balance=df2.format(balanced);
        
   
 
    
        try {

            String sql = "Update customerInfo set Balancedue='" + balance + "',saleamount='" + aftersaleamt + "',totaldiscount='" + todicount + "' where id='" + cusId + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
         //   JOptionPane.showMessageDialog(null, "Data Update");
          statementinsert(balance);
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
            pst.setString(6, ((JTextField) inputdate.getDateEditor().getUiComponent()).getText());
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
                    pst.setString(13, ((JTextField) inputdate.getDateEditor().getUiComponent()).getText());
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
            pst.setString(5, ((JTextField) inputdate.getDateEditor().getUiComponent()).getText());
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

            String arebic = convertNumberToArabicWords(num);
            String inwords = convertToIndianCurrency(num);
            String convert = inwords + " ( " + arebic + " ) ";
            
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
            float vat = (float) 0.05;
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

        updarerext.setText("0");
        
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        unitrateText = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        qtytext = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        unibox = new javax.swing.JComboBox<>();
        addbtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        discounttext = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        inputdate = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        currencybox = new javax.swing.JComboBox<>();
        updarerext = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        customerbox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        refnotext = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        invoicetext = new javax.swing.JTextField();
        editinvoicetext = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        totalinvoiceamount = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        netdiscount = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        totalrate = new javax.swing.JTextField();
        totalvattext = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        customeridtext = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        previseamonmttext = new javax.swing.JTextField();
        netstext = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        customernametext = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cradit Invoice");
        setFocusable(false);
        setVisible(false);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 0));
        jLabel1.setText("Invoice No");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, 29));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Code");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, 30));

        codetext.setBackground(new java.awt.Color(255, 255, 204));
        codetext.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        codetext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        codetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codetextKeyTyped(evt);
            }
        });
        jPanel2.add(codetext, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 129, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 0));
        jLabel4.setText("Item Name");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(183, 76, 88, -1));

        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        jPanel2.add(itemnamesearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(275, 70, 253, 30));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 102, 0));
        jLabel5.setText("Unit Rate");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(532, 70, -1, 30));

        unitrateText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        unitrateText.setForeground(new java.awt.Color(153, 0, 0));
        jPanel2.add(unitrateText, new org.netbeans.lib.awtextra.AbsoluteConstraints(603, 70, 94, 30));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 0, 0));
        jLabel6.setText("Qty");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(701, 70, -1, 30));

        qtytext.setBackground(new java.awt.Color(255, 255, 204));
        qtytext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        qtytext.setForeground(new java.awt.Color(0, 102, 0));
        qtytext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        qtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytextKeyPressed(evt);
            }
        });
        jPanel2.add(qtytext, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 70, 83, 30));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Unit");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(817, 70, 38, 30));

        unibox.setEditable(true);
        unibox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        unibox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        jPanel2.add(unibox, new org.netbeans.lib.awtextra.AbsoluteConstraints(859, 70, 100, 30));

        addbtn.setBackground(new java.awt.Color(153, 0, 0));
        addbtn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        addbtn.setForeground(new java.awt.Color(255, 255, 255));
        addbtn.setText("ADD");
        addbtn.setBorder(null);
        addbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addbtnActionPerformed(evt);
            }
        });
        jPanel2.add(addbtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1125, 69, 54, 30));

        jButton2.setBackground(new java.awt.Color(153, 0, 0));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("CLEAR");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1185, 70, 61, 30));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 102, 0));
        jLabel15.setText("Discount");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(963, 70, -1, 30));

        discounttext.setBackground(new java.awt.Color(255, 255, 204));
        discounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        discounttext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel2.add(discounttext, new org.netbeans.lib.awtextra.AbsoluteConstraints(1033, 70, 86, 30));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("Date");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 10, -1, 29));

        inputdate.setDateFormatString("yyyy-MM-dd");
        inputdate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel2.add(inputdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 10, 160, 29));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("Curency Type");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, -1, 29));

        currencybox.setEditable(true);
        currencybox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel2.add(currencybox, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 10, 79, 29));

        updarerext.setText("0");
        jPanel2.add(updarerext, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 80, 14, -1));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("Customer Name");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 10, -1, 30));

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
        jPanel2.add(customerbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 13, 290, 30));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Ref No:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 10, 40, 32));
        jPanel2.add(refnotext, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 10, 110, 33));

        jButton1.setBackground(new java.awt.Color(204, 0, 0));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Check");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 10, 80, 30));

        invoicetext.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jPanel2.add(invoicetext, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 180, 30));

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
        jPanel2.add(editinvoicetext, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, -1, -1));

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
                false, false, true, false, false, false, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTable.setRowHeight(30);
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
        jScrollPane1.setViewportView(dataTable);
        if (dataTable.getColumnModel().getColumnCount() > 0) {
            dataTable.getColumnModel().getColumn(2).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        }

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 51)));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 0, 0));
        jLabel8.setText("Total Amount");

        totalinvoiceamount.setBackground(new java.awt.Color(255, 255, 153));
        totalinvoiceamount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalinvoiceamount.setForeground(new java.awt.Color(0, 51, 0));
        totalinvoiceamount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalinvoiceamount.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        totalinvoiceamount.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalinvoiceamount.setEnabled(false);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(204, 0, 0));
        jLabel9.setText("VAT(5%)");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 0, 0));
        jLabel10.setText("Invoice Amount");

        netdiscount.setBackground(new java.awt.Color(204, 204, 255));
        netdiscount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        netdiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netdiscount.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        netdiscount.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        netdiscount.setEnabled(false);
        netdiscount.setSelectedTextColor(new java.awt.Color(51, 51, 51));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 153, 51));
        jLabel14.setText("Total Discount");

        totalrate.setBackground(new java.awt.Color(255, 204, 204));
        totalrate.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalrate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalrate.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        totalrate.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalrate.setEnabled(false);

        totalvattext.setBackground(new java.awt.Color(204, 255, 204));
        totalvattext.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalvattext.setForeground(new java.awt.Color(204, 51, 0));
        totalvattext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalvattext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        totalvattext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalvattext.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(45, 45, 45)
                        .addComponent(totalrate, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(netdiscount)
                            .addComponent(totalvattext)
                            .addComponent(totalinvoiceamount))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(netdiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalinvoiceamount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 51)));

        jButton3.setBackground(new java.awt.Color(255, 0, 0));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Execute");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 102, 0));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Reload");
        jButton4.setBorder(null);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton5.setText("Cancel");
        jButton5.setBorder(null);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 51)));

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("Customer Code");

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

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("Total Credit Amt(prev)");

        previseamonmttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        previseamonmttext.setForeground(new java.awt.Color(204, 0, 0));
        previseamonmttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        previseamonmttext.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        previseamonmttext.setEnabled(false);
        previseamonmttext.setSelectedTextColor(new java.awt.Color(102, 0, 0));

        netstext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        netstext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netstext.setDisabledTextColor(new java.awt.Color(102, 0, 0));
        netstext.setEnabled(false);
        netstext.setSelectedTextColor(new java.awt.Color(102, 0, 0));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 0, 0));
        jLabel13.setText("Net Tota(After Inv)");

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));
        jLabel20.setText("Customer Name");

        customernametext.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        customernametext.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        customernametext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        customernametext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        customernametext.setEnabled(false);
        customernametext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                customernametextKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                customernametextKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customernametext)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(customeridtext, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(netstext, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(previseamonmttext, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 110, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customeridtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(customernametext, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(previseamonmttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(netstext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1338, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 948, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBounds(0, 10, 1360, 558);
    }// </editor-fold>//GEN-END:initComponents

    private void codetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyReleased
        itemnamesearch.removeAllItems();

        String SearchText = (String) codetext.getText();
        try {

            String sql = "Select id,itemName, mrp from item where id='" + SearchText + "' OR barcode='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            if (rs.next()) {

                String name = rs.getString("itemName");
                itemnamesearch.setSelectedItem(name);
                String unitrate = rs.getString("mrp");
                unitrateText.setText(unitrate);

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {
            Item();
        } catch (SQLException ex) {
            Logger.getLogger(CraditInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_codetextKeyReleased

    private void codetextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyTyped
        unitrateText.setText(null);
        itemnamesearch.setSelectedIndex(0); 
    }//GEN-LAST:event_codetextKeyTyped

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
        String SearchText = (String) itemnamesearch.getSelectedItem();
        try {

            String sql = "Select id,itemName,stockunit,mrp from item where itemName='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("id");
                codetext.setText(Id);
                String unitrate = rs.getString("mrp");
                unitrateText.setText(unitrate);
                String unit = rs.getString("stockunit");
                unibox.setSelectedItem(unit);
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }


    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void qtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyPressed

        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
        } else if ("0".equals(updarerext.getText())) {
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
    }//GEN-LAST:event_qtytextKeyPressed

    private void addbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbtnActionPerformed
        if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0 || customerbox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
        } else if ("0".equals(updarerext.getText())) {
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
    }//GEN-LAST:event_addbtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clear();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        int selectedRowIndex = dataTable.getSelectedRow();
        updarerext.setText("1");
        codetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        unitrateText.setText(model.getValueAt(selectedRowIndex, 3).toString());
        qtytext.setText(model.getValueAt(selectedRowIndex, 4).toString());
        unibox.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());
        
      
    }//GEN-LAST:event_dataTableMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        parchase_code();
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
     int emptytbl=model.getRowCount();
        
        if(invoicetext.getText().isEmpty()){
      
       JOptionPane.showMessageDialog(null, "Please Check Invoice No");
      }else{
      if(emptytbl>0){
        try {
           // saleInsert();
            
           invoicecheck();
           SaleDetailsInsert();
           // statementinsert();
            Stockpdate();
            customerbalanceupdate();
            vatcollection();
            printInvoice();
            AfterExecute();

        } catch (SQLException | JRException ex) {
            Logger.getLogger(SalePoint.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      }
      


    }//GEN-LAST:event_jButton3ActionPerformed

    private void customeridtextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customeridtextKeyPressed

        if (totalinvoiceamount.getText().isEmpty() || customeridtext.getText().isEmpty()) {
        } else {

            float creditAmunt = Float.parseFloat(customeridtext.getText());
            float paidAmount = Float.parseFloat(totalinvoiceamount.getText());
            if (evt.getKeyCode() != KeyEvent.VK_ENTER) {
            } else if (paidAmount <= creditAmunt) {
                float balanceDue = (creditAmunt - paidAmount);
                String balanceDuestring = String.format("%.2f", balanceDue);
                netstext.setText(balanceDuestring);
                previseamonmttext.setText(totalinvoiceamount.getText());

            } else {

            }
        }

    }//GEN-LAST:event_customeridtextKeyPressed

    private void customeridtextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customeridtextKeyTyped

    }//GEN-LAST:event_customeridtextKeyTyped

    private void customernametextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customernametextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_customernametextKeyPressed

    private void customernametextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customernametextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_customernametextKeyTyped

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
                customernametext.setText(Name);
                
                
                
                openingbalance=rs.getFloat("OpenigBalance");
                dipositamt=rs.getFloat("DipositAmt");
               
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
    }//GEN-LAST:event_customerboxActionPerformed

    private void dataTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMousePressed
    
    }//GEN-LAST:event_dataTableMousePressed

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
        deleterow();
    }//GEN-LAST:event_dataTableKeyPressed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
parchase_code();     
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
      if(editinvoicetext.isSelected() ){
      if(invoicetext.getText().isEmpty()){
      JOptionPane.showMessageDialog(null, "Please Fillup Give Credit Invoice No");
      }else
      {
      String Applyinvoicenumber=invoicetext.getText();
        try {
            String sql = "Select invoiceNo from sale where invoiceNo='"+Applyinvoicenumber+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()==true) {
             JOptionPane.showMessageDialog(null, "this Number Already Exits");
             invoicetext.setText(null);
            }else{
            invoicetext.setText(Applyinvoicenumber);
                
                
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
      
      
      
      }
      
      
      }else{
      parchase_code();
      
      }
        
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void editinvoicetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editinvoicetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editinvoicetextActionPerformed

    private void editinvoicetextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editinvoicetextMouseClicked
      editinvoiceno();
    }//GEN-LAST:event_editinvoicetextMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addbtn;
    private javax.swing.JTextField codetext;
    private javax.swing.JComboBox<String> currencybox;
    private javax.swing.JComboBox<String> customerbox;
    private javax.swing.JTextField customeridtext;
    private javax.swing.JTextField customernametext;
    private javax.swing.JTable dataTable;
    private javax.swing.JTextField discounttext;
    private javax.swing.JCheckBox editinvoicetext;
    private com.toedter.calendar.JDateChooser inputdate;
    private javax.swing.JTextField invoicetext;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField netdiscount;
    private javax.swing.JTextField netstext;
    private javax.swing.JTextField previseamonmttext;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextField refnotext;
    private javax.swing.JTextField totalinvoiceamount;
    private javax.swing.JTextField totalrate;
    private javax.swing.JTextField totalvattext;
    private javax.swing.JComboBox<String> unibox;
    private javax.swing.JTextField unitrateText;
    private javax.swing.JLabel updarerext;
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
        String digits[] = {"", "Hundred", "Thousand", "Lakh", "Crore"};
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

        String paise = (decimal) > 0 ? " And " + words.get((int) (decimal - decimal % 10)) + " " + words.get((int) (decimal % 10)) : "";
        return Rupees + paise + "Files" + " " + "AED Only";
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
                    return convertOneDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 2:
                    return convertTwoDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 3:
                    return convertThreeDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 4:
                    return convertFourDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 5:
                    return convertFiveDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 6:
                    return convertSixDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
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
                return "واحد";
            case 2:
                return "إثنان";
            case 3:
                return "ثلاثه";
            case 4:
                return "أربعه";
            case 5:
                return "خمسه";
            case 6:
                return "سته";
            case 7:
                return "سبعه";
            case 8:
                return "ثمانيه";
            case 9:
                return "تسعه";
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
                        return "إحدىعشر";
                    }
                    if (getIntVal(twoDigits.charAt(1)) == 2) {
                        return "إثنىعشر";
                    } else {
                        return convertOneDigits(String.valueOf(twoDigits.charAt(1))) + " " + "عشر";
                    }
                }
                case 2:// 2x x:not 0
                    returnAlpha = "عشرون";
                    break;
                case 3:// 3x x:not 0
                    returnAlpha = "ثلاثون";
                    break;
                case 4:// 4x x:not 0
                    returnAlpha = "أريعون";
                    break;
                case 5:// 5x x:not 0
                    returnAlpha = "خمسون";
                    break;
                case 6:// 6x x:not 0
                    returnAlpha = "ستون";
                    break;
                case 7:// 7x x:not 0
                    returnAlpha = "سبعون";
                    break;
                case 8:// 8x x:not 0
                    returnAlpha = "ثمانون";
                    break;
                case 9:// 9x x:not 0
                    returnAlpha = "تسعون";
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
            return convertOneDigits(String.valueOf(twoDigits.charAt(1))) + " و " + returnAlpha;
        }
    }

    private static String convertThreeDigits(String threeDigits) {

// check the first digit x00
        switch (getIntVal(threeDigits.charAt(0))) {

            case 1: {// 100 - 199
                if (getIntVal(threeDigits.charAt(1)) == 0) {// 10x
                    if (getIntVal(threeDigits.charAt(2)) == 0) {// 100
                        return "مائه";
                    } else {// 10x x: is not 0
                        return "مائه" + " و " + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else {// 1xx x: is not 0
                    return "مائه" + " و " + convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            case 2: {// 200 - 299
                if (getIntVal(threeDigits.charAt(1)) == 0) {// 20x
                    if (getIntVal(threeDigits.charAt(2)) == 0) {// 200
                        return "مائتين";
                    } else {// 20x x:not 0
                        return "مائتين" + " و " + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else {// 2xx x:not 0
                    return "مائتين" + " و " + convertTwoDigits(threeDigits.substring(1, 3));
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
                        return convertOneDigits(String.valueOf(threeDigits.charAt(1) + "مائه"));
                    } else {// x0x x:not 0
                        return convertOneDigits(String.valueOf(threeDigits.charAt(0))) + "مائه" + " و "
                                + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else {// xxx x:not 0
                    return convertOneDigits(String.valueOf(threeDigits.charAt(0))) + "مائه" + " و "
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
                            return "ألف";
                        } else {// 100x x:not 0
                            return "ألف" + " و " + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else {// 10xx x:not 0
                        return "ألف" + " و " + convertTwoDigits(fourDigits.substring(2, 3));
                    }
                } else {// 1xxx x:not 0
                    return "ألف" + " و " + convertThreeDigits(fourDigits.substring(1, 4));
                }
            }
            case 2: {// 2000 - 2999
                if (getIntVal(fourDigits.charAt(1)) == 0) {// 20xx
                    if (getIntVal(fourDigits.charAt(2)) == 0) {// 200x
                        if (getIntVal(fourDigits.charAt(3)) == 0) {// 2000
                            return "ألفين";
                        } else {// 200x x:not 0
                            return "ألفين" + " و " + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else {// 20xx x:not 0
                        return "ألفين" + " و " + convertTwoDigits(fourDigits.substring(2, 3));
                    }
                } else {// 2xxx x:not 0
                    return "ألفين" + " و " + convertThreeDigits(fourDigits.substring(1, 4));
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
                            return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ألاف";
                        } else {// x00x x:not 0
                            return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ألاف" + " و "
                                    + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else {// x0xx x:not 0
                        return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ألاف" + " و "
                                + convertTwoDigits(fourDigits.substring(2, 3));
                    }
                } else {// xxxx x:not 0
                    return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ألاف" + " و "
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
            return convertTwoDigits(fiveDigits.substring(0, 2)) + " ألف ";
        } else {// xxxxx x:not 0
            return convertTwoDigits(fiveDigits.substring(0, 2)) + " ألفا " + " و "
                    + convertThreeDigits(fiveDigits.substring(2, 5));
        }
    }

    private static String convertSixDigits(String sixDigits) {

        if (convertThreeDigits(sixDigits.substring(2, 5)).length() == 0) {// xxx000
// x:not
// 0
            return convertThreeDigits(sixDigits.substring(0, 3)) + " ألف ";
        } else {// xxxxxx x:not 0
            return convertThreeDigits(sixDigits.substring(0, 3)) + " ألفا " + " و "
                    + convertThreeDigits(sixDigits.substring(3, 6));
        }
    }

    private static int getIntVal(char c) {
        return Integer.parseInt(String.valueOf(c));
    }


}
