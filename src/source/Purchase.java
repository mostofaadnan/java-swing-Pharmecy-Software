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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class Purchase extends javax.swing.JInternalFrame {

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
    public Purchase() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        AutoCompleteDecorator.decorate(unibox);
        unit();
        parchase_code();
        currentDate();
        accsessModification();
        Deletebtnactivate();
        jCheckBox1.setSelected(false);
        ExtractFilter();
        ExtractFilterSupplier();
        itemnamesearch.requestFocusInWindow();
    }

    public Purchase(String table_click, int activemodification) throws SQLException, IOException {
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
        // suppliyer();
        accsessModification();

        updatedeletekey = 1;
        Deletebtnactivate();
        jCheckBox1.setSelected(false);
        ExtractFilter();
        ExtractFilterSupplier();
        itemnamesearch.requestFocusInWindow();
    }

    private void ExtractFilterSupplier() {
        final JTextField textfield = (JTextField) supliyerbox.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        supid = 0;
                    } else {
                        comboFilterSupplier(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFilterSupplier(String enteredText) {
        supliyerbox.removeAllItems();
        //  itemnamesearch.addItem("");
        supliyerbox.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;
        try {

            String sql = "Select supliyername from suplyierinfo WHERE lower(supliyername)  LIKE '" + enteredText + "%' ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("supliyername");
                filterArray.add(str1);
                supliyerbox.addItem(str1);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
        if (filterArray.size() > 0) {

            supliyerbox.setSelectedItem(enteredText);
            supliyerbox.showPopup();

        } else {
            supliyerbox.hidePopup();
        }
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

                        discounttext.setText(null);
                        pvattext.setText(null);
                        pvatpertext.setText(null);
                        ptotaltptext.setText(null);
                        qtytext.setText(null);
                        bonusqtytext.setText(null);
                        unibox.setSelectedIndex(0);
                        altotalvattext.setText(null);
                        netotaltext.setText(null);
                        stocktext.setText(null);
                        expdate.setText(null);
                        totalamounttext.setText(null);
                        totaldiscounttext.setText(null);

                    } else {
                        comboFilter(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFilter(String enteredText) {
        itemnamesearch.removeAllItems();
        itemnamesearch.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;
        try {
            String sql;
            if (jCheckBox1.isSelected() == true) {
                sql = "Select nameformate from item WHERE lower(nameformate)  LIKE N'" + enteredText + "%' AND company_id='" + supid + "' GROUP BY nameformate ORDER BY itemName ASC";
            } else {
                sql = "Select nameformate from item WHERE lower(nameformate)  LIKE N'" + enteredText + "%' GROUP BY nameformate ORDER BY itemName ASC";
            }

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

            genericbox.removeAllItems();
            strengthtext.setText(null);
            batchtext.setText(null);
            boxsizetext.setText(null);
            unitrateText.setText(null);

            discounttext.setText(null);
            pvattext.setText(null);
            pvatpertext.setText(null);
            ptotaltptext.setText(null);
            qtytext.setText(null);
            bonusqtytext.setText(null);

            unibox.setSelectedIndex(0);
            totalamounttext.setText(null);
            totaldiscounttext.setText(null);
            altotalvattext.setText(null);
            netotaltext.setText(null);
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
                    + "nameformate as 'Itemname',"
                    + "batch,"
                    + "expdate,"
                    + "boxsize,"
                    + "unitrate,"
                    + "mrp,"
                    + "discount,"
                    + "vat,"
                    + "qty,"
                    + "bonusqty,"
                    + "unit,"
                    + "total,"
                    + "totalvat,"
                    + "totaldiscount,"
                    + "nettotal"
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
                float bonusqty = rs.getFloat("bonusqty");
                // String unit = rs.getString("unit");
                float total = rs.getFloat("total");
                float discount = rs.getFloat("discount");
                float vat = rs.getFloat("vat");
                float totaldiscount = rs.getFloat("totaldiscount");
                float totalvat = rs.getFloat("totalvat");
                float nettotal = rs.getFloat("nettotal");
                model2.addRow(new Object[]{tree, id, itemname, batch, expdates, boxsize, unitrate, mrp, discount, vat, qty, bonusqty, total, totaldiscount, totalvat, nettotal});

            }
            //  dataTable.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {

            String sql = "Select purchaseCode,Totalrate,totalvat,nettotal,supliyer,pdate,remark,(select sup.supliyername from suplyierinfo sup where sup.id=par.supliyer) as'supliyerinfo'  from purchase par  where par.purchaseCode='" + table_click + "'";
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
        Total();
    }

    private void deleterow() {

        int[] toDelete = dataTable.getSelectedRows();
        Arrays.sort(toDelete); // be shure to have them in ascending order.
        DefaultTableModel myTableModel = (DefaultTableModel) dataTable.getModel();
        for (int ii = toDelete.length - 1; ii >= 0; ii--) {
            myTableModel.removeRow(toDelete[ii]); // beginning at the largest.
        }
        Total();
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
                    + "totalvat,"
                    + "nettotal,"
                    + "totaldiscount,"
                    + "remark,"
                    + "status,"
                    + "GRNstatus,"
                    + "inputuser,"
                    + "rounding,"
                    + "payble,itemupdate) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, parchaseText.getText());
            pst.setString(2, ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText());
            pst.setInt(3, supid);
            pst.setString(4, totalrate.getText());
            pst.setString(5, totalvattext.getText());
            pst.setString(6, nettotaltext.getText());
            pst.setString(7, netdiscounttext.getText());
            pst.setString(8, remarktext.getText());
            pst.setString(9, "1");
            pst.setInt(10, 0);
            pst.setInt(11, userid);
            pst.setString(12, roundingtext.getText());
            pst.setString(13, netpaybletext.getText());
            pst.setInt(14, 0);
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
                String procode = (String) dataTable.getValueAt(row, 1);
                String nameformate = (String) dataTable.getValueAt(row, 2);
                String batch = (String) dataTable.getValueAt(row, 3);
                String expdates = (String) dataTable.getValueAt(row, 4);
                //  String exd = expdates.toString();
                String boxsize = (String) dataTable.getValueAt(row, 5);
                float tp = (Float) dataTable.getValueAt(row, 6);
                float mrp = (Float) dataTable.getValueAt(row, 7);
                float discount = (Float) dataTable.getValueAt(row, 8);
                float vat = (Float) dataTable.getValueAt(row, 9);
                float qty = (Float) dataTable.getValueAt(row, 10);
                float bonusqty = (Float) dataTable.getValueAt(row, 11);
                tpdiscount = tp - discount;
                tpvate = tp + vat;
                profit = mrp - tp;
                profitper = ((profit * tp) / 100);
                float total = (Float) dataTable.getValueAt(row, 12);
                float totaldiscount = (Float) dataTable.getValueAt(row, 13);

                float totalvat = (Float) dataTable.getValueAt(row, 14);
                float nettotal = (Float) dataTable.getValueAt(row, 15);
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
                            + "discount,"
                            + "vat,"
                            + "tpvat,"
                            + "tpdis,"
                            + "qty,"
                            + "bonusqty,"
                            + "unit,"
                            + "total,"
                            + "totaldiscount,"
                            + "totalvat,"
                            + "nettotal,"
                            + "supid,"
                            + "profit,"
                            + "profitper,"
                            + "nameformate"
                            + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, parchaseText.getText());
                    pst.setString(2, procode);
                    pst.setString(3, procode);
                    pst.setString(4, batch);
                    pst.setString(5, expdates);
                    pst.setString(6, boxsize);
                    pst.setFloat(7, tp);
                    pst.setFloat(8, mrp);
                    pst.setFloat(9, discount);
                    pst.setFloat(10, vat);
                    pst.setFloat(11, tpvate);
                    pst.setFloat(12, tpdiscount);
                    pst.setFloat(13, qty);
                    pst.setFloat(14, bonusqty);
                    pst.setString(15, "");
                    pst.setFloat(16, total);
                    pst.setFloat(17, totaldiscount);
                    pst.setFloat(18, totalvat);
                    pst.setFloat(19, nettotal);
                    pst.setInt(20, supid);
                    pst.setFloat(21, profit);
                    pst.setFloat(22, profitper);
                    pst.setString(23, nameformate);
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
            int itemupdate = 0;
            int grnstatus = 0;
            String sql = "Update purchase set "
                    + "pdate='" + date + "',"
                    + "supliyer='" + supid + "',"
                    + "Totalrate='" + totalrate.getText() + "',"
                    + "totalvat='" + totalvattext.getText() + "',"
                    + "nettotal='" + nettotaltext.getText() + "',"
                    + "totaldiscount='" + netdiscounttext.getText() + "',"
                    + "payble='" + roundingtext.getText() + "',"
                    + "payble='" + netpaybletext.getText() + "',"
                    + "itemupdate='" + itemupdate + "',"
                    + "GRNstatus='" + grnstatus + "',"
                    + "inputuser='" + userid + "' where purchaseCode='" + parchaseText.getText() + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();
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

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 12).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_discount() {

        int rowaCount = dataTable.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
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

    private void clear() {
        itemnamesearch.removeAllItems();
        genericbox.removeAllItems();
        strengthtext.setText(null);
        batchtext.setText(null);
        boxsizetext.setText(null);
        unitrateText.setText(null);
        mrptext.setText(null);
        discounttext.setText(null);
        pvattext.setText(null);
        pvatpertext.setText(null);
        ptotaltptext.setText(null);
        qtytext.setText(null);
        bonusqtytext.setText(null);
        //  unibox.setSelectedIndex(0);
        totalamounttext.setText(null);
        totaldiscounttext.setText(null);
        altotalvattext.setText(null);
        netotaltext.setText(null);
        stocktext.setText(null);
        expdate.setText(null);
        mrptext.setText(null);
        itemnamesearch.requestFocusInWindow();
    }

    private void checkentry() {

        String s = "";
        boolean exists = false;
        //DefaultTableModel myTableModel = (DefaultTableModel) dataTable.getModel();
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            s = dataTable.getValueAt(i, 1).toString().trim();
            float tpd = Float.parseFloat(unitrateText.getText());
            float qtytbl = (Float) dataTable.getValueAt(i, 9);
            float applyqty = Float.parseFloat(qtytext.getText());
            float totalqty = qtytbl + applyqty;
            float bonusqty = 0;
            float discount = 0;
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
            float totalamount = totalqty * tpd;
            float totaldiscounts = totalqty * discount;
            float allTotalVat = vat * totalqty;
            float NetallTotal = totalamount + allTotalVat;
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
                dataTable.setValueAt(discount, i, 8);
                dataTable.setValueAt(vat, i, 9);
                dataTable.setValueAt(totalqty, i, 10);
                dataTable.setValueAt(bonusqty, i, 11);
                dataTable.setValueAt(totalamount, i, 12);
                dataTable.setValueAt(totaldiscounts, i, 13);
                dataTable.setValueAt(allTotalVat, i, 14);
                dataTable.setValueAt(NetallTotal, i, 15);
                Total();
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
        if (bonusqtytext.getText().isEmpty()) {
            bonusqty = 0;
        } else {
            bonusqty = Float.parseFloat(bonusqtytext.getText());
        }
        float tpd = Float.parseFloat(unitrateText.getText());
        float mrp = Float.parseFloat(mrptext.getText());
        if (discounttext.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Float.parseFloat(discounttext.getText());

        }
        float vat = Float.parseFloat(pvattext.getText());
        float qty = Float.parseFloat(qtytext.getText());
        float totalamount = Float.parseFloat(totalamounttext.getText());
        float totaldiscount = Float.parseFloat(totaldiscounttext.getText());
        float allTotalVat = Float.parseFloat(altotalvattext.getText());
        float NetallTotal = Float.parseFloat(netotaltext.getText());
        model2.addRow(new Object[]{tree, Itemcode, itemnamesearch.getSelectedItem(), batchtext.getText(), expdate.getText(), boxsizetext.getText(), tpd, mrp, discount, vat, qty, bonusqty, totalamount, totaldiscount, allTotalVat, NetallTotal});
        Total();
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
                String batch = batchtext.getText();
                String expdates = expdate.getText();
                String boxsize = boxsizetext.getText();
                float discount;
                float bonusqty = 0;
                if (bonusqtytext.getText().isEmpty()) {
                    bonusqty = 0;
                } else {
                    bonusqty = Float.parseFloat(bonusqtytext.getText());
                }
                float tpd = Float.parseFloat(unitrateText.getText());
                float mrp = Float.parseFloat(mrptext.getText());
                if (discounttext.getText().isEmpty()) {
                    discount = 0;
                } else {
                    discount = Float.parseFloat(discounttext.getText());

                }
                float vat = Float.parseFloat(pvattext.getText());
                float qty = Float.parseFloat(qtytext.getText());
                float totalamount = Float.parseFloat(totalamounttext.getText());
                float totaldiscount = Float.parseFloat(totaldiscounttext.getText());
                float allTotalVat = Float.parseFloat(altotalvattext.getText());
                float NetallTotal = Float.parseFloat(netotaltext.getText());
                dataTable.setValueAt(batch, i, 3);
                dataTable.setValueAt(expdates, i, 4);
                dataTable.setValueAt(boxsize, i, 5);
                dataTable.setValueAt(tpd, i, 6);
                dataTable.setValueAt(mrp, i, 7);
                dataTable.setValueAt(discount, i, 8);
                dataTable.setValueAt(vat, i, 9);
                dataTable.setValueAt(qty, i, 10);
                dataTable.setValueAt(bonusqty, i, 11);
                dataTable.setValueAt(totalamount, i, 12);
                dataTable.setValueAt(totaldiscount, i, 13);
                dataTable.setValueAt(allTotalVat, i, 14);
                dataTable.setValueAt(NetallTotal, i, 15);
                Total();
                clear();
                updatekey = 0;
                Itemcode = null;

            }

        }

    }

    private void Total() {
        float amount = total_action_mrp();
        totalrate.setText(df2.format(amount));

        float totaldiscounts = total_action_discount();
        netdiscounttext.setText(df2.format(totaldiscounts));

        float totalvats = total_action_vat();
        totalvattext.setText(df2.format(totalvats));

        float netalltotals = total_action_nettotal();
        nettotaltext.setText(df2.format(netalltotals));
        NetPayble(netalltotals);
    }

    private void NetPayble(float doubleNumber) {

        String doubleAsString = String.valueOf(doubleNumber);
        int indexOfDecimal = doubleAsString.indexOf(".");
        String netpaybel = doubleAsString.substring(0, indexOfDecimal);
        String rounding = doubleAsString.substring(indexOfDecimal);
        netpaybletext.setText(netpaybel);
        roundingtext.setText(rounding);

    }

    private void reload() throws SQLException {

        Itemcode = null;
        itemnamesearch.setSelectedIndex(0);
        unitrateText.setText(null);
        qtytext.setText(null);
        unibox.setSelectedIndex(0);
        totalrate.setText(null);
        discounttext.setText(null);
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
        float mrp = 0;
        float discount = 0;
        float vat = 0;
        float tpvat = 0;
        float tpwithdiscount = 0;
        float qty = 0;
        float totaldiscount = 0;
        float totalvat = 0;
        float totalamount = 0;
        float nettotal = 0;
        if (unitrateText.getText().isEmpty()) {
            tp = 0;
        } else {
            tp = Float.parseFloat(unitrateText.getText());
        }
        if (mrptext.getText().isEmpty()) {
            mrp = 0;
        } else {
            mrp = Float.parseFloat(mrptext.getText());
        }
        if (discounttext.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Float.parseFloat(discounttext.getText());
        }
        if (pvattext.getText().isEmpty()) {
            vat = 0;
        } else {
            vat = Float.parseFloat(pvattext.getText());
        }

        if (qtytext.getText().isEmpty()) {
            qty = 0;
        } else {
            qty = Float.parseFloat(qtytext.getText());
        }

        tpwithdiscount = tp - discount;
        float vatper = vatPercentage(vat, tp);
        // pvatpertext.setText(String.valueOf(Math.round(vatper)));
        float vatafterdis = vatValue(vatper, tpwithdiscount);

        tpvat = tpwithdiscount + vatafterdis;
        ptotaltptext.setText(df2.format(tpvat));
        if (qty == 0) {
            totalamounttext.setText(null);
            totaldiscounttext.setText(null);
            altotalvattext.setText(null);
            netotaltext.setText(null);
        } else {
            totalamount = tp * qty;
            totalamounttext.setText(df2.format(totalamount));
            totaldiscount = discount * qty;
            totaldiscounttext.setText(df2.format(totaldiscount));
            totalvat = vat * qty;
            altotalvattext.setText(df2.format(totalvat));
            float amountwithdiscount = (totalamount - totaldiscount);
            nettotal = (amountwithdiscount + totalvat);
            netotaltext.setText(df2.format(nettotal));

        }

    }

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

    private float vatPercentage(float vat, float tp) {
        float per = (vat * 100) / tp;
        return per;
    }

    private float vatValue(float per, float tp) {

        //float vatper = ((tp / (100+per)) * per);
        float vatper = (per * tp / 100);
        return vatper;

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
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
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
        jButton3 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        itemnamesearch = new javax.swing.JComboBox<>();
        jPanel11 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        discounttext = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        mrptext = new javax.swing.JTextField();
        ptotaltptext = new javax.swing.JTextField();
        totaldiscounttext = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        netotaltext = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        altotalvattext = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        bonusqtytext = new javax.swing.JTextField();
        unitrateText = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        unibox = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        qtytext = new javax.swing.JTextField();
        pvattext = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        totalamounttext = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        pvatpertext = new javax.swing.JTextField();
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
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        netdiscounttext = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        totalvattext = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        nettotaltext = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        svbtn = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        netpaybletext = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        roundingtext = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
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
        genericbox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        genericbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        genericbox.setBorder(null);
        genericbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                genericboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                genericboxPopupMenuWillBecomeVisible(evt);
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
        jLabel24.setText("Pack Size");

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
        expdate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                expdateKeyPressed(evt);
            }
        });

        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jCheckBox1.setForeground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setText("Search By Company");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        itemnamesearch.setBackground(new java.awt.Color(255, 255, 255));
        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
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
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton3)))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(genericbox, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(strengthtext, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
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
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(strengthtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(batchtext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(expdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
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

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("TP+VAT");

        discounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        discounttext.setForeground(new java.awt.Color(0, 102, 0));
        discounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        discounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                discounttextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                discounttextKeyReleased(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Total Vat");

        mrptext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        mrptext.setForeground(new java.awt.Color(102, 0, 0));
        mrptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mrptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mrptextKeyPressed(evt);
            }
        });

        ptotaltptext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        ptotaltptext.setForeground(new java.awt.Color(0, 102, 0));
        ptotaltptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ptotaltptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                ptotaltptextKeyReleased(evt);
            }
        });

        totaldiscounttext.setEditable(false);
        totaldiscounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totaldiscounttext.setForeground(new java.awt.Color(0, 102, 0));
        totaldiscounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Qty");

        netotaltext.setEditable(false);
        netotaltext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        netotaltext.setForeground(new java.awt.Color(0, 102, 0));
        netotaltext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netotaltext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                netotaltextKeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Discount");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("TP");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Total Discount");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Net Total");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Vat Value");

        altotalvattext.setEditable(false);
        altotalvattext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        altotalvattext.setForeground(new java.awt.Color(0, 102, 0));
        altotalvattext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Unit ");

        bonusqtytext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        bonusqtytext.setForeground(new java.awt.Color(153, 0, 0));
        bonusqtytext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bonusqtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bonusqtytextKeyPressed(evt);
            }
        });

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

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Bonus Qty");

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

        pvattext.setEditable(false);
        pvattext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pvattext.setForeground(new java.awt.Color(0, 102, 0));
        pvattext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pvattext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pvattextKeyReleased(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Amount");

        totalamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalamounttext.setForeground(new java.awt.Color(0, 102, 0));
        totalamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalamounttext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                totalamounttextMouseClicked(evt);
            }
        });
        totalamounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                totalamounttextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                totalamounttextKeyReleased(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Vat%");

        pvatpertext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pvatpertext.setForeground(new java.awt.Color(0, 102, 0));
        pvatpertext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        pvatpertext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pvatpertextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pvatpertextKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                    .addComponent(pvatpertext))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ptotaltptext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bonusqtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(totaldiscounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(altotalvattext)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(netotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pvatpertext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel11Layout.createSequentialGroup()
                            .addComponent(jLabel29)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(totalamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ptotaltptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(qtytext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel11Layout.createSequentialGroup()
                                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel11Layout.createSequentialGroup()
                                                    .addGap(1, 1, 1)
                                                    .addComponent(jLabel14)
                                                    .addGap(2, 2, 2))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                                    .addComponent(jLabel18)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                            .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(pvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jLabel3))
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                    .addComponent(jLabel23)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(bonusqtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(altotalvattext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addGap(1, 1, 1)
                                    .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel25)
                                        .addComponent(jLabel27))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(totaldiscounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                                    .addComponent(jLabel28)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(netotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))))
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
        jLabel13.setText("Company");

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
                    .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jPanel7.setBackground(new java.awt.Color(0, 102, 102));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Discount");

        netdiscounttext.setBackground(new java.awt.Color(0, 102, 102));
        netdiscounttext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        netdiscounttext.setForeground(new java.awt.Color(255, 255, 255));
        netdiscounttext.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(netdiscounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(netdiscounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(0, 102, 102));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        totalvattext.setBackground(new java.awt.Color(255, 255, 255));
        totalvattext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        totalvattext.setForeground(new java.awt.Color(255, 255, 255));
        totalvattext.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Total Vat");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(0, 102, 102));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        nettotaltext.setBackground(new java.awt.Color(255, 255, 255));
        nettotaltext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        nettotaltext.setForeground(new java.awt.Color(255, 255, 255));
        nettotaltext.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("NetTotal");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(nettotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nettotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(deletebtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel14.setBackground(new java.awt.Color(0, 102, 102));
        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        netpaybletext.setBackground(new java.awt.Color(255, 255, 255));
        netpaybletext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        netpaybletext.setForeground(new java.awt.Color(255, 255, 255));
        netpaybletext.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Net Payble");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32)
                    .addComponent(netpaybletext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(netpaybletext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.setBackground(new java.awt.Color(0, 102, 102));
        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        roundingtext.setBackground(new java.awt.Color(255, 255, 255));
        roundingtext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        roundingtext.setForeground(new java.awt.Color(255, 255, 255));
        roundingtext.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Rounding");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel33)
                    .addComponent(roundingtext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(roundingtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout hereLayout = new javax.swing.GroupLayout(here);
        here.setLayout(hereLayout);
        hereLayout.setHorizontalGroup(
            hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hereLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        hereLayout.setVerticalGroup(
            hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hereLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        dataTable.setAutoCreateRowSorter(true);
        dataTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        dataTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Item Code", "Item Name", "Batch", "Exp Date", "Box Size", "TP", "MRP", "Discount", "Vat", "Qty", "Bonus Qty", "Amount", "Total Discount", "Total Vat", "Net Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTable.setRowHeight(25);
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
            dataTable.getColumnModel().getColumn(10).setResizable(false);
            dataTable.getColumnModel().getColumn(11).setResizable(false);
            dataTable.getColumnModel().getColumn(12).setResizable(false);
            dataTable.getColumnModel().getColumn(13).setResizable(false);
            dataTable.getColumnModel().getColumn(14).setResizable(false);
            dataTable.getColumnModel().getColumn(15).setResizable(false);
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
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
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

        setBounds(0, 0, 1379, 548);
    }// </editor-fold>//GEN-END:initComponents
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
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        float totalamt = 0;
       if (totalamounttext.getText().isEmpty() || totalamounttext.getText().matches("^[a-zA-Z]+$") || qtytext.getText().matches("^[a-zA-Z]+$") || qtytext.getText().isEmpty()) {
            totalamt = 0;
        } else {
            totalamt = Float.parseFloat(totalamounttext.getText());
        }
        if (totalamt > 0) {
            //   MakeTp();
            finelEntry();
            //qtytext.setBackground(Color.WHITE);
        }
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
        discounttext.setText(model.getValueAt(selectedRowIndex, 8).toString());
        pvattext.setText(model.getValueAt(selectedRowIndex, 9).toString());
        float tp = Float.parseFloat((model.getValueAt(selectedRowIndex, 6).toString()));
        float vat = Float.parseFloat((model.getValueAt(selectedRowIndex, 9).toString()));
        float vatper = vatPercentage(vat, tp);
        pvatpertext.setText(df2.format(vatper));
        float totalvat = tp + vat;
        ptotaltptext.setText(df2.format(totalvat));
        qtytext.setText(model.getValueAt(selectedRowIndex, 10).toString());
        bonusqtytext.setText(model.getValueAt(selectedRowIndex, 11).toString());
        totalamounttext.setText(model.getValueAt(selectedRowIndex, 12).toString());
        totaldiscounttext.setText(model.getValueAt(selectedRowIndex, 13).toString());
        altotalvattext.setText(model.getValueAt(selectedRowIndex, 14).toString());
        netotaltext.setText(model.getValueAt(selectedRowIndex, 15).toString());
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

    private void IsModificationaStockMinus() {
        try {

            int rows = dataTable.getRowCount();
            for (int row = 0; row < rows; row++) {
                String procode = (String) dataTable.getValueAt(row, 1);
                float qty = (Float) dataTable.getValueAt(row, 10);
              //  int is_minus = CheckDetails(procode, qty);
              //  if (is_minus == 1) {
                    float prevousQty = GetPurchasePreviusQty(procode);
                    StockloadMinus(procode, prevousQty);
               // }
            }

        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

    }

    private int CheckDetails(String Itemcode, float tableQty) {
        int is_update = 0;
        try {
            String sql = "Select qty from purchasedetails where prcode='" + Itemcode + "' AND purchaseCode='" + parchaseText.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                float qty = rs.getFloat("qty");
                if (qty == tableQty) {
                    is_update = 0;
                } else {
                    is_update = 1;
                }
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return is_update;

    }

    private float GetPurchasePreviusQty(String Itemcode) {
        float qty = 0;
        try {
            String sql = "Select qty from purchasedetails where prcode='" + Itemcode + "' AND purchaseCode='" + parchaseText.getText() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                qty = rs.getFloat("qty");
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return qty;
    }

    private void StockloadMinus(String Itemcode, float prevousQty) {

        try {
            String sql1 = "Select st.Qty as 'stockqty',st.itemcode from stockdetails st where st.itemcode ='" + Itemcode + "'";
            pst = conn.prepareStatement(sql1);
            rs = pst.executeQuery();
            while (rs.next()) {
                Float updateqty = rs.getFloat("stockqty");
                float totalqty = updateqty - prevousQty;
                String sql = "Update stockdetails sta set sta.Qty='" + totalqty + "' where sta.itemcode='" + Itemcode + "'";
                pst = conn.prepareStatement(sql);
                pst.execute();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void StockloadPlus() {
        try {
            String sql1 = "Select st.Qty as 'stockqty' from stockdetails st where st.itemcode =(Select pc.prcode from purchasedetails pc where pc.prcode=st.itemcode  AND pc.purchaseCode='" + parchaseText.getText() + "' )";
            pst = conn.prepareStatement(sql1);
            rs = pst.executeQuery();
            while (rs.next()) {
                Float updateqty = rs.getFloat("stockqty");
                String sql = "Update stockdetails sta set sta.Qty=(Select (pc.qty+pc.bonusqty)-'" + updateqty + "'  from purchasedetails pc where pc.prcode=sta.itemcode  AND pc.purchaseCode='" + parchaseText.getText() + "') where (select pc.prcode from purchasedetails pc where pc.prcode=sta.itemcode AND pc.purchaseCode='" + parchaseText.getText() + "')=sta.itemcode";
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
            Logger.getLogger(Purchase.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void purchaseVoidupdate() {

        purchasedeletehistory();
        purchaseupdate();
        try {
            parcheDetailsInsert();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Purchase.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        try {
            loadPurchaseDetails();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Purchase.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void purchaseModification() {
        Minusamount();
        UpdateMunusSupplier();
        IsModificationaStockMinus();
        purchasedeletehistory();
        purchaseupdate();
        try {
            String sql = "Delete from grninfo where purchaseCode=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, parchaseText.getText());
            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        try {
            parcheDetailsInsert();

        } catch (SQLException | IOException ex) {
            Logger.getLogger(Purchase.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        try {
            loadGRN();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Purchase.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        if (supid == 0 || totalrate.getText().isEmpty()) {
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

            qtytext.setBackground(Color.WHITE);
            totalamounttext.requestFocusInWindow();

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
                Purchase filte = null;
                try {
                    filte = new Purchase();

                } catch (SQLException ex) {
                    Logger.getLogger(ItemList.class
                            .getName()).log(Level.SEVERE, null, ex);

                } catch (IOException ex) {
                    Logger.getLogger(Purchase.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                filte.setVisible(true);
                this.getDesktopPane().add(filte);
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
                IsModificationaStockMinus();
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
                    Logger.getLogger(Purchase.class
                            .getName()).log(Level.SEVERE, null, ex);
                }

                filte.setVisible(true);
                this.getDesktopPane().add(filte);
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

    private void bonusqtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bonusqtytextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            finelEntry();

            qtytext.setBackground(Color.WHITE);

        }
    }//GEN-LAST:event_bonusqtytextKeyPressed

    private void boxsizetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_boxsizetextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_boxsizetextKeyPressed

    private void unitrateTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateTextKeyReleased
        // SetVatValueWithDiscount();
        //  TotalAction();
    }//GEN-LAST:event_unitrateTextKeyReleased

    private void discounttextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discounttextKeyReleased
        //  SetVatValueWithDiscount();
        //  TotalAction();
    }//GEN-LAST:event_discounttextKeyReleased

    private void pvattextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pvattextKeyReleased
//        TotalAction();
    }//GEN-LAST:event_pvattextKeyReleased

    private void qtytextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyReleased
        if (totalamounttext.getText().isEmpty() || totalamounttext.getText().matches("^[a-zA-Z]+$") || qtytext.getText().matches("^[a-zA-Z]+$") || qtytext.getText().isEmpty()) {
        } else {
            MakeTp();
        }
    }//GEN-LAST:event_qtytextKeyReleased

    private void pvatpertextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pvatpertextKeyReleased
        // SetPvat();
        // SetVatValueWithDiscount();
        //  TotalAction();
    }//GEN-LAST:event_pvatpertextKeyReleased

    private void unitrateTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unitrateTextKeyPressed

    }//GEN-LAST:event_unitrateTextKeyPressed

    private void mrptextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrptextKeyPressed

    }//GEN-LAST:event_mrptextKeyPressed

    private void discounttextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discounttextKeyPressed

    }//GEN-LAST:event_discounttextKeyPressed

    private void pvatpertextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pvatpertextKeyPressed

    }//GEN-LAST:event_pvatpertextKeyPressed

    private void stocktextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stocktextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_stocktextKeyPressed

    private void ptotaltptextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ptotaltptextKeyReleased
        //SetPvat();
        // TotalAction();
    }//GEN-LAST:event_ptotaltptextKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
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
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed
    private void MakeTp() {
        float nettotal = 0;
        float qty = 0;
        float vat = 0;
        float totalamount = 0;
        float totalvat = 0;
        float tpvat = 0;
        float tp = 0;
        float discount = 0;
        float totaldiscount = 0;
        float tpplusvat = 0;
        if (totalamounttext.getText().isEmpty() || totalamounttext.getText().matches("^[a-zA-Z]+$")) {
            totalamount = 0;
        } else if (qtytext.getText().isEmpty() || qtytext.getText().matches("^[a-zA-Z]+$")) {
            qty = 0;
        } else if (pvatpertext.getText().isEmpty() || pvatpertext.getText().matches("^[a-zA-Z]+$")) {
            vat = 0;
        } else if (discounttext.getText().isEmpty() || discounttext.getText().matches("^[a-zA-Z]+$")) {
            discount = 0;
        } else {
            totalamount = Float.parseFloat(totalamounttext.getText());
            qty = Float.parseFloat(qtytext.getText());
            vat = Float.parseFloat(pvatpertext.getText());
            discount = Float.parseFloat(discounttext.getText());
        }

        totalvat = (totalamount * vat) / 100;
        tpvat = totalvat / qty;
        totaldiscount = discount * qty;
        tp = (totalamount / qty);
        tpplusvat = tp + tpvat;
        nettotal = (totalamount + totalvat) - totaldiscount;
        totaldiscounttext.setText(df2.format(totaldiscount));
        altotalvattext.setText(df2.format(totalvat));
        ptotaltptext.setText(df2.format(tpplusvat));
        pvattext.setText(df2.format(tpvat));
        unitrateText.setText(df2.format(tp));
        netotaltext.setText(df2.format(nettotal));
    }
    private void netotaltextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_netotaltextKeyReleased

    }//GEN-LAST:event_netotaltextKeyReleased

    private void totalamounttextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalamounttextKeyReleased
       if (totalamounttext.getText().isEmpty() || totalamounttext.getText().matches("^[a-zA-Z]+$") || qtytext.getText().matches("^[a-zA-Z]+$") || qtytext.getText().isEmpty()) {

        } else {
            MakeTp();
        }

    }//GEN-LAST:event_totalamounttextKeyReleased

    private void totalamounttextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalamounttextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            float totalamt = 0;
            if (totalamounttext.getText().isEmpty() || totalamounttext.getText().matches("^[a-zA-Z]+$")) {
                totalamt = 0;
            } else {
                totalamt = Float.parseFloat(totalamounttext.getText());
            }
            if (totalamt > 0) {
                finelEntry();
                qtytext.setBackground(Color.WHITE);
            }
        }
    }//GEN-LAST:event_totalamounttextKeyPressed

    private void totalamounttextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_totalamounttextMouseClicked
        totalamounttext.setText(null);
    }//GEN-LAST:event_totalamounttextMouseClicked

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
                    + "from item it where it.nameformate='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                Itemcode = rs.getString("itemcode");
                String genericname = rs.getString("genericname");
                genericbox.setSelectedItem(genericname);
                String strangth = rs.getString("strangth");
                strengthtext.setText(strangth);
                String boxSize = rs.getString("boxSize");
                boxsizetext.setText(boxSize);
                String expdates = rs.getString("expdate");
                expdate.setText(expdates);
                String unitname = rs.getString("unitname");
                unibox.setSelectedItem(unitname);
                String batch = rs.getString("batch");
                batchtext.setText(batch);
                String tpprice = rs.getString("tp");
                unitrateText.setText(tpprice);
                String pvat = rs.getString("pvat");
                pvattext.setText(pvat);
                String pdiscount = rs.getString("pdiscount");
                discounttext.setText(pdiscount);
                String ptpwd = rs.getString("ptpwd");
                String ptpwv = rs.getString("ptpwv");
                ptotaltptext.setText(ptpwv);
                String marpprice = rs.getString("mrp");
                mrptext.setText(marpprice);
                float vat = rs.getFloat("pvat");
                float tp = rs.getFloat("tp");
                if (tp > 0) {
                    float vatper = (vat * 100) / tp;
                    pvatpertext.setText(df2.format(vatper));
                } else {
                    pvatpertext.setText("17.40");
                }
                stocktext.setText(GetStrock(Itemcode));
                expdate.requestFocusInWindow();
            } else {
                clear();
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void expdateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_expdateKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            // finelEntry();
            // qtytext.setBackground(Color.WHITE);
            qtytext.requestFocusInWindow();
            // totalamounttext.setText(null);
        }
    }//GEN-LAST:event_expdateKeyPressed

    private void genericboxPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_genericboxPopupMenuWillBecomeVisible
        // TODO add your handling code here:
    }//GEN-LAST:event_genericboxPopupMenuWillBecomeVisible


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField altotalvattext;
    private javax.swing.JTextField batchtext;
    private javax.swing.JTextField bonusqtytext;
    private javax.swing.JTextField boxsizetext;
    private javax.swing.JTable dataTable;
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField discounttext;
    private javax.swing.JTextField expdate;
    private javax.swing.JComboBox<String> genericbox;
    private javax.swing.JPanel here;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
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
    private javax.swing.JPanel jPanel15;
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
    private javax.swing.JTextField mrptext;
    private javax.swing.JTextField netdiscounttext;
    private javax.swing.JTextField netotaltext;
    private javax.swing.JLabel netpaybletext;
    private javax.swing.JLabel nettotaltext;
    private javax.swing.JLabel parchaseText;
    private com.toedter.calendar.JDateChooser parchasedate;
    private javax.swing.JTextField ptotaltptext;
    private javax.swing.JTextField pvatpertext;
    private javax.swing.JTextField pvattext;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JLabel roundingtext;
    private javax.swing.JTextField stocktext;
    private javax.swing.JTextField strengthtext;
    private javax.swing.JComboBox<String> supliyerbox;
    private javax.swing.JButton svbtn;
    private javax.swing.JTextField totalamounttext;
    private javax.swing.JTextField totaldiscounttext;
    private javax.swing.JLabel totalrate;
    private javax.swing.JLabel totalvattext;
    private javax.swing.JComboBox<String> unibox;
    private javax.swing.JTextField unitrateText;
    // End of variables declaration//GEN-END:variables
}
