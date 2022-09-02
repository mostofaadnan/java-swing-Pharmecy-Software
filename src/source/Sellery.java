/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
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
public class Sellery extends javax.swing.JInternalFrame {
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String code=null;
    /**
     * Creates new form Sellery
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.sql.SQLException
     */
    public Sellery() throws IOException, FileNotFoundException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        DataView();
        Employer();
        AutoCompleteDecorator.decorate(employeebox);
    }
     private void Employer() throws SQLException {
        employeebox.removeAllItems();
        employeebox.addItem("Select");
        employeebox.setSelectedItem("Select");
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
private void DataView(){
    String description="Employeer: "+employeebox.getSelectedItem()+" Month: "+monthbox.getSelectedItem()+" Year "+yeartext.getText();
     descriptionmsg.setText(description);
 DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
     try {
            String sql = "Select id,(select name from employeeinfo em where em.id=ems.emplyid) as 'name',inputdate,month,year,amount,paymentype,(select name from bankinfo bn where bn.id=ems.bankid) as 'bank',accNo,chequeNo,(select name from admin ad where ad.name=ems.inputuser) as 'admin' from emplyersallery ems";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String id = rs.getString("id");
               String name = rs.getString("name");
               Date inputdate = rs.getDate("inputdate");
               String month = rs.getString("month");
               String year = rs.getString("year");
               double amount = rs.getDouble("amount");
               String paymentype = rs.getString("paymentype");
               String bank=rs.getString("bank");
               String accNo=rs.getString("accNo");
               String chequeNo=rs.getString("chequeNo");
               String admin=rs.getString("admin");
                tree++;
                model2.addRow(new Object[]{tree, id, name, inputdate,month, year, amount, paymentype, bank,accNo,chequeNo,admin});


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

        tablemenu = new javax.swing.JPopupMenu();
        view = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        employeebox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        monthbox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        yeartext = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        descriptionmsg = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();

        view.setText("View");
        view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewActionPerformed(evt);
            }
        });
        tablemenu.add(view);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Salary List");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Employer:");

        employeebox.setEditable(true);
        employeebox.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        employeebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", " " }));
        employeebox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                employeeboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Month:");

        monthbox.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        monthbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "January", "February", "March", "April", "May", "June", "July", "Agust", "September", "October", "November", "December", " " }));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Year");

        jButton1.setBackground(new java.awt.Color(0, 153, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 153, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Report Print");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        descriptionmsg.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        descriptionmsg.setForeground(new java.awt.Color(204, 204, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionmsg, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(employeebox, javax.swing.GroupLayout.PREFERRED_SIZE, 433, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(monthbox, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yeartext, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(employeebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(monthbox)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(yeartext)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptionmsg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "Payment Id", "Employeer", "Payment Date", "Month", "Year", "Amount", "Payment Type", "Bank", "Acc No", "Cheque No", "Input user"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        datatbl.setRowHeight(30);
        datatbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setResizable(false);
            datatbl.getColumnModel().getColumn(1).setResizable(false);
            datatbl.getColumnModel().getColumn(2).setResizable(false);
            datatbl.getColumnModel().getColumn(2).setPreferredWidth(300);
            datatbl.getColumnModel().getColumn(3).setResizable(false);
            datatbl.getColumnModel().getColumn(4).setResizable(false);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
            datatbl.getColumnModel().getColumn(8).setResizable(false);
            datatbl.getColumnModel().getColumn(9).setResizable(false);
            datatbl.getColumnModel().getColumn(10).setResizable(false);
            datatbl.getColumnModel().getColumn(11).setResizable(false);
            datatbl.getColumnModel().getColumn(11).setPreferredWidth(200);
        }

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setText("New Entry");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void employeeboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_employeeboxPopupMenuWillBecomeInvisible
   
        if(employeebox.getSelectedIndex()>0){
        String description="Employeer: "+employeebox.getSelectedItem()+" Month: "+monthbox.getSelectedItem()+" Year "+yeartext.getText();
       descriptionmsg.setText(description);
             try {
            String sql = "Select id from employeeinfo where name='" + employeebox.getSelectedItem() + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                code = rs.getString("id");
               
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
            
            
     DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
     try {
            String sql = "Select id,(select name from employeeinfo em where em.id=ems.emplyid) as 'name',inputdate,month,year,amount,paymentype,(select name from bankinfo bn where bn.id=ems.bankid) as 'bank',accNo,chequeNo,(select name from admin ad where ad.name=ems.inputuser) as 'admin' from emplyersallery ems where ems.emplyid='"+code+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String id = rs.getString("id");
               String name = rs.getString("name");
               Date inputdate = rs.getDate("inputdate");
               String month = rs.getString("month");
               String year = rs.getString("year");
               double amount = rs.getDouble("amount");
               String paymentype = rs.getString("paymentype");
               String bank=rs.getString("bank");
               String accNo=rs.getString("accNo");
               String chequeNo=rs.getString("chequeNo");
               String admin=rs.getString("admin");
                tree++;
                model2.addRow(new Object[]{tree, id, name, inputdate,month, year, amount, paymentype, bank,accNo,chequeNo,admin});


            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        }else{
            DataView();

        }


    }//GEN-LAST:event_employeeboxPopupMenuWillBecomeInvisible

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
          Sallerypayment filte = null;

        try {
            filte = new Sallerypayment();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Sellery.class.getName()).log(Level.SEVERE, null, ex);
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
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       String sql=null;
       String description="Employeer: "+employeebox.getSelectedItem()+" Month: "+monthbox.getSelectedItem()+" Year "+yeartext.getText();
       descriptionmsg.setText(description);
        if(employeebox.getSelectedIndex()<0 || monthbox.getSelectedIndex()<0 || yeartext.getText().equals("")==false){
         sql = "Select id,(select name from employeeinfo em where em.id=ems.emplyid) as 'name',inputdate,month,year,amount,paymentype,(select name from bankinfo bn where bn.id=ems.bankid) as 'bank',accNo,chequeNo,(select name from admin ad where ad.name=ems.inputuser) as 'admin' from emplyersallery ems Where year='"+yeartext.getText()+"'";
        }else if(employeebox.getSelectedIndex()>0 || monthbox.getSelectedIndex()>0 || yeartext.getText().equals("")==false) {
        sql = "Select id,(select name from employeeinfo em where em.id=ems.emplyid) as 'name',inputdate,month,year,amount,paymentype,(select name from bankinfo bn where bn.id=ems.bankid) as 'bank',accNo,chequeNo,(select name from admin ad where ad.name=ems.inputuser) as 'admin' from emplyersallery ems where ems.emplyid='"+code+"' AND ems.month='"+monthbox.getSelectedItem()+"' AND year='"+yeartext.getText()+"'";
   

        }
        else if(employeebox.getSelectedIndex()<0 || monthbox.getSelectedIndex()>0 || yeartext.getText().equals("")==false){
         sql = "Select id,(select name from employeeinfo em where em.id=ems.emplyid) as 'name',inputdate,month,year,amount,paymentype,(select name from bankinfo bn where bn.id=ems.bankid) as 'bank',accNo,chequeNo,(select name from admin ad where ad.name=ems.inputuser) as 'admin' from emplyersallery ems where ems.month='"+monthbox.getSelectedItem()+"' AND year='"+yeartext.getText()+"'";
            
      
        
        
        }else if(employeebox.getSelectedIndex()>0 || monthbox.getSelectedIndex()<0 || yeartext.getText().equals("")==false){
        
        sql = "Select id,(select name from employeeinfo em where em.id=ems.emplyid) as 'name',inputdate,month,year,amount,paymentype,(select name from bankinfo bn where bn.id=ems.bankid) as 'bank',accNo,chequeNo,(select name from admin ad where ad.name=ems.inputuser) as 'admin' from emplyersallery ems where ems.emplyid='"+code+"' AND year='"+yeartext.getText()+"'";
        
        }
        
        else{
        
         sql = "Select id,(select name from employeeinfo em where em.id=ems.emplyid) as 'name',inputdate,month,year,amount,paymentype,(select name from bankinfo bn where bn.id=ems.bankid) as 'bank',accNo,chequeNo,(select name from admin ad where ad.name=ems.inputuser) as 'admin' from emplyersallery ems ";
        
        }
        
       DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
         try {
          
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String id = rs.getString("id");
               String name = rs.getString("name");
               Date inputdate = rs.getDate("inputdate");
               String month = rs.getString("month");
               String year = rs.getString("year");
               double amount = rs.getDouble("amount");
               String paymentype = rs.getString("paymentype");
               String bank=rs.getString("bank");
               String accNo=rs.getString("accNo");
               String chequeNo=rs.getString("chequeNo");
               String admin=rs.getString("admin");
               tree++;
               model2.addRow(new Object[]{tree, id, name, inputdate,month, year, amount, paymentype, bank,accNo,chequeNo,admin});


            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
              DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        if (model2.getRowCount() == 0) {

            JOptionPane.showMessageDialog(null, "Data Table Is Empty");
        } else {

            try {
                
                //double totaltrade = Double.parseDouble(nettotaltradetext.getText());
                //double totalinvoice = Double.parseDouble(nettotalsalepricetext.getText());
                // double totalprofit = Double.parseDouble(profittext.getText());

                JRTableModelDataSource ItemJRBean = new JRTableModelDataSource(model2);
                JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/SellryReport.jrxml");

                Map<String,Object> para = new HashMap<>();
                // HashMap para = new HashMap();
                para.put("ItemDatasource", ItemJRBean);
                // para.put("product", producttextdata.getText());
                //  para.put("counttype", countdaytextdata.getText());
             //   para.put("totaltradeprice", totaltrade);
               // para.put("totalsaleprice", totalinvoice);
                para.put("description", descriptionmsg.getText());

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {

                JOptionPane.showMessageDialog(null, ex);

            }

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
    if (SwingUtilities.isRightMouseButton(evt)) {
            tablemenu.show(evt.getComponent(), evt.getX() + 1, evt.getY() + 1);

        }
    }//GEN-LAST:event_datatblMouseClicked

    private void viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewActionPerformed
        int row = datatbl.getSelectedRow();
            String table_click = (datatbl.getModel().getValueAt(row, 1).toString());
        Sallerypayment filte = null; 
        try {
                filte = new Sallerypayment(table_click);
            } catch (SQLException | IOException ex) {
                Logger.getLogger(PurchaseDetails.class.getName()).log(Level.SEVERE, null, ex);
            }

            filte.setVisible(true);
            this.getDesktopPane().add(filte);
              Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
    }//GEN-LAST:event_viewActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable datatbl;
    private javax.swing.JLabel descriptionmsg;
    private javax.swing.JComboBox<String> employeebox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> monthbox;
    private javax.swing.JPopupMenu tablemenu;
    private javax.swing.JMenuItem view;
    private javax.swing.JTextField yeartext;
    // End of variables declaration//GEN-END:variables
}
