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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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
public class Import extends javax.swing.JInternalFrame {

    String grnno;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String countryid = null;
    String ParchseNo = null;
    int Grnstatus = 0;
    int supid = 0;
    java.sql.Date date;
   // Dashboard dashboard = new Dashboard();
    int userid = 0;
    double TotalRate = 0;
    String description = null;
    double impayment = 0;

    /**
     * Creates new form Import
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public Import() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
       userkey();
        AutoCompleteDecorator.decorate(supliyerbox);

        suppliyer();
        currentDate();
        table_update_today();
        accsessModification();
    }

    public Import(String table_click) throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        AutoCompleteDecorator.decorate(supliyerbox);
        importdetaisl(table_click);
        suppliyer();
        currentDate();
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
    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        //  inputdate.setDate(date);

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

                    modification.setEnabled(true);

                } else {

                   modification.setEnabled(false);

                }
                
                if (imexdelete == 1) {

                    delete.setEnabled(true);

                } else {

                    delete.setEnabled(false);

                }

            } else {
               modification.setEnabled(false);
                 delete.setEnabled(false);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
     
    private float total_action_amount() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 5).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_paid() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 8).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_due() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 9).toString());
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void table_update() throws SQLException {
        String grnstri = null;
        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select Totalrate,netcommision,importCode,pdate,grnstatus,(select sp.supliyername from suplyierinfo sp where sp.id=im.supliyer) as 'supliyername',(select cy.name from country cy where cy.id=im.countryid) as 'countryname',(select ad.name from admin ad where ad.id=im.inputuser) as 'inputusername',payment,paymentDue  from import im ORDER BY pdate DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {

                double TotalRatetbl = rs.getDouble("Totalrate");
                double netcommision=rs.getDouble("netcommision");
                String importcode = rs.getString("importCode");

                String supliyername = rs.getString("supliyername");

                String countryname = rs.getString("countryname");

                String inutDate = rs.getString("pdate");

                int grnstatus = rs.getInt("GRNstatus");

                if (grnstatus == 1) {

                    grnstri = "Active";
                } else {

                    grnstri = "DiActive";

                }
                String userinput = rs.getString("inputusername");
                double payment = rs.getDouble("payment");
                double paymentdue = rs.getDouble("paymentDue");
                tree++;
                model2.addRow(new Object[]{tree, importcode, inutDate, countryname, supliyername, TotalRatetbl,netcommision, grnstri, payment, paymentdue, userinput});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        description = "Description: (Search ALL Data) Status: ALL";
        descriptiontext.setText(description);
        totalamttext.setText(Float.toString(total_action_amount()));
        paidamounttext.setText(Float.toString(total_action_paid()));
        duetext.setText(Float.toString(total_action_due()));
    }

    private void table_update_today() throws SQLException {
        String grnstri = null;
        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select Totalrate,netcommision,importCode,pdate,grnstatus,(select sp.supliyername from suplyierinfo sp where sp.id=im.supliyer) as 'supliyername',(select cy.name from country cy where cy.id=im.countryid) as 'countryname',(select ad.name from admin ad where ad.id=im.inputuser) as 'inputusername',payment,paymentDue  from import im where im.pdate='" + date + "' ORDER BY pdate DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {

                double TotalRatetbl = rs.getDouble("Totalrate");
                 double netcommision=rs.getDouble("netcommision");
                String importcode = rs.getString("importCode");

                String supliyername = rs.getString("supliyername");

                String countryname = rs.getString("countryname");

                String inutDate = rs.getString("pdate");

                int grnstatus = rs.getInt("GRNstatus");

                if (grnstatus == 1) {

                    grnstri = "Active";
                } else {

                    grnstri = "DiActive";

                }
                String userinput = rs.getString("inputusername");
                double payment = rs.getDouble("payment");
                double paymentdue = rs.getDouble("paymentDue");
                tree++;
                model2.addRow(new Object[]{tree, importcode, inutDate, countryname, supliyername, TotalRatetbl,netcommision, grnstri, payment, paymentdue, userinput});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        description = "Description: (Search By Current Date) Date " + date;
        descriptiontext.setText(description);
        totalamttext.setText(Float.toString(total_action_amount()));
        paidamounttext.setText(Float.toString(total_action_paid()));
        duetext.setText(Float.toString(total_action_due()));
    }

    private void importdetaisl(String table_click) {

        String grnstri = null;
        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select Totalrate,netcommision,importCode,pdate,GRNstatus,countryid,supliyer,(select sp.supliyername from suplyierinfo sp where sp.id=im.supliyer) as 'supliyername',(select cy.name from country cy where cy.id=im.countryid) as 'countryname',(select ad.name from admin ad where ad.id=im.inputuser) as 'inputusername',payment,paymentDue  from  import im where im.importCode='" + table_click + "' ORDER BY pdate DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {

                TotalRate = rs.getDouble("Totalrate");
                 double netcommision=rs.getDouble("netcommision");
                String importcode = rs.getString("importCode");

                String supliyername = rs.getString("supliyername");

                String countryname = rs.getString("countryname");
                supid = rs.getInt("supliyer");
                String inutDate = rs.getString("pdate");

                int grnstatus = rs.getInt("GRNstatus");

                ParchseNo = rs.getString("importCode");
                //parchasetext.setText(ParchseNo);

                countryid = rs.getString("countryid");

                Grnstatus = rs.getInt("GRNstatus");

                if (grnstatus == 1) {

                    grnstri = "Active";
                } else {

                    grnstri = "DiActive";

                }
                String userinput = rs.getString("inputusername");
                double payment = rs.getDouble("payment");
                double paymentdue = rs.getDouble("paymentDue");
                tree++;
                model2.addRow(new Object[]{tree, importcode, inutDate, countryname, supliyername, TotalRate, netcommision,grnstri, payment, paymentdue, userinput});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        description = "Description: (Add New Import Order) Import No " + ParchseNo;
        descriptiontext.setText(description);
        totalamttext.setText(Float.toString(total_action_amount()));
        paidamounttext.setText(Float.toString(total_action_paid()));
        duetext.setText(Float.toString(total_action_due()));
    }

    private void Grnstatus() {

        try {

            String parchasecode = ParchseNo;
            int grnstatus = 1;
            String paymentstatus = "Pending";

            String sql = "Update import set GRNstatus='" + grnstatus + "',paymentstatus='" + paymentstatus + "' where importCode='" + parchasecode + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void Stockload() {

        try {
            String sql1 = "Select st.Qty as 'stockqty' from stock st where st.procode=(Select pc.prcode from importdetails pc where pc.prcode=st.procode AND pc.importCode='" + ParchseNo + "' )";
            pst = conn.prepareStatement(sql1);

            rs = pst.executeQuery();

            while (rs.next()) {
                Float updateqty = rs.getFloat("stockqty");
                //String sql1 = "Select prcode,qty,unit , count(id)  from purchasedetails pc where pc.purchaseCode='" + parchasetext.getText() + "'.Update  stock st set st.Qty=pc.qty where st.procode=pc.prcode";
                String sql = "Update stock sta set sta.Qty=(Select pc.qty+'" + updateqty + "'  from importdetails pc where pc.prcode=sta.procode AND pc.importCode='" + ParchseNo + "') where (select pc.prcode from importdetails pc where pc.prcode=sta.procode AND pc.importCode='" + ParchseNo + "')=sta.procode";
                //  String sql="update stock sta set Qty=(select sum(pc.qty+(select st.Qty from stock st where st.procode=pc.prcode))from purchasedetails pc where sta.procode=pc.prcode AND pc.purchaseCode='"+parchasetext.getText()+"')";

                pst = conn.prepareStatement(sql);
                pst.execute();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void priceupdate() {

        try {

            //String sql1 = "Select prcode,qty,unit , count(id)  from purchasedetails pc where pc.purchaseCode='" + parchasetext.getText() + "'.Update  stock st set st.Qty=pc.qty where st.procode=pc.prcode";
            String sql = "Update Item sta set sta.tp=(Select pc.unitrate from importdetails pc where pc.prcode=sta.id AND pc.importCode='" + ParchseNo + "') where (select pc.prcode from importdetails pc where pc.prcode=sta.id AND pc.importCode='" + ParchseNo + "')=sta.id";
            //  String sql="update stock sta set Qty=(select sum(pc.qty+(select st.Qty from stock st where st.procode=pc.prcode))from purchasedetails pc where sta.procode=pc.prcode AND pc.purchaseCode='"+parchasetext.getText()+"')";

            pst = conn.prepareStatement(sql);
            pst.execute();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void printGRN() {

        try {
            int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Export Document", "Export Document Print", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/import.jrxml");

                //   JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\ExpencessRiportbydate.jrxml");
                String ParchaseNo = ParchseNo;
                //  String GRNno =parchasetext.getText();
                HashMap para = new HashMap();
                para.put("importid", ParchaseNo);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            }
        } catch (HeadlessException | JRException ex) {
            JOptionPane.showMessageDialog(null, ex);

        }

    }

    private void loadGRN() {

        try {
            int p = JOptionPane.showConfirmDialog(null, "Do you really want to Print Export Document", "Export Document Print", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/import.jrxml");

                //   JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\ExpencessRiportbydate.jrxml");
                String ParchaseNo = ParchseNo;
                //  String GRNno =parchasetext.getText();
                HashMap para = new HashMap();
                para.put("importid", ParchaseNo);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            }
        } catch (HeadlessException | JRException ex) {
            JOptionPane.showMessageDialog(null, ex);

        }

    }

    private void suppliyer_import_export_balance_insert() {

        try {

            String sql = "Insert into suppliyer_import_export_balance(exportimportcode,supid,countryid,importAmount,exportAmount,payment,recieve,paymentdue,reciebedue,inputdate,inputuser) values (?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, ParchseNo);
            pst.setInt(2, supid);
            pst.setString(3, countryid);
            pst.setDouble(4, TotalRate);
            pst.setDouble(5, 0);
            pst.setDouble(6, 0);
            pst.setDouble(7, 0);
            pst.setDouble(8, 0);
            pst.setDouble(9, 0);
            pst.setDate(10, date);
            pst.setInt(11, userid);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void importDetailsDate() {

        String grnstri = null;
        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select Totalrate,netcommision,importCode,pdate,grnstatus,(select sp.supliyername from suplyierinfo sp where sp.id=im.supliyer) as 'supliyername',(select cy.name from country cy where cy.id=im.countryid) as 'countryname',(select ad.name from admin ad where ad.id=im.inputuser) as 'inputusername',payment,paymentDue   from import im where im.pdate BETWEEN ? AND ? OR year(im.pdate)='" + yeartext.getText() + "'  ORDER BY pdate DESC";
            pst = conn.prepareStatement(sql);
            pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
            pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
            rs = pst.executeQuery();
            // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

            while (rs.next()) {

                double TotalRatedate = rs.getDouble("Totalrate");
                 double netcommision=rs.getDouble("netcommision");
                String importcode = rs.getString("importCode");

                String supliyername = rs.getString("supliyername");

                String countryname = rs.getString("countryname");

                String inutDate = rs.getString("pdate");

                int grnstatus = rs.getInt("GRNstatus");

                if (grnstatus == 1) {

                    grnstri = "Active";
                } else {

                    grnstri = "DiActive";

                }
                String userinput = rs.getString("inputusername");
                double payment = rs.getDouble("payment");
                double paymentdue = rs.getDouble("paymentDue");
                tree++;
                model2.addRow(new Object[]{tree, importcode, inutDate, countryname, supliyername, TotalRatedate,netcommision, grnstri, payment, paymentdue, userinput});

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        totalamttext.setText(Float.toString(total_action_amount()));
        paidamounttext.setText(Float.toString(total_action_paid()));
        duetext.setText(Float.toString(total_action_due()));
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
        execution = new javax.swing.JMenuItem();
        view = new javax.swing.JMenuItem();
        delete = new javax.swing.JMenuItem();
        modification = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        grnstatusbox = new javax.swing.JComboBox<>();
        codetexysearch = new javax.swing.JTextField();
        supliyerbox = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        fromdatepayment = new com.toedter.calendar.JDateChooser();
        yeartext = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        todatepayment = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        descriptiontext = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        totalamttext = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        paidamounttext = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        duetext = new javax.swing.JLabel();

        tablemenu.setPreferredSize(new java.awt.Dimension(300, 140));

        execution.setText("Import Execution");
        execution.setToolTipText("");
        execution.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executionActionPerformed(evt);
            }
        });
        tablemenu.add(execution);

        view.setText("Details View");
        view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewActionPerformed(evt);
            }
        });
        tablemenu.add(view);

        delete.setText("Delete Import ");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });
        tablemenu.add(delete);

        modification.setText("Import Modification");
        modification.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificationActionPerformed(evt);
            }
        });
        tablemenu.add(modification);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Import List");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Import No:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Suppliyer");

        grnstatusbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        grnstatusbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Activate", "Inactive" }));
        grnstatusbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                grnstatusboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        codetexysearch.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        codetexysearch.setForeground(new java.awt.Color(0, 51, 0));
        codetexysearch.setBorder(null);
        codetexysearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codetexysearchKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetexysearchKeyReleased(evt);
            }
        });

        supliyerbox.setEditable(true);
        supliyerbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
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

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Status");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(codetexysearch, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(grnstatusbox, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel6))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(grnstatusbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supliyerbox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(codetexysearch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        jPanel3.setBackground(new java.awt.Color(67, 86, 86));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        fromdatepayment.setDateFormatString("yyyy-MM-dd");

        yeartext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        yeartext.setForeground(new java.awt.Color(102, 0, 0));
        yeartext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                yeartextKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("To");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("From");

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

        todatepayment.setDateFormatString("yyyy-MM-dd");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Year");

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
                .addGap(1, 1, 1)
                .addComponent(jLabel8)
                .addGap(1, 1, 1)
                .addComponent(todatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(yeartext, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(fromdatepayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(todatepayment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(yeartext, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Load All");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setText("Today");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        descriptiontext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        descriptiontext.setForeground(new java.awt.Color(255, 255, 0));

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
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
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(descriptiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 556, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)))
                .addContainerGap(327, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4))
        );

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SI No", "ID", "Import Date", "Country", "Supplier", "Total Amount", "Total Commision", "GRN Status", "Payment", "Payment Due", "Input User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Object.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
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
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                datatblMouseEntered(evt);
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
            datatbl.getColumnModel().getColumn(4).setPreferredWidth(300);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
            datatbl.getColumnModel().getColumn(8).setResizable(false);
            datatbl.getColumnModel().getColumn(9).setResizable(false);
            datatbl.getColumnModel().getColumn(10).setResizable(false);
            datatbl.getColumnModel().getColumn(10).setPreferredWidth(200);
        }

        jPanel4.setBackground(new java.awt.Color(67, 86, 86));

        jButton3.setBackground(new java.awt.Color(102, 153, 0));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("New");
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

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Paid");

        paidamounttext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        paidamounttext.setForeground(new java.awt.Color(255, 255, 255));
        paidamounttext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        paidamounttext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Due");

        duetext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        duetext.setForeground(new java.awt.Color(255, 255, 255));
        duetext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        duetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalamttext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paidamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(duetext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(duetext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(paidamounttext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(totalamttext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void codetexysearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetexysearchKeyReleased

    }//GEN-LAST:event_codetexysearchKeyReleased

    private void supliyerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supliyerboxPopupMenuWillBecomeInvisible
        if (supliyerbox.getSelectedIndex() > 0) {

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

            String grnstri = null;
            int tree = 0;
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            try {
                String sql = "Select Totalrate,netcommision,importCode,pdate,grnstatus,(select sp.supliyername from suplyierinfo sp where sp.id=im.supliyer) as 'supliyername',(select cy.name from country cy where cy.id=im.countryid) as 'countryname',(select ad.name from admin ad where ad.id=im.inputuser) as 'inputusername',payment,paymentDue  from import im where im.supliyer='" + supid + "'  ORDER BY pdate DESC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

                while (rs.next()) {

                    double TotalRatesup = rs.getDouble("Totalrate");
                    double netcommision=rs.getDouble("netcommision");
                    String importcode = rs.getString("importCode");

                    String supliyername = rs.getString("supliyername");

                    String countryname = rs.getString("countryname");

                    String inutDate = rs.getString("pdate");

                    int grnstatus = rs.getInt("GRNstatus");

                    if (grnstatus == 1) {

                        grnstri = "Active";
                    } else {

                        grnstri = "DiActive";

                    }
                    String userinput = rs.getString("inputusername");
                    double payment = rs.getDouble("payment");
                    double paymentdue = rs.getDouble("paymentDue");
                    tree++;
                    model2.addRow(new Object[]{tree, importcode, inutDate, countryname, supliyername, TotalRatesup,netcommision, grnstri, payment, paymentdue, userinput});

                    codetexysearch.setText(null);
                    grnstatusbox.setSelectedIndex(0);
                    yeartext.setText(null);

                    description = "Description: (Search By Supplier) Supplier:" + supliyerbox.getSelectedItem();
                    descriptiontext.setText(description);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            totalamttext.setText(Float.toString(total_action_amount()));
            paidamounttext.setText(Float.toString(total_action_paid()));
            duetext.setText(Float.toString(total_action_due()));
        } else {

            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_supliyerboxPopupMenuWillBecomeInvisible

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        NewImportExport filte = null;

        try {
            filte = new NewImportExport();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
        }

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
        this.dispose();

    }//GEN-LAST:event_jButton3ActionPerformed

    private void grnstatusboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_grnstatusboxPopupMenuWillBecomeInvisible

        if (grnstatusbox.getSelectedIndex() > 0) {
            int status = 0;

            int grns = grnstatusbox.getSelectedIndex();
            if (grns == 1) {

                status = 1;

            } else if (grns == 2) {
                status = 0;

            }

            String grnstri = null;
            int tree = 0;
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            try {
                String sql = "Select Totalrate,netcommision,importCode,pdate,grnstatus,(select sp.supliyername from suplyierinfo sp where sp.id=im.supliyer) as 'supliyername',(select cy.name from country cy where cy.id=im.countryid) as 'countryname',(select ad.name from admin ad where ad.id=im.inputuser) as 'inputusername',payment,paymentDue   from import im  where im.GRNstatus='" + status + "' ORDER BY pdate DESC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

                while (rs.next()) {

                    double TotalRategrn = rs.getDouble("Totalrate");
                    double netcommision=rs.getDouble("netcommision");
                    String importcode = rs.getString("importCode");

                    String supliyername = rs.getString("supliyername");

                    String countryname = rs.getString("countryname");

                    String inutDate = rs.getString("pdate");

                    int grnstatus = rs.getInt("GRNstatus");

                    if (grnstatus == 1) {

                        grnstri = "Active";
                    } else {

                        grnstri = "DiActive";

                    }
                    String userinput = rs.getString("inputusername");
                    double payment = rs.getDouble("payment");
                    double paymentdue = rs.getDouble("paymentDue");
                    tree++;
                    model2.addRow(new Object[]{tree, importcode, inutDate, countryname, supliyername, TotalRategrn, netcommision,grnstri, payment, paymentdue, userinput});

                    codetexysearch.setText(null);
                    supliyerbox.setSelectedIndex(0);
                    yeartext.setText(null);

                    description = "Description: (Search By Activation) Status:" + grnstatusbox.getSelectedItem();
                    descriptiontext.setText(description);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            totalamttext.setText(Float.toString(total_action_amount()));
            paidamounttext.setText(Float.toString(total_action_paid()));
            duetext.setText(Float.toString(total_action_due()));
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
            yeartext.setText(null);
            String descriptionhere = null;
            importDetailsDate();
            String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
            String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();
            descriptionhere = "Description:Date Between From: " + fromdate + " To: " + todate;
            descriptiontext.setText(descriptionhere);
            description = descriptionhere;
        }


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            String descriptionhere;
            importDetailsDate();
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
                    + "/src/Report/importview.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();
            para.put("fromdate", fromdate);
            para.put("todate", todate);
            para.put("decription", description);
            para.put("year", yeartext.getText());
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);

        } catch (NumberFormatException | JRException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
        try {

            int row = datatbl.getSelectedRow();
            String table_click = (datatbl.getModel().getValueAt(row, 1).toString());
            String sql = "Select * from import where importCode='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {

                ParchseNo = rs.getString("importCode");

                countryid = rs.getString("countryid");
                Grnstatus = rs.getInt("GRNstatus");
                supid = rs.getInt("supliyer");
                TotalRate = rs.getDouble("TotalRate");
                impayment = rs.getDouble("payment");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        if (SwingUtilities.isRightMouseButton(evt)) {
            tablemenu.show(evt.getComponent(), evt.getX() + 1, evt.getY() + 1);

        }
    }//GEN-LAST:event_datatblMouseClicked

    private void datatblMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_datatblMouseEntered

    private void executionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executionActionPerformed
        Grnstatus();
        Stockload();
        priceupdate();
        suppliyer_import_export_balance_insert();
        printGRN();

        Import filte = null;
        try {
            filte = new Import();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
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

    }//GEN-LAST:event_executionActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed

        if (Grnstatus == 1) {
            JOptionPane.showMessageDialog(null, "Faild to Remove activate Import! This Already make GRN");

        } else {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Purchase Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from import where importCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, ParchseNo);

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                try {
                    String sql = "Delete from importdetails where importCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, ParchseNo);

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
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_deleteActionPerformed

    private void viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewActionPerformed

        if (Grnstatus == 1) {

            loadGRN();
        } else {

            String table_click = ParchseNo;
            int modifiaction = 0;
            ImportView filte = null;

            try {
                filte = new ImportView(table_click, modifiaction);
            } catch (SQLException | IOException ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
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


    }//GEN-LAST:event_viewActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            table_update_today();
        } catch (SQLException ex) {
            Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void codetexysearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetexysearchKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            String grnstri = null;
            int tree = 0;
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            try {
                String sql = "Select Totalrate,netcommision,importCode,pdate,grnstatus,(select sp.supliyername from suplyierinfo sp where sp.id=im.supliyer) as 'supliyername',(select cy.name from country cy where cy.id=im.countryid) as 'countryname',(select ad.name from admin ad where ad.id=im.inputuser) as 'inputusername',payment,paymentDue  from import im where im.importCode='" + codetexysearch.getText() + "'  ORDER BY pdate DESC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                // datatbl.setModel(DbUtils.resultSetToTableModel(rs));

                if (rs.next()) {

                    double TotalRatecode = rs.getDouble("Totalrate");
                    double netcommision=rs.getDouble("netcommision");
                    String importcode = rs.getString("importCode");

                    String supliyername = rs.getString("supliyername");

                    String countryname = rs.getString("countryname");

                    String inutDate = rs.getString("pdate");

                    int grnstatus = rs.getInt("GRNstatus");

                    if (grnstatus == 1) {

                        grnstri = "Active";
                    } else {

                        grnstri = "DiActive";

                    }
                    String userinput = rs.getString("inputusername");
                    double payment = rs.getDouble("payment");
                    double paymentdue = rs.getDouble("paymentDue");
                    tree++;
                    model2.addRow(new Object[]{tree, importcode, inutDate, countryname, supliyername, TotalRatecode,netcommision, grnstri, payment, paymentdue, userinput});
                    description = "Description: (Search By PurchaseCode) Purchase Code:" + codetexysearch.getText();
                    descriptiontext.setText(description);
                    codetexysearch.setBackground(Color.YELLOW);

                } else {

                    codetexysearch.setBackground(Color.WHITE);

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            totalamttext.setText(Float.toString(total_action_amount()));
            paidamounttext.setText(Float.toString(total_action_paid()));
            duetext.setText(Float.toString(total_action_due()));
        }
    }//GEN-LAST:event_codetexysearchKeyPressed

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
            importDetailsDate();
            description = descriptionhere;
        }
    }//GEN-LAST:event_yeartextKeyPressed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        if (model2.getRowCount() == 0) {

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {

            try {
                String totalamount = totalamttext.getText();
                String totalpaid = paidamounttext.getText();
                String totaldue = duetext.getText();

                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/ImportDetails.jrxml");

                Map<String, Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDataSource", ItemJRBean);
                para.put("description", description);

                para.put("totalamount", totalamount);
                para.put("totalpaid", totalpaid);
                para.put("totaldue", totaldue);
                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void modificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificationActionPerformed
        if (impayment <= 0) {

            String table_click = ParchseNo;
            int modifiaction = 1;
            ImportView filte = null;

            try {
                filte = new ImportView(table_click, modifiaction);
            } catch (SQLException | IOException ex) {
                Logger.getLogger(Import.class.getName()).log(Level.SEVERE, null, ex);
            }

            filte.setVisible(true);
            this.getDesktopPane().add(filte);
            Dimension desktopSize = getDesktopPane().getSize();
            Dimension jInternalFrameSize = filte.getSize();
            filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                    (desktopSize.height - jInternalFrameSize.height) / 2);
            filte.moveToFront();
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "This Import Data alraedy paid: " + impayment + " Please remove Payment First.Please make Paid Amount zero ");

        }
    }//GEN-LAST:event_modificationActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField codetexysearch;
    private javax.swing.JTable datatbl;
    private javax.swing.JMenuItem delete;
    private javax.swing.JLabel descriptiontext;
    private javax.swing.JLabel duetext;
    private javax.swing.JMenuItem execution;
    private com.toedter.calendar.JDateChooser fromdatepayment;
    private javax.swing.JComboBox<String> grnstatusbox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem modification;
    private javax.swing.JLabel paidamounttext;
    private javax.swing.JComboBox<String> supliyerbox;
    private javax.swing.JPopupMenu tablemenu;
    private com.toedter.calendar.JDateChooser todatepayment;
    private javax.swing.JLabel totalamttext;
    private javax.swing.JMenuItem view;
    private javax.swing.JTextField yeartext;
    // End of variables declaration//GEN-END:variables
}
