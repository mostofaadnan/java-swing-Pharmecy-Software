/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 *
 * @author adnan
 */
public class LoginFrame extends javax.swing.JFrame {

    int xMouse;
    int yMouse;

    //public  int LoginUser;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    LicenseKey licensekey = new LicenseKey();
    String[] keyarray = licensekey.key;
    String CompanyName;
    FileInputStream fis = new FileInputStream("src\\licensekey.properties");
    Properties p = new Properties();
    int LoginUser;
    
     Properties prop = new Properties();
    OutputStream output = null;
    /**
     * Creates new form LoginFrame
     *
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.sql.SQLException
     */
    public LoginFrame() throws IOException, FileNotFoundException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        report();
        CompanyInfo();
        companycheck();
        intilize();
       // jLabel3.hide();
      //  companytext.setText("TIMS");
       // descriptiontext1.hide();
        

    }

    private void intilize() {

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("company.png")));
    }
    private void StoreUserKey(int ukey) throws FileNotFoundException, IOException {
       
       
        String key=String.format("%d", ukey);
        output = new FileOutputStream("src\\userkey.properties");
 
		prop.setProperty("userid", key);
		

		// save properties to project root folder
		prop.store(output, null);
              

    }
    private void LoginProcess() throws IOException {
        if (UserName.getText().isEmpty() || Password.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Please Enter User Name/password");
        } else {

            String sql = "Select * from admin where UserName=? and Password=?";
            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, UserName.getText());
                pst.setString(2, Password.getText());
                ///   pst.setInt(3, type);
                rs = pst.executeQuery();

                if (rs.next() == true) {

                     LoginUser = rs.getInt("id");
                     StoreUserKey(LoginUser);   
                    // JOptionPane.showMessageDialog(null, "Username and password Correct");
                  
                    //    FileInputStream fis = new FileInputStream("src\\licensekey.properties");
                    p.load(fis);

                    String acesskey = (String) p.get("key");

                    boolean found = false;
                    for (String element : keyarray) {
                        if (element.equals(acesskey)) {
                            found = true;
                            // LoginAccess desboard=new LoginAccess();
                            String company = licensekey.company(acesskey);
                            if (CompanyName == null ? company == null : CompanyName.equals(company)) {
                                dispose();
                                Dashboard view = new Dashboard(LoginUser);
                                view.setVisible(true);
                            } else {
                                dispose();
                                LicenceRegistration licenseregistration = new LicenceRegistration();
                                licenseregistration.setVisible(true);

                            }
                        }
                    }
                    if (!found) {
                        LicenceRegistration licenseregistration = new LicenceRegistration();
                        licenseregistration.setVisible(true);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Username and password not Correct");

                }

            } catch (SQLException | HeadlessException e) {

                JOptionPane.showMessageDialog(null, e);

            }
        }

    }

    private void report() throws SQLException, IOException {
         p.load(fis);
        String acesskey = (String) p.get("key");
        String companyS = licensekey.company(acesskey);
        try {
            String sql = "Select companyname from reportdesign where id=1;";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

               String cname=rs.getString("companyname");
                if(cname==null || (cname == null ? companyS != null : !cname.equals(companyS))){
                
                 updatereport();
                
                
                }

            } 
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void CompanyInfo() throws SQLException, IOException {

        try {
            String sql = "Select companyname,address from companyinfo where id=1;";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String cname=rs.getString("companyname");
                String Address=rs.getString("address");
                descriptiontext1.setText(Address);
                if(cname.isEmpty()){
                
                 companyupdate();
                
                
                }
                
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void updatereport() throws IOException {
        p.load(fis);
        String acesskey = (String) p.get("key");
         //JOptionPane.showMessageDialog(null, acesskey);
        String companyS = licensekey.company(acesskey);
        try {

            String id = "1";

            String sql = "Update reportdesign set companyname='" + companyS + "' where id='" + id + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            // JOptionPane.showMessageDialog(null, "Execution Complted");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void companyupdate() throws IOException {
        p.load(fis);
        String acesskey = (String) p.get("key");
        String companyS = licensekey.company(acesskey);

        try {

            String id = "1";
            String sql = "Update companyinfo set companyname='" + companyS + "' where id='" + id + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();
            // JOptionPane.showMessageDialog(null, "Execution Complted");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
 private void companycheck() throws SQLException, IOException {

        try {
            String sql = "Select companyname,headeraddress from reportdesign where id=1;";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                CompanyName = rs.getString("companyname");
                this.setTitle("Login Frame-" + CompanyName);
             //   companytext.setText(CompanyName);
                String Companyaddress=rs.getString("headeraddress");
             

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

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        companytext = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        UserName = new javax.swing.JTextField();
        Password = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        descriptiontext1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 425, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 272, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login Frame");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));

        companytext.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        companytext.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        companytext.setText("National Pharmacy");
        companytext.setBorder(null);

        jLabel6.setFont(new java.awt.Font("Tahoma", 2, 11)); // NOI18N
        jLabel6.setText("Powerd By vosoft");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 0, 51));
        jLabel5.setText("Password");

        UserName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        UserName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 0), 1, true));

        Password.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        Password.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 0), 1, true));
        Password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PasswordKeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 0, 51));
        jLabel2.setText("User Name");

        jButton1.setBackground(new java.awt.Color(102, 0, 0));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Login");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        descriptiontext1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        descriptiontext1.setForeground(new java.awt.Color(0, 51, 51));
        descriptiontext1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descriptiontext1.setText("Address");
        descriptiontext1.setBorder(null);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo-sm.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Password)
                            .addComponent(UserName, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(companytext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(descriptiontext1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(2186, 2186, 2186))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(companytext, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(descriptiontext1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(UserName, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jLabel6)
                        .addGap(45, 45, 45))))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 340));

        setSize(new java.awt.Dimension(694, 356));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            LoginProcess();
            
        } catch (IOException ex) {
            Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void PasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PasswordKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            try {
                LoginProcess();
            } catch (IOException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_PasswordKeyPressed

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
            java.util.logging.Logger.getLogger(LoginFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new LoginFrame().setVisible(true);
            } catch (IOException | SQLException ex) {
                Logger.getLogger(LoginFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField Password;
    private javax.swing.JTextField UserName;
    private javax.swing.JLabel companytext;
    private javax.swing.JLabel descriptiontext1;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
