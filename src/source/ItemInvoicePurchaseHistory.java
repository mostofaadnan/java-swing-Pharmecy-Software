/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author FAHIM
 */
public class ItemInvoicePurchaseHistory extends javax.swing.JInternalFrame {

    private static final DecimalFormat df2 = new DecimalFormat("#.00");
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String generic_idsearch;

    /**
     * Creates new form ItemInvoicePurchaseHistory
     *
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public ItemInvoicePurchaseHistory() throws IOException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        ExtractFilter();
        unit(); 
    }
 private void unit() throws SQLException {

        try {
            String sql = "Select * from unit";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("unitshort");
                punibox.addItem(name);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

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

                        itemnamesearch.hidePopup();
                        genericbox.removeAllItems();
                        strengthtext.setText(null);
                        boxsizetext.setText(null);
                        mrptext.setText(null);
                        tptext.setText(null);
                        stocktext.setText(null);
                    } else {
                        comboFilter(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFilter(String enteredText) {
        itemnamesearch.removeAllItems();
        //  itemnamesearch.addItem("");
        itemnamesearch.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;

        try {

            String sql = "Select nameformate from item WHERE lower(nameformate)  LIKE '" + enteredText + "%'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("nameformate");
                filterArray.add(str1);
                itemnamesearch.addItem(str1);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        if (filterArray.size() > 0) {
            //  itemnamesearch.setModel(new DefaultComboBoxModel(filterArray.toArray()));

            itemnamesearch.setSelectedItem(enteredText);
            itemnamesearch.showPopup();

        } else {

            itemnamesearch.hidePopup();
            genericbox.removeAllItems();
            strengthtext.setText(null);
            boxsizetext.setText(null);
            mrptext.setText(null);
            tptext.setText(null);
            stocktext.setText(null);

        }
    }

    private void PurchaseDetailsPR(String table_click) throws SQLException {
        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) pdataTable.getModel();
        model2.setRowCount(0);
        try {

            String sql = "Select pr.prcode as 'id',"
                    + "(select ita.itemName from item ita where ita.Itemcode=pr.prcode) as 'Itemname',"
                    + "batch,"
                    + "purchaseCode,"
                    + "expdate,"
                    + "boxsize,"
                    + "unitrate,"
                    + "mrp,"
                    + " qty,"
                    + "unit,"
                    + "total"
                    + " from purchasedetails pr  where pr.prcode='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                tree++;
                String id = rs.getString("id");
                String purchaseCode = rs.getString("purchaseCode");
                String batch = rs.getString("batch");
                String expdates = rs.getString("expdate");
                String boxsize = rs.getString("boxsize");
                float unitrate = rs.getFloat("unitrate");
                float mrp = rs.getFloat("mrp");
                float qty = rs.getFloat("qty");
                float total = rs.getFloat("total");
                model2.addRow(new Object[]{tree, purchaseCode, batch, expdates, unitrate, qty, total});

            }
           

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void UnvviewData(String procode) throws SQLException {
        try {
            DefaultTableModel model2 = (DefaultTableModel) sdatatbl.getModel();
            model2.setRowCount(0);
            int tree = 0;
            String sql = "Select "
                    + "prcode,"
                    + "(select ci.customername from customerInfo ci where ci.customerid=sl.cusid) as 'customername',"
                    + "sl.batch as 'batch',"
                    + "sl.expdate as 'expdate',"
                    + "sl.invoiceNo as 'invoiceNo',"
                    + "Cast(sl.unitrate as decimal(10,2)) as 'UnitRate',"
                    + "sl.qty as 'Qty',"
                    + "sl.bonusqty as 'bonusqty',"
                    + "Cast(sl.totalamount as decimal(10,2)) as 'Amount'"
                    + "  from cashsaledetails sl where sl.prcode='" + procode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
               
                String invoiceNo = rs.getString("invoiceNo");
                String batch = rs.getString("batch");
                String expdate = rs.getString("expdate");
                String UnitRate = rs.getString("UnitRate");
                float Qty = rs.getFloat("Qty");
                float bonusqty = rs.getFloat("bonusqty");
                String Amount = rs.getString("Amount");
                String customername = rs.getString("customername");
                //   String GenericName = getGeneric(genericid);
                tree++;
                model2.addRow(new Object[]{tree, invoiceNo, customername, batch, expdate, UnitRate, Qty, bonusqty, Amount});

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private float totalpr() {
        int rowaCount = pdataTable.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());
            totaltpmrp = totaltpmrp + Float.parseFloat(pdataTable.getValueAt(i, 6).toString());
        }
        return (float) totaltpmrp;

    }

    private float totalqtypr() {
        int rowaCount = pdataTable.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            totaltpmrp = totaltpmrp + Float.parseFloat(pdataTable.getValueAt(i, 5).toString());
        }
        return (float) totaltpmrp;

    }

    private float totalsl() {
        int rowaCount = sdatatbl.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());
            totaltpmrp = totaltpmrp + Float.parseFloat(sdatatbl.getValueAt(i, 8).toString());
        }
        return (float) totaltpmrp;

    }

    private float totalqtysl() {
        int rowaCount = sdatatbl.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            totaltpmrp = totaltpmrp + Float.parseFloat(sdatatbl.getValueAt(i, 6).toString());
        }
        return (float) totaltpmrp;

    }

    private void TotalAction() {
        float totalpr = totalpr();
        totalprtext.setText(df2.format(totalpr));
        float totalqtypr = totalqtypr();
        totalqtyprtext.setText(df2.format(totalqtypr));
        float totalsl = totalsl();
        totalsltext.setText(df2.format(totalsl));
        float totalqtysl = totalqtysl();
        totalqtysltext.setText(df2.format(totalqtysl));
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
        jLabel12 = new javax.swing.JLabel();
        searchtext = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        genericbox = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        strengthtext = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        boxsizetext = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        tptext = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        mrptext = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        stocktext = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        dosbox = new javax.swing.JTextField();
        punibox = new javax.swing.JComboBox<>();
        jLabel51 = new javax.swing.JLabel();
        pvattext = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pdataTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        totalqtyprtext = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        totalprtext = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        sdatatbl = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        totalqtysltext = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        totalsltext = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Product Code");

        searchtext.setEditable(false);
        searchtext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        searchtext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchtext.setBorder(null);
        searchtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchtextKeyReleased(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Generic");

        genericbox.setEditable(true);
        genericbox.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        genericbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                genericboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Item Name");

        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Strength");

        strengthtext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        strengthtext.setForeground(new java.awt.Color(102, 0, 0));
        strengthtext.setBorder(null);

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("Box Size");

        boxsizetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        boxsizetext.setForeground(new java.awt.Color(153, 0, 0));
        boxsizetext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        boxsizetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                boxsizetextKeyPressed(evt);
            }
        });

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("TP");

        tptext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tptext.setForeground(new java.awt.Color(153, 0, 0));
        tptext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        tptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tptextKeyPressed(evt);
            }
        });

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setText("MRP");

        mrptext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        mrptext.setForeground(new java.awt.Color(153, 0, 0));
        mrptext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        mrptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mrptextKeyPressed(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("Prasent Stock");

        stocktext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        stocktext.setForeground(new java.awt.Color(153, 0, 0));
        stocktext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        stocktext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                stocktextKeyPressed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("DOS");

        dosbox.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        dosbox.setForeground(new java.awt.Color(102, 0, 0));
        dosbox.setBorder(null);

        punibox.setEditable(true);
        punibox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        punibox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        punibox.setBorder(null);
        punibox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                puniboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        punibox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                puniboxActionPerformed(evt);
            }
        });
        punibox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                puniboxPropertyChange(evt);
            }
        });
        punibox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                puniboxKeyPressed(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Vat");

        pvattext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pvattext.setForeground(new java.awt.Color(153, 0, 0));
        pvattext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        pvattext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pvattextKeyPressed(evt);
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dosbox, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(strengthtext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(boxsizetext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tptext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(stocktext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(punibox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(genericbox, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(2, 2, 2)
                        .addComponent(searchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(2, 2, 2)
                        .addComponent(genericbox))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(22, 22, 22)
                            .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel11)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(strengthtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel47)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(boxsizetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel48)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel49)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel50)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(stocktext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(punibox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel23)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(dosbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel51)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)), "Purchase"));

        pdataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl", "Invoice No", "Batch", "Exp Date", "TP", "Qty", "Amount"
            }
        ));
        pdataTable.setRowHeight(20);
        pdataTable.setShowHorizontalLines(true);
        pdataTable.setShowVerticalLines(true);
        jScrollPane1.setViewportView(pdataTable);
        if (pdataTable.getColumnModel().getColumnCount() > 0) {
            pdataTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            pdataTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        }

        jPanel4.setBackground(new java.awt.Color(0, 51, 51));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Total Qty:");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Total Amount");

        totalprtext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(191, 191, 191)
                .addComponent(jLabel1)
                .addGap(0, 0, 0)
                .addComponent(totalqtyprtext, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(totalprtext, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalprtext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalqtyprtext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)), "Invoice"));

        sdatatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl", "Invoice No", "Customer", "Batch", "Exp Date", "MRP", "Qty", "bonus Qty", "Amount"
            }
        ));
        sdatatbl.setRowHeight(20);
        sdatatbl.setShowHorizontalLines(true);
        sdatatbl.setShowVerticalLines(true);
        jScrollPane2.setViewportView(sdatatbl);
        if (sdatatbl.getColumnModel().getColumnCount() > 0) {
            sdatatbl.getColumnModel().getColumn(0).setPreferredWidth(50);
            sdatatbl.getColumnModel().getColumn(1).setPreferredWidth(100);
            sdatatbl.getColumnModel().getColumn(2).setPreferredWidth(200);
        }

        jPanel5.setBackground(new java.awt.Color(0, 51, 51));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total Qty:");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Total Amount");

        totalsltext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalqtysltext, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(0, 0, 0)
                .addComponent(totalsltext, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalqtysltext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalsltext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void searchtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchtextKeyReleased

        try {

            String sql = "Select "
                    + "itemcode,"
                    + "itemName,"
                    + "openingDate,"
                    + "category,"
                    + "generic_id,"
                    + "(Select generic_name from generic sinfo where sinfo.generic_id=it.generic_id) as 'genericname',"
                    + "company_id,"
                    + "(Select supliyername from suplyierinfo sinfo where sinfo.id=it.company_id) as 'companyname',"
                    + "brand_id,"
                    + "(Select brand_name from brand sinfo where sinfo.id=it.brand_id) as 'brandname',"
                    + "dos_id,"
                    + "(Select ShortName from medicinforms sinfo where sinfo.FormId=it.dos_id) as 'fromname',"
                    + "strangth,"
                    + "boxSize,"
                    + "expdate,"
                    + "openingDate,"
                    + "tp,"
                    + "pvat,"
                    + "pdiscount,"
                    + "ptpwv,"
                    + "ptpwd,"
                    + "mrp,"
                    + "sdiscount,"
                    + "smrpwv,"
                    + "pmrpwd,"
                    + "profite,"
                    + "profiteParcentage,"
                    + "stockunit,"
                    + "batch,"
                    + "stockunit,"
                    + "(select unitshort  from unit un where un.id=it.stockunit) as 'unitname'"
                    + "from item it where it.Itemcode='" + searchtext.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String Name = rs.getString("itemName");

                itemnamesearch.setSelectedItem(Name);

                String genericname = rs.getString("genericname");
                genericbox.setSelectedItem(genericname);
                String brandname = rs.getString("brandname");
                // brandbox.setSelectedItem(brandname);
                String companyname = rs.getString("companyname");
                // supplierbox.setSelectedItem(companyname);
                String fromname = rs.getString("fromname");
                // dosbox.setSelectedItem(fromname);
                String strangth = rs.getString("strangth");
                strengthtext.setText(strangth);
                String boxSize = rs.getString("boxSize");
                boxsizetext.setText(boxSize);
                String expdate = rs.getString("expdate");
                // expdatetext.setText(expdate);
                String unitname = rs.getString("unitname");
                //   punibox.setSelectedItem(unitname);
                String batch = rs.getString("batch");
                //  batchtext.setText(batch);
                //  Date date = rs.getDate("openingDate");
                //  openingdate.setDate(date);
                String tpprice = rs.getString("tp");
                tptext.setText(tpprice);
                String pvat = rs.getString("pvat");
                // pvattext.setText(pvat);
                String pdiscount = rs.getString("pdiscount");
                //  pdiscountext.setText(pdiscount);
                String ptpwd = rs.getString("ptpwd");
                //  tpwdtext.setText(ptpwd);
                String ptpwv = rs.getString("ptpwv");
                //  ptotaltptext.setText(ptpwv);
                String marpprice = rs.getString("mrp");
                mrptext.setText(marpprice);
                String sdiscount = rs.getString("sdiscount");
                // sdiscounttext.setText(sdiscount);
                String smrpwv = rs.getString("smrpwv");
                // stotalmrp.setText(smrpwv);
                String pmrpwd = rs.getString("pmrpwd");
                // mrpwdtext.setText(pmrpwd);
                String profite = rs.getString("profite");
                // profitetext.setText(profite);
                String profiteParcentage = rs.getString("profiteParcentage");
                // profiteparcentagetext.setText(profiteParcentage);

            } else {
                /*
                nametext.setText(null);
                genericbox.removeAllItems();
                categorybox.setSelectedIndex(0);
                strengthtext.setText(null);
                brandbox.removeAllItems();
                supplierbox.removeAllItems();
                dosbox.removeAllItems();
                boxsizetext.setText(null);
                batchtext.setText(null);
                expdatetext.setText(null);
                punibox.setSelectedIndex(0);
                tptext.setText("0");
                pvattext.setText("0");
                pdiscountext.setText("0");
                tpwdtext.setText("0");
                ptotaltptext.setText("0");

                mrptext.setText("0");

                sdiscounttext.setText("0");
                mrpwdtext.setText("0");
                stotalmrp.setText("0");
                profitetext.setText("0");
                profiteparcentagetext.setText("0%");

                catid = null;
                brandid = null;
                generic_id = null;
                company_id = null;
                dosid = null;
                punitid = null;
                sunitid = null;
                updatekey = 0;
                genericbox.removeAllItems();
                 */
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_searchtextKeyReleased

