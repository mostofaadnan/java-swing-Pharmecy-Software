/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
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
public final class Expences extends javax.swing.JInternalFrame {

    float bankbalance1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    float bankcashout = 0;
    float bankcashin = 0;
    int bankid;
    float accbalance = 0;
    java.sql.Date date;
    int updatekey = 0;
    float updatebankbalance = 0;
    float totalb = 0;
    String AccNo;
    int paymentstatus = 0;
    float Amounts = 0;
    int userid;

    /**
     * Creates new form Expences
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public Expences() throws SQLException, IOException {
        initComponents();

        conn = Java_Connect.conectrDB();
        AutoCompleteDecorator.decorate(expencestypebox);
        AutoCompleteDecorator.decorate(expensesearchbox);
        currentDate();
        currentdatedata();
        expenstype();
        balance();
        TableDesign();
        transactioncode();
        otherstext.hide();
        jLabel3.hide();
        userkey();
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
    private void transactioncode() {
        try {
            int new_inv = 1;
            String NewParchaseCode = ("EXP-" + new_inv + "");
            String sql = "Select id from expencess";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = ("EXP-" + ParchaseCode + "");
            }

            codetext.setText(NewParchaseCode);
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void TableDesign() {
        JTableHeader jtblheader = datatbl.getTableHeader();
        jtblheader.setBackground(Color.red);
        jtblheader.setForeground(Color.BLACK);
        jtblheader.setFont(new Font("Times New Roman", Font.BOLD, 16));
        ((DefaultTableCellRenderer) jtblheader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        datatbl.setDefaultRenderer(Object.class, centerRenderer);
    }

    private void currentDate() {
        /*  java.util.Date now = new java.util.Date();
         java.sql.Date date = new java.sql.Date(now.getTime());
         inputdate.setDate(date);
         date = new java.sql.Date(now.getTime());
         */

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

