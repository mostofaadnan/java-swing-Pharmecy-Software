/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.io.File;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author adnan
 */
public class StockDemand extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String catid = null;
    String scatid = null;
    String company_id = null;

    /**
     * Creates new form StockDemand
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public StockDemand() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        ExtractFilterComapny();
    }

    private void ExtractFilterComapny() {
        final JTextField textfield = (JTextField) supplierbox.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        supplierbox.removeAllItems();

                    } else {
                        comboFilterComany(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFilterComany(String enteredText) {
        supplierbox.removeAllItems();
        supplierbox.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;
        try {
            String sql = "Select DISTINCT  supliyername from suplyierinfo WHERE lower(supliyername)  LIKE '%" + enteredText + "%' ORDER BY supliyername ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("supliyername");
                filterArray.add(str1);
                supplierbox.addItem(str1);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        if (filterArray.size() > 0) {
            supplierbox.setSelectedItem(enteredText);
            supplierbox.showPopup();

        } else {

            supplierbox.hidePopup();

        }
    }

    private void table_update() throws SQLException {
        int tree = 1;
        double totaltradeprice;
        double totalsaleeprice;
        try {

            String sql = "SELECT "
                    + "ita.itemcode as 'Code',"
                    + "ita.itemname as 'Item',"
                    + "(select name from category  cat where cat.id=ita.category) as 'Category',"
                    + "(select supliyername from suplyierinfo sup where sup.id=ita.company_id) as 'suplyer',"
                    + "ita.tp as 'tradeprice',"
                    + "ita.mrp as 'saleprice',"
                    + "st.Qty as 'Stock',"
                    + "(select unitshort from unit un where un.id=ita.stockunit) as 'unit'"
                    + "  FROM item ita inner join stockdetails st ON ita.itemcode=st.itemcode where st.Qty BETWEEN 1 AND 5";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String procode = rs.getString("Code");
                String Itemname = rs.getString("Item");
                String category = rs.getString("category");

                double tp = rs.getDouble("tradeprice");
                double mrp = rs.getDouble("saleprice");
                float qty = rs.getFloat("Stock");
                String unit = rs.getString("Unit");
                String suplyer = rs.getString("suplyer");
                if (qty < 0) {

                    totaltradeprice = 0;
                    totalsaleeprice = 0;

                } else {

                    totaltradeprice = qty * tp;
                    totalsaleeprice = qty * mrp;

                }

                double profite = totalsaleeprice - totalsaleeprice;
                DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();

                model2.addRow(new Object[]{tree, procode, Itemname, category, suplyer, qty, unit, tp, mrp, totaltradeprice, totalsaleeprice});
                tree++;

            }

        } catch (SQLException | HeadlessException e) {
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

        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        supplierbox = new javax.swing.JComboBox<>();
        jButton6 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Stock Demand");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Supplier");

        supplierbox.setEditable(true);
        supplierbox.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        supplierbox.setForeground(new java.awt.Color(102, 102, 102));
        supplierbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                supplierboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        supplierbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierboxActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(102, 102, 102));
        jButton6.setText("Report");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton1.setText("Submit");
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
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(supplierbox, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(0, 0, 0)
                .addComponent(jButton1)
                .addGap(0, 0, 0)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(supplierbox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton6)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setBorder(null);
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl.setForeground(new java.awt.Color(51, 51, 51));
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Si No", "Product Code", "Product Name", "Batch", "Exp Date", "Box Size", "Stock", "Unit", "TP", "MRP"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        datatbl.setGridColor(new java.awt.Color(204, 204, 204));
        datatbl.setMaximumSize(new java.awt.Dimension(2147483647, 12235555));
        datatbl.setRowHeight(25);
        datatbl.setShowHorizontalLines(true);
        datatbl.setShowVerticalLines(true);
        datatbl.getTableHeader().setResizingAllowed(false);
        datatbl.getTableHeader().setReorderingAllowed(false);
        datatbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatblMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(2).setPreferredWidth(300);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void supplierboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supplierboxPopupMenuWillBecomeInvisible
        try {
            String comapany = (String) supplierbox.getSelectedItem();
            String sql = "Select id from suplyierinfo where supliyername='" + comapany + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                company_id = rs.getString("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_supplierboxPopupMenuWillBecomeInvisible

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        if (model2.getRowCount() == 0 || company_id == null) {
            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {

            try {
                String company = (String) supplierbox.getSelectedItem();

                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/StockDemand.jrxml");

                Map<String, Object> para = new HashMap<>();
                para.put("ItemDatasource", ItemJRBean);
                para.put("company", company);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void supplierboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierboxActionPerformed

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked

    }//GEN-LAST:event_datatblMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (company_id == null) {

        } else {
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            int tree = 1;
            try {
                String sql = "SELECT "
                        + "ita.Itemcode as 'Code',"
                        + "ita.nameformate as 'Item',"
                        + "ita.batch as 'batch',"
                        + "ita.expdate as 'expdate',"
                        + "ita.boxsize as 'boxsize',"
                        + "ita.tp as 'tradeprice',"
                        + "ita.mrp as 'saleprice',"
                        + "st.Qty as 'Stock',"
                        + "(select unitshort from unit un where un.id=ita.stockunit) as 'unit' "
                        + " FROM item ita inner join stockdetails st inner join purchasedetails pr ON ita.itemcode=st.itemcode AND st.Itemcode=pr.prcode WHERE pr.supid='" + company_id + "' GROUP BY ita.nameformate";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    String procode = rs.getString("Code");
                    String Itemname = rs.getString("Item");
                    String batch = rs.getString("batch");
                    String expdate = rs.getString("expdate");
                    String boxsize = rs.getString("boxsize");
                    double tp = rs.getDouble("tradeprice");
                    double mrp = rs.getDouble("saleprice");
                    int qty = rs.getInt("Stock");
                    String unit = rs.getString("Unit");       
                    model2.addRow(new Object[]{tree, procode, Itemname, batch, expdate, boxsize, qty, unit, tp, mrp});
                    tree++;

                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private float saleQty(String prcode) {
        float qty = 0;
        try {
            String sql = "Select "
                    + "sum(sl.qty) as 'Qty'"
                    + "  from saledetails sl where sl.prcode='" + prcode + "' GROUP BY prcode";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                qty = rs.getFloat("Qty");
                return qty;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return 0;
    }

    private float purchaseQty(String prcode) {
        float qty = 0;
        try {
            String sql = "Select "
                    + "sum(sl.qty) as 'Qty'"
                    + "  from purchasedetails sl where sl.prcode='" + prcode + "' GROUP BY prcode";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                qty = rs.getFloat("Qty");
                return qty;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return 0;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable datatbl;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> supplierbox;
    // End of variables declaration//GEN-END:variables
}
