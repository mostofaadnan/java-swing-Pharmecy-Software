/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import com.toedter.calendar.JCalendar;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
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

/**
 *
 * @author Jasim
 */
public class IncomeExpenss extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int year;
    int month;
    String description = null;
    private static final DecimalFormat df2 = new DecimalFormat("#.00");

    /**
     * Creates new form IncomeExpenss
     *
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public IncomeExpenss() throws IOException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        TableDesign();
        TableDesignone();
        TableDesigntwo();
        // table_update();
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

    private void TableDesignone() {
        JTableHeader jtblheader = datatbl1.getTableHeader();
        jtblheader.setBackground(Color.red);
        jtblheader.setForeground(Color.BLACK);
        jtblheader.setFont(new Font("Times New Roman", Font.BOLD, 16));
        ((DefaultTableCellRenderer) jtblheader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        datatbl1.setDefaultRenderer(Object.class, centerRenderer);
    }

    private void TableDesigntwo() {
        JTableHeader jtblheader = datatbl2.getTableHeader();
        jtblheader.setBackground(Color.red);
        jtblheader.setForeground(Color.BLACK);
        jtblheader.setFont(new Font("Times New Roman", Font.BOLD, 16));
        ((DefaultTableCellRenderer) jtblheader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        datatbl2.setDefaultRenderer(Object.class, centerRenderer);
    }

    private void getMonthYera() {
        month = monthbox.getMonth() + 1;
        year = yearbox.getYear();
    }

    private void table_update() throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        DefaultTableModel model3 = (DefaultTableModel) datatbl1.getModel();
        model3.setRowCount(0);
        DefaultTableModel model4 = (DefaultTableModel) datatbl2.getModel();
        model4.setRowCount(0);
        int tree = 0;
        int tree1 = 0;
        int tree2 = 0;

        try {
            //String sql = "SELECT c.invoicedate as 'date',SUM(c.Totalinvoice) as 'cashamount',SUM(cr.Totalinvoice) as 'creditamount' FROM cashsale c INNER JOIN sale cr ON c.invoicedate=cr.invoicedate JOIN Expencess ex ON ex.inputdate=c.invoicedate WHERE c.invoicedate BETWEEN ? AND ? GROUP BY c.invoicedate, cr.invoicedate ";
            String sql = "Select invoicedate as 'date',sum(Totalinvoice) as 'cashamount' from cashsale Group by invoicedate";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {
                String date = rs.getString("date");
                float CashF=rs.getFloat("cashamount");
                String cashamt = df2.format(CashF);
               
                tree++;
                model2.addRow(new Object[]{tree, date, cashamt});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {
            //String sql = "SELECT c.invoicedate as 'date',SUM(c.Totalinvoice) as 'cashamount',SUM(cr.Totalinvoice) as 'creditamount' FROM cashsale c INNER JOIN sale cr ON c.invoicedate=cr.invoicedate JOIN Expencess ex ON ex.inputdate=c.invoicedate WHERE c.invoicedate BETWEEN ? AND ? GROUP BY c.invoicedate, cr.invoicedate ";
            String sql = "Select invoicedate as 'date',sum(Totalinvoice) as 'cashamount' from sale  Group by invoicedate";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {
                String date = rs.getString("date");
               float CashF=rs.getFloat("cashamount");
                String cashamt = df2.format(CashF);
                tree1++;
                model3.addRow(new Object[]{tree1, date, cashamt});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        try {
            //String sql = "SELECT c.invoicedate as 'date',SUM(c.Totalinvoice) as 'cashamount',SUM(cr.Totalinvoice) as 'creditamount' FROM cashsale c INNER JOIN sale cr ON c.invoicedate=cr.invoicedate JOIN Expencess ex ON ex.inputdate=c.invoicedate WHERE c.invoicedate BETWEEN ? AND ? GROUP BY c.invoicedate, cr.invoicedate ";
            String sql = "Select inputdate as 'date',sum(amount) as 'cashamount' from Expencess  Group by inputdate";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {
                String date = rs.getString("date");
                float CashF=rs.getFloat("cashamount");
                String cashamt = df2.format(CashF);
                tree2++;
                model4.addRow(new Object[]{tree2, date, cashamt});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        totalaction();
        description = "All Data Description";
    }

    private void ByDateCash() throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
        String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();
        try {
            //String sql = "SELECT c.invoicedate as 'date',SUM(c.Totalinvoice) as 'cashamount',SUM(cr.Totalinvoice) as 'creditamount' FROM cashsale c INNER JOIN sale cr ON c.invoicedate=cr.invoicedate JOIN Expencess ex ON ex.inputdate=c.invoicedate WHERE c.invoicedate BETWEEN ? AND ? GROUP BY c.invoicedate, cr.invoicedate ";
            String sql = "Select invoicedate as 'date',sum(Totalinvoice) as 'cashamount' from cashsale where invoicedate BETWEEN '" + fromdate + "' AND  '" + todate + "' Group by invoicedate";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {
                String date = rs.getString("date");
                float CashF=rs.getFloat("cashamount");
                String cashamt = df2.format(CashF);
                tree++;
                model2.addRow(new Object[]{tree, date, cashamt});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void ByDateCredit() throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
        model2.setRowCount(0);
        int tree = 0;
        String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
        String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();
        try {
            //String sql = "SELECT c.invoicedate as 'date',SUM(c.Totalinvoice) as 'cashamount',SUM(cr.Totalinvoice) as 'creditamount' FROM cashsale c INNER JOIN sale cr ON c.invoicedate=cr.invoicedate JOIN Expencess ex ON ex.inputdate=c.invoicedate WHERE c.invoicedate BETWEEN ? AND ? GROUP BY c.invoicedate, cr.invoicedate ";
            String sql = "Select invoicedate as 'date',sum(Totalinvoice) as 'cashamount' from sale where invoicedate BETWEEN '" + fromdate + "' AND  '" + todate + "' Group by invoicedate";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {
                String date = rs.getString("date");
                float CashF=rs.getFloat("cashamount");
                String cashamt = df2.format(CashF);
                tree++;
                model2.addRow(new Object[]{tree, date, cashamt});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void ByDateExpens() throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) datatbl2.getModel();
        model2.setRowCount(0);
        int tree = 0;
        String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
        String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();
        try {
            //String sql = "SELECT c.invoicedate as 'date',SUM(c.Totalinvoice) as 'cashamount',SUM(cr.Totalinvoice) as 'creditamount' FROM cashsale c INNER JOIN sale cr ON c.invoicedate=cr.invoicedate JOIN Expencess ex ON ex.inputdate=c.invoicedate WHERE c.invoicedate BETWEEN ? AND ? GROUP BY c.invoicedate, cr.invoicedate ";
            String sql = "Select inputdate as 'date',sum(amount) as 'cashamount' from Expencess where inputdate BETWEEN '" + fromdate + "' AND  '" + todate + "' Group by inputdate";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {
                String date = rs.getString("date");
                float CashF=rs.getFloat("cashamount");
                String cashamt = df2.format(CashF);
                tree++;
                model2.addRow(new Object[]{tree, date, cashamt});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private float total_action_cashinvoice() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 2).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_creditnvoice() {

        int rowaCount = datatbl1.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl1.getValueAt(i, 2).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_exp() {

        int rowaCount = datatbl2.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl2.getValueAt(i, 2).toString());
        }

        return (float) totaltpmrp;

    }

    private void totalaction() {
        float cashinv = total_action_cashinvoice();
        cashtext.setText(df2.format(cashinv));
        float credit = total_action_creditnvoice();
        credittex.setText(df2.format(credit));
        float exp = total_action_exp();
        exptext.setText(df2.format(exp));
        float totalinv = cashinv + credit;
        totalinvtext.setText(df2.format(totalinv));
        float profit = totalinv - exp;
        profittext.setText(df2.format(profit));

    }

    private void bymonthCash() throws SQLException {
        getMonthYera();
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        DefaultTableModel model3 = (DefaultTableModel) datatbl1.getModel();
        model3.setRowCount(0);
        DefaultTableModel model4 = (DefaultTableModel) datatbl2.getModel();
        model4.setRowCount(0);
        int tree = 0;
        int tree1 = 0;
        int tree2 = 0;

        try {
            //String sql = "SELECT c.invoicedate as 'date',SUM(c.Totalinvoice) as 'cashamount',SUM(cr.Totalinvoice) as 'creditamount' FROM cashsale c INNER JOIN sale cr ON c.invoicedate=cr.invoicedate JOIN Expencess ex ON ex.inputdate=c.invoicedate WHERE c.invoicedate BETWEEN ? AND ? GROUP BY c.invoicedate, cr.invoicedate ";
            String sql = "Select invoicedate as 'date',sum(Totalinvoice) as 'cashamount' from cashsale where month(invoicedate)='" + month + "' AND year(invoicedate)='" + year + "' Group by invoicedate";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {
                String date = rs.getString("date");
                float CashF=rs.getFloat("cashamount");
                String cashamt = df2.format(CashF);
                tree++;
                model2.addRow(new Object[]{tree, date, cashamt});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {
            //String sql = "SELECT c.invoicedate as 'date',SUM(c.Totalinvoice) as 'cashamount',SUM(cr.Totalinvoice) as 'creditamount' FROM cashsale c INNER JOIN sale cr ON c.invoicedate=cr.invoicedate JOIN Expencess ex ON ex.inputdate=c.invoicedate WHERE c.invoicedate BETWEEN ? AND ? GROUP BY c.invoicedate, cr.invoicedate ";
            String sql = "Select invoicedate as 'date',sum(Totalinvoice) as 'cashamount' from sale where month(invoicedate)='" + month + "' AND year(invoicedate)='" + year + "' Group by invoicedate";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {
                String date = rs.getString("date");
               float CashF=rs.getFloat("cashamount");
                String cashamt = df2.format(CashF);
                tree1++;
                model3.addRow(new Object[]{tree1, date, cashamt});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        try {
            //String sql = "SELECT c.invoicedate as 'date',SUM(c.Totalinvoice) as 'cashamount',SUM(cr.Totalinvoice) as 'creditamount' FROM cashsale c INNER JOIN sale cr ON c.invoicedate=cr.invoicedate JOIN Expencess ex ON ex.inputdate=c.invoicedate WHERE c.invoicedate BETWEEN ? AND ? GROUP BY c.invoicedate, cr.invoicedate ";
            String sql = "Select inputdate as 'date',sum(amount) as 'cashamount' from Expencess where month(inputdate)='" + month + "' AND year(inputdate)='" + year + "' Group by inputdate";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {
                String date = rs.getString("date");
                float CashF=rs.getFloat("cashamount");
                String cashamt = df2.format(CashF);
                tree2++;
                model4.addRow(new Object[]{tree2, date, cashamt});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        totalaction();
    }
    /*  private void ByDate() throws SQLException {
     DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
     model2.setRowCount(0);
     int tree = 0;

     try {
     String sql = "SELECT c.invoicedate as 'date',SUM(c.Totalinvoice) as 'cashamount' from cashsale c WHERE c.invoicedate BETWEEN ? AND ? GROUP BY c.invoicedate";
     pst = conn.prepareStatement(sql);
     pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
     pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
     rs = pst.executeQuery();
     //datatbl.setModel(DbUtils.resultSetToTableModel(rs));

     while (rs.next()) {
     String date = rs.getString("date");
     String cashamt = rs.getString("cashamount");
     String creditamt = "";
     String expamount = "";

     tree++;
     model2.addRow(new Object[]{tree, date, cashamt, creditamt, expamount});

     }
     } catch (Exception e) {
     JOptionPane.showMessageDialog(null, e);

     }

     }
    
     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        todatepayment = new com.toedter.calendar.JDateChooser();
        fromdatepayment = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        monthbox = new com.toedter.calendar.JMonthChooser();
        yearbox = new com.toedter.calendar.JYearChooser();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        cashtext = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        credittex = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        exptext = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        totalinvtext = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        profittext = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        datatbl1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        datatbl2 = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setTitle("Income & Expensess");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jPanel7.setBackground(new java.awt.Color(0, 51, 51));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("To");

        todatepayment.setDateFormatString("yyyy-MM-dd");

        fromdatepayment.setDateFormatString("yyyy-MM-dd");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("From");

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(102, 102, 102));
        jButton5.setText("Submit");
        jButton5.setBorder(null);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fromdatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(1, 1, 1)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(todatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel13))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12))
                .addGap(2, 2, 2)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(todatepayment, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(fromdatepayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(102, 102, 102));
        jButton6.setText("Statement Print");
        jButton6.setBorder(null);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(0, 51, 51));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Month");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Year");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(monthbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(yearbox, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel9))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(yearbox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(monthbox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("Load ALL");
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
                .addGap(5, 5, 5)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL No", "Input Date", "Invoice Sale"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        datatbl.setRowHeight(25);
        jScrollPane1.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setResizable(false);
            datatbl.getColumnModel().getColumn(0).setPreferredWidth(30);
            datatbl.getColumnModel().getColumn(1).setResizable(false);
            datatbl.getColumnModel().getColumn(2).setResizable(false);
        }

        jPanel2.setBackground(new java.awt.Color(0, 51, 51));

        cashtext.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        cashtext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Cash Invoice");

        credittex.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        credittex.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        credittex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                credittexActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Credit Invoice");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("TotalExpensess");

        exptext.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        exptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Total Invoice");

        totalinvtext.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        totalinvtext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalinvtext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalinvtextActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Profit");

        profittext.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        profittext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cashtext, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(credittex, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalinvtext, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(exptext, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(profittext, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addGap(3, 3, 3)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(totalinvtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(exptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(cashtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(credittex, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(profittext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel10))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Cash Invoice");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Credit Invoice");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        datatbl1.setAutoCreateRowSorter(true);
        datatbl1.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        datatbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL No", "Input Date", "Credit Sale"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        datatbl1.setRowHeight(25);
        jScrollPane2.setViewportView(datatbl1);
        if (datatbl1.getColumnModel().getColumnCount() > 0) {
            datatbl1.getColumnModel().getColumn(0).setResizable(false);
            datatbl1.getColumnModel().getColumn(0).setPreferredWidth(30);
            datatbl1.getColumnModel().getColumn(1).setResizable(false);
            datatbl1.getColumnModel().getColumn(2).setResizable(false);
        }

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Exepenses");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        datatbl2.setAutoCreateRowSorter(true);
        datatbl2.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        datatbl2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL No", "Input Date", "Expenses Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        datatbl2.setRowHeight(25);
        jScrollPane3.setViewportView(datatbl2);
        if (datatbl2.getColumnModel().getColumnCount() > 0) {
            datatbl2.getColumnModel().getColumn(0).setResizable(false);
            datatbl2.getColumnModel().getColumn(0).setPreferredWidth(30);
            datatbl2.getColumnModel().getColumn(1).setResizable(false);
            datatbl2.getColumnModel().getColumn(2).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            ByDateCash();
            ByDateCredit();
            ByDateExpens();
            totalaction();
            String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
            String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();
            description = "Data Compare Between '" + fromdate + "'To '" + todate + "'";
        } catch (SQLException ex) {
            Logger.getLogger(IncomeExpenss.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        DefaultTableModel model3 = (DefaultTableModel) datatbl1.getModel();
        DefaultTableModel model4 = (DefaultTableModel) datatbl2.getModel();
        if (model2.getRowCount() == 0) {

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {

            try {

                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JRTableModelDataSource ItemJRBean1 = new JRTableModelDataSource(model3);
                JRTableModelDataSource ItemJRBean2 = new JRTableModelDataSource(model4);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/IncomeExp.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDataSource", ItemJRBean);
                para.put("ItemDataSource1", ItemJRBean1);
                para.put("ItemDataSource2", ItemJRBean2);
                para.put("totalcash", cashtext.getText());
                para.put("totalcredit", credittex.getText());
                para.put("totalexp", exptext.getText());
                para.put("totalinv", totalinvtext.getText());
                para.put("profit", profittext.getText());
                para.put("description", description);
                // para.put("fromdate", fromdate);
                // para.put("todate", todate);
                // para.put("nettotal", netexptext.getText());
                // para.put("description", description);
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void credittexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_credittexActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_credittexActionPerformed

    private void totalinvtextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalinvtextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalinvtextActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            bymonthCash();
            int months = month;
            String monthname;
            if (months == 1) {

                monthname = "January";

            } else if (months == 2) {
                monthname = "February";
            } else if (months == 3) {
                monthname = "March";
            } else if (months == 4) {
                monthname = "April";
            } else if (months == 5) {
                monthname = "May";
            } else if (months == 6) {
                monthname = "June";
            } else if (months == 7) {
                monthname = "July";
            } else if (months == 8) {
                monthname = "August";
            } else if (months == 9) {
                monthname = "September";
            } else if (months == 10) {
                monthname = "October";
            } else if (months == 11) {
                monthname = "November";
            } else if (months == 12) {
                monthname = "December";
            } else {
                monthname = " ";
            }

            int years = yearbox.getYear();
            description = "Data Details From Month:-" + monthname +","+ years +".";
        } catch (SQLException ex) {
            Logger.getLogger(IncomeExpenss.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(IncomeExpenss.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cashtext;
    private javax.swing.JTextField credittex;
    private javax.swing.JTable datatbl;
    private javax.swing.JTable datatbl1;
    private javax.swing.JTable datatbl2;
    private javax.swing.JTextField exptext;
    private com.toedter.calendar.JDateChooser fromdatepayment;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private com.toedter.calendar.JMonthChooser monthbox;
    private javax.swing.JTextField profittext;
    private com.toedter.calendar.JDateChooser todatepayment;
    private javax.swing.JTextField totalinvtext;
    private com.toedter.calendar.JYearChooser yearbox;
    // End of variables declaration//GEN-END:variables
}
