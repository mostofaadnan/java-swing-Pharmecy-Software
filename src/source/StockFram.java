/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
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
public class StockFram extends javax.swing.JInternalFrame {

    private static final DecimalFormat df2 = new DecimalFormat("#.00");
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String catid = null;
    String scatid = null;
    String genericid;

    /**
     * Creates new form StockFram
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public StockFram() throws SQLException, IOException {
        initComponents();

        conn = Java_Connect.conectrDB();
        // table_update();
        //  Item();
        //  category();
        Itemformat();

        // AutoCompleteDecorator.decorate(itemnamesearch);
        AutoCompleteDecorator.decorate(itemformatobox);
        //    AutoCompleteDecorator.decorate(genericbox);
        AutoCompleteDecorator.decorate(colorbox);
        jTabbedPane1.setEnabledAt(1, false);
        ExtractFilterGeneric();
        ExtractFilter();

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
        //  itemnamesearch.addItem("");
        itemnamesearch.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;

        try {

            String sql = "Select itemName from item WHERE lower(itemName)  LIKE '%" + enteredText + "%' OR Itemcode LIKE '%" + enteredText + "%'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("itemName");
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

        }
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
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
            //  itemnamesearch.setModel(new DefaultComboBoxModel(filterArray.toArray()));

            genericbox.setSelectedItem(enteredText);
            genericbox.showPopup();

        } else {

            genericbox.hidePopup();

        }
    }

    private void color() throws SQLException {

        try {
            String sql = "Select name from color";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("name");
                colorbox.addItem(name);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void Item() throws SQLException {

        try {
            String sql = "Select itemName from item";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("itemName");
                itemnamesearch.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void Itemformat() throws SQLException {

        try {
            String sql = "Select nameformat from barcodeproduct";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("nameformat");
                itemformatobox.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void category() throws SQLException {

        try {
            String sql = "Select * from category";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                int id = rs.getInt("id");
                String category = rs.getString("name");
                genericbox.addItem(category);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private float total_action_tp() {

        int rowaCount = datatbl.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 10).toString());
        }
        return (float) totaltpmrp;

    }

    private float total_action_mrp() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 11).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_qty() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 6).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_stock() {

        int rowaCount = datatbl1.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl1.getValueAt(i, 9).toString());
        }

        return (float) totaltpmrp;

    }

    private void ItemDescription() {

        // stockdetails
        DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select it.barcode as 'barcode',"
                    + "it.nameformat as 'nameformat',"
                    + "it.color as 'color',"
                    + "it.size as 'size',"
                    + "it.tp as 'tp',"
                    + " it.mrp as 'mrp',"
                    + "it.inputdate as 'inputdate',"
                    + "(select ad.name from admin ad where ad.id=it.inputuser) as 'InputUser',"
                    + "st.Qty as 'stock' FROM barcodeproduct it inner join stockdetails st ON it.barcode=st.barcode";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //  datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String barcode = rs.getString("barcode");
                String nameformat = rs.getString("nameformat");
                String color = rs.getString("color");
                String size = rs.getString("size");
                double mrp = rs.getDouble("mrp");
                double tp = rs.getDouble("tp");
                Date inpudate = rs.getDate("inputdate");
                String inputuser = rs.getString("InputUser");
                float stock = rs.getFloat("stock");
                tree++;
                model2.addRow(new Object[]{tree, barcode, nameformat, color, size, tp, mrp, inputuser, inpudate, stock});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        totalstocktext.setText(Float.toString(total_action_stock()));
    }

    private void table_update() throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 1;

        try {
            double totaltradeprice = 0;
            double totalsaleeprice = 0;
            double profite = 0;
            String sql = "SELECT "
                    + "ita.Itemcode as 'Code',"
                    + "ita.itemName as 'Item',"
                    + "ita.batch as 'batch',"
                    + "ita.expdate as 'expdate',"
                    + "ita.boxsize as 'boxsize',"
                    + "ita.tp as 'tradeprice',"
                    + "ita.mrp as 'saleprice',"
                    + "Qty as 'Stock',"
                    + "(select unitshort from unit un where un.id=ita.stockunit) as 'unit' "
                    + " FROM item ita inner join stockdetails st ON ita.itemcode=st.itemcode WHERE st.Qty>0 GROUP BY st.itemcode";
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
                if (qty < 0) {
                    totaltradeprice = 0;
                    totalsaleeprice = 0;
                } else {

                    totaltradeprice = round((qty * tp), 2);
                    totalsaleeprice = round((qty * mrp), 2);

                }
                //   String totaltradeprices = df2.format(totaltradeprice);
                //double profite = totalsaleeprice - totalsaleeprice;

                model2.addRow(new Object[]{tree, procode, Itemname, batch, expdate, boxsize, qty, unit, tp, mrp, totaltradeprice, totalsaleeprice});
                tree++;

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        nettotaltradetext.setText(Float.toString(total_action_tp()));
        nettotalsalepricetext.setText(Float.toString(total_action_mrp()));
        jLabel7.setText(Float.toString(total_action_qty()));
    }

    private void loadItem() throws SQLException {

        itemnamesearch.removeAllItems();
        itemnamesearch.setSelectedItem("Select");

        genericbox.removeAllItems();

        genericbox.setSelectedItem("Select");

        table_update();
        category();

        Item();

    }

    private void serach() {
        String SearchText = searchext.getText();
        try {

            String sql = "Select ita.id as 'Code',ita.itemname as 'Item',ita.category as 'Category' , ita.mrp as 'Trade price',(Select st.Qty from stock st where st.procode=ita.id) as 'Available Stock',(select st.unit from stock st where st.procode=ita.id) as 'unit' from item ita where ita.itemName LIKE CONCAT('%', ? ,'%') OR ita.id LIKE CONCAT('%', ? ,'%') OR ita.category LIKE CONCAT('%', ? ,'%') OR ita.supliyer LIKE CONCAT('%', ? ,'%')  OR ita.barcode LIKE CONCAT('%', ? ,'%')";
            pst = conn.prepareStatement(sql);
            pst.setString(1, SearchText);
            pst.setString(2, SearchText);
            pst.setString(3, SearchText);
            pst.setString(4, SearchText);
            pst.setString(5, SearchText);

            rs = pst.executeQuery();
            datatbl.setModel(DbUtils.resultSetToTableModel(rs));

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

        tablemenu = new javax.swing.JPopupMenu();
        description = new javax.swing.JMenuItem();
        view = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        searchext = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        genericbox = new javax.swing.JComboBox<>();
        jPanel8 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nettotaltradetext = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nettotalsalepricetext = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        datatbl1 = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        searchext2 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        itemformatobox = new javax.swing.JComboBox<>();
        jPanel13 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        itemcodetext = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        colorbox = new javax.swing.JComboBox<>();
        sizetext = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        totalstocktext = new javax.swing.JLabel();

        tablemenu.setPreferredSize(new java.awt.Dimension(300, 80));

        description.setText("Description View");
        description.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descriptionActionPerformed(evt);
            }
        });
        tablemenu.add(description);

        view.setText("Item View");
        view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewActionPerformed(evt);
            }
        });
        tablemenu.add(view);

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Stock Item");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        jPanel1.setPreferredSize(new java.awt.Dimension(900, 533));

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setBorder(null);
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl.setForeground(new java.awt.Color(51, 51, 51));
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Si No", "Product Code", "Product Name", "Batch", "Exp Date", "Box Size", "Stock", "Unit", "TP", "MRP", "Total(TP)", "Total(MRP)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false, false, false, false, false, false
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
            datatbl.getColumnModel().getColumn(0).setPreferredWidth(50);
            datatbl.getColumnModel().getColumn(1).setPreferredWidth(100);
            datatbl.getColumnModel().getColumn(2).setPreferredWidth(300);
            datatbl.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));

        jPanel4.setBackground(new java.awt.Color(67, 86, 86));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Product Code");

        searchext.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        searchext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchext.setToolTipText("Product Code");
        searchext.setBorder(null);
        searchext.setCaretColor(new java.awt.Color(0, 102, 102));
        searchext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchextKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchext, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(67, 86, 86));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Product Name");

        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        itemnamesearch.setForeground(new java.awt.Color(102, 102, 102));
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(67, 86, 86));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Generic");

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

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(genericbox, 0, 339, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(genericbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(67, 86, 86));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(102, 102, 102));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/report.png"))); // NOI18N
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
                .addContainerGap()
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(67, 86, 86));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Load All");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 12, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(6, 6, 6))
        );

        jPanel3.setBackground(new java.awt.Color(67, 86, 86));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Net Total(Trade Price):");

        nettotaltradetext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nettotaltradetext.setForeground(new java.awt.Color(255, 255, 255));
        nettotaltradetext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        nettotaltradetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Net Total(Sale Price):");

        nettotalsalepricetext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nettotalsalepricetext.setForeground(new java.awt.Color(255, 255, 255));
        nettotalsalepricetext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        nettotalsalepricetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total Stock");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jButton3.setBackground(new java.awt.Color(102, 0, 0));
        jButton3.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Opening Stock");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addComponent(nettotaltradetext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addGap(6, 6, 6)
                .addComponent(nettotalsalepricetext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(nettotaltradetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(nettotalsalepricetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addGap(61, 61, 61))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jScrollPane1.setViewportView(jPanel1);

        jTabbedPane1.addTab("Single View", jScrollPane1);

        datatbl1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        datatbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL No", "Barcode", "Item Name", "Color", "Size", "Trade Price", "Purchase Price", "Input User", "Input Date", "Stock"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false, false, false, false, false
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
        datatbl1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatbl1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(datatbl1);
        if (datatbl1.getColumnModel().getColumnCount() > 0) {
            datatbl1.getColumnModel().getColumn(2).setPreferredWidth(300);
        }

        jPanel11.setBackground(new java.awt.Color(67, 86, 86));

        jPanel12.setBackground(new java.awt.Color(67, 86, 86));
        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Barcode");

        searchext2.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        searchext2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchext2.setToolTipText("Product Code");
        searchext2.setBorder(null);
        searchext2.setCaretColor(new java.awt.Color(0, 102, 102));
        searchext2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchext2KeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchext2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchext2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(67, 86, 86));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Item Name");

        itemformatobox.setEditable(true);
        itemformatobox.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        itemformatobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        itemformatobox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemformatoboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(itemformatobox, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemformatobox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel13.setBackground(new java.awt.Color(67, 86, 86));
        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Item Code");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Size");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Color");

        jButton2.setBackground(new java.awt.Color(153, 51, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Submit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        colorbox.setEditable(true);
        colorbox.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        colorbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemcodetext, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(0, 0, 0)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(colorbox, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(0, 0, 0)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(sizetext, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(colorbox)
                    .addComponent(sizetext)
                    .addComponent(itemcodetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(102, 102, 102))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );

        jPanel14.setBackground(new java.awt.Color(67, 86, 86));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Total Stock");

        totalstocktext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        totalstocktext.setForeground(new java.awt.Color(255, 255, 255));
        totalstocktext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalstocktext, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(988, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalstocktext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jTabbedPane1.addTab("Details View", jPanel10);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1379, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane1))
        );

        setBounds(40, 10, 1334, 609);
    }// </editor-fold>//GEN-END:initComponents

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        double totaltradeprice;
        double totalsaleeprice;
        final JTextField textfield = (JTextField) genericbox.getEditor().getEditorComponent();
        if (textfield.getText().isEmpty()) {
            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(StockFram.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            int tree = 1;
            try {

                String sql = "SELECT "
                        + "ita.Itemcode as 'Code',"
                        + "ita.itemName as 'Item',"
                        + "ita.batch as 'batch',"
                        + "ita.expdate as 'expdate',"
                        + "ita.boxsize as 'boxsize',"
                        + "ita.tp as 'tradeprice',"
                        + "ita.mrp as 'saleprice',"
                        + "sum(st.Qty) as 'Stock',"
                        + "(select unitshort from unit un where un.id=ita.stockunit) as 'unit' "
                        + "FROM item ita inner join stockdetails st ON ita.Itemcode=st.itemcode where ita.itemName='" + itemnamesearch.getSelectedItem() + "'";

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

                    if (qty < 0) {

                        totaltradeprice = 0;
                        totalsaleeprice = 0;

                    } else {

                        totaltradeprice = round((qty * tp), 2);
                        totalsaleeprice = round((qty * mrp), 2);

                    }
                    double profite = totalsaleeprice * totalsaleeprice;

                    model2.addRow(new Object[]{tree, procode, Itemname, batch, expdate, boxsize, qty, unit, tp, mrp, totaltradeprice, totalsaleeprice});
                    tree++;
                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
        nettotaltradetext.setText(Float.toString(total_action_tp()));
        nettotalsalepricetext.setText(Float.toString(total_action_mrp()));
        jLabel7.setText(Float.toString(total_action_qty()));
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void genericboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_genericboxPopupMenuWillBecomeInvisible
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        double totaltradeprice;
        double totalsaleeprice;
        int tree = 1;

        final JTextField textfield = (JTextField) genericbox.getEditor().getEditorComponent();
        if (textfield.getText().isEmpty()) {
            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(StockFram.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                String generic = (String) genericbox.getSelectedItem();
                String sql = "Select generic_id from generic where generic_name='" + generic + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                if (rs.next()) {
                    genericid = rs.getString("generic_id");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

            try {

                String sql = "SELECT "
                        + "ita.Itemcode as 'Code',"
                        + "ita.itemName as 'Item',"
                        + "ita.batch as 'batch',"
                        + "ita.expdate as 'expdate',"
                        + "ita.boxsize as 'boxsize',"
                        + "ita.tp as 'tradeprice',"
                        + "ita.mrp as 'saleprice',"
                        + "sum(st.Qty) as 'Stock',"
                        + "(select unitshort from unit un where un.id=ita.stockunit) as 'unit' "
                        + "FROM item ita inner join stockdetails st ON ita.Itemcode=st.itemcode where ita.generic_id='" + genericid + "' AND Qty>0 GROUP BY st.itemcode ORDER BY ita.id ASC";
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

                    if (qty < 0) {

                        totaltradeprice = 0;
                        totalsaleeprice = 0;

                    } else {

                        totaltradeprice = round((qty * tp), 2);
                        totalsaleeprice = round((qty * mrp), 2);

                    }
                    double profite = totalsaleeprice * totalsaleeprice;

                    model2.addRow(new Object[]{tree, procode, Itemname, batch, expdate, boxsize, qty, unit, tp, mrp, totaltradeprice, totalsaleeprice});
                    tree++;
                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }

        nettotaltradetext.setText(Float.toString(total_action_tp()));
        nettotalsalepricetext.setText(Float.toString(total_action_mrp()));
        jLabel7.setText(Float.toString(total_action_qty()));

    }//GEN-LAST:event_genericboxPopupMenuWillBecomeInvisible

    private void searchextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchextKeyReleased
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);

        if (searchext.getText().isEmpty()) {
            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(StockFram.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

            int tree = 0;
            double totaltradeprice;
            double totalsaleeprice;

            try {

                String sql = "SELECT "
                        + "ita.Itemcode as 'Code',"
                        + "ita.itemname as 'Item',"
                        + "(select name from category  cat where cat.id=ita.category) as 'Category',"
                        + "(select supliyername from suplyierinfo sup where sup.id=ita.company_id) as 'suplyer',ita.tp as 'tradeprice',ita.mrp as 'saleprice',sum(st.Qty) as 'Stock',(select unitshort from unit un where un.id=ita.stockunit) as 'unit'  FROM item ita inner join stockdetails st ON ita.Itemcode=st.itemcode where  ita.Itemcode='" + searchext.getText() + "' GROUP BY st.itemcode ORDER BY ita.id ASC";
                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();

                if (rs.next()) {
                    String procode = rs.getString("Code");
                    String Itemname = rs.getString("Item");
                    String category = rs.getString("category");

                    double tp = rs.getDouble("tradeprice");
                    double mrp = rs.getDouble("saleprice");
                    int qty = rs.getInt("Stock");
                    String unit = rs.getString("Unit");
                    String suplyer = rs.getString("suplyer");

                    if (qty < 0) {

                        totaltradeprice = 0;
                        totalsaleeprice = 0;

                    } else {

                        totaltradeprice = round((qty * tp), 2);
                        totalsaleeprice = round((qty * mrp), 2);

                    }
                    double profite = totalsaleeprice * totalsaleeprice;
                    tree++;

                    model2.addRow(new Object[]{tree, procode, Itemname, category, suplyer, qty, unit, tp, mrp, totaltradeprice, totalsaleeprice});

                } else {
                    table_update();
                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            nettotaltradetext.setText(Float.toString(total_action_tp()));
            nettotalsalepricetext.setText(Float.toString(total_action_mrp()));
            jLabel7.setText(Float.toString(total_action_qty()));

            DefaultTableModel model3 = (DefaultTableModel) datatbl1.getModel();
            model3.setRowCount(0);

            try {
                String sql = "Select it.barcode as 'barcode',it.nameformat as 'nameformat',it.color as 'color',it.size as 'size',it.tp as 'tp', it.mrp as 'mrp',it.inputdate as 'inputdate',(select ad.name from admin ad where ad.id=it.inputuser) as 'InputUser',st.Qty as 'stock' FROM barcodeproduct it inner join stockdetails st ON it.barcode=st.barcode where it.itemcode='" + searchext.getText() + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //  datatbl.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    String barcode = rs.getString("barcode");
                    String nameformat = rs.getString("nameformat");
                    String color = rs.getString("color");
                    String size = rs.getString("size");
                    double mrp = rs.getDouble("mrp");
                    double tp = rs.getDouble("tp");
                    Date inpudate = rs.getDate("inputdate");
                    String inputuser = rs.getString("InputUser");
                    float stock = rs.getFloat("stock");

                    tree++;

                    model3.addRow(new Object[]{tree, barcode, nameformat, color, size, tp, mrp, inputuser, inpudate, stock});

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }


    }//GEN-LAST:event_searchextKeyReleased

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        if (model2.getRowCount() == 0) {

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {

            try {
                String titleame = "Stock Report";
                double totaltrade = Double.parseDouble(nettotaltradetext.getText());
                double totalinvoice = Double.parseDouble(nettotalsalepricetext.getText());
                // double totalprofit = Double.parseDouble(profittext.getText());

                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/Stock.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDatasource", ItemJRBean);
                // para.put("product", producttextdata.getText());
                //  para.put("counttype", countdaytextdata.getText());
                para.put("totaltradeprice", totaltrade);
                para.put("totalsaleprice", totalinvoice);
                para.put("titlename", titleame);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            table_update();
            ItemDescription();
        } catch (SQLException ex) {
            Logger.getLogger(StockFram.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void descriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descriptionActionPerformed
        int row = datatbl.getSelectedRow();
        String table_click = (datatbl.getModel().getValueAt(row, 1).toString());

        ItemDescrption filte = null;

        try {
            filte = new ItemDescrption(table_click);
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
    }//GEN-LAST:event_descriptionActionPerformed

    private void viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewActionPerformed
        int row = datatbl.getSelectedRow();
        String table_click = (datatbl.getModel().getValueAt(row, 1).toString());

        NewItem filte = null;

        try {
            filte = new NewItem(table_click);
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
    }//GEN-LAST:event_viewActionPerformed

    private void datatbl1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatbl1MouseClicked
        /*        DefaultTableModel model = (DefaultTableModel) datatbl.getModel();
        int selectedRowIndex = datatbl.getSelectedRow();
        barcodetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        allcolorbox.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        sizetext.setText(model.getValueAt(selectedRowIndex, 3).toString());
        tptextpro.setText(model.getValueAt(selectedRowIndex, 4).toString());
        mrptextpro.setText(model.getValueAt(selectedRowIndex, 5).toString());
        inputdate.setDate((java.util.Date) model.getValueAt(selectedRowIndex, 7));
        stocktextpro.setText(model.getValueAt(selectedRowIndex, 8).toString());

         */
    }//GEN-LAST:event_datatbl1MouseClicked

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
        if (SwingUtilities.isRightMouseButton(evt)) {
            tablemenu.show(evt.getComponent(), evt.getX() + 1, evt.getY() + 1);

        }
    }//GEN-LAST:event_datatblMouseClicked

    private void searchext2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchext2KeyReleased
        DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
        model2.setRowCount(0);
        if (searchext2.getText().isEmpty()) {

            ItemDescription();

        } else {
            int tree = 0;
            try {
                String sql = "Select it.barcode as 'barcode',it.nameformat as 'nameformat',it.color as 'color',it.size as 'size',it.tp as 'tp', it.mrp as 'mrp',it.inputdate as 'inputdate',(select ad.name from admin ad where ad.id=it.inputuser) as 'InputUser',st.Qty as 'stock' FROM barcodeproduct it inner join stockdetails st ON it.barcode=st.barcode where it.barcode='" + searchext2.getText() + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //  datatbl.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    String barcode = rs.getString("barcode");
                    String nameformat = rs.getString("nameformat");
                    String color = rs.getString("color");
                    String size = rs.getString("size");
                    double mrp = rs.getDouble("mrp");
                    double tp = rs.getDouble("tp");
                    Date inpudate = rs.getDate("inputdate");
                    String inputuser = rs.getString("InputUser");
                    float stock = rs.getFloat("stock");

                    tree++;

                    model2.addRow(new Object[]{tree, barcode, nameformat, color, size, tp, mrp, inputuser, inpudate, stock});

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            totalstocktext.setText(Float.toString(total_action_stock()));
        }
    }//GEN-LAST:event_searchext2KeyReleased

    private void itemformatoboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemformatoboxPopupMenuWillBecomeInvisible
        DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
        model2.setRowCount(0);
        if (itemformatobox.getSelectedIndex() > 0) {
            int tree = 0;
            try {
                String sql = "Select it.barcode as 'barcode',it.nameformat as 'nameformat',it.color as 'color',it.size as 'size',it.tp as 'tp', it.mrp as 'mrp',it.inputdate as 'inputdate',(select ad.name from admin ad where ad.id=it.inputuser) as 'InputUser',st.Qty as 'stock' FROM barcodeproduct it inner join stockdetails st ON it.barcode=st.barcode where it.nameformat='" + itemformatobox.getSelectedItem() + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //  datatbl.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    String barcode = rs.getString("barcode");
                    String nameformat = rs.getString("nameformat");
                    String color = rs.getString("color");
                    String size = rs.getString("size");
                    double mrp = rs.getDouble("mrp");
                    double tp = rs.getDouble("tp");
                    Date inpudate = rs.getDate("inputdate");
                    String inputuser = rs.getString("InputUser");
                    float stock = rs.getFloat("stock");

                    tree++;

                    model2.addRow(new Object[]{tree, barcode, nameformat, color, size, tp, mrp, inputuser, inpudate, stock});

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            totalstocktext.setText(Float.toString(total_action_stock()));
        } else {
            ItemDescription();
        }
    }//GEN-LAST:event_itemformatoboxPopupMenuWillBecomeInvisible

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
        model2.setRowCount(0);
        if (itemcodetext.getText().isEmpty() || colorbox.getSelectedIndex() == 0 || sizetext.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Fillup Requrment Field");

        } else {
            int tree = 0;
            try {
                String sql = "Select it.barcode as 'barcode',it.nameformat as 'nameformat',it.color as 'color',it.size as 'size',it.tp as 'tp', it.mrp as 'mrp',it.inputdate as 'inputdate',(select ad.name from admin ad where ad.id=it.inputuser) as 'InputUser',st.Qty as 'stock' FROM barcodeproduct it inner join stockdetails st ON it.barcode=st.barcode where it.itemcode='" + itemcodetext.getText() + "' AND it.color='" + colorbox.getSelectedItem() + "' AND size='" + sizetext.getText() + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //  datatbl.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    String barcode = rs.getString("barcode");
                    String nameformat = rs.getString("nameformat");
                    String color = rs.getString("color");
                    String size = rs.getString("size");
                    double mrp = rs.getDouble("mrp");
                    double tp = rs.getDouble("tp");
                    Date inpudate = rs.getDate("inputdate");
                    String inputuser = rs.getString("InputUser");
                    float stock = rs.getFloat("stock");

                    tree++;

                    model2.addRow(new Object[]{tree, barcode, nameformat, color, size, tp, mrp, inputuser, inpudate, stock});

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            totalstocktext.setText(Float.toString(total_action_stock()));
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        OpeningStock filte = null;

        try {
            filte = new OpeningStock();
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
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> colorbox;
    private javax.swing.JTable datatbl;
    private javax.swing.JTable datatbl1;
    private javax.swing.JMenuItem description;
    private javax.swing.JComboBox<String> genericbox;
    private javax.swing.JTextField itemcodetext;
    private javax.swing.JComboBox<String> itemformatobox;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel nettotalsalepricetext;
    private javax.swing.JLabel nettotaltradetext;
    private javax.swing.JTextField searchext;
    private javax.swing.JTextField searchext2;
    private javax.swing.JTextField sizetext;
    private javax.swing.JPopupMenu tablemenu;
    private javax.swing.JLabel totalstocktext;
    private javax.swing.JMenuItem view;
    // End of variables declaration//GEN-END:variables
}
