/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

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

/**
 *
 * @author adnan
 */
public class ItemForCast extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    /**
     * Creates new form ItemForCast
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public ItemForCast() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        ExtractFilter();
        ByItemCheck();
    }

    private void ByItemCheck() {
        if (byitemcheckbox.isSelected() == true) {
            itemnamesearch.setEditable(true);
        } else {
            itemnamesearch.setEditable(false);
        }
    }

    private void ExtractFilter() {
        final JTextField textfield = (JTextField) itemnamesearch.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        itemnamesearch.removeAllItems();

                    } else {
                        comboFilter(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFilter(String enteredText) {
        itemnamesearch.removeAllItems();
        itemnamesearch.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;
        try {
            String sql;
            sql = "Select itemformat from cashsaledetails WHERE lower(itemformat)  LIKE N'" + enteredText + "%' GROUP BY itemformat ORDER BY itemformat ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("itemformat");
                filterArray.add(str1);
                itemnamesearch.addItem(str1);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        if (filterArray.size() > 0) {
            itemnamesearch.setSelectedItem(enteredText);
            itemnamesearch.showPopup();
        } else {

            itemnamesearch.hidePopup();

        }
    }

    private float total_action_tp() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 5).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_mrp() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 6).toString());
        }

        return (float) totaltpmrp;

    }

    private float profit_total() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 7).toString());
        }

        return (float) totaltpmrp;

    }

    private void Table_Data() {
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 1;

        String filtertype = "All Cash Sale History";
        String countdatebetweeb = filtertype;
        countdaytextdata.setText(countdatebetweeb);
        try {
            String Name = (String) itemnamesearch.getSelectedItem();
            producttextdata.setText(Name);
            String sql = "SELECT "
                    + "prcode,"
                    + "itemformat as 'ItemName',"
                    + "tp,"
                    + "unitrate,"
                    + "SUM(qty) AS 'TotalQuantity',"
                    + " CAST(SUM(totalamount) AS DECIMAL(18,2)) AS 'TotalInvoice',"
                    + "CAST((SUM(qty)*tp) AS DECIMAL(18,2)) as 'totalTradeprice'"
                    + "  FROM cashsaledetails cd  GROUP BY prcode ORDER BY SUM(qty) DESC ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String procode = rs.getString("prcode");
                String Itemname = rs.getString("ItemName");
                double totalqty = rs.getDouble("TotalQuantity");
                double TotalInvoice = rs.getDouble("TotalInvoice");
                double totalTradeprice = rs.getDouble("totalTradeprice");
                double tp = rs.getDouble("tp");
                double unitrate = rs.getDouble("unitrate");
                double totalprofit = TotalInvoice - totalTradeprice;

                model2.addRow(new Object[]{tree, procode, Itemname, tp, unitrate, totalqty, totalTradeprice, TotalInvoice, totalprofit});
                tree++;

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        nettotaltradetext.setText(Float.toString(total_action_tp()));
        nettotalsalepricetext.setText(Float.toString(total_action_mrp()));
        profittext.setText(Float.toString(profit_total()));
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
        jLabel4 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        fromdatepayment = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        todatepayment = new com.toedter.calendar.JDateChooser();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        itemnamesearch = new javax.swing.JComboBox<>();
        byitemcheckbox = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        nettotaltradetext = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        nettotalsalepricetext = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        profittext = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        countdaytextdata = new javax.swing.JLabel();
        producttextdata = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Item For Cast");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Product Description");

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
        jButton5.setText("Submit");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton6.setText("Report");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Load All");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        itemnamesearch.setBackground(new java.awt.Color(255, 255, 255));
        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuCanceled(evt);
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        itemnamesearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                itemnamesearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                itemnamesearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                itemnamesearchKeyTyped(evt);
            }
        });

        byitemcheckbox.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        byitemcheckbox.setForeground(new java.awt.Color(255, 255, 255));
        byitemcheckbox.setText("By Item");
        byitemcheckbox.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                byitemcheckboxMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(byitemcheckbox))
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fromdatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(todatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel4)
                    .addComponent(byitemcheckbox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                            .addComponent(todatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fromdatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(4, 4, 4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Net Total(Trade Price):");

        nettotaltradetext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nettotaltradetext.setForeground(new java.awt.Color(255, 255, 255));
        nettotaltradetext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nettotaltradetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Net Total(Sale Price):");

        nettotalsalepricetext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nettotalsalepricetext.setForeground(new java.awt.Color(255, 255, 255));
        nettotalsalepricetext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nettotalsalepricetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Profit");

        profittext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        profittext.setForeground(new java.awt.Color(255, 255, 255));
        profittext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        profittext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(356, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nettotaltradetext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nettotalsalepricetext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(profittext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(profittext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(nettotalsalepricetext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(nettotaltradetext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "Product Code", "Product Name", "Trade Price", "Sale Price", "Total Qty", "Total Invoice", "Total Profit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class
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
        datatbl.setRowHeight(25);
        datatbl.setShowHorizontalLines(true);
        datatbl.setShowVerticalLines(true);
        jScrollPane1.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setResizable(false);
            datatbl.getColumnModel().getColumn(0).setPreferredWidth(30);
            datatbl.getColumnModel().getColumn(1).setResizable(false);
            datatbl.getColumnModel().getColumn(1).setPreferredWidth(30);
            datatbl.getColumnModel().getColumn(2).setResizable(false);
            datatbl.getColumnModel().getColumn(2).setPreferredWidth(200);
            datatbl.getColumnModel().getColumn(3).setResizable(false);
            datatbl.getColumnModel().getColumn(3).setPreferredWidth(100);
            datatbl.getColumnModel().getColumn(4).setResizable(false);
            datatbl.getColumnModel().getColumn(4).setPreferredWidth(100);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(5).setPreferredWidth(100);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setPreferredWidth(100);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setPreferredWidth(100);
        }

        countdaytextdata.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        countdaytextdata.setForeground(new java.awt.Color(0, 51, 51));
        countdaytextdata.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        producttextdata.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        producttextdata.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Product:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Query Type");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(producttextdata, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(countdaytextdata, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(countdaytextdata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(producttextdata, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 1;
        String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
        String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();
        if (fromdate.isEmpty() || todate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "please Select Date");
        } else {
            if (byitemcheckbox.isSelected() == true) {
                final JTextField textfield = (JTextField) itemnamesearch.getEditor().getEditorComponent();
                if (textfield.getText().isEmpty()) {

                } else {
                    String filtertype = "Date To Date By Product";
                    String countdatebetweeb = filtertype + ": " + fromdate + " To " + todate;
                    countdaytextdata.setText(countdatebetweeb);
                    try {
                        String Name = (String) itemnamesearch.getSelectedItem();
                        producttextdata.setText(Name);
                        String sql = "SELECT "
                                + "prcode,"
                                + "itemformat as 'ItemName',"
                                + "tp,"
                                + "unitrate,"
                                + "SUM(qty) AS 'TotalQuantity',"
                                + " CAST(SUM(totalamount) AS DECIMAL(18,2)) AS 'TotalInvoice',"
                                + "CAST((SUM(qty)*tp) AS DECIMAL(18,2)) as 'totalTradeprice'"
                                + "  FROM cashsaledetails cd WHERE invoicedate BETWEEN ? AND ? AND itemformat='" + itemnamesearch.getSelectedItem() + "'  GROUP BY prcode ORDER BY SUM(qty) DESC ";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
                        pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
                        rs = pst.executeQuery();
                        while (rs.next()) {
                            String procode = rs.getString("prcode");
                            String Itemname = rs.getString("ItemName");
                            double totalqty = rs.getDouble("TotalQuantity");
                            double TotalInvoice = rs.getDouble("TotalInvoice");
                            double totalTradeprice = rs.getDouble("totalTradeprice");
                            double tp = rs.getDouble("tp");
                            double unitrate = rs.getDouble("unitrate");
                            double totalprofit = TotalInvoice - totalTradeprice;

                            model2.addRow(new Object[]{tree, procode, Itemname, tp, unitrate, totalqty, totalTradeprice, TotalInvoice, totalprofit});
                            tree++;

                        }

                    } catch (SQLException | HeadlessException e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                }

            } else {

                String filtertype = "Date To Date By ALL Product";

                String countdatebetweeb = filtertype + ": " + fromdate + " To " + todate;
                countdaytextdata.setText(countdatebetweeb);
                try {
                    String sql = "SELECT "
                            + "prcode,"
                            + "itemformat as 'ItemName',"
                            + "tp,"
                            + "unitrate,"
                            + "SUM(qty) AS 'TotalQuantity',"
                            + " CAST(SUM(totalamount) AS DECIMAL(18,2)) AS 'TotalInvoice',"
                            + "CAST((SUM(qty)*tp) AS DECIMAL(18,2)) as 'totalTradeprice'"
                            + "  FROM cashsaledetails cd WHERE invoicedate BETWEEN ? AND ?  GROUP BY prcode ORDER BY SUM(qty) DESC ";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
                    pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
                    rs = pst.executeQuery();

                    while (rs.next()) {
                        String procode = rs.getString("prcode");
                        String Itemname = rs.getString("ItemName");
                        double totalqty = rs.getDouble("TotalQuantity");
                        double TotalInvoice = rs.getDouble("TotalInvoice");
                        double totalTradeprice = rs.getDouble("totalTradeprice");
                        double tp = rs.getDouble("tp");
                        double unitrate = rs.getDouble("unitrate");
                        double totalprofit = TotalInvoice - totalTradeprice;

                        model2.addRow(new Object[]{tree, procode, Itemname, tp, unitrate, totalqty, totalTradeprice, TotalInvoice, totalprofit});
                        tree++;

                    }

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
            }
            nettotaltradetext.setText(Float.toString(total_action_tp()));
            nettotalsalepricetext.setText(Float.toString(total_action_mrp()));
            profittext.setText(Float.toString(profit_total()));
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        if (model2.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {

            try {
                double totaltrade = Double.parseDouble(nettotaltradetext.getText());
                double totalinvoice = Double.parseDouble(nettotalsalepricetext.getText());
                double totalprofit = Double.parseDouble(profittext.getText());

                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/Itemforcast.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDataSource", ItemJRBean);
                para.put("product", producttextdata.getText());
                para.put("counttype", countdaytextdata.getText());
                para.put("totaltrade", totaltrade);
                para.put("totalinvoice", totalinvoice);
                para.put("totalprofit", totalprofit);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void itemnamesearchPopupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuCanceled
        // TODO add your handling code here:
    }//GEN-LAST:event_itemnamesearchPopupMenuCanceled

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible

    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void itemnamesearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyPressed

    }//GEN-LAST:event_itemnamesearchKeyPressed

    private void itemnamesearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_itemnamesearchKeyReleased

    private void itemnamesearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyTyped

    }//GEN-LAST:event_itemnamesearchKeyTyped

    private void byitemcheckboxMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_byitemcheckboxMouseClicked
        ByItemCheck();
        itemnamesearch.requestFocusInWindow();

    }//GEN-LAST:event_byitemcheckboxMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        byitemcheckbox.setSelected(false);
        producttextdata.setText(null);
        itemnamesearch.removeAllItems();
        ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).setText(null);
        ((JTextField) todatepayment.getDateEditor().getUiComponent()).setText(null);
        ByItemCheck();
        Table_Data();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox byitemcheckbox;
    private javax.swing.JLabel countdaytextdata;
    private javax.swing.JTable datatbl;
    private com.toedter.calendar.JDateChooser fromdatepayment;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nettotalsalepricetext;
    private javax.swing.JLabel nettotaltradetext;
    private javax.swing.JLabel producttextdata;
    private javax.swing.JLabel profittext;
    private com.toedter.calendar.JDateChooser todatepayment;
    // End of variables declaration//GEN-END:variables
}
