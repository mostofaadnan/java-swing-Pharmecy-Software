/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
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
 * @author adnan
 */
public class CreditBook extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int cusid;

    /**
     * Creates new form CreditBook
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public CreditBook() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        table_update();
        customer();
    }

    private void customer() throws SQLException {

        try {
            String sql = "Select customername from customerInfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("customername");
                customerinfobox.addItem(name);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void search() throws SQLException {
        if (customerinfobox.getSelectedIndex() > 0) {
            int tree = 1;
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            try {
                String sql = "Select id as 'Code',customername as 'Customer',customerType as 'Type',OpenigBalance as 'OpeningBalance',creditAmnt as 'CreditInvoice',cashamt as 'CashInvoice',saleamount as 'TotalSaleAmount',totaldiscount as 'TotalDiscount',paidamount,Balancedue as 'BalanceDue' from customerInfo where  id ='" + cusid + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()) {
                    String code = rs.getString("Code");
                    String customername = rs.getString("Customer");
                    String customerType = rs.getString("Type");
                    double OpeningBalance = rs.getDouble("OpeningBalance");

                    double CreditInvoice = rs.getDouble("CreditInvoice");
                    double CashInvoice = rs.getDouble("CashInvoice");
                    double TotalSaleAmount = rs.getDouble("TotalSaleAmount");
                    double paidamount = rs.getDouble("paidamount");
                    double TotalDiscount = rs.getDouble("TotalDiscount");
                    double BalanceDue = rs.getDouble("BalanceDue");

                    model2.addRow(new Object[]{tree, code, customername, customerType, OpeningBalance, CreditInvoice, CashInvoice, TotalSaleAmount, TotalDiscount, paidamount, BalanceDue});
                    tree++;

                }
                // datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            cashtext.setText(Float.toString(total_action_credit()));
            credittext.setText(Float.toString(total_action_cash()));
            saletext.setText(Float.toString(total_action_sale()));
            paidtext.setText(Float.toString(total_action_payment()));
            balancetext.setText(Float.toString(total_action_balance()));
        }
    }

    private void table_update() throws SQLException {
        int tree = 1;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select id as 'Code',customername as 'Customer',customerType as 'Type',OpenigBalance as 'OpeningBalance',creditAmnt as 'CreditInvoice',cashamt as 'CashInvoice',saleamount as 'TotalSaleAmount',totaldiscount as 'TotalDiscount',paidamount,Balancedue as 'BalanceDue' from customerInfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String code = rs.getString("Code");
                String customername = rs.getString("Customer");
                String customerType = rs.getString("Type");
                double OpeningBalance = rs.getDouble("OpeningBalance");

                double CreditInvoice = rs.getDouble("CreditInvoice");
                double CashInvoice = rs.getDouble("CashInvoice");
                double TotalSaleAmount = rs.getDouble("TotalSaleAmount");
                double paidamount = rs.getDouble("paidamount");
                double TotalDiscount = rs.getDouble("TotalDiscount");
                double BalanceDue = rs.getDouble("BalanceDue");

                model2.addRow(new Object[]{tree, code, customername, customerType, OpeningBalance, CreditInvoice, CashInvoice, TotalSaleAmount, TotalDiscount, paidamount, BalanceDue});
                tree++;

            }
            // datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        cashtext.setText(Float.toString(total_action_cash()));
        credittext.setText(Float.toString(total_action_credit()));
        saletext.setText(Float.toString(total_action_sale()));
        paidtext.setText(Float.toString(total_action_payment()));
        balancetext.setText(Float.toString(total_action_balance()));

    }

    private float total_action_credit() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 5).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_cash() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 6).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_sale() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 7).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_payment() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 9).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_balance() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 10).toString());
        }

        return (float) totaltpmrp;

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
        jLabel1 = new javax.swing.JLabel();
        customerinfobox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        saletext = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        paidtext = new javax.swing.JLabel();
        cashtext = new javax.swing.JLabel();
        credittext = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        balancetext = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Credit Book");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Customer name");

        customerinfobox.setEditable(true);
        customerinfobox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        customerinfobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        customerinfobox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                customerinfoboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(customerinfobox, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(customerinfobox, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(5, 5, 5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Net Total"));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Sale:");

        saletext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        saletext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        saletext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Paid Amount:");

        paidtext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        paidtext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        paidtext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        cashtext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cashtext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cashtext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        credittext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        credittext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        credittext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Credit Invoice:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Cash Invoice:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Balance Due");

        balancetext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        balancetext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        balancetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashtext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(credittext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saletext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paidtext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(balancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cashtext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(credittext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saletext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(paidtext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(balancetext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "Code", "Customer Name", "balanceType", "Openig Balance", "Credit Amnt", "Cash Amount", "Sale Amount", "Total Discount", "Paid Amount", "Balance Due"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
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
        datatbl.setShowVerticalLines(false);
        jScrollPane1.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setResizable(false);
            datatbl.getColumnModel().getColumn(1).setResizable(false);
            datatbl.getColumnModel().getColumn(1).setPreferredWidth(100);
            datatbl.getColumnModel().getColumn(2).setResizable(false);
            datatbl.getColumnModel().getColumn(2).setPreferredWidth(200);
            datatbl.getColumnModel().getColumn(3).setResizable(false);
            datatbl.getColumnModel().getColumn(3).setPreferredWidth(100);
            datatbl.getColumnModel().getColumn(4).setResizable(false);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
            datatbl.getColumnModel().getColumn(8).setResizable(false);
            datatbl.getColumnModel().getColumn(9).setResizable(false);
            datatbl.getColumnModel().getColumn(10).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void customerinfoboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_customerinfoboxPopupMenuWillBecomeInvisible
        if (customerinfobox.getSelectedIndex() > 0) {

            try {
                String sql = "Select id from customerInfo where customername='" + customerinfobox.getSelectedItem() + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    cusid = rs.getInt("id");

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

            try {
                search();
            } catch (SQLException ex) {
                Logger.getLogger(CreditBook.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(CreditBook.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_customerinfoboxPopupMenuWillBecomeInvisible

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        if (model2.getRowCount() == 0) {

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {

            try {
                double totalcrdit = Double.parseDouble(cashtext.getText());
                double totalcash = Double.parseDouble(credittext.getText());
                double totalsale = Double.parseDouble(saletext.getText());
                double totalpaid = Double.parseDouble(paidtext.getText());
                double totaldue = Double.parseDouble(balancetext.getText());
                String customer=(String) customerinfobox.getSelectedItem();
                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/CreditBook.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDataSource", ItemJRBean);
                para.put("totalcrdit", totalcrdit);
                para.put("totalcash", totalcash);
                para.put("totalsale", totalsale);
                para.put("totalpaid", totalpaid);
                para.put("totaldue", totaldue);
                  para.put("customer", customer);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel balancetext;
    private javax.swing.JLabel cashtext;
    private javax.swing.JLabel credittext;
    private javax.swing.JComboBox<String> customerinfobox;
    private javax.swing.JTable datatbl;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel paidtext;
    private javax.swing.JLabel saletext;
    // End of variables declaration//GEN-END:variables
}
