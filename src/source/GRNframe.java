/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.Dimension;

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
public class GRNframe extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int supid;
    String grn = null;
    String pcode = null;
    java.sql.Date date;
    String description;
    // Dashboard dashboard = new Dashboard();
    int userid = 0;
    float paidamount;

    /**
     * Creates new form GRNframe
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public GRNframe() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        ///table_update();
        suppliyer();
        AutoCompleteDecorator.decorate(supliyerbox);
        currentDate();
        table_update_today();
        accsessModification();
    }

    private void userkey() throws IOException, SQLException {
        FileInputStream fis = new FileInputStream("src\\userkey.properties");

        //  /abdulbasit?useUnicode=yes&characterEncoding=UTF-8
        //jdbc:mysql://localhost:3306
        Properties p = new Properties();
        p.load(fis);

        String userids = (String) p.get("userid");
        userid = Integer.parseInt(userids);
        // this.dispose();
        //LoginAccess desboard=new LoginAccess();
    }

    private void accsessModification() {
        try {
            String sql = "Select purcedit from modificationaccsess where userid='" + userid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                int purcedit = rs.getInt("purcedit");
                if (purcedit == 1) {
                    purchasemodification.setEnabled(true);
                } else {
                    purchasemodification.setEnabled(false);
                }

            } else {
                purchasemodification.setEnabled(false);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        //  inputdate.setDate(date);

    }

    private float total_action_amount() {

        int rowaCount = datatbl.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 5).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_paid() {

        int rowaCount = datatbl.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 6).toString());
        }

        return (float) totaltpmrp;

    }

    private float total_action_due() {

        int rowaCount = datatbl.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 7).toString());
        }
        return (float) totaltpmrp;
    }

    private void table_update() throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select "
                    + "gn.GRNdate as 'inputdate',"
                    + "gn.GRNCode as 'grnno',"
                    + "gn.purchaseCode as 'pcode',"
                    + "(select sup.supliyername from suplyierinfo sup where par.supliyer=sup.id) as 'supplier',"
                    + "par.nettotal as 'nettotal',"
                    + "gn.paidamount as 'PaidAmount' ,"
                    + "gn.due as 'DueAmount',"
                    + "gn.payment as 'PaymenStatus'"
                    + " from purchase par inner join grninfo gn ON par.purchaseCode=gn.purchaseCode";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String inputdate = rs.getString("inputdate");
                String grnno = rs.getString("grnno");
                String parchasecode = rs.getString("pcode");
                String suppliyer = rs.getString("supplier");
                double nettotal = rs.getDouble("nettotal");
                double PaidAmount = rs.getDouble("PaidAmount");
                double DueAmount = rs.getDouble("DueAmount");
                String PaymenStatus = rs.getString("PaymenStatus");
                tree++;
                model2.addRow(new Object[]{tree, inputdate, grnno, parchasecode, suppliyer, nettotal, PaidAmount, DueAmount, PaymenStatus});

                description = "Description: (Search ALL Data) Status: ALL";
                descriptiontext.setText(description);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        totalamttext.setText(Float.toString(total_action_amount()));
        paidamounttext.setText(Float.toString(total_action_paid()));
        duetext.setText(Float.toString(total_action_due()));
    }

    private void table_update_today() throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select "
                    + "gn.GRNdate as 'inputdate',"
                    + "gn.GRNCode as 'grnno',"
                    + "gn.purchaseCode as 'pcode',"
                    + "(select sup.supliyername from suplyierinfo sup where par.supliyer=sup.id) as 'supplier',"
                    + "par.nettotal as 'nettotal',"
                    + "gn.paidamount as 'PaidAmount' ,"
                    + "gn.due as 'DueAmount',"
                    + "gn.payment as 'PaymenStatus'"
                    + " from purchase par inner join grninfo gn ON par.purchaseCode=gn.purchaseCode where gn.GRNdate='" + date + "'";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String inputdate = rs.getString("inputdate");
                String grnno = rs.getString("grnno");
                String parchasecode = rs.getString("pcode");
                String suppliyer = rs.getString("supplier");
                double nettotal = rs.getDouble("nettotal");
                double PaidAmount = rs.getDouble("PaidAmount");
                double DueAmount = rs.getDouble("DueAmount");
                String PaymenStatus = rs.getString("PaymenStatus");
                tree++;
                model2.addRow(new Object[]{tree, inputdate, grnno, parchasecode, suppliyer, nettotal, PaidAmount, DueAmount, PaymenStatus});

                description = "Description: (Search By Current Date) Date " + date;
                descriptiontext.setText(description);

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        description = "Description: (Search By Current Date) Date " + date;
        descriptiontext.setText(description);
        totalamttext.setText(Float.toString(total_action_amount()));
        paidamounttext.setText(Float.toString(total_action_paid()));
        duetext.setText(Float.toString(total_action_due()));
    }

    private void loadGRN() {

        try {

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/GRN.jrxml");

            HashMap para = new HashMap();
            para.put("ParchaseNo", pcode);
            para.put("GRNno", grn);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, ex);

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

    private void grnDetailsDate() {

        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {

            String sql = "Select "
                    + "gn.GRNdate as 'inputdate',"
                    + "gn.GRNCode as 'grnno',"
                    + "gn.purchaseCode as 'pcode',"
                    + "(select sup.supliyername from suplyierinfo sup where par.supliyer=sup.id) as 'supplier',"
                    + "par.nettotal as 'nettotal',"
                    + "gn.paidamount as 'PaidAmount' ,"
                    + "gn.due as 'DueAmount',"
                    + "gn.payment as 'PaymenStatus'"
                    + " from purchase par inner join grninfo gn ON par.purchaseCode=gn.purchaseCode where gn.GRNdate BETWEEN ? AND ? OR year(gn.GRNdate)='" + yeartext.getText() + "'";

            pst = conn.prepareStatement(sql);
            pst.setString(1, ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText());
            pst.setString(2, ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText());
            rs = pst.executeQuery();

            while (rs.next()) {
                String inputdate = rs.getString("inputdate");
                String grnno = rs.getString("grnno");
                String parchasecode = rs.getString("pcode");
                String suppliyer = rs.getString("supplier");
                double nettotal = rs.getDouble("nettotal");
                double PaidAmount = rs.getDouble("PaidAmount");
                double DueAmount = rs.getDouble("DueAmount");
                String PaymenStatus = rs.getString("PaymenStatus");
                tree++;
                model2.addRow(new Object[]{tree, inputdate, grnno, parchasecode, suppliyer, nettotal, PaidAmount, DueAmount, PaymenStatus});

            }

        } catch (SQLException e) {
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
        viewgrn = new javax.swing.JMenuItem();
        viewpayment = new javax.swing.JMenuItem();
        purchasemodification = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        todatepayment = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        fromdatepayment = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        yeartext = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        supliyerbox = new javax.swing.JComboBox<>();
        jComboBox1 = new javax.swing.JComboBox<>();
        searchtext = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        descriptiontext = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        totalamttext = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        paidamounttext = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        duetext = new javax.swing.JLabel();

        tablemenu.setPreferredSize(new java.awt.Dimension(300, 180));

        viewgrn.setText("GRN Load");
        viewgrn.setPreferredSize(new java.awt.Dimension(107, 40));
        viewgrn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewgrnActionPerformed(evt);
            }
        });
        tablemenu.add(viewgrn);

        viewpayment.setText("Payment View");
        viewpayment.setEnabled(false);
        viewpayment.setPreferredSize(new java.awt.Dimension(107, 40));
        tablemenu.add(viewpayment);

        purchasemodification.setText("Purchase Modification");
        purchasemodification.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchasemodificationActionPerformed(evt);
            }
        });
        tablemenu.add(purchasemodification);

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("GRN Details");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("To");

        todatepayment.setDateFormatString("yyyy-MM-dd");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("From");

        fromdatepayment.setDateFormatString("yyyy-MM-dd");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Year");

        jButton4.setBackground(new java.awt.Color(204, 0, 0));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Report");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        yeartext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        yeartext.setForeground(new java.awt.Color(102, 0, 0));
        yeartext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                yeartextKeyPressed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 102, 0));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Submit");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel6)
                .addGap(10, 10, 10)
                .addComponent(fromdatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(todatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(jLabel8)
                .addGap(9, 9, 9)
                .addComponent(yeartext, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(yeartext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fromdatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(todatepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(67, 86, 86));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("GRN No/PR No:");

        supliyerbox.setEditable(true);
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

        jComboBox1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Pending", "Due Remain", "Completed" }));
        jComboBox1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBox1PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        searchtext.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        searchtext.setBorder(null);
        searchtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchtextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchtextKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Suppliyer");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Payment Status");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supliyerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(9, 9, 9))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(supliyerbox, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(searchtext))
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "Input Date", "GRN No", "PR No", "Supplier", "Net Amount", "Paid Amount", "Due Amount", "Payment Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
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
        datatbl.setRowHeight(25);
        datatbl.setShowHorizontalLines(true);
        datatbl.setShowVerticalLines(true);
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
            datatbl.getColumnModel().getColumn(4).setPreferredWidth(300);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
            datatbl.getColumnModel().getColumn(8).setResizable(false);
        }

        descriptiontext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        descriptiontext.setForeground(new java.awt.Color(255, 255, 0));

        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton7.setText("Load All");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 255, 255));
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton8.setText("Today");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(153, 51, 0));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Print Data Table");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(descriptiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 568, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 370, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        jPanel5.setBackground(new java.awt.Color(67, 86, 86));

        jButton1.setBackground(new java.awt.Color(67, 86, 86));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("New Purchase");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(67, 86, 86));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Purchase Details");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(67, 86, 86));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Purchase Payment");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
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

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Due");

        duetext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        duetext.setForeground(new java.awt.Color(255, 255, 255));
        duetext.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        duetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paidamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(duetext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 95, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(duetext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(paidamounttext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(totalamttext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setBounds(20, 10, 1337, 576);
    }// </editor-fold>//GEN-END:initComponents

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
        try {

            int row = datatbl.getSelectedRow();
            String table_click = (datatbl.getModel().getValueAt(row, 2).toString());
            String sql = "Select GRNCode,purchaseCode,paidamount from grninfo where GRNCode='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                grn = rs.getString("GRNCode");
                pcode = rs.getString("purchaseCode");
                paidamount = rs.getFloat("paidamount");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        if (SwingUtilities.isRightMouseButton(evt)) {
            tablemenu.show(evt.getComponent(), evt.getX() + 1, evt.getY() + 1);

        }
    }//GEN-LAST:event_datatblMouseClicked

    private void supliyerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supliyerboxPopupMenuWillBecomeInvisible
        if (supliyerbox.getSelectedIndex() == 0) {

            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(GRNframe.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                String sql = "Select id from suplyierInfo where supliyername='" + supliyerbox.getSelectedItem() + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                if (rs.next()) {
                    supid = rs.getInt("id");

                    //  supliyerbox.setSelectedIndex(id);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            int tree = 0;
            try {
                String sql = "Select "
                        + "gn.GRNdate as 'inputdate',"
                        + "gn.GRNCode as 'grnno',"
                        + "gn.purchaseCode as 'pcode',"
                        + "(select sup.supliyername from suplyierinfo sup where par.supliyer=sup.id) as 'supplier',"
                        + "par.nettotal as 'nettotal',"
                        + "gn.paidamount as 'PaidAmount' ,"
                        + "gn.due as 'DueAmount',"
                        + "gn.payment as 'PaymenStatus'"
                        + " from purchase par inner join grninfo gn ON par.purchaseCode=gn.purchaseCode where par.supliyer='" + supid + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                while (rs.next()) {
                    String inputdate = rs.getString("inputdate");
                    String grnno = rs.getString("grnno");
                    String parchasecode = rs.getString("pcode");
                    String suppliyer = rs.getString("supplier");
                    double nettotal = rs.getDouble("nettotal");
                    double PaidAmount = rs.getDouble("PaidAmount");
                    double DueAmount = rs.getDouble("DueAmount");
                    String PaymenStatus = rs.getString("PaymenStatus");
                    tree++;
                    model2.addRow(new Object[]{tree, inputdate, grnno, parchasecode, suppliyer, nettotal, PaidAmount, DueAmount, PaymenStatus});

                    description = "Description: (Search By Supplier) Supplier:" + supliyerbox.getSelectedItem();
                    descriptiontext.setText(description);

                }

                // datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            totalamttext.setText(Float.toString(total_action_amount()));
            paidamounttext.setText(Float.toString(total_action_paid()));
            duetext.setText(Float.toString(total_action_due()));

        }

    }//GEN-LAST:event_supliyerboxPopupMenuWillBecomeInvisible

    private void jComboBox1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox1PopupMenuWillBecomeInvisible
        if (jComboBox1.getSelectedIndex() > 0) {
            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            int tree = 0;
            try {
                String sql = "Select "
                        + "gn.GRNdate as 'inputdate',"
                        + "gn.GRNCode as 'grnno',"
                        + "gn.purchaseCode as 'pcode',"
                        + "(select sup.supliyername from suplyierinfo sup where par.supliyer=sup.id) as 'supplier',"
                        + "par.nettotal as 'nettotal',"
                        + "gn.paidamount as 'PaidAmount' ,"
                        + "gn.due as 'DueAmount',"
                        + "gn.payment as 'PaymenStatus'"
                        + " from purchase par inner join grninfo gn ON par.purchaseCode=gn.purchaseCode where gn.payment='" + jComboBox1.getSelectedItem() + "'";

                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()) {
                    String inputdate = rs.getString("inputdate");
                    String grnno = rs.getString("grnno");
                    String parchasecode = rs.getString("pcode");
                    String suppliyer = rs.getString("supplier");
                    double nettotal = rs.getDouble("nettotal");
                    double PaidAmount = rs.getDouble("PaidAmount");
                    double DueAmount = rs.getDouble("DueAmount");
                    String PaymenStatus = rs.getString("PaymenStatus");
                    tree++;
                    model2.addRow(new Object[]{tree, inputdate, grnno, parchasecode, suppliyer, nettotal, PaidAmount, DueAmount, PaymenStatus});

                    description = "Description: (Search By Payment Details) Status:" + jComboBox1.getSelectedItem();
                    descriptiontext.setText(description);

                }

                // datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            totalamttext.setText(Float.toString(total_action_amount()));
            paidamounttext.setText(Float.toString(total_action_paid()));
            duetext.setText(Float.toString(total_action_due()));
        } else {
            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(GRNframe.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jComboBox1PopupMenuWillBecomeInvisible

    private void searchtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchtextKeyReleased

    }//GEN-LAST:event_searchtextKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText().isEmpty() || ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select From And To date Value");
        } else {
            yeartext.setText(null);
            String descriptionhere = null;
            grnDetailsDate();
            String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
            String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();
            descriptionhere = "Description:Date Between From: " + fromdate + " To: " + todate;
            descriptiontext.setText(descriptionhere);
            description = descriptionhere;
        }


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        grnDetailsDate();
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
                    + "/src/Report/GrnOverall.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();
            para.put("fromdate", fromdate);
            para.put("todate", todate);
            para.put("year", yeartext.getText());
            para.put("description", description);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);

        } catch (NumberFormatException | JRException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void viewgrnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewgrnActionPerformed
        loadGRN();
    }//GEN-LAST:event_viewgrnActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(GRNframe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        try {
            table_update_today();
        } catch (SQLException ex) {
            Logger.getLogger(GRNframe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void searchtextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchtextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {

            DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
            model2.setRowCount(0);
            int tree = 0;
            try {
                String sql = "Select "
                        + "gn.GRNdate as 'inputdate',"
                        + "gn.GRNCode as 'grnno',"
                        + "gn.purchaseCode as 'pcode',"
                        + "(select sup.supliyername from suplyierinfo sup where par.supliyer=sup.id) as 'supplier',"
                        + "par.nettotal as 'nettotal',"
                        + "gn.paidamount as 'PaidAmount' ,"
                        + "gn.due as 'DueAmount',"
                        + "gn.payment as 'PaymenStatus'"
                        + " from purchase par inner join grninfo gn ON par.purchaseCode=gn.purchaseCode where gn.purchaseCode='" + searchtext.getText() + "' OR gn.GRNCode='" + searchtext.getText() + "'";

                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                if (rs.next()) {
                    String inputdate = rs.getString("inputdate");
                    String grnno = rs.getString("grnno");
                    String parchasecode = rs.getString("pcode");
                    String suppliyer = rs.getString("supplier");
                    double nettotal = rs.getDouble("nettotal");
                    double PaidAmount = rs.getDouble("PaidAmount");
                    double DueAmount = rs.getDouble("DueAmount");
                    String PaymenStatus = rs.getString("PaymenStatus");
                    tree++;
                    model2.addRow(new Object[]{tree, inputdate, grnno, parchasecode, suppliyer, nettotal, PaidAmount, DueAmount, PaymenStatus});

                    searchtext.setBackground(Color.YELLOW);
                    description = "Description: (Search By PurchaseCode) Purchase Code:" + searchtext.getText();
                    descriptiontext.setText(description);

                } else {
                    searchtext.setBackground(Color.WHITE);

                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            totalamttext.setText(Float.toString(total_action_amount()));
            paidamounttext.setText(Float.toString(total_action_paid()));
            duetext.setText(Float.toString(total_action_due()));

        }
    }//GEN-LAST:event_searchtextKeyPressed

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
            grnDetailsDate();
            description = descriptionhere;
        }
    }//GEN-LAST:event_yeartextKeyPressed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
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
                        + "/src/Report/GrnDetails.jrxml");

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
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        PurchasePaymentEntry filte = null;

        try {
            filte = new PurchasePaymentEntry();
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
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        PurchaseDetails filte = null;

        try {
            filte = new PurchaseDetails();
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
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
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
        //this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed


    private void purchasemodificationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchasemodificationActionPerformed

        if (paidamount <= 0) {
            String table_click = pcode;
            Purchase filte = null;
            int activemodifiaction = 1;

            try {
                filte = new Purchase(table_click, activemodifiaction);
            } catch (SQLException | IOException ex) {
                Logger.getLogger(GRNframe.class.getName()).log(Level.SEVERE, null, ex);
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

            JOptionPane.showMessageDialog(null, "This Purchase Data alraedy paid " + paidamount + " Please remove Payment First.Please make Paid Amount zero ");

        }
    }//GEN-LAST:event_purchasemodificationActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable datatbl;
    private javax.swing.JLabel descriptiontext;
    private javax.swing.JLabel duetext;
    private com.toedter.calendar.JDateChooser fromdatepayment;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel paidamounttext;
    private javax.swing.JMenuItem purchasemodification;
    private javax.swing.JTextField searchtext;
    private javax.swing.JComboBox<String> supliyerbox;
    private javax.swing.JPopupMenu tablemenu;
    private com.toedter.calendar.JDateChooser todatepayment;
    private javax.swing.JLabel totalamttext;
    private javax.swing.JMenuItem viewgrn;
    private javax.swing.JMenuItem viewpayment;
    private javax.swing.JTextField yeartext;
    // End of variables declaration//GEN-END:variables
}
