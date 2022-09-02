/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public final class Sallerypayment extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String code = null;
    int bankid;
    float bankcashout = 0;
    float bankcashin = 0;
    float accbalance = 0;
  //  Dashboard dashboard = new Dashboard();
    int userid = 0;
    float bankbalance1 = 0;
    float updatebankbalance = 0;
    float totalb = 0;
    float totalpaidsellery = 0;
    int updatekey = 0;
    String itemid=null;

    /**
     * Creates new form Sallerypayment
     *
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.sql.SQLException
     */
    public Sallerypayment() throws IOException, FileNotFoundException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        Employer();
        balance();
        currentDate();
         AutoCompleteDecorator.decorate(employeebox);
         AutoCompleteDecorator.decorate(yearbox);
         AutoCompleteDecorator.decorate(monthbox);
    }

    public Sallerypayment(String Table_Click) throws IOException, FileNotFoundException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        Employer();
        balance();
        currentDate();
        updatekey = 1;
        Item_View(Table_Click);
        monthbox.setEnabled(false);
        yearbox.setEnabled(false);
        employeebox.setEnabled(false);
        paidamounttext.setEnabled(false);
        Bankinfobox.setEnabled(false);
        accountbox.setEnabled(false);
        paidamounttext.setEditable(false);
        chequenotext.setEditable(false);
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
        java.sql.Date date = new java.sql.Date(now.getTime());
        openingdate.setDate(date);
       
       Calendar cal = Calendar.getInstance();
       cal.setTime(date);
       int year=cal.get(Calendar.YEAR);
       String years=String.format("%d", year);
      yearbox.setSelectedItem(years);
    
       int month=date.getMonth();
        monthbox.setSelectedIndex(month);
       

    }

    private void Item_View(String Table_Click) {
        try {
            String sql = "Select id,emplyid,(select name from employeeinfo em where em.id=ems.emplyid) as 'name',inputdate,month,year,amount,paymentype,(select name from bankinfo bn where bn.id=ems.bankid) as 'bank',accNo,chequeNo,(select name from admin ad where ad.name=ems.inputuser) as 'admin',remark from emplyersallery ems where ems.id='" + Table_Click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                itemid= rs.getString("id");
                code = rs.getString("emplyid");
                String name = rs.getString("name");
                employeebox.setSelectedItem(name);
                Date inputdate = rs.getDate("inputdate");
                openingdate.setDate(inputdate);
                String month = rs.getString("month");
                monthbox.setSelectedItem(month);
                String year = rs.getString("year");
                yearbox.setSelectedItem(year);
                String amount = rs.getString("amount");
                paidamounttext.setText(amount);
                String paymentype = rs.getString("paymentype");
                paymenttypebox.setSelectedItem(paymentype);
                if (paymenttypebox.getSelectedIndex() > 0) {
                    String bank = rs.getString("bank");
                    Bankinfobox.addItem(bank);
                    Bankinfobox.setSelectedItem(bank);
                    String accNo = rs.getString("accNo");
                    accountbox.addItem(accNo);
                    accountbox.setSelectedItem(accNo);
                    String chequeNo = rs.getString("chequeNo");
                    chequenotext.setText(chequeNo);
                    String remark = rs.getString("remark");
                    remarktext.setText(remark);
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
  try {
            String sql = "Select id,disignation,startingsallery,paidsalery from employeeinfo where id='" + code + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                
                String disignation = rs.getString("disignation");
                designationtext.setText(disignation);

                String startingsallery = rs.getString("startingsallery");
                sallerytext.setText(startingsallery);

                String paidsalery = rs.getString("paidsalery");
                totalpaymenttext.setText(paidsalery);
                totalpaidsellery = rs.getFloat("paidsalery");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void Employer() throws SQLException {

        try {
            String sql = "Select name from employeeinfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("name");
                employeebox.addItem(name);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void addStock() throws SQLException {
        float amount = Float.parseFloat(paidamounttext.getText());
        try {

            String sql = "Insert into emplyersallery(emplyid,month,year,inputdate,amount,paymentype,bankid,accNo,chequeNo,remark,inputuser) values(?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, code);
            pst.setString(2, (String) monthbox.getSelectedItem());
            pst.setString(3, (String) yearbox.getSelectedItem());

            pst.setString(4, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
            pst.setString(5, paidamounttext.getText());
            pst.setString(6, (String) paymenttypebox.getSelectedItem());
            if(paymenttypebox.getSelectedIndex()>0){
             pst.setInt(7, bankid);

            pst.setString(8, (String) accountbox.getSelectedItem());
            pst.setString(9, chequenotext.getText());
            
            }else{
             pst.setInt(7, 0);

            pst.setString(8, "  ");
            pst.setString(9, "  ");
            
            }
          
            pst.setString(10, remarktext.getText());
            pst.setInt(11, userid);
            pst.execute();
            ResultSet rshere = pst.getGeneratedKeys();
            int generatedKey = 0;
            if (rshere.next()) {
                generatedKey = rshere.getInt(1);
                if (paymenttypebox.getSelectedIndex() == 0) {
                    cashInDrwaer(generatedKey);

                } else {
                    String accountno = (String) accountbox.getSelectedItem();
                    float presentbalnce = bankcashout + amount;
                    balanceupdatecashout(accountno, presentbalnce);

                    balanceamount(accountno);
                    totalbalance();
                    overallinsert(updatebankbalance, generatedKey);

                }

            }
            stockUpdate();
            JOptionPane.showMessageDialog(null, "Data Saved");
            clear();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void stockUpdate() {
        try {
            float amount = Float.parseFloat(paidamounttext.getText());
            float totalpaid = totalpaidsellery + amount;

            String sql = "Update employeeinfo set paidsalery='" + totalpaid + "' where id='" + code + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void overallinsert(float presentbalnce, int trainid) {

        float cashin = 0;
        float cashout = Float.parseFloat(paidamounttext.getText());
        try {

            String sql = "Insert into bankoverall(inputdate,Description,bank,AccountNo,cashin,cashout,Balance,prasentbalance,totalbalance,remark,transactionid) values(?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());

            pst.setString(2, "Sallery Payment");
            pst.setString(3, (String) Bankinfobox.getSelectedItem());
            pst.setString(4, (String) accountbox.getSelectedItem());
            pst.setFloat(5, cashin);
            pst.setFloat(6, cashout);
            pst.setFloat(7, accbalance);
            pst.setFloat(8, presentbalnce);
            pst.setFloat(9, totalb);

            pst.setString(10, "Sallery Payment");
            pst.setInt(11, trainid);

            pst.execute();

            // JOptionPane.showMessageDialog(null, "Data Saved");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalbalance() {

        try {
            String sql = "Select sum(cashin) as 'bankcashin',sum(cashout) as 'bankcashout' from bankaccount";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                float cashin = rs.getFloat("bankcashin");
                float cashout = rs.getFloat("bankcashout");
                totalb = cashin - cashout;

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void balanceupdatecashout(String accountno, float cahout) {
        try {
            String sql = "Update BankAccount set cashout='" + cahout + "' where accountno='" + accountno + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            // JOptionPane.showMessageDialog(null, "Data Update");
            //   clear();
            // totalbalance();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void balanceamount(String accountno) {

        try {
            String sql = "Select cashout,cashin from BankAccount where accountno='" + accountno + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                //accbalance = rs.getFloat("prasentbalance");
                float bankcashouts = rs.getFloat("cashout");
                float bankcashins = rs.getFloat("cashin");
                updatebankbalance = bankcashins - bankcashouts;

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void cashInDrwaer(int trainid) {
        //float cashout=paidamounttext.
        float cashout = Float.parseFloat(paidamounttext.getText());

        float balance = bankbalance1 - cashout;
        try {

            String sql = "Insert into CashDrower(cashin,cashout,balance,type,upatedate,tranid) values(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setFloat(1, 0);
            pst.setFloat(2, cashout);
            pst.setFloat(3, balance);
            pst.setString(4, "Sallery Payment");
            pst.setString(5, ((JTextField) openingdate.getDateEditor().getUiComponent()).getText());
            pst.setInt(6, trainid);
            pst.execute();

            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void clear() throws SQLException {
        monthbox.setSelectedIndex(0);
        yearbox.setSelectedIndex(0);
        ((JTextField) openingdate.getDateEditor().getUiComponent()).setText(null);
        paidamounttext.setText(null);
        paymenttypebox.setSelectedIndex(0);
        accountbox.setSelectedIndex(0);
        chequenotext.setText(null);
        remarktext.setText(null);
        balance();
        currentDate();

    }

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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        designationtext = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        yearbox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        sallerytext = new javax.swing.JTextField();
        openingdate = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        totalpaymenttext = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        paidamounttext = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarktext = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        paymenttypebox = new javax.swing.JComboBox<>();
        svbtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        monthbox = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        chequenotext = new javax.swing.JTextField();
        Bankinfobox = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        accountbox = new javax.swing.JComboBox<>();
        employeebox = new javax.swing.JComboBox<>();

        setClosable(true);
        setIconifiable(true);
        setTitle("Sallery Payment");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 0)));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 51, 0));
        jLabel2.setText("Payment Date");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 102, 0));
        jLabel3.setText("Employer");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 51, 0));
        jLabel4.setText("Year");

        designationtext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        designationtext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        designationtext.setEnabled(false);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Disignation");

        yearbox.setEditable(true);
        yearbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        yearbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030" }));
        yearbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                yearboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 51, 0));
        jLabel7.setText("Sallery");

        sallerytext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        sallerytext.setForeground(new java.awt.Color(204, 51, 0));
        sallerytext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        sallerytext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        sallerytext.setEnabled(false);
        sallerytext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sallerytextActionPerformed(evt);
            }
        });

        openingdate.setForeground(new java.awt.Color(102, 102, 102));
        openingdate.setDateFormatString("yyyy-MM-dd");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 51, 0));
        jLabel9.setText("Total Payment");

        totalpaymenttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalpaymenttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalpaymenttext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalpaymenttext.setEnabled(false);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 51, 0));
        jLabel10.setText("Amount");

        paidamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        paidamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paidamounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                paidamounttextKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                paidamounttextKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 51, 0));
        jLabel12.setText("Remark");

        remarktext.setColumns(20);
        remarktext.setRows(5);
        jScrollPane2.setViewportView(remarktext);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 51, 0));
        jLabel13.setText("Payment Type");

        paymenttypebox.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        paymenttypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Bank" }));
        paymenttypebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                paymenttypeboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        svbtn.setBackground(new java.awt.Color(255, 51, 0));
        svbtn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        svbtn.setForeground(new java.awt.Color(255, 255, 255));
        svbtn.setText("Execute");
        svbtn.setBorder(null);
        svbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svbtnActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 102, 0));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Clear");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        deletebtn.setBackground(new java.awt.Color(102, 0, 0));
        deletebtn.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        deletebtn.setForeground(new java.awt.Color(255, 255, 255));
        deletebtn.setText("Delete");
        deletebtn.setBorder(null);
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 51, 0));
        jLabel14.setText("Month");

        monthbox.setEditable(true);
        monthbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        monthbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "January", "February", "March", "April", "May", "June", "July", "Agust", "September", "October", "November", "December", " " }));
        monthbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                monthboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Bank Info(Payment)"));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel6.setText("Acc No");

        chequenotext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chequenotextActionPerformed(evt);
            }
        });

        Bankinfobox.setEditable(true);
        Bankinfobox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                BankinfoboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel15.setText("Bank Name");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel16.setText("Cheque No");

        accountbox.setEditable(true);
        accountbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                accountboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel6)
                    .addComponent(jLabel16))
                .addGap(90, 90, 90)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Bankinfobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(accountbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(chequenotext, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(Bankinfobox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(accountbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(chequenotext, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        employeebox.setEditable(true);
        employeebox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        employeebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        employeebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                employeeboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 498, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(openingdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(monthbox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(yearbox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 340, Short.MAX_VALUE)
                                    .addComponent(employeebox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 340, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, 0)
                                        .addComponent(deletebtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(paidamounttext, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalpaymenttext, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sallerytext, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(designationtext, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(paymenttypebox, 0, 340, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(24, 24, 24))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(openingdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(monthbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yearbox, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(employeebox, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(designationtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sallerytext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalpaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paidamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paymenttypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void yearboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_yearboxPopupMenuWillBecomeInvisible

    }//GEN-LAST:event_yearboxPopupMenuWillBecomeInvisible

    private void sallerytextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sallerytextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sallerytextActionPerformed

    private void paidamounttextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttextKeyPressed
        /*
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {
        } else {

            float paidamthere = Float.parseFloat(paidamounttext.getText());
            float amount = Float.parseFloat(sallerytext.getText());
            //float due=Float.parseFloat(duetext.getText());
            float finelpaidamnt = paidamtnprevoous + paidamthere;

            // float totalamt=amount-finelpaidamnt;
            if (finelpaidamnt > amount) {

                balanceduetext.setText(null);

            } else {
                float balanceDue = amount - finelpaidamnt;
                String Balancdus = String.format("%.2f", balanceDue);
                balanceduetext.setText(Balancdus);

            }

        }
        
         */
    }//GEN-LAST:event_paidamounttextKeyPressed

    private void paidamounttextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_paidamounttextKeyTyped

    }//GEN-LAST:event_paidamounttextKeyTyped

    private void paymenttypeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_paymenttypeboxPopupMenuWillBecomeInvisible
        Bankinfobox.removeAllItems();
        Bankinfobox.addItem("Select");
        Bankinfobox.setSelectedItem("Select");
        accountbox.removeAllItems();
        accountbox.addItem("Select");
        accountbox.setSelectedItem("Select");
        chequenotext.setText(null);

        if (paymenttypebox.getSelectedIndex() == 1) {
            String status = "Active";
            try {
                String sql = "Select Bankname from bankinfo where status='" + status + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {

                    String name = rs.getString("Bankname");
                    Bankinfobox.addItem(name);

                    //expensesearchbox.addItem(name);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_paymenttypeboxPopupMenuWillBecomeInvisible

    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed
        if (updatekey == 0) {
            try {
                balance();
            } catch (SQLException ex) {
                Logger.getLogger(Sallerypayment.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (monthbox.getSelectedIndex() == 0 || employeebox.getSelectedIndex() == 0 || paidamounttext.getText().isEmpty() || paidamounttext.getText().matches("^[a-zA-Z]+$")) {
                JOptionPane.showMessageDialog(null, "Please Complete Requirement Field");

            } else {
                float amount = Float.parseFloat(paidamounttext.getText());

                if (paymenttypebox.getSelectedIndex() == 0) {

                    if (amount > bankbalance1) {
                        JOptionPane.showMessageDialog(null, "Not Save!! Lack Of Cash Balance On Your Cash Box");

                    } else {
                        try {
                            addStock();
                        } catch (SQLException ex) {
                            Logger.getLogger(Sallerypayment.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                } else if (accbalance >= amount) {

                    try {
                        addStock();
                    } catch (SQLException ex) {
                        Logger.getLogger(Sallerypayment.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {

                    JOptionPane.showMessageDialog(null, "Fail To save, Amount Value More than preasent Value in this account");

                }

            }
        }
    }//GEN-LAST:event_svbtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        try {
            clear();

        } catch (SQLException ex) {
            Logger.getLogger(Purchasepayment.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton2ActionPerformed
       private void Selectbankaccount() {

        String accno = (String) accountbox.getSelectedItem();
        try {
            String sql = "Select cashout,cashin from BankAccount where accountno='" + accno + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                //accbalance = rs.getFloat("prasentbalance");
                bankcashout = rs.getFloat("cashout");
                bankcashin = rs.getFloat("cashin");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void creditpaymentdeleteDrwoerbank() {

        float paidamount = Float.parseFloat(paidamounttext.getText());
        float afterpaid = totalpaidsellery - paidamount;

        try {

            String sql = "Update employeeinfo set paidsalery='" + afterpaid + "'  where id='" + code + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
          
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        String reaon = "Sallery Payment";
        if (paymenttypebox.getSelectedIndex() == 0) {

            try {
                String sql = "Delete from CashDrower where type='" + reaon + "' AND tranid=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, itemid);

                pst.execute();
                //JOptionPane.showMessageDialog(null, "Data Deleted");

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        } else {
            //bank  payment delete
            try {
                String sql = "Delete from bankoverall where Description='" + reaon + "' AND transactionid=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, itemid);

                pst.execute();
                //JOptionPane.showMessageDialog(null, "Data Deleted");

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            Selectbankaccount();
            float amount = Float.parseFloat(paidamounttext.getText());
            float cashoutminus = bankcashin - amount;
            String accountno = (String) accountbox.getSelectedItem();
            String bank = (String) Bankinfobox.getSelectedItem();

            try {
                String sql = "Update BankAccount set cashin='" + cashoutminus + "' where accountno='" + accountno + "' AND bank='" + bank + "'";
                pst = conn.prepareStatement(sql);

                pst.execute();
                // JOptionPane.showMessageDialog(null, "Data Update");
                //   clear();
                // totalbalance();
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
        }

    }
    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
           if (updatekey == 1) {
            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from emplyersallery where id=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, itemid);
                    pst.execute();
                    // JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                creditpaymentdeleteDrwoerbank();
                try {
                    clear();
                } catch (SQLException ex) {
                    Logger.getLogger(Sallerypayment.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }//GEN-LAST:event_deletebtnActionPerformed

    private void monthboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_monthboxPopupMenuWillBecomeInvisible

    }//GEN-LAST:event_monthboxPopupMenuWillBecomeInvisible

    private void chequenotextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chequenotextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chequenotextActionPerformed

    private void BankinfoboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_BankinfoboxPopupMenuWillBecomeInvisible

        chequenotext.setText(null);
        if (Bankinfobox.getSelectedIndex() > 0) {

            String SearchText = (String) Bankinfobox.getSelectedItem();
            try {
                String sql = "Select id from bankinfo where Bankname='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                if (rs.next()) {

                    bankid = rs.getInt("id");

                    //expensesearchbox.addItem(name);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

            try {

                String sql = "Select accountno from bankaccount where bank='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                //  pst.setString(1, SearchText);

                rs = pst.executeQuery();
                while (rs.next()) {
                    String Acc = rs.getString("accountno");
                    accountbox.addItem(Acc);

                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_BankinfoboxPopupMenuWillBecomeInvisible

    private void accountboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_accountboxPopupMenuWillBecomeInvisible
        String accno = (String) accountbox.getSelectedItem();
        try {
            String sql = "Select cashout,cashin from BankAccount where accountno='" + accno + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {

                //accbalance = rs.getFloat("prasentbalance");
                bankcashout = rs.getFloat("cashout");
                bankcashin = rs.getFloat("cashin");
                accbalance = bankcashin - bankcashout;

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_accountboxPopupMenuWillBecomeInvisible

    private void employeeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_employeeboxPopupMenuWillBecomeInvisible
        try {
            String sql = "Select id,disignation,startingsallery,paidsalery from employeeinfo where name='" + employeebox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                code = rs.getString("id");
                String disignation = rs.getString("disignation");
                designationtext.setText(disignation);

                String startingsallery = rs.getString("startingsallery");
                sallerytext.setText(startingsallery);

                String paidsalery = rs.getString("paidsalery");
                totalpaymenttext.setText(paidsalery);
                totalpaidsellery = rs.getFloat("paidsalery");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_employeeboxPopupMenuWillBecomeInvisible


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> Bankinfobox;
    private javax.swing.JComboBox<String> accountbox;
    private javax.swing.JTextField chequenotext;
    private javax.swing.JButton deletebtn;
    private javax.swing.JTextField designationtext;
    private javax.swing.JComboBox<String> employeebox;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox<String> monthbox;
    private com.toedter.calendar.JDateChooser openingdate;
    private javax.swing.JTextField paidamounttext;
    private javax.swing.JComboBox<String> paymenttypebox;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JTextField sallerytext;
    private javax.swing.JButton svbtn;
    private javax.swing.JTextField totalpaymenttext;
    private javax.swing.JComboBox<String> yearbox;
    // End of variables declaration//GEN-END:variables
}
