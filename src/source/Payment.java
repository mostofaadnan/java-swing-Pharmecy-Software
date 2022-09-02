/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import java.io.File;
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
import java.util.Map;
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
import static source.PurchasePaymentEntry.convertToIndianCurrency;

/**
 *
 * @author adnan
 */
public final class Payment extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String trnNo = null;
    String paymenttype = null;
    String bankname = null;
    java.sql.Date date;
    String description = null;
    double pamount = 0;

    /**
     * Creates new form ParchsePayment
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public Payment() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        currentDate();
        table_updateh_today();
        // table_updateh();

    }

    private float total_action_amount() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 6).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_paid() {

        int rowaCount = datatbl.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 4).toString());
        }
        return (float) totaltpmrp;

    }

    private float total_action_due() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 9).toString());
        }

        return (float) totaltpmrp;

    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        //  inputdate.setDate(date);

    }

    public void table_updateh_today() throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select "
                    + "TR_No,"
                    + "(select supliyername from suplyierinfo sp where sp.id=par.supplierid)  as 'Suppliyer',"
                    + "DATE_FORMAT(par.paymentdate, '%d-%m-%Y')  'PaymentDate',"
                    + "paidamount,"
                    + "paymenttype,"
                    + "bank,"
                    + "accno,"
                    + "chaqueno,"
                    + "(select name from admin ad where ad.id=par.inputuser) as 'inputusername' "
                    + "from purchasepaymenthistory par where par.paymentdate='" + date + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String transactionno = rs.getString("TR_No");
                String PaymentDate = rs.getString("PaymentDate");
                String suppliyer = rs.getString("Suppliyer");
                double PaidAmount = rs.getDouble("paidamount");
                String PaymentType = rs.getString("paymenttype");
                String AccNo = rs.getString("accno");
                String ChequeNo = rs.getString("chaqueno");
                String Bank = rs.getString("bank");
                String UserName = rs.getString("inputusername");
                tree++;
                model2.addRow(new Object[]{tree, transactionno, PaymentDate, suppliyer, PaidAmount, PaymentType, AccNo, ChequeNo, Bank, UserName});

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        //totalamttext.setText(Float.toString(total_action_amount()));
        paidamounttext.setText(Float.toString(total_action_paid()));
        //float amount=total_action_amount();
        //float paid=total_action_paid();
        //float due=amount-paid;
        //String dueformat=df2.format(due);
        //duetext.setText(dueformat);

        description = "Description: (Search By Current Date) Date " + date;
        descriptiontext.setText(description);
    }

    public void table_updateh() throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select "
                    + "TR_No,"
                    + "(select supliyername from suplyierinfo sp where sp.id=par.supplierid)  as 'Suppliyer',"
                    + "DATE_FORMAT(par.paymentdate, '%d-%m-%Y')  'PaymentDate',"
                    + "paidamount,"
                    + "paymenttype,"
                    + "bank,"
                    + "accno,"
                    + "chaqueno,"
                    + "(select name from admin ad where ad.id=par.inputuser) as 'inputusername' "
                    + "from purchasepaymenthistory par";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String transactionno = rs.getString("TR_No");
                String PaymentDate = rs.getString("PaymentDate");
                String suppliyer = rs.getString("Suppliyer");
                double PaidAmount = rs.getDouble("paidamount");
                String PaymentType = rs.getString("paymenttype");
                String AccNo = rs.getString("accno");
                String ChequeNo = rs.getString("chaqueno");
                String Bank = rs.getString("bank");
                String UserName = rs.getString("inputusername");
                tree++;
                model2.addRow(new Object[]{tree, transactionno, PaymentDate, suppliyer, PaidAmount, PaymentType, AccNo, ChequeNo, Bank, UserName});

                description = "Description: (Search ALL Data) Status: ALL";
                descriptiontext.setText(description);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        //totalamttext.setText(Float.toString(total_action_amount()));
        paidamounttext.setText(Float.toString(total_action_paid()));
        //  float amount=total_action_amount();
        //float paid=total_action_paid();
        //float due=amount-paid;
        //String dueformat=df2.format(due);
        //duetext.setText(dueformat);
    }
    private static final DecimalFormat df2 = new DecimalFormat("#.00");

    private void printInvoice() throws JRException {
        try {
            double amount = pamount;
            String amounts = df2.format(amount);
            String inwords = convertToIndianCurrency(amounts);
            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/paymentvoucher.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();
            para.put("transactionno", trnNo);
            para.put("inword", inwords);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);

        } catch (NumberFormatException | JRException ex) {
            JOptionPane.showMessageDialog(null, ex);

        }

    }

    private void Transearch() {

        String SearchText = transearch.getText();
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {

            String sql = "Select "
                    + "TR_No,"
                    + "(select supliyername from suplyierinfo sp where sp.id=par.supplierid)  as 'Suppliyer',"
                    + "DATE_FORMAT(par.paymentdate, '%d-%m-%Y')  'PaymentDate',"
                    + "paidamount,"
                    + "paymenttype,"
                    + "bank,"
                    + "accno,"
                    + "chaqueno,"
                    + "(select name from admin ad where ad.id=par.inputuser) as 'inputusername' "
                    + "from purchasepaymenthistory par where  par.TR_No='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String transactionno = rs.getString("TR_No");
                String PaymentDate = rs.getString("PaymentDate");
                String suppliyer = rs.getString("Suppliyer");
                double PaidAmount = rs.getDouble("paidamount");
                String PaymentType = rs.getString("paymenttype");
                String AccNo = rs.getString("accno");
                String ChequeNo = rs.getString("chaqueno");
                String Bank = rs.getString("bank");
                String UserName = rs.getString("inputusername");
                tree++;
                model2.addRow(new Object[]{tree, transactionno, PaymentDate, suppliyer, PaidAmount, PaymentType, AccNo, ChequeNo, Bank, UserName});

                transearch.setBackground(Color.YELLOW);
                description = "Description: (Search By Transaction No) Transaction No:" + transearch.getText();
                descriptiontext.setText(description);

            } else {
                transearch.setBackground(Color.WHITE);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        //   totalamttext.setText(Float.toString(total_action_amount()));
        paidamounttext.setText(Float.toString(total_action_paid()));
        // float amount=total_action_amount();
        //float paid=total_action_paid();
        //float due=amount-paid;
        ///String dueformat=df2.format(due);
        //duetext.setText(dueformat);
    }

    private void paymentByDate() {

        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {

            String sql = "Select "
                    + "TR_No,"
                    + "(select supliyername from suplyierinfo sp where sp.id=par.supplierid)  as 'Suppliyer',"
                    + "DATE_FORMAT(par.paymentdate, '%d-%m-%Y')  'PaymentDate',"
                    + "paidamount,"
                    + "paymenttype,"
                    + "bank,"
                    + "accno,"
                    + "chaqueno,"
                    + "(select name from admin ad where ad.id=par.inputuser) as 'inputusername' "
                    + "from purchasepaymenthistory par  where par.paymentdate BETWEEN ? AND ? OR year(par.paymentdate)='" + yeartext.getText() + "'";

            pst = conn.prepareStatement(sql);
            pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
            pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                String transactionno = rs.getString("TR_No");
                String PaymentDate = rs.getString("PaymentDate");
                String suppliyer = rs.getString("Suppliyer");
                double PaidAmount = rs.getDouble("paidamount");
                String PaymentType = rs.getString("paymenttype");
                String AccNo = rs.getString("accno");
                String ChequeNo = rs.getString("chaqueno");
                String Bank = rs.getString("bank");
                String UserName = rs.getString("inputusername");
                tree++;
                model2.addRow(new Object[]{tree, transactionno, PaymentDate, suppliyer, PaidAmount, PaymentType, AccNo, ChequeNo, Bank, UserName});

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        paidamounttext.setText(Float.toString(total_action_paid()));

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
        view = new javax.swing.JMenuItem();
        print = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        todatepayment = new com.toedter.calendar.JDateChooser();
        fromdatepayment = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        yeartext = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        descriptiontext = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        transearch = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel4 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        suppliyetext1 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        parchaseBox1 = new javax.swing.JComboBox<>();
        grntext1 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        totalmttext1 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        openingdate1 = new com.toedter.calendar.JDateChooser();
        jLabel25 = new javax.swing.JLabel();
        duetext1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        paidamounttext1 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        balanceduetext1 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        remarktext1 = new javax.swing.JTextArea();
        jLabel29 = new javax.swing.JLabel();
        paymenttypebox1 = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        ppayemtnid1 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        transectiontext1 = new javax.swing.JTextField();
        paymentsbox1 = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        transearch1 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        yeartext1 = new javax.swing.JTextField();
        todatepayment1 = new com.toedter.calendar.JDateChooser();
        fromdatepayment1 = new com.toedter.calendar.JDateChooser();
        jLabel34 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        datatbl1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        paidamounttext = new javax.swing.JLabel();

        tablemenu.setPreferredSize(new java.awt.Dimension(300, 90));

        view.setText("View");
        view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewActionPerformed(evt);
            }
        });
        tablemenu.add(view);

        print.setText("Print Voucher");
        print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printActionPerformed(evt);
            }
        });
        tablemenu.add(print);

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Parchase Payment List");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));

        jPanel3.setBackground(new java.awt.Color(67, 86, 86));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("From");

        jButton5.setBackground(new java.awt.Color(0, 102, 0));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Submit");
        jButton5.setBorder(null);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        todatepayment.setDateFormatString("yyyy-MM-dd");

        fromdatepayment.setDateFormatString("yyyy-MM-dd");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("To");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Year");

        yeartext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        yeartext.setForeground(new java.awt.Color(102, 0, 0));
        yeartext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                yeartextKeyPressed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(0, 204, 0));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Report");
        jButton6.setBorder(null);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fromdatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(todatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(yeartext, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel17))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel15)
                                .addComponent(jLabel16))
                            .addGap(1, 1, 1)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(fromdatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(todatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel17)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(yeartext, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("Load");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setText("Today");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(153, 51, 0));
        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Print Data Table");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        descriptiontext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        descriptiontext.setForeground(new java.awt.Color(255, 255, 0));

        jPanel7.setBackground(new java.awt.Color(67, 86, 86));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Transaction No");

        transearch.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        transearch.setForeground(new java.awt.Color(51, 51, 51));
        transearch.setBorder(null);
        transearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                transearchKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transearch, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(transearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("New Payment");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(descriptiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(130, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(descriptiontext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(5, 5, 5))
        );

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL No", "Tr No", "Payment Date", "Supplier", "Paid Amount", "Payment Type", "Acc No", "Cheque No", "Bank", "Input User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        datatbl.setGridColor(new java.awt.Color(204, 204, 204));
        datatbl.setRowHeight(25);
        datatbl.setShowHorizontalLines(true);
        datatbl.setShowVerticalLines(true);
        datatbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setResizable(false);
            datatbl.getColumnModel().getColumn(0).setPreferredWidth(30);
            datatbl.getColumnModel().getColumn(1).setResizable(false);
            datatbl.getColumnModel().getColumn(2).setResizable(false);
            datatbl.getColumnModel().getColumn(3).setResizable(false);
            datatbl.getColumnModel().getColumn(3).setPreferredWidth(200);
            datatbl.getColumnModel().getColumn(4).setResizable(false);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
            datatbl.getColumnModel().getColumn(8).setResizable(false);
            datatbl.getColumnModel().getColumn(9).setResizable(false);
        }

        jInternalFrame1.setBackground(new java.awt.Color(255, 255, 255));
        jInternalFrame1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jInternalFrame1.setClosable(true);
        jInternalFrame1.setIconifiable(true);
        jInternalFrame1.setMaximizable(true);
        jInternalFrame1.setResizable(true);
        jInternalFrame1.setTitle("Parchase Payment");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 0)));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 102, 51));
        jLabel18.setText("Payment Section");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 51, 0));
        jLabel19.setText("Payment Date");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 102, 0));
        jLabel20.setText("Purchase No");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 51, 0));
        jLabel21.setText("GRN No");

        suppliyetext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        suppliyetext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        suppliyetext1.setEnabled(false);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setText("Suppliyer Name");

        parchaseBox1.setEditable(true);
        parchaseBox1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        parchaseBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        parchaseBox1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                parchaseBox1PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        parchaseBox1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                parchaseBox1PropertyChange(evt);
            }
        });

        grntext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        grntext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        grntext1.setEnabled(false);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 51, 0));
        jLabel23.setText("Total Amount");

        totalmttext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalmttext1.setForeground(new java.awt.Color(204, 51, 0));
        totalmttext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalmttext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalmttext1.setEnabled(false);

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 51, 0));
        jLabel24.setText("Transaction No");

        openingdate1.setForeground(new java.awt.Color(102, 102, 102));
        openingdate1.setDateFormatString("yyyy-MM-dd");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 51, 0));
        jLabel25.setText("Due");

        duetext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        duetext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        duetext1.setText("0");
        duetext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        duetext1.setEnabled(false);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 51, 0));
        jLabel26.setText("Paid Amount");

        paidamounttext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        paidamounttext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paidamounttext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                paidamounttext1KeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                paidamounttext1KeyTyped(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 51, 0));
        jLabel27.setText("Balance Due");

        balanceduetext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        balanceduetext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        balanceduetext1.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        balanceduetext1.setEnabled(false);

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 51, 0));
        jLabel28.setText("Remark");

        remarktext1.setColumns(20);
        remarktext1.setRows(5);
        jScrollPane3.setViewportView(remarktext1);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 51, 0));
        jLabel29.setText("Payment Type");

        paymenttypebox1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        paymenttypebox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Cheque" }));

        jButton4.setBackground(new java.awt.Color(255, 51, 0));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Execute");
        jButton4.setBorder(null);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(0, 102, 0));
        jButton7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Clear");
        jButton7.setBorder(null);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(102, 0, 0));
        jButton8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Delete");
        jButton8.setBorder(null);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 51, 0));
        jLabel30.setText("Payment Status");

        transectiontext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        transectiontext1.setForeground(new java.awt.Color(51, 153, 0));
        transectiontext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        transectiontext1.setEnabled(false);

        paymentsbox1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        paymentsbox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Pending", "Due Remain" }));
        paymentsbox1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                paymentsbox1PopupMenuWillBecomeInvisible(evt);
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
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ppayemtnid1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(parchaseBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(transectiontext1)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(openingdate1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(paymentsbox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(grntext1)
                            .addComponent(suppliyetext1)
                            .addComponent(totalmttext1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(duetext1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(paidamounttext1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(balanceduetext1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3)
                            .addComponent(paymenttypebox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 5, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(ppayemtnid1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(openingdate1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transectiontext1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymentsbox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(parchaseBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(grntext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(suppliyetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(totalmttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(duetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymenttypebox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(paidamounttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(balanceduetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(67, 86, 86));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Transaction No");

        transearch1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        transearch1.setForeground(new java.awt.Color(0, 102, 0));
        transearch1.setBorder(null);
        transearch1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                transearch1KeyReleased(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(67, 86, 86));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Year");

        jButton9.setBackground(new java.awt.Color(0, 204, 0));
        jButton9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Report");
        jButton9.setBorder(null);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("From");

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

        yeartext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        yeartext1.setForeground(new java.awt.Color(102, 0, 0));

        todatepayment1.setDateFormatString("yyyy-MM-dd");

        fromdatepayment1.setDateFormatString("yyyy-MM-dd");

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("To");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fromdatepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(todatepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(yeartext1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel32))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34)
                    .addComponent(jLabel32))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fromdatepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(todatepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yeartext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(transearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        datatbl1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        datatbl1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        datatbl1.setGridColor(new java.awt.Color(204, 204, 204));
        datatbl1.setRowHeight(30);
        datatbl1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatbl1MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(datatbl1);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Total Payment ");

        paidamounttext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        paidamounttext.setForeground(new java.awt.Color(255, 255, 255));
        paidamounttext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        paidamounttext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(2, 2, 2)
                .addComponent(paidamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paidamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1324, Short.MAX_VALUE))
                .addGap(0, 0, 0))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 662, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 662, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 274, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 274, Short.MAX_VALUE)))
        );

        setBounds(10, 5, 1340, 577);
    }// </editor-fold>//GEN-END:initComponents

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked

        int row = datatbl.getSelectedRow();
        trnNo = (datatbl.getModel().getValueAt(row, 1).toString());
        pamount = (double) (datatbl.getModel().getValueAt(row, 4));
        if (SwingUtilities.isRightMouseButton(evt)) {
            tablemenu.show(evt.getComponent(), evt.getX() + 1, evt.getY() + 1);

        }

    }//GEN-LAST:event_datatblMouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText().isEmpty() || ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select From And To date Value");
        } else {
            yeartext.setText(null);
            String descriptionhere = null;
            paymentByDate();
            String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
            String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();
            descriptionhere = "Description:Date Between From: " + fromdate + " To: " + todate;
            descriptiontext.setText(descriptionhere);
            description = descriptionhere;
            yeartext.setBackground(Color.WHITE);
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        if (((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText().isEmpty() || ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Complete Requirement Field");

        } else {
            try {
                String sql = "Select "
                        + "TR_No,"
                        + "(select supliyername from suplyierinfo sp where sp.id=par.supplierid)  as 'Suppliyer',"
                        + "DATE_FORMAT(par.paymentdate, '%d-%m-%Y')  'PaymentDate',"
                        + "paidamount,"
                        + "paymenttype,"
                        + "bank,"
                        + "accno,"
                        + "chaqueno,"
                        + "(select name from admin ad where ad.id=par.inputuser) as 'inputusername' "
                        + "from purchasepaymenthistory par  where par.paymentdate BETWEEN ? AND ? OR year(par.paymentdate)='" + yeartext.getText() + "'";

                pst = conn.prepareStatement(sql);
                pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
                pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
                rs = pst.executeQuery();

                while (rs.next()) {
                    String transactionno = rs.getString("TR_No");
                    String PaymentDate = rs.getString("PaymentDate");
                    String suppliyer = rs.getString("Suppliyer");
                    double PaidAmount = rs.getDouble("paidamount");
                    String PaymentType = rs.getString("paymenttype");
                    String AccNo = rs.getString("accno");
                    String ChequeNo = rs.getString("chaqueno");
                    String Bank = rs.getString("bank");
                    String UserName = rs.getString("inputusername");
                    tree++;
                    model2.addRow(new Object[]{tree, transactionno, PaymentDate, suppliyer, PaidAmount, PaymentType, AccNo, ChequeNo, Bank, UserName});

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

            try {

                String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
                String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/Paymentreport.jrxml");
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

        }


    }//GEN-LAST:event_jButton6ActionPerformed

    private void parchaseBox1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_parchaseBox1PopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_parchaseBox1PopupMenuWillBecomeInvisible

    private void parchaseBox1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_parchaseBox1PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_parchaseBox1PropertyChange

    private void paidamounttext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttext1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_paidamounttext1KeyPressed

    private void paidamounttext1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttext1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_paidamounttext1KeyTyped

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void paymentsbox1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_paymentsbox1PopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_paymentsbox1PopupMenuWillBecomeInvisible

    private void transearch1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_transearch1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_transearch1KeyReleased

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void datatbl1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatbl1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_datatbl1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        PurchasePaymentEntry purchasepayment = null;

        try {
            purchasepayment = new PurchasePaymentEntry();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }

        purchasepayment.setVisible(true);
        this.getDesktopPane().add(purchasepayment);

        //Homedesktop.add(It);
        purchasepayment.setVisible(true);

        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = purchasepayment.getSize();
        purchasepayment.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        purchasepayment.moveToFront();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewActionPerformed
        Purchasepayment purchasepayment = null;

        try {
            purchasepayment = new Purchasepayment(trnNo);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }

        purchasepayment.setVisible(true);
        this.getDesktopPane().add(purchasepayment);

        //Homedesktop.add(It);
        purchasepayment.setVisible(true);

        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = purchasepayment.getSize();
        purchasepayment.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        purchasepayment.moveToFront();
    }//GEN-LAST:event_viewActionPerformed

    private void printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printActionPerformed
        try {
            printInvoice();
        } catch (JRException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_printActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            table_updateh();
        } catch (SQLException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            table_updateh_today();
        } catch (SQLException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void yeartextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_yeartextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else if (yeartext.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select Year Value");
        } else {
            ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).setText(null);
            ((JTextField) todatepayment.getDateEditor().getUiComponent()).setText(null);
            yeartext.setBackground(Color.YELLOW);
            String descriptionhere = "Description:Year: " + yeartext.getText();
            descriptiontext.setText(descriptionhere);
            paymentByDate();
            description = descriptionhere;
        }
    }//GEN-LAST:event_yeartextKeyPressed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        if (model2.getRowCount() == 0) {

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {

            try {
                //  String totalamount = totalamttext.getText();
                String totalpaid = paidamounttext.getText();
                //String totaldue = duetext.getText();

                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/PaymentDetails.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDataSource", ItemJRBean);
                para.put("description", description);

                //   para.put("totalamount", totalamount);
                para.put("totalpaid", totalpaid);
                //  para.put("totaldue", totaldue);
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }

    }//GEN-LAST:event_jButton12ActionPerformed

    private void transearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_transearchKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            if (transearch.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Pleae give Transaction No");

            } else {
                Transearch();

            }

        }
    }//GEN-LAST:event_transearchKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField balanceduetext1;
    private javax.swing.JTable datatbl;
    private javax.swing.JTable datatbl1;
    private javax.swing.JLabel descriptiontext;
    private javax.swing.JTextField duetext1;
    private com.toedter.calendar.JDateChooser fromdatepayment;
    private com.toedter.calendar.JDateChooser fromdatepayment1;
    private javax.swing.JTextField grntext1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private com.toedter.calendar.JDateChooser openingdate1;
    private javax.swing.JLabel paidamounttext;
    private javax.swing.JTextField paidamounttext1;
    private javax.swing.JComboBox<String> parchaseBox1;
    private javax.swing.JComboBox<String> paymentsbox1;
    private javax.swing.JComboBox<String> paymenttypebox1;
    private javax.swing.JLabel ppayemtnid1;
    private javax.swing.JMenuItem print;
    private javax.swing.JTextArea remarktext1;
    private javax.swing.JTextField suppliyetext1;
    private javax.swing.JPopupMenu tablemenu;
    private com.toedter.calendar.JDateChooser todatepayment;
    private com.toedter.calendar.JDateChooser todatepayment1;
    private javax.swing.JTextField totalmttext1;
    private javax.swing.JTextField transearch;
    private javax.swing.JTextField transearch1;
    private javax.swing.JTextField transectiontext1;
    private javax.swing.JMenuItem view;
    private javax.swing.JTextField yeartext;
    private javax.swing.JTextField yeartext1;
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
