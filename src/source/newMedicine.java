/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author FAHIM
 */
public class newMedicine extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    String catid = null;
    String brandid = null;
    String generic_id = null;
    String generic_idsearch = null;
    String company_id = null;
    String dosid = null;
    String punitid = null;
    String sunitid = null;

    // Dashboard dashboard = new Dashboard();
    int userid = 0;
    int updatekey = 0;
    java.sql.Date prodate;
    String Itemcodedate;
    private static final DecimalFormat df2 = new DecimalFormat("#.00");

    /**
     * Creates new form newMedicine
     *
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public newMedicine() throws IOException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        AutoCompleteDecorator.decorate(categorybox);
        AutoCompleteDecorator.decorate(punibox);

        accsessModification();
        category();
        unit();;
        currentDate();
        parchase_code();
        Item();
        ExtractFilterGeneric();
        ExtractFilterGenericSearch();
        ExtractFilterDos();
        ExtractFilterComapny();
        ExtractFilterBrandSearch();

    }

    public newMedicine(String table_click) throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        AutoCompleteDecorator.decorate(categorybox);
        AutoCompleteDecorator.decorate(punibox);
        accsessModification();
        category();
        unit();
        currentDate();
        parchase_code();
        itemview(table_click);
        ExtractFilterGeneric();
        ExtractFilterGenericSearch();
        ExtractFilterDos();
        ExtractFilterComapny();
        ExtractFilterBrandSearch();

    }

    private void ExtractFilterBrandSearch() {
        final JTextField textfield = (JTextField) itemnamesearch.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        itemnamesearch.removeAllItems();
                    } else {
                        comboFilterBrandSearch(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFilterBrandSearch(String enteredText) {
        itemnamesearch.removeAllItems();
        itemnamesearch.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;
        try {
            String sql = "Select  nameformate from item WHERE lower(nameformate)  LIKE '" + enteredText + "%' ORDER BY itemName ASC";
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
            itemnamesearch.setSelectedItem(enteredText);
            itemnamesearch.showPopup();
        } else {
            itemnamesearch.hidePopup();
        }
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

    private void ExtractFilterDos() {
        final JTextField textfield = (JTextField) dosbox.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        dosbox.removeAllItems();

                    } else {
                        comboFilterDos(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFilterDos(String enteredText) {
        dosbox.removeAllItems();
        //  itemnamesearch.addItem("");
        dosbox.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;

        try {

            String sql = "Select DISTINCT  ShortName from medicinforms WHERE lower(ShortName)  LIKE '%" + enteredText + "%' ORDER BY ShortName ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("ShortName");
                filterArray.add(str1);
                dosbox.addItem(str1);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        if (filterArray.size() > 0) {
            //  itemnamesearch.setModel(new DefaultComboBoxModel(filterArray.toArray()));

            dosbox.setSelectedItem(enteredText);
            dosbox.showPopup();

        } else {

            dosbox.hidePopup();

        }
    }

    private void ExtractFilterGenericSearch() {
        final JTextField textfield = (JTextField) genericsearchbox.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        genericsearchbox.removeAllItems();

                    } else {
                        comboFiltergenericSearch(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFiltergenericSearch(String enteredText) {
        genericsearchbox.removeAllItems();
        //  itemnamesearch.addItem("");
        genericsearchbox.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;

        try {

            String sql = "Select DISTINCT  generic_name from generic WHERE lower(generic_name)  LIKE '%" + enteredText + "%' ORDER BY generic_name ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("generic_name");
                filterArray.add(str1);
                genericsearchbox.addItem(str1);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        if (filterArray.size() > 0) {
            //  itemnamesearch.setModel(new DefaultComboBoxModel(filterArray.toArray()));

            genericsearchbox.setSelectedItem(enteredText);
            genericsearchbox.showPopup();

        } else {

            genericsearchbox.hidePopup();

        }
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
        itemnamesearch.removeAllItems();
        itemnamesearch.addItem("Select");
        itemnamesearch.setSelectedIndex(0);
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

    private void Supplier() throws SQLException {

        try {
            String sql = "Select supliyername from suplyierinfo ORDER BY supliyername ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("supliyername");
                supplierbox.addItem(name);
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
                    + "from item it where it.Itemcode='" + searchtext + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("itemcode");
                codetext.setText(Id);
                String Name = rs.getString("itemName");
                nametext.setText(Name);
                itemnamesearch.setSelectedItem(Name);
                String categoryname = rs.getString("category");
                categorybox.setSelectedItem(categoryname);
                String genericname = rs.getString("genericname");
                genericbox.setSelectedItem(genericname);
                String companyname = rs.getString("companyname");
                supplierbox.setSelectedItem(companyname);
                String fromname = rs.getString("fromname");
                dosbox.setSelectedItem(fromname);
                String strangth = rs.getString("strangth");
                strengthtext.setText(strangth);
                String boxSize = rs.getString("boxSize");
                boxsizebox.setText(boxSize);
                String expdate = rs.getString("expdate");
                expdatetext.setText(expdate);
                String unitname = rs.getString("unitname");
                punibox.setSelectedItem(unitname);
                String batch = rs.getString("batch");
                batchtext.setText(batch);
                Date date = rs.getDate("openingDate");
                openingdate.setDate(date);
                String tpprice = rs.getString("tp");
                tptext.setText(tpprice);
                String pvat = rs.getString("pvat");
                pvattext.setText(pvat);
                String pdiscount = rs.getString("pdiscount");
                pdiscountext.setText(pdiscount);
                String ptpwd = rs.getString("ptpwd");
                tpwdtext.setText(ptpwd);
                String ptpwv = rs.getString("ptpwv");
                ptotaltptext.setText(ptpwv);
                String marpprice = rs.getString("mrp");
                mrptext.setText(marpprice);

                String sdiscount = rs.getString("sdiscount");
                sdiscounttext.setText(sdiscount);
                String smrpwv = rs.getString("smrpwv");
                stotalmrp.setText(smrpwv);
                String pmrpwd = rs.getString("pmrpwd");
                mrpwdtext.setText(pmrpwd);
                String profite = rs.getString("profite");
                profitetext.setText(profite);
                String profiteParcentage = rs.getString("profiteParcentage");
                profiteparcentagetext.setText(profiteParcentage);
                generic_id = rs.getString("generic_id");

                company_id = rs.getString("company_id");
                punitid = rs.getString("stockunit");
                dosid = rs.getString("dos_id");
                punitid = rs.getString("stockunit");
                updatekey = 1;

            } else {
                clear();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void clear() {

        parchase_code();
        currentDate();
        try {
            Item();
        } catch (SQLException ex) {
            Logger.getLogger(newMedicine.class.getName()).log(Level.SEVERE, null, ex);
        }
        nametext.setText(null);
        genericbox.removeAllItems();
        categorybox.setSelectedIndex(0);
        strengthtext.setText(null);
        supplierbox.removeAllItems();
        dosbox.removeAllItems();
        boxsizebox.setText(null);
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
        searchtext.setText(null);
        genericsearchbox.removeAllItems();
        //  nametext.requestFocusInWindow();

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

    private void Forms() throws SQLException {

        try {
            String sql = "Select ShortName from medicinforms";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {

                String ShortName = rs.getString("ShortName");
                dosbox.addItem(ShortName);

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
                punibox.addItem(name);

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

                }
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }

    private void addStock() throws SQLException {

        if (nametext.getText().isEmpty() || generic_id == null || dosid == null || company_id == null || tptext.getText().isEmpty() || mrptext.getText().isEmpty() || punibox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Select Requirment Field.You Must Fillup Yellow Color");
            if (nametext.getText().isEmpty()) {
                nametext.setBackground(Color.YELLOW);
            }

            if (tptext.getText().isEmpty()) {
                tptext.setBackground(Color.YELLOW);

            }
            if (mrptext.getText().isEmpty()) {
                mrptext.setBackground(Color.YELLOW);

            }
        } else {
            String Itemnameformate = "";
            String Strength = "";
            String form = "";
            String name = nametext.getText();

            if (strengthtext.getText().isEmpty()) {
                Strength = "";
            } else {
                Strength = strengthtext.getText();
            }
            form = (String) dosbox.getSelectedItem();
            Itemnameformate = (name + " " + Strength + " " + form);
            try {

                String sql = "Insert into item(itemcode,"
                        + "barcode,"
                        + "itemName,"
                        + "category,"
                        + "generic_id,"
                        + "company_id,"
                        + "dos_id,"
                        + "strangth,"
                        + "stockunit,"
                        + "batch,"
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
                        + "inputuser,"
                        + "updateuser,"
                        + "lastupdate,"
                        + "nameformate) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                //pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pst = conn.prepareStatement(sql);
                pst.setString(1, codetext.getText());
                pst.setString(2, codetext.getText());
                pst.setString(3, nametext.getText());
                pst.setString(4, (String) categorybox.getSelectedItem());
                pst.setString(5, generic_id);
                pst.setString(6, company_id);
                pst.setString(7, dosid);
                pst.setString(8, strengthtext.getText());
                pst.setString(9, punitid);
                pst.setString(10, batchtext.getText());
                pst.setString(11, boxsizebox.getText());
                pst.setString(12, expdatetext.getText());
                pst.setString(13, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
                pst.setString(14, tptext.getText());
                pst.setString(15, pvattext.getText());
                pst.setString(16, pdiscountext.getText());
                pst.setString(17, ptotaltptext.getText());
                pst.setString(18, tpwdtext.getText());
                pst.setString(19, mrptext.getText());
                pst.setString(20, sdiscounttext.getText());
                pst.setString(21, stotalmrp.getText());
                pst.setString(22, mrpwdtext.getText());
                pst.setString(23, profitetext.getText());
                pst.setString(24, profiteparcentagetext.getText());
                pst.setInt(25, userid);
                pst.setInt(26, userid);
                pst.setString(27, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
                pst.setString(28, Itemnameformate);
                pst.execute();
                Stockentry();
                JOptionPane.showMessageDialog(null, "Successfuly Data Saved");
                clear();

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }

    private void stockUpdate() {
        try {
            String date = ((JTextField) openingdate.getDateEditor().getUiComponent()).getText();
            String id = codetext.getText();
            String Itemnameformate = "";
            String Strength = "";
            String form = "";
            String name = nametext.getText();

            if (strengthtext.getText().isEmpty()) {
                Strength = "";
            } else {
                Strength = strengthtext.getText();
            }
            form = (String) dosbox.getSelectedItem();
            Itemnameformate = (name + " " + Strength + " " + form);
            String sql = "Update item set "
                    + "itemName='" + nametext.getText() + "',"
                    + "category='" + categorybox.getSelectedItem() + "',"
                    + "generic_id='" + generic_id + "',"
                    + "company_id='" + company_id + "',"
                    + "dos_id='" + dosid + "',"
                    + "strangth='" + strengthtext.getText() + "',"
                    + "boxSize='" + boxsizebox.getText() + "',"
                    + "expdate='" + expdatetext.getText() + "',"
                    + "tp='" + tptext.getText() + "',"
                    + "pvat='" + pvattext.getText() + "',"
                    + "pdiscount='" + pdiscountext.getText() + "',"
                    + "ptpwv='" + ptotaltptext.getText() + "',"
                    + "ptpwd='" + tpwdtext.getText() + "',"
                    + "mrp='" + mrptext.getText() + "',"
                    + "sdiscount='" + sdiscounttext.getText() + "',"
                    + "smrpwv='" + stotalmrp.getText() + "',"
                    + "pmrpwd='" + mrpwdtext.getText() + "',"
                    + "stockunit='" + punitid + "',"
                    + "batch='" + batchtext.getText() + "',"
                    + "openingDate='" + date + "',"
                    + "profite='" + profitetext.getText() + "',"
                    + "profiteParcentage='" + profiteparcentagetext.getText() + "',"
                    + "updateuser='" + userid + "',"
                    + "lastupdate='" + date + "',"
                    + "nameformate='" + Itemnameformate + "'"
                    + " where Itemcode='" + id + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();
            updatekey = 0;
            JOptionPane.showMessageDialog(null, "Data Update Successfuly");
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

    private void ItemTP() {
        float tp = 0;
        float vat = 0;
        float discount;
        float tpwv = 0;
        float tpwd = 0;

        if (tptext.getText().isEmpty()) {
            tp = 0;
        } else {
            tp = Float.parseFloat(tptext.getText());
        }

        if (pvattext.getText().isEmpty()) {
            vat = 0;
        } else {
            vat = Float.parseFloat(pvattext.getText());
        }

        if (pdiscountext.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Float.parseFloat(pdiscountext.getText());
        }
        tpwd = tp - discount;
        tpwdtext.setText(df2.format(tpwd));
        tpwv = tpwd + vat;
        ptotaltptext.setText(df2.format(tpwv));
        Profite();

    }

    private void ItemMRP() {
        float mrp = 0;
        float vat = 0;
        float discount;
        float mrpwv = 0;
        float mrpwd = 0;

        if (mrptext.getText().isEmpty()) {
            mrp = 0;
        } else {
            mrp = Float.parseFloat(mrptext.getText());
        }

        if (pvattext.getText().isEmpty()) {
            vat = 0;
        } else {
            vat = Float.parseFloat(pvattext.getText());
        }

        if (sdiscounttext.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Float.parseFloat(sdiscounttext.getText());
        }
        mrpwd = mrp - discount;
        mrpwdtext.setText(df2.format(mrpwd));
        mrpwv = mrpwd + vat;
        stotalmrp.setText(df2.format(mrpwv));
        Profite();

    }

    private void Profite() {
        float tp = 0;
        float mrp = 0;
        float profite = 0;
        float profiteparcentage = 0;
        if (mrpwdtext.getText().isEmpty()) {
            mrp = 0;
        } else {
            mrp = Float.parseFloat(mrpwdtext.getText());
        }

        if (tpwdtext.getText().isEmpty()) {
            tp = 0;
        } else {
            tp = Float.parseFloat(tpwdtext.getText());
        }

        profite = mrp - tp;
        profiteparcentage = ((profite * tp) / 100);
        if (profite > 0 || tp > 0) {
            profitetext.setText(df2.format(profite));
            profiteparcentagetext.setText(df2.format(profiteparcentage) + "%");
        } else {
            profitetext.setText("0");
            profiteparcentagetext.setText("0%");
        }

    }

    static float GetParcantage(float CP, float PP) {

        // Decimal Equivalent of Profit Percentage 
        float P_decimal = (PP / 100);

        // Find the Selling Price 
        float res = P_decimal * CP;

        // return the calculated Selling Price 
        return res;
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
        jLabel28 = new javax.swing.JLabel();
        genericsearchbox = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        tptext = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        pdiscountext = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        ptotaltptext = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        tpwdtext = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        mrptext = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        sdiscounttext = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        stotalmrp = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        mrpwdtext = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        expdatetext = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        genericbox = new javax.swing.JComboBox<>();
        boxsizebox = new javax.swing.JTextField();
        supplierbox = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        strengthtext = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        pvattext = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        dosbox = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        categorybox = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        nametext = new javax.swing.JTextField();
        batchtext = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        punibox = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        openingdate = new com.toedter.calendar.JDateChooser();
        jLabel27 = new javax.swing.JLabel();
        profitetext = new javax.swing.JTextField();
        profiteparcentagetext = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        svbtn = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setTitle("New Item");

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Brand Name");

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

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Generic");

        genericsearchbox.setEditable(true);
        genericsearchbox.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        genericsearchbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                genericsearchboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
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
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(genericsearchbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(2, 2, 2)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(itemnamesearch, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                            .addComponent(searchtext))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(jLabel11))
                        .addGap(2, 2, 2)
                        .addComponent(genericsearchbox, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)))
                .addGap(3, 3, 3))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("CODE");

        codetext.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        codetext.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        codetext.setEnabled(false);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setBackground(new java.awt.Color(153, 0, 51));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Purchase Price");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("TP");

        tptext.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        tptext.setForeground(new java.awt.Color(51, 51, 51));
        tptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tptext.setText("0");
        tptext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        tptext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tptextMouseClicked(evt);
            }
        });
        tptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tptextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tptextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tptextKeyTyped(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("DISCOUNT");

        pdiscountext.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        pdiscountext.setForeground(new java.awt.Color(51, 51, 51));
        pdiscountext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pdiscountext.setText("0");
        pdiscountext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pdiscountext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pdiscountextMouseClicked(evt);
            }
        });
        pdiscountext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pdiscountextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pdiscountextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pdiscountextKeyTyped(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("TOTAL TP(tp+vat)");

        ptotaltptext.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        ptotaltptext.setForeground(new java.awt.Color(51, 51, 51));
        ptotaltptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ptotaltptext.setText("0");
        ptotaltptext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setText("TPWD(TP-D)");

        tpwdtext.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        tpwdtext.setForeground(new java.awt.Color(51, 51, 51));
        tpwdtext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tpwdtext.setText("0");
        tpwdtext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 62, 62)
                        .addComponent(tptext)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ptotaltptext)
                            .addComponent(pdiscountext)
                            .addComponent(tpwdtext))
                        .addGap(6, 6, 6))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pdiscountext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tpwdtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ptotaltptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel7.setBackground(new java.awt.Color(0, 102, 102));

        jLabel20.setBackground(new java.awt.Color(153, 0, 51));
        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("Sale Price");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
        );

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("MRP");

        mrptext.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        mrptext.setForeground(new java.awt.Color(51, 51, 51));
        mrptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mrptext.setText("0");
        mrptext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        mrptext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mrptextMouseClicked(evt);
            }
        });
        mrptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mrptextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                mrptextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mrptextKeyTyped(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setText("DISCOUNT");

        sdiscounttext.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        sdiscounttext.setForeground(new java.awt.Color(51, 51, 51));
        sdiscounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        sdiscounttext.setText("0");
        sdiscounttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        sdiscounttext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sdiscounttextMouseClicked(evt);
            }
        });
        sdiscounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                sdiscounttextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                sdiscounttextKeyTyped(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("TOTAL MRP(mrp+vat)");

        stotalmrp.setEditable(false);
        stotalmrp.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        stotalmrp.setForeground(new java.awt.Color(51, 51, 51));
        stotalmrp.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        stotalmrp.setText("0");
        stotalmrp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        stotalmrp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                stotalmrpKeyTyped(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel30.setText("MRPWD(MRP-D)");

        mrpwdtext.setEditable(false);
        mrpwdtext.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        mrpwdtext.setForeground(new java.awt.Color(51, 51, 51));
        mrpwdtext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mrpwdtext.setText("0");
        mrpwdtext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        mrpwdtext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mrpwdtextActionPerformed(evt);
            }
        });
        mrpwdtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                mrpwdtextKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85)
                        .addComponent(mrptext)
                        .addContainerGap())
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(stotalmrp)
                            .addComponent(sdiscounttext)
                            .addComponent(mrpwdtext))
                        .addGap(6, 6, 6))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sdiscounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mrpwdtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stotalmrp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        expdatetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        expdatetext.setForeground(new java.awt.Color(153, 0, 0));
        expdatetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        expdatetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        expdatetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                expdatetextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                expdatetextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                expdatetextKeyTyped(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("COMPANY");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("GENERIC");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("STRENGTH");

        genericbox.setEditable(true);
        genericbox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        genericbox.setAutoscrolls(true);
        genericbox.setBorder(null);
        genericbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                genericboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        genericbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genericboxActionPerformed(evt);
            }
        });
        genericbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                genericboxKeyPressed(evt);
            }
        });

        boxsizebox.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        boxsizebox.setForeground(new java.awt.Color(153, 0, 0));
        boxsizebox.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        boxsizebox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        boxsizebox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                boxsizeboxKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                boxsizeboxKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                boxsizeboxKeyTyped(evt);
            }
        });

        supplierbox.setEditable(true);
        supplierbox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        supplierbox.setBorder(null);
        supplierbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
                supplierboxPopupMenuCanceled(evt);
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
        supplierbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                supplierboxKeyPressed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("EXP DATE");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("BOX SIZE");

        strengthtext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        strengthtext.setForeground(new java.awt.Color(153, 0, 0));
        strengthtext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        strengthtext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        strengthtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                strengthtextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                strengthtextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                strengthtextKeyTyped(evt);
            }
        });

        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("VAT");

        pvattext.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        pvattext.setForeground(new java.awt.Color(51, 51, 51));
        pvattext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pvattext.setText("0");
        pvattext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        pvattext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pvattextMouseClicked(evt);
            }
        });
        pvattext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pvattextActionPerformed(evt);
            }
        });
        pvattext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pvattextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pvattextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pvattextKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(supplierbox, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pvattext)
                            .addComponent(expdatetext)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(boxsizebox))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(strengthtext, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(genericbox, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(genericbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(strengthtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supplierbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(boxsizebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(expdatetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("BRAND NAME");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("CATEGORY");

        dosbox.setEditable(true);
        dosbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dosbox.setBorder(null);
        dosbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                dosboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        dosbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dosboxActionPerformed(evt);
            }
        });
        dosbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dosboxKeyPressed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("BATCH");

        categorybox.setEditable(true);
        categorybox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        categorybox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Medicine" }));
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

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("DOS");

        nametext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nametext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        nametext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nametextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nametextKeyReleased(evt);
            }
        });

        batchtext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        batchtext.setForeground(new java.awt.Color(153, 0, 0));
        batchtext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        batchtext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        batchtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                batchtextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                batchtextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                batchtextKeyTyped(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("UNIT");

        punibox.setEditable(true);
        punibox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(categorybox, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nametext, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(dosbox, 0, 350, Short.MAX_VALUE)
                                    .addComponent(batchtext))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(punibox, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nametext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(categorybox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dosbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(batchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(punibox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setText("OPENING DATE");

        openingdate.setForeground(new java.awt.Color(102, 102, 102));
        openingdate.setDateFormatString("yyyy-MM-dd");
        openingdate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setText("Profite");

        profitetext.setEditable(false);
        profitetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        profitetext.setForeground(new java.awt.Color(153, 0, 0));
        profitetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        profitetext.setText("0");
        profitetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        profitetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                profitetextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                profitetextKeyTyped(evt);
            }
        });

        profiteparcentagetext.setEditable(false);
        profiteparcentagetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        profiteparcentagetext.setForeground(new java.awt.Color(153, 0, 0));
        profiteparcentagetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        profiteparcentagetext.setText("0%");
        profiteparcentagetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        profiteparcentagetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                profiteparcentagetextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                profiteparcentagetextKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(profitetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(profiteparcentagetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49)
                        .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(openingdate, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(openingdate, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profitetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profiteparcentagetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
 try {

                String searchcode = (String) itemnamesearch.getSelectedItem();

                String sql = "Select "
                        + "itemcode,"
                        + "itemName,"
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
                    codetext.setText(Id);
                    searchtext.setText(Id);
                    String Name = rs.getString("itemName");
                    nametext.setText(Name);
                    String categoryname = rs.getString("category");
                    categorybox.setSelectedItem(categoryname);
                    String genericname = rs.getString("genericname");
                    genericbox.setSelectedItem(genericname);
                    String companyname = rs.getString("companyname");
                    supplierbox.setSelectedItem(companyname);
                    String fromname = rs.getString("fromname");
                    dosbox.setSelectedItem(fromname);
                    String strangth = rs.getString("strangth");
                    strengthtext.setText(strangth);
                    String boxSize = rs.getString("boxSize");
                    boxsizebox.setText(boxSize);
                    String expdate = rs.getString("expdate");
                    expdatetext.setText(expdate);
                    String unitname = rs.getString("unitname");
                    punibox.setSelectedItem(unitname);
                    String batch = rs.getString("batch");
                    batchtext.setText(batch);
                    Date date = rs.getDate("openingDate");
                    openingdate.setDate(date);
                    String tpprice = rs.getString("tp");
                    tptext.setText(tpprice);
                    String pvat = rs.getString("pvat");
                    pvattext.setText(pvat);
                    String pdiscount = rs.getString("pdiscount");
                    pdiscountext.setText(pdiscount);
                    String ptpwd = rs.getString("ptpwd");
                    tpwdtext.setText(ptpwd);
                    String ptpwv = rs.getString("ptpwv");
                    ptotaltptext.setText(ptpwv);
                    String marpprice = rs.getString("mrp");
                    mrptext.setText(marpprice);
                    String sdiscount = rs.getString("sdiscount");
                    sdiscounttext.setText(sdiscount);
                    String smrpwv = rs.getString("smrpwv");
                    stotalmrp.setText(smrpwv);
                    String pmrpwd = rs.getString("pmrpwd");
                    mrpwdtext.setText(pmrpwd);
                    String profite = rs.getString("profite");
                    profitetext.setText(profite);
                    String profiteParcentage = rs.getString("profiteParcentage");
                    profiteparcentagetext.setText(profiteParcentage);
                    generic_id = rs.getString("generic_id");
                    company_id = rs.getString("company_id");
                    punitid = rs.getString("stockunit");
                    dosid = rs.getString("dos_id");
                    punitid = rs.getString("stockunit");
                    updatekey = 1;
                } else {
                    clear();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
       
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

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

                String Id = rs.getString("itemcode");
                codetext.setText(Id);
                String Name = rs.getString("itemName");
                nametext.setText(Name);
                itemnamesearch.setSelectedItem(Name);
                String categoryname = rs.getString("category");
                categorybox.setSelectedItem(categoryname);
                String genericname = rs.getString("genericname");
                genericbox.setSelectedItem(genericname);
                String companyname = rs.getString("companyname");
                supplierbox.setSelectedItem(companyname);
                String fromname = rs.getString("fromname");
                dosbox.setSelectedItem(fromname);
                String strangth = rs.getString("strangth");
                strengthtext.setText(strangth);
                String boxSize = rs.getString("boxSize");
                boxsizebox.setText(boxSize);
                String expdate = rs.getString("expdate");
                expdatetext.setText(expdate);
                String unitname = rs.getString("unitname");
                punibox.setSelectedItem(unitname);
                String batch = rs.getString("batch");
                batchtext.setText(batch);
                Date date = rs.getDate("openingDate");
                openingdate.setDate(date);
                String tpprice = rs.getString("tp");
                tptext.setText(tpprice);
                String pvat = rs.getString("pvat");
                pvattext.setText(pvat);
                String pdiscount = rs.getString("pdiscount");
                pdiscountext.setText(pdiscount);
                String ptpwd = rs.getString("ptpwd");
                tpwdtext.setText(ptpwd);
                String ptpwv = rs.getString("ptpwv");
                ptotaltptext.setText(ptpwv);
                String marpprice = rs.getString("mrp");
                mrptext.setText(marpprice);
                String sdiscount = rs.getString("sdiscount");
                sdiscounttext.setText(sdiscount);
                String smrpwv = rs.getString("smrpwv");
                stotalmrp.setText(smrpwv);
                String pmrpwd = rs.getString("pmrpwd");
                mrpwdtext.setText(pmrpwd);
                String profite = rs.getString("profite");
                profitetext.setText(profite);
                String profiteParcentage = rs.getString("profiteParcentage");
                profiteparcentagetext.setText(profiteParcentage);
                generic_id = rs.getString("generic_id");

                company_id = rs.getString("company_id");
                punitid = rs.getString("stockunit");
                dosid = rs.getString("dos_id");
                punitid = rs.getString("stockunit");
                updatekey = 1;

            } else {

                nametext.setText(null);
                genericbox.removeAllItems();
                categorybox.setSelectedIndex(0);
                strengthtext.setText(null);
                supplierbox.removeAllItems();
                dosbox.removeAllItems();
                boxsizebox.setText(null);
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
                genericsearchbox.removeAllItems();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_searchtextKeyReleased

    private void nametextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nametextKeyReleased
        nametext.setBackground(Color.WHITE);
    }//GEN-LAST:event_nametextKeyReleased

    private void genericboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_genericboxPopupMenuWillBecomeInvisible

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


    }//GEN-LAST:event_genericboxPopupMenuWillBecomeInvisible

    private void genericboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genericboxActionPerformed

    }//GEN-LAST:event_genericboxActionPerformed

    private void categoryboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_categoryboxPopupMenuWillBecomeInvisible
        if (genericbox.getSelectedIndex() > 0) {

            try {
                String category = (String) genericbox.getSelectedItem();
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

    private void categoryboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryboxActionPerformed

    }//GEN-LAST:event_categoryboxActionPerformed

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

    private void supplierboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierboxActionPerformed

    private void puniboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_puniboxPopupMenuWillBecomeInvisible
        if (punibox.getSelectedIndex() > 0) {
            try {
                String unit = (String) punibox.getSelectedItem();
                String sql = "Select id from unit where unitshort='" + unit + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                if (rs.next()) {
                    punitid = rs.getString("id");

                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }
    }//GEN-LAST:event_puniboxPopupMenuWillBecomeInvisible

    private void puniboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_puniboxActionPerformed

    }//GEN-LAST:event_puniboxActionPerformed

    private void puniboxPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_puniboxPropertyChange

    }//GEN-LAST:event_puniboxPropertyChange

    private void tptextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tptextKeyReleased
        tptext.setBackground(Color.WHITE);
        // mrptext.setText(tptext.getText());
        ItemTP();
        //  ItemMRP();
    }//GEN-LAST:event_tptextKeyReleased

    private void tptextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tptextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_tptextKeyTyped

    private void pvattextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pvattextKeyReleased
        ItemTP();
        ItemMRP();
    }//GEN-LAST:event_pvattextKeyReleased

    private void pvattextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pvattextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_pvattextKeyTyped

    private void pdiscountextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pdiscountextKeyReleased

        //  sdiscounttext.setText(pdiscountext.getText());
        ItemTP();
        // ItemMRP();
    }//GEN-LAST:event_pdiscountextKeyReleased

    private void pdiscountextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pdiscountextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_pdiscountextKeyTyped

    private void dosboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_dosboxPopupMenuWillBecomeInvisible
        try {
            String dos = (String) dosbox.getSelectedItem();
            String sql = "Select FormId from medicinforms where ShortName='" + dos + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                dosid = rs.getString("FormId");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_dosboxPopupMenuWillBecomeInvisible

    private void dosboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dosboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dosboxActionPerformed

    private void batchtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_batchtextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_batchtextKeyReleased

    private void batchtextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_batchtextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_batchtextKeyTyped

    private void expdatetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_expdatetextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_expdatetextKeyReleased

    private void expdatetextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_expdatetextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_expdatetextKeyTyped

    private void boxsizeboxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_boxsizeboxKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_boxsizeboxKeyReleased

    private void boxsizeboxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_boxsizeboxKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_boxsizeboxKeyTyped

    private void mrptextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrptextKeyReleased
        ItemMRP();
    }//GEN-LAST:event_mrptextKeyReleased

    private void mrptextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrptextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_mrptextKeyTyped

    private void sdiscounttextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sdiscounttextKeyReleased
        ItemMRP();
    }//GEN-LAST:event_sdiscounttextKeyReleased

    private void sdiscounttextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sdiscounttextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_sdiscounttextKeyTyped

    private void stotalmrpKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stotalmrpKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_stotalmrpKeyTyped

    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed
        if (updatekey == 0) {
            try {
                addStock();
            } catch (SQLException ex) {
                Logger.getLogger(newMedicine.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            stockUpdate();
        }
    }//GEN-LAST:event_svbtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        clear();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        stockCheck();
    }//GEN-LAST:event_deletebtnActionPerformed

    private void profitetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_profitetextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_profitetextKeyReleased

    private void profitetextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_profitetextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_profitetextKeyTyped

    private void profiteparcentagetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_profiteparcentagetextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_profiteparcentagetextKeyReleased

    private void profiteparcentagetextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_profiteparcentagetextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_profiteparcentagetextKeyTyped

    private void genericsearchboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_genericsearchboxPopupMenuWillBecomeInvisible
        try {
            String generic = (String) genericsearchbox.getSelectedItem();
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
    }//GEN-LAST:event_genericsearchboxPopupMenuWillBecomeInvisible

    private void mrpwdtextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrpwdtextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_mrpwdtextKeyTyped

    private void pvattextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pvattextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pvattextActionPerformed

    private void mrpwdtextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mrpwdtextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mrpwdtextActionPerformed

    private void tptextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tptextMouseClicked
        tptext.setText(null);
    }//GEN-LAST:event_tptextMouseClicked

    private void pvattextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pvattextMouseClicked
        pvattext.setText(null);
    }//GEN-LAST:event_pvattextMouseClicked

    private void pdiscountextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pdiscountextMouseClicked
        pdiscountext.setText(null);
    }//GEN-LAST:event_pdiscountextMouseClicked

    private void mrptextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mrptextMouseClicked
        mrptext.setText(null);
    }//GEN-LAST:event_mrptextMouseClicked

    private void sdiscounttextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sdiscounttextMouseClicked
        sdiscounttext.setText(null);
    }//GEN-LAST:event_sdiscounttextMouseClicked

    private void strengthtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_strengthtextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_strengthtextKeyReleased

    private void strengthtextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_strengthtextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_strengthtextKeyTyped

    private void supplierboxPopupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supplierboxPopupMenuCanceled
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierboxPopupMenuCanceled

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        medicineGenric filte = null;
        if (generic_id == null) {
            try {
                filte = new medicineGenric();
            } catch (IOException | SQLException ex) {
                Logger.getLogger(newMedicine.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                filte = new medicineGenric(generic_id);
            } catch (IOException | SQLException ex) {
                Logger.getLogger(newMedicine.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
        filte.setEnabled(true);
        try {
            filte.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(newMedicine.class.getName()).log(Level.SEVERE, null, ex);
        }
        //this.dispose();
        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        filte.moveToFront();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void nametextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nametextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_RIGHT) {

        } else {
            genericbox.requestFocusInWindow();
        }
    }//GEN-LAST:event_nametextKeyPressed

    private void genericboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_genericboxKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_RIGHT) {

        } else {
            strengthtext.requestFocusInWindow();
        }
    }//GEN-LAST:event_genericboxKeyPressed

    private void strengthtextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_strengthtextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_RIGHT) {

        } else {
            // brandbox.requestFocusInWindow();
        }
    }//GEN-LAST:event_strengthtextKeyPressed

    private void supplierboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supplierboxKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_RIGHT) {

        } else {
            dosbox.requestFocusInWindow();
        }
    }//GEN-LAST:event_supplierboxKeyPressed

    private void dosboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dosboxKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_RIGHT) {

        } else {
            boxsizebox.requestFocusInWindow();
        }
    }//GEN-LAST:event_dosboxKeyPressed

    private void boxsizeboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_boxsizeboxKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_RIGHT) {

        } else {
            batchtext.requestFocusInWindow();
        }
    }//GEN-LAST:event_boxsizeboxKeyPressed

    private void batchtextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_batchtextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_RIGHT) {

        } else {
            expdatetext.requestFocusInWindow();
        }
    }//GEN-LAST:event_batchtextKeyPressed

    private void expdatetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_expdatetextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_RIGHT) {

        } else {
            punibox.requestFocusInWindow();
        }
    }//GEN-LAST:event_expdatetextKeyPressed

    private void puniboxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_puniboxKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_RIGHT) {

        } else {
            pvattext.setText(null);
            pvattext.requestFocusInWindow();
        }
    }//GEN-LAST:event_puniboxKeyPressed

    private void pvattextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pvattextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_RIGHT) {

        } else {
            tptext.setText(null);
            tptext.requestFocusInWindow();
        }
    }//GEN-LAST:event_pvattextKeyPressed

    private void tptextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tptextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_RIGHT) {

        } else {
            pdiscountext.setText(null);
            pdiscountext.requestFocusInWindow();
        }
    }//GEN-LAST:event_tptextKeyPressed

    private void pdiscountextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pdiscountextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_RIGHT) {

        } else {
            mrptext.setText(null);
            mrptext.requestFocusInWindow();
        }
    }//GEN-LAST:event_pdiscountextKeyPressed

    private void mrptextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrptextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_RIGHT) {

        } else {
            sdiscounttext.setText(null);
            sdiscounttext.requestFocusInWindow();
        }
    }//GEN-LAST:event_mrptextKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField batchtext;
    private javax.swing.JTextField boxsizebox;
    private javax.swing.JComboBox<String> categorybox;
    private javax.swing.JTextField codetext;
    private javax.swing.JButton deletebtn;
    private javax.swing.JComboBox<String> dosbox;
    private javax.swing.JTextField expdatetext;
    private javax.swing.JComboBox<String> genericbox;
    private javax.swing.JComboBox<String> genericsearchbox;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField mrptext;
    private javax.swing.JTextField mrpwdtext;
    private javax.swing.JTextField nametext;
    private com.toedter.calendar.JDateChooser openingdate;
    private javax.swing.JTextField pdiscountext;
    private javax.swing.JTextField profiteparcentagetext;
    private javax.swing.JTextField profitetext;
    private javax.swing.JTextField ptotaltptext;
    private javax.swing.JComboBox<String> punibox;
    private javax.swing.JTextField pvattext;
    private javax.swing.JTextField sdiscounttext;
    private javax.swing.JTextField searchtext;
    private javax.swing.JTextField stotalmrp;
    private javax.swing.JTextField strengthtext;
    private javax.swing.JComboBox<String> supplierbox;
    private javax.swing.JButton svbtn;
    private javax.swing.JTextField tptext;
    private javax.swing.JTextField tpwdtext;
    // End of variables declaration//GEN-END:variables
}
