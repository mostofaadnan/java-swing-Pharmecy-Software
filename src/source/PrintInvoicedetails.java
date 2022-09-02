/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author adnan
 */
public class PrintInvoicedetails extends javax.swing.JFrame {
 Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    String cusId;
    String currency;
    String username=null;
    /**
     * Creates new form PrintInvoicedetails
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public PrintInvoicedetails() throws IOException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        intilize();
    }
    public PrintInvoicedetails(String table_click, String paymenttype) throws IOException, FileNotFoundException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        intilize();
        paymenttypeboxdetails.setSelectedItem(paymenttype);
        viewData(table_click, paymenttype);

    }
    private void intilize() {

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("company.png")));
    }
   private void clear() {
        customertext.setText(null);
        Datetext.setText(null);
        totalamounttext.setText(null);
        totalVatText.setText(null);
        netTotalText.setText(null);
        paymenttypetext.setText(null);
        //banktext.setText(null);
       // accNotext.setText(null);
        cardNotext.setText(null);
       // bkashtext.setText(null);
    }
   
    private static final DecimalFormat df2 = new DecimalFormat("#.00");
    private void viewData(String inv, String paymenttype) {
        String invoiceNo = inv;
        InvoiceText.setText(inv);
        paymenttypeboxdetails.setSelectedItem(paymenttype);
        if ("Cash".equals(paymenttype)) {
            try {
                DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
                model2.setRowCount(0);
                int tree = 0;
                String sql = "Select prcode , (select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item', sl.unitrate as 'UnitRate', sl.qty as 'Qty',sl.unit as 'Unit', sl.discount as 'Discount', sl.totalamount as 'Amount', sl.vat as 'Vat', sl.Totalvat as 'Total Vat',sl.NetTotal  as 'NetTotal' from cashsaledetails sl where sl.invoiceNo='" + invoiceNo + "'";
                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();
                // datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {
                    String prcode = rs.getString("prcode");
                    String Item = rs.getString("Item");
                    double UnitRate = rs.getDouble("UnitRate");
                    float Qty = rs.getFloat("Qty");
                    String Unit = rs.getString("Unit");
                    double Discount = rs.getDouble("Discount");
                    double Amount = rs.getDouble("Amount");
                    double Vat = rs.getDouble("Vat");
                    double NetTotal = rs.getDouble("NetTotal");
                    tree++;
                    model2.addRow(new Object[]{tree, prcode, Item, UnitRate, Qty, Unit, Discount, Amount, Vat, NetTotal});

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            try {
                String sql = "Select invoiceNo,invoicedate,paymentType,paymentCurency,TotalAmount,TotalVat,Totalinvoice,customerid,(select ci.customername from customerInfo ci where ci.id=sl.customerid ) as 'customername',paymentType,(select Bankname from bankinfo bin where bin.id=sl.bank)as 'bank',accNo,cardNo,BikashNo, (select name from admin ad where ad.id=sl.inputuserid) as 'user' from cashsale sl  where sl.invoiceNo='" + invoiceNo + "'";
                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();
                if (rs.next()) {

                    String Id = rs.getString("invoiceNo");
                    /// invoiceapplytex.setText(Id);
                    String date = rs.getString("invoicedate");
                    Datetext.setText(date);

                    currency = rs.getString("paymentCurency");
                    String payment = rs.getString("paymentType");
                    paymenttypetext.setText(payment);
                    String totalam = rs.getString("TotalAmount");
                    totalamounttext.setText(totalam);
                    String tovat = rs.getString("TotalVat");
                    totalVatText.setText(tovat);
                    double netinoice = rs.getDouble("Totalinvoice");
                    netTotalText.setText(df2.format(netinoice));
                    cusId = rs.getString("customerid");
                    String customername = rs.getString("customername");
                    customertext.setText(customername);

                    String paymentType = rs.getString("paymentType");
                    paymenttypetext.setText(paymentType);
                    String bank = rs.getString("bank");
                    //banktext.setText(bank);
                    String accNo = rs.getString("accNo");
                    ///accNotext.setText(accNo);
                    String cardNo = rs.getString("cardNo");
                    cardNotext.setText(cardNo);
                    String BikashNo = rs.getString("BikashNo");
                   // bkashtext.setText(BikashNo);
                    username=rs.getString("user");

                    //  currency=rs.getString("paymentCurency");
                } else {
                    clear();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
        } else {
            paymenttypeboxdetails.setSelectedIndex(2);

            try {

                DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
                model2.setRowCount(0);
                int tree = 0;
                String sql = "Select prcode , (select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item', sl.unitrate as 'UnitRate', sl.qty as 'Qty',sl.unit as 'Unit', sl.discount as 'Discount', sl.totalamount as 'Amount', sl.vat as 'Vat', sl.Totalvat as 'TotalVat',sl.NetTotal  as 'NetTotal'  from saledetails sl where sl.invoiceNo='" + invoiceNo + "'";
                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();
                //   datatbl1.setModel(DbUtils.resultSetToTableModel(rs));
                while (rs.next()) {
                    String prcode = rs.getString("prcode");
                    String Item = rs.getString("Item");
                    double UnitRate = rs.getDouble("UnitRate");
                    float Qty = rs.getFloat("Qty");
                    String Unit = rs.getString("Unit");
                    double Discount = rs.getDouble("Discount");
                    double Amount = rs.getDouble("Amount");
                    double Vat = rs.getDouble("TotalVat");
                    double NetTotal = rs.getDouble("NetTotal");
                    tree++;
                    model2.addRow(new Object[]{tree, prcode, Item, UnitRate, Qty, Unit, Discount, Amount, Vat, NetTotal});

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }
            try {
                String sql = "Select invoiceNo,invoicedate,paymentType,paymentCurency,TotalAmount,TotalVat,Totalinvoice,customerid,(select ci.customername from customerInfo ci where ci.id=sl.customerid ) as 'customername',(select name from admin ad where ad.id=sl.inputuserid) as 'user' from sale sl  where sl.invoiceNo='" + invoiceNo + "'";
                pst = conn.prepareStatement(sql);

                rs = pst.executeQuery();
                if (rs.next()) {

                    String Id = rs.getString("invoiceNo");
                    /// invoiceapplytex.setText(Id);
                    String date = rs.getString("invoicedate");
                    Datetext.setText(date);
                    currency = rs.getString("paymentCurency");
                    String payment = rs.getString("paymentType");
                    paymenttypetext.setText(payment);
                    String totalam = rs.getString("TotalAmount");
                    totalamounttext.setText(totalam);
                    String tovat = rs.getString("TotalVat");
                    totalVatText.setText(tovat);
                    double netinoice = rs.getDouble("Totalinvoice");
                    netTotalText.setText(df2.format(netinoice));
                    cusId = rs.getString("customerid");
                    String customername = rs.getString("customername");
                    customertext.setText(customername);
                    username=rs.getString("user");

                    //   currency=rs.getString("paymentCurency");
                } else {
                    clear();

                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);

            }

        }

    }

    private void Serach1() throws SQLException {
        if (InvoiceText.getText().isEmpty()) {

        } else {

            String invoiceNo = InvoiceText.getText();
            switch (paymenttypeboxdetails.getSelectedIndex()) {
                case 1:
                    try {
                        DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
                        model2.setRowCount(0);
                        int tree = 0;
                        String sql = "Select prcode , (select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item', sl.unitrate as 'UnitRate', sl.qty as 'Qty',sl.unit as 'Unit', sl.discount as 'Discount', sl.totalamount as 'Amount', sl.vat as 'Vat', sl.Totalvat as 'Total Vat',sl.NetTotal  as 'NetTotal'   from cashsaledetails sl where sl.invoiceNo='" + invoiceNo + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        while (rs.next()) {
                            String prcode = rs.getString("prcode");
                            String Item = rs.getString("Item");
                            double UnitRate = rs.getDouble("UnitRate");
                            float Qty = rs.getFloat("Qty");
                            String Unit = rs.getString("Unit");
                            double Discount = rs.getDouble("Discount");
                            double Amount = rs.getDouble("Amount");
                            double Vat = rs.getDouble("Vat");
                            double NetTotal = rs.getDouble("NetTotal");
                            tree++;
                            model2.addRow(new Object[]{tree, prcode, Item, UnitRate, Qty, Unit, Discount, Amount, Vat, NetTotal});

                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    try {
                        String sql = "Select invoiceNo,invoicedate,paymentType,paymentCurency,TotalAmount,TotalVat,Totalinvoice,customerid,(select ci.customername from customerInfo ci where ci.id=sl.customerid ) as 'customername',paymentType,(select Bankname from bankinfo bin where bin.id=sl.bank)as 'bank',accNo,cardNo,BikashNo,(select name from admin ad where ad.id=sl.inputuserid) as 'user' from cashsale sl   where sl.invoiceNo='" + invoiceNo + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        if (rs.next()) {

                            String Id = rs.getString("invoiceNo");
                            /// invoiceapplytex.setText(Id);
                            String date = rs.getString("invoicedate");
                            Datetext.setText(date);

                            currency = rs.getString("paymentCurency");
                            String payment = rs.getString("paymentType");
                            paymenttypetext.setText(payment);
                            String totalam = rs.getString("TotalAmount");
                            totalamounttext.setText(totalam);
                            String tovat = rs.getString("TotalVat");
                            totalVatText.setText(tovat);
                             double netinoice = rs.getDouble("Totalinvoice");
                            netTotalText.setText(df2.format(netinoice));
                            cusId = rs.getString("customerid");
                            String customername = rs.getString("customername");
                            customertext.setText(customername);
                            String paymentType = rs.getString("paymentType");
                            paymenttypetext.setText(paymentType);
                            String bank = rs.getString("bank");
                           // banktext.setText(bank);
                            String accNo = rs.getString("accNo");
                         //   accNotext.setText(accNo);
                            String cardNo = rs.getString("cardNo");
                            cardNotext.setText(cardNo);
                            String BikashNo = rs.getString("BikashNo");
                          //  bkashtext.setText(BikashNo);
                            username=rs.getString("user");

                            //  currency=rs.getString("paymentCurency");
                        } else {
                            clear();
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    break;
                case 2:
                    try {
                        DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
                        model2.setRowCount(0);
                        int tree = 0;
                        String sql = "Select prcode , (select ite.itemName from item ite where ite.Itemcode=sl.prcode ) as 'Item', sl.unitrate as 'UnitRate', sl.qty as 'Qty',sl.unit as 'Unit', sl.discount as 'Discount', sl.totalamount as 'Amount', sl.vat as 'Vat', sl.Totalvat as 'Total Vat',sl.NetTotal  as 'NetTotal'  from saledetails sl where sl.invoiceNo='" + invoiceNo + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        while (rs.next()) {
                            String prcode = rs.getString("prcode");
                            String Item = rs.getString("Item");
                            double UnitRate = rs.getDouble("UnitRate");
                            float Qty = rs.getFloat("Qty");
                            String Unit = rs.getString("Unit");
                            double Discount = rs.getDouble("Discount");
                            double Amount = rs.getDouble("Amount");
                            double Vat = rs.getDouble("Vat");
                            double NetTotal = rs.getDouble("NetTotal");
                            tree++;
                            model2.addRow(new Object[]{tree, prcode, Item, UnitRate, Qty, Unit, Discount, Amount, Vat, NetTotal});

                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    try {
                        String sql = "Select invoiceNo,invoicedate,paymentType,paymentCurency,TotalAmount,TotalVat,Totalinvoice,customerid,(select ci.customername from customerInfo ci where ci.id=sl.customerid ) as 'customername',(select name from admin ad where ad.id=sl.inputuserid) as 'user' from sale sl  where sl.invoiceNo='" + invoiceNo + "'";
                        pst = conn.prepareStatement(sql);

                        rs = pst.executeQuery();
                        if (rs.next()) {

                            String Id = rs.getString("invoiceNo");
                            /// invoiceapplytex.setText(Id);
                            String date = rs.getString("invoicedate");
                            Datetext.setText(date);
                            currency = rs.getString("paymentCurency");
                            String payment = rs.getString("paymentType");
                            paymenttypetext.setText(payment);
                            String totalam = rs.getString("TotalAmount");
                            totalamounttext.setText(totalam);
                            String tovat = rs.getString("TotalVat");
                            totalVatText.setText(tovat);
                            double netinoice = rs.getDouble("Totalinvoice");
                            netTotalText.setText(df2.format(netinoice));
                            cusId = rs.getString("customerid");
                            String customername = rs.getString("customername");
                            customertext.setText(customername);
                            username=rs.getString("user");

                            //   currency=rs.getString("paymentCurency");
                        } else {
                            clear();

                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);

                    }
                    break;
                default:
                    break;
            }

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

        jPanel5 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        paymenttypeboxdetails = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        InvoiceText = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        customertext = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        Datetext = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        totalamounttext = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        totalVatText = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        netTotalText = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        paymenttypetext = new javax.swing.JTextField();
        cardNotext = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);

        jPanel5.setBackground(new java.awt.Color(67, 86, 86));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Invoice Type");

        paymenttypeboxdetails.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Cash", "Credit " }));
        paymenttypeboxdetails.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                paymenttypeboxdetailsPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Invoice No:");

        InvoiceText.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        InvoiceText.setBorder(null);
        InvoiceText.setEnabled(false);
        InvoiceText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                InvoiceTextKeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Customer Name");

        customertext.setEditable(false);
        customertext.setBackground(new java.awt.Color(255, 255, 255));
        customertext.setBorder(null);
        customertext.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Invoice Date:");

        Datetext.setEditable(false);
        Datetext.setBackground(new java.awt.Color(255, 255, 255));
        Datetext.setBorder(null);
        Datetext.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Invoice Amount:");

        totalamounttext.setEditable(false);
        totalamounttext.setBackground(new java.awt.Color(255, 255, 255));
        totalamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalamounttext.setForeground(new java.awt.Color(153, 0, 0));
        totalamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalamounttext.setBorder(null);
        totalamounttext.setDisabledTextColor(new java.awt.Color(204, 0, 0));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Total Vat:");

        totalVatText.setEditable(false);
        totalVatText.setBackground(new java.awt.Color(255, 255, 255));
        totalVatText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalVatText.setForeground(new java.awt.Color(153, 0, 0));
        totalVatText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalVatText.setBorder(null);
        totalVatText.setDisabledTextColor(new java.awt.Color(204, 0, 0));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Total Invoice:");

        netTotalText.setEditable(false);
        netTotalText.setBackground(new java.awt.Color(255, 255, 255));
        netTotalText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        netTotalText.setForeground(new java.awt.Color(153, 0, 0));
        netTotalText.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netTotalText.setBorder(null);
        netTotalText.setDisabledTextColor(new java.awt.Color(204, 0, 0));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Payment Type");

        paymenttypetext.setEditable(false);
        paymenttypetext.setBackground(new java.awt.Color(255, 255, 255));
        paymenttypetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        paymenttypetext.setForeground(new java.awt.Color(153, 0, 0));
        paymenttypetext.setBorder(null);
        paymenttypetext.setDisabledTextColor(new java.awt.Color(204, 0, 0));

        cardNotext.setEditable(false);
        cardNotext.setBackground(new java.awt.Color(255, 255, 255));
        cardNotext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cardNotext.setForeground(new java.awt.Color(153, 0, 0));
        cardNotext.setBorder(null);
        cardNotext.setDisabledTextColor(new java.awt.Color(204, 0, 0));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Card No:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel20)))
                .addGap(8, 8, 8)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(paymenttypeboxdetails, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(InvoiceText, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(Datetext, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(316, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(customertext, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(paymenttypetext, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                    .addComponent(totalamounttext))
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel21))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(7, 7, 7)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cardNotext)
                                    .addComponent(totalVatText, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(netTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(InvoiceText, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Datetext, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(paymenttypeboxdetails, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(customertext, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalamounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(netTotalText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(totalVatText, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(paymenttypetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cardNotext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBackground(new java.awt.Color(153, 153, 153));
        jPanel7.setForeground(new java.awt.Color(255, 255, 255));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Sale Details");

        jButton2.setBackground(new java.awt.Color(0, 102, 102));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Report View");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)))
                .addGap(5, 5, 5))
        );

        datatbl1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        datatbl1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "SI No", "Item Code", "Description", "Unit Rate", "Qty", "Unit", "Discount", "Amount", "Vat", "Net Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Float.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
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
        datatbl1.setShowVerticalLines(false);
        jScrollPane1.setViewportView(datatbl1);
        if (datatbl1.getColumnModel().getColumnCount() > 0) {
            datatbl1.getColumnModel().getColumn(0).setResizable(false);
            datatbl1.getColumnModel().getColumn(0).setPreferredWidth(30);
            datatbl1.getColumnModel().getColumn(2).setResizable(false);
            datatbl1.getColumnModel().getColumn(2).setPreferredWidth(300);
            datatbl1.getColumnModel().getColumn(3).setResizable(false);
            datatbl1.getColumnModel().getColumn(4).setResizable(false);
            datatbl1.getColumnModel().getColumn(5).setResizable(false);
            datatbl1.getColumnModel().getColumn(6).setResizable(false);
            datatbl1.getColumnModel().getColumn(7).setResizable(false);
            datatbl1.getColumnModel().getColumn(8).setResizable(false);
            datatbl1.getColumnModel().getColumn(9).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(1153, 575));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void paymenttypeboxdetailsPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_paymenttypeboxdetailsPopupMenuWillBecomeInvisible
        if (paymenttypeboxdetails.getSelectedIndex() == 0) {

            InvoiceText.setEnabled(false);

        } else {

            InvoiceText.setEnabled(true);

        }

        clear();
        InvoiceText.setText(null);

        DefaultTableModel model2 = (DefaultTableModel) datatbl1.getModel();
        model2.setRowCount(0);
    }//GEN-LAST:event_paymenttypeboxdetailsPopupMenuWillBecomeInvisible

    private void InvoiceTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_InvoiceTextKeyReleased
        if (paymenttypeboxdetails.getSelectedIndex() > 0) {

            try {
                // Serach();
                Serach1();
            } catch (SQLException ex) {
                Logger.getLogger(InvoiceCheck.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_InvoiceTextKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       if (InvoiceText.getText().isEmpty() || paymenttypeboxdetails.getSelectedIndex() == 0 || customertext.getText().isEmpty()) {

        } else {
           
           
           if ("Cash".equals(paymenttypeboxdetails.getSelectedItem())) {
           
            try {
          
            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/BilReciept.jrxml");

            HashMap para = new HashMap();
            para.put("invoiceno", InvoiceText.getText());
            para.put("receive", netTotalText.getText());
            para.put("return", "0.00");
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);

        } catch (NumberFormatException | JRException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }

        } else {
         try {
           

            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/BilRecieptCredit.jrxml");

            HashMap para = new HashMap();
            para.put("invoiceno", InvoiceText.getText());
          //  para.put("receive", totalinvoice);
            //para.put("return", "0.00");
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);
            JasperViewer.viewReport(jp, false);

        } catch (NumberFormatException | JRException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }

        }
       
       dispose();
       }
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(PrintInvoicedetails.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
     //</editor-fold>
     //</editor-fold>
     
        //</editor-fold>

        /* Create and display the form */
      
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Datetext;
    private javax.swing.JTextField InvoiceText;
    private javax.swing.JTextField cardNotext;
    private javax.swing.JTextField customertext;
    private javax.swing.JTable datatbl1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField netTotalText;
    private javax.swing.JComboBox<String> paymenttypeboxdetails;
    private javax.swing.JTextField paymenttypetext;
    private javax.swing.JTextField totalVatText;
    private javax.swing.JTextField totalamounttext;
    // End of variables declaration//GEN-END:variables
 public static String convertToIndianCurrency(String num) {
        BigDecimal bd = new BigDecimal(num);
        long number = bd.longValue();
        long no = bd.longValue();
        int decimal = (int) (bd.remainder(BigDecimal.ONE).doubleValue() * 100);
        int digits_length = String.valueOf(no).length();
        int i = 0;
        ArrayList<String> str = new ArrayList<>();
        HashMap<Integer, String> words = new HashMap<>();
        words.put(0, "");
        words.put(1, "One");
        words.put(2, "Two");
        words.put(3, "Three");
        words.put(4, "Four");
        words.put(5, "Five");
        words.put(6, "Six");
        words.put(7, "Seven");
        words.put(8, "Eight");
        words.put(9, "Nine");
        words.put(10, "Ten");
        words.put(11, "Eleven");
        words.put(12, "Twelve");
        words.put(13, "Thirteen");
        words.put(14, "Fourteen");
        words.put(15, "Fifteen");
        words.put(16, "Sixteen");
        words.put(17, "Seventeen");
        words.put(18, "Eighteen");
        words.put(19, "Nineteen");
        words.put(20, "Twenty");
        words.put(30, "Thirty");
        words.put(40, "Forty");
        words.put(50, "Fifty");
        words.put(60, "Sixty");
        words.put(70, "Seventy");
        words.put(80, "Eighty");
        words.put(90, "Ninety");
        String digits[] = {"", "Hundred", "Thousand", "Lakh", "Crore"};
        while (i < digits_length) {
            int divider = (i == 2) ? 10 : 100;
            number = no % divider;
            no = no / divider;
            i += divider == 10 ? 1 : 2;
            if (number > 0) {
                int counter = str.size();
                String plural = (counter > 0 && number > 9) ? "s" : "";
                String tmp = (number < 21) ? words.get((int) number) + " " + digits[counter] + plural : words.get((int) Math.floor(number / 10) * 10) + " " + words.get((int) (number % 10)) + " " + digits[counter] + plural;
                str.add(tmp);
            } else {
                str.add("");
            }
        }

        Collections.reverse(str);
        // ArrayList<String> Rupees =str;
//String Rupees = String.join(" ", str).trim();

        String Rupees = String.join(" ", str).trim();

        String paise = (decimal) > 0 ? " And " + words.get((int) (decimal - decimal % 10)) + " Files" + words.get((int) (decimal % 10)) : "";
        return Rupees + paise + " " + "AED Only";
    }
//Arebic

