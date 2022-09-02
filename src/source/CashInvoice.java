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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
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
public final class CashInvoice extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    float tpprice;
    float bankbalance1;
    int tree = 0;
    String Invoiceno;

    String cusId;
    String Paymenttype = "Cash";
//    int new_inv = 2018001;
    int new_inv;

    /**
     * Creates new form CashInvoice
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public CashInvoice() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        currentDate();
        Item();
        customer();
        AutoCompleteDecorator.decorate(unibox);
        AutoCompleteDecorator.decorate(itemnamesearch);

        unit();

        currency();
        cashnumber();
        updarerext.hide();
        parchase_code();
      
       
    }
    
     /*private void MyPanel() {

        InputMap im = getInputMap(WHEN_FOCUSED);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "onEnter");

        am.put("onEnter", new AbstractAction() {
           
            @Override
            public void actionPerformed(ActionEvent e) {
                    parchase_code();

        try {
            balance();
        } catch (SQLException ex) {
            Logger.getLogger(CashInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (customerbox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Select Customer Information");
        } else {
            try {
                DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
                int emptytbl = model.getRowCount();
                if (invoicetext.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(null, "Please Check Invoice No");
                } else if (emptytbl > 0) {
                    //   saleInsert();
                    invoicecheck();
                    SaleDetailsInsert();
                    vatcollection();
                    //statementinsert();
                    Stockpdate();

                    int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Invoice", "Invoice Print", JOptionPane.YES_NO_OPTION);
                    if (p == 0) {
                        printInvoice();
                    }
                    AfterExecute();

                }
            } catch (JRException | SQLException ex) {
                Logger.getLogger(CashInvoice.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

            }

           
        });

    }
*/
 
    private void cashnumber() throws SQLException {

        try {
            String sql = "Select number	 from numberformat where id=1";
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

    private void editinvoiceno() {
        if (editinvoicetext.isSelected() == true) {
            invoicetext.setEnabled(true);
            invoicetext.setText(null);

        } else {
            invoicetext.setEnabled(false);
            parchase_code();
        }

    }

    /* final private static String[] units = {"Zero", "One", "Two", "Three", "Four",
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
            while (rs.next()) {

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

            String NewParchaseCode = ("CI-" + new_inv + "");
            String sql = "Select id from cashsale";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = ("CI-" + ParchaseCode + "");
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

        try {
            String sql = "Select customername from customerInfo";
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
        netdiscount.setText(null);
        invoicetext.setText(null);
        parchase_code();
       // codetext.requestFocusInWindow();

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
        float totalvat, productvat;

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        tree++;

        float tpd = Float.parseFloat(unitrateText.getText());

        productvat = (tpd * vat);
        String gpper = String.format("%.2f", productvat);
        float vatp = Float.parseFloat(gpper);
        if (discounttext.getText().isEmpty()) {

            discount = 0;

        } else {
            discount = Float.parseFloat(discounttext.getText());

        }
        float tpwdisc = tpd - discount;

        float qty = Float.parseFloat(qtytext.getText());
        float nettotaltp = (tpwdisc * qty);
        totalvat = (vatp * qty);
        float totalall = nettotaltp + totalvat;

        //String gpper = String.format("%.2f", gp);
        model2.addRow(new Object[]{tree, codetext.getText(), itemnamesearch.getSelectedItem(), tpd, qty, unibox.getSelectedItem(), discount, nettotaltp, vatp, totalvat, totalall});
        ///  totalrate.setText(Integer.toString(total_action_rate()));
        
        
        double inputotalrate =total_action_mrp();
        totalrate.setText(df2.format(inputotalrate));
       
        double inputvat =total_action_vat();
        totalvattext.setText(df2.format(inputvat));
       
        double input = total_invoice_amount();

        totalinvoiceamount.setText(df2.format(input));
        
        netdiscount.setText(Float.toString(total_discount_amount()));

        //  float totalinvoce=total_invoice_amount();
        ///   float prebamt=Float.parseFloat(previseamonmttext.getText());
        //float nettotalin=totalinvoce+prebamt;
        //String nets = String.format("%.2f", nettotalin);
        // netstext.setText(nets);
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

    private double total_invoice_amount() {

        int rowaCount = dataTable.getRowCount();

        double totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Double.parseDouble(dataTable.getValueAt(i, 10).toString());
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

    private void invoicecheck() {

        try {
            String sql = "Select id from cashsale";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next() == false) {
                saleInsertcheck();

            } else {
                saleInsert();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void saleInsertcheck() {
        float chasin = Float.parseFloat(totalinvoiceamount.getText());

        try {
            float totaltd = totaltrade();
            float totalamt = Float.parseFloat(totalrate.getText());
            float totalprofite = totalamt - totaltd;
            float discount = Float.parseFloat(netdiscount.getText());
            float profite = totalprofite - discount;
            String sql = "Insert into cashsale(id,invoiceNo,invoicedate,paymentCurency,paymentType,netdiscount,TotalAmount,TotalVat,Totalinvoice,customerid,Ref_No,totaltraprice,totalprofite) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
            cashInDrwaer(chasin);
            JOptionPane.showMessageDialog(null, "Invoice Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void saleInsert() {
        float chasin = Float.parseFloat(totalinvoiceamount.getText());
        try {
            
            float totaltd = totaltrade();
            float totalamt=Float.parseFloat(totalrate.getText());
            float totalprofite=totalamt-totaltd;
            float discount=Float.parseFloat(netdiscount.getText());
            float profite=totalprofite-discount;
            
            String sql = "Insert into cashsale(invoiceNo,invoicedate,paymentCurency,paymentType,netdiscount,TotalAmount,TotalVat,Totalinvoice,customerid,Ref_No,totaltraprice,totalprofite) values (?,?,?,?,?,?,?,?,?,?,?,?)";
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
            cashInDrwaer(chasin);
            JOptionPane.showMessageDialog(null, "Data Saved");

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

                    String sql = "Insert into cashsaleDetails(invoiceNo,prcode,unitrate,qty,unit,discount,commesion,totalamount,vat,Totalvat,NetTotal,tradprice,invoicedate) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
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

                float rate = (Float) dataTable.getValueAt(row, 3);

                float qty = (Float) dataTable.getValueAt(row, 4);
                float totaltrade=qty*tpprice;
                totaltradep=totaltradep+totaltrade;
                
            }
      return (float) totaltradep;
    
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

            //  JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void printInvoice() throws JRException {
        Invoiceno = invoicetext.getText();

        // int i=1; 
        try {

            String currency = (String) currencybox.getSelectedItem();

            Double d = Double.parseDouble(totalinvoiceamount.getText());

            String num = totalinvoiceamount.getText();

            String arebic = convertNumberToArabicWords(num);
            String inwords = convertToIndianCurrency(num);
            String convert = inwords + " ( " + arebic + " ) ";
            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/Invoice.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();
            para.put("invoiceno", Invoiceno);

            para.put("inwords", convert);

            para.put("customerid", cusId);

            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

            JasperPrintManager.printReport(jp, true);
            //JasperViewer.viewReport(jp, true);

        } catch (NumberFormatException | JRException ex) {
            JOptionPane.showMessageDialog(null, ex);

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
            String gpper = String.format("%.2f", productvat);
            float vatp = Float.parseFloat(gpper);
            if (discounttext.getText().isEmpty()) {

                discount = 0;

            } else {
                discount = Float.parseFloat(discounttext.getText());

            }
            float tpwdisc = tpd - discount;

            float qty = Float.parseFloat(qtytext.getText());
            float nettotaltp = (tpwdisc * qty);
            totalvat = (vatp * qty);
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        discounttext = new javax.swing.JTextField();
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
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        invoicetext = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        inputdate = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        currencybox = new javax.swing.JComboBox<>();
        jButton6 = new javax.swing.JButton();
        editinvoicetext = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        updarerext = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        customerbox = new javax.swing.JComboBox<>();
        refnotext = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cash Invoice");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Code");

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

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 0));
        jLabel4.setText("Item Name");

        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 102, 0));
        jLabel5.setText("Unit Rate");

        unitrateText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        unitrateText.setForeground(new java.awt.Color(153, 0, 0));
        unitrateText.setBorder(null);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 0, 0));
        jLabel6.setText("Qty");

        qtytext.setBackground(new java.awt.Color(255, 255, 204));
        qtytext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        qtytext.setForeground(new java.awt.Color(0, 102, 0));
        qtytext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        qtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytextKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Unit");

        unibox.setEditable(true);
        unibox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        unibox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

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

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 102, 0));
        jLabel15.setText("Discount");

        discounttext.setBackground(new java.awt.Color(255, 255, 204));
        discounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        discounttext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(7, 7, 7)
                .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addGap(4, 4, 4)
                .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(codetext)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(itemnamesearch, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(unitrateText)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(qtytext)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(unibox)
                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(discounttext, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );

        dataTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        dataTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL.No", "Item Code", "Item Name", "Unit Rate", "Qty", "Unit", "Discount", "Total Amount", "Vat(5%)", "Total Vat", "Net Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true, true, true
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
        });
        dataTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dataTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(dataTable);

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
        netdiscount.setDisabledTextColor(new java.awt.Color(255, 255, 255));
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
                        .addComponent(totalrate))
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

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        invoicetext.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        invoicetext.setEnabled(false);
        invoicetext.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        jPanel1.add(invoicetext, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 47, 174, 28));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 0));
        jLabel1.setText("Invoice No");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 45, 80, 28));

        inputdate.setDateFormatString("yyyy-MM-dd\n");
        inputdate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(inputdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 11, 273, 28));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("Curency ");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 168, 80, 24));

        currencybox.setEditable(true);
        currencybox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jPanel1.add(currencybox, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 165, 273, 30));

        jButton6.setBackground(new java.awt.Color(204, 0, 0));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Check");
        jButton6.setBorder(null);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 84, 273, 32));

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
        jPanel1.add(editinvoicetext, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 45, -1, 28));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("Date");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 80, 28));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Ref No:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 202, 80, 29));

        updarerext.setText("0");
        jPanel1.add(updarerext, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 85, 35, 32));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setText("Customer ");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 123, 80, 30));

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
        jPanel1.add(customerbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 124, 273, 30));

        refnotext.setBorder(null);
        jPanel1.add(refnotext, new org.netbeans.lib.awtextra.AbsoluteConstraints(94, 201, 273, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void codetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyReleased
//        itemnamesearch.removeAllItems();

        String SearchText = (String) codetext.getText();
        try {

            String sql = "Select id,itemName,stockunit, mrp from item where id='" + SearchText + "' OR barcode='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            if (rs.next()) {

                String name = rs.getString("itemName");
                itemnamesearch.setSelectedItem(name);
                String unitrate = rs.getString("mrp");
                unitrateText.setText(unitrate);
                 String unit = rs.getString("stockunit");
                unibox.setSelectedItem(unit);

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

     
     qtytext.requestFocusInWindow();
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
    qtytext.requestFocusInWindow();
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void qtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyPressed

        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else{
            
          if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
        } else if ("0".equals(updarerext.getText())) {
            
             try {
            checkentry();
            } catch (NumberFormatException e) {
        JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Quantity Validation", JOptionPane.CANCEL_OPTION);
    }

        } else {
              
             try {
            checkUpdate();
      } catch (NumberFormatException e) {
        JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Quantity Validation", JOptionPane.CANCEL_OPTION);
    }
        }
codetext.requestFocusInWindow();
          
        }
        
      // codetext.requestFocusInWindow(); 
    }//GEN-LAST:event_qtytextKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
        } else if ("0".equals(updarerext.getText())) {
            try {
            checkentry();
} catch (NumberFormatException e) {
        JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Quantity Validation", JOptionPane.CANCEL_OPTION);
    }
        } else {
            try {
            checkUpdate();
            }catch (NumberFormatException e) {
        JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Quantity Validation", JOptionPane.CANCEL_OPTION);
    }
        }
codetext.requestFocusInWindow();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clear();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void customerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_customerboxPopupMenuWillBecomeInvisible
        String Customername = (String) customerbox.getSelectedItem();
        try {
            String sql = "Select * from customerInfo  where customername='" + Customername + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {

                cusId = rs.getString("id");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }//GEN-LAST:event_customerboxPopupMenuWillBecomeInvisible

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

        try {
            balance();
        } catch (SQLException ex) {
            Logger.getLogger(CashInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (customerbox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Select Customer Information");
        } else {
            try {
                DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
                int emptytbl = model.getRowCount();
                if (invoicetext.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(null, "Please Check Invoice No");
                } else if (emptytbl > 0) {
                    //   saleInsert();
                    invoicecheck();
                    SaleDetailsInsert();
                    vatcollection();
                    //statementinsert();
                    Stockpdate();

                    int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Invoice", "Invoice Print", JOptionPane.YES_NO_OPTION);
                    if (p == 0) {
                        printInvoice();
                    }
                    AfterExecute();

                }
            } catch (JRException | SQLException ex) {
                Logger.getLogger(CashInvoice.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


    }//GEN-LAST:event_jButton3ActionPerformed

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
        deleterow();
    }//GEN-LAST:event_dataTableKeyPressed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (editinvoicetext.isSelected()) {
            if (invoicetext.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Fillup Give Credit Invoice No");
            } else {
                String Applyinvoicenumber = invoicetext.getText();
                try {
                    String sql = "Select invoiceNo from cashsale where invoiceNo='" + Applyinvoicenumber + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    if (rs.next() == true) {
                        JOptionPane.showMessageDialog(null, "this Number Already Exits");
                        invoicetext.setText(null);
                    } else {
                        invoicetext.setText(Applyinvoicenumber);

                    }
                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }

        } else {
            parchase_code();

        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void editinvoicetextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editinvoicetextMouseClicked
        editinvoiceno();
    }//GEN-LAST:event_editinvoicetextMouseClicked

    private void editinvoicetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editinvoicetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editinvoicetextActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField codetext;
    private javax.swing.JComboBox<String> currencybox;
    private javax.swing.JComboBox<String> customerbox;
    private javax.swing.JTable dataTable;
    private javax.swing.JTextField discounttext;
    private javax.swing.JCheckBox editinvoicetext;
    private com.toedter.calendar.JDateChooser inputdate;
    private javax.swing.JTextField invoicetext;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField netdiscount;
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
