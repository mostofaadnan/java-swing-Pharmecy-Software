/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
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
public class Bank extends javax.swing.JInternalFrame {

    float totalb;

    String publicaccountno;
    float publicaccountbalance;
    String publicbankname;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    float publiccashin;
    float publiccashout;
    String accountid;
   // Dashboard dashboard = new Dashboard();
    int userid=0;
    float updatebankbalance = 0;

    /**
     * Creates new form Bank
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public Bank() throws SQLException, IOException {
        initComponents();

        conn = Java_Connect.conectrDB();
        userkey();
        bankaccount();
        AutoCompleteDecorator.decorate(Bankinfobox);
        AutoCompleteDecorator.decorate(bankinfoboxwin);

       // AutoCompleteDecorator.decorate(banknamepayment);
       // AutoCompleteDecorator.decorate(accountnopayment);
        AutoCompleteDecorator.decorate(bankinfoboxwin);

        currentDate();
        table_update();
        accountno();
        //cashwinhistorytable();
        totalbalance();
       

        //bankstatement();
    }
 private void userkey() throws FileNotFoundException, IOException, SQLException {
        FileInputStream fis = new FileInputStream("src\\userkey.properties");

        //  /abdulbasit?useUnicode=yes&characterEncoding=UTF-8
        //jdbc:mysql://localhost:3306
        Properties p = new Properties();
        p.load(fis);

        String userids = (String) p.get("userid");
       userid=Integer.parseInt(userids);
        // this.dispose();
        //LoginAccess desboard=new LoginAccess();
    }
    private void bankstatement() throws SQLException {
        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) bankstatementtbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select DATE_FORMAT(inputdate, '%d-%m-%Y') as 'InputDate', Description, bank as 'BankName', AccountNo  as 'AccNo', Balance as 'AccAmount', cashin as 'CashIn', cashout as 'CashOut', prasentbalance as 'AccBalance',totalbalance as 'TotalBalance',remark as 'Remark' from bankoverall";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            // bankstatementtbl.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String InputDate = rs.getString("InputDate");
                String Description = rs.getString("Description");
                String BankName = rs.getString("BankName");
                String AccNo = rs.getString("AccNo");
                double AccAmount = rs.getDouble("AccAmount");
                double CashIn = rs.getDouble("CashIn");
                double CashOut = rs.getDouble("CashOut");
                double AccBalance = rs.getDouble("AccBalance");
                double TotalBalance = rs.getDouble("TotalBalance");
                String remark = rs.getString("Remark");
                tree++;
                model2.addRow(new Object[]{tree, InputDate, Description, BankName, AccNo, AccAmount, CashIn, CashOut, AccBalance, TotalBalance, remark});

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void table_update() throws SQLException {
        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select id, bank, accountholder, accountno, openingbalance, cashin,cashout from bankaccount";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String id = rs.getString("id");
                String accno = rs.getString("accountno");
                String bank = rs.getString("bank");
                String accountholder = rs.getString("accountholder");
                double opening = rs.getDouble("openingbalance");
                double cashin = rs.getDouble("cashin");
                double cashout = rs.getDouble("cashout");

                double prasentbalance = cashin - cashout;
                tree++;
                model2.addRow(new Object[]{tree, id, accno, bank, accountholder, opening, cashin, cashout, prasentbalance});

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        cashintext.setText(Float.toString(total_action_cashin()));
        cashouttext.setText(Float.toString(total_action_cashout()));

    }

    private void cashwinhistorytable() {
        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) cashwhistorybl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select id,inputdate,paytype,amount,bank,accountno,remark,(select name from admin ad where ad.id=cashw.inputuser) as 'inputusername' from cashwithdrawin cashw";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //cashwhistorybl.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String id = rs.getString("id");
                String inputdateher = rs.getString("inputdate");
                String paytype = rs.getString("paytype");
                double amount = rs.getDouble("amount");
                String Bank = rs.getString("bank");
                String accNo = rs.getString("accountno");
                String remark = rs.getString("remark");
                String inputuser = rs.getString("inputusername");

                tree++;
                model2.addRow(new Object[]{tree, id, inputdateher, paytype, amount, Bank, accNo, remark, inputuser});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        totalcashintext.setText(Float.toString(total_action_cashincahwsection()));
        totalcashwtext.setText(Float.toString(total_action_cashwidrawcahwsection()));

        // cashintotal();
    }

    private float total_action_cashincahwsection() {

        int rowaCount = cashwhistorybl.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
           
            String type = cashwhistorybl.getValueAt(i, 3).toString();
            if ("Cash In".equals(type)) {
                totaltpmrp = totaltpmrp + Float.parseFloat(cashwhistorybl.getValueAt(i, 4).toString());
            }
        }

        return (float) totaltpmrp;

    }

    private float total_action_cashwidrawcahwsection() {

        int rowaCount = cashwhistorybl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());
            String type = cashwhistorybl.getValueAt(i, 3).toString();
            if ("Cash Withdraw".equals(type)) {
                totaltpmrp = totaltpmrp + Float.parseFloat(cashwhistorybl.getValueAt(i, 4).toString());
            }
        }

        return (float) totaltpmrp;

    }

    private void cashindatetodate() {
        String fromdate = ((JTextField) inputdate2.getDateEditor().getUiComponent()).getText();
        String todate = ((JTextField) inputdate3.getDateEditor().getUiComponent()).getText();
        if (fromdate.isEmpty() || todate.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select Requirment Field");
        } else {

            int tree = 0;
            DefaultTableModel model2 = (DefaultTableModel) cashwhistorybl.getModel();
            model2.setRowCount(0);
            try {
                String sql = "Select id,inputdate,paytype,amount,bank,accountno,remark,(select name from admin ad where ad.id=cashw.inputuser) as 'inputusername' from cashwithdrawin cashw where cashw.inputdate BETWEEN ? AND ? ";
                pst = conn.prepareStatement(sql);
                pst.setString(1, ((JTextField) inputdate2.getDateEditor().getUiComponent()).getText());
                pst.setString(2, ((JTextField) inputdate3.getDateEditor().getUiComponent()).getText());
                rs = pst.executeQuery();
                //cashwhistorybl.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {
                    String id = rs.getString("id");
                    String inputdateher = rs.getString("inputdate");
                    String paytype = rs.getString("paytype");
                    double amount = rs.getDouble("amount");
                    String Bank = rs.getString("bank");
                    String accNo = rs.getString("accountno");
                    String remark = rs.getString("remark");
                    String inputuser = rs.getString("inputusername");

                    tree++;
                    model2.addRow(new Object[]{tree, id, inputdateher, paytype, amount, Bank, accNo, remark, inputuser});
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
        totalcashintext.setText(Float.toString(total_action_cashincahwsection()));
        totalcashwtext.setText(Float.toString(total_action_cashwidrawcahwsection()));

    }

    private float total_action_cashin() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 6).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_cashout() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 7).toString());
        }

        return (float) totaltpmrp;

    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        java.sql.Date date = new java.sql.Date(now.getTime());
        inputdate.setDate(date);
        winputdate.setDate(date);

    }

    private void totalbalance() {

        try {
            String sql = "Select sum(cashin) as 'cashin',sum(cashout) as 'cashout' from bankaccount";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                float cashin = rs.getFloat("cashin");
                float cashout = rs.getFloat("cashout");
                totalb = cashin - cashout;
                String totalbalancestring = String.format("%.2f", totalb);
                totalbalancetext.setText(totalbalancestring);
                totalbalancetext1.setText(totalbalancestring);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void bankaccount() throws SQLException {
        String status = "Active";
        try {
            String sql = "Select Bankname from bankinfo where status='" + status + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("Bankname");
                Bankinfobox.addItem(name);
                
                bankinfoboxwin.addItem(name);
                //expensesearchbox.addItem(name);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void accountno() throws SQLException {
      accountnobox.removeAllItems();
      accountnobox.addItem("Select");
      accountnobox.setSelectedItem("Select");
        try {
            String sql = "Select accountno from BankAccount";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("accountno");
                accountnobox.addItem(name);

                //expensesearchbox.addItem(name);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void addStock() throws SQLException {
        float cashin = 0;
        float cashout = 0;
        float openbalance = 0;
        if (openingbalancetext.getText().isEmpty()) {
            openingbalancetext.setText("0");
            cashin = 0;
        } else {

            cashin = Float.parseFloat(openingbalancetext.getText());

        }
        float prasentBalance = cashin - cashout;

        try {

            String Bank = (String) Bankinfobox.getSelectedItem();
            String Accno = accountNoText.getText();

            String reason = "Open New Account";
            String indate = ((JTextField) inputdate.getDateEditor().getUiComponent()).getText();

            String sql = "Insert into bankaccount(bank,branch,address,accounttype,currencytype,accountholder,accountno,accountopendate,openingbalance,cashin,cashout,remark,status) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
           pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, (String) Bankinfobox.getSelectedItem());
            pst.setString(2, brachtext.getText());
            pst.setString(3, addresstext.getText());
            pst.setString(4, (String) accounttypebox.getSelectedItem());
            pst.setString(5, (String) currencytypebox.getSelectedItem());
            pst.setString(6, accountholdertext.getText());
            pst.setString(7, accountNoText.getText());
            pst.setString(8, ((JTextField) inputdate.getDateEditor().getUiComponent()).getText());
            pst.setFloat(9, openbalance);
            pst.setFloat(10, cashin);
            pst.setFloat(11, cashout);

            pst.setString(12, remarktext.getText());
            pst.setString(13, (String) statusbox.getSelectedItem());
            pst.execute();
            ResultSet rshere = pst.getGeneratedKeys();
            int generatedKey = 0;
             if (rshere.next()) {
                generatedKey = rshere.getInt(1);
                overallinsert(prasentBalance, cashin, cashout, reason, indate, Bank, Accno, prasentBalance,generatedKey);

            }
            
            clear();
            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void amountCheck() throws SQLException {
        float prasent = Float.parseFloat(wbalance.getText());
        float amount = Float.parseFloat(cashwamounttext1.getText());
        int typecashw = cashwintypebox.getSelectedIndex();
        if (typecashw == 2) {
            if (amount > prasent) {

                JOptionPane.showMessageDialog(null, "Faild To Save, apply ammount biger than prsent balance");

            } else {
                cashwininsert();

            }
        } else {

            cashwininsert();

        }

    }

    private void cashwininsert() throws SQLException {
        float cashout, cashin;
        float balancs = publicaccountbalance;
        try {
            String Bank = (String) bankinfoboxwin.getSelectedItem();

            String indate = ((JTextField) winputdate.getDateEditor().getUiComponent()).getText();
            String reason;
            float presentbalnce = 0;
            String accountno = (String) accountnobox.getSelectedItem();
            float prasent = Float.parseFloat(wbalance.getText());

            float amount = Float.parseFloat(cashwamounttext1.getText());
            int typecashw = cashwintypebox.getSelectedIndex();
            if (typecashw == 1) {
                cashin = Float.parseFloat(cashwamounttext1.getText());
                cashout = 0;
                reason = "Cash In";
                //presentbalnce = (prasent + amount);
            } else {
                // presentbalnce = (prasent - amount);
                cashin = 0;
                cashout = Float.parseFloat(cashwamounttext1.getText());
                reason = "Cash Out";

            }

            float accountcashin = publiccashin + cashin;
            float accountcashout = publiccashout + cashout;

            String sql = "Insert into cashwithdrawin(inputdate,paytype,amount,bank,accountno,remark,inputuser) values(?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, ((JTextField) winputdate.getDateEditor().getUiComponent()).getText());
            pst.setString(2, (String) cashwintypebox.getSelectedItem());
            pst.setString(3, cashwamounttext1.getText());
            pst.setString(4, (String) bankinfoboxwin.getSelectedItem());
            pst.setString(5, (String) accountnobox.getSelectedItem());
            pst.setString(6, remarktextcashw.getText());
            pst.setInt(7, userid);

            pst.execute();
            ResultSet rshere = pst.getGeneratedKeys();
          int generatedKey = 0;
          balanceupdate(accountno, accountcashin, accountcashout);
            balanceamount(accountno);
            if (rshere.next()) {
                generatedKey = rshere.getInt(1);
                overallinsert(balancs, cashin, cashout, reason, indate, Bank, accountno, updatebankbalance,generatedKey);

            }
            

            
            cashwclear();
            // overallinsert(presentbalnce,cashin,cashout, reason,indate);
            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

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

    private void overallinsert(float balnce, float chasin, float cashot, String reason, String inputdate, String Bank, String Accno, float accountbalance,int trainid) {
        totalbalance();
        try {
           
            String sql = "Insert into bankoverall(inputdate,Description,bank,AccountNo,cashin,cashout,Balance,prasentbalance,totalbalance,remark,transactionid) values(?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, inputdate);

            pst.setString(2, reason);
            pst.setString(3, Bank);
            pst.setString(4, Accno);
            pst.setFloat(5, chasin);
            pst.setFloat(6, cashot);
            pst.setFloat(7, balnce);
            pst.setFloat(8, accountbalance);
            pst.setFloat(9, totalb);

            pst.setString(10, reason);
            pst.setInt(11, trainid);
            pst.execute();

            // JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void search() {

        String SearchText = accounttext.getText();

        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select id, bank, accountholder, accountno, openingbalance, cashin,cashout from bankaccount where accountno LIKE CONCAT('%', ? ,'%') OR accountholder LIKE CONCAT('%', ? ,'%') OR bank LIKE CONCAT('%', ? ,'%') OR accounttype LIKE CONCAT('%', ? ,'%')";
            pst = conn.prepareStatement(sql);
            pst.setString(1, SearchText);
            pst.setString(2, SearchText);
            pst.setString(3, SearchText);
            pst.setString(4, SearchText);
            rs = pst.executeQuery();
            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String id = rs.getString("id");
                String accno = rs.getString("accountno");
                String bank = rs.getString("bank");
                String accountholder = rs.getString("accountholder");
                double opening = rs.getDouble("openingbalance");
                double cashin = rs.getDouble("cashin");
                double cashout = rs.getDouble("cashout");
                double prasentbalance = cashin - cashout;
                tree++;
                model2.addRow(new Object[]{tree, id, accno, bank, accountholder, opening, cashin, cashout, prasentbalance});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void stockUpdate() {
        try {
            String id = codetext.getText();

            String sql = "Update BankAccount set bank='" + Bankinfobox.getSelectedItem() + "',branch='" + brachtext.getText() + "',address='" + addresstext.getText() + "',accounttype='" + accounttypebox.getSelectedItem() + "',currencytype='" + currencytypebox.getSelectedItem() + "',accountholder='" + accountholdertext.getText() + "',accountno='" + accountNoText.getText() + "',accountopendate='" + ((JTextField) inputdate.getDateEditor().getUiComponent()).getText() + "',remark='" + remarktext.getText() + "',status='" + statusbox.getSelectedItem() + "' where id='" + id + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Update");
            clear();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void clear() throws SQLException {
        codetext.setText(null);
        Bankinfobox.setSelectedIndex(0);
        brachtext.setText(null);
        addresstext.setText(null);
        accounttypebox.setSelectedIndex(0);
        currencytypebox.setSelectedIndex(0);
        accountholdertext.setText(null);
        accountNoText.setText(null);
        currentDate();
        openingbalancetext.setText(null);
        prasentbalancetext.setText(null);
        remarktext.setText(null);
        statusbox.setSelectedIndex(0);
        table_update();
        accountno();
        totalbalance();
        bankstatement();
    

    }

    private void tableClick() {

        statusbox.removeAllItems();
        statusbox.addItem("Active");
        statusbox.addItem("Deactive");
        accounttypebox.removeAllItems();
        accounttypebox.addItem("Select");
        accounttypebox.addItem("Current");
        accounttypebox.addItem("Saving");

        currencytypebox.removeAllItems();
        currencytypebox.addItem("AED");

        Bankinfobox.removeAllItems();
        try {
            bankaccount();
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {

            int row = datatbl.getSelectedRow();
            String table_click = (datatbl.getModel().getValueAt(row, 0).toString());
            String sql = "Select * from BankAccount where id='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                accountid = rs.getString("id");
                codetext.setText(accountid);
                String bank = rs.getString("bank");
                Bankinfobox.setSelectedItem(bank);
                String branch = rs.getString("branch");
                brachtext.setText(branch);
                String address = rs.getString("address");
                addresstext.setText(address);
                String accounttype = rs.getString("accounttype");
                accounttypebox.setSelectedItem(accounttype);
                String currencytype = rs.getString("currencytype");
                currencytypebox.setSelectedItem(currencytype);
                String accountholder = rs.getString("accountholder");
                accountholdertext.setText(accountholder);
                String accountno = rs.getString("accountno");
                accountNoText.setText(accountno);
                Date accountopendate = rs.getDate("accountopendate");
                inputdate.setDate(accountopendate);

                String openingbalance = rs.getString("openingbalance");
                openingbalancetext.setText(openingbalance);
                double acccashin=rs.getDouble("cashin");
                double acccashout=rs.getDouble("cashout");
                double accprasentb=acccashin-acccashout;
                
                String presentbalances=String.format("%.2f", accprasentb);
                prasentbalancetext.setText(presentbalances);
                String status = rs.getString("status");
                statusbox.setSelectedItem(status);

                String remark = rs.getString("remark");
                remarktext.setText(remark);

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void cashwclear() throws SQLException {
        cashwintypebox.setSelectedIndex(0);
        bankinfoboxwin.setSelectedIndex(0);
        wholdertext.setText(null);
        wactype.setText(null);
        wbankname.setText(null);
        wbalance.setText(null);
        cashwamounttext1.setText(null);
        remarktextcashw.setText(null);
        totalbalance();
        table_update();
       accountno();
        codetext.setText(null);
        accountnobox.setSelectedIndex(0);
    }

    private void balanceupdate(String accountno, float cashin, float cashout) {
        try {
            String sql = "Update BankAccount set cashin='" + cashin + "',cashout='" + cashout + "' where accountno='" + accountno + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            totalbalance();
            // JOptionPane.showMessageDialog(null, "Data Update");
            clear();

        } catch (SQLException | HeadlessException e) {
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

        tablemenu = new javax.swing.JPopupMenu();
        statementview = new javax.swing.JMenuItem();
        report = new javax.swing.JMenuItem();
        view = new javax.swing.JMenuItem();
        tablemenuone = new javax.swing.JPopupMenu();
        vewdata = new javax.swing.JMenuItem();
        delete = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        totalbalancetext1 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        fromdatepayment1 = new com.toedter.calendar.JDateChooser();
        jLabel59 = new javax.swing.JLabel();
        todatepayment1 = new com.toedter.calendar.JDateChooser();
        jLabel61 = new javax.swing.JLabel();
        yeartext = new javax.swing.JTextField();
        overalsubmit = new javax.swing.JButton();
        reportbox = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        bankstatementtbl = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        accountpanel = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        brachtext = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        addresstext = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        accountholdertext = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        openingbalancetext = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        accounttypebox = new javax.swing.JComboBox<>();
        currencytypebox = new javax.swing.JComboBox<>();
        inputdate = new com.toedter.calendar.JDateChooser();
        jScrollPane4 = new javax.swing.JScrollPane();
        remarktext = new javax.swing.JTextArea();
        jLabel40 = new javax.swing.JLabel();
        statusbox = new javax.swing.JComboBox<>();
        jLabel41 = new javax.swing.JLabel();
        prasentbalancetext = new javax.swing.JTextField();
        codetext = new javax.swing.JTextField();
        Bankinfobox = new javax.swing.JComboBox<>();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        accountNoText = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        accounttext = new javax.swing.JTextField();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cashintext = new javax.swing.JTextField();
        jLabel91 = new javax.swing.JLabel();
        cashouttext = new javax.swing.JTextField();
        totalbalancetext = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        cashwthdrwin = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        winputdate = new com.toedter.calendar.JDateChooser();
        bankinfoboxwin = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        wholdertext = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        wactype = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        wbankname = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        remarktextcashw = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        wbalance = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        cashwintypebox = new javax.swing.JComboBox<>();
        accountnobox = new javax.swing.JComboBox<>();
        jLabel62 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        cashwcodetext = new javax.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        cashwamounttext1 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        inputdate2 = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        inputdate3 = new com.toedter.calendar.JDateChooser();
        jButton7 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jLabel92 = new javax.swing.JLabel();
        totalcashwtext = new javax.swing.JTextField();
        jLabel93 = new javax.swing.JLabel();
        totalcashintext = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        cashwhistorybl = new javax.swing.JTable();

        tablemenu.setPreferredSize(new java.awt.Dimension(300, 90));

        statementview.setText("Statement View");
        statementview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statementviewActionPerformed(evt);
            }
        });
        tablemenu.add(statementview);

        report.setText("Report View");
        report.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportActionPerformed(evt);
            }
        });
        tablemenu.add(report);

        view.setText("View Details");
        view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewActionPerformed(evt);
            }
        });
        tablemenu.add(view);

        tablemenuone.setPreferredSize(new java.awt.Dimension(300, 80));

        vewdata.setText("View");
        vewdata.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vewdataActionPerformed(evt);
            }
        });
        tablemenuone.add(vewdata);

        delete.setText("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        tablemenuone.add(delete);

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Bank Information");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jPanel19.setBackground(new java.awt.Color(67, 86, 86));

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setText("Total Balance");

        totalbalancetext1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        totalbalancetext1.setForeground(new java.awt.Color(255, 255, 255));
        totalbalancetext1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalbalancetext1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("From");

        fromdatepayment1.setDateFormatString("yyyy-MM-dd");

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("To");

        todatepayment1.setDateFormatString("yyyy-MM-dd");

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("Year");

        yeartext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        yeartext.setForeground(new java.awt.Color(102, 0, 0));

        overalsubmit.setBackground(new java.awt.Color(0, 102, 0));
        overalsubmit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        overalsubmit.setForeground(new java.awt.Color(255, 255, 255));
        overalsubmit.setText("Submit");
        overalsubmit.setBorder(null);
        overalsubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overalsubmitActionPerformed(evt);
            }
        });

        reportbox.setBackground(new java.awt.Color(0, 204, 0));
        reportbox.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        reportbox.setForeground(new java.awt.Color(255, 255, 255));
        reportbox.setText("Report");
        reportbox.setBorder(null);
        reportbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportboxActionPerformed(evt);
            }
        });

        jButton19.setBackground(new java.awt.Color(255, 255, 255));
        jButton19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton19.setText("Load All");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton19)
                .addGap(1, 1, 1)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel57)
                    .addComponent(fromdatepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(jLabel59)
                        .addGap(222, 222, 222)
                        .addComponent(jLabel61))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(todatepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(yeartext, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(overalsubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(reportbox, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalbalancetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(overalsubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalbalancetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(reportbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel57, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel59)
                                .addComponent(jLabel61)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(fromdatepayment1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(todatepayment1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(yeartext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(5, 5, 5))
        );

        bankstatementtbl.setAutoCreateRowSorter(true);
        bankstatementtbl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        bankstatementtbl.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        bankstatementtbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "Inputdate", "Description", "Bank", "ACC No", "Acc Amount", "Cashin", "Cashout", "Acc Balance", "Total Balance", "Remark"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
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
        bankstatementtbl.setGridColor(new java.awt.Color(204, 204, 204));
        bankstatementtbl.setRowHeight(30);
        bankstatementtbl.setShowHorizontalLines(true);
        bankstatementtbl.setShowVerticalLines(true);
        jScrollPane6.setViewportView(bankstatementtbl);
        if (bankstatementtbl.getColumnModel().getColumnCount() > 0) {
            bankstatementtbl.getColumnModel().getColumn(0).setResizable(false);
            bankstatementtbl.getColumnModel().getColumn(0).setPreferredWidth(30);
            bankstatementtbl.getColumnModel().getColumn(1).setResizable(false);
            bankstatementtbl.getColumnModel().getColumn(2).setResizable(false);
            bankstatementtbl.getColumnModel().getColumn(2).setPreferredWidth(300);
            bankstatementtbl.getColumnModel().getColumn(3).setResizable(false);
            bankstatementtbl.getColumnModel().getColumn(4).setResizable(false);
            bankstatementtbl.getColumnModel().getColumn(5).setResizable(false);
            bankstatementtbl.getColumnModel().getColumn(5).setPreferredWidth(50);
            bankstatementtbl.getColumnModel().getColumn(6).setResizable(false);
            bankstatementtbl.getColumnModel().getColumn(6).setPreferredWidth(50);
            bankstatementtbl.getColumnModel().getColumn(7).setResizable(false);
            bankstatementtbl.getColumnModel().getColumn(7).setPreferredWidth(50);
            bankstatementtbl.getColumnModel().getColumn(8).setResizable(false);
            bankstatementtbl.getColumnModel().getColumn(8).setPreferredWidth(50);
            bankstatementtbl.getColumnModel().getColumn(9).setResizable(false);
            bankstatementtbl.getColumnModel().getColumn(9).setPreferredWidth(50);
            bankstatementtbl.getColumnModel().getColumn(10).setResizable(false);
            bankstatementtbl.getColumnModel().getColumn(10).setPreferredWidth(300);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1591, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
        );

        jScrollPane7.setViewportView(jPanel4);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jTabbedPane1.addTab("Bank Statement", jPanel8);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        accountpanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                accountpanelMouseClicked(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(102, 102, 102));
        jLabel31.setText("Code ");

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 102, 0));
        jLabel32.setText("Branch");

        brachtext.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        brachtext.setBorder(null);
        brachtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                brachtextKeyPressed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(51, 51, 51));
        jLabel33.setText("Address");

        addresstext.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        addresstext.setBorder(null);
        addresstext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                addresstextKeyPressed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(51, 51, 51));
        jLabel34.setText("Account Type");

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(51, 51, 51));
        jLabel35.setText("Currency Type");

        accountholdertext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        accountholdertext.setForeground(new java.awt.Color(204, 0, 0));
        accountholdertext.setBorder(null);

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(51, 51, 51));
        jLabel36.setText("Account Holder");

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(51, 51, 51));
        jLabel37.setText("Account Open Date");

        openingbalancetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        openingbalancetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        openingbalancetext.setBorder(null);

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(51, 51, 51));
        jLabel38.setText("Opening Balance");

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(51, 51, 51));
        jLabel39.setText("Remark");

        accounttypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Current", "Saving" }));

        currencytypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BDT" }));

        inputdate.setForeground(new java.awt.Color(102, 102, 102));
        inputdate.setDateFormatString("yyyy-MM-dd\n");

        remarktext.setColumns(20);
        remarktext.setRows(5);
        jScrollPane4.setViewportView(remarktext);

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(51, 51, 51));
        jLabel40.setText("Status");

        statusbox.setEditable(true);
        statusbox.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        statusbox.setForeground(new java.awt.Color(0, 102, 51));
        statusbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Deactive" }));

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(51, 51, 51));
        jLabel41.setText("Current Balance");

        prasentbalancetext.setEditable(false);
        prasentbalancetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        prasentbalancetext.setForeground(new java.awt.Color(204, 0, 0));
        prasentbalancetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        prasentbalancetext.setBorder(null);
        prasentbalancetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        codetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        codetext.setEnabled(false);

        Bankinfobox.setEditable(true);
        Bankinfobox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        Bankinfobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        Bankinfobox.setBorder(null);

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 0, 0));
        jLabel42.setText("Bank ");

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(51, 51, 51));
        jLabel43.setText("Account No");

        accountNoText.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        accountNoText.setBorder(null);
        accountNoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                accountNoTextKeyPressed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(204, 0, 0));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Execute");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 153, 51));
        jButton2.setText("Clear");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(102, 0, 0));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Delete");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout accountpanelLayout = new javax.swing.GroupLayout(accountpanel);
        accountpanel.setLayout(accountpanelLayout);
        accountpanelLayout.setHorizontalGroup(
            accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountpanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31)
                    .addComponent(jLabel32)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37)
                    .addComponent(jLabel38)
                    .addComponent(jLabel39)
                    .addComponent(jLabel40)
                    .addComponent(jLabel41)
                    .addComponent(jLabel42)
                    .addComponent(jLabel43))
                .addGap(1, 1, 1)
                .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prasentbalancetext, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(openingbalancetext, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputdate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accountholdertext, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(currencytypebox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accounttypebox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accountNoText, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addresstext, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(brachtext, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Bankinfobox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, accountpanelLayout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE))
                    .addComponent(codetext, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        accountpanelLayout.setVerticalGroup(
            accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(accountpanelLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codetext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Bankinfobox, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, accountpanelLayout.createSequentialGroup()
                        .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(brachtext, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addresstext, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(accountNoText, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(accounttypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(currencytypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(accountholdertext, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(inputdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openingbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prasentbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusbox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(accountpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Search ");

        accounttext.setBackground(new java.awt.Color(255, 255, 204));
        accounttext.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        accounttext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        accounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                accounttextKeyReleased(evt);
            }
        });

        jButton20.setBackground(new java.awt.Color(255, 255, 255));
        jButton20.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jButton20.setText("Print All");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setBackground(new java.awt.Color(255, 255, 255));
        jButton21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton21.setText("Load All");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(accounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(438, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(accounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );

        jPanel27.setBackground(new java.awt.Color(67, 86, 86));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Cash In");

        jLabel91.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(255, 255, 255));
        jLabel91.setText("Cash Out");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Total Balance");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashintext, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashintext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(totalbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        datatbl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "Acc ID", "Acc No", "Bank", "Holder", "Opening Blalance", "Cash In", "Casho Out", "Prasent Balance"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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
        datatbl.setShowHorizontalLines(true);
        datatbl.setShowVerticalLines(true);
        datatbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatblMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setResizable(false);
            datatbl.getColumnModel().getColumn(0).setPreferredWidth(30);
            datatbl.getColumnModel().getColumn(1).setResizable(false);
            datatbl.getColumnModel().getColumn(1).setPreferredWidth(30);
            datatbl.getColumnModel().getColumn(2).setResizable(false);
            datatbl.getColumnModel().getColumn(3).setResizable(false);
            datatbl.getColumnModel().getColumn(4).setResizable(false);
            datatbl.getColumnModel().getColumn(4).setPreferredWidth(200);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
            datatbl.getColumnModel().getColumn(8).setResizable(false);
        }

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1068, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3)
                .addGap(0, 0, 0))
        );

        jScrollPane9.setViewportView(jPanel10);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(accountpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, 744, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(accountpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        jTabbedPane1.addTab("Account", jPanel1);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel3MouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 0, 0));
        jLabel1.setText("Cash Withdraw/In");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 102, 0));
        jLabel2.setText("Input Date");

        winputdate.setForeground(new java.awt.Color(102, 102, 102));
        winputdate.setDateFormatString("yyyy-MM-dd");
        winputdate.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        bankinfoboxwin.setEditable(true);
        bankinfoboxwin.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        bankinfoboxwin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        bankinfoboxwin.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                bankinfoboxwinPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Account Holder Name");

        wholdertext.setBackground(new java.awt.Color(255, 255, 255));
        wholdertext.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        wholdertext.setForeground(new java.awt.Color(51, 51, 51));
        wholdertext.setAlignmentX(0.5F);
        wholdertext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 0)));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Account type");

        wactype.setBackground(new java.awt.Color(255, 255, 255));
        wactype.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        wactype.setForeground(new java.awt.Color(51, 51, 51));
        wactype.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 0)));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Bank Name");

        wbankname.setBackground(new java.awt.Color(255, 255, 255));
        wbankname.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        wbankname.setForeground(new java.awt.Color(51, 51, 51));
        wbankname.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 0)));

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Remark");

        remarktextcashw.setColumns(20);
        remarktextcashw.setRows(5);
        jScrollPane1.setViewportView(remarktextcashw);

        jButton4.setBackground(new java.awt.Color(204, 0, 0));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Execute");
        jButton4.setBorder(null);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 153, 51));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Clear");
        jButton5.setBorder(null);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(102, 0, 0));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Delete");
        jButton6.setBorder(null);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Prasent Balance");

        wbalance.setBackground(new java.awt.Color(255, 255, 255));
        wbalance.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        wbalance.setForeground(new java.awt.Color(255, 51, 51));
        wbalance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        wbalance.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        wbalance.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 0)));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(204, 0, 0));
        jLabel14.setText("Type");

        cashwintypebox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cashwintypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Cash In", "Cash Withdraw" }));
        cashwintypebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                cashwintypeboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        accountnobox.setEditable(true);
        accountnobox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        accountnobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        accountnobox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                accountnoboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel62.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 0, 0));
        jLabel62.setText("Accoun No");

        jLabel63.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(255, 0, 0));
        jLabel63.setText("Bank Name");

        cashwcodetext.setEditable(false);
        cashwcodetext.setEnabled(false);

        jLabel94.setText("Code");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 0));
        jLabel17.setText("Amount");

        cashwamounttext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cashwamounttext1.setForeground(new java.awt.Color(204, 0, 0));
        cashwamounttext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout cashwthdrwinLayout = new javax.swing.GroupLayout(cashwthdrwin);
        cashwthdrwin.setLayout(cashwthdrwinLayout);
        cashwthdrwinLayout.setHorizontalGroup(
            cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashwthdrwinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cashwthdrwinLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, cashwthdrwinLayout.createSequentialGroup()
                        .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(cashwthdrwinLayout.createSequentialGroup()
                                    .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel9)
                                        .addComponent(jLabel14)
                                        .addComponent(jLabel63)
                                        .addComponent(jLabel17))
                                    .addGap(5, 5, 5))
                                .addGroup(cashwthdrwinLayout.createSequentialGroup()
                                    .addComponent(jLabel94, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(52, 52, 52)))
                            .addGroup(cashwthdrwinLayout.createSequentialGroup()
                                .addComponent(jLabel62)
                                .addGap(81, 81, 81)))
                        .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(accountnobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(cashwthdrwinLayout.createSequentialGroup()
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(wactype, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(wbankname, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(wbalance, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, cashwthdrwinLayout.createSequentialGroup()
                                .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cashwintypebox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(winputdate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cashwcodetext, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bankinfoboxwin, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(wholdertext, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cashwamounttext1, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(91, 91, 91))))
        );
        cashwthdrwinLayout.setVerticalGroup(
            cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cashwthdrwinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(2, 2, 2)
                .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cashwcodetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel94, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(winputdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashwintypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bankinfoboxwin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(accountnobox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(wholdertext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(wactype, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(wbankname, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(wbalance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cashwthdrwinLayout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11))
                    .addGroup(cashwthdrwinLayout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(cashwamounttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(cashwthdrwinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(67, 86, 86));

        jLabel5.setBackground(new java.awt.Color(67, 86, 86));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Cash In/Withdraw History");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel7.setBackground(new java.awt.Color(67, 86, 86));
        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("From");

        inputdate2.setForeground(new java.awt.Color(102, 102, 102));
        inputdate2.setDateFormatString("yyyy-MM-dd");

        jLabel10.setBackground(new java.awt.Color(67, 86, 86));
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("To");

        inputdate3.setForeground(new java.awt.Color(102, 102, 102));
        inputdate3.setDateFormatString("yyyy-MM-dd");

        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton7.setText("Submit");
        jButton7.setBorder(null);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel15.setBackground(new java.awt.Color(67, 86, 86));
        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Acc No ");

        jButton22.setBackground(new java.awt.Color(255, 255, 255));
        jButton22.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        jButton22.setText("Report");
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton23.setText("Load All");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(inputdate2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputdate3, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton22)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(inputdate3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(inputdate2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        jPanel18.setBackground(new java.awt.Color(0, 51, 51));

        jLabel92.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(255, 255, 255));
        jLabel92.setText("Cash Withdraw");

        jLabel93.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(255, 255, 255));
        jLabel93.setText("CashIn");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(192, Short.MAX_VALUE)
                .addComponent(jLabel92)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalcashwtext, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel93)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalcashintext, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalcashwtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel92, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalcashintext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel93, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        cashwhistorybl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cashwhistorybl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cashwhistorybl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "ID", "Input Date", "Payment Type", "Amount", "Bank", "Acc No", "Remark", "Input User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        cashwhistorybl.setGridColor(new java.awt.Color(204, 204, 204));
        cashwhistorybl.setRowHeight(30);
        cashwhistorybl.setShowHorizontalLines(true);
        cashwhistorybl.setShowVerticalLines(true);
        cashwhistorybl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cashwhistoryblMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(cashwhistorybl);
        if (cashwhistorybl.getColumnModel().getColumnCount() > 0) {
            cashwhistorybl.getColumnModel().getColumn(0).setResizable(false);
            cashwhistorybl.getColumnModel().getColumn(0).setPreferredWidth(30);
            cashwhistorybl.getColumnModel().getColumn(1).setResizable(false);
            cashwhistorybl.getColumnModel().getColumn(1).setPreferredWidth(50);
            cashwhistorybl.getColumnModel().getColumn(2).setResizable(false);
            cashwhistorybl.getColumnModel().getColumn(3).setResizable(false);
            cashwhistorybl.getColumnModel().getColumn(4).setResizable(false);
            cashwhistorybl.getColumnModel().getColumn(5).setResizable(false);
            cashwhistorybl.getColumnModel().getColumn(6).setResizable(false);
            cashwhistorybl.getColumnModel().getColumn(7).setResizable(false);
            cashwhistorybl.getColumnModel().getColumn(7).setPreferredWidth(200);
            cashwhistorybl.getColumnModel().getColumn(8).setResizable(false);
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1103, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
        );

        jScrollPane8.setViewportView(jPanel6);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(cashwthdrwin, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane8)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cashwthdrwin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Cash In/Withdraw", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane1)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        setBounds(5, 5, 1139, 588);
    }// </editor-fold>//GEN-END:initComponents

    private void statementviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statementviewActionPerformed
        String table_click = accountid;
        AccountHistory filte = null;

        try {
            filte = new AccountHistory(table_click);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
    }//GEN-LAST:event_statementviewActionPerformed

    private void reportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportActionPerformed
        try {

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/AccountHisory.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();

            para.put("accountid", accountid);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);

        } catch (NumberFormatException | JRException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
    }//GEN-LAST:event_reportActionPerformed

    private void viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewActionPerformed
        tableClick();
       

    }//GEN-LAST:event_viewActionPerformed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked

    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void jPanel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel3MouseClicked
        cashwinhistorytable();
    }//GEN-LAST:event_jPanel3MouseClicked

    private void cashwhistoryblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cashwhistoryblMouseClicked
        
        try {

            int row = cashwhistorybl.getSelectedRow();
            String table_click = (cashwhistorybl.getModel().getValueAt(row, 1).toString());
            String sql = "Select cashwin.id as  'id',cashwin.inputdate as 'inputdate',cashwin.paytype as'paytype',cashwin.amount as 'amount',cashwin.remark as 'remark',cashwin.bank as 'bank',cashwin.accountno as 'accountno',bn.accountholder as 'holder',bn.accounttype as 'accounttype',bn.cashin as 'cashin',bn.cashout as 'cashout' from cashwithdrawin cashwin inner join bankaccount bn ON cashwin.accountno=bn.accountno 	 where cashwin.id='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String id = rs.getString("id");
                cashwcodetext.setText(id);
                Date input = rs.getDate("inputdate");
                winputdate.setDate(input);
                String paytype = rs.getString("paytype");
                cashwintypebox.setSelectedItem(paytype);
                String amount = rs.getString("amount");
                cashwamounttext1.setText(amount);
                String remark = rs.getString("remark");
                remarktextcashw.setText(remark);
                String bank = rs.getString("bank");
                bankinfoboxwin.setSelectedItem(bank);
                String accountno = rs.getString("accountno");
                ///  accountnobox.addItem(accountno);
                accountnobox.setSelectedItem(accountno);
                String holder = rs.getString("holder");
                wholdertext.setText(holder);
                String accounttype = rs.getString("accounttype");
                wactype.setText(accounttype);
                float cashin = rs.getFloat("cashin");
                float cashout = rs.getFloat("cashout");
                float prasentbalance = cashin - cashout;
                String balance = String.format("%.2f", prasentbalance);
                wbalance.setText(balance);
                wbankname.setText(bank);

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        if (SwingUtilities.isRightMouseButton(evt)) {
            tablemenuone.show(evt.getComponent(), evt.getX() + 1, evt.getY() + 1);

        }

    }//GEN-LAST:event_cashwhistoryblMouseClicked

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed

        ((JTextField) inputdate2.getDateEditor().getUiComponent()).setText(null);
        ((JTextField) inputdate3.getDateEditor().getUiComponent()).setText(null);
        cashwinhistorytable();
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) cashwhistorybl.getModel();
        if (model2.getRowCount() == 0) {

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {

            try {
                String description = null;
                String fromdate = ((JTextField) inputdate2.getDateEditor().getUiComponent()).getText();
                String todate = ((JTextField) inputdate3.getDateEditor().getUiComponent()).getText();
                if (fromdate.isEmpty() || todate.isEmpty()) {
                    description = "All";
                } else if (fromdate.isEmpty() || todate.isEmpty()) {

                } else {
                    cashindatetodate();
                    description = "From :" + fromdate + " To :" + todate;

                }

                String cashin = totalcashintext.getText();
                String cashwidrwa = totalcashwtext.getText();
                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/CashWinInReport.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDataSource", ItemJRBean);
                para.put("description", description);
                para.put("cashin", cashin);
                para.put("cashwidrwa", cashwidrwa);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        cashindatetodate();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void accountnoboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_accountnoboxPopupMenuWillBecomeInvisible
        if (accountnobox.getSelectedIndex() > 0) {
            String SearchText = (String) accountnobox.getSelectedItem();
            try {

                String sql = "Select accountholder,accounttype,bank,accountno,cashin,cashout from BankAccount where accountno='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                //  pst.setString(1, SearchText);

                rs = pst.executeQuery();
                if (rs.next()) {

                    String accountholder = rs.getString("accountholder");
                    wholdertext.setText(accountholder);

                    String accounttype = rs.getString("accounttype");
                    wactype.setText(accounttype);

                    String bank = rs.getString("bank");
                    wbankname.setText(bank);

                    publicaccountno = rs.getString("accountno");
                    publiccashin = rs.getFloat("cashin");
                    publiccashout = rs.getFloat("cashout");
                    publicaccountbalance = publiccashin - publiccashout;
                    wbalance.setText(String.format("%.2f", publicaccountbalance));

                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_accountnoboxPopupMenuWillBecomeInvisible

    private void cashwintypeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_cashwintypeboxPopupMenuWillBecomeInvisible
        publicbankname = (String) cashwintypebox.getSelectedItem();
    }//GEN-LAST:event_cashwintypeboxPopupMenuWillBecomeInvisible

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
      if (cashwcodetext.getText().isEmpty()) {

        } else {
            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from cashwithdrawin where id=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, cashwcodetext.getText());

                    pst.execute();
                   // JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                String type = (String) cashwintypebox.getSelectedItem();
                try {
                    String sql = "Delete from bankoverall where transactionid=? AND Description='" + type + "' ";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, cashwcodetext.getText());

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                AccountCheck();
                float cashin = 0;
                float cashout = 0;

                String Accno = (String) accountnobox.getSelectedItem();
                float amount = Float.parseFloat(cashwamounttext1.getText());
                if ("Cash In".equals(type)) {
                    cashin = 0;
                    cashout = amount;

                } else {
                    cashin = amount;
                    cashout = 0;

                }
                float accountcashin = publiccashin + cashin;
                float accountcashout = publiccashout + cashout;
                balanceupdate(Accno, accountcashin, accountcashout);
                try {
                    cashwclear();
                    cashwinhistorytable();
                } catch (SQLException ex) {
                    Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            cashwclear();
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
      
        if (cashwcodetext.getText().isEmpty()) {
        if (cashwintypebox.getSelectedIndex() == 0 || bankinfoboxwin.getSelectedIndex() == 0 || cashwamounttext1.getText().isEmpty()) {

        } else {
            try {
                amountCheck();
            } catch (SQLException ex) {
                Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       // cashwthdrwin.hide();
        cashwinhistorytable();
        try {
            bankstatement();
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void bankinfoboxwinPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_bankinfoboxwinPopupMenuWillBecomeInvisible

    }//GEN-LAST:event_bankinfoboxwinPopupMenuWillBecomeInvisible

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
int row = datatbl.getSelectedRow();
accountid= (datatbl.getModel().getValueAt(row, 1).toString());
     
        if (SwingUtilities.isRightMouseButton(evt)) {
            tablemenu.show(evt.getComponent(), evt.getX() + 1, evt.getY() + 1);

        }
    }//GEN-LAST:event_datatblMouseClicked

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        try {
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        try {

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/AccountReport.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();

            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);

        } catch (NumberFormatException | JRException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void accounttextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_accounttextKeyReleased

        if (accounttext.getText().isEmpty()) {
            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            search();

        }
    }//GEN-LAST:event_accounttextKeyReleased

    private void accountpanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_accountpanelMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_accountpanelMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
      if(codetext.getText().isEmpty()){
      
      
      }else{
        double prasentbalance=Double.parseDouble(prasentbalancetext.getText());
        if(prasentbalance>0){
        JOptionPane.showMessageDialog(null, "This Account has Amount Value,You cant Remove Before prasent Balance is zero");
        }else{
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            try {
                String sql = "Delete from BankAccount where id=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, codetext.getText());

                pst.execute();
                JOptionPane.showMessageDialog(null, "Data Deleted");

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
        try {
            clear();
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      }}
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            clear();
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (codetext.getText().isEmpty()) {

            try {
                addStock();

            } catch (SQLException ex) {
                Logger.getLogger(BankInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            stockUpdate();

        }
       
        try {
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(BankInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void accountNoTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_accountNoTextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            accounttypebox.requestFocusInWindow();

        }
    }//GEN-LAST:event_accountNoTextKeyPressed

    private void addresstextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_addresstextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            accountNoText.requestFocusInWindow();

        }
    }//GEN-LAST:event_addresstextKeyPressed

    private void brachtextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_brachtextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            addresstext.requestFocusInWindow();

        }
    }//GEN-LAST:event_brachtextKeyPressed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        try {
            bankstatement();
            totalbalance();
        } catch (SQLException ex) {
            Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void reportboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportboxActionPerformed
        try {

            //  int total = Integer.parseInt(AllTotalText.getText());
            //String inwords = convert(total) + " Tk only";
            String fromdate = ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).getText();
            String todate = ((JTextField) todatepayment1.getDateEditor().getUiComponent()).getText();

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/Bankstatementreport.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();
            para.put("fromdate", fromdate);
            para.put("todate", todate);
            para.put("year", yeartext.getText());
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);

        } catch (NumberFormatException | JRException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }

    }//GEN-LAST:event_reportboxActionPerformed

    private void overalsubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overalsubmitActionPerformed
        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) bankstatementtbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select DATE_FORMAT(inputdate, '%d-%m-%Y') as 'InputDate', Description, bank as 'BankName', AccountNo  as 'AccNo', Balance as 'AccAmount', cashin as 'CashIn', cashout as 'CashOut', prasentbalance as 'AccBalance',totalbalance as 'TotalBalance',remark as 'Remark' from bankoverall where inputdate BETWEEN ? AND ? OR year(inputdate)='" + yeartext.getText() + "'";
            pst = conn.prepareStatement(sql);
            pst.setString(1, ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).getText());
            pst.setString(2, ((JTextField) todatepayment1.getDateEditor().getUiComponent()).getText());
            rs = pst.executeQuery();
            // bankstatementtbl.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String InputDate = rs.getString("InputDate");
                String Description = rs.getString("Description");
                String BankName = rs.getString("BankName");
                String AccNo = rs.getString("AccNo");
                double AccAmount = rs.getDouble("AccAmount");
                double CashIn = rs.getDouble("CashIn");
                double CashOut = rs.getDouble("CashOut");
                double AccBalance = rs.getDouble("AccBalance");
                double TotalBalance = rs.getDouble("TotalBalance");
                String remark = rs.getString("Remark");
                tree++;
                model2.addRow(new Object[]{tree, InputDate, Description, BankName, AccNo, AccAmount, CashIn, CashOut, AccBalance, TotalBalance, remark});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_overalsubmitActionPerformed

    private void vewdataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vewdataActionPerformed
        try {

            int row = cashwhistorybl.getSelectedRow();
            String table_click = (cashwhistorybl.getModel().getValueAt(row, 1).toString());
            String sql = "Select cashwin.id as  'id',cashwin.inputdate as 'inputdate',cashwin.paytype as'paytype',cashwin.amount as 'amount',cashwin.remark as 'remark',cashwin.bank as 'bank',cashwin.accountno as 'accountno',bn.accountholder as 'holder',bn.accounttype as 'accounttype',bn.cashin as 'cashin',bn.cashout as 'cashout' from cashwithdrawin cashwin inner join bankaccount bn ON cashwin.accountno=bn.accountno 	 where cashwin.id='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String id = rs.getString("id");
                cashwcodetext.setText(id);
                Date input = rs.getDate("inputdate");
                winputdate.setDate(input);
                String paytype = rs.getString("paytype");
                cashwintypebox.setSelectedItem(paytype);
                String amount = rs.getString("amount");
                cashwamounttext1.setText(amount);
                String remark = rs.getString("remark");
                remarktextcashw.setText(remark);
                String bank = rs.getString("bank");
                bankinfoboxwin.setSelectedItem(bank);
                String accountno = rs.getString("accountno");
                ///  accountnobox.addItem(accountno);
                accountnobox.setSelectedItem(accountno);
                String holder = rs.getString("holder");
                wholdertext.setText(holder);
                String accounttype = rs.getString("accounttype");
                wactype.setText(accounttype);
                float cashin = rs.getFloat("cashin");
                float cashout = rs.getFloat("cashout");
                float prasentbalance = cashin - cashout;
                String balance = String.format("%.2f", prasentbalance);
                wbalance.setText(balance);
                wbankname.setText(bank);

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

       
    }//GEN-LAST:event_vewdataActionPerformed
    private void AccountCheck() {
        String SearchText = (String) accountnobox.getSelectedItem();
        try {

            String sql = "Select cashin,cashout from BankAccount where accountno='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            if (rs.next()) {

                publiccashin = rs.getFloat("cashin");
                publiccashout = rs.getFloat("cashout");

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        if (cashwcodetext.getText().isEmpty()) {

        } else {
            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from cashwithdrawin where id=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, cashwcodetext.getText());

                    pst.execute();
                   // JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                String type = (String) cashwintypebox.getSelectedItem();
                try {
                    String sql = "Delete from bankoverall where transactionid=? AND Description='" + type + "' ";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, cashwcodetext.getText());

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                AccountCheck();
                float cashin = 0;
                float cashout = 0;

                String Accno = (String) accountnobox.getSelectedItem();
                float amount = Float.parseFloat(cashwamounttext1.getText());
                if ("Cash In".equals(type)) {
                    cashin = 0;
                    cashout = amount;

                } else {
                    cashin = amount;
                    cashout = 0;

                }
                float accountcashin = publiccashin + cashin;
                float accountcashout = publiccashout + cashout;
                balanceupdate(Accno, accountcashin, accountcashout);
                try {
                    cashwclear();
                    cashwinhistorytable();
                } catch (SQLException ex) {
                    Logger.getLogger(Bank.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }//GEN-LAST:event_deleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Bankinfobox;
    private javax.swing.JTextField accountNoText;
    private javax.swing.JTextField accountholdertext;
    private javax.swing.JComboBox<String> accountnobox;
    private javax.swing.JPanel accountpanel;
    private javax.swing.JTextField accounttext;
    private javax.swing.JComboBox<String> accounttypebox;
    private javax.swing.JTextField addresstext;
    private javax.swing.JComboBox<String> bankinfoboxwin;
    private javax.swing.JTable bankstatementtbl;
    private javax.swing.JTextField brachtext;
    private javax.swing.JTextField cashintext;
    private javax.swing.JTextField cashouttext;
    private javax.swing.JTextField cashwamounttext1;
    private javax.swing.JTextField cashwcodetext;
    private javax.swing.JTable cashwhistorybl;
    private javax.swing.JComboBox<String> cashwintypebox;
    private javax.swing.JPanel cashwthdrwin;
    private javax.swing.JTextField codetext;
    private javax.swing.JComboBox<String> currencytypebox;
    private javax.swing.JTable datatbl;
    private javax.swing.JMenuItem delete;
    private com.toedter.calendar.JDateChooser fromdatepayment1;
    private com.toedter.calendar.JDateChooser inputdate;
    private com.toedter.calendar.JDateChooser inputdate2;
    private com.toedter.calendar.JDateChooser inputdate3;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField openingbalancetext;
    private javax.swing.JButton overalsubmit;
    private javax.swing.JTextField prasentbalancetext;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JTextArea remarktextcashw;
    private javax.swing.JMenuItem report;
    private javax.swing.JButton reportbox;
    private javax.swing.JMenuItem statementview;
    private javax.swing.JComboBox<String> statusbox;
    private javax.swing.JPopupMenu tablemenu;
    private javax.swing.JPopupMenu tablemenuone;
    private com.toedter.calendar.JDateChooser todatepayment1;
    private javax.swing.JTextField totalbalancetext;
    private javax.swing.JLabel totalbalancetext1;
    private javax.swing.JTextField totalcashintext;
    private javax.swing.JTextField totalcashwtext;
    private javax.swing.JMenuItem vewdata;
    private javax.swing.JMenuItem view;
    private javax.swing.JLabel wactype;
    private javax.swing.JLabel wbalance;
    private javax.swing.JLabel wbankname;
    private javax.swing.JLabel wholdertext;
    private com.toedter.calendar.JDateChooser winputdate;
    private javax.swing.JTextField yeartext;
    // End of variables declaration//GEN-END:variables
}
