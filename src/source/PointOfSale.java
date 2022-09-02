/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.Dimension;
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
public final class PointOfSale extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    float tpprice;
    float bankbalance1;
    int tree = 0;

    int treebook = 0;
    int treereturn = 0;
    String Invoiceno;

    String cusId = "101";
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

    /**
     * Creates new form PointOfSale
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public PointOfSale() throws SQLException, IOException {

        initComponents();
        conn = Java_Connect.conectrDB();
        CurrentDatetime();
        CurrentTime();
        userkey();
        currentDate();
        //   Item();
        customer();
        intilize();

        currency();
        cashnumber();
        parchase_code();
        //offer();
        offerCheck();
        // hidePanel();
        bankinfo();
        username();
        order();
        mobilebanktype();
        book();
        customernameactive();
        customerid();
        recieptmsg();
        //    AutoCompleteDecorator.decorate(itemnamesearch);
        //AutoCompleteDecorator.decorate(itemnamesearch);
        AutoCompleteDecorator.decorate(customerbox);
        AutoCompleteDecorator.decorate(Bankinfobox);
        AutoCompleteDecorator.decorate(BankinfoboxCard);
        AutoCompleteDecorator.decorate(orderNobox);
        AutoCompleteDecorator.decorate(mobilebanktype);
        AutoCompleteDecorator.decorate(bookbox);
        AutoCompleteDecorator.decorate(searchtypebox);
        AutoCompleteDecorator.decorate(previceinvoicebox);

        placebtn.setEnabled(false);
        codetext.requestFocusInWindow();
        bookrecieptbtn.setEnabled(false);
        directstore.setSelected(false);
        printinvoicecheck.setSelected(true);
        TableDesign();
        discounttypebox.setSelectedIndex(1);
        discountboxtypefinel.setSelectedIndex(1);
        final JTextField textfield = (JTextField) itemnamesearch.getEditor().getEditorComponent();
        textfield.requestFocusInWindow();
        ExtractFilter();
        ExtractFilterInvoicecheck();
        // jScrollPane2.getViewport().setBackground(Color.WHITE);
        jScrollPane5.hide();
        //   jScrollPane5.setH
        itemtables.setSize(640, 200);
        Color ivory = new Color(255, 255, 255);
        jScrollPane5.getViewport().setBackground(ivory);
        itemtables.setPreferredSize(new Dimension(640, 200));
        itemtables.setPreferredSize(new Dimension(640, 200));
    }

    private void recieptmsg() {
        try {
            String sql = "Select cashinvoice,creditinvoice from recieptmsg  where id=1";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {
                cashinvoiceprintslip = rs.getString("cashinvoice");
                printertypebox.setSelectedItem(cashinvoiceprintslip);
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
                        codetext.setText(null);
                        unitrateText.setText(null);
                        discounttext.setText(null);
                        qtytext.setText(null);

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

            String sql = "Select DISTINCT  itemName from item WHERE lower(itemName)  LIKE '%" + enteredText + "%' ORDER BY itemName ASC";
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
            codetext.setText(null);
            unitrateText.setText(null);
            discounttext.setText(null);
            qtytext.setText(null);

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

    private void TableDesignItem() {
        JTableHeader jtblheader = itemtables.getTableHeader();
        jtblheader.setBackground(Color.red);
        jtblheader.setForeground(Color.DARK_GRAY);
        jtblheader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ((DefaultTableCellRenderer) jtblheader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        itemtables.setDefaultRenderer(Object.class, centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        itemtables.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        itemtables.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        itemtables.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        itemtables.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        //  itemtables.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
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

    private void tablepanelActivate() {
        if (typebox.getSelectedIndex() == 0) {
            itempane.setEnabledAt(0, true);

        }

    }

    private void cashboxActive() {
        if (typebox.getSelectedIndex() == 0) {
            bankamounttex.setEditable(true);
            cardamounttext.setEditable(true);
            mobilebankamounttext.setEditable(true);
            cashrecivetext.setEditable(true);

        } else {
            bankamounttex.setEditable(false);
            cardamounttext.setEditable(false);
            mobilebankamounttext.setEditable(false);
            cashrecivetext.setEditable(false);
        }

    }

    private void priviousInvoice() {
        previceinvoicebox.removeAllItems();
        previceinvoicebox.addItem("Select");
        previceinvoicebox.setSelectedItem("Select");
        switch (typebox.getSelectedIndex()) {
            case 0:
                try {
                    String sql = "Select invoiceNo from cashsale";
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
                break;
            case 1:
                try {
                    String sql = "Select invoiceNo from sale";
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
                break;
            default:
                break;
        }

    }

    private void parchase_code() {
        priviousInvoice();
        switch (typebox.getSelectedIndex()) {
            case 0:
                itempane.setSelectedIndex(0);
                /*
                itempane.setEnabledAt(0, true);
                itempane.setEnabledAt(2, false);
                itempane.setEnabledAt(3, false);
                 */

                panelactive();
                cashcredit = 0;
                jLabel17.setText("Invoice No:");

                try {

                    String NewParchaseCode = ("INV-1" + new_inv + "");
                    String sql = "Select id from cashsale";

                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    if (rs.last()) {
                        String add1 = rs.getString("id");
                        //invoc_text.setText(add1);
                        int inv = Integer.parseInt(add1);
                        new_inv = (inv + 1);

                    }
                    //String ParchaseCode =String.format("%08d",new_inv);

                    NewParchaseCode = ("INV-1" + new_inv + "");
                    invoicetext.setText(NewParchaseCode);
                    Invoicenocheck = ("INV-1" + new_inv + "");

                } catch (SQLException | NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                break;
            case 1:
                itempane.setSelectedIndex(0);
                cashcredit = 1;
                /*
                itempane.setEnabledAt(0, true);
                itempane.setEnabledAt(2, false);
                itempane.setEnabledAt(3, false);
                 */

                //paneldactive();
                entrucesure();
                jLabel17.setText("Invoice No:");

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
                break;
            //book number
            case 2:
                itempane.setSelectedIndex(2);
                /*
                itempane.setEnabledAt(0, false);
                itempane.setEnabledAt(2, true);
                itempane.setEnabledAt(3, false);
                
                 */
                panelactive();

                jLabel17.setText("Order No:");

                try {
                    int bookno = 1;
                    String NewParchaseCode = ("OR-1" + bookno + "");
                    String sql = "Select id from book";

                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    if (rs.last()) {
                        String add1 = rs.getString("id");
                        //invoc_text.setText(add1);
                        int inv = Integer.parseInt(add1);
                        bookno = (inv + 1);

                    }
                    //String ParchaseCode = String.format("%010d",new_inv);
                    NewParchaseCode = ("BK-1" + new_inv + "");
                    invoicetext.setText(NewParchaseCode);
                } catch (SQLException | NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                break;
            //return
            default:
                itempane.setSelectedIndex(3);
                /*
                itempane.setEnabledAt(0, false);
                itempane.setEnabledAt(2, false);
                itempane.setEnabledAt(3, true);
                 */
                panelactive();

                jLabel17.setText("Return No:");

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
                    //String ParchaseCode = String.format("%010d",new_inv);
                    NewParchaseCode = ("SR-1" + new_inv + "");
                    invoicetext.setText(NewParchaseCode);
                } catch (SQLException | NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                break;
        }

        cashboxActive();
    }

    private void Item_Tabclick() {
        switch (itempane.getSelectedIndex()) {
            case 0:
                if (cashcredit == 0) {
                    typebox.setSelectedIndex(0);
                } else {
                    typebox.setSelectedIndex(1);

                }
                parchase_code();
                break;

            case 2:
                typebox.setSelectedIndex(2);
                parchase_code();
                break;
            //return
            case 3:
                typebox.setSelectedIndex(3);
                parchase_code();
                break;

            default:
                break;
        }

    }

    private void panelactive() {
        codetext.setEnabled(true);
        itemnamesearch.setEnabled(true);
        unitrateText.setEnabled(true);
        qtytext.setEnabled(true);

        discounttext.setEnabled(true);
        addbtn.setEnabled(true);

    }

    private void paneldactive() {
        codetext.setEnabled(false);
        itemnamesearch.setEnabled(false);
        unitrateText.setEnabled(false);
        qtytext.setEnabled(false);

        discounttext.setEnabled(false);
        addbtn.setEnabled(false);

    }

    private void entrucesure() {

        if (typebox.getSelectedIndex() == 1) {
            if (customerbox.getSelectedIndex() == 0) {

                paneldactive();
            } else {
                panelactive();

            }

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
            codetext.setEditable(true);
            itemnamesearch.setEditable(true);
            itemnamesearch.setEnabled(true);
        } else {
            codetext.setEditable(false);
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

    private void bankinfo() {

        String status = "Active";
        try {
            String sql = "Select Bankname from bankinfo where status='" + status + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("Bankname");
                Bankinfobox.addItem(name);
                BankinfoboxCard.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    //book Module
    private void book() {
        bookbox.removeAllItems();
        bookbox.addItem("Select");
        bookbox.setSelectedItem("Select");
        String status = "0";
        try {
            String sql = "Select bookid from book where status='" + status + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("bookid");
                bookbox.addItem(name);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void Bookinsert() throws JRException {

        try {

            String sql = "Insert into book(bookid,cusid,inputdate,amount,advanced,remain,status,inputuser) values (?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, invoicetext.getText());
            pst.setString(2, cusId);
            pst.setDate(3, date);
            pst.setString(4, bookamounttext.getText());
            pst.setString(5, advancedtext.getText());
            pst.setString(6, remaintext.getText());
            pst.setInt(7, 0);
            pst.setInt(8, userid);

            pst.execute();
            float cashin = Float.parseFloat(advancedtext.getText());

            bookDetailsInsert();
            Stockpdatebook();
            if (cashin > 0) {
                cashInDrwaerBooking(cashin);
            }
            printInvoice();
            JOptionPane.showMessageDialog(null, "Successfuly Order");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void bookDetailsInsert() throws SQLException {

        try {

            int rows = booktable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);

                String probarcode = (String) booktable.getValueAt(row, 1);
                try {
                    String sql = "Select Itemcode,tp from item where Itemcode='" + itemcode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    if (rs.next()) {
                        itemcode = rs.getString("Itemcode");
                        tpprice = rs.getFloat("tp");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                float rate = (Float) booktable.getValueAt(row, 3);
                float qty = (Float) booktable.getValueAt(row, 4);

                //String unit = (String) dataTable.getValueAt(row, 6);
                float discount = (Float) booktable.getValueAt(row, 5);

                float itemvat = (rate - (discount / qty)) / qty;
                float totalamount = (Float) booktable.getValueAt(row, 7);

                // float vat = (Float) booktable.getValueAt(row, 8);
                float totalvat = (Float) booktable.getValueAt(row, 8);
                float neTotal = (Float) booktable.getValueAt(row, 9);

                try {

                    String sql = "Insert into bookdetails(bookid,"
                            + "prcode,"
                            + "barcode,"
                            + "color,"
                            + "prosize,"
                            + "unitrate,"
                            + "qty,"
                            + "discount,"
                            + "totalamount,"
                            + "vat,"
                            + "Totalvat,"
                            + "NetTotal,"
                            + "tradprice,"
                            + "invoicedate,"
                            + "cusid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, invoicetext.getText());
                    pst.setString(2, itemcode);
                    pst.setString(3, probarcode);
                    pst.setString(4, color);
                    pst.setString(5, size);
                    pst.setFloat(6, rate);
                    pst.setFloat(7, qty);
                    pst.setFloat(8, discount);
                    pst.setFloat(9, totalamount);
                    pst.setFloat(10, itemvat);
                    pst.setFloat(11, totalvat);
                    pst.setFloat(12, neTotal);
                    pst.setFloat(13, tpprice);
                    pst.setDate(14, date);
                    pst.setString(15, cusId);
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

    private void Stockpdatebook() {
        // int cat=(Integer) categoryBox.getSelectedIndex();
        for (int i = 0; i < booktable.getRowCount(); i++) {

            String s = booktable.getValueAt(i, 1).toString().trim();
            float qty = (Float) booktable.getValueAt(i, 4);

            Stockpdateumuse(s, qty);

        }

    }

    private void BookView() {
        String bookid = (String) bookbox.getSelectedItem();
        try {

            DefaultTableModel model2 = (DefaultTableModel) booktable.getModel();
            model2.setRowCount(0);

            String sql = "Select prcode ,barcode, (select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item',color,prosize, sl.unitrate as 'UnitRate', sl.qty as 'Qty', sl.discount as 'Discount',sl.discounttype as 'discounttype', sl.totalamount as 'Amount', sl.vat as 'Vat', sl.Totalvat as 'TotalVat',sl.NetTotal  as 'NetTotal'  from bookdetails sl where sl.bookid='" + bookid + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            //   datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String prcode = rs.getString("prcode");
                String probarcode = rs.getString("barcode");
                String Item = rs.getString("Item");
                String procolor = rs.getString("color");
                String prosize = rs.getString("prosize");
                float UnitRate = rs.getFloat("UnitRate");
                float Qty = rs.getFloat("Qty");

                float Discount = rs.getFloat("Discount");
                String type = rs.getString("discounttype");
                float Amount = rs.getFloat("Amount");
                float Vat = rs.getFloat("Vat");
                float totalVat = rs.getFloat("TotalVat");
                float NetTotal = rs.getFloat("NetTotal");
                treebook++;
                model2.addRow(new Object[]{treebook, probarcode, Item, UnitRate, Qty, Discount, type, Amount, totalVat, NetTotal});

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        try {
            String sql = "Select bookid,inputdate,amount,advanced,remain from book sl  where sl.bookid='" + bookid + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {

                //   String Id = rs.getString("invoiceNo");
                /// invoiceapplytex.setText(Id);
                String bookidinv = rs.getString("bookid");
                invoicetext.setText(bookidinv);
                String bookinputdate = rs.getString("inputdate");
                bookDatetext.setText(bookinputdate);

                float amount = rs.getFloat("amount");
                bookamounttext.setText(df2.format(amount));

                float advanced = rs.getFloat("advanced");
                advancedtext.setText(df2.format(advanced));

                float remain = rs.getFloat("remain");
                remaintext.setText(df2.format(remain));

                //   currency=rs.getString("paymentCurency");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void Bookupdate() throws SQLException {
        try {
            int status = 1;
            String bookid = (String) bookbox.getSelectedItem();

            String sql = "Update book set amount='" + bookamounttext.getText() + "',advanced='" + advancedtext.getText() + "',remain='" + remaintext.getText() + "' where bookid='" + bookid + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {
            String sql = "Delete from bookdetails where bookid=? ";
            pst = conn.prepareStatement(sql);

            pst.setString(1, (String) bookbox.getSelectedItem());

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        bookDetailsInsert();
        Stockpdatebook();
    }

    private void BookupdateStatus() throws SQLException {
        try {
            int status = 1;
            String bookid = (String) bookbox.getSelectedItem();

            String sql = "Update book set status='" + status + "' where bookid='" + bookid + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        book();

    }

    private void bookTotalcount() {
        float remain = (float) 0.00;
        float amount = 0;
        float advanced = 0;
        amount = (float) total_invoice_amount_Booking();
        advanced = Float.parseFloat(advancedtext.getText());
        if (amount >= advanced) {
            remain = amount - advanced;
            remaintext.setText(df2.format(remain));
        } else {
            remaintext.setText(null);
        }
    }

    private void bookPlacement() {
        typebox.setSelectedIndex(0);
        parchase_code();
        String bookid = (String) bookbox.getSelectedItem();
        try {
            String sql = "Select advanced from book sl  where sl.bookid='" + bookid + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {
                bookReturn = rs.getFloat("advanced");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {

            DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
            //model2.setRowCount(0);

            String sql = "Select prcode ,barcode, (select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item',color,prosize, sl.unitrate as 'UnitRate', sl.qty as 'Qty', sl.discount as 'Discount', sl.totalamount as 'Amount', sl.vat as 'Vat', sl.Totalvat as 'TotalVat',sl.NetTotal  as 'NetTotal'  from bookdetails sl where sl.bookid='" + bookid + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            //   datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                itemcode = rs.getString("prcode");
                barcode = rs.getString("barcode");
                itemname = rs.getString("Item");
                itemnamesearch.setSelectedItem(itemname);
                color = rs.getString("color");
                //colorbox.setSelectedItem(color);
                size = rs.getString("prosize");
                //sizetext.setText(size);
                mrp = rs.getString("UnitRate");
                unitrateText.setText(mrp);
                String Qty = rs.getString("Qty");
                qtytext.setText(Qty);
                // float Discount = rs.getFloat("Discount");
                //float Amount = rs.getFloat("Amount");
                float Vat = rs.getFloat("Vat");
                includevat = (float) 0.00;
                CheckEntryFilter();
                //float totalVat = rs.getFloat("TotalVat");
                /// float NetTotal = rs.getFloat("NetTotal");
                //tree++;
                //  model2.addRow(new Object[]{tree, prcode,probarcode,Item,procolor,prosize, UnitRate, Qty, Discount, Amount, Vat,totalVat, NetTotal});

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
                pst.setString(4, (String) typebox.getSelectedItem());
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

    private void mobilebanktype() throws SQLException {

        try {
            String sql = "Select name from mobilebank";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("name");
                mobilebanktype.addItem(name);
            }
        } catch (SQLException e) {
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
        customerbox.setSelectedIndex(0);
        openingbalancetext.setText(null);
        creditamounttext.setText(null);
        cashamounttext.setText(null);
        totalsaletext.setText(null);
        totaldiscounttext.setText(null);
        totalpaymenttext.setText(null);
        balanceduetext.setText(null);
        netstext.setText(null);
        codetext.requestFocusInWindow();

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
        paytext.setText(null);
        changetext.setText(null);
        accnotext.setText(null);
        Bankinfobox.setSelectedIndex(0);
        cardnotext.setText(null);
        BankinfoboxCard.setSelectedIndex(0);
        bkashtext.setText(null);
        bookreturntext.setText(null);
        nettotaltext.setText(null);
        order();
        orderupdate = 0;
        saleupdate = 0;
        //executebtn.setEnabled(false);
        orderbtn.setText("Hold Order");
        codetext.requestFocusInWindow();
        changetext.setBackground(Color.WHITE);
        typebox.setSelectedIndex(0);
        totalqtytext.setText(null);
        customernameactive();
        cusId = "101";
        customernameactive();
        customer_field_clear();
        cashcredit = 0;
        hold = 0;

        //cashbox clear
        bankamounttex.setText("0.00");
        cardamounttext.setText("0.00");
        mobilebankamounttext.setText("0.00");
        roundoffamounttext.setText("0.00");
        paidableamountext.setText("0.00");
        cashrecivetext.setText("0.00");
        paytext.setText("0.00");
        changetext.setText(null);
        mobilebank = 0;
        clear();
        parchase_code();
        tree = 0;
        discounttypebox.setSelectedIndex(1);
        discountboxtypefinel.setSelectedIndex(1);
    }

    private void cashboxClear() {
        bankamounttex.setText("0.00");
        cardamounttext.setText("0.00");
        mobilebankamounttext.setText("0.00");
        roundoffamounttext.setText("0.00");
        paidableamountext.setText("0.00");
        cashrecivetext.setText("0.00");

        changetext.setText(null);
        mobilebank = 0;

    }

    private void creditCustomerAcount() {
        if (typebox.getSelectedIndex() == 1) {

            float totalinvoce = (float) total_invoice_amount();
            float prebamt = Float.parseFloat(balanceduetext.getText());
            float nettotalin = totalinvoce + prebamt;
            String nets = String.format("%.2f", nettotalin);
            netstext.setText(nets);
        }

    }

    private void CheckEntryFilter() {
        switch (typebox.getSelectedIndex()) {
            case 0:

                //cash
                checkentry();
                break;
            case 1:

                //credit
                checkentry();
                break;
            case 2:

                //booking
                checkentryBooking();
                break;
//return booking
            default:

                //return
                checkentry_return();
                break;
        }
    }
    //cash and credit table

    private static final DecimalFormat df3 = new DecimalFormat("#");

    private void checkentry() {
        /* String itemcode=null;
     String barcode=null;
     String itemname =null; 
     String  color=null;
     String size=null;
     String mrp=null;
     String stockunit = null;
        
         */

        String s = "";
        boolean exists = false;
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            s = dataTable.getValueAt(i, 1).toString().trim();
            ///filter
            float tpd = Float.parseFloat(mrp);
            float qty = Float.parseFloat(dataTable.getValueAt(i, 4).toString());
            float applyqty = Float.parseFloat(qtytext.getText());
            float totalqty = qty + applyqty;
            float totaldiscountget = (Float) dataTable.getValueAt(i, 5);
            float discount = totaldiscountget / qty;
            float tpwdisc = tpd - discount;
            float nettotaltp = (tpd * totalqty);
            float nettotaltpformat = Float.parseFloat(df2.format(nettotaltp));
            float totaldiscounts = totalqty * discount;
            float totaldiscountformat = Float.parseFloat(df2.format(totaldiscounts));
            float vats = includevat / 100;
            float itemvat = tpwdisc * vats;
            float itemvatfromat = Float.parseFloat(df2.format(itemvat));
            float totalvat = itemvatfromat * totalqty;
            float nettotal = nettotaltpformat + totalvat - totaldiscountformat;
            float nettotalformat = Float.parseFloat(df2.format(nettotal));

            if (itemcode.equals("")) {
                JOptionPane.showMessageDialog(null, "Enter Invoice Details First");
            } else if (itemcode.equals(s)) {
                exists = true;
                //update
                dataTable.setValueAt(totalqty, i, 4);
                dataTable.setValueAt(totaldiscountformat, i, 5);
                // dataTable.setValueAt(nettotaltpformat, i, 7);
                //  dataTable.setValueAt(totalvatformat, i, 8);
                dataTable.setValueAt(nettotalformat, i, 6);
                //total calculation
                totalqtytext.setText(df3.format(total_qty()));

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
                paidableamountext.setText(df2.format(nettotalwbr));
                //creditCustomerAcount();
                break;
            }
        }
        if (!exists) {
            entryData();
        } else {
            // JOptionPane.showMessageDialog(null, "This Data Already Exist.");
            clear();

        }
        codetext.requestFocusInWindow();
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
            discounttype = " ";
        } else {
            discounttype = (String) discounttypebox.getSelectedItem();
            float discountpersantage = Float.parseFloat(discounttext.getText());
            if (discounttypebox.getSelectedIndex() == 0) {
                float discountvalue = discountpersantage / 100;
                discount = tpd * discountvalue;
            } else {
                discount = Float.parseFloat(discounttext.getText());
            }
        }

        float tpwdisc = tpd - discount;
        productvat = (tpwdisc * vat);
        float productvatfloat = Float.parseFloat(df2.format(productvat));
        float qty = Float.parseFloat(qtytext.getText());
        float totaldics = discount * qty;
        float totaldiscountformat = Float.parseFloat(df2.format(totaldics));
        float nettotaltp = (tpd * qty);
        totalvat = (productvatfloat * qty);
        float totalvatfloat = Float.parseFloat(df2.format(totalvat));
        float totalall = nettotaltp + totalvatfloat - totaldiscountformat;

        model2.addRow(new Object[]{tree, itemcode, itemname, tpd, qty, totaldiscountformat, totalall});
        model2.moveRow(model2.getRowCount() - 1, model2.getRowCount() - 1, 0);
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
        paidableamountext.setText(df2.format(nettotalwbr));
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
        paidableamountext.setText(df2.format(nettotalwbr));
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
            itemmrp = Float.parseFloat(dataTable.getValueAt(i, 3).toString());
            qty = Float.parseFloat(dataTable.getValueAt(i, 4).toString());
            totaltpmrp = totaltpmrp + (itemmrp * qty);
            // totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 7).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_vat() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;
        float itemmrp = 0;
        float qty = 0;
        float totaldiscountamount = 0;
        float discount = 0;
        float mrpdiscount = 0;
        float itemvate = 0;
        float totalvat = 0;
        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());
            String probarcode = (String) dataTable.getValueAt(i, 1);
            try {
                String sql = "Select it.vat as 'Vat' from item it where it.barcode='" + probarcode + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                if (rs.next()) {
                    includevat = rs.getFloat("Vat");

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

            itemmrp = Float.parseFloat(dataTable.getValueAt(i, 3).toString());
            qty = Float.parseFloat(dataTable.getValueAt(i, 4).toString());
            totaldiscountamount = Float.parseFloat(dataTable.getValueAt(i, 5).toString());
            discount = totaldiscountamount / qty;
            mrpdiscount = itemmrp - discount;
            itemvate = includevat / 100 * mrpdiscount;
            totalvat = itemvate * qty;
            totaltpmrp = totaltpmrp + totalvat;
        }

        return (float) totaltpmrp;

    }

    private double total_invoice_amount() {

        int rowaCount = dataTable.getRowCount();

        double totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Double.parseDouble(dataTable.getValueAt(i, 6).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_discount_amount() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 5).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_qty() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 4).toString());
        }

        return (float) totaltpmrp;

    }
//End credit cash table insert

    //book table
    private void checkentryBooking() {
        /* String itemcode=null;
     String barcode=null;
     String itemname =null; 
     String  color=null;
     String size=null;
     String mrp=null;
     String stockunit = null;
        
         */

        String s = "";
        boolean exists = false;
        for (int i = 0; i < booktable.getRowCount(); i++) {
            s = booktable.getValueAt(i, 1).toString().trim();
            ///filter
            float tpd = Float.parseFloat(mrp);
            float qty = (Float) booktable.getValueAt(i, 4);
            float applyqty = Float.parseFloat(qtytext.getText());
            float totalqty = qty + applyqty;

            float totaldiscountget = (Float) booktable.getValueAt(i, 5);
            float discount = totaldiscountget / qty;
            float tpwdisc = tpd - discount;

            float nettotaltp = (tpd * totalqty);

            float nettotaltpformat = Float.parseFloat(df2.format(nettotaltp));
            float totaldiscounts = totalqty * discount;
            float totaldiscountformat = Float.parseFloat(df2.format(totaldiscounts));

            float vats = includevat / 100;
            float itemvat = tpwdisc * vats;
            float itemvatfromat = Float.parseFloat(df2.format(itemvat));

            float totalvat = itemvatfromat * totalqty;
            float totalvatformat = Float.parseFloat(df2.format(totalvat));

            float nettotal = nettotaltpformat + totalvat - totaldiscountformat;
            float nettotalformat = Float.parseFloat(df2.format(nettotal));

            if (itemcode.equals("")) {
                // JOptionPane.showMessageDialog(null, "Enter Invoice Details First");
            } else if (barcode.equals(s)) {
                exists = true;

                //update
                booktable.setValueAt(totalqty, i, 4);
                booktable.setValueAt(totaldiscountformat, i, 5);
                booktable.setValueAt(nettotaltpformat, i, 7);
                booktable.setValueAt(totalvatformat, i, 8);
                booktable.setValueAt(nettotalformat, i, 9);

                //total calculation
                double input = total_invoice_amount_Booking();
                bookamounttext.setText(df2.format(input));
                bookTotalcount();

                break;
            }
        }
        if (!exists) {
            entryDatabooking();
        } else {
            // JOptionPane.showMessageDialog(null, "This Data Already Exist.");
            clear();

        }
        codetext.requestFocusInWindow();
    }

    private void entryDatabooking() {
        float discount;
        String discounttype;
        float vat = includevat / 100;
        float totalvat, productvat;

        DefaultTableModel model2 = (DefaultTableModel) booktable.getModel();
        treebook++;

        float tpd = Float.parseFloat(unitrateText.getText());

        if (discounttext.getText().isEmpty()) {

            discount = 0;
            discounttype = " ";

        } else {
            discounttype = (String) discounttypebox.getSelectedItem();
            float discountpersantage = Float.parseFloat(discounttext.getText());
            if (discounttypebox.getSelectedIndex() == 0) {
                float discountvalue = discountpersantage / 100;
                discount = tpd * discountvalue;

            } else {
                discount = Float.parseFloat(discounttext.getText());

            }
        }

        float tpwdisc = tpd - discount;
        productvat = (tpwdisc * vat);
        float productvatfloat = Float.parseFloat(df2.format(productvat));

        float qty = Float.parseFloat(qtytext.getText());
        float totaldics = discount * qty;
        float totaldiscountformat = Float.parseFloat(df2.format(totaldics));

        float nettotaltp = (tpd * qty);
        totalvat = (productvatfloat * qty);

        float totalvatfloat = Float.parseFloat(df2.format(totalvat));
        float totalall = nettotaltp + totalvatfloat - totaldiscountformat;

        //String gpper = String.format("%.2f", gp);
        model2.addRow(new Object[]{treebook, itemcode, itemname, tpd, qty, totaldiscountformat, discounttype, nettotaltp, totalvatfloat, totalall});
        ///  totalrate.setText(Integer.toString(total_action_rate()));

        double input = total_invoice_amount_Booking();
        bookamounttext.setText(df2.format(input));
        bookTotalcount();

        clear();
    }

    private void deleterowbooking() {

        int[] toDelete = booktable.getSelectedRows();
        Arrays.sort(toDelete); // be shure to have them in ascending order.
        DefaultTableModel myTableModel = (DefaultTableModel) booktable.getModel();
        for (int ii = toDelete.length - 1; ii >= 0; ii--) {
            myTableModel.removeRow(toDelete[ii]); // beginning at the largest.
        }

        double input = total_invoice_amount_Booking();
        bookamounttext.setText(df2.format(input));
        bookTotalcount();

        updatekey = 0;
        clear();
    }

    private double total_invoice_amount_Booking() {

        int rowaCount = booktable.getRowCount();

        double totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Double.parseDouble(booktable.getValueAt(i, 9).toString());
        }

        return (float) totaltpmrp;

    }

    //end book table   
    //return entry table
    private void checkentry_return() {
        /* String itemcode=null;
     String barcode=null;
     String itemname =null; 
     String  color=null;
     String size=null;
     String mrp=null;
     String stockunit = null;
        
         */

        String s = "";
        boolean exists = false;
        for (int i = 0; i < returndataTable.getRowCount(); i++) {
            s = returndataTable.getValueAt(i, 1).toString().trim();
            ///filter
            float tpd = Float.parseFloat(mrp);
            float qty = (Float) returndataTable.getValueAt(i, 4);
            float applyqty = Float.parseFloat(qtytext.getText());
            float totalqty = qty + applyqty;

            float totaldiscountget = (Float) returndataTable.getValueAt(i, 5);
            float discount = totaldiscountget / qty;
            float tpwdisc = tpd - discount;

            float nettotaltp = (tpd * totalqty);

            float nettotaltpformat = Float.parseFloat(df2.format(nettotaltp));
            float totaldiscounts = totalqty * discount;
            float totaldiscountformat = Float.parseFloat(df2.format(totaldiscounts));

            float vats = includevat / 100;
            float itemvat = tpwdisc * vats;
            float itemvatfromat = Float.parseFloat(df2.format(itemvat));

            float totalvat = itemvatfromat * totalqty;
            float totalvatformat = Float.parseFloat(df2.format(totalvat));

            float nettotal = nettotaltpformat + totalvat - totaldiscountformat;
            float nettotalformat = Float.parseFloat(df2.format(nettotal));

            if (itemcode.equals("")) {
                // JOptionPane.showMessageDialog(null, "Enter Invoice Details First");
            } else if (barcode.equals(s)) {
                exists = true;

                //update
                returndataTable.setValueAt(totalqty, i, 4);
                returndataTable.setValueAt(totaldiscountformat, i, 5);
                returndataTable.setValueAt(nettotaltpformat, i, 7);
                returndataTable.setValueAt(totalvatformat, i, 8);
                returndataTable.setValueAt(nettotalformat, i, 9);

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
            // JOptionPane.showMessageDialog(null, "This Data Already Exist.");
            clear();

        }
        codetext.requestFocusInWindow();
    }

    private void entryData_return() {
        float discount;
        String discounttype;
        float vat = includevat / 100;
        float totalvat, productvat;

        DefaultTableModel model2 = (DefaultTableModel) returndataTable.getModel();
        treereturn++;

        float tpd = Float.parseFloat(unitrateText.getText());

        if (discounttext.getText().isEmpty()) {

            discount = 0;
            discounttype = " ";

        } else {
            discounttype = (String) discounttypebox.getSelectedItem();
            float discountpersantage = Float.parseFloat(discounttext.getText());
            if (discounttypebox.getSelectedIndex() == 0) {
                float discountvalue = discountpersantage / 100;
                discount = tpd * discountvalue;

            } else {
                discount = Float.parseFloat(discounttext.getText());

            }
        }

        float tpwdisc = tpd - discount;
        productvat = (tpwdisc * vat);
        float productvatfloat = Float.parseFloat(df2.format(productvat));

        float qty = Float.parseFloat(qtytext.getText());
        float totaldics = discount * qty;
        float totaldiscountformat = Float.parseFloat(df2.format(totaldics));

        float nettotaltp = (tpd * qty);
        totalvat = (productvatfloat * qty);

        float totalvatfloat = Float.parseFloat(df2.format(totalvat));
        float totalall = nettotaltp + totalvatfloat - totaldiscountformat;

        //String gpper = String.format("%.2f", gp);
        model2.addRow(new Object[]{treereturn, itemcode, itemname, tpd, qty, totaldiscountformat, discounttype, nettotaltp, totalvatfloat, totalall});
        ///  totalrate.setText(Integer.toString(total_action_rate()));

        double inputotalrate = total_action_mrp_return();
        totalrateReturn.setText(df2.format(inputotalrate));
        netdiscountreturn.setText(Float.toString(total_discount_amount_return()));
        double inputvat = total_action_vat_return();
        totalvattextreturn.setText(df2.format(inputvat));
        double input = total_invoice_amount_return();
        totalinvoiceamountreturn.setText(df2.format(input));
        clear();
    }

    private void Table_Update_Element() {

        switch (updatekey) {
            case 1:
                itempane.setSelectedIndex(0);
                checkUpdate();
                break;
            case 2:
                itempane.setSelectedIndex(2);
                checkUpdatebooking();
                break;
            case 3:
                itempane.setSelectedIndex(3);
                checkUpdate_return();
                break;
            default:
                break;
        }

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
        paidableamountext.setText(df2.format(input));

        float paidtotal = Float.parseFloat(paidableamountext.getText());
        double roundof = Math.abs(paidtotal);
        roundoffamounttext.setText(df3.format(roundof));
        creditCustomerAcount();
        clear();

    }

    private void checkUpdatebooking() {

        DefaultTableModel model = (DefaultTableModel) booktable.getModel();
        int i = booktable.getSelectedRow();
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
        double input = total_invoice_amount_Booking();
        bookamounttext.setText(df2.format(input));
        bookTotalcount();

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

            totaltpmrp = totaltpmrp + Float.parseFloat(returndataTable.getValueAt(i, 7).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_vat_return() {

        int rowaCount = returndataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(returndataTable.getValueAt(i, 8).toString());
        }

        return (float) totaltpmrp;

    }

    private double total_invoice_amount_return() {

        int rowaCount = returndataTable.getRowCount();

        double totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Double.parseDouble(returndataTable.getValueAt(i, 9).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_discount_amount_return() {

        int rowaCount = returndataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(returndataTable.getValueAt(i, 5).toString());
        }

        return (float) totaltpmrp;

    }

    private void salereturnInsert() {
        //double cashout = Double.parseDouble(totalinvoiceamount.getText());
        try {

            returncashamount = Float.parseFloat(totalinvoiceamountreturn.getText());
            switch (paymenttypeboxdetails.getSelectedIndex()) {
                case 0:
                    if (bookReturnStatus == 0) {
                        cashInDrwaerCashReturn(returncashamount);
                    }
                    //customerbalanceupdatecash();
                    break;
                case 1:
                    //customerbalanceupdate();
                    break;
                default:
                    break;
            }
            String sql = "Insert into salereturn(ReturnNo,invoiceNo,invoicetype,returndate,TotalAmount,TotalVat,Totalinvoice,returnacash,prevdue,customerid) values (?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, invoicetext.getText());
            pst.setString(2, (String) invoiceNotext.getSelectedItem());
            pst.setString(3, (String) paymenttypeboxdetails.getSelectedItem());
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
            customerbalanceupdatereturn();

            //   JOptionPane.showMessageDialog(null, "Sale Return Successfuly Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void customerbalanceupdatereturn() throws SQLException {
        if (paymenttypeboxdetails.getSelectedIndex() == 0) {
            float todaysale = Float.parseFloat(totalinvoiceamountreturn.getText());

            float aftersaleamt = salesamt - todaysale;

            float aftercashamt = (float) (cashamt - todaysale);

            try {

                String sql = "Update customerInfo set cashamt='" + aftercashamt + "',saleamount='" + aftersaleamt + "' where customerid='" + cusId + "'";
                pst = conn.prepareStatement(sql);

                pst.execute();

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        } else {

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

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);

                String probarcode = (String) returndataTable.getValueAt(row, 1);
                try {
                    String sql = "Select Itemcode,tp,stockunit from item where Itemcode='" + probarcode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {
                        itemcode = rs.getString("Itemcode");
                        tpprice = rs.getFloat("tp");
                        unit = rs.getString("stockunit");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                float rate = (Float) returndataTable.getValueAt(row, 3);
                float qty = (Float) returndataTable.getValueAt(row, 4);
                float discount = (Float) returndataTable.getValueAt(row, 5);
                float itemvat = (rate - (discount / qty)) / qty;
                float totalamount = (Float) returndataTable.getValueAt(row, 7);
                //float vat = (Float) returndataTable.getValueAt(row, 8);
                float totalvat = (Float) returndataTable.getValueAt(row, 8);
                float neTotal = (Float) returndataTable.getValueAt(row, 9);
                try {

                    String sql = "Insert into salereturnDetails(returnNo,prcode,barcode,unitrate,qty,unit,discount,totalamount,vat,Totalvat,NetTotal,returnedate) values (?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, invoicetext.getText());
                    pst.setString(2, itemcode);
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
            float qty = (Float) returndataTable.getValueAt(i, 4);
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
        totalrateReturn.setText(null);
        netdiscountreturn.setText(null);
        totalvattextreturn.setText(null);
        totalinvoiceamountreturn.setText(null);
        cusId = "101";
        customerbox.setSelectedIndex(0);
        totalamounttext.setText(null);
        totalVatText.setText(null);
        netTotalText.setText(null);
        paymenttypeboxdetails.setSelectedIndex(0);
        invoiceNotext.removeAllItems();
        final JTextField textfield = (JTextField) invoiceNotext.getEditor().getEditorComponent();
        textfield.setText(null);
        ReturnInvoice = "0";
        returnCode();

    }
//End return table 

    private void clear() {
        codetext.setText(null);
        itemnamesearch.removeAllItems();
        //itemnamesearch.addItem("");
        //  itemnamesearch.setSelectedIndex(0);
        unitrateText.setText(null);
        qtytext.setText(null);
        discounttext.setText(null);
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
        //  codetext.requestFocusInWindow();
        final JTextField textfield = (JTextField) itemnamesearch.getEditor().getEditorComponent();
        textfield.requestFocusInWindow();
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

            String type = (String) typebox.getSelectedItem();
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
            float profite = totalprofite - discount;

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
                    + "due"
                    + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, new_inv);
            pst.setString(2, invoicetext.getText());
            pst.setDate(3, date);
            pst.setString(4, time);
            pst.setString(5, "AED");
            pst.setString(6, (String) Paymenttype);
            pst.setFloat(7, discountfinel);
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
            pst.execute();
            SaleDetailsInsertcredit();
            //  DiscountExpencess();
            // statementinsert();
            Stockpdate();
            customerbalanceupdate();
            vatcollection();
            itemforcastInsert();
            bookreturnstatusUpdate();
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
            float profite = totalprofite - discount;
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
                    + "due"
                    + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, invoicetext.getText());
            pst.setDate(2, date);
            pst.setString(3, time);
            pst.setString(4, (String) "Tk");
            pst.setString(5, (String) Paymenttype);
            pst.setFloat(6, discountfinel);
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
            pst.execute();

            SaleDetailsInsertcredit();
            //DiscountExpencess();
            bookreturnstatusUpdate();
            // statementinsert();
            Stockpdate();
            customerbalanceupdate();
            vatcollection();
            itemforcastInsert();
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

                    String sql = "Insert into saledetails(invoiceNo,prcode,barcode,unitrate,qty,unit,discount,commesion,totalamount,vat,Totalvat,NetTotal,tradprice,invoicedate,cusid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, invoicetext.getText());
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

    ///end 
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
        } catch (SQLException | JRException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void reciebCashAction() {

        float bankamount = 0;
        float cardamount = 0;
        float mobileamount = 0;
        float cashrecieve = 0;
        float totalrecieve = 0;
        float change = 0;
        float paidableamount = 0;

        if (bankamounttex.getText().isEmpty()) {
            bankamount = 0;

        } else {
            bankamount = Float.parseFloat(bankamounttex.getText());
        }

        if (cardamounttext.getText().isEmpty()) {
            bankamount = 0;

        } else {
            cardamount = Float.parseFloat(cardamounttext.getText());
        }

        if (mobilebankamounttext.getText().isEmpty()) {
            mobileamount = 0;

        } else {
            mobileamount = Float.parseFloat(mobilebankamounttext.getText());
        }

        if (cashrecivetext.getText().isEmpty()) {
            cashrecieve = 0;

        } else {
            cashrecieve = Float.parseFloat(cashrecivetext.getText());
        }

        totalrecieve = bankamount + cardamount + mobileamount + cashrecieve;
        paidableamount = Float.parseFloat(paidableamountext.getText());
        if (totalrecieve >= paidableamount) {

            change = totalrecieve - paidableamount;
            changetext.setText(df2.format(change));

        } else {
            changetext.setText(null);

        }
        paytext.setText(df2.format(totalrecieve));
    }

    private void PaymentSecurity() throws IOException, FileNotFoundException, SQLException {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int emptytbl = model.getRowCount();
        if (nettotaltext.getText().isEmpty() || emptytbl == 0) {
            //JOptionPane.showMessageDialog(null, "hello");
        } else {
            float chasin = Float.parseFloat(paidableamountext.getText());
            float paidamount = Float.parseFloat(paytext.getText());
            float bankamount = 0;
            float cardamount = 0;
            float mobileamount = 0;
            float cashrecieve = 0;

            if (bankamounttex.getText().isEmpty() || cardamounttext.getText().isEmpty() || mobilebankamounttext.getText().isEmpty() || cashrecivetext.getText().isEmpty()) {
                bankamount = 0;
                cardamount = 0;
                mobileamount = 0;
                cashrecieve = 0;
            } else {

                bankamount = Float.parseFloat(bankamounttex.getText());
                cardamount = Float.parseFloat(cardamounttext.getText());
                mobileamount = Float.parseFloat(mobilebankamounttext.getText());
                cashrecieve = Float.parseFloat(cashrecivetext.getText());
            }
            float paidableamount = Float.parseFloat(paidableamountext.getText());

            if (paidamount >= chasin) {
                float totalrecievecash = bankamount + cardamount + mobileamount;
                //cash insert            
                float cashrecieves = paidableamount - totalrecievecash;
                if (cashrecivetext.getText().isEmpty() || cashrecieve == 0) {

                    // cashrecieve=0;
                } else {
                    if (cashrecieves > 0) {
                        cashInDrwaer(cashrecieves);
                    }
                }

                //bank insert
                float totalrecievebank = cardamount + mobileamount + cashrecieve;
                float bankamounts = paidableamount - totalrecievebank;
                if (bankamounttex.getText().isEmpty() || bankamount == 0) {

                    // cashrecieve=0;
                } else {

                    // if (accnotext.getText().isEmpty() || Bankinfobox.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Please Confirm Bank And Acc Information For bank Payment");

                    // } else {
                    if (bankamounts > 0) {
                        paymentinfo();
                        checkBalance(acc);
                        float presentbalnce = bankcashin + bankamounts;
                        String bank = (String) Bankinfobox.getSelectedItem();
                        AccBalance();
                        balanceupdate(acc, presentbalnce);
                        totalbalance();
                        overallinsert(presentbalnce, acc, bank);
                    }

                    //  }   
                }

                //card insert
                float totalrecievecard = bankamount + cardamount + mobileamount + cashrecieve;
                float cardamounts = paidableamount - totalrecievecard;
                if (cardamounttext.getText().isEmpty() || cardamount == 0) {

                    // cashrecieve=0;
                } else {

                    //if (cardnotext.getText().isEmpty() || BankinfoboxCard.getSelectedIndex() == 0) {
                    //      JOptionPane.showMessageDialog(null, "Please Confirm Bank And Card Information For bank Card Payment");
                    // } else {
                    if (cardamounts > 0) {
                        paymentinfo();
                        checkBalance(cardacc);
                        float presentbalnce = bankcashin + cardamounts;
                        String bank = (String) BankinfoboxCard.getSelectedItem();
                        CardBalance();
                        balanceupdate(cardacc, presentbalnce);
                        totalbalance();
                        overallinsert(presentbalnce, cardacc, bank);
                    }
                    //   }  

                }

                //Mobile Insert
                float totalrecievemoobile = bankamount + cardamount + cashrecieve;
                float mobileamounts = paidableamount - totalrecievemoobile;
                if (mobilebankamounttext.getText().isEmpty() || mobileamount == 0) {

                    // cashrecieve=0;
                } else {

                    // if (bkashtext.getText().isEmpty()) {
                    //   JOptionPane.showMessageDialog(null, "Please Confirm Mobile Number for Bkash Payment");
                    // } else {
                    if (mobileamounts > 0) {
                        paymentinfo();
                        mobilebalanceInsert(mobileamounts);
                        mobileBalanceupdate(mobileamounts);
                    }
                    // }

                }
                OrderStatus();
                //bookreturnstatusUpdate();
                float bookreturnamount = Float.parseFloat(bookreturntext.getText());
                if (bookreturnamount > 0) {
                    switch (bookReturnStatus) {
                        case 0:
                            break;
                        case 1:
                            BookupdateStatus();
                            break;
                        default:
                            typebox.setSelectedIndex(3);
                            parchase_code();
                            salereturnInsert();
                            break;
                    }
                }
                typebox.setSelectedIndex(0);
                parchase_code();
                invoicecheck();
                //CashPaymentHistoryInsert();
                AfterExecute();

            } else {

                JOptionPane.showMessageDialog(null, "Please Confirm payment Value");

            }

        }
    }

    private void CashPaymentHistoryInsert() throws SQLException {
        float bankamount = 0;
        float cardamount = 0;
        float mobileamount = 0;
        float cashrecieve = 0;

        if (bankamounttex.getText().isEmpty()) {
            bankamount = 0;

        } else {
            bankamount = Float.parseFloat(bankamounttex.getText());
        }

        if (cardamounttext.getText().isEmpty()) {
            cardamount = 0;

        } else {
            cardamount = Float.parseFloat(cardamounttext.getText());
        }

        if (mobilebankamounttext.getText().isEmpty()) {
            mobileamount = 0;

        } else {
            mobileamount = Float.parseFloat(mobilebankamounttext.getText());
        }

        if (cashrecivetext.getText().isEmpty()) {
            cashrecieve = 0;

        } else {
            cashrecieve = Float.parseFloat(cashrecivetext.getText());
        }

        try {

            String sql = "Insert into cashinvoicepaymenthistory(InvoiceNo,PaydableAmount,CashRecieve,CardRecieve,Bankrecieve,MobileRecieve,TotalRecieve,ChangeAmount,InputDate) values(?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setString(1, invoicetext.getText());
            pst.setString(2, paidableamountext.getText());
            pst.setFloat(3, cashrecieve);
            pst.setFloat(4, cardamount);
            pst.setFloat(5, bankamount);
            pst.setFloat(6, mobileamount);
            pst.setString(7, paytext.getText());
            pst.setString(8, changetext.getText());
            pst.setDate(9, date);
            pst.execute();

            //JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void mobilebalanceInsert(float cashin) throws SQLException {

        try {

            String sql = "Insert into mobilebankbalance(inputdate,Type,mobileNo,remark,cashIn,cashOut,trnid) values(?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setDate(1, date);
            pst.setString(2, (String) mobilebanktype.getSelectedItem());
            pst.setString(3, bkashNo);
            pst.setString(4, "Sale Invoice");
            pst.setFloat(5, cashin);
            pst.setFloat(6, 0);
            pst.setString(7, invoicetext.getText());

            pst.execute();

            //JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void mobileBalanceupdate(float cashin) throws SQLException {

        try {

            String sql = "Insert into mobileno(type,cashIn,Cashout,Mobileno) values(?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setString(1, (String) mobilebanktype.getSelectedItem());
            pst.setFloat(2, cashin);
            pst.setFloat(3, 0);
            pst.setString(4, bkashNo);
            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void balanceupdate(String accountno, float presentbalnce) {
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

    private void overallinsert(float presentbalnce, String AccNo, String bank) {
        String trans = invoicetext.getText();
        float cashin = Float.parseFloat(totalinvoiceamount.getText());
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

            pst.setString(10, "Sale Invoice");
            pst.setString(11, trans);

            pst.execute();

            // JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void saleInsertcheck() throws JRException {
        float chasin = Float.parseFloat(totalinvoiceamount.getText());

        try {
            float totaltd = totaltrade();
            float totalamt = Float.parseFloat(totalrate.getText());
            float totalprofite = totalamt - totaltd;
            float discount = discountfinel;
            float profite = totalprofite - discount;

            String sql = "Insert into cashsale(id,"
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
                    + "Ref_No,"
                    + "totaltraprice,"
                    + "totalprofite,"
                    + "inputuserid,"
                    + "bank,"
                    + "accNo,"
                    + "cardNo,"
                    + "MobileBankingType,"
                    + "MobileNo,"
                    + "recieved,"
                    + "changeamt) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, new_inv);
            pst.setString(2, invoicetext.getText());
            pst.setDate(3, date);
            pst.setString(4, time);
            pst.setString(5, "TK");
            pst.setString(6, paymentType);
            pst.setFloat(7, discountfinel);
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
            pst.setInt(19, bankid);
            pst.setString(20, accnotext.getText());
            pst.setString(21, cardnotext.getText());

            if (mobilebank == 1) {
                pst.setString(22, (String) mobilebanktype.getSelectedItem());
            } else {
                pst.setString(22, " ");
            }
            pst.setString(23, bkashtext.getText());
            pst.setString(24, cashrecivetext.getText());
            pst.setString(25, changetext.getText());
            pst.execute();
            SaleDetailsInsert();
            vatcollection();
            //statementinsert();
            Stockpdate();
            customerbalanceupdate();
            itemforcastInsert();
            // DiscountExpencess();
            printInvoice();

            //JOptionPane.showMessageDialog(null, "Invoice Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void saleInsert() throws JRException {

        try {

            float totaltd = totaltrade();
            float totalamt = Float.parseFloat(totalrate.getText());
            float totalprofite = totalamt - totaltd;
            float discount = discountfinel;
            float profite = totalprofite - discount;

            String sql = "Insert into cashsale(invoiceNo,"
                    + "invoicedate,"
                    + "time,paymentCurency,"
                    + "paymentType,"
                    + "netdiscount,"
                    + "TotalAmount,"
                    + "TotalVat,"
                    + "Totalinvoice,"
                    + "ReturnBookAmount,"
                    + "returnbookingtype,"
                    + "nettotal,"
                    + "customerid,"
                    + "Ref_No,"
                    + "totaltraprice,"
                    + "totalprofite,"
                    + "inputuserid,"
                    + "bank,"
                    + "accNo,"
                    + "cardNo,"
                    + "MobileBankingType,"
                    + "MobileNo,"
                    + "recieved,"
                    + "changeamt) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, invoicetext.getText());
            pst.setDate(2, date);
            pst.setString(3, time);
            pst.setString(4, "TK");
            pst.setString(5, paymentType);
            pst.setFloat(6, discountfinel);
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
            pst.setInt(18, bankid);
            pst.setString(19, accnotext.getText());
            pst.setString(20, cardnotext.getText());
            if (mobilebank == 1) {
                pst.setString(21, (String) mobilebanktype.getSelectedItem());
            } else {
                pst.setString(21, " ");
            }
            pst.setString(22, bkashtext.getText());
            pst.setString(23, cashrecivetext.getText());
            pst.setString(24, changetext.getText());
            pst.execute();
            SaleDetailsInsert();
            vatcollection();
            Stockpdate();
            customerbalanceupdate();
            itemforcastInsert();
            //  DiscountExpencess();
            printInvoice();

            //JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void SaleDetailsInsert() throws SQLException {

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

                    String sql = "Insert into cashsaleDetails(invoiceNo,prcode,barcode,unitrate,qty,unit,discount,commesion,totalamount,vat,Totalvat,NetTotal,tradprice,invoicedate,cusid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, invoicetext.getText());
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

            String sql = "Insert into vatcollection(invoiceNopurchaseNo,invoicedate,paymenttype,vattype,TotalAmount,totaldiscount,TotalVat,Totalinvoice,customerid) values (?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, invoicetext.getText());
            pst.setDate(2, date);

            pst.setString(3, (String) Paymenttype);
            pst.setString(4, "Input");
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

            float rate = (Float) dataTable.getValueAt(row, 3);

            float qty = Float.parseFloat(dataTable.getValueAt(row, 4).toString());
            float totaltrade = qty * tpprice;
            totaltradep = totaltradep + totaltrade;

        }
        return (float) totaltradep;

    }

    private void cashInDrwaer(float cashin) {
        Float balanc = bankbalance1 + cashin;
        try {

            String sql = "Insert into CashDrower(cashin,cashout,balance,type,upatedate,inputuser,tranid) values(?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setFloat(1, cashin);
            pst.setFloat(2, 0);
            pst.setFloat(3, balanc);
            pst.setString(4, "Sale Product");
            pst.setDate(5, date);
            pst.setInt(6, userid);
            pst.setString(7, invoicetext.getText());
            pst.execute();

            //  JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

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
            switch (typebox.getSelectedIndex()) {
                case 0:

                    String recieve = null;
                    String returnpaid = null;
                    recieve = paytext.getText();
                    returnpaid = changetext.getText();
                    String returnst = null;
                    String nettotalst = null;
                    String returnbook = null;

                    returnst = jLabel45.getText();
                    nettotalst = "Net Total";
                    returnbook = bookreturntext.getText();

                    if ("Normal".equals(cashinvoiceprintslip)) {
                        try {
                            String num = nettotal;
                            String inwords = convertToIndianCurrency(num);
                            // String arebic = convertNumberToArabicWords(num);
                            // String convert = arebic;
                            JasperDesign jd = JRXmlLoader.load(new File("")
                                    .getAbsolutePath()
                                    + "/src/Report/Invoice.jrxml");
                            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");
                            HashMap para = new HashMap();
                            para.put("invoiceno", Invoiceno);
                            para.put("inwords", inwords);
                            JasperReport jr = JasperCompileManager.compileReport(jd);
                            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                            JasperPrintManager.printReport(jp, true);
                            //JasperViewer.viewReport(jp, true);

                        } catch (NumberFormatException | JRException ex) {
                            JOptionPane.showMessageDialog(null, ex);

                        }
                    } else {

                        try {
                            JasperDesign jd = JRXmlLoader.load(new File("")
                                    .getAbsolutePath()
                                    + "/src/Report/BilReciept.jrxml");
                            HashMap para = new HashMap();
                            para.put("invoiceno", Invoiceno);
                            para.put("receive", recieve);
                            para.put("return", returnpaid);
                            // para.put("discount", netdiscount.getText());
                            para.put("cashier", username);

                            //return book
                            para.put("returnst", returnst);
                            para.put("netotalst", nettotalst);
                            para.put("returnbook", returnbook);
                            para.put("nettotal", nettotal);

                            JasperReport jr = JasperCompileManager.compileReport(jd);
                            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

                            JasperPrintManager.printReport(jp, true);
                            //JasperViewer.viewReport(jp, true);

                        } catch (NumberFormatException | JRException ex) {
                            JOptionPane.showMessageDialog(null, ex);

                        }

                    }

                    break;
                case 1: {

                    if ("Normal".equals(creditinvoiceprintslip)) {
                        //JOptionPane.showMessageDialog(null, balanceduetext.getText());
                        try {
                            String num = nettotal;
                            //String arebic = convertNumberToArabicWords(num);
                            String inwords = convertToIndianCurrency(num);
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
                    } else {
                        try {
                            JasperDesign jd = JRXmlLoader.load(new File("")
                                    .getAbsolutePath()
                                    + "/src/Report/BilRecieptCredit.jrxml");
                            HashMap para = new HashMap();
                            para.put("invoiceno", Invoiceno);
                            para.put("cusid", cusId);
                            para.put("cashier", username);
                            para.put("prevoiusdue", balanceduetext.getText());
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

                    break;
                }
                case 2: {

                    Invoiceno = invoicetext.getText();
                    try {

                        JasperDesign jd = JRXmlLoader.load(new File("")
                                .getAbsolutePath()
                                + "/src/Report/booking.jrxml");
                        //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");
                        HashMap para = new HashMap();
                        para.put("bookingno", Invoiceno);
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
                    break;
                }
                //return reciept
                default:
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

                    break;
            }
        }

    }

    private void Stockpdate() {
        // int cat=(Integer) categoryBox.getSelectedIndex();
        for (int i = 0; i < dataTable.getRowCount(); i++) {

            String s = dataTable.getValueAt(i, 1).toString().trim();
            float qty = Float.parseFloat(dataTable.getValueAt(i, 4).toString());
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
        if (typebox.getSelectedIndex() == 0) {
            float todaysale = Float.parseFloat(totalinvoiceamount.getText());

            float aftersaleamt = salesamt + todaysale;
            float dicount = discountfinel;
            float todicount = totaldiscount + dicount;
            float aftercashamt = (float) (cashamt + todaysale);

            try {

                String sql = "Update customerInfo set cashamt='" + aftercashamt + "',saleamount='" + aftersaleamt + "',totaldiscount='" + todicount + "' where customerid='" + cusId + "'";
                pst = conn.prepareStatement(sql);

                pst.execute();

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        } else {

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

                customerbox.setSelectedIndex(0);

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
        jPanel4 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jPanel16 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        unitrateText = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        discounttext = new javax.swing.JTextField();
        discounttypebox = new javax.swing.JComboBox<>();
        jPanel18 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        qtytext = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        addbtn = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        orderNobox = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        inputdate = new com.toedter.calendar.JDateChooser();
        jLabel27 = new javax.swing.JLabel();
        invoicetext = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        typebox = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        customerbox = new javax.swing.JComboBox<>();
        customeridtext = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        printertypebox = new javax.swing.JComboBox<>();
        directstore = new javax.swing.JCheckBox();
        printinvoicecheck = new javax.swing.JCheckBox();
        orderbtn = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        voidbtn = new javax.swing.JButton();
        executebtn = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        previceinvoicebox = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel38 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        bankamounttex = new javax.swing.JTextField();
        cardamounttext = new javax.swing.JTextField();
        mobilebankamounttext = new javax.swing.JTextField();
        roundoffamounttext = new javax.swing.JTextField();
        paidableamountext = new javax.swing.JTextField();
        paytext = new javax.swing.JTextField();
        changetext = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        Bankinfobox = new javax.swing.JComboBox<>();
        accnotext = new javax.swing.JTextField();
        BankinfoboxCard = new javax.swing.JComboBox<>();
        cardnotext = new javax.swing.JTextField();
        mobilebanktype = new javax.swing.JComboBox<>();
        bkashtext = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        cashrecivetext = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        itemtables = new javax.swing.JTable();
        itempane = new javax.swing.JTabbedPane();
        jPanel28 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        totalrate = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        netdiscount = new javax.swing.JTextField();
        discountboxtypefinel = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        totalvattext = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        totalinvoiceamount = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        bookreturntext = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        nettotaltext = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        totalqtytext = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descrptiontbl = new javax.swing.JTable();
        jPanel36 = new javax.swing.JPanel();
        searchtext = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        searchtypebox = new javax.swing.JComboBox<>();
        jPanel23 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        bookbtn = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        remaintext = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        bookamounttext = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        advancedtext = new javax.swing.JTextField();
        placebtn = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel30 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        booktable = new javax.swing.JTable();
        jPanel37 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        bookrecieptbtn = new javax.swing.JButton();
        bookbox = new javax.swing.JComboBox<>();
        jLabel30 = new javax.swing.JLabel();
        bookDatetext = new javax.swing.JLabel();
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
        jPanel20 = new javax.swing.JPanel();
        paymenttypeboxdetails = new javax.swing.JComboBox<>();
        jLabel43 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        invoiceNotext = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        totalamounttext = new javax.swing.JLabel();
        netTotalText = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        totalVatText = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jPanel31 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        returndataTable = new javax.swing.JTable();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("POS");
        setExtendedState(6);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(0, 51, 51));

        jPanel12.setBackground(new java.awt.Color(0, 51, 51));
        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Code");

        codetext.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        codetext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        codetext.setBorder(null);
        codetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codetextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetextcodetextKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(codetext, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel11)
                .addGap(1, 1, 1)
                .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel15.setBackground(new java.awt.Color(0, 51, 51));
        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Item Description");

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

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(itemnamesearch, 0, 400, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel10)
                .addGap(1, 1, 1)
                .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );

        jPanel16.setBackground(new java.awt.Color(0, 51, 51));
        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unitrateText)
                    .addComponent(jLabel12))
                .addGap(3, 3, 3))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel17.setBackground(new java.awt.Color(0, 51, 51));
        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        discounttypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "%", "Value" }));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(discounttext, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(discounttypebox, 0, 72, Short.MAX_VALUE)))
                .addGap(3, 3, 3))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(discounttext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(discounttypebox))
                .addGap(5, 5, 5))
        );

        jPanel18.setBackground(new java.awt.Color(0, 51, 51));
        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Quantity");

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

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(qtytext, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel14)
                .addGap(1, 1, 1)
                .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel21.setBackground(new java.awt.Color(0, 51, 51));
        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        addbtn.setBackground(new java.awt.Color(255, 255, 255));
        addbtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addbtn.setText("Add");
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

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(addbtn, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel5.setBackground(new java.awt.Color(0, 51, 51));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Invoice No:");

        inputdate.setForeground(new java.awt.Color(102, 102, 102));
        inputdate.setDateFormatString("yyyy-MM-dd");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Date");

        invoicetext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        invoicetext.setForeground(new java.awt.Color(255, 255, 255));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Hold No");

        typebox.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        typebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Credit", "Order", "Sale Return" }));
        typebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                typeboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

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

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Printer");

        printertypebox.setBackground(new java.awt.Color(255, 255, 255));
        printertypebox.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        printertypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Normal", "Thermal" }));
        printertypebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                printertypeboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(typebox, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputdate, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customeridtext, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderNobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(printertypebox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(customeridtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(customerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7))))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(printertypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(orderNobox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(typebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(inputdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(invoicetext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        directstore.setBackground(new java.awt.Color(0, 51, 51));
        directstore.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        directstore.setForeground(new java.awt.Color(255, 255, 255));
        directstore.setText("Direct Store");

        printinvoicecheck.setBackground(new java.awt.Color(0, 51, 51));
        printinvoicecheck.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        printinvoicecheck.setForeground(new java.awt.Color(255, 255, 255));
        printinvoicecheck.setText("Prnt Invoice");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(directstore, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(printinvoicecheck, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(2, 2, 2)
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(directstore)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(printinvoicecheck)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(1, 1, 1))))
        );

        orderbtn.setBackground(new java.awt.Color(0, 153, 0));
        orderbtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        orderbtn.setForeground(new java.awt.Color(255, 255, 255));
        orderbtn.setText("Hold Order");
        orderbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                orderbtnActionPerformed(evt);
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

        jPanel19.setBackground(new java.awt.Color(0, 51, 51));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Previse Invoice:");

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

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(previceinvoicebox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(previceinvoicebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        jScrollPane4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel38.setBackground(new java.awt.Color(0, 51, 51));
        jPanel38.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel16.setBackground(new java.awt.Color(0, 51, 51));
        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Type");
        jLabel16.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Bank:");

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("Card");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Paidable Amount");

        jLabel49.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("Round Off");

        jLabel50.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("Cash(F5)");

        jLabel51.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Mobile Banking");

        jLabel52.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setText("Change Amount");

        jLabel53.setBackground(new java.awt.Color(0, 51, 51));
        jLabel53.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setText("Amount");
        jLabel53.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        bankamounttex.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        bankamounttex.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bankamounttex.setText("0.00");
        bankamounttex.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bankamounttexMouseClicked(evt);
            }
        });
        bankamounttex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bankamounttexKeyReleased(evt);
            }
        });

        cardamounttext.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        cardamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cardamounttext.setText("0.00");
        cardamounttext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardamounttextMouseClicked(evt);
            }
        });
        cardamounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cardamounttextKeyReleased(evt);
            }
        });

        mobilebankamounttext.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        mobilebankamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mobilebankamounttext.setText("0.00");
        mobilebankamounttext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mobilebankamounttextMouseClicked(evt);
            }
        });
        mobilebankamounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mobilebankamounttextKeyReleased(evt);
            }
        });

        roundoffamounttext.setEditable(false);
        roundoffamounttext.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        roundoffamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        roundoffamounttext.setText("0.00");
        roundoffamounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                roundoffamounttextKeyReleased(evt);
            }
        });

        paidableamountext.setEditable(false);
        paidableamountext.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        paidableamountext.setForeground(new java.awt.Color(204, 0, 0));
        paidableamountext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paidableamountext.setText("0.00");

        paytext.setEditable(false);
        paytext.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        paytext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paytext.setText("0.00");
        paytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paytextKeyReleased(evt);
            }
        });

        changetext.setEditable(false);
        changetext.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        changetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel54.setBackground(new java.awt.Color(0, 51, 51));
        jLabel54.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 255, 255));
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setText("Transfer Details");
        jLabel54.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        Bankinfobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        Bankinfobox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                BankinfoboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        BankinfoboxCard.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BankinfoboxCard.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        BankinfoboxCard.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                BankinfoboxCardPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        mobilebanktype.setEditable(true);
        mobilebanktype.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel55.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(255, 255, 255));
        jLabel55.setText("Total Recived");

        cashrecivetext.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        cashrecivetext.setForeground(new java.awt.Color(0, 102, 0));
        cashrecivetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashrecivetext.setText("0.00");
        cashrecivetext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cashrecivetextMouseClicked(evt);
            }
        });
        cashrecivetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cashrecivetextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cashrecivetextKeyReleased(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jButton1.setText("Calculator");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                        .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel34, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8)
                                .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel55, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5))
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                    .addComponent(cardamounttext, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(bankamounttex, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(mobilebankamounttext, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(roundoffamounttext, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(changetext, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(paidableamountext, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cashrecivetext, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(paytext, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(0, 0, 0)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel38Layout.createSequentialGroup()
                            .addComponent(mobilebanktype, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(bkashtext))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel38Layout.createSequentialGroup()
                            .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(BankinfoboxCard, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Bankinfobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(accnotext, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
                                .addComponent(cardnotext))))))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bankamounttex, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Bankinfobox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(accnotext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel38Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cardamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BankinfoboxCard, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cardnotext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mobilebankamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mobilebanktype, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bkashtext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(roundoffamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paidableamountext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashrecivetext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paytext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changetext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane4.setViewportView(jPanel38);

        jPanel3.setLayout(new javax.swing.OverlayLayout(jPanel3));

        jScrollPane5.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane5.setAutoscrolls(true);
        jScrollPane5.setOpaque(true);
        jScrollPane5.setPreferredSize(new java.awt.Dimension(456, 200));

        itemtables.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        itemtables.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl", "Item Code", "Name", "Category", "Tp", "Mrp", "Stock"
            }
        ));
        itemtables.setRowHeight(25);
        itemtables.setShowHorizontalLines(true);
        itemtables.setShowVerticalLines(true);
        itemtables.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemtablesMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(itemtables);
        if (itemtables.getColumnModel().getColumnCount() > 0) {
            itemtables.getColumnModel().getColumn(2).setPreferredWidth(300);
        }

        jPanel3.add(jScrollPane5);

        itempane.setBorder(null);
        itempane.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        itempane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itempaneMouseClicked(evt);
            }
        });

        jScrollPane6.setBackground(new java.awt.Color(255, 255, 255));

        jPanel29.setPreferredSize(new java.awt.Dimension(900, 632));

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane2.setBorder(null);

        dataTable.setAutoCreateRowSorter(true);
        dataTable.setBorder(null);
        dataTable.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL.No", "ItemCode", "Descripion", "Unit Rate", "Qty", "Discount", "Net Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false, false
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
            dataTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            dataTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(300);
            dataTable.getColumnModel().getColumn(4).setPreferredWidth(50);
        }

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1011, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 632, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jScrollPane6.setViewportView(jPanel29);

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
                    .addComponent(totalrate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jPanel13.setBackground(new java.awt.Color(0, 51, 51));
        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setBackground(new java.awt.Color(0, 51, 51));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Discount");

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
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(3, 3, 3))
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(netdiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(discountboxtypefinel, 0, 56, Short.MAX_VALUE)
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
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(totalvattext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                    .addComponent(totalinvoiceamount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalinvoiceamount, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel32.setBackground(new java.awt.Color(0, 51, 51));
        jPanel32.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel45.setBackground(new java.awt.Color(0, 51, 51));
        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("Order/Return");

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
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                    .addComponent(bookreturntext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
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
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                    .addComponent(nettotaltext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nettotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
        jLabel15.setText("Item");

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

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel32, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(423, Short.MAX_VALUE))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        itempane.addTab("Item Description", jPanel24);

        jPanel23.setBackground(new java.awt.Color(0, 51, 51));
        jPanel23.setEnabled(false);

        jPanel26.setBackground(new java.awt.Color(0, 51, 51));

        bookbtn.setBackground(new java.awt.Color(0, 102, 102));
        bookbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bookbtn.setForeground(new java.awt.Color(255, 255, 255));
        bookbtn.setText("Order");
        bookbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookbtnActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Amount");

        remaintext.setEditable(false);
        remaintext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Remain");

        bookamounttext.setEditable(false);
        bookamounttext.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Advanced");

        advancedtext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        advancedtext.setText("0");
        advancedtext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advancedtextActionPerformed(evt);
            }
        });
        advancedtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                advancedtextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                advancedtextKeyReleased(evt);
            }
        });

        placebtn.setBackground(new java.awt.Color(102, 0, 0));
        placebtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        placebtn.setForeground(new java.awt.Color(255, 255, 255));
        placebtn.setText("Placement");
        placebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                placebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bookamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(advancedtext, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(remaintext, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bookbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(placebtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bookbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(placebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bookamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(advancedtext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(remaintext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel30.setPreferredSize(new java.awt.Dimension(900, 657));

        booktable.setAutoCreateRowSorter(true);
        booktable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        booktable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        booktable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL.No", "Barcode", "Descripion", "Unit Rate", "Qty", "Discount", "Type", "Amount", "Vat", "Net Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        booktable.setGridColor(new java.awt.Color(204, 204, 204));
        booktable.setRowHeight(40);
        booktable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                booktableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                booktableMousePressed(evt);
            }
        });
        booktable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                booktableKeyPressed(evt);
            }
        });
        jScrollPane8.setViewportView(booktable);
        if (booktable.getColumnModel().getColumnCount() > 0) {
            booktable.getColumnModel().getColumn(0).setResizable(false);
            booktable.getColumnModel().getColumn(0).setPreferredWidth(50);
            booktable.getColumnModel().getColumn(1).setResizable(false);
            booktable.getColumnModel().getColumn(1).setPreferredWidth(100);
            booktable.getColumnModel().getColumn(2).setResizable(false);
            booktable.getColumnModel().getColumn(2).setPreferredWidth(300);
            booktable.getColumnModel().getColumn(3).setResizable(false);
            booktable.getColumnModel().getColumn(4).setResizable(false);
            booktable.getColumnModel().getColumn(4).setPreferredWidth(100);
            booktable.getColumnModel().getColumn(5).setResizable(false);
            booktable.getColumnModel().getColumn(6).setResizable(false);
            booktable.getColumnModel().getColumn(7).setResizable(false);
            booktable.getColumnModel().getColumn(8).setResizable(false);
            booktable.getColumnModel().getColumn(9).setResizable(false);
        }

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 1011, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
        );

        jScrollPane7.setViewportView(jPanel30);

        jPanel37.setBackground(new java.awt.Color(0, 51, 51));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Order Date:");
        jLabel20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        bookrecieptbtn.setBackground(new java.awt.Color(0, 102, 51));
        bookrecieptbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bookrecieptbtn.setForeground(new java.awt.Color(255, 255, 255));
        bookrecieptbtn.setText("View Reciept");
        bookrecieptbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookrecieptbtnActionPerformed(evt);
            }
        });

        bookbox.setEditable(true);
        bookbox.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bookbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        bookbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                bookboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Order ID");

        bookDatetext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        bookDatetext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bookbox, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bookDatetext, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bookrecieptbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bookrecieptbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bookbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bookDatetext, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 1032, Short.MAX_VALUE)
            .addComponent(jPanel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        itempane.addTab("Order", jPanel23);

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adjustbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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

        jPanel20.setBackground(new java.awt.Color(0, 51, 51));
        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        paymenttypeboxdetails.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        paymenttypeboxdetails.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Credit" }));
        paymenttypeboxdetails.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                paymenttypeboxdetailsPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel43.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Invoice Type:");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel43)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymenttypeboxdetails, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(paymenttypeboxdetails))
                .addContainerGap())
        );

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

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invoiceNotext, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(invoiceNotext)
                    .addComponent(jButton2))
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

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48)
                    .addComponent(jLabel40)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(netTotalText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalVatText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalVatText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(netTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel31.setPreferredSize(new java.awt.Dimension(900, 623));

        returndataTable.setAutoCreateRowSorter(true);
        returndataTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        returndataTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        returndataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL.No", "Barcode", "Descripion", "Unit Rate", "Qty", "Discount", "type", "Amount", "Vat", "Net Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        returndataTable.setGridColor(new java.awt.Color(204, 204, 204));
        returndataTable.setRowHeight(40);
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
        }

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 1011, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
        );

        jScrollPane9.setViewportView(jPanel31);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        itempane.addTab("Sale Return", jPanel25);

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
                .addContainerGap(411, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(367, Short.MAX_VALUE))
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

        jPanel3.add(itempane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(executebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(orderbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(voidbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(533, 533, 533)
                        .addComponent(executebtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(orderbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(voidbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void InsertData() {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int emptytbl = model.getRowCount();
        if (invoicetext.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Please Check Invoice No");
        } else if (emptytbl > 0) {
            //   saleInsert();
            invoicecheck();

            AfterExecute();

        }

    }

    private void bookreturnstatusUpdate() throws SQLException {
        switch (bookReturnStatus) {
            case 0:
                break;
            case 1:
                BookupdateStatus();
                break;
            default:
                typebox.setSelectedIndex(3);
                parchase_code();
                salereturnInsert();
                break;
        }

    }
    private void executebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executebtnActionPerformed

        //  if (nettotal > 0 && paidamount >= chasin) {
        if (saleupdate == 0) {
            if (netdiscount.getText().isEmpty()) {
                discountfinel = total_discount_amount();
                netdiscount.setText(df2.format(discountfinel));
            }

            inputDate();
            parchase_code();

            try {
                balance();
            } catch (SQLException ex) {
                Logger.getLogger(CashInvoice.class.getName()).log(Level.SEVERE, null, ex);
            }
            switch (typebox.getSelectedIndex()) {
                case 0:
                    float nettotal = Float.parseFloat(paidableamountext.getText());
                    float chasin;
                    float paidamount;
                    if (paidableamountext.getText().isEmpty()) {
                        chasin = 0;
                    } else {
                        chasin = Float.parseFloat(paidableamountext.getText());
                    }

                    if (paytext.getText().isEmpty()) {
                        paidamount = 0;
                    } else {
                        paidamount = Float.parseFloat(paytext.getText());
                    }
                    if (nettotal > 0 && paidamount >= chasin) {
                        try {
                            int p = JOptionPane.showConfirmDialog(null, "Do you really want to Save This Cash Invoice", "Invoice Exexcution", JOptionPane.YES_NO_OPTION);
                            if (p == 0) {
                                PaymentSecurity();
                            }
                        } catch (IOException | SQLException ex) {
                            Logger.getLogger(PointOfSale.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please Confirm payment Value");
                    }
                    break;
                case 1:
                    int p = JOptionPane.showConfirmDialog(null, "Do you really want to Save This Credit Invoice", "Invoice Exexcution", JOptionPane.YES_NO_OPTION);
                    if (p == 0) {
                        if (customerbox.getSelectedIndex() > 0) {
                            creditCustomerAcount();
                            //invoicecheck();
                            invoicecheckcredit();

                        } else {
                            JOptionPane.showMessageDialog(null, "Please Select Customer Information");

                        }
                    }
                    break;
                default:
                    break;
            }
            // tablenobox.setSelectedIndex(0);
            codetext.requestFocusInWindow();
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
                    Logger.getLogger(PointOfSale.class.getName()).log(Level.SEVERE, null, ex);
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
                typebox.setSelectedItem(type);
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
        paidableamountext.setText(df2.format(nettotalwbr));
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
                        Logger.getLogger(PointOfSale.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }//GEN-LAST:event_voidbtnActionPerformed

    private void netdiscountKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_netdiscountKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

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
                    paidableamountext.setText(df2.format(nettotalwbr));
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
                    paidableamountext.setText(df2.format(nettotalwbr));
                }

            }

        }
    }//GEN-LAST:event_netdiscountKeyPressed

    private void codetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyPressed

        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            if (codetext.getText().isEmpty() || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
            } else {
                parchase_code();
                try {

                    //CheckEntryFilter();
                    NormalItem(itemcode);
                    //  checkentry();

                } catch (NumberFormatException e) {
                    JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Quantity Validation", JOptionPane.CANCEL_OPTION);
                    qtytext.setText(null);
                }

                //  codetext.requestFocusInWindow();
            }

        }
        float nettotal = Float.parseFloat(paidableamountext.getText());
        if (evt.getKeyCode() != KeyEvent.VK_F5) {

        } else {
            if (nettotal > 0) {
                //cashpanel.setSelectedIndex(0);
                cashrecivetext.requestFocusInWindow();
                cashrecivetext.setText(null);
            }

        }
        if (evt.getKeyCode() != KeyEvent.VK_F4) {

        } else {
            //cashpanel.setSelectedIndex(0);
            if (nettotal > 0) {
                netdiscount.requestFocusInWindow();
                netdiscount.setText(null);
            }

        }

    }//GEN-LAST:event_codetextKeyPressed
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
        try {

            String sql = "Select ita.itemcode as 'itemcode' ,ita.barcode as 'barcode',ita.mrp as 'mrp',ita.itemName as 'itemName',ita.vat as 'vat' FROM item ita where ita.Itemcode='" + itcode + "'";
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
                codetext.requestFocusInWindow();

            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

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
                codetext.requestFocusInWindow();

            }

        } catch (SQLException ex) {
            //  JOptionPane.showMessageDialog(null, ex);
        }

    }
    private void codetextcodetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextcodetextKeyReleased
        if (codetext.getText().isEmpty()) {
            jScrollPane5.hide();
            itemnamesearch.removeAllItems();
            itemnamesearch.addItem("");
            itemnamesearch.setSelectedIndex(0);
            unitrateText.setText(null);
            discounttext.setText(null);
            qtytext.setText(null);
            //codetext.requestFocusInWindow();
            //  colorbox.setSelectedIndex(0);
            //  sizetext.setText(null);

        } else {
            jScrollPane5.show();
            updatekey = 0;
            DefaultTableModel model2 = (DefaultTableModel) itemtables.getModel();
            model2.setNumRows(0);
            tree = 0;
            String searctexthere = codetext.getText();
            String sql = null;
            try {
                sql = "Select bp.barcode as 'barcode',bp.itemcode as 'itemcode',bp.itemName as 'itemname' ,bp.mrp as 'mrp',(select Qty from stockdetails st where bp.Itemcode=st.itemcode) as 'stock' from item bp where bp.itemName LIKE '%" + searctexthere + "%' ";

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
                    model2.addRow(new Object[]{tree, itemcodes, itemnam, itemmrp, stock});

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            String bcode = codetext.getText();
            // JOptionPane.showMessageDialog(null, offer);
            // NormalItemCodeRealesd(bcode);

        }


    }//GEN-LAST:event_codetextcodetextKeyReleased

    private void unitrateTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateTextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();

        }
    }//GEN-LAST:event_unitrateTextKeyPressed

    private void discounttextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discounttextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();

        }
    }//GEN-LAST:event_discounttextKeyPressed

    private void qtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            if (codetext.getText().isEmpty() || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
                qtytext.setForeground(Color.red);
                // qtytext.setToolTipText("Please Fillup Code box OR Item name Or qty Or Unit Fields");

            } else {
                qtytext.setForeground(Color.black);
                parchase_code();

                try {

                    CheckEntryFilter();
                    //  checkentry();

                } catch (NumberFormatException e) {
                    JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Quantity Validation", JOptionPane.CANCEL_OPTION);
                    qtytext.setText(null);
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

        if (codetext.getText().isEmpty() || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
        } else {
            parchase_code();

            try {

                CheckEntryFilter();
                //  checkentry();

            } catch (NumberFormatException e) {
                JOptionPane.showConfirmDialog(null, "Please enter numbers only", "Quantity Validation", JOptionPane.CANCEL_OPTION);
                qtytext.setText(null);
            }

            codetext.requestFocusInWindow();

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
            codetext.requestFocusInWindow();
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
            Logger.getLogger(PointOfSale.class.getName()).log(Level.SEVERE, null, ex);
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
                JOptionPane.showMessageDialog(null, "This Number Is Already Use");

            }
            try {
                customerid();
            } catch (SQLException ex) {
                Logger.getLogger(PointOfSale.class.getName()).log(Level.SEVERE, null, ex);
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
        //cashamt = rs.getFloat("cashamt")

        //entrucesure();
        if (customerbox.getSelectedIndex() > 0) {

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

                    codetext.requestFocusInWindow();
                } else {
                    customerbox.setSelectedIndex(0);

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

    private void typeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_typeboxPopupMenuWillBecomeInvisible

        parchase_code();


    }//GEN-LAST:event_typeboxPopupMenuWillBecomeInvisible

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        updatekey = 1;
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int selectedRowIndex = dataTable.getSelectedRow();

        String probarocde = model.getValueAt(selectedRowIndex, 1).toString();
        try {
            String sql = "Select vat as 'Vat' from item WHERE Itemcode='" + probarocde + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {

                includevat = rs.getFloat("Vat");

            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        float qtys = Float.parseFloat((model.getValueAt(selectedRowIndex, 4).toString()));
        float discount = Float.parseFloat((model.getValueAt(selectedRowIndex, 5).toString()));
        float indiscount = discount / qtys;

        discountamt = df2.format(indiscount);
    }//GEN-LAST:event_dataTableMouseClicked

    private void booktableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_booktableMouseClicked
        /*  DefaultTableModel model = (DefaultTableModel) booktable.getModel();

        int selectedRowIndex = booktable.getSelectedRow();

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
        updatekey = 2;
         */
        // updateSystemProcess();


    }//GEN-LAST:event_booktableMouseClicked

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

    private void booktableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_booktableMousePressed

    }//GEN-LAST:event_booktableMousePressed

    private void advancedtextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advancedtextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_advancedtextActionPerformed

    private void advancedtextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_advancedtextKeyPressed

    }//GEN-LAST:event_advancedtextKeyPressed

    private void advancedtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_advancedtextKeyReleased
        float remain = (float) 0.00;
        float amount = 0;
        float advanced = 0;
        amount = (float) total_invoice_amount_Booking();
        advanced = Float.parseFloat(advancedtext.getText());
        if (amount >= advanced) {
            remain = amount - advanced;
            remaintext.setText(df2.format(remain));
        } else {
            remaintext.setText(null);
        }
    }//GEN-LAST:event_advancedtextKeyReleased
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
    private void bookbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookbtnActionPerformed
        inputDate();
        try {
            balance();
        } catch (SQLException ex) {
            Logger.getLogger(PointOfSale.class.getName()).log(Level.SEVERE, null, ex);
        }
        int rows = booktable.getRowCount();
        if (rows == 0 || bookamounttext.getText().isEmpty() || advancedtext.getText().isEmpty() || remaintext.getText().isEmpty()) {

        } else {
            if (bookupdate == 0) {

                int p = JOptionPane.showConfirmDialog(null, "Do you really want to Save This Order", "Order Exexcution", JOptionPane.YES_NO_OPTION);
                if (p == 0) {
                    try {
                        Bookinsert();
                    } catch (JRException ex) {
                        Logger.getLogger(PointOfSale.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    bookTableClear();
                }

            } else {

                int p = JOptionPane.showConfirmDialog(null, "Do you really want to Update This Order", "Order Exexcution", JOptionPane.YES_NO_OPTION);
                if (p == 0) {
                    try {
                        Bookupdate();
                        bookTableClear();
                    } catch (SQLException ex) {
                        Logger.getLogger(PointOfSale.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }

        }
    }//GEN-LAST:event_bookbtnActionPerformed

    private void bookboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_bookboxPopupMenuWillBecomeInvisible
        if (bookbox.getSelectedIndex() > 0) {
            bookReturnStatus = 0;
            BookView();
            bookbtn.setText("Update Order");
            placebtn.setEnabled(true);
            bookupdate = 1;
            bookrecieptbtn.setEnabled(true);
        } else {
            bookTableClear();

        }
    }//GEN-LAST:event_bookboxPopupMenuWillBecomeInvisible

    private void placebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_placebtnActionPerformed
        int rows = booktable.getRowCount();
        if (rows == 0 || bookamounttext.getText().isEmpty() || advancedtext.getText().isEmpty() || remaintext.getText().isEmpty()) {

        } else {
            switch (bookReturnStatus) {
                case 0:
                    bookPlacement();
                    bookReturnStatus = 1;
                    bookreturnstatus();
                    break;
                case 1:
                    JOptionPane.showMessageDialog(null, "This Order Already Placement in Invoice");
                    typebox.setSelectedIndex(0);
                    parchase_code();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Return Already Adjust in Invoice");
                    typebox.setSelectedIndex(0);
                    parchase_code();
                    break;
            }
        }
    }//GEN-LAST:event_placebtnActionPerformed
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
        paidableamountext.setText(df2.format(nettotalwbr));
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

            invoicetext.setText(NewParchaseCode);
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void returnexecutionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnexecutionActionPerformed
        int rows = returndataTable.getRowCount();
        inputDate();
        // returnCode();
        //typebox.setSelectedIndex(3);  
        parchase_code();
        if (rows == 0 || adjustbtn.getText().isEmpty() || netdiscountreturn.getText().isEmpty() || totalvattextreturn.getText().isEmpty() || totalinvoiceamountreturn.getText().isEmpty()) {

        } else {
            int p = JOptionPane.showConfirmDialog(null, "Do you really want to Return This Items", "Return Exexcution", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    balance();
                } catch (SQLException ex) {
                    Logger.getLogger(CashInvoice.class.getName()).log(Level.SEVERE, null, ex);
                }
                salereturnInsert();
                try {
                    printInvoice();
                } catch (JRException ex) {
                    Logger.getLogger(PointOfSale.class.getName()).log(Level.SEVERE, null, ex);
                }

                returnClear();
            }

        }
    }//GEN-LAST:event_returnexecutionActionPerformed
    private void PrintReciept() {
        String bookid = (String) bookbox.getSelectedItem();

        try {

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/booking.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();
            para.put("bookingno", bookid);
            // para.put("cusid", cusId);
            para.put("cashier", username);
            // para.put("discount", netdiscount.getText());
            // para.put("prevoiusdue", previseamonmttext.getText());

            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

            //JasperPrintManager.printReport(jp, true);
            JasperViewer.viewReport(jp, false);

        } catch (NumberFormatException | JRException ex) {
            JOptionPane.showMessageDialog(null, ex);

        }

    }
    private void bookrecieptbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookrecieptbtnActionPerformed
        PrintReciept();
    }//GEN-LAST:event_bookrecieptbtnActionPerformed

    private void BankinfoboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_BankinfoboxPopupMenuWillBecomeInvisible
        try {
            String sql = "Select id from bankinfo where Bankname='" + Bankinfobox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                bankid = rs.getInt("id");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_BankinfoboxPopupMenuWillBecomeInvisible

    private void BankinfoboxCardPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_BankinfoboxCardPopupMenuWillBecomeInvisible
        try {
            String sql = "Select id from bankinfo where Bankname='" + BankinfoboxCard.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                bankid = rs.getInt("id");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_BankinfoboxCardPopupMenuWillBecomeInvisible

    private void itempaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itempaneMouseClicked
        Item_Tabclick();
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

    private void booktableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_booktableKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_DELETE) {

        } else {
            deleterowbooking();
        }

    }//GEN-LAST:event_booktableKeyPressed

    private void returndataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_returndataTableKeyPressed

        if (evt.getKeyCode() != KeyEvent.VK_DELETE) {

        } else {
            deleterow_return();
        }

    }//GEN-LAST:event_returndataTableKeyPressed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_F6) {

        } else {
            // cashpanel.setSelectedIndex(0);
            paytext.requestFocusInWindow();

        }
    }//GEN-LAST:event_formKeyPressed

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

    private void bankamounttexKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bankamounttexKeyReleased
        if (bankamounttex.getText().matches("^[a-zA-Z]+$")) {

        } else {
            reciebCashAction();

        }
    }//GEN-LAST:event_bankamounttexKeyReleased

    private void cardamounttextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cardamounttextKeyReleased
        if (cardamounttext.getText().matches("^[a-zA-Z]+$")) {

        } else {
            reciebCashAction();

        }
    }//GEN-LAST:event_cardamounttextKeyReleased

    private void mobilebankamounttextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mobilebankamounttextKeyReleased
        if (mobilebankamounttext.getText().matches("^[a-zA-Z]+$")) {

        } else {
            reciebCashAction();

        }
    }//GEN-LAST:event_mobilebankamounttextKeyReleased

    private void roundoffamounttextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_roundoffamounttextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_roundoffamounttextKeyReleased

    private void paytextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paytextKeyReleased

    }//GEN-LAST:event_paytextKeyReleased

    private void cashrecivetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cashrecivetextKeyReleased
        if (cashrecivetext.getText().matches("^[a-zA-Z]+$")) {

        } else {
            reciebCashAction();

        }


    }//GEN-LAST:event_cashrecivetextKeyReleased

    private void bankamounttexMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bankamounttexMouseClicked
        bankamounttex.setText(null);
        reciebCashAction();
    }//GEN-LAST:event_bankamounttexMouseClicked

    private void cardamounttextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cardamounttextMouseClicked
        cardamounttext.setText(null);
        reciebCashAction();
    }//GEN-LAST:event_cardamounttextMouseClicked

    private void mobilebankamounttextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mobilebankamounttextMouseClicked
        mobilebankamounttext.setText(null);
        reciebCashAction();
    }//GEN-LAST:event_mobilebankamounttextMouseClicked

    private void cashrecivetextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cashrecivetextMouseClicked
        cashrecivetext.setText(null);
        reciebCashAction();
    }//GEN-LAST:event_cashrecivetextMouseClicked

    private void customeridtextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customeridtextMouseClicked
        customeridtext.setText(null);
        customerbox.setSelectedIndex(0);
    }//GEN-LAST:event_customeridtextMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Calculator calc = null;
        calc = new Calculator();
        calc.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
        paidableamountext.setText(df2.format(nettotalwbr));
    }//GEN-LAST:event_netdiscountMouseClicked

    private void cashrecivetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cashrecivetextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            if (saleupdate == 0) {
                if (netdiscount.getText().isEmpty()) {
                    discountfinel = total_discount_amount();
                    netdiscount.setText(df2.format(discountfinel));
                }
                inputDate();
                parchase_code();

                try {
                    balance();
                } catch (SQLException ex) {
                    Logger.getLogger(CashInvoice.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    int p = JOptionPane.showConfirmDialog(null, "Do you really want to Save This Cash Invoice", "Invoice Exexcution", JOptionPane.YES_NO_OPTION);
                    if (p == 0) {
                        PaymentSecurity();
                    }
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(PointOfSale.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_cashrecivetextKeyPressed
    private void invoicecashsaleview() {
        tree = 0;
        try {
            DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
            model2.setRowCount(0);
            // int slNo = 0;
            String sql = "Select prcode,"
                    + "(select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item',"
                    + " sl.unitrate as 'UnitRate', "
                    + "sl.qty as 'Qty', "
                    + "sl.discount as 'Discount',"
                    + " sl.totalamount as 'Amount',"
                    + " sl.Totalvat as 'TotalVat',"
                    + "sl.NetTotal  as 'NetTotal'"
                    + " from cashsaledetails sl where sl.invoiceNo='" + previceinvoicebox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            // datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String probarcode = rs.getString("prcode");
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
            String sql = "Select invoiceNo,customerid,(select Bankname from bankinfo bin where bin.id=sl.bank)as 'bank',MobileBankingType,MobileNo from cashsale sl where sl.invoiceNo='" + previceinvoicebox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            // datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                cusId = rs.getString("customerid");
                String bank = rs.getString("bank");
                BankinfoboxCard.setSelectedItem(bank);
                String MobileBankingType = rs.getString("MobileBankingType");
                mobilebanktype.setSelectedItem(MobileBankingType);
                String MobileNo = rs.getString("MobileNo");
                bkashtext.setText(MobileNo);
                //  String type=rs.getString("type");
                // typebox.setSelectedItem(type);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {
            String sql = "Select CashRecieve,"
                    + "MobileRecieve,"
                    + "TotalRecieve,"
                    + "realcash,"
                    + "ChangeAmount from cashinvoicepaymenthistory where InvoiceNo='" + previceinvoicebox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            // datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                float cashrecieve = rs.getFloat("CashRecieve");
                cashrecivetext.setText(df2.format(cashrecieve));

                float mobilereciev = rs.getFloat("MobileRecieve");
                mobilebankamounttext.setText(df2.format(mobilereciev));
                float totalrecieve = rs.getFloat("TotalRecieve");
                paytext.setText(df2.format(totalrecieve));
                float changeamt = rs.getFloat("ChangeAmount");
                changetext.setText(df2.format(changeamt));
                realcashamt = rs.getFloat("realcash");
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

        paidableamountext.setText(df2.format(input));
        // parchase_code(); 
        customerCheck();

    }

    private void invoicecreditsaleview() {
        tree = 0;

        try {
            DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
            model2.setRowCount(0);
            // int slNo = 0;
            String sql = "Select prcode ,(select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item', sl.unitrate as 'UnitRate', sl.qty as 'Qty', sl.discount as 'Discount', sl.totalamount as 'Amount', sl.Totalvat as 'TotalVat',sl.NetTotal  as 'NetTotal' from saledetails sl where sl.invoiceNo='" + previceinvoicebox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            // datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String probarcode = rs.getString("prcode");
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
        paidableamountext.setText(df2.format(input));
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

    private void mobilebalanceInsertVoid(float cashout) throws SQLException {

        try {

            String sql = "Insert into mobilebankbalance(inputdate,Type,mobileNo,remark,cashIn,cashOut,trnid) values(?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setDate(1, date);
            pst.setString(2, (String) mobilebanktype.getSelectedItem());
            pst.setString(3, bkashNo);
            pst.setString(4, "Void Invoice");
            pst.setFloat(5, 0);
            pst.setFloat(6, cashout);
            pst.setString(7, invoicetext.getText());

            pst.execute();

            //JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void mobileBalanceupdateVoid(float cashin) throws SQLException {

        try {

            String sql = "Insert into mobileno(type,cashIn,Cashout,Mobileno) values(?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setString(1, (String) mobilebanktype.getSelectedItem());
            pst.setFloat(2, 0);
            pst.setFloat(3, cashin);
            pst.setString(4, bkashNo);
            pst.execute();

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
            pst.setString(17, cardnotext.getText());
            if (mobilebank == 1) {
                pst.setString(18, (String) mobilebanktype.getSelectedItem());
            } else {
                pst.setString(18, " ");
            }
            pst.setString(19, bkashtext.getText());
            pst.setString(20, (String) typebox.getSelectedItem());
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
            float qty = (Float) dataTable.getValueAt(i, 4);
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
        if (typebox.getSelectedIndex() == 0) {
            float todaysale = Float.parseFloat(nettotaltext.getText());
            float aftersaleamt = salesamt - todaysale;
            float aftercashamt = (float) (cashamt - todaysale);
            try {

                String sql = "Update customerInfo set cashamt='" + aftercashamt + "',saleamount='" + aftersaleamt + "' where customerid='" + cusId + "'";
                pst = conn.prepareStatement(sql);

                pst.execute();

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        } else {

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

        if (typebox.getSelectedIndex() == 0) {
            cashamts = Float.parseFloat(cashrecivetext.getText());
            cardamts = Float.parseFloat(cardamounttext.getText());
            mobileamt = Float.parseFloat(mobilebankamounttext.getText());
            if (cashamts > 0) {
                balance();
                cashInDrwaerCashVoid(realcashamt);
            }
            if (cardamts > 0) {
                paymentinfo();
                checkBalance(cardacc);
                float presentbalnce = bankcashin - cardamts;
                String bank = (String) BankinfoboxCard.getSelectedItem();
                CardBalance();
                balanceupdateVoid(cardacc, presentbalnce);
                totalbalance();
                overallinsertVoid(presentbalnce, cardacc, bank);
            }
            if (mobileamt > 0) {
                paymentinfo();
                mobilebalanceInsertVoid(mobileamt);
                mobileBalanceupdateVoid(mobileamt);

            }
            CashsaleDelete();
        } else {
            saleDelete();

        }
        VoidInsert();
        AfterExecute();

    }

    private void PanelActiveDeiactive() {
        if (previceinvoicebox.getSelectedIndex() > 0) {
            panelactive();
            //cardamounttext.setEnabled(true);
            //BankinfoboxCard.setEnabled(true);
            // mobilebankamounttext.setEnabled(true);
            // mobilebanktype.setEnabled(true);
            cashrecivetext.setEnabled(true);
            codetext.setEnabled(true);
            voidbtn.setEnabled(false);
        } else {
            entrucesure();
            //cardamounttext.setEnabled(false);
            //BankinfoboxCard.setEnabled(false);
            //mobilebankamounttext.setEnabled(false);
            //mobilebanktype.setEnabled(false);
            cashrecivetext.setEnabled(false);
            codetext.setEnabled(false);
            voidbtn.setEnabled(true);
        }
    }


    private void previceinvoiceboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_previceinvoiceboxPopupMenuWillBecomeInvisible
        // PanelActiveDeiactive();
        if (previceinvoicebox.getSelectedIndex() > 0) {
            addbtn.setEnabled(false);
            // cardamounttext.setEnabled(false);
            //BankinfoboxCard.setEnabled(false);
            ///mobilebankamounttext.setEnabled(false);
            //mobilebanktype.setEnabled(false);
            cashrecivetext.setEnabled(false);
            saleupdate = 1;
            if (typebox.getSelectedIndex() == 0) {
                invoicecashsaleview();
            } else {
                invoicecreditsaleview();

            }
        } else {
            addbtn.setEnabled(true);
            cashrecivetext.setEnabled(true);

            if (saleupdate > 0) {
                DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
                model2.setRowCount(0);
                totalqtytext.setText(null);
                totalrate.setText(null);
                totalvattext.setText(null);
                netdiscount.setText(null);
                nettotaltext.setText(null);
                paidableamountext.setText(null);
                //cash box clear
                cardamounttext.setText("0.00");
                mobilebankamounttext.setText("0.00");
                roundoffamounttext.setText("0.00");
                paidableamountext.setText("0.00");
                cashrecivetext.setText("0.00");
                paytext.setText("0.00");
                changetext.setText(null);
            }

//Customer Box
            customerbox.setSelectedIndex(0);
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
            paidableamountext.setText(df2.format(nettotalwbr));
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
                paidableamountext.setText(df2.format(nettotalwbr));
            } else {
                double totalqty = total_qty();
                double tvat = total_action_vat();
                double pervat = tvat / totalqty;
                double change = amount - discountfinel;
                //double inputvat = total_action_vat();
                double totalvat = change * 0;
                double totalamount = change + totalvat;
                //  String gpper = String.format("%.2f", productvat);
                totalvattext.setText(df2.format(totalvat));
                totalinvoiceamount.setBackground(Color.YELLOW);
                totalinvoiceamount.setText(String.format("%.2f", totalamount));
                double nettotalwbr = totalamount - bookReturn;
                nettotaltext.setText(df2.format(nettotalwbr));
                paidableamountext.setText(df2.format(nettotalwbr));
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

    private void printertypeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_printertypeboxPopupMenuWillBecomeInvisible
        try {
            String sql = "Update recieptmsg set "
                    + "cashinvoice='" + printertypebox.getSelectedItem() + "',"
                    + "creditinvoice='" + printertypebox.getSelectedItem() + "'"
                    + "where id=1";
            pst = conn.prepareStatement(sql);
            pst.execute();
            recieptmsg();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_printertypeboxPopupMenuWillBecomeInvisible

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
                    codetext.setText(itemcode);
                    //NormalItem(itemcode);

                    NormalItem(itemcode);

                } else {
                    codetext.setText(null);
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

    private void paymenttypeboxdetailsPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_paymenttypeboxdetailsPopupMenuWillBecomeInvisible
        totalamounttext.setText(null);
        totalVatText.setText(null);
        netTotalText.setText(null);
    }//GEN-LAST:event_paymenttypeboxdetailsPopupMenuWillBecomeInvisible
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
            if (paymenttypeboxdetails.getSelectedIndex() == 0) {
                String sql = "Select DISTINCT invoiceNo from cashsale WHERE lower(invoiceNo)  LIKE '%" + enteredText + "%' ORDER BY invoiceNo ASC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    str1 = rs.getString("invoiceNo");
                    filterArray.add(str1);
                    invoiceNotext.addItem(str1);
                }
            } else {
                String sql = "Select DISTINCT invoiceNo from sale WHERE lower(invoiceNo)  LIKE '%" + enteredText + "%' ORDER BY invoiceNo ASC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    str1 = rs.getString("invoiceNo");
                    filterArray.add(str1);
                    invoiceNotext.addItem(str1);
                }
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
        String invoiceNo = (String) invoiceNotext.getSelectedItem();
        if (paymenttypeboxdetails.getSelectedIndex() == 0) {
            try {
                String sql = "Select "
                        + "invoiceNo,TotalAmount,"
                        + "TotalVat,"
                        + "Totalinvoice,"
                        + "customerid from cashsale sl  where sl.invoiceNo='" + invoiceNo + "'";
                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();
                if (rs.next()) {
                    ReturnInvoice = rs.getString("invoiceNo");
                    String totalam = rs.getString("TotalAmount");
                    totalamounttext.setText(totalam);
                    String tovat = rs.getString("TotalVat");
                    totalVatText.setText(tovat);
                    double netinoice = rs.getDouble("Totalinvoice");
                    netTotalText.setText(df2.format(netinoice));
                    //netTotalText.setText(netinoice);
                    cusId = rs.getString("customerid");
                    customerCheck();

                    //  currency=rs.getString("paymentCurency");
                } else {
                    totalamounttext.setText(null);
                    totalVatText.setText(null);
                    netTotalText.setText(null);
                    cusId = "101";
                    customerCheck();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        } else {

            try {
                String sql = "Select invoiceNo,TotalAmount,TotalVat,Totalinvoice,customerid from sale sl  where sl.invoiceNo='" + invoiceNo + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
                    ReturnInvoice = rs.getString("invoiceNo");
                    String totalam = rs.getString("TotalAmount");
                    totalamounttext.setText(totalam);
                    String tovat = rs.getString("TotalVat");
                    totalVatText.setText(tovat);
                    double netinoice = rs.getDouble("Totalinvoice");
                    netTotalText.setText(df2.format(netinoice));
                    cusId = rs.getString("customerid");
                    customerCheck();

                    //   currency=rs.getString("paymentCurency");
                } else {
                    totalamounttext.setText(null);
                    totalVatText.setText(null);
                    netTotalText.setText(null);
                    cusId = "101";
                    customerCheck();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }

    }//GEN-LAST:event_invoiceNotextPopupMenuWillBecomeInvisible

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        final JTextField textfield = (JTextField) invoiceNotext.getEditor().getEditorComponent();
        if ("0".equals(ReturnInvoice) || textfield.getText().isEmpty()) {

        } else {
            if (paymenttypeboxdetails.getSelectedIndex() == 0) {
                if (printertypebox.getSelectedItem() == "Normal") {
                    try {
                        String num = netTotalText.getText();
                        String inwords = convertToIndianCurrency(num);
                        // String arebic = convertNumberToArabicWords(num);
                        // String convert = arebic;
                        JasperDesign jd = JRXmlLoader.load(new File("")
                                .getAbsolutePath()
                                + "/src/Report/Invoice.jrxml");
                        //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");
                        HashMap para = new HashMap();
                        para.put("invoiceno", invoiceNotext.getSelectedItem());
                        para.put("inwords", inwords);
                        JasperReport jr = JasperCompileManager.compileReport(jd);
                        JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                        //  JasperPrintManager.printReport(jp, true);
                        JasperViewer.viewReport(jp, false);

                    } catch (NumberFormatException | JRException ex) {
                        JOptionPane.showMessageDialog(null, ex);

                    }
                } else {
                    try {
                        JasperDesign jd = JRXmlLoader.load(new File("")
                                .getAbsolutePath()
                                + "/src/Report/BilReciept.jrxml");
                        HashMap para = new HashMap();
                        para.put("invoiceno", invoiceNotext.getSelectedItem());
                        para.put("receive", netTotalText.getText());
                        para.put("return", "0.00");
                        para.put("discounttype", "Discount");
                        JasperReport jr = JasperCompileManager.compileReport(jd);
                        JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                        JasperViewer.viewReport(jp, false);

                    } catch (NumberFormatException | JRException ex) {

                        JOptionPane.showMessageDialog(null, ex);

                    }
                }
            } else {
                if (printertypebox.getSelectedItem() == "Normal") {

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
                } else {
                    try {

                        JasperDesign jd = JRXmlLoader.load(new File("")
                                .getAbsolutePath()
                                + "/src/Report/BilRecieptCredit.jrxml");

                        HashMap para = new HashMap();
                        para.put("invoiceno", invoiceNotext.getSelectedItem());
                        //  para.put("receive", totalinvoice);
                        //para.put("return", "0.00");
                        JasperReport jr = JasperCompileManager.compileReport(jd);
                        JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                        JasperViewer.viewReport(jp, false);

                    } catch (NumberFormatException | JRException ex) {

                        JOptionPane.showMessageDialog(null, ex);

                    }
                }
            }
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void itemtablesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemtablesMouseClicked
        DefaultTableModel model2 = (DefaultTableModel) itemtables.getModel();
        int selectedRowIndex = itemtables.getSelectedRow();
        itemcode = (model2.getValueAt(selectedRowIndex, 1).toString());
        NormalItem(itemcode);
        jScrollPane5.hide();


    }//GEN-LAST:event_itemtablesMouseClicked
    private void bookTableClear() {
        bookReturnStatus = 0;
        bookreturnstatus();
        bookamounttext.setText(null);
        advancedtext.setText("0.00");
        remaintext.setText(null);
        DefaultTableModel model2 = (DefaultTableModel) booktable.getModel();
        model2.setRowCount(0);
        book();
        bookbtn.setText("Order");
        placebtn.setEnabled(false);
        bookrecieptbtn.setEnabled(false);
        bookupdate = 0;
        parchase_code();

    }

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
            java.util.logging.Logger.getLogger(PointOfSale.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Bankinfobox;
    private javax.swing.JComboBox<String> BankinfoboxCard;
    private javax.swing.JTextField accnotext;
    private javax.swing.JButton addbtn;
    private javax.swing.JTextArea addresstext;
    private javax.swing.JButton adjustbtn;
    private javax.swing.JTextField advancedtext;
    private javax.swing.JTextField balanceduetext;
    private javax.swing.JTextField bankamounttex;
    private javax.swing.JTextField bkashtext;
    private javax.swing.JLabel bookDatetext;
    private javax.swing.JTextField bookamounttext;
    private javax.swing.JComboBox<String> bookbox;
    private javax.swing.JButton bookbtn;
    private javax.swing.JButton bookrecieptbtn;
    private javax.swing.JLabel bookreturntext;
    private javax.swing.JTable booktable;
    private javax.swing.JTextField cardamounttext;
    private javax.swing.JTextField cardnotext;
    private javax.swing.JTextField cashamounttext;
    private javax.swing.JTextField cashrecivetext;
    private javax.swing.JTextField changetext;
    private javax.swing.JTextField codetext;
    private javax.swing.JTextField contactnotext;
    private javax.swing.JTextField creditamounttext;
    private javax.swing.JComboBox<String> customerbox;
    private javax.swing.JTextField customeridtext;
    private javax.swing.JLabel customeridtextsecond;
    private javax.swing.JTabbedPane customerpane;
    private javax.swing.JTable dataTable;
    private javax.swing.JTable descrptiontbl;
    private javax.swing.JCheckBox directstore;
    private javax.swing.JComboBox<String> discountboxtypefinel;
    private javax.swing.JTextField discounttext;
    private javax.swing.JComboBox<String> discounttypebox;
    private javax.swing.JLabel duelabel;
    private javax.swing.JLabel duelabel1;
    private javax.swing.JLabel duelabel2;
    private javax.swing.JLabel duelabel3;
    private javax.swing.JLabel duelabel4;
    private javax.swing.JLabel duelabel5;
    private javax.swing.JLabel duelabel6;
    private javax.swing.JButton executebtn;
    private com.toedter.calendar.JDateChooser inputdate;
    private javax.swing.JComboBox<String> invoiceNotext;
    private javax.swing.JLabel invoicetext;
    private javax.swing.JPopupMenu itemmenu;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JTabbedPane itempane;
    private javax.swing.JTable itemtables;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton16;
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
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
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
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
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
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField mobilebankamounttext;
    private javax.swing.JComboBox<String> mobilebanktype;
    private javax.swing.JLabel msgtext;
    private javax.swing.JLabel netTotalText;
    private javax.swing.JTextField netdiscount;
    private javax.swing.JLabel netdiscountreturn;
    private javax.swing.JTextField netstext;
    private javax.swing.JLabel nettoalabel;
    private javax.swing.JLabel nettotaltext;
    private javax.swing.JTextField newcustomeridtext;
    private javax.swing.JTextField openingbalancetext;
    private javax.swing.JComboBox<String> orderNobox;
    private javax.swing.JButton orderbtn;
    private javax.swing.JTextField paidableamountext;
    private javax.swing.JComboBox<String> paymenttypeboxdetails;
    private javax.swing.JTextField paytext;
    private javax.swing.JButton placebtn;
    private javax.swing.JComboBox<String> previceinvoicebox;
    private javax.swing.JComboBox<String> printertypebox;
    private javax.swing.JCheckBox printinvoicecheck;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextField remaintext;
    private javax.swing.JTable returndataTable;
    private javax.swing.JButton returnexecution;
    private javax.swing.JTextField roundoffamounttext;
    private javax.swing.JTextField searchtext;
    private javax.swing.JComboBox<String> searchtypebox;
    private javax.swing.JTextField snametext;
    private javax.swing.JLabel totalVatText;
    private javax.swing.JLabel totalamounttext;
    private javax.swing.JTextField totaldiscounttext;
    private javax.swing.JLabel totalinvoiceamount;
    private javax.swing.JLabel totalinvoiceamountreturn;
    private javax.swing.JTextField totalpaymenttext;
    private javax.swing.JLabel totalqtytext;
    private javax.swing.JLabel totalrate;
    private javax.swing.JLabel totalrateReturn;
    private javax.swing.JTextField totalsaletext;
    private javax.swing.JLabel totalvattext;
    private javax.swing.JLabel totalvattextreturn;
    private javax.swing.JComboBox<String> typebox;
    private javax.swing.JTextField unitrateText;
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
