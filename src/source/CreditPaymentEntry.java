/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import static source.CreditPaymen.convertToIndianCurrency;

/**
 *
 * @author FAHIM
 */
public class CreditPaymentEntry extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String cusId = null;
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
    int tree = 1;
    private static final DecimalFormat df2 = new DecimalFormat("#.00");

    /**
     * Creates new form CreditPaymentEntry
     *
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public CreditPaymentEntry() throws IOException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        //  customer();
        currentDate();
        totalbalance();
        // AutoCompleteDecorator.decorate(customerbox);
        AutoCompleteDecorator.decorate(Bankinfobox);
        AutoCompleteDecorator.decorate(accountbox);
        checkBalance();

        // CreditInvoice();
        // deletebtn.setEnabled(false);
        parchase_code();
        accsessModification();
        ExtractFilterCustomer();
        // itempane.setEnabledAt(1, false);
    }

    CreditPaymentEntry(String table_click) throws IOException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        //  customer();
        currentDate();
        totalbalance();
        // AutoCompleteDecorator.decorate(customerbox);
        AutoCompleteDecorator.decorate(Bankinfobox);
        AutoCompleteDecorator.decorate(accountbox);
        checkBalance();

        // CreditInvoice();
        // deletebtn.setEnabled(false);
        ExtractFilterCustomer();
        parchase_code();
        accsessModification();
        trnno = table_click;
        Data_View();
        updatekey = 1;
    }

    private void ExtractFilterCustomer() {
        final JTextField textfield = (JTextField) customerbox.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        customerbox.removeAllItems();
                        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
                        model2.setRowCount(0);
                        totalamounttext.setText(null);

                        itemnamesearch.removeAllItems();
                        itemnamesearch.addItem("Select");
                        itemnamesearch.setSelectedIndex(0);
                        parchase_code();
                        currentDate();
                        paymemtypebox.setSelectedIndex(0);
                        Bankinfobox.removeAllItems();
                        accountbox.removeAllItems();
                        chequenotext.setText(null);
                        updatekey = 0;
                        cusId = null;

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

            itemnamesearch.removeAllItems();
            itemnamesearch.addItem("Select");
            itemnamesearch.setSelectedIndex(0);
            parchase_code();
            currentDate();
            paymemtypebox.setSelectedIndex(0);
            Bankinfobox.removeAllItems();
            accountbox.removeAllItems();
            chequenotext.setText(null);
            updatekey = 0;
            cusId = null;

        }
    }

    private void Data_View() {
        updatekey = 1;
        try {
            String sql = "Select id,"
                    + "inp.TR_No as 'trans',"
                    + " (select customername from customerInfo cin where cin.customerid=inp.customerid) as 'cusname',"
                    + "customerid,"
                    + "paidamount,"
                    + "paymenttype,"
                    + "paymenttype,"
                    + "chaqueno,"
                    + "bank,"
                    + "accno"
                    + " from invoicepaymenthistory inp where inp.TR_No='" + trnno + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String trans = rs.getString("trans");
                tranidtext.setText(trans);
                String cusname = rs.getString("cusname");
                customerbox.setSelectedItem(cusname);
                cusId = rs.getString("customerid");
                String paidamtdata = rs.getString("paidamount");
                totalamounttext.setText(paidamtdata);
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
        try {
            DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
            model2.setRowCount(0);
            tree = 0;
            String sql = "Select cr.InvoiceNo as 'invnum',"
                    + "sl.invoicedate as 'invdate',"
                    + " sl.TotalAmount as 'amount',"
                    + "sl.netdiscount as 'discount',"
                    + " sl.TotalVat as 'vat',"
                    + " sl.nettotal as 'nettotal',"
                     + "cr.payment as 'payment',"
                     + "cr.due as 'due'"
                    + " FROM creditpaymentdetails cr inner join sale sl ON cr.InvoiceNo=sl.invoiceNo where cr.TranId='" + trnno + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            // datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String invnum = rs.getString("invnum");
                String invdate = rs.getString("invdate");
                String amount = rs.getString("amount");
                String discount = rs.getString("discount");
                String vat = rs.getString("vat");
                String nettotal = rs.getString("nettotal");
                String payment = rs.getString("payment");
                String due = rs.getString("due");
                tree++;
                model2.addRow(new Object[]{tree, invnum, invdate, amount, discount, vat, nettotal,payment,due});

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

    private void CreditInvoice() throws SQLException {
        itemnamesearch.removeAllItems();
        itemnamesearch.addItem("Select");
        itemnamesearch.setSelectedIndex(0);
        try {
            String sql = "Select invoiceNo from sale where due>0 AND customerid='" + cusId + "' ORDER BY id DESC";
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
                    //   deletebtn.setEnabled(true);
                } else {
                    // deletebtn.setEnabled(false);
                }
            } else {
                svbtn.setEnabled(false);
                //  deletebtn.setEnabled(false);
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

    private void checkentry() {

        String s = "";
        boolean exists = false;
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            s = dataTable.getValueAt(i, 1).toString().trim();
            if (itemnamesearch.getSelectedItem().equals(s)) {
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
        if (duetext.getText().isEmpty()) {

        } else {

            DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
            tree++;

            float totalamt = Float.parseFloat(nettotaltexts.getText());
            float payble = Float.parseFloat(paymenttext.getText());
            float due = Float.parseFloat(duetext.getText());
            model2.addRow(new Object[]{tree, itemnamesearch.getSelectedItem(), Datetext.getText(), amttext.getText(), discounttext.getText(), vattext.getText(), totalamt, payble, due});
            float amount = total_action_amount();
            totalamounttext.setText(df2.format(amount));
            clear();
        }

    }

    private float total_action_amount() {

        int rowaCount = dataTable.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());
            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 7).toString());
        }
        return (float) totaltpmrp;

    }

    private void clear() {
        itemnamesearch.setSelectedIndex(0);
        Datetext.setText(null);
        amttext.setText(null);
        discounttext.setText(null);
        vattext.setText(null);
        nettotaltexts.setText(null);
        paybletext.setText(null);
        paymenttext.setText(null);
        duetext.setText(null);
    }

    private void deleterow() {

        int[] toDelete = dataTable.getSelectedRows();
        Arrays.sort(toDelete); // be shure to have them in ascending order.
        DefaultTableModel myTableModel = (DefaultTableModel) dataTable.getModel();
        for (int ii = toDelete.length - 1; ii >= 0; ii--) {
            myTableModel.removeRow(toDelete[ii]); // beginning at the largest.
        }
        float amount = total_action_amount();
        totalamounttext.setText(df2.format(amount));
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

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        java.sql.Date date = new java.sql.Date(now.getTime());
        openingdate.setDate(date);

    }

    private void addStock() throws SQLException, JRException {

        try {
            float amount = Float.parseFloat(totalamounttext.getText());
            String accountno = (String) accountbox.getSelectedItem();
            float presentbalnce = bankcashin + amount;
            afterbalancedue = balancedue1 - amount;
            /// String afterdue = df2.format(afterbalancedue);
            String sql = "Insert into invoicepaymenthistory("
                    + "TR_No,"
                    + "customerid,"
                    + "paymentdate,"
                    + "paidamount,"
                    + "paymenttype,"
                    + "bank,"
                    + "accno,"
                    + "chaqueno,"
                    + "inputuser"
                    + ") values(?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, tranidtext.getText());
            pst.setString(2, cusId);
            pst.setString(3, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
            pst.setFloat(4, amount);
            pst.setString(5, (String) paymemtypebox.getSelectedItem());
            if (paymemtypebox.getSelectedIndex() == 1) {
                pst.setString(6, (String) Bankinfobox.getSelectedItem());
                pst.setString(7, (String) accountbox.getSelectedItem());
                pst.setString(8, chequenotext.getText());
            } else {
                pst.setString(6, "");
                pst.setString(7, "");
                pst.setString(8, "");
            }

            pst.setInt(9, userid);
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
            saleDetails();
            customerbalanceupdate();

            JOptionPane.showMessageDialog(null, "Credit Payment Recieved Successfuly");

            printInvoice();
            AfterExecution();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void saleDetails() {
        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String invoiceno = (String) dataTable.getValueAt(row, 1);
                float amount = (Float) dataTable.getValueAt(row, 6);
                float payment = (Float) dataTable.getValueAt(row, 7);
                float due = (Float) dataTable.getValueAt(row, 8);
                try {

                    String sql = "Insert into creditpaymentdetails(TranId,InvoiceNo,payment,due) values (?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, tranidtext.getText());
                    pst.setString(2, invoiceno);
                    pst.setFloat(3, payment);
                    pst.setFloat(4, due);
                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                SaleUpdate(payment,invoiceno,due);
            }
            //  loadGRN();
            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void SaleUpdate(float totalrecied, String invno, float dueamt) {

        String doubleAsString = String.valueOf(dueamt);
        int indexOfDecimal = doubleAsString.indexOf(".");
        String netpaybel = doubleAsString.substring(0, indexOfDecimal);
        try {
            String sql = "Update sale set "
                    + "due='" + dueamt + "',"
                    + "payble='" + netpaybel + "',"
                    + "recieved='" + totalrecied + "' where invoiceNo='" + invno + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();
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
            pst = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, cusId);
            pst.setString(2, "0");
            pst.setString(3, "Payment");
            pst.setString(4, tranidtext.getText());
            pst.setString(5, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
            pst.setDouble(6, 0.00);
            pst.setString(7, totalamounttext.getText());
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

    private void cashInDrwaer(float cashin, int tranid) throws SQLException {
        balance();
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
            totalbalance();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void overallinsert(float presentbalnce, int tranid) {
        String trans = String.format("%s", tranid);
        float cashin = Float.parseFloat(totalamounttext.getText());
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

    private void customerbalanceupdate() throws SQLException {
        float paidamount = Float.parseFloat(totalamounttext.getText());
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

    private void printInvoice() throws JRException {
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Payment Voucher", "GRN Print", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            try {
                double amount = Float.parseFloat(totalamounttext.getText());
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

    private void AfterExecution() {
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);
        totalamounttext.setText(null);
        customerbox.removeAllItems();
        itemnamesearch.removeAllItems();
        itemnamesearch.addItem("Select");
        itemnamesearch.setSelectedIndex(0);
        parchase_code();
        currentDate();
        paymemtypebox.setSelectedIndex(0);
        Bankinfobox.removeAllItems();
        accountbox.removeAllItems();
        chequenotext.setText(null);
        updatekey = 0;
        cusId = null;
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
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        tranidtext = new javax.swing.JTextField();
        openingdate = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        customerbox = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        Datetext = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        itemnamesearch = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        nettotaltexts = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        amttext = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        discounttext = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        vattext = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        paybletext = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        paymenttext = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        duetext = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        totalamounttext = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        paymemtypebox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        Bankinfobox = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        accountbox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        chequenotext = new javax.swing.JTextField();
        svbtn = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Credit Payment");

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Payment Date");

        tranidtext.setEditable(false);
        tranidtext.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        openingdate.setForeground(new java.awt.Color(102, 102, 102));
        openingdate.setDateFormatString("yyyy-MM-dd");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Tran Id");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Customer");

        customerbox.setEditable(true);
        customerbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                customerboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(2, 2, 2)
                .addComponent(customerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(2, 2, 2)
                .addComponent(openingdate, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(tranidtext, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(customerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tranidtext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(openingdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Invoice Date");

        jButton2.setText("Clear");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Invoice No");

        Datetext.setEditable(false);
        Datetext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jButton1.setBackground(new java.awt.Color(102, 0, 0));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        itemnamesearch.setBackground(new java.awt.Color(255, 255, 255));
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

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Net Total");

        nettotaltexts.setEditable(false);
        nettotaltexts.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        nettotaltexts.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Amount");

        amttext.setEditable(false);
        amttext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        amttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Discount");

        discounttext.setEditable(false);
        discounttext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        discounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Vat");

        vattext.setEditable(false);
        vattext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        vattext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Payble Amount");

        paybletext.setEditable(false);
        paybletext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        paybletext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paybletext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paybletextKeyReleased(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Payment");

        paymenttext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        paymenttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paymenttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                paymenttextKeyReleased(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Due");

        duetext.setEditable(false);
        duetext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        duetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(Datetext, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(amttext, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(vattext, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nettotaltexts, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paybletext, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(duetext, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton2)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(duetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(amttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Datetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nettotaltexts, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2)
                            .addComponent(paybletext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(paymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel18)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel16)
                                .addComponent(jLabel17)))
                        .addGap(36, 36, 36)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        jPanel4.setBackground(new java.awt.Color(0, 51, 51));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Payment Type");

        totalamounttext.setEditable(false);
        totalamounttext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalamounttext.setForeground(new java.awt.Color(153, 51, 0));
        totalamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Total Amount");

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

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Acc No");

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

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Bank");

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

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Cheque No");

        chequenotext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chequenotextActionPerformed(evt);
            }
        });

        svbtn.setBackground(new java.awt.Color(204, 0, 0));
        svbtn.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        svbtn.setForeground(new java.awt.Color(255, 255, 255));
        svbtn.setText("Execute");
        svbtn.setBorder(null);
        svbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svbtnActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 153, 51));
        jButton3.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Refresh");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(153, 0, 0));
        jButton4.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Delete");
        jButton4.setBorder(null);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Bankinfobox, 0, 262, Short.MAX_VALUE)
                    .addComponent(accountbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chequenotext)
                    .addComponent(svbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(paymemtypebox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalamounttext)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(1, 1, 1)
                .addComponent(totalamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel6)
                .addGap(1, 1, 1)
                .addComponent(paymemtypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel9)
                .addGap(1, 1, 1)
                .addComponent(Bankinfobox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel8)
                .addGap(1, 1, 1)
                .addComponent(accountbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chequenotext, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dataTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl", "Invoice No", "Invoice Date", "Amount", "Discount", "Vat", "Net Total", "Payment", "Due"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTable.setRowHeight(25);
        dataTable.setSelectionBackground(new java.awt.Color(0, 102, 102));
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
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void paymemtypeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_paymemtypeboxPopupMenuWillBecomeInvisible

        Bankinfobox.removeAllItems();
        Bankinfobox.addItem("Select");
        accountbox.removeAllItems();
        accountbox.addItem("Select");
        if (paymemtypebox.getSelectedIndex() == 1) {
            try {
                bankaccount();
            } catch (SQLException ex) {
                Logger.getLogger(CreditPaymentEntry.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_paymemtypeboxPopupMenuWillBecomeInvisible

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

    private void accountboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_accountboxPopupMenuWillBecomeInvisible
        checkBalance();
    }//GEN-LAST:event_accountboxPopupMenuWillBecomeInvisible

    private void accountboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_accountboxActionPerformed

    private void chequenotextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chequenotextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chequenotextActionPerformed

    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        if (cusId == null || totalamounttext.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Execute Faild, Due to Data Box Empty");
        } else {
            if (updatekey == 0) {
                parchase_code();
                currentDate();
                try {
                    addStock();
                } catch (SQLException | JRException ex) {
                    Logger.getLogger(CreditPaymentEntry.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {

            }

        }

    }//GEN-LAST:event_svbtnActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        clear();
        AfterExecution();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
        if (itemnamesearch.getSelectedIndex() > 0) {
            try {
                String sql = "Select "
                        + "invoiceNo,"
                        + "invoicedate,"
                        + "TotalAmount,"
                        + "netdiscount,"
                        + "TotalVat,"
                        + "recieved,"
                        + "due,"
                        + "nettotal,"
                        + "customerid,"
                        + "payble,"
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

                    String amount = rs.getString("TotalAmount");
                    amttext.setText(amount);
                    String discount = rs.getString("netdiscount");
                    discounttext.setText(discount);
                    String vat = rs.getString("TotalVat");
                    vattext.setText(vat);
                    double due = rs.getDouble("due");
                    // netTotalText.setText(df2.format(due));
                    Paidableamt = rs.getFloat("due");
                    double netinoice = rs.getDouble("nettotal");
                    nettotaltexts.setText(df2.format(netinoice));
                    cusId = rs.getString("customerid");

                    salerecieved = rs.getFloat("recieved");
                    String payble = rs.getString("payble");
                    paybletext.setText(payble);
                    //  currency=rs.getString("paymentCurency");
                } else {
                    clear();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void customerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_customerboxPopupMenuWillBecomeInvisible

        Datetext.setText(null);
        amttext.setText(null);
        discounttext.setText(null);
        vattext.setText(null);
        nettotaltexts.setText(null);
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);
        totalamounttext.setText(null);
        parchase_code();
        currentDate();
        paymemtypebox.setSelectedIndex(0);
        Bankinfobox.removeAllItems();
        accountbox.removeAllItems();
        chequenotext.setText(null);
        updatekey = 0;
        itemnamesearch.setEnabled(true);
        String Customername = (String) customerbox.getSelectedItem();
        try {
            String sql = "Select * from customerInfo  where customername='" + Customername + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {

                cusId = rs.getString("customerid");
                // customeridtext.setText(cusId);
                openingbalance = rs.getFloat("OpenigBalance");
                dipositamt = rs.getFloat("DipositAmt");

                creditAmnt = rs.getFloat("creditAmnt");
                paidamt = rs.getFloat("paidamount");
                balancedue1 = rs.getFloat("Balancedue");

                //  String trans = String.format("%s", balancedue1);
                ///  previseamonmttext.setText(trans);
                Paidableamt = rs.getFloat("Balancedue");
                CreditInvoice();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }


    }//GEN-LAST:event_customerboxPopupMenuWillBecomeInvisible

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (itemnamesearch.getSelectedIndex() > 0) {
            checkentry();
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_dataTableMouseClicked

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
        deleterow();
    }//GEN-LAST:event_dataTableKeyPressed
    private void saleDetailsDelete() {
        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String invoiceno = (String) dataTable.getValueAt(row, 1);
                String amount = (String) dataTable.getValueAt(row, 6);
                SaleUpdateDelete(amount, invoiceno);
            }
            //  loadGRN();
            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void SaleUpdateDelete(String totalrecied, String invno) {
        try {
            String sql = "Update sale set "
                    + "due='" + totalrecied + "',"
                    + "recieved=0 where invoiceNo='" + invno + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

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
        saleDetailsDelete();
        float paidamount = Float.parseFloat(totalamounttext.getText());
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
            float amount = Float.parseFloat(totalamounttext.getText());
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
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
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
                try {
                    String sql = "Delete from creditpaymentdetails where TranId=?";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, tranidtext.getText());
                    pst.execute();
                    // JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                try {
                    String sql = "Delete from CraditStatement where invoiceno=?";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, tranidtext.getText());
                    pst.execute();
                    // JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                creditpaymentdeleteDrwoerbank();
                JOptionPane.showMessageDialog(null, "Successfuly Data Deleted");
                AfterExecution();

            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void paybletextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paybletextKeyReleased


    }//GEN-LAST:event_paybletextKeyReleased

    private void paymenttextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paymenttextKeyReleased
        float payment = 0;
        float paybleamt = 0;
        float remaindue = 0;
        if (paymenttext.getText().isEmpty()) {
            payment = 0;
        } else {
            payment = Float.parseFloat(paymenttext.getText());
        }
        if (paybletext.getText().isEmpty()) {
            paybleamt = 0;
        } else {
            paybleamt = Float.parseFloat(paybletext.getText());
        }
        remaindue = (paybleamt - payment);
        if (remaindue >= 0) {
            duetext.setText(df2.format(remaindue));
        } else {
            duetext.setText(null);
        }


    }//GEN-LAST:event_paymenttextKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Bankinfobox;
    private javax.swing.JTextField Datetext;
    private javax.swing.JComboBox<String> accountbox;
    private javax.swing.JTextField amttext;
    private javax.swing.JTextField chequenotext;
    private javax.swing.JComboBox<String> customerbox;
    private javax.swing.JTable dataTable;
    private javax.swing.JTextField discounttext;
    private javax.swing.JTextField duetext;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField nettotaltexts;
    private com.toedter.calendar.JDateChooser openingdate;
    private javax.swing.JTextField paybletext;
    private javax.swing.JComboBox<String> paymemtypebox;
    private javax.swing.JTextField paymenttext;
    private javax.swing.JButton svbtn;
    private javax.swing.JTextField totalamounttext;
    private javax.swing.JTextField tranidtext;
    private javax.swing.JTextField vattext;
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