    public static String convertNumberToArabicWords(String number) throws NumberFormatException {

// check if the input string is number or not
        Double.parseDouble(number);

// check if its floating point number or not
        if (number.contains(".")) {// yes
// the number
            String theNumber = number.substring(0, number.indexOf('.'));
// the floating point number
            String theFloat = number.substring(number.indexOf('.') + 1);
// check how many digits in the number 1:x 2:xx 3:xxx 4:xxxx 5:xxxxx
// 6:xxxxxx
            switch (theNumber.length()) {
                case 1:
                    return convertOneDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 2:
                    return convertTwoDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 3:
                    return convertThreeDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 4:
                    return convertFourDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 5:
                    return convertFiveDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                case 6:
                    return convertSixDigits(theNumber) + " فاصلة " + convertTwoDigits(theFloat);
                default:
                    return "";
            }
        } else {
            switch (number.length()) {
                case 1:
                    return convertOneDigits(number);
                case 2:
                    return convertTwoDigits(number);
                case 3:
                    return convertThreeDigits(number);
                case 4:
                    return convertFourDigits(number);
                case 5:
                    return convertFiveDigits(number);
                case 6:
                    return convertSixDigits(number);
                default:
                    return "";
            }

        }
    }

// -------------------------------------------
    private static String convertOneDigits(String oneDigit) {
        switch (Integer.parseInt(oneDigit)) {
            case 1:
                return "واحد";
            case 2:
                return "إثنان";
            case 3:
                return "ثلاثه";
            case 4:
                return "أربعه";
            case 5:
                return "خمسه";
            case 6:
                return "سته";
            case 7:
                return "سبعه";
            case 8:
                return "ثمانيه";
            case 9:
                return "تسعه";
            default:
                return "";
        }
    }

