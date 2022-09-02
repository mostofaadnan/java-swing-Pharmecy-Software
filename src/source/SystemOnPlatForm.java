/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;
import java.io.File;
import java.awt.HeadlessException;
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
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class SystemOnPlatForm extends javax.swing.JInternalFrame {

    static void EXIT_ON_CLOSE(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int userid =0;
    int itmedit, itmdelete, purcedit, purcdelete, imexedit, imexdelete, cshcrditedit, cshcrditdelete, prcpedit, purcpdelete, imexpedit, imexpdelete, creditpedit, creditpdelete, supedit, supdelete, cusedit, cusdelete;
   // Properties prop = new Properties();
    //OutputStream output = null;
    /**
     * Creates new form System
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public SystemOnPlatForm() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        user();
        AutoCompleteDecorator.decorate(userbox);
        systemLayout();
    }
private void systemLayout() throws FileNotFoundException, IOException{

  FileInputStream fis=new FileInputStream("src\\layout.properties"); 
       
       //  /abdulbasit?useUnicode=yes&characterEncoding=UTF-8
       
       //jdbc:mysql://localhost:3306
        Properties p=new Properties (); 
        p.load (fis);
         String laoutname= (String) p.get ("laoutname"); 
         themelayoutbox.setSelectedItem(laoutname);




}

private void StoreLicenseKey() throws FileNotFoundException, IOException {
      
       String layout=(String) themelayoutbox.getSelectedItem();
 
                try (OutputStream out = new FileOutputStream(new File("").getAbsolutePath()+"\\src\\layout.properties")) {
			Properties properties = new Properties();
			properties.setProperty("laoutname", layout);
			properties.store(out,
					null);
                         JOptionPane.showMessageDialog(null, "Successfully Save, This Layout Show Affter Restart Application");

		} catch (IOException e) {
			 JOptionPane.showMessageDialog(null, e);
		}
                
                
               

    }
    private void user() {
        try {
            String sql = "Select name from admin ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                userbox.addItem(name);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
private void checkall(){


                    itemedit.setSelected(true);
              
                    itemdelete.setSelected(true);
               

                    purchaseedit.setSelected(true);
              

                    purchasedelete.setSelected(true);
               

                    importexportedit.setSelected(true);
              

                    importexportdelete.setSelected(true);
                

                    cashcreditedit.setSelected(true);
             

                    cashcreditdelete.setSelected(true);
            

                    purchasepaymentedit.setSelected(true);
              

                    purchasepaymentdelete.setSelected(true);
            
                    exportimportpaymentedit.setSelected(true);
              
                    exportimportpaymentdelete.setSelected(true);
           

                    creditpaymentedit.setSelected(true);
               
                    creditpaymentdelete.setSelected(true);
              
                    supplierinfoedit.setSelected(true);
             

                    supplierdelete.setSelected(true);
               

                    customerinfoedit.setSelected(true);
             

                    customerinfodelete.setSelected(true);
               




}


private void checknull(){
   

                    itemedit.setSelected(false);
               
                    itemdelete.setSelected(false);

               
                    purchaseedit.setSelected(false);

                

                    purchasedelete.setSelected(false);

              
                    importexportedit.setSelected(false);

              
                    importexportdelete.setSelected(false);

                
                    cashcreditedit.setSelected(false);

                    cashcreditdelete.setSelected(false);


                    purchasepaymentedit.setSelected(false);

               
                    purchasepaymentdelete.setSelected(false);

               
                    exportimportpaymentedit.setSelected(false);


                    exportimportpaymentdelete.setSelected(false);

                
                    creditpaymentedit.setSelected(false);

                    creditpaymentdelete.setSelected(false);

               
                    supplierinfoedit.setSelected(false);

               
                    supplierdelete.setSelected(false);

               
                    customerinfoedit.setSelected(false);
                

                    customerinfodelete.setSelected(false);
                
}
    private void accessCheck() {
        try {
            String sql = "Select * from modificationaccsess where userid='" + userid + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                itmedit = rs.getInt("itmedit");
                itmdelete = rs.getInt("itmdelete");
                purcedit = rs.getInt("purcedit");
                purcdelete = rs.getInt("purcdelete");
                imexedit = rs.getInt("imexedit");
                imexdelete = rs.getInt("imexdelete");
                cshcrditedit = rs.getInt("cshcrditedit");
                cshcrditdelete = rs.getInt("cshcrditdelete");
                prcpedit = rs.getInt("prcpedit");
                purcpdelete = rs.getInt("purcpdelete");
                imexpedit = rs.getInt("imexpedit");
                imexpdelete = rs.getInt("imexpdelete");
                creditpedit = rs.getInt("creditpedit");
                creditpdelete = rs.getInt("creditpdelete");
                supedit = rs.getInt("supedit");
                supdelete = rs.getInt("supdelete");
                cusedit = rs.getInt("cusedit");

                cusdelete = rs.getInt("cusdelete");

                if (itmedit == 1) {

                    itemedit.setSelected(true);
                } else {

                    itemedit.setSelected(false);
                }
                if (itmdelete == 1) {

                    itemdelete.setSelected(true);
                } else {
                    itemdelete.setSelected(false);

                }
                if (purcedit == 1) {

                    purchaseedit.setSelected(true);
                } else {
                    purchaseedit.setSelected(false);

                }
                if (purcdelete == 1) {

                    purchasedelete.setSelected(true);
                } else {

                    purchasedelete.setSelected(false);

                }

                if (imexedit == 1) {

                    importexportedit.setSelected(true);
                } else {
                    importexportedit.setSelected(false);

                }

                if (imexdelete == 1) {

                    importexportdelete.setSelected(true);
                } else {
                    importexportdelete.setSelected(false);

                }

                if (cshcrditedit == 1) {

                    cashcreditedit.setSelected(true);
                } else {
                    cashcreditedit.setSelected(false);

                }
                if (cshcrditdelete == 1) {

                    cashcreditdelete.setSelected(true);
                } else {
                    cashcreditdelete.setSelected(true);

                }
                if (prcpedit == 1) {

                    purchasepaymentedit.setSelected(true);
                } else {

                    purchasepaymentedit.setSelected(false);

                }

                if (purcpdelete == 1) {

                    purchasepaymentdelete.setSelected(true);
                } else {
                    purchasepaymentdelete.setSelected(false);

                }

                if (imexpedit == 1) {
                  
                    exportimportpaymentedit.setSelected(true);
                } else {
                    exportimportpaymentedit.setSelected(false);

                }
                if (imexpdelete == 1) {
                 
                    exportimportpaymentdelete.setSelected(true);
                } else {

                    exportimportpaymentdelete.setSelected(false);

                }

                if (creditpedit == 1) {

                    creditpaymentedit.setSelected(true);
                } else {
                    creditpaymentedit.setSelected(false);

                }

                if (creditpdelete == 1) {
                    creditpaymentdelete.setSelected(true);
                } else {
                    creditpaymentdelete.setSelected(false);

                }

                if (supedit == 1) {
                   
                    supplierinfoedit.setSelected(true);
                } else {

                    supplierinfoedit.setSelected(false);

                }
                if (supdelete == 1) {

                    supplierdelete.setSelected(true);
                } else {

                    supplierdelete.setSelected(false);

                }
                if (cusedit == 1) {

                    customerinfoedit.setSelected(true);
                } else {

                    customerinfoedit.setSelected(false);
                }

                if (cusdelete == 1) {

                    customerinfodelete.setSelected(true);
                } else {

                    customerinfodelete.setSelected(false);
                }
            }else{
            checknull();
            
            
            
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void modification_update() {

        if (itemedit.isSelected()) {
            itmedit = 1;
        } else {
            itmedit = 0;

        }
        if (itemdelete.isSelected()) {
            itmdelete = 1;
        } else {
            itmdelete = 0;

        }
        if (purchaseedit.isSelected()) {
            purcedit = 1;
        } else {
            purcedit = 0;

        }
        if (purchasedelete.isSelected()) {
            purcdelete = 1;
        } else {
            purcdelete = 0;

        }

        if (importexportedit.isSelected()) {
            imexedit = 1;
        } else {
            imexedit = 0;

        }

        if (importexportdelete.isSelected()) {
            imexdelete = 1;
        } else {
            imexdelete = 0;

        }

        if (cashcreditedit.isSelected()) {
            cshcrditedit = 1;
        } else {
            cshcrditedit = 0;

        }
        if (cashcreditdelete.isSelected()) {
            cshcrditdelete = 1;
        } else {
            cshcrditdelete = 0;

        }
        if (purchasepaymentedit.isSelected()) {
            prcpedit = 1;
        } else {
            prcpedit = 0;

        }

        if (purchasepaymentdelete.isSelected()) {
            purcpdelete = 1;
        } else {
            purcpdelete = 0;

        }

        if (exportimportpaymentedit.isSelected()) {
            imexpedit = 1;
        } else {
            imexpedit = 0;

        }
        if (exportimportpaymentdelete.isSelected()) {
            imexpdelete = 1;
        } else {
            imexpdelete = 0;

        }

        if (creditpaymentedit.isSelected()) {
            creditpedit = 1;
        } else {
            creditpedit = 0;

        }

        if (creditpaymentdelete.isSelected()) {
            creditpdelete = 1;
        } else {
            creditpdelete = 0;

        }

        if (supplierinfoedit.isSelected()) {
            supedit = 1;
        } else {
            supedit = 0;

        }
        if (supplierdelete.isSelected()) {
            supdelete = 1;
        } else {
            supdelete = 0;

        }
        if (customerinfoedit.isSelected()) {
            cusedit = 1;
        } else {
            cusedit = 0;

        }

        if (customerinfodelete.isSelected()) {
            cusdelete = 1;
        } else {
            cusdelete = 0;

        }

        try {

            //String id = codetext.getText();
            String sql = "Update modificationaccsess set itmedit='" + itmedit + "',itmdelete='" + itmdelete + "',purcedit='" + purcedit + "',purcdelete='" + purcdelete + "',imexedit='" + imexedit + "',imexdelete='" + imexdelete + "',cshcrditedit='" + cshcrditedit + "',cshcrditdelete='" + cshcrditdelete + "',prcpedit='" + prcpedit + "',purcpdelete='" + purcpdelete + "',imexpedit='" + imexpedit + "',imexpdelete='" + imexpdelete + "',creditpedit='" + creditpedit + "',purcedit='" + purcedit + "',creditpdelete='" + creditpdelete + "',supedit='" + supedit + "',supdelete='" + supdelete + "',cusedit='" + cusedit + "',cusdelete='" + cusdelete + "' Where userid='" + userid + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            
            
            
             JOptionPane.showMessageDialog(null, "Data Update Successfully");
          

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
        jLabel1 = new javax.swing.JLabel();
        layoutbox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        userbox = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        itemedit = new javax.swing.JCheckBox();
        itemdelete = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        purchaseedit = new javax.swing.JCheckBox();
        purchasedelete = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        importexportedit = new javax.swing.JCheckBox();
        importexportdelete = new javax.swing.JCheckBox();
        jPanel9 = new javax.swing.JPanel();
        cashcreditedit = new javax.swing.JCheckBox();
        cashcreditdelete = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        purchasepaymentedit = new javax.swing.JCheckBox();
        purchasepaymentdelete = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        exportimportpaymentedit = new javax.swing.JCheckBox();
        exportimportpaymentdelete = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        creditpaymentedit = new javax.swing.JCheckBox();
        creditpaymentdelete = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        supplierinfoedit = new javax.swing.JCheckBox();
        supplierdelete = new javax.swing.JCheckBox();
        jPanel14 = new javax.swing.JPanel();
        customerinfoedit = new javax.swing.JCheckBox();
        customerinfodelete = new javax.swing.JCheckBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        themelayoutbox = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();

        jLabel1.setText("System Layout");

        layoutbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Metal", "Nimbus", "CDE/Motif", "Windows", "Windows Classic" }));

        jButton1.setText("Apply");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(layoutbox, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(90, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(19, 19, 19))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(layoutbox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 338, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        setClosable(true);
        setIconifiable(true);
        setTitle("System Information");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("User");

        userbox.setEditable(true);
        userbox.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        userbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        userbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                userboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jPanel5.setBackground(new java.awt.Color(0, 51, 51));

        jButton3.setBackground(new java.awt.Color(153, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Execution");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Item Modification"));

        itemedit.setBackground(new java.awt.Color(255, 255, 255));
        itemedit.setText("Edit");

        itemdelete.setBackground(new java.awt.Color(255, 255, 255));
        itemdelete.setText("Delete");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(itemedit)
                    .addComponent(itemdelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(itemedit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemdelete))
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Purchase & Sale Modification"));

        purchaseedit.setBackground(new java.awt.Color(255, 255, 255));
        purchaseedit.setText("Edit");

        purchasedelete.setBackground(new java.awt.Color(255, 255, 255));
        purchasedelete.setText("Delete");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(purchaseedit)
                    .addComponent(purchasedelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(purchaseedit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(purchasedelete))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Import/Export Modification"));

        importexportedit.setBackground(new java.awt.Color(255, 255, 255));
        importexportedit.setText("Edit");

        importexportdelete.setBackground(new java.awt.Color(255, 255, 255));
        importexportdelete.setText("Delete");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(importexportedit)
                    .addComponent(importexportdelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(importexportedit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(importexportdelete))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Cash/Credit Modification"));

        cashcreditedit.setBackground(new java.awt.Color(255, 255, 255));
        cashcreditedit.setText("Edit");

        cashcreditdelete.setBackground(new java.awt.Color(255, 255, 255));
        cashcreditdelete.setText("Delete");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cashcreditedit)
                    .addComponent(cashcreditdelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cashcreditedit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashcreditdelete))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Purchase Payment Modification"));

        purchasepaymentedit.setBackground(new java.awt.Color(255, 255, 255));
        purchasepaymentedit.setText("Edit");

        purchasepaymentdelete.setBackground(new java.awt.Color(255, 255, 255));
        purchasepaymentdelete.setText("Delete");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(purchasepaymentedit)
                    .addComponent(purchasepaymentdelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(purchasepaymentedit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(purchasepaymentdelete))
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Export/Import Payment Modification"));

        exportimportpaymentedit.setBackground(new java.awt.Color(255, 255, 255));
        exportimportpaymentedit.setText("Edit");

        exportimportpaymentdelete.setBackground(new java.awt.Color(255, 255, 255));
        exportimportpaymentdelete.setText("Delete");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(exportimportpaymentedit)
                    .addComponent(exportimportpaymentdelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(exportimportpaymentedit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportimportpaymentdelete))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Credit Payment"));

        creditpaymentedit.setBackground(new java.awt.Color(255, 255, 255));
        creditpaymentedit.setText("Edit");

        creditpaymentdelete.setBackground(new java.awt.Color(255, 255, 255));
        creditpaymentdelete.setText("Delete");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(creditpaymentedit)
                    .addComponent(creditpaymentdelete))
                .addContainerGap(378, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(creditpaymentedit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(creditpaymentdelete))
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Supplier Information"));

        supplierinfoedit.setBackground(new java.awt.Color(255, 255, 255));
        supplierinfoedit.setText("Edit");

        supplierdelete.setBackground(new java.awt.Color(255, 255, 255));
        supplierdelete.setText("Delete");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(supplierinfoedit)
                    .addComponent(supplierdelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(supplierinfoedit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(supplierdelete))
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Information"));

        customerinfoedit.setBackground(new java.awt.Color(255, 255, 255));
        customerinfoedit.setText("Edit");

        customerinfodelete.setBackground(new java.awt.Color(255, 255, 255));
        customerinfodelete.setText("Delete");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customerinfoedit)
                    .addComponent(customerinfodelete))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(customerinfoedit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(customerinfodelete))
        );

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox1.setText("Check All");
        jCheckBox1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCheckBox1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel4);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(userbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Acceess", jPanel3);

        jLabel3.setText("Layout Name:");

        themelayoutbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Metal", "Nimbus", "Windows", "Windows Classic", "Seaglass", "JGoodies", "Quaqua ", "Napkin LaF", "Synthetica", "Liquidlnf", "JTatoo" }));

        jButton4.setBackground(new java.awt.Color(0, 102, 0));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Execution");
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
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(themelayoutbox, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(themelayoutbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(392, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Theme Layout", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        setBounds(230, 50, 523, 543);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

    private void userboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_userboxPopupMenuWillBecomeInvisible
        if (userbox.getSelectedIndex() > 0) {

            try {
                String sql = "Select id from admin where name='" + userbox.getSelectedItem() + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next()) {
                    userid = rs.getInt("id");
              
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
           accessCheck();
        }else{
        checknull();
        
        
        }
    }//GEN-LAST:event_userboxPopupMenuWillBecomeInvisible

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (userbox.getSelectedIndex() > 0) {
            modification_update();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCheckBox1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCheckBox1MouseClicked
       if(jCheckBox1.isSelected()){
       checkall();
       
       
       
       }else{
        checknull();
       
       }
    }//GEN-LAST:event_jCheckBox1MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            StoreLicenseKey();
        } catch (IOException ex) {
            Logger.getLogger(SystemOnPlatForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            systemLayout();
        } catch (IOException ex) {
            Logger.getLogger(SystemOnPlatForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cashcreditdelete;
    private javax.swing.JCheckBox cashcreditedit;
    private javax.swing.JCheckBox creditpaymentdelete;
    private javax.swing.JCheckBox creditpaymentedit;
    private javax.swing.JCheckBox customerinfodelete;
    private javax.swing.JCheckBox customerinfoedit;
    private javax.swing.JCheckBox exportimportpaymentdelete;
    private javax.swing.JCheckBox exportimportpaymentedit;
    private javax.swing.JCheckBox importexportdelete;
    private javax.swing.JCheckBox importexportedit;
    private javax.swing.JCheckBox itemdelete;
    private javax.swing.JCheckBox itemedit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JComboBox<String> layoutbox;
    private javax.swing.JCheckBox purchasedelete;
    private javax.swing.JCheckBox purchaseedit;
    private javax.swing.JCheckBox purchasepaymentdelete;
    private javax.swing.JCheckBox purchasepaymentedit;
    private javax.swing.JCheckBox supplierdelete;
    private javax.swing.JCheckBox supplierinfoedit;
    private javax.swing.JComboBox<String> themelayoutbox;
    private javax.swing.JComboBox<String> userbox;
    // End of variables declaration//GEN-END:variables
}
