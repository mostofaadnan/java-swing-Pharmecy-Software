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
import javax.swing.JOptionPane;
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
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class importexportsuppliyerdetails extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String spId = null;

    /**
     * Creates new form importexportsuppliyerdetails
     *
     * @throws java.sql.SQLException
     */
    public importexportsuppliyerdetails() throws SQLException, IOException{
        initComponents();
        conn = Java_Connect.conectrDB();
        supplyerbox();
        AutoCompleteDecorator.decorate(suppliyername);
        DataView();
    }

    public importexportsuppliyerdetails(String tableclick, String SuppliyerName) throws SQLException , IOException{
        initComponents();
        conn = Java_Connect.conectrDB();
        supplyerbox();
        AutoCompleteDecorator.decorate(suppliyername);
        spId = tableclick;
        DataView();
        suppliyername.setSelectedItem(SuppliyerName);

    }

    private void DataView() {

        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select inputdate,(select sp.supliyername from suplyierinfo sp where sp.id=im.supid) as 'supliyername',importAmount as 'importamount',exportAmount as 'exportamount',payment as 'ImportPayment',recieve as 'ExportRecieve',paymentdue as 'paymentdue',reciebedue as 'recievedue' from suppliyer_import_export_balance im where supid='" + spId + "' ORDER BY supid ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {

                String supliyername = rs.getString("supliyername");
                String inputdate = rs.getString("inputdate");
                double importamount = rs.getDouble("importamount");
                double exportamount = rs.getDouble("exportamount");
                double ImportPayment = rs.getDouble("ImportPayment");
                double ExportRecieve = rs.getDouble("ExportRecieve");
                double paymentdue = rs.getDouble("paymentdue");
                double recievedue = rs.getDouble("recievedue");

                tree++;
                model2.addRow(new Object[]{tree, inputdate, supliyername, importamount, exportamount, ImportPayment, ExportRecieve, paymentdue, recievedue});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        importamounttext.setText(Float.toString(total_action_totalimportamount()));
        exportamounttext.setText(Float.toString(total_action_totalexportamount()));
        importpaymenttext.setText(Float.toString(total_action_importpayment()));
        exportrecievetext.setText(Float.toString(total_action_exportrecieve()));
        importduetext.setText(Float.toString(total_action_paymentdue()));
        exportduetext.setText(Float.toString(total_action_recievedue()));

    }

    private float total_action_totalimportamount() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 3).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_totalexportamount() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 4).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_importpayment() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 5).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_exportrecieve() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 6).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_paymentdue() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 7).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_recievedue() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 8).toString());
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
                model2.addRow(new Object[]{tree, supliyername, importamount, exportamount, ImportPayment, ExportRecieve, paymentdue, recievedue});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        importamounttext.setText(Float.toString(total_action_totalimportamount()));
        exportamounttext.setText(Float.toString(total_action_totalexportamount()));
        importpaymenttext.setText(Float.toString(total_action_importpayment()));
        exportrecievetext.setText(Float.toString(total_action_exportrecieve()));
        importduetext.setText(Float.toString(total_action_paymentdue()));
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

        jPanel8 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        suppliyername = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        fromdatepayment = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        todatepayment = new com.toedter.calendar.JDateChooser();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
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

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Supplier Import Export");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

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

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("From");

        fromdatepayment.setDateFormatString("yyyy-MM-dd");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("To");

        todatepayment.setDateFormatString("yyyy-MM-dd");

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21)
                    .addComponent(suppliyername, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fromdatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(todatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(75, 75, 75))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(2, 2, 2)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(todatepayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fromdatepayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(suppliyername, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SI No", "Input Date", "Suppliyer", "Import Amount", "Export Amount", "Import Payment", "Export Recieve", "Payment Due", "Recived Due"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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
            datatbl.getColumnModel().getColumn(2).setResizable(false);
            datatbl.getColumnModel().getColumn(3).setResizable(false);
            datatbl.getColumnModel().getColumn(4).setResizable(false);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
            datatbl.getColumnModel().getColumn(8).setResizable(false);
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
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

            DataView();

        }
    }//GEN-LAST:event_suppliyernamePopupMenuWillBecomeInvisible

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        /*
        try {
            String sql = "Select invoiceNo as 'Invoice No',invoicedate as 'Invoice Date', TotalAmount as 'Total Amount',TotalVat as 'Total Vat',Totalinvoice as 'Total Invoice', paymentCurency as 'Currency',(select ad.name from admin ad where ad.id=it.inputuserid) as 'Input User'   from sale it  where it.invoicedate BETWEEN ? AND ? OR year(it.invoicedate)='" + yeartext.getText() + "' ORDER BY invoiceNo DESC";
            pst = conn.prepareStatement(sql);
            pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
            pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
            rs = pst.executeQuery();
            datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

         */
        if (suppliyername.getSelectedIndex() > 0) {
            int tree = 0;
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            try {
                String sql = "Select inputdate,(select sp.supliyername from suplyierinfo sp where sp.id=im.supid) as 'supliyername',importAmount as 'importamount',exportAmount as 'exportamount',payment as 'ImportPayment',recieve as 'ExportRecieve',paymentdue as 'paymentdue',reciebedue as 'recievedue' from suppliyer_import_export_balance im where supid='" + spId + "' AND inputdate BETWEEN ? AND ?  ORDER BY supid ASC";
                pst = conn.prepareStatement(sql);
                pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
                pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
                rs = pst.executeQuery();
                // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

                while (rs.next()) {

                    String supliyername = rs.getString("supliyername");
                    String inputdate = rs.getString("inputdate");
                    double importamount = rs.getDouble("importamount");
                    double exportamount = rs.getDouble("exportamount");
                    double ImportPayment = rs.getDouble("ImportPayment");
                    double ExportRecieve = rs.getDouble("ExportRecieve");
                    double paymentdue = rs.getDouble("paymentdue");
                    double recievedue = rs.getDouble("recievedue");

                    tree++;
                    model2.addRow(new Object[]{tree, inputdate, supliyername, importamount, exportamount, ImportPayment, ExportRecieve, paymentdue, recievedue});

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

            importamounttext.setText(Float.toString(total_action_totalimportamount()));
            exportamounttext.setText(Float.toString(total_action_totalexportamount()));
            importpaymenttext.setText(Float.toString(total_action_importpayment()));
            exportrecievetext.setText(Float.toString(total_action_exportrecieve()));
            importduetext.setText(Float.toString(total_action_paymentdue()));
            exportduetext.setText(Float.toString(total_action_recievedue()));
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        if (model2.getRowCount() == 0) {

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {
            String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
            String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();

            try {
                double importamt = Double.parseDouble(importamounttext.getText());
                double exportamt = Double.parseDouble(exportamounttext.getText());
                double importpayment = Double.parseDouble(importpaymenttext.getText());
                double exportrecieve = Double.parseDouble(exportrecievetext.getText());
                double paymentdue = Double.parseDouble(importduetext.getText());
                double recievedue = Double.parseDouble(exportduetext.getText());
                String datadescription = "Date Between:" + fromdate + " To " + todate;
                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/SuppliyerImportExportDetails.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDataSource", ItemJRBean);
                para.put("importamt", importamt);
                para.put("exportamt", exportamt);
                para.put("importpayment", importpayment);
                para.put("exportrecieve", exportrecieve);
                para.put("paymentdue", paymentdue);
                para.put("recievedue", recievedue);
                para.put("spid", spId);

                para.put("fromdate", fromdate);
                if (((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText().isEmpty() || ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText().isEmpty()) {

                    para.put("datedescription", " ");
                } else {
                    para.put("datedescription", datadescription);
                }

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }//GEN-LAST:event_jButton6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable datatbl;
    private javax.swing.JTextField exportamounttext;
    private javax.swing.JTextField exportduetext;
    private javax.swing.JTextField exportrecievetext;
    private com.toedter.calendar.JDateChooser fromdatepayment;
    private javax.swing.JTextField importamounttext;
    private javax.swing.JTextField importduetext;
    private javax.swing.JTextField importpaymenttext;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> suppliyername;
    private com.toedter.calendar.JDateChooser todatepayment;
    // End of variables declaration//GEN-END:variables
}
