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
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
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
public class Cashstatement extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String cusId;

    /**
     * Creates new form Cashstatement
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public Cashstatement() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        AutoCompleteDecorator.decorate(customerbox);
        customer();
    }

    private void customer() throws SQLException {
        String type = "Cash";
        String type2 = "Credit And Cash";

        try {
            // String sql = "Select customername from customerInfo where balanceType='"+type+"' OR balanceType='"+type2+"'";
            String sql = "Select customername from customerInfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("customername");
                customerbox.addItem(name);
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

        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        addresstext = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        contactnotext = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        supliertypebox = new javax.swing.JLabel();
        balancetypebox = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        salesAmounttext = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        customerbox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        fromdatepayment1 = new com.toedter.calendar.JDateChooser();
        jLabel59 = new javax.swing.JLabel();
        todatepayment1 = new com.toedter.calendar.JDateChooser();
        overalsubmit = new javax.swing.JButton();
        reportbox = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        typebox = new javax.swing.JComboBox<>();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        datatbl1 = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cash Statement");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Address:");

        addresstext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jLabel28.setText("Contact No");

        contactnotext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jLabel29.setText("Customer Type");

        supliertypebox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        balancetypebox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jLabel30.setText("Balance Type");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addresstext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(contactnotext, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
                            .addComponent(supliertypebox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(balancetypebox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(addresstext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contactnotext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(supliertypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(balancetypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 255, 255), 1, true), "Amount Box"));

        salesAmounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        salesAmounttext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        salesAmounttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jLabel12.setText("Total Sales Amount");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(salesAmounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(salesAmounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 2, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(0, 51, 51));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Customer");

        customerbox.setEditable(true);
        customerbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        customerbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        customerbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                customerboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 153, 102));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("All ");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(customerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(customerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));
        jPanel1.setInheritsPopupMenu(true);

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setText("From");

        fromdatepayment1.setDateFormatString("yyyy-MM-dd");

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setText("To");

        todatepayment1.setDateFormatString("yyyy-MM-dd");

        overalsubmit.setBackground(new java.awt.Color(0, 102, 0));
        overalsubmit.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        overalsubmit.setForeground(new java.awt.Color(255, 255, 255));
        overalsubmit.setText("Submit");
        overalsubmit.setBorder(null);
        overalsubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overalsubmitActionPerformed(evt);
            }
        });

        reportbox.setBackground(new java.awt.Color(0, 204, 0));
        reportbox.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        reportbox.setForeground(new java.awt.Color(255, 255, 255));
        reportbox.setText("Statement");
        reportbox.setBorder(null);
        reportbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportboxActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Type");

        typebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash statement" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(typebox, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fromdatepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(todatepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(overalsubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(reportbox, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel59))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel57)
                    .addComponent(jLabel59))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(typebox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fromdatepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(todatepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(overalsubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reportbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        datatbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        datatbl.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        datatbl.setGridColor(new java.awt.Color(255, 255, 255));
        datatbl.setRowHeight(30);
        jScrollPane1.setViewportView(datatbl);

        jTabbedPane1.addTab("Single View", jScrollPane1);

        datatbl1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SI No", "Date", "Item Code", "Description", "Unit Rate", "Qty", "Unit", "Discount", "Amount", "Vat", "Net Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Float.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
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
        datatbl1.setGridColor(new java.awt.Color(204, 204, 204));
        datatbl1.setRowHeight(30);
        jScrollPane2.setViewportView(datatbl1);
        if (datatbl1.getColumnModel().getColumnCount() > 0) {
            datatbl1.getColumnModel().getColumn(0).setResizable(false);
            datatbl1.getColumnModel().getColumn(0).setPreferredWidth(50);
            datatbl1.getColumnModel().getColumn(1).setResizable(false);
            datatbl1.getColumnModel().getColumn(1).setPreferredWidth(100);
            datatbl1.getColumnModel().getColumn(2).setResizable(false);
            datatbl1.getColumnModel().getColumn(3).setResizable(false);
            datatbl1.getColumnModel().getColumn(3).setPreferredWidth(300);
            datatbl1.getColumnModel().getColumn(4).setResizable(false);
            datatbl1.getColumnModel().getColumn(5).setResizable(false);
            datatbl1.getColumnModel().getColumn(6).setResizable(false);
            datatbl1.getColumnModel().getColumn(7).setResizable(false);
            datatbl1.getColumnModel().getColumn(8).setResizable(false);
            datatbl1.getColumnModel().getColumn(9).setResizable(false);
            datatbl1.getColumnModel().getColumn(10).setResizable(false);
        }

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 919, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 919, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 423, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Details View", jPanel7);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        setBounds(0, 0, 1392, 571);
    }// </editor-fold>//GEN-END:initComponents
private void invoiceDetails() {

        try {

            DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
            model2.setRowCount(0);
            int tree = 0;
            String sql = "Select prcode , DATE_FORMAT(invoicedate, '%d-%m-%Y') as 'invoicedate',(select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item', sl.unitrate as 'UnitRate', sl.qty as 'Qty',sl.unit as 'Unit', sl.discount as 'Discount', sl.totalamount as 'Amount', sl.vat as 'Vat', sl.Totalvat as 'Total Vat',sl.NetTotal  as 'NetTotal'  from cashsaledetails sl where sl.cusid='" + cusId + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            //   datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String prcode = rs.getString("prcode");
                String date = rs.getString("invoicedate");
                String Item = rs.getString("Item");
                double UnitRate = rs.getDouble("UnitRate");
                float Qty = rs.getFloat("Qty");
                String Unit = rs.getString("Unit");
                double Discount = rs.getDouble("Discount");
                double Amount = rs.getDouble("Amount");
                double Vat = rs.getDouble("Vat");
                double NetTotal = rs.getDouble("NetTotal");
                tree++;
                model2.addRow(new Object[]{tree, date, prcode, Item, UnitRate, Qty, Unit, Discount, Amount, Vat, NetTotal});

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        //description="All Sale Details";
        //descriptiontext.setText(description);
    }

    private void invoiceDetails_bydate() {
        String fromdate = ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).getText();
        String todate = ((JTextField) todatepayment1.getDateEditor().getUiComponent()).getText();
        try {

            DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
            model2.setRowCount(0);
            int tree = 0;
            String sql = "Select prcode , DATE_FORMAT(invoicedate, '%d-%m-%Y') as 'invoicedate',(select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item', sl.unitrate as 'UnitRate', sl.qty as 'Qty',sl.unit as 'Unit', sl.discount as 'Discount', sl.totalamount as 'Amount', sl.vat as 'Vat', sl.Totalvat as 'Total Vat',sl.NetTotal  as 'NetTotal'  from cashsaledetails sl where sl.cusid='" + cusId + "' AND invoicedate BETWEEN ? AND ?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).getText());
            pst.setString(2, ((JTextField) todatepayment1.getDateEditor().getUiComponent()).getText());
            rs = pst.executeQuery();
            //   datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String prcode = rs.getString("prcode");
                String date = rs.getString("invoicedate");
                String Item = rs.getString("Item");
                double UnitRate = rs.getDouble("UnitRate");
                float Qty = rs.getFloat("Qty");
                String Unit = rs.getString("Unit");
                double Discount = rs.getDouble("Discount");
                double Amount = rs.getDouble("Amount");
                double Vat = rs.getDouble("Vat");
                double NetTotal = rs.getDouble("NetTotal");
                tree++;
                model2.addRow(new Object[]{tree, date, prcode, Item, UnitRate, Qty, Unit, Discount, Amount, Vat, NetTotal});

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        //description="Date Between "+fromdate+" To "+todate;
        // descriptiontext.setText(description);
    }
    private void customerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_customerboxPopupMenuWillBecomeInvisible

        try {

            String table_click = (String) customerbox.getSelectedItem();
            String sql = "Select id,customerid,address,customerType,balanceType,cast(cashamt as decimal(10,2)) as 'cashamt',ContactNo from customerInfo where customername='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                cusId = rs.getString("customerid");
                String address = rs.getString("address");
                addresstext.setText(address);
                String customertype = rs.getString("customerType");
                supliertypebox.setText(customertype);
                String balanceType = rs.getString("balanceType");
                balancetypebox.setText(balanceType);
                String saleamount = rs.getString("cashamt");
                salesAmounttext.setText(saleamount);
                String contctno = rs.getString("ContactNo");
                contactnotext.setText(contctno);

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {
            String sql = "Select DATE_FORMAT(invoicedate, '%d-%m-%Y') as 'Invoice Date',invoiceNo as 'Invoice No',paymentType as 'Mode', paymentCurency as 'Currency', cast(Totalinvoice as decimal(10,2)) as 'Total Invoice' from cashsale where customerid='" + cusId + "' ORDER BY invoiceNo DESC";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        invoiceDetails();
    }//GEN-LAST:event_customerboxPopupMenuWillBecomeInvisible

    private void overalsubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overalsubmitActionPerformed

        if (customerbox.getSelectedIndex() > 0) {
            try {
                String sql = "Select DATE_FORMAT(invoicedate, '%d-%m-%Y') as 'Invoice Date',invoiceNo as 'Invoice No',paymentType as 'Mode', paymentCurency as 'Currency', cast(Totalinvoice as decimal(10,2)) as 'Total Invoice' from cashsale cs where invoicedate BETWEEN ? AND ? AND customerid='" + cusId + "'";
                pst = conn.prepareStatement(sql);
                pst.setString(1, ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).getText());
                pst.setString(2, ((JTextField) todatepayment1.getDateEditor().getUiComponent()).getText());
                rs = pst.executeQuery();
                datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

            invoiceDetails_bydate();
        }


    }//GEN-LAST:event_overalsubmitActionPerformed

    private void reportboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportboxActionPerformed
        if (customerbox.getSelectedIndex() == 0 || ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).getText().isEmpty() || ((JTextField) todatepayment1.getDateEditor().getUiComponent()).getText().isEmpty()) {
            JOptionPane.showConfirmDialog(null, "Please Select Requirmetn Field", "Requirment", JOptionPane.CANCEL_OPTION);
        } else {
            String User;

            // int i=1;
            User = (String) customerbox.getSelectedItem();
            String fromdate = ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).getText();
            String todate = ((JTextField) todatepayment1.getDateEditor().getUiComponent()).getText();

            try {

                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/cashstatement.jrxml");
                //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

                HashMap para = new HashMap();
                para.put("fromdate", fromdate);
                para.put("todate", todate);

                para.put("customerid", cusId);
                para.put("customer", User);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {
                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }//GEN-LAST:event_reportboxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (customerbox.getSelectedIndex() == 0) {
            JOptionPane.showConfirmDialog(null, "Please Select Customer Name", "Requirment", JOptionPane.CANCEL_OPTION);
        } else {
            try {

                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/AllCashstatement.jrxml");
                //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

                HashMap para = new HashMap();

                para.put("customerid", cusId);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {
                JOptionPane.showMessageDialog(null, ex);

            }

        }

    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addresstext;
    private javax.swing.JLabel balancetypebox;
    private javax.swing.JLabel contactnotext;
    private javax.swing.JComboBox<String> customerbox;
    private javax.swing.JTable datatbl;
    private javax.swing.JTable datatbl1;
    private com.toedter.calendar.JDateChooser fromdatepayment1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton overalsubmit;
    private javax.swing.JButton reportbox;
    private javax.swing.JLabel salesAmounttext;
    private javax.swing.JLabel supliertypebox;
    private com.toedter.calendar.JDateChooser todatepayment1;
    private javax.swing.JComboBox<String> typebox;
    // End of variables declaration//GEN-END:variables
}