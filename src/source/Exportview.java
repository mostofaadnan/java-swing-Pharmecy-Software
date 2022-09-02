/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class Exportview extends javax.swing.JInternalFrame {

    int tree = 0;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String countryid;
    int updatekey = 0;
    int supid = 0;
    int importmodification = 0;
     // Dashboard dashboard = new Dashboard();
    int userid =0;
    /**
     * Creates new form Exportview
     *
     * @param table_click
     * @param exportM
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public Exportview(String table_click, int exportM) throws SQLException, IOException {
        initComponents();
        
        conn = Java_Connect.conectrDB();
        userkey();

        Item();
        unit();
        AutoCompleteDecorator.decorate(unibox);
        deleterow();

        currentDate();
        PurchaseDetailsPR(table_click);

        parchaseText.setText(table_click);
        country();
        suppliyer();
        importmodification = exportM;
        if (importmodification == 1) {

            supliyerbox.setEnabled(false);

        } else {

            supliyerbox.setEnabled(true);

        }
        
        accsessModification();
    }
     private void userkey() throws  IOException, SQLException {
        FileInputStream fis = new FileInputStream("src\\userkey.properties");

        Properties p = new Properties();
        p.load(fis);

        String userids = (String) p.get("userid");
       userid=Integer.parseInt(userids);
        // this.dispose();
        //LoginAccess desboard=new LoginAccess();
    }
      private void accsessModification() {
        try {
            String sql = "Select imexedit,imexdelete from modificationaccsess where userid='" + userid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                int imexedit = rs.getInt("imexedit");
                int imexdelete = rs.getInt("imexdelete");

                if (imexedit == 1) {

                    svbtn.setEnabled(true);

                } else {

                    svbtn.setEnabled(false);

                }
                if (imexdelete == 1) {

                    deletebtn.setEnabled(true);

                } else {

                    deletebtn.setEnabled(false);

                }

            } else {
                svbtn.setEnabled(false);
                deletebtn.setEnabled(false);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void deleterow() {

        int[] toDelete = dataTable.getSelectedRows();
        Arrays.sort(toDelete); // be shure to have them in ascending order.
        DefaultTableModel myTableModel = (DefaultTableModel) dataTable.getModel();
        for (int ii = toDelete.length - 1; ii >= 0; ii--) {
            myTableModel.removeRow(toDelete[ii]); // beginning at the largest.
        }
        totalrate.setText(Float.toString(total_action_mrp()));
        nettotalcommitiontext.setText(df2.format(total_action_nettotal()));
        clear();
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        java.sql.Date date = new java.sql.Date(now.getTime());
        parchasedate.setDate(date);

    }

    private void PurchaseDetailsPR(String table_click) throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();

        try {

            String sql = "Select pr.prcode as 'id',(select ita.itemName from item ita where ita.Itemcode=pr.prcode) as 'Itemname',unitrate, qty,unit,total,discount,comisionInpersantage,comissionperitem,totalcommision from exportdetails pr  where pr.exportCode='" + table_click + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            while (rs.next()) {
                tree++;
                String id = rs.getString("id");
                String itemname = rs.getString("Itemname");
                float unitrate = rs.getFloat("unitrate");
                float qty = rs.getFloat("qty");
                String unit = rs.getString("unit");
                float total = rs.getFloat("total");
                float discount = rs.getFloat("discount");
                String comisionInpersantage=rs.getString("comisionInpersantage");
                float comissionperitem=rs.getFloat("comissionperitem");
                float totalcommision=rs.getFloat("totalcommision");
                model2.addRow(new Object[]{tree, id, itemname, unitrate, qty, unit, discount, total,comisionInpersantage,comissionperitem,totalcommision});

            }
            //  dataTable.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {

            String sql = "Select Totalrate,exportCode,supliyer,countryid,pdate,(select sp.supliyername from suplyierinfo sp where sp.id=im.supliyer) as 'supliyername',(select cy.name from country cy where cy.id=im.countryid) as 'countryname',netcommision  from export im where im.exportCode='" + table_click + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            if (rs.next()) {

                String TotalRate = rs.getString("Totalrate");
                totalrate.setText(TotalRate);

                String ParchseNo = rs.getString("exportCode");
                parchaseText.setText(ParchseNo);
                supid = rs.getInt("supliyer");
                String supliyername = rs.getString("supliyername");
                supliyerbox.setSelectedItem(supliyername);

                countryid = rs.getString("countryid");
                String countryname = rs.getString("countryname");
                countrybox.setSelectedItem(countryname);
                Date inutDate = rs.getDate("pdate");
                parchasedate.setDate(inutDate);
                String netcommision=rs.getString("netcommision");
                nettotalcommitiontext.setText(netcommision);
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void parchase_code() {
        try {
            int new_inv = 1;
            String NewParchaseCode = ("im-" + new_inv + "");
            String sql = "Select id from export";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = ("im-" + ParchaseCode + "");
            }

            parchaseText.setText(NewParchaseCode);
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void exportupdate() {
        try {

            String date = ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText();

            String sql = "Update export set pdate='" + date + "',supliyer='" + supid + "',Totalrate='" + totalrate.getText() + "',countryid='" + countryid + "',netcommision='"+nettotalcommitiontext.getText()+"' where exportCode='" + parchaseText.getText() + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void purchasedeletehistory() {

        try {
            String sql = "Delete from exportDetails where exportCode=?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, parchaseText.getText());

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

   private void exportDetailsInsert() throws SQLException, IOException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String procode = (String) dataTable.getValueAt(row, 1);

                float rate = (Float) dataTable.getValueAt(row, 3);

                float qty = (Float) dataTable.getValueAt(row, 4);

                String unit = (String) dataTable.getValueAt(row, 5);
                float total = (Float) dataTable.getValueAt(row, 7);
                float discount = (Float) dataTable.getValueAt(row, 6);
                 String commisionparsentage = (String) dataTable.getValueAt(row, 8);
                 float itemcommision = (Float) dataTable.getValueAt(row, 9);
                 float totalcomision = (Float) dataTable.getValueAt(row, 10);

                try {
                    String sql = "Insert into exportDetails(exportCode,prcode,unitrate,qty,unit,discount,total,comisionInpersantage,comissionperitem,totalcommision) values (?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, parchaseText.getText());
                    pst.setString(2, procode);
                    pst.setFloat(3, rate);
                    pst.setFloat(4, qty);
                    pst.setString(5, unit);
                    pst.setFloat(6, discount);
                    pst.setFloat(7, total);
                    pst.setString(8, commisionparsentage);
                    pst.setFloat(9, itemcommision);
                    pst.setFloat(10, totalcomision);
                    pst.execute();

                    // 
                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }
            loadGRN();
            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
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

    private void unit() throws SQLException {

        try {
            String sql = "Select * from unit";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("unitshort");
                int id = rs.getInt("id");
                unibox.addItem(name);
                //unibox.setSelectedIndex(id);

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

    private float total_action_mrp() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 7).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_commition(){

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 10).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_nettotal() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 10).toString());
        }

        return (float) totaltpmrp;

    }

    private void clear() {
        codetext.setText(null);
        itemnamesearch.setSelectedIndex(0);
        unitrateText.setText(null);
        qtytext.setText(null);
        unibox.setSelectedIndex(0);
        updatekey = 0;
    }

  private static final DecimalFormat df2 = new DecimalFormat("#.00");
    private void entryData() {
        float discount;
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        tree++;

        float tpd = Float.parseFloat(unitrateText.getText());
        if (discounttext.getText().isEmpty()) {

            discount = 0;

        } else {
            discount = Float.parseFloat(discounttext.getText());

        }
        String commitionpersantage=commitiontext.getText();
        float comisionconvert=Float.parseFloat(commitionpersantage);
        float comitionamount=comisionconvert/100;
        float itemcommition=comitionamount*tpd; 
        String commitionamt=df2.format(itemcommition);
        float commitionamttable=Float.parseFloat(commitionamt);
        float tpwdisc = tpd - discount;

        float qty = Float.parseFloat(qtytext.getText());
        float nettotaltp = (tpwdisc * qty);
        float totalcommition=qty*commitionamttable;
        String totalcommitions=df2.format(totalcommition);
        float totalcommitionfinel=Float.parseFloat(totalcommitions);
        //String gpper = String.format("%.2f", gp);
        model2.addRow(new Object[]{tree, codetext.getText(), itemnamesearch.getSelectedItem(), tpd, qty, unibox.getSelectedItem(), discount, nettotaltp,commitionpersantage,commitionamttable,totalcommitionfinel});
        ///  totalrate.setText(Integer.toString(total_action_rate()));
        totalrate.setText(Float.toString(total_action_mrp()));
        nettotalcommitiontext.setText(df2.format(total_action_commition()));
        clear();
    }

    private void country() throws SQLException {

        try {
            String sql = "Select name from country";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("name");

                countrybox.addItem(name);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void finelEntry() {

        if (updatekey == 0) {

            if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0 || commitiontext.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");

            } else {
                if (qtytext.getText().matches("^[a-zA-Z]+$") || commitiontext.getText().matches("^[a-zA-Z]+$")) {
               
                JOptionPane.showMessageDialog(null, "Please Give Number Value in qty and Commision Field");
                }
                else{
                    checkentry();
            }
            }

        } else if (unitrateText.getText().isEmpty() || qtytext.getText().isEmpty()) {

        } else {

            DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
            int i = dataTable.getSelectedRow();
            if (i >= 0) {
                float discount;
                float tpd = Float.parseFloat(unitrateText.getText());
                if (discounttext.getText().isEmpty()) {

                    discount = 0;

                } else {
                    discount = Float.parseFloat(discounttext.getText());

                }
         String commitionpersantage=commitiontext.getText();
        float comisionconvert=Float.parseFloat(commitionpersantage);
        float comitionamount=comisionconvert/100;
        float itemcommition=comitionamount*tpd; 
        String commitionamt=df2.format(itemcommition);
        float commitionamttable=Float.parseFloat(commitionamt);
                
                float tpwdisc = tpd - discount;

                float qty = Float.parseFloat(qtytext.getText());
                float nettotaltp = (tpwdisc * qty);
                
                 float totalcommition=qty*commitionamttable;
        String totalcommitions=df2.format(totalcommition);
        float totalcommitionfinel=Float.parseFloat(totalcommitions);
                
                model.setValueAt(qty, i, 4);
                model.setValueAt(tpd, i, 3);
                model.setValueAt(nettotaltp, i, 7);
                model.setValueAt(commitionpersantage, i, 8);
                model.setValueAt(commitionamttable, i, 9);
                 model.setValueAt(totalcommitionfinel, i, 10);
                

                totalrate.setText(Float.toString(total_action_mrp()));
               nettotalcommitiontext.setText(df2.format(total_action_nettotal()));
                clear();
                updatekey = 0;
                codetext.setText(null);
                itemnamesearch.setSelectedIndex(0);
            }

        }

    }

    private void reload() throws SQLException {

        codetext.setText(null);
        itemnamesearch.setSelectedIndex(0);
        unitrateText.setText(null);
        qtytext.setText(null);
        unibox.setSelectedIndex(0);
        totalrate.setText(null);
        discounttext.setText(null);
        parchasedate.setDate(null);
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);
        codetext.enable(true);
        itemnamesearch.enable(true);

        parchase_code();
        suppliyer();

    }

    private void loadGRN() throws SQLException, IOException {

        String table_click = parchaseText.getText();
        Export filte = null;

        filte = new Export(table_click);

        filte.setVisible(true);

        this.getDesktopPane().add(filte);
        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
        this.dispose();

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
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        parchasedate = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarktext = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        parchaseText = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        countrybox = new javax.swing.JComboBox<>();
        supliyerbox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel13 = new javax.swing.JLabel();
        lottext = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        enbledpnl = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        addbtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        unibox = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        codetext = new javax.swing.JTextField();
        qtytext = new javax.swing.JTextField();
        itemnamesearch = new javax.swing.JComboBox<>();
        enbledpanel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        unitrateText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        commitiontext = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        totalrate = new javax.swing.JLabel();
        discounttext = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        nettotalcommitiontext = new javax.swing.JTextField();
        here = new javax.swing.JPanel();
        svbtn = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Export View");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Ref No");

        parchasedate.setDateFormatString("yyyy-MM-dd");

        remarktext.setColumns(20);
        remarktext.setRows(5);
        remarktext.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jScrollPane2.setViewportView(remarktext);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Date");

        parchaseText.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        parchaseText.setForeground(new java.awt.Color(255, 255, 255));
        parchaseText.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Export No");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(3, 3, 3)
                .addComponent(parchaseText, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(8, 8, 8)
                .addComponent(parchasedate, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(parchaseText, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(parchasedate, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(67, 86, 86));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        countrybox.setEditable(true);
        countrybox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        countrybox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                countryboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        supliyerbox.setEditable(true);
        supliyerbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        supliyerbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                supliyerboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Lot");

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jCheckBox1.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("Load Items");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseClicked(evt);
            }
        });
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Suppliyer");

        lottext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lottext.setForeground(new java.awt.Color(255, 255, 255));
        lottext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Country");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addGap(18, 18, 18)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(countrybox, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(lottext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(countrybox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lottext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        enbledpnl.setBackground(new java.awt.Color(67, 86, 86));
        enbledpnl.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Unit ");

        addbtn.setBackground(new java.awt.Color(255, 0, 0));
        addbtn.setForeground(new java.awt.Color(255, 255, 255));
        addbtn.setText("ADD");
        addbtn.setBorder(null);
        addbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addbtnActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Item Name");

        unibox.setEditable(true);
        unibox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        jButton2.setBackground(new java.awt.Color(0, 153, 0));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Clear");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        codetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        codetext.setForeground(new java.awt.Color(153, 0, 0));
        codetext.setBorder(null);
        codetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codetextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codetextKeyTyped(evt);
            }
        });

        qtytext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        qtytext.setForeground(new java.awt.Color(153, 0, 0));
        qtytext.setBorder(null);
        qtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytextKeyPressed(evt);
            }
        });

        itemnamesearch.setBackground(new java.awt.Color(255, 249, 248));
        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        itemnamesearch.setBorder(null);
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        itemnamesearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemnamesearchActionPerformed(evt);
            }
        });
        itemnamesearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                itemnamesearchKeyTyped(evt);
            }
        });

        enbledpanel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        enbledpanel.setForeground(new java.awt.Color(255, 255, 255));
        enbledpanel.setText("Code");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Unit Rate");

        unitrateText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        unitrateText.setForeground(new java.awt.Color(102, 0, 0));
        unitrateText.setBorder(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Qty");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Commision(%)");

        commitiontext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        commitiontext.setForeground(new java.awt.Color(102, 0, 0));
        commitiontext.setBorder(null);
        commitiontext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                commitiontextKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout enbledpnlLayout = new javax.swing.GroupLayout(enbledpnl);
        enbledpnl.setLayout(enbledpnlLayout);
        enbledpnlLayout.setHorizontalGroup(
            enbledpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(enbledpnlLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(enbledpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(enbledpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(enbledpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(enbledpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(enbledpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(enbledpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(enbledpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(commitiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(addbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        enbledpnlLayout.setVerticalGroup(
            enbledpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(enbledpnlLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(enbledpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enbledpanel)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel11)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(enbledpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(codetext)
                    .addComponent(unitrateText)
                    .addComponent(qtytext)
                    .addGroup(enbledpnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(commitiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(addbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(67, 86, 86));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Total Rate");

        totalrate.setBackground(new java.awt.Color(255, 255, 255));
        totalrate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalrate.setForeground(new java.awt.Color(255, 255, 255));
        totalrate.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        discounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        discounttext.setForeground(new java.awt.Color(0, 102, 0));
        discounttext.setBorder(null);
        discounttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discounttextActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Discount");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Total Commision");

        nettotalcommitiontext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nettotalcommitiontext.setForeground(new java.awt.Color(0, 102, 0));
        nettotalcommitiontext.setBorder(null);
        nettotalcommitiontext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nettotalcommitiontextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalrate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(discounttext)
                    .addComponent(nettotalcommitiontext))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nettotalcommitiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, 0)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1))
                    .addComponent(enbledpnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(enbledpnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        svbtn.setBackground(new java.awt.Color(0, 153, 0));
        svbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        svbtn.setForeground(new java.awt.Color(255, 255, 255));
        svbtn.setText("Execute");
        svbtn.setBorder(null);
        svbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svbtnActionPerformed(evt);
            }
        });

        deletebtn.setBackground(new java.awt.Color(153, 0, 0));
        deletebtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deletebtn.setForeground(new java.awt.Color(255, 255, 255));
        deletebtn.setText("Delete");
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout hereLayout = new javax.swing.GroupLayout(here);
        here.setLayout(hereLayout);
        hereLayout.setHorizontalGroup(
            hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hereLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        hereLayout.setVerticalGroup(
            hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hereLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        dataTable.setAutoCreateRowSorter(true);
        dataTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        dataTable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Item Code", "Item Name", "Unit Rate", "Qty", "Unit", "Discount", "Net Total ", "Commision(%)", "Com.Amt", "Total Commision"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTable.setRowHeight(30);
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
            dataTable.getColumnModel().getColumn(1).setPreferredWidth(50);
            dataTable.getColumnModel().getColumn(2).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(300);
            dataTable.getColumnModel().getColumn(3).setResizable(false);
            dataTable.getColumnModel().getColumn(3).setPreferredWidth(50);
            dataTable.getColumnModel().getColumn(4).setResizable(false);
            dataTable.getColumnModel().getColumn(4).setPreferredWidth(50);
            dataTable.getColumnModel().getColumn(5).setResizable(false);
            dataTable.getColumnModel().getColumn(6).setResizable(false);
            dataTable.getColumnModel().getColumn(7).setResizable(false);
            dataTable.getColumnModel().getColumn(8).setResizable(false);
            dataTable.getColumnModel().getColumn(9).setResizable(false);
            dataTable.getColumnModel().getColumn(10).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(here, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(1, 1, 1))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(here, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseClicked
        try {

            supliyerbox.removeAllItems();
            supliyerbox.setSelectedItem("Select");

            itemnamesearch.removeAllItems();
            itemnamesearch.setSelectedItem("Select");
            unibox.removeAllItems();
            unibox.setSelectedItem("Select");
            Item();
            unit();
            // suppliyer();
        } catch (SQLException ex) {
            Logger.getLogger(Purchase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jCheckBox1MouseClicked

    private void countryboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_countryboxPopupMenuWillBecomeInvisible
        String country = (String) countrybox.getSelectedItem();
        try {
            String sql = "Select * from country  where name='" + country + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {

                countryid = rs.getString("id");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_countryboxPopupMenuWillBecomeInvisible
    private void StockloadMinus() {

        try {
            String sql1 = "Select st.Qty as 'stockqty' from stock st where st.procode=(Select pc.prcode from exportdetails pc where pc.prcode=st.procode AND pc.exportCode='" + parchaseText.getText() + "' )";
            pst = conn.prepareStatement(sql1);

            rs = pst.executeQuery();

            while (rs.next()) {
                Float updateqty = rs.getFloat("stockqty");
                //String sql1 = "Select prcode,qty,unit , count(id)  from purchasedetails pc where pc.purchaseCode='" + parchasetext.getText() + "'.Update  stock st set st.Qty=pc.qty where st.procode=pc.prcode";
                String sql = "Update stock sta set sta.Qty=(Select pc.qty-'" + updateqty + "'  from exportdetails pc where pc.prcode=sta.procode AND pc.exportCode='" + parchaseText.getText() + "') where (select pc.prcode from exportdetails pc where pc.prcode=sta.procode AND pc.exportCode='" + parchaseText.getText() + "')=sta.procode";
                //  String sql="update stock sta set Qty=(select sum(pc.qty+(select st.Qty from stock st where st.procode=pc.prcode))from purchasedetails pc where sta.procode=pc.prcode AND pc.purchaseCode='"+parchasetext.getText()+"')";

                pst = conn.prepareStatement(sql);
                pst.execute();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void StockloadPlus() {

        try {
            String sql1 = "Select st.Qty as 'stockqty' from stock st where st.procode=(Select pc.prcode from exportdetails pc where pc.prcode=st.procode AND pc.exportCode='" + parchaseText.getText() + "' )";
            pst = conn.prepareStatement(sql1);

            rs = pst.executeQuery();

            while (rs.next()) {
                Float updateqty = rs.getFloat("stockqty");
                //String sql1 = "Select prcode,qty,unit , count(id)  from purchasedetails pc where pc.purchaseCode='" + parchasetext.getText() + "'.Update  stock st set st.Qty=pc.qty where st.procode=pc.prcode";
                String sql = "Update stock sta set sta.Qty=(Select pc.qty+'" + updateqty + "'  from exportdetails pc where pc.prcode=sta.procode AND pc.exportCode='" + parchaseText.getText() + "') where (select pc.prcode from exportdetails pc where pc.prcode=sta.procode AND pc.exportCode='" + parchaseText.getText() + "')=sta.procode";
                //  String sql="update stock sta set Qty=(select sum(pc.qty+(select st.Qty from stock st where st.procode=pc.prcode))from purchasedetails pc where sta.procode=pc.prcode AND pc.purchaseCode='"+parchasetext.getText()+"')";

                pst = conn.prepareStatement(sql);
                pst.execute();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void SuppliyerUpdate() {
        try {

            String id = codetext.getText();

            String sql = "Update suppliyer_import_export_balance set supid ='" + supid + "',countryid='" + countryid + "',exportAmount='" + totalrate.getText() + "' where exportimportcode='" + parchaseText.getText() + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed
        if (importmodification == 0) {
            try {
                purchasedeletehistory();
                exportupdate();
                exportDetailsInsert();
                //reload();
            } catch (SQLException ex) {
                Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Exportview.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            StockloadPlus();

            try {
                purchasedeletehistory();
                ///importInsert();
                exportupdate();
                exportDetailsInsert();
                //reload();
            } catch (SQLException | IOException ex) {
                Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
            }
            SuppliyerUpdate();
            StockloadMinus();

        }

    }//GEN-LAST:event_svbtnActionPerformed

    private void supliyerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supliyerboxPopupMenuWillBecomeInvisible
        try {
            String sql = "Select id from suplyierInfo where supliyername='" + supliyerbox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                supid = rs.getInt("id");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_supliyerboxPopupMenuWillBecomeInvisible

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        if (importmodification == 0) {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from export where exportCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, parchaseText.getText());

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                try {
                    String sql = "Delete from exportDetails where exporttCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, parchaseText.getText());

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                Export filte = null;

                try {
                    filte = new Export();
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(Exportview.class.getName()).log(Level.SEVERE, null, ex);
                }

                filte.setVisible(true);
                this.getDesktopPane().add(filte);
                Dimension desktopSize = getDesktopPane().getSize();
                Dimension jInternalFrameSize = filte.getSize();
                filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                        (desktopSize.height - jInternalFrameSize.height) / 2);
                filte.moveToFront();
                this.dispose();

            }

        } else {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete This Execute Export Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {

                StockloadPlus();
                try {
                    String sql = "Delete from export where exportCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, parchaseText.getText());

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                try {
                    String sql = "Delete from exportDetails where exportCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, parchaseText.getText());

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                try {
                    String sql = "Delete from suppliyer_import_export_balance where exportimportcode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, parchaseText.getText());

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                Export filte = null;

                try {
                    filte = new Export();
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(Exportview.class.getName()).log(Level.SEVERE, null, ex);
                }

                filte.setVisible(true);
                this.getDesktopPane().add(filte);
                Dimension desktopSize = getDesktopPane().getSize();
                Dimension jInternalFrameSize = filte.getSize();
                filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                        (desktopSize.height - jInternalFrameSize.height) / 2);
                filte.moveToFront();
                this.dispose();

            }
        }
    }//GEN-LAST:event_deletebtnActionPerformed

    private void addbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbtnActionPerformed
        finelEntry();
        codetext.requestFocusInWindow();
        qtytext.setBackground(Color.WHITE);

    }//GEN-LAST:event_addbtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clear();
        updatekey = 0;
        codetext.enable(true);
        itemnamesearch.enable(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void codetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();
            qtytext.setBackground(Color.YELLOW);

        }
    }//GEN-LAST:event_codetextKeyPressed

    private void codetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyReleased

        String SearchText = (String) codetext.getText();
        try {

            String sql = "Select id,ita.itemName as 'itemn', ita.tp as 'tpprice',mrp,(select un.unitshort from unit un where un.id=ita.stockunit) as 'unitshort',(select st.Qty from stock st where st.procode=ita.id) as 'stock' from item ita where ita.id='" + SearchText + "' OR ita.barcode='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            if (rs.next()) {

                String name = rs.getString("itemn");
                itemnamesearch.setSelectedItem(name);
                String tpprice = rs.getString("tpprice");
                String mrp=rs.getString("mrp");
                unitrateText.setText(mrp);
                String stock = rs.getString("stock");
                lottext.setText(stock);
                String unit = rs.getString("unitshort");
                unibox.setSelectedItem(unit);
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }//GEN-LAST:event_codetextKeyReleased

    private void codetextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyTyped

    }//GEN-LAST:event_codetextKeyTyped

    private void qtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            //  finelEntry();
            commitiontext.requestFocusInWindow();
            qtytext.setBackground(Color.WHITE);

        }
    }//GEN-LAST:event_qtytextKeyPressed

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
        String SearchText = (String) itemnamesearch.getSelectedItem();
        try {

            String sql = "Select ita.Itemcode as 'itaid',itemName, (select un.unitshort from unit un where un.id=ita.stockunit) as 'unitshort',ita.tp as 'tprice',mrp,(select st.Qty from stock st where st.procode=ita.id) as 'stock' from item ita where ita.itemName='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("itaid");
                codetext.setText(Id);
                String unitrate = rs.getString("tprice");
                unitrateText.setText(unitrate);

                String stock = rs.getString("stock");
                lottext.setText(stock);

                String unit = rs.getString("unitshort");
                unibox.setSelectedItem(unit);

                float tp = rs.getFloat("tprice");
                float mrp = rs.getFloat("mrp");

                float profite = (mrp - tp / 100 * tp);
                String pro = String.format("%.2f", profite);
                // proftetext.setText(pro +"%");
                qtytext.requestFocusInWindow();
                qtytext.setBackground(Color.YELLOW);
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void itemnamesearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemnamesearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemnamesearchActionPerformed

    private void itemnamesearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyTyped

        try {
            Item();
        } catch (SQLException ex) {
            Logger.getLogger(NewImportExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_itemnamesearchKeyTyped

    private void commitiontextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_commitiontextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            finelEntry();
            codetext.requestFocusInWindow();
            qtytext.setBackground(Color.WHITE);

        }
    }//GEN-LAST:event_commitiontextKeyPressed

    private void discounttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discounttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_discounttextActionPerformed

    private void nettotalcommitiontextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nettotalcommitiontextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nettotalcommitiontextActionPerformed

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        int selectedRowIndex = dataTable.getSelectedRow();
        updatekey = 1;
        codetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        unitrateText.setText(model.getValueAt(selectedRowIndex, 3).toString());
        qtytext.setText(model.getValueAt(selectedRowIndex, 4).toString());
        unibox.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());
        commitiontext.setText(model.getValueAt(selectedRowIndex, 8).toString());
    }//GEN-LAST:event_dataTableMouseClicked

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
        deleterow();
    }//GEN-LAST:event_dataTableKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addbtn;
    private javax.swing.JTextField codetext;
    private javax.swing.JTextField commitiontext;
    private javax.swing.JComboBox<String> countrybox;
    private javax.swing.JTable dataTable;
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField discounttext;
    private javax.swing.JLabel enbledpanel;
    private javax.swing.JPanel enbledpnl;
    private javax.swing.JPanel here;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton2;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lottext;
    private javax.swing.JTextField nettotalcommitiontext;
    private javax.swing.JLabel parchaseText;
    private com.toedter.calendar.JDateChooser parchasedate;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JComboBox<String> supliyerbox;
    private javax.swing.JButton svbtn;
    private javax.swing.JLabel totalrate;
    private javax.swing.JComboBox<String> unibox;
    private javax.swing.JTextField unitrateText;
    // End of variables declaration//GEN-END:variables
}
