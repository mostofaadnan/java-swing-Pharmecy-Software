/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sf.jasperreports.engine.JRException;
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
public final class CustomerInfo extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int updatekey = 0;
    // Dashboard dashboard = new Dashboard();
    int userid = 0;

    String customerid = null;
    //int new_inv = 1;

    /**
     * Creates new form CustomerInfo
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public CustomerInfo() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        currentDate();
        //  AutoCompleteDecorator.decorate(customerinfobox);
        customer();
        cus_code();
        AutoCompleteDecorator.decorate(customerinfobox);
        accsessModification();
    }

    public CustomerInfo(String table_click) throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();

        currentDate();
        //  AutoCompleteDecorator.decorate(customerinfobox);
        customer();
        AutoCompleteDecorator.decorate(customerinfobox);
        CustomerInfo(table_click);
        accsessModification();
    }

    public void cus_code() {
        try {

            int new_inv = 1;
            String NewParchaseCode = (+new_inv + "");
            String sql = "Select customerid from customerinfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                int add1 = rs.getInt("customerid");
                //invoc_text.setText(add1);
                // int inv = Integer.parseInt(add1);
                new_inv = (add1 + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = (ParchaseCode);
            }
            // String finelcode = NewParchaseCode + Itemcodedate;
            String finelcode = NewParchaseCode;
            String StartCode = "1000";
            codetext.setText(finelcode);
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
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
            String sql = "Select cusedit,cusdelete from modificationaccsess where userid='" + userid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                int cusedit = rs.getInt("cusedit");
                int cusdelete = rs.getInt("cusdelete");

                if (cusedit == 1) {

                    svbtn.setEnabled(true);

                } else {

                    svbtn.setEnabled(false);

                }
                if (cusdelete == 1) {

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

    private void CustomerInfo(String custid) {
        try {
            updatekey = 1;
            //String searchtext = (String) customerinfobox.getSelectedItem();

            String sql = "Select * from customerInfo where customerid='" + custid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                customerid = rs.getString("customerid");
                codetext.setText(customerid);

                String sname = rs.getString("customername");
                snametext.setText(sname);

                String address = rs.getString("address");
                addresstext.setText(address);
                String remark = rs.getString("remark");
                remarktext.setText(remark);

                String status = rs.getString("status");
                statusbox.setSelectedItem(status);

                String customertype = rs.getString("customerType");
                supliertypebox.setSelectedItem(customertype);

                String balanceType = rs.getString("balanceType");
                balancetypebox.setSelectedItem(balanceType);

                String openBalanace = rs.getString("OpenigBalance");
                openingbalancetext.setText(openBalanace);
                String totaldiscount = rs.getString("totaldiscount");
                dicounttext.setText(totaldiscount);
                String diposit = rs.getString("DipositAmt");
                dipositamointtext.setText(diposit);

                String credit = rs.getString("creditAmnt");
                creditamounttext.setText(credit);

                String cashamt = rs.getString("cashamt");
                cashamounttext.setText(cashamt);

                String saleamount = rs.getString("saleamount");
                salesAmounttext.setText(saleamount);

                String apidamt = rs.getString("paidamount");
                paidamounttext.setText(apidamt);

                String balandue = rs.getString("Balancedue");
                balancedue.setText(balandue);

          

                String contctno = rs.getString("ContactNo");
                contactnotext.setText(contctno);
              

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void customer() throws SQLException {
        String cash = "Cash Customer";
        try {
            String sql = "Select customername from customerInfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("customername");
                customerinfobox.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        java.sql.Date date = new java.sql.Date(now.getTime());
        openeningDatetext.setDate(date);

    }

    private void addStock() throws SQLException {

        try {

            double blancedu = 0;
            double openingbalnce = Double.parseDouble(openingbalancetext.getText());
            double deposit = Double.parseDouble(dipositamointtext.getText());
            blancedu = openingbalnce - deposit;
            if (openingbalnce < deposit) {
                JOptionPane.showMessageDialog(null, "Starting Due grater Than Opening Balance");

            } else {

                String sql = "Insert into customerinfo(customerid,"
                        + "customername,"
                        + "address,"
                        + "remark,"
                        + "status,"
                        + "customerType,"
                        + "balanceType,"
                        + "OpeningDate,"
                        + "OpenigBalance,"
                        + "DipositAmt,"
                        + "creditAmnt,"
                        + "cashamt,"
                        + "saleamount,"
                        + "paidamount,"
                        + "totaldiscount,"
                        + "Balancedue,"
                        + "ContactNo,"
                        + "email,"
                        + "inputuser,"
                        + "picture) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, codetext.getText());
                pst.setString(2, snametext.getText());
                pst.setString(3, addresstext.getText());
                pst.setString(4, remarktext.getText());
                pst.setString(5, (String) statusbox.getSelectedItem());
                pst.setString(6, (String) supliertypebox.getSelectedItem());
                pst.setString(7, (String) balancetypebox.getSelectedItem());
                pst.setString(8, ((JTextField) openeningDatetext.getDateEditor().getUiComponent()).getText());
                pst.setString(9, openingbalancetext.getText());
                pst.setString(10, dipositamointtext.getText());
                pst.setString(11, openingbalancetext.getText());
                pst.setString(12, cashamounttext.getText());
                pst.setString(13, openingbalancetext.getText());
                pst.setString(14, dipositamointtext.getText());
                pst.setString(15, dicounttext.getText());
                pst.setDouble(16, blancedu);
                pst.setString(17, contactnotext.getText());
                pst.setString(18, emailtext.getText());
                pst.setInt(19, userid);
                pst.setBytes(20, person_image);
                pst.execute();

                JOptionPane.showMessageDialog(null, "Data Saved");

                customerinfobox.removeAllItems();
                customerinfobox.addItem("All");
                customerinfobox.setSelectedItem("All");
                customer();
                clear();

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void stockUpdate() throws SQLException {
        try {

            String id = codetext.getText();

            String sql = "Update customerInfo set "
                    + "customername='" + snametext.getText() + "',"
                    + "address='" + addresstext.getText() + "',"
                    + "remark='" + remarktext.getText() + "',"
                    + "balanceType='" + balancetypebox.getSelectedItem() + "',"
                    + "customerType='" + supliertypebox.getSelectedItem() + "',"
                    + "ContactNo='" + contactnotext.getText() + "',"
                    + "picture=? where customerid='" + id + "' ";
            pst = conn.prepareStatement(sql);
            pst.setBytes(1, person_image);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Update");

            customerinfobox.removeAllItems();
            customerinfobox.addItem("All");
            customerinfobox.setSelectedIndex(0);
            customer();
            clear();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        clear();

    }

    private void clear() throws SQLException {
        updatekey = 0;
        codetext.setText(null);
        snametext.setText(null);
        addresstext.setText(null);

        remarktext.setText(null);
        openingbalancetext.setText("0");
        dipositamointtext.setText("0");
        creditamounttext.setText("0");
        paidamounttext.setText("0");
        balancedue.setText("0");
        salesAmounttext.setText("0");

        contactnotext.setText(null);
        supliertypebox.setSelectedIndex(0);
        statusbox.setSelectedIndex(0);
        balancetypebox.setSelectedIndex(0);
        cashamounttext.setText("0");
       
        cus_code();
        person_image = null;;

    }

    private void deleteCheck() throws SQLException {
        if (codetext.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Item Are Not Selected");

        } else {
            double balanced = Double.parseDouble(balancedue.getText());
            if (balanced > 0) {

                JOptionPane.showMessageDialog(null, "Sory! " + snametext.getText() + " has Balance Due,Please Make Sure Balance due zero");

            } else {
                delteCustomer();

            }

        }

    }

    private void delteCustomer() throws SQLException {
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            try {
                String sql = "Delete from customerInfo where id=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, codetext.getText());

                pst.execute();
                JOptionPane.showMessageDialog(null, "Data Deleted");

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            customerinfobox.removeAllItems();
            customerinfobox.addItem("Select");
            customerinfobox.setSelectedIndex(0);
            customer();
        }
        clear();

    }

 
  

 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        codetext = new javax.swing.JTextField();
        snametext = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        addresstext = new javax.swing.JTextArea();
        remarktext = new javax.swing.JTextField();
        statusbox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        contactnotext = new javax.swing.JTextField();
        emailtext = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        balancedue = new javax.swing.JTextField();
        salesAmounttext = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        openingbalancetext = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        openeningDatetext = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        creditamounttext = new javax.swing.JTextField();
        dipositamointtext = new javax.swing.JTextField();
        supliertypebox = new javax.swing.JComboBox<>();
        balancetypebox = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        paidamounttext = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        dicounttext = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        cashamounttext = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        balancedue1 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        balancedue2 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        balancedue3 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        svbtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        customerinfobox = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Customer Info");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        codetext.setEnabled(false);

        snametext.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        snametext.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        snametext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                snametextActionPerformed(evt);
            }
        });

        addresstext.setColumns(20);
        addresstext.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        addresstext.setRows(5);
        addresstext.setBorder(null);
        jScrollPane1.setViewportView(addresstext);

        remarktext.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        remarktext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        statusbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Deactive" }));
        statusbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusboxActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel10.setText("Remark");

        jLabel11.setText("Status");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel3.setText("Address");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("Customer Name");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setText("CODE");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel28.setText("Contact No");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel30.setText("Email");

        contactnotext.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        contactnotext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        emailtext.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        emailtext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(81, 81, 81)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(emailtext)
                            .addComponent(contactnotext))
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(statusbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(remarktext)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                            .addComponent(codetext)
                            .addComponent(snametext, javax.swing.GroupLayout.Alignment.TRAILING)))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(snametext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(remarktext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactnotext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Sales Amont");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 0, 0));
        jLabel13.setText("Balance Type");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Credit Amount");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Opening Date");

        balancedue.setEditable(false);
        balancedue.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        balancedue.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        balancedue.setText("0");
        balancedue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        balancedue.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        salesAmounttext.setEditable(false);
        salesAmounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        salesAmounttext.setForeground(new java.awt.Color(0, 102, 0));
        salesAmounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        salesAmounttext.setText("0");
        salesAmounttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        salesAmounttext.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Start Balance Due");

        openingbalancetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        openingbalancetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        openingbalancetext.setText("0");
        openingbalancetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Balance Due");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 0, 0));
        jLabel12.setText("Customer Type");

        openeningDatetext.setDateFormatString("yyyy-MM-dd");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Opening Balance");

        creditamounttext.setEditable(false);
        creditamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        creditamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        creditamounttext.setText("0");
        creditamounttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        creditamounttext.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        dipositamointtext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        dipositamointtext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        dipositamointtext.setText("0");
        dipositamointtext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        supliertypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Company", "Branch" }));

        balancetypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Credit", "Credit And Cash", " " }));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("Paid Amount");

        paidamounttext.setEditable(false);
        paidamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        paidamounttext.setForeground(new java.awt.Color(204, 0, 0));
        paidamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paidamounttext.setText("0");
        paidamounttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        paidamounttext.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Discount");

        dicounttext.setEditable(false);
        dicounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        dicounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        dicounttext.setText("0");
        dicounttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        dicounttext.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Cash Amount");

        cashamounttext.setEditable(false);
        cashamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cashamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashamounttext.setText("0");
        cashamounttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        cashamounttext.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Total Point");

        balancedue1.setEditable(false);
        balancedue1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        balancedue1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        balancedue1.setText("0");
        balancedue1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        balancedue1.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setText("Point Use");

        balancedue2.setEditable(false);
        balancedue2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        balancedue2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        balancedue2.setText("0");
        balancedue2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        balancedue2.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setText("Remain Point");

        balancedue3.setEditable(false);
        balancedue3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        balancedue3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        balancedue3.setText("0");
        balancedue3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        balancedue3.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(creditamounttext, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(salesAmounttext, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dicounttext, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cashamounttext, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dipositamointtext, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(balancedue, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(paidamounttext)
                            .addComponent(balancedue1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(balancedue2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(balancedue3, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(openeningDatetext, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                            .addComponent(openingbalancetext)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(supliertypebox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(balancetypebox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(supliertypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(balancetypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openeningDatetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(openingbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dipositamointtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(creditamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dicounttext)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(salesAmounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paidamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(balancedue)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(balancedue1)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(balancedue2)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(balancedue3)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        svbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        svbtn.setForeground(new java.awt.Color(0, 102, 0));
        svbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/save.png"))); // NOI18N
        svbtn.setText("Execute");
        svbtn.setBorder(null);
        svbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svbtnActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 153, 51));
        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/clear.png"))); // NOI18N
        jButton2.setText("Clear");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        deletebtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deletebtn.setForeground(new java.awt.Color(204, 0, 0));
        deletebtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/delete.png"))); // NOI18N
        deletebtn.setText("Delete");
        deletebtn.setBorder(null);
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(500, 500, 500)
                .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(67, 86, 86));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Customer");

        customerinfobox.setEditable(true);
        customerinfobox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        customerinfobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        customerinfobox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                customerinfoboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jButton3.setBackground(new java.awt.Color(204, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("IDENTITY CARD");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(customerinfobox, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(customerinfobox, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        setBounds(0, 0, 872, 567);
    }// </editor-fold>//GEN-END:initComponents

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
        try {
            deleteCheck();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerInfo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_deletebtnActionPerformed

    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed
        if (snametext.getText().isEmpty() || contactnotext.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Fillup Name And Mobile Number");
        } else {
            if (updatekey == 0) {
                if (snametext.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please Customer Name Require");
                } else {
                    try {
                        addStock();

                    } catch (SQLException ex) {
                        Logger.getLogger(SupplierInfo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                try {
                    stockUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerInfo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_svbtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            clear();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void customerinfoboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_customerinfoboxPopupMenuWillBecomeInvisible

        if (customerinfobox.getSelectedIndex() == 0) {
            try {
                clear();
            } catch (SQLException ex) {
                Logger.getLogger(CustomerInfo.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            updatekey = 1;
            try {

                String searchtext = (String) customerinfobox.getSelectedItem();

                String sql = "Select * from customerInfo where customername='" + searchtext + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {

                    customerid = rs.getString("customerid");
                    codetext.setText(customerid);

                    String sname = rs.getString("customername");
                    snametext.setText(sname);

                    String address = rs.getString("address");
                    addresstext.setText(address);
                    String remark = rs.getString("remark");
                    remarktext.setText(remark);

                    String status = rs.getString("status");
                    statusbox.setSelectedItem(status);

                    String customertype = rs.getString("customerType");
                    supliertypebox.setSelectedItem(customertype);

                    String balanceType = rs.getString("balanceType");
                    balancetypebox.setSelectedItem(balanceType);

                    String openBalanace = rs.getString("OpenigBalance");
                    openingbalancetext.setText(openBalanace);
                    String totaldiscount = rs.getString("totaldiscount");
                    dicounttext.setText(totaldiscount);
                    String diposit = rs.getString("DipositAmt");
                    dipositamointtext.setText(diposit);

                    String credit = rs.getString("creditAmnt");
                    creditamounttext.setText(credit);

                    String cashamt = rs.getString("cashamt");
                    cashamounttext.setText(cashamt);

                    String saleamount = rs.getString("saleamount");
                    salesAmounttext.setText(saleamount);

                    String apidamt = rs.getString("paidamount");
                    paidamounttext.setText(apidamt);

                    String balandue = rs.getString("Balancedue");
                    balancedue.setText(balandue);

                    String contctno = rs.getString("ContactNo");
                    contactnotext.setText(contctno);
                    String email = rs.getString("email");
                    emailtext.setText(email);



                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }


    }//GEN-LAST:event_customerinfoboxPopupMenuWillBecomeInvisible

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        if (customerinfobox.getSelectedIndex() > 0) {
            try {

                //  int total = Integer.parseInt(AllTotalText.getText());
                //String inwords = convert(total) + " Tk only";
                JasperDesign jd = JRXmlLoader.load(new File("")
                        .getAbsolutePath()
                        + "/src/Report/CustomerId.jrxml");
                //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

                HashMap para = new HashMap();
                para.put("customerid", customerid);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void statusboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusboxActionPerformed

    private void snametextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_snametextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_snametextActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea addresstext;
    private javax.swing.JTextField balancedue;
    private javax.swing.JTextField balancedue1;
    private javax.swing.JTextField balancedue2;
    private javax.swing.JTextField balancedue3;
    private javax.swing.JComboBox<String> balancetypebox;
    private javax.swing.JTextField cashamounttext;
    private javax.swing.JTextField codetext;
    private javax.swing.JTextField contactnotext;
    private javax.swing.JTextField creditamounttext;
    private javax.swing.JComboBox<String> customerinfobox;
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField dicounttext;
    private javax.swing.JTextField dipositamointtext;
    private javax.swing.JTextField emailtext;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JDateChooser openeningDatetext;
    private javax.swing.JTextField openingbalancetext;
    private javax.swing.JTextField paidamounttext;
    private javax.swing.JTextField remarktext;
    private javax.swing.JTextField salesAmounttext;
    private javax.swing.JTextField snametext;
    private javax.swing.JComboBox<String> statusbox;
    private javax.swing.JComboBox<String> supliertypebox;
    private javax.swing.JButton svbtn;
    // End of variables declaration//GEN-END:variables
String filename = null;
    int s = 0;
    byte[] person_image = null;

    private ImageIcon format = null;

}