    private static String convertTwoDigits(String twoDigits) {
        String returnAlpha = "00";
// check if the first digit is 0 like 0x
        if (twoDigits.charAt(0) == '0' && twoDigits.charAt(1) != '0') {// yes
// convert two digits to one
            return convertOneDigits(String.valueOf(twoDigits.charAt(1)));
        } else {// no
// check the first digit 1x 2x 3x 4x 5x 6x 7x 8x 9x
            switch (getIntVal(twoDigits.charAt(0))) {
                case 1: {// 1x
                    if (getIntVal(twoDigits.charAt(1)) == 1) {
                        return "إحدىعشر";
                    }
                    if (getIntVal(twoDigits.charAt(1)) == 2) {
                        return "إثنىعشر";
                    } else {
                        return convertOneDigits(String.valueOf(twoDigits.charAt(1))) + " " + "عشر";
                    }
                }
                case 2:// 2x x:not 0
                    returnAlpha = "عشرون";
                    break;
                case 3:// 3x x:not 0
                    returnAlpha = "ثلاثون";
                    break;
                case 4:// 4x x:not 0
                    returnAlpha = "أريعون";
                    break;
                case 5:// 5x x:not 0
                    returnAlpha = "خمسون";
                    break;
                case 6:// 6x x:not 0
                    returnAlpha = "ستون";
                    break;
                case 7:// 7x x:not 0
                    returnAlpha = "سبعون";
                    break;
                case 8:// 8x x:not 0
                    returnAlpha = "ثمانون";
                    break;
                case 9:// 9x x:not 0
                    returnAlpha = "تسعون";
                    break;
                default:
                    returnAlpha = "";
                    break;
            }
        }

// 20 - 99
// x0 x:not 0,1
        if (convertOneDigits(String.valueOf(twoDigits.charAt(1))).length() == 0) {
            return returnAlpha;
        } else {// xx x:not 0
            return convertOneDigits(String.valueOf(twoDigits.charAt(1))) + " و " + returnAlpha;
        }
    }

