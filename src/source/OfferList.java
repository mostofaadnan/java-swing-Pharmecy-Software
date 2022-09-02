/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author adnan
 */
public class OfferList extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    Dashboard dashboardhere = new Dashboard();
    int userid = dashboardhere.id;
    java.sql.Date date;
    int updatekey = 0;
    int offerid = 0;

    /**
     * Creates new form OfferList
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public OfferList() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        currentDate();
        table_update();
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        //  inputdate.setDate(date);

    }

    private void stockUpdate() {
        int sts=0;
        if(statusbox.getSelectedIndex()==0){
        sts=1;
        
        }else{
        sts=0;
        }
        try {

            String sql = "Update offerlist set offername='" + offernamebox.getSelectedItem() + "',startdate='" + ((JTextField) startdate.getDateEditor().getUiComponent()).getText() + "',enddate='" + ((JTextField) enddate.getDateEditor().getUiComponent()).getText() + "',remark='" + remarktext.getText() + "',Status='"+sts+"' where id='" + offerid + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Update");
            updatekey = 0;
            clear();
            table_update();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void addStock() throws SQLException {
         int status=0;
         
         
         if(statusbox.getSelectedIndex()==0){
         
         status=1;
         
         }else{
         status=0;
         
         }
        try {

            String sql = "Insert into offerlist(offername,startdate,enddate,remark,Status,inputdate,inputuser) values(?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setString(1, (String) offernamebox.getSelectedItem());
            pst.setString(2, ((JTextField) startdate.getDateEditor().getUiComponent()).getText());
            pst.setString(3, ((JTextField) enddate.getDateEditor().getUiComponent()).getText());
            pst.setString(4, remarktext.getText());
            pst.setInt(5, status);
            pst.setDate(6, date);
            pst.setInt(7, userid);

            pst.execute();

            JOptionPane.showMessageDialog(null, "Data Saved");
            clear();
            table_update();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void clear() {
        offernamebox.setSelectedItem(null);
        startdate.setDate(null);
        startdate.setDate(null);
        enddate.setDate(null);
        remarktext.setText(null);
        updatekey=0;

    }

    private void table_update() throws SQLException {
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        int tree = 0;
        try {
            String sql = "Select id as 'Code',offername as 'OfferTitle',startdate as 'StartDate',enddate as 'EndDate',Status,remark as 'Remark',inputdate as 'InputDate',(select name from admin ad where ad.id=of.inputuser) as 'InputUser',(select count(procode) from offerdetails ofd where ofd.offerid=of.id) as 'Totalitem' from offerlist of";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String id = rs.getString("Code");
                String OfferTitle = rs.getString("OfferTitle");
                String StartDate = rs.getString("StartDate");
                String EndDate = rs.getString("EndDate");
                String remark = rs.getString("Remark");
                String InputDate = rs.getString("InputDate");
                String inputuser = rs.getString("InputUser");
                int status=rs.getInt("Status");
                String statusstring=null;
                
                if(status==1){
                statusstring="Active";
                
                }else{
                statusstring="Di-Active";
                
                }
                
                        
                        
                int Totalitem = rs.getInt("Totalitem");

                tree++;

                model2.addRow(new Object[]{tree, id, OfferTitle, StartDate, EndDate, Totalitem, InputDate,statusstring ,remark, inputuser});

            }

            //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
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
        aditem = new javax.swing.JMenuItem();
        itemclear = new javax.swing.JMenuItem();
        deleteoffer = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        offernamebox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        startdate = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        enddate = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        remarktext = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        statusbox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();

        tablemenu.setPreferredSize(new java.awt.Dimension(300, 140));

        aditem.setText("View And Add/Edit Item");
        aditem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aditemActionPerformed(evt);
            }
        });
        tablemenu.add(aditem);

        itemclear.setText("Clear All Offer Item");
        itemclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemclearActionPerformed(evt);
            }
        });
        tablemenu.add(itemclear);

        deleteoffer.setText("Delete Offer");
        deleteoffer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteofferActionPerformed(evt);
            }
        });
        tablemenu.add(deleteoffer);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("OfferList");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Offer Name:");

        offernamebox.setEditable(true);
        offernamebox.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Start Date");

        startdate.setDateFormatString("yyyy-MM-dd");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("End Date");

        enddate.setDateFormatString("yyyy-MM-dd");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Comment:");

        remarktext.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        remarktext.setBorder(null);

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Execute");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setText("Delete");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setText("Load");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Status");

        statusbox.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        statusbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Di-Active" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(offernamebox, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(10, 10, 10)
                        .addComponent(startdate, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(remarktext))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(2, 2, 2)
                        .addComponent(enddate, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jButton1)
                        .addGap(1, 1, 1)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(statusbox, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(statusbox)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(startdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(enddate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(offernamebox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(remarktext))
                .addGap(5, 5, 5))
        );

        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SI No", "Offer ID", "Offer Name", "Start Date", "Endate", "Item(Count)", "Input Date", "Status", "Remark", "Input User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true, false, false
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
            datatbl.getColumnModel().getColumn(8).setResizable(false);
            datatbl.getColumnModel().getColumn(9).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (updatekey == 1) {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from offerlist where id=?";
                    pst = conn.prepareStatement(sql);

                    pst.setInt(1, offerid);

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                
                  try {
                    String sql = "Delete from offerdetails where offerid=?";
                    pst = conn.prepareStatement(sql);

                    pst.setInt(1, offerid);

                    pst.execute();
                    

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }
            clear();

            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(OfferList.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (updatekey == 0) {
            try {
                addStock();
            } catch (SQLException ex) {
                Logger.getLogger(OfferList.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            stockUpdate();

        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
        if (SwingUtilities.isRightMouseButton(evt)) {
            tablemenu.show(evt.getComponent(), evt.getX() + 1, evt.getY() + 1);

        }

        int row = datatbl.getSelectedRow();
        String table_click = (String) datatbl.getValueAt(row, 1);

        updatekey = 1;

        try {
            String sql = "Select * from offerlist where id='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            if (rs.next()) {
                offerid = rs.getInt("id");
                String name = rs.getString("offername");
                offernamebox.setSelectedItem(name);
                Date strtdate = rs.getDate("startdate");
                startdate.setDate(strtdate);
                Date endate = rs.getDate("enddate");
                enddate.setDate(endate);
                String remark = rs.getString("remark");
                remarktext.setText(remark);
                int Status=rs.getInt("Status");
                String statusst=null;
                if(Status==1){
                statusst="Active";
                
                }else {
                statusst="Di-Active";
                }
                statusbox.setSelectedItem(statusst);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }//GEN-LAST:event_datatblMouseClicked

    private void aditemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aditemActionPerformed
       // int row = datatbl.getSelectedRow();
     //   int table_click = (Integer) datatbl.getValueAt(row, 1);

        OfferDetails newitemadd = null;
        try {
            newitemadd = new OfferDetails(offerid);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(OfferList.class.getName()).log(Level.SEVERE, null, ex);
        }
        newitemadd.setVisible(true);
        this.getDesktopPane().add(newitemadd);
        newitemadd.setEnabled(true);
        try {
            newitemadd.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
        }
        //this.dispose();
        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = newitemadd.getSize();
        newitemadd.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        newitemadd.moveToFront();
    }//GEN-LAST:event_aditemActionPerformed

    private void itemclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemclearActionPerformed
        if (updatekey == 1) {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to Clear Offer Item", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
             
                
                  try {
                    String sql = "Delete from offerdetails where offerid=?";
                    pst = conn.prepareStatement(sql);

                    pst.setInt(1, offerid);

                    pst.execute();
                    

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }
            clear();

            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(OfferList.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_itemclearActionPerformed

    private void deleteofferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteofferActionPerformed
         if (updatekey == 1) {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from offerlist where id=?";
                    pst = conn.prepareStatement(sql);

                    pst.setInt(1, offerid);

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
                
                  try {
                    String sql = "Delete from offerdetails where offerid=?";
                    pst = conn.prepareStatement(sql);

                    pst.setInt(1, offerid);

                    pst.execute();
                    

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }
            clear();

            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(OfferList.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_deleteofferActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(OfferList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aditem;
    private javax.swing.JTable datatbl;
    private javax.swing.JMenuItem deleteoffer;
    private com.toedter.calendar.JDateChooser enddate;
    private javax.swing.JMenuItem itemclear;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> offernamebox;
    private javax.swing.JTextField remarktext;
    private com.toedter.calendar.JDateChooser startdate;
    private javax.swing.JComboBox<String> statusbox;
    private javax.swing.JPopupMenu tablemenu;
    // End of variables declaration//GEN-END:variables
}
