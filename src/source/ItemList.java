/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.beans.PropertyVetoException;
import java.io.File;

import java.io.IOException;

import java.sql.Connection;
import javax.swing.table.DefaultTableModel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
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

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class ItemList extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int catid;
    String generic_id;

    /**
     * Creates new form ItemList
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public ItemList() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        Item();
        AutoCompleteDecorator.decorate(itemnamesearch);
        itemcount();
        TableDesign();
        ExtractFilterGeneric();
        jButton9.hide();
     //   jLabel5.hide();
      //  itemcounttext.hide();
        jButton7.hide();
    }

    private void ExtractFilterGeneric() {
        final JTextField textfield = (JTextField) genericbox.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        genericbox.removeAllItems();

                    } else {
                        comboFiltergeneric(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFiltergeneric(String enteredText) {
        genericbox.removeAllItems();
        //  itemnamesearch.addItem("");
        genericbox.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;

        try {

            String sql = "Select DISTINCT  generic_name from generic WHERE lower(generic_name)  LIKE '%" + enteredText + "%' ORDER BY generic_name ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("generic_name");
                filterArray.add(str1);
                genericbox.addItem(str1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        if (filterArray.size() > 0) {

            genericbox.setSelectedItem(enteredText);
            genericbox.showPopup();
        } else {
            genericbox.hidePopup();

        }
    }

    private void TableDesign() {
        JTableHeader jtblheader = datatbl.getTableHeader();
        jtblheader.setBackground(Color.WHITE);
        jtblheader.setForeground(Color.BLACK);
        jtblheader.setFont(new Font("Times New Roman", Font.BOLD, 12));
        ((DefaultTableCellRenderer) jtblheader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        datatbl.setDefaultRenderer(Object.class, centerRenderer);
    }

    private void Item() throws SQLException {

        try {
            String sql = "Select itemName from item ORDER BY itemName ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("itemName");
                itemnamesearch.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void itemcount() {
        try {
            String sql = "Select count(id) as 'itemcount' from item";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("itemcount");
                itemcounttext.setText(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void table_update() throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select "
                    + "Itemcode as 'Code',"
                    + "itemname as 'Item',"
                    + "category,"
                    + "(select generic_name from generic sup where sup.generic_id=it.generic_id ) as 'genericname',"
                    + "(select supliyername from suplyierinfo sup where sup.id=it.company_id ) as 'companyname',"
                    + "(select ShortName from medicinforms sup where sup.FormId=it.dos_id) as 'dosname',"
                    + " (select un.unitshort from unit un where un.id=it.stockunit) as 'stockunit',"
                    + " strangth ,"
                    + " batch ,"
                    + " boxSize ,"
                    + " expdate ,"
                    + " pvat ,"
                    + " tp as 'PurchasePrice' ,"
                    + " pdiscount ,"
                    + " ptpwv ,"
                    + " ptpwd ,"
                    + "mrp as 'SalePrice',"
                    + " sdiscount ,"
                    + " smrpwv ,"
                    + " pmrpwd ,"
                    + " profite ,"
                    + " profiteParcentage ,"
                    + "openingDate as 'InputDate',"
                    + "(select ad.name from admin ad where ad.id=it.inputuser) as 'InputUser',"
                    + "(select ad.name from admin ad where ad.id=it.updateuser) as 'UpdateUser',"
                    + "lastupdate"
                    + " from item it ORDER BY it.id ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //  datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String procode = rs.getString("Code");
                String Itemname = rs.getString("Item");
                String category = rs.getString("Category");
                String generic = rs.getString("genericname");
                String companyname = rs.getString("companyname");

                String dosname = rs.getString("dosname");
                String strangth = rs.getString("strangth");
                String boxSize = rs.getString("boxSize");
                String batch = rs.getString("batch");
                String expdate = rs.getString("expdate");
                String unit = rs.getString("stockunit");
                double vat = rs.getDouble("pvat");
                double tp = rs.getDouble("PurchasePrice");
                double pdiscount = rs.getDouble("pdiscount");
                double ptpwv = rs.getDouble("ptpwv");
                double ptpwd = rs.getDouble("ptpwd");
                double mrp = rs.getDouble("SalePrice");
                double sdiscount = rs.getDouble("sdiscount");
                double smrpwv = rs.getDouble("smrpwv");
                double pmrpwd = rs.getDouble("pmrpwd");
                String openingdate = rs.getString("InputDate");
                String inputuser = rs.getString("InputUser");
                String updateuser = rs.getString("UpdateUser");
                String lastupdate = rs.getString("lastupdate");
                tree++;

                model2.addRow(new Object[]{tree, procode, Itemname, strangth, generic, category, companyname, dosname, boxSize, unit, batch, expdate, vat, tp, pdiscount, ptpwd, ptpwv, mrp, sdiscount, pmrpwd, smrpwv, inputuser, updateuser, lastupdate});

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

        tablemenu = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        procodeserchtext = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        genericbox = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        itemcounttext = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();

        tablemenu.setBackground(new java.awt.Color(255, 255, 255));
        tablemenu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tablemenu.setAlignmentX(0.5F);
        tablemenu.setBorder(null);
        tablemenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tablemenu.setLabel("Table Menu");
        tablemenu.setPreferredSize(new java.awt.Dimension(300, 80));

        jMenuItem1.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/item.png"))); // NOI18N
        jMenuItem1.setText("View");
        jMenuItem1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jMenuItem1.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/image/item.png"))); // NOI18N
        jMenuItem1.setMargin(new java.awt.Insets(1, 1, 5, 1));
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        tablemenu.add(jMenuItem1);

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Item List");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Product Code");

        procodeserchtext.setBorder(null);
        procodeserchtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                procodeserchtextKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Brand Name");

        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        itemnamesearch.setForeground(new java.awt.Color(102, 102, 102));
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Report");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setText("Load All");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Generic Name");

        genericbox.setEditable(true);
        genericbox.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        genericbox.setForeground(new java.awt.Color(102, 102, 102));
        genericbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                genericboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(procodeserchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(genericbox, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(procodeserchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(genericbox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );

        jButton2.setBackground(new java.awt.Color(67, 86, 86));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/graph.png"))); // NOI18N
        jButton2.setText("Item Forcast");
        jButton2.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/source/fruit.jpg"))); // NOI18N
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(67, 86, 86));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/barcode.png"))); // NOI18N
        jButton3.setText("Print Label");
        jButton3.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/source/fruit.jpg"))); // NOI18N
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(67, 86, 86));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/if_plus_1646001 (1).png"))); // NOI18N
        jButton6.setText("New Item");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(67, 86, 86));
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/price-tag.png"))); // NOI18N
        jButton7.setText("Price Update");
        jButton7.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/source/fruit.jpg"))); // NOI18N
        jButton7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(67, 86, 86));
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search.png"))); // NOI18N
        jButton8.setText("Item Search");
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Total item:");

        itemcounttext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        itemcounttext.setForeground(new java.awt.Color(102, 51, 0));

        jButton9.setBackground(new java.awt.Color(67, 86, 86));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/search.png"))); // NOI18N
        jButton9.setText("Item Description");
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButton9.setEnabled(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemcounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton3)
                                .addComponent(jButton2))
                            .addComponent(itemcounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton7)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton8)
                                .addComponent(jButton9)))
                        .addGap(15, 15, 15))))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane2.setViewportBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "Code", "Product Name", "Strangth", "Generic", "Category", "Company", "Dos", "Box Size", "Unit", "Batch", "Exp Date", "VAT", "TP", "TD", "TDT", "TVT", "MRP", "MD", "MDT", "MVT", "Input User", "Update User", "Last Update"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class, java.lang.Object.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class, java.lang.Object.class, java.lang.Float.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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
        jScrollPane2.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setResizable(false);
            datatbl.getColumnModel().getColumn(0).setPreferredWidth(30);
            datatbl.getColumnModel().getColumn(1).setResizable(false);
            datatbl.getColumnModel().getColumn(2).setResizable(false);
            datatbl.getColumnModel().getColumn(2).setPreferredWidth(100);
            datatbl.getColumnModel().getColumn(3).setResizable(false);
            datatbl.getColumnModel().getColumn(4).setResizable(false);
            datatbl.getColumnModel().getColumn(4).setPreferredWidth(150);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setPreferredWidth(200);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
            datatbl.getColumnModel().getColumn(8).setResizable(false);
            datatbl.getColumnModel().getColumn(9).setResizable(false);
            datatbl.getColumnModel().getColumn(10).setResizable(false);
            datatbl.getColumnModel().getColumn(11).setResizable(false);
            datatbl.getColumnModel().getColumn(12).setResizable(false);
            datatbl.getColumnModel().getColumn(13).setResizable(false);
            datatbl.getColumnModel().getColumn(14).setResizable(false);
            datatbl.getColumnModel().getColumn(15).setResizable(false);
            datatbl.getColumnModel().getColumn(16).setResizable(false);
            datatbl.getColumnModel().getColumn(17).setResizable(false);
            datatbl.getColumnModel().getColumn(18).setResizable(false);
            datatbl.getColumnModel().getColumn(19).setResizable(false);
            datatbl.getColumnModel().getColumn(20).setResizable(false);
            datatbl.getColumnModel().getColumn(21).setResizable(false);
            datatbl.getColumnModel().getColumn(21).setPreferredWidth(100);
            datatbl.getColumnModel().getColumn(22).setResizable(false);
            datatbl.getColumnModel().getColumn(23).setResizable(false);
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 2297, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanel4);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        newMedicine newitm = null;

        try {
            newitm = new newMedicine();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
        }

        newitm.setVisible(true);
        this.getDesktopPane().add(newitm);

        //Homedesktop.add(It);
        newitm.setVisible(true);

        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = newitm.getSize();
        newitm.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        newitm.moveToFront();


    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        BarcodeMaker barcodemaker = null;
        try {
            barcodemaker = new BarcodeMaker();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
        }
        barcodemaker.setVisible(true);
        this.getDesktopPane().add(barcodemaker);
        barcodemaker.setVisible(true);
        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = barcodemaker.getSize();
        barcodemaker.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        barcodemaker.moveToFront();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
        if (itemnamesearch.getSelectedIndex() > 0) {
            String SearchText = (String) itemnamesearch.getSelectedItem();
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            int tree = 0;
            try {
                String sql = "Select "
                        + "Itemcode as 'Code',"
                        + "itemname as 'Item',"
                        + "category,"
                        + "(select generic_name from generic sup where sup.generic_id=it.generic_id ) as 'genericname',"
                        + "(select supliyername from suplyierinfo sup where sup.id=it.company_id ) as 'companyname',"
                        + "(select ShortName from medicinforms sup where sup.FormId=it.dos_id) as 'dosname',"
                        + " (select un.unitshort from unit un where un.id=it.stockunit) as 'stockunit',"
                        + " strangth ,"
                        + " batch ,"
                        + " boxSize ,"
                        + " expdate ,"
                        + " pvat ,"
                        + " tp as 'PurchasePrice' ,"
                        + " pdiscount ,"
                        + " ptpwv ,"
                        + " ptpwd ,"
                        + "mrp as 'SalePrice',"
                        + " sdiscount ,"
                        + " smrpwv ,"
                        + " pmrpwd ,"
                        + " profite ,"
                        + " profiteParcentage ,"
                        + "openingDate as 'InputDate',"
                        + "(select ad.name from admin ad where ad.id=it.inputuser) as 'InputUser',"
                        + "(select ad.name from admin ad where ad.id=it.updateuser) as 'UpdateUser',"
                        + "lastupdate"
                        + " from item it where it.itemName='" + SearchText + "' ";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //  datatbl.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    String procode = rs.getString("Code");
                    String Itemname = rs.getString("Item");
                    String category = rs.getString("Category");
                    String generic = rs.getString("genericname");
                    String companyname = rs.getString("companyname");
                    String dosname = rs.getString("dosname");
                    String strangth = rs.getString("strangth");
                    String boxSize = rs.getString("boxSize");
                    String batch = rs.getString("batch");
                    String expdate = rs.getString("expdate");
                    String unit = rs.getString("stockunit");
                    double vat = rs.getDouble("pvat");
                    double tp = rs.getDouble("PurchasePrice");
                    double pdiscount = rs.getDouble("pdiscount");
                    double ptpwv = rs.getDouble("ptpwv");
                    double ptpwd = rs.getDouble("ptpwd");
                    double mrp = rs.getDouble("SalePrice");
                    double sdiscount = rs.getDouble("sdiscount");
                    double smrpwv = rs.getDouble("smrpwv");
                    double pmrpwd = rs.getDouble("pmrpwd");
                    String openingdate = rs.getString("InputDate");
                    String inputuser = rs.getString("InputUser");
                    String updateuser = rs.getString("UpdateUser");
                    String lastupdate = rs.getString("lastupdate");
                    tree++;

                    model2.addRow(new Object[]{tree, procode, Itemname, strangth, generic, category, companyname, dosname, boxSize, unit, batch, expdate, vat, tp, pdiscount, ptpwd, ptpwv, mrp, sdiscount, pmrpwd, smrpwv, inputuser, updateuser, lastupdate});

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        } else {
            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed

        int row = datatbl.getSelectedRow();
        String table_click = (datatbl.getModel().getValueAt(row, 1).toString());

        newMedicine filte = null;

        try {
            filte = new newMedicine(table_click);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
        }

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
        filte.setEnabled(true);
        try {
            filte.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
        }
        //this.dispose();
        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        filte.moveToFront();

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ItemForCast itemforacst = null;

        try {
            itemforacst = new ItemForCast();
        } catch (SQLException | IOException ecten) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ecten);
        }

        itemforacst.setVisible(true);
        this.getDesktopPane().add(itemforacst);

        itemforacst.setVisible(true);

        Dimension desksize = getDesktopPane().getSize();
        Dimension framesize = itemforacst.getSize();
        itemforacst.setLocation((desksize.width - framesize.width) / 2,
                (desksize.height - framesize.height) / 2);
        itemforacst.moveToFront();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        ItempriceUpdate priceupdate = null;

        try {
            priceupdate = new ItempriceUpdate();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
        }

        priceupdate.setVisible(true);
        this.getDesktopPane().add(priceupdate);

        //Homedesktop.add(It);
        priceupdate.setVisible(true);

        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = priceupdate.getSize();
        priceupdate.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        priceupdate.moveToFront();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        ItemSearch itemsearch = null;

        try {
            itemsearch = new ItemSearch();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
        }
        itemsearch.setVisible(!itemsearch.isVisible());
    }//GEN-LAST:event_jButton8ActionPerformed

    private void procodeserchtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_procodeserchtextKeyReleased
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select "
                    + "Itemcode as 'Code',"
                    + "itemname as 'Item',"
                    + "category,"
                    + "(select generic_name from generic sup where sup.generic_id=it.generic_id ) as 'genericname',"
                    + "(select supliyername from suplyierinfo sup where sup.id=it.company_id ) as 'companyname',"
                    + "(select ShortName from medicinforms sup where sup.FormId=it.dos_id) as 'dosname',"
                    + " (select un.unitshort from unit un where un.id=it.stockunit) as 'stockunit',"
                    + " strangth ,"
                    + " batch ,"
                    + " boxSize ,"
                    + " expdate ,"
                    + " pvat ,"
                    + " tp as 'PurchasePrice' ,"
                    + " pdiscount ,"
                    + " ptpwv ,"
                    + " ptpwd ,"
                    + "mrp as 'SalePrice',"
                    + " sdiscount ,"
                    + " smrpwv ,"
                    + " pmrpwd ,"
                    + " profite ,"
                    + " profiteParcentage ,"
                    + "openingDate as 'InputDate',"
                    + "(select ad.name from admin ad where ad.id=it.inputuser) as 'InputUser',"
                    + "(select ad.name from admin ad where ad.id=it.updateuser) as 'UpdateUser',"
                    + "lastupdate"
                    + " from item it where it.Itemcode='" + procodeserchtext.getText() + "' ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //  datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String procode = rs.getString("Code");
                String Itemname = rs.getString("Item");
                String category = rs.getString("Category");
                String generic = rs.getString("genericname");
                String companyname = rs.getString("companyname");
                String dosname = rs.getString("dosname");
                String strangth = rs.getString("strangth");
                String boxSize = rs.getString("boxSize");
                String batch = rs.getString("batch");
                String expdate = rs.getString("expdate");
                String unit = rs.getString("stockunit");
                double vat = rs.getDouble("pvat");
                double tp = rs.getDouble("PurchasePrice");
                double pdiscount = rs.getDouble("pdiscount");
                double ptpwv = rs.getDouble("ptpwv");
                double ptpwd = rs.getDouble("ptpwd");
                double mrp = rs.getDouble("SalePrice");
                double sdiscount = rs.getDouble("sdiscount");
                double smrpwv = rs.getDouble("smrpwv");
                double pmrpwd = rs.getDouble("pmrpwd");
                String openingdate = rs.getString("InputDate");
                String inputuser = rs.getString("InputUser");
                String updateuser = rs.getString("UpdateUser");
                String lastupdate = rs.getString("lastupdate");
                tree++;

                model2.addRow(new Object[]{tree, procode, Itemname, strangth, generic, category, companyname, dosname, boxSize, unit, batch, expdate, vat, tp, pdiscount, ptpwd, ptpwv, mrp, sdiscount, pmrpwd, smrpwv, inputuser, updateuser, lastupdate});

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_procodeserchtextKeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        if (model2.getRowCount() == 0) {

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {

            try {

                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/ItemList.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDataSource", ItemJRBean);
                // para.put("product", producttextdata.getText());

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        ItemDescrption filte = null;
        try {
            filte = new ItemDescrption();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
        }

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
        filte.setEnabled(true);
        try {
            filte.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
        }
        //this.dispose();
        Dimension desktopSize = getDesktopPane().getSize();
        filte.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        filte.moveToFront();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
        if (SwingUtilities.isRightMouseButton(evt)) {
            tablemenu.show(evt.getComponent(), evt.getX() + 1, evt.getY() + 1);

        }
    }//GEN-LAST:event_datatblMouseClicked

    private void genericboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_genericboxPopupMenuWillBecomeInvisible

        final JTextField textfield = (JTextField) genericbox.getEditor().getEditorComponent();
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        if (textfield.getText().isEmpty()) {

        } else {
            try {
                String generic = (String) genericbox.getSelectedItem();
                String sql = "Select generic_id from generic where generic_name='" + generic + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                if (rs.next()) {
                    generic_id = rs.getString("generic_id");

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

            int tree = 0;
            try {
                String sql = "Select "
                        + "Itemcode as 'Code',"
                        + "itemname as 'Item',"
                        + "category,"
                        + "(select generic_name from generic sup where sup.generic_id=it.generic_id ) as 'genericname',"
                        + "(select supliyername from suplyierinfo sup where sup.id=it.company_id ) as 'companyname',"
                        + "(select ShortName from medicinforms sup where sup.FormId=it.dos_id) as 'dosname',"
                        + " (select un.unitshort from unit un where un.id=it.stockunit) as 'stockunit',"
                        + " strangth ,"
                        + " batch ,"
                        + " boxSize ,"
                        + " expdate ,"
                        + " pvat ,"
                        + " tp as 'PurchasePrice' ,"
                        + " pdiscount ,"
                        + " ptpwv ,"
                        + " ptpwd ,"
                        + "mrp as 'SalePrice',"
                        + " sdiscount ,"
                        + " smrpwv ,"
                        + " pmrpwd ,"
                        + " profite ,"
                        + " profiteParcentage ,"
                        + "openingDate as 'InputDate',"
                        + "(select ad.name from admin ad where ad.id=it.inputuser) as 'InputUser',"
                        + "(select ad.name from admin ad where ad.id=it.updateuser) as 'UpdateUser',"
                        + "lastupdate"
                        + " from item it where it.generic_id='" + generic_id + "' ";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //  datatbl.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    String procode = rs.getString("Code");
                    String Itemname = rs.getString("Item");
                    String category = rs.getString("Category");
                    String generic = rs.getString("genericname");
                    String companyname = rs.getString("companyname");
                    String dosname = rs.getString("dosname");
                    String strangth = rs.getString("strangth");
                    String boxSize = rs.getString("boxSize");
                    String batch = rs.getString("batch");
                    String expdate = rs.getString("expdate");
                    String unit = rs.getString("stockunit");
                    double vat = rs.getDouble("pvat");
                    double tp = rs.getDouble("PurchasePrice");
                    double pdiscount = rs.getDouble("pdiscount");
                    double ptpwv = rs.getDouble("ptpwv");
                    double ptpwd = rs.getDouble("ptpwd");
                    double mrp = rs.getDouble("SalePrice");
                    double sdiscount = rs.getDouble("sdiscount");
                    double smrpwv = rs.getDouble("smrpwv");
                    double pmrpwd = rs.getDouble("pmrpwd");
                    String openingdate = rs.getString("InputDate");
                    String inputuser = rs.getString("InputUser");
                    String updateuser = rs.getString("UpdateUser");
                    String lastupdate = rs.getString("lastupdate");
                    tree++;
                    model2.addRow(new Object[]{tree, procode, Itemname, strangth, generic, category, companyname, dosname, boxSize, unit, batch, expdate, vat, tp, pdiscount, ptpwd, ptpwv, mrp, sdiscount, pmrpwd, smrpwv, inputuser, updateuser, lastupdate});

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }


    }//GEN-LAST:event_genericboxPopupMenuWillBecomeInvisible


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable datatbl;
    private javax.swing.JComboBox<String> genericbox;
    private javax.swing.JLabel itemcounttext;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField procodeserchtext;
    private javax.swing.JPopupMenu tablemenu;
    // End of variables declaration//GEN-END:variables
}
