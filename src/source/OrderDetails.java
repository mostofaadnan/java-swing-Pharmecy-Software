/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author adnan
 */
public class OrderDetails extends javax.swing.JInternalFrame {
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String orderNocash=null;
    String orderNocredit=null;
    java.sql.Date date;
    /**
     * Creates new form OrderDetails
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.sql.SQLException
     */
    public OrderDetails() throws IOException, FileNotFoundException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        currentDate();
       // CashOrder();
       
        //CasdetaislhOrder();
    }
    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        //  inputdate.setDate(date);

    }
       private float total_action_cashinvoice() {

        int rowaCount = datatbl.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl.getValueAt(i, 3).toString());
        }

        return (float) totaltpmrp;

    }
             private float total_action_creditinvoice() {

        int rowaCount = datatbl1.getRowCount();

        float totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Float.parseFloat(datatbl1.getValueAt(i, 3).toString());
        }

        return (float) totaltpmrp;

    }
    private void CasdetaislhOrderbyclick() throws SQLException {
int tree = 0;
 DefaultTableModel model2 = (DefaultTableModel) itemtable.getModel();
 model2.setRowCount(0);
        try {
            String sql = "Select prcode,(select itemname from item ita where sld.prcode=ita.itemcode) as 'itemname',qty,unitrate,NetTotal from orderdetails sld where sld.orderNo='"+orderNocash+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
             //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
             while(rs.next()){
             String Code=rs.getString("prcode");
             String name=rs.getString("itemname");
             double qty=rs.getDouble("qty");
             double vat=rs.getDouble("unitrate");
             double nettotal=rs.getDouble("NetTotal");
           
               tree++;
                model2.addRow(new Object[]{tree, Code, name, qty, vat, nettotal});
                
             
             }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    } 
        private void CreditdetaislhOrderbyclick() throws SQLException {
int tree = 0;
 DefaultTableModel model2 = (DefaultTableModel) itemtable1.getModel();
 model2.setRowCount(0);
        try {
            String sql = "Select prcode,(select itemname from item ita where sld.prcode=ita.itemcode) as 'itemname',qty,unitrate,NetTotal from creditorderdetails sld where sld.orderNo='"+orderNocredit+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
             //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
             while(rs.next()){
             String Code=rs.getString("prcode");
             String name=rs.getString("itemname");
             double qty=rs.getDouble("qty");
             double vat=rs.getDouble("unitrate");
             double nettotal=rs.getDouble("NetTotal");
           
               tree++;
                model2.addRow(new Object[]{tree, Code, name, qty, vat, nettotal});
                
             
             }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    } 
   private void CasdetaislhOrder() throws SQLException {
int tree = 0;
 DefaultTableModel model2 = (DefaultTableModel) itemtable.getModel();
 
        try {
           
            
            String sql = "Select prcode,(select itemname from item ita where sld.prcode=ita.itemcode) as 'itemname',qty,unitrate,NetTotal from orderdetails sld";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
             //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
             while(rs.next()){
             String Code=rs.getString("prcode");
             String name=rs.getString("itemname");
             double qty=rs.getDouble("qty");
             double vat=rs.getDouble("unitrate");
             double nettotal=rs.getDouble("NetTotal");
           
               tree++;
                model2.addRow(new Object[]{tree, Code, name, qty, vat, nettotal});
                
             
             }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }  
   
    private void CashOrder_loadall() throws SQLException {
    
       int tree = 0;
         DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
         model2.setRowCount(0);
         
         try{
        String sql = "Select ords.orderNo as 'EntryCode',sum(ords.totalamount) as 'Amount',sum(ords.Totalvat) as 'Vat',sum(ords.NetTotal) as 'Nettotal',st.orderdate as 'InputDate',st.status  as 'status',(select name from admin ad where st.inputuserid=ad.id) as 'inputu'  from saleorder st inner join orderdetails ords ON ords.orderNo=st.id  GROUP BY ords.orderNo";
            
      
           
           pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
             //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
             while(rs.next()){
             String Code=rs.getString("EntryCode");
             String orderdate=rs.getString("InputDate");
             double amount=rs.getDouble("Amount");
             double vat=rs.getDouble("Vat");
             double nettotal=rs.getDouble("Nettotal");
             int status=rs.getInt("status");
             String s=null;
                switch (status) {
                    case 1:
                        s="Paid";
                        break;
                    case 2:
                        s="Cancel";
                        break;
                    default:
                        s="Waiting Confirmation";
                        break;
                }
             String inputuser=rs.getString("inputu");
               tree++;
                model2.addRow(new Object[]{tree, Code, orderdate, nettotal, s, inputuser});
                
             
             }
          
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
nettotaltext.setText(Float.toString(total_action_cashinvoice()));
    }
    
        private void creditOrder_loadall() throws SQLException {
    
       int tree = 0;
         DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
         model2.setRowCount(0);
         
         try{
        String sql = "Select ords.orderNo as 'EntryCode',sum(ords.totalamount) as 'Amount',sum(ords.Totalvat) as 'Vat',sum(ords.NetTotal) as 'Nettotal',st.orderdate as 'InputDate',st.status  as 'status',(select name from admin ad where st.inputuserid=ad.id) as 'inputu'  from creditsaleorder st inner join creditorderdetails ords ON ords.orderNo=st.id  GROUP BY ords.orderNo";
            
      
           
           pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
             //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
             while(rs.next()){
             String Code=rs.getString("EntryCode");
             String orderdate=rs.getString("InputDate");
             double amount=rs.getDouble("Amount");
             double vat=rs.getDouble("Vat");
             double nettotal=rs.getDouble("Nettotal");
             int status=rs.getInt("status");
             String s=null;
                switch (status) {
                    case 1:
                        s="Paid";
                        break;
                    case 2:
                        s="Cancel";
                        break;
                    default:
                        s="Waiting Confirmation";
                        break;
                }
             String inputuser=rs.getString("inputu");
               tree++;
                model2.addRow(new Object[]{tree, Code, orderdate, nettotal, s, inputuser});
                
             
             }
          
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
nettotaltext.setText(Float.toString(total_action_creditinvoice()));
    }
 private void CashOrder() throws SQLException {
    
       int tree = 0;
         DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
         model2.setRowCount(0);
         String sql=null;
        try {
            if(dataviewbox.getSelectedIndex()>0){
           int sta;
                switch (dataviewbox.getSelectedIndex()) {
                    case 1:
                        sta=0;
                        break;
                    case 2:
                        sta=1;
                        break;
                    default:
                        sta=2;
                        break;
                }
                sql = "Select ords.orderNo as 'EntryCode',sum(ords.totalamount) as 'Amount',sum(ords.Totalvat) as 'Vat',sum(ords.NetTotal) as 'Nettotal',st.orderdate as 'InputDate',st.status  as 'status',(select name from admin ad where st.inputuserid=ad.id) as 'inputu'  from saleorder st inner join orderdetails ords ON ords.orderNo=st.id   where st.orderdate='"+date+"' And st.status='"+sta+"' GROUP BY ords.orderNo";
            }
            else{
                
             sql = "Select ords.orderNo as 'EntryCode',sum(ords.totalamount) as 'Amount',sum(ords.Totalvat) as 'Vat',sum(ords.NetTotal) as 'Nettotal',st.orderdate as 'InputDate',st.status  as 'status',(select name from admin ad where st.inputuserid=ad.id) as 'inputu'  from saleorder st inner join orderdetails ords ON ords.orderNo=st.id   where st.orderdate='"+date+"' GROUP BY ords.orderNo";
            
            }
           
           pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
             //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
             while(rs.next()){
             String Code=rs.getString("EntryCode");
             String orderdate=rs.getString("InputDate");
             double amount=rs.getDouble("Amount");
             double vat=rs.getDouble("Vat");
             double nettotal=rs.getDouble("Nettotal");
             int status=rs.getInt("status");
             String s=null;
                switch (status) {
                    case 1:
                        s="Paid";
                        break;
                    case 2:
                        s="Cancel";
                        break;
                    default:
                        s="Waiting Confirmation";
                        break;
                }
             String inputuser=rs.getString("inputu");
               tree++;
                model2.addRow(new Object[]{tree, Code, orderdate, nettotal, s, inputuser});
                
             
             }
          
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
nettotaltext.setText(Float.toString(total_action_cashinvoice()));
    }
 
  private void CreditOrder() throws SQLException {
    
       int tree = 0;
         DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
         model2.setRowCount(0);
         String sql=null;
        try {
            if(dataviewbox1.getSelectedIndex()>0){
           int sta;
                switch (dataviewbox1.getSelectedIndex()) {
                    case 1:
                        sta=0;
                        break;
                    case 2:
                        sta=1;
                        break;
                    default:
                        sta=2;
                        break;
                }
                sql = "Select ords.orderNo as 'EntryCode',sum(ords.totalamount) as 'Amount',sum(ords.Totalvat) as 'Vat',sum(ords.NetTotal) as 'Nettotal',st.orderdate as 'InputDate',st.status  as 'status',(select name from admin ad where st.inputuserid=ad.id) as 'inputu'  from creditsaleorder st inner join creditorderdetails ords ON ords.orderNo=st.id   where st.orderdate='"+date+"' And st.status='"+sta+"' GROUP BY ords.orderNo";
            }
            else{
                
             sql = "Select ords.orderNo as 'EntryCode',sum(ords.totalamount) as 'Amount',sum(ords.Totalvat) as 'Vat',sum(ords.NetTotal) as 'Nettotal',st.orderdate as 'InputDate',st.status  as 'status',(select name from admin ad where st.inputuserid=ad.id) as 'inputu'  from creditsaleorder st inner join creditorderdetails ords ON ords.orderNo=st.id   where st.orderdate='"+date+"' GROUP BY ords.orderNo";
            
            }
           
           pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
             //datatbl.setModel(DbUtils.resultSetToTableModel(rs));
             while(rs.next()){
             String Code=rs.getString("EntryCode");
             String orderdate=rs.getString("InputDate");
             double amount=rs.getDouble("Amount");
             double vat=rs.getDouble("Vat");
             double nettotal=rs.getDouble("Nettotal");
             int status=rs.getInt("status");
             String s=null;
                switch (status) {
                    case 1:
                        s="Paid";
                        break;
                    case 2:
                        s="Cancel";
                        break;
                    default:
                        s="Waiting Confirmation";
                        break;
                }
             String inputuser=rs.getString("inputu");
               tree++;
                model2.addRow(new Object[]{tree, Code, orderdate, nettotal, s, inputuser});
                
             
             }
          
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
nettotaltext1.setText(Float.toString(total_action_creditinvoice()));
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        dataviewbox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        datebox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        nettotaltext = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        itemtable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        dataviewbox1 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        datebox1 = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        itemtable1 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        datatbl1 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        nettotaltext1 = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Order Details");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(67, 86, 86));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Data View");

        dataviewbox.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        dataviewbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Waiting For Confirmation", "Paid", "Cancel Order" }));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Date");

        datebox.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        datebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Current Date", " " }));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(102, 0, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Load All");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dataviewbox, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datebox, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(529, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dataviewbox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(datebox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4))
        );

        jPanel4.setBackground(new java.awt.Color(67, 86, 86));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Net Total");

        nettotaltext.setEditable(false);
        nettotaltext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nettotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(nettotaltext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Order No", "Order Date", "Total Invoice", "Status", "Input User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(5).setPreferredWidth(200);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane2.setViewportView(jPanel5);

        itemtable.setAutoCreateRowSorter(true);
        itemtable.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        itemtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL No", "Item Code", "Description", "Qty", "Unit Rate", "Total Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemtable.setGridColor(new java.awt.Color(204, 204, 204));
        itemtable.setRowHeight(30);
        itemtable.setShowVerticalLines(false);
        jScrollPane3.setViewportView(itemtable);
        if (itemtable.getColumnModel().getColumnCount() > 0) {
            itemtable.getColumnModel().getColumn(0).setResizable(false);
            itemtable.getColumnModel().getColumn(0).setPreferredWidth(30);
            itemtable.getColumnModel().getColumn(1).setResizable(false);
            itemtable.getColumnModel().getColumn(2).setResizable(false);
            itemtable.getColumnModel().getColumn(2).setPreferredWidth(300);
            itemtable.getColumnModel().getColumn(3).setResizable(false);
            itemtable.getColumnModel().getColumn(4).setResizable(false);
            itemtable.getColumnModel().getColumn(5).setResizable(false);
        }

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Details View");
        jLabel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Single View");
        jLabel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Cash Order", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel6.setBackground(new java.awt.Color(67, 86, 86));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Data View");

        dataviewbox1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        dataviewbox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Waiting For Confirmation", "Paid", "Cancel Order" }));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Date");

        datebox1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        datebox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Current Date", " " }));

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setText("Submit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(102, 0, 0));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Load All");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dataviewbox1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datebox1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(529, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dataviewbox1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(datebox1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4))
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Single View");
        jLabel8.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Details View");
        jLabel9.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        itemtable1.setAutoCreateRowSorter(true);
        itemtable1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        itemtable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL No", "Item Code", "Description", "Qty", "Unit Rate", "Total Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemtable1.setGridColor(new java.awt.Color(204, 204, 204));
        itemtable1.setRowHeight(30);
        itemtable1.setShowVerticalLines(false);
        jScrollPane4.setViewportView(itemtable1);
        if (itemtable1.getColumnModel().getColumnCount() > 0) {
            itemtable1.getColumnModel().getColumn(0).setResizable(false);
            itemtable1.getColumnModel().getColumn(0).setPreferredWidth(30);
            itemtable1.getColumnModel().getColumn(1).setResizable(false);
            itemtable1.getColumnModel().getColumn(2).setResizable(false);
            itemtable1.getColumnModel().getColumn(2).setPreferredWidth(300);
            itemtable1.getColumnModel().getColumn(3).setResizable(false);
            itemtable1.getColumnModel().getColumn(4).setResizable(false);
            itemtable1.getColumnModel().getColumn(5).setResizable(false);
        }

        datatbl1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Order No", "Order Date", "Total Invoice", "Status", "Input User"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        datatbl1.setGridColor(new java.awt.Color(204, 204, 204));
        datatbl1.setRowHeight(30);
        datatbl1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatbl1MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(datatbl1);
        if (datatbl1.getColumnModel().getColumnCount() > 0) {
            datatbl1.getColumnModel().getColumn(0).setResizable(false);
            datatbl1.getColumnModel().getColumn(0).setPreferredWidth(30);
            datatbl1.getColumnModel().getColumn(1).setResizable(false);
            datatbl1.getColumnModel().getColumn(2).setResizable(false);
            datatbl1.getColumnModel().getColumn(3).setResizable(false);
            datatbl1.getColumnModel().getColumn(4).setResizable(false);
            datatbl1.getColumnModel().getColumn(5).setResizable(false);
            datatbl1.getColumnModel().getColumn(5).setPreferredWidth(200);
        }

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane5.setViewportView(jPanel7);

        jPanel8.setBackground(new java.awt.Color(67, 86, 86));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Net Total");

        nettotaltext1.setEditable(false);
        nettotaltext1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nettotaltext1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(nettotaltext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Credit Order", jPanel2);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
      int row = datatbl.getSelectedRow();
            orderNocash = (datatbl.getModel().getValueAt(row, 1).toString());
          try {
              CasdetaislhOrderbyclick();
          } catch (SQLException ex) {
              Logger.getLogger(OrderDetails.class.getName()).log(Level.SEVERE, null, ex);
          }
    }//GEN-LAST:event_datatblMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            CashOrder();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            CashOrder_loadall();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            CreditOrder();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            creditOrder_loadall();
        } catch (SQLException ex) {
            Logger.getLogger(OrderDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void datatbl1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatbl1MouseClicked
         int row = datatbl1.getSelectedRow();
            orderNocredit = (datatbl1.getModel().getValueAt(row, 1).toString());
          try {
              CreditdetaislhOrderbyclick();
          } catch (SQLException ex) {
              Logger.getLogger(OrderDetails.class.getName()).log(Level.SEVERE, null, ex);
          }
    }//GEN-LAST:event_datatbl1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable datatbl;
    private javax.swing.JTable datatbl1;
    private javax.swing.JComboBox<String> dataviewbox;
    private javax.swing.JComboBox<String> dataviewbox1;
    private javax.swing.JComboBox<String> datebox;
    private javax.swing.JComboBox<String> datebox1;
    private javax.swing.JTable itemtable;
    private javax.swing.JTable itemtable1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField nettotaltext;
    private javax.swing.JTextField nettotaltext1;
    // End of variables declaration//GEN-END:variables
}
