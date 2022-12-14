/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class GRNReportframe extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    /**
     * Creates new form GRNReportframe
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public GRNReportframe() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        AutoCompleteDecorator.decorate(grntext);
        GRN();
    }

    private void GRN() throws SQLException {

        try {
            String sql = "Select GRNCode from grninfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("GRNCode");

                grntext.addItem(name);
                //  supliyerbox.setSelectedIndex(id);

            }
        } catch (Exception e) {
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
        jButton11 = new javax.swing.JButton();
        yeartext = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        todatepayment = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        fromdatepayment = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        grntext = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        prText = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        setClosable(true);
        setIconifiable(true);
        setTitle("GRN Details Report");

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton11.setBackground(new java.awt.Color(0, 204, 0));
        jButton11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Report");
        jButton11.setBorder(null);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(608, 115, 70, 30));

        yeartext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        yeartext.setForeground(new java.awt.Color(102, 0, 0));
        yeartext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jPanel1.add(yeartext, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 197, 257, 30));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 102, 0));
        jLabel17.setText("Year");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(78, 196, -1, 30));

        todatepayment.setDateFormatString("yyyy-MM-dd");
        todatepayment.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel1.add(todatepayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 155, 257, 30));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 102, 0));
        jLabel16.setText("To");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 155, -1, 25));

        fromdatepayment.setDateFormatString("yyyy-MM-dd");
        fromdatepayment.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel1.add(fromdatepayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 119, 257, 30));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 102, 0));
        jLabel15.setText("From");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 119, -1, 30));

        jButton2.setBackground(new java.awt.Color(0, 102, 51));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Submit");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(297, 238, 80, 36));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("GRN Code");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 16, -1, -1));

        grntext.setEditable(true);
        grntext.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        grntext.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        grntext.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                grntextPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        jPanel1.add(grntext, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 11, 246, 30));

        jButton1.setBackground(new java.awt.Color(0, 102, 0));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Load");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(165, 79, 103, 29));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Purchase No");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 47, -1, 26));

        prText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        prText.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0)));
        jPanel1.add(prText, new org.netbeans.lib.awtextra.AbsoluteConstraints(119, 47, 246, 26));

        jButton3.setBackground(new java.awt.Color(0, 153, 153));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Print");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(274, 79, 91, 29));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBounds(350, 100, 461, 401);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {

            //  int total = Integer.parseInt(AllTotalText.getText());
            //String inwords = convert(total) + " Tk only";
            String fromdate = ((JTextField) fromdatepayment.getDateEditor().getUiComponent()).getText();
            String todate = ((JTextField) todatepayment.getDateEditor().getUiComponent()).getText();

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/GrnOverall.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();
            para.put("fromdate", fromdate);
            para.put("todate", todate);
            para.put("year", yeartext.getText());
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);

        } catch (NumberFormatException | JRException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/GRN.jrxml");

            //   JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\ExpencessRiportbydate.jrxml");
            String grnNo = (String) grntext.getSelectedItem();
            String ParchaseNo = prText.getText();
            //  String GRNno =parchasetext.getText();
            HashMap para = new HashMap();
            para.put("ParchaseNo", ParchaseNo);
            para.put("GRNno", grnNo);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void grntextPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_grntextPopupMenuWillBecomeInvisible
        if (grntext.getSelectedIndex() == 0) {

        } else {
            String SearchText = (String) grntext.getSelectedItem();
            try {

                String sql = "Select gr.purchaseCode as 'purchaseNo' from GRNinfo gr where gr.GRNCode='" + SearchText + "'";
                pst = conn.prepareStatement(sql);
                //  pst.setString(1, SearchText);

                rs = pst.executeQuery();
                if (rs.next()) {

                  

                    String unitrate = rs.getString("purchaseNo");
                    prText.setText(unitrate);

                
                }

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }
    }//GEN-LAST:event_grntextPopupMenuWillBecomeInvisible

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
             try {

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/GRN.jrxml");

            //   JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\ExpencessRiportbydate.jrxml");
            String grnNo = (String) grntext.getSelectedItem();
            String ParchaseNo = prText.getText();
            //  String GRNno =parchasetext.getText();
            HashMap para = new HashMap();
            para.put("ParchaseNo", ParchaseNo);
            para.put("GRNno", grnNo);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
           // JasperViewer.viewReport(jp, false);
            JasperPrintManager.printReport(jp, true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);

        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser fromdatepayment;
    private javax.swing.JComboBox<String> grntext;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel prText;
    private com.toedter.calendar.JDateChooser todatepayment;
    private javax.swing.JTextField yeartext;
    // End of variables declaration//GEN-END:variables
}
