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
import java.io.FileInputStream;
import java.io.IOException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class PurchaseEntrys extends javax.swing.JInternalFrame {

    private static final DecimalFormat df2 = new DecimalFormat("#.00");
    int tree = 0;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int unitid = 0;
    int supid = 0;
    //  Dashboard dashboard = new Dashboard();
    int userid = 0;
    int updatekey = 0;
    int view = 0;

    int updatedeletekey = 0;
    int modifiaction = 0;
    float minusAmount;

    //supplier
    double openingbalance;
    double consighnmentamnt;
    double Balancedue;
    String suplyer = null;
    float includevat;

    float itemmrp;
    String Itemcode;

    /**
     * Creates new form Purchase
     *
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public PurchaseEntrys() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        //   AutoCompleteDecorator.decorate(itemnamesearch);
        AutoCompleteDecorator.decorate(unibox);
        AutoCompleteDecorator.decorate(supliyerbox);
        // Item();
        unit();
        suppliyer();
        parchase_code();
        currentDate();
        accsessModification();
        Deletebtnactivate();
        ExtractFilter();
        itemnamesearch.requestFocusInWindow();
    }

    public PurchaseEntrys(String table_click, int activemodification) throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        // AutoCompleteDecorator.decorate(itemnamesearch);
        AutoCompleteDecorator.decorate(unibox);

        // Item();
        unit();
        currentDate();
        PurchaseDetailsPR(table_click);
        parchaseText.setText(table_click);
        view = 1;
        modifiaction = activemodification;
        suppliyer();
        accsessModification();

        updatedeletekey = 1;
        Deletebtnactivate();
        ExtractFilter();
        itemnamesearch.requestFocusInWindow();
    }

    private void ExtractFilter() {
        final JTextField textfield = (JTextField) itemnamesearch.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {

                        genericbox.removeAllItems();
                        strengthtext.setText(null);
                        batchtext.setText(null);
                        boxsizetext.setText(null);
                        unitrateText.setText(null);
                        /*
                        discounttext.setText(null);
                        pvattext.setText(null);
                        pvatpertext.setText(null);
                        ptotaltptext.setText(null);
                         */
                        qtytext.setText(null);
                        //    bonusqtytext.setText(null);
                        unibox.setSelectedIndex(0);
                        /*
                        altotalvattext.setText(null);
                        netotaltext.setText(null);
                         */
                        stocktext.setText(null);
                        expdate.setText(null);
                        totalamounttext.setText(null);
                        //  totaldiscounttext.setText(null);

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

            String sql = "Select itemName from item WHERE lower(itemName) LIKE '%" + enteredText + "%' OR Itemcode LIKE '%" + enteredText + "%'";
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
            //itemnamesearch.setModel(new DefaultComboBoxModel(filterArray.toArray()));

            itemnamesearch.setSelectedItem(enteredText);
            itemnamesearch.showPopup();

        } else {
            itemnamesearch.hidePopup();

            genericbox.removeAllItems();
            strengthtext.setText(null);
            batchtext.setText(null);
            boxsizetext.setText(null);
            unitrateText.setText(null);
            /*
            discounttext.setText(null);
            pvattext.setText(null);
            pvatpertext.setText(null);
            ptotaltptext.setText(null);
             */
            qtytext.setText(null);
            // bonusqtytext.setText(null);

            unibox.setSelectedIndex(0);
            totalamounttext.setText(null);
            /*
            totaldiscounttext.setText(null);
            altotalvattext.setText(null);
            netotaltext.setText(null);
             */
            stocktext.setText(null);
            expdate.setText(null);

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

    private void Deletebtnactivate() {
        if (updatedeletekey == 1) {

            deletebtn.setEnabled(true);

        } else {

            deletebtn.setEnabled(false);

        }

    }

    private void accsessModification() {
        try {
            String sql = "Select purcedit,purcdelete from modificationaccsess where userid='" + userid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                int purcedit = rs.getInt("purcedit");
                int purcdelete = rs.getInt("purcdelete");

                if (purcedit == 1) {

                    svbtn.setEnabled(true);

                } else {

                    svbtn.setEnabled(false);

                }
                if (purcdelete == 1) {

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

    private void PurchaseDetailsPR(String table_click) throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();

        try {

            String sql = "Select pr.prcode as 'id',"
                    + "(select ita.itemName from item ita where ita.Itemcode=pr.prcode) as 'Itemname',"
                    + "batch,"
                    + "expdate,"
                    + "boxsize,"
                    + "unitrate,"
                    + "mrp,"
                    /*  + "discount,"
                    + "vat,"
                     */
                    + " qty,"
                    //   + " bonusqty,"
                    + "unit,"
                    + "total"
                    //  + "totalvat,"
                    /// + "totaldiscount,"
                    //  + "nettotal"
                    + " from purchasedetails pr  where pr.purchaseCode='" + table_click + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            while (rs.next()) {
                tree++;
                String id = rs.getString("id");
                String itemname = rs.getString("Itemname");
                String batch = rs.getString("batch");
                String expdates = rs.getString("expdate");
                String boxsize = rs.getString("boxsize");
                float unitrate = rs.getFloat("unitrate");
                float mrp = rs.getFloat("mrp");
                float qty = rs.getFloat("qty");
                // float bonusqty = rs.getFloat("bonusqty");
                // String unit = rs.getString("unit");
                float total = rs.getFloat("total");
                /*
                float discount = rs.getFloat("discount");
                float vat = rs.getFloat("vat");
                float totaldiscount = rs.getFloat("totaldiscount");
                float totalvat = rs.getFloat("totalvat");
                float nettotal = rs.getFloat("nettotal");
                 */

                model2.addRow(new Object[]{tree, id, itemname, batch, expdates, boxsize, unitrate, mrp, qty, total});

            }
            //  dataTable.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {

            String sql = "Select "
                    + "purchaseCode,"
                    + "Totalrate,"
                    //   + "totalvat,"
                    //  + "nettotal"
                    + "supliyer,"
                    + "pdate,"
                    + "remark,"
                    + " (select sup.supliyername from suplyierinfo sup where sup.id=par.supliyer) as'supliyerinfo'  from purchase par  where par.purchaseCode='" + table_click + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            if (rs.next()) {
                /*
                String TotalRate = rs.getString("Totalrate");
                totalrate.setText(TotalRate);
                String totalvat = rs.getString("totalvat");
                totalvattext.setText(totalvat);
                String nettotal = rs.getString("nettotal");
                nettotaltext.setText(nettotal);
               
                 */

                String ParchseNo = rs.getString("purchaseCode");
                parchaseText.setText(ParchseNo);
                supid = rs.getInt("supliyer");
                String suppliyer = rs.getString("supliyerinfo");
                supliyerbox.setSelectedItem(suppliyer);

                Date inutDate = rs.getDate("pdate");
                parchasedate.setDate(inutDate);
                String remark = rs.getString("remark");
                remarktext.setText(remark);

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        totalrate.setText(df2.format(total_action_mrp()));
        /* netdiscounttext.setText(df2.format(total_action_discount()));
        totalvattext.setText(df2.format(total_action_vat()));
        nettotaltext.setText(df2.format(total_action_nettotal()));
         */
    }

    private void deleterow() {

        int[] toDelete = dataTable.getSelectedRows();
        Arrays.sort(toDelete); // be shure to have them in ascending order.
        DefaultTableModel myTableModel = (DefaultTableModel) dataTable.getModel();
        for (int ii = toDelete.length - 1; ii >= 0; ii--) {
            myTableModel.removeRow(toDelete[ii]); // beginning at the largest.
        }
        float amount = total_action_mrp();
        totalrate.setText(df2.format(amount));
        /*
        float totaldiscounts = total_action_discount();
        netdiscounttext.setText(df2.format(totaldiscounts));

        float totalvats = total_action_vat();
        totalvattext.setText(df2.format(totalvats));

        float netalltotals = total_action_nettotal();
        nettotaltext.setText(df2.format(netalltotals));
         */

        clear();
        updatekey = 0;
    }

    private void enter() {
        java.awt.event.KeyEvent evt = null;
        if (evt.getKeyCode() != KeyEvent.VK_F5) {

        } else {

            finelEntry();

        }

    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        java.sql.Date date = new java.sql.Date(now.getTime());
        parchasedate.setDate(date);

    }

    private void parchase_code() {
        try {
            int new_inv = 1;
            String NewParchaseCode = ("PR-" + new_inv + "");
            String sql = "Select id from purchase";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = ("PR-" + ParchaseCode + "");
            }

            parchaseText.setText(NewParchaseCode);
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void parchaseInsert() {
        try {
            String sql = "Insert into purchase("
                    + "purchaseCode,"
                    + "pdate,"
                    + "supliyer,"
                    + "Totalrate,"
                    + "nettotal,"
                    + "remark,"
                    + "status,"
                    + "GRNstatus,"
                    + "inputuser) values (?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, parchaseText.getText());
            pst.setString(2, ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText());
            pst.setInt(3, supid);
            pst.setString(4, totalrate.getText());
            pst.setString(5, totalrate.getText());
            /*  pst.setString(5, totalvattext.getText());
            pst.setString(6, nettotaltext.getText());
            pst.setString(7, netdiscounttext.getText());
             */
            pst.setString(6, remarktext.getText());
            pst.setString(7, "1");
            pst.setInt(8, 0);
            pst.setInt(9, userid);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void parcheDetailsInsert() throws SQLException, IOException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                float tpvate = 0;
                float tpdiscount = 0;
                float profit = 0;
                float profitper = 0;
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String procode = (String) dataTable.getValueAt(row, 1);
                String batch = (String) dataTable.getValueAt(row, 3);
                String expdates = (String) dataTable.getValueAt(row, 4);
                //  String exd = expdates.toString();
                String boxsize = (String) dataTable.getValueAt(row, 5);
                float tp = (Float) dataTable.getValueAt(row, 6);
                float mrp = (Float) dataTable.getValueAt(row, 7);
                //   float discount = (Float) dataTable.getValueAt(row, 8);
                //  float vat = (Float) dataTable.getValueAt(row, 9);
                float qty = (Float) dataTable.getValueAt(row, 8);
                //  float bonusqty = (Float) dataTable.getValueAt(row, 11);
                //  tpdiscount = tp - discount;
                //   tpvate = tp + vat;
                profit = mrp - tp;
                profitper = ((profit * tp) / 100);

                //  String unit = (String) dataTable.getValueAt(row, 6);
                float total = (Float) dataTable.getValueAt(row, 9);
                //  float totaldiscount = (Float) dataTable.getValueAt(row, 13);

                //   float totalvat = (Float) dataTable.getValueAt(row, 14);
                //  float nettotal = (Float) dataTable.getValueAt(row, 15);
                try {
                    String sql = "Insert into purchaseDetails("
                            + "purchaseCode,"
                            + "prcode,"
                            + "barcode,"
                            + "batch,"
                            + "expdate,"
                            + "boxsize,"
                            + "unitrate,"
                            + "mrp,"
                            + "qty,"
                            + "unit,"
                            + "total,"
                            + "supid,"
                            + "profit,"
                            + "profitper"
                            + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, parchaseText.getText());
                    pst.setString(2, procode);
                    pst.setString(3, procode);
                    pst.setString(4, batch);
                    pst.setString(5, expdates);
                    pst.setString(6, boxsize);
                    pst.setFloat(7, tp);
                    pst.setFloat(8, mrp);
                    pst.setFloat(9, qty);
                    pst.setString(10, "");
                    pst.setFloat(11, total);
                    pst.setInt(12, supid);
                    pst.setFloat(13, profit);
                    pst.setFloat(14, profitper);
                    pst.execute();
                    ///    examperticipationinsert(id,examcode,exam);

                    // 
                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }
            //  loadGRN();
            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void purchaseupdate() {
        try {

            String date = ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText();

            String sql = "Update purchase set pdate='" + date + "',"
                    + "supliyer='" + supid + "',"
                    + "Totalrate='" + totalrate.getText() + "',"
                    + "nettotal='" + totalrate.getText() + "',"
                    + "inputuser='" + userid + "' where purchaseCode='" + parchaseText.getText() + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
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
                int id = rs.getInt("id");
                unibox.addItem(name);
                //unibox.setSelectedIndex(id);

            }
        } catch (SQLException e) {
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

    private float total_action_mrp() {

        int rowaCount = dataTable.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 9).toString());
        }

        return (float) totaltpmrp;

    }

    /*
    private float total_action_discount() {

        int rowaCount = dataTable.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());
            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 13).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_vat() {

        int rowaCount = dataTable.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 14).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_nettotal() {

        int rowaCount = dataTable.getRowCount();
        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 15).toString());
        }

        return (float) totaltpmrp;

    }
     */
    private void clear() {
        itemnamesearch.removeAllItems();
        genericbox.removeAllItems();
        strengthtext.setText(null);
        batchtext.setText(null);
        boxsizetext.setText(null);
        unitrateText.setText(null);
        mrptext.setText(null);
        // discounttext.setText(null);
        //    pvattext.setText(null);
        //  pvatpertext.setText(null);
        //  ptotaltptext.setText(null);
        qtytext.setText(null);
        //  bonusqtytext.setText(null);

        unibox.setSelectedIndex(0);
        totalamounttext.setText(null);
        //  totaldiscounttext.setText(null);
        //  altotalvattext.setText(null);
        //  netotaltext.setText(null);
        stocktext.setText(null);
        expdate.setText(null);
        itemnamesearch.requestFocusInWindow();
    }

    private void checkentry() {

        String s = "";
        boolean exists = false;
        //DefaultTableModel myTableModel = (DefaultTableModel) dataTable.getModel();
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            s = dataTable.getValueAt(i, 1).toString().trim();
            /*  String batch = batchtext.getText();
            String expdates = expdate.getText();
            String boxsize = boxsizetext.getText();
             */
            float tpd = Float.parseFloat(unitrateText.getText());
            float qtytbl = (Float) dataTable.getValueAt(i, 9);
            float applyqty = Float.parseFloat(qtytext.getText());
            float totalqty = qtytbl + applyqty;
            float bonusqty = 0;
            float discount = 0;
            float mrp = Float.parseFloat(mrptext.getText());
            /*
            if (bonusqtytext.getText().isEmpty()) {
                bonusqty = 0;
            } else {
                bonusqty = Float.parseFloat(bonusqtytext.getText());
            }

            float mrp = Float.parseFloat(mrptext.getText());
            if (discounttext.getText().isEmpty()) {
                discount = 0;
            } else {
                discount = Float.parseFloat(discounttext.getText());

            }
            float vat = Float.parseFloat(pvattext.getText());
             */

            float totalamount = totalqty * tpd;
            //float totaldiscounts = totalqty * discount;
            //   float allTotalVat = vat * totalqty;
            ///   float NetallTotal = totalamount + allTotalVat;
            if (Itemcode.equals("")) {
                // JOptionPane.showMessageDialog(null, "Enter Invoice Details First");
            } else if (Itemcode.equals(s)) {
                exists = true;
                //  myTableModel.removeRow(i);
                /* dataTable.setValueAt(batch, i, 3);
                dataTable.setValueAt(expdates, i, 4);
                dataTable.setValueAt(boxsize, i, 5);*/
                dataTable.setValueAt(tpd, i, 6);
                dataTable.setValueAt(mrp, i, 7);
                //dataTable.setValueAt(discount, i, 8);
                // dataTable.setValueAt(vat, i, 9);
                dataTable.setValueAt(totalqty, i, 8);
                //   dataTable.setValueAt(bonusqty, i, 9);
                dataTable.setValueAt(totalamount, i, 9);
                //  dataTable.setValueAt(totaldiscounts, i, 13);
                //  dataTable.setValueAt(allTotalVat, i, 14);
                // dataTable.setValueAt(NetallTotal, i, 15);
                float amount = total_action_mrp();
                totalrate.setText(df2.format(amount));
                /*
                float totaldiscount = total_action_discount();
                netdiscounttext.setText(df2.format(totaldiscount));

                float totalvats = total_action_vat();
                totalvattext.setText(df2.format(totalvats));

                float netalltotals = total_action_nettotal();
                nettotaltext.setText(df2.format(netalltotals));
                
                 */
                break;
            }
        }
        if (!exists) {
            entryData();
        } else {
            //JOptionPane.showMessageDialog(null, "This Data Already Exist.");

            clear();
        }

    }

    private void entryData() {
        float discount;
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        tree++;
        float bonusqty = 0;
        /*
        if (bonusqtytext.getText().isEmpty()) {
            bonusqty = 0;
        } else {
            bonusqty = Float.parseFloat(bonusqtytext.getText());
        }
         */
        float tpd = Float.parseFloat(unitrateText.getText());
        float mrp = Float.parseFloat(mrptext.getText());
        /*
        if (discounttext.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Float.parseFloat(discounttext.getText());

        }
         */
        //  float vat = Float.parseFloat(pvattext.getText());
        float qty = Float.parseFloat(qtytext.getText());
        float totalamount = Float.parseFloat(totalamounttext.getText());
        //  float totaldiscount = Float.parseFloat(totaldiscounttext.getText());
        //  float allTotalVat = Float.parseFloat(altotalvattext.getText());
        // float NetallTotal = Float.parseFloat(netotaltext.getText());
        model2.addRow(new Object[]{tree, Itemcode, itemnamesearch.getSelectedItem(), batchtext.getText(), expdate.getText(), boxsizetext.getText(), tpd, mrp, qty, totalamount});

        float amount = total_action_mrp();
        totalrate.setText(df2.format(amount));
        // float totaldiscounts = total_action_discount();
        // netdiscounttext.setText(df2.format(totaldiscounts));
        //   float totalvats = total_action_vat();
        //  totalvattext.setText(df2.format(totalvats));
        //   float netalltotals = total_action_nettotal();
        //  nettotaltext.setText(df2.format(netalltotals));
        clear();
    }

    private void finelEntry() {

        if (updatekey == 0) {

            if (Itemcode == null || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");

            } else {
                checkentry();

            }

        } else if (unitrateText.getText().isEmpty() || qtytext.getText().isEmpty()) {

        } else {

            DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
            int i = dataTable.getSelectedRow();
            if (i >= 0) {
                float qty = Float.parseFloat(qtytext.getText());
                if (qty > 0) {
                    String batch = batchtext.getText();
                    String expdates = expdate.getText();
                    String boxsize = boxsizetext.getText();
                    float discount;
                    float bonusqty = 0;
                    float tpd = Float.parseFloat(unitrateText.getText());
                    float mrp = Float.parseFloat(mrptext.getText());
                    /*
                if (bonusqtytext.getText().isEmpty()) {
                    bonusqty = 0;
                } else {
                    bonusqty = Float.parseFloat(bonusqtytext.getText());
                }
             
                if (discounttext.getText().isEmpty()) {
                    discount = 0;
                } else {
                    discount = Float.parseFloat(discounttext.getText());

                }
                     */
                    //  float vat = Float.parseFloat(pvattext.getText());

                    float totalamount = Float.parseFloat(totalamounttext.getText());
                    //    float totaldiscount = Float.parseFloat(totaldiscounttext.getText());
                    //   float allTotalVat = Float.parseFloat(altotalvattext.getText());
                    //   float NetallTotal = Float.parseFloat(netotaltext.getText());
                    dataTable.setValueAt(batch, i, 3);
                    dataTable.setValueAt(expdates, i, 4);
                    dataTable.setValueAt(boxsize, i, 5);
                    dataTable.setValueAt(tpd, i, 6);
                    dataTable.setValueAt(mrp, i, 7);
                    //  dataTable.setValueAt(discount, i, 8);
                    //   dataTable.setValueAt(vat, i, 9);
                    dataTable.setValueAt(qty, i, 8);
                    // dataTable.setValueAt(bonusqty, i, 11);
                    dataTable.setValueAt(totalamount, i, 9);
                    // dataTable.setValueAt(totaldiscount, i, 13);
                    //  dataTable.setValueAt(allTotalVat, i, 14);
                    //  dataTable.setValueAt(NetallTotal, i, 15);
                    float amount = total_action_mrp();
                    totalrate.setText(df2.format(amount));

                    // float totaldiscounts = total_action_discount();
                    //  netdiscounttext.setText(df2.format(totaldiscounts));
                    //  float totalvats = total_action_vat();
                    // totalvattext.setText(df2.format(totalvats));
                    //   float netalltotals = total_action_nettotal();
                    //  nettotaltext.setText(df2.format(netalltotals));
                    clear();
                    updatekey = 0;
                    Itemcode = null;
                }

            }

        }

    }

    private void reload() throws SQLException {

        Itemcode = null;
        itemnamesearch.setSelectedIndex(0);
        unitrateText.setText(null);
        qtytext.setText(null);
        unibox.setSelectedIndex(0);
        totalrate.setText(null);
        //  discounttext.setText(null);
        parchasedate.setDate(null);
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);

        itemnamesearch.enable(true);

        parchase_code();
        suppliyer();

    }

    private void loadGRN() throws SQLException, IOException {
        if (modifiaction == 0) {
            String table_click = parchaseText.getText();
            PurchaseDetails filte = null;

            filte = new PurchaseDetails(table_click);

            filte.setVisible(true);
            this.getDesktopPane().add(filte);

            //Homedesktop.add(It);
            filte.setVisible(true);

            Dimension desktopSize = getDesktopPane().getSize();
            Dimension jInternalFrameSize = filte.getSize();
            filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                    (desktopSize.height - jInternalFrameSize.height) / 2);
            filte.moveToFront();
            this.dispose();
        } else {
            GRNframe filte = null;

            filte = new GRNframe();

            filte.setVisible(true);
            this.getDesktopPane().add(filte);

            //Homedesktop.add(It);
            filte.setVisible(true);

            Dimension desktopSize = getDesktopPane().getSize();
            Dimension jInternalFrameSize = filte.getSize();
            filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                    (desktopSize.height - jInternalFrameSize.height) / 2);
            filte.moveToFront();
            this.dispose();

        }

    }

    private void purchasedeletehistory() {

        try {
            String sql = "Delete from purchaseDetails where purchaseCode=?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, parchaseText.getText());

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void TotalAction() {
        float tp = 0;
        float totalamount = 0;
        float qty = 0;
        if (unitrateText.getText().isEmpty()) {
            tp = 0;
        } else {
            tp = Float.parseFloat(unitrateText.getText());
        }

        if (qtytext.getText().isEmpty()) {
            qty = 0;
        } else {
            qty = Float.parseFloat(qtytext.getText());
        }

        // tpwithdiscount = tp - discount;
        //   float vatper = vatPercentage(vat, tp);
        // pvatpertext.setText(String.valueOf(Math.round(vatper)));
        // float vatafterdis = vatValue(vatper, tpwithdiscount);
        // tpvat = tpwithdiscount + vatafterdis;
        //  ptotaltptext.setText(df2.format(tpvat));
        if (qty == 0) {
            totalamounttext.setText(null);

        } else {
            totalamount = tp * qty;
            totalamounttext.setText(df2.format(totalamount));

        }

    }

    /*
    private void SetVatValueWithDiscount() {
        float tp = 0;
        float discount = 0;
        float vat = 0;
        float tpwithdiscount = 0;

        if (unitrateText.getText().isEmpty()) {
            tp = 0;
        } else {
            tp = Float.parseFloat(unitrateText.getText());
        }

        if (discounttext.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Float.parseFloat(discounttext.getText());
        }
        if (pvatpertext.getText().isEmpty()) {
            vat = 0;
        } else {
            vat = Float.parseFloat(pvatpertext.getText());
        }
        tpwithdiscount = tp - discount;
        float vatafterdis = vatValue(vat, tpwithdiscount);
        pvattext.setText(df2.format(vatafterdis));

    }

    private void SetPvat() {
        float tprate = 0;
        float tp = 0;
        float discount = 0;
        float vat = 0;
        float tpwithdiscount = 0;

        if (ptotaltptext.getText().isEmpty()) {
            tp = 0;
        } else {
            tp = Float.parseFloat(ptotaltptext.getText());
        }

        if (discounttext.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Float.parseFloat(discounttext.getText());
        }
        if (pvatpertext.getText().isEmpty()) {
            vat = 0;
        } else {
            vat = Float.parseFloat(pvatpertext.getText());
        }
        tpwithdiscount = tp - discount;
        float vatafterdis = vatValue(vat, tpwithdiscount);
        pvattext.setText(df2.format(vatafterdis));
        tprate = tp - vatafterdis;
        unitrateText.setText(df2.format(tprate));
       // mrptext.setText(df2.format(tprate));
    }

    private float vatPercentage(float vat, float tp) {
        float per = (vat + 100) / tp;
        return per;
    }

    private float vatValue(float per, float tp) {

        float vatper = ((tp / (100+per)) * per);
        return vatper;

    }
     */
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
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        strengthtext = new javax.swing.JTextField();
        batchtext = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        genericbox = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        boxsizetext = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        stocktext = new javax.swing.JTextField();
        expdate = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        mrptext = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        unitrateText = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        unibox = new javax.swing.JComboBox<>();
        qtytext = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        totalamounttext = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        parchaseText = new javax.swing.JLabel();
        parchasedate = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarktext = new javax.swing.JTextArea();
        supliyerbox = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        here = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        totalrate = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        svbtn = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Purchase");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(1147, 2147483647));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Batch");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Item Name");

        itemnamesearch.setBackground(new java.awt.Color(255, 255, 255));
        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
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
        itemnamesearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                itemnamesearchKeyTyped(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Generic");

        strengthtext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        strengthtext.setForeground(new java.awt.Color(102, 0, 0));

        batchtext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        batchtext.setForeground(new java.awt.Color(102, 0, 0));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Exp Date");

        genericbox.setBackground(new java.awt.Color(255, 255, 255));
        genericbox.setEditable(true);
        genericbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        genericbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
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
        genericbox.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                genericboxKeyTyped(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Strength");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Box Size");

        boxsizetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        boxsizetext.setForeground(new java.awt.Color(153, 0, 0));
        boxsizetext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        boxsizetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                boxsizetextKeyPressed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("Prasent Stock");

        stocktext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        stocktext.setForeground(new java.awt.Color(153, 0, 0));
        stocktext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        stocktext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                stocktextKeyPressed(evt);
            }
        });

        expdate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        expdate.setForeground(new java.awt.Color(102, 0, 0));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(genericbox, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(strengthtext, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(batchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expdate, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(boxsizetext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stocktext, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(genericbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(strengthtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(batchtext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(expdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(1, 1, 1))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(boxsizetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stocktext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel11.setBackground(new java.awt.Color(0, 102, 102));

        mrptext.setEditable(false);
        mrptext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        mrptext.setForeground(new java.awt.Color(102, 0, 0));
        mrptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mrptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mrptextKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Qty");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("TP");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Unit ");

        unitrateText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        unitrateText.setForeground(new java.awt.Color(102, 0, 0));
        unitrateText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        unitrateText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unitrateTextActionPerformed(evt);
            }
        });
        unitrateText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                unitrateTextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                unitrateTextKeyReleased(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("MRP");

        unibox.setBackground(new java.awt.Color(255, 255, 255));
        unibox.setEditable(true);
        unibox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        qtytext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        qtytext.setForeground(new java.awt.Color(153, 0, 0));
        qtytext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        qtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                qtytextKeyReleased(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Total");

        totalamounttext.setEditable(false);
        totalamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalamounttext.setForeground(new java.awt.Color(0, 102, 0));
        totalamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(totalamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel11Layout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel3))
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(1, 1, 1)
                                .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );

        jPanel12.setBackground(new java.awt.Color(0, 102, 102));

        jButton2.setBackground(new java.awt.Color(0, 153, 0));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Clear");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 0, 0));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("ADD");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );

        jPanel6.setBackground(new java.awt.Color(0, 102, 102));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Date");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Invoice No/Ref.No:");

        parchaseText.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        parchaseText.setForeground(new java.awt.Color(255, 255, 255));
        parchaseText.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        parchasedate.setDateFormatString("yyyy-MM-dd ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Purchase No");

        remarktext.setColumns(20);
        remarktext.setRows(5);
        remarktext.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jScrollPane2.setViewportView(remarktext);

        supliyerbox.setBackground(new java.awt.Color(255, 255, 255));
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

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Suppliyer");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(3, 3, 3)
                .addComponent(parchaseText, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(parchasedate, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(parchaseText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(parchasedate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        here.setBackground(new java.awt.Color(0, 102, 102));

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        totalrate.setBackground(new java.awt.Color(255, 255, 255));
        totalrate.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalrate.setForeground(new java.awt.Color(255, 255, 255));
        totalrate.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Amount");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(0, 102, 102));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        svbtn.setBackground(new java.awt.Color(0, 102, 51));
        svbtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(svbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout hereLayout = new javax.swing.GroupLayout(here);
        here.setLayout(hereLayout);
        hereLayout.setHorizontalGroup(
            hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hereLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        hereLayout.setVerticalGroup(
            hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hereLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        dataTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        dataTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Item Code", "Item Name", "Batch", "Exp Date", "Box Size", "TP", "MRP", "Qty", "Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTable.setRowHeight(30);
        dataTable.setShowHorizontalLines(true);
        dataTable.setShowVerticalLines(true);
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
            dataTable.getColumnModel().getColumn(0).setPreferredWidth(20);
            dataTable.getColumnModel().getColumn(1).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(200);
            dataTable.getColumnModel().getColumn(3).setResizable(false);
            dataTable.getColumnModel().getColumn(4).setResizable(false);
            dataTable.getColumnModel().getColumn(5).setResizable(false);
            dataTable.getColumnModel().getColumn(6).setResizable(false);
            dataTable.getColumnModel().getColumn(7).setResizable(false);
            dataTable.getColumnModel().getColumn(8).setResizable(false);
            dataTable.getColumnModel().getColumn(9).setResizable(false);
        }

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1528, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE))
        );

        jScrollPane3.setViewportView(jPanel13);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(here, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3)
                .addGap(0, 0, 0)
                .addComponent(here, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible

        final JTextField productname = (JTextField) itemnamesearch.getEditor().getEditorComponent();
        String SearchText = productname.getText();

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
                    + "from item it where it.itemName='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                Itemcode = rs.getString("itemcode");
                String genericname = rs.getString("genericname");
                genericbox.setSelectedItem(genericname);

                /* String fromname = rs.getString("fromname");
                dosbox.setSelectedItem(fromname);*/
                String strangth = rs.getString("strangth");
                strengthtext.setText(strangth);
                String boxSize = rs.getString("boxSize");
                boxsizetext.setText(boxSize);
                String expdates = rs.getString("expdate");
                expdate.setText(expdates);

                //  expdatetext.setText(expdate);
                String unitname = rs.getString("unitname");
                unibox.setSelectedItem(unitname);
                String batch = rs.getString("batch");
                batchtext.setText(batch);

                String tpprice = rs.getString("tp");
                unitrateText.setText(tpprice);
                String pvat = rs.getString("pvat");

                String marpprice = rs.getString("mrp");
                mrptext.setText(marpprice);
                String profite = rs.getString("profite");
                //   profitetext.setText(profite);
                String profiteParcentage = rs.getString("profiteParcentage");
                //profiteparcentagetext.setText(profiteParcentage);
                float vat = rs.getFloat("pvat");
                float tp = rs.getFloat("tp");

                stocktext.setText(GetStrock(Itemcode));
                qtytext.requestFocusInWindow();
            } else {
                clear();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible
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
    private void itemnamesearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyTyped

    }//GEN-LAST:event_itemnamesearchKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        finelEntry();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clear();
        updatekey = 0;

        itemnamesearch.enable(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int selectedRowIndex = dataTable.getSelectedRow();

        updatekey = 1;
        Itemcode = (model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        batchtext.setText(model.getValueAt(selectedRowIndex, 3).toString());
        expdate.setText(model.getValueAt(selectedRowIndex, 4).toString());
        boxsizetext.setText((model.getValueAt(selectedRowIndex, 5).toString()));
        unitrateText.setText(model.getValueAt(selectedRowIndex, 6).toString());
        mrptext.setText(model.getValueAt(selectedRowIndex, 7).toString());
        ///   discounttext.setText(model.getValueAt(selectedRowIndex, 8).toString());
        //  pvattext.setText(model.getValueAt(selectedRowIndex, 9).toString());
        float tp = Float.parseFloat((model.getValueAt(selectedRowIndex, 6).toString()));
        // float vat = Float.parseFloat((model.getValueAt(selectedRowIndex, 9).toString()));
        //  float vatper = vatPercentage(vat, tp);
        // pvatpertext.setText(df2.format(vatper));
        //  float totalvat = tp + vat;
        //  ptotaltptext.setText(df2.format(totalvat));
        qtytext.setText(model.getValueAt(selectedRowIndex, 8).toString());
        //  bonusqtytext.setText(model.getValueAt(selectedRowIndex, 11).toString());
        totalamounttext.setText(model.getValueAt(selectedRowIndex, 9).toString());
        //  totaldiscounttext.setText(model.getValueAt(selectedRowIndex, 13).toString());
        //  altotalvattext.setText(model.getValueAt(selectedRowIndex, 14).toString());
        //   netotaltext.setText(model.getValueAt(selectedRowIndex, 15).toString());
        stocktext.setText(GetStrock(Itemcode));
        //  unibox.setSelectedItem(model.getValueAt(selectedRowIndex, 6).toString());
        qtytext.setBackground(Color.YELLOW);
        try {
            String sql = "Select "
                    + "(Select generic_name from generic sinfo where sinfo.generic_id=it.generic_id) as 'genericname',"
                    + "strangth"
                    + " from item it where it.Itemcode='" + Itemcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String genericname = rs.getString("genericname");
                genericbox.setSelectedItem(genericname);
                String strangth = rs.getString("strangth");
                strengthtext.setText(strangth);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }//GEN-LAST:event_dataTableMouseClicked
    private void Minusamount() {
        try {
            String sql = "Select nettotal as 'minusamount'  from purchase where purchaseCode='" + parchaseText.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                minusAmount = rs.getFloat("minusamount");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void selectsupliyer() {
        try {

            String sql = "Select  supliyer from purchase where purchaseCode='" + parchaseText.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                suplyer = rs.getString("supliyer");
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void suplyerinfo() {
        try {

            String sql = "Select consighnmentamnt,Balancedue from suplyierInfo where id='" + suplyer + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                consighnmentamnt = rs.getDouble("consighnmentamnt");
                Balancedue = rs.getDouble("Balancedue");
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void UpdateMunusSupplier() {
        selectsupliyer();
        suplyerinfo();
        try {
            double preamt = consighnmentamnt;
            double presntamt = minusAmount;
            double prentconsigntotal = preamt - presntamt;
            double afterblncedue = Balancedue - presntamt;

            String sql = "Update suplyierInfo set consighnmentamnt='" + prentconsigntotal + "',Balancedue='" + afterblncedue + "' where id='" + suplyer + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void StockloadMinus() {

        try {
            String sql1 = "Select st.Qty as 'stockqty' from stock st where st.procode=(Select pc.prcode from purchasedetails pc where pc.prcode=st.procode AND pc.purchaseCode='" + parchaseText.getText() + "' )";
            pst = conn.prepareStatement(sql1);

            rs = pst.executeQuery();

            while (rs.next()) {
                Float updateqty = rs.getFloat("stockqty");
                //String sql1 = "Select prcode,qty,unit , count(id)  from purchasedetails pc where pc.purchaseCode='" + parchasetext.getText() + "'.Update  stock st set st.Qty=pc.qty where st.procode=pc.prcode";
                String sql = "Update stock sta set sta.Qty=(Select (pc.qty+ps.bonusqty)-'" + updateqty + "'  from purchasedetails pc where pc.prcode=sta.procode AND pc.purchaseCode='" + parchaseText.getText() + "') where (select pc.prcode from purchasedetails pc where pc.prcode=sta.procode AND pc.purchaseCode='" + parchaseText.getText() + "')=sta.procode";
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
            String sql1 = "Select st.Qty as 'stockqty' from stock st where st.procode=(Select pc.prcode from purchasedetails pc where pc.prcode=st.procode AND pc.purchaseCode='" + parchaseText.getText() + "' )";
            pst = conn.prepareStatement(sql1);

            rs = pst.executeQuery();

            while (rs.next()) {
                Float updateqty = rs.getFloat("stockqty");
                //String sql1 = "Select prcode,qty,unit , count(id)  from purchasedetails pc where pc.purchaseCode='" + parchasetext.getText() + "'.Update  stock st set st.Qty=pc.qty where st.procode=pc.prcode";
                String sql = "Update stock sta set sta.Qty=(Select (pc.qty+ps.bonusqty)-'" + updateqty + "'  from purchasedetails pc where pc.prcode=sta.procode AND pc.purchaseCode='" + parchaseText.getText() + "') where (select pc.prcode from purchasedetails pc where pc.prcode=sta.procode AND pc.purchaseCode='" + parchaseText.getText() + "')=sta.procode";
                //  String sql="update stock sta set Qty=(select sum(pc.qty+(select st.Qty from stock st where st.procode=pc.prcode))from purchasedetails pc where sta.procode=pc.prcode AND pc.purchaseCode='"+parchasetext.getText()+"')";

                pst = conn.prepareStatement(sql);
                pst.execute();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void UpdatePlusSupplier() {
        selectsupliyer();
        suplyerinfo();
        try {
            double preamt = consighnmentamnt;
            double presntamt = minusAmount;
            double prentconsigntotal = preamt + presntamt;
            double afterblncedue = Balancedue + presntamt;

            String sql = "Update suplyierInfo set consighnmentamnt='" + prentconsigntotal + "',Balancedue='" + afterblncedue + "' where id='" + suplyer + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            // JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void loadPurchaseDetails() throws SQLException, IOException {
        String table_click = parchaseText.getText();
        PurchaseDetails filte = null;

        filte = new PurchaseDetails(table_click);

        filte.setVisible(true);
        this.getDesktopPane().add(filte);

        //Homedesktop.add(It);
        filte.setVisible(true);

        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
        this.dispose();

    }

    private void purchasenewInsert() {

        parchase_code();
        parchaseInsert();
        try {
            parcheDetailsInsert();
            loadGRN();

        } catch (SQLException | IOException ex) {
            Logger.getLogger(PurchaseEntrys.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void purchaseVoidupdate() {

        purchasedeletehistory();
        purchaseupdate();
        try {
            parcheDetailsInsert();
            //reload();

        } catch (SQLException | IOException ex) {
            Logger.getLogger(PurchaseEntrys.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        try {
            loadPurchaseDetails();

        } catch (SQLException | IOException ex) {
            Logger.getLogger(PurchaseEntrys.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void purchaseModification() {

        Minusamount();
        UpdateMunusSupplier();
        StockloadMinus();
        purchasedeletehistory();
        // parchaseInsert();
        purchaseupdate();
        try {
            parcheDetailsInsert();
            //reload();

        } catch (SQLException | IOException ex) {
            Logger.getLogger(PurchaseEntrys.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        Minusamount();
        UpdatePlusSupplier();
        StockloadPlus();
        try {
            loadGRN();

        } catch (SQLException | IOException ex) {
            Logger.getLogger(PurchaseEntrys.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();

        if (supliyerbox.getSelectedIndex() == 0 || totalrate.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Execute Faild, Due to Data Box Empty");
        } else {
            if (view == 0) {
                purchasenewInsert();

            } else {
                if (modifiaction == 0) {
                    purchaseVoidupdate();
                } else {

                    purchaseModification();

                }

            }

        }


    }//GEN-LAST:event_svbtnActionPerformed

    private void qtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            finelEntry();

            qtytext.setBackground(Color.WHITE);

        }
    }//GEN-LAST:event_qtytextKeyPressed

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
        deleterow();
    }//GEN-LAST:event_dataTableKeyPressed

    private void supliyerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supliyerboxPopupMenuWillBecomeInvisible
        try {
            String sql = "Select id from suplyierInfo where supliyername='" + supliyerbox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                supid = rs.getInt("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_supliyerboxPopupMenuWillBecomeInvisible

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        if (modifiaction == 0) {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from purchase where purchaseCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, parchaseText.getText());

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                purchasedeletehistory();
                PurchaseEntrys filte = null;

                try {
                    filte = new PurchaseEntrys();

                } catch (SQLException ex) {
                    Logger.getLogger(ItemList.class
                            .getName()).log(Level.SEVERE, null, ex);

                } catch (IOException ex) {
                    Logger.getLogger(PurchaseEntrys.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                filte.setVisible(true);
                this.getDesktopPane().add(filte);

                //Homedesktop.add(It);
                filte.setVisible(true);

                Dimension desktopSize = getDesktopPane().getSize();
                Dimension jInternalFrameSize = filte.getSize();
                filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                        (desktopSize.height - jInternalFrameSize.height) / 2);
                filte.moveToFront();
                this.dispose();

            }

        } else {

//delete Grn
            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Grn Information", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                Minusamount();
                UpdateMunusSupplier();
                StockloadMinus();
                try {
                    String sql = "Delete from purchase where purchaseCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, parchaseText.getText());

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                purchasedeletehistory();

                try {
                    String sql = "Delete from grninfo where purchaseCode=?";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, parchaseText.getText());
                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                GRNframe filte = null;

                try {
                    filte = new GRNframe();

                } catch (SQLException ex) {
                    Logger.getLogger(ItemList.class
                            .getName()).log(Level.SEVERE, null, ex);

                } catch (IOException ex) {
                    Logger.getLogger(PurchaseEntrys.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                filte.setVisible(true);
                this.getDesktopPane().add(filte);

                //Homedesktop.add(It);
                filte.setVisible(true);

                Dimension desktopSize = getDesktopPane().getSize();
                Dimension jInternalFrameSize = filte.getSize();
                filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                        (desktopSize.height - jInternalFrameSize.height) / 2);
                filte.moveToFront();
                this.dispose();

            }

        }
    }//GEN-LAST:event_deletebtnActionPerformed

    private void unitrateTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unitrateTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_unitrateTextActionPerformed

    private void genericboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_genericboxPopupMenuWillBecomeInvisible
        // TODO add your handling code here:
    }//GEN-LAST:event_genericboxPopupMenuWillBecomeInvisible

    private void genericboxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_genericboxKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_genericboxKeyTyped

    private void boxsizetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_boxsizetextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxsizetextKeyPressed

    private void unitrateTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateTextKeyReleased
        //SetVatValueWithDiscount();
        TotalAction();
    }//GEN-LAST:event_unitrateTextKeyReleased

    private void qtytextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyReleased
        TotalAction();
    }//GEN-LAST:event_qtytextKeyReleased

    private void unitrateTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateTextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            finelEntry();

            qtytext.setBackground(Color.WHITE);

        }
    }//GEN-LAST:event_unitrateTextKeyPressed

    private void mrptextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrptextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            finelEntry();

            qtytext.setBackground(Color.WHITE);

        }
    }//GEN-LAST:event_mrptextKeyPressed

    private void stocktextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stocktextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_stocktextKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField batchtext;
    private javax.swing.JTextField boxsizetext;
    private javax.swing.JTable dataTable;
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField expdate;
    private javax.swing.JComboBox<String> genericbox;
    private javax.swing.JPanel here;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
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
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField mrptext;
    private javax.swing.JLabel parchaseText;
    private com.toedter.calendar.JDateChooser parchasedate;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JTextField stocktext;
    private javax.swing.JTextField strengthtext;
    private javax.swing.JComboBox<String> supliyerbox;
    private javax.swing.JButton svbtn;
    private javax.swing.JTextField totalamounttext;
    private javax.swing.JLabel totalrate;
    private javax.swing.JComboBox<String> unibox;
    private javax.swing.JTextField unitrateText;
    // End of variables declaration//GEN-END:variables
}