    private void expenstype() throws SQLException {

        try {
            String sql = "Select typename from Expencesstype";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("typename");
                expencestypebox.addItem(name);
                expensesearchbox.addItem(name);
            }
            expencestypebox.addItem("Others");
            expensesearchbox.addItem("Others");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void addStock() throws SQLException {
        float cashout = Float.parseFloat(amounttext.getText());
        if (cashout > bankbalance1) {
            JOptionPane.showMessageDialog(null, "Not Save! Lack Of Cash Box Balance ");
        } else {

            String reason = (String) expencestypebox.getSelectedItem();
            try {

                String sql = "Insert into Expencess(expid,inputdate,extype,amount,paymenttype,remark,status,other,userid) values(?,?,?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, codetext.getText());
                pst.setDate(2, date);
                pst.setString(3, (String) expencestypebox.getSelectedItem());
                pst.setString(4, amounttext.getText());
                pst.setString(5, (String) paymenttypebox.getSelectedItem());
                pst.setString(6, remarktext.getText());
                pst.setString(7, (String) statusbox.getSelectedItem());
                pst.setString(8, otherstext.getText());
                pst.setInt(9, userid);
                pst.execute();

                cashInDrwaer(cashout, reason);
                JOptionPane.showMessageDialog(null, "Data Saved");

                clear();
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }

    private void cashInDrwaer(float cashout, String reason) {
        float balance = bankbalance1 - cashout;
        try {

            String sql = "Insert into CashDrower(cashin,cashout,balance,type,upatedate,tranid) values(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setFloat(1, 0);
            pst.setFloat(2, cashout);
            pst.setFloat(3, balance);
            pst.setString(4, "Expensess," + reason);
            pst.setDate(5, date);
            pst.setString(6, codetext.getText());
            pst.execute();

            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
//bank section

    private void AddStockBank() {
        float amount = Float.parseFloat(amounttext.getText());
        if (Bankinfobox.getSelectedIndex() > 0 || accountbox.getSelectedIndex() > 0) {
            if (accbalance >= amount) {

                //String reason = (String) expencestypebox.getSelectedItem();
                try {

                    String sql = "Insert into Expencess(expid,inputdate,extype,amount,paymenttype,remark,status,other,bank,accno,userid) values(?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, codetext.getText());
                    pst.setDate(2, date);
                    pst.setString(3, (String) expencestypebox.getSelectedItem());
                    pst.setString(4, amounttext.getText());
                    pst.setString(5, (String) paymenttypebox.getSelectedItem());
                    pst.setString(6, remarktext.getText());
                    pst.setString(7, (String) statusbox.getSelectedItem());

                    pst.setString(8, otherstext.getText());
                    pst.setString(9, (String) Bankinfobox.getSelectedItem());
                    pst.setString(10, (String) accountbox.getSelectedItem());
                    
                    pst.setInt(11, userid);
                    pst.execute();
                    String accountno = (String) accountbox.getSelectedItem();
                    float presentbalnce = bankcashout + amount;
                    balanceupdatecashout(accountno, presentbalnce);
                    balanceamount(accountno);
                    totalbalance();

                    overallinsert(updatebankbalance);

                    JOptionPane.showMessageDialog(null, "Data Saved");

                    clear();
                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            } else {
                JOptionPane.showMessageDialog(null, "Lack Account Balance");
            }

        } else {

            JOptionPane.showMessageDialog(null, "Please Select Bank Information");
        }

    }

    private void balanceamount(String accountno) {

        try {
            String sql = "Select cashout,cashin from BankAccount where accountno='" + accountno + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                //accbalance = rs.getFloat("prasentbalance");
                float bankcashouts = rs.getFloat("cashout");
                float bankcashins = rs.getFloat("cashin");
                updatebankbalance = bankcashins - bankcashouts;

            }
        } catch (Exception e) {
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void balanceupdatecashout(String accountno, float cahout) {
        try {
            String sql = "Update BankAccount set cashout='" + cahout + "' where accountno='" + accountno + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            // JOptionPane.showMessageDialog(null, "Data Update");
            //   clear();
            // totalbalance();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void overallinsert(float presentbalnce) {

        float cashin = 0;
        float cashout = Float.parseFloat(amounttext.getText());
        try {
            String reason = (String) expencestypebox.getSelectedItem();
            String sql = "Insert into bankoverall(inputdate,Description,bank,AccountNo,cashin,cashout,Balance,prasentbalance,totalbalance,remark,transactionid) values(?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setDate(1, date);

            pst.setString(2, "Expenses Payment," + reason);
            pst.setString(3, (String) Bankinfobox.getSelectedItem());
            pst.setString(4, (String) accountbox.getSelectedItem());
            pst.setFloat(5, cashin);
            pst.setFloat(6, cashout);
            pst.setFloat(7, accbalance);
            pst.setFloat(8, presentbalnce);
            pst.setFloat(9, totalb);
            pst.setString(10, "Expenses Payment," + "Reason:" + reason + " Exp No: " + codetext.getText() + "");
            pst.setString(11, codetext.getText());

            pst.execute();

            // JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    //End Bank section
    //Data Retrive Section
    private void currentdatedata() throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select expid as 'InputCode', inputdate as 'InputDate', extype as  'Description', amount as 'Amount', paymenttype as 'Paymentsource',remark,other,bank,accno,(select name from admin ad where ad.id=ex.userid) as 'uname' from Expencess ex Where ex.inputdate='" + date + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {
                String code = rs.getString("InputCode");
                String dates = rs.getString("InputDate");
                String exptype = rs.getString("Description");
                String source = rs.getString("Paymentsource");
                String amount = rs.getString("Amount");
                String other = rs.getString("other");
                String remark = rs.getString("remark");
                String bank = rs.getString("bank");
                String accno = rs.getString("accno");
                String uname=rs.getString("uname");
                tree++;
                model2.addRow(new Object[]{tree, code, dates, exptype, other, source, amount, remark, bank, accno,uname});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        try {
            String sql = "Select  sum(amount) as 'netAmount' from Expencess where inputdate='" + date + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                float totalexp = rs.getFloat("netAmount");
                netexptext.setText(String.format("%.2f", totalexp));

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void table_update() throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select expid as 'InputCode', inputdate as 'InputDate', extype as  'Description', amount as 'Amount', paymenttype as 'Paymentsource',remark,other,bank,accno,(select name from admin ad where ad.id=ex.userid) as 'uname' from Expencess ex";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {
                String code = rs.getString("InputCode");
                String dates = rs.getString("InputDate");
                String exptype = rs.getString("Description");
                String source = rs.getString("Paymentsource");
                String amount = rs.getString("Amount");
                String other = rs.getString("other");
                String remark = rs.getString("remark");
                String bank = rs.getString("bank");
                String accno = rs.getString("accno");
                String uname=rs.getString("uname");
                tree++;
                model2.addRow(new Object[]{tree, code, dates, exptype, other, source, amount, remark, bank, accno,uname});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        try {
            String sql = "Select  sum(amount) as 'netAmount' from Expencess";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                float totalexp = rs.getFloat("netAmount");
                netexptext.setText(String.format("%.2f", totalexp));

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void clear() {
        codetext.setText(null);
        expencestypebox.setSelectedIndex(0);
        amounttext.setText(null);
        paymenttypebox.setSelectedIndex(0);
        remarktext.setText(null);
        statusbox.setSelectedIndex(0);
        otherstext.setText(null);
        otherstext.hide();
        jLabel3.hide();
        paymenttypebox.setSelectedIndex(0);
        remarktext.setText(null);
        Bankinfobox.removeAllItems();
        accountbox.removeAllItems();
        chequenotext.setText(null);
        updatekey = 0;
        paymentstatus=0;
        Amounts=0;
        transactioncode();
    }

    private void stockUpdate() {
        try {

            String id = codetext.getText();

            String sql = "Update Expencess set inputdate='" + ((JTextField) inputdate.getDateEditor().getUiComponent()).getText() + "',extype='" + expencestypebox.getSelectedItem() + "',amount='" + otherstext.getText() + "',paymenttype='" + paymenttypebox.getSelectedItem() + "',remark='" + remarktext.getText() + "',status='" + statusbox.getSelectedItem() + "' where id='" + id + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void search() {
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        String SearchText = (String) expensesearchbox.getSelectedItem();
        try {

            String sql = "Select expid as 'InputCode', inputdate as 'InputDate', extype as  'Description', amount as 'Amount', paymenttype as 'Paymentsource',remark,other,bank,accno,(select name from admin ad where ad.id=ex.userid) as 'uname'  from Expencess ex where ex.extype='" + SearchText + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            while (rs.next()) {
                String code = rs.getString("InputCode");
                String dates = rs.getString("InputDate");
                String exptype = rs.getString("Description");
                String source = rs.getString("Paymentsource");
                String amount = rs.getString("Amount");
                String other = rs.getString("other");
                String remark = rs.getString("remark");
                String bank = rs.getString("bank");
                String accno = rs.getString("accno");
                String uname=rs.getString("uname");
                tree++;
                model2.addRow(new Object[]{tree, code, dates, exptype, other, source, amount, remark, bank, accno,uname});

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {
            String sql = "Select  sum(amount) as 'netAmount' from Expencess where extype='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                float totalexp = rs.getFloat("netAmount");
                netexptext.setText(String.format("%.2f", totalexp));

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
//Delete Histoy

    private void CashDrawerDelete() {
        try {
            String sql = "Delete from cashdrower where tranid=?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, codetext.getText());

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void BankDrwerDelete() {
        try {
            String sql = "Delete from bankoverall where transactionid=?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, codetext.getText());

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        float presentbalnce;
        GetAccountNo();
        presentbalnce = bankcashout - Amounts;
        balanceupdatecashout(AccNo, presentbalnce);

    }

    private void GetAccountNo() {
        try {
            String sql = "Select cashout from BankAccount where accountno='" + AccNo + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                //accbalance = rs.getFloat("prasentbalance");
                bankcashout = rs.getFloat("cashout");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

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

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        statusbox = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarktext = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        inputdate = new com.toedter.calendar.JDateChooser();
        expencestypebox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        otherstext = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        paymenttypebox = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        amounttext = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        chequenotext = new javax.swing.JTextField();
        Bankinfobox = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        accountbox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        expensesearchbox = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        fromdatepayment = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        todatepayment = new com.toedter.calendar.JDateChooser();
        jPanel7 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        netexptext = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Expenses");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N
        setInheritsPopupMenu(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 102, 0));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Date");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Remark");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Status");

        statusbox.setEditable(true);
        statusbox.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        statusbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Deactive" }));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Description");

        remarktext.setColumns(20);
        remarktext.setRows(5);
        jScrollPane2.setViewportView(remarktext);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("Code");

        codetext.setEnabled(false);

        inputdate.setForeground(new java.awt.Color(102, 102, 102));
        inputdate.setDateFormatString("yyyy-MM-dd");

        expencestypebox.setEditable(true);
        expencestypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        expencestypebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                expencestypeboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Expense Type");

        otherstext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        otherstext.setForeground(new java.awt.Color(204, 0, 0));
        otherstext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        otherstext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                otherstextActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Payment From");

        paymenttypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Bank" }));
        paymenttypebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                paymenttypeboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("Amount");

        amounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        amounttext.setForeground(new java.awt.Color(204, 0, 0));
        amounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        amounttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                amounttextActionPerformed(evt);
            }
        });

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Bank Info(Payment)"));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel13.setText("Acc No");

        chequenotext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chequenotextActionPerformed(evt);
            }
        });

        Bankinfobox.setEditable(true);
        Bankinfobox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                BankinfoboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel17.setText("Bank Name");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel18.setText("Cheque No");

        accountbox.setEditable(true);
        accountbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                accountboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel13)
                    .addComponent(jLabel18))
                .addGap(64, 64, 64)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Bankinfobox, 0, 217, Short.MAX_VALUE)
                    .addComponent(accountbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chequenotext))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(Bankinfobox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(accountbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(chequenotext, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setBackground(new java.awt.Color(204, 0, 0));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Execute");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 102, 0));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Clear");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(102, 0, 0));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Delete");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paymenttypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(statusbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(codetext)
                            .addComponent(expencestypebox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(otherstext)
                            .addComponent(amounttext)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                            .addComponent(inputdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(inputdate, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expencestypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(1, 1, 1))
                        .addComponent(otherstext, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(amounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(statusbox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paymenttypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 51, 51));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 0)));

        jPanel4.setBackground(new java.awt.Color(0, 51, 51));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Expense Type");

        expensesearchbox.setEditable(true);
        expensesearchbox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        expensesearchbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        expensesearchbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                expensesearchboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expensesearchbox, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(2, 2, 2)
                .addComponent(expensesearchbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(0, 51, 51));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        fromdatepayment.setDateFormatString("yyyy-MM-dd");

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("From");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("To");

        todatepayment.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(fromdatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(todatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(todatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fromdatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(0, 51, 51));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton10.setBackground(new java.awt.Color(0, 102, 0));
        jButton10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Submit");
        jButton10.setBorder(null);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(0, 204, 0));
        jButton11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Report");
        jButton11.setBorder(null);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                .addGap(48, 48, 48))
        );

        jPanel3.setBackground(new java.awt.Color(0, 51, 51));
        jPanel3.setFocusable(false);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Net Total:");

        netexptext.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        netexptext.setForeground(new java.awt.Color(255, 255, 255));
        netexptext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        netexptext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setText("Load All");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton5.setText("Current Date");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(netexptext, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(netexptext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        datatbl.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL No", "Code", "Input Date", "Type", "Remark", "Payment source", "Amount", "Description", "Bank", "Acc No", "Input User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
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
        datatbl.setGridColor(new java.awt.Color(204, 204, 204));
        datatbl.setRowHeight(30);
        datatbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setResizable(false);
            datatbl.getColumnModel().getColumn(1).setResizable(false);
            datatbl.getColumnModel().getColumn(2).setResizable(false);
            datatbl.getColumnModel().getColumn(3).setResizable(false);
            datatbl.getColumnModel().getColumn(4).setResizable(false);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setPreferredWidth(200);
            datatbl.getColumnModel().getColumn(8).setResizable(false);
            datatbl.getColumnModel().getColumn(9).setResizable(false);
            datatbl.getColumnModel().getColumn(10).setResizable(false);
            datatbl.getColumnModel().getColumn(10).setPreferredWidth(150);
        }

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1750, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(jPanel9);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane3)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );

        setBounds(5, 20, 1241, 544);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        inputDate();
        transactioncode();
        try {
            balance();
        } catch (SQLException ex) {
            Logger.getLogger(Expences.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (updatekey == 0) {
            if (paymenttypebox.getSelectedIndex() == 0) {
                try {
                    //cash insert
                    addStock();
                } catch (SQLException ex) {
                    Logger.getLogger(Expences.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //bank insert
                AddStockBank();

            }

        } else {

        }
        try {
            currentdatedata();
        } catch (SQLException ex) {
            Logger.getLogger(Expences.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clear();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (updatekey == 1) {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from expencess where expid=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, codetext.getText());

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                if (paymentstatus == 1) {
                    //cashdrawer
                    CashDrawerDelete();

                } else if (paymentstatus == 2) {
                    //bank Deleted
                    BankDrwerDelete();
                } else {
                    //

                }
            }
            clear();
            try {
                currentdatedata();
            } catch (SQLException ex) {
                Logger.getLogger(Locale.Category.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void otherstextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_otherstextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_otherstextActionPerformed
    private void AccNo() {
        try {
            String SearchText = (String) Bankinfobox.getSelectedItem();
            String sqls = "Select accountno from bankaccount where bank='" + SearchText + "'";
            pst = conn.prepareStatement(sqls);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            while (rs.next()) {
                String Acc = rs.getString("accountno");
                accountbox.addItem(Acc);

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
        DefaultTableModel model = (DefaultTableModel) datatbl.getModel();
        int selectedRowIndex = datatbl.getSelectedRow();
        String code = model.getValueAt(selectedRowIndex, 1).toString();
        codetext.setText(code);
        //inputdate.setDate((Date) datatbl.getValueAt(datatbl.getSelectedRow(), 2));
        String dateValue = model.getValueAt(selectedRowIndex, 2).toString();
        java.util.Date dates = null;
        try {
            dates = new SimpleDateFormat("yyyy-MM-dd").parse(dateValue);
        } catch (ParseException ex) {
            Logger.getLogger(Expences.class.getName()).log(Level.SEVERE, null, ex);
        }

        inputdate.setDate(dates);

        expencestypebox.setSelectedItem(model.getValueAt(selectedRowIndex, 3).toString());
        String check=model.getValueAt(selectedRowIndex, 4).toString();
        if(check.trim().length()==0){
            otherstext.hide();
        }else{
            otherstext.show();
            otherstext.setText(model.getValueAt(selectedRowIndex, 4).toString());
        }
        paymenttypebox.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());
        amounttext.setText(model.getValueAt(selectedRowIndex, 6).toString());
        Amounts = Float.parseFloat(model.getValueAt(selectedRowIndex, 6).toString());
        remarktext.setText(model.getValueAt(selectedRowIndex, 7).toString());
        if (paymenttypebox.getSelectedIndex() == 1) {
            paymentstatus = 2;
            Bankinfobox.removeAllItems();
            Bankinfobox.addItem("Select");
            Bankinfobox.setSelectedItem("Select");
            accountbox.removeAllItems();
            accountbox.addItem("Select");
            accountbox.setSelectedItem("Select");

            String status = "Active";
            try {
                String sqls = "Select Bankname from bankinfo where status='" + status + "'";
                pst = conn.prepareStatement(sqls);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    String bankname = rs.getString("Bankname");
                    Bankinfobox.addItem(bankname);

                    //expensesearchbox.addItem(name);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }

            Bankinfobox.setSelectedItem(model.getValueAt(selectedRowIndex, 8).toString());
            AccNo();
            accountbox.setSelectedItem(model.getValueAt(selectedRowIndex, 9).toString());
            AccNo = model.getValueAt(selectedRowIndex, 9).toString();
        } else {
            paymentstatus = 1;
            Bankinfobox.removeAllItems();
            accountbox.removeAllItems();
        }

        updatekey = 1;

        /* updatekey = 0;
         expencestypebox.removeAllItems();
         expencestypebox.addItem("All");
         try {
         expenstype();
         } catch (SQLException ex) {
         Logger.getLogger(Expences.class.getName()).log(Level.SEVERE, null, ex);
         }
      
         statusbox.removeAllItems();
         statusbox.addItem("Active");
         statusbox.addItem("Deactive");
        
        
        
         try {

         int row = datatbl.getSelectedRow();
         String table_click = (datatbl.getModel().getValueAt(row, 1).toString());
         String sql = "Select * from Expencess where id='" + table_click + "'";
         pst = conn.prepareStatement(sql);
         rs = pst.executeQuery();
         if (rs.next()) {

         String Id = rs.getString("expid");
         codetext.setText(Id);

         Date indate = rs.getDate("inputdate");
         inputdate.setDate(indate);

         String extype = rs.getString("extype");
         expencestypebox.setSelectedItem(extype);

         String amount = rs.getString("amount");
         amounttext.setText(amount);

         String paymenttype = rs.getString("paymenttype");
         paymenttypebox.setSelectedItem(paymenttype);
         String remark = rs.getString("remark");
         remarktext.setText(remark);
         String status = rs.getString("status");
         statusbox.setSelectedItem(status);
                

         }

         } catch (Exception e) {
         JOptionPane.showMessageDialog(null, e);

         }
               
         */
    }//GEN-LAST:event_datatblMouseClicked

    private void expensesearchboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_expensesearchboxPopupMenuWillBecomeInvisible
        if (expensesearchbox.getSelectedIndex() > 0) {
            search();
        } else {
            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(Expences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_expensesearchboxPopupMenuWillBecomeInvisible

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed

        if (expensesearchbox.getSelectedIndex() == 0) {
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            int tree = 0;
            try {
                String sql = "Select id as 'InputCode', inputdate as 'InputDate',extype as 'Description', amount as 'Amount', paymenttype as 'Paymentsource',remark,other,bank,accno,(select name from admin ad where ad.id=ex.userid) as 'uname' from Expencess ex where ex.inputdate BETWEEN ? AND ?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
                pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
                rs = pst.executeQuery();
                while (rs.next()) {
                    String code = rs.getString("InputCode");
                    String dates = rs.getString("InputDate");
                    String exptype = rs.getString("Description");
                    String source = rs.getString("Paymentsource");
                    String amount = rs.getString("Amount");
                    String other = rs.getString("other");
                    String remark = rs.getString("remark");
                    String bank = rs.getString("bank");
                    String accno = rs.getString("accno");
                    String uname=rs.getString("uname");
                    tree++;
                    model2.addRow(new Object[]{tree, code, dates, exptype, other, source, amount, remark, bank, accno,uname});

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            try {
                String sql = "Select  sum(amount) as 'netAmount' from Expencess where inputdate BETWEEN ? AND ? ";
                pst = conn.prepareStatement(sql);
                pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
                pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
                rs = pst.executeQuery();
                while (rs.next()) {
                    float totalexp = rs.getFloat("netAmount");
                    netexptext.setText(String.format("%.2f", totalexp));

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

        } else {
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            int tree = 0;
            try {
                String sql = "Select id as 'InputCode', inputdate as 'InputDate',extype as 'Description', amount as 'Amount', paymenttype as 'Paymentsource',remark,other,bank,accno,(select name from admin ad where ad.id=ex.userid) as 'uname' from Expencess ex where ex.inputdate BETWEEN ? AND ?  AND ex.extype='" + expensesearchbox.getSelectedItem() + "'";
                pst = conn.prepareStatement(sql);
                pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
                pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
                rs = pst.executeQuery();
                while (rs.next()) {
                    String code = rs.getString("InputCode");
                    String dates = rs.getString("InputDate");
                    String exptype = rs.getString("Description");
                    String source = rs.getString("Paymentsource");
                    String amount = rs.getString("Amount");
                    String other = rs.getString("other");
                    String remark = rs.getString("remark");
                    String bank = rs.getString("bank");
                    String accno = rs.getString("accno");
                    String uname=rs.getString("uname");
                    tree++;
                    model2.addRow(new Object[]{tree, code, dates, exptype, other, source, amount, remark, bank, accno,uname});

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

            try {
                String sql = "Select  sum(amount) as 'netAmount' from Expencess where inputdate BETWEEN ? AND ?  AND extype='" + expensesearchbox.getSelectedItem() + "'";
                pst = conn.prepareStatement(sql);
                pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
                pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
                rs = pst.executeQuery();
                while (rs.next()) {
                    float totalexp = rs.getFloat("netAmount");
                    netexptext.setText(String.format("%.2f", totalexp));

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }


    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        if (model2.getRowCount() == 0) {

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {

            try {
                String fromdate;
                String todate;

                if (((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText().isEmpty()) {
                    fromdate = "";
                } else {
                    fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
                }

                if (((JTextField) todatepayment.getDateEditor().getUiComponent()).getText().isEmpty()) {
                    todate = "";
                } else {
                    todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();
                }
                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/Expensessreportbyall.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDataSource", ItemJRBean);
                para.put("exptype", expensesearchbox.getSelectedItem());
                para.put("nettotal", netexptext.getText());
                para.put("fromdate", fromdate);
                para.put("todate", todate);
                para.put("nettotal", netexptext.getText());
                // para.put("description", description);
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void amounttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_amounttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_amounttextActionPerformed

    private void expencestypeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_expencestypeboxPopupMenuWillBecomeInvisible
        if (expencestypebox.getSelectedIndex() > 0) {
            String texts = (String) expencestypebox.getSelectedItem();
            if ("Others".equals(texts)) {
                otherstext.show(true);
                jLabel3.show(true);
            } else {
                otherstext.hide();
                jLabel3.hide();
            }

        }


    }//GEN-LAST:event_expencestypeboxPopupMenuWillBecomeInvisible

    private void chequenotextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chequenotextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chequenotextActionPerformed

    private void BankinfoboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_BankinfoboxPopupMenuWillBecomeInvisible

        chequenotext.setText(null);
        if (Bankinfobox.getSelectedIndex() > 0) {

            String SearchText = (String) Bankinfobox.getSelectedItem();
            try {
                String sql = "Select id from bankinfo where Bankname='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                if (rs.next()) {

                    bankid = rs.getInt("id");

                    //expensesearchbox.addItem(name);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

            try {

                String sql = "Select accountno from bankaccount where bank='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                //  pst.setString(1, SearchText);

                rs = pst.executeQuery();
                while (rs.next()) {
                    String Acc = rs.getString("accountno");
                    accountbox.addItem(Acc);

                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_BankinfoboxPopupMenuWillBecomeInvisible

    private void accountboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_accountboxPopupMenuWillBecomeInvisible
        String accno = (String) accountbox.getSelectedItem();
        try {
            String sql = "Select cashout,cashin from BankAccount where accountno='" + accno + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                //accbalance = rs.getFloat("prasentbalance");
                bankcashout = rs.getFloat("cashout");
                bankcashin = rs.getFloat("cashin");
                accbalance = bankcashin - bankcashout;

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_accountboxPopupMenuWillBecomeInvisible

    private void paymenttypeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_paymenttypeboxPopupMenuWillBecomeInvisible
        Bankinfobox.removeAllItems();
        Bankinfobox.addItem("Select");
        Bankinfobox.setSelectedItem("Select");
        accountbox.removeAllItems();
        accountbox.addItem("Select");
        accountbox.setSelectedItem("Select");
        chequenotext.setText(null);

        if (paymenttypebox.getSelectedIndex() == 1) {
            String status = "Active";
            try {
                String sql = "Select Bankname from bankinfo where status='" + status + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    String name = rs.getString("Bankname");
                    Bankinfobox.addItem(name);

                    //expensesearchbox.addItem(name);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_paymenttypeboxPopupMenuWillBecomeInvisible

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(Expences.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            currentdatedata();
        } catch (SQLException ex) {
            Logger.getLogger(Expences.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Bankinfobox;
    private javax.swing.JComboBox<String> accountbox;
    private javax.swing.JTextField amounttext;
    private javax.swing.JTextField chequenotext;
    private javax.swing.JTextField codetext;
    private javax.swing.JTable datatbl;
    private javax.swing.JComboBox<String> expencestypebox;
    private javax.swing.JComboBox<String> expensesearchbox;
    private com.toedter.calendar.JDateChooser fromdatepayment;
    private com.toedter.calendar.JDateChooser inputdate;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel netexptext;
    private javax.swing.JTextField otherstext;
    private javax.swing.JComboBox<String> paymenttypebox;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JComboBox<String> statusbox;
    private com.toedter.calendar.JDateChooser todatepayment;
    // End of variables declaration//GEN-END:variables
}
