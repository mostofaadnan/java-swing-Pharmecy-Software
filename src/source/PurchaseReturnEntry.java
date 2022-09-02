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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class PurchaseReturnEntry extends javax.swing.JInternalFrame {

    float bankbalance1;
    String grnNo = "";
    String prNo;
    int tree = 0;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int updatekey = 0;
    int supid = 0;

    float consighnmentamnt = 0;

    float paidamount = 0;
    float Balancedue = 0;

    float payment = 0;
    String prcode = null;
    String qty = null;

    float nettotal = 0;
    float due = 0;
    // Dashboard dashboard = new Dashboard();
    int userid = 0;
    int modificationaccsess = 0;

    float includevat;

    String itemcode;
    String unit;

    /**
     * Creates new form PurchaseReturnEntry
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public PurchaseReturnEntry() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();

        AutoCompleteDecorator.decorate(itemnamesearch);

        AutoCompleteDecorator.decorate(supliyerbox);

        Item();

        suppliyer();
        parchase_code();
        currentDate();

        modificationaccsess = 0;
        Deletebtnactivate();
        accsessModification();
        // purchaseNo();
    }

    public PurchaseReturnEntry(String table_Click) throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();

        AutoCompleteDecorator.decorate(itemnamesearch);

        AutoCompleteDecorator.decorate(supliyerbox);
        Item();

        suppliyer();
        parchase_code();
        currentDate();

        modificationaccsess = 1;
        parchaseText.setText(table_Click);
        PurchaseDetailsPR(table_Click);
        Deletebtnactivate();
        accsessModification();
        // purchaseNo();
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
        if (modificationaccsess == 1) {

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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private static final DecimalFormat df2 = new DecimalFormat("#.00");

    private void PurchaseDetailsPR(String table_click) throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();

        try {

            String sql = "Select pr.barcode as 'id',(select ita.itemName from item ita where ita.Itemcode=pr.prcode) as 'Itemname',unitrate, qty,total,discount,totalvate,nettotal from purchasereturndetails pr  where pr.purchaseReurnCode='" + table_click + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            while (rs.next()) {
                tree++;
                String id = rs.getString("id");
                String itemname = rs.getString("Itemname");
                float unitrate = rs.getFloat("unitrate");
                float qtyhere = rs.getFloat("qty");

                float total = rs.getFloat("total");
                float discount = rs.getFloat("discount");

                float totalvat = rs.getFloat("totalvate");
                float nettotalhere = rs.getFloat("nettotal");

                model2.addRow(new Object[]{tree, id, itemname, unitrate, qtyhere, discount, total, totalvat, nettotalhere});

            }
            //  dataTable.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {

            String sql = "Select purchaseReurnCode,grnno,(select purchaseCode from grninfo gn where gn.GRNCode=par.grnno ) as 'prchasecode', Totalrate,vat,nettotal,supliyer,pdate,remark,(select sup.supliyername from suplyierinfo sup where sup.id=par.supliyer) as'supliyerinfo'  from purchasereturn par  where par.purchaseReurnCode='" + table_click + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            if (rs.next()) {
                String grn = rs.getString("grnno");

                String TotalRate = rs.getString("Totalrate");
                totalrate.setText(TotalRate);
                String totalvat = rs.getString("vat");
                totalvattext.setText(totalvat);
                String nettotalhere = rs.getString("nettotal");
                nettotaltext.setText(nettotalhere);

                prNo = rs.getString("prchasecode");

                String ParchseNo = rs.getString("purchaseReurnCode");
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
        suplyerinfo();
        try {
            purchaseItem();
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseReturnEntry.class.getName()).log(Level.SEVERE, null, ex);
        }

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

        float totaldiscount = total_action_discount();
        totaldiscounttext.setText(df2.format(totaldiscount));

        float totalvats = total_action_vat();
        totalvattext.setText(df2.format(totalvats));

        float netalltotals = total_action_nettotal();
        nettotaltext.setText(df2.format(netalltotals));

        clear();
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        java.sql.Date date = new java.sql.Date(now.getTime());
        parchasedate.setDate(date);

    }

    private void purchaseItem() throws SQLException {
        try {
            String sql = "Select nameformat,barcode from barcodeproduct";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String item = rs.getString("nameformat");
                String barcode = rs.getString("barcode");
                itemnamesearch.addItem(barcode + "-" + item);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void suplyerinfo() {
        try {

            String sql = "Select  consighnmentamnt,paidamount,Balancedue from suplyierInfo where id='" + supid + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            if (rs.next()) {

                consighnmentamnt = rs.getFloat("consighnmentamnt");

                paidamount = rs.getFloat("paidamount");
                Balancedue = rs.getFloat("Balancedue");
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void supliyerupdate() {
        try {
            float presentpaid = Float.parseFloat(nettotaltext.getText());
            float totalpaid = 0;
            float remainbalancedue = 0;
            totalpaid = paidamount - presentpaid;
            double prentconsigntotal = consighnmentamnt - presentpaid;
            remainbalancedue = Balancedue - presentpaid;

            String sql = "Update suplyierInfo set paidamount='" + totalpaid + "',consighnmentamnt='" + prentconsigntotal + "',Balancedue='" + remainbalancedue + "' where id='" + supid + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void parchase_code() {
        try {
            int new_inv = 1;
            String NewParchaseCode = ("RE-" + new_inv + "");
            String sql = "Select id from purchaseReturn";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = ("RE-" + ParchaseCode + "");
            }

            parchaseText.setText(NewParchaseCode);
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void parchaseInsert() throws IOException {

        try {

            String sql = "Insert into purchaseReturn(grnno,purchaseReurnCode,pdate,supliyer,Totalrate,netdiscount,vat,nettotal,remark,inputuser) values (?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, grnnotext.getText());
            pst.setString(2, parchaseText.getText());
            pst.setString(3, ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText());
            pst.setInt(4, supid);
            pst.setString(5, totalrate.getText());
            pst.setString(6, totaldiscounttext.getText());
            pst.setString(7, totalvattext.getText());
            pst.setString(8, nettotaltext.getText());
            pst.setString(9, remarktext.getText());
            pst.setInt(10, userid);
            pst.execute();
            // paymentBack();
            parcheDetailsInsert();
            Stockloadminus();
            supliyerupdate();
            printGrn();
            loadGRN();

            // JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    String barocode;

    private void parcheDetailsInsert() throws SQLException, IOException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String probarcode = (String) dataTable.getValueAt(row, 1);
                try {
                    String sql = "Select barcode,stockunit from item where Itemcode='" + probarcode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {
                        // itemcode = rs.getString("barcode");
                        unit = rs.getString("stockunit");
                        barocode = rs.getString("barcode");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                float rate = (Float) dataTable.getValueAt(row, 3);

                float qtyhere = (Float) dataTable.getValueAt(row, 4);

                float discount = (Float) dataTable.getValueAt(row, 5);
                float total = (Float) dataTable.getValueAt(row, 6);

                float totalvat = (Float) dataTable.getValueAt(row, 7);
                float vat = totalvat / qtyhere;
                float netotalhere = (Float) dataTable.getValueAt(row, 8);
                try {

                    String sql = "Insert into purchasereturndetails(purchaseReurnCode,prcode,barcode,unitrate,qty,unit,total,discount,vat,totalvate,nettotal) values (?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, parchaseText.getText());
                    pst.setString(2, probarcode);
                    pst.setString(3, barocode);
                    pst.setFloat(4, rate);
                    pst.setFloat(5, qtyhere);
                    pst.setString(6, unit);
                    pst.setFloat(7, total);
                    pst.setFloat(8, discount);
                    pst.setFloat(9, vat);
                    pst.setFloat(10, totalvat);
                    pst.setFloat(11, netotalhere);
                    pst.execute();
                    ///    examperticipationinsert(id,examcode,exam);

                    // 
                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }
            //loadGRN();
            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void printGrn() {
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Purchase Document", "Purchase Return Document Print", JOptionPane.YES_NO_OPTION);
        if (p == 0) {

            try {

                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/purchaseReturn.jrxml");
                String ParchseNo = parchaseText.getText();
                //   JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\ExpencessRiportbydate.jrxml");
                //String ParchaseNo = parchasetext.getText();
                //  String GRNno =parchasetext.getText();
                HashMap para = new HashMap();
                para.put("purchasereturnno", ParchseNo);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);
                // JasperPrintManager.printReport(jp, true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }

    /*
        private void paymentBack() {
        try {
          float paymentbackamount=0;
          float afterdue=0;
           float amount=Float.parseFloat(totalrate.getText());
            float afternettoal=nettotal-amount;
            if(payment==0){
            afterdue=afternettoal;
            paymentbackamount=0;
            
            }else{
             if(payment>afternettoal){
             
             paymentbackamount=payment;
             afterdue=0;
             
             }else{
             
              afterdue=afternettoal-payment;
             paymentbackamount=payment;
             }   
                
           
            
            }
          
        
               
         
                String sql = "Update GRNinfo set paidamount=sta.paidamount-'" + paymentbackamount + "' where GRNCode='"+purchasecodetext.getSelectedItem()+"' ";
              

                pst = conn.prepareStatement(sql);
                pst.execute();
                cashInDrwaer();
                Stockloadminus();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
     */
    public void balance() throws SQLException {

        try {
            String sql = "Select sum(cashin),sum(cashout) from CashDrower";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {

                float cashin = rs.getFloat("sum(cashin)");
                float cashout = rs.getFloat("sum(cashout)");
                bankbalance1 = (cashin - cashout);

            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void cashInDrwaer() {
        float paymentbackamount = Float.parseFloat(totalrate.getText());
        String reaon = "purchase Return";
        float balance = bankbalance1 + paymentbackamount;
        try {

            String sql = "Insert into CashDrower(cashin,cashout,balance,type,upatedate,tranid) values(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setFloat(1, paymentbackamount);
            pst.setFloat(2, 0);
            pst.setFloat(3, balance);
            pst.setString(4, "Purchase Return,");
            pst.setString(5, ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText());
            pst.setString(6, parchaseText.getText());
            pst.execute();

            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void Stockloadminus() {
        // int cat=(Integer) categoryBox.getSelectedIndex();
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            // String cat = dataTable.getValueAt(i, 8).toString().trim();

            String s = dataTable.getValueAt(i, 1).toString().trim();
            float checkqty = (Float) dataTable.getValueAt(i, 4);

            Stockpdateumuse(s, checkqty);

        }

    }

    private void Stockpdateumuse(String s, float qtyint) {

        try {

            String sql = "Select Qty from stockdetails where barcode=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, s);
            rs = pst.executeQuery();

            while (rs.next()) {

                float stock = rs.getFloat("Qty");

                float update_qty = stock - qtyint;

                String sql1 = "Update stockdetails set Qty='" + update_qty + "' where barcode='" + s + "'";

                pst = conn.prepareStatement(sql1);

                pst.execute();

            }  //JOptionPane.showMessageDialog(null, add1);

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    /*
      private void Stockloadminus() {

        try {
            String sql1 = "Select st.Qty as 'stockqty' from stock st where st.procode=(Select pc.prcode from purchasereturndetails pc where pc.prcode=st.procode AND pc.purchaseReurnCode='" + prNo + "' )";
            pst = conn.prepareStatement(sql1);

            rs = pst.executeQuery();

            while (rs.next()) {
                float updateqty = rs.getFloat("stockqty");
                //String sql1 = "Select prcode,qty,unit , count(id)  from purchasedetails pc where pc.purchaseCode='" + parchasetext.getText() + "'.Update  stock st set st.Qty=pc.qty where st.procode=pc.prcode";
                String sql = "Update stock sta set sta.Qty=(Select pc.qty-'" + updateqty + "'  from purchasereturndetails pc where pc.prcode=sta.procode AND pc.purchaseReurnCode='" + prNo + "') where (select pc.prcode from purchasereturndetails pc where pc.prcode=sta.procode AND pc.purchaseReurnCode='" + prNo + "')=sta.procode";
                //  String sql="update stock sta set Qty=(select sum(pc.qty+(select st.Qty from stock st where st.procode=pc.prcode))from purchasedetails pc where sta.procode=pc.prcode AND pc.purchaseCode='"+parchasetext.getText()+"')";

                pst = conn.prepareStatement(sql);
                pst.execute();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    
     */
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

    private float total_action_discount() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 5).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_mrp() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 6).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_vat() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 7).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_nettotal() {

        int rowaCount = dataTable.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 8).toString());
        }

        return (float) totaltpmrp;

    }

    private void clear() {
        codetext.setText(null);
        itemnamesearch.setSelectedIndex(0);
        unitrateText.setText(null);
        qtytext.setText(null);
        discouttext.setText(null);
        updatekey = 0;
        codetext.requestFocusInWindow();
    }

    private void checkentry() {

        String s = "";
        boolean exists = false;
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            s = dataTable.getValueAt(i, 1).toString().trim();
            float tpd = Float.parseFloat(unitrateText.getText());
            float qtytbl = (Float) dataTable.getValueAt(i, 4);
            float applyqty = Float.parseFloat(qtytext.getText());
            float totalqty = qtytbl + applyqty;

            float totaldiscountget = (Float) dataTable.getValueAt(i, 5);
            float discount = totaldiscountget / qtytbl;
            float tpwdisc = tpd - discount;

            float nettotaltp = (tpd * totalqty);

            float nettotaltpformat = Float.parseFloat(df2.format(nettotaltp));
            float totaldiscounts = totalqty * discount;
            float totaldiscountformat = Float.parseFloat(df2.format(totaldiscounts));
            float vats = includevat / 100;
            float itemvat = tpwdisc * vats;
            float itemvatfromat = Float.parseFloat(df2.format(itemvat));

            float totalvat = itemvatfromat * totalqty;
            float totalvatformat = Float.parseFloat(df2.format(totalvat));

            float nettotals = nettotaltpformat + totalvat - totaldiscountformat;
            float nettotalformat = Float.parseFloat(df2.format(nettotals));
            if (codetext.getText().equals("")) {
                // JOptionPane.showMessageDialog(null, "Enter Invoice Details First");
            } else if (codetext.getText().equals(s)) {
                exists = true;
                dataTable.setValueAt(totalqty, i, 4);
                dataTable.setValueAt(totaldiscountformat, i, 5);
                dataTable.setValueAt(nettotaltpformat, i, 6);
                dataTable.setValueAt(totalvatformat, i, 7);
                dataTable.setValueAt(nettotalformat, i, 8);

                float amount = total_action_mrp();
                totalrate.setText(df2.format(amount));

                float totaldiscount = total_action_discount();
                totaldiscounttext.setText(df2.format(totaldiscount));

                float totalvats = total_action_vat();
                totalvattext.setText(df2.format(totalvats));

                float netalltotals = total_action_nettotal();
                nettotaltext.setText(df2.format(netalltotals));

                break;
            }
        }
        if (!exists) {
            entryData();
        } else {
            // JOptionPane.showMessageDialog(null, "This Data Already Exist.");

            clear();
        }

    }

    private void entryData() {
        float discount = 0;
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        tree++;

        float tpd = Float.parseFloat(unitrateText.getText());
        if (discouttext.getText().isEmpty()) {

            discount = 0;
        } else {
            discount = Float.parseFloat(discouttext.getText());

        }

        float tpwdisc = tpd - discount;
        float vat = includevat;
        float vats = vat / 100;
        float itemvat = tpd * vats;

        float qtyhere = Float.parseFloat(qtytext.getText());

        float nettotaltp = (tpwdisc * qtyhere);
        float nettotaltpfromat = Float.parseFloat(df2.format(nettotaltp));

        float totaldics = discount * qtyhere;
        float totaldiscountformat = Float.parseFloat(df2.format(totaldics));

        float totalvat = itemvat * qtyhere;
        float totalvatformat = Float.parseFloat(df2.format(totalvat));

        float nettotalhere = nettotaltp + totalvat;
        float nettotalhereformat = Float.parseFloat(df2.format(nettotalhere));

        model2.addRow(new Object[]{tree, codetext.getText(), itemnamesearch.getSelectedItem(), tpd, qtyhere, totaldiscountformat, nettotaltpfromat, totalvatformat, nettotalhereformat});

        float amount = total_action_mrp();
        totalrate.setText(df2.format(amount));

        float totaldiscount = total_action_discount();
        totaldiscounttext.setText(df2.format(totaldiscount));

        float totalvats = total_action_vat();
        totalvattext.setText(df2.format(totalvats));

        float netalltotals = total_action_nettotal();
        nettotaltext.setText(df2.format(netalltotals));

        clear();

    }

    private void finelEntry() {

        if (updatekey == 0) {

            if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");

            } else {
                checkentry();

            }

        } else if (unitrateText.getText().isEmpty() || qtytext.getText().isEmpty()) {

        } else {

            DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
            int i = dataTable.getSelectedRow();
            if (i >= 0) {
                float discount;
                float tpd = Float.parseFloat(unitrateText.getText());
                discount = Float.parseFloat(discouttext.getText());

                float tpwdisc = tpd - discount;

                float qtyhere = Float.parseFloat(qtytext.getText());
                float nettotaltp = (tpwdisc * qtyhere);

                float vat = includevat;
                float vats = vat / 100;
                float itemvat = tpd * vats;

                float totaldics = discount * qtyhere;
                float totaldiscountformat = Float.parseFloat(df2.format(totaldics));

                float totalvat = itemvat * qtyhere;
                float totalvatformat = Float.parseFloat(df2.format(totalvat));

                float netotalhere = nettotaltp + totalvat;
                float netotalhereformat = Float.parseFloat(df2.format(netotalhere));

                model.setValueAt(qtyhere, i, 4);
                model.setValueAt(tpd, i, 3);
                model.setValueAt(totaldiscountformat, i, 5);
                model.setValueAt(nettotaltp, i, 6);
                model.setValueAt(totalvatformat, i, 7);
                model.setValueAt(netotalhereformat, i, 8);

                float amount = total_action_mrp();
                totalrate.setText(df2.format(amount));

                float totaldiscount = total_action_discount();
                totaldiscounttext.setText(df2.format(totaldiscount));

                float totalvats = total_action_vat();
                totalvattext.setText(df2.format(totalvats));

                float netalltotals = total_action_nettotal();
                nettotaltext.setText(df2.format(netalltotals));

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
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);
        totalrate.setText(null);
        totalvattext.setText(null);
        totaldiscounttext.setText(null);
        nettotaltext.setText(null);
        grnnotext.setText(null);
        updatekey = 0;
        supliyerbox.setSelectedIndex(0);
        parchase_code();

    }

    private void loadGRN() throws SQLException, IOException {
        reload();

        /*
      PurchaseReturn filte = null;

        try {
            filte = new PurchaseReturn();
        } catch (SQLException ex) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PurchaseReturn.class.getName()).log(Level.SEVERE, null, ex);
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
    
         */
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        parchasedate = new com.toedter.calendar.JDateChooser();
        parchaseText = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarktext = new javax.swing.JTextArea();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        supliyerbox = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        grnnotext = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        nettotaltext = new javax.swing.JLabel();
        totalvattext = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        totalrate = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        totaldiscounttext = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        qtytext = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        unitrateText = new javax.swing.JTextField();
        codetext = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        discouttext = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        here = new javax.swing.JPanel();
        svbtn = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Purchase Return Details");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Return No");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Date");

        parchasedate.setDateFormatString("yyyy-MM-dd");

        parchaseText.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        parchaseText.setForeground(new java.awt.Color(255, 255, 255));
        parchaseText.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        remarktext.setColumns(20);
        remarktext.setRows(5);
        remarktext.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jScrollPane2.setViewportView(remarktext);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Remark");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(8, 8, 8)
                .addComponent(parchaseText, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel8)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(parchasedate, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(parchaseText, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(parchasedate, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(9, 9, 9))
        );

        jPanel3.setBackground(new java.awt.Color(67, 86, 86));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Suppliyer");

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

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("GRN NO:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(grnnotext, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(supliyerbox, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(grnnotext))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(67, 86, 86));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Amount");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Net Total");

        nettotaltext.setBackground(new java.awt.Color(255, 255, 255));
        nettotaltext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        nettotaltext.setForeground(new java.awt.Color(255, 255, 255));
        nettotaltext.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        totalvattext.setBackground(new java.awt.Color(255, 255, 255));
        totalvattext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalvattext.setForeground(new java.awt.Color(255, 255, 255));
        totalvattext.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Vat");

        totalrate.setBackground(new java.awt.Color(255, 255, 255));
        totalrate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalrate.setForeground(new java.awt.Color(255, 255, 255));
        totalrate.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Discount");

        totaldiscounttext.setBackground(new java.awt.Color(255, 255, 255));
        totaldiscounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totaldiscounttext.setForeground(new java.awt.Color(255, 255, 255));
        totaldiscounttext.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 1, true));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalvattext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nettotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totaldiscounttext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(5, 5, 5))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(totaldiscounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(nettotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(67, 86, 86));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Qty");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Code");

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
        itemnamesearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                itemnamesearchKeyTyped(evt);
            }
        });

        qtytext.setBackground(new java.awt.Color(255, 255, 204));
        qtytext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        qtytext.setForeground(new java.awt.Color(153, 0, 0));
        qtytext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        qtytext.setBorder(null);
        qtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytextKeyPressed(evt);
            }
        });

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

        unitrateText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        unitrateText.setForeground(new java.awt.Color(102, 0, 0));
        unitrateText.setBorder(null);

        codetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        codetext.setForeground(new java.awt.Color(153, 0, 0));
        codetext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        codetext.setBorder(null);
        codetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codetextKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Unit Rate");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Item Name");

        discouttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        discouttext.setForeground(new java.awt.Color(102, 0, 0));
        discouttext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        discouttext.setBorder(null);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Discount");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(discouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(discouttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(157, 157, 157)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        svbtn.setBackground(new java.awt.Color(51, 153, 0));
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
                .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
        );
        hereLayout.setVerticalGroup(
            hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hereLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deletebtn, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                    .addComponent(svbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        dataTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        dataTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Barcode", "Item Name", "Unit Rate", "Qty", "Discount", "Total ", "Total Vat", "Net Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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
            dataTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            dataTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(here, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addGap(3, 3, 3)
                .addComponent(here, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setBounds(20, 10, 1259, 538);
    }// </editor-fold>//GEN-END:initComponents

    private void codetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyReleased

        String barcode = codetext.getText();
        try {
            String sql = "Select itemcode,barcode,nameformat,tp,(select vat from item ite where bp.itemcode=ite.Itemcode) as 'vat' from barcodeproduct bp where barcode='" + barcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                String barcodes = rs.getString("barcode");
                String item = rs.getString("nameformat");
                String nameformat = barcodes + "-" + item;
                itemnamesearch.setSelectedItem(nameformat);
                String unitrate = rs.getString("tp");
                unitrateText.setText(unitrate);
                includevat = rs.getFloat("vat");
                qtytext.requestFocusInWindow();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }//GEN-LAST:event_codetextKeyReleased

    private void codetextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyTyped
        unitrateText.setText(null);
        itemnamesearch.setSelectedIndex(0);
    }//GEN-LAST:event_codetextKeyTyped

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
        String SearchText = (String) itemnamesearch.getSelectedItem();

        //ItemSearch(part1);
        try {

            String sql = "Select ita.Itemcode as 'itaid',itemName,(select un.unitshort from unit un where un.id=ita.stockunit) as 'unitshort',ita.tp as 'tprice',mrp,vat,(select st.Qty from stock st where st.procode=ita.id) as 'stock' from item ita where ita.itemName='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("itaid");
                codetext.setText(Id);
                String unitrate = rs.getString("tprice");
                unitrateText.setText(unitrate);
                includevat = rs.getFloat("vat");
                String stock = rs.getString("stock");

                float tp = rs.getFloat("tprice");
                float mrp = rs.getFloat("mrp");

                float profite = (mrp - tp / 100 * tp);
                String pro = String.format("%.2f", profite);

                qtytext.requestFocusInWindow();
                qtytext.setBackground(Color.YELLOW);

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }


    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void itemnamesearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyTyped

        try {
            Item();
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseReturnEntry.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_itemnamesearchKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        finelEntry();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clear();

        codetext.enable(true);
        itemnamesearch.enable(true);
    }//GEN-LAST:event_jButton2ActionPerformed
    private void purchaseupdate() {
        try {

            String date = ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText();

            String sql = "Update purchasereturn set pdate='" + date + "',supliyer='" + supid + "',Totalrate='" + totalrate.getText() + "', vat='" + totalvattext.getText() + "', nettotal='" + nettotaltext.getText() + "',inputuser='" + userid + "' where purchaseReurnCode='" + parchaseText.getText() + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void purchasedeletehistory() {

        try {
            String sql = "Delete from purchasereturndetails where purchaseReurnCode=?";
            pst = conn.prepareStatement(sql);

            pst.setString(1, parchaseText.getText());

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed
        if (modificationaccsess == 0) {

            DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();

            if (supliyerbox.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Execute Faild, Due to Data Box Empty");
            } else {
                try {
                    parchaseInsert();
                } catch (IOException ex) {
                    Logger.getLogger(PurchaseReturnEntry.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } else {
            purchaseupdate();
            purchasedeletehistory();
            try {
                parcheDetailsInsert();
                Stockloadminus();
                printGrn();
                loadGRN();
            } catch (SQLException | IOException ex) {
                Logger.getLogger(PurchaseReturnEntry.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_svbtnActionPerformed

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        int selectedRowIndex = dataTable.getSelectedRow();

        updatekey = 1;
        codetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        unitrateText.setText(model.getValueAt(selectedRowIndex, 3).toString());
        qtytext.setText(model.getValueAt(selectedRowIndex, 4).toString());

        float qtytbl = Float.parseFloat((model.getValueAt(selectedRowIndex, 4).toString()));
        float discount = Float.parseFloat((model.getValueAt(selectedRowIndex, 5).toString()));
        float totalvat = Float.parseFloat((model.getValueAt(selectedRowIndex, 7).toString()));
        float indiscount = discount / qtytbl;
        includevat = (totalvat / qtytbl);
        discouttext.setText(df2.format(indiscount));

    }//GEN-LAST:event_dataTableMouseClicked

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_DELETE) {

        } else {
            deleterow();
        }
    }//GEN-LAST:event_dataTableKeyPressed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        if (modificationaccsess == 1) {
            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                purchasedeletehistory();
                try {
                    String sql = "Delete from purchasereturn where purchaseReurnCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, parchaseText.getText());

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                try {

                    loadGRN();
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(PurchaseReturnEntry.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }//GEN-LAST:event_deletebtnActionPerformed

    private void qtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            finelEntry();

        }
    }//GEN-LAST:event_qtytextKeyPressed

    private void supliyerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supliyerboxPopupMenuWillBecomeInvisible
        if (supliyerbox.getSelectedIndex() > 0) {

            try {
                String sql = "Select id from suplyierInfo where supliyername='" + supliyerbox.getSelectedItem() + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    supid = rs.getInt("id");

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }//GEN-LAST:event_supliyerboxPopupMenuWillBecomeInvisible


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField codetext;
    private javax.swing.JTable dataTable;
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField discouttext;
    private javax.swing.JTextField grnnotext;
    private javax.swing.JPanel here;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JLabel nettotaltext;
    private javax.swing.JLabel parchaseText;
    private com.toedter.calendar.JDateChooser parchasedate;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JComboBox<String> supliyerbox;
    private javax.swing.JButton svbtn;
    private javax.swing.JLabel totaldiscounttext;
    private javax.swing.JLabel totalrate;
    private javax.swing.JLabel totalvattext;
    private javax.swing.JTextField unitrateText;
    // End of variables declaration//GEN-END:variables
}
