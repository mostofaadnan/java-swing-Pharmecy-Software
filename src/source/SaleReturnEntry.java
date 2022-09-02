/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
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
public final class SaleReturnEntry extends javax.swing.JInternalFrame {

    int tree = 0;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    double tpprice;
    double bankbalance1;

    String Invoiceno;

    String cusId;

    double openingbalance;
    double dipositamt;

    double salesamt;
    double balancedue;
    double paidamt;
    double totaldiscount;
    String Paymenttype = "Credit";
    int new_inv = 1;
    String proId;
   int updatekey=0;
   java.sql.Date date;
   double creditamt=0;
   double cashamt=0;
   String invoiceId=null;
   double returncashamount=0;
    /**
     * Creates new form SaleReturnEntry
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public SaleReturnEntry() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        currentDate();
        Item();
        AutoCompleteDecorator.decorate(unibox);
        AutoCompleteDecorator.decorate(itemnamesearch);

        unit();

        returninvoicenotext.setEnabled(false);
        parchase_code();
        currentDate(); 
        //balance();
    }
    
       public SaleReturnEntry(String invoiceNo,String PaymentType) throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        currentDate();
        Item();
        AutoCompleteDecorator.decorate(unibox);
        AutoCompleteDecorator.decorate(itemnamesearch);

        unit();

        returninvoicenotext.setEnabled(false);
        parchase_code();
        currentDate(); 
        paymenttypeboxdetails.setSelectedItem(PaymentType);
        Serach_byJump(invoiceNo);
        Creditcustomercheck();
        
        //balance();
    }
       
       
         private void Serach_byJump(String invoiceno) throws SQLException {
                  InvoiceText.setText(invoiceno);
                  String invoiceNo = InvoiceText.getText();
            switch (paymenttypeboxdetails.getSelectedIndex()) {
                case 1:
                    try {
                        String sql = "Select prcode as 'Item Code', (select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item', sl.unitrate as 'Unit Rate', sl.qty as 'Qty',sl.unit as 'Unit', sl.discount as 'Discount', sl.commesion as 'Commesstion', sl.totalamount as 'Total Amount', sl.vat as 'Vat(5%)', sl.Totalvat as 'Total Vat',sl.NetTotal   from cashsaledetails sl where sl.invoiceNo='" + invoiceNo + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    try {
                        String sql = "Select invoiceNo,invoicedate,paymentType,paymentCurency,TotalAmount,TotalVat,Totalinvoice,customerid,(select ci.customername from customerInfo ci where ci.id=sl.customerid ) as 'customername' from cashsale sl  where sl.invoiceNo='" + invoiceNo + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        if (rs.next()) {

                       invoiceId = rs.getString("invoiceNo");
                            /// invoiceapplytex.setText(Id);
                            String datehere = rs.getString("invoicedate");
                            Datetext.setText(datehere);

                            String payment = rs.getString("paymentType");
                            //paymenttypetext.setText(payment);
                            String totalam = rs.getString("TotalAmount");
                            totalamounttext.setText(totalam);
                            String tovat = rs.getString("TotalVat");
                            totalVatText.setText(tovat);
                            String netinoice = rs.getString("Totalinvoice");
                            netTotalText.setText(netinoice);
                            cusId = rs.getString("customerid");
                            String customername = rs.getString("customername");
                            customertext.setText(customername);

                            //  currency=rs.getString("paymentCurency");
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    break;
                case 2:
                    try {
                        String sql = "Select prcode as 'Item Code', (select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item', sl.unitrate as 'Unit Rate', sl.qty as 'Qty',sl.unit as 'Unit', sl.discount as 'Discount', sl.commesion as 'Commesstion', sl.totalamount as 'Total Amount', sl.vat as 'Vat(5%)', sl.Totalvat as 'Total Vat',sl.NetTotal   from saledetails sl where sl.invoiceNo='" + invoiceNo + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    try {
                        String sql = "Select invoiceNo,invoicedate,paymentType,paymentCurency,TotalAmount,TotalVat,Totalinvoice,customerid,(select ci.customername from customerInfo ci where ci.id=sl.customerid ) as 'customername' from sale sl  where sl.invoiceNo='" + invoiceNo + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        if (rs.next()) {

                            invoiceId = rs.getString("invoiceNo");
                            /// invoiceapplytex.setText(Id);
                            String dateheer = rs.getString("invoicedate");
                            Datetext.setText(dateheer);

                            String payment = rs.getString("paymentType");
                            //paymenttypetext.setText(payment);
                            String totalam = rs.getString("TotalAmount");
                            totalamounttext.setText(totalam);
                            String tovat = rs.getString("TotalVat");
                            totalVatText.setText(tovat);
                            String netinoice = rs.getString("Totalinvoice");
                            netTotalText.setText(netinoice);
                            cusId = rs.getString("customerid");
                            String customername = rs.getString("customername");
                            customertext.setText(customername);

                            //   currency=rs.getString("paymentCurency");
                        } 
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    break;
                default:
                    break;
            }

     

    }
   private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        //  inputdate.setDate(date);

    }
    private void Serach1() throws SQLException {
        if (InvoiceText.getText().isEmpty()) {

        } else {

            String invoiceNo = InvoiceText.getText();
            switch (paymenttypeboxdetails.getSelectedIndex()) {
                case 1:
                    try {
                        String sql = "Select prcode as 'Item Code', (select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item', sl.unitrate as 'Unit Rate', sl.qty as 'Qty',sl.unit as 'Unit', sl.discount as 'Discount', sl.commesion as 'Commesstion', sl.totalamount as 'Total Amount', sl.vat as 'Vat(5%)', sl.Totalvat as 'Total Vat',sl.NetTotal   from cashsaledetails sl where sl.invoiceNo='" + invoiceNo + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    try {
                        String sql = "Select invoiceNo,invoicedate,paymentType,paymentCurency,TotalAmount,TotalVat,Totalinvoice,customerid,(select ci.customername from customerInfo ci where ci.id=sl.customerid ) as 'customername' from cashsale sl  where sl.invoiceNo='" + invoiceNo + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        if (rs.next()) {

                       invoiceId = rs.getString("invoiceNo");
                            /// invoiceapplytex.setText(Id);
                            String datehere = rs.getString("invoicedate");
                            Datetext.setText(datehere);

                            String payment = rs.getString("paymentType");
                            //paymenttypetext.setText(payment);
                            String totalam = rs.getString("TotalAmount");
                            totalamounttext.setText(totalam);
                            String tovat = rs.getString("TotalVat");
                            totalVatText.setText(tovat);
                            String netinoice = rs.getString("Totalinvoice");
                            netTotalText.setText(netinoice);
                            cusId = rs.getString("customerid");
                            String customername = rs.getString("customername");
                            customertext.setText(customername);

                            //  currency=rs.getString("paymentCurency");
                        } else {
                            clearbox();
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    break;
                case 2:
                    try {
                        String sql = "Select prcode as 'Item Code', (select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item', sl.unitrate as 'Unit Rate', sl.qty as 'Qty',sl.unit as 'Unit', sl.discount as 'Discount', sl.commesion as 'Commesstion', sl.totalamount as 'Total Amount', sl.vat as 'Vat(5%)', sl.Totalvat as 'Total Vat',sl.NetTotal   from saledetails sl where sl.invoiceNo='" + invoiceNo + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    try {
                        String sql = "Select invoiceNo,invoicedate,paymentType,paymentCurency,TotalAmount,TotalVat,Totalinvoice,customerid,(select ci.customername from customerInfo ci where ci.id=sl.customerid ) as 'customername' from sale sl  where sl.invoiceNo='" + invoiceNo + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        if (rs.next()) {

                            invoiceId = rs.getString("invoiceNo");
                            /// invoiceapplytex.setText(Id);
                            String dateheer = rs.getString("invoicedate");
                            Datetext.setText(dateheer);

                            String payment = rs.getString("paymentType");
                            //paymenttypetext.setText(payment);
                            String totalam = rs.getString("TotalAmount");
                            totalamounttext.setText(totalam);
                            String tovat = rs.getString("TotalVat");
                            totalVatText.setText(tovat);
                            String netinoice = rs.getString("Totalinvoice");
                            netTotalText.setText(netinoice);
                            cusId = rs.getString("customerid");
                            String customername = rs.getString("customername");
                            customertext.setText(customername);

                            //   currency=rs.getString("paymentCurency");
                        } else {
                            clearbox();
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    break;
                default:
                    break;
            }

        }

    }

    private void Creditcustomercheck() {

        try {
            String sql = "Select saleamount,paidamount,balancedue,totaldiscount,creditAmnt,cashamt from customerInfo  where id='" + cusId + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {
                salesamt = rs.getDouble("saleamount");
                paidamt = rs.getDouble("paidamount");
                balancedue = rs.getDouble("Balancedue");
                totaldiscount = rs.getDouble("totaldiscount");
                creditamt=rs.getDouble("creditAmnt");
                cashamt=rs.getDouble("cashamt");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
  
    private static final DecimalFormat df2 = new DecimalFormat("#.00");
    private void customerbalanceupdate() throws SQLException {
        double afterbalancedue=0;
               
        double todaysale = Double.parseDouble(totalinvoiceamount.getText());
     
        double aftersaleamt = salesamt - todaysale;
        String saleamt=df2.format(aftersaleamt);
        double dicount = Double.parseDouble(netdiscount.getText());
        double todicount = totaldiscount - dicount;
        String disct=df2.format(todicount);
      
         if(balancedue==0){
                afterbalancedue=0;
                returncashamount=todaysale;
                }else{
         if(todaysale>balancedue){
         returncashamount=todaysale-balancedue;
         afterbalancedue=0;
         
         }else{
         afterbalancedue =balancedue-todaysale;
          returncashamount=0;
         }
          
         }
        String dues=df2.format(afterbalancedue);
        
        double aftercreditamt=creditamt-todaysale;
        String credamts=df2.format(aftercreditamt);

        try {

            String sql = "Update customerInfo set creditAmnt='"+credamts+"',Balancedue='" + dues + "',saleamount='" + saleamt + "',totaldiscount='" + disct + "' where id='" + cusId + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //   JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
   private void customerbalanceupdatecash() throws SQLException {
    
        double todaysale = Double.parseDouble(totalinvoiceamount.getText());
     
        double aftersaleamt = salesamt - todaysale;
        String saleamt=df2.format(aftersaleamt);
        double dicount = Double.parseDouble(netdiscount.getText());
        double todicount = totaldiscount - dicount;
        String disct=df2.format(todicount);
        
        returncashamount=todaysale;
        double aftercashamt=cashamt-todaysale;
        String cashamts=df2.format(aftercashamt);

        try {

            String sql = "Update customerInfo set cashamt='"+cashamts+"',saleamount='" + saleamt + "',totaldiscount='" + disct + "' where id='" + cusId + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //   JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

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

                double cashin = rs.getDouble("sum(cashin)");
                double cashout = rs.getDouble("sum(cashout)");
                bankbalance1 = (cashin - cashout);

            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void parchase_code() {
        try {

            String NewParchaseCode = ("SR-" + new_inv + "");
            String sql = "Select id from salereturn";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = ("SR-" + ParchaseCode + "");
            }

            returninvoicenotext.setText(NewParchaseCode);
        } catch (SQLException | NumberFormatException e) {
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

    private void proItem() throws SQLException {

        try {
            String sql = "Select itemName from item where id='" + proId + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("itemName");
                itemnamesearch.setSelectedItem(name);
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

    private void AfterExecute() {

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);
        totalrate.setText(null);
        totalvattext.setText(null);
        totalinvoiceamount.setText(null);
        //  customerbox.setSelectedIndex(0);
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

    private void entryData() {
        double discount;
        double vat = (double) 0.05;
        double totalvat;

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        tree++;

        double tpd = Double.parseDouble(unitrateText.getText());
        double productvat = tpd * vat;
        String prorductvatfromat=df2.format(productvat);
        double productvatfloat=Double.parseDouble(prorductvatfromat);
        if (discounttext.getText().isEmpty()) {

            discount = 0;

        } else {
            discount = Double.parseDouble(discounttext.getText());

        }
        double tpwdisc = tpd - discount;

        float qty = Float.parseFloat(qtytext.getText());
        double nettotaltp = (tpwdisc * qty);
        totalvat = (productvatfloat * qty);
         String totalvatformat=df2.format(totalvat);
        double totalvatfloat=Double.parseDouble(totalvatformat);
        double totalall = nettotaltp + totalvatfloat;

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

    }

    private void clearreturn() {
        InvoiceText.setText(null);
        paymenttypeboxdetails.setSelectedIndex(0);
        customertext.setText(null);
        Datetext.setText(null);

        totalamounttext.setText(null);
        totalVatText.setText(null);
        totalVatText.setText(null);
        netTotalText.setText(null);
        returninvoicenotext.setText(null);

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);

        DefaultTableModel model3 = (DefaultTableModel) datatbl1.getModel();
        model3.setRowCount(0);
        totalrate.setText(null);
        netdiscount.setText(null);
        totalvattext.setText(null);
        totalinvoiceamount.setText(null);
        
        parchase_code();

    }

    private void clearbox() {

        customertext.setText(null);
        Datetext.setText(null);

        totalamounttext.setText(null);
        totalVatText.setText(null);
        totalVatText.setText(null);
        netTotalText.setText(null);

        DefaultTableModel model3 = (DefaultTableModel) datatbl1.getModel();
        model3.setRowCount(0);
        parchase_code();

    }

    private void salereturnInsert() {
        double cashout = Double.parseDouble(totalinvoiceamount.getText());
        try {
             switch (paymenttypeboxdetails.getSelectedIndex()) {
                case 1:
                    cashInDrwaer(cashout);
                    customerbalanceupdatecash();
                    break;
                case 2:
                    customerbalanceupdate();
                    break;
                default:
                    break;
            }
            String sql = "Insert into salereturn(ReturnNo,invoiceNo,invoicetype,returndate,TotalAmount,TotalVat,Totalinvoice,returnacash,prevdue,customerid) values (?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, returninvoicenotext.getText());
            pst.setString(2, invoiceId);
            pst.setString(3, (String) paymenttypeboxdetails.getSelectedItem());
            pst.setDate(4, date);

            pst.setString(5, totalrate.getText());
            pst.setString(6, totalvattext.getText());
            pst.setString(7, totalinvoiceamount.getText());
            pst.setDouble(8,returncashamount);
            pst.setDouble(9,balancedue);
            pst.setString(10, cusId);
            //pst.setString(10, refnotext.getText());

            pst.execute();
           

            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void saleinsertupdate() {
        try {

            double totalratehere = Double.parseDouble(totalamounttext.getText());
            double returntotalrate = Double.parseDouble(totalrate.getText());

            double remaintotalrate = (totalratehere - returntotalrate);
            String remaintotalrates=df2.format(remaintotalrate);
            double totalvat = Double.parseDouble(totalVatText.getText());
            double returnvat = Double.parseDouble(totalvattext.getText());
            double remainvat = totalvat - returnvat;
            String remainvats=df2.format(remainvat);

            double totalinvoice = remaintotalrate + remainvat;
            
            String totalinvoices=df2.format(totalinvoice);
            
            if (remaintotalrate == 0) {
                vatdelete();
                if (paymenttypeboxdetails.getSelectedIndex() == 1) {

                    try {
                        String sql = "Delete from cashsale where invoiceNo=?";
                        pst = conn.prepareStatement(sql);

                        pst.setString(1, invoiceId);

                        pst.execute();
                        // JOptionPane.showMessageDialog(null, "Data Deleted");

                    } catch (SQLException | HeadlessException e) {
                        JOptionPane.showMessageDialog(null, e);

                    }

                    try {
                        String sql = "Delete from cashsaledetails where invoiceNo=?";
                        pst = conn.prepareStatement(sql);

                        pst.setString(1, invoiceId);

                        pst.execute();
                        // JOptionPane.showMessageDialog(null, "Data Deleted");

                    } catch (SQLException | HeadlessException e) {
                        JOptionPane.showMessageDialog(null, e);

                    }

                } else {

                    try {
                        String sql = "Delete from sale where invoiceNo=?";
                        pst = conn.prepareStatement(sql);

                        pst.setString(1, invoiceId);

                        pst.execute();
                        //  JOptionPane.showMessageDialog(null, "Data Deleted");

                    } catch (SQLException | HeadlessException e) {
                        JOptionPane.showMessageDialog(null, e);

                    }

                    try {
                        String sql = "Delete from saledetails where invoiceNo=?";
                        pst = conn.prepareStatement(sql);

                        pst.setString(1, invoiceId);

                        pst.execute();
                        // JOptionPane.showMessageDialog(null, "Data Deleted");

                    } catch (SQLException | HeadlessException e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    stamentdelete();
                }

            } else {
                if (paymenttypeboxdetails.getSelectedIndex() == 1) {

                    String sql = "Update cashsale set TotalAmount='" + remaintotalrates + "',TotalVat='" + remainvats + "',Totalinvoice='" + totalinvoices + "' where invoiceNo='" + invoiceId + "'";
                    pst = conn.prepareStatement(sql);

                    pst.execute();
                    //JOptionPane.showMessageDialog(null, "Data Update");

                } else {

                    String sql = "Update sale set TotalAmount='" + remaintotalrates + "',TotalVat='" + remainvats + "',Totalinvoice='" + totalinvoices + "' where invoiceNo='" + invoiceId + "'";
                    pst = conn.prepareStatement(sql);

                    pst.execute();
                    //JOptionPane.showMessageDialog(null, "Data Update");

                    String sql1 = "Update CraditStatement set totalInvoice='" + totalinvoices + "' where invoiceno='" + invoiceId + "'";
                    pst = conn.prepareStatement(sql1);

                    pst.execute();

                }

                // vat Updae
                String sql = "Update vatcollection set TotalAmount='" + remaintotalrates + "',TotalVat='" + remainvats + "',Totalinvoice='" + totalinvoices + "' where invoiceNopurchaseNo='" + invoiceId + "'";
                pst = conn.prepareStatement(sql);

                pst.execute();
                //  JOptionPane.showMessageDialog(null, "Data Update");

            saledeitailsupdate();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    
     private void saledeitailsupdate() throws SQLException {
        int row = dataTable.getRowCount();
          for (int j = 0; j <row; j++) {

                String remainprocode= (String) dataTable.getValueAt(j, 1);

                float returnqty = (float) dataTable.getValueAt(j, 4);

                double returntotalamount = (Double) dataTable.getValueAt(j, 7);

               // double retrunvat = (Double) dataTable.getValueAt(i, 8);
                double returntotalvat = (Double) dataTable.getValueAt(j, 9);
                double retrunneTotal = (Double) dataTable.getValueAt(j, 10);
              

                    switch (paymenttypeboxdetails.getSelectedIndex()) {
                        case 1: {
                          checkByProductCodecashsale(remainprocode, returnqty,returntotalamount,returntotalvat,retrunneTotal);
                            break;
                        }
                        case 2: {
                         checkByProductCodecreditsale(remainprocode, returnqty,returntotalamount,returntotalvat,retrunneTotal);
                            break;
                        }
                        //nothing
                        default:
                            break;
                    }

                }

        

    }
private void checkByProductCodecashsale( String prcode,float returnqty,double returntotalamount,double returntotalvat,double retrunneTotal){

  try {
                       String sql = "Select  qty,totalamount,Totalvat,NetTotal  from cashsaledetails where  invoiceNo='" + invoiceId + "' AND prcode='" + prcode + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        if (rs.next()) {
                       float qty=rs.getFloat("qty");
                       double totalamt=rs.getDouble("totalamount");
                       double Totalvat=rs.getDouble("Totalvat");
                       double NetTotal=rs.getDouble("NetTotal");
                       //return balance
                       float remainqty=qty-returnqty;
                       double remaintotalamt=totalamt-returntotalamount;
                       double remaintotalvat=Totalvat-returntotalvat;
                       double remaintotalinvoice=NetTotal-retrunneTotal;
                       if(remainqty==0){
                       deletecashsaleDetailsbyProduct(prcode);
                       }else{
                       
                        updateDataTablecashsaleDetails(prcode,remainqty,remaintotalamt,remaintotalvat,remaintotalinvoice);
                       }
                       
                      
                        } 
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }



}

private void updateDataTablecashsaleDetails(String procode, float remainqty,double remaintotalamt,double remaintotalvat, double remaintotalinvoice){

 try {
     String sql="UPDATE cashsaledetails  SET qty ='"+remainqty+"',totalamount='"+remaintotalamt+"',Totalvat='"+remaintotalvat+"',NetTotal='"+remaintotalinvoice+"' where  invoiceNo='" + invoiceId + "' AND prcode='" + procode + "'"; 
                           pst = conn.prepareStatement(sql);
                            pst.execute();
     
     
 
 }catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }

}
   

private void checkByProductCodecreditsale( String prcode,float returnqty,double returntotalamount,double returntotalvat,double retrunneTotal){

  try {
                       String sql = "Select  qty,totalamount,Totalvat,NetTotal  from saledetails where  invoiceNo='" + invoiceId + "' AND prcode='" + prcode + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        if (rs.next()) {
                       float qty=rs.getFloat("qty");
                       double totalamt=rs.getDouble("totalamount");
                       double Totalvat=rs.getDouble("Totalvat");
                       double NetTotal=rs.getDouble("NetTotal");
                       //return balance
                       float remainqty=qty-returnqty;
                       double remaintotalamt=totalamt-returntotalamount;
                       double remaintotalvat=Totalvat-returntotalvat;
                       double remaintotalinvoice=NetTotal-retrunneTotal;
                       
                       if(remainqty==0){
                       deletecreditsaleDetailsbyProduct(prcode);
                       }else{
                       
                        updateDataTablecreditsaleDetails(prcode,remainqty,remaintotalamt,remaintotalvat,remaintotalinvoice);
                       }
                      
                       
                        } 
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }



}

private void updateDataTablecreditsaleDetails(String procode, float remainqty,double remaintotalamt,double remaintotalvat, double remaintotalinvoice){

 try {
     String sql="UPDATE saledetails  SET qty ='"+remainqty+"',totalamount='"+remaintotalamt+"',Totalvat='"+remaintotalvat+"',NetTotal='"+remaintotalinvoice+"' where  invoiceNo='" + invoiceId + "' AND prcode='" + procode + "'"; 
      pst = conn.prepareStatement(sql);
       pst.execute();
     
     
 
 }catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }

}
private void deletecashsaleDetailsbyProduct(String procode){

 try {
                        String sql = "Delete from cashsaledetails where invoiceNo='"+invoiceId+"' AND prcode='"+procode+"'";
                        pst = conn.prepareStatement(sql);

                        pst.execute();
                        // JOptionPane.showMessageDialog(null, "Data Deleted");

                    } catch (SQLException | HeadlessException e) {
                        JOptionPane.showMessageDialog(null, e);

                    }


}
private void deletecreditsaleDetailsbyProduct(String procode){

 try {
                        String sql = "Delete from saledetails where invoiceNo='"+invoiceId+"' AND prcode='"+procode+"'";
                        pst = conn.prepareStatement(sql);

                        pst.execute();
                        // JOptionPane.showMessageDialog(null, "Data Deleted");

                    } catch (SQLException | HeadlessException e) {
                        JOptionPane.showMessageDialog(null, e);

                    }


}
    /*
    
        private void saledeitailsupdate() throws SQLException {
        int i = dataTable.getRowCount();
        

            for (int j = 0; j <i; j++) {

                String remainprocode = (String) dataTable.getValueAt(row, 1);

                float returnqty = (float) dataTable.getValueAt(i, 4);

                double returntotalamount = (Double) dataTable.getValueAt(row, 7);

                double retrunvat = (Double) dataTable.getValueAt(row, 8);
                double returntotalvat = (Double) dataTable.getValueAt(row, 9);
                double retrunneTotal = (Double) dataTable.getValueAt(row, 10);
                

                    switch (paymenttypeboxdetails.getSelectedIndex()) {
                        case 1: {
                           // String sql = "Update cashsaledetails set qty='" + remainqty + "',totalamount='" + remaintotal + "',Totalvat='" + remaintotlavat + "',NetTotal='" + remainnettotl + "' where invoiceNo='" + invoiceId + "' AND prcode='" + dataprocode + "'";
                           String sql="UPDATE cashsaledetails  SET qty = (SELECT sale.customerid FROM sale WHERE sale.invoiceNo = saledetails.invoiceNo ) WHERE  EXISTS ( SELECT * FROM sale WHERE sale.invoiceNo = saledetails.invoiceNo )"; 
                           pst = conn.prepareStatement(sql);
                            pst.execute();

                            break;
                        }
                        case 2: {
                            String sql = "Update saledetails set qty='" + remainqty + "',totalamount='" + remaintotal + "',Totalvat='" + remaintotlavat + "',NetTotal='" + remainnettotl + "' where invoiceNo='" + invoiceId + "'AND prcode='" + dataprocode + "' ";
                            pst = conn.prepareStatement(sql);
                            pst.execute();

                            break;
                        }
                        //nothing
                        default:
                            break;
                    }

           

            }

      

    }
    
   */ 
    private void vatdelete() {

        try {
            String sql = "Delete from vatcollection where invoiceNopurchaseNo=?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, invoiceId);

            pst.execute();
            // JOptionPane.showMessageDialog(null, "Data Deleted");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void stamentdelete() {

        try {
            String sql = "Delete from CraditStatement where invoiceno=?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, invoiceId);

            pst.execute();
            // JOptionPane.showMessageDialog(null, "Data Deleted");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void updateretnqty() throws SQLException {
        int i = dataTable.getRowCount();

        for (int j = 0; j < i; j++) {

            String remainprocode = (String) dataTable.getValueAt(j, 1);
            //     JOptionPane.showMessageDialog(null, remainprocode);
            float returnqty = (Float) dataTable.getValueAt(j, 4);

            try {

                String sql = "Select Qty from Stock where procode=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, remainprocode);
                rs = pst.executeQuery();

                while (rs.next()) {

                    float stock = rs.getFloat("Qty");

                    float update_qty = stock + returnqty;

                    String sql1 = "Update stock set Qty='" + update_qty + "' where procode='" + remainprocode + "'";

                    pst = conn.prepareStatement(sql1);

                    pst.execute();

                }  //JOptionPane.showMessageDialog(null, add1);

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }

    private void SalereturnDetailsInsert() throws SQLException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String procode = (String) dataTable.getValueAt(row, 1);

                double rate = (Double) dataTable.getValueAt(row, 3);

                float qty = (Float) dataTable.getValueAt(row, 4);

                String unit = (String) dataTable.getValueAt(row, 5);
                double discount = (Double) dataTable.getValueAt(row, 6);
                double totalamount = (Double) dataTable.getValueAt(row, 7);

                double vat = (Double) dataTable.getValueAt(row, 8);
                double totalvat = (Double) dataTable.getValueAt(row, 9);
                double neTotal = (Double) dataTable.getValueAt(row, 10);
                try {

                    String sql = "Insert into salereturnDetails(returnNo,prcode,unitrate,qty,unit,totalamount,vat,Totalvat,NetTotal,returnedate) values (?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, returninvoicenotext.getText());
                    pst.setString(2, procode);
                    pst.setDouble(3, rate);
                    pst.setFloat(4, qty);
                    pst.setString(5, unit);

                    pst.setDouble(6, totalamount);
                    pst.setDouble(7, vat);
                    pst.setDouble(8, totalvat);
                    pst.setDouble(9, neTotal);

                     pst.setDate(10, date);
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

    private void cashInDrwaer(double cashout) {

        Double balanc = bankbalance1 - cashout;
        try {

            String sql = "Insert into CashDrower(cashin,cashout,balance,type,upatedate,tranid) values(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setDouble(1, 0);
            pst.setDouble(2, cashout);
            pst.setDouble(3, balanc);
            pst.setString(4, "Sale Return");
            pst.setDate(5, date);
            pst.setString(6, returninvoicenotext.getText());
            pst.execute();

            //  JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void printInvoice() throws JRException {

        int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Return Document", "Sale Return Print", JOptionPane.YES_NO_OPTION);
        if (p == 0) {

            Invoiceno = returninvoicenotext.getText();
             String returncash=df2.format(returncashamount);
             String previousdue=df2.format(balancedue);
            // int i=1; 
            try {

                // String currency = (String) currencybox.getSelectedItem();
                //int total = Integer.parseInt(totalinvoiceamount.getText());
                // String inwords = convert(total) + currency + " Only";

                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/InvoiceReturn.jrxml");
                //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

                HashMap para = new HashMap();
                para.put("returnno", Invoiceno);

                //  para.put("inwords", inwords);
                para.put("customerid", cusId);
                para.put("returncash",returncash);
                para.put("previousdue",previousdue);
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
            double qty = (Double) dataTable.getValueAt(i, 4);

            Stockpdateumuse(s, qty);

        }

    }

    private void Stockpdateumuse(String s, double qtyint) {

        try {

            String sql = "Select * from Stock where procode=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, s);
            rs = pst.executeQuery();

            while (rs.next()) {

                double stock = rs.getDouble("qty");

                double update_qty = stock - qtyint;

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
            double discount;
            double vat = (double) 0.05;
            double totalvat;

            double tpd = Double.parseDouble(unitrateText.getText());
            double productvat = tpd * vat;

            if (discounttext.getText().isEmpty()) {

                discount = 0;

            } else {
                discount = Double.parseDouble(discounttext.getText());

            }
            double tpwdisc = tpd - discount;

            float qty = Float.parseFloat(qtytext.getText());
            double nettotaltp = (tpwdisc * qty);
            totalvat = (productvat * qty);
            double totalall = nettotaltp + totalvat;

            model.setValueAt(qty, i, 4);
            model.setValueAt(nettotaltp, i, 7);
            model.setValueAt(totalvat, i, 9);
            model.setValueAt(totalall, i, 9);

        }

        updatekey=0;
        double inputotalrate =total_action_mrp();
        totalrate.setText(df2.format(inputotalrate));
       
        double inputvat =total_action_vat();
        totalvattext.setText(df2.format(inputvat));
       
        double input = total_invoice_amount();
        totalinvoiceamount.setText(df2.format(input));
      
        netdiscount.setText(Float.toString(total_discount_amount()));

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

        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        paymenttypeboxdetails = new javax.swing.JComboBox<>();
        InvoiceText = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        totalamounttext = new javax.swing.JTextField();
        netTotalText = new javax.swing.JTextField();
        totalVatText = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        customertext = new javax.swing.JTextField();
        Datetext = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        returninvoicenotext = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        unibox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        qtytext = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        discounttext = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        unitrateText = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        totalinvoiceamount = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        netdiscount = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        totalrate = new javax.swing.JTextField();
        totalvattext = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Sale Return Execution");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        jPanel5.setBackground(new java.awt.Color(67, 86, 86));

        jPanel8.setBackground(new java.awt.Color(67, 86, 86));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        paymenttypeboxdetails.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Cash Invoice", "Credit Invoice" }));
        paymenttypeboxdetails.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                paymenttypeboxdetailsPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        InvoiceText.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        InvoiceText.setBorder(null);
        InvoiceText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                InvoiceTextKeyReleased(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Invoice Type");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Invoice No:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(paymenttypeboxdetails, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(InvoiceText, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(109, 109, 109)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(paymenttypeboxdetails, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(InvoiceText))
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(67, 86, 86));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        totalamounttext.setEditable(false);
        totalamounttext.setBackground(new java.awt.Color(255, 255, 255));
        totalamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalamounttext.setForeground(new java.awt.Color(153, 0, 0));
        totalamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalamounttext.setBorder(null);
        totalamounttext.setDisabledTextColor(new java.awt.Color(204, 0, 0));

        netTotalText.setEditable(false);
        netTotalText.setBackground(new java.awt.Color(255, 255, 255));
        netTotalText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        netTotalText.setForeground(new java.awt.Color(153, 0, 0));
        netTotalText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netTotalText.setBorder(null);
        netTotalText.setDisabledTextColor(new java.awt.Color(204, 0, 0));

        totalVatText.setEditable(false);
        totalVatText.setBackground(new java.awt.Color(255, 255, 255));
        totalVatText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalVatText.setForeground(new java.awt.Color(153, 0, 0));
        totalVatText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalVatText.setBorder(null);
        totalVatText.setDisabledTextColor(new java.awt.Color(204, 0, 0));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Total Vat:");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Total Invoice:");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Invoice Amount:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(0, 0, 0)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(totalVatText, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(netTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22))
                .addGap(5, 5, 5)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(netTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(totalamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalVatText, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(67, 86, 86));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Customer Name");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Invoice Date:");

        customertext.setEditable(false);
        customertext.setBackground(new java.awt.Color(255, 255, 255));
        customertext.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        customertext.setBorder(null);
        customertext.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        Datetext.setEditable(false);
        Datetext.setBackground(new java.awt.Color(255, 255, 255));
        Datetext.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        Datetext.setBorder(null);
        Datetext.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(customertext, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Datetext, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel19))
                .addGap(5, 5, 5)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(customertext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(Datetext))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        datatbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        datatbl1.setRowHeight(30);
        datatbl1.setShowVerticalLines(false);
        datatbl1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatbl1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(datatbl1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(67, 86, 86));

        jPanel7.setBackground(new java.awt.Color(67, 86, 86));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Return No");

        returninvoicenotext.setEditable(false);
        returninvoicenotext.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        returninvoicenotext.setSelectedTextColor(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(78, 78, 78))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(returninvoicenotext, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(returninvoicenotext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel11.setBackground(new java.awt.Color(67, 86, 86));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        unibox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        unibox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        unibox.setEnabled(false);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Qty");

        codetext.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        codetext.setBorder(null);
        codetext.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        codetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codetextKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Item Name");

        qtytext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        qtytext.setForeground(new java.awt.Color(0, 102, 0));
        qtytext.setBorder(null);
        qtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytextKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Unit Rate");

        jButton1.setBackground(new java.awt.Color(153, 0, 0));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("ADD");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Code");

        jButton2.setBackground(new java.awt.Color(153, 0, 0));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("CLEAR");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        discounttext.setBackground(new java.awt.Color(255, 255, 204));
        discounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        discounttext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Discount");

        unitrateText.setEditable(false);
        unitrateText.setBackground(new java.awt.Color(255, 255, 255));
        unitrateText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        unitrateText.setForeground(new java.awt.Color(153, 0, 0));
        unitrateText.setBorder(null);
        unitrateText.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Unit");

        itemnamesearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        itemnamesearch.setBorder(null);
        itemnamesearch.setEnabled(false);
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(242, 242, 242)
                                .addComponent(jLabel6)))
                        .addGap(0, 0, 0)
                        .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(458, 458, 458)
                        .addComponent(jLabel5)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        jPanel4.setBackground(new java.awt.Color(67, 86, 86));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 51)));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Total Amount");

        totalinvoiceamount.setEditable(false);
        totalinvoiceamount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        totalinvoiceamount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalinvoiceamount.setBorder(null);
        totalinvoiceamount.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalinvoiceamount.setEnabled(false);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("VAT(5%)");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Invoice Amount");

        netdiscount.setEditable(false);
        netdiscount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        netdiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netdiscount.setBorder(null);
        netdiscount.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        netdiscount.setEnabled(false);
        netdiscount.setSelectedTextColor(new java.awt.Color(51, 51, 51));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Total Discount");

        totalrate.setEditable(false);
        totalrate.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        totalrate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalrate.setBorder(null);
        totalrate.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalrate.setEnabled(false);

        totalvattext.setEditable(false);
        totalvattext.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        totalvattext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalvattext.setBorder(null);
        totalvattext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalvattext.setEnabled(false);

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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(netdiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(totalinvoiceamount, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel14)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(totalinvoiceamount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(totalvattext, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(netdiscount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(totalrate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(5, 5, 5))
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
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

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
            dataTable.getColumnModel().getColumn(1).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setResizable(false);
            dataTable.getColumnModel().getColumn(3).setResizable(false);
            dataTable.getColumnModel().getColumn(4).setResizable(false);
            dataTable.getColumnModel().getColumn(5).setResizable(false);
            dataTable.getColumnModel().getColumn(6).setResizable(false);
            dataTable.getColumnModel().getColumn(7).setResizable(false);
            dataTable.getColumnModel().getColumn(8).setResizable(false);
            dataTable.getColumnModel().getColumn(9).setResizable(false);
            dataTable.getColumnModel().getColumn(10).setResizable(false);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
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

      /*  if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else 
        {
            if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
        } else if (updatekey==0)) {
            checkentry();
        } else {
            checkUpdate();

        }

*/
    }//GEN-LAST:event_qtytextKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     
        
        if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
        } else{ 
        
        if (updatekey==0) {
            checkentry();
        } else {
            checkUpdate();

        }}



    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clear();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
  parchase_code();
        try {
            balance();
        } catch (SQLException ex) {
            Logger.getLogger(SaleReturnEntry.class.getName()).log(Level.SEVERE, null, ex);
        }
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int emptytbl = model.getRowCount();

        if (returninvoicenotext.getText().isEmpty() || emptytbl == 0) {
            JOptionPane.showMessageDialog(null, "Please Click Check No Or Check Table Data which is may Empty");
        } else {

            salereturnInsert();

            try {
                SalereturnDetailsInsert();
                updateretnqty();
                saleinsertupdate();
                printInvoice();
                // updateretnqty();
            } catch (SQLException | JRException ex) {
                Logger.getLogger(SaleReturnEntry.class.getName()).log(Level.SEVERE, null, ex);
            }
clearreturn();
        }

       // clearreturn();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void InvoiceTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_InvoiceTextKeyReleased
        if (paymenttypeboxdetails.getSelectedIndex() > 0) {
            try {
                // Serach();
                Serach1();
               

                    Creditcustomercheck();

                

            } catch (SQLException ex) {
                Logger.getLogger(SaleReturnEntry.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_InvoiceTextKeyReleased

    private void paymenttypeboxdetailsPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_paymenttypeboxdetailsPopupMenuWillBecomeInvisible
        if (paymenttypeboxdetails.getSelectedIndex() == 0) {

            InvoiceText.setEnabled(false);

        } else {

            InvoiceText.setEnabled(true);

        }
        clearbox();
         InvoiceText.setText(null);
       
        customertext.setText(null);
        Datetext.setText(null);

        totalamounttext.setText(null);
        totalVatText.setText(null);
        totalVatText.setText(null);
        netTotalText.setText(null);
        returninvoicenotext.setText(null);

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);

        DefaultTableModel model3 = (DefaultTableModel) datatbl1.getModel();
        model3.setRowCount(0);
        totalrate.setText(null);
        netdiscount.setText(null);
        totalvattext.setText(null);
        totalinvoiceamount.setText(null);
    }//GEN-LAST:event_paymenttypeboxdetailsPopupMenuWillBecomeInvisible

    private void datatbl1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatbl1MouseClicked
        if (paymenttypeboxdetails.getSelectedIndex() == 1) {
            try {

                int row = datatbl1.getSelectedRow();
                String table_click = (datatbl1.getModel().getValueAt(row, 0).toString());
                String sql = "Select prcode,(select itemName from item it where it.Itemcode=sl.prcode) as 'itemname',unitrate,qty,unit from cashsaledetails sl where prcode='" + table_click + "' AND invoiceNo='" + InvoiceText.getText() + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {

                    proId = rs.getString("prcode");
                    codetext.setText(proId);
                    String item=rs.getString("itemname");
                    itemnamesearch.setSelectedItem(item);
                    String unitrate = rs.getString("unitrate");
                    unitrateText.setText(unitrate);
                    String qty = rs.getString("qty");
                    qtytext.setText(qty);
                    String unit = rs.getString("unit");
                    unibox.setSelectedItem(unit);

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

        } else {
            try {

                int row = datatbl1.getSelectedRow();
                String table_click = (datatbl1.getModel().getValueAt(row, 0).toString());
                String sql = "Select prcode,(select itemName from item it where it.Itemcode=sl.prcode) as 'itemname',unitrate,qty,unit from saledetails sl where prcode='" + table_click + "' AND invoiceNo='" + InvoiceText.getText() + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {

                    proId = rs.getString("prcode");
                    codetext.setText(proId);
                    String item=rs.getString("itemname");
                    itemnamesearch.setSelectedItem(item);
                    String unitrate = rs.getString("unitrate");
                    unitrateText.setText(unitrate);
                    String qty = rs.getString("qty");
                    qtytext.setText(qty);
                    String unit = rs.getString("unit");
                    unibox.setSelectedItem(unit);

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_datatbl1MouseClicked

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        int selectedRowIndex = dataTable.getSelectedRow();

        codetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        unitrateText.setText(model.getValueAt(selectedRowIndex, 3).toString());
        qtytext.setText(model.getValueAt(selectedRowIndex, 4).toString());
        discounttext.setText(model.getValueAt(selectedRowIndex, 6).toString());
        unibox.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());
        updatekey=1;
        //updateSystemProcess();
    }//GEN-LAST:event_dataTableMouseClicked

    private void dataTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMousePressed

    }//GEN-LAST:event_dataTableMousePressed

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
        deleterow();
    }//GEN-LAST:event_dataTableKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Datetext;
    private javax.swing.JTextField InvoiceText;
    private javax.swing.JTextField codetext;
    private javax.swing.JTextField customertext;
    private javax.swing.JTable dataTable;
    private javax.swing.JTable datatbl1;
    private javax.swing.JTextField discounttext;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField netTotalText;
    private javax.swing.JTextField netdiscount;
    private javax.swing.JComboBox<String> paymenttypeboxdetails;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextField returninvoicenotext;
    private javax.swing.JTextField totalVatText;
    private javax.swing.JTextField totalamounttext;
    private javax.swing.JTextField totalinvoiceamount;
    private javax.swing.JTextField totalrate;
    private javax.swing.JTextField totalvattext;
    private javax.swing.JComboBox<String> unibox;
    private javax.swing.JTextField unitrateText;
    // End of variables declaration//GEN-END:variables
}
