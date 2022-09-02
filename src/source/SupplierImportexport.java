/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Dimension;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class SupplierImportexport extends javax.swing.JInternalFrame {
Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String spId=null;
    String SuppliyerName;
    /**
     * Creates new form SupplierImportexport
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public SupplierImportexport() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
         supplyerbox();
         //table_update();
          AutoCompleteDecorator.decorate(suppliyername);
    }
      private float total_action_totalimportamount() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 2).toString());
        }

        return (float) totaltpmrp;

    }
        private float total_action_totalexportamount() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 3).toString());
        }

        return (float) totaltpmrp;

    }
          private float total_action_importpayment() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 4).toString());
        }

        return (float) totaltpmrp;

    }
            private float total_action_exportrecieve() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 5).toString());
        }

        return (float) totaltpmrp;

    }
              private float total_action_paymentdue() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 6).toString());
        }

        return (float) totaltpmrp;

    }
                private float total_action_recievedue() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i,7).toString());
        }

        return (float) totaltpmrp;

    }
     private void table_update() throws SQLException {
       
        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select (select sp.supliyername from suplyierinfo sp where sp.id=im.supid) as 'supliyername',SUM(importAmount) as 'importamount',SUM(exportAmount) as 'exportamount',SUM(payment) as 'ImportPayment',SUM(recieve) as 'ExportRecieve',SUM(paymentdue) as 'paymentdue',SUM(reciebedue) as 'recievedue' from suppliyer_import_export_balance im GROUP BY supid ORDER BY supid ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {

                
                String supliyername = rs.getString("supliyername");
               

                double importamount = rs.getDouble("importamount");
                double exportamount = rs.getDouble("exportamount");
                double ImportPayment = rs.getDouble("ImportPayment");
                double ExportRecieve = rs.getDouble("ExportRecieve");
                double paymentdue = rs.getDouble("paymentdue");
                double recievedue = rs.getDouble("recievedue");

                tree++;
                model2.addRow(new Object[]{tree,supliyername, importamount, exportamount,ImportPayment,ExportRecieve,paymentdue,recievedue});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
importamounttext.setText(Float.toString(total_action_totalimportamount()) );
        exportamounttext.setText(Float.toString(total_action_totalexportamount()));
        importpaymenttext.setText(Float.toString(total_action_importpayment()) );
        exportrecievetext.setText(Float.toString(total_action_exportrecieve()));
        importduetext.setText(Float.toString(total_action_paymentdue()) );
        exportduetext.setText(Float.toString(total_action_recievedue()));
    }
private void supplyerbox() throws SQLException {
     
     
        try {
            String sql = "Select supliyername from suplyierInfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String category = rs.getString("supliyername");
                suppliyername.addItem(category);

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

        tablemenu = new javax.swing.JPopupMenu();
        detailsview = new javax.swing.JMenuItem();
        reportprint = new javax.swing.JMenuItem();
        jPanel8 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        suppliyername = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        importamounttext = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        exportamounttext = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        importpaymenttext = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        exportrecievetext = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        importduetext = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        exportduetext = new javax.swing.JTextField();

        tablemenu.setPreferredSize(new java.awt.Dimension(300, 100));

        detailsview.setText("Details View");
        detailsview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailsviewActionPerformed(evt);
            }
        });
        tablemenu.add(detailsview);

        reportprint.setText("Report Print");
        tablemenu.add(reportprint);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Suppliyer Import/Expor");

        jPanel8.setBackground(new java.awt.Color(67, 86, 86));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Supplyer name");

        suppliyername.setEditable(true);
        suppliyername.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        suppliyername.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        suppliyername.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                suppliyernamePopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(153, 0, 0));
        jButton1.setText("Load");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(suppliyername, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(717, 717, 717))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(suppliyername, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SI No", "Suppliyer", "Import Amount", "Export Amount", "Import Payment", "Export Recieve", "Payment Due", "Recived Due"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
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
            datatbl.getColumnModel().getColumn(1).setResizable(false);
            datatbl.getColumnModel().getColumn(1).setPreferredWidth(300);
            datatbl.getColumnModel().getColumn(2).setResizable(false);
            datatbl.getColumnModel().getColumn(3).setResizable(false);
            datatbl.getColumnModel().getColumn(4).setResizable(false);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
        }

        jLabel1.setText("Import Amount");

        importamounttext.setEditable(false);
        importamounttext.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        jLabel2.setText("Export Amount");

        exportamounttext.setEditable(false);
        exportamounttext.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        jLabel3.setText("Import Payment");

        importpaymenttext.setEditable(false);
        importpaymenttext.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        jLabel4.setText("Export Recive");

        exportrecievetext.setEditable(false);
        exportrecievetext.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        jLabel5.setText("Payment Due");

        importduetext.setEditable(false);
        importduetext.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        jLabel6.setText("Recieve Due");

        exportduetext.setEditable(false);
        exportduetext.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importpaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportrecievetext, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(importduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(exportduetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(importamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(importpaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(exportamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(exportrecievetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void suppliyernamePopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_suppliyernamePopupMenuWillBecomeInvisible
 if (suppliyername.getSelectedIndex() > 0) {
            try {

                String table_click = (String) suppliyername.getSelectedItem();
                String sql = "Select id from suplyierInfo where supliyername='" + table_click + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
               
                    spId = rs.getString("id");
                  

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
 
   int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select (select sp.supliyername from suplyierinfo sp where sp.id=im.supid) as 'supliyername',SUM(importAmount) as 'importamount',SUM(exportAmount) as 'exportamount',SUM(payment) as 'ImportPayment',SUM(recieve) as 'ExportRecieve',SUM(paymentdue) as 'paymentdue',SUM(reciebedue) as 'recievedue' from suppliyer_import_export_balance im where supid='"+spId+"' GROUP BY supid ORDER BY supid ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {

                
                String supliyername = rs.getString("supliyername");

                double importamount = rs.getDouble("importamount");
                double exportamount = rs.getDouble("exportamount");
                double ImportPayment = rs.getDouble("ImportPayment");
                double ExportRecieve = rs.getDouble("ExportRecieve");
                double paymentdue = rs.getDouble("paymentdue");
                double recievedue = rs.getDouble("recievedue");

                tree++;
                model2.addRow(new Object[]{tree, supliyername, importamount, exportamount,ImportPayment,ExportRecieve,paymentdue,recievedue});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
 
 importamounttext.setText(Float.toString(total_action_totalimportamount()) );
        exportamounttext.setText(Float.toString(total_action_totalexportamount()));
        importpaymenttext.setText(Float.toString(total_action_importpayment()) );
        exportrecievetext.setText(Float.toString(total_action_exportrecieve()));
        importduetext.setText(Float.toString(total_action_paymentdue()) );
        exportduetext.setText(Float.toString(total_action_recievedue()));
 }     
    }//GEN-LAST:event_suppliyernamePopupMenuWillBecomeInvisible

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    try {
        table_update();
    } catch (SQLException ex) {
        Logger.getLogger(SupplierImportexport.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
        int row = datatbl.getSelectedRow();
        SuppliyerName = (datatbl.getModel().getValueAt(row, 1).toString());
        
         try {

              
                String sql = "Select id from suplyierInfo where supliyername='" + SuppliyerName + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
               
                    spId = rs.getString("id");
                  

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
        if (SwingUtilities.isRightMouseButton(evt)) {
            tablemenu.show(evt.getComponent(), evt.getX() + 1, evt.getY() + 1);

        }
    }//GEN-LAST:event_datatblMouseClicked

    private void detailsviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailsviewActionPerformed
        importexportsuppliyerdetails filte = null;

    try {
        filte = new importexportsuppliyerdetails(spId,SuppliyerName);
    } catch (SQLException | IOException ex) {
        Logger.getLogger(SupplierImportexport.class.getName()).log(Level.SEVERE, null, ex);
    }
        filte.setVisible(true);
        this.getDesktopPane().add(filte);
        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
        (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
    }//GEN-LAST:event_detailsviewActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable datatbl;
    private javax.swing.JMenuItem detailsview;
    private javax.swing.JTextField exportamounttext;
    private javax.swing.JTextField exportduetext;
    private javax.swing.JTextField exportrecievetext;
    private javax.swing.JTextField importamounttext;
    private javax.swing.JTextField importduetext;
    private javax.swing.JTextField importpaymenttext;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem reportprint;
    private javax.swing.JComboBox<String> suppliyername;
    private javax.swing.JPopupMenu tablemenu;
    // End of variables declaration//GEN-END:variables
}
