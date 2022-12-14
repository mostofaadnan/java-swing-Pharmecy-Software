/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Dimension;
import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author adnan
 */
public class SupplyerList extends javax.swing.JInternalFrame {
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String supid;
    /**
     * Creates new form SupplyerList
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public SupplyerList() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
       //table_update();
        supplyerbox();
        
    }
    private float totalconsign() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 6).toString());
        }

        return (float) totaltpmrp;

    }

    private float totalpayment() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 7).toString());
        }

        return (float) totaltpmrp;

    }

    private float totaldue() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 8).toString());
        }

        return (float) totaltpmrp;

    }

    private void table_update() throws SQLException {
        int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select id as 'Code',supliyername,ContactNo,suplType as 'Type',OpenigBalance,DipositAmt,	consighnmentamnt,paidamount,Balancedue,OpeningDate,(select name from admin ad where ad.id=sf.inputuser) as 'username' from suplyierInfo sf";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String id = rs.getString("Code");
                String name = rs.getString("supliyername");
                String type = rs.getString("Type");
                double opening = rs.getDouble("OpenigBalance");
                double DipositAmt = rs.getDouble("DipositAmt");
                double consighnmentamnt = rs.getDouble("consighnmentamnt");
                double paidamount = rs.getDouble("paidamount");
                double Balancedue = rs.getDouble("Balancedue");
                String OpeningDate = rs.getString("OpeningDate");
                String user = rs.getString("username");
                String contact=rs.getString("ContactNo");
                tree++;
                model2.addRow(new Object[]{tree, id, name, contact ,type, opening, DipositAmt, consighnmentamnt, paidamount, Balancedue, OpeningDate, user});
            }
            // datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
      totalcredit.setText(Float.toString(totalconsign()));
      totalrecieve.setText(Float.toString(totalpayment()));
      duetext.setText(Float.toString(totaldue()));
    }
 private void supplyerbox() throws SQLException {
     
     
        try {
            String sql = "Select supliyername from suplyierInfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String category = rs.getString("supliyername");
                suppliyername.addItem(category);

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
        edit = new javax.swing.JMenuItem();
        purchase = new javax.swing.JMenuItem();
        payment = new javax.swing.JMenuItem();
        delete = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        suppliyername = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        searchtext = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        totalcredit = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        totalrecieve = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        duetext = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();

        tablemenu.setPreferredSize(new java.awt.Dimension(300, 200));

        view.setText("View");
        view.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewActionPerformed(evt);
            }
        });
        tablemenu.add(view);

        edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });
        tablemenu.add(edit);

        purchase.setText("Purchase History");
        purchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseActionPerformed(evt);
            }
        });
        tablemenu.add(purchase);

        payment.setText("Payment History");
        payment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentActionPerformed(evt);
            }
        });
        tablemenu.add(payment);

        delete.setText("Delete");
        tablemenu.add(delete);

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Supplyer List");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Supplyer name");

        suppliyername.setEditable(true);
        suppliyername.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        suppliyername.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        suppliyername.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                suppliyernamePopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jButton1.setText("Load");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Search:");

        searchtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchtextKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchtext, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(suppliyername, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(138, 138, 138))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(suppliyername, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchtext))
                .addGap(20, 20, 20))
        );

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Total Asignment");

        totalcredit.setEditable(false);
        totalcredit.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total Payment:");

        totalrecieve.setEditable(false);
        totalrecieve.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Due");

        duetext.setEditable(false);
        duetext.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/if_plus_1646001 (1).png"))); // NOI18N
        jButton2.setText("New Supplier");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalcredit, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalrecieve, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(duetext, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(totalcredit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(totalrecieve, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(duetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );

        jScrollPane2.setBorder(null);
        jScrollPane2.setViewportBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        datatbl.setAutoCreateRowSorter(true);
        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "ID", "Supplier Name", "Contact", "Type", "Opening Balance", "Starting Due", "Consignment Amount", "Payment", "Due", "Opening Date", "Input User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false, false, false, false, false, false, false
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
        datatbl.getTableHeader().setResizingAllowed(false);
        datatbl.getTableHeader().setReorderingAllowed(false);
        datatbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatblMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setPreferredWidth(50);
            datatbl.getColumnModel().getColumn(2).setPreferredWidth(300);
            datatbl.getColumnModel().getColumn(7).setPreferredWidth(100);
            datatbl.getColumnModel().getColumn(11).setPreferredWidth(200);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void suppliyernamePopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_suppliyernamePopupMenuWillBecomeInvisible
    if(suppliyername.getSelectedIndex()==0){
        try {
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(SupplyerList.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }else{
      int tree = 0;
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select id as 'Code',supliyername,ContactNo,suplType as 'Type',OpenigBalance,DipositAmt,consighnmentamnt,paidamount,Balancedue,OpeningDate,(select name from admin ad where ad.id=sf.inputuser) as 'username' from suplyierInfo sf where supliyername='"+suppliyername.getSelectedItem()+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String id = rs.getString("Code");
                String name = rs.getString("supliyername");
                String type = rs.getString("Type");
                double opening = rs.getDouble("OpenigBalance");
                double DipositAmt = rs.getDouble("DipositAmt");
                double consighnmentamnt = rs.getDouble("consighnmentamnt");
                double paidamount = rs.getDouble("paidamount");
                double Balancedue = rs.getDouble("Balancedue");
                String OpeningDate = rs.getString("OpeningDate");
                String user = rs.getString("username");
                String contact=rs.getString("ContactNo");
                tree++;
                model2.addRow(new Object[]{tree, id, name,contact,type, opening, DipositAmt, consighnmentamnt, paidamount, Balancedue, OpeningDate, user});
            }
            // datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
      totalcredit.setText(Float.toString(totalconsign()));
      totalrecieve.setText(Float.toString(totalpayment()));
      duetext.setText(Float.toString(totaldue()));
    
    }
        
        

       
    }//GEN-LAST:event_suppliyernamePopupMenuWillBecomeInvisible

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
        if (SwingUtilities.isRightMouseButton(evt)) {
            tablemenu.show(evt.getComponent(), evt.getX() + 1, evt.getY() + 1);

        }
         int row = datatbl.getSelectedRow();
        supid = (datatbl.getModel().getValueAt(row, 1).toString());
    }//GEN-LAST:event_datatblMouseClicked

    private void viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewActionPerformed
         String table_click = supid;
       

        SupplierInfo filte = null;

         try {
            filte = new SupplierInfo(table_click);
        } catch (SQLException ex) {
            Logger.getLogger(Customerlist.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SupplyerList.class.getName()).log(Level.SEVERE, null, ex);
        }

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
    }//GEN-LAST:event_viewActionPerformed

    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
         String table_click = supid;
       

        SupplierInfo filte = null;

         try {
            filte = new SupplierInfo(table_click);
        } catch (SQLException ex) {
            Logger.getLogger(Customerlist.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SupplyerList.class.getName()).log(Level.SEVERE, null, ex);
        }

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
    }//GEN-LAST:event_editActionPerformed

    private void purchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseActionPerformed
        String table_click = supid;
         String pur="Purchase";

        SuplierProfile filte = null;

         
        try {
            filte = new SuplierProfile(table_click,pur);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(SupplyerList.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
    }//GEN-LAST:event_purchaseActionPerformed

    private void paymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentActionPerformed
         String table_click = supid;
         String pur="Payment";

        SuplierProfile filte = null;

         
        try {
            filte = new SuplierProfile(table_click,pur);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(SupplyerList.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        filte.setVisible(true);
        this.getDesktopPane().add(filte);
        Dimension desktopSize = getDesktopPane().getSize();
        Dimension jInternalFrameSize = filte.getSize();
        filte.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        filte.moveToFront();
    }//GEN-LAST:event_paymentActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(SupplyerList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
          SupplierInfo filte = null;

        try {
            filte = new SupplierInfo();
        } catch (SQLException ex) {
            Logger.getLogger(ItemList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SupplyerList.class.getName()).log(Level.SEVERE, null, ex);
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

    private void searchtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchtextKeyReleased
        String search=searchtext.getText();
        DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        if(search.isEmpty())
        {
        model2.setRowCount(0);
        }else{
        model2.setRowCount(0);
        int tree = 0;
        
        try {
            String sql = "Select id as 'Code',supliyername,ContactNo,suplType as 'Type',OpenigBalance,DipositAmt,consighnmentamnt,paidamount,Balancedue,OpeningDate,(select name from admin ad where ad.id=sf.inputuser) as 'username' from suplyierInfo sf  where sf.supliyername LIKE '%"+search+"%' OR sf.ContactNo LIKE '%"+search+"%' ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String id = rs.getString("Code");
                String name = rs.getString("supliyername");
                String type = rs.getString("Type");
                double opening = rs.getDouble("OpenigBalance");
                double DipositAmt = rs.getDouble("DipositAmt");
                double consighnmentamnt = rs.getDouble("consighnmentamnt");
                double paidamount = rs.getDouble("paidamount");
                double Balancedue = rs.getDouble("Balancedue");
                String OpeningDate = rs.getString("OpeningDate");
                String user = rs.getString("username");
                String contact=rs.getString("ContactNo");
                tree++;
                model2.addRow(new Object[]{tree, id, name, contact,type, opening, DipositAmt, consighnmentamnt, paidamount, Balancedue, OpeningDate, user});
            }
            // datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
      totalcredit.setText(Float.toString(totalconsign()));
      totalrecieve.setText(Float.toString(totalpayment()));
      duetext.setText(Float.toString(totaldue()));
      
        }
    }//GEN-LAST:event_searchtextKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable datatbl;
    private javax.swing.JMenuItem delete;
    private javax.swing.JTextField duetext;
    private javax.swing.JMenuItem edit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuItem payment;
    private javax.swing.JMenuItem purchase;
    private javax.swing.JTextField searchtext;
    private javax.swing.JComboBox<String> suppliyername;
    private javax.swing.JPopupMenu tablemenu;
    private javax.swing.JTextField totalcredit;
    private javax.swing.JTextField totalrecieve;
    private javax.swing.JMenuItem view;
    // End of variables declaration//GEN-END:variables
}
