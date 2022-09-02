/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.view.JasperViewer;


/**
 *
 * @author adnan
 */
public final class Purchasepayment extends javax.swing.JInternalFrame {

    float bankbalance1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String suplyer = null;
    float openingbalance;
    float consighnmentamnt;
    float Balancedue;
    float paidamount;
    int supid = 0;

    float totalb = 0;
    float accbalance = 0;
    int bankid = 0;
  //  Dashboard dashboard = new Dashboard();
    int userid =0;
    float paidamtnprevoous = 0;
    float bankcashout = 0;
    float bankcashin = 0;
    float updatebankbalance = 0;

    //delete info
    float grnpaid = 0;
    float grndue = 0;

    /**
     * Creates new form purchasepayment
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public Purchasepayment() throws SQLException, IOException {
        initComponents();

        conn = Java_Connect.conectrDB();
        userkey();
        currentDate();
        // GRN();
        AutoCompleteDecorator.decorate(parchaseBox);
        //  AutoCompleteDecorator.decorate(Bankinfobox);
        transactioncode();
        balance();
        accsessModification();
        // totalbalance();
        //  bankaccount();
    }

    public Purchasepayment(String table_Click) throws SQLException, IOException {

        initComponents();

        conn = Java_Connect.conectrDB();
        userkey();
        currentDate();
        // GRN();
        AutoCompleteDecorator.decorate(parchaseBox);
        //  AutoCompleteDecorator.decorate(Bankinfobox);
        transactioncode();
        balance();
        tableclick(table_Click);
        paymentsbox.setEnabled(false);
        parchaseBox.setEnabled(false);
        paidamounttext.setEditable(false);
        accsessModification();

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
            String sql = "Select prcpedit,purcpdelete from modificationaccsess where userid='" + userid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                int prcpedit = rs.getInt("prcpedit");
                int purcpdelete = rs.getInt("purcpdelete");

                if (prcpedit == 1) {

                    svbtn.setEnabled(true);

                } else {

                    svbtn.setEnabled(false);

                }
                if (purcpdelete == 1) {

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

    private void tableclick(String table_click) {
        paymenttypebox.setEnabled(false);
        paymentsbox.setEnabled(false);
        parchaseBox.setEnabled(false);
        parchaseBox.removeAllItems();
        parchaseBox.addItem("Select");

        try {

            String sql = "Select pp.id as 'id', pp.transactionNo  as 'Transaction No',pp.paymenttype as 'paymenttype',(select Bankname from bankinfo bn where pp.bankid=bn.id) as 'bank',pp.chequeNo  as 'chaqueno',pp.accNo as 'accno', pp.parchseNo as 'Purchase No', pp.Grnno as 'GRN No', pp.paymentdate as 'Payment Date',(select sup.supliyername from suplyierinfo sup where par.supliyer=sup.id) as 'Suppliyer', (select pas.nettotal from purchase pas where pas.purchaseCode= pp.parchseNo) as 'Total Amount' ,pp.paymenttype as 'Payment Type', pp.paidamount as 'Paid Amount', pp.balancedue as 'Balance Due' , pp.remark as 'remark' from purchase par inner join parchasePayment pp ON par.purchaseCode=pp.parchseNo where pp.transactionNo='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String id = rs.getString("id");
                ppayemtnid.setText(id);

                String tran = rs.getString("Transaction No");
                transectiontext.setText(tran);

                String puchaseno = rs.getString("Purchase No");
                grntext.setText(puchaseno);

                String grnno = rs.getString("GRN No");

                parchaseBox.addItem(grnno);
                parchaseBox.setSelectedItem(grnno);

                Date pdate = rs.getDate("Payment Date");
                openingdate.setDate(pdate);

                String suppliyer = rs.getString("Suppliyer");
                suppliyetext.setText(suppliyer);

                float totalAmt = rs.getFloat("Total Amount");
                String totalamts = String.format("%.2f", totalAmt);
                totalmttext.setText(totalamts);

                float paodamt = rs.getFloat("Paid Amount");
                String paodamts = String.format("%.2f", paodamt);
                paidamounttext.setText(paodamts);

                float balanced = rs.getFloat("Balance Due");
                String balances = String.format("%.2f", balanced);
                balanceduetext.setText(balances);

                String remark = rs.getString("remark");
                remarktext.setText(remark);
                String paymenttype = rs.getString("paymenttype");
                paymenttypebox.setSelectedItem(paymenttype);

                if ("Bank".equals(paymenttype)) {

                    String chaqueno = rs.getString("chaqueno");
                    chequenotext.setText(chaqueno);

                    String bank = rs.getString("bank");
                    Bankinfobox.addItem(bank);

                    String accno = rs.getString("accno");
                    accountbox.addItem(accno);
                }

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

    private void suplyerinfo() {
        try {

            String sql = "Select  * from suplyierInfo where id='" + supid + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            if (rs.next()) {

                openingbalance = rs.getFloat("OpenigBalance");
                consighnmentamnt = rs.getFloat("consighnmentamnt");
                Balancedue = rs.getFloat("Balancedue");
                paidamount = rs.getFloat("paidamount");

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
private static final DecimalFormat df2 = new DecimalFormat("#.00");
    private void supliyerupdate() {
        try {
            float presentpaid = Float.parseFloat(paidamounttext.getText());
            float totalpaid = presentpaid + paidamount;
            String totalpaidformat=df2.format(totalpaid);
            float afterblncedue = Balancedue - presentpaid;
            String afterblnceduefromat=df2.format(afterblncedue);

            String sql = "Update suplyierInfo set paidamount='" + totalpaidformat + "',Balancedue='" + afterblnceduefromat + "' where id='" + supid + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");

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

    private void transactioncode() {
        try {
            int new_inv = 1;
            String NewParchaseCode = ("TN-" + new_inv + "");
            String sql = "Select id from parchasepayment";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = ("TN-" + ParchaseCode + "");
            }

            transectiontext.setText(NewParchaseCode);
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        java.sql.Date date = new java.sql.Date(now.getTime());
        openingdate.setDate(date);

    }

    private void GRN() throws SQLException {

        try {
            String sql = "Select GRNCode from grninfo where  payment='Due Remain'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("GRNCode");

                parchaseBox.addItem(name);
                //  supliyerbox.setSelectedIndex(id);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void addStock() throws SQLException {
        float amount=0;
        if(paymentsbox.getSelectedItem()=="Pending"){
        amount = Float.parseFloat(totalmttext.getText());
       
        }else{
        
         amount = Float.parseFloat(duetext.getText());
        }
     
        float paidamt = Float.parseFloat(paidamounttext.getText());
        float balancedue = amount - paidamt;
        // String reason = (String) parchaseBox.getSelectedItem();

        try {

            String sql = "Insert into parchasepayment(transactionNo,parchseNo,Grnno,payamount,paidamount,balancedue,paymentdate,paymenttype,bankid,accNo,chequeNo,payment,remark,inputuser,supid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setString(1, transectiontext.getText());

            pst.setString(2, grntext.getText());
            pst.setString(3, (String) parchaseBox.getSelectedItem());
            pst.setFloat(4,amount);
            pst.setFloat(5, Float.parseFloat(paidamounttext.getText()));
            pst.setFloat(6, balancedue);

            pst.setString(7, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
            pst.setString(8, (String) paymenttypebox.getSelectedItem());

            if (paymenttypebox.getSelectedIndex() == 1) {
                if (Bankinfobox.getSelectedIndex() > 0 || accountbox.getSelectedIndex() > 0 || chequenotext.getText().isEmpty()) {

                    pst.setInt(9, bankid);
                    pst.setString(10, (String) accountbox.getSelectedItem());
                    pst.setString(11, chequenotext.getText());
                    pst.setInt(12, 1);

                } else {

                    JOptionPane.showMessageDialog(null, "Please Complete Bank payment Information");

                }

            } else {

                pst.setInt(9, 0);
                pst.setString(10, " ");
                pst.setString(11, " ");
                pst.setInt(12, 0);

            }

            pst.setString(13, remarktext.getText());
            pst.setInt(14, userid);
            pst.setInt(15,supid);
            pst.execute();
            parchaseUpdate();
            suplyerinfo();
            supliyerupdate();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void cashInDrwaer() {
        //float cashout=paidamounttext.
        float cashout = Float.parseFloat(paidamounttext.getText());
        String reaon = (String) parchaseBox.getSelectedItem();
        float balance = bankbalance1 - cashout;
        try {

            String sql = "Insert into CashDrower(cashin,cashout,balance,type,upatedate,tranid) values(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setFloat(1, 0);
            pst.setFloat(2, cashout);
            pst.setFloat(3, balance);
            pst.setString(4, "Purchase Payment," + reaon);
            pst.setString(5, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
            pst.setString(6, transectiontext.getText());
            pst.execute();

            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
 
    private void printInvoice() throws JRException {
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Payment Voucher", "Voucher Print", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            try {
                 double amount = Double.parseDouble(paidamounttext.getText());
                       String amounts=df2.format(amount);
                String inwords = convertToIndianCurrency(amounts);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/purchasePaymentReceipt.jrxml");
                //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

                HashMap para = new HashMap();
                para.put("transactionno", transectiontext.getText());
                if (paymenttypebox.getSelectedIndex() == 1) {
                    para.put("bankid", Bankinfobox.getSelectedItem());
                } else {
                    para.put("bankid", " ");

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

    private void cashdrowerupdate() {
        try {

            String id = transectiontext.getText();

            String sql = "Update CashDrower set cashout='" + paidamounttext.getText() + "'  where tranid='" + id + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void parchaseUpdate() {
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
            String parchasecode = (String) parchaseBox.getSelectedItem();

            String sql = "Update GRNinfo set paidamount='" + finelpaidamnt + "', payment='" + pstatus + "',TransactionNo='" + transectiontext.getText() + "',due='" + balanceduetext.getText() + "'  where GRNCode='" + parchasecode + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
          //  JOptionPane.showMessageDialog(null, "Execute Successfully");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void parchaseUpdateDelete() {
        try {
            String pstatus;
            selectgrn();
            float deleteamount = Float.parseFloat(paidamounttext.getText());
            float grndeletepaid = grnpaid - deleteamount;
            float grnduedelet = grndue + deleteamount;
            if (grndeletepaid == 0) {
                pstatus = "Pending";

            } else if (grnduedelet > 0) {
                pstatus = "Due Remain";
            } else {
                pstatus = "Completed";

            }

            String parchasecode = (String) parchaseBox.getSelectedItem();

            String sql = "Update GRNinfo set paidamount='" + grndeletepaid + "',due='" + grnduedelet + "',payment='" + pstatus + "'  where GRNCode='" + parchasecode + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //   JOptionPane.showMessageDialog(null, "Execute Successfully");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        try {
            float presentpaid = Float.parseFloat(paidamounttext.getText());
            float totalpaid = paidamount - presentpaid;
            float afterblncedue = Balancedue + presentpaid;

            String sql = "Update suplyierInfo set paidamount='" + totalpaid + "',Balancedue='" + afterblncedue + "' where id='" + supid + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        String reaon = "Purchase Payment," + (String) parchaseBox.getSelectedItem();
        if (paymenttypebox.getSelectedIndex() == 0) {

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
            float amount=Float.parseFloat(paidamounttext.getText());
            float cashoutminus=bankcashout-amount;
            String accountno=(String) accountbox.getSelectedItem();
            String bank=(String) Bankinfobox.getSelectedItem();
            
   try {
            String sql = "Update BankAccount set cashout='" + cashoutminus + "' where accountno='" + accountno + "' AND bank='"+bank+"'";
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

    private void payentUpdate() {
        try {
            selectgrn();
            float amount = Float.parseFloat(totalmttext.getText());
            float paidamt = Float.parseFloat(paidamounttext.getText());
            float balancedue = amount - paidamt;

            String id = ppayemtnid.getText();

            String sql = "Update parchasepayment set paidamount='" + paidamounttext.getText() + "',balancedue='" + balancedue + "',paymenttype='" + paymenttypebox.getSelectedItem() + "' ,remark='" + remarktext.getText() + "', paymentdate='" + ((JTextField) openingdate.getDateEditor().getUiComponent()).getText() + "' where id='" + id + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            JOptionPane.showMessageDialog(null, "Execute Successfully");

            clear();
            ppayemtnid.setText(null);
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void clear() throws SQLException {
        transactioncode();
        currentDate();
        balance();
        totalbalance();
        parchaseBox.removeAllItems();
        parchaseBox.addItem("Select");
        GRN();
        parchaseBox.setSelectedIndex(0);
        grntext.setText(null);
        suppliyetext.setText(null);
        totalmttext.setText(null);
        paymenttypebox.setSelectedIndex(0);
        paidamounttext.setText(null);
        balanceduetext.setText(null);
        remarktext.setText(null);
        Bankinfobox.removeAllItems();
        accountbox.removeAllItems();
        chequenotext.setText(null);
    }

    //bank section 
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
        float cashout = Float.parseFloat(paidamounttext.getText());
        try {
            String reason = transectiontext.getText();
            String sql = "Insert into bankoverall(inputdate,Description,bank,AccountNo,cashin,cashout,Balance,prasentbalance,totalbalance,remark,transactionid) values(?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());

            pst.setString(2, "Purchase Payment," + reason);
            pst.setString(3, (String) Bankinfobox.getSelectedItem());
            pst.setString(4, (String) accountbox.getSelectedItem());
            pst.setFloat(5, cashin);
            pst.setFloat(6, cashout);
            pst.setFloat(7, accbalance);
            pst.setFloat(8, presentbalnce);
            pst.setFloat(9, totalb);

            pst.setString(10, "Purchase Payment," + "Trasnsaction:" + reason + " GRN No: " + grntext.getText() + "Purchase No:" + grntext.getText());
            pst.setString(11, transectiontext.getText());

            pst.execute();

            // JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void selectgrn() {
        String SearchText = (String) parchaseBox.getSelectedItem();
        try {
            String sql = "Select paidamount,due from grninfo gn where gn.GRNCode='" + SearchText + "'";
            //  String sql = "Select gr.purchaseCode as 'purchaseNo', (select pr.supliyer from purchase pr where pr.purchaseCode=gr.purchaseCode) as 'supl', (select pr.nettotal from purchase pr where pr.purchaseCode=gr.purchaseCode) as 'Totalamt' ,gr.due as 'due' from GRNinfo gr where gr.GRNCode='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            if (rs.next()) {

                // supid = rs.getInt("supid");
                grnpaid = rs.getFloat("paidamount");

                grndue = rs.getFloat("due");

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
private void Selectbankaccount(){

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
        suppliyetext = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        parchaseBox = new javax.swing.JComboBox<>();
        grntext = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        totalmttext = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        openingdate = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        duetext = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        paidamounttext = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        balanceduetext = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarktext = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        paymenttypebox = new javax.swing.JComboBox<>();
        svbtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();
        ppayemtnid = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        transectiontext = new javax.swing.JTextField();
        paymentsbox = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        chequenotext = new javax.swing.JTextField();
        Bankinfobox = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        accountbox = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        setTitle("Purchase Payment");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 0)));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 0));
        jLabel2.setText("Payment Date");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 102, 0));
        jLabel3.setText("Purchase No");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 51, 0));
        jLabel4.setText("GRN No");

        suppliyetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        suppliyetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        suppliyetext.setEnabled(false);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Suppliyer Name");

        parchaseBox.setEditable(true);
        parchaseBox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        parchaseBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        parchaseBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                parchaseBoxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        grntext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        grntext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        grntext.setEnabled(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 51, 0));
        jLabel7.setText("Total Amount");

        totalmttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalmttext.setForeground(new java.awt.Color(204, 51, 0));
        totalmttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalmttext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalmttext.setEnabled(false);
        totalmttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalmttextActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 51, 0));
        jLabel8.setText("Transaction No");

        openingdate.setForeground(new java.awt.Color(102, 102, 102));
        openingdate.setDateFormatString("yyyy-MM-dd");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 51, 0));
        jLabel9.setText("Due");

        duetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        duetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        duetext.setText("0");
        duetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        duetext.setEnabled(false);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 51, 0));
        jLabel10.setText("Paid Amount");

        paidamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        paidamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paidamounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                paidamounttextKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                paidamounttextKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 51, 0));
        jLabel11.setText("Balance Due");

        balanceduetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        balanceduetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        balanceduetext.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        balanceduetext.setEnabled(false);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 0));
        jLabel12.setText("Remark");

        remarktext.setColumns(20);
        remarktext.setRows(5);
        jScrollPane2.setViewportView(remarktext);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 0));
        jLabel13.setText("Payment Type");

        paymenttypebox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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

        svbtn.setBackground(new java.awt.Color(255, 51, 0));
        svbtn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        svbtn.setForeground(new java.awt.Color(255, 255, 255));
        svbtn.setText("Execute");
        svbtn.setBorder(null);
        svbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svbtnActionPerformed(evt);
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

        deletebtn.setBackground(new java.awt.Color(102, 0, 0));
        deletebtn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        deletebtn.setForeground(new java.awt.Color(255, 255, 255));
        deletebtn.setText("Delete");
        deletebtn.setBorder(null);
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        ppayemtnid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 51, 0));
        jLabel14.setText("Payment Status");

        transectiontext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        transectiontext.setForeground(new java.awt.Color(51, 153, 0));
        transectiontext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        transectiontext.setEnabled(false);

        paymentsbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        paymentsbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Pending", "Due Remain" }));
        paymentsbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                paymentsboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Bank Info(Payment)"));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel6.setText("Acc No");

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

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel15.setText("Bank Name");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel16.setText("Cheque No");

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
                    .addComponent(jLabel15)
                    .addComponent(jLabel6)
                    .addComponent(jLabel16))
                .addGap(90, 90, 90)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Bankinfobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accountbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chequenotext, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(Bankinfobox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(accountbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(chequenotext, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 498, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(ppayemtnid, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 34, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(paymentsbox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(parchaseBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 340, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(openingdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(balanceduetext, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(paidamounttext, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(duetext, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalmttext, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(suppliyetext, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(grntext, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(paymenttypebox, 0, 340, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(transectiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(openingdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(transectiontext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(paymentsbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(parchaseBox, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(96, 96, 96)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(grntext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(suppliyetext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(1, 1, 1)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(totalmttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(1, 1, 1)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(duetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)
                                .addComponent(paidamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(balanceduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(paymenttypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(123, 123, 123)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ppayemtnid, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void parchaseBoxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_parchaseBoxPopupMenuWillBecomeInvisible
        if (parchaseBox.getSelectedIndex() == 0) {

        } else {
            String SearchText = (String) parchaseBox.getSelectedItem();
            try {
                String sql = "Select gn.purchaseCode as 'purchaseNo',par.supliyer as 'supid',paidamount,(select sup.supliyername from suplyierinfo sup where par.supliyer=sup.id) as 'supplier',par.nettotal as 'nettotal',gn.due as 'DueAmount' from purchase par inner join grninfo gn ON par.purchaseCode=gn.purchaseCode  where gn.GRNCode='" + SearchText + "'";
                //  String sql = "Select gr.purchaseCode as 'purchaseNo', (select pr.supliyer from purchase pr where pr.purchaseCode=gr.purchaseCode) as 'supl', (select pr.nettotal from purchase pr where pr.purchaseCode=gr.purchaseCode) as 'Totalamt' ,gr.due as 'due' from GRNinfo gr where gr.GRNCode='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                //  pst.setString(1, SearchText);

                rs = pst.executeQuery();
                if (rs.next()) {

                    String grnco = rs.getString("supplier");
                    suppliyetext.setText(grnco);
                    supid = rs.getInt("supid");

                    String unitrate = rs.getString("purchaseNo");
                    grntext.setText(unitrate);

                    String totalamt = rs.getString("nettotal");
                    totalmttext.setText(totalamt);

                    String due = rs.getString("DueAmount");
                    duetext.setText(due);

                    paidamtnprevoous = rs.getFloat("paidamount");

                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }//GEN-LAST:event_parchaseBoxPopupMenuWillBecomeInvisible

    private void totalmttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalmttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalmttextActionPerformed

    private void paidamounttextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {
        } else {

            float paidamthere = Float.parseFloat(paidamounttext.getText());
            float amount = Float.parseFloat(totalmttext.getText());
            //float due=Float.parseFloat(duetext.getText());
            float finelpaidamnt = paidamtnprevoous + paidamthere;

            // float totalamt=amount-finelpaidamnt;
            if (finelpaidamnt > amount) {

                balanceduetext.setText(null);

            } else {
                float balanceDue = amount - finelpaidamnt;
                String Balancdus = String.format("%.2f", balanceDue);
                balanceduetext.setText(Balancdus);

            }

        }

    }//GEN-LAST:event_paidamounttextKeyPressed

    private void paidamounttextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttextKeyTyped

    }//GEN-LAST:event_paidamounttextKeyTyped

    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed

        currentDate();
        transactioncode();
        try {
            balance();
        } catch (SQLException ex) {
            Logger.getLogger(Purchasepayment.class.getName()).log(Level.SEVERE, null, ex);
        }

        float amount = Float.parseFloat(paidamounttext.getText());
        try {
            if (ppayemtnid.getText().isEmpty()) {
                if (parchaseBox.getSelectedIndex() == 0 || paidamounttext.getText().isEmpty() || balanceduetext.getText().isEmpty()) {

                } else {
                    try {

                        if (paymenttypebox.getSelectedIndex() == 0) {

                            if (amount > bankbalance1) {
                                JOptionPane.showMessageDialog(null, "Not Save!! Lack Of Cash Balance On Your Cash Box");

                            } else {

                                addStock();
                                cashInDrwaer();
                                printInvoice();
                                clear();
                            }

                        } else if (accbalance >= amount) {
                            addStock();
                            String accountno = (String) accountbox.getSelectedItem();
                            float presentbalnce = bankcashout + amount;
                            balanceupdatecashout(accountno, presentbalnce);
                            balanceamount(accountno);
                            totalbalance();

                            overallinsert(updatebankbalance);
                            printInvoice();
                             clear();
                        } else {

                            JOptionPane.showMessageDialog(null, "Fail To save, Amount Value More than preasent Value in this account");

                        }

                        // paidamtcheck();
                        
                      

                    } catch (SQLException | JRException ex) {
                        Logger.getLogger(Purchasepayment.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                //   payentUpdate();
                // cashdrowerupdate();
                ////  parchaseUpdate();
                // paidamtcheck();

            }

        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_svbtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        try {
            clear();

        } catch (SQLException ex) {
            Logger.getLogger(Purchasepayment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        if (ppayemtnid.getText().isEmpty()) {

        } else {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from parchasepayment where transactionNo=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, transectiontext.getText());

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }

            try {
                parchaseUpdateDelete();
                clear();
                ppayemtnid.getText();
            } catch (SQLException ex) {
                Logger.getLogger(Purchasepayment.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_deletebtnActionPerformed

    private void paymentsboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_paymentsboxPopupMenuWillBecomeInvisible
        try {
            parchaseBox.removeAllItems();
            parchaseBox.addItem("Select");
            String sql = "Select GRNCode from grninfo where  payment='" + paymentsbox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("GRNCode");

                parchaseBox.addItem(name);
                //  supliyerbox.setSelectedIndex(id);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_paymentsboxPopupMenuWillBecomeInvisible

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
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField duetext;
    private javax.swing.JTextField grntext;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JScrollPane jScrollPane2;
    private com.toedter.calendar.JDateChooser openingdate;
    private javax.swing.JTextField paidamounttext;
    private javax.swing.JComboBox<String> parchaseBox;
    private javax.swing.JComboBox<String> paymentsbox;
    private javax.swing.JComboBox<String> paymenttypebox;
    private javax.swing.JLabel ppayemtnid;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JTextField suppliyetext;
    private javax.swing.JButton svbtn;
    private javax.swing.JTextField totalmttext;
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
