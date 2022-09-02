/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public final class InvoiceTest extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    float tpprice;
    String genericname = null;
    float bankbalance1;
    int tree = 0;

    int treebook = 0;
    int treereturn = 0;
    String Invoiceno;

    String cusId = "0";
    String Paymenttype = "Cash";
//    int new_inv = 2018001;
    int new_inv;
    java.sql.Date date;
    int updatekey = 0;
    int id = 1;

    //customer
    float salesamt;
    float dipositamt;
    float cashamt;
    float creditAmnt;
    float openingbalance;
    float totaldiscount;
    float paidamt;
    float balancedue;
    float balanced;

    int userid = 0;
    int offer = 0;
    String offerid;

    //bank payment
    float totalb;
    String bankforaccount = null;
    String acc = null;
    String cardbank = null;
    String cardacc = null;
    String bkashNo = null;

    float accbalance = 0;
    float bankcashout = 0;
    float bankcashin = 0;
    float balance = 0;

    String username = null;
    int orderupdate = 0;
    float includevat = 0;

    // item description
    String itemcode = null;
    String barcode = null;
    String itemname = null;
    String color = null;
    String size = null;
    String mrp = null;
    String stockunit = null;
    //book

    int bookupdate = 0;
    float bookReturn = 0;
    int bookReturnStatus = 0;

    //uinit
    String unit = null;
    int bankid;

    //RETURN
    float returncashamount = 0;

    String value = "";
    String valueone = "";
    int numberone;
    int numbertwo;
    int result = 0;
    String operand;
    int cashcredit = 0;
    int hold = 0;
    float discountfinel = 0;
    float daysBetween = 0;

    String paymentType = "Cash";
    int mobilebank = 0;
    int saleupdate = 0;
    String time;
    String Invoicenocheck = null;
    String cashinvoiceprintslip = null;
    String creditinvoiceprintslip = null;
    float qty = 0;
    float tpds = 0;
    String discountamt;
    float realcashamt = 0;
    String ReturnInvoice = "0";
    float returnQty = 0;
    String ReturnItemcode = "";
    String ReturnCusid = "";

    /**
     * Creates new form PointOfSale
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public InvoiceTest() throws SQLException, IOException {

        initComponents();
        conn = Java_Connect.conectrDB();
        CurrentDatetime();
        CurrentTime();
        userkey();
        currentDate();
        intilize();
        currency();
        cashnumber();
        parchase_code();
        offerCheck();
        username();
        order();
        customernameactive();
        customerid();
        recieptmsg();
        AutoCompleteDecorator.decorate(orderNobox);
        AutoCompleteDecorator.decorate(searchtypebox);
        AutoCompleteDecorator.decorate(previceinvoicebox);
        itemnamesearch.requestFocusInWindow();
        directstore.setSelected(false);
        printinvoicecheck.setSelected(true);
        TableDesign();
        discountboxtypefinel.setSelectedIndex(0);
        final JTextField textfield = (JTextField) itemnamesearch.getEditor().getEditorComponent();
        textfield.requestFocusInWindow();
        ExtractFilter();
        ExtractFilterInvoicecheck();
        MPO();
        Territory();
        ExtractFilterCustomer();
        return_code();
     

    }

    private void MPO() throws SQLException {
        mpobox.removeAllItems();
        mpobox.addItem("Select");
        mpobox.setSelectedItem("Select");

        try {
            String sql = "Select name from mpo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("name");
                mpobox.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void Territory() throws SQLException {
        territorybox.removeAllItems();
        territorybox.addItem("Select");
        territorybox.setSelectedItem("Select");

        try {
            String sql = "Select name from territory";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("name");
                territorybox.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void TotalAction() {
        float tp = 0;
        float mrp = 0;
        float discount = 0;
        float vat = 0;
        float tpvat = 0;
        float tpwithdiscount = 0;
        float qty = 0;
        float totaldiscount = 0;
        float totalvat = 0;
        float totalamount = 0;
        float nettotal = 0;
        if (unitrateText.getText().isEmpty()) {
            tp = 0;
        } else {
            tp = Float.parseFloat(unitrateText.getText());
        }

        if (discounttext.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Float.parseFloat(discounttext.getText());
        }
        if (pvattext.getText().isEmpty()) {
            vat = 0;
        } else {
            vat = Float.parseFloat(pvattext.getText());
        }

        if (qtytext.getText().isEmpty()) {
            qty = 0;
        } else {
            qty = Float.parseFloat(qtytext.getText());
        }

        tpwithdiscount = tp - discount;
        float vatper = vatPercentage(vat, tp);
        // pvatpertext.setText(String.valueOf(Math.round(vatper)));
        float vatafterdis = vatValue(vatper, tpwithdiscount);

        tpvat = tpwithdiscount + vatafterdis;
        ptotaltptext.setText(df2.format(tpvat));
        if (qty == 0) {
            totalamounttext1.setText(null);
            totaldiscounttext1.setText(null);
            altotalvattext.setText(null);
            netotaltext.setText(null);
        } else {
            totalamount = tp * qty;
            totalamounttext1.setText(df2.format(totalamount));
            totaldiscount = discount * qty;
            totaldiscounttext1.setText(df2.format(totaldiscount));
            totalvat = vat * qty;
            altotalvattext.setText(df2.format(totalvat));
            float amountwithdiscount = (totalamount - totaldiscount);
            nettotal = (amountwithdiscount + totalvat);
            netotaltext.setText(df2.format(nettotal));

        }

    }

    private void SetVatValueWithDiscount() {
        float tp = 0;
        float discount = 0;
        float vat = 0;
        float tpwithdiscount = 0;

        if (unitrateText.getText().isEmpty()) {
            tp = 0;
        } else {
            tp = Float.parseFloat(unitrateText.getText());
        }

        if (discounttext.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Float.parseFloat(discounttext.getText());
        }
        if (pvatpertext.getText().isEmpty()) {
            vat = 0;
        } else {
            vat = Float.parseFloat(pvatpertext.getText());
        }
        tpwithdiscount = tp - discount;
        float vatafterdis = vatValue(vat, tpwithdiscount);
        pvattext.setText(df2.format(vatafterdis));

    }

    private float vatPercentage(float vat, float tp) {
        float per = (vat * 100) / tp;
        return per;
    }

    private float vatValue(float per, float tp) {

        float vatper = ((tp / (100 + per)) * per);
        return vatper;

    }

    private void SetPvat() {
        float tprate = 0;
        float tp = 0;
        float discount = 0;
        float vat = 0;
        float tpwithdiscount = 0;

        if (ptotaltptext.getText().isEmpty()) {
            tp = 0;
        } else {
            tp = Float.parseFloat(ptotaltptext.getText());
        }

        if (discounttext.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Float.parseFloat(discounttext.getText());
        }
        if (pvatpertext.getText().isEmpty()) {
            vat = 0;
        } else {
            vat = Float.parseFloat(pvatpertext.getText());
        }
        tpwithdiscount = tp - discount;
        float vatafterdis = vatValue(vat, tpwithdiscount);
        pvattext.setText(df2.format(vatafterdis));
        tprate = tp - vatafterdis;
        unitrateText.setText(df2.format(tprate));
        // mrptext.setText(df2.format(tprate));
    }

    private void recieptmsg() {
        try {
            String sql = "Select cashinvoice,creditinvoice from recieptmsg  where id=1";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {
                cashinvoiceprintslip = rs.getString("cashinvoice");
                creditinvoiceprintslip = rs.getString("creditinvoice");

                //setLanguage();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void ExtractFilter() {
        final JTextField textfield = (JTextField) itemnamesearch.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        itemnamesearch.removeAllItems();

                        genericbox.removeAllItems();
                        strengthtext.setText(null);
                        batchtext.setText(null);
                        boxsizetext.setText(null);
                        unitrateText.setText(null);

                        discounttext.setText(null);
                        pvattext.setText(null);
                        pvatpertext.setText(null);
                        ptotaltptext.setText(null);
                        qtytext.setText(null);
                        bonusqtytext.setText(null);

                        unibox.setSelectedIndex(0);
                        totalamounttext1.setText(null);
                        totaldiscounttext1.setText(null);
                        altotalvattext.setText(null);
                        netotaltext.setText(null);
                        stocktext.setText(null);
                        expdate.setText(null);

                    } else {
                        comboFilter(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFilter(String enteredText) {
        itemnamesearch.removeAllItems();
        //  itemnamesearch.addItem("");
        itemnamesearch.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;

        try {

            String sql = "Select itemName from item WHERE lower(itemName)  LIKE '%" + enteredText + "%' OR Itemcode LIKE '%" + enteredText + "%'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("itemName");
                filterArray.add(str1);
                itemnamesearch.addItem(str1);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        if (filterArray.size() > 0) {
            //  itemnamesearch.setModel(new DefaultComboBoxModel(filterArray.toArray()));

            itemnamesearch.setSelectedItem(enteredText);
            itemnamesearch.showPopup();

        } else {

            itemnamesearch.hidePopup();

            genericbox.removeAllItems();
            strengthtext.setText(null);
            batchtext.setText(null);
            boxsizetext.setText(null);
            unitrateText.setText(null);

            discounttext.setText(null);
            pvattext.setText(null);
            pvatpertext.setText(null);
            ptotaltptext.setText(null);
            qtytext.setText(null);
            bonusqtytext.setText(null);

            unibox.setSelectedIndex(0);
            totalamounttext1.setText(null);
            totaldiscounttext1.setText(null);
            altotalvattext.setText(null);
            netotaltext.setText(null);
            stocktext.setText(null);
            expdate.setText(null);

        }
    }

    private void ExtractFilterCustomer() {
        final JTextField textfield = (JTextField) customerbox.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        customerbox.removeAllItems();
                        customeridtext.setText(null);
                        cusId = "0";
                    } else {
                        comboFilterCustomer(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFilterCustomer(String enteredText) {
        customerbox.removeAllItems();
        //  itemnamesearch.addItem("");
        customerbox.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;

        try {

            String sql = "Select DISTINCT  customername from customerinfo WHERE lower(customername)  LIKE '%" + enteredText + "%' ORDER BY customername ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("customername");
                filterArray.add(str1);
                customerbox.addItem(str1);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        if (filterArray.size() > 0) {
            //  itemnamesearch.setModel(new DefaultComboBoxModel(filterArray.toArray()));

            customerbox.setSelectedItem(enteredText);
            customerbox.showPopup();

        } else {

            customerbox.hidePopup();
            customeridtext.setText(null);
            cusId = "0";

        }
    }

    private void TableDesign() {
        JTableHeader jtblheader = dataTable.getTableHeader();
        jtblheader.setBackground(Color.red);
        jtblheader.setForeground(Color.DARK_GRAY);
        jtblheader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ((DefaultTableCellRenderer) jtblheader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        dataTable.setDefaultRenderer(Object.class, centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        dataTable.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        dataTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        dataTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        dataTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        dataTable.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
    }

    public void CurrentDatetime() {

        Thread clock = new Thread() {

            @Override
            public void run() {

                for (;;) {
                    Calendar cal = new GregorianCalendar();

                    int second = cal.get(Calendar.SECOND);
                    int houre = cal.get(Calendar.HOUR);
                    int minit = cal.get(Calendar.MINUTE);
                    int AM_PM = cal.get(Calendar.AM_PM);
                    String pmoram = null;
                    if (AM_PM == 0) {
                        pmoram = "AM";
                    } else {
                        pmoram = "PM";
                    }
                    // time_text.setText("Time:"+" "+houre + ":" + minit + ":" + second+" "+pmoram);

                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        };

        clock.start();
    }

    private void CurrentTime() {
        /*  Calendar cal = new GregorianCalendar();
         String AP;
         int second = cal.get(Calendar.SECOND);
         int houre = cal.get(Calendar.HOUR);
         int minit = cal.get(Calendar.MINUTE);
         int AM_PM = cal.get(Calendar.AM_PM);
         if(AM_PM==1){
         AP="PM";
         }else{
         AP="AM";
         }
      
         time = +houre + ":" + minit +  " " + AP;
        
         */
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleformat = new SimpleDateFormat("hh:mm a");
        time = simpleformat.format(cal.getTime());

        /*
         String pattern = "hh:mm a";
         DateFormat dateFormat = new SimpleDateFormat(pattern);
         LocalTime now = LocalTime.now();
         time = now.format(DateTimeFormatter.ofPattern(pattern));
         */
    }

    private void customernameactive() {
        String customer = (String) customerbox.getSelectedItem();
        jLabel35.setText(customer);
    }

    private void priviousInvoice() {
        previceinvoicebox.removeAllItems();
        previceinvoicebox.addItem("Select");
        previceinvoicebox.setSelectedItem("Select");

        try {
            String sql = "Select invoiceNo from sale WHERE recieved=0 ORDER BY invoiceNo DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("invoiceNo");
                previceinvoicebox.addItem(name);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void parchase_code() {
        priviousInvoice();
        entrucesure();
        try {
            String NewParchaseCode = ("CIN-1" + new_inv + "");
            String sql = "Select id from sale";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);

            }
            //String ParchaseCode = String.format("%08d",new_inv);
            NewParchaseCode = ("CIN-1" + new_inv + "");
            invoicetext.setText(NewParchaseCode);
            Invoicenocheck = ("CIN-1" + new_inv + "");
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void return_code() {
        priviousInvoice();
        entrucesure();
        try {
            String NewParchaseCode = ("SR-1" + new_inv + "");
            String sql = "Select id from salereturn";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);

            }
            //String ParchaseCode = String.format("%08d",new_inv);
            NewParchaseCode = ("SR-1" + new_inv + "");
            invoicetext.setText(NewParchaseCode);
            Invoicenocheck = ("SR-1" + new_inv + "");
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void panelactive() {

        itemnamesearch.setEnabled(true);
        unitrateText.setEnabled(true);
        qtytext.setEnabled(true);

        discounttext.setEnabled(true);
        addbtn.setEnabled(true);

    }

    private void paneldactive() {

        itemnamesearch.setEnabled(false);
        unitrateText.setEnabled(false);
        qtytext.setEnabled(false);

        discounttext.setEnabled(false);
        addbtn.setEnabled(false);

    }

    private void entrucesure() {

        if ("0".equals(cusId)) {

            paneldactive();
        } else {
            panelactive();
        }

    }

    private void userkey() throws FileNotFoundException, IOException, SQLException {
        FileInputStream fis = new FileInputStream("src\\userkey.properties");

        //  /abdulbasit?useUnicode=yes&characterEncoding=UTF-8
        //jdbc:mysql://localhost:3306
        Properties p = new Properties();
        p.load(fis);

        String userids = (String) p.get("userid");
        userid = Integer.parseInt(userids);
        // this.dispose();
        //LoginAccess desboard=new LoginAccess();
    }

    private void updateSystemProcess() {
        if (updatekey == 0) {

            itemnamesearch.setEditable(true);
            itemnamesearch.setEnabled(true);
        } else {

            itemnamesearch.setEditable(false);
            itemnamesearch.setEnabled(false);
        }

    }

    private void paymentinfo() throws FileNotFoundException, IOException, SQLException {
        FileInputStream fis = new FileInputStream("src\\paymentSetup.properties");

        //  /abdulbasit?useUnicode=yes&characterEncoding=UTF-8
        //jdbc:mysql://localhost:3306
        Properties p = new Properties();
        p.load(fis);

        bankforaccount = (String) p.get("bankforaccount");
        acc = (String) p.get("acc");
        cardbank = (String) p.get("cardbank");
        cardacc = (String) p.get("cardacc");
        bkashNo = (String) p.get("bkashNo");
        // this.dispose();
        //LoginAccess desboard=new LoginAccess();
    }

    private void AccBalance() {

        try {
            String sql = "Select cashout,cashin from BankAccount where accountno='" + acc + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                //accbalance = rs.getFloat("prasentbalance");
                bankcashout = rs.getFloat("cashout");
                bankcashin = rs.getFloat("cashin");
                accbalance = bankcashin - bankcashout;

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void CardBalance() {

        try {
            String sql = "Select cashout,cashin from BankAccount where accountno='" + cardacc + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                //accbalance = rs.getFloat("prasentbalance");
                bankcashout = rs.getFloat("cashout");
                bankcashin = rs.getFloat("cashin");
                accbalance = bankcashin - bankcashout;

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalbalance() {

        try {
            String sql = "Select sum(cashin) as 'bankcashin',sum(cashout) as 'bankcashout' from bankaccount";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                float cashin = rs.getFloat("bankcashin");
                float cashout = rs.getFloat("bankcashout");
                totalb = cashin - cashout;

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void bookreturnstatus() {
        switch (bookReturnStatus) {
            case 0:
                jLabel45.setText("Order/Return");
                break;
            case 1:
                jLabel45.setText("Order Advanced");
                break;
            default:
                jLabel45.setText("Return Amount");
                break;
        }
    }

    //end book module
    //Hold Module
    private void order() {
        orderNobox.removeAllItems();
        orderNobox.addItem("Select");
        orderNobox.setSelectedItem("Select");
        String status = "0";
        try {
            String sql = "Select id from saleorder where status='" + status + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("id");
                orderNobox.addItem(name);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void OderInsert() {
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to Place New Order", "Kitchen Order", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            try {

                String sql = "Insert into saleorder(orderdate,"
                        + "customerid,"
                        + "inputuserid,"
                        + "type,"
                        + "status,"
                        + "discount,"
                        + "discounttype) values (?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);

                pst.setDate(1, date);
                pst.setString(2, cusId);
                pst.setInt(3, userid);
                pst.setString(4, "Invoice");
                pst.setInt(5, 0);
                pst.setString(6, netdiscount.getText());
                pst.setString(7, (String) discountboxtypefinel.getSelectedItem());

                pst.execute();
                ResultSet rshere = pst.getGeneratedKeys();
                int generatedKey = 0;
                if (rshere.next()) {
                    generatedKey = rshere.getInt(1);
                    orderDetailsInsert(generatedKey);
                    //   PrintOrder(generatedKey);
                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }
    }

    private void orderDetailsInsert(int id) throws SQLException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                String probarcode = (String) dataTable.getValueAt(row, 1);
                try {
                    String sql = "Select bp.itemcode as 'itemcode',bp.tp as 'tp',bp.stockunit as 'unit',bp.vat as 'vat' from item bp  where bp.Itemcode='" + probarcode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {
                        itemcode = rs.getString("itemcode");
                        tpprice = rs.getFloat("tp");
                        unit = rs.getString("unit");
                        includevat = rs.getFloat("vat");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                float rate = (Float) dataTable.getValueAt(row, 3);
                float qty = Float.parseFloat(dataTable.getValueAt(row, 4).toString());
                float discount = (Float) dataTable.getValueAt(row, 5);
                float totalamount = rate * qty;
                float neTotal = (Float) dataTable.getValueAt(row, 6);
                float itemdiscount = discount / qty;
                float mrpdiscount = rate - itemdiscount;
                float itemvat = includevat / 100 * mrpdiscount;
                float totalvat = itemvat * qty;

                try {

                    String sql = "Insert into orderDetails(orderNo,prcode,barcode,unitrate,qty,unit,discount,totalamount,Totalvat,NetTotal) values (?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1, id);
                    pst.setString(2, itemcode);
                    pst.setString(3, probarcode);
                    pst.setFloat(4, rate);
                    pst.setFloat(5, qty);
                    pst.setString(6, unit);
                    pst.setFloat(7, discount);
                    pst.setFloat(8, totalamount);
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
            int status = 1;
            String orderid = (String) orderNobox.getSelectedItem();

            String sql = "Update saleorder set status='" + status + "'AND orderdate='" + date + "' where id='" + orderid + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {
            String sql = "Delete from saleorder where id=? ";
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

    }

    private void orderUpdate() throws SQLException {
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to Update Order", "Kitchen Order Update", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            try {
                String sql = "Update saleorder set discount='" + netdiscount.getText() + "',discounttype='" + discountboxtypefinel.getSelectedItem() + "' where id='" + orderNobox.getSelectedItem() + "'";
                pst = conn.prepareStatement(sql);
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
            String oderids = (String) orderNobox.getSelectedItem();
            int oderid = Integer.parseInt(oderids);
            orderDetailsInsert(oderid);
            //PrintOrder(oderid);
        }
    }

    private void intilize() {

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("company.png")));
    }

    private void offerCheck() {

        try {
            String sql = "Select offerid,status from offersetting where id=1";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                offerid = rs.getString("offerid");
                int status = rs.getInt("status");
                if (status == 1) {
                    offer();

                } else {
                    offer = 0;

                }

            }

        } catch (SQLException e) {

        }

    }

    private void offer() throws SQLException {

        try {
            String sql = "Select id,startdate,enddate from offerlist where id='" + offerid + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                Date startdate = rs.getDate("startdate");
                Date enddate = rs.getDate("enddate");

                long difference = enddate.getTime() - date.getTime();

                daysBetween = (difference / (1000 * 60 * 60 * 24));
                float daysBetweens = daysBetween + 1;
                //JOptionPane.showMessageDialog(null,daysBetweens);
                if (daysBetweens > 0) {
                    offer = 1;

                } else {
                    offer = 0;
                }

                //offerid = rs.getString("id");
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

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
            while (rs.next()) {

                float cashin = rs.getFloat("sum(cashin)");
                float cashout = rs.getFloat("sum(cashout)");
                bankbalance1 = (cashin - cashout);

            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        inputdate.setDate(date);

    }

    private void inputDate() {
        date = convertUtilToSql(inputdate.getDate());

    }

    private static java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());

        return sDate;
    }

    public String firstTwo(String str) {
        return str.length() < 2 ? str : str.substring(0, 2);
    }

    private void Item() throws SQLException {

        try {
            String sql = "Select itemName from item";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                // String barcode=rs.getString("barcode");
                String name = rs.getString("itemName");

                itemnamesearch.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void customer() throws SQLException {
        customerbox.removeAllItems();
        customerbox.addItem("Select");
        customerbox.setSelectedItem("Select");

        try {
            String sql = "Select customername from customerInfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("customername");
                customerbox.addItem(name);
            }
        } catch (SQLException e) {
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
                ///currencybox.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void customer_field_clear() {
        customeridtext.setText(null);
        customerbox.removeAllItems();
        openingbalancetext.setText(null);
        creditamounttext.setText(null);
        cashamounttext.setText(null);
        totalsaletext.setText(null);
        totaldiscounttext.setText(null);
        totalpaymenttext.setText(null);
        balanceduetext.setText(null);
        netstext.setText(null);
        itemnamesearch.requestFocusInWindow();

    }

    private void AfterExecute() {

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);
        cusId = "0";
        totalrate.setText(null);
        totalvattext.setText(null);
        totalinvoiceamount.setText(null);
        customerbox.removeAllItems();
        netdiscount.setText(null);
        invoicetext.setText(null);

        bookreturntext.setText(null);
        nettotaltext.setText(null);
        order();
        orderupdate = 0;
        saleupdate = 0;
        //executebtn.setEnabled(false);
        orderbtn.setText("Hold Order");

        totalqtytext.setText(null);
        customernameactive();

        customernameactive();
        customer_field_clear();
        cashcredit = 0;
        hold = 0;

        mobilebank = 0;
        clear();
        parchase_code();
        tree = 0;

        discountboxtypefinel.setSelectedIndex(0);
        mpobox.setSelectedIndex(0);
        territorybox.setSelectedIndex(0);
        entrucesure();
    }

    private void creditCustomerAcount() {

        float totalinvoce = (float) total_invoice_amount();
        float prebamt = Float.parseFloat(balanceduetext.getText());
        float nettotalin = totalinvoce + prebamt;
        String nets = String.format("%.2f", nettotalin);
        netstext.setText(nets);

    }

    private void CheckEntryFilter() {
        finelEntry();
    }

    //cash and credit table
    private void finelEntry() {

        if (updatekey == 0) {

            if (itemcode == null || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");

            } else {
                checkentry();

            }

        } else {
            int i = dataTable.getSelectedRow();
            if (i >= 0) {
                String batch = batchtext.getText();
                String expdates = expdate.getText();
                String boxsize = boxsizetext.getText();
                float tpd = Float.parseFloat(unitrateText.getText());
                float qtytbl = (Float) dataTable.getValueAt(i, 9);
                float applyqty = Float.parseFloat(qtytext.getText());
                float totalqty = applyqty;
                String bonusqty = "";
                float discount = 0;
                if (bonusqtytext.getText().isEmpty()) {
                    bonusqty = "";
                } else {
                    bonusqty = bonusqtytext.getText();
                }

                if (discounttext.getText().isEmpty()) {
                    discount = 0;
                } else {
                    discount = Float.parseFloat(discounttext.getText());

                }
                float vat = Float.parseFloat(pvattext.getText());

                float totalamount = totalqty * tpd;
                float totaldiscounts = totalqty * discount;
                float allTotalVat = vat * totalqty;
                float NetallTotal = totalamount + allTotalVat;
                dataTable.setValueAt(batch, i, 3);
                dataTable.setValueAt(expdates, i, 4);
                dataTable.setValueAt(boxsize, i, 5);
                dataTable.setValueAt(tpd, i, 6);
                dataTable.setValueAt(discount, i, 7);
                dataTable.setValueAt(vat, i, 8);
                dataTable.setValueAt(totalqty, i, 9);
                dataTable.setValueAt(bonusqty, i, 10);
                dataTable.setValueAt(totalamount, i, 11);
                dataTable.setValueAt(totaldiscounts, i, 12);
                dataTable.setValueAt(allTotalVat, i, 13);
                dataTable.setValueAt(NetallTotal, i, 14);

                totalqtytext.setText(df3.format(total_qty()));
                double inputotalrate = total_action_mrp();
                totalrate.setText(df2.format(inputotalrate));

                double inputvat = total_action_vat();
                totalvattext.setText(df2.format(inputvat));

                double input = total_invoice_amount();

                totalinvoiceamount.setText(df2.format(input));

                netdiscount.setText(Float.toString(total_discount_amount()));
                discountfinel = total_discount_amount();
                bookreturntext.setText(df2.format(bookReturn));
                double nettotalwbr = input - bookReturn;
                nettotaltext.setText(df2.format(nettotalwbr));

                creditCustomerAcount();
                clear();

            }

        }

    }
    private static final DecimalFormat df3 = new DecimalFormat("#");

    private void checkentry() {
        String s = "";
        boolean exists = false;
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            s = dataTable.getValueAt(i, 1).toString().trim();
            ///filter
            float tpd = Float.parseFloat(unitrateText.getText());
            float qtytbl = (Float) dataTable.getValueAt(i, 9);
            float applyqty = Float.parseFloat(qtytext.getText());
            float totalqty = qtytbl + applyqty;
            String bonusqty = "";
            float discount = 0;
            if (bonusqtytext.getText().isEmpty()) {
                bonusqty = "";
            } else {
                bonusqty = bonusqtytext.getText();
            }

            if (discounttext.getText().isEmpty()) {
                discount = 0;
            } else {
                discount = Float.parseFloat(discounttext.getText());

            }
            float vat = Float.parseFloat(pvattext.getText());

            float totalamount = totalqty * tpd;
            float totaldiscounts = totalqty * discount;
            float allTotalVat = vat * totalqty;
            float NetallTotal = totalamount + allTotalVat;

            if (itemcode.equals("")) {
                JOptionPane.showMessageDialog(null, "Enter Invoice Details First");
            } else if (itemcode.equals(s)) {
                exists = true;
                dataTable.setValueAt(tpd, i, 6);

                dataTable.setValueAt(discount, i, 7);
                dataTable.setValueAt(vat, i, 8);
                dataTable.setValueAt(totalqty, i, 9);
                dataTable.setValueAt(bonusqty, i, 10);
                dataTable.setValueAt(totalamount, i, 11);
                dataTable.setValueAt(totaldiscounts, i, 12);
                dataTable.setValueAt(allTotalVat, i, 13);
                dataTable.setValueAt(NetallTotal, i, 14);

                double inputotalrate = total_action_mrp();
                totalrate.setText(df2.format(inputotalrate));
                double inputvat = total_action_vat();
                totalvattext.setText(df2.format(inputvat));
                double input = total_invoice_amount();
                totalinvoiceamount.setText(df2.format(input));
                netdiscount.setText(Float.toString(total_discount_amount()));
                discountfinel = total_discount_amount();
                // dataTable.setRowSelectionInterval(0,i);
                bookreturntext.setText(df2.format(bookReturn));
                double nettotalwbr = input - bookReturn;
                nettotaltext.setText(df2.format(nettotalwbr));
                creditCustomerAcount();
                break;
            }
        }
        if (!exists) {
            entryData();
        } else {
            // JOptionPane.showMessageDialog(null, "This Data Already Exist.");
            clear();

        }
        itemnamesearch.requestFocusInWindow();
    }
    private static final DecimalFormat df2 = new DecimalFormat("#.00");

    private void entryData() {
        float discount;
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        tree++;
        String bonusqty = "";

        if (bonusqtytext.getText().isEmpty()) {
            bonusqty = "";
        } else {
            bonusqty = bonusqtytext.getText();
        }

        float tpd = Float.parseFloat(unitrateText.getText());

        if (discounttext.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Float.parseFloat(discounttext.getText());

        }
        float vat = Float.parseFloat(pvattext.getText());
        float qty = Float.parseFloat(qtytext.getText());
        float totalamount = Float.parseFloat(totalamounttext1.getText());
        float totaldiscount = Float.parseFloat(totaldiscounttext1.getText());
        float allTotalVat = Float.parseFloat(altotalvattext.getText());
        float NetallTotal = Float.parseFloat(netotaltext.getText());

        model2.addRow(new Object[]{tree, itemcode, itemname, batchtext.getText(), expdate.getText(), boxsizetext.getText(), tpd, discount, vat, qty, bonusqty, totalamount, totaldiscount, allTotalVat, NetallTotal});

        // model2.moveRow(model2.getRowCount() - 1, model2.getRowCount() - 1, 0);
        totalqtytext.setText(df3.format(total_qty()));
        double inputotalrate = total_action_mrp();
        totalrate.setText(df2.format(inputotalrate));

        double inputvat = total_action_vat();
        totalvattext.setText(df2.format(inputvat));

        double input = total_invoice_amount();

        totalinvoiceamount.setText(df2.format(input));

        netdiscount.setText(Float.toString(total_discount_amount()));
        discountfinel = total_discount_amount();
        bookreturntext.setText(df2.format(bookReturn));
        double nettotalwbr = input - bookReturn;
        nettotaltext.setText(df2.format(nettotalwbr));

        creditCustomerAcount();
        clear();
    }

    private void deleterow() {

        int[] toDelete = dataTable.getSelectedRows();
        Arrays.sort(toDelete); // be shure to have them in ascending order.
        DefaultTableModel myTableModel = (DefaultTableModel) dataTable.getModel();
        for (int ii = toDelete.length - 1; ii >= 0; ii--) {
            myTableModel.removeRow(toDelete[ii]); // beginning at the largest.
        }
        totalqtytext.setText(df3.format(total_qty()));
        double inputotalrate = total_action_mrp();
        totalrate.setText(df2.format(inputotalrate));

        double inputvat = total_action_vat();
        totalvattext.setText(df2.format(inputvat));

        double input = total_invoice_amount();

        totalinvoiceamount.setText(df2.format(input));

        netdiscount.setText(Float.toString(total_discount_amount()));
        discountfinel = total_discount_amount();
        bookreturntext.setText(df2.format(bookReturn));
        double nettotalwbr = input - bookReturn;
        nettotaltext.setText(df2.format(nettotalwbr));

        creditCustomerAcount();
        updatekey = 0;
        clear();
    }

    private float total_action_mrp() {

        int rowaCount = dataTable.getRowCount();
        float qty;
        float itemmrp;
        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 11).toString());
            // totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 7).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_discount_amount() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 12).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_vat() {

        int rowaCount = dataTable.getRowCount();
        float qty;
        float itemmrp;
        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 13).toString());
            // totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 7).toString());
        }

        return (float) totaltpmrp;

    }

    private double total_invoice_amount() {

        int rowaCount = dataTable.getRowCount();

        double totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Double.parseDouble(dataTable.getValueAt(i, 14).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_qty() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 9).toString());
        }

        return (float) totaltpmrp;

    }

    //end book table   
    //return entry table
    private void checkentry_return() {
        String s = "";
        boolean exists = false;
        for (int i = 0; i < returndataTable.getRowCount(); i++) {
            s = returndataTable.getValueAt(i, 1).toString().trim();
            ///filter
            float tpd = Float.parseFloat(unitrateText1.getText());
            float qtytbl = (Float) returndataTable.getValueAt(i, 9);
            float applyqty = Float.parseFloat(qtytext1.getText());
            float totalqty = qtytbl + applyqty;
            String bonusqty = "";
            float discount = 0;
            if (bonusqtytext1.getText().isEmpty()) {
                bonusqty = "";
            } else {
                bonusqty = bonusqtytext1.getText();
            }

            if (discounttext1.getText().isEmpty()) {
                discount = 0;
            } else {
                discount = Float.parseFloat(discounttext1.getText());

            }
            float vat = Float.parseFloat(pvattext1.getText());
            float totalamount = totalqty * tpd;
            float totaldiscounts = totalqty * discount;
            float allTotalVat = vat * totalqty;
            float NetallTotal = totalamount + allTotalVat;

            if (ReturnItemcode.equals("")) {
                // JOptionPane.showMessageDialog(null, "Enter Invoice Details First");
            } else if (ReturnItemcode.equals(s)) {
                exists = true;

                //update
                returndataTable.setValueAt(totalqty, i, 9);
                returndataTable.setValueAt(totalamount, i, 10);
                returndataTable.setValueAt(totaldiscounts, i, 11);
                returndataTable.setValueAt(allTotalVat, i, 12);
                returndataTable.setValueAt(NetallTotal, i, 13);

                //total calculation
                double inputotalrate = total_action_mrp_return();
                totalrateReturn.setText(df2.format(inputotalrate));
                netdiscountreturn.setText(Float.toString(total_discount_amount_return()));
                double inputvat = total_action_vat_return();
                totalvattextreturn.setText(df2.format(inputvat));
                double input = total_invoice_amount_return();
                totalinvoiceamountreturn.setText(df2.format(input));
                break;
            }
        }
        if (!exists) {
            entryData_return();
        } else {
            JOptionPane.showMessageDialog(null, "This Data Already Exist.");
            Returnclear();

        }
        // itemnamesearch.requestFocusInWindow();
    }

    private void entryData_return() {
        float discount;
        DefaultTableModel model2 = (DefaultTableModel) returndataTable.getModel();
        tree++;
        String bonusqty = "";

        if (bonusqtytext1.getText().isEmpty()) {
            bonusqty = "";
        } else {
            bonusqty = bonusqtytext1.getText();
        }

        float tpd = Float.parseFloat(unitrateText1.getText());

        if (discounttext1.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Float.parseFloat(discounttext1.getText());

        }
        float vat = Float.parseFloat(pvattext1.getText());
        float qty = Float.parseFloat(qtytext1.getText());
        float totalamount = Float.parseFloat(totalamounttext2.getText());
        float totaldiscount = Float.parseFloat(totaldiscounttext2.getText());
        float allTotalVat = Float.parseFloat(altotalvattext1.getText());
        float NetallTotal = Float.parseFloat(netotaltext1.getText());

        model2.addRow(new Object[]{tree, ReturnItemcode, itemnamesearch1.getText(), batchtext1.getText(), expdate1.getText(), boxsizetext1.getText(), tpd, discount, vat, qty, totalamount, totaldiscount, allTotalVat, NetallTotal});

        double inputotalrate = total_action_mrp_return();
        totalrateReturn.setText(df2.format(inputotalrate));
        netdiscountreturn.setText(Float.toString(total_discount_amount_return()));
        double inputvat = total_action_vat_return();
        totalvattextreturn.setText(df2.format(inputvat));
        double input = total_invoice_amount_return();
        totalinvoiceamountreturn.setText(df2.format(input));
        Returnclear();
    }

    private void Returnclear() {
        ReturnItemcode = "";
        itemnamesearch1.setText(null);
        genericbox1.setText(null);
        strengthtext1.setText(null);
        batchtext1.setText(null);
        boxsizetext1.setText(null);
        unitrateText1.setText(null);

        discounttext1.setText(null);
        pvattext1.setText(null);
        pvatpertext1.setText(null);
        ptotaltptext1.setText(null);
        qtytext1.setText(null);
        bonusqtytext1.setText(null);

        unibox1.setSelectedIndex(0);
        totalamounttext2.setText(null);
        totaldiscounttext2.setText(null);
        altotalvattext1.setText(null);
        netotaltext1.setText(null);
        stocktext1.setText(null);
        expdate1.setText(null);

    }

    private void Table_Update_Element() {
        checkUpdate();

    }

    private void checkUpdate() {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int i = dataTable.getSelectedRow();
        if (i >= 0) {
            float discount;

            float vat = includevat / 100;
            float totalvat, productvat;

            //float tpd = Float.parseFloat(unitrateText.getText());
            if (" ".equals(discountamt)) {

                discount = 0;

            } else {

                discount = Float.parseFloat(discountamt);

            }

            float tpwdisc = tpds - discount;
            productvat = (tpwdisc * vat);

            float totaldics = discount * qty;
            float totaldiscountformat = Float.parseFloat(df2.format(totaldics));

            float nettotaltp = (tpds * qty);

            totalvat = (productvat * qty);
            float totalvatfloat = Float.parseFloat(df2.format(totalvat));

            float totalall = nettotaltp + totalvat - totaldics;

            // model.setValueAt(tpds, i, 3);
            model.setValueAt(totaldics, i, 5);
            //model.setValueAt(qty, i, 4);
            model.setValueAt(nettotaltp, i, 6);
//            model.setValueAt(totalvat, i, 7);
            //   model.setValueAt(totalall, i, 8);

        }

        updatekey = 0;
        totalqtytext.setText(df3.format(total_qty()));
        double inputotalrate = total_action_mrp();
        totalrate.setText(df2.format(inputotalrate));
        double inputvat = total_action_vat();
        totalvattext.setText(df2.format(inputvat));
        double input = total_invoice_amount();
        nettotaltext.setText(df2.format(input));
        netdiscount.setText(Float.toString(total_discount_amount()));
        discountfinel = total_discount_amount();

        creditCustomerAcount();
        clear();

    }

    private void checkUpdate_return() {

        DefaultTableModel model = (DefaultTableModel) returndataTable.getModel();
        int i = returndataTable.getSelectedRow();
        if (i >= 0) {
            float discount;
            float vat = includevat;
            float totalvat;

            float tpd = Float.parseFloat(unitrateText.getText());

            if (discounttext.getText().isEmpty()) {

                discount = 0;

            } else {
                discount = Float.parseFloat(discounttext.getText());

            }
            float tpwdisc = tpd - discount;
            float productvat = tpwdisc * vat;
            float productvatfloat = Float.parseFloat(df2.format(productvat));
            float qty = Float.parseFloat(qtytext.getText());
            float nettotaltp = (tpd * qty);
            float totaldisc = discount * qty;
            float totaldiscountformat = Float.parseFloat(df2.format(totaldisc));
            totalvat = (productvatfloat * qty);
            float totalvatfloat = Float.parseFloat(df2.format(totalvat));
            float totalall = (nettotaltp + totalvatfloat) - totaldisc;

            model.setValueAt(tpd, i, 3);
            model.setValueAt(totaldiscountformat, i, 5);
            model.setValueAt(qty, i, 4);
            model.setValueAt(nettotaltp, i, 7);
            model.setValueAt(totalvatfloat, i, 8);
            model.setValueAt(totalall, i, 9);

        }

        updatekey = 0;
        //total calculation
        double inputotalrate = total_action_mrp_return();
        totalrateReturn.setText(df2.format(inputotalrate));
        netdiscountreturn.setText(Float.toString(total_discount_amount_return()));
        double inputvat = total_action_vat_return();
        totalvattextreturn.setText(df2.format(inputvat));
        double input = total_invoice_amount_return();
        totalinvoiceamountreturn.setText(df2.format(input));
        clear();

    }

    private void deleterow_return() {

        int[] toDelete = returndataTable.getSelectedRows();
        Arrays.sort(toDelete); // be shure to have them in ascending order.
        DefaultTableModel myTableModel = (DefaultTableModel) returndataTable.getModel();
        for (int ii = toDelete.length - 1; ii >= 0; ii--) {
            myTableModel.removeRow(toDelete[ii]); // beginning at the largest.
        }
        //totalcalculation
        updatekey = 0;
        //total calculation
        double inputotalrate = total_action_mrp_return();
        totalrateReturn.setText(df2.format(inputotalrate));
        netdiscountreturn.setText(Float.toString(total_discount_amount_return()));
        double inputvat = total_action_vat_return();
        totalvattextreturn.setText(df2.format(inputvat));
        double input = total_invoice_amount_return();
        totalinvoiceamountreturn.setText(df2.format(input));
        clear();

    }

    private float total_action_mrp_return() {

        int rowaCount = returndataTable.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());
            totaltpmrp = totaltpmrp + Float.parseFloat(returndataTable.getValueAt(i, 10).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_discount_amount_return() {

        int rowaCount = returndataTable.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            totaltpmrp = totaltpmrp + Float.parseFloat(returndataTable.getValueAt(i, 11).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_vat_return() {
        int rowaCount = returndataTable.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            totaltpmrp = totaltpmrp + Float.parseFloat(returndataTable.getValueAt(i, 12).toString());
        }
        return (float) totaltpmrp;
    }

    private double total_invoice_amount_return() {
        int rowaCount = returndataTable.getRowCount();
        double totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            totaltpmrp = totaltpmrp + Double.parseDouble(returndataTable.getValueAt(i, 13).toString());
        }
        return (float) totaltpmrp;
    }

    private void salereturnInsert() {
        //double cashout = Double.parseDouble(totalinvoiceamount.getText());
        try {

            returncashamount = Float.parseFloat(totalinvoiceamountreturn.getText());
            /*
            if (bookReturnStatus == 0) {
                cashInDrwaerCashReturn(returncashamount);
            }
            
             */
            //customerbalanceupdatecash();

            String sql = "Insert into salereturn("
                    + "ReturnNo,"
                    + "invoiceNo,"
                    + "invoicetype,"
                    + "returndate,"
                    + "TotalAmount,"
                    + "TotalVat,"
                    + "Totalinvoice,"
                    + "returnacash,"
                    + "prevdue,"
                    + "customerid) values (?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, invoicetext1.getText());
            pst.setString(2, (String) invoiceNotext.getSelectedItem());
            pst.setString(3, "Invoice");
            pst.setDate(4, date);
            pst.setString(5, totalrateReturn.getText());
            pst.setString(6, totalvattextreturn.getText());
            pst.setString(7, totalinvoiceamountreturn.getText());
            pst.setDouble(8, returncashamount);
            pst.setDouble(9, 0);//balancedue
            pst.setString(10, cusId);
            //pst.setString(10, refnotext.getText());

            pst.execute();
            SalereturnDetailsInsert();
            Stockpdatereturn();
           // customerbalanceupdatereturn();

            //   JOptionPane.showMessageDialog(null, "Sale Return Successfuly Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void customerbalanceupdatereturn() throws SQLException {

        float todaysale = Float.parseFloat(totalinvoiceamountreturn.getText());
        float customer_amt = paidamt;
        float aftersaleamt = salesamt - todaysale;

        float aftercreditamt = (float) (creditAmnt - todaysale);

        balanced = balancedue - todaysale;

        String afterdue = df2.format(balanced);
        String creditamt = df2.format(aftercreditamt);
        String saleamt = df2.format(aftersaleamt);

        try {

            String sql = "Update customerInfo set creditAmnt='" + creditamt + "',Balancedue='" + afterdue + "',saleamount='" + saleamt + "' where customerid='" + cusId + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //   JOptionPane.showMessageDialog(null, "Data Update");
            //  statementinsert(balanced);
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void cashInDrwaerCashReturn(float cashout) {

        float balanc = bankbalance1 - cashout;
        try {

            String sql = "Insert into CashDrower(cashin,cashout,balance,type,upatedate,tranid) values(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setDouble(1, 0);
            pst.setDouble(2, cashout);
            pst.setDouble(3, balanc);
            pst.setString(4, "Sale Return");
            pst.setDate(5, date);
            pst.setString(6, invoicetext.getText());
            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void SalereturnDetailsInsert() throws SQLException {
        try {
            int rows = returndataTable.getRowCount();
            for (int row = 0; row < rows; row++) {
                String probarcode = (String) returndataTable.getValueAt(row, 1);
                try {
                    String sql = "Select "
                            + "Itemcode,"
                            + "tp,"
                            + "stockunit"
                            + " from item where Itemcode='" + probarcode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {
                        // itemcode = rs.getString("Itemcode");
                        tpprice = rs.getFloat("tp");
                        unit = rs.getString("stockunit");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                float rate = (Float) returndataTable.getValueAt(row, 6);
                float itemvat = (Float) returndataTable.getValueAt(row, 7);
                float discount = (Float) returndataTable.getValueAt(row, 8);
                float qty = (Float) returndataTable.getValueAt(row, 9);
                float totalamount = (Float) returndataTable.getValueAt(row, 10);
                float totalvat = (Float) returndataTable.getValueAt(row, 12);
                float neTotal = (Float) returndataTable.getValueAt(row, 13);
                try {

                    String sql = "Insert into salereturnDetails("
                            + "returnNo,"
                            + "prcode,"
                            + "barcode,"
                            + "unitrate,"
                            + "qty,"
                            + "unit,"
                            + "discount,"
                            + "totalamount,"
                            + "vat,"
                            + "Totalvat,"
                            + "NetTotal,"
                            + "returnedate) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, invoicetext.getText());
                    pst.setString(2, probarcode);
                    pst.setString(3, probarcode);
                    pst.setDouble(4, rate);
                    pst.setFloat(5, qty);
                    pst.setString(6, unit);
                    pst.setDouble(7, discount);
                    pst.setDouble(8, totalamount);
                    pst.setDouble(9, itemvat);
                    pst.setDouble(10, totalvat);
                    pst.setDouble(11, neTotal);
                    pst.setDate(12, date);
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

    private void Stockpdatereturn() {
        // int cat=(Integer) categoryBox.getSelectedIndex();
        for (int i = 0; i < returndataTable.getRowCount(); i++) {
            String s = returndataTable.getValueAt(i, 1).toString().trim();
            float qty = (Float) returndataTable.getValueAt(i, 9);
            Stockpdatplus(s, qty);
        }

    }

    private void Stockpdatplus(String s, float qtyint) {

        try {

            String sql = "Select Qty from stockdetails where itemcode=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, s);
            rs = pst.executeQuery();

            while (rs.next()) {
                float stock = rs.getFloat("Qty");
                float update_qty = stock + qtyint;
                String sql1 = "Update stockdetails set Qty='" + update_qty + "' where itemcode='" + s + "'";
                pst = conn.prepareStatement(sql1);
                pst.execute();

            }  //JOptionPane.showMessageDialog(null, add1);

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void returnClear() {
        DefaultTableModel model2 = (DefaultTableModel) returndataTable.getModel();
        model2.setNumRows(0);
        DefaultTableModel model = (DefaultTableModel) datatbl1.getModel();
        model.setNumRows(0);

        totalrateReturn.setText(null);
        netdiscountreturn.setText(null);
        totalvattextreturn.setText(null);
        totalinvoiceamountreturn.setText(null);
        // cusId = "0";
        customerbox.removeAllItems();
        totalamounttext.setText(null);
        totalVatText.setText(null);
        netTotalText.setText(null);

        invoiceNotext.removeAllItems();
        final JTextField textfield = (JTextField) invoiceNotext.getEditor().getEditorComponent();
        textfield.setText(null);
        ReturnInvoice = "0";
        returnCode();

    }
//End return table 

    private void clear() {

        itemnamesearch.removeAllItems();
        genericbox.removeAllItems();
        strengthtext.setText(null);
        batchtext.setText(null);
        boxsizetext.setText(null);
        unitrateText.setText(null);

        discounttext.setText(null);
        pvattext.setText(null);
        pvatpertext.setText(null);
        ptotaltptext.setText(null);
        qtytext.setText(null);
        bonusqtytext.setText(null);

        unibox.setSelectedIndex(0);
        totalamounttext1.setText(null);
        totaldiscounttext1.setText(null);
        altotalvattext.setText(null);
        netotaltext.setText(null);
        stocktext.setText(null);
        expdate.setText(null);
        itemnamesearch.requestFocusInWindow();

        //updateSystemProcess();
        updatekey = 0;
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        if (model2.getRowCount() == 0) {
            totalrate.setText(null);
            netdiscount.setText(null);
            totalvattext.setText(null);
            totalinvoiceamount.setText(null);
            if (model2.getRowCount() == 0 && orderNobox.getSelectedIndex() > 0) {
                OrderStatus();
                AfterExecute();
                orderbtn.setText("Hold Order");
            }
        }

        directstore.setSelected(false);
        dataTable.clearSelection();

    }

    //credit inert
    private void invoicecheckcredit() {

        try {
            String sql = "Select id from sale";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next() == false) {
                saleInsertcheckcredit();

            } else {
                saleInsertcredit();

            }
        } catch (SQLException | JRException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    String Newexpcode;

    private void transactioncode() {
        try {
            int new_invs = 1;
            Newexpcode = ("EXP-" + new_invs + "");
            String sql = "Select id from expencess";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_invs = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                Newexpcode = ("EXP-" + ParchaseCode + "");
            }

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void DiscountExpencess() {
        transactioncode();
        float discountallow = discountfinel;
        if (discountallow > 0) {

            String type = "Invoice";
            String invoiceno = invoicetext.getText();
            String reason = "Sale Discount Expencess.Type:" + type + "Invoice No:" + invoiceno;
            try {

                String sql = "Insert into Expencess(expid,"
                        + "inputdate,"
                        + "extype,"
                        + "amount,"
                        + "paymenttype,"
                        + "remark,"
                        + "status) values(?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, Newexpcode);
                pst.setDate(2, date);
                pst.setString(3, "Sale Expencess");
                pst.setFloat(4, discountfinel);
                pst.setString(5, " ");
                pst.setString(6, reason);
                pst.setString(7, "Active");
                pst.execute();

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        } else {

        }

    }

    private void saleInsertcheckcredit() throws JRException {
        //   float chasin = Float.parseFloat(totalinvoiceamount.getText());

        try {
            float totaltd = totaltrade();
            float totalamt = Float.parseFloat(totalrate.getText());
            float totalprofite = totalamt - totaltd;
            float discount = discountfinel;
            String discountamt = df2.format(discount);
            float discountfnl = Float.parseFloat(discountamt);
            float profite = totalprofite - discount;
            float doubleNumber = Float.parseFloat(nettotaltext.getText());
            String doubleAsString = String.valueOf(doubleNumber);
            int indexOfDecimal = doubleAsString.indexOf(".");
            String netpaybel = doubleAsString.substring(0, indexOfDecimal);
            String rounding = doubleAsString.substring(indexOfDecimal);

            String sql = "Insert into sale(id,"
                    + "invoiceNo,"
                    + "invoicedate,"
                    + "time,"
                    + "paymentCurency,"
                    + "paymentType,"
                    + "netdiscount,"
                    + "TotalAmount,"
                    + "TotalVat,"
                    + "Totalinvoice,"
                    + "ReturnBookAmount,"
                    + "returnbookingtype,"
                    + "nettotal,"
                    + "customerid,"
                    + "Ref_No, "
                    + "totaltraprice,"
                    + "totalprofite,"
                    + "inputuserid,"
                    + "due,"
                    + "mpo,"
                    + "territory,"
                    + "rounding,"
                    + "payble,"
                    + "discountPercentage"
                    + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, new_inv);
            pst.setString(2, invoicetext.getText());
            pst.setDate(3, date);
            pst.setString(4, time);
            pst.setString(5, "TK");
            pst.setString(6, (String) Paymenttype);
            pst.setFloat(7, discountfnl);
            pst.setString(8, totalrate.getText());
            pst.setString(9, totalvattext.getText());
            pst.setString(10, totalinvoiceamount.getText());
            pst.setString(11, bookreturntext.getText());
            switch (bookReturnStatus) {
                case 0:
                    pst.setString(12, "Order/Ret");
                    break;
                case 1:
                    pst.setString(12, "Order Advanced");
                    break;
                default:
                    pst.setString(12, "Return");
                    break;
            }
            pst.setString(13, nettotaltext.getText());
            pst.setString(14, cusId);
            pst.setString(15, " ");
            pst.setFloat(16, totaltd);
            pst.setFloat(17, profite);
            pst.setInt(18, userid);
            pst.setString(19, nettotaltext.getText());
            pst.setString(20, (String) mpobox.getSelectedItem());
            pst.setString(21, (String) territorybox.getSelectedItem());
            pst.setString(22, rounding);
            pst.setString(23, netpaybel);
            if (discountfinel > 0 && discountboxtypefinel.getSelectedIndex() == 0) {
                pst.setString(24, "(" + netdiscount.getText() + "%)");
            } else {
                pst.setString(24, "");
            }
            pst.execute();
            SaleDetailsInsertcredit();
            //  DiscountExpencess();
            // statementinsert();
            Stockpdate();
            customerbalanceupdate();
            vatcollection();
            //  itemforcastInsert();

            if (orderNobox.getSelectedIndex() > 0) {
                OrderStatus();
            }

            //JOptionPane.showMessageDialog(null, "Invoice Data Saved");
            printInvoice();
            AfterExecute();
            // cashInDrwaer(chasin);

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void saleInsertcredit() throws JRException {
        //   float chasin = Float.parseFloat(totalinvoiceamount.getText());
        try {
            float totaltd = totaltrade();
            float totalamt = Float.parseFloat(totalrate.getText());
            float totalprofite = totalamt - totaltd;
            float discount = discountfinel;
            String discountamt = df2.format(discount);
            float discountfnl = Float.parseFloat(discountamt);
            float profite = totalprofite - discount;

            float doubleNumber = Float.parseFloat(nettotaltext.getText());
            String doubleAsString = String.valueOf(doubleNumber);
            int indexOfDecimal = doubleAsString.indexOf(".");
            String netpaybel = doubleAsString.substring(0, indexOfDecimal);
            String rounding = doubleAsString.substring(indexOfDecimal);

            String sql = "Insert into sale(invoiceNo,"
                    + "invoicedate,"
                    + "time,"
                    + "paymentCurency,"
                    + "paymentType,"
                    + "netdiscount,"
                    + "TotalAmount,"
                    + "TotalVat,"
                    + "Totalinvoice,"
                    + "ReturnBookAmount,"
                    + "returnbookingtype,"
                    + "nettotal,"
                    + "customerid,"
                    + "Ref_No ,"
                    + "totaltraprice,"
                    + "totalprofite,"
                    + "inputuserid,"
                    + "due, "
                    + "mpo, "
                    + "territory,"
                    + "rounding,"
                    + "payble,"
                    + "discountPercentage"
                    + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, invoicetext.getText());
            pst.setDate(2, date);
            pst.setString(3, time);
            pst.setString(4, (String) "Tk");
            pst.setString(5, (String) Paymenttype);
            pst.setFloat(6, discountfnl);
            pst.setString(7, totalrate.getText());
            pst.setString(8, totalvattext.getText());
            pst.setString(9, totalinvoiceamount.getText());
            pst.setString(10, bookreturntext.getText());
            switch (bookReturnStatus) {
                case 0:
                    pst.setString(11, "Order/Ret");
                    break;
                case 1:
                    pst.setString(11, "Order Advanced");
                    break;
                default:
                    pst.setString(11, "Return");
                    break;
            }
            pst.setString(12, nettotaltext.getText());
            pst.setString(13, cusId);
            pst.setString(14, " ");
            pst.setFloat(15, totaltd);
            pst.setFloat(16, profite);
            pst.setInt(17, userid);
            pst.setString(18, nettotaltext.getText());
            pst.setString(19, (String) mpobox.getSelectedItem());
            pst.setString(20, (String) territorybox.getSelectedItem());
            pst.setString(21, rounding);
            pst.setString(22, netpaybel);
            if (discountfinel > 0 && discountboxtypefinel.getSelectedIndex() == 0) {
                pst.setString(23, "(" + netdiscount.getText() + "%)");
            } else {
                pst.setString(23, "");
            }
            pst.execute();

            SaleDetailsInsertcredit();
            //DiscountExpencess();
            //bookreturnstatusUpdate();
            // statementinsert();
            Stockpdate();
            customerbalanceupdate();
            vatcollection();
            // itemforcastInsert();
            if (orderNobox.getSelectedIndex() > 0) {
                OrderStatus();

            }

            //JOptionPane.showMessageDialog(null, "Invoice Data Saved");
            printInvoice();
            AfterExecute();
            // cashInDrwaer(chasin);

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void SaleDetailsInsertcredit() throws SQLException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                String proitemcode = (String) dataTable.getValueAt(row, 1);
                try {
                    String sql = "Select barcode,"
                            + "tp,"
                            + "(select generic_name from generic un where un.generic_id=it.generic_id) as 'genericname',"
                            + "(select unitshort from unit un where un.id=it.stockunit) as 'unit' "
                            + "from item it where it.Itemcode='" + proitemcode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {
                        barcode = rs.getString("barcode");
                        tpprice = rs.getFloat("tp");
                        unit = rs.getString("unit");
                        genericname = rs.getString("genericname");

                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                float mrpvate = 0;
                float mrpdiscount = 0;
                float profit = 0;
                float profitper = 0;

                String batch = (String) dataTable.getValueAt(row, 3);
                String expdates = (String) dataTable.getValueAt(row, 4);
                String boxsize = (String) dataTable.getValueAt(row, 5);
                float mrp = (Float) dataTable.getValueAt(row, 6);
                float discount = (Float) dataTable.getValueAt(row, 7);
                float vat = (Float) dataTable.getValueAt(row, 8);
                float qty = (Float) dataTable.getValueAt(row, 9);
                String bonusqty = (String) dataTable.getValueAt(row, 10);
                mrpdiscount = mrp - discount;
                mrpvate = mrp + vat;
                profit = mrp - mrp;
                profitper = ((profit * mrp) / 100);
                float total = (Float) dataTable.getValueAt(row, 11);
                float totaldiscount = (Float) dataTable.getValueAt(row, 12);
                float totalvat = (Float) dataTable.getValueAt(row, 13);
                float nettotal = (Float) dataTable.getValueAt(row, 14);

                try {

                    String sql = "Insert into saledetails("
                            + "invoiceNo,"
                            + "prcode,"
                            + "barcode,"
                            + "batch,"
                            + "expdate,"
                            + "boxsize,"
                            + "tp,"
                            + "unitrate,"
                            + "discount,"
                            + "vat,"
                            + "mrpvat,"
                            + "mrpdis,"
                            + "qty,"
                            + "bonusqty,"
                            + "unit,"
                            + "totalamount,"
                            + "totaldiscount,"
                            + "Totalvat,"
                            + "NetTotal,"
                            + "invoicedate,"
                            + "cusid,"
                            + "profit,"
                            + "profitper,"
                            + "generic"
                            + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, invoicetext.getText());
                    pst.setString(2, proitemcode);
                    pst.setString(3, barcode);
                    pst.setString(4, batch);
                    pst.setString(5, expdates);
                    pst.setString(6, boxsize);
                    pst.setFloat(7, tpprice);
                    pst.setFloat(8, mrp);
                    pst.setFloat(9, discount);
                    pst.setFloat(10, vat);
                    pst.setFloat(11, mrpvate);
                    pst.setFloat(12, mrpdiscount);
                    pst.setFloat(13, qty);
                    pst.setString(14, bonusqty);
                    pst.setString(15, unit);
                    pst.setFloat(16, total);
                    pst.setFloat(17, totaldiscount);
                    pst.setFloat(18, totalvat);
                    pst.setFloat(19, nettotal);
                    pst.setDate(20, date);
                    pst.setString(21, cusId);
                    pst.setFloat(22, profit);
                    pst.setFloat(23, profitper);
                    pst.setString(24, genericname);
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

    private void checkBalance(String accno) {
        // String accno = (String) accountbox.getSelectedItem();
        try {
            String sql = "Select cashin,cashout from BankAccount where accountno='" + accno + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                bankcashout = rs.getFloat("cashout");
                bankcashin = rs.getFloat("cashin");
                balance = bankcashin - bankcashout;

                // addStock(balance);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void itemforcastInsert() throws SQLException {

        try {

            int rows = dataTable.getRowCount();

            for (int row = 0; row < rows; row++) {

                String probarcode = (String) dataTable.getValueAt(row, 1);
                try {
                    String sql = "Select bp.itemcode as 'itemcode',bp.tp as 'tp',bp.stockunit as 'unit',bp.vat as 'vat' from item bp where bp.Itemcode='" + probarcode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        itemcode = rs.getString("itemcode");
                        tpprice = rs.getFloat("tp");
                        unit = rs.getString("unit");
                        includevat = rs.getFloat("vat");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                float rate = (Float) dataTable.getValueAt(row, 3);
                float qty = Float.parseFloat(dataTable.getValueAt(row, 4).toString());
                float discount = (Float) dataTable.getValueAt(row, 5);
                float totalamount = rate * qty;
                float neTotal = (Float) dataTable.getValueAt(row, 6);
                float itemdiscount = discount / qty;
                float mrpdiscount = rate - itemdiscount;
                float itemvat = includevat / 100 * mrpdiscount;
                float totalvat = itemvat * qty;
                try {

                    String sql = "Insert into itemforcast(invoiceNo,prcode,barcode,unitrate,qty,unit,discount,commesion,totalamount,vat,Totalvat,NetTotal,tradprice,invoicedate) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, invoicetext.getText());
                    pst.setString(2, itemcode);
                    pst.setString(3, barcode);
                    pst.setFloat(4, rate);
                    pst.setFloat(5, qty);
                    pst.setString(6, unit);
                    pst.setFloat(7, discount);
                    pst.setFloat(8, 0);
                    pst.setFloat(9, totalamount);
                    pst.setFloat(10, itemvat);
                    pst.setFloat(11, totalvat);
                    pst.setFloat(12, neTotal);
                    pst.setFloat(13, tpprice);
                    pst.setDate(14, date);
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

    private void vatcollection() {
        //   float chasin = Float.parseFloat(totalinvoiceamount.getText());
        try {

            String sql = "Insert into vatcollection("
                    + "invoiceNopurchaseNo,"
                    + "invoicedate,"
                    + "paymenttype,"
                    + "vattype,"
                    + "TotalAmount,"
                    + "totaldiscount,"
                    + "TotalVat,"
                    + "Totalinvoice,"
                    + "customerid) values (?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, invoicetext.getText());
            pst.setDate(2, date);

            pst.setString(3, (String) Paymenttype);
            pst.setString(4, "Invoice");
            pst.setString(5, totalrate.getText());
            pst.setFloat(6, discountfinel);
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

    private float totaltrade() {

        int rows = dataTable.getRowCount();
        float totaltradep = 0;
        // int rows1 = configtable.getRowCount();
        for (int row = 0; row < rows; row++) {
            // String coitemname = (String)sel_tbl.getValueAt(row, 0);
            String procode = (String) dataTable.getValueAt(row, 1);
            try {
                String sql = "Select tp from item where Itemcode='" + procode + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    tpprice = rs.getFloat("tp");

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

            // float rate = (Float) dataTable.getValueAt(row, 6);
            float qty = Float.parseFloat(dataTable.getValueAt(row, 9).toString());
            float totaltrade = qty * tpprice;
            totaltradep = totaltradep + totaltrade;

        }
        return (float) totaltradep;

    }

    private void username() throws SQLException {

        try {
            String sql = "Select name from admin where id='" + userid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                username = rs.getString("name");

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void printInvoice() throws JRException {
        if (printinvoicecheck.isSelected() == true) {
            String nettotal = null;
            nettotal = nettotaltext.getText();
            Invoiceno = invoicetext.getText();

            //JOptionPane.showMessageDialog(null, balanceduetext.getText());
            try {

                //String arebic = convertNumberToArabicWords(num);
                float doubleNumber = Float.parseFloat(nettotaltext.getText());
                String doubleAsString = String.valueOf(doubleNumber);
                int indexOfDecimal = doubleAsString.indexOf(".");
                String netpaybel = doubleAsString.substring(0, indexOfDecimal);

                String inwords = convertToIndianCurrency(netpaybel);
                String convert = inwords;

                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/creditinvoice.jrxml");

                HashMap para = new HashMap();
                para.put("invoiceno", Invoiceno);
                para.put("paymenttype", "Credit");
                para.put("cashier", username);
                para.put("prevoiusdue", balanceduetext.getText());
                para.put("currentdue", totalinvoiceamount.getText());
                para.put("totaldue", netstext.getText());
                para.put("inwords", convert);
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

                JasperPrintManager.printReport(jp, true);
                //JasperViewer.viewReport(jp, true);

            } catch (NumberFormatException | JRException ex) {
                JOptionPane.showMessageDialog(null, ex);

            }

            /*
               

              
                    Invoiceno = invoicetext.getText();

                    try {

                        JasperDesign jd = JRXmlLoader.load(new File("")
                                .getAbsolutePath()
                                + "/src/Report/Return.jrxml");
                        //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

                        HashMap para = new HashMap();
                        para.put("returnno", Invoiceno);
                        // para.put("cusid", cusId);
                        para.put("cashier", username);
                        // para.put("discount", netdiscount.getText());
                        // para.put("prevoiusdue", previseamonmttext.getText());

                        JasperReport jr = JasperCompileManager.compileReport(jd);
                        JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

                        JasperPrintManager.printReport(jp, true);
                        //JasperViewer.viewReport(jp, true);

                    } catch (NumberFormatException | JRException ex) {
                        JOptionPane.showMessageDialog(null, ex);

                    }

             */
        }

    }

    private void Stockpdate() {
        // int cat=(Integer) categoryBox.getSelectedIndex();
        for (int i = 0; i < dataTable.getRowCount(); i++) {

            String s = dataTable.getValueAt(i, 1).toString().trim();
            float qty = Float.parseFloat(dataTable.getValueAt(i, 9).toString());
            Stockpdateumuse(s, qty);

        }

    }

    private void Stockpdateumuse(String s, float qtyint) {

        try {

            String sql = "Select Qty from stockdetails where itemcode =?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, s);
            rs = pst.executeQuery();

            while (rs.next()) {

                float stock = rs.getFloat("Qty");
                float update_qty = stock - qtyint;
                String sql1 = "Update stockdetails set Qty='" + update_qty + "' where itemcode='" + s + "'";
                pst = conn.prepareStatement(sql1);
                pst.execute();

            }  //JOptionPane.showMessageDialog(null, add1);

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void customerbalanceupdate() throws SQLException {

        float todaysale = Float.parseFloat(totalinvoiceamount.getText());
        float customer_amt = paidamt;
        float aftersaleamt = salesamt + todaysale;
        float dicount = discountfinel;
        float todicount = totaldiscount + dicount;
        float aftercreditamt = (float) (creditAmnt + todaysale);
        balanced = balancedue + todaysale;
        String afterdue = df2.format(balanced);
        String creditamt = df2.format(aftercreditamt);
        String saleamt = df2.format(aftersaleamt);

        try {

            String sql = "Update customerInfo set creditAmnt='" + creditamt + "',Balancedue='" + afterdue + "',saleamount='" + saleamt + "',totaldiscount='" + todicount + "' where customerid='" + cusId + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //   JOptionPane.showMessageDialog(null, "Data Update");
            statementinsert(balanced);
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void statementinsert(float balancedue) throws SQLException {
        String afterdue = df2.format(balancedue);
        try {

            String sql = "Insert into CraditStatement(customerid,Orderno,type,invoiceno,invoicedate,totalInvoice,Received_amt,remark,balance) values(?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, customeridtext.getText());
            pst.setString(2, "0");
            pst.setString(3, "Invoice");
            pst.setString(4, invoicetext.getText());
            pst.setDate(5, date);
            pst.setString(6, totalinvoiceamount.getText());
            pst.setDouble(7, 0.00);
            pst.setString(8, "Invoice");
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

    private void customerCheck() {

        entrucesure();
        String mobile = customeridtext.getText();

        try {
            String sql = "Select * from customerInfo  where customerid='" + cusId + "'OR ContactNo='" + mobile + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {
                cusId = rs.getString("customerid");
                customeridtextsecond.setText(cusId);
                //customeridtext.setText(cusId);
                String Name = rs.getString("customername");
                customerbox.setSelectedItem(Name);
                openingbalance = rs.getFloat("OpenigBalance");
                openingbalancetext.setText(String.format("%s", openingbalance));
                dipositamt = rs.getFloat("DipositAmt");
                creditAmnt = rs.getFloat("creditAmnt");
                creditamounttext.setText(String.format("%s", creditAmnt));
                cashamt = rs.getFloat("cashamt");
                cashamounttext.setText(String.format("%s", cashamt));
                salesamt = rs.getFloat("saleamount");
                totalsaletext.setText(String.format("%s", salesamt));
                paidamt = rs.getFloat("paidamount");
                totalpaymenttext.setText(String.format("%s", paidamt));
                balancedue = rs.getFloat("Balancedue");
                totaldiscount = rs.getFloat("totaldiscount");
                totaldiscounttext.setText(String.format("%s", totaldiscount));
                String trans = String.format("%s", balancedue);
                balanceduetext.setText(trans);

                customernameactive();
                //parchase_code();
                //codetext.requestFocusInWindow();
            } else {

                customerbox.removeAllItems();
                openingbalancetext.setText(null);
                creditamounttext.setText(null);
                cashamounttext.setText(null);
                totalsaletext.setText(null);
                totalpaymenttext.setText(null);
                totaldiscounttext.setText(null);
                balanceduetext.setText(null);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        int rows = dataTable.getRowCount();
        if (rows > 0) {
            creditCustomerAcount();

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

        itemmenu = new javax.swing.JPopupMenu();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel18 = new javax.swing.JPanel();
        itempane = new javax.swing.JTabbedPane();
        jPanel28 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        totalqtytext = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        netdiscount = new javax.swing.JTextField();
        discountboxtypefinel = new javax.swing.JComboBox<>();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        totalinvoiceamount = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        totalvattext = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        totalrate = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        bookreturntext = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        nettotaltext = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        orderbtn = new javax.swing.JButton();
        voidbtn = new javax.swing.JButton();
        executebtn = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descrptiontbl = new javax.swing.JTable();
        jPanel36 = new javax.swing.JPanel();
        searchtext = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        searchtypebox = new javax.swing.JComboBox<>();
        customerpane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        duelabel = new javax.swing.JLabel();
        openingbalancetext = new javax.swing.JTextField();
        nettoalabel = new javax.swing.JLabel();
        netstext = new javax.swing.JTextField();
        duelabel1 = new javax.swing.JLabel();
        creditamounttext = new javax.swing.JTextField();
        duelabel2 = new javax.swing.JLabel();
        cashamounttext = new javax.swing.JTextField();
        totalsaletext = new javax.swing.JTextField();
        duelabel3 = new javax.swing.JLabel();
        duelabel4 = new javax.swing.JLabel();
        totaldiscounttext = new javax.swing.JTextField();
        duelabel5 = new javax.swing.JLabel();
        totalpaymenttext = new javax.swing.JTextField();
        duelabel6 = new javax.swing.JLabel();
        balanceduetext = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        customeridtextsecond = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        newcustomeridtext = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        addresstext = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        contactnotext = new javax.swing.JTextField();
        jButton13 = new javax.swing.JButton();
        msgtext = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        snametext = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        orderNobox = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        inputdate = new com.toedter.calendar.JDateChooser();
        jLabel27 = new javax.swing.JLabel();
        invoicetext = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        customerbox = new javax.swing.JComboBox<>();
        customeridtext = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        previceinvoicebox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        mpobox = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        territorybox = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        qtytext = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        altotalvattext = new javax.swing.JTextField();
        totalamounttext1 = new javax.swing.JTextField();
        pvattext = new javax.swing.JTextField();
        bonusqtytext = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        unibox = new javax.swing.JComboBox<>();
        netotaltext = new javax.swing.JTextField();
        totaldiscounttext1 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        ptotaltptext = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        discounttext = new javax.swing.JTextField();
        unitrateText = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        pvatpertext = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        strengthtext = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        batchtext = new javax.swing.JTextField();
        boxsizetext = new javax.swing.JTextField();
        stocktext = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        expdate = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        genericbox = new javax.swing.JComboBox<>();
        jLabel34 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jPanel15 = new javax.swing.JPanel();
        printinvoicecheck = new javax.swing.JCheckBox();
        directstore = new javax.swing.JCheckBox();
        jPanel16 = new javax.swing.JPanel();
        jButton16 = new javax.swing.JButton();
        addbtn = new javax.swing.JButton();
        jPanel266 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        netdiscountreturn = new javax.swing.JLabel();
        totalvattextreturn = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        returnexecution = new javax.swing.JButton();
        totalinvoiceamountreturn = new javax.swing.JLabel();
        totalrateReturn = new javax.swing.JLabel();
        adjustbtn = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        invoiceNotext = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jLabel72 = new javax.swing.JLabel();
        invoicetext1 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        totalamounttext = new javax.swing.JLabel();
        netTotalText = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        totalVatText = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        netTotalText1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        datatbl1 = new javax.swing.JTable();
        jPanel26 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JButton();
        addbtn1 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        strengthtext1 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        batchtext1 = new javax.swing.JTextField();
        boxsizetext1 = new javax.swing.JTextField();
        stocktext1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        expdate1 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        genericbox1 = new javax.swing.JTextField();
        itemnamesearch1 = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        qtytext1 = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        altotalvattext1 = new javax.swing.JTextField();
        totalamounttext2 = new javax.swing.JTextField();
        pvattext1 = new javax.swing.JTextField();
        bonusqtytext1 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        unibox1 = new javax.swing.JComboBox<>();
        netotaltext1 = new javax.swing.JTextField();
        totaldiscounttext2 = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        ptotaltptext1 = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        discounttext1 = new javax.swing.JTextField();
        unitrateText1 = new javax.swing.JTextField();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        pvatpertext1 = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel20 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        returndataTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Invoice");
        setExtendedState(6);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        itempane.setBorder(null);
        itempane.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        itempane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itempaneMouseClicked(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(0, 51, 51));

        jPanel2.setBackground(new java.awt.Color(0, 51, 51));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        totalqtytext.setBackground(new java.awt.Color(0, 51, 51));
        totalqtytext.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        totalqtytext.setForeground(new java.awt.Color(255, 255, 255));
        totalqtytext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalqtytext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setBackground(new java.awt.Color(0, 51, 51));
        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("QTY");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                    .addComponent(totalqtytext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalqtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        jPanel13.setBackground(new java.awt.Color(0, 51, 51));
        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setBackground(new java.awt.Color(0, 51, 51));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Discount");

        netdiscount.setBackground(new java.awt.Color(0, 51, 51));
        netdiscount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        netdiscount.setForeground(new java.awt.Color(153, 0, 0));
        netdiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netdiscount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                netdiscountMouseClicked(evt);
            }
        });
        netdiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                netdiscountKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                netdiscountKeyReleased(evt);
            }
        });

        discountboxtypefinel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "%", "Value" }));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                        .addGap(3, 3, 3))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(netdiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(discountboxtypefinel, 0, 0, Short.MAX_VALUE)
                        .addGap(2, 2, 2))))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(netdiscount)
                    .addComponent(discountboxtypefinel))
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(0, 51, 51));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setBackground(new java.awt.Color(0, 51, 51));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Invoice");

        totalinvoiceamount.setBackground(new java.awt.Color(0, 51, 51));
        totalinvoiceamount.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        totalinvoiceamount.setForeground(new java.awt.Color(255, 255, 255));
        totalinvoiceamount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalinvoiceamount.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(totalinvoiceamount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalinvoiceamount, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(0, 51, 51));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        totalvattext.setBackground(new java.awt.Color(0, 51, 51));
        totalvattext.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        totalvattext.setForeground(new java.awt.Color(255, 255, 255));
        totalvattext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalvattext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel25.setBackground(new java.awt.Color(0, 51, 51));
        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("Vat");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(0, 51, 51));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        totalrate.setBackground(new java.awt.Color(0, 51, 51));
        totalrate.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        totalrate.setForeground(new java.awt.Color(255, 255, 255));
        totalrate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalrate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        totalrate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel7.setBackground(new java.awt.Color(0, 51, 51));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Amount");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel32.setBackground(new java.awt.Color(0, 51, 51));
        jPanel32.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel45.setBackground(new java.awt.Color(0, 51, 51));
        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("Return");

        bookreturntext.setBackground(new java.awt.Color(0, 51, 51));
        bookreturntext.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        bookreturntext.setForeground(new java.awt.Color(255, 255, 255));
        bookreturntext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        bookreturntext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bookreturntext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bookreturntext, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel33.setBackground(new java.awt.Color(0, 51, 51));
        jPanel33.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel46.setBackground(new java.awt.Color(0, 51, 51));
        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("Net Total");

        nettotaltext.setBackground(new java.awt.Color(0, 51, 51));
        nettotaltext.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        nettotaltext.setForeground(new java.awt.Color(255, 255, 255));
        nettotaltext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nettotaltext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nettotaltext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nettotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(0, 51, 51));

        orderbtn.setBackground(new java.awt.Color(0, 153, 0));
        orderbtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        orderbtn.setForeground(new java.awt.Color(255, 255, 255));
        orderbtn.setText("Hold Order");
        orderbtn.setEnabled(false);
        orderbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderbtnActionPerformed(evt);
            }
        });

        voidbtn.setBackground(new java.awt.Color(0, 153, 153));
        voidbtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        voidbtn.setForeground(new java.awt.Color(255, 255, 255));
        voidbtn.setText("Void");
        voidbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                voidbtnActionPerformed(evt);
            }
        });

        executebtn.setBackground(new java.awt.Color(67, 86, 86));
        executebtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        executebtn.setForeground(new java.awt.Color(255, 255, 255));
        executebtn.setText("Execution(F6)");
        executebtn.setBorder(null);
        executebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executebtnActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(153, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Check Invoice");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 102, 102));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Clear All");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(executebtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(voidbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                    .addComponent(orderbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton3)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(orderbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(voidbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(executebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);

        dataTable.setAutoCreateRowSorter(true);
        dataTable.setBorder(null);
        dataTable.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL.No", "ItemCode", "Descripion", "Batch", "Exp Date", "Box Sze", "Unit Rate", "Discount", "Vat", "Qty", "Bonus Qty", "Amount", "Total Discount", "Total Vat", "Net Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true, false, false, true, true, true, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTable.setGridColor(new java.awt.Color(204, 204, 204));
        dataTable.setRowHeight(35);
        dataTable.setRowMargin(1);
        dataTable.setShowHorizontalLines(true);
        dataTable.setShowVerticalLines(true);
        dataTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataTableMouseClicked(evt);
            }
        });
        dataTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dataTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dataTableKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(dataTable);
        if (dataTable.getColumnModel().getColumnCount() > 0) {
            dataTable.getColumnModel().getColumn(0).setResizable(false);
            dataTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            dataTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            dataTable.getColumnModel().getColumn(2).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(300);
            dataTable.getColumnModel().getColumn(9).setPreferredWidth(50);
        }

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1517, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 544, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jScrollPane4.setViewportView(jPanel17);

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        itempane.addTab("Sale", jPanel28);

        descrptiontbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        descrptiontbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Item Code", "Barcode", "Description", "MRP", "Stock"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        descrptiontbl.setGridColor(new java.awt.Color(204, 204, 204));
        descrptiontbl.setRowHeight(30);
        descrptiontbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                descrptiontblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(descrptiontbl);
        if (descrptiontbl.getColumnModel().getColumnCount() > 0) {
            descrptiontbl.getColumnModel().getColumn(0).setResizable(false);
            descrptiontbl.getColumnModel().getColumn(0).setPreferredWidth(50);
            descrptiontbl.getColumnModel().getColumn(1).setResizable(false);
            descrptiontbl.getColumnModel().getColumn(2).setResizable(false);
            descrptiontbl.getColumnModel().getColumn(3).setResizable(false);
            descrptiontbl.getColumnModel().getColumn(3).setPreferredWidth(300);
            descrptiontbl.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel36.setBackground(new java.awt.Color(0, 51, 51));

        searchtext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchtextMouseClicked(evt);
            }
        });
        searchtext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchtextActionPerformed(evt);
            }
        });
        searchtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchtextKeyReleased(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("Search: ");

        searchtypebox.setEditable(true);
        searchtypebox.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        searchtypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Itemcode", "Barcode", "Item Name" }));
        searchtypebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                searchtypeboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(searchtypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(searchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(738, Short.MAX_VALUE))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel36Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel36Layout.createSequentialGroup()
                        .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel38)
                            .addComponent(searchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(searchtypebox, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        itempane.addTab("Item Description", jPanel24);

        customerpane.setBackground(new java.awt.Color(67, 86, 86));
        customerpane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        customerpane.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Name");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("ID");

        duelabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        duelabel.setForeground(new java.awt.Color(255, 255, 255));
        duelabel.setText("Opening Balance");

        openingbalancetext.setEditable(false);
        openingbalancetext.setBackground(new java.awt.Color(0, 51, 51));
        openingbalancetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        openingbalancetext.setForeground(new java.awt.Color(255, 255, 255));
        openingbalancetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        openingbalancetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        openingbalancetext.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        openingbalancetext.setSelectedTextColor(new java.awt.Color(102, 0, 0));
        openingbalancetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openingbalancetextActionPerformed(evt);
            }
        });

        nettoalabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nettoalabel.setForeground(new java.awt.Color(255, 255, 255));
        nettoalabel.setText("Net Total");

        netstext.setEditable(false);
        netstext.setBackground(new java.awt.Color(0, 51, 51));
        netstext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        netstext.setForeground(new java.awt.Color(255, 255, 255));
        netstext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netstext.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        netstext.setDisabledTextColor(new java.awt.Color(102, 0, 0));
        netstext.setSelectedTextColor(new java.awt.Color(102, 0, 0));

        duelabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        duelabel1.setForeground(new java.awt.Color(255, 255, 255));
        duelabel1.setText("Credit Amount");

        creditamounttext.setEditable(false);
        creditamounttext.setBackground(new java.awt.Color(0, 51, 51));
        creditamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        creditamounttext.setForeground(new java.awt.Color(255, 255, 255));
        creditamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        creditamounttext.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        creditamounttext.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        creditamounttext.setSelectedTextColor(new java.awt.Color(102, 0, 0));
        creditamounttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditamounttextActionPerformed(evt);
            }
        });

        duelabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        duelabel2.setForeground(new java.awt.Color(255, 255, 255));
        duelabel2.setText("Cash Amount");

        cashamounttext.setEditable(false);
        cashamounttext.setBackground(new java.awt.Color(0, 51, 51));
        cashamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cashamounttext.setForeground(new java.awt.Color(255, 255, 255));
        cashamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashamounttext.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        cashamounttext.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        cashamounttext.setSelectedTextColor(new java.awt.Color(102, 0, 0));
        cashamounttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashamounttextActionPerformed(evt);
            }
        });

        totalsaletext.setEditable(false);
        totalsaletext.setBackground(new java.awt.Color(0, 51, 51));
        totalsaletext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalsaletext.setForeground(new java.awt.Color(255, 255, 255));
        totalsaletext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalsaletext.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        totalsaletext.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        totalsaletext.setSelectedTextColor(new java.awt.Color(102, 0, 0));
        totalsaletext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalsaletextActionPerformed(evt);
            }
        });

        duelabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        duelabel3.setForeground(new java.awt.Color(255, 255, 255));
        duelabel3.setText("Total Sale");

        duelabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        duelabel4.setForeground(new java.awt.Color(255, 255, 255));
        duelabel4.setText("Total Discount");

        totaldiscounttext.setEditable(false);
        totaldiscounttext.setBackground(new java.awt.Color(0, 51, 51));
        totaldiscounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totaldiscounttext.setForeground(new java.awt.Color(255, 255, 255));
        totaldiscounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totaldiscounttext.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        totaldiscounttext.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        totaldiscounttext.setSelectedTextColor(new java.awt.Color(102, 0, 0));
        totaldiscounttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totaldiscounttextActionPerformed(evt);
            }
        });

        duelabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        duelabel5.setForeground(new java.awt.Color(255, 255, 255));
        duelabel5.setText("Paid Amount");

        totalpaymenttext.setEditable(false);
        totalpaymenttext.setBackground(new java.awt.Color(0, 51, 51));
        totalpaymenttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalpaymenttext.setForeground(new java.awt.Color(255, 255, 255));
        totalpaymenttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalpaymenttext.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        totalpaymenttext.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        totalpaymenttext.setSelectedTextColor(new java.awt.Color(102, 0, 0));
        totalpaymenttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalpaymenttextActionPerformed(evt);
            }
        });

        duelabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        duelabel6.setForeground(new java.awt.Color(255, 255, 255));
        duelabel6.setText("Balance Due");

        balanceduetext.setEditable(false);
        balanceduetext.setBackground(new java.awt.Color(0, 51, 51));
        balanceduetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        balanceduetext.setForeground(new java.awt.Color(255, 255, 255));
        balanceduetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        balanceduetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        balanceduetext.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        balanceduetext.setSelectedTextColor(new java.awt.Color(102, 0, 0));
        balanceduetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                balanceduetextActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        customeridtextsecond.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(duelabel, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(duelabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(duelabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(20, 20, 20))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(duelabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(30, 30, 30))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(nettoalabel)
                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(duelabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(duelabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(duelabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(30, 30, 30)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(openingbalancetext)
                    .addComponent(netstext, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                    .addComponent(creditamounttext)
                    .addComponent(cashamounttext)
                    .addComponent(totalsaletext)
                    .addComponent(totaldiscounttext)
                    .addComponent(totalpaymenttext)
                    .addComponent(balanceduetext)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(customeridtextsecond, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(726, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(customeridtextsecond, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(duelabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openingbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(duelabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(creditamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(duelabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(duelabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalsaletext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(duelabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totaldiscounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(duelabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalpaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(duelabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(balanceduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(netstext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nettoalabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(188, Short.MAX_VALUE))
        );

        customerpane.addTab("Customer Information", jPanel1);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Customer Name*");

        newcustomeridtext.setEditable(false);
        newcustomeridtext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newcustomeridtextActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Address:");

        addresstext.setColumns(20);
        addresstext.setRows(5);
        jScrollPane3.setViewportView(addresstext);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel42.setText("Customer Name*");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(msgtext, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel42))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(contactnotext, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(newcustomeridtext, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(snametext, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 359, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(682, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(msgtext, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newcustomeridtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(snametext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactnotext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        customerpane.addTab("New Customer", jPanel10);

        itempane.addTab("Customer", customerpane);

        jPanel4.setBackground(new java.awt.Color(0, 51, 51));

        jPanel5.setBackground(new java.awt.Color(0, 51, 51));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        orderNobox.setEditable(true);
        orderNobox.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        orderNobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        orderNobox.setEnabled(false);
        orderNobox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                orderNoboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Invoice No:");

        inputdate.setForeground(new java.awt.Color(102, 102, 102));
        inputdate.setDateFormatString("yyyy-MM-dd");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Date");

        invoicetext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        invoicetext.setForeground(new java.awt.Color(255, 255, 255));
        invoicetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Hold No");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Customer:");

        customerbox.setEditable(true);
        customerbox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        customerbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                customerboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        customeridtext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        customeridtext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        customeridtext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        customeridtext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        customeridtext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customeridtextMouseClicked(evt);
            }
        });
        customeridtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                customeridtextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customeridtextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                customeridtextKeyTyped(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Check Invoice:");

        previceinvoicebox.setEditable(true);
        previceinvoicebox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        previceinvoicebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        previceinvoicebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                previceinvoiceboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("MPO");

        mpobox.setEditable(true);
        mpobox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mpobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        mpobox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                mpoboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Territory");

        territorybox.setEditable(true);
        territorybox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        territorybox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        territorybox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                territoryboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputdate, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(invoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(customerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(customeridtext, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel28))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(mpobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(territorybox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addGap(76, 76, 76))
                    .addComponent(orderNobox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(previceinvoicebox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(5, 5, 5)
                        .addComponent(territorybox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel27))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(inputdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addComponent(jLabel28)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(invoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(customerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(customeridtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(mpobox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(jPanel5Layout.createSequentialGroup()
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel29)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addGap(5, 5, 5)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(previceinvoicebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(orderNobox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel8))
                            .addGap(0, 0, Short.MAX_VALUE))))
                .addGap(2, 2, 2))
        );

        jPanel12.setBackground(new java.awt.Color(0, 51, 51));
        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        qtytext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        qtytext.setForeground(new java.awt.Color(153, 0, 0));
        qtytext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        qtytext.setBorder(null);
        qtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                qtytextqtytextKeyReleased(evt);
            }
        });

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setText("Amount");

        altotalvattext.setEditable(false);
        altotalvattext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        altotalvattext.setForeground(new java.awt.Color(0, 102, 0));
        altotalvattext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        altotalvattext.setBorder(null);

        totalamounttext1.setEditable(false);
        totalamounttext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalamounttext1.setForeground(new java.awt.Color(0, 102, 0));
        totalamounttext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalamounttext1.setBorder(null);

        pvattext.setEditable(false);
        pvattext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pvattext.setForeground(new java.awt.Color(0, 102, 0));
        pvattext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pvattext.setBorder(null);
        pvattext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pvattextKeyReleased(evt);
            }
        });

        bonusqtytext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bonusqtytext.setForeground(new java.awt.Color(153, 0, 0));
        bonusqtytext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bonusqtytext.setBorder(null);
        bonusqtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bonusqtytextKeyPressed(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("Vat%");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("QTY");

        unibox.setBackground(new java.awt.Color(255, 255, 255));
        unibox.setEditable(true);
        unibox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        netotaltext.setEditable(false);
        netotaltext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        netotaltext.setForeground(new java.awt.Color(0, 102, 0));
        netotaltext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netotaltext.setBorder(null);

        totaldiscounttext1.setEditable(false);
        totaldiscounttext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totaldiscounttext1.setForeground(new java.awt.Color(0, 102, 0));
        totaldiscounttext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totaldiscounttext1.setBorder(null);

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("MRP");

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Total Vat");

        ptotaltptext.setEditable(false);
        ptotaltptext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ptotaltptext.setForeground(new java.awt.Color(0, 102, 0));
        ptotaltptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ptotaltptext.setBorder(null);
        ptotaltptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ptotaltptextKeyReleased(evt);
            }
        });

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Vat Value");

        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(255, 255, 255));
        jLabel56.setText("Net Total");

        discounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        discounttext.setForeground(new java.awt.Color(0, 102, 0));
        discounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        discounttext.setBorder(null);
        discounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                discounttextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                discounttextKeyReleased(evt);
            }
        });

        unitrateText.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        unitrateText.setForeground(new java.awt.Color(153, 0, 0));
        unitrateText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        unitrateText.setBorder(null);
        unitrateText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                unitrateTextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                unitrateTextKeyReleased(evt);
            }
        });

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setText("Bonus Qty");

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("TP+VAT");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Unit ");

        pvatpertext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pvatpertext.setForeground(new java.awt.Color(0, 102, 0));
        pvatpertext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pvatpertext.setBorder(null);
        pvatpertext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pvatpertextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pvatpertextKeyReleased(evt);
            }
        });

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setText("Total Discount");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Discount");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pvatpertext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ptotaltptext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bonusqtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalamounttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel54, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(totaldiscounttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(altotalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(netotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addComponent(jLabel50)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(pvatpertext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addComponent(jLabel51)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(ptotaltptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel12Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(jLabel16)
                                    .addGap(2, 2, 2))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                                    .addComponent(jLabel49)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel12Layout.createSequentialGroup()
                            .addComponent(jLabel14)
                            .addGap(1, 1, 1)
                            .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel53)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalamounttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                            .addComponent(jLabel52)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(bonusqtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(altotalvattext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addGap(1, 1, 1)
                            .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel54)
                                .addComponent(jLabel55))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(totaldiscounttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                            .addComponent(jLabel56)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(netotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(2, 2, 2))
        );

        jPanel14.setBackground(new java.awt.Color(0, 51, 51));
        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("Prasent Stock");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Strength");

        strengthtext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        strengthtext.setForeground(new java.awt.Color(102, 0, 0));
        strengthtext.setBorder(null);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Batch");

        batchtext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        batchtext.setForeground(new java.awt.Color(102, 0, 0));
        batchtext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        batchtext.setBorder(null);

        boxsizetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        boxsizetext.setForeground(new java.awt.Color(153, 0, 0));
        boxsizetext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        boxsizetext.setBorder(null);
        boxsizetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                boxsizetextKeyPressed(evt);
            }
        });

        stocktext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        stocktext.setForeground(new java.awt.Color(153, 0, 0));
        stocktext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        stocktext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                stocktextKeyPressed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Item Description");

        expdate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        expdate.setForeground(new java.awt.Color(102, 0, 0));
        expdate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        expdate.setBorder(null);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Generic");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Exp Date");

        genericbox.setBackground(new java.awt.Color(255, 255, 255));
        genericbox.setEditable(true);
        genericbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        genericbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        genericbox.setBorder(null);
        genericbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                genericboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        genericbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                genericboxKeyTyped(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Box Size");

        itemnamesearch.setBackground(new java.awt.Color(255, 255, 255));
        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
        itemnamesearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                itemnamesearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                itemnamesearchKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(genericbox, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(strengthtext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(batchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(expdate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxsizetext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stocktext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel26))
                        .addGap(49, 49, 49))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(genericbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(1, 1, 1)
                                .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(strengthtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(batchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(expdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(boxsizetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel14Layout.createSequentialGroup()
                                .addComponent(jLabel47)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stocktext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );

        jPanel15.setBackground(new java.awt.Color(0, 51, 51));

        printinvoicecheck.setBackground(new java.awt.Color(0, 51, 51));
        printinvoicecheck.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        printinvoicecheck.setForeground(new java.awt.Color(255, 255, 255));
        printinvoicecheck.setText("Prnt Invoice");

        directstore.setBackground(new java.awt.Color(0, 51, 51));
        directstore.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        directstore.setForeground(new java.awt.Color(255, 255, 255));
        directstore.setText("Direct Store");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(directstore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(printinvoicecheck))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(directstore)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(printinvoicecheck)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel16.setBackground(new java.awt.Color(0, 51, 51));

        jButton16.setBackground(new java.awt.Color(153, 0, 0));
        jButton16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setText("CLEAR");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        addbtn.setBackground(new java.awt.Color(255, 255, 255));
        addbtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addbtn.setText("Add");
        addbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addbtnjButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(2, 2, 2)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itempane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(itempane)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Invoice", jPanel18);

        jPanel266.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(0, 51, 51));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("Vat");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("Amount");

        netdiscountreturn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        netdiscountreturn.setForeground(new java.awt.Color(255, 255, 255));
        netdiscountreturn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        totalvattextreturn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalvattextreturn.setForeground(new java.awt.Color(255, 255, 255));
        totalvattextreturn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("Discount");

        jLabel41.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("Net Total");

        returnexecution.setBackground(new java.awt.Color(51, 0, 51));
        returnexecution.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        returnexecution.setForeground(new java.awt.Color(255, 255, 255));
        returnexecution.setText("Return");
        returnexecution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnexecutionActionPerformed(evt);
            }
        });

        totalinvoiceamountreturn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalinvoiceamountreturn.setForeground(new java.awt.Color(255, 255, 255));
        totalinvoiceamountreturn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        totalrateReturn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalrateReturn.setForeground(new java.awt.Color(255, 255, 255));
        totalrateReturn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        adjustbtn.setBackground(new java.awt.Color(51, 0, 0));
        adjustbtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        adjustbtn.setForeground(new java.awt.Color(255, 255, 255));
        adjustbtn.setText("Clear All");
        adjustbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adjustbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalrateReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(netdiscountreturn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalvattextreturn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalinvoiceamountreturn, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(returnexecution, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(adjustbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addGap(0, 0, 0)
                        .addComponent(totalrateReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addGap(0, 0, 0)
                        .addComponent(netdiscountreturn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addGap(0, 0, 0)
                        .addComponent(totalvattextreturn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addGap(0, 0, 0)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(totalinvoiceamountreturn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(returnexecution, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(adjustbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel27.setBackground(new java.awt.Color(0, 51, 51));

        jPanel34.setBackground(new java.awt.Color(0, 51, 51));
        jPanel34.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel44.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("Invoice No:");

        invoiceNotext.setEditable(true);
        invoiceNotext.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        invoiceNotext.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                invoiceNotextPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jButton2.setBackground(new java.awt.Color(102, 0, 0));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Details View");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(255, 255, 255));
        jLabel72.setText("Return No");

        invoicetext1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        invoicetext1.setForeground(new java.awt.Color(255, 255, 255));
        invoicetext1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(invoicetext1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(invoiceNotext, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel44)
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel72)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(invoiceNotext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(invoicetext1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel34Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel35.setBackground(new java.awt.Color(0, 51, 51));
        jPanel35.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel36.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Amount");

        jLabel40.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Vat");

        totalamounttext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        totalamounttext.setForeground(new java.awt.Color(255, 255, 255));
        totalamounttext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalamounttext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        netTotalText.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        netTotalText.setForeground(new java.awt.Color(255, 255, 255));
        netTotalText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        netTotalText.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel48.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Total Invoice");

        totalVatText.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        totalVatText.setForeground(new java.awt.Color(255, 255, 255));
        totalVatText.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalVatText.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel57.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Total Invoice");

        netTotalText1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        netTotalText1.setForeground(new java.awt.Color(255, 255, 255));
        netTotalText1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        netTotalText1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel74.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(255, 255, 255));
        jLabel74.setText("Customer");

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36)
                    .addComponent(totalamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel57)
                    .addComponent(netTotalText1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40)
                    .addComponent(totalVatText, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48)
                    .addComponent(netTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel35Layout.createSequentialGroup()
                            .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(2, 2, 2)
                            .addComponent(totalVatText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel35Layout.createSequentialGroup()
                            .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel36)
                                .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(6, 6, 6)
                            .addComponent(netTotalText1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(totalamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel35Layout.createSequentialGroup()
                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(2, 2, 2)
                            .addComponent(netTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                        .addComponent(jLabel74)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        datatbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Code", "Product Name", "Batch", "Exp Date", "Box Size", "MRP", "Discount", "Vat", "Qty", "Bonus Qty", "Amount", "Total Discount", "Total Vat", "Net Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        datatbl1.setRowHeight(25);
        datatbl1.setRowMargin(5);
        datatbl1.setShowHorizontalLines(true);
        datatbl1.setShowVerticalLines(true);
        datatbl1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatbl1MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(datatbl1);
        if (datatbl1.getColumnModel().getColumnCount() > 0) {
            datatbl1.getColumnModel().getColumn(0).setResizable(false);
            datatbl1.getColumnModel().getColumn(1).setResizable(false);
            datatbl1.getColumnModel().getColumn(2).setResizable(false);
            datatbl1.getColumnModel().getColumn(2).setPreferredWidth(300);
            datatbl1.getColumnModel().getColumn(3).setResizable(false);
            datatbl1.getColumnModel().getColumn(4).setResizable(false);
            datatbl1.getColumnModel().getColumn(5).setResizable(false);
            datatbl1.getColumnModel().getColumn(6).setResizable(false);
            datatbl1.getColumnModel().getColumn(7).setResizable(false);
            datatbl1.getColumnModel().getColumn(8).setResizable(false);
            datatbl1.getColumnModel().getColumn(9).setResizable(false);
            datatbl1.getColumnModel().getColumn(10).setResizable(false);
            datatbl1.getColumnModel().getColumn(11).setResizable(false);
            datatbl1.getColumnModel().getColumn(12).setResizable(false);
            datatbl1.getColumnModel().getColumn(13).setResizable(false);
            datatbl1.getColumnModel().getColumn(14).setResizable(false);
        }

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1661, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jScrollPane6.setViewportView(jPanel19);

        jPanel26.setBackground(new java.awt.Color(0, 51, 51));

        jPanel23.setBackground(new java.awt.Color(0, 51, 51));

        jButton17.setBackground(new java.awt.Color(153, 0, 0));
        jButton17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton17.setForeground(new java.awt.Color(255, 255, 255));
        jButton17.setText("CLEAR");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        addbtn1.setBackground(new java.awt.Color(255, 255, 255));
        addbtn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addbtn1.setText("Add");
        addbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addbtn1jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addbtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel21.setBackground(new java.awt.Color(0, 51, 51));
        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("Prasent Stock");

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Strength");

        strengthtext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        strengthtext1.setForeground(new java.awt.Color(102, 0, 0));
        strengthtext1.setBorder(null);

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Batch");

        batchtext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        batchtext1.setForeground(new java.awt.Color(102, 0, 0));
        batchtext1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        batchtext1.setBorder(null);

        boxsizetext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        boxsizetext1.setForeground(new java.awt.Color(153, 0, 0));
        boxsizetext1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        boxsizetext1.setBorder(null);
        boxsizetext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                boxsizetext1KeyPressed(evt);
            }
        });

        stocktext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        stocktext1.setForeground(new java.awt.Color(153, 0, 0));
        stocktext1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        stocktext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                stocktext1KeyPressed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Item Description");

        expdate1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        expdate1.setForeground(new java.awt.Color(102, 0, 0));
        expdate1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        expdate1.setBorder(null);

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Generic");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Exp Date");

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("Box Size");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(itemnamesearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(genericbox1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(strengthtext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addComponent(batchtext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(expdate1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxsizetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stocktext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel33))
                        .addGap(49, 49, 49))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(3, 3, 3)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(genericbox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemnamesearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(strengthtext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(batchtext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(expdate1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel59)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(boxsizetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(jLabel58)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stocktext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );

        jPanel22.setBackground(new java.awt.Color(0, 51, 51));
        jPanel22.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        qtytext1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        qtytext1.setForeground(new java.awt.Color(153, 0, 0));
        qtytext1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        qtytext1.setBorder(null);
        qtytext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytext1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                qtytext1qtytextKeyReleased(evt);
            }
        });

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(255, 255, 255));
        jLabel60.setText("Amount");

        altotalvattext1.setEditable(false);
        altotalvattext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        altotalvattext1.setForeground(new java.awt.Color(0, 102, 0));
        altotalvattext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        altotalvattext1.setBorder(null);

        totalamounttext2.setEditable(false);
        totalamounttext2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalamounttext2.setForeground(new java.awt.Color(0, 102, 0));
        totalamounttext2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalamounttext2.setBorder(null);

        pvattext1.setEditable(false);
        pvattext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pvattext1.setForeground(new java.awt.Color(0, 102, 0));
        pvattext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pvattext1.setBorder(null);
        pvattext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pvattext1KeyReleased(evt);
            }
        });

        bonusqtytext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bonusqtytext1.setForeground(new java.awt.Color(153, 0, 0));
        bonusqtytext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bonusqtytext1.setBorder(null);
        bonusqtytext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bonusqtytext1KeyPressed(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("Vat%");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("QTY");

        unibox1.setBackground(new java.awt.Color(255, 255, 255));
        unibox1.setEditable(true);
        unibox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        netotaltext1.setEditable(false);
        netotaltext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        netotaltext1.setForeground(new java.awt.Color(0, 102, 0));
        netotaltext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netotaltext1.setBorder(null);

        totaldiscounttext2.setEditable(false);
        totaldiscounttext2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totaldiscounttext2.setForeground(new java.awt.Color(0, 102, 0));
        totaldiscounttext2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totaldiscounttext2.setBorder(null);

        jLabel62.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("MRP");

        jLabel63.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(255, 255, 255));
        jLabel63.setText("Total Vat");

        ptotaltptext1.setEditable(false);
        ptotaltptext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ptotaltptext1.setForeground(new java.awt.Color(0, 102, 0));
        ptotaltptext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ptotaltptext1.setBorder(null);
        ptotaltptext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ptotaltptext1KeyReleased(evt);
            }
        });

        jLabel64.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 255, 255));
        jLabel64.setText("Vat Value");

        jLabel65.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(255, 255, 255));
        jLabel65.setText("Net Total");

        discounttext1.setEditable(false);
        discounttext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        discounttext1.setForeground(new java.awt.Color(0, 102, 0));
        discounttext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        discounttext1.setBorder(null);
        discounttext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                discounttext1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                discounttext1KeyReleased(evt);
            }
        });

        unitrateText1.setEditable(false);
        unitrateText1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        unitrateText1.setForeground(new java.awt.Color(153, 0, 0));
        unitrateText1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        unitrateText1.setBorder(null);
        unitrateText1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                unitrateText1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                unitrateText1KeyReleased(evt);
            }
        });

        jLabel66.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(255, 255, 255));
        jLabel66.setText("Bonus Qty");

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(255, 255, 255));
        jLabel67.setText("TP+VAT");

        jLabel68.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(255, 255, 255));
        jLabel68.setText("Unit ");

        pvatpertext1.setEditable(false);
        pvatpertext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pvatpertext1.setForeground(new java.awt.Color(0, 102, 0));
        pvatpertext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pvatpertext1.setBorder(null);
        pvatpertext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pvatpertext1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pvatpertext1KeyReleased(evt);
            }
        });

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(255, 255, 255));
        jLabel69.setText("Total Discount");

        jLabel70.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(255, 255, 255));
        jLabel70.setText("Discount");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel62)
                    .addComponent(unitrateText1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discounttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pvattext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pvatpertext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ptotaltptext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel67, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(qtytext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bonusqtytext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel68)
                    .addComponent(unibox1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalamounttext2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(totaldiscounttext2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(altotalvattext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(netotaltext1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel22Layout.createSequentialGroup()
                            .addComponent(jLabel61)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(pvatpertext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel22Layout.createSequentialGroup()
                            .addComponent(jLabel67)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(ptotaltptext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel22Layout.createSequentialGroup()
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel22Layout.createSequentialGroup()
                                    .addGap(1, 1, 1)
                                    .addComponent(jLabel70)
                                    .addGap(2, 2, 2))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                                    .addComponent(jLabel64)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(discounttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pvattext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel22Layout.createSequentialGroup()
                            .addComponent(jLabel20)
                            .addGap(1, 1, 1)
                            .addComponent(qtytext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(unitrateText1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel60)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalamounttext2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                            .addComponent(jLabel66)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(bonusqtytext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(altotalvattext1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                            .addComponent(jLabel68)
                            .addGap(1, 1, 1)
                            .addComponent(unibox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel69)
                                .addComponent(jLabel63))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(totaldiscounttext2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                            .addComponent(jLabel65)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(netotaltext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(86, 86, 86))
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        returndataTable.setAutoCreateRowSorter(true);
        returndataTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        returndataTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        returndataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL.No", "Code", "Descripion", "Batch", "Exp Date", "Box Size", "MRP", "Discount", "Vat", "Qty", "Amount", "Total Discount", "Total Vat", "Net Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        returndataTable.setGridColor(new java.awt.Color(204, 204, 204));
        returndataTable.setRowHeight(25);
        returndataTable.setShowHorizontalLines(true);
        returndataTable.setShowVerticalLines(true);
        returndataTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returndataTableMouseClicked(evt);
            }
        });
        returndataTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                returndataTableKeyPressed(evt);
            }
        });
        jScrollPane10.setViewportView(returndataTable);
        if (returndataTable.getColumnModel().getColumnCount() > 0) {
            returndataTable.getColumnModel().getColumn(0).setResizable(false);
            returndataTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            returndataTable.getColumnModel().getColumn(1).setResizable(false);
            returndataTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            returndataTable.getColumnModel().getColumn(2).setResizable(false);
            returndataTable.getColumnModel().getColumn(2).setPreferredWidth(300);
            returndataTable.getColumnModel().getColumn(3).setResizable(false);
            returndataTable.getColumnModel().getColumn(4).setResizable(false);
            returndataTable.getColumnModel().getColumn(4).setPreferredWidth(100);
            returndataTable.getColumnModel().getColumn(5).setResizable(false);
            returndataTable.getColumnModel().getColumn(6).setResizable(false);
            returndataTable.getColumnModel().getColumn(7).setResizable(false);
            returndataTable.getColumnModel().getColumn(8).setResizable(false);
            returndataTable.getColumnModel().getColumn(9).setResizable(false);
            returndataTable.getColumnModel().getColumn(10).setResizable(false);
            returndataTable.getColumnModel().getColumn(11).setResizable(false);
            returndataTable.getColumnModel().getColumn(12).setResizable(false);
            returndataTable.getColumnModel().getColumn(13).setResizable(false);
        }

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 1751, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jScrollPane7.setViewportView(jPanel20);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel266Layout = new javax.swing.GroupLayout(jPanel266);
        jPanel266.setLayout(jPanel266Layout);
        jPanel266Layout.setHorizontalGroup(
            jPanel266Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel266Layout.setVerticalGroup(
            jPanel266Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel25, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Sale Return", jPanel266);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    private void executebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executebtnActionPerformed

        //  if (nettotal > 0 && paidamount >= chasin) {
        if (saleupdate == 0) {
            /* if (netdiscount.getText().isEmpty()) {
                discountfinel = total_discount_amount();
                netdiscount.setText(df2.format(discountfinel));
            }
             */
            inputDate();
            parchase_code();
            DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
            if (model2.getRowCount() > 0) {
                int p = JOptionPane.showConfirmDialog(null, "Do you really want to Save This Credit Invoice", "Invoice Exexcution", JOptionPane.YES_NO_OPTION);
                if (p == 0) {
                    if (!"0".equals(cusId) && mpobox.getSelectedIndex() > 0 && territorybox.getSelectedIndex() > 0) {

                        creditCustomerAcount();
                        //invoicecheck();
                        invoicecheckcredit();

                    } else {
                        JOptionPane.showMessageDialog(null, "Please Select All Requirment Information");

                    }
                }

            }

            // tablenobox.setSelectedIndex(0);
            itemnamesearch.requestFocusInWindow();
        }

    }//GEN-LAST:event_executebtnActionPerformed

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
                para.put("tableNo", tablenobox.getSelectedItem());
                para.put("total", total);
                para.put("vat", vat);
                para.put("netTotal", netTotal);
                para.put("discount", netdiscount.getText());
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
            printinvoice = new PrintInvoice();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(CreditPos.class.getName()).log(Level.SEVERE, null, ex);
        }
        printinvoice.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed
    private void PrintOrder(int orderno) {
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Order", "Kitchen Order", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
            if (model2.getRowCount() == 0) {

                JOptionPane.showMessageDialog(null, "Data Table Is Empty");
            } else {

                try {

                    String type = null;
                    if (orderupdate == 0) {
                        type = "Order";
                    } else {
                        type = "Order Update";

                    }
                    String ordersNo = String.format("%d", orderno);
                    String total = totalrate.getText();
                    String vat = totalvattext.getText();
                    String netTotal = totalinvoiceamount.getText();

                    JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                    JasperDesign jd = JRXmlLoader.load(new File("")
                            .getAbsolutePath()
                            + "/src/Report/Bill.jrxml");

                    Map<String, Object> para = new HashMap<>();
                    // HashMap para = new HashMap();
                    para.put("ItemDataSource", ItemJRBean);
                    para.put("invoiceno", ordersNo);
                    //para.put("tableNo", tablenobox.getSelectedItem());
                    para.put("total", total);
                    para.put("vat", vat);
                    // para.put("discount", netdiscount.getText());
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
        if (model2.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Please Select Table No Or Table Data Is Empty");
        } else {
            if (orderupdate == 0) {
                //order Insert
                OderInsert();
            } else {
                try {
                    //Order Update

                    orderUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            AfterExecute();

        }

    }//GEN-LAST:event_orderbtnActionPerformed
    private void orderview() {
        tree = 0;
        try {
            DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
            model2.setRowCount(0);
            // int slNo = 0;
            String sql = "Select barcode ,(select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item', sl.unitrate as 'UnitRate', sl.qty as 'Qty', sl.discount as 'Discount', sl.totalamount as 'Amount', sl.Totalvat as 'TotalVat',sl.NetTotal  as 'NetTotal' from orderdetails sl where sl.orderNo='" + orderNobox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            // datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String probarcode = rs.getString("barcode");
                String Item = rs.getString("Item");
                float UnitRate = rs.getFloat("UnitRate");
                float Qty = rs.getFloat("Qty");
                //String Unit = rs.getString("Unit");
                float Discount = rs.getFloat("Discount");
                // float Amount = rs.getFloat("Amount");
                //float Vat = rs.getFloat("Vat");
                String type = "";
                float TotalVat = rs.getFloat("TotalVat");
                float NetTotal = rs.getFloat("NetTotal");
                tree++;
                model2.addRow(new Object[]{tree, probarcode, Item, UnitRate, Qty, Discount, NetTotal});

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        try {
            String sql = "Select * from saleorder where id='" + orderNobox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            // datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                cusId = rs.getString("customerid");
                String type = rs.getString("type");

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        totalqtytext.setText(df3.format(total_qty()));
        double inputotalrate = total_action_mrp();
        totalrate.setText(df2.format(inputotalrate));
        double inputvat = total_action_vat();
        totalvattext.setText(df2.format(inputvat));
        double input = total_invoice_amount();
        totalinvoiceamount.setText(df2.format(input));
        netdiscount.setText(Float.toString(total_discount_amount()));
        discountfinel = total_discount_amount();
        bookreturntext.setText(df2.format(bookReturn));
        double nettotalwbr = input - bookReturn;
        nettotaltext.setText(df2.format(nettotalwbr));

        parchase_code();
        customerCheck();

    }
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        int p = JOptionPane.showConfirmDialog(null, "Do you Sure Clear All Type Of Order And Data Table", "Clear All Operation", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            clear();
            AfterExecute();

        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void voidbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_voidbtnActionPerformed
        if (saleupdate > 0) {
            if (previceinvoicebox.getSelectedIndex() > 0) {
                int p = JOptionPane.showConfirmDialog(null, "Do you really want to Void This Invoice", "Void Exexcution", JOptionPane.YES_NO_OPTION);
                if (p == 0) {
                    try {
                        CancaleInvoice();
                    } catch (SQLException | IOException | JRException ex) {
                        Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }//GEN-LAST:event_voidbtnActionPerformed

    private void netdiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_netdiscountKeyPressed
        /*     if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            if (totalinvoiceamount.getText().isEmpty() || netdiscount.getText().isEmpty()) {
                //  changetext.setText(null);

            } else {
                float discount = Float.parseFloat(netdiscount.getText());
                double amount = total_action_mrp();

                // totalvattextreturn.setText(df2.format(inputvat));
                discountfinel = 0;
                if (discountboxtypefinel.getSelectedIndex() == 0) {
                    float amountdis = discount / 100;
                    discountfinel = (float) (amount * amountdis);

                } else {

                    discountfinel = discount;

                }

                if (amount < discountfinel) {
                    // changetext.setText(null);
                    JOptionPane.showMessageDialog(null, "Discount Amount is Bigger than Invoice Amount");
                    double toalinvoce = total_invoice_amount();
                    totalinvoiceamount.setText(String.format("%.2f", toalinvoce));
                    double inputvat = total_action_vat();
                    totalvattext.setText(df2.format(inputvat));
                    netdiscount.setText(Float.toString(total_discount_amount()));
                    discountfinel = total_discount_amount();
                    double nettotalwbr = toalinvoce - bookReturn;
                    nettotaltext.setText(df2.format(nettotalwbr));

                } else {

                    double change = amount - discountfinel;
                    //double inputvat = total_action_vat();
                    double totalvat = change * 0.05;
                    double totalamount = change + totalvat;
                    //  String gpper = String.format("%.2f", productvat);
                    totalvattext.setText(df2.format(totalvat));
                    totalinvoiceamount.setBackground(Color.YELLOW);
                    totalinvoiceamount.setText(String.format("%.2f", totalamount));
                    double nettotalwbr = totalamount - bookReturn;
                    nettotaltext.setText(df2.format(nettotalwbr));

                }

            }

        }
         */
    }//GEN-LAST:event_netdiscountKeyPressed
    /*
    private void NormalItem(String bcode) {
        try {

            String sql = "Select bp.itemcode as 'itemcode' ,bp.barcode as 'barcode',bp.nameformat as 'nameformat',bp.mrp as 'mrp',ita.itemName as 'itemName',ita.vat as 'vat' FROM item ita inner join barcodeproduct bp ON ita.itemcode=bp.itemcode where bp.barcode='" + bcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                barcode = rs.getString("barcode");
                itemname = rs.getString("itemName");
                mrp = rs.getString("mrp");

                String nameformat = rs.getString("nameformat");
                String nameformats = barcode + "-" + nameformat;
                itemnamesearch.setSelectedItem(nameformats);
                unitrateText.setText(mrp);
                includevat = rs.getFloat("vat");
                qtytext.setText("1");
                if (directstore.isSelected() == true) {
                    CheckEntryFilter();
                } else {
                    qtytext.requestFocusInWindow();
                }

                codetext.requestFocusInWindow();
            } else {

                itemnamesearch.setSelectedIndex(0);
                unitrateText.setText(null);
                discounttext.setText(null);
                qtytext.setText(null);
                codetext.requestFocusInWindow();
                codetext.requestFocusInWindow();

            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, ex);
        }

    }

    private void OfferItem(String bcode) {
        try {

            String sql = "Select bp.procode as 'itemcode' ,bp.barcode as 'barcode',bp.discountype as 'discountype',bp.discount as 'discount',(select mrp from barcodeproduct brp where brp.barcode=bp.barcode) as 'unitrate',(select nameformat from barcodeproduct brp where brp.barcode=bp.barcode) as 'nameformat',bp.offerprice as 'mrp',ita.itemName as 'itemName',ita.vat as 'vat' FROM item ita inner join offerdetails bp ON ita.itemcode=bp.procode where bp.barcode='" + bcode + "' AND offerid='" + offerid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                //itemcode=rs.getString("itemcode");
                barcode = rs.getString("barcode");
                itemname = rs.getString("itemName");
                mrp = rs.getString("unitrate");
                String nameformat = rs.getString("nameformat");
                String namefind = barcode + "-" + nameformat;
                itemnamesearch.setSelectedItem(namefind);
                unitrateText.setText(mrp);
                float unitr = rs.getFloat("unitrate");
                float offerprice = rs.getFloat("mrp");
                String disctype = rs.getString("discountype");
                String discount = rs.getString("discount");
                discounttext.setText(discount);
                if ("Parsantage".equals(disctype)) {
                    discounttypebox.setSelectedIndex(0);

                } else {

                    discounttypebox.setSelectedIndex(1);
                }

                includevat = rs.getFloat("vat");
                qtytext.setText("1");

                if (directstore.isSelected() == true) {
                    CheckEntryFilter();
                } else {
                    qtytext.requestFocusInWindow();
                }
                codetext.requestFocusInWindow();
            } else {

                NormalItem(bcode);

            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, ex);
        }

    }
    
     */

    private void NormalItem(String itcode) {

        // final JTextField productname = (JTextField) itemnamesearch.getEditor().getEditorComponent();
        /// String SearchText = productname.getText();
        try {
            String sql = "Select "
                    + "itemcode,"
                    + "itemName,"
                    + "openingDate,"
                    + "category,"
                    + "generic_id,"
                    + "(Select generic_name from generic sinfo where sinfo.generic_id=it.generic_id) as 'genericname',"
                    + "company_id,"
                    + "(Select supliyername from suplyierinfo sinfo where sinfo.id=it.company_id) as 'companyname',"
                    + "brand_id,"
                    + "(Select brand_name from brand sinfo where sinfo.id=it.brand_id) as 'brandname',"
                    + "dos_id,"
                    + "(Select ShortName from medicinforms sinfo where sinfo.FormId=it.dos_id) as 'fromname',"
                    + "strangth,"
                    + "boxSize,"
                    + "expdate,"
                    + "pvat,"
                    + "mrp,"
                    + "sdiscount,"
                    + "smrpwv,"
                    + "pmrpwd,"
                    + "profite,"
                    + "profiteParcentage,"
                    + "stockunit,"
                    + "batch,"
                    + "stockunit,"
                    + "(select unitshort  from unit un where un.id=it.stockunit) as 'unitname'"
                    + "from item it where it.Itemcode='" + itcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {

                //  barcode = rs.getString("barcode");
                itemcode = rs.getString("Itemcode");
                itemname = rs.getString("itemName");
                String genericname = rs.getString("genericname");
                genericbox.setSelectedItem(genericname);
                mrp = rs.getString("mrp");
                itemnamesearch.setSelectedItem(itemname);
                unitrateText.setText(mrp);
                String strangth = rs.getString("strangth");
                strengthtext.setText(strangth);
                String boxSize = rs.getString("boxSize");
                boxsizetext.setText(boxSize);
                String expdates = rs.getString("expdate");
                expdate.setText(expdates);

                //  expdatetext.setText(expdate);
                String unitname = rs.getString("unitname");
                unibox.setSelectedItem(unitname);
                String batch = rs.getString("batch");
                batchtext.setText(batch);
                String pvat = rs.getString("pvat");
                pvattext.setText(pvat);
                String sdiscount = rs.getString("sdiscount");
                discounttext.setText(sdiscount);
                String smrpwv = rs.getString("smrpwv");
                ptotaltptext.setText(smrpwv);
                String profite = rs.getString("profite");
                //   profitetext.setText(profite);
                String profiteParcentage = rs.getString("profiteParcentage");
                //profiteparcentagetext.setText(profiteParcentage);
                float vat = rs.getFloat("pvat");
                float mrp = rs.getFloat("mrp");
                float vatper = vatPercentage(vat, mrp);
                pvatpertext.setText(String.valueOf(Math.round(vatper)));
                stocktext.setText(GetStrock(itemcode));
                qtytext.requestFocusInWindow();
                // qtytext.setText("1");
                if (directstore.isSelected() == true) {
                    CheckEntryFilter();
                } else {
                    qtytext.requestFocusInWindow();
                }
                qtytext.requestFocusInWindow();
            } else {

                final JTextField textfield = (JTextField) itemnamesearch.getEditor().getEditorComponent();
                textfield.setText(null);
                unitrateText.setText(null);
                discounttext.setText(null);
                qtytext.setText(null);
                itemnamesearch.requestFocusInWindow();

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private String GetStrock(String itemcode) {
        try {
            String sql = "Select qty from stockdetails where itemcode='" + itemcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String stock = rs.getString("qty");
                return stock;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return null;
    }

    private void OfferItem(String bcode) {
        try {

            String sql = "Select bp.procode as 'itemcode' ,bp.barcode as 'barcode',bp.discountype as 'discountype',bp.discount as 'discount',ita.mrp as 'unitrate',bp.offerprice as 'mrp',ita.itemName as 'itemName',ita.vat as 'vat' FROM item ita inner join offerdetails bp ON ita.itemcode=bp.procode where bp.procode='" + bcode + "' AND offerid='" + offerid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                //itemcode=rs.getString("itemcode");
                barcode = rs.getString("barcode");
                itemcode = rs.getString("itemcode");
                itemname = rs.getString("itemName");
                mrp = rs.getString("unitrate");
                itemnamesearch.setSelectedItem(itemname);
                unitrateText.setText(mrp);
                float unitr = rs.getFloat("unitrate");
                float offerprice = rs.getFloat("mrp");

                String discount = rs.getString("discount");
                discounttext.setText(discount);

                includevat = rs.getFloat("vat");
                qtytext.setText("1");

                if (directstore.isSelected() == true) {
                    CheckEntryFilter();
                } else {
                    qtytext.requestFocusInWindow();
                }
                qtytext.requestFocusInWindow();
            } else {

                NormalItem(bcode);

            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, ex);
        }

    }

    private void NormalItemCodeRealesd(String itcode) {
        try {

            String sql = "Select * from item where Itemcode='" + itcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                barcode = rs.getString("barcode");
                itemcode = rs.getString("Itemcode");
                itemname = rs.getString("itemName");
                mrp = rs.getString("mrp");
                itemnamesearch.setSelectedItem(itemname);
                unitrateText.setText(mrp);
                includevat = rs.getFloat("vat");
                qtytext.setText("1");

            } else {
                final JTextField textfield = (JTextField) itemnamesearch.getEditor().getEditorComponent();
                textfield.setText(null);
                unitrateText.setText(null);
                discounttext.setText(null);
                qtytext.setText(null);
                itemnamesearch.requestFocusInWindow();

            }

        } catch (SQLException ex) {
            //  JOptionPane.showMessageDialog(null, ex);
        }

    }
    private void unitrateTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateTextKeyPressed

        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();

        }
    }//GEN-LAST:event_unitrateTextKeyPressed

    private void qtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            if (unitrateText.getText().isEmpty() || qtytext.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
                qtytext.setForeground(Color.red);
                // qtytext.setToolTipText("Please Fillup Code box OR Item name Or qty Or Unit Fields");

            } else {
                qtytext.setForeground(Color.black);
                parchase_code();
                CheckEntryFilter();
                // try {

                //   CheckEntryFilter();
                //  checkentry();
                //  } catch (NumberFormatException e) {
                //  JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Quantity Validation", JOptionPane.CANCEL_OPTION);
                //      qtytext.setText(null);
                //  }
                itemnamesearch.requestFocusInWindow();

            }

        }
    }//GEN-LAST:event_qtytextKeyPressed

    private void qtytextqtytextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextqtytextKeyReleased
        TotalAction();
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

        if (unitrateText.getText().isEmpty() || qtytext.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
        } else {
            //   qtytext.setForeground(Color.black);
            parchase_code();
            CheckEntryFilter();
            // try {

            //   CheckEntryFilter();
            //  checkentry();
            //  } catch (NumberFormatException e) {
            //  JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Quantity Validation", JOptionPane.CANCEL_OPTION);
            //      qtytext.setText(null);
            //  }
            itemnamesearch.requestFocusInWindow();

        }

    }//GEN-LAST:event_addbtnjButton12ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        clear();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void orderNoboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_orderNoboxPopupMenuWillBecomeInvisible
        if (orderNobox.getSelectedIndex() == 0) {
            if (hold == 1) {
                AfterExecute();
            }
        } else {
            orderview();
            orderupdate = 1;
            orderbtn.setText("Update Hold Order");
            itemnamesearch.requestFocusInWindow();
            hold = 1;

        }
    }//GEN-LAST:event_orderNoboxPopupMenuWillBecomeInvisible
    public static int checkSum(String Input) {
        int evens = 0; //initialize evens variable
        int odds = 0; //initialize odds variable
        int checkSum = 0; //initialize the checkSum
        for (int i = 0; i < Input.length(); i++) {
            //check if number is odd or even
            if ((int) Input.charAt(i) % 2 == 0) { // check that the character at position "i" is divisible by 2 which means it's even
                evens += (int) Input.charAt(i);// then add it to the evens
            } else {
                odds += (int) Input.charAt(i); // else add it to the odds
            }
        }
        odds = odds * 3; //multiply odds by three
        int total = odds + evens; //sum odds and evens
        if (total % 10 == 0) { //if total is divisible by ten, special case
            checkSum = 0;//checksum is zero
        } else { //total is not divisible by ten
            checkSum = 10 - (total % 10); //subtract the ones digit from 10 to find the checksum
        }
        return checkSum;
    }

    private void customerid() throws SQLException {
        try {

            int new_inv = 1;
            String NewParchaseCode = (+new_inv + "");
            String sql = "Select customerid from customerinfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                int add1 = rs.getInt("customerid");
                //invoc_text.setText(add1);
                // int inv = Integer.parseInt(add1);
                new_inv = (add1 + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = (ParchaseCode);
            }
            // String finelcode = NewParchaseCode + Itemcodedate;
            String finelcode = NewParchaseCode;
            String StartCode = "1000";
            newcustomeridtext.setText(finelcode);
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        try {
            customerid();
        } catch (SQLException ex) {
            Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (snametext.getText().isEmpty() || contactnotext.getText().isEmpty()) {

        } else {
            try {
                String sql = "Insert into customerinfo("
                        + "customername,"
                        + "customerid,"
                        + "address,"
                        + "status,"
                        + "customerType,"
                        + "OpeningDate,"
                        + "ContactNo,"
                        + "inputuser) values(?,?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, snametext.getText());
                pst.setString(2, newcustomeridtext.getText());
                pst.setString(3, addresstext.getText());
                pst.setInt(4, 1);
                pst.setString(5, "Credit And Cash");
                pst.setDate(6, date);
                pst.setString(7, contactnotext.getText());
                pst.setInt(8, userid);
                pst.execute();

                // customerbox.removeAllItems();
                //  customerbox.addItem("Select");
                //  customerbox.setSelectedItem("Select");
                // customer();
                snametext.setText(null);
                addresstext.setText(null);
                contactnotext.setText(null);
                msgtext.setText("Successfully Save");

                // JOptionPane.showMessageDialog(null, "Data Saved");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            try {
                customerid();
            } catch (SQLException ex) {
                Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton13ActionPerformed

    private void openingbalancetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openingbalancetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_openingbalancetextActionPerformed

    private void customeridtextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customeridtextKeyTyped

    }//GEN-LAST:event_customeridtextKeyTyped

    private void customeridtextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customeridtextKeyPressed

    }//GEN-LAST:event_customeridtextKeyPressed

    private void customerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_customerboxPopupMenuWillBecomeInvisible
        String Customername = (String) customerbox.getSelectedItem();
        try {
            String sql = "Select * from customerInfo  where customername='" + Customername + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {

                cusId = rs.getString("customerid");
                customeridtext.setText(cusId);
                customeridtextsecond.setText(cusId);
                String Name = rs.getString("customername");
                openingbalance = rs.getFloat("OpenigBalance");
                openingbalancetext.setText(String.format("%s", openingbalance));
                dipositamt = rs.getFloat("DipositAmt");
                creditAmnt = rs.getFloat("creditAmnt");
                creditamounttext.setText(String.format("%s", creditAmnt));
                cashamt = rs.getFloat("cashamt");
                cashamounttext.setText(String.format("%s", cashamt));
                salesamt = rs.getFloat("saleamount");
                totalsaletext.setText(String.format("%s", salesamt));
                paidamt = rs.getFloat("paidamount");
                totalpaymenttext.setText(String.format("%s", paidamt));
                balancedue = rs.getFloat("Balancedue");
                totaldiscount = rs.getFloat("totaldiscount");
                totaldiscounttext.setText(String.format("%s", totaldiscount));
                String trans = String.format("%s", balancedue);
                balanceduetext.setText(trans);
                //parchase_code();
                customernameactive();

                itemnamesearch.requestFocusInWindow();
            } else {
                // customerbox.setSelectedIndex(0);
                cusId = "0";
                openingbalancetext.setText(null);
                creditamounttext.setText(null);
                cashamounttext.setText(null);
                totalsaletext.setText(null);
                totalpaymenttext.setText(null);
                totaldiscounttext.setText(null);
                balanceduetext.setText(null);
                customeridtext.setText(null);
            }
        } catch (SQLException e) {
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
        int rows = dataTable.getRowCount();
        if (rows > 0) {
            creditCustomerAcount();

        }

        entrucesure();
    }//GEN-LAST:event_customerboxPopupMenuWillBecomeInvisible

    private void descrptiontblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_descrptiontblMouseClicked
        offerCheck();
        DefaultTableModel model = (DefaultTableModel) descrptiontbl.getModel();

        int selectedRowIndex = descrptiontbl.getSelectedRow();

        itemcode = (model.getValueAt(selectedRowIndex, 1).toString());
        //descriptionItemEntry();

        //itempane.setSelectedIndex(0);
        if (offer == 1) {

            OfferItem(itemcode);

        } else {

            NormalItem(itemcode);

        }
        model.setNumRows(0);
        parchase_code();


    }//GEN-LAST:event_descrptiontblMouseClicked

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        updatekey = 1;
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int selectedRowIndex = dataTable.getSelectedRow();

        updatekey = 1;
        itemcode = (model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        batchtext.setText(model.getValueAt(selectedRowIndex, 3).toString());
        expdate.setText(model.getValueAt(selectedRowIndex, 4).toString());
        boxsizetext.setText((model.getValueAt(selectedRowIndex, 5).toString()));
        unitrateText.setText(model.getValueAt(selectedRowIndex, 6).toString());
        discounttext.setText(model.getValueAt(selectedRowIndex, 7).toString());
        pvattext.setText(model.getValueAt(selectedRowIndex, 8).toString());
        float tp = Float.parseFloat((model.getValueAt(selectedRowIndex, 6).toString()));
        float vat = Float.parseFloat((model.getValueAt(selectedRowIndex, 8).toString()));
        float vatper = vatPercentage(vat, tp);
        pvatpertext.setText(df2.format(vatper));
        float totalvat = tp + vat;
        ptotaltptext.setText(df2.format(totalvat));
        qtytext.setText(model.getValueAt(selectedRowIndex, 9).toString());
        bonusqtytext.setText(model.getValueAt(selectedRowIndex, 10).toString());
        totalamounttext.setText(model.getValueAt(selectedRowIndex, 11).toString());
        totaldiscounttext.setText(model.getValueAt(selectedRowIndex, 12).toString());
        altotalvattext.setText(model.getValueAt(selectedRowIndex, 13).toString());
        netotaltext.setText(model.getValueAt(selectedRowIndex, 14).toString());
        stocktext.setText(GetStrock(itemcode));
        //  unibox.setSelectedItem(model.getValueAt(selectedRowIndex, 6).toString());
        qtytext.setBackground(Color.YELLOW);
        try {
            String sql = "Select "
                    + "(Select generic_name from generic sinfo where sinfo.generic_id=it.generic_id) as 'genericname',"
                    + "strangth"
                    + " from item it where it.Itemcode='" + itemcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String genericname = rs.getString("genericname");
                genericbox.setSelectedItem(genericname);
                String strangth = rs.getString("strangth");
                strengthtext.setText(strangth);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        //  discountamt = df2.format(indiscount);
    }//GEN-LAST:event_dataTableMouseClicked

    private void returndataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returndataTableMouseClicked
        /*
        DefaultTableModel model = (DefaultTableModel) returndataTable.getModel();
        int selectedRowIndex = returndataTable.getSelectedRow();

        codetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        unitrateText.setText(model.getValueAt(selectedRowIndex, 3).toString());
        qtytext.setText(model.getValueAt(selectedRowIndex, 4).toString());
        //  discounttext.setText(model.getValueAt(selectedRowIndex, 6).toString());
        //colorbox.setSelectedItem(model.getValueAt(selectedRowIndex, 6).toString());
        float itemrate = Float.parseFloat((model.getValueAt(selectedRowIndex, 3).toString()));
        float qty = Float.parseFloat((model.getValueAt(selectedRowIndex, 4).toString()));
        float discount = Float.parseFloat((model.getValueAt(selectedRowIndex, 5).toString()));
        float totalvat = Float.parseFloat((model.getValueAt(selectedRowIndex, 8).toString()));
        float indiscount = discount / qty;
        includevat = (totalvat / qty);
        discounttext.setText(df2.format(indiscount));
        updatekey = 3;
         */
        //updateSystemProcess();
    }//GEN-LAST:event_returndataTableMouseClicked

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed

        if (evt.getKeyCode() != KeyEvent.VK_DELETE) {

        } else {
            deleterow();
        }

    }//GEN-LAST:event_dataTableKeyPressed
    private void cashInDrwaerBooking(float cashin) {
        Float balanc = bankbalance1 + cashin;
        try {

            String sql = "Insert into CashDrower(cashin,cashout,balance,type,upatedate,inputuser,tranid) values(?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setFloat(1, cashin);
            pst.setFloat(2, 0);
            pst.setFloat(3, balanc);
            pst.setString(4, "Order Item");
            pst.setDate(5, date);
            pst.setInt(6, userid);
            pst.setString(7, invoicetext.getText());
            pst.execute();

            //  JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void DatatableTotalAction() {
        //total calculation
        double inputotalrate = total_action_mrp();
        totalrate.setText(df2.format(inputotalrate));

        double inputvat = total_action_vat();
        totalvattext.setText(df2.format(inputvat));

        double input = total_invoice_amount();
        totalinvoiceamount.setText(df2.format(input));

        netdiscount.setText(Float.toString(total_discount_amount()));
        discountfinel = total_discount_amount();
        // dataTable.setRowSelectionInterval(0,i);
        bookreturntext.setText(df2.format(bookReturn));
        double nettotalwbr = input - bookReturn;
        nettotaltext.setText(df2.format(nettotalwbr));

        creditCustomerAcount();

    }
    private void adjustbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adjustbtnActionPerformed
        returnClear();
        /*        int rows = returndataTable.getRowCount();
        int datatablerows = dataTable.getRowCount();
        if (datatablerows == 0 || rows == 0 || adjustbtn.getText().isEmpty() || netdiscountreturn.getText().isEmpty() || totalvattextreturn.getText().isEmpty() || totalinvoiceamountreturn.getText().isEmpty()) {

        } else {

            float returnamount = Float.parseFloat(totalinvoiceamountreturn.getText());
            float invoiceaount = Float.parseFloat(totalinvoiceamount.getText());

            if (returnamount > invoiceaount) {
                JOptionPane.showMessageDialog(null, "Return Amount More Than Invoice Amount");
            } else {
                switch (bookReturnStatus) {
                    case 0:
                        bookReturn = Float.parseFloat(totalinvoiceamountreturn.getText());
                        bookReturnStatus = 2;
                        typebox.setSelectedIndex(0);
                        parchase_code();
                        bookreturnstatus();
                        DatatableTotalAction();
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, "This Order Already Placement in Invoice");
                        //typebox.setSelectedIndex(0);
                        //parchase_code();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Return Already Adjust in Invoice");
                        break;
                }
            }
        }
         */
    }//GEN-LAST:event_adjustbtnActionPerformed
    private void returnCode() {
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

            invoicetext1.setText(NewParchaseCode);
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void returnexecutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnexecutionActionPerformed
        int rows = returndataTable.getRowCount();
      //  inputDate();
       returnCode();
      
        if (rows == 0 || adjustbtn.getText().isEmpty() || netdiscountreturn.getText().isEmpty() || totalvattextreturn.getText().isEmpty() || totalinvoiceamountreturn.getText().isEmpty()) {

        } else {
            int p = JOptionPane.showConfirmDialog(null, "Do you really want to Return This Items", "Return Exexcution", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                /*
                try {
                    balance();
                } catch (SQLException ex) {
                    Logger.getLogger(CashInvoice.class.getName()).log(Level.SEVERE, null, ex);
                }
                 */
                salereturnInsert();
                try {
                    printInvoice();
                } catch (JRException ex) {
                    Logger.getLogger(InvoiceTest.class.getName()).log(Level.SEVERE, null, ex);
                }

                returnClear();
            }

        }
    }//GEN-LAST:event_returnexecutionActionPerformed

    private void itempaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itempaneMouseClicked

    }//GEN-LAST:event_itempaneMouseClicked

    private void customeridtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customeridtextKeyReleased
        cusId = customeridtext.getText();
        customerCheck();
        // cusId="1";
    }//GEN-LAST:event_customeridtextKeyReleased

    private void creditamounttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditamounttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_creditamounttextActionPerformed

    private void cashamounttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashamounttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cashamounttextActionPerformed

    private void totalsaletextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalsaletextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalsaletextActionPerformed

    private void totaldiscounttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totaldiscounttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totaldiscounttextActionPerformed

    private void totalpaymenttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalpaymenttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalpaymenttextActionPerformed

    private void balanceduetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_balanceduetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_balanceduetextActionPerformed

    private void returndataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_returndataTableKeyPressed

        if (evt.getKeyCode() != KeyEvent.VK_DELETE) {

        } else {
            deleterow_return();
        }

    }//GEN-LAST:event_returndataTableKeyPressed

    private void searchtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchtextKeyReleased
        DefaultTableModel model2 = (DefaultTableModel) descrptiontbl.getModel();
        model2.setNumRows(0);
        tree = 0;
        if (searchtext.getText().isEmpty()) {
            model2.setNumRows(0);

        } else {

            String searctexthere = searchtext.getText();
            String sql = null;
            try {
                // String sql="Select bp.barcode as 'barcode',bp.itemcode as 'itemcode', (select ita.itemname from item ita where ita.itemcode=bp.itemcode) as 'itemname' ,bp.color as 'color',bp.size as 'size',bp.mrp as 'mrp' from barcodeproduct bp where bp.itemcode LIKE '%"+searctexthere+"%' OR  bp.barcode LIKE '%"+searctexthere+"%' OR bp.nameformat LIKE '%"+searctexthere+"%' ";
                switch (searchtypebox.getSelectedIndex()) {
                    case 0:
                        sql = "Select bp.barcode as 'barcode',bp.itemcode as 'itemcode',bp.itemName as 'itemname' ,bp.mrp as 'mrp',(select Qty from stockdetails st where bp.Itemcode=st.itemcode) as 'stock' from item bp where bp.Itemcode LIKE '%" + searctexthere + "%' ";
                        break;
                    case 1:
                        sql = "Select bp.barcode as 'barcode',bp.itemcode as 'itemcode',bp.itemName as 'itemname' ,bp.mrp as 'mrp',(select Qty from stockdetails st where bp.Itemcode=st.itemcode) as 'stock' from item bp where bp.barcode LIKE '%" + searctexthere + "%' ";
                        break;
                    default:
                        sql = "Select bp.barcode as 'barcode',bp.itemcode as 'itemcode',bp.itemName as 'itemname' ,bp.mrp as 'mrp',(select Qty from stockdetails st where bp.Itemcode=st.itemcode) as 'stock' from item bp where bp.itemName LIKE '%" + searctexthere + "%' ";
                        break;
                }

                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    String itembarcode = rs.getString("barcode");
                    String itemcodes = rs.getString("itemcode");
                    String itemnam = rs.getString("itemname");
                    // String itemcolor=rs.getString("color");
                    //  String itemsize=rs.getString("size");
                    String itemmrp = rs.getString("mrp");
                    String stock = rs.getString("stock");
                    tree++;

                    model2.addRow(new Object[]{tree, itemcodes, itembarcode, itemnam, itemmrp, stock});

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_searchtextKeyReleased

    private void newcustomeridtextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newcustomeridtextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newcustomeridtextActionPerformed

    private void customeridtextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customeridtextMouseClicked
        customeridtext.setText(null);
        customerbox.removeAllItems();
    }//GEN-LAST:event_customeridtextMouseClicked

    private void searchtypeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_searchtypeboxPopupMenuWillBecomeInvisible
        DefaultTableModel model2 = (DefaultTableModel) descrptiontbl.getModel();
        model2.setNumRows(0);
        searchtext.setText(null);
        searchtext.requestFocusInWindow();
    }//GEN-LAST:event_searchtypeboxPopupMenuWillBecomeInvisible

    private void searchtextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchtextMouseClicked
        DefaultTableModel model2 = (DefaultTableModel) descrptiontbl.getModel();
        model2.setNumRows(0);
        searchtext.setText(null);
    }//GEN-LAST:event_searchtextMouseClicked

    private void netdiscountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_netdiscountMouseClicked
        netdiscount.setText(null);
        double toalinvoce = total_invoice_amount();
        totalinvoiceamount.setText(String.format("%.2f", toalinvoce));
        double inputvat = total_action_vat();
        totalvattext.setText(df2.format(inputvat));
        // netdiscount.setText(Float.toString(total_discount_amount()));
        discountfinel = total_discount_amount();
        double nettotalwbr = toalinvoce - bookReturn;
        nettotaltext.setText(df2.format(nettotalwbr));

    }//GEN-LAST:event_netdiscountMouseClicked

    private void invoicecreditsaleview() {
        tree = 0;

        try {
            DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
            model2.setRowCount(0);
            // int slNo = 0;
            String sql = "Select "
                    + "prcode,"
                    + "(select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item',"
                    + "(select ite.generic_id from item ite where ite.Itemcode=sl.prcode ) as 'genericid',"
                    + "sl.batch as 'batch',"
                    + "sl.expdate as 'expdate',"
                    + "sl.boxsize as 'boxsize',"
                    + "Cast(sl.unitrate as decimal(10,2)) as 'UnitRate',"
                    + "Cast(sl.tp as decimal(10,2)) as 'tp',"
                    + "sl.qty as 'Qty',"
                    + "sl.bonusqty as 'bonusqty',"
                    + "sl.unit as 'Unit',"
                    + "Cast(sl.discount as decimal(10,2)) as 'Discount',"
                    + "Cast(sl.vat as decimal(10,2)) as 'Vat',"
                    + "Cast(sl.totalamount as decimal(10,2)) as 'Amount',"
                    + "Cast(sl.Totalvat as decimal(10,2)) as 'TotalVat',"
                    + "Cast(sl.totaldiscount as decimal(10,2)) as 'Totaldiscount',"
                    + "Cast(sl.NetTotal as decimal(10,2))  as 'NetTotal'"
                    + "from saledetails sl where sl.invoiceNo='" + previceinvoicebox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            // datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String prcode = rs.getString("prcode");
                String Item = rs.getString("Item");
                String genericid = rs.getString("genericid");
                String batch = rs.getString("batch");
                String expdate = rs.getString("expdate");
                String boxsize = rs.getString("boxsize");
                float UnitRate = rs.getFloat("UnitRate");
                float tp = rs.getFloat("tp");
                float Qty = rs.getFloat("Qty");
                float bonusqty = rs.getFloat("bonusqty");
                String Unit = rs.getString("Unit");
                float Discount = rs.getFloat("Discount");
                float Vat = rs.getFloat("Vat");
                float Amount = rs.getFloat("Amount");
                float Totaldiscount = rs.getFloat("Totaldiscount");
                float TotalVat = rs.getFloat("TotalVat");
                float NetTotal = rs.getFloat("NetTotal");
                //   String GenericName = getGeneric(genericid);
                tree++;
                model2.addRow(new Object[]{tree, prcode, Item, batch, expdate, boxsize, UnitRate, Discount, Vat, Qty, bonusqty, Amount, Totaldiscount, TotalVat, NetTotal});

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        try {
            String sql = "Select * from sale where invoiceNo='" + previceinvoicebox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            // datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                cusId = rs.getString("customerid");
                //  String type=rs.getString("type");
                //  typebox.setSelectedItem(type);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        totalqtytext.setText(df3.format(total_qty()));
        double inputotalrate = total_action_mrp();
        totalrate.setText(df2.format(inputotalrate));
        double inputvat = total_action_vat();
        totalvattext.setText(df2.format(inputvat));
        double input = total_invoice_amount();
        nettotaltext.setText(df2.format(input));
        netdiscount.setText(Float.toString(total_discount_amount()));
        discountfinel = total_discount_amount();

        // parchase_code(); 
        customerCheck();

    }

    private void cashInDrwaerCashVoid(float cashout) {

        float balanc = bankbalance1 - cashout;
        try {

            String sql = "Insert into CashDrower(cashin,cashout,balance,type,upatedate,tranid) values(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setDouble(1, 0);
            pst.setDouble(2, cashout);
            pst.setDouble(3, balanc);
            pst.setString(4, "Void Invoice");
            pst.setDate(5, date);
            pst.setString(6, (String) previceinvoicebox.getSelectedItem());
            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void balanceupdateVoid(String accountno, float presentbalnce) {
        try {
            String sql = "Update BankAccount set cashin='" + presentbalnce + "' where accountno='" + accountno + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            // JOptionPane.showMessageDialog(null, "Data Update");
            //   clear();
            totalbalance();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void overallinsertVoid(float presentbalnce, String AccNo, String bank) {
        String trans = invoicetext.getText();
        float cashin = Float.parseFloat(nettotaltext.getText());
        float cashout = 0;
        try {
            String reason = "Sale Invoice";
            String sql = "Insert into bankoverall(inputdate,Description,bank,AccountNo,cashin,cashout,Balance,prasentbalance,totalbalance,remark,transactionid) values(?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setDate(1, date);

            pst.setString(2, reason);
            pst.setString(3, bank);
            pst.setString(4, AccNo);
            pst.setFloat(5, cashin);
            pst.setFloat(6, cashout);
            pst.setFloat(7, balance);
            pst.setFloat(8, presentbalnce);
            pst.setFloat(9, totalb);

            pst.setString(10, "Void Invoice");
            pst.setString(11, trans);

            pst.execute();

            // JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void VoidInsert() throws JRException {

        try {

            float totaltd = totaltrade();
            float totalamt = Float.parseFloat(totalrate.getText());
            float totalprofite = totalamt - totaltd;
            float discount = discountfinel;
            float profite = totalprofite - discount;

            String sql = "Insert into void(invoiceNo,"
                    + "invoicedate,"
                    + "time,"
                    + "paymentCurency,"
                    + "paymentType,"
                    + "netdiscount,"
                    + "TotalAmount,"
                    + "TotalVat,"
                    + "Totalinvoice,"
                    + "customerid,"
                    + "Ref_No,"
                    + "totaltraprice,"
                    + "totalprofite,"
                    + "inputuserid,"
                    + "bank,accNo,"
                    + "cardNo,"
                    + "MobileBankingType,"
                    + "MobileNo,"
                    + "type) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) previceinvoicebox.getSelectedItem());
            pst.setDate(2, date);
            pst.setString(3, time);
            pst.setString(4, "TK");
            pst.setString(5, paymentType);
            pst.setFloat(6, discountfinel);
            pst.setString(7, totalrate.getText());
            pst.setString(8, totalvattext.getText());
            pst.setString(9, nettotaltext.getText());
            pst.setString(10, cusId);
            pst.setString(11, " ");
            pst.setFloat(12, totaltd);
            pst.setFloat(13, profite);
            pst.setInt(14, userid);
            pst.setInt(15, bankid);
            pst.setString(16, "");
            // pst.setString(17, cardnotext.getText());
            if (mobilebank == 1) {
                //    pst.setString(18, (String) mobilebanktype.getSelectedItem());
            } else {
                pst.setString(18, " ");
            }
            // pst.setString(19, bkashtext.getText());
            pst.setString(20, "Invoice");
            pst.execute();
            VoidDetailsInsert();
            VatDeelete();
            StockpdatetVoid();
            customerbalanceupdateVoid();
            ItemForCastDelete();
            DiscountExpDeelete();
            JOptionPane.showMessageDialog(null, "Successfulye Cancel This Invoice");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void VoidDetailsInsert() throws SQLException {

        try {

            int rows = dataTable.getRowCount();

            for (int row = 0; row < rows; row++) {

                String proitemcode = (String) dataTable.getValueAt(row, 1);
                try {
                    String sql = "Select barcode,tp,vat,(select unitshort from unit un where un.id=it.stockunit)as 'unit' from item it where it.Itemcode='" + proitemcode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {
                        barcode = rs.getString("barcode");
                        tpprice = rs.getFloat("tp");
                        unit = rs.getString("unit");
                        includevat = rs.getFloat("vat");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                float rate = (Float) dataTable.getValueAt(row, 3);
                float qty = Float.parseFloat(dataTable.getValueAt(row, 4).toString());
                float discount = (Float) dataTable.getValueAt(row, 5);
                float totalamount = rate * qty;
                float neTotal = (Float) dataTable.getValueAt(row, 6);
                float itemdiscount = discount / qty;
                float mrpdiscount = rate - itemdiscount;
                float itemvat = includevat / 100 * mrpdiscount;
                float totalvat = itemvat * qty;

                try {

                    String sql = "Insert into Voiddetails(invoiceNo,prcode,barcode,unitrate,qty,unit,discount,commesion,totalamount,vat,Totalvat,NetTotal,tradprice,invoicedate,cusid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, (String) previceinvoicebox.getSelectedItem());
                    pst.setString(2, proitemcode);
                    pst.setString(3, barcode);
                    pst.setFloat(4, rate);
                    pst.setFloat(5, qty);
                    pst.setString(6, unit);
                    pst.setFloat(7, discount);
                    pst.setFloat(8, 0);
                    pst.setFloat(9, totalamount);
                    pst.setFloat(10, itemvat);
                    pst.setFloat(11, totalvat);
                    pst.setFloat(12, neTotal);
                    pst.setFloat(13, tpprice);
                    pst.setDate(14, date);
                    pst.setString(15, cusId);
                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);
                }
            }
            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void VatDeelete() {
        try {
            String sql = "Delete from vatcollection where invoiceNopurchaseNo=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) previceinvoicebox.getSelectedItem());
            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void StockpdatetVoid() {
        // int cat=(Integer) categoryBox.getSelectedIndex();
        for (int i = 0; i < dataTable.getRowCount(); i++) {

            String s = dataTable.getValueAt(i, 1).toString().trim();
            float qty = (Float) dataTable.getValueAt(i, 9);
            Stockpdatplus(s, qty);
        }

    }

    private void DiscountExpDeelete() {
        float discount = Float.parseFloat(netdiscount.getText());
        if (discount > 0) {
            try {
                String sql = "Delete from Expencess where trnno=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, (String) previceinvoicebox.getSelectedItem());
                pst.execute();

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }

    }

    private void customerbalanceupdateVoid() throws SQLException {

        float todaysale = Float.parseFloat(nettotaltext.getText());
        float customer_amt = paidamt;
        float aftersaleamt = salesamt - todaysale;

        float aftercreditamt = (float) (creditAmnt - todaysale);

        balanced = balancedue - todaysale;

        String afterdue = df2.format(balanced);
        String creditamt = df2.format(aftercreditamt);
        String saleamt = df2.format(aftersaleamt);

        try {

            String sql = "Update customerInfo set creditAmnt='" + creditamt + "',Balancedue='" + afterdue + "',saleamount='" + saleamt + "' where customerid='" + cusId + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //   JOptionPane.showMessageDialog(null, "Data Update");
            //  statementinsert(balanced);
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void ItemForCastDelete() {
        try {
            String sql = "Delete from itemforcast where invoiceNo=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) previceinvoicebox.getSelectedItem());
            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void CashsaleDelete() {
        try {
            String sql = "Delete from cashsale where invoiceNo=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) previceinvoicebox.getSelectedItem());
            pst.execute();
            CashsaleDetailsDelete();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void CashsaleDetailsDelete() {
        try {
            String sql = "Delete from cashsaledetails where invoiceNo=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) previceinvoicebox.getSelectedItem());
            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void saleDelete() {

        try {
            String sql = "Delete from sale where invoiceNo=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) previceinvoicebox.getSelectedItem());
            pst.execute();
            saleDetailsDelete();
            VatDeelete();
            StockpdatetVoid();
            customerbalanceupdateVoid();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void saleDetailsDelete() {
        try {
            String sql = "Delete from saledetails where invoiceNo=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) previceinvoicebox.getSelectedItem());
            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void CancaleInvoice() throws SQLException, IOException, JRException {
        float cashamts = 0;
        float cardamts = 0;
        float mobileamt = 0;

        try {
            String sql = "Select  recieved from sale WHERE invoiceNo='" + previceinvoicebox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                float recieved = rs.getFloat("recieved");
                if (recieved == 0) {
                    saleDelete();
                } else {
                    JOptionPane.showMessageDialog(null, "Sorry This Invoice Already Paid, You Can not Delete This Invoice");
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        //   VoidInsert();
        AfterExecute();

    }

    private void PanelActiveDeiactive() {
        if (previceinvoicebox.getSelectedIndex() > 0) {
            panelactive();
            //cardamounttext.setEnabled(true);
            //BankinfoboxCard.setEnabled(true);
            // mobilebankamounttext.setEnabled(true);
            // mobilebanktype.setEnabled(true);

            voidbtn.setEnabled(false);
        } else {
            entrucesure();
            //cardamounttext.setEnabled(false);
            //BankinfoboxCard.setEnabled(false);
            //mobilebankamounttext.setEnabled(false);
            //mobilebanktype.setEnabled(false);

            voidbtn.setEnabled(true);
        }
    }


    private void previceinvoiceboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_previceinvoiceboxPopupMenuWillBecomeInvisible
        // PanelActiveDeiactive();
        if (previceinvoicebox.getSelectedIndex() > 0) {
            addbtn.setEnabled(false);
            saleupdate = 1;
            invoicecreditsaleview();

        } else {
            addbtn.setEnabled(true);

            if (saleupdate > 0) {
                DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
                model2.setRowCount(0);
                totalqtytext.setText(null);
                totalrate.setText(null);
                totalvattext.setText(null);
                netdiscount.setText(null);
                nettotaltext.setText(null);

            }

//Customer Box
            customerbox.removeAllItems();
            saleupdate = 0;

        }
    }//GEN-LAST:event_previceinvoiceboxPopupMenuWillBecomeInvisible

    private void netdiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_netdiscountKeyReleased
        if (totalinvoiceamount.getText().isEmpty() || netdiscount.getText().isEmpty()) {
            //  changetext.setText(null);
            double toalinvoce = total_invoice_amount();
            totalinvoiceamount.setText(String.format("%.2f", toalinvoce));
            double inputvat = total_action_vat();
            totalvattext.setText(df2.format(inputvat));
            // netdiscount.setText(Float.toString(total_discount_amount()));
            discountfinel = total_discount_amount();
            double nettotalwbr = toalinvoce - bookReturn;
            nettotaltext.setText(df2.format(nettotalwbr));

        } else {
            float discount = Float.parseFloat(netdiscount.getText());
            double amount = total_action_mrp();

            // totalvattextreturn.setText(df2.format(inputvat));
            discountfinel = 0;
            if (discountboxtypefinel.getSelectedIndex() == 0) {
                float amountdis = discount / 100;
                discountfinel = (float) (amount * amountdis);

            } else {

                discountfinel = discount;

            }

            if (amount < discountfinel) {
                // changetext.setText(null);
                JOptionPane.showMessageDialog(null, "Discount Amount is Bigger than Invoice Amount");
                double toalinvoce = total_invoice_amount();
                totalinvoiceamount.setText(String.format("%.2f", toalinvoce));
                double inputvat = total_action_vat();
                totalvattext.setText(df2.format(inputvat));
                netdiscount.setText(Float.toString(total_discount_amount()));
                discountfinel = total_discount_amount();
                double nettotalwbr = toalinvoce - bookReturn;
                nettotaltext.setText(df2.format(nettotalwbr));

            } else {
                double totalqty = total_qty();
                double tvat = total_action_vat();
                // double pervat = tvat / totalqty;
                double change = (amount + tvat) - discountfinel;
                //double inputvat = total_action_vat();
                //  double totalvat = change * 0;
                //   double totalamount = change + totalvat;
                //  String gpper = String.format("%.2f", productvat);
                ///  totalvattext.setText(df2.format(totalvat));
                totalinvoiceamount.setBackground(Color.YELLOW);
                totalinvoiceamount.setText(String.format("%.2f", change));
                double nettotalwbr = change - bookReturn;
                nettotaltext.setText(df2.format(nettotalwbr));

            }
        }
    }//GEN-LAST:event_netdiscountKeyReleased

    private void dataTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyReleased
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int selectedRowIndex = dataTable.getSelectedRow();
        String getqty = model.getValueAt(selectedRowIndex, 4).toString();
        if (getqty.isEmpty() || "0".equals(getqty)) {
            deleterow();
        } else {
            qty = Float.parseFloat((model.getValueAt(selectedRowIndex, 4).toString()));
            tpds = Float.parseFloat((model.getValueAt(selectedRowIndex, 3).toString()));
            float discount = Float.parseFloat((model.getValueAt(selectedRowIndex, 5).toString()));
            float indiscount = discount / qty;
            discountamt = df2.format(indiscount);
            Table_Update_Element();

        }
    }//GEN-LAST:event_dataTableKeyReleased

    private void searchtextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchtextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchtextActionPerformed

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
        if (saleupdate == 0) {
            //  offerCheck();
            DefaultTableModel model2 = (DefaultTableModel) descrptiontbl.getModel();
            model2.setNumRows(0);
            String Itemnameformat = (String) itemnamesearch.getSelectedItem();
            // final JTextField productname = (JTextField) itemnamesearch.getEditor().getEditorComponent();
            //  String Itemnameformat = productname.getText();
            try {
                String sql = "Select ita.Itemcode as 'itemcode' FROM item ita  where ita.itemName='" + Itemnameformat + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next() == true) {
                    itemcode = rs.getString("itemcode");

                    NormalItem(itemcode);

                } else {

                    unitrateText.setText(null);
                    discounttext.setText(null);
                    qtytext.setText(null);

                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void itemnamesearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyTyped

    }//GEN-LAST:event_itemnamesearchKeyTyped

    private void itemnamesearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_itemnamesearchKeyReleased

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        if (model2.getRowCount() > 0) {
            int p = JOptionPane.showConfirmDialog(null, "Are you sure you want to close this window", "Close Window", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                dispose();
            }
        } else {
            dispose();
        }

    }//GEN-LAST:event_formWindowClosing
    private void ExtractFilterInvoicecheck() {
        final JTextField textfield = (JTextField) invoiceNotext.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        invoiceNotext.removeAllItems();
                        totalamounttext.setText(null);
                        totalVatText.setText(null);
                        netTotalText.setText(null);

                    } else {
                        comboFilterInvoicecheck(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFilterInvoicecheck(String enteredText) {
        invoiceNotext.removeAllItems();
        //  itemnamesearch.addItem("");
        invoiceNotext.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;
        try {

            String sql = "Select DISTINCT invoiceNo from sale WHERE lower(invoiceNo)  LIKE '%" + enteredText + "%' ORDER BY invoiceNo ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("invoiceNo");
                filterArray.add(str1);
                invoiceNotext.addItem(str1);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        if (filterArray.size() > 0) {
            //  itemnamesearch.setModel(new DefaultComboBoxModel(filterArray.toArray()));

            invoiceNotext.setSelectedItem(enteredText);
            invoiceNotext.showPopup();

        } else {

            invoiceNotext.hidePopup();
            totalamounttext.setText(null);
            totalVatText.setText(null);
            netTotalText.setText(null);

        }
    }
    private void invoiceNotextPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_invoiceNotextPopupMenuWillBecomeInvisible

        DefaultTableModel model2 = (DefaultTableModel) returndataTable.getModel();
        model2.setRowCount(0);
        Returnclear();
        String invoiceNo = (String) invoiceNotext.getSelectedItem();
        try {
            String sql = "Select "
                    + "invoiceNo,"
                    + "TotalAmount,"
                    + "TotalVat,"
                    + "Totalinvoice,"
                    + "customerid"
                    + " from sale sl  where sl.invoiceNo='" + invoiceNo + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String inv = rs.getString("invoiceNo");
                ReturnInvoice = rs.getString("invoiceNo");
                String totalam = rs.getString("TotalAmount");
                totalamounttext.setText(totalam);
                String tovat = rs.getString("TotalVat");
                totalVatText.setText(tovat);
                double netinoice = rs.getDouble("Totalinvoice");
                netTotalText.setText(df2.format(netinoice));
                // returndataTable = rs.getString("customerid");
                viewData(inv);
                // customerCheck();

                //   currency=rs.getString("paymentCurency");
            } else {
                totalamounttext.setText(null);
                totalVatText.setText(null);
                netTotalText.setText(null);
                cusId = "";
                customerCheck();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_invoiceNotextPopupMenuWillBecomeInvisible
    private void viewData(String inv) throws SQLException {
        String invoiceNo = inv;
        try {
            DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
            model2.setRowCount(0);
            int tree = 0;
            String sql = "Select "
                    + "prcode,"
                    + "(select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item',"
                    + "sl.batch as 'batch',"
                    + "sl.expdate as 'expdate',"
                    + "sl.boxsize as 'boxsize',"
                    + "Cast(sl.unitrate as decimal(10,2)) as 'UnitRate',"
                    + "Cast(sl.tp as decimal(10,2)) as 'tp',"
                    + "sl.qty as 'Qty',"
                    + "sl.bonusqty as 'bonusqty',"
                    + "sl.unit as 'Unit',"
                    + "Cast(sl.discount as decimal(10,2)) as 'Discount',"
                    + "Cast(sl.vat as decimal(10,2)) as 'Vat',"
                    + "Cast(sl.totalamount as decimal(10,2)) as 'Amount',"
                    + "Cast(sl.Totalvat as decimal(10,2)) as 'TotalVat',"
                    + "Cast(sl.totaldiscount as decimal(10,2)) as 'Totaldiscount',"
                    + "Cast(sl.NetTotal as decimal(10,2))  as 'NetTotal'"
                    + "  from saledetails sl where sl.invoiceNo='" + invoiceNo + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //   datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String prcode = rs.getString("prcode");
                String Item = rs.getString("Item");
                String batch = rs.getString("batch");
                String expdate = rs.getString("expdate");
                String boxsize = rs.getString("boxsize");
                String UnitRate = rs.getString("UnitRate");
                String tp = rs.getString("tp");
                float Qty = rs.getFloat("Qty");
                float bonusqty = rs.getFloat("bonusqty");
                String Unit = rs.getString("Unit");
                String Discount = rs.getString("Discount");
                String Vat = rs.getString("Vat");
                String Amount = rs.getString("Amount");
                String Totaldiscount = rs.getString("Totaldiscount");
                String TotalVat = rs.getString("TotalVat");
                String NetTotal = rs.getString("NetTotal");
                tree++;
                model2.addRow(new Object[]{tree, prcode, Item, batch, expdate, boxsize, UnitRate, Discount, Vat, Qty, bonusqty, Amount, Totaldiscount, TotalVat, NetTotal});

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        final JTextField textfield = (JTextField) invoiceNotext.getEditor().getEditorComponent();
        if ("0".equals(ReturnInvoice) || textfield.getText().isEmpty()) {

        } else {
            try {
                String num = netTotalText.getText();
                //String arebic = convertNumberToArabicWords(num);
                String inwords = convertToIndianCurrency(num);
                String convert = inwords;

                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/creditinvoice.jrxml");

                HashMap para = new HashMap();
                para.put("invoiceno", invoiceNotext.getSelectedItem());
                para.put("paymenttype", "Credit");
                para.put("cashier", username);
                para.put("prevoiusdue", balanceduetext.getText());
                para.put("currentdue", totalinvoiceamount.getText());
                para.put("totaldue", netstext.getText());
                para.put("inwords", convert);
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

                //JasperPrintManager.printReport(jp, true);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {
                JOptionPane.showMessageDialog(null, ex);

            }
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void genericboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_genericboxPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_genericboxPopupMenuWillBecomeInvisible

    private void genericboxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_genericboxKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_genericboxKeyTyped

    private void boxsizetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_boxsizetextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxsizetextKeyPressed

    private void stocktextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stocktextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_stocktextKeyPressed

    private void discounttextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discounttextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            // finelEntry();
            qtytext.setBackground(Color.WHITE);

        }
    }//GEN-LAST:event_discounttextKeyPressed

    private void discounttextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discounttextKeyReleased
        SetVatValueWithDiscount();
        TotalAction();
    }//GEN-LAST:event_discounttextKeyReleased

    private void pvattextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pvattextKeyReleased
        //  TotalAction()
    }//GEN-LAST:event_pvattextKeyReleased

    private void pvatpertextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pvatpertextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            // finelEntry();
            qtytext.setBackground(Color.WHITE);

        }
    }//GEN-LAST:event_pvatpertextKeyPressed

    private void pvatpertextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pvatpertextKeyReleased
        SetVatValueWithDiscount();
        TotalAction();
    }//GEN-LAST:event_pvatpertextKeyReleased

    private void bonusqtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bonusqtytextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            //  finelEntry();
            qtytext.setBackground(Color.WHITE);

        }
    }//GEN-LAST:event_bonusqtytextKeyPressed

    private void unitrateTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateTextKeyReleased
        SetVatValueWithDiscount();
        TotalAction();
    }//GEN-LAST:event_unitrateTextKeyReleased

    private void mpoboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_mpoboxPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_mpoboxPopupMenuWillBecomeInvisible

    private void territoryboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_territoryboxPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_territoryboxPopupMenuWillBecomeInvisible

    private void ptotaltptextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ptotaltptextKeyReleased
        SetPvat();
    }//GEN-LAST:event_ptotaltptextKeyReleased

    private void boxsizetext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_boxsizetext1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxsizetext1KeyPressed

    private void stocktext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stocktext1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_stocktext1KeyPressed

    private void qtytext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytext1KeyPressed

    }//GEN-LAST:event_qtytext1KeyPressed
    private void TotalActionReturn() {
        float tp = 0;
        float mrp = 0;
        float discount = 0;
        float vat = 0;
        float tpvat = 0;
        float tpwithdiscount = 0;
        float qty = 0;
        float totaldiscount = 0;
        float totalvat = 0;
        float totalamount = 0;
        float nettotal = 0;
        if (unitrateText1.getText().isEmpty()) {
            tp = 0;
        } else {
            tp = Float.parseFloat(unitrateText1.getText());
        }

        if (discounttext1.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Float.parseFloat(discounttext1.getText());
        }
        if (pvattext1.getText().isEmpty()) {
            vat = 0;
        } else {
            vat = Float.parseFloat(pvattext1.getText());
        }

        if (qtytext1.getText().isEmpty()) {
            qty = 0;
        } else {
            qty = Float.parseFloat(qtytext1.getText());
        }

        tpwithdiscount = tp - discount;
        float vatper = vatPercentage(vat, tp);
        // pvatpertext.setText(String.valueOf(Math.round(vatper)));
        float vatafterdis = vatValue(vatper, tpwithdiscount);

        tpvat = tpwithdiscount + vatafterdis;
        ptotaltptext1.setText(df2.format(tpvat));
        if (qty == 0) {
            totalamounttext2.setText(null);
            totaldiscounttext2.setText(null);
            altotalvattext1.setText(null);
            netotaltext1.setText(null);
        } else {
            if (returnQty >= qty) {
                totalamount = tp * qty;
                totalamounttext2.setText(df2.format(totalamount));
                totaldiscount = discount * qty;
                totaldiscounttext2.setText(df2.format(totaldiscount));
                totalvat = vat * qty;
                altotalvattext1.setText(df2.format(totalvat));
                float amountwithdiscount = (totalamount - totaldiscount);
                nettotal = (amountwithdiscount + totalvat);
                netotaltext1.setText(df2.format(nettotal));
            } else {

                totalamounttext2.setText(null);
                totaldiscounttext2.setText(null);
                altotalvattext1.setText(null);
                netotaltext1.setText(null);
            }

        }

    }
    private void qtytext1qtytextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytext1qtytextKeyReleased
        TotalActionReturn();
    }//GEN-LAST:event_qtytext1qtytextKeyReleased

    private void pvattext1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pvattext1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_pvattext1KeyReleased

    private void bonusqtytext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bonusqtytext1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_bonusqtytext1KeyPressed

    private void ptotaltptext1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ptotaltptext1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_ptotaltptext1KeyReleased

    private void discounttext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discounttext1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_discounttext1KeyPressed

    private void discounttext1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discounttext1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_discounttext1KeyReleased

    private void unitrateText1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateText1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitrateText1KeyPressed

    private void unitrateText1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateText1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_unitrateText1KeyReleased

    private void pvatpertext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pvatpertext1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_pvatpertext1KeyPressed

    private void pvatpertext1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pvatpertext1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_pvatpertext1KeyReleased

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton17ActionPerformed

    private void addbtn1jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbtn1jButton12ActionPerformed
        float rqty = Float.parseFloat(qtytext1.getText());
        if (returnQty >= rqty) {
            checkentry_return();
        }

    }//GEN-LAST:event_addbtn1jButton12ActionPerformed

    private void datatbl1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatbl1MouseClicked
        //updatekey = 1;
        DefaultTableModel model = (DefaultTableModel) datatbl1.getModel();
        int selectedRowIndex = datatbl1.getSelectedRow();

        ReturnItemcode = (model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch1.setText(model.getValueAt(selectedRowIndex, 2).toString());
        batchtext1.setText(model.getValueAt(selectedRowIndex, 3).toString());
        expdate1.setText(model.getValueAt(selectedRowIndex, 4).toString());
        boxsizetext1.setText((model.getValueAt(selectedRowIndex, 5).toString()));
        unitrateText1.setText(model.getValueAt(selectedRowIndex, 6).toString());
        discounttext1.setText(model.getValueAt(selectedRowIndex, 7).toString());
        pvattext1.setText(model.getValueAt(selectedRowIndex, 8).toString());
        float tp = Float.parseFloat((model.getValueAt(selectedRowIndex, 6).toString()));
        float vat = Float.parseFloat((model.getValueAt(selectedRowIndex, 8).toString()));
        returnQty = Float.parseFloat((model.getValueAt(selectedRowIndex, 9).toString()));
        float vatper = vatPercentage(vat, tp);
        pvatpertext1.setText(df2.format(vatper));
        float totalvat = tp + vat;
        ptotaltptext1.setText(df2.format(totalvat));
        qtytext1.setText(model.getValueAt(selectedRowIndex, 9).toString());
        bonusqtytext1.setText(model.getValueAt(selectedRowIndex, 10).toString());
        totalamounttext2.setText(model.getValueAt(selectedRowIndex, 11).toString());
        totaldiscounttext2.setText(model.getValueAt(selectedRowIndex, 12).toString());
        altotalvattext1.setText(model.getValueAt(selectedRowIndex, 13).toString());
        netotaltext1.setText(model.getValueAt(selectedRowIndex, 14).toString());
        stocktext1.setText(GetStrock(ReturnItemcode));
        //  unibox.setSelectedItem(model.getValueAt(selectedRowIndex, 6).toString());
        qtytext1.setBackground(Color.YELLOW);
        try {
            String sql = "Select "
                    + "(Select generic_name from generic sinfo where sinfo.generic_id=it.generic_id) as 'genericname',"
                    + "strangth"
                    + " from item it where it.Itemcode='" + ReturnItemcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String genericnames = rs.getString("genericname");
                genericbox1.setText(genericnames);
                String strangth = rs.getString("strangth");
                strengthtext1.setText(strangth);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_datatbl1MouseClicked

    private void descriptionItemEntry() {
        try {

            String sql = "Select bp.itemcode as 'itemcode' ,bp.barcode as 'barcode',bp.color as 'color',bp.size as 'size',bp.mrp as 'mrp',ita.itemName as 'itemName',ita.vat as 'vat',(select un.unitshort from unit un where un.id=ita.stockunit) as 'stockunit' FROM item ita inner join barcodeproduct bp ON ita.itemcode=bp.itemcode where bp.barcode='" + barcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                barcode = rs.getString("barcode");
                itemname = rs.getString("itemName");
                itemnamesearch.setSelectedItem(itemname);
                color = rs.getString("color");
                //  colorbox.setSelectedItem(color);
                size = rs.getString("size");
                // sizetext.setText(size);
                mrp = rs.getString("mrp");
                unitrateText.setText(mrp);
                stockunit = rs.getString("stockunit");
                includevat = rs.getFloat("vat");
                qtytext.setText("1");
                //checkentry();
                if (directstore.isSelected() == true) {
                    CheckEntryFilter();
                } else {
                    qtytext.requestFocusInWindow();
                }

            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
    }

    private void tableNoAction() {
        if (orderNobox.getSelectedIndex() == 0) {
            AfterExecute();

        } else {
            orderview();
            orderupdate = 1;
            orderbtn.setText("Update Kitchen Order");
            executebtn.setEnabled(true);

        }

    }

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
            java.util.logging.Logger.getLogger(InvoiceTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addbtn;
    private javax.swing.JButton addbtn1;
    private javax.swing.JTextArea addresstext;
    private javax.swing.JButton adjustbtn;
    private javax.swing.JTextField altotalvattext;
    private javax.swing.JTextField altotalvattext1;
    private javax.swing.JTextField balanceduetext;
    private javax.swing.JTextField batchtext;
    private javax.swing.JTextField batchtext1;
    private javax.swing.JTextField bonusqtytext;
    private javax.swing.JTextField bonusqtytext1;
    private javax.swing.JLabel bookreturntext;
    private javax.swing.JTextField boxsizetext;
    private javax.swing.JTextField boxsizetext1;
    private javax.swing.JTextField cashamounttext;
    private javax.swing.JTextField contactnotext;
    private javax.swing.JTextField creditamounttext;
    private javax.swing.JComboBox<String> customerbox;
    private javax.swing.JTextField customeridtext;
    private javax.swing.JLabel customeridtextsecond;
    private javax.swing.JTabbedPane customerpane;
    private javax.swing.JTable dataTable;
    private javax.swing.JTable datatbl1;
    private javax.swing.JTable descrptiontbl;
    private javax.swing.JCheckBox directstore;
    private javax.swing.JComboBox<String> discountboxtypefinel;
    private javax.swing.JTextField discounttext;
    private javax.swing.JTextField discounttext1;
    private javax.swing.JLabel duelabel;
    private javax.swing.JLabel duelabel1;
    private javax.swing.JLabel duelabel2;
    private javax.swing.JLabel duelabel3;
    private javax.swing.JLabel duelabel4;
    private javax.swing.JLabel duelabel5;
    private javax.swing.JLabel duelabel6;
    private javax.swing.JButton executebtn;
    private javax.swing.JTextField expdate;
    private javax.swing.JTextField expdate1;
    private javax.swing.JComboBox<String> genericbox;
    private javax.swing.JTextField genericbox1;
    private com.toedter.calendar.JDateChooser inputdate;
    private javax.swing.JComboBox<String> invoiceNotext;
    private javax.swing.JLabel invoicetext;
    private javax.swing.JLabel invoicetext1;
    private javax.swing.JPopupMenu itemmenu;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JTextField itemnamesearch1;
    private javax.swing.JTabbedPane itempane;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
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
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel74;
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
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel266;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JComboBox<String> mpobox;
    private javax.swing.JLabel msgtext;
    private javax.swing.JLabel netTotalText;
    private javax.swing.JLabel netTotalText1;
    private javax.swing.JTextField netdiscount;
    private javax.swing.JLabel netdiscountreturn;
    private javax.swing.JTextField netotaltext;
    private javax.swing.JTextField netotaltext1;
    private javax.swing.JTextField netstext;
    private javax.swing.JLabel nettoalabel;
    private javax.swing.JLabel nettotaltext;
    private javax.swing.JTextField newcustomeridtext;
    private javax.swing.JTextField openingbalancetext;
    private javax.swing.JComboBox<String> orderNobox;
    private javax.swing.JButton orderbtn;
    private javax.swing.JComboBox<String> previceinvoicebox;
    private javax.swing.JCheckBox printinvoicecheck;
    private javax.swing.JTextField ptotaltptext;
    private javax.swing.JTextField ptotaltptext1;
    private javax.swing.JTextField pvatpertext;
    private javax.swing.JTextField pvatpertext1;
    private javax.swing.JTextField pvattext;
    private javax.swing.JTextField pvattext1;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextField qtytext1;
    private javax.swing.JTable returndataTable;
    private javax.swing.JButton returnexecution;
    private javax.swing.JTextField searchtext;
    private javax.swing.JComboBox<String> searchtypebox;
    private javax.swing.JTextField snametext;
    private javax.swing.JTextField stocktext;
    private javax.swing.JTextField stocktext1;
    private javax.swing.JTextField strengthtext;
    private javax.swing.JTextField strengthtext1;
    private javax.swing.JComboBox<String> territorybox;
    private javax.swing.JLabel totalVatText;
    private javax.swing.JLabel totalamounttext;
    private javax.swing.JTextField totalamounttext1;
    private javax.swing.JTextField totalamounttext2;
    private javax.swing.JTextField totaldiscounttext;
    private javax.swing.JTextField totaldiscounttext1;
    private javax.swing.JTextField totaldiscounttext2;
    private javax.swing.JLabel totalinvoiceamount;
    private javax.swing.JLabel totalinvoiceamountreturn;
    private javax.swing.JTextField totalpaymenttext;
    private javax.swing.JLabel totalqtytext;
    private javax.swing.JLabel totalrate;
    private javax.swing.JLabel totalrateReturn;
    private javax.swing.JTextField totalsaletext;
    private javax.swing.JLabel totalvattext;
    private javax.swing.JLabel totalvattextreturn;
    private javax.swing.JComboBox<String> unibox;
    private javax.swing.JComboBox<String> unibox1;
    private javax.swing.JTextField unitrateText;
    private javax.swing.JTextField unitrateText1;
    private javax.swing.JButton voidbtn;
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

        String paise = (decimal) > 0 ? " And " + words.get((int) (decimal - decimal % 10)) + " Files" + words.get((int) (decimal % 10)) : "";
        return Rupees + paise + " " + "Taka Only";
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
