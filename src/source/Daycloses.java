/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
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

/**
 *
 * @author FAHIM
 */
public class Daycloses extends javax.swing.JInternalFrame {

    private static final DecimalFormat df2 = new DecimalFormat("#.00");
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    java.sql.Date date;
    int month = 0;
    String year = null;
    String year1 = null;
    String description;
    int userid;

    /**
     * Creates new form Daycloses
     *
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public Daycloses() throws IOException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        currentDate();
        dayClose();

    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        fromdatepayment1.setDate(date);

    }

    private void dayClose() throws SQLException, IOException {
        purchase();
        purchaseDue();
        purchaseDueReturn();
        purchasepayment();
        totalpurchasePyment();

        creditinvoice();
        expensess();
        //  saleryexpensess();
        // totalexp();
        creditrecive();
        totalcreditrecievd();
        Invoicedue();
        bankcashincashout();
        drwercashincashout();
        totalcashincashout();
        bankbalance();
        cashbalance();
        salereturn();
        Income();
        Profit();
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

    private void purchase() {
        try {
            String sql = "select sum(nettotal)as 'totalpurchase' from purchase where GRNstatus=1 AND pdate='" + date + "' OR MONTH(pdate)='" + month + "' AND YEAR(pdate)='" + year + "' OR YEAR(pdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String Purchase = rs.getString("totalpurchase");

                if (Purchase == null) {
                    purchasetext.setText("0");

                } else {
                    purchasetext.setText(Purchase);

                }

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void purchaseDue() {
        try {
            String sql = "select sum(due) as 'purchasedue' from grninfo where GRNdate='" + date + "' OR MONTH(GRNdate)='" + month + "' AND YEAR(GRNdate)='" + year + "' OR YEAR(GRNdate)='" + year1 + "' ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String Purchase = rs.getString("purchasedue");
                if (Purchase == null) {
                    purchaseduetext.setText("0");

                } else {
                    purchaseduetext.setText(Purchase);

                }

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void purchaseDueReturn() {
        try {
            String sql = "select sum(nettotal) as 'preturn' from purchasereturn  where pdate='" + date + "' OR MONTH(pdate)='" + month + "' AND YEAR(pdate)='" + year + "' OR YEAR(pdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String Purchase = rs.getString("preturn");
                if (Purchase == null) {
                    purchasereturntext.setText("0");

                } else {
                    purchasereturntext.setText(Purchase);

                }

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    /*   private void cashinvoice() {
        try {
            String sql = "select sum(Totalinvoice)as 'totalcash' from cashsale where invoicedate='" + date + "'OR MONTH(invoicedate)='" + month + "' AND YEAR(invoicedate)='" + year + "' OR YEAR(invoicedate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String totalcash = rs.getString("totalcash");
              

                if (totalcash == null) {
                    TotalInvoicecashtext.setText("0");

                } else {

                    TotalInvoicecashtext.setText(totalcash);

                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
     */
    private void creditinvoice() {
        try {
            String sql = "select sum(Totalinvoice)as 'totalcredit' from sale where invoicedate='" + date + "'OR MONTH(invoicedate)='" + month + "' AND YEAR(invoicedate)='" + year + "' OR YEAR(invoicedate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String totalcredit = rs.getString("totalcredit");

                if (totalcredit == null) {

                    TotalInvoicetext.setText("0");

                } else {

                    TotalInvoicetext.setText(totalcredit);
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void salereturn() {
        try {
            String sql = "select sum(Totalinvoice)as 'totalcredit' from salereturn where returndate='" + date + "'OR MONTH(returndate)='" + month + "' AND YEAR(returndate)='" + year + "' OR YEAR(returndate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String totalcredit = rs.getString("totalcredit");

                if (totalcredit == null) {

                    salereturnInvoicetext.setText("0");

                } else {

                    salereturnInvoicetext.setText(totalcredit);
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void Invoicedue() {
        try {
            String sql = "select sum(due)as 'dueamt' from sale where invoicedate='" + date + "'OR MONTH(invoicedate)='" + month + "' AND YEAR(invoicedate)='" + year + "' OR YEAR(invoicedate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String dueamt = rs.getString("dueamt");

                if (dueamt == null) {

                    invoiceduetext.setText("0");

                } else {

                    invoiceduetext.setText(dueamt);
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void purchasepayment() {
        String cashtype = "Cash";
        try {
            String sql = "select sum(paidamount)as 'purchasecashpayment' from purchasepaymenthistory where paymenttype='" + cashtype + "' AND paymentdate='" + date + "'OR MONTH(paymentdate)='" + month + "' AND YEAR(paymentdate)='" + year + "' OR YEAR(paymentdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String purchasecashpayment = rs.getString("purchasecashpayment");

                if (purchasecashpayment == null) {
                    cashpaymenttext.setText("0");
                } else {
                    cashpaymenttext.setText(purchasecashpayment);
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        String banktype = "Bank";
        try {
            String sql = "select sum(paidamount)as 'purchasebankpayment' from purchasepaymenthistory where paymenttype='" + banktype + "'AND paymentdate='" + date + "'OR MONTH(paymentdate)='" + month + "' AND YEAR(paymentdate)='" + year + "' OR YEAR(paymentdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String purchasebankpayment = rs.getString("purchasebankpayment");

                if (purchasebankpayment == null) {
                    chequepayment.setText("0");

                } else {
                    chequepayment.setText(purchasebankpayment);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalpurchasePyment() {
        double cashpayment = 0;
        double bankpayment = 0;
        double totalpurchasepayment = 0;
        cashpayment = Double.parseDouble(cashpaymenttext.getText());
        bankpayment = Double.parseDouble(chequepayment.getText());
        totalpurchasepayment = cashpayment + bankpayment;
        String totalpurchasepayments = String.format("%.2f", totalpurchasepayment);
        totalpurchasepaymenttext.setText(totalpurchasepayments);

    }

    private void totalinvoice() {
        double creditinvoice = 0;
        double cashinvoice = 0;
        double totalinvoice = 0;
        creditinvoice = Double.parseDouble(TotalInvoicetext.getText());
        //    cashinvoice = Double.parseDouble(TotalInvoicecashtext.getText());
        totalinvoice = creditinvoice + cashinvoice;
        String totlcashins = String.format("%.2f", totalinvoice);
        //   totalinvoicetext.setText(totlcashins);

    }

    private void expensess() {
        try {
            String sql = "select sum(amount)as 'tamount' from expencess where inputdate='" + date + "' OR MONTH(inputdate)='" + month + "' AND YEAR(inputdate)='" + year + "' OR YEAR(inputdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String amount = rs.getString("tamount");

                if (amount == null) {
                    TotalExpencesstext.setText("0");

                } else {
                    TotalExpencesstext.setText(amount);

                }

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    /* private void saleryexpensess() {
        try {
            String sql = "select sum(amount)as 'tamount' from emplyersallery where inputdate='" + date + "' OR MONTH(inputdate)='" + month + "' AND YEAR(inputdate)='" + year + "' OR YEAR(inputdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String amount = rs.getString("tamount");

                if (amount == null) {
                    saleryExpencesstext.setText("0");

                } else {
                    saleryExpencesstext.setText(amount);

                }

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalexp() {
        double saleryexp = 0;
        double otherexp = 0;
        double totlexp = 0;

        saleryexp = Double.parseDouble(saleryExpencesstext.getText());
        otherexp = Double.parseDouble(TotalExpencesstext.getText());
        totlexp = saleryexp + otherexp;
        String totalexps = String.format("%.2f", totlexp);
        totalexp.setText(totalexps);
    }
     */
    private void creditrecive() {
        String cashtype = "Cash";
        try {
            String sql = "select sum(paidamount)as 'creditcashrecieve' from invoicepaymenthistory where paymenttype='" + cashtype + "' AND paymentdate='" + date + "' OR MONTH(paymentdate)='" + month + "' AND YEAR(paymentdate)='" + year + "' OR YEAR(paymentdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String creditcashrecieve = rs.getString("creditcashrecieve");
                if (creditcashrecieve == null) {
                    creditcashrecievetext.setText("0");

                } else {
                    creditcashrecievetext.setText(creditcashrecieve);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        String banktype = "Bank";
        try {
            String sql = "select sum(paidamount)as 'creditbankrecieve' from invoicepaymenthistory where paymenttype='" + banktype + "'AND paymentdate='" + date + "'OR MONTH(paymentdate)='" + month + "' AND YEAR(paymentdate)='" + year + "' OR YEAR(paymentdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String creditbankrecieve = rs.getString("creditbankrecieve");
                if (creditbankrecieve == null) {

                    creditbankrecievetext.setText("0");
                } else {
                    creditbankrecievetext.setText(creditbankrecieve);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalcreditrecievd() {
        double cash = 0;
        double bank = 0;
        double total = 0;
        cash = Double.parseDouble(creditcashrecievetext.getText());
        bank = Double.parseDouble(creditbankrecievetext.getText());
        //    cashinvoice = Double.parseDouble(TotalInvoicecashtext.getText());
        total = cash + bank;
        String totlcashins = String.format("%.2f", total);
        totalrecievedtext.setText(totlcashins);

    }

    private void bankcashincashout() {
        try {
            String sql = "select sum(cashin) as 'cashin',sum(cashout) as 'cashout' from bankoverall where inputdate='" + date + "' OR MONTH(inputdate)='" + month + "' AND YEAR(inputdate)='" + year + "' OR YEAR(inputdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == true) {
                String cashin = rs.getString("cashin");
                String cashout = rs.getString("cashout");
                if (cashin == null) {

                    bankcashintext.setText("0");

                } else {
                    bankcashintext.setText(cashin);
                }
                if (cashout == null) {
                    bankcashouttext.setText("0");
                } else {

                    bankcashouttext.setText(cashout);

                }

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void drwercashincashout() {
        try {
            String sql = "select sum(cashin) as 'cashin',sum(cashout) as 'cashout' from cashdrower where upatedate='" + date + "' OR MONTH(upatedate)='" + month + "' AND YEAR(upatedate)='" + year + "' OR YEAR(upatedate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == true) {
                String cashin = rs.getString("cashin");

                String cashout = rs.getString("cashout");

                if (cashin == null) {

                    cashboxcashin.setText("0");

                } else {
                    cashboxcashin.setText(cashin);
                }
                if (cashout == null) {
                    cashboxcashouttext.setText("0");
                } else {

                    cashboxcashouttext.setText(cashout);

                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalcashincashout() {
        //cshdrwer

        double cashindrwer = Double.parseDouble(cashboxcashin.getText());
        double cashinbank = Double.parseDouble(bankcashintext.getText());

        double totalcashin = cashindrwer + cashinbank;
        String totalcashins = String.format("%.2f", totalcashin);
        totalcashintext.setText(totalcashins);

        ///bank
        double cashoutdrwer = Double.parseDouble(cashboxcashouttext.getText());
        double cashoutbank = Double.parseDouble(bankcashouttext.getText());
        double totalcashout = cashoutbank + cashoutdrwer;
        String totalcashouts = String.format("%.2f", totalcashout);
        totalcashouttext.setText(totalcashouts);

    }

    private void cashbalance() {

        try {
            String sql = "select sum(cashin) as 'cashin',sum(cashout) as 'cashout' from cashdrower ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == true) {
                double cashin = rs.getDouble("cashin");
                double cashout = rs.getDouble("cashout");
                double bankbalance = cashin - cashout;
                String bankbalances = String.format("%.2f", bankbalance);
                cashbalancetext.setText(bankbalances);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void bankbalance() {

        try {
            String sql = "select sum(cashin) as 'cashin',sum(cashout) as 'cashout' from bankoverall ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == true) {
                double cashin = rs.getDouble("cashin");
                double cashout = rs.getDouble("cashout");
                double bankbalance = cashin - cashout;
                String bankbalances = String.format("%.2f", bankbalance);
                bankbalancetext.setText(bankbalances);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void Income() {

        try {
            String sql = "select sum(totalprofite) as 'totalcredit' from sale where invoicedate='" + date + "'OR MONTH(invoicedate)='" + month + "' AND YEAR(invoicedate)='" + year + "' OR YEAR(invoicedate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String totalcredit = rs.getString("totalcredit");

                if (totalcredit == null) {

                    incometext.setText("0");

                } else {

                    incometext.setText(totalcredit);
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void Profit() {
        float income = 0;
        float expenses = 0;
        float profit = 0;
        if (incometext.getText().isEmpty()) {
            income = 0;
        } else {
            income = Float.parseFloat(incometext.getText());

        }
        if (TotalExpencesstext.getText().isEmpty()) {
            expenses = 0;
        } else {
            expenses = Float.parseFloat(TotalExpencesstext.getText());
        }
        profit = income - expenses;
        profttext.setText(df2.format(profit));

    }

    private void ClosingMake() {
        try {
            String sql = "Insert into day_closes(date,Invoice,invoiceReturn,purchase,purchasereturn,purchase_payment,purchase_payment_bank,purchase_payment_cash,purchase_due,Payment_Recievd,payment_recieved_bank,payment_recieved_cash,Invoice_due,expenses,income,profit,cashin,cashin_bank,cashin_cash,cashout,cashout_cash,cashout_bank,cashdrawer,cashinbank,status,user_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).getText());
            pst.setString(2, TotalInvoicetext.getText());
            pst.setString(3, salereturnInvoicetext.getText());
            pst.setString(4, purchasetext.getText());
            pst.setString(5, purchasereturntext.getText());
            pst.setString(6, totalpurchasepaymenttext.getText());
            pst.setString(7, cashpaymenttext.getText());
            pst.setString(8, chequepayment.getText());
            pst.setString(9, purchaseduetext.getText());
            pst.setString(10, totalpurchasepaymenttext.getText());
            pst.setString(11, creditcashrecievetext.getText());
            pst.setString(12, creditbankrecievetext.getText());
            pst.setString(13, invoiceduetext.getText());
            pst.setString(14, TotalExpencesstext.getText());
            pst.setString(15, incometext.getText());
            pst.setString(16, profttext.getText());
            pst.setString(17, totalcashintext.getText());
            pst.setString(18, bankcashintext.getText());
            pst.setString(19, cashboxcashin.getText());
            pst.setString(20, totalcashouttext.getText());
            pst.setString(21, cashboxcashouttext.getText());
            pst.setString(22, bankcashouttext.getText());
            pst.setString(23, cashbalancetext.getText());
            pst.setString(24, bankbalancetext.getText());
            pst.setInt(25, 1);
            pst.setInt(26, userid);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Day Close Successfuly");
            Clear();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void Clear() {
        TotalInvoicetext.setText(null);
        salereturnInvoicetext.setText(null);
        purchasetext.setText(null);
        purchasereturntext.setText(null);
        totalpurchasepaymenttext.setText(null);
        cashpaymenttext.setText(null);
        chequepayment.setText(null);
        purchaseduetext.setText(null);
        totalpurchasepaymenttext.setText(null);
        creditcashrecievetext.setText(null);
        creditbankrecievetext.setText(null);
        invoiceduetext.setText(null);
        TotalExpencesstext.setText(null);
        incometext.setText(null);
        profttext.setText(null);
        totalcashintext.setText(null);
        bankcashintext.setText(null);
        cashboxcashin.setText(null);
        totalcashouttext.setText(null);
        cashboxcashouttext.setText(null);
        bankcashouttext.setText(null);
        cashbalancetext.setText(null);
        bankbalancetext.setText(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jYearChooser2 = new com.toedter.calendar.JYearChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        fromdatepayment1 = new com.toedter.calendar.JDateChooser();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        cashbalancetext = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        totalcashintext = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        TotalInvoicetext = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        incometext = new javax.swing.JTextField();
        profttext = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        salereturnInvoicetext = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        creditbankrecievetext = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        creditcashrecievetext = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        totalrecievedtext = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        purchaseduetext = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        TotalExpencesstext = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        invoiceduetext = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        bankcashintext = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        cashboxcashin = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        purchasetext = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        purchasereturntext = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        chequepayment = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        cashpaymenttext = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        totalpurchasepaymenttext = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        bankcashouttext = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        cashboxcashouttext = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        totalcashouttext = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        bankbalancetext = new javax.swing.JTextField();

        setBorder(null);
        setClosable(true);
        setIconifiable(true);
        setTitle("Closing");

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jLabel57.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Day");

        fromdatepayment1.setDateFormatString("yyyy-MM-dd");

        jButton2.setText("Submit");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel57)
                .addGap(4, 4, 4)
                .addComponent(fromdatepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fromdatepayment1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );

        jPanel2.setBackground(new java.awt.Color(0, 51, 51));

        jButton1.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setText("Print");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addGap(2, 2, 2))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Cash Balance");

        cashbalancetext.setEditable(false);
        cashbalancetext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cashbalancetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashbalancetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cashbalancetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashbalancetextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(cashbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Cash In");

        totalcashintext.setEditable(false);
        totalcashintext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalcashintext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalcashintext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        totalcashintext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalcashintextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(totalcashintext, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalcashintext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        TotalInvoicetext.setEditable(false);
        TotalInvoicetext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TotalInvoicetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalInvoicetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        TotalInvoicetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalInvoicetextActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Invoice");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(TotalInvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(131, 131, 131))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TotalInvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setText("Income");

        incometext.setEditable(false);
        incometext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        incometext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        incometext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        profttext.setEditable(false);
        profttext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        profttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        profttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel30.setText("profit");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(incometext, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(profttext, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(incometext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("Invoice Return");

        salereturnInvoicetext.setEditable(false);
        salereturnInvoicetext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        salereturnInvoicetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        salereturnInvoicetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        salereturnInvoicetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salereturnInvoicetextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(salereturnInvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(salereturnInvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        creditbankrecievetext.setEditable(false);
        creditbankrecievetext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        creditbankrecievetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        creditbankrecievetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("Total");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setText("Bank");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Cash");

        creditcashrecievetext.setEditable(false);
        creditcashrecievetext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        creditcashrecievetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        creditcashrecievetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Payment Recieved");

        totalrecievedtext.setEditable(false);
        totalrecievedtext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalrecievedtext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalrecievedtext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel22)
                .addGap(2, 2, 2)
                .addComponent(creditcashrecievetext, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel23)
                .addGap(2, 2, 2)
                .addComponent(creditbankrecievetext, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel21)
                .addGap(2, 2, 2)
                .addComponent(totalrecievedtext, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(creditcashrecievetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(creditbankrecievetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalrecievedtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        purchaseduetext.setEditable(false);
        purchaseduetext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        purchaseduetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        purchaseduetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Purchase Due");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(purchaseduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchaseduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel16.setText("Status");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Open", "Close" }));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        TotalExpencesstext.setEditable(false);
        TotalExpencesstext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TotalExpencesstext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalExpencesstext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        TotalExpencesstext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalExpencesstextActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Expenses");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(TotalExpencesstext, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TotalExpencesstext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        invoiceduetext.setEditable(false);
        invoiceduetext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        invoiceduetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        invoiceduetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        invoiceduetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceduetextActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Invoice Due");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(invoiceduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(invoiceduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Cashi In");

        bankcashintext.setEditable(false);
        bankcashintext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        bankcashintext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bankcashintext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Bank");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setText("Cash");

        cashboxcashin.setEditable(false);
        cashboxcashin.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cashboxcashin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashboxcashin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addGap(34, 34, 34)
                .addComponent(bankcashintext, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashboxcashin, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bankcashintext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel24)
                    .addComponent(cashboxcashin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        purchasetext.setEditable(false);
        purchasetext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        purchasetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        purchasetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Purchase");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(purchasetext, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchasetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setText("Purchase Return");

        purchasereturntext.setEditable(false);
        purchasereturntext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        purchasereturntext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        purchasereturntext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(purchasereturntext, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchasereturntext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        chequepayment.setEditable(false);
        chequepayment.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        chequepayment.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        chequepayment.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setText("Total");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setText("Bank");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Cash");

        cashpaymenttext.setEditable(false);
        cashpaymenttext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cashpaymenttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashpaymenttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Payment Recieved");

        totalpurchasepaymenttext.setEditable(false);
        totalpurchasepaymenttext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalpurchasepaymenttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalpurchasepaymenttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel18)
                .addGap(2, 2, 2)
                .addComponent(cashpaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel19)
                .addGap(2, 2, 2)
                .addComponent(chequepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel20)
                .addGap(2, 2, 2)
                .addComponent(totalpurchasepaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashpaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chequepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalpurchasepaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(2, 2, 2))
        );

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("Cashout");

        bankcashouttext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        bankcashouttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bankcashouttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setText("Bank");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel27.setText("Cash");

        cashboxcashouttext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        cashboxcashouttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashboxcashouttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel26)
                .addGap(34, 34, 34)
                .addComponent(bankcashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashboxcashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bankcashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(cashboxcashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel28.setText("Cash Out");

        totalcashouttext.setEditable(false);
        totalcashouttext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalcashouttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalcashouttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        totalcashouttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalcashouttextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(totalcashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalcashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel29.setText("Bank Balance");

        bankbalancetext.setEditable(false);
        bankbalancetext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        bankbalancetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bankbalancetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        bankbalancetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bankbalancetextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(bankbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bankbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void totalcashintextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalcashintextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalcashintextActionPerformed

    private void TotalInvoicetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TotalInvoicetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalInvoicetextActionPerformed

    private void salereturnInvoicetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salereturnInvoicetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_salereturnInvoicetextActionPerformed

    private void cashbalancetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashbalancetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cashbalancetextActionPerformed

    private void invoiceduetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceduetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_invoiceduetextActionPerformed

    private void TotalExpencesstextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TotalExpencesstextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TotalExpencesstextActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ClosingMake();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void totalcashouttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalcashouttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalcashouttextActionPerformed

    private void bankbalancetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bankbalancetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bankbalancetextActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //String dates = ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).getText();

        month = 0;
        year = null;
        year1 = null;
        date = new java.sql.Date(fromdatepayment1.getDate().getTime());
        try {
            dayClose();
        } catch (SQLException ex) {
            Logger.getLogger(Daycloses.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Daycloses.class.getName()).log(Level.SEVERE, null, ex);
        }
        description = "Description:" + " Day Search: " + date;

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        /*
        try {

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/DaycloseThermal.jrxml");

           
            HashMap para = new HashMap();
            para.put("description", description);
            para.put("Invoice", TotalInvoicetext.getText());
            para.put("invoiceReturn", salereturnInvoicetext.getText());
            para.put("purchase", purchasetext.getText());
            para.put("purchasereturn", purchasereturntext.getText());
            para.put("purchase_payment", totalpurchasepaymenttext.getText());
            para.put("purchase_due", purchaseduetext.getText());
            para.put("Payment_Recievd", totalrecievedtext.getText());
            para.put("expenses", TotalExpencesstext.getText());
            para.put("Invoice_due", invoiceduetext.getText());;
            para.put("income", incometext.getText());
            para.put("profit", profttext.getText());
            para.put("cashin", totalcashintext.getText());
            para.put("cashout", totalcashouttext.getText());
            para.put("cashdrawer", cashbalancetext.getText());
            para.put("cashinbank", bankbalancetext.getText());

            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);
        } catch (NumberFormatException | JRException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
        
         */
        try {

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/Dayclose.jrxml");

            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");
            HashMap para = new HashMap();
            para.put("date", ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).getText());
            para.put("purchase_due", purchaseduetext.getText());
                para.put("Invoice_due", invoiceduetext.getText());;
            para.put("purchase", purchasetext.getText());
            para.put("expenses", TotalExpencesstext.getText());
            para.put("invoiceReturn", salereturnInvoicetext.getText());
            para.put("creditinvoice", TotalInvoicetext.getText());
            para.put("purchasereturn", purchasereturntext.getText());
            para.put("pcaspay", cashpaymenttext.getText());
            para.put("pbanpay", chequepayment.getText());
            para.put("ptotalpay", totalpurchasepaymenttext.getText());

            para.put("crcasre", creditcashrecievetext.getText());
            para.put("crbancr", creditbankrecievetext.getText());
            para.put("totalcrre", totalrecievedtext.getText());

            para.put("bankcashout", bankcashintext.getText());
            para.put("bankcashin", bankcashouttext.getText());
            para.put("drwercashin", cashboxcashin.getText());
            para.put("drwercashout", cashboxcashouttext.getText());
            para.put("totlcashin", totalcashintext.getText());
            para.put("totlcashout", totalcashouttext.getText());
            para.put("bankbalanc", bankbalancetext.getText());
            para.put("cashbalance", cashbalancetext.getText());

            para.put("otherexp", TotalExpencesstext.getText());

            para.put("description", description);

            JasperReport jr = JasperCompileManager.compileReport(jd);

            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

            JasperViewer.viewReport(jp, false);

        } catch (NumberFormatException | JRException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TotalExpencesstext;
    private javax.swing.JTextField TotalInvoicetext;
    private javax.swing.JTextField bankbalancetext;
    private javax.swing.JTextField bankcashintext;
    private javax.swing.JTextField bankcashouttext;
    private javax.swing.JTextField cashbalancetext;
    private javax.swing.JTextField cashboxcashin;
    private javax.swing.JTextField cashboxcashouttext;
    private javax.swing.JTextField cashpaymenttext;
    private javax.swing.JTextField chequepayment;
    private javax.swing.JTextField creditbankrecievetext;
    private javax.swing.JTextField creditcashrecievetext;
    private com.toedter.calendar.JDateChooser fromdatepayment1;
    private javax.swing.JTextField incometext;
    private javax.swing.JTextField invoiceduetext;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox2;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JYearChooser jYearChooser2;
    private javax.swing.JTextField profttext;
    private javax.swing.JTextField purchaseduetext;
    private javax.swing.JTextField purchasereturntext;
    private javax.swing.JTextField purchasetext;
    private javax.swing.JTextField salereturnInvoicetext;
    private javax.swing.JTextField totalcashintext;
    private javax.swing.JTextField totalcashouttext;
    private javax.swing.JTextField totalpurchasepaymenttext;
    private javax.swing.JTextField totalrecievedtext;
    // End of variables declaration//GEN-END:variables
}