    private void genericboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_genericboxPopupMenuWillBecomeInvisible
        try {
            String generic = (String) genericbox.getSelectedItem();
            String sql = "Select generic_id from generic where generic_name='" + generic + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                generic_idsearch = rs.getString("generic_id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        itemnamesearch.removeAllItems();
        itemnamesearch.addItem("Select");
        itemnamesearch.setSelectedIndex(0);
        try {
            String sql = "Select itemName from item where generic_id='" + generic_idsearch + "'  ORDER BY itemName ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("itemName");
                itemnamesearch.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_genericboxPopupMenuWillBecomeInvisible
    private String GetStrock(String itemcode) {
        try {
            String sql = "Select qty from stockdetails where itemcode='" + itemcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String stock = rs.getString("qty");
                return stock;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return null;
    }
    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible

        try {
            String searchcode = (String) itemnamesearch.getSelectedItem();
            String sql = "Select "
                    + "itemcode,"
                    + "itemName,"
                    + "nameformate,"
                    + "openingDate,"
                    + "category,"
                    + "generic_id,"
                    + "(Select generic_name from generic sinfo where sinfo.generic_id=it.generic_id) as 'genericname',"
                    + "company_id,"
                    + "(Select supliyername from suplyierinfo sinfo where sinfo.id=it.company_id) as 'companyname',"                 
                    + "dos_id,"
                    + "(Select ShortName from medicinforms sinfo where sinfo.FormId=it.dos_id) as 'fromname',"
                    + "strangth,"
                    + "boxSize,"
                    + "expdate,"
                    + "openingDate,"
                    + "tp,"
                    + "pvat,"
                    + "pdiscount,"
                    + "ptpwv,"
                    + "ptpwd,"
                    + "mrp,"
                    + "sdiscount,"
                    + "smrpwv,"
                    + "pmrpwd,"
                    + "profite,"
                    + "profiteParcentage,"
                    + "stockunit,"
                    + "batch,"
                    + "stockunit,"
                    + "(select unitshort  from unit un where un.id=it.stockunit) as 'unitname'"
                    + "from item it where it.nameformate='" + searchcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("itemcode");
                searchtext.setText(Id);
                String Name = rs.getString("nameformate");
                String genericname = rs.getString("genericname");
                genericbox.setSelectedItem(genericname);  
                String fromname = rs.getString("fromname");
                dosbox.setText(fromname);
                String strangth = rs.getString("strangth");
                strengthtext.setText(strangth);
                String boxSize = rs.getString("boxSize");
                boxsizetext.setText(boxSize);
                String unitname = rs.getString("unitname");
                punibox.setSelectedItem(unitname);
                String tpprice = rs.getString("tp");
                tptext.setText(tpprice);
                String pvat = rs.getString("pvat");
                pvattext.setText(pvat);
                String marpprice = rs.getString("mrp");
                mrptext.setText(marpprice);          
                stocktext.setText(GetStrock(Id));
                PurchaseDetailsPR(Id);
                UnvviewData(Id);
                TotalAction();
            } else {
                //  clear();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void boxsizetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_boxsizetextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxsizetextKeyPressed

    private void tptextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tptextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tptextKeyPressed

    private void mrptextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrptextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_mrptextKeyPressed

    private void stocktextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stocktextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_stocktextKeyPressed

    private void puniboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_puniboxPopupMenuWillBecomeInvisible

    }//GEN-LAST:event_puniboxPopupMenuWillBecomeInvisible

    private void puniboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_puniboxActionPerformed

    }//GEN-LAST:event_puniboxActionPerformed

    private void puniboxPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_puniboxPropertyChange

    }//GEN-LAST:event_puniboxPropertyChange

    private void puniboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_puniboxKeyPressed

    }//GEN-LAST:event_puniboxKeyPressed

    private void pvattextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pvattextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_pvattextKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField boxsizetext;
    private javax.swing.JTextField dosbox;
    private javax.swing.JComboBox<String> genericbox;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField mrptext;
    private javax.swing.JTable pdataTable;
    private javax.swing.JComboBox<String> punibox;
    private javax.swing.JTextField pvattext;
    private javax.swing.JTable sdatatbl;
    private javax.swing.JTextField searchtext;
    private javax.swing.JTextField stocktext;
    private javax.swing.JTextField strengthtext;
    private javax.swing.JTextField totalprtext;
    private javax.swing.JTextField totalqtyprtext;
    private javax.swing.JTextField totalqtysltext;
    private javax.swing.JTextField totalsltext;
    private javax.swing.JTextField tptext;
    // End of variables declaration//GEN-END:variables
}
