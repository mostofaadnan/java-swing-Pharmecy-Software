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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author adnan
 */
public class CutomerInvoice extends javax.swing.JInternalFrame {
int tree=0;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String cusId;
    String invoiceNo;
    String paymenttype;
    /**
     * Creates new form CutomerInvoice
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.sql.SQLException
     */
    public CutomerInvoice() throws IOException, FileNotFoundException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
    }
    
    
    public CutomerInvoice(String table_click) throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
         cusId=table_click;
         
        table_click();
        customer();
        customer_Information();
         
    }
     private void customer() throws SQLException {
       
       
        try {
            String sql = "Select customername from customerInfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("customername");
                customerbox.addItem(name);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
private void table_click(){

   DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
        model2.setRowCount(0);
        try {
            String sql = "Select * from vatcollection it where customerid='"+cusId+"' ORDER BY invoiceNopurchaseNo DESC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {

                String invoiceno = rs.getString("invoiceNopurchaseNo");
                String invoicedate = rs.getString("invoicedate");
                String paymenttypehere = rs.getString("paymenttype");
                double totalrate = rs.getDouble("TotalAmount");
                double totalvat = rs.getDouble("TotalVat");
                double netinvoice = rs.getDouble("Totalinvoice");
                

                tree++;
                model2.addRow(new Object[]{tree, invoiceno, invoicedate, paymenttypehere, totalrate, totalvat, netinvoice});

            }

            // datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }





}
    private void customer_Information(){
    
         try {

            
            String sql = "Select * from customerInfo where customerid='" + cusId + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                cusId = rs.getString("customerid");
                 String name=rs.getString("customername");
                 customerbox.setSelectedItem(name);
                String address = rs.getString("address");
                addresstext.setText(address);


                String customertype = rs.getString("customerType");
                supliertypebox.setText(customertype);

                String balanceType = rs.getString("balanceType");
                balancetypebox.setText(balanceType);

                String openBalanace = rs.getString("OpenigBalance");
                openingbalancetext.setText(openBalanace);

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
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        customerbox = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        addresstext = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        contactnotext = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        supliertypebox = new javax.swing.JLabel();
        balancetypebox = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        openingbalancetext = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        dipositamointtext = new javax.swing.JLabel();
        cashamounttext = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        paidamounttext = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        balancedue = new javax.swing.JLabel();
        creditamounttext = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        salesAmounttext = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();

        tablemenu.setToolTipText("");
        tablemenu.setLabel("Menu");
        tablemenu.setPreferredSize(new java.awt.Dimension(300, 28));

        view.setText("View Details");
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
        setTitle("Customer Invoice");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Customer");

        customerbox.setEditable(true);
        customerbox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        customerbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        customerbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                customerboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setText("Address:");

        addresstext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jLabel28.setText("Contact No");

        contactnotext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jLabel29.setText("Customer Type");

        supliertypebox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        balancetypebox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jLabel30.setText("Balance Type");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addresstext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                            .addComponent(jLabel30, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(contactnotext, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                            .addComponent(supliertypebox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(balancetypebox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(addresstext, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(contactnotext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(supliertypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(balancetypebox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 255, 255), 1, true), "Amount Box"));

        jLabel10.setText("Opening Balnce");

        openingbalancetext.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        openingbalancetext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        openingbalancetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jLabel11.setText("Diposit ");

        dipositamointtext.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dipositamointtext.setForeground(new java.awt.Color(0, 102, 153));
        dipositamointtext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dipositamointtext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        cashamounttext.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cashamounttext.setForeground(new java.awt.Color(0, 0, 204));
        cashamounttext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cashamounttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jLabel12.setText("Cash Amount");

        jLabel13.setText("Paid Amount");

        paidamounttext.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        paidamounttext.setForeground(new java.awt.Color(0, 153, 0));
        paidamounttext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        paidamounttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jLabel14.setText("Balance Due");

        balancedue.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        balancedue.setForeground(new java.awt.Color(255, 0, 0));
        balancedue.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        balancedue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        creditamounttext.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        creditamounttext.setForeground(new java.awt.Color(204, 0, 51));
        creditamounttext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        creditamounttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jLabel15.setText("Credit  Amount");

        jLabel17.setText("Sales Amount");

        salesAmounttext.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        salesAmounttext.setForeground(new java.awt.Color(0, 0, 204));
        salesAmounttext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        salesAmounttext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cashamounttext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(paidamounttext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(balancedue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(salesAmounttext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dipositamointtext, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(openingbalancetext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(creditamounttext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(openingbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dipositamointtext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(creditamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cashamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(salesAmounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paidamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(balancedue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jButton1.setBackground(new java.awt.Color(0, 153, 102));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("All ");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(customerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "SI No", "Invoice No", "Invoice Date", "Type", "Amount", "Vat", "Net Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
        jScrollPane3.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setResizable(false);
            datatbl.getColumnModel().getColumn(0).setPreferredWidth(50);
            datatbl.getColumnModel().getColumn(1).setResizable(false);
            datatbl.getColumnModel().getColumn(2).setResizable(false);
            datatbl.getColumnModel().getColumn(3).setResizable(false);
            datatbl.getColumnModel().getColumn(4).setResizable(false);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 893, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Invoice", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 898, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void customerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_customerboxPopupMenuWillBecomeInvisible
        
        if(customerbox.getSelectedIndex()>0){
        
        String Customername = (String) customerbox.getSelectedItem();
        try {
            String sql = "Select customerid from customerInfo  where customername='" + Customername + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {

                cusId = rs.getString("customerid");

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        
        
         table_click();
       
        customer_Information();
        }
        /*

        try {

            String table_click = (String) customerbox.getSelectedItem();
            String sql = "Select * from customerInfo where customername='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                cusId = rs.getString("id");

                String address = rs.getString("address");
                addresstext.setText(address);

                String vatno = rs.getString("vatReNo");
                vatregistrationotext.setText(vatno);

                String customertype = rs.getString("customerType");
                supliertypebox.setText(customertype);

                String balanceType = rs.getString("balanceType");
                balancetypebox.setText(balanceType);

                String openBalanace = rs.getString("OpenigBalance");
                openingbalancetext.setText(openBalanace);

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

                String telephon = rs.getString("TelephoneNo");
                telephonetext.setText(telephon);

                String contctno = rs.getString("ContactNo");
                contactnotext.setText(contctno);

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        try {
            String sql = "Select invoicedate as 'Tran.Date', invoiceno as 'Tran.No',type 'Tran.type',remark as 'Remark/Purpose',Received_amt as 'Received Amoun', totalInvoice as 'invoice Amount', balance as 'Balance' from CraditStatement cs where  customerid='" + cusId + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
*/
    }//GEN-LAST:event_customerboxPopupMenuWillBecomeInvisible

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (customerbox.getSelectedIndex() == 0) {
            JOptionPane.showConfirmDialog(null, "Please Select Customer Name", "Requirment", JOptionPane.CANCEL_OPTION);
        } else {
            try {

                JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/Allcreditstatement.jrxml");
                //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

                HashMap para = new HashMap();

                para.put("customerid", cusId);

                JasperReport jr = JasperCompileManager.compileReport(jd);
                JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
                JasperViewer.viewReport(jp, false);

            } catch (NumberFormatException | JRException ex) {
                JOptionPane.showMessageDialog(null, ex);

            }

        }
        
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked
          int row = datatbl.getSelectedRow();
        invoiceNo = (datatbl.getModel().getValueAt(row, 1).toString());
        paymenttype = (datatbl.getModel().getValueAt(row, 3).toString());
       
        if (SwingUtilities.isRightMouseButton(evt)) {
            tablemenu.show(evt.getComponent(), evt.getX() + 1, evt.getY() + 1);

        }
    }//GEN-LAST:event_datatblMouseClicked

    private void viewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewActionPerformed
         String table_click = invoiceNo;
     

        InvoiceCheck filte = null;

    try {
        filte = new InvoiceCheck(table_click, paymenttype);
    } catch (IOException | SQLException ex) {
        Logger.getLogger(CutomerInvoice.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JLabel addresstext;
    private javax.swing.JLabel balancedue;
    private javax.swing.JLabel balancetypebox;
    private javax.swing.JLabel cashamounttext;
    private javax.swing.JLabel contactnotext;
    private javax.swing.JLabel creditamounttext;
    private javax.swing.JComboBox<String> customerbox;
    private javax.swing.JTable datatbl;
    private javax.swing.JLabel dipositamointtext;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel openingbalancetext;
    private javax.swing.JLabel paidamounttext;
    private javax.swing.JLabel salesAmounttext;
    private javax.swing.JLabel supliertypebox;
    private javax.swing.JPopupMenu tablemenu;
    private javax.swing.JMenuItem view;
    // End of variables declaration//GEN-END:variables
}
