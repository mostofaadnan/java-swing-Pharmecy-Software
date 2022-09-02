/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author adnan
 */
public class AdminInfo extends javax.swing.JFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    /**
     * Creates new form AdminInfo
     *
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.sql.SQLException
     */
    public AdminInfo() throws IOException, FileNotFoundException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
    }

    private void insertdata() throws IOException {
        try {
            int sts;
            if (statuscheck.isSelected()) {

                sts = 1;
            } else {
                sts = 0;

            }
            String sql = "Insert into admin(name,username,password,status) values(?,?,?,?)";
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, nametext.getText());
            pst.setString(2, usernametext.getText());
            pst.setString(3, password.getText());
            pst.setInt(4, sts);
            pst.execute();
            ResultSet rshere = pst.getGeneratedKeys();
            int generatedKey = 0;
            if (rshere.next()) {
                generatedKey = rshere.getInt(1);
                loginaccess(generatedKey);
                modificationAccsess(generatedKey);

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void loginaccess(int generatedKey) throws SQLException {

        String[] queries = {
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', ' User Panel ',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'System',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Sale Details',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Cash Invoice',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Credit Invoice',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Sale Return Details',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Bank Info',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Bank Acount',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Cash Drower',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Daily Purchase & Sale Item',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Credit Payment',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Vat Collection',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Day close',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Income',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Purchase',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Purchase Detalls',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'GRN Details',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Purchase Return',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Import/Export',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Import View',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Export View',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Item List',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Stock',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Wastage',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Label Print',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Purchase Payment',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Credit Payment',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Expensess',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Item Category',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Unit Management',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Expencess Type',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Country List',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Company Info',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Item Report',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Sale Report',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Cash Box Report',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Bank Statement',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'GRN Details',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Purchase Report',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Instance Check',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Customer Info',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Credit Statement',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Price Update',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Supplier List',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Supply Statement',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Database Backup',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Database Restore',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Stock Demand',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Offer List',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Item Forcast',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Supplier Tarif',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Supplyier Purchase List',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Supplier Payment List',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Supplier Forcast',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Import/Export Supplier',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Invoice Check',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Import Export Payment/Recieve',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Company Document',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Suppliyer Document',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Customer Document',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Database Information',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Employee List',1)",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', 'Sallery Management',1)",};

        Statement statement = conn.createStatement();

        for (String query : queries) {
            statement.execute(query);
        }
    }

    private void modificationAccsess(int userid) {
        try {

            String sql = "Insert into modificationaccsess(userid,itmedit,itmdelete,purcedit,purcdelete,imexedit,imexdelete,cshcrditedit,cshcrditdelete,prcpedit,purcpdelete,imexpedit,imexpdelete,creditpedit,creditpdelete,supedit,supdelete,cusedit,cusdelete) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setInt(1, userid);
            pst.setInt(2, 0);
            pst.setInt(3, 0);
            pst.setInt(4, 0);
            pst.setInt(5, 0);
            pst.setInt(6, 0);
            pst.setInt(7, 0);
            pst.setInt(8, 0);
            pst.setInt(9, 0);
            pst.setInt(10, 0);
            pst.setInt(11, 0);
            pst.setInt(12, 0);
            pst.setInt(13, 0);
            pst.setInt(14, 0);
            pst.setInt(15, 0);
            pst.setInt(16, 0);
            pst.setInt(17, 0);
            pst.setInt(18, 0);
            pst.setInt(19, 0);

            pst.execute();

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
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        nametext = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        usernametext = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        statuscheck = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Information");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setText("Execution");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Name");

        nametext.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        nametext.setForeground(new java.awt.Color(0, 102, 51));
        nametext.setToolTipText("Name");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Username");

        usernametext.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        usernametext.setForeground(new java.awt.Color(0, 102, 51));
        usernametext.setToolTipText("Username");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Password");

        password.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        password.setForeground(new java.awt.Color(0, 102, 51));

        statuscheck.setText("Status");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 51, 0));
        jLabel1.setText("Admin Information");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(96, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(statuscheck, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nametext, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(usernametext, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(49, 49, 49))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nametext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(usernametext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(statuscheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(603, 464));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            insertdata();
        } catch (IOException ex) {
            Logger.getLogger(AdminInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showMessageDialog(null, "Succesfuly Create Adminstration Panel");
        this.dispose();
        try {
            LoginAccess desh = new LoginAccess();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(AdminInfo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminInfo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new AdminInfo().setVisible(true);
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(AdminInfo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField nametext;
    private javax.swing.JPasswordField password;
    private javax.swing.JCheckBox statuscheck;
    private javax.swing.JTextField usernametext;
    // End of variables declaration//GEN-END:variables
}
