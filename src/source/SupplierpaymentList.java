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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import javax.swing.JTextField;
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
import java.util.Map;

/**
 *
 * @author adnan
 */
public class SupplierpaymentList extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String supId = null;
    String spId = null;
    String description=null;

    /**
     * Creates new form SupplierpurchaseList
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public SupplierpaymentList() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        AutoCompleteDecorator.decorate(supliyerbox);
        supplyerbox();
        //table_update();
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
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 8).toString());
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
 

    private void supplyerbox() throws SQLException {

        try {
            String sql = "Select supliyername from suplyierInfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String category = rs.getString("supliyername");
                supliyerbox.addItem(category);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
private void spaymentdate(){
   DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            int tree = 0;
            try {
                String sql = "Select pp.transactionNo as 'transactionno' , pp.parchseNo as 'PurchaseNo', pp.Grnno as 'GRNNo', pp.paymentdate as 'PaymentDate',(select supliyername from suplyierinfo sp where sp.id=par.supliyer)  as 'Suppliyer', par.nettotal as 'TotalAmount' ,pp.paymenttype as 'PaymentType', pp.paidamount as 'PaidAmount', pp.balancedue as 'BalanceDue', pp.accNo as 'AccNo',pp.chequeNo as  'ChequeNo',(select Bankname from bankinfo bin where bin.id=pp.bankid) as 'Bank' from parchasepayment pp inner join purchase par ON par.purchaseCode=pp.parchseNo where par.supliyer='" + spId + "'  AND pp.paymentdate BETWEEN ? AND ? ";
                pst = conn.prepareStatement(sql);
                 pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
                pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
                rs = pst.executeQuery();
                // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

                while (rs.next()) {
                    String PaymentDate = rs.getString("PaymentDate");
                    String grnno = rs.getString("GRNNo");
                    String parchasecode = rs.getString("PurchaseNo");
                    String suppliyer = rs.getString("Suppliyer");
                    double TotalAmount = rs.getDouble("TotalAmount");
                    String transactionno = rs.getString("transactionno");
                    double PaidAmount = rs.getDouble("PaidAmount");
                    double BalanceDue = rs.getDouble("BalanceDue");

                    String PaymentType = rs.getString("PaymentType");
                    String AccNo = rs.getString("AccNo");
                    String ChequeNo = rs.getString("ChequeNo");
                    String Bank = rs.getString("Bank");
                    tree++;
                    model2.addRow(new Object[]{tree, transactionno, grnno, parchasecode, PaymentDate, suppliyer, TotalAmount, PaymentType, PaidAmount, BalanceDue, AccNo, ChequeNo, Bank});

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            
        totalamttext.setText(Float.toString(total_action_amount()));
        paidamounttext.setText(Float.toString(total_action_paid()));
        duetext.setText(Float.toString(total_action_due()));
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
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        supliyerbox = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        todatepayment = new com.toedter.calendar.JDateChooser();
        jButton6 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        fromdatepayment = new com.toedter.calendar.JDateChooser();
        jButton5 = new javax.swing.JButton();
        descriptiontext = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        totalamttext = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        paidamounttext = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        duetext = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Supplyier Payment List");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        jPanel3.setBackground(new java.awt.Color(67, 86, 86));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Supplyer name");

        supliyerbox.setEditable(true);
        supliyerbox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        supliyerbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        supliyerbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                supliyerboxPopupMenuWillBecomeInvisible(evt);
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
                    .addComponent(jLabel1)
                    .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel4.setBackground(new java.awt.Color(67, 86, 86));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        todatepayment.setDateFormatString("yyyy-MM-dd");

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(102, 102, 102));
        jButton6.setText("Report");
        jButton6.setBorder(null);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("To");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("From");

        fromdatepayment.setDateFormatString("yyyy-MM-dd");

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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fromdatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(todatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(todatepayment, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fromdatepayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        descriptiontext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        descriptiontext.setForeground(new java.awt.Color(255, 255, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(descriptiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(211, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(descriptiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total Amount");

        totalamttext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalamttext.setForeground(new java.awt.Color(255, 255, 255));
        totalamttext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        totalamttext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Paid");

        paidamounttext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        paidamounttext.setForeground(new java.awt.Color(255, 255, 255));
        paidamounttext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        paidamounttext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Due");

        duetext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        duetext.setForeground(new java.awt.Color(255, 255, 255));
        duetext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        duetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton12.setBackground(new java.awt.Color(153, 51, 0));
        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("Print Data Table");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalamttext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(paidamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(duetext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(duetext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(paidamounttext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(totalamttext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL No", "Tr No", "Purchase No", "GRN No", "Payment Date", "Supplier", "Amount", "Payment Type", "Paid Amount", "Balance Due", "Acc No", "Cheque No", "Bank"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false
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
        datatbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setResizable(false);
            datatbl.getColumnModel().getColumn(0).setPreferredWidth(20);
            datatbl.getColumnModel().getColumn(1).setResizable(false);
            datatbl.getColumnModel().getColumn(1).setPreferredWidth(30);
            datatbl.getColumnModel().getColumn(2).setResizable(false);
            datatbl.getColumnModel().getColumn(3).setResizable(false);
            datatbl.getColumnModel().getColumn(4).setResizable(false);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(5).setPreferredWidth(100);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
            datatbl.getColumnModel().getColumn(8).setResizable(false);
            datatbl.getColumnModel().getColumn(9).setResizable(false);
            datatbl.getColumnModel().getColumn(10).setResizable(false);
            datatbl.getColumnModel().getColumn(10).setPreferredWidth(100);
            datatbl.getColumnModel().getColumn(11).setResizable(false);
            datatbl.getColumnModel().getColumn(12).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void supliyerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supliyerboxPopupMenuWillBecomeInvisible
        if (supliyerbox.getSelectedIndex() > 0) {
            try {

                String table_click = (String) supliyerbox.getSelectedItem();
                String sql = "Select * from suplyierInfo where supliyername='" + table_click + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {

                    spId = rs.getString("id");
                    supId = rs.getString("supliyername");

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            int tree = 0;
            try {
                String sql = "Select pp.transactionNo as 'transactionno' , pp.parchseNo as 'PurchaseNo', pp.Grnno as 'GRNNo', pp.paymentdate as 'PaymentDate',(select supliyername from suplyierinfo sp where sp.id=par.supliyer)  as 'Suppliyer', par.nettotal as 'TotalAmount' ,pp.paymenttype as 'PaymentType', pp.paidamount as 'PaidAmount', pp.balancedue as 'BalanceDue', pp.accNo as 'AccNo',pp.chequeNo as  'ChequeNo',(select Bankname from bankinfo bin where bin.id=pp.bankid) as 'Bank' from parchasepayment pp inner join purchase par ON par.purchaseCode=pp.parchseNo where par.supliyer='" + spId + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

                while (rs.next()) {
                    String PaymentDate = rs.getString("PaymentDate");
                    String grnno = rs.getString("GRNNo");
                    String parchasecode = rs.getString("PurchaseNo");
                    String suppliyer = rs.getString("Suppliyer");
                    double TotalAmount = rs.getDouble("TotalAmount");
                    String transactionno = rs.getString("transactionno");
                    double PaidAmount = rs.getDouble("PaidAmount");
                    double BalanceDue = rs.getDouble("BalanceDue");

                    String PaymentType = rs.getString("PaymentType");
                    String AccNo = rs.getString("AccNo");
                    String ChequeNo = rs.getString("ChequeNo");
                    String Bank = rs.getString("Bank");
                    tree++;
                    model2.addRow(new Object[]{tree, transactionno, grnno, parchasecode, PaymentDate, suppliyer, TotalAmount, PaymentType, PaidAmount, BalanceDue, AccNo, ChequeNo, Bank});

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            
        totalamttext.setText(Float.toString(total_action_amount()));
        paidamounttext.setText(Float.toString(total_action_paid()));
        duetext.setText(Float.toString(total_action_due()));
          description = "Supplier: " + supliyerbox.getSelectedItem();
         descriptiontext.setText(description);
        
        }

    }//GEN-LAST:event_supliyerboxPopupMenuWillBecomeInvisible

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
if (supliyerbox.getSelectedIndex()==0 || ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText().isEmpty() || ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select From And To date Value");
        } else {
            String descriptionhere = null;
            spaymentdate();
            String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
            String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();
            descriptionhere = "Supplier : "+supliyerbox.getSelectedItem()+ " "
                    + " Date Between From: " + fromdate + " To: " + todate;
            descriptiontext.setText(descriptionhere);
            description=descriptionhere;
        } 

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (supliyerbox.getSelectedIndex()==0 || ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText().isEmpty() || ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select From And To date Value");
        } else {
            String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
            String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();

            try {

                JasperDesign jd = null;
                try {
                    jd = JRXmlLoader.load(new File("")
                            .getAbsolutePath()
                            + "/src/Report/supplyerpaymetn.jrxml");
                    //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");
                } catch (JRException ex) {
                    Logger.getLogger(SupplyeStatement.class.getName()).log(Level.SEVERE, null, ex);
                }

                HashMap para = new HashMap();
                para.put("fromdate", fromdate);
                para.put("todate", todate);

                para.put("suplyid", spId);
                //  para.put("trno", trno);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

                //  }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked

    }//GEN-LAST:event_datatblMouseClicked

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        if (model2.getRowCount() == 0) {

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {

            try {
                String totalamount = totalamttext.getText();
                String totalpaid = paidamounttext.getText();
                String totaldue = duetext.getText();

                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/PaymentDetails.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDataSource", ItemJRBean);
                para.put("description", description);

                para.put("totalamount", totalamount);
                para.put("totalpaid", totalpaid);
                para.put("totaldue", totaldue);
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }

    }//GEN-LAST:event_jButton12ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable datatbl;
    private javax.swing.JLabel descriptiontext;
    private javax.swing.JLabel duetext;
    private com.toedter.calendar.JDateChooser fromdatepayment;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel paidamounttext;
    private javax.swing.JComboBox<String> supliyerbox;
    private com.toedter.calendar.JDateChooser todatepayment;
    private javax.swing.JLabel totalamttext;
    // End of variables declaration//GEN-END:variables
}
