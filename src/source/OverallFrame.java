/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author adnan
 */
public class OverallFrame extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    java.sql.Date date;
    int month = 0;
    String year = null;
    String year1 = null;
    String description;

    /**
     * Creates new form OverallFrame
     *
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.sql.SQLException
     */
    public OverallFrame() throws IOException, FileNotFoundException, SQLException {
        initComponents();

        conn = Java_Connect.conectrDB();

        //dayClose();
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        //  inputdate.setDate(date);

    }

    private void dayClose() {
        purchase();
        purchaseDue();
        purchasepayment();
        totalpurchasePyment();

        creditinvoice();
        expensess();
        //  saleryexpensess();
        totalexp();
        creditrecive();
        Invoicedue();
        bankcashincashout();
        drwercashincashout();
        totalcashincashout();
        bankbalance();
        cashbalance();

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
            String sql = "select sum(due) as 'purchasedue' from grninfo";
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

    private void Invoicedue() {
        try {
            String sql = "select sum(due)as 'dueamt' from sale";
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
            String sql = "select sum(paidamount)as 'purchasecashpayment' from parchasepayment where paymenttype='" + cashtype + "' AND paymentdate='" + date + "'OR MONTH(paymentdate)='" + month + "' AND YEAR(paymentdate)='" + year + "' OR YEAR(paymentdate)='" + year1 + "'";
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        String banktype = "Bank";
        try {
            String sql = "select sum(paidamount)as 'purchasebankpayment' from parchasepayment where paymenttype='" + banktype + "'AND paymentdate='" + date + "'OR MONTH(paymentdate)='" + month + "' AND YEAR(paymentdate)='" + year + "' OR YEAR(paymentdate)='" + year1 + "'";
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

        } catch (Exception e) {
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

    private void saleryexpensess() {
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        TotalInvoicetext = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        TotalExpencesstext = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        saleryExpencesstext = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        totalexp = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        purchasetext = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        creditbankrecievetext = new javax.swing.JTextField();
        creditcashrecievetext = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        totalcredittext = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        invoiceduetext = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        cashpaymenttext = new javax.swing.JTextField();
        chequepayment = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        totalpurchasepaymenttext = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        purchaseduetext = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        bankcashintext = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        bankcashouttext = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        cashboxcashin = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        cashboxcashouttext = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        totalcashintext = new javax.swing.JTextField();
        totalcashouttext = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        cashbalancetext = new javax.swing.JTextField();
        bankbalancetext = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        fromdatepayment1 = new com.toedter.calendar.JDateChooser();
        jLabel57 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        monthbox = new javax.swing.JComboBox<>();
        yeartext = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        overalsubmit = new javax.swing.JButton();
        jLabel59 = new javax.swing.JLabel();
        descriptiontext = new javax.swing.JLabel();
        reportbox = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Day Close");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(null);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Operation"));

        TotalInvoicetext.setEditable(false);
        TotalInvoicetext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        TotalInvoicetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalInvoicetext.setText("0");
        TotalInvoicetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        TotalInvoicetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("Invoice");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Expensess");

        TotalExpencesstext.setEditable(false);
        TotalExpencesstext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        TotalExpencesstext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalExpencesstext.setText("0");
        TotalExpencesstext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        TotalExpencesstext.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("Salery Expenses");

        saleryExpencesstext.setEditable(false);
        saleryExpencesstext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        saleryExpencesstext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        saleryExpencesstext.setText("0");
        saleryExpencesstext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        saleryExpencesstext.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("Total Expenses");

        totalexp.setEditable(false);
        totalexp.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        totalexp.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalexp.setText("0");
        totalexp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        totalexp.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("Purchase");

        purchasetext.setEditable(false);
        purchasetext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        purchasetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        purchasetext.setText("0");
        purchasetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        purchasetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(TotalInvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalexp, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saleryExpencesstext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TotalExpencesstext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(purchasetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TotalInvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(purchasetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TotalExpencesstext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(saleryExpencesstext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalexp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(228, Short.MAX_VALUE))
        );

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Recieve"));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Payment Recieved"));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(51, 51, 51));
        jLabel17.setText("Cash Recieve");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(51, 51, 51));
        jLabel18.setText("Bank Recieve");

        creditbankrecievetext.setEditable(false);
        creditbankrecievetext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        creditbankrecievetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        creditbankrecievetext.setText("0");
        creditbankrecievetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        creditbankrecievetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        creditbankrecievetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditbankrecievetextActionPerformed(evt);
            }
        });

        creditcashrecievetext.setEditable(false);
        creditcashrecievetext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        creditcashrecievetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        creditcashrecievetext.setText("0");
        creditcashrecievetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        creditcashrecievetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        creditcashrecievetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditcashrecievetextActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(51, 51, 51));
        jLabel32.setText("Total");

        totalcredittext.setEditable(false);
        totalcredittext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        totalcredittext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalcredittext.setText("0");
        totalcredittext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        totalcredittext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalcredittext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalcredittextActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(51, 51, 51));
        jLabel33.setText("Due");

        invoiceduetext.setEditable(false);
        invoiceduetext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        invoiceduetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        invoiceduetext.setText("0");
        invoiceduetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        invoiceduetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        invoiceduetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceduetextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(9, 9, 9))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(4, 4, 4)))
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(creditcashrecievetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(creditbankrecievetext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalcredittext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11))
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(invoiceduetext)
                        .addGap(9, 9, 9))))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(creditcashrecievetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(creditbankrecievetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalcredittext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(invoiceduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Purchase Payment"));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(51, 51, 51));
        jLabel27.setText("Cash Payment");

        cashpaymenttext.setEditable(false);
        cashpaymenttext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        cashpaymenttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashpaymenttext.setText("0");
        cashpaymenttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cashpaymenttext.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        chequepayment.setEditable(false);
        chequepayment.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        chequepayment.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        chequepayment.setText("0");
        chequepayment.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        chequepayment.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(51, 51, 51));
        jLabel28.setText("Bank Payment");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(51, 51, 51));
        jLabel29.setText("Net Total");

        totalpurchasepaymenttext.setEditable(false);
        totalpurchasepaymenttext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        totalpurchasepaymenttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalpurchasepaymenttext.setText("0");
        totalpurchasepaymenttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        totalpurchasepaymenttext.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(51, 51, 51));
        jLabel30.setText("Due");

        purchaseduetext.setEditable(false);
        purchaseduetext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        purchaseduetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        purchaseduetext.setText("0");
        purchaseduetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        purchaseduetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(chequepayment)
                    .addComponent(cashpaymenttext)
                    .addComponent(totalpurchasepaymenttext)
                    .addComponent(purchaseduetext))
                .addContainerGap())
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashpaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chequepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalpurchasepaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchaseduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Bank And Cash"));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Bank"));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("CashOut");

        bankcashintext.setEditable(false);
        bankcashintext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        bankcashintext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bankcashintext.setText("0");
        bankcashintext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        bankcashintext.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Cashin");

        bankcashouttext.setEditable(false);
        bankcashouttext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        bankcashouttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bankcashouttext.setText("0");
        bankcashouttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        bankcashouttext.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bankcashintext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bankcashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bankcashintext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bankcashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Cash"));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setText("CashOut");

        cashboxcashin.setEditable(false);
        cashboxcashin.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        cashboxcashin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashboxcashin.setText("0");
        cashboxcashin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cashboxcashin.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("Cashin");

        cashboxcashouttext.setEditable(false);
        cashboxcashouttext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        cashboxcashouttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashboxcashouttext.setText("0");
        cashboxcashouttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cashboxcashouttext.setDisabledTextColor(new java.awt.Color(51, 51, 51));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cashboxcashin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashboxcashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cashboxcashin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cashboxcashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Total Cash & Bank(Cashin And Cashout)"));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("Total Cash In");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setText("Total Cash Out");

        totalcashintext.setEditable(false);
        totalcashintext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        totalcashintext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalcashintext.setText("0");
        totalcashintext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        totalcashintext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalcashintext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalcashintextActionPerformed(evt);
            }
        });

        totalcashouttext.setEditable(false);
        totalcashouttext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        totalcashouttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalcashouttext.setText("0");
        totalcashouttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        totalcashouttext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalcashouttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalcashouttextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(totalcashintext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(totalcashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalcashintext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalcashouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Prasent Balance"));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Cash");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(51, 51, 51));
        jLabel35.setText("BanK");

        cashbalancetext.setEditable(false);
        cashbalancetext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        cashbalancetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashbalancetext.setText("0");
        cashbalancetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cashbalancetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        cashbalancetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashbalancetextActionPerformed(evt);
            }
        });

        bankbalancetext.setEditable(false);
        bankbalancetext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        bankbalancetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bankbalancetext.setText("0");
        bankbalancetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        bankbalancetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        bankbalancetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bankbalancetextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(70, 70, 70)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cashbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bankbalancetext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cashbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bankbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Current Date");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        fromdatepayment1.setDateFormatString("yyyy-MM-dd");

        jLabel57.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("Day");

        jButton2.setText("Submit");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel57)
                .addGap(4, 4, 4)
                .addComponent(fromdatepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fromdatepayment1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        monthbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "January", "February", "March", "April", "May", "June", "July", "Agust", "September", "October", "November", "December" }));
        monthbox.setBorder(null);

        yeartext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        yeartext.setForeground(new java.awt.Color(102, 0, 0));
        yeartext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                yeartextKeyPressed(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("Year");

        overalsubmit.setBackground(new java.awt.Color(255, 255, 255));
        overalsubmit.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        overalsubmit.setForeground(new java.awt.Color(51, 51, 51));
        overalsubmit.setText("Submit");
        overalsubmit.setBorder(null);
        overalsubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overalsubmitActionPerformed(evt);
            }
        });

        jLabel59.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("Month");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel59)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(monthbox, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel61)
                .addGap(9, 9, 9)
                .addComponent(yeartext, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(overalsubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(monthbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(overalsubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yeartext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        descriptiontext.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        descriptiontext.setForeground(new java.awt.Color(255, 255, 255));
        descriptiontext.setBorder(null);

        reportbox.setBackground(new java.awt.Color(0, 204, 0));
        reportbox.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        reportbox.setForeground(new java.awt.Color(255, 255, 255));
        reportbox.setText("Print");
        reportbox.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        reportbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportboxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(descriptiontext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(reportbox, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(193, 193, 193))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(reportbox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(3, 3, 3)
                .addComponent(descriptiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1133, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(121, 121, 121))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        setBounds(0, 0, 1145, 517);
    }// </editor-fold>//GEN-END:initComponents

    private void creditbankrecievetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditbankrecievetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_creditbankrecievetextActionPerformed

    private void creditcashrecievetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditcashrecievetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_creditcashrecievetextActionPerformed

    private void totalcredittextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalcredittextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalcredittextActionPerformed

    private void totalcashintextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalcashintextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalcashintextActionPerformed

    private void totalcashouttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalcashouttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalcashouttextActionPerformed

    private void overalsubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overalsubmitActionPerformed

        if (monthbox.getSelectedIndex() == 0 || yeartext.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select Month AND Year");
        } else {
            yeartext.setBackground(Color.WHITE);
            year1 = null;
            date = null;
            month = monthbox.getSelectedIndex();
            year = yeartext.getText();
            dayClose();
            description = "Description:" + "Monthly Search: " + "Month: " + monthbox.getSelectedItem() + " Year: " + yeartext.getText();
            descriptiontext.setText(description);

        }

    }//GEN-LAST:event_overalsubmitActionPerformed

    private void reportboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportboxActionPerformed

        try {

            /* JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/Dayclose.jrxml");*/
            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/DaycloseThermal.jrxml");

            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");
            HashMap para = new HashMap();

            para.put("creditinvoice", TotalInvoicetext.getText());

            para.put("crcasre", creditcashrecievetext.getText());
            para.put("crbancr", creditbankrecievetext.getText());
            para.put("totalcrre", totalcredittext.getText());
            para.put("tocashre", cashpaymenttext.getText());
            para.put("tobanre", chequepayment.getText());
            para.put("totalrecieve", totalpurchasepaymenttext.getText());
            para.put("bankcashout", bankcashintext.getText());
            para.put("bankcashin", bankcashouttext.getText());
            para.put("drwercashin", cashboxcashin.getText());
            para.put("drwercashout", cashboxcashouttext.getText());
            para.put("totlcashin", totalcashintext.getText());
            para.put("totlcashout", totalcashouttext.getText());
            para.put("bankbalanc", bankbalancetext.getText());
            para.put("cashbalance", cashbalancetext.getText());
            para.put("slrexp", saleryExpencesstext.getText());
            para.put("otherexp", TotalExpencesstext.getText());
            para.put("totalexp", totalexp.getText());
            para.put("description", description);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);
        } catch (NumberFormatException | JRException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
    }//GEN-LAST:event_reportboxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        yeartext.setText(null);
        year1 = null;
        ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).setText(null);
        month = 0;
        year = null;
        yeartext.setBackground(Color.WHITE);
        currentDate();
        dayClose();
        description = "Description:" + " Current Date: " + date;
        descriptiontext.setText(description);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //String dates = ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).getText();
        yeartext.setText(null);
        monthbox.setSelectedIndex(0);
        month = 0;
        year = null;
        year1 = null;
        yeartext.setBackground(Color.WHITE);
        date = new java.sql.Date(fromdatepayment1.getDate().getTime());

        dayClose();
        description = "Description:" + " Day Search: " + date;
        descriptiontext.setText(description);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void yeartextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_yeartextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            yeartext.setBackground(Color.YELLOW);
            monthbox.setSelectedIndex(0);
            month = 0;
            year = null;
            ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).setText(null);
            date = null;
            year1 = yeartext.getText();
            dayClose();
            description = "Description:" + " Yearly Search: " + "Year: " + yeartext.getText();
            descriptiontext.setText(description);

        }
    }//GEN-LAST:event_yeartextKeyPressed

    private void cashbalancetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashbalancetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cashbalancetextActionPerformed

    private void bankbalancetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bankbalancetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bankbalancetextActionPerformed

    private void invoiceduetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceduetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_invoiceduetextActionPerformed


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
    private javax.swing.JLabel descriptiontext;
    private com.toedter.calendar.JDateChooser fromdatepayment1;
    private javax.swing.JTextField invoiceduetext;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JComboBox<String> monthbox;
    private javax.swing.JButton overalsubmit;
    private javax.swing.JTextField purchaseduetext;
    private javax.swing.JTextField purchasetext;
    private javax.swing.JButton reportbox;
    private javax.swing.JTextField saleryExpencesstext;
    private javax.swing.JTextField totalcashintext;
    private javax.swing.JTextField totalcashouttext;
    private javax.swing.JTextField totalcredittext;
    private javax.swing.JTextField totalexp;
    private javax.swing.JTextField totalpurchasepaymenttext;
    private javax.swing.JTextField yeartext;
    // End of variables declaration//GEN-END:variables
}
