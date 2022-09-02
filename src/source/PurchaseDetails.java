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
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class PurchaseDetails extends javax.swing.JInternalFrame {

    String grnno;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String suplyer = null;
    double openingbalance;
    double consighnmentamnt = 0;
    double Balancedue = 0;
    int supid = 0;
    String pcode = null;
    int grn = 0;
    double nettotal = 0;
    java.sql.Date date;
    String description;
    float tp = 0;
    float mrp = 0;
    String batch = "";
    String expdate = "";
    String boxsize = "";
    float pvat = 0;
    float pdiscount = 0;
    float ptpwv = 0;
    float ptpwd = 0;
    float smrpwv = 0;
    float pmrpwd = 0;
    float profite = 0;
    String profiteParcentage = "";
    String Grncode = "";

    /**
     * Creates new form PurchaseDetails
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public PurchaseDetails() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        //table_update();
        AutoCompleteDecorator.decorate(supliyerbox);

        suppliyer();
        currentDate();
        currentDate();
        table_update_today();

    }

    public PurchaseDetails(String table_click) throws SQLException, IOException {

        conn = Java_Connect.conectrDB();
        initComponents();
        suppliyer();
        AutoCompleteDecorator.decorate(supliyerbox);
        PurchaseDetailsPR(table_click);

        currentDate();

        // grnno();
        //  invoiceno.setText(table_click);
        //product_data();
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        //  inputdate.setDate(date);

    }

    private void table_update_today() throws SQLException {
        String grnstri = null;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select purchaseCode as 'ParcheseCode' , DATE_FORMAT(pdate, '%d-%m-%Y') as 'InputDate',(select sup.supliyername from suplyierinfo sup where sup.id=par.supliyer) as'SuppliyerInfo', Totalrate as 'TotalRate',totalvat as 'TotalVat',nettotal as 'NetTotal',totaldiscount,(select ad.name from admin ad where ad.id=par.inputuser) as 'inputuser',GRNstatus from purchase par where par.pdate='" + date + "' ORDER BY pdate DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String purchaseCode = rs.getString("ParcheseCode");
                String pdate = rs.getString("InputDate");
                String supplier = rs.getString("SuppliyerInfo");
                double TotalRate = rs.getDouble("TotalRate");
                double discount = rs.getDouble("totaldiscount");
                double totalvat = rs.getDouble("TotalVat");
                double nettotalhere = rs.getDouble("NetTotal");
                int grnstatus = rs.getInt("GRNstatus");
                if (grnstatus == 1) {

                    grnstri = "Active";
                } else {

                    grnstri = "DiActive";

                }
                String userinput = rs.getString("inputuser");
                tree++;
                model2.addRow(new Object[]{tree, purchaseCode, pdate, supplier, TotalRate, discount, totalvat, nettotalhere, grnstri, userinput});

                description = "Description: (Search By Current Date) Date " + date;
                descriptiontext.setText(description);
            }

            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        totalamttext.setText(Float.toString(total_action_amount()));
        vattext.setText(Float.toString(total_action_vat()));
        nettotaltext.setText(Float.toString(total_action_nettotal()));

    }

    private float total_action_amount() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 4).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_vat() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 6).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_nettotal() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 7).toString());
        }

        return (float) totaltpmrp;

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

    private void grnno() throws SQLException {

        try {
            String sql = "Select GRNdate from GRNCode='" + grnno + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                grnno = rs.getString("GRNCode");

                //  supliyerbox.setSelectedIndex(id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void table_update() throws SQLException {
        String grnstri = null;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select purchaseCode as 'ParcheseCode' , DATE_FORMAT(pdate, '%d-%m-%Y') as 'InputDate',(select sup.supliyername from suplyierinfo sup where sup.id=par.supliyer) as'SuppliyerInfo', Totalrate as 'TotalRate',totaldiscount,totalvat as 'TotalVat',nettotal as 'NetTotal',(select ad.name from admin ad where ad.id=par.inputuser) as 'inputuser',GRNstatus from purchase par ORDER BY id DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String purchaseCode = rs.getString("ParcheseCode");
                String pdate = rs.getString("InputDate");
                String supplier = rs.getString("SuppliyerInfo");
                double TotalRate = rs.getDouble("TotalRate");
                double discount = rs.getDouble("totaldiscount");
                double totalvat = rs.getDouble("TotalVat");
                double nettotalhere = rs.getDouble("NetTotal");
                int grnstatus = rs.getInt("GRNstatus");
                if (grnstatus == 1) {

                    grnstri = "Active";
                } else {

                    grnstri = "DiActive";

                }
                String userinput = rs.getString("inputuser");
                tree++;
                model2.addRow(new Object[]{tree, purchaseCode, pdate, supplier, TotalRate, discount, totalvat, nettotalhere, grnstri, userinput});

                description = "Description: (Search ALL Data) Status: ALL";
                descriptiontext.setText(description);
            }

            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        totalamttext.setText(Float.toString(total_action_amount()));
        vattext.setText(Float.toString(total_action_vat()));
        nettotaltext.setText(Float.toString(total_action_nettotal()));

    }

    private void PurchaseDetailsPR(String table_click) {
        String grns = null;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select purchaseCode as 'ParcheseCode' , DATE_FORMAT(pdate, '%d-%m-%Y') as 'InputDate',(select sup.supliyername from suplyierinfo sup where sup.id=par.supliyer) as'SuppliyerInfo', Totalrate as 'TotalRate',totaldiscount,totalvat as 'TotalVat',nettotal as 'NetTotal',(select ad.name from admin ad where ad.id=par.inputuser) as 'inputuser',GRNstatus from purchase par where par.purchaseCode='" + table_click + "' ORDER BY pdate DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                pcode = rs.getString("ParcheseCode");
                String pdate = rs.getString("InputDate");
                String supplier = rs.getString("SuppliyerInfo");
                double TotalRate = rs.getDouble("TotalRate");
                double discount = rs.getDouble("totaldiscount");
                double totalvat = rs.getDouble("TotalVat");
                nettotal = rs.getDouble("NetTotal");
                int grnstatus = rs.getInt("GRNstatus");

                if (grnstatus == 1) {

                    grns = "Active";
                } else {

                    grns = "DiActive";

                }
                String userinput = rs.getString("inputuser");
                tree++;
                model2.addRow(new Object[]{tree, pcode, pdate, supplier, TotalRate, discount, totalvat, nettotal, grn, userinput});

            }

            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        totalamttext.setText(Float.toString(total_action_amount()));
        vattext.setText(Float.toString(total_action_vat()));
        nettotaltext.setText(Float.toString(total_action_nettotal()));

    }

    private void GRNstatusCheck() {

        if (grn == 1) {
            JOptionPane.showMessageDialog(null, "This Parchase Already Activated or Save in Database");
        } else {
            int p = JOptionPane.showConfirmDialog(null, "Do you really want to Save GRN", "Save Informatoion", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                GRNInsert(Grncode);
            }

        }

    }

    private void GRN_Code() {
        try {
            int new_inv = 1;
            String NewParchaseCode = ("GRN-1" + new_inv + "");
            String sql = "Select id from grninfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);

            }
            String ParchaseCode = String.format("%01d", new_inv);
            // String ParchaseCode = Integer.toString(new_inv);
            NewParchaseCode = ("GRN-1" + ParchaseCode + "");
            //  parchaseText.setText(NewParchaseCode);
            Grncode = NewParchaseCode;

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void GRNInsert(String NewParchaseCode) {
        try {

            java.util.Date now = new java.util.Date();
            java.sql.Date datehere = new java.sql.Date(now.getTime());
            String sql = "Insert into grninfo("
                    + "GRNCode,"
                    + "purchaseCode,"
                    + "GRNdate,"
                    + "status,"
                    + "payment,"
                    + "paidamount,"
                    + "due,"
                    + "supplierid) values (?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, NewParchaseCode);
            pst.setString(2, pcode);
            pst.setDate(3, datehere);
            pst.setString(4, "Active");
            pst.setString(5, "pending");
            pst.setDouble(6, 0);
            pst.setDouble(7, nettotal);
            pst.setInt(8, supid);
            pst.execute();
            Grnstatus();
            // StockCheck();
            //Stockload();
            supliyerupdate();
            JOptionPane.showMessageDialog(null, "GRN LOAD Successfully");
            printGRN(Grncode);
            Grncode = "";
            consighnmentamnt = 0;
            Balancedue = 0;
            // Stockload();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void Grnstatus() {
        try {
            int grnstatus = 1;
            String sql = "Update purchase set GRNstatus='" + grnstatus + "' where purchaseCode='" + pcode + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void selectsupliyer() {
        try {

            String sql = "Select supliyer from purchase where purchaseCode='" + pcode + "'";
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
            String sql = "Select  consighnmentamnt,Balancedue from suplyierInfo where id='" + supid + "'";
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
    private static final DecimalFormat df2 = new DecimalFormat("#.00");

    private void supliyerupdate() {
        try {
            double preamt = consighnmentamnt;
            double presntamt = nettotal;
            double prentconsigntotal = preamt + presntamt;
            String prentconsigntotalfromat = df2.format(prentconsigntotal);
            double afterblncedue = Balancedue + presntamt;
            String afterblnceduefromat = df2.format(afterblncedue);
            String sql = "Update suplyierInfo set "
                    + "consighnmentamnt='" + prentconsigntotalfromat + "',"
                    + "Balancedue='" + afterblnceduefromat + "' where id='" + supid + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    /*
    private void Stockload() {

        try {
            String sql1 = "Select st.Qty as 'stockqty' from stock st where st.procode=(Select pc.prcode from purchasedetails pc where pc.prcode=st.procode AND pc.purchaseCode='" + pcode + "' )";
            pst = conn.prepareStatement(sql1);

            rs = pst.executeQuery();

            while (rs.next()) {
                Float updateqty = rs.getFloat("stockqty");
                //String sql1 = "Select prcode,qty,unit , count(id)  from purchasedetails pc where pc.purchaseCode='" + parchasetext.getText() + "'.Update  stock st set st.Qty=pc.qty where st.procode=pc.prcode";
                String sql = "Update stock sta set sta.Qty=(Select pc.qty+'" + updateqty + "'  from purchasedetails pc where pc.prcode=sta.procode AND pc.purchaseCode='" + pcode + "') where (select pc.prcode from purchasedetails pc where pc.prcode=sta.procode AND pc.purchaseCode='" + pcode + "')=sta.procode";
                //  String sql="update stock sta set Qty=(select sum(pc.qty+(select st.Qty from stock st where st.procode=pc.prcode))from purchasedetails pc where sta.procode=pc.prcode AND pc.purchaseCode='"+parchasetext.getText()+"')";

                pst = conn.prepareStatement(sql);
                pst.execute();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    
     */
    private void StockCheck() {
        try {
            String sql = "Select pc.barcode as 'parbarcode' FROM purchasedetails pc  where  pc.purchaseCode='" + pcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String parbarcode = rs.getString("parbarcode");
                String sql1 = "Select pc.barcode as 'stbarcode' FROM stockdetails pc  where  pc.barcode='" + parbarcode + "'";
                pst = conn.prepareStatement(sql1);
                rs = pst.executeQuery();
                if (rs.next()) {
                } else {
                    StockInsert();
                }
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
/*
    private void Stockload() {
        try {
            String sql1 = "Select st.Qty as 'stockqty',st.itemcode as 'itemcode' from stockdetails st where st.itemcode=(Select pc.prcode from purchasedetails pc where pc.prcode=st.itemcode AND pc.purchaseCode='" + pcode + "')";
            pst = conn.prepareStatement(sql1);
            rs = pst.executeQuery();
            while (rs.next()) {
                Float updateqty = rs.getFloat("stockqty");
                String Itemcode = rs.getString("itemcode");
                float pqty=SelectQty(Itemcode);
                System.out.print(pqty);
                float totalQty=updateqty+pqty;
                String sql = "Update stockdetails sta set"
                        + " sta.Qty='"+totalQty+"'"
                        + " where sta.itemcode='"+Itemcode+"'";
                pst = conn.prepareStatement(sql);
                pst.execute(); 
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
*/
      private void Stockload() {
        try {
            String sql1 = "Select st.Qty as 'stockqty' from stockdetails st where st.itemcode=(Select pc.prcode from purchasedetails pc where pc.prcode=st.itemcode AND pc.purchaseCode='" + pcode + "' )";
            pst = conn.prepareStatement(sql1);
            rs = pst.executeQuery();
            while (rs.next()) {
                Float updateqty = rs.getFloat("stockqty");
                String sql = "Update stockdetails sta set sta.Qty=(Select pc.qty+'" + updateqty + "'  from purchasedetails pc where pc.prcode=sta.itemcode AND pc.purchaseCode='" + pcode + "') where (select pc.prcode from purchasedetails pc where pc.prcode=sta.itemcode AND pc.purchaseCode='" + pcode + "')=sta.itemcode";
                pst = conn.prepareStatement(sql);
                pst.execute();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private float SelectQty(String Itemcode) {
        try {
            String sql = "Select qty from purchasedetails pr where pr.purchaseCode='" + pcode + "' AND prcode='" + Itemcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                float qty = rs.getFloat("qty");
                return qty;
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        return 0;

    }

    private void StockInsert() {

        try {
            String sql = "INSERT INTO stockdetails(itemcode,barcode,color,size,picture,Qty) Select prcode,barcode,color,size,picture,qty from purchasedetails Where purchaseCode='" + pcode + "' ";
            pst = conn.prepareStatement(sql);
            pst.execute();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void priceupdate(String Itemcode) {

        try {
            // GetData(String Itemcode, String Purchasecode)

            String sql = "Update Item sta "
                    + "set sta.tp='" + tp + "',"
                    + "sta.mrp='" + mrp + "',"
                    + "sta.batch='" + batch + "' ,"
                    + "sta.expdate='" + expdate + "',"
                    + "sta.boxsize='" + boxsize + "' ,"
                    + "sta.pvat='" + pvat + "',"
                    + "sta.pdiscount='" + pdiscount + "',"
                    + "sta.ptpwv='" + ptpwv + "',"
                    + "sta.ptpwd='" + ptpwd + "',"
                    + "sta.smrpwv='" + smrpwv + "',"
                    + "sta.pmrpwd='" + pmrpwd + "',"
                    + "sta.profite='" + profite + "',"
                    + "sta.profiteParcentage='" + profiteParcentage + "' where  sta.Itemcode='" + Itemcode + "'";

            //String sql1 = "Select prcode,qty,unit , count(id)  from purchasedetails pc where pc.purchaseCode='" + parchasetext.getText() + "'.Update  stock st set st.Qty=pc.qty where st.procode=pc.prcode";
            /*
            String sql = "Update Item sta set sta.tp=(Select pc.unitrate from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "') ,"
                    + "sta.mrp=(Select pc.mrp from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "'),"
                    + "sta.batch=(Select pc.batch from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "') ,"
                    + "sta.expdate=(Select pc.expdate from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "'),"
                    + "sta.boxsize=(Select pc.boxsize from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "')  ,"
                    + "sta.pvat=(Select pc.vat from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "') ,"
                    + "sta.pdiscount=(Select pc.discount from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "'),"
                    + "sta.ptpwv=(Select pc.tpvat from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "') ,"
                    + "sta.ptpwd=(Select pc.tpdis from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "'),"
                    + "sta.smrpwv=(Select pc.tpvat from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "') ,"
                    + "sta.pmrpwd=(Select pc.tpdis from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "'),"
                    + "sta.profite=(Select pc.profit from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "'),"
                    + "sta.profiteParcentage=(Select pc.profitper from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "')  where (select pc.prcode from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "')=sta.Itemcode"
                    + "";
             */
 /*  String sql = "Update Item sta set sta.tp=(Select pc.unitrate from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "') ,"
                    + "sta.tp=(Select pc.unitrate from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "'),"
                    + "sta.mrp=(Select pc.mrp from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "'),"
                    + "sta.batch=(Select pc.batch from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "') ,"
                    + "sta.expdate=(Select pc.expdate from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "'),"
                    + "sta.boxsize=(Select pc.boxsize from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "')  ,"
                    /*
                    + "sta.pdiscount=(Select pc.discount from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "'),"
                    + "sta.ptpwv=(Select pc.tpvat from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "') ,"
                    + "sta.ptpwd=(Select pc.tpdis from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "'),"                   
                    + "sta.profite=(Select pc.profit from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "'),"
                    + "sta.profiteParcentage=(Select pc.profitper from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "')  where (select pc.prcode from purchasedetails pc where pc.prcode=sta.Itemcode AND pc.purchaseCode='" + pcode + "')=sta.Itemcode"
                    + "";
             */
            //  String sql="update stock sta set Qty=(select sum(pc.qty+(select st.Qty from stock st where st.procode=pc.prcode))from purchasedetails pc where sta.procode=pc.prcode AND pc.purchaseCode='"+parchasetext.getText()+"')";
            pst = conn.prepareStatement(sql);
            pst.execute();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void GetData() {
        try {
            String sql = "Select prcode,"
                    + "batch,count(id) as 'p'"
                    + "expdate,"
                    + "boxsize,"
                    + "unitrate,"
                    + "mrp,"
                    + "discount,"
                    + "vat,"
                    + "tpvat,"
                    + "tpdis,"
                    + "profit,"
                    + "profitper,"
                    + "qty"
                    + " from purchasedetails pr where pr.purchaseCode='" + pcode + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String Itemcode = rs.getString("prcode");
                batch = rs.getString("batch");
                expdate = rs.getString("expdate");
                boxsize = rs.getString("boxsize");
                tp = rs.getFloat("unitrate");
                mrp = rs.getFloat("mrp");
                pvat = rs.getFloat("vat");
                pdiscount = rs.getFloat("discount");
                ptpwv = rs.getFloat("tpvat");
                ptpwd = rs.getFloat("tpdis");
                smrpwv = rs.getFloat("tpvat");
                pmrpwd = rs.getFloat("tpdis");
                profite = rs.getFloat("profit");
                profiteParcentage = rs.getString("profitper");
                float qty = rs.getFloat("qty");
                priceupdate(Itemcode);
                // Stockload(Itemcode, qty);
                int p = rs.getInt("p");
                System.out.print(p);
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void printGRN(String NewParchaseCode) {

        try {
            int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Purchase Document", "Purchase Document Print", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/GRN.jrxml");

                //   JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\ExpencessRiportbydate.jrxml");
                String ParchaseNo = pcode;
                //  String GRNno =parchasetext.getText();
                HashMap para = new HashMap();
                para.put("ParchaseNo", ParchaseNo);
                para.put("GRNno", NewParchaseCode);
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);
            }
        } catch (HeadlessException | JRException ex) {
            JOptionPane.showMessageDialog(null, ex);

        }

    }

    private void loadGRN() {
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print GRN Document", "GRN Print", JOptionPane.YES_NO_OPTION);
        if (p == 0) {

            try {

                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/GRN.jrxml");

                //   JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\ExpencessRiportbydate.jrxml");
                String grnNo = grnno;
                String ParchaseNo = pcode;
                //  String GRNno =parchasetext.getText();
                HashMap para = new HashMap();
                para.put("ParchaseNo", ParchaseNo);
                para.put("GRNno", grnNo);
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);
            } catch (JRException ex) {
                JOptionPane.showMessageDialog(null, ex);

            }
        }
    }

    private void purchaseDate() {

        String grnstri = null;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select purchaseCode as 'ParcheseCode' , DATE_FORMAT(pdate, '%d-%m-%Y') as 'InputDate',(select sup.supliyername from suplyierinfo sup where sup.id=par.supliyer) as'SuppliyerInfo', Totalrate as 'TotalRate',totaldiscount,totalvat as 'TotalVat',nettotal as 'NetTotal',(select ad.name from admin ad where ad.id=par.inputuser) as 'inputuser',GRNstatus from purchase par where par.pdate BETWEEN ? AND ? OR year(par.pdate)='" + yeartext.getText() + "' ORDER BY pdate DESC";
            pst = conn.prepareStatement(sql);
            pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
            pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
            rs = pst.executeQuery();
            while (rs.next()) {
                String purchaseCode = rs.getString("ParcheseCode");
                String pdate = rs.getString("InputDate");
                String supplier = rs.getString("SuppliyerInfo");
                double TotalRate = rs.getDouble("TotalRate");
                double discount = rs.getDouble("totaldiscount");
                double totalvat = rs.getDouble("TotalVat");
                double nettotalhere = rs.getDouble("NetTotal");
                int grnstatus = rs.getInt("GRNstatus");
                if (grnstatus == 1) {

                    grnstri = "Active";
                } else {

                    grnstri = "DiActive";

                }
                String userinput = rs.getString("inputuser");
                tree++;
                model2.addRow(new Object[]{tree, purchaseCode, pdate, supplier, TotalRate, discount, totalvat, nettotalhere, grnstri, userinput});

            }

            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        totalamttext.setText(Float.toString(total_action_amount()));
        vattext.setText(Float.toString(total_action_vat()));
        nettotaltext.setText(Float.toString(total_action_nettotal()));

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
        Exeutegrn = new javax.swing.JMenuItem();
        view = new javax.swing.JMenuItem();
        deletepurchase = new javax.swing.JMenuItem();
        barcode = new javax.swing.JMenuItem();
        viewpurchase = new javax.swing.JMenuItem();
        loadgrn = new javax.swing.JMenuItem();
        jPanel5 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        totalamttext = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        vattext = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nettotaltext = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        todatepayment = new com.toedter.calendar.JDateChooser();
        jButton6 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        fromdatepayment = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        yeartext = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        grnstatusbox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        supliyerbox = new javax.swing.JComboBox<>();
        codetexysearch = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        descriptiontext = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();

        tablemenu.setPreferredSize(new java.awt.Dimension(300, 300));

        Exeutegrn.setText("Execute GRN");
        Exeutegrn.setPreferredSize(new java.awt.Dimension(127, 40));
        Exeutegrn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExeutegrnActionPerformed(evt);
            }
        });
        tablemenu.add(Exeutegrn);

        view.setText("Edit Purchase");
        view.setPreferredSize(new java.awt.Dimension(127, 40));
        view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewActionPerformed(evt);
            }
        });
        tablemenu.add(view);

        deletepurchase.setText("Delete Purchase");
        deletepurchase.setPreferredSize(new java.awt.Dimension(127, 40));
        deletepurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletepurchaseActionPerformed(evt);
            }
        });
        tablemenu.add(deletepurchase);

        barcode.setText("Load Labels");
        barcode.setToolTipText("");
        barcode.setEnabled(false);
        barcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barcodeActionPerformed(evt);
            }
        });
        tablemenu.add(barcode);

        viewpurchase.setText("Purchase View");
        viewpurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewpurchaseActionPerformed(evt);
            }
        });
        tablemenu.add(viewpurchase);

        loadgrn.setText("Load GRN");
        loadgrn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadgrnActionPerformed(evt);
            }
        });
        tablemenu.add(loadgrn);

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Purchase Details");
        setToolTipText("");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        setVisible(true);

        jPanel5.setBackground(new java.awt.Color(67, 86, 86));

        jButton3.setBackground(new java.awt.Color(102, 153, 0));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("New Purchase");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total Amount");

        totalamttext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        totalamttext.setForeground(new java.awt.Color(255, 255, 255));
        totalamttext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        totalamttext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Vat");

        vattext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        vattext.setForeground(new java.awt.Color(255, 255, 255));
        vattext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        vattext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Net Total");

        nettotaltext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nettotaltext.setForeground(new java.awt.Color(255, 255, 255));
        nettotaltext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        nettotaltext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalamttext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(vattext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nettotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(nettotaltext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(vattext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(totalamttext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jPanel3.setBackground(new java.awt.Color(67, 86, 86));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        todatepayment.setDateFormatString("yyyy-MM-dd");

        jButton6.setBackground(new java.awt.Color(0, 204, 0));
        jButton6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Report");
        jButton6.setBorder(null);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("From");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("To");

        fromdatepayment.setDateFormatString("yyyy-MM-dd");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Year");

        yeartext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        yeartext.setForeground(new java.awt.Color(102, 0, 0));
        yeartext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                yeartextKeyPressed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 102, 0));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Submit");
        jButton5.setBorder(null);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel7)
                .addGap(1, 1, 1)
                .addComponent(fromdatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addGap(2, 2, 2)
                .addComponent(todatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(yeartext, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fromdatepayment, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(todatepayment, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(yeartext, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(67, 86, 86));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("GRN Status");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Search");

        grnstatusbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        grnstatusbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Activate", "Inactive" }));
        grnstatusbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                grnstatusboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Suppliyer");

        supliyerbox.setEditable(true);
        supliyerbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        supliyerbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));
        supliyerbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                supliyerboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        codetexysearch.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        codetexysearch.setForeground(new java.awt.Color(0, 51, 0));
        codetexysearch.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        codetexysearch.setBorder(null);
        codetexysearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codetexysearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetexysearchKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codetexysearchKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(codetexysearch, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(grnstatusbox, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(3, 3, 3)
                        .addComponent(codetexysearch, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(grnstatusbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(5, 5, 5))
        );

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Load All");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setText("Today Purchase");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        descriptiontext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        descriptiontext.setForeground(new java.awt.Color(255, 255, 0));

        jButton4.setBackground(new java.awt.Color(153, 51, 0));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Print Data Table");
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
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(descriptiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(278, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(descriptiontext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "Purchase Code", "Input Date", "Supplier Name", "Amount", "Discount", "Vat", "Net Total", "GRN Status", "Inpiut User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        datatbl.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        datatbl.setGridColor(new java.awt.Color(204, 204, 204));
        datatbl.setOpaque(false);
        datatbl.setRowHeight(25);
        datatbl.setShowHorizontalLines(true);
        datatbl.setShowVerticalLines(true);
        datatbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatblMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                datatblMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setPreferredWidth(30);
            datatbl.getColumnModel().getColumn(1).setPreferredWidth(150);
            datatbl.getColumnModel().getColumn(3).setPreferredWidth(300);
            datatbl.getColumnModel().getColumn(8).setResizable(false);
            datatbl.getColumnModel().getColumn(9).setResizable(false);
            datatbl.getColumnModel().getColumn(9).setPreferredWidth(200);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setBounds(15, 5, 1347, 599);
    }// </editor-fold>//GEN-END:initComponents

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
        try {

            int row = datatbl.getSelectedRow();
            String table_click = (datatbl.getModel().getValueAt(row, 1).toString());
            String sql = "Select payble,purchaseCode,GRNstatus,supliyer from purchase where purchaseCode='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                nettotal = rs.getDouble("payble");
                pcode = rs.getString("purchaseCode");
                grn = rs.getInt("GRNstatus");
                supid = rs.getInt("supliyer");
                System.out.print(supid);
               
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        if (grn == 0) {
            suplyerinfo();
            GRN_Code();
        } else {
            consighnmentamnt = 0;
            Balancedue = 0;
        }

        if (SwingUtilities.isRightMouseButton(evt)) {
            tablemenu.show(evt.getComponent(), evt.getX() + 1, evt.getY() + 1);

        }
    }//GEN-LAST:event_datatblMouseClicked

    private void datatblMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_datatblMouseEntered

    private void supliyerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supliyerboxPopupMenuWillBecomeInvisible
        if (supliyerbox.getSelectedIndex() > 0) {
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

            String grns = null;
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            int tree = 0;
            try {
                String sql = "Select purchaseCode as 'ParcheseCode' , DATE_FORMAT(pdate, '%d-%m-%Y') as 'InputDate',(select sup.supliyername from suplyierinfo sup where sup.id=par.supliyer) as'SuppliyerInfo', Totalrate as 'TotalRate',totaldiscount,totalvat as 'TotalVat',nettotal as 'NetTotal',(select ad.name from admin ad where ad.id=par.inputuser) as 'inputuser',GRNstatus from purchase par where par.supliyer='" + supid + "' ORDER BY pdate DESC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    String purchaseCode = rs.getString("ParcheseCode");
                    String pdate = rs.getString("InputDate");
                    String supplier = rs.getString("SuppliyerInfo");
                    double TotalRate = rs.getDouble("TotalRate");
                    double discount = rs.getDouble("totaldiscount");
                    double totalvat = rs.getDouble("TotalVat");
                    double nettotalhere = rs.getDouble("NetTotal");
                    int grnstatus = rs.getInt("GRNstatus");
                    if (grnstatus == 1) {

                        grns = "Active";
                    } else {

                        grns = "DiActive";

                    }
                    String userinput = rs.getString("inputuser");
                    tree++;
                    model2.addRow(new Object[]{tree, purchaseCode, pdate, supplier, TotalRate, discount, totalvat, nettotalhere, grn, userinput});

                    codetexysearch.setText(null);
                    grnstatusbox.setSelectedIndex(0);
                    yeartext.setText(null);

                    description = "Description: (Search By Supplier) Supplier:" + supliyerbox.getSelectedItem();
                    descriptiontext.setText(description);
                }

                //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            totalamttext.setText(Float.toString(total_action_amount()));
            vattext.setText(Float.toString(total_action_vat()));
            nettotaltext.setText(Float.toString(total_action_nettotal()));
        } else {

            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(PurchaseDetails.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }//GEN-LAST:event_supliyerboxPopupMenuWillBecomeInvisible

    private void grnstatusboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_grnstatusboxPopupMenuWillBecomeInvisible

        if (grnstatusbox.getSelectedIndex() > 0) {

            int status = 0;

            int grnst = grnstatusbox.getSelectedIndex();
            if (grnst == 1) {

                status = 1;

            } else if (grnst == 2) {
                status = 0;

            }

            String grns = null;
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            int tree = 0;
            try {
                String sql = "Select purchaseCode as 'ParcheseCode' , DATE_FORMAT(pdate, '%d-%m-%Y') as 'InputDate',(select sup.supliyername from suplyierinfo sup where sup.id=par.supliyer) as'SuppliyerInfo', Totalrate as 'TotalRate',totaldiscount,totalvat as 'TotalVat',nettotal as 'NetTotal',(select ad.name from admin ad where ad.id=par.inputuser) as 'inputuser',GRNstatus from purchase par where par.GRNstatus='" + status + "' ORDER BY pdate DESC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    String purchaseCode = rs.getString("ParcheseCode");
                    String pdate = rs.getString("InputDate");
                    String supplier = rs.getString("SuppliyerInfo");
                    double TotalRate = rs.getDouble("TotalRate");
                    double discount = rs.getDouble("totaldiscount");
                    double totalvat = rs.getDouble("TotalVat");
                    double nettotalhere = rs.getDouble("NetTotal");
                    int grnstatus = rs.getInt("GRNstatus");
                    if (grnstatus == 1) {

                        grns = "Active";
                    } else {

                        grns = "DiActive";

                    }
                    String userinput = rs.getString("inputuser");
                    tree++;
                    model2.addRow(new Object[]{tree, purchaseCode, pdate, supplier, TotalRate, discount, totalvat, nettotalhere, grn, userinput});

                    codetexysearch.setText(null);
                    supliyerbox.setSelectedIndex(0);
                    yeartext.setText(null);

                    description = "Description: (Search By GRN Activation) Status:" + grnstatusbox.getSelectedItem();
                    descriptiontext.setText(description);

                }

                //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

            totalamttext.setText(Float.toString(total_action_amount()));
            vattext.setText(Float.toString(total_action_vat()));
            nettotaltext.setText(Float.toString(total_action_nettotal()));

        } else {

            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(PurchaseDetails.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_grnstatusboxPopupMenuWillBecomeInvisible

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText().isEmpty() || ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select From And To date Value");
        } else {
            String descriptionhere = null;
            purchaseDate();
            String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
            String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();
            descriptionhere = "Description:Date Between From: " + fromdate + " To: " + todate;
            descriptiontext.setText(descriptionhere);
            description = descriptionhere;
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        purchaseDate();
        try {
            String descriptionhere = null;
            //  int total = Integer.parseInt(AllTotalText.getText());
            //String inwords = convert(total) + " Tk only";
            String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
            String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();
            if (yeartext.getText().isEmpty()) {
                descriptionhere = "Description:Date Between From: " + fromdate + " To: " + todate;

            } else {

                descriptionhere = "Description:Year: " + yeartext.getText();
            }
            descriptiontext.setText(descriptionhere);
            description = descriptionhere;

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/Purchasebydate.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();
            para.put("fromdate", fromdate);
            para.put("todate", todate);
            para.put("year", yeartext.getText());
            para.put("description", descriptionhere);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);

        } catch (NumberFormatException | JRException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void codetexysearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetexysearchKeyReleased
        /*  TableModel myTableModel = datatbl.getModel();
        final TableRowSorter<TableModel> sorter = new TableRowSorter<>(myTableModel);
        datatbl.setRowSorter(sorter);
        String text = codetexysearch.getText();
        if (text.length() == 0) {
            sorter.setRowFilter(null);
            datatbl.clearSelection();
        } else {
            try {
                // sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1));
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1));
                datatbl.clearSelection();
            } catch (PatternSyntaxException pse) {
                JOptionPane.showMessageDialog(null, "Bad regex pattern",
                        "Bad regex pattern", JOptionPane.ERROR_MESSAGE);
            }
        }
        
         */
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        if (codetexysearch.getText().isEmpty()) {
            model2.setRowCount(0);
        } else {
            String sesrchtext = codetexysearch.getText();
            String grns = null;

            int tree = 0;
            try {
                String sql = "Select purchaseCode as 'ParcheseCode' , DATE_FORMAT(pdate, '%d-%m-%Y') as 'InputDate',(select sup.supliyername from suplyierinfo sup where sup.id=par.supliyer) as'SuppliyerInfo', Totalrate as 'TotalRate',totaldiscount,totalvat as 'TotalVat',nettotal as 'NetTotal',(select ad.name from admin ad where ad.id=par.inputuser) as 'inputuser',GRNstatus from purchase par where par.purchaseCode LIKE '%" + sesrchtext + "%' OR par.remark LIKE '%" + sesrchtext + "%'  ORDER BY pdate DESC ";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
                    pcode = rs.getString("ParcheseCode");
                    String pdate = rs.getString("InputDate");
                    String supplier = rs.getString("SuppliyerInfo");
                    double TotalRate = rs.getDouble("TotalRate");
                    double discount = rs.getDouble("totaldiscount");
                    double totalvat = rs.getDouble("TotalVat");
                    nettotal = rs.getDouble("NetTotal");
                    int grnstatus = rs.getInt("GRNstatus");

                    if (grnstatus == 1) {

                        grns = "Active";
                    } else {

                        grns = "DiActive";

                    }
                    String userinput = rs.getString("inputuser");
                    tree++;
                    model2.addRow(new Object[]{tree, pcode, pdate, supplier, TotalRate, discount, totalvat, nettotal, grn, userinput});

                }

                //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            totalamttext.setText(Float.toString(total_action_amount()));
            vattext.setText(Float.toString(total_action_vat()));
            nettotaltext.setText(Float.toString(total_action_nettotal()));
        }
    }//GEN-LAST:event_codetexysearchKeyReleased

    private void ExeutegrnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExeutegrnActionPerformed
//        selectsupliyer();
        GRNstatusCheck();
        try {
            table_update();
            /*  GRNframe filte = null;
            
            try {
            filte = new GRNframe();
            } catch (SQLException | IOException ex) {
            Logger.getLogger(PurchaseDetails.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDetails.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_ExeutegrnActionPerformed

    private void viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewActionPerformed
        if (grn == 1) {
            JOptionPane.showMessageDialog(null, "This Purchase Already Activate. Disable To Edit");
            // loadGRN();
        } else {

            String table_click = pcode;
            Purchase filte = null;
            int activemodification = 0;
            try {
                filte = new Purchase(table_click, activemodification);
            } catch (SQLException | IOException ex) {
                Logger.getLogger(PurchaseDetails.class.getName()).log(Level.SEVERE, null, ex);
            }

            filte.setVisible(true);
            this.getDesktopPane().add(filte);
            Dimension desktopSize = getDesktopPane().getSize();
            filte.setSize(desktopSize.width, desktopSize.height);
            Dimension jInternalFrameSize = filte.getSize();
            filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                    (desktopSize.height - jInternalFrameSize.height) / 2);
            filte.moveToFront();
            this.dispose();

        }
    }//GEN-LAST:event_viewActionPerformed

    private void deletepurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletepurchaseActionPerformed

        if (grn == 1) {
            JOptionPane.showMessageDialog(null, "Faild to Remove activate purchase! This Already make GRN");

        } else {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Purchase Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from purchase where purchaseCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, pcode);

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                try {
                    String sql = "Delete from purchasedetails where purchaseCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, pcode);

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
            }

        }

        try {
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(Locale.Category.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_deletepurchaseActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        Purchase filte = null;
        try {
            filte = new Purchase();
        } catch (SQLException ex) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PurchaseDetails.class.getName()).log(Level.SEVERE, null, ex);
        }

        filte.setVisible(true);
        this.getDesktopPane().add(filte);

        //Homedesktop.add(It);
        filte.setVisible(true);

        Dimension desktopSize = getDesktopPane().getSize();
        filte.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
        this.dispose();

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            table_update_today();
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseDetails.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void yeartextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_yeartextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else if (yeartext.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select Year Value");
        } else {
            ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).setText(null);
            ((JTextField) todatepayment.getDateEditor().getUiComponent()).setText(null);
            yeartext.setBackground(Color.YELLOW);
            String descriptionhere = "Description:Year: " + yeartext.getText();
            descriptiontext.setText(descriptionhere);
            purchaseDate();
            description = descriptionhere;
        }
    }//GEN-LAST:event_yeartextKeyPressed

    private void codetexysearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetexysearchKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            String SearchText = codetexysearch.getText();

            String grns = null;
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            int tree = 0;
            try {
                String sql = "Select purchaseCode as 'ParcheseCode' , DATE_FORMAT(pdate, '%d-%m-%Y') as 'InputDate',(select sup.supliyername from suplyierinfo sup where sup.id=par.supliyer) as'SuppliyerInfo', Totalrate as 'TotalRate',totaldiscount,totalvat as 'TotalVat',nettotal as 'NetTotal',(select ad.name from admin ad where ad.id=par.inputuser) as 'inputuser',GRNstatus from purchase par where par.purchaseCode='" + SearchText + "'ORDER BY pdate DESC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
                    String purchaseCode = rs.getString("ParcheseCode");
                    String pdate = rs.getString("InputDate");
                    String supplier = rs.getString("SuppliyerInfo");
                    double TotalRate = rs.getDouble("TotalRate");
                    double discount = rs.getDouble("totaldiscount");
                    double totalvat = rs.getDouble("TotalVat");
                    double nettotalhere = rs.getDouble("NetTotal");
                    int grnstatus = rs.getInt("GRNstatus");
                    if (grnstatus == 1) {

                        grns = "Active";
                    } else {

                        grns = "DiActive";

                    }
                    String userinput = rs.getString("inputuser");
                    tree++;
                    model2.addRow(new Object[]{tree, purchaseCode, pdate, supplier, TotalRate, discount, totalvat, nettotalhere, grn, userinput});
                    description = "Description: (Search By PurchaseCode) Purchase Code:" + SearchText;
                    descriptiontext.setText(description);
                    codetexysearch.setBackground(Color.YELLOW);
                    supliyerbox.setSelectedIndex(0);
                    grnstatusbox.setSelectedIndex(0);

                } else {
                    descriptiontext.setText(null);
                    codetexysearch.setBackground(Color.WHITE);

                }

                //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            totalamttext.setText(Float.toString(total_action_amount()));
            vattext.setText(Float.toString(total_action_vat()));
            nettotaltext.setText(Float.toString(total_action_nettotal()));
        }
    }//GEN-LAST:event_codetexysearchKeyPressed

    private void codetexysearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetexysearchKeyTyped
        codetexysearch.setBackground(Color.WHITE);
    }//GEN-LAST:event_codetexysearchKeyTyped

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        if (model2.getRowCount() == 0) {

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {

            try {
                String totalamount = totalamttext.getText();
                String totalvat = vattext.getText();
                String nettotals = nettotaltext.getText();

                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/PurchaseDetails.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDataSource", ItemJRBean);
                para.put("description", description);

                para.put("totalamount", totalamount);
                para.put("totalvat", totalvat);
                para.put("nettotals", nettotals);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                /*JRViewer viewer = new JRViewer(jp);
            
            Reportview frame =new Reportview();


       JInternalFrame frame = new JInternalFrame("Report");
            
           
       frame.add(viewer);
      this.getDesktopPane().add(frame);
      frame.setVisible(true);
        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = frame.getSize();
        frame.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
        (desktopSize.height - jInternalFrameSize.height) / 2);
        frame.moveToFront();

                 */
 /*  JFrame frame = new JFrame("Report");
frame.getContentPane().add(new JRViewer(jp));
frame.pack();
frame.setVisible(true);

                 */
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void barcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barcodeActionPerformed
        String table_click = pcode;
        PrintLabels filte = null;

        try {
            filte = new PrintLabels(table_click);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(PurchaseDetails.class.getName()).log(Level.SEVERE, null, ex);
        }

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
        Dimension desktopSize = getDesktopPane().getSize();
        filte.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();


    }//GEN-LAST:event_barcodeActionPerformed

    private void viewpurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewpurchaseActionPerformed
        String table_click = pcode;
        PurchaseView filte = null;

        try {
            filte = new PurchaseView(table_click);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(PurchaseDetails.class.getName()).log(Level.SEVERE, null, ex);
        }

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
        Dimension desktopSize = getDesktopPane().getSize();
        filte.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
    }//GEN-LAST:event_viewpurchaseActionPerformed

    private void loadgrnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadgrnActionPerformed
        if (grn == 1) {

            loadGRN();
        } else {
            JOptionPane.showMessageDialog(null, "This Purchase Not GRN Activate");

        }
    }//GEN-LAST:event_loadgrnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem Exeutegrn;
    private javax.swing.JMenuItem barcode;
    private javax.swing.JTextField codetexysearch;
    private javax.swing.JTable datatbl;
    private javax.swing.JMenuItem deletepurchase;
    private javax.swing.JLabel descriptiontext;
    private com.toedter.calendar.JDateChooser fromdatepayment;
    private javax.swing.JComboBox<String> grnstatusbox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem loadgrn;
    private javax.swing.JLabel nettotaltext;
    private javax.swing.JComboBox<String> supliyerbox;
    private javax.swing.JPopupMenu tablemenu;
    private com.toedter.calendar.JDateChooser todatepayment;
    private javax.swing.JLabel totalamttext;
    private javax.swing.JLabel vattext;
    private javax.swing.JMenuItem view;
    private javax.swing.JMenuItem viewpurchase;
    private javax.swing.JTextField yeartext;
    // End of variables declaration//GEN-END:variables
}
