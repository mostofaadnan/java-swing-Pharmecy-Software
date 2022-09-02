/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
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
public final class ImportExportPaymentRecieve extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    double balancedue;
   // Dashboard dashboard = new Dashboard();
    int userid = 0;
    int supid = 0;
    String countryid = null;
    float paidamtnprevoous = 0;
    private static final DecimalFormat df2 = new DecimalFormat("#.00");
    float bankbalance1;
    float accbalance;
    float totalb;
    String trnNo;
    float bankcashout = 0;
    float bankcashin = 0;
    float updatebankbalance = 0;
    int updatekey = 0;
    float importexportpaid = 0;
    float importexportdue = 0;

    /**
     * Creates new form ImportExportPaymentRecieve
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public ImportExportPaymentRecieve() throws SQLException, IOException {

        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        AutoCompleteDecorator.decorate(importexportnumbox);
        AutoCompleteDecorator.decorate(Bankinfobox);
        AutoCompleteDecorator.decorate(accountbox);

        currentDate();
        balance();
        // totalbalance();
        transactioncode();
        accsessModification();

    }

    public ImportExportPaymentRecieve(String table_click) throws SQLException, IOException {

        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        AutoCompleteDecorator.decorate(importexportnumbox);
        AutoCompleteDecorator.decorate(Bankinfobox);
        AutoCompleteDecorator.decorate(accountbox);

        currentDate();
        balance();
        // totalbalance();
        transactioncode();
        trnNo = table_click;
        viewData();
        accsessModification();
        paymentsbox.setEnabled(false);
        imprttypebox.setEnabled(false);
        importexportnumbox.setEnabled(false);
        paidamounttext.setEditable(false);
        balanceduetext.setEditable(false);
        paymemtypebox.setEnabled(false);
        Bankinfobox.setEnabled(false);
        accountbox.setEnabled(false);
        chequenotext.setEditable(false);        
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
    private void accsessModification() {
        try {
            String sql = "Select imexpedit,imexpdelete from modificationaccsess where userid='" + userid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                int imexpedit = rs.getInt("imexpedit");
                int imexpdelete = rs.getInt("imexpdelete");

                if (imexpedit == 1) {

                    svbtn.setEnabled(true);

                } else {

                    svbtn.setEnabled(false);

                }
                if (imexpdelete == 1) {

                    deletebtn.setEnabled(true);

                } else {

                    deletebtn.setEnabled(false);

                }

            } else {
                svbtn.setEnabled(false);
                deletebtn.setEnabled(false);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void viewData() throws SQLException {

        updatekey = 1;
        String sql = "Select TR_No,type,importexportcode,amount,paidamount,paymenttype,bank,accNo,chaqueNo,BalanceDue,inputdate,(Select supliyername from suplyierinfo spinfo where spinfo.id=ei.supliyer) as 'spname' from exportimportpaymentrecieve ei where ei.TR_No='" + trnNo + "'";
        pst = conn.prepareStatement(sql);
        rs = pst.executeQuery();
        if (rs.next()) {
            String trn = rs.getString("TR_No");
            transectiontext.setText(trn);
            String type = rs.getString("type");
            imprttypebox.setSelectedItem(type);
            String importcode = rs.getString("importexportcode");
            importexportnumbox.addItem(importcode);
            importexportnumbox.setSelectedItem(importcode);
            customeridtext.setText(rs.getString("spname"));
            previseamonmttext.setText(rs.getString("amount"));
            paidamounttext.setText(rs.getString("paidamount"));
            duetext.setText(rs.getString("BalanceDue"));

            String paymentype = rs.getString("paymenttype");

            paymemtypebox.setSelectedItem(paymentype);
            if (paymemtypebox.getSelectedIndex() > 0) {
                String bank = rs.getString("bank");
                Bankinfobox.addItem(bank);
                Bankinfobox.setSelectedItem(bank);

                String accNo = rs.getString("accNo");
                accountbox.addItem(accNo);
                accountbox.setSelectedItem(accNo);

                String chequeNo = rs.getString("chaqueNo");
                chequenotext.setText(chequeNo);
            }

        }
    }

    private void transactioncode() {
        try {
            int new_inv = 1;
            String NewParchaseCode = ("TRN-" + new_inv + "");
            String sql = "Select id from exportimportpaymentrecieve";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = ("TRN-" + ParchaseCode + "");
            }

            transectiontext.setText(NewParchaseCode);
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
        } catch (Exception e) {
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

    private void cashInDrwaercashoutcashin(float cashin, float cashout, String reaon, float balance) {
        // float balance = bankbalance1 - cashout;
        try {

            String sql = "Insert into CashDrower(cashin,cashout,balance,type,upatedate,tranid) values(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setFloat(1, cashin);
            pst.setFloat(2, cashout);
            pst.setFloat(3, balance);
            pst.setString(4, reaon);
            pst.setString(5, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
            pst.setString(6, transectiontext.getText());
            pst.execute();

            ///  JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void balanceupdatecashout(String accountno, float presentbalnce) {
        try {
            String sql = "Update BankAccount set cashout='" + presentbalnce + "' where accountno='" + accountno + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void balanceupdatecashin(String accountno, float presentbalnce) {
        try {
            String sql = "Update BankAccount set cashin='" + presentbalnce + "' where accountno='" + accountno + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void overallinsertcashout(float presentbalnce, float cashin, float cashout) {

        //float cashin = 0;
        //float cashout = Float.parseFloat(paidamounttext.getText());
        try {
            String reason = (String) transectiontext.getText();
            String sql = "Insert into bankoverall(inputdate,Description,bank,AccountNo,cashin,cashout,Balance,prasentbalance,totalbalance,remark,transactionid) values(?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());

            pst.setString(2, "Export/import Payment," + reason);
            pst.setString(3, (String) Bankinfobox.getSelectedItem());
            pst.setString(4, (String) accountbox.getSelectedItem());
            pst.setFloat(5, cashin);
            pst.setFloat(6, cashout);
            pst.setFloat(7, accbalance);
            pst.setFloat(8, presentbalnce);
            pst.setFloat(9, totalb);

            pst.setString(10, "Import Payment," + "Payment Type:" + paymentsbox.getSelectedItem() + ",Trasnsaction:" + reason + " Import No: " + importexportnumbox.getSelectedItem());
            pst.setString(11, (String) transectiontext.getText());

            pst.execute();

            // JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        java.sql.Date date = new java.sql.Date(now.getTime());
        openingdate.setDate(date);

    }

    private void clearfix() {
        transactioncode();
        paymentsbox.setSelectedIndex(0);
        imprttypebox.setSelectedIndex(0);
        importexportnumbox.setSelectedIndex(0);
        customeridtext.setText(null);
        previseamonmttext.setText(null);
        duetext.setText(null);
        paidamounttext.setText(null);
        balanceduetext.setText(null);
        paymemtypebox.setSelectedIndex(0);
        if (paymemtypebox.getSelectedIndex() == 1) {
            Bankinfobox.setSelectedIndex(0);
            accountbox.setSelectedIndex(0);
            chequenotext.setText(null);

        }

        remarktext.setText(null);
        updatekey = 0;
    }

    private void addStock() throws SQLException, JRException {

        try {

            String sql = "Insert into exportimportpaymentrecieve(TR_No,type,importexportcode,supliyer,amount,paidamount,paymenttype,bank,accNo,chaqueNo,BalanceDue,inputdate,inputuser) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, transectiontext.getText());
            pst.setString(2, (String) imprttypebox.getSelectedItem());
            pst.setString(3, (String) importexportnumbox.getSelectedItem());
            pst.setInt(4, supid);
            pst.setString(5, previseamonmttext.getText());
            pst.setString(6, paidamounttext.getText());
            pst.setString(7, (String) paymemtypebox.getSelectedItem());
            if (paymemtypebox.getSelectedIndex() == 1) {
                if (Bankinfobox.getSelectedIndex() > 0 || accountbox.getSelectedIndex() > 0 || chequenotext.getText().isEmpty()) {

                    pst.setString(8, (String) Bankinfobox.getSelectedItem());
                    pst.setString(9, (String) accountbox.getSelectedItem());
                    pst.setString(10, chequenotext.getText());
                } else {

                    JOptionPane.showMessageDialog(null, "Please Complete Bank payment Information");
                }

            } else {

                pst.setString(8, " ");
                pst.setString(9, " ");
                pst.setString(10, " ");

            }
            pst.setString(11, balanceduetext.getText());
            pst.setString(12, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
            pst.setInt(13, userid);
            pst.execute();

            /// amountinsert();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void printInvoice() throws JRException {
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Payment Voucher", "GRN Print", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            try {
                      double amount = Double.parseDouble(paidamounttext.getText());
                       String amounts=df2.format(amount);
                       String inwords = convertToIndianCurrency(amounts);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/exportimportpaymentrecievevoucher.jrxml");
                //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

                HashMap para = new HashMap();
                para.put("transactionno", transectiontext.getText());
                if (imprttypebox.getSelectedIndex() == 1) {

                    para.put("processtype", "Import Amount");
                    para.put("paymentorrecieve", "Payment To");
                } else {
                    para.put("processtype", "Export Amount");
                    para.put("paymentorrecieve", "Recieve From");
                }
                para.put("inword",inwords);    
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {
                JOptionPane.showMessageDialog(null, ex);

            }
        }

    }

    private void suppliyer_import_balance_insert() {

        try {

            String sql = "Insert into suppliyer_import_export_balance(exportimportcode,supid,countryid,importAmount,exportAmount,payment,recieve,paymentdue,reciebedue,inputdate,inputuser,trnid) values (?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) importexportnumbox.getSelectedItem());
            pst.setInt(2, supid);
            pst.setString(3, countryid);
            pst.setDouble(4, 0);
            pst.setDouble(5, 0);
            pst.setString(6, paidamounttext.getText());
            pst.setDouble(7, 0);
            pst.setString(8, balanceduetext.getText());
            pst.setDouble(9, 0);
            pst.setString(10, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
            pst.setInt(11, userid);
            pst.setString(12, transectiontext.getText());
            pst.execute();
            //  JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void suppliyer_export_balance_insert() {

        try {

            String sql = "Insert into suppliyer_import_export_balance(exportimportcode,supid,countryid,importAmount,exportAmount,payment,recieve,paymentdue,reciebedue,inputdate,inputuser,trnid) values (?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, (String) importexportnumbox.getSelectedItem());
            pst.setInt(2, supid);
            pst.setString(3, countryid);
            pst.setDouble(4, 0);
            pst.setDouble(5, 0);
            pst.setDouble(6, 0);
            pst.setString(7, paidamounttext.getText());
            pst.setString(8, balanceduetext.getText());
            pst.setDouble(9, 0);
            pst.setString(10, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
            pst.setInt(11, userid);
            pst.setString(12, transectiontext.getText());
            pst.execute();
            //  JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void exportUpdate() {
        try {

            String pstatus;
            float due = Float.parseFloat(balanceduetext.getText());

            if (due > 0) {
                pstatus = "Due Remain";
            } else {
                pstatus = "Completed";

            }
            float prasentpaidmnt = Float.parseFloat(paidamounttext.getText());
            float finelpaidamnt = prasentpaidmnt + paidamtnprevoous;
            String parchasecode = (String) importexportnumbox.getSelectedItem();

            String sql = "Update export set recieve='" + finelpaidamnt + "', paymentstatus='" + pstatus + "',recievedue='" + balanceduetext.getText() + "'  where exportCode='" + parchasecode + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();

            // JOptionPane.showMessageDialog(null, "Execute Successfullyhghh");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void importUpdate() {
        try {

            String pstatus;
            float due = Float.parseFloat(balanceduetext.getText());

            if (due > 0) {
                pstatus = "Due Remain";
            } else {
                pstatus = "Completed";

            }
            float prasentpaidmnt = Float.parseFloat(paidamounttext.getText());
            float finelpaidamnt = prasentpaidmnt + paidamtnprevoous;
            String parchasecode = (String) importexportnumbox.getSelectedItem();

            String sql = "Update import set payment='" + finelpaidamnt + "', paymentstatus='" + pstatus + "',paymentDue='" + balanceduetext.getText() + "'  where importCode='" + parchasecode + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Execute Successfullyhghh");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void amountinsert() throws SQLException, JRException {

        if (imprttypebox.getSelectedIndex() == 1) {
            suppliyer_import_balance_insert();
            importUpdate();

            float amount = Float.parseFloat(paidamounttext.getText());
            if (paymemtypebox.getSelectedIndex() == 0) {

                //Cash Drawer cashout 
                if (amount > bankbalance1) {
                    JOptionPane.showMessageDialog(null, "Not Save!! Lack Of Cash Balance On Your Cash Box");

                } else {
                    float balance = bankbalance1 - amount;
                    String reaon = "Import Payment";
                    float cashin = 0;
                    addStock();
                    cashInDrwaercashoutcashin(cashin, amount, reaon, balance);
                    JOptionPane.showMessageDialog(null, "Data Saved");
                    printInvoice();
                    clearfix();
                }

            } else //bank cashout
            {
                if (accbalance >= amount) {
                    String accountno = (String) accountbox.getSelectedItem();
                    float presentbalnce = bankcashout + amount;
                    float cashin = 0;
                    float cashout = amount;
                    addStock();
                    balanceupdatecashout(accountno, presentbalnce);
                    balanceamount(accountno);
                    totalbalance();

                    overallinsertcashout(updatebankbalance, cashin, cashout);
                    JOptionPane.showMessageDialog(null, "Data Saved");
                    printInvoice();
                    clearfix();
                } else {

                    JOptionPane.showMessageDialog(null, "Fail To save, Amount Value More than preasent Value in this account");

                }
            }

        } else {
            addStock();
            suppliyer_export_balance_insert();
            exportUpdate();
            float chashin = Float.parseFloat(paidamounttext.getText());
            //  String reaon="Export Payment Recive";
            if (paymemtypebox.getSelectedIndex() == 0) {
                //cashInDrwaercashin(chashin,reaon);
                float balance = bankbalance1 + chashin;
                String reaon = "Export Payment Recieve";
                float cashout = 0;
                cashInDrwaercashoutcashin(chashin, cashout, reaon, balance);

            } else {

                float amount = Float.parseFloat(paidamounttext.getText());
                String accountno = (String) accountbox.getSelectedItem();
                float presentbalnce = bankcashin + amount;
                balanceupdatecashin(accountno, presentbalnce);

                balanceamount(accountno);
                totalbalance();
                float cashin = amount;
                float cashout = 0;
                overallinsertcashout(updatebankbalance, cashin, cashout);

            }
            JOptionPane.showMessageDialog(null, "Data Saved");
            printInvoice();
            clearfix();
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

    private void selectimportExport() {
        String importexportno = (String) importexportnumbox.getSelectedItem();

        if (imprttypebox.getSelectedIndex() == 1) {

            try {
                String sql = "Select payment,paymentDue from import where importCode='" + importexportno + "'";
                //  String sql = "Select gr.purchaseCode as 'purchaseNo', (select pr.supliyer from purchase pr where pr.purchaseCode=gr.purchaseCode) as 'supl', (select pr.nettotal from purchase pr where pr.purchaseCode=gr.purchaseCode) as 'Totalamt' ,gr.due as 'due' from GRNinfo gr where gr.GRNCode='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                //  pst.setString(1, SearchText);

                rs = pst.executeQuery();
                if (rs.next()) {

                    // supid = rs.getInt("supid");
                    importexportpaid = rs.getFloat("payment");

                    importexportdue = rs.getFloat("paymentDue");

                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        } else {

            try {
                String sql = "Select recieve,recievedue from export where exportCode='" + importexportno + "'";
                //  String sql = "Select gr.purchaseCode as 'purchaseNo', (select pr.supliyer from purchase pr where pr.purchaseCode=gr.purchaseCode) as 'supl', (select pr.nettotal from purchase pr where pr.purchaseCode=gr.purchaseCode) as 'Totalamt' ,gr.due as 'due' from GRNinfo gr where gr.GRNCode='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                //  pst.setString(1, SearchText);

                rs = pst.executeQuery();
                if (rs.next()) {

                    // supid = rs.getInt("supid");
                    importexportpaid = rs.getFloat("recieve");

                    importexportdue = rs.getFloat("recievedue");

                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void ExportImportCashBankDelete() {

        String pstatus;
        selectimportExport();
        float deleteamount = Float.parseFloat(paidamounttext.getText());
        float grndeletepaid = importexportpaid - deleteamount;
        float grnduedelet = importexportdue + deleteamount;
        if (grndeletepaid == 0) {
            pstatus = "Pending";

        } else if (grnduedelet > 0) {
            pstatus = "Due Remain";
        } else {
            pstatus = "Completed";

        }

        String importexportno = (String) importexportnumbox.getSelectedItem();
        if (imprttypebox.getSelectedIndex() == 1) {
            try {
                String sql = "Update import set payment='" + grndeletepaid + "',paymentDue='" + grnduedelet + "',paymentstatus='" + pstatus + "'  where importCode='" + importexportno + "'";
                pst = conn.prepareStatement(sql);

                pst.execute();
                //   JOptionPane.showMessageDialog(null, "Execute Successfully");

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

            String reaon = "Import Payment";

            if (paymemtypebox.getSelectedIndex() == 0) {

                try {
                    String sql = "Delete from CashDrower where type='" + reaon + "' AND tranid=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, transectiontext.getText());

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

                    pst.setString(1, transectiontext.getText());

                    pst.execute();
                    //JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                Selectbankaccount();
                float amount = Float.parseFloat(paidamounttext.getText());
                float cashoutminus = bankcashout - amount;
                String accountno = (String) accountbox.getSelectedItem();
                String bank = (String) Bankinfobox.getSelectedItem();

                try {
                    String sql = "Update BankAccount set cashout='" + cashoutminus + "' where accountno='" + accountno + "' AND bank='" + bank + "'";
                    pst = conn.prepareStatement(sql);

                    pst.execute();
                    // JOptionPane.showMessageDialog(null, "Data Update");
                    //   clear();
                    // totalbalance();
                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
            }

        } else {
            //export

            try {
                String sql = "Update export set recieve='" + grndeletepaid + "',recievedue='" + grnduedelet + "',paymentstatus='" + pstatus + "'  where exportCode='" + importexportno + "'";
                pst = conn.prepareStatement(sql);

                pst.execute();
                //   JOptionPane.showMessageDialog(null, "Execute Successfully");

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

            String reaon = "Export Payment Recieve";

            if (paymemtypebox.getSelectedIndex() == 0) {

                try {
                    String sql = "Delete from CashDrower where type='" + reaon + "' AND tranid=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, transectiontext.getText());

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

                    pst.setString(1, transectiontext.getText());

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
        openingdate = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        importexportnumbox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        paymemtypebox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        customeridtext = new javax.swing.JTextField();
        paidamounttext = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        balanceduetext = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        chequenotext = new javax.swing.JTextField();
        Bankinfobox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        accountbox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        previseamonmttext = new javax.swing.JTextField();
        jscro = new javax.swing.JScrollPane();
        remarktext = new javax.swing.JTextArea();
        svbtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        transectiontext = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        imprttypebox = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        paymentsbox = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        duetext = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Import Export Payment Recieve");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 204, 0)));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 0));
        jLabel2.setText("Payment Date");

        openingdate.setForeground(new java.awt.Color(102, 102, 102));
        openingdate.setDateFormatString("yyyy-MM-dd");

        jLabel1.setText("Import/Export No");

        importexportnumbox.setEditable(true);
        importexportnumbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        importexportnumbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                importexportnumboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

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

        jLabel7.setText("Supplier ");

        jLabel8.setText("Paid Amount");

        customeridtext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        customeridtext.setEnabled(false);

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

        jLabel9.setText("Balance Due");

        balanceduetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        balanceduetext.setForeground(new java.awt.Color(153, 0, 0));
        balanceduetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

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
                    .addComponent(Bankinfobox, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accountbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chequenotext))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel4))
                    .addComponent(Bankinfobox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jLabel10.setText("Total Amount");

        previseamonmttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        previseamonmttext.setForeground(new java.awt.Color(204, 0, 0));
        previseamonmttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        previseamonmttext.setDisabledTextColor(new java.awt.Color(153, 0, 0));
        previseamonmttext.setEnabled(false);

        remarktext.setColumns(20);
        remarktext.setRows(5);
        jscro.setViewportView(remarktext);

        svbtn.setBackground(new java.awt.Color(204, 0, 0));
        svbtn.setForeground(new java.awt.Color(255, 255, 255));
        svbtn.setText("Execute");
        svbtn.setBorder(null);
        svbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svbtnActionPerformed(evt);
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

        deletebtn.setBackground(new java.awt.Color(102, 0, 0));
        deletebtn.setForeground(new java.awt.Color(255, 255, 255));
        deletebtn.setText("Delete");
        deletebtn.setBorder(null);
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        jLabel11.setText("Remark");

        jLabel12.setText("TRN ID");

        transectiontext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        transectiontext.setEnabled(false);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Type");

        imprttypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Import", "Export" }));
        imprttypebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                imprttypeboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 51, 0));
        jLabel14.setText("Payment Status");

        paymentsbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        paymentsbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Pending", "Due Remain" }));

        jLabel15.setText("Due Ammount");

        duetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        duetext.setForeground(new java.awt.Color(204, 0, 0));
        duetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        duetext.setDisabledTextColor(new java.awt.Color(153, 0, 0));
        duetext.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(34, 34, 34)
                                        .addComponent(jscro))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                            .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(47, 47, 47)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(balanceduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(previseamonmttext)
                                                    .addComponent(paidamounttext, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                                                    .addComponent(duetext)
                                                    .addComponent(customeridtext)))
                                            .addComponent(paymemtypebox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(importexportnumbox, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel13)
                                                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel12))
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(imprttypebox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(openingdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(transectiontext)
                                            .addComponent(paymentsbox, 0, 336, Short.MAX_VALUE))))))))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paymentsbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(imprttypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(openingdate, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(transectiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addComponent(importexportnumbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customeridtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(previseamonmttext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(duetext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paidamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(balanceduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(paymemtypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(1, 1, 1)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jscro, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void importexportnumboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_importexportnumboxPopupMenuWillBecomeInvisible
        if (importexportnumbox.getSelectedIndex() > 0) {
            if (imprttypebox.getSelectedIndex() == 1) {

                try {
                    String sql = "Select Totalrate,supliyer,countryid,payment,(select sp.supliyername from suplyierinfo sp where sp.id=im.supliyer) as 'supliyername',paymentDue from import im where im.importCode='" + importexportnumbox.getSelectedItem() + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

                    if (rs.next()) {

                        double TotalRatecode = rs.getDouble("Totalrate");
                        String TotalRatecodes = String.format("%.2f", TotalRatecode);
                        previseamonmttext.setText(TotalRatecodes);

                        double paymentDue = rs.getDouble("paymentDue");
                        String paymentDues = String.format("%.2f", paymentDue);
                        duetext.setText(paymentDues);

                        String supliyername = rs.getString("supliyername");
                        customeridtext.setText(supliyername);
                        supid = rs.getInt("supliyer");
                        countryid = rs.getString("countryid");
                        paidamtnprevoous = rs.getFloat("payment");
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            } else {
                try {
                    String sql = "Select Totalrate,supliyer,recieve,countryid,(select sp.supliyername from suplyierinfo sp where sp.id=im.supliyer) as 'supliyername',recievedue from export im where im.exportCode='" + importexportnumbox.getSelectedItem() + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

                    if (rs.next()) {

                        double TotalRatecode = rs.getDouble("Totalrate");
                        String TotalRatecodes = String.format("%.2f", TotalRatecode);
                        previseamonmttext.setText(TotalRatecodes);

                        double paymentDue = rs.getDouble("recievedue");
                        String paymentDues = String.format("%.2f", paymentDue);
                        duetext.setText(paymentDues);

                        String supliyername = rs.getString("supliyername");
                        customeridtext.setText(supliyername);
                        supid = rs.getInt("supliyer");
                        countryid = rs.getString("countryid");
                        paidamtnprevoous = rs.getFloat("recieve");

                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }

        }
    }//GEN-LAST:event_importexportnumboxPopupMenuWillBecomeInvisible

    private void paymemtypeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_paymemtypeboxPopupMenuWillBecomeInvisible
        Bankinfobox.removeAllItems();
        Bankinfobox.addItem("Select");
        Bankinfobox.setSelectedItem("Select");
        accountbox.removeAllItems();
        accountbox.addItem("Select");
        accountbox.setSelectedItem("Select");
        chequenotext.setText(null);

        if (paymemtypebox.getSelectedIndex() == 1) {
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
    }//GEN-LAST:event_paymemtypeboxPopupMenuWillBecomeInvisible

    private void paidamounttextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {
        } else {

            float paidamthere = Float.parseFloat(paidamounttext.getText());
            float amount = Float.parseFloat(previseamonmttext.getText());

            float finelpaidamnt = paidamtnprevoous + paidamthere;

            if (finelpaidamnt > amount) {

                balanceduetext.setText(null);

            } else {
                float balanceDue = amount - finelpaidamnt;
                String Balancdus = String.format("%.2f", balanceDue);
                balanceduetext.setText(Balancdus);

            }

        }

    }//GEN-LAST:event_paidamounttextKeyPressed

    private void paidamounttextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_paidamounttextKeyReleased

    private void paidamounttextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttextKeyTyped
        //duetext.setText(null);
    }//GEN-LAST:event_paidamounttextKeyTyped

    private void chequenotextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chequenotextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chequenotextActionPerformed

    private void BankinfoboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_BankinfoboxPopupMenuWillBecomeInvisible
        accountbox.removeAllItems();
        accountbox.addItem("Select");
        accountbox.setSelectedItem("Select");
        chequenotext.setText(null);
        if (Bankinfobox.getSelectedIndex() > 0) {
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

        }

    }//GEN-LAST:event_BankinfoboxPopupMenuWillBecomeInvisible

    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed
        if (updatekey == 0) {
            currentDate();
            try {
                balance();
            } catch (SQLException ex) {
                Logger.getLogger(ImportExportPaymentRecieve.class.getName()).log(Level.SEVERE, null, ex);
            }
            totalbalance();
            transactioncode();
            if (paymentsbox.getSelectedIndex() < 0 || imprttypebox.getSelectedIndex() < 0 || importexportnumbox.getSelectedIndex() < 0 || paidamounttext.getText().isEmpty() || balanceduetext.getText().isEmpty()) {

            } else {

                try {
                    amountinsert();
                } catch (SQLException | JRException ex) {
                    Logger.getLogger(ImportExportPaymentRecieve.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }//GEN-LAST:event_svbtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

    }//GEN-LAST:event_jButton2ActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
       if(updatekey==1){
        
       try {
            String sql = "Delete from suppliyer_import_export_balance where trnid=?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, transectiontext.getText());

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Deleted");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        ExportImportCashBankDelete();
        try {
            String sql = "Delete from exportimportpaymentrecieve where TR_No=?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, transectiontext.getText());

            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Deleted");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

       }
       clearfix();
    }//GEN-LAST:event_deletebtnActionPerformed

    private void imprttypeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_imprttypeboxPopupMenuWillBecomeInvisible
        importexportnumbox.removeAllItems();
        importexportnumbox.addItem("Select");
        importexportnumbox.setSelectedItem("Select");
        if (imprttypebox.getSelectedIndex() > 0 || paymentsbox.getSelectedIndex() > 0) {

            if (imprttypebox.getSelectedIndex() == 1) {

                try {
                    String sql = "Select importCode from import where paymentstatus='" + paymentsbox.getSelectedItem() + "' AND GRNstatus=1";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {
                        String name = rs.getString("importCode");

                        importexportnumbox.addItem(name);
                        //  supliyerbox.setSelectedIndex(id);

                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            } else {
                try {
                    String sql = "Select exportCode from export where paymentstatus='" + paymentsbox.getSelectedItem() + "' AND GRNstatus=1";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {
                        String name = rs.getString("exportCode");

                        importexportnumbox.addItem(name);
                        //  supliyerbox.setSelectedIndex(id);

                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }

        }
    }//GEN-LAST:event_imprttypeboxPopupMenuWillBecomeInvisible

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Bankinfobox;
    private javax.swing.JComboBox<String> accountbox;
    private javax.swing.JTextField balanceduetext;
    private javax.swing.JTextField chequenotext;
    private javax.swing.JTextField customeridtext;
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField duetext;
    private javax.swing.JComboBox<String> importexportnumbox;
    private javax.swing.JComboBox<String> imprttypebox;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jscro;
    private com.toedter.calendar.JDateChooser openingdate;
    private javax.swing.JTextField paidamounttext;
    private javax.swing.JComboBox<String> paymemtypebox;
    private javax.swing.JComboBox<String> paymentsbox;
    private javax.swing.JTextField previseamonmttext;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JButton svbtn;
    private javax.swing.JTextField transectiontext;
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
        return Rupees + paise + " " + "AED Only";
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
