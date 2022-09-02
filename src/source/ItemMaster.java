package source;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.proteanit.sql.DbUtils;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import net.proteanit.sql.DbUtils;
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
public final class ItemMaster extends javax.swing.JInternalFrame {

    int selectedunitid;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    /**
     * Creates new form ItemMaster
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public ItemMaster() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        AutoCompleteDecorator.decorate(categorybox);
        AutoCompleteDecorator.decorate(categoryboxsearch);
        AutoCompleteDecorator.decorate(supliyerbox);
        AutoCompleteDecorator.decorate(unibox);
        AutoCompleteDecorator.decorate(itemnamesearch);
        AutoCompleteDecorator.decorate(suppliyerboxsearch);
        Item();
        category();
        table_update();
        suppliyer();
        unit();
        currentDate();
        itemcount();
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        java.sql.Date date = new java.sql.Date(now.getTime());
        openingdate.setDate(date);

    }

    private void loadItem() throws SQLException {

        itemnamesearch.removeAllItems();
        itemnamesearch.setSelectedItem("Select");

        categorybox.removeAllItems();
        categoryboxsearch.removeAllItems();

        categorybox.setSelectedItem("Select");
        categoryboxsearch.setSelectedItem("Select");

        unibox.removeAllItems();
        unibox.setSelectedItem("Select");

        table_update();
        supliyerbox.removeAllItems();
        suppliyerboxsearch.removeAllItems();
        suppliyerboxsearch.setSelectedItem("Select");
        supliyerbox.setSelectedItem("Select");
        category();
        unit();
        currentDate();
        Item();
        suppliyer();
         itemcount();
    }

    private void itemcount() {

        try {
            String sql = "Select count(id) as 'itemcount' from item";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("itemcount");
                itemcounttext.setText(name);
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
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                int id = rs.getInt("id");
                String category = rs.getString("name");
                categorybox.addItem(category);
                categoryboxsearch.addItem(category);
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
                suppliyerboxsearch.addItem(name);
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
                int unitid = rs.getInt("id");

                unibox.addItem(name);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void Stockentry(int generatedKey) throws SQLException {
        String prcodeS = String.format("%d", generatedKey);
        try {

            String sql = "Insert into stock(procode,Qty,unit,status) values(?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setString(1, prcodeS);
            pst.setFloat(2, 0);

            pst.setString(3, (String) unibox.getSelectedItem());
            pst.setInt(4, 0);
            pst.execute();

            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void addStock() throws SQLException {
        if (nametext.getText().isEmpty() || categorybox.getSelectedIndex() == 0 || supliyerbox.getSelectedIndex() == 0 || unibox.getSelectedIndex() == 0 || tptext.getText().isEmpty() || mrptext.getText().isEmpty()) {

        } else {

            try {

                String sql = "Insert into item(barcode,itemName,category,supliyer,stockunit,openingDate,tp,mrp,remark,status) values(?,?,?,?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                pst.setString(1, barcodetext.getText());
                pst.setString(2, nametext.getText());
                pst.setString(3, (String) categorybox.getSelectedItem());

                pst.setString(4, (String) supliyerbox.getSelectedItem());
                pst.setString(5, (String) unibox.getSelectedItem());

                pst.setString(6, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
                pst.setString(7, tptext.getText());
                pst.setString(8, mrptext.getText());

                pst.setString(9, remarktext.getText());
                pst.setString(10, (String) statusbox.getSelectedItem());
                pst.execute();

                ResultSet rshere = pst.getGeneratedKeys();
                int generatedKey = 0;
                if (rshere.next()) {
                    generatedKey = rshere.getInt(1);
                    Stockentry(generatedKey);
                }

                //JOptionPane.showMessageDialog(null, "Data Saved");
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }

    private void table_update() throws SQLException {

        try {
            String sql = "Select id as 'Code',itemname as 'Item',category as 'Category' , stockunit as 'stockunit', tp as 'TradePrice' ,mrp as 'MRP', status as 'Status'  from item ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
private void afterinsertexecution() throws SQLException{
itemcount();
itemnamesearch.removeAllItems();
  itemnamesearch.setSelectedItem("Select");


 Item(); 
}
    private void clear() throws SQLException {
        codetext.setText(null);
        nametext.setText(null);

        openingdate.setDate(null);
        tptext.setText(null);
        mrptext.setText(null);
        remarktext.setText(null);
        barcodetext.setText(null);
        categorybox.setSelectedIndex(0);
        supliyerbox.setSelectedIndex(0);
        unibox.setSelectedIndex(0);
        currentDate();
      
        //itemnamesearch.removeAllItems();
        //itemnamesearch.setSelectedItem("Select");
       
      //  loadItem();
    }

    private void search() {

        String SearchText = codetexysearch.getText();
        try {

            String sql = "Select id as 'Code',itemname as 'Item',category as 'Category' , stockunit as 'stockunit', tp as 'TradePrice' ,mrp as 'MRP', status as 'Status'  from item where id='" + SearchText + "' OR barcode='" + SearchText + "' ";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            datatbl.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void stockUpdate() {
        try {
            String date = ((JTextField) openingdate.getDateEditor().getUiComponent()).getText();
            String id = codetext.getText();

            String sql = "Update item set barcode='"+barcodetext.getText()+"',itemName='" + nametext.getText() + "',category='" + categorybox.getSelectedItem() + "',supliyer='" + supliyerbox.getSelectedItem() + "',stockunit='" + unibox.getSelectedItem() + "',openingDate='" + date + "',tp='" + tptext.getText() + "',mrp='" + mrptext.getText() + "',remark='" + remarktext.getText() + "' where id='" + id + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Update");

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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        nametext = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        openingdate = new com.toedter.calendar.JDateChooser();
        tptext = new javax.swing.JTextField();
        mrptext = new javax.swing.JTextField();
        statusbox = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        unibox = new javax.swing.JComboBox<>();
        categorybox = new javax.swing.JComboBox<>();
        supliyerbox = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarktext = new javax.swing.JTextArea();
        barcodetext = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        codetexysearch = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        categoryboxsearch = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        suppliyerboxsearch = new javax.swing.JComboBox<>();
        checkloaditems = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        itemcounttext = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Itm Master");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 102, 0));
        jLabel1.setText("ITEM ENTRY");

        codetext.setEnabled(false);

        nametext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nametext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("ITEM NAME");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("STATUS");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setText("REMARK");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("ITEM CATEGORY");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("CODE");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("Sypplyer/Company");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 0, 0));
        jLabel8.setText("TRP");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("STOCK UNIT ");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("OPENING DATE");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("TP");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("Barcode");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                            .addGap(95, 95, 95)
                                            .addComponent(jLabel2))
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(12, 12, 12)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        openingdate.setForeground(new java.awt.Color(102, 102, 102));
        openingdate.setDateFormatString("yyyy-MM-dd");

        tptext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tptext.setForeground(new java.awt.Color(153, 0, 0));
        tptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tptext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        tptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tptextKeyTyped(evt);
            }
        });

        mrptext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        mrptext.setForeground(new java.awt.Color(153, 0, 0));
        mrptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mrptext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        mrptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mrptextKeyTyped(evt);
            }
        });

        statusbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        statusbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Deactive" }));

        jButton2.setBackground(new java.awt.Color(204, 0, 0));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("EXECUTE");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 153, 102));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("CLEAR");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

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

        supliyerbox.setEditable(true);
        supliyerbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        supliyerbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        supliyerbox.setBorder(null);
        supliyerbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supliyerboxActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(153, 0, 0));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Delete");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        remarktext.setColumns(20);
        remarktext.setRows(5);
        jScrollPane2.setViewportView(remarktext);

        barcodetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        barcodetext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jButton4.setBackground(new java.awt.Color(0, 102, 51));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Barcode");
        jButton4.setBorder(null);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(250, 250, 250)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(nametext, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(categorybox, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(openingdate, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tptext, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(statusbox, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(barcodetext, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addComponent(nametext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(barcodetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(categorybox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(openingdate, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(tptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(statusbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 500, 500));

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 102, 0));
        jLabel12.setText("CODE");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, 28));

        codetexysearch.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        codetexysearch.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        codetexysearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetexysearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codetexysearchKeyTyped(evt);
            }
        });
        jPanel4.add(codetexysearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, 140, 28));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 102, 0));
        jLabel15.setText("ITEM NAME");
        jPanel4.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 70, 70, 30));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 153, 0));
        jLabel16.setText("CATEGORY");
        jPanel4.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 10, 70, 30));

        categoryboxsearch.setEditable(true);
        categoryboxsearch.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        categoryboxsearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        categoryboxsearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                categoryboxsearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        categoryboxsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryboxsearchActionPerformed(evt);
            }
        });
        jPanel4.add(categoryboxsearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 10, 140, 29));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 102, 0));
        jLabel17.setText("SUPLYIER");
        jPanel4.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 70, 80, 30));

        itemnamesearch.setEditable(true);
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        itemnamesearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemnamesearchMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                itemnamesearchMousePressed(evt);
            }
        });
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        itemnamesearch.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                itemnamesearchItemStateChanged(evt);
            }
        });
        itemnamesearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemnamesearchActionPerformed(evt);
            }
        });
        itemnamesearch.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                itemnamesearchPropertyChange(evt);
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
        jPanel4.add(itemnamesearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 70, 190, 30));

        suppliyerboxsearch.setEditable(true);
        suppliyerboxsearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        suppliyerboxsearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        suppliyerboxsearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                suppliyerboxsearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        jPanel4.add(suppliyerboxsearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 70, 230, 30));

        checkloaditems.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        checkloaditems.setForeground(new java.awt.Color(0, 153, 0));
        checkloaditems.setText("Load Items");
        checkloaditems.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkloaditemsMouseClicked(evt);
            }
        });
        jPanel4.add(checkloaditems, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 10, 810, 110));

        datatbl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        datatbl.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        datatbl.setRowHeight(30);
        datatbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(datatbl);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 130, 810, 320));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Item Count");

        itemcounttext.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        itemcounttext.setForeground(new java.awt.Color(0, 102, 0));
        itemcounttext.setText("Item Count");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(itemcounttext)
                .addContainerGap(638, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(itemcounttext))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 460, 810, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1337, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setBounds(5, 5, 1339, 546);
    }// </editor-fold>//GEN-END:initComponents

    private void tptextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tptextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_tptextKeyTyped

    private void mrptextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrptextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_mrptextKeyTyped

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (codetext.getText().isEmpty()) {

            try {
                addStock();
            } catch (SQLException ex) {
                Logger.getLogger(ItemMaster.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            stockUpdate();

        }
        try {
            table_update();
            clear();
            afterinsertexecution();
            
        } catch (SQLException ex) {
            Logger.getLogger(ItemMaster.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void itemnamesearchPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_itemnamesearchPropertyChange


    }//GEN-LAST:event_itemnamesearchPropertyChange

    private void itemnamesearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyReleased


    }//GEN-LAST:event_itemnamesearchKeyReleased

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
        if (itemnamesearch.getSelectedIndex() > 0) {
            String SearchText = (String) itemnamesearch.getSelectedItem();
            try {

                String sql = "Select id as 'Code',itemname as 'Item',category as 'Category' , stockunit as 'stockunit', tp as 'TradePrice' ,mrp as 'MRP', status as 'Status'  from item where itemName='"+SearchText+"'";
                pst = conn.prepareStatement(sql);
               

                rs = pst.executeQuery();
                datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
        //categorybox.removeAllItems();
        //unibox.removeAllItems();
       /// supliyerbox.removeAllItems();
        try {

            int row = datatbl.getSelectedRow();
            String table_click = (datatbl.getModel().getValueAt(row, 0).toString());
            String sql = "Select * from item where id='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("id");
                codetext.setText(Id);
                String barcode=rs.getString("barcode");
                barcodetext.setText(barcode);
                String Name = rs.getString("itemName");
                nametext.setText(Name);
                Date date = rs.getDate("openingDate");
                openingdate.setDate(date);
                String tpprice = rs.getString("tp");
                tptext.setText(tpprice);

                String marpprice = rs.getString("mrp");
                mrptext.setText(marpprice);

                String category = rs.getString("category");
                categorybox.setSelectedItem(category);

                String suppliyer = rs.getString("supliyer");
                supliyerbox.setSelectedItem(suppliyer);

                String unit = rs.getString("stockunit");
                unibox.setSelectedItem(unit);

                String remark = rs.getString("remark");
                remarktext.setText(remark);
               // category();
                //suppliyer();
                //unit();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_datatblMouseClicked

    private void itemnamesearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyTyped

    }//GEN-LAST:event_itemnamesearchKeyTyped

    private void itemnamesearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemnamesearchMouseClicked

    }//GEN-LAST:event_itemnamesearchMouseClicked

    private void itemnamesearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyPressed

    }//GEN-LAST:event_itemnamesearchKeyPressed

    private void itemnamesearchMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemnamesearchMousePressed

    }//GEN-LAST:event_itemnamesearchMousePressed

    private void itemnamesearchItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_itemnamesearchItemStateChanged

    }//GEN-LAST:event_itemnamesearchItemStateChanged

    private void itemnamesearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemnamesearchActionPerformed


    }//GEN-LAST:event_itemnamesearchActionPerformed

    private void categoryboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryboxActionPerformed

    }//GEN-LAST:event_categoryboxActionPerformed

    private void supliyerboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supliyerboxActionPerformed

    }//GEN-LAST:event_supliyerboxActionPerformed

    private void uniboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uniboxActionPerformed

    }//GEN-LAST:event_uniboxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            clear();
        } catch (SQLException ex) {
            Logger.getLogger(ItemMaster.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (codetext.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "You Have to Select Data From Data Table");

        } else {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from item where id=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, codetext.getText());

                    pst.execute();
                   // JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                try {
                    String sql = "Delete from stock where procode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, codetext.getText());

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }
            try {
                clear();
            } catch (SQLException ex) {
                Logger.getLogger(ItemMaster.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                table_update();
                 afterinsertexecution();
            } catch (SQLException ex) {
                Logger.getLogger(Locale.Category.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void uniboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_uniboxPopupMenuWillBecomeInvisible


    }//GEN-LAST:event_uniboxPopupMenuWillBecomeInvisible

    private void uniboxPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_uniboxPropertyChange

    }//GEN-LAST:event_uniboxPropertyChange

    private void categoryboxsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryboxsearchActionPerformed
        String SearchText = (String) suppliyerboxsearch.getSelectedItem();
        try {

            String sql = "Select id as 'Code',itemname as 'Item',category as 'Category' , stockunit as 'stockunit', tp as 'TradePrice' ,mrp as 'MRP', status as 'Status'  from item where category='" + SearchText + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            datatbl.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        
              try {
                String sql = "Select count(id) as 'itemcount' from item where category='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    String name = rs.getString("itemcount");
                    itemcounttext.setText(name);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
    }//GEN-LAST:event_categoryboxsearchActionPerformed

    private void codetexysearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetexysearchKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_codetexysearchKeyTyped

    private void codetexysearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetexysearchKeyReleased
        search();
    }//GEN-LAST:event_codetexysearchKeyReleased

    private void checkloaditemsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkloaditemsMouseClicked
        if (checkloaditems.isSelected()) {

            try {
               afterinsertexecution();
            } catch (SQLException ex) {
                Logger.getLogger(ItemMaster.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_checkloaditemsMouseClicked

    private void suppliyerboxsearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_suppliyerboxsearchPopupMenuWillBecomeInvisible

        if (suppliyerboxsearch.getSelectedIndex() > 0) {
            String SearchText = (String) suppliyerboxsearch.getSelectedItem();
            try {

                String sql = "Select id as 'Code',itemname as 'Item',category as 'Category' , stockunit as 'stockunit', tp as 'TradePrice' ,mrp as 'MRP', status as 'Status'  from item where supliyer='" + SearchText + "'";
                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();
                datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

            try {
                String sql = "Select count(id) as 'itemcount' from item where supliyer='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    String name = rs.getString("itemcount");
                    itemcounttext.setText(name);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_suppliyerboxsearchPopupMenuWillBecomeInvisible

    private void categoryboxsearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_categoryboxsearchPopupMenuWillBecomeInvisible

        if (categoryboxsearch.getSelectedIndex() > 0) {
            String SearchText = (String) categoryboxsearch.getSelectedItem();
            try {

                String sql = "Select id as 'Code',itemname as 'Item',category as 'Category' , stockunit as 'stockunit', tp as 'TradePrice' ,mrp as 'MRP', status as 'Status'  from item where category='" + SearchText + "'";
                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();
                datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            
        try {
                String sql = "Select count(id) as 'itemcount' from item where category='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    String name = rs.getString("itemcount");
                    itemcounttext.setText(name);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_categoryboxsearchPopupMenuWillBecomeInvisible

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if(codetext.getText().isEmpty()){

        }else{
            try {
                JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/Barcode.jrxml");

                int Imei=Integer.parseInt(codetext.getText());

                HashMap para = new HashMap();

                para.put("imeinumber", Imei);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }

    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField barcodetext;
    private javax.swing.JComboBox<String> categorybox;
    private javax.swing.JComboBox<String> categoryboxsearch;
    private javax.swing.JCheckBox checkloaditems;
    private javax.swing.JTextField codetext;
    private javax.swing.JTextField codetexysearch;
    private javax.swing.JTable datatbl;
    private javax.swing.JLabel itemcounttext;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField mrptext;
    private javax.swing.JTextField nametext;
    private com.toedter.calendar.JDateChooser openingdate;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JComboBox<String> statusbox;
    private javax.swing.JComboBox<String> supliyerbox;
    private javax.swing.JComboBox<String> suppliyerboxsearch;
    private javax.swing.JTextField tptext;
    private javax.swing.JComboBox<String> unibox;
    // End of variables declaration//GEN-END:variables
}
