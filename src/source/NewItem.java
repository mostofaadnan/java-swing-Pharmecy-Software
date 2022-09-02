/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.HeadlessException;
import java.io.FileInputStream;
import java.io.IOException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class NewItem extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    String catid = null;

    String brandid = null;
    String unitid = "1";
    // Dashboard dashboard = new Dashboard();
    int userid = 0;
    int updatekey = 0;
    java.sql.Date prodate;
    String Itemcodedate;

    /**
     * Creates new form NewItem
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public NewItem() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        AutoCompleteDecorator.decorate(categorybox);
        AutoCompleteDecorator.decorate(brandbox);
        AutoCompleteDecorator.decorate(unibox);
        AutoCompleteDecorator.decorate(itemnamesearch);
        accsessModification();
        category();
        brand();
        unit();
        currentDate();
        parchase_code();
        //  itemlist.revalidate();
        Item();

    }

    public NewItem(String table_click) throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        AutoCompleteDecorator.decorate(categorybox);
        AutoCompleteDecorator.decorate(brandbox);
        AutoCompleteDecorator.decorate(unibox);
        AutoCompleteDecorator.decorate(itemnamesearch);
        accsessModification();
        category();
        brand();
        unit();
        currentDate();
        parchase_code();
        //  itemlist.revalidate();
        itemview(table_click);
        Item();

//To change body of generated methods, choose Tools | Templates.
    }

    private void userkey() throws IOException, SQLException {
        FileInputStream fis = new FileInputStream("src\\userkey.properties");

        Properties p = new Properties();
        p.load(fis);

        String userids = (String) p.get("userid");
        userid = Integer.parseInt(userids);
        // this.dispose();
        //LoginAccess desboard=new LoginAccess();
    }

    private void accsessModification() {
        try {
            String sql = "Select itmedit,itmdelete from modificationaccsess where userid='" + userid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                int itmedit = rs.getInt("itmedit");
                int itmdelete = rs.getInt("itmdelete");

                if (itmedit == 1) {

                    svbtn.setEnabled(true);

                } else {

                    svbtn.setEnabled(false);

                }
                if (itmdelete == 1) {

                    deletebtn.setEnabled(true);

                } else {

                    deletebtn.setEnabled(false);

                }

            } else {
                svbtn.setEnabled(false);
                deletebtn.setEnabled(false);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void Item() throws SQLException {

        try {
            String sql = "Select itemName from item ORDER BY itemName ASC";
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

    private void itemview(String searchtext) {

        try {

            String sql = "Select "
                    + "itemcode,"
                    + "itemName,"
                    + "openingDate,"
                    + "tp,"
                    + "mrp,"
                    + "remark,"
                    + "category,"
                    + "(select name from category ct where ct.id=it.category) as 'categoryname',"
                    + "brand_id,"
                    + "(Select name from brand sinfo where sinfo.id=it.brand_id) as 'brandname',"
                    + "stockunit,"
                    + "(select unitshort from unit un where un.id=it.stockunit) as 'unitname',"
                    + "includevate,"
                    + "vat from item it where it.itemcode='" + searchtext + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("itemcode");
                codetext.setText(Id);

                String Name = rs.getString("itemName");
                nametext.setText(Name);
                String categoryname = rs.getString("categoryname");
                categorybox.setSelectedItem(categoryname);
                String suppliername = rs.getString("brandname");
                brandbox.setSelectedItem(suppliername);
                String unitname = rs.getString("unitname");
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
                brandid = rs.getString("brand_id");
                unitid = rs.getString("stockunit");

                String remark = rs.getString("remark");
                remarktext.setText(remark);
                updatekey = 1;
                filterCategory();
            } else {
                clear();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void clear() {

        parchase_code();
        nametext.setText(null);
        categorybox.setSelectedIndex(0);

        brandbox.setSelectedIndex(0);
        unibox.setSelectedIndex(0);
        currentDate();
        tptext.setText(null);
        mrptext.setText(null);
        remarktext.setText(null);
        statusbox.setSelectedIndex(0);
        vatcheck.setSelected(false);
        vattext.setEditable(false);
        vattext.setText(null);
        catid = null;

        brandid = null;
        unitid = null;
        updatekey = 0;

    }

    private void currentDatepro() {

        DateFormat df = new SimpleDateFormat("hhmmssdMM");
        java.util.Date now = new java.util.Date();

        prodate = new java.sql.Date(now.getTime());
        Itemcodedate = df.format(prodate);
        //  inputdate.setDate(date);

    }

    private void currentDate() {

        java.util.Date now = new java.util.Date();
        java.sql.Date nowdate = new java.sql.Date(now.getTime());
        openingdate.setDate(nowdate);

    }

    private void filterCategory() {
        /*
         subcategorybox.removeAllItems();
         subcategorybox.addItem("Select");
         subcategorybox.setSelectedIndex(0);
           
       
         try {
         String sql = "Select name from subcategory where catid='" + catid + "'";
         pst = conn.prepareStatement(sql);
         rs = pst.executeQuery();
         //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
         while (rs.next()) {

         String subcategory = rs.getString("name");
         subcategorybox.addItem(subcategory);

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
         subcategorybox.setSelectedItem(subcategory);

         }
         } catch (Exception e) {
         JOptionPane.showMessageDialog(null, e);

         } 
       
         */
    }

    private void parchase_code() {
        try {
            currentDatepro();
            int new_inv = 1;
            String NewParchaseCode = (+new_inv + "");
            String sql = "Select id from item";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = (ParchaseCode);
            }
            // String finelcode = NewParchaseCode + Itemcodedate;
            String finelcode = NewParchaseCode;
            String StartCode = "10";
            codetext.setText(StartCode + finelcode);
        } catch (SQLException | NumberFormatException e) {
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void brand() throws SQLException {

        try {
            String sql = "Select name from brand";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("name");
                brandbox.addItem(name);

                //  supliyerbox.setSelectedIndex(id);
            }
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void codeCheck() {
        if (codetext.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Item Code Cannot  be Null");
        } else {

            try {
                String sql = "Select Itemcode from item where Itemcode='" + codetext.getText() + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                if (rs.next() == true) {
                    JOptionPane.showMessageDialog(null, "This Item Code Already Inserted,Try Another");

                } else {

                    vatCheck();

                }
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }

    private void vatCheck() throws SQLException {
        if (vatcheck.isSelected()) {
            if (vattext.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Include Vat,In vat Field");

            } else {
                addStock();

            }
        } else {
            addStock();

        }

    }

    private void addStock() throws SQLException {

        if (nametext.getText().isEmpty() || categorybox.getSelectedIndex() == 0 || tptext.getText().isEmpty() || mrptext.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select Requirment Field.You Must Fillup Yellow Color");
            if (nametext.getText().isEmpty()) {
                nametext.setBackground(Color.YELLOW);

            }
            if (categorybox.getSelectedIndex() == 0) {

                categorybox.setBackground(Color.YELLOW);
            }

            if (brandbox.getSelectedIndex() == 0) {

                brandbox.setBackground(Color.YELLOW);
            }

            if (tptext.getText().isEmpty()) {
                tptext.setBackground(Color.YELLOW);

            }

            if (mrptext.getText().isEmpty()) {
                mrptext.setBackground(Color.YELLOW);

            }
        } else {

            try {

                String sql = "Insert into item(itemcode,"
                        + "itemName,"
                        + "category,"
                        + "brand_id,"
                        + "stockunit,"
                        + "openingDate,"
                        + "tp,"
                        + "mrp,"
                        + "includevate,"
                        + "vat,"
                        + "inputuser,"
                        + "updateuser,"
                        + "lastupdate,"
                        + "remark,"
                        + "status) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                //pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pst = conn.prepareStatement(sql);
                pst.setString(1, codetext.getText());
                pst.setString(2, nametext.getText());
                pst.setString(3, catid);
                pst.setString(4, brandid);
                pst.setString(5, unitid);
                pst.setString(6, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
                pst.setString(7, tptext.getText());
                pst.setString(8, mrptext.getText());
                if (vatcheck.isSelected()) {
                    pst.setInt(9, 1);
                    pst.setString(10, vattext.getText());
                } else {
                    pst.setInt(9, 0);
                    pst.setDouble(10, 0);
                }
                pst.setInt(11, userid);
                pst.setInt(12, userid);
                pst.setString(13, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
                pst.setString(14, remarktext.getText());

                pst.setString(15, (String) statusbox.getSelectedItem());

                pst.execute();
                Stockentry();

                clear();
                /*
                 ResultSet rshere = pst.getGeneratedKeys();
                 int generatedKey = 0;
                 if (rshere.next()) {
                 generatedKey = rshere.getInt(1);
                 Stockentry(generatedKey);
                 }
                 */
                //JOptionPane.showMessageDialog(null, "Data Saved");
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }

    private void vatCheckupdate() throws SQLException {
        if (vatcheck.isSelected()) {
            if (vattext.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Include Vat,In vat Field");

            } else {
                stockUpdate();

            }
        } else {
            stockUpdate();

        }

    }

    private void stockUpdate() {
        int includevat;
        String vat = "0";
        try {
            if (vatcheck.isSelected()) {
                includevat = 1;
                vat = vattext.getText();
            } else {
                includevat = 0;
                vat = "0";
            }
            String date = ((JTextField) openingdate.getDateEditor().getUiComponent()).getText();
            String id = codetext.getText();

            String sql = "Update item set "
                    + "itemName='" + nametext.getText() + "',"
                    + "category='" + catid + "',"
                    + "brand_id='" + brandid + "',"
                    + "stockunit='" + unitid + "',"
                    + "openingDate='" + date + "',"
                    + "tp='" + tptext.getText() + "',"
                    + "mrp='" + mrptext.getText() + "',"
                    + "includevate='" + includevat + "',"
                    + "vat='" + vat + "',"
                    + "remark='" + remarktext.getText() + "'"
                    + " where Itemcode='" + id + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            updatekey = 0;
            JOptionPane.showMessageDialog(null, "Data Update");
            clear();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void Stockentry() throws SQLException {
        // String prcodeS = String.format("%d", generatedKey);
        try {

            String sql = "Insert into stockdetails(itemcode,Qty) values(?,?)";
            pst = conn.prepareStatement(sql);

            pst.setString(1, codetext.getText());
            pst.setFloat(2, 0);

            pst.execute();

            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void stockCheck() {

        if (codetext.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Item Code Cannot  be Null");
        } else {

            try {
                String sql = "Select Qty from stockdetails where itemcode='" + codetext.getText() + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                if (rs.next()) {

                    float qty = rs.getFloat("Qty");
                    if (qty == 0) {
                        deleteproduct();
                    } else {

                        JOptionPane.showMessageDialog(null, "This Item Stock Already Available,Please Make Sure Zero");
                    }
                }
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }

    private void deleteproduct() throws SQLException {

        if (updatekey == 1) {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from item where Itemcode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, codetext.getText());

                    pst.execute();
                    // JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                try {
                    String sql = "Delete from stockdetails where itemcode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, codetext.getText());

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                itemnamesearch.removeAllItems();
                itemnamesearch.addItem("Select");
                Item();
                itemnamesearch.setSelectedIndex(0);
            }

            clear();

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     *
     * @param args
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        searchtext = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        codetext = new javax.swing.JTextField();
        nametext = new javax.swing.JTextField();
        openingdate = new com.toedter.calendar.JDateChooser();
        tptext = new javax.swing.JTextField();
        mrptext = new javax.swing.JTextField();
        statusbox = new javax.swing.JComboBox<>();
        unibox = new javax.swing.JComboBox<>();
        categorybox = new javax.swing.JComboBox<>();
        brandbox = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarktext = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        checkcodemanul = new javax.swing.JCheckBox();
        msgtext = new javax.swing.JLabel();
        vatcheck = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        vattext = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        deletebtn = new javax.swing.JButton();
        svbtn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setTitle("Item");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addGap(234, 234, 234))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(searchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(searchtext))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        codetext.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        codetext.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        codetext.setEnabled(false);
        codetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetextKeyReleased(evt);
            }
        });

        nametext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nametext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        nametext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nametextKeyReleased(evt);
            }
        });

        openingdate.setForeground(new java.awt.Color(102, 102, 102));
        openingdate.setDateFormatString("yyyy-MM-dd");
        openingdate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tptext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tptext.setForeground(new java.awt.Color(153, 0, 0));
        tptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tptext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        tptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tptextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tptextKeyTyped(evt);
            }
        });

        mrptext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        mrptext.setForeground(new java.awt.Color(153, 0, 0));
        mrptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mrptext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        mrptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mrptextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mrptextKeyTyped(evt);
            }
        });

        statusbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        statusbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Deactive" }));

        unibox.setEditable(true);
        unibox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
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
        categorybox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                categoryboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        categorybox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryboxActionPerformed(evt);
            }
        });

        brandbox.setEditable(true);
        brandbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        brandbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        brandbox.setBorder(null);
        brandbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                brandboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        brandbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brandboxActionPerformed(evt);
            }
        });

        remarktext.setColumns(20);
        remarktext.setRows(5);
        jScrollPane2.setViewportView(remarktext);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("CODE");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("ITEM NAME");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("CATEGORY");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Brand");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("STOCK UNIT ");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("OPENING DATE");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("PURCHASE PRICE");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("SALE PRICE");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("REMARK");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("STATUS");

        checkcodemanul.setBackground(new java.awt.Color(255, 255, 255));
        checkcodemanul.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        checkcodemanul.setText("Manualy Code Entry");
        checkcodemanul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkcodemanulActionPerformed(evt);
            }
        });

        vatcheck.setBackground(new java.awt.Color(255, 255, 255));
        vatcheck.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        vatcheck.setText("Include ");
        vatcheck.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vatcheckMouseClicked(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Vat Requirment");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Vat");

        vattext.setEditable(false);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("%");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(openingdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(unibox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(mrptext)
                            .addComponent(jScrollPane2)
                            .addComponent(msgtext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(codetext)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel16))
                            .addComponent(categorybox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(brandbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nametext, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(checkcodemanul)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(vatcheck)
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(vattext, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(tptext, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(statusbox, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(msgtext, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkcodemanul)
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nametext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(categorybox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(brandbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(openingdate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(vatcheck, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(vattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(statusbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        deletebtn.setBackground(new java.awt.Color(255, 255, 255));
        deletebtn.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        deletebtn.setForeground(new java.awt.Color(204, 0, 0));
        deletebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete.png"))); // NOI18N
        deletebtn.setText("Delete");
        deletebtn.setBorder(null);
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        svbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        svbtn.setForeground(new java.awt.Color(51, 51, 51));
        svbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/save.png"))); // NOI18N
        svbtn.setText("Save");
        svbtn.setBorder(null);
        svbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svbtnActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 153, 102));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/clear.png"))); // NOI18N
        jButton1.setText("CLEAR");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 521, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tptextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tptextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_tptextKeyTyped

    private void mrptextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrptextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_mrptextKeyTyped

    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed
        if (updatekey == 0) {

            codeCheck();

        } else {

            try {
                vatCheckupdate();
            } catch (SQLException ex) {
                Logger.getLogger(NewItem.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        itemnamesearch.removeAllItems();
        itemnamesearch.addItem("Select");
        itemnamesearch.setSelectedIndex(0);
        checkcodemanul.setSelected(false);
        codetext.setEnabled(false);
        try {
            Item();
        } catch (SQLException ex) {
            Logger.getLogger(NewItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        msgtext.setText(null);
        itemnamesearch.setSelectedItem("Select");

    }//GEN-LAST:event_svbtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        clear();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void uniboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_uniboxPopupMenuWillBecomeInvisible
        try {
            String unit = (String) unibox.getSelectedItem();
            String sql = "Select * from unit where unitshort='" + unit + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                unitid = rs.getString("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_uniboxPopupMenuWillBecomeInvisible

    private void uniboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uniboxActionPerformed

    }//GEN-LAST:event_uniboxActionPerformed

    private void uniboxPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_uniboxPropertyChange

    }//GEN-LAST:event_uniboxPropertyChange

    private void categoryboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryboxActionPerformed


    }//GEN-LAST:event_categoryboxActionPerformed

    private void brandboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brandboxActionPerformed

    }//GEN-LAST:event_brandboxActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        stockCheck();

    }//GEN-LAST:event_deletebtnActionPerformed

    private void checkcodemanulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkcodemanulActionPerformed
        if (checkcodemanul.isSelected() == true) {
            codetext.setEnabled(true);
            codetext.setText(null);
            nametext.setEnabled(false);
            categorybox.setEnabled(false);

            brandbox.setEnabled(false);
            unibox.setEnabled(false);

            tptext.setEnabled(false);
            mrptext.setEnabled(false);
            remarktext.setEnabled(false);
            statusbox.setEnabled(false);
            updatekey = 0;
            vatcheck.setEnabled(false);

        } else {
            clear();
            codetext.setEnabled(false);
            nametext.setEnabled(true);
            categorybox.setEnabled(true);

            brandbox.setEnabled(true);
            unibox.setEnabled(true);

            tptext.setEnabled(true);
            mrptext.setEnabled(true);
            remarktext.setEnabled(true);
            statusbox.setEnabled(true);
            vatcheck.setEnabled(true);
        }

    }//GEN-LAST:event_checkcodemanulActionPerformed

    private void brandboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_brandboxPopupMenuWillBecomeInvisible
        try {
            String sup = (String) brandbox.getSelectedItem();
            String sql = "Select * from suplyierinfo where supliyername='" + sup + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                brandid = rs.getString("id");

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_brandboxPopupMenuWillBecomeInvisible


    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible

        if (itemnamesearch.getSelectedIndex() > 0) {
            try {

                String searchcode = (String) itemnamesearch.getSelectedItem();

                String sql = "Select itemcode,"
                        + "itemName,"
                        + "openingDate,"
                        + "tp,"
                        + "mrp,"
                        + "remark,"
                        + "category,"
                        + "(select name from category ct where ct.id=it.category) as 'categoryname',"
                        + "brand_id,"
                        + "(Select name from brand sinfo where sinfo.id=it.brand_id) as 'brandname',"
                        + "stockunit,"
                        + "(select unitshort  from unit un where un.id=it.stockunit) as 'unitname',"
                        + "includevate,"
                        + "vat from item it where it.itemName='" + searchcode + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {

                    String Id = rs.getString("itemcode");
                    codetext.setText(Id);
                    String Name = rs.getString("itemName");
                    nametext.setText(Name);
                    String categoryname = rs.getString("categoryname");
                    categorybox.setSelectedItem(categoryname);
                    String suppliername = rs.getString("brandname");
                    brandbox.setSelectedItem(suppliername);
                    String unitname = rs.getString("unitname");
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
                    brandid = rs.getString("brand_id");
                    unitid = rs.getString("stockunit");

                    String remark = rs.getString("remark");
                    remarktext.setText(remark);
                    updatekey = 1;
                    filterCategory();
                } else {
                    clear();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }

    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void searchtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchtextKeyReleased

        try {

            String sql = "Select "
                    + "itemcode,"
                    + "itemName,"
                    + "openingDate,"
                    + "tp,"
                    + "mrp,"
                    + "remark,"
                    + "category,"
                    + "(select name from category ct where ct.id=it.category) as 'categoryname',"
                    + "brand_id,"
                    + "(Select name from brand sinfo where sinfo.id=it.brand_id) as 'brandname',"
                    + "stockunit,"
                    + "(select unitshort from unit un where un.id=it.stockunit) as 'unitname',"
                    + "includevate,"
                    + "vat from item it where it.Itemcode='" + searchtext.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("itemcode");
                codetext.setText(Id);

                String Name = rs.getString("itemName");
                nametext.setText(Name);
                String categoryname = rs.getString("categoryname");
                categorybox.setSelectedItem(categoryname);
                String suppliername = rs.getString("brandname");
                brandbox.setSelectedItem(suppliername);
                String unitname = rs.getString("unitname");
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

                brandid = rs.getString("brand_id");
                unitid = rs.getString("stockunit");

                String remark = rs.getString("remark");
                remarktext.setText(remark);
                updatekey = 1;

            } else {
                clear();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_searchtextKeyReleased

    private void codetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyReleased
        try {
            String sql = "Select "
                    + "itemcode,"
                    + "itemName,"
                    + "openingDate,"
                    + "tp,"
                    + "mrp,"
                    + "remark,"
                    + "category,"
                    + "(select name from category ct where ct.id=it.category) as 'categoryname',"
                    + "brand_id,"
                    + "(Select name from brand sinfo where sinfo.id=it.brand_id) as 'brandname',"
                    + "stockunit,"
                    + "(select unitshort  from unit un where un.id=it.stockunit) as 'unitname',"
                    + "includevate,"
                    + "vat"
                    + " from item it where it.Itemcode='" + codetext.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("itemcode");
                codetext.setText(Id);

                String Name = rs.getString("itemName");
                nametext.setText(Name);
                String categoryname = rs.getString("categoryname");
                categorybox.setSelectedItem(categoryname);
                String suppliername = rs.getString("brandname");
                brandbox.setSelectedItem(suppliername);
                String unitname = rs.getString("unitname");
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

                brandid = rs.getString("brand_id");
                unitid = rs.getString("stockunit");

                String remark = rs.getString("remark");
                remarktext.setText(remark);
                // updatekey = 1;

                msgtext.setText("This Item Code Already Use, Try Another");
                msgtext.setForeground(Color.red);
            } else {

                msgtext.setText("This Item Code Empty,Ready To Use");
                msgtext.setForeground(Color.green);

                // codetext.setEnabled(false);
                nametext.setEnabled(true);
                categorybox.setEnabled(true);

                brandbox.setEnabled(true);
                unibox.setEnabled(true);

                tptext.setEnabled(true);
                mrptext.setEnabled(true);
                remarktext.setEnabled(true);
                statusbox.setEnabled(true);
                nametext.setText(null);
                categorybox.setSelectedIndex(0);

                brandbox.setSelectedIndex(0);
                unibox.setSelectedIndex(0);

                tptext.setText(null);
                mrptext.setText(null);
                vatcheck.setSelected(false);
                vattext.setEditable(false);
                vattext.setText(null);
                remarktext.setText(null);
                statusbox.setSelectedIndex(0);

                catid = null;

                brandid = null;
                unitid = null;

                //  checkcodemanul.setSelected(false);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_codetextKeyReleased

    private void vatcheckMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vatcheckMouseClicked
        if (vatcheck.isSelected()) {
            vattext.setEditable(true);

        } else {
            vattext.setText(null);
            vattext.setEditable(false);
        }
    }//GEN-LAST:event_vatcheckMouseClicked

    private void categoryboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_categoryboxPopupMenuWillBecomeInvisible
        if (categorybox.getSelectedIndex() > 0) {

            try {
                String category = (String) categorybox.getSelectedItem();
                String sql = "Select id from category where name='" + category + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                if (rs.next()) {
                    catid = rs.getString("id");

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_categoryboxPopupMenuWillBecomeInvisible

    private void mrptextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrptextKeyReleased
        mrptext.setBackground(Color.WHITE);
    }//GEN-LAST:event_mrptextKeyReleased

    private void tptextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tptextKeyReleased
        tptext.setBackground(Color.WHITE);
    }//GEN-LAST:event_tptextKeyReleased

    private void nametextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nametextKeyReleased
        nametext.setBackground(Color.WHITE);
    }//GEN-LAST:event_nametextKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> brandbox;
    private javax.swing.JComboBox<String> categorybox;
    private javax.swing.JCheckBox checkcodemanul;
    private javax.swing.JTextField codetext;
    private javax.swing.JButton deletebtn;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField mrptext;
    private javax.swing.JLabel msgtext;
    private javax.swing.JTextField nametext;
    private com.toedter.calendar.JDateChooser openingdate;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JTextField searchtext;
    private javax.swing.JComboBox<String> statusbox;
    private javax.swing.JButton svbtn;
    private javax.swing.JTextField tptext;
    private javax.swing.JComboBox<String> unibox;
    private javax.swing.JCheckBox vatcheck;
    private javax.swing.JTextField vattext;
    // End of variables declaration//GEN-END:variables

}
