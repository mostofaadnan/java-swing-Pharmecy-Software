/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class OfferDetails extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    int tree = 1;
    int offerid = 0;
    Dashboard dashboardhere = new Dashboard();
    int userid = dashboardhere.id;
    java.sql.Date date;
    int updatekey = 0;
    String itemcode=null;
    String barcode;
    /**
     * Creates new form OfferDetails
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public OfferDetails() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        AutoCompleteDecorator.decorate(itemnamesearch);
        Item();
        currentDate();
    }

    public OfferDetails(int table_click) throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        AutoCompleteDecorator.decorate(itemnamesearch);
        Item();
        Alloffer();
        offerid = table_click;//for data insert
        offerView(table_click);//for data view
        currentDate();
        offerDetailsviwe();

        //  currentDate();
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        //  inputdate.setDate(date);

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

    private void offerDetailsDelete() {

        try {
            String sql = "Delete from offerdetails where offerid=?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1, offerid);
            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void offerView(int table_click) throws SQLException {

        try {
            String sql = "Select * from offerlist where id='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                String name = rs.getString("offername");
                offernamebox.setSelectedItem(name);
                Date strtdate = rs.getDate("startdate");
                startdate.setDate(strtdate);
                Date endate = rs.getDate("startdate");
                enddate.setDate(endate);
                //offerDetailsviwe(offerid);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void Alloffer() throws SQLException {

        try {
            String sql = "Select * from offerlist";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("offername");
                offernamebox.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void checkentry() {

        String s = "";
        boolean exists = false;
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            s = dataTable.getValueAt(i, 1).toString().trim();

            if (codetext.getText().equals("")) {
                // JOptionPane.showMessageDialog(null, "Enter Invoice Details First");
            } else if (codetext.getText().equals(s)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            entryData();
        } else {
            JOptionPane.showMessageDialog(null, "This Data Already Exist.");
            clear();

        }

    }

    private void entryData() {

        String Discountype = null;
        double discount = 0;
        double offerpric = 0;

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();

        double price = Double.parseDouble(unitrateText.getText());
        if (discountdisimaletext.getText().isEmpty()) {
            Discountype = "Parsantage";
            discount = Double.parseDouble(discountpersantagetext.getText());
        } else if (discountpersantagetext.getText().isEmpty()) {
            Discountype = "Normal";
            discount = Double.parseDouble(discountdisimaletext.getText());
        } else {

        }
        offerpric = Double.parseDouble(offerpricetxt.getText());

        model2.addRow(new Object[]{tree, codetext.getText(), itemnamesearch.getSelectedItem(), price, Discountype, discount, offerpric});
        tree++;
        clear();
    }

    private void clear() {
        codetext.setText(null);
        itemnamesearch.setSelectedIndex(0);
        unitrateText.setText(null);
        discountpersantagetext.setText(null);
        discountdisimaletext.setText(null);
        offerpricetxt.setText(null);
    }

    private void offerDetailsInsert() throws SQLException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {

                String proitemcode = (String) dataTable.getValueAt(row, 1);
                      try {
                    String sql = "Select barcode,tp,(select unitshort from unit un where un.id=it.stockunit) as 'unit' from item it where it.Itemcode='" + proitemcode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {
                        barcode=rs.getString("barcode");
                        
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                String discountytpe = (String) dataTable.getValueAt(row, 4);
                double discount = (Double) dataTable.getValueAt(row, 5);

                double offerprice = (Double) dataTable.getValueAt(row, 6);

                try {

                    String sql = "Insert into offerdetails(offerid,procode,barcode,discountype,discount,offerprice,inputdate,inputuser) values (?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setInt(1, offerid);
                    pst.setString(2, proitemcode);
                    pst.setString(3, barcode);
                    pst.setString(4, discountytpe);
                    pst.setDouble(5, discount);
                    pst.setDouble(6, offerprice);
                    pst.setDate(7, date);
                    pst.setInt(8, userid);

                    pst.execute();
                    
                    
                    //JOptionPane.showMessageDialog(null, "Successfully Save");

                    // 
                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }
          
                    DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
                    model2.setRowCount(0);
            offerDetailsviwe();
            JOptionPane.showMessageDialog(null, "Successfully Save");
            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void offerDetailsviwe() {

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();

        model2.setRowCount(0);

        try {

            String sql = "Select procode as 'Code',(select ita.itemName from item ita where ita.Itemcode=of.procode) as 'Item',(select ita.mrp from item ita where ita.Itemcode=of.procode) as 'price',discountype,discount,offerprice from offerdetails of where offerid='" + offerid + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            while (rs.next()) {
                String procode = rs.getString("Code");
                String Itemname = rs.getString("Item");
                double price = rs.getDouble("price");
                String Discountype = rs.getString("discountype");
                double discount = rs.getDouble("discount");
                double offerpric = rs.getDouble("offerprice");

                model2.addRow(new Object[]{tree, procode, Itemname, price, Discountype, discount, offerpric});
                tree++;
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void deleterow() {

        int[] toDelete = dataTable.getSelectedRows();
        /// Arrays.sort(toDelete); // be shure to have them in ascending order.
        DefaultTableModel myTableModel = (DefaultTableModel) dataTable.getModel();
        for (int ii = toDelete.length - 1; ii >= 0; ii--) {
            myTableModel.removeRow(toDelete[ii]); // beginning at the largest.
        }

        clear();
    }

    private void checkUpdate() {
        String Discountype = null;
        double discount = 0;
        double offerpric = 0;
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int i = dataTable.getSelectedRow();
        if (i >= 0) {
            if (discountdisimaletext.getText().isEmpty()) {
                Discountype = "Parsantage";
                discount = Double.parseDouble(discountpersantagetext.getText());
            } else if (discountpersantagetext.getText().isEmpty()) {
                Discountype = "Normal";
                discount = Double.parseDouble(discountdisimaletext.getText());
            } else {

            }
            offerpric = Double.parseDouble(offerpricetxt.getText());

            model.setValueAt(Discountype, i, 4);
            model.setValueAt(discount, i, 5);
            model.setValueAt(offerpric, i, 6);

        }
        updatekey = 0;
        //updarerext.setText("0");

        clear();

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
        offernamebox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        startdate = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        enddate = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        unitrateText = new javax.swing.JTextField();
        discountpersantagetext = new javax.swing.JTextField();
        discountdisimaletext = new javax.swing.JTextField();
        codetext = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        offerpricetxt = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Offer Details");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Offer Name:");

        offernamebox.setEditable(true);
        offernamebox.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        offernamebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                offernameboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Start Date");

        startdate.setDateFormatString("yyyy-MM-dd");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("End Date");

        enddate.setDateFormatString("yyyy-MM-dd");

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchitemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        itemnamesearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                itemnamesearchKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Discount(%)");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Price");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Discount");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Item Description");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Barcode");

        unitrateText.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        unitrateText.setBorder(null);
        unitrateText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                unitrateTextunitrateTextKeyPressed(evt);
            }
        });

        discountpersantagetext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        discountpersantagetext.setBorder(null);
        discountpersantagetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                discountpersantagetextcodetextKeyReleased(evt);
            }
        });

        discountdisimaletext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        discountdisimaletext.setBorder(null);
        discountdisimaletext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                discountdisimaletextcodetextKeyReleased(evt);
            }
        });

        codetext.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        codetext.setBorder(null);
        codetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetextcodetextKeyReleased(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Offer Price");

        offerpricetxt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        offerpricetxt.setBorder(null);
        offerpricetxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                offerpricetxtunitrateTextKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discountpersantagetext, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(discountdisimaletext, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(offerpricetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel10))
                                .addGap(1, 1, 1)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(unitrateText, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(codetext, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(itemnamesearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel13)
                                            .addComponent(jLabel12))
                                        .addComponent(jLabel14))
                                    .addGap(1, 1, 1)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(discountpersantagetext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                                        .addComponent(discountdisimaletext)))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(offerpricetxt)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(offernamebox, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(10, 10, 10)
                .addComponent(startdate, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(6, 6, 6)
                .addComponent(enddate, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(185, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(offernamebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(startdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(enddate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jButton3.setBackground(new java.awt.Color(0, 102, 102));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Execute");
        jButton3.setBorder(null);
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        dataTable.setAutoCreateRowSorter(true);
        dataTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI.No", "Barcode", "Product Name", "Price", "Discount type", "Discount ", "Offer Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTable.setGridColor(new java.awt.Color(204, 204, 204));
        dataTable.setRowHeight(30);
        dataTable.setShowVerticalLines(false);
        dataTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataTableMouseClicked(evt);
            }
        });
        dataTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dataTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(dataTable);
        if (dataTable.getColumnModel().getColumnCount() > 0) {
            dataTable.getColumnModel().getColumn(0).setResizable(false);
            dataTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            dataTable.getColumnModel().getColumn(1).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(300);
            dataTable.getColumnModel().getColumn(3).setResizable(false);
            dataTable.getColumnModel().getColumn(4).setResizable(false);
            dataTable.getColumnModel().getColumn(5).setResizable(false);
            dataTable.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (updatekey == 0) {
            if(codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex()==0 || offerpricetxt.getText().isEmpty()){
           }else{
             checkentry();
            }
            
        } else {

            checkUpdate();
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void codetextcodetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextcodetextKeyReleased
        discountpersantagetext.setText(null);
        discountdisimaletext.setText(null);
        offerpricetxt.setText(null);
        if (codetext.getText().isEmpty() || codetext.getText().matches("^[a-zA-Z]+$")) {

            itemnamesearch.setSelectedIndex(0);
            unitrateText.setText(null);

            // unibox.setSelectedIndex(0);
        } else {
            String SearchText = (String) codetext.getText();

            try {

                String sql = "Select itemName,mrp from item ita where ita.Itemcode='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                //  pst.setString(1, SearchText);

                rs = pst.executeQuery();
                if (rs.next()) {

                    String name = rs.getString("itemName");
                    itemnamesearch.setSelectedItem(name);
                    String unitrate = rs.getString("mrp");
                    unitrateText.setText(unitrate);

                    // String unit = rs.getString("stockunit");
                    // unibox.setSelectedItem(unit);
                } else {

                    itemnamesearch.setSelectedIndex(0);
                    unitrateText.setText(null);

                    // unibox.setSelectedIndex(0);
                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_codetextcodetextKeyReleased

    private void itemnamesearchitemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchitemnamesearchPopupMenuWillBecomeInvisible
        discountpersantagetext.setText(null);
        discountdisimaletext.setText(null);
        offerpricetxt.setText(null);

        if (itemnamesearch.getSelectedIndex() == 0) {
            codetext.setText(null);

            unitrateText.setText(null);

            //  unibox.setSelectedIndex(0);
        } else {

            String SearchText = (String) itemnamesearch.getSelectedItem();
            try {

                String sql = "Select Itemcode,itemName,mrp from item where itemName='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {

                    String Id = rs.getString("Itemcode");
                    codetext.setText(Id);

                    String unitrate = rs.getString("mrp");
                    unitrateText.setText(unitrate);
                 
                    //unibox.setSelectedItem(unit);
                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_itemnamesearchitemnamesearchPopupMenuWillBecomeInvisible

    private void discountpersantagetextcodetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discountpersantagetextcodetextKeyReleased
        if (discountpersantagetext.getText().isEmpty() || codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0) {
            offerpricetxt.setText(null);
        } else {
            discountdisimaletext.setText(null);
            double discountpersantage = Double.parseDouble(discountpersantagetext.getText());
            double itemprice = Double.parseDouble(unitrateText.getText());
            double offerpriceper = itemprice - (itemprice * discountpersantage / 100);

            offerpricetxt.setText(String.format("%.2f", offerpriceper));
        }
    }//GEN-LAST:event_discountpersantagetextcodetextKeyReleased

    private void unitrateTextunitrateTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateTextunitrateTextKeyPressed

    }//GEN-LAST:event_unitrateTextunitrateTextKeyPressed

    private void discountdisimaletextcodetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discountdisimaletextcodetextKeyReleased
        if (discountdisimaletext.getText().isEmpty() || codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0) {
            offerpricetxt.setText(null);
        } else {
            discountpersantagetext.setText(null);
            double discountpersantage = Double.parseDouble(discountdisimaletext.getText());
            double itemprice = Double.parseDouble(unitrateText.getText());
            double offerpriceper = (itemprice - discountpersantage);

            offerpricetxt.setText(String.format("%.2f", offerpriceper));
        }
    }//GEN-LAST:event_discountdisimaletextcodetextKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clear();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void itemnamesearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyTyped

    }//GEN-LAST:event_itemnamesearchKeyTyped

    private void offerpricetxtunitrateTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_offerpricetxtunitrateTextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_offerpricetxtunitrateTextKeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        if (model2.getRowCount() == 0) {
            try {
                offerDetailsInsert();
            } catch (SQLException ex) {
                Logger.getLogger(OfferDetails.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

            try {
                offerDetailsDelete();
                offerDetailsInsert();
            } catch (SQLException ex) {
                Logger.getLogger(OfferDetails.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void offernameboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_offernameboxPopupMenuWillBecomeInvisible
        try {
            String offertitle = (String) offernamebox.getSelectedItem();
            String sql = "Select * from offerlist where offername='" + offertitle + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                offerid = rs.getInt("id");
                String name = rs.getString("offername");
                offernamebox.setSelectedItem(name);
                Date strtdate = rs.getDate("startdate");
                startdate.setDate(strtdate);
                Date endate = rs.getDate("startdate");
                enddate.setDate(endate);
                offerDetailsviwe();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_offernameboxPopupMenuWillBecomeInvisible

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        updatekey = 1;
        int selectedRowIndex = dataTable.getSelectedRow();
        //updarerext.setText("1");
        codetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        unitrateText.setText(model.getValueAt(selectedRowIndex, 3).toString());
        String discounttype = model.getValueAt(selectedRowIndex, 4).toString();
        if ("Parsantage".equals(discounttype)) {
            discountpersantagetext.setText(model.getValueAt(selectedRowIndex, 5).toString());
        } else {
            discountdisimaletext.setText(model.getValueAt(selectedRowIndex, 5).toString());

        }
        offerpricetxt.setText(model.getValueAt(selectedRowIndex, 6).toString());
        //unibox.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());
    }//GEN-LAST:event_dataTableMouseClicked

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_DELETE) {

        } else {
           deleterow();
          }
    }//GEN-LAST:event_dataTableKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField codetext;
    private javax.swing.JTable dataTable;
    private javax.swing.JTextField discountdisimaletext;
    private javax.swing.JTextField discountpersantagetext;
    private com.toedter.calendar.JDateChooser enddate;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> offernamebox;
    private javax.swing.JTextField offerpricetxt;
    private com.toedter.calendar.JDateChooser startdate;
    private javax.swing.JTextField unitrateText;
    // End of variables declaration//GEN-END:variables
}