    private static String convertThreeDigits(String threeDigits) {

// check the first digit x00
        switch (getIntVal(threeDigits.charAt(0))) {

            case 1: {// 100 - 199
                if (getIntVal(threeDigits.charAt(1)) == 0) {// 10x
                    if (getIntVal(threeDigits.charAt(2)) == 0) {// 100
                        return "مائه";
                    } else {// 10x x: is not 0
                        return "مائه" + " و " + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else {// 1xx x: is not 0
                    return "مائه" + " و " + convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            case 2: {// 200 - 299
                if (getIntVal(threeDigits.charAt(1)) == 0) {// 20x
                    if (getIntVal(threeDigits.charAt(2)) == 0) {// 200
                        return "مائتين";
                    } else {// 20x x:not 0
                        return "مائتين" + " و " + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else {// 2xx x:not 0
                    return "مائتين" + " و " + convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: {// 300 - 999
                if (getIntVal(threeDigits.charAt(1)) == 0) {// x0x x:not 0
                    if (getIntVal(threeDigits.charAt(2)) == 0) {// x00 x:not 0
                        return convertOneDigits(String.valueOf(threeDigits.charAt(1) + "مائه"));
                    } else {// x0x x:not 0
                        return convertOneDigits(String.valueOf(threeDigits.charAt(0))) + "مائه" + " و "
                                + convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else {// xxx x:not 0
                    return convertOneDigits(String.valueOf(threeDigits.charAt(0))) + "مائه" + " و "
                            + convertTwoDigits(threeDigits.substring(1, 3));
                }
            }

            case 0: {// 000 - 099
                if (threeDigits.charAt(1) == '0') {// 00x
                    if (threeDigits.charAt(2) == '0') {// 000
                        return "";
                    } else {// 00x x:not 0
                        return convertOneDigits(String.valueOf(threeDigits.charAt(2)));
                    }
                } else {// 0xx x:not 0
                    return convertTwoDigits(threeDigits.substring(1, 3));
                }
            }
            default:
                return "";
        }
    }

    private static String convertFourDigits(String fourDigits) {
// xxxx
        switch (getIntVal(fourDigits.charAt(0))) {

            case 1: {// 1000 - 1999
                if (getIntVal(fourDigits.charAt(1)) == 0) {// 10xx x:not 0
                    if (getIntVal(fourDigits.charAt(2)) == 0) {// 100x x:not 0
                        if (getIntVal(fourDigits.charAt(3)) == 0) {// 1000
                            return "ألف";
                        } else {// 100x x:not 0
                            return "ألف" + " و " + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else {// 10xx x:not 0
                        return "ألف" + " و " + convertTwoDigits(fourDigits.substring(2, 3));
                    }
                } else {// 1xxx x:not 0
                    return "ألف" + " و " + convertThreeDigits(fourDigits.substring(1, 4));
                }
            }
            case 2: {// 2000 - 2999
                if (getIntVal(fourDigits.charAt(1)) == 0) {// 20xx
                    if (getIntVal(fourDigits.charAt(2)) == 0) {// 200x
                        if (getIntVal(fourDigits.charAt(3)) == 0) {// 2000
                            return "ألفين";
                        } else {// 200x x:not 0
                            return "ألفين" + " و " + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else {// 20xx x:not 0
                        return "ألفين" + " و " + convertTwoDigits(fourDigits.substring(2, 3));
                    }
                } else {// 2xxx x:not 0
                    return "ألفين" + " و " + convertThreeDigits(fourDigits.substring(1, 4));
                }
            }
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9: {// 3000 - 9999
                if (getIntVal(fourDigits.charAt(1)) == 0) {// x0xx x:not 0
                    if (getIntVal(fourDigits.charAt(2)) == 0) {// x00x x:not 0
                        if (getIntVal(fourDigits.charAt(3)) == 0) {// x000 x:not 0
                            return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ألاف";
                        } else {// x00x x:not 0
                            return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ألاف" + " و "
                                    + convertOneDigits(String.valueOf(fourDigits.charAt(3)));
                        }
                    } else {// x0xx x:not 0
                        return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ألاف" + " و "
                                + convertTwoDigits(fourDigits.substring(2, 3));
                    }
                } else {// xxxx x:not 0
                    return convertOneDigits(String.valueOf(fourDigits.charAt(0))) + " ألاف" + " و "
                            + convertThreeDigits(fourDigits.substring(1, 4));
                }
            }

            default:
                return "";
        }
    }

    private static String convertFiveDigits(String fiveDigits) {
        if (convertThreeDigits(fiveDigits.substring(2, 5)).length() == 0) {// xx000
// x:not
// 0
            return convertTwoDigits(fiveDigits.substring(0, 2)) + " ألف ";
        } else {// xxxxx x:not 0
            return convertTwoDigits(fiveDigits.substring(0, 2)) + " ألفا " + " و "
                    + convertThreeDigits(fiveDigits.substring(2, 5));
        }
    }

    private static String convertSixDigits(String sixDigits) {

        if (convertThreeDigits(sixDigits.substring(2, 5)).length() == 0) {// xxx000
// x:not
// 0
            return convertThreeDigits(sixDigits.substring(0, 3)) + " ألف ";
        } else {// xxxxxx x:not 0
            return convertThreeDigits(sixDigits.substring(0, 3)) + " ألفا " + " و "
                    + convertThreeDigits(sixDigits.substring(3, 6));
        }
    }

    private static int getIntVal(char c) {
        return Integer.parseInt(String.valueOf(c));
    }

}
