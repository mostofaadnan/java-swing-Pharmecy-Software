/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class CreditPaymen extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String cusId;
    float balancedue = (float) 0.0;
    float totalb;
    float accountbalance;
    float bankbalance1;
    float balance = (float) 0.0;
    float prasentbalance;

    //customer update
    float openingbalance;
    float dipositamt;

    float creditAmnt;
    float balancedue1;
    float paidamt;
    int new_inv;
    float afterbalancedue;
    String trnno;
    int upadatekey = 0;
    // Dashboard dashboard = new Dashboard();
    int userid = 0;

    float bankcashout = 0;
    float bankcashin = 0;
    float updatebankbalance = 0;
    int updatekey = 0;
    float Paidableamt = 0;
    float recievedamt = 0;
    float salerecieved = 0;
    int type = 1;

    /**
     * Creates new form CreditPaymen
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public CreditPaymen() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        customer();
        currentDate();
        totalbalance();
        AutoCompleteDecorator.decorate(customerbox);
        AutoCompleteDecorator.decorate(Bankinfobox);
        AutoCompleteDecorator.decorate(accountbox);
        checkBalance();
        CreditInvoice();
        deletebtn.setEnabled(false);
        parchase_code();
        accsessModification();
        itempane.setEnabledAt(1, false);

        //codetext.hide();
    }

    public CreditPaymen(String table_click) throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        customer();
        currentDate();
        totalbalance();
        AutoCompleteDecorator.decorate(customerbox);
        AutoCompleteDecorator.decorate(Bankinfobox);
        AutoCompleteDecorator.decorate(accountbox);
        checkBalance();
        CreditInvoice();
        customerbox.setEnabled(false);
        paidamounttext.setEditable(false);
        duetext.setEditable(false);
        paymemtypebox.setEditable(false);
        Bankinfobox.setEnabled(false);
        accountbox.setEnabled(false);
        chequenotext.setEnabled(false);
        paymemtypebox.setEnabled(false);
        trnno = table_click;
        Data_View();
        accsessModification();
        itempane.setEnabledAt(1, false);
        // parchase_code();
        //codetext.hide();

    }

    private void CreditInvoice() throws SQLException {
        try {
            String sql = "Select invoiceNo from sale where due>0 ORDER BY id DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("invoiceNo");
                itemnamesearch.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void userkey() throws IOException, SQLException {
        FileInputStream fis = new FileInputStream("src\\userkey.properties");
        Properties p = new Properties();
        p.load(fis);

        String userids = (String) p.get("userid");
        userid = Integer.parseInt(userids);
        // this.dispose();
        //LoginAccess desboard=new LoginAccess();
    }

    private void accsessModification() {
        try {
            String sql = "Select creditpedit,creditpdelete from modificationaccsess where userid='" + userid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                int creditpedit = rs.getInt("creditpedit");
                int creditpdelete = rs.getInt("creditpdelete");
                if (creditpedit == 1) {
                    svbtn.setEnabled(true);
                } else {
                    svbtn.setEnabled(false);
                }
                if (creditpdelete == 1) {
                    deletebtn.setEnabled(true);
                } else {
                    deletebtn.setEnabled(false);
                }
            } else {
                svbtn.setEnabled(false);
                deletebtn.setEnabled(false);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void Data_View() {
        updatekey = 1;
        try {
            String sql = "Select id,TR_No as 'trans', (select customername from customerInfo cin where cin.id=inp.customerid) as 'cusname',customerid,paidamount,paymenttype,paymenttype,chaqueno,bank,accno from invoicepaymenthistory inp where inp.TR_No='" + trnno + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String trans = rs.getString("trans");
                tranidtext.setText(trans);
                String cusname = rs.getString("cusname");
                customerbox.setSelectedItem(cusname);
                cusId = rs.getString("customerid");
                customeridtext.setText(cusId);
                String paidamtdata = rs.getString("paidamount");
                paidamounttext.setText(paidamtdata);
                String paymenttype = rs.getString("paymenttype");
                paymemtypebox.setSelectedItem(paymenttype);
                if ("Bank".equals(paymenttype)) {
                    String chaqueno = rs.getString("chaqueno");
                    chequenotext.setText(chaqueno);
                    String bank = rs.getString("bank");
                    Bankinfobox.setSelectedItem(bank);
                    String accno = rs.getString("accno");
                    accountbox.addItem(accno);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        String Customername = (String) customerbox.getSelectedItem();
        try {
            String sql = "Select * from customerInfo  where customername='" + Customername + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                openingbalance = rs.getFloat("OpenigBalance");
                dipositamt = rs.getFloat("DipositAmt");
                creditAmnt = rs.getFloat("creditAmnt");
                paidamt = rs.getFloat("paidamount");
                balancedue1 = rs.getFloat("Balancedue");
                String trans = String.format("%s", balancedue1);
                previseamonmttext.setText(trans);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void parchase_code() {

        try {

            String NewParchaseCode = ("TR-" + new_inv + "1");
            String sql = "Select id from invoicepaymenthistory";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = ("TR-" + ParchaseCode + "");
            }
            tranidtext.setText(NewParchaseCode);
        } catch (SQLException | NumberFormatException e) {
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

    private void checkBalance() {
        String accno = (String) accountbox.getSelectedItem();
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void addStock() throws SQLException, JRException {
        String remark;
        if (type == 1) {
            remark = "Payment By Invoice. Invoice No:" + itemnamesearch.getSelectedItem();
        } else {
            remark = "Payment By Customer";
        }
        try {
            float amount = recievedamt;
            String accountno = (String) accountbox.getSelectedItem();
            float presentbalnce = bankcashin + amount;
            afterbalancedue = balancedue1 - amount;
            /// String afterdue = df2.format(afterbalancedue);
            String sql = "Insert into invoicepaymenthistory(TR_No,"
                    + "customerid,"
                    + "paymentdate,"
                    + "amount,"
                    + "paidamount,"
                    + "balancedue,"
                    + "paymenttype,"
                    + "bank,"
                    + "accno,"
                    + "chaqueno,"
                    + "remark,"
                    + "inputuser,"
                    + "InvoiceNo) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, tranidtext.getText());
            pst.setString(2, cusId);
            pst.setString(3, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
            pst.setFloat(4, Paidableamt);
            pst.setFloat(5, recievedamt);
            pst.setFloat(6, balancedue);
            pst.setString(7, (String) paymemtypebox.getSelectedItem());
            if (paymemtypebox.getSelectedIndex() == 1) {
                pst.setString(8, (String) Bankinfobox.getSelectedItem());
                pst.setString(9, (String) accountbox.getSelectedItem());
                pst.setString(10, chequenotext.getText());
            } else {
                pst.setString(8, "");
                pst.setString(9, "");
                pst.setString(10, "");
            }
            pst.setString(11, remark);
            pst.setInt(12, userid);
            pst.setString(13, (String) itemnamesearch.getSelectedItem());
            pst.execute();
            ResultSet rshere = pst.getGeneratedKeys();
            int generatedKey = 0;
            if (rshere.next()) {
                generatedKey = rshere.getInt(1);
                if (paymemtypebox.getSelectedIndex() == 1) {
                    balanceupdate(accountno, presentbalnce);
                    balanceamount(accountno);
                    totalbalance();
                    overallinsert(presentbalnce, generatedKey);
                } else {
                    balance();
                    cashInDrwaer(amount, generatedKey);
                }

            }

            customerbalanceupdate();
            if (type == 1) {
                SaleUpdate();
            }
            JOptionPane.showMessageDialog(null, "Credit Payment Recieved Successfuly");

            printInvoice();

            //clear();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void SaleUpdate() {
        float totalrecied = (salerecieved + recievedamt);
        try {
            String sql = "Update sale set "
                    + "due='" + balancedue + "',"
                    + "recieved='" + totalrecied + "' where invoiceNo='" + itemnamesearch.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();
            // JOptionPane.showMessageDialog(null, "Data Update");
            //   clear();
            totalbalance();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void statementinsert(float balancedue) throws SQLException {
        String remark;

        if (paymemtypebox.getSelectedIndex() == 0) {
            remark = "Payment type:Cash";
        } else {
            String bank = (String) Bankinfobox.getSelectedItem();
            String acc = (String) accountbox.getSelectedItem();
            String cheque = chequenotext.getText();
            remark = "Acc No:" + acc + "," + "Che.No:" + cheque + "," + "Bank:" + bank;
        }
        try {

            String sql = "Insert into CraditStatement(customerid,Orderno,type,invoiceno,invoicedate,totalInvoice,Received_amt,remark,balance) values(?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, cusId);
            pst.setString(2, "0");
            pst.setString(3, "Payment");
            pst.setString(4, tranidtext.getText());
            pst.setString(5, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
            pst.setDouble(6, 0.00);
            pst.setFloat(7, recievedamt);
            pst.setString(8, remark);
            pst.setFloat(9, balancedue);
            pst.execute();
            ResultSet rshere = pst.getGeneratedKeys();
            int generatedKey = 0;
            if (rshere.next()) {
                generatedKey = rshere.getInt(1);
            }
            // JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

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

    private void cashInDrwaer(float cashin, int tranid) {

        Float balanc = bankbalance1 + cashin;
        String trans = tranidtext.getText();
        try {

            String sql = "Insert into CashDrower(cashin,cashout,balance,type,upatedate,tranid) values(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setFloat(1, cashin);
            pst.setFloat(2, 0);
            pst.setFloat(3, balanc);
            pst.setString(4, "Credit Payment");
            pst.setString(5, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
            pst.setString(6, trans);
            pst.execute();

            //  JOptionPane.showMessageDialog(null, "Data Saved");
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

    private void overallinsert(float presentbalnce, int tranid) {
        String trans = String.format("%s", tranid);
        float cashin = Float.parseFloat(paidamounttext.getText());
        float cashout = 0;
        try {
            String reason = "Credit Payment";
            String sql = "Insert into bankoverall(inputdate,Description,bank,AccountNo,cashin,cashout,Balance,prasentbalance,totalbalance,remark,transactionid) values(?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());

            pst.setString(2, reason);
            pst.setString(3, (String) Bankinfobox.getSelectedItem());
            pst.setString(4, (String) accountbox.getSelectedItem());
            pst.setFloat(5, cashin);
            pst.setFloat(6, cashout);
            pst.setFloat(7, balance);
            pst.setFloat(8, presentbalnce);
            pst.setFloat(9, totalb);

            pst.setString(10, "Credit Payment");
            pst.setString(11, trans);

            pst.execute();

            // JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
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
                //expensesearchbox.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
    private static final DecimalFormat df2 = new DecimalFormat("#.00");

    private void customerbalanceupdate() throws SQLException {
        float paidamount = recievedamt;
        float afterpaid = paidamt + paidamount;
        float customer_amt = afterpaid;
        float netsale = creditAmnt;
        afterbalancedue = balancedue1 - paidamount;
        String afterdue = df2.format(afterbalancedue);
        try {
            String sql = "Update customerInfo set Balancedue='" + afterdue + "',paidamount='" + afterpaid + "' where customerid='" + cusId + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");
            statementinsert(afterbalancedue);
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void customerbalanceupdateafterdelete() throws SQLException {
        float paidamount = Float.parseFloat(paidamounttext.getText());
        float afterpaid = Math.abs(paidamt - paidamount);
        afterbalancedue = balancedue1 - paidamount;
        try {
            String sql = "Update customerInfo set Balancedue='" + afterbalancedue + "',paidamount='" + afterpaid + "' where id='" + cusId + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
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

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        java.sql.Date date = new java.sql.Date(now.getTime());
        openingdate.setDate(date);

    }

    private void clear() throws SQLException {
        updatekey = 0;
        customerbox.setSelectedIndex(0);
        customeridtext.setText(null);
        previseamonmttext.setText(null);
        paidamounttext.setText(null);
        duetext.setText(null);
        paymemtypebox.setSelectedIndex(0);
        chequenotext.setText(null);
        paidamounttext1.setText(null);
        duetext1.setText(null);
        itemnamesearch.removeAllItems();
        itemnamesearch.addItem("Select");
        CreditInvoice();
        Datetext.setText(null);
        customertext.setText(null);
        nettotaltexts.setText(null);
        netTotalText.setText(null);
        Paidableamt = 0;
        recievedamt = 0;
        salerecieved = 0;
        type = 1;
        parchase_code();
    }

    private void printInvoice() throws JRException {
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Payment Voucher", "GRN Print", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            try {
                double amount = recievedamt;
                String amounts = df2.format(amount);
                String inwords = convertToIndianCurrency(amounts);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/creditvoucher.jrxml");
                //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

                HashMap para = new HashMap();
                para.put("transactionno", tranidtext.getText());
                para.put("inword", inwords);
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {
                JOptionPane.showMessageDialog(null, ex);

            }
        }

    }

    private void Selectbankaccount() {

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

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void creditpaymentdeleteDrwoerbank() {

        float paidamount = Float.parseFloat(paidamounttext.getText());
        float afterpaid = paidamt - paidamount;

        afterbalancedue = balancedue1 + paidamount;
        String afterdue = df2.format(afterbalancedue);
        try {

            String sql = "Update customerInfo set Balancedue='" + afterdue + "',paidamount='" + afterpaid + "' where id='" + cusId + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");
            statementinsert(afterbalancedue);
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        String reaon = "Credit Payment";
        if (paymemtypebox.getSelectedIndex() == 0) {

            try {
                String sql = "Delete from CashDrower where type='" + reaon + "' AND tranid=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, tranidtext.getText());

                pst.execute();
                //JOptionPane.showMessageDialog(null, "Data Deleted");

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        } else {
            //bank  payment delete
            try {
                String sql = "Delete from bankoverall where Description='" + reaon + "' AND transactionid=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, tranidtext.getText());

                pst.execute();
                //JOptionPane.showMessageDialog(null, "Data Deleted");

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            Selectbankaccount();
            float amount = Float.parseFloat(paidamounttext.getText());
            float cashoutminus = bankcashin - amount;
            String accountno = (String) accountbox.getSelectedItem();
            String bank = (String) Bankinfobox.getSelectedItem();

            try {
                String sql = "Update BankAccount set cashin='" + cashoutminus + "' where accountno='" + accountno + "' AND bank='" + bank + "'";
                pst = conn.prepareStatement(sql);

                pst.execute();
                // JOptionPane.showMessageDialog(null, "Data Update");
                //   clear();
                // totalbalance();
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
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
        jLabel3 = new javax.swing.JLabel();
        paymemtypebox = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        chequenotext = new javax.swing.JTextField();
        Bankinfobox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        accountbox = new javax.swing.JComboBox<>();
        itempane = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        Datetext = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        netTotalText = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        paidamounttext1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        duetext1 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        customertext = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        nettotaltexts = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        duetext = new javax.swing.JTextField();
        customeridtext = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        paidamounttext = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        customerbox = new javax.swing.JComboBox<>();
        previseamonmttext = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        deletebtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        svbtn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        openingdate = new com.toedter.calendar.JDateChooser();
        tranidtext = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        typebox = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Credit Recieve Entry");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Payment Type");

        paymemtypebox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        paymemtypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Bank" }));
        paymemtypebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                paymemtypeboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Bank Info(Payment)"));

        jLabel5.setText("Acc No");

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

        jLabel4.setText("Bank Name");

        jLabel6.setText("Cheque No");

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
        accountbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Bankinfobox, 0, 336, Short.MAX_VALUE)
                    .addComponent(accountbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chequenotext))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(Bankinfobox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(accountbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(chequenotext, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jLabel13.setText("Invoice No");

        itemnamesearch.setEditable(true);
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

        jLabel14.setText("Invoice Date");

        Datetext.setEditable(false);
        Datetext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Datetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        Datetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DatetextKeyReleased(evt);
            }
        });

        jLabel15.setText("Paidable Amount");

        netTotalText.setEditable(false);
        netTotalText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        netTotalText.setForeground(new java.awt.Color(204, 0, 0));
        netTotalText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netTotalText.setDisabledTextColor(new java.awt.Color(153, 0, 0));

        jLabel16.setText("Recieved");

        paidamounttext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        paidamounttext1.setForeground(new java.awt.Color(204, 0, 0));
        paidamounttext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paidamounttext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                paidamounttext1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paidamounttext1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                paidamounttext1KeyTyped(evt);
            }
        });

        jLabel17.setText("Remain Amount");

        duetext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        duetext1.setForeground(new java.awt.Color(153, 0, 0));
        duetext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel18.setText("Customer");

        customertext.setEditable(false);
        customertext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        customertext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        customertext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customertextKeyReleased(evt);
            }
        });

        jLabel19.setText("Net Total");

        nettotaltexts.setEditable(false);
        nettotaltexts.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        nettotaltexts.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        nettotaltexts.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        nettotaltexts.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nettotaltextsKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemnamesearch, 0, 342, Short.MAX_VALUE)
                            .addComponent(Datetext)
                            .addComponent(customertext)
                            .addComponent(nettotaltexts)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(paidamounttext1)
                            .addComponent(netTotalText)
                            .addComponent(duetext1))))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Datetext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customertext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nettotaltexts, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(netTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(paidamounttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(duetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        itempane.addTab("By Invoice", jPanel6);

        jLabel9.setText("Due Amt");

        duetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        duetext.setForeground(new java.awt.Color(153, 0, 0));
        duetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        customeridtext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        customeridtext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        customeridtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                customeridtextKeyReleased(evt);
            }
        });

        jLabel1.setText("Customer Name");

        jLabel8.setText("Paid Amount");

        paidamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        paidamounttext.setForeground(new java.awt.Color(204, 0, 0));
        paidamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paidamounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                paidamounttextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paidamounttextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                paidamounttextKeyTyped(evt);
            }
        });

        jLabel7.setText("Cust ID");

        jLabel10.setText("Total (Credit Amt)");

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

        previseamonmttext.setEditable(false);
        previseamonmttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        previseamonmttext.setForeground(new java.awt.Color(204, 0, 0));
        previseamonmttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        previseamonmttext.setDisabledTextColor(new java.awt.Color(153, 0, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customeridtext, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(customerbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(paidamounttext)
                            .addComponent(previseamonmttext)
                            .addComponent(duetext))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customeridtext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(previseamonmttext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(paidamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(duetext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        itempane.addTab("By Customer", jPanel2);

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));

        deletebtn.setBackground(new java.awt.Color(102, 0, 0));
        deletebtn.setForeground(new java.awt.Color(255, 255, 255));
        deletebtn.setText("Delete");
        deletebtn.setBorder(null);
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 153, 51));
        jButton2.setText("Refresh");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        svbtn.setBackground(new java.awt.Color(204, 0, 0));
        svbtn.setForeground(new java.awt.Color(255, 255, 255));
        svbtn.setText("Execute");
        svbtn.setBorder(null);
        svbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svbtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(svbtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));

        openingdate.setForeground(new java.awt.Color(102, 102, 102));
        openingdate.setDateFormatString("yyyy-MM-dd");

        tranidtext.setEditable(false);
        tranidtext.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Tran Id");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Payment Date");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(openingdate, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tranidtext, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tranidtext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(openingdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jLabel20.setText("Type");

        typebox.setBackground(new java.awt.Color(255, 255, 255));
        typebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "By Invoice" }));
        typebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                typeboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(60, 60, 60)
                            .addComponent(paymemtypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(itempane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 501, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(typebox, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typebox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(itempane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paymemtypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        setBounds(50, 10, 539, 562);
    }// </editor-fold>//GEN-END:initComponents

    private void chequenotextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chequenotextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chequenotextActionPerformed

    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed

        if (updatekey == 0) {
            if (recievedamt <= 0) {
                JOptionPane.showMessageDialog(null, "Please Select The value");
            } else {
                try {
                    parchase_code();
                    addStock();
                    // statementinsert();
                    //customerbalanceupdate();
                    clear();
                } catch (SQLException | JRException ex) {
                    Logger.getLogger(CreditPaymen.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }//GEN-LAST:event_svbtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            clear();
        } catch (SQLException ex) {
            Logger.getLogger(CreditPaymen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        if (updatekey == 1) {
            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from invoicepaymenthistory where TR_No=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, tranidtext.getText());
                    pst.execute();
                    // JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                creditpaymentdeleteDrwoerbank();
                try {
                    clear();
                } catch (SQLException ex) {
                    Logger.getLogger(CreditPaymen.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }//GEN-LAST:event_deletebtnActionPerformed

    private void BankinfoboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_BankinfoboxPopupMenuWillBecomeInvisible
        accountbox.removeAllItems();

        String SearchText = (String) Bankinfobox.getSelectedItem();
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

    }//GEN-LAST:event_BankinfoboxPopupMenuWillBecomeInvisible

    private void customerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_customerboxPopupMenuWillBecomeInvisible
        String Customername = (String) customerbox.getSelectedItem();
        try {
            String sql = "Select * from customerInfo  where customername='" + Customername + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {

                cusId = rs.getString("customerid");
                customeridtext.setText(cusId);

                openingbalance = rs.getFloat("OpenigBalance");
                dipositamt = rs.getFloat("DipositAmt");

                creditAmnt = rs.getFloat("creditAmnt");
                paidamt = rs.getFloat("paidamount");
                balancedue1 = rs.getFloat("Balancedue");

                String trans = String.format("%s", balancedue1);
                previseamonmttext.setText(trans);
                Paidableamt = rs.getFloat("Balancedue");

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }


    }//GEN-LAST:event_customerboxPopupMenuWillBecomeInvisible

    private void paidamounttextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttextKeyPressed


    }//GEN-LAST:event_paidamounttextKeyPressed

    private void paidamounttextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttextKeyReleased
        if (paidamounttext.getText().isEmpty() || paidamounttext.getText().matches("^[a-zA-Z]+$") || "0".equals(paidamounttext.getText())) {
            recievedamt = 0;
        } else {
            recievedamt = Float.parseFloat(paidamounttext.getText());
            if (Paidableamt >= recievedamt) {
                balancedue = Paidableamt - recievedamt;
                duetext.setText(df2.format(balancedue));
            } else {
                recievedamt = 0;
                duetext.setText(null);
            }
        }
    }//GEN-LAST:event_paidamounttextKeyReleased

    private void paidamounttextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttextKeyTyped
//duetext.setText(null);
    }//GEN-LAST:event_paidamounttextKeyTyped

    private void paymemtypeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_paymemtypeboxPopupMenuWillBecomeInvisible

        Bankinfobox.removeAllItems();
        Bankinfobox.addItem("Select");
        accountbox.removeAllItems();
        accountbox.addItem("Select");
        if (paymemtypebox.getSelectedIndex() == 1) {

            try {
                bankaccount();
            } catch (SQLException ex) {
                Logger.getLogger(CreditPaymen.class.getName()).log(Level.SEVERE, null, ex);
            }

        }


    }//GEN-LAST:event_paymemtypeboxPopupMenuWillBecomeInvisible

    private void accountboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_accountboxActionPerformed

    private void accountboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_accountboxPopupMenuWillBecomeInvisible
        checkBalance();
    }//GEN-LAST:event_accountboxPopupMenuWillBecomeInvisible

    private void customeridtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customeridtextKeyReleased

        if (customeridtext.getText().isEmpty()) {
            previseamonmttext.setText(null);
            customerbox.setSelectedIndex(0);
        } else {
            try {
                String sql = "Select * from customerInfo  where customerid='" + customeridtext.getText() + "'";
                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();
                if (rs.next()) {
                    String customername = rs.getString("customername");
                    customerbox.setSelectedItem(customername);
                    cusId = rs.getString("customerid");
                    // customeridtext.setText(cusId);

                    openingbalance = rs.getFloat("OpenigBalance");
                    dipositamt = rs.getFloat("DipositAmt");

                    creditAmnt = rs.getFloat("creditAmnt");
                    paidamt = rs.getFloat("paidamount");
                    balancedue1 = rs.getFloat("Balancedue");

                    String trans = String.format("%s", balancedue1);
                    previseamonmttext.setText(trans);

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }//GEN-LAST:event_customeridtextKeyReleased

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
        try {
            String sql = "Select "
                    + "invoiceNo,"
                    + "invoicedate,"
                    + "recieved,"
                    + "due,"
                    + "nettotal,"
                    + "customerid,"
                    + "(select ci.customername from customerInfo ci "
                    + "where ci.customerid=sl.customerid ) as 'customername'"
                    + " from sale sl  where sl.invoiceNo='" + itemnamesearch.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String Id = rs.getString("invoiceNo");
                itemnamesearch.setSelectedItem(Id);
                String date = rs.getString("invoicedate");
                Datetext.setText(date);
                double due = rs.getDouble("due");
                netTotalText.setText(df2.format(due));
                Paidableamt = rs.getFloat("due");
                double netinoice = rs.getDouble("nettotal");
                nettotaltexts.setText(df2.format(netinoice));
                cusId = rs.getString("customerid");
                String customername = rs.getString("customername");
                customertext.setText(customername);
                salerecieved = rs.getFloat("recieved");
                //  currency=rs.getString("paymentCurency");
            } else {
                clear();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void DatetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DatetextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_DatetextKeyReleased

    private void paidamounttext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttext1KeyPressed


    }//GEN-LAST:event_paidamounttext1KeyPressed

    private void paidamounttext1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttext1KeyReleased
        if (paidamounttext1.getText().isEmpty() || paidamounttext1.getText().matches("^[a-zA-Z]+$") || "0".equals(paidamounttext1.getText())) {
            recievedamt = 0;
        } else {
            recievedamt = Float.parseFloat(paidamounttext1.getText());
            if (Paidableamt >= recievedamt) {
                balancedue = Paidableamt - recievedamt;
                duetext1.setText(df2.format(balancedue));
            } else {
                recievedamt = 0;
                duetext1.setText(null);
            }
        }

    }//GEN-LAST:event_paidamounttext1KeyReleased

    private void paidamounttext1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttext1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_paidamounttext1KeyTyped

    private void customertextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_customertextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_customertextKeyReleased

    private void nettotaltextsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nettotaltextsKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_nettotaltextsKeyReleased

    private void typeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_typeboxPopupMenuWillBecomeInvisible
        if (typebox.getSelectedIndex() == 0) {
            type = 1;
        } else {
            type = 2;
        }
    }//GEN-LAST:event_typeboxPopupMenuWillBecomeInvisible


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Bankinfobox;
    private javax.swing.JTextField Datetext;
    private javax.swing.JComboBox<String> accountbox;
    private javax.swing.JTextField chequenotext;
    private javax.swing.JComboBox<String> customerbox;
    private javax.swing.JTextField customeridtext;
    private javax.swing.JTextField customertext;
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField duetext;
    private javax.swing.JTextField duetext1;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JTabbedPane itempane;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField netTotalText;
    private javax.swing.JTextField nettotaltexts;
    private com.toedter.calendar.JDateChooser openingdate;
    private javax.swing.JTextField paidamounttext;
    private javax.swing.JTextField paidamounttext1;
    private javax.swing.JComboBox<String> paymemtypebox;
    private javax.swing.JTextField previseamonmttext;
    private javax.swing.JButton svbtn;
    private javax.swing.JTextField tranidtext;
    private javax.swing.JComboBox<String> typebox;
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
