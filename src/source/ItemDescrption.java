/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author adnan
 */
public class ItemDescrption extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String catid = null;
    String scatid=null;
    String suplyerid = null;
    String unitid = null;
    String itemcode = null;

    
     String barcodenamefromat=null;
    String barcodemrp=null;
    String barcodecomment=null;
    /**
     * Creates new form ItemDescrption
     *
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public ItemDescrption() throws IOException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        Item();
        category();
        suppliyer();
        unit();

    }

    public ItemDescrption(String tableClick) throws IOException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        Item();
        category();
        suppliyer();
        unit();
        itemcode = tableClick;
        itemview();
    }

    private void itemview() {

        try {

               

                String sql = "Select itemcode,itemName,openingDate,tp,mrp,remark,category,subcategory,(select name from category ct where ct.id=it.category) as 'categoryname',supliyer,(Select supliyername from suplyierinfo sinfo where sinfo.id=it.supliyer) as 'suppliername',stockunit,(select unitshort  from unit un where un.id=it.stockunit) as 'unitname',includevate,vat from item it where it.Itemcode='" + itemcode + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {

                    String Id = rs.getString("itemcode");
                    codetext.setText(Id);

                    String Name = rs.getString("itemName");
                    nametext.setText(Name);
                    String categoryname=rs.getString("categoryname");
                    categorybox.setSelectedItem(categoryname);
                    String suppliername=rs.getString("suppliername");
                    supliyerbox.setSelectedItem(suppliername);
                    String unitname=rs.getString("unitname");
                    unibox.setSelectedItem(unitname);
                    
                    Date date = rs.getDate("openingDate");
                    openingdate.setDate(date);
                    String tpprice = rs.getString("tp");
                    tptext.setText(tpprice);

                    String marpprice = rs.getString("mrp");
                    mrptext.setText(marpprice);
                    int vatchek=rs.getInt("includevate");
                    String vat=rs.getString("vat");
                     if(vatchek==1){
                     vatcheck.setSelected(true);
                     vattext.setText(vat);
                    }else{
                    vatcheck.setSelected(false);
                    vattext.setText(null);
                      }
                    catid = rs.getString("category");

                    suplyerid = rs.getString("supliyer");
                    unitid = rs.getString("stockunit");
                    scatid=rs.getString("subcategory");
                    

                filterCategory();
                countitem();
                table_update();
                itemdtotalstock();
            } else {
                clear();
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
            String sql = "Select barcode,color,size,tp,mrp,inputdate,(select ad.name from admin ad where ad.id=it.inputuser) as 'InputUser',(select Qty from stockdetails st where st.barcode=it.barcode) as 'stock' from barcodeproduct it where it.itemcode='" + itemcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //  datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String barcode = rs.getString("barcode");
                String color = rs.getString("color");
                String size = rs.getString("size");
                double mrp = rs.getDouble("mrp");
                double tp = rs.getDouble("tp");
                Date inpudate = rs.getDate("inputdate");
                String inputuser = rs.getString("InputUser");
                float stock = rs.getFloat("stock");

                tree++;

                model2.addRow(new Object[]{tree, barcode, color, size, tp, mrp, inputuser,inpudate, stock});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void clear() {

        nametext.setText(null);
        categorybox.setSelectedIndex(0);

        supliyerbox.setSelectedIndex(0);
        unibox.setSelectedIndex(0);

        tptext.setText(null);
        mrptext.setText(null);

        vatcheck.setSelected(false);
        vattext.setEditable(false);
        vattext.setText(null);
        catid = null;

        suplyerid = null;
        unitid = null;

    }

   private void filterCategory() {

        supliyerbox.removeAllItems();
        supliyerbox.addItem("Select");
        supliyerbox.setSelectedIndex(0);
           
       
        try {
            String sql = "Select name from subcategory where catid='" + catid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String subcategory = rs.getString("name");
                supliyerbox.addItem(subcategory);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        } 
       try {
            String sql = "Select name from subcategory where catid='" + scatid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String subcategory = rs.getString("name");
                supliyerbox.setSelectedItem(subcategory);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        } 
       

    }

    private void itemdtotalstock() {
        try {
            String sql = "Select sum(Qty) as 'totalstock' from stockdetails where itemcode='" + itemcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String totalstock = rs.getString("totalstock");
                stocktext.setText(totalstock);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void countitem() {
        try {
            String sql = "Select count(*) as 'totalcount' from barcodeproduct where itemcode='" + itemcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String totalcount = rs.getString("totalcount");
                counttext.setText(totalcount);
            }

        } catch (Exception e) {
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void category() throws SQLException {

        try {
            String sql = "Select * from category";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String category = rs.getString("name");
                categorybox.addItem(category);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void suppliyer() throws SQLException {

        try {
            String sql = "Select * from suplyierInfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("supliyername");
                int id = rs.getInt("id");
                supliyerbox.addItem(name);

                //  supliyerbox.setSelectedIndex(id);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void unit() throws SQLException {

        try {
            String sql = "Select * from unit";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("unitshort");

                unibox.addItem(name);

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

        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        searchtext = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        barcodetextsearch = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        barcodetext = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        allcolorbox = new javax.swing.JComboBox<>();
        sizetext = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        tptextpro = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        mrptextpro = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        inputdate = new com.toedter.calendar.JDateChooser();
        jLabel24 = new javax.swing.JLabel();
        stocktextpro = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        sizetext4 = new javax.swing.JTextField();
        sizetext5 = new javax.swing.JTextField();
        sizetext6 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        namformattext = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        commenttext = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        unibox = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        mrptext = new javax.swing.JTextField();
        supliyerbox = new javax.swing.JComboBox<>();
        counttext = new javax.swing.JTextField();
        vattext = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        stocktext = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        categorybox = new javax.swing.JComboBox<>();
        nametext = new javax.swing.JTextField();
        tptext = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        vatcheck = new javax.swing.JCheckBox();
        openingdate = new com.toedter.calendar.JDateChooser();
        subcategorybox = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Item Description");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel4.setBackground(new java.awt.Color(67, 86, 86));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Product Name");

        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Product Code");

        searchtext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        searchtext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        searchtext.setBorder(null);
        searchtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchtextKeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Barcode");

        barcodetextsearch.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        barcodetextsearch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        barcodetextsearch.setBorder(null);
        barcodetextsearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                barcodetextsearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barcodetextsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(374, 374, 374))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(barcodetextsearch))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(itemnamesearch, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(searchtext))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        barcodetext.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        barcodetext.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        barcodetext.setEnabled(false);
        barcodetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                barcodetextKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Barcode");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Color");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Size");

        allcolorbox.setEditable(true);
        allcolorbox.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        allcolorbox.setBorder(null);
        allcolorbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                allcolorboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        sizetext.setEditable(false);
        sizetext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("TP");

        tptextpro.setEditable(false);
        tptextpro.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tptextpro.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("MRP");

        mrptextpro.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        mrptextpro.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Opening Date");

        inputdate.setForeground(new java.awt.Color(102, 102, 102));
        inputdate.setDateFormatString("yyyy-MM-dd");
        inputdate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setText("Stock");

        stocktextpro.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("Count Of Sale");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setText("Amount Of Sale");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setText("Amount Of profit");

        sizetext4.setEditable(false);
        sizetext4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        sizetext5.setEditable(false);
        sizetext5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        sizetext6.setEditable(false);
        sizetext6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jButton1.setBackground(new java.awt.Color(51, 0, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Barcode");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Name:");

        namformattext.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        namformattext.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        namformattext.setEnabled(false);
        namformattext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                namformattextKeyReleased(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setText("Comment");

        commenttext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        commenttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jButton2.setBackground(new java.awt.Color(0, 51, 51));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Update");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sizetext5)
                            .addComponent(sizetext6)
                            .addComponent(sizetext4)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(2, 2, 2)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(inputdate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(mrptextpro, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tptextpro, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sizetext, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(allcolorbox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(barcodetext, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(stocktextpro)
                                    .addComponent(commenttext, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(namformattext, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(barcodetext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(namformattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(allcolorbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sizetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tptextpro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mrptextpro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(inputdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(stocktextpro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sizetext4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sizetext5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sizetext6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SL No", "Barcode", "Color", "Size", "Trade Price", "Purchase Price", "Input User", "Input Date", "Stock"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
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
        datatbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setResizable(false);
            datatbl.getColumnModel().getColumn(0).setPreferredWidth(30);
            datatbl.getColumnModel().getColumn(1).setResizable(false);
            datatbl.getColumnModel().getColumn(2).setResizable(false);
            datatbl.getColumnModel().getColumn(3).setResizable(false);
            datatbl.getColumnModel().getColumn(4).setResizable(false);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
            datatbl.getColumnModel().getColumn(8).setResizable(false);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1023, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jTabbedPane1.addTab("Item Description", jPanel1);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Item Code:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Vat");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("PURCHASE PRICE");

        unibox.setEditable(true);
        unibox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        unibox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        unibox.setBorder(null);
        unibox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                uniboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        unibox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uniboxActionPerformed(evt);
            }
        });
        unibox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                uniboxPropertyChange(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Vat Requirment");

        mrptext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        mrptext.setForeground(new java.awt.Color(153, 0, 0));
        mrptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mrptext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        mrptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mrptextKeyTyped(evt);
            }
        });

        supliyerbox.setEditable(true);
        supliyerbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        supliyerbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        supliyerbox.setBorder(null);
        supliyerbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                supliyerboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        supliyerbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supliyerboxActionPerformed(evt);
            }
        });

        counttext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        counttext.setForeground(new java.awt.Color(0, 0, 51));

        vattext.setEditable(false);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("STOCK UNIT ");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("OPENING DATE");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Category:");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Name:");

        stocktext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        stocktext.setForeground(new java.awt.Color(0, 102, 51));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("SALE PRICE");

        jLabel20.setText("Total Stock");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("%");

        categorybox.setEditable(true);
        categorybox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        categorybox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        categorybox.setAutoscrolls(true);
        categorybox.setBorder(null);
        categorybox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryboxActionPerformed(evt);
            }
        });

        tptext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tptext.setForeground(new java.awt.Color(153, 0, 0));
        tptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tptext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        tptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tptextKeyTyped(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Sypplyer/Company");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Item Count");

        vatcheck.setBackground(new java.awt.Color(255, 255, 255));
        vatcheck.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        vatcheck.setText("Include ");
        vatcheck.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vatcheckMouseClicked(evt);
            }
        });

        openingdate.setForeground(new java.awt.Color(102, 102, 102));
        openingdate.setDateFormatString("yyyy-MM-dd");
        openingdate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        subcategorybox.setEditable(true);
        subcategorybox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        subcategorybox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        subcategorybox.setAutoscrolls(true);
        subcategorybox.setBorder(null);
        subcategorybox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subcategoryboxActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setText("Sub Category:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tptext))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(unibox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(categorybox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(vatcheck, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(vattext, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16)
                        .addGap(353, 353, 353))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subcategorybox, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addGap(3, 3, 3)
                        .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(nametext, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(openingdate, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(counttext, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stocktext, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nametext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openingdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(categorybox, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                            .addComponent(subcategorybox)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(6, 6, 6)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vatcheck, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(counttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stocktext, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible

        if (itemnamesearch.getSelectedIndex() > 0) {
            try {

                String searchcode = (String) itemnamesearch.getSelectedItem();

                String sql = "Select itemcode from item it where it.itemName='" + searchcode + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {

                    itemcode = rs.getString("itemcode");

                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            itemview();
        }


    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void searchtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchtextKeyReleased

        try {

            String sql = "Select itemcode,itemName from item it where it.itemcode='" + searchtext.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                itemcode = rs.getString("itemcode");
                String itemname = rs.getString("itemName");
                itemnamesearch.setSelectedItem(itemname);
                itemview();

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }


    }//GEN-LAST:event_searchtextKeyReleased

    private void barcodetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barcodetextKeyReleased
        /*
        try {

            //String searchtext = serachcode.getText();
            String sql = "Select * from item  where itemcode='" + codetext.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("itemcode");
                codetext.setText(Id);

                String Name = rs.getString("itemName");
                nametext.setText(Name);
                Date date = rs.getDate("openingDate");
                openingdate.setDate(date);
                String tpprice = rs.getString("tp");
                tptext.setText(tpprice);

                String marpprice = rs.getString("mrp");
                mrptext.setText(marpprice);
                int vatchek=rs.getInt("includevate");
                String vat=rs.getString("vat");
                if(vatchek==1){
                    vatcheck.setSelected(true);
                    vattext.setText(vat);
                }else{
                    vatcheck.setSelected(false);
                    vattext.setText(null);
                }
                catid = rs.getString("category");

                suplyerid = rs.getString("supliyer");
                unitid = rs.getString("stockunit");

                String remark = rs.getString("remark");
                remarktext.setText(remark);
                filterCategory();
                msgtext.setText("This Item Code Already Use, Try Another");
                msgtext.setForeground(Color.red);
            } else {

                msgtext.setText("This Item Code Empty,Ready To Use");
                msgtext.setForeground(Color.green);

                // codetext.setEnabled(false);
                nametext.setEnabled(true);
                categorybox.setEnabled(true);

                supliyerbox.setEnabled(true);
                unibox.setEnabled(true);

                tptext.setEnabled(true);
                mrptext.setEnabled(true);
                remarktext.setEnabled(true);
                statusbox.setEnabled(true);
                nametext.setText(null);
                categorybox.setSelectedIndex(0);

                supliyerbox.setSelectedIndex(0);
                unibox.setSelectedIndex(0);

                tptext.setText(null);
                mrptext.setText(null);
                vatcheck.setSelected(false);
                vattext.setEditable(false);
                vattext.setText(null);
                remarktext.setText(null);
                statusbox.setSelectedIndex(0);

                catid = null;

                suplyerid = null;
                unitid = null;

                //  checkcodemanul.setSelected(false);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
         */
    }//GEN-LAST:event_barcodetextKeyReleased

    private void tptextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tptextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_tptextKeyTyped

    private void mrptextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrptextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_mrptextKeyTyped

    private void uniboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_uniboxPopupMenuWillBecomeInvisible
        /*   try {
            String unit = (String) unibox.getSelectedItem();
            String sql = "Select * from unit where unitshort='" + unit + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                unitid = rs.getString("id");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

         */
    }//GEN-LAST:event_uniboxPopupMenuWillBecomeInvisible

    private void uniboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uniboxActionPerformed

    }//GEN-LAST:event_uniboxActionPerformed

    private void uniboxPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_uniboxPropertyChange

    }//GEN-LAST:event_uniboxPropertyChange

    private void categoryboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryboxActionPerformed
        /*
        try {
            String category = (String) categorybox.getSelectedItem();
            String sql = "Select * from category where name='" + category + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                catid = rs.getString("id");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

         */
    }//GEN-LAST:event_categoryboxActionPerformed

    private void supliyerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supliyerboxPopupMenuWillBecomeInvisible
        /* try {
            String sup = (String) supliyerbox.getSelectedItem();
            String sql = "Select * from suplyierinfo where supliyername='" + sup + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                suplyerid = rs.getString("id");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

         */
    }//GEN-LAST:event_supliyerboxPopupMenuWillBecomeInvisible

    private void supliyerboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supliyerboxActionPerformed

    }//GEN-LAST:event_supliyerboxActionPerformed

    private void vatcheckMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vatcheckMouseClicked
        if (vatcheck.isSelected()) {
            vattext.setEditable(true);

        } else {
            vattext.setText(null);
            vattext.setEditable(false);
        }
    }//GEN-LAST:event_vatcheckMouseClicked

    private void allcolorboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_allcolorboxPopupMenuWillBecomeInvisible
        /* String colorname=(String) allcolorbox.getSelectedItem();

        try {
            String sql = "Select id from color where name='"+colorname+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                colorid=rs.getString("id");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
         */
    }//GEN-LAST:event_allcolorboxPopupMenuWillBecomeInvisible

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
        DefaultTableModel model = (DefaultTableModel) datatbl.getModel();
        int selectedRowIndex = datatbl.getSelectedRow();
        String pbarcode=model.getValueAt(selectedRowIndex, 1).toString();
          try {
                    String sql = "Select nameformat,comment from barcodeproduct it where it.barcode='" + pbarcode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    if (rs.next()) {
                         
                        String namefrom=rs.getString("nameformat");
                        namformattext.setText(namefrom);
                        String comment=rs.getString("comment");
                        commenttext.setText(comment);
                       
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                } 
        barcodetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        allcolorbox.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        sizetext.setText(model.getValueAt(selectedRowIndex, 3).toString());
        tptextpro.setText(model.getValueAt(selectedRowIndex, 4).toString());
        mrptextpro.setText(model.getValueAt(selectedRowIndex, 5).toString());
        inputdate.setDate((java.util.Date) model.getValueAt(selectedRowIndex, 7));
        stocktextpro.setText(model.getValueAt(selectedRowIndex, 8).toString());
    }//GEN-LAST:event_datatblMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         try {
JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/Barcode.jrxml");
   
                String Imei=barcodetext.getText();
                barcodenamefromat=namformattext.getText();
                 barcodemrp=mrptextpro.getText();
                  barcodecomment=commenttext.getText();
             HashMap para = new HashMap();
              para.put("imeinumber", Imei);
              para.put("formatname", barcodenamefromat);
              para.put("mrp", barcodemrp);
              para.put("comment", barcodecomment);
             
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

                JasperViewer.viewReport(jp, false);
              

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void barcodetextsearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barcodetextsearchKeyReleased
      
        
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select barcode,itemcode,color,size,tp,mrp,inputdate,(select ad.name from admin ad where ad.id=it.inputuser) as 'InputUser',(select Qty from stockdetails st where st.barcode=it.barcode) as 'stock' from barcodeproduct it where it.barcode='" + barcodetextsearch.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //  datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                itemcode=rs.getString("itemcode");
                String barcode = rs.getString("barcode");
                String color = rs.getString("color");
                String size = rs.getString("size");
                double mrp = rs.getDouble("mrp");
                double tp = rs.getDouble("tp");
                Date inpudate = rs.getDate("inputdate");
                String inputuser = rs.getString("InputUser");
                float stock = rs.getFloat("stock");

                tree++;

                model2.addRow(new Object[]{tree, barcode, color, size, tp, mrp, inputuser,inpudate, stock});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        
           try {
           //  String sql1 = "Select itemcode,itemName,openingDate,tp,mrp,remark,category,subcategory,(select name from category ct where ct.id=it.category) as 'categoryname',supliyer,(Select supliyername from suplyierinfo sinfo where sinfo.id=it.supliyer) as 'suppliername',stockunit,(select unitshort from unit un where un.id=it.stockunit) as 'unitname',includevate,vat from item it where it.Itemcode='" + searchtext.getText() + "'";
            //String searchtext = serachcode.getText();
            String sql = "Select itemcode,itemName,openingDate,tp,mrp,remark,category,subcategory,(select name from category ct where ct.id=it.category) as 'categoryname',supliyer,(Select supliyername from suplyierinfo sinfo where sinfo.id=it.supliyer) as 'suppliername',stockunit,(select unitshort from unit un where un.id=it.stockunit) as 'unitname',includevate,vat from item it where it.itemcode='" + itemcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("itemcode");
                codetext.setText(Id);

                String Name = rs.getString("itemName");
                nametext.setText(Name);
                String categoryname=rs.getString("categoryname");
                categorybox.setSelectedItem(categoryname);
                String suppliername=rs.getString("suppliername");
                supliyerbox.setSelectedItem(suppliername);
                String unitname=rs.getString("unitname");
                unibox.setSelectedItem(unitname);
                Date date = rs.getDate("openingDate");
                openingdate.setDate(date);
                String tpprice = rs.getString("tp");
                tptext.setText(tpprice);

                String marpprice = rs.getString("mrp");
                mrptext.setText(marpprice);
                int vatchek = rs.getInt("includevate");
                String vat = rs.getString("vat");
                if (vatchek == 1) {
                    vatcheck.setSelected(true);
                    vattext.setText(vat);
                } else {
                    vatcheck.setSelected(false);
                    vattext.setText(null);
                }
                catid = rs.getString("category");
                scatid=rs.getString("subcategory");  
                suplyerid = rs.getString("supliyer");
                unitid = rs.getString("stockunit");
                  
                filterCategory();
                countitem();
                //table_update();
                itemdtotalstock();
            } else {
                clear();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_barcodetextsearchKeyReleased

    private void subcategoryboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subcategoryboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_subcategoryboxActionPerformed

    private void namformattextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_namformattextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_namformattextKeyReleased
private void barcodeview(String bcode){
   DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select barcode,itemcode,color,size,tp,mrp,inputdate,(select ad.name from admin ad where ad.id=it.inputuser) as 'InputUser',(select Qty from stockdetails st where st.barcode=it.barcode) as 'stock' from barcodeproduct it where it.barcode='" + bcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //  datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                itemcode=rs.getString("itemcode");
                String barcode = rs.getString("barcode");
                String color = rs.getString("color");
                String size = rs.getString("size");
                double mrp = rs.getDouble("mrp");
                double tp = rs.getDouble("tp");
                Date inpudate = rs.getDate("inputdate");
                String inputuser = rs.getString("InputUser");
                float stock = rs.getFloat("stock");

                tree++;

                model2.addRow(new Object[]{tree, barcode, color, size, tp, mrp, inputuser,inpudate, stock});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        
           try {
           //  String sql1 = "Select itemcode,itemName,openingDate,tp,mrp,remark,category,subcategory,(select name from category ct where ct.id=it.category) as 'categoryname',supliyer,(Select supliyername from suplyierinfo sinfo where sinfo.id=it.supliyer) as 'suppliername',stockunit,(select unitshort from unit un where un.id=it.stockunit) as 'unitname',includevate,vat from item it where it.Itemcode='" + searchtext.getText() + "'";
            //String searchtext = serachcode.getText();
            String sql = "Select itemcode,itemName,openingDate,tp,mrp,remark,category,subcategory,(select name from category ct where ct.id=it.category) as 'categoryname',supliyer,(Select supliyername from suplyierinfo sinfo where sinfo.id=it.supliyer) as 'suppliername',stockunit,(select unitshort from unit un where un.id=it.stockunit) as 'unitname',includevate,vat from item it where it.itemcode='" + itemcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("itemcode");
                codetext.setText(Id);

                String Name = rs.getString("itemName");
                nametext.setText(Name);
                String categoryname=rs.getString("categoryname");
                categorybox.setSelectedItem(categoryname);
                String suppliername=rs.getString("suppliername");
                supliyerbox.setSelectedItem(suppliername);
                String unitname=rs.getString("unitname");
                unibox.setSelectedItem(unitname);
                Date date = rs.getDate("openingDate");
                openingdate.setDate(date);
                String tpprice = rs.getString("tp");
                tptext.setText(tpprice);

                String marpprice = rs.getString("mrp");
                mrptext.setText(marpprice);
                int vatchek = rs.getInt("includevate");
                String vat = rs.getString("vat");
                if (vatchek == 1) {
                    vatcheck.setSelected(true);
                    vattext.setText(vat);
                } else {
                    vatcheck.setSelected(false);
                    vattext.setText(null);
                }
                catid = rs.getString("category");
                scatid=rs.getString("subcategory");  
                suplyerid = rs.getString("supliyer");
                unitid = rs.getString("stockunit");
                  
                filterCategory();
                countitem();
                //table_update();
                itemdtotalstock();
            } else {
                clear();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }


}
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
      
        if(barcodetext.getText().isEmpty() || mrptextpro.getText().isEmpty()){
        
        
        }else{
             String id = barcodetext.getText();
             String qty=stocktextpro.getText();
        try {

           

            String sql = "Update barcodeproduct set mrp='" + mrptextpro.getText() + "',	comment='" + commenttext.getText() + "' where barcode='" + id + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
           

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
            try {

            

            String sql = "Update stockdetails set Qty='" + qty + "' where barcode='" + id + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
             JOptionPane.showMessageDialog(null, "Data Update");
            barcodeview(id);

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        
        barcodetext.setText(null);
        namformattext.setText(null);
        sizetext.setText(null);
        tptextpro.setText(null);
        mrptextpro.setText(null);
        stocktextpro.setText(null);
        commenttext.setText(null);
        }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> allcolorbox;
    private javax.swing.JTextField barcodetext;
    private javax.swing.JTextField barcodetextsearch;
    private javax.swing.JComboBox<String> categorybox;
    private javax.swing.JTextField codetext;
    private javax.swing.JTextField commenttext;
    private javax.swing.JTextField counttext;
    private javax.swing.JTable datatbl;
    private com.toedter.calendar.JDateChooser inputdate;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField mrptext;
    private javax.swing.JTextField mrptextpro;
    private javax.swing.JTextField nametext;
    private javax.swing.JTextField namformattext;
    private com.toedter.calendar.JDateChooser openingdate;
    private javax.swing.JTextField searchtext;
    private javax.swing.JTextField sizetext;
    private javax.swing.JTextField sizetext4;
    private javax.swing.JTextField sizetext5;
    private javax.swing.JTextField sizetext6;
    private javax.swing.JTextField stocktext;
    private javax.swing.JTextField stocktextpro;
    private javax.swing.JComboBox<String> subcategorybox;
    private javax.swing.JComboBox<String> supliyerbox;
    private javax.swing.JTextField tptext;
    private javax.swing.JTextField tptextpro;
    private javax.swing.JComboBox<String> unibox;
    private javax.swing.JCheckBox vatcheck;
    private javax.swing.JTextField vattext;
    // End of variables declaration//GEN-END:variables
}
