/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class SalePoint extends javax.swing.JFrame {


    double tpprice;
    double bankbalance1;
    int tree = 0;
    String Invoiceno;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    /**
     * Creates new form SalePoint
     *

     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public SalePoint() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        currentDate();
        Item();
        customer();
        AutoCompleteDecorator.decorate(unibox);
        AutoCompleteDecorator.decorate(itemnamesearch);

        unit();

        currency();
        parchase_code();
        
      
    }
    
    
    
    
    
	final private  static String[] units = {"Zero","One","Two","Three","Four",
		"Five","Six","Seven","Eight","Nine","Ten",
		"Eleven","Twelve","Thirteen","Fourteen","Fifteen",
		"Sixteen","Seventeen","Eighteen","Nineteen"};
	final private static String[] tens = {"","","Twenty","Thirty","Forty","Fifty",
		"Sixty","Seventy","Eighty","Ninety"};


	public static String convert(Integer i) {
		//
		if( i < 20)  return units[i];
		if( i < 100) return tens[i/10] + ((i % 10 > 0)? " " + convert(i % 10):"");
		if( i < 1000) return units[i/100] + " Hundred" + ((i % 100 > 0)?" and " + convert(i % 100):"");
		if( i < 1000000) return convert(i / 1000) + " Thousand " + ((i % 1000 > 0)? " " + convert(i % 1000):"") ;
		return convert(i / 1000000) + " Million " + ((i % 1000000 > 0)? " " + convert(i % 1000000):"") ;
	} 
        
    public void balance() throws SQLException {

        try {
            String sql = "Select sum(cashin),sum(cashout) from CashDrower";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {

                double cashin = rs.getDouble("sum(cashin)");
                double cashout = rs.getDouble("sum(cashout)");
                bankbalance1 = (cashin - cashout);

            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void parchase_code() {
        try {
            int new_inv = 1;
            String NewParchaseCode = ("ST" + new_inv + "");
            String sql = "Select id from sale";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = ("ST" + ParchaseCode + "");
            }

            invoicetext.setText(NewParchaseCode);
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        java.sql.Date date = new java.sql.Date(now.getTime());
        inputdate.setDate(date);

    }

    private void Item() throws SQLException {

        try {
            String sql = "Select itemName from item";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                String name = rs.getString("itemName");
                itemnamesearch.addItem(name);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void unit() throws SQLException {

        try {
            String sql = "Select * from unit";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("unitshort");

                unibox.addItem(name);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void currency() throws SQLException {

        try {
            String sql = "Select * from currency";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("currency");

                currencybox.addItem(name);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void AfterExecute() {

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);
        totalrate.setText(null);
        totalvattext.setText(null);
        totalinvoiceamount.setText(null);
        creditamounttext.setText(null);
        paidamounttext.setText(null);
        balanceduetext.setText(null);
        customerbox.setSelectedIndex(0);
        parchase_code();

    }

    private void checkentry() {

        String s = "";
        boolean exists = false;
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            s = dataTable.getValueAt(i, 1).toString().trim();

            if (codetext.getText().equals("")) {
                // JOptionPane.showMessageDialog(null, "Enter Invoice Details First");
            } else if (codetext.getText().equals(s)) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            entryData();
        } else {
            JOptionPane.showMessageDialog(null, "This Data Already Exist.");
            clear();

        }

    }

    private void entryData() {
        double discount;
        double vat = 0.05;
        double totalvat;

        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        tree++;

        double tpd = Double.parseDouble(unitrateText.getText());
        double productvat = tpd * vat;

        if (discounttext.getText().isEmpty()) {

            discount = 0;

        } else {
            discount = Double.parseDouble(discounttext.getText());

        }
        double tpwdisc = tpd - discount;

        float qty = Float.parseFloat(qtytext.getText());
        double nettotaltp = (tpwdisc * qty);
        totalvat = (productvat * qty);
        double totalall = nettotaltp + totalvat;

        //String gpper = String.format("%.2f", gp);
        model2.addRow(new Object[]{tree, codetext.getText(), itemnamesearch.getSelectedItem(), tpd, qty, unibox.getSelectedItem(), discount, nettotaltp, productvat, totalvat, totalall});
        ///  totalrate.setText(Integer.toString(total_action_rate()));
        totalrate.setText(Double.toString(total_action_mrp()));
        totalvattext.setText(Double.toString(total_action_vat()));
        totalinvoiceamount.setText(Integer.toString(total_invoice_amount()));
        netdiscount.setText(Double.toString(total_discount_amount()));

        clear();
    }

    private int total_action_mrp() {

        int rowaCount = dataTable.getRowCount();

        double totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Double.parseDouble(dataTable.getValueAt(i, 7).toString());
        }

        return (int) totaltpmrp;

    }

    private int total_action_vat() {

        int rowaCount = dataTable.getRowCount();

        double totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Double.parseDouble(dataTable.getValueAt(i, 9).toString());
        }

        return (int) totaltpmrp;

    }

    private int total_invoice_amount() {

        int rowaCount = dataTable.getRowCount();

        double totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Double.parseDouble(dataTable.getValueAt(i, 10).toString());
        }

        return (int) totaltpmrp;

    }
 private int total_discount_amount() {

        int rowaCount = dataTable.getRowCount();

        double totaltpmrp = 0;

        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());

            totaltpmrp = totaltpmrp + Double.parseDouble(dataTable.getValueAt(i, 6).toString());
        }

        return (int) totaltpmrp;

    }
    private void clear() {
        codetext.setText(null);
        itemnamesearch.setSelectedIndex(0);
        unitrateText.setText(null);
        qtytext.setText(null);
        unibox.setSelectedIndex(0);
    }

    private void saleInsert() {
        double chasin = Double.parseDouble(totalinvoiceamount.getText());
        try {

            String sql = "Insert into sale(invoiceNo,invoicedate,paymentCurency,paymentType,netdiscount,TotalAmount,TotalVat,Totalinvoice,customerid) values (?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, invoicetext.getText());
            pst.setString(2, ((JTextField) inputdate.getDateEditor().getUiComponent()).getText());
            pst.setString(3, (String) currencybox.getSelectedItem());
            pst.setString(4, (String) paymenttypebox.getSelectedItem());
            pst.setString(5, netdiscount.getText());
            pst.setString(6, totalrate.getText());
            pst.setString(7, totalvattext.getText());
            pst.setString(8, totalinvoiceamount.getText());
            pst.setInt(9, 1);
            
            pst.execute();
            cashInDrwaer(chasin);
            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void SaleDetailsInsert() throws SQLException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String procode = (String) dataTable.getValueAt(row, 1);
                try {
                    String sql = "Select * from item where id='" + procode + "'";
                    pst = conn.prepareStatement(sql);
                    rs = pst.executeQuery();
                    //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
                    while (rs.next()) {

                        tpprice = rs.getDouble("tp");

                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }

                double rate =(Double) dataTable.getValueAt(row, 3);

                float qty = (Float) dataTable.getValueAt(row, 4);

                String unit = (String) dataTable.getValueAt(row, 5);
                double discount = (Double) dataTable.getValueAt(row, 6);
                double totalamount = (Double) dataTable.getValueAt(row, 7);

                double vat = (Double) dataTable.getValueAt(row, 8);
                double totalvat = (Double) dataTable.getValueAt(row, 9);
                double neTotal = (Double) dataTable.getValueAt(row, 10);
                try {

                    String sql = "Insert into saleDetails(invoiceNo,prcode,unitrate,qty,unit,discount,commesion,totalamount,vat,Totalvat,NetTotal,tradprice,invoicedate) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, invoicetext.getText());
                    pst.setString(2, procode);
                    pst.setDouble(3, rate);
                    pst.setFloat(4, qty);
                    pst.setString(5, unit);
                    pst.setDouble(6, discount);
                    pst.setDouble(7, 0);
                    pst.setDouble(8, totalamount);
                    pst.setDouble(9, vat);
                    pst.setDouble(10, totalvat);
                    pst.setDouble(11, neTotal);
                    pst.setDouble(12, tpprice);
                    pst.setString(13, ((JTextField) inputdate.getDateEditor().getUiComponent()).getText());
                    pst.execute();

///    examperticipationinsert(id,examcode,exam);
                    // 
                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }

            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void cashInDrwaer(double cashin) {
        Double balanc = bankbalance1 + cashin;
        try {

            String sql = "Insert into CashDrower(cashin,cashout,balance,type,upatedate,tranid) values(?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setDouble(1, cashin);
            pst.setDouble(2, 0);
            pst.setDouble(3, balanc);
            pst.setString(4, "Sale Product");
            pst.setString(5, ((JTextField) inputdate.getDateEditor().getUiComponent()).getText());
            pst.setString(6, invoicetext.getText());
            pst.execute();

            JOptionPane.showMessageDialog(null, "Data Saved");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void printInvoice() throws JRException {
        Invoiceno = invoicetext.getText();
        String User;
      
        String Paymenttype = (String) paymenttypebox.getSelectedItem();
        // int i=1; 
        if (customerbox.getSelectedIndex() == 0) {
            User = " ";

        } else {
            User = (String) customerbox.getSelectedItem();
        }
         
        try {
            
           String currency=(String) currencybox.getSelectedItem();
           int total = Integer.parseInt(totalinvoiceamount.getText());
           String inwords = convert(total)+ currency+" Only";
             
            
          
           JOptionPane.showMessageDialog(null, inwords);
            JasperDesign jd = JRXmlLoader.load(new File("")
                    .getAbsolutePath()
                    + "/src/Report/Invoice.jrxml");
            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");

            HashMap para = new HashMap();
            para.put("invoiceno", Invoiceno);
          

           para.put("inwords", inwords);
           

            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

            JasperPrintManager.printReport(jp, true);
            //JasperViewer.viewReport(jp, true);

        } catch (NumberFormatException | JRException ex){
            JOptionPane.showMessageDialog(null, ex);

        }

    }

    private void Stockpdate() {
        // int cat=(Integer) categoryBox.getSelectedIndex();
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            String cat = dataTable.getValueAt(i, 8).toString().trim();

            String s = dataTable.getValueAt(i, 1).toString().trim();
            float qty = (Float) dataTable.getValueAt(i, 4);

            Stockpdateumuse(s, qty);

        }

    }

    private void Stockpdateumuse(String s, float qtyint) {

        try {

            String sql = "Select * from Stock where procode=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, s);
            rs = pst.executeQuery();

            while (rs.next()) {

                float stock = rs.getFloat("qty");

                float update_qty = stock - qtyint;

                String sql1 = "Update stock set qty='" + update_qty + "' where procode='" + s + "'";

                pst = conn.prepareStatement(sql1);

                pst.execute();

            }  //JOptionPane.showMessageDialog(null, add1);

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void checkUpdate() {

        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int i = dataTable.getSelectedRow();
        if (i >= 0) {
            double discount;
            double vat = 0.05;
            double totalvat;

            double tpd = Double.parseDouble(unitrateText.getText());
            double productvat = tpd * vat;

            if (discounttext.getText().isEmpty()) {

                discount = 0;

            } else {
                discount = Double.parseDouble(discounttext.getText());

            }
            double tpwdisc = tpd - discount;

            float qty = Float.parseFloat(qtytext.getText());
            double nettotaltp = (tpwdisc * qty);
            totalvat = (productvat * qty);
            double totalall = nettotaltp + totalvat;

            model.setValueAt(qty, i, 4);
            model.setValueAt(nettotaltp, i, 7);
            model.setValueAt(totalvat, i, 9);
            model.setValueAt(totalall, i, 10);

        }

        updarerext.setText("0");
        totalrate.setText(Double.toString(total_action_mrp()));
        totalvattext.setText(Double.toString(total_action_vat()));
        totalinvoiceamount.setText(Double.toString(total_invoice_amount()));
        netdiscount.setText(Double.toString(total_discount_amount()));
        clear();

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
        jLabel1 = new javax.swing.JLabel();
        invoicetext = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        unitrateText = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        qtytext = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        unibox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        discounttext = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        inputdate = new com.toedter.calendar.JDateChooser();
        updarerext = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        customerbox = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        creditamounttext = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        paidamounttext = new javax.swing.JTextField();
        balanceduetext = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        totalinvoiceamount = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        netdiscount = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        totalrate = new javax.swing.JTextField();
        totalvattext = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        currencybox = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        paymenttypebox = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setExtendedState(6);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 51, 0));
        jLabel1.setText("Invoice No");

        invoicetext.setBackground(new java.awt.Color(255, 255, 255));
        invoicetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        invoicetext.setForeground(new java.awt.Color(153, 0, 0));
        invoicetext.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Code");

        codetext.setBackground(new java.awt.Color(255, 255, 204));
        codetext.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        codetext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        codetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codetextKeyTyped(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 51, 0));
        jLabel4.setText("Item Name");

        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 102, 0));
        jLabel5.setText("Unit Rate");

        unitrateText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        unitrateText.setForeground(new java.awt.Color(153, 0, 0));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 0, 0));
        jLabel6.setText("Qty");

        qtytext.setBackground(new java.awt.Color(255, 255, 204));
        qtytext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        qtytext.setForeground(new java.awt.Color(0, 102, 0));
        qtytext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        qtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytextKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Unit");

        unibox.setEditable(true);
        unibox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        unibox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        jButton1.setBackground(new java.awt.Color(153, 0, 0));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("ADD");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(153, 0, 0));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("CLEAR");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 102, 0));
        jLabel15.setText("Discount");

        discounttext.setBackground(new java.awt.Color(255, 255, 204));
        discounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        discounttext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setText("Date");

        inputdate.setDateFormatString("yyyy-MM-dd");
        inputdate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        updarerext.setText("0");

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setText("Customer Name");

        customerbox.setEditable(true);
        customerbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(10, 10, 10)
                        .addComponent(invoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(390, 390, 390)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(inputdate, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addGap(27, 27, 27)
                        .addComponent(customerbox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(updarerext, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addGap(7, 7, 7)
                                .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(jLabel6)
                                .addGap(4, 4, 4)
                                .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(4, 4, 4)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(discounttext, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(invoicetext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(inputdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(customerbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updarerext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(codetext)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(unitrateText)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(qtytext)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(unibox)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(discounttext, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(itemnamesearch)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 51)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("Cradit Amount");
        jPanel4.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 12, -1, 30));

        creditamounttext.setBackground(new java.awt.Color(255, 255, 204));
        creditamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        creditamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        creditamounttext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        creditamounttext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                creditamounttextKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                creditamounttextKeyTyped(evt);
            }
        });
        jPanel4.add(creditamounttext, new org.netbeans.lib.awtextra.AbsoluteConstraints(151, 13, 140, 30));

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("Paid Amount");
        jPanel4.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 49, -1, 30));

        paidamounttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        paidamounttext.setForeground(new java.awt.Color(204, 0, 0));
        paidamounttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paidamounttext.setDisabledTextColor(new java.awt.Color(204, 0, 0));
        paidamounttext.setEnabled(false);
        paidamounttext.setSelectedTextColor(new java.awt.Color(102, 0, 0));
        jPanel4.add(paidamounttext, new org.netbeans.lib.awtextra.AbsoluteConstraints(151, 50, 140, 30));

        balanceduetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        balanceduetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        balanceduetext.setDisabledTextColor(new java.awt.Color(102, 0, 0));
        balanceduetext.setEnabled(false);
        balanceduetext.setSelectedTextColor(new java.awt.Color(102, 0, 0));
        jPanel4.add(balanceduetext, new org.netbeans.lib.awtextra.AbsoluteConstraints(151, 87, 140, 30));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 0, 0));
        jLabel13.setText("Balance Due ");
        jPanel4.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(11, 86, -1, 30));

        dataTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        dataTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL.No", "Item Code", "Item Name", "Unit Rate", "Qty", "Unit", "Discount", "Total Amount", "Vat(5%)", "Total Vat", "Net Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTable.setRowHeight(30);
        dataTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(dataTable);
        if (dataTable.getColumnModel().getColumnCount() > 0) {
            dataTable.getColumnModel().getColumn(0).setMinWidth(50);
            dataTable.getColumnModel().getColumn(0).setMaxWidth(50);
            dataTable.getColumnModel().getColumn(1).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setMinWidth(400);
            dataTable.getColumnModel().getColumn(2).setMaxWidth(400);
            dataTable.getColumnModel().getColumn(3).setResizable(false);
            dataTable.getColumnModel().getColumn(4).setResizable(false);
            dataTable.getColumnModel().getColumn(5).setResizable(false);
            dataTable.getColumnModel().getColumn(6).setResizable(false);
            dataTable.getColumnModel().getColumn(7).setResizable(false);
        }

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 51)));

        jButton3.setBackground(new java.awt.Color(255, 0, 0));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Execute");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(0, 102, 0));
        jButton4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Reload");
        jButton4.setBorder(null);

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton5.setText("Cancel");
        jButton5.setBorder(null);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 53, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 51)));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 0, 0));
        jLabel8.setText("Total Amount");

        totalinvoiceamount.setBackground(new java.awt.Color(255, 255, 153));
        totalinvoiceamount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalinvoiceamount.setForeground(new java.awt.Color(0, 51, 0));
        totalinvoiceamount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalinvoiceamount.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        totalinvoiceamount.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalinvoiceamount.setEnabled(false);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(204, 0, 0));
        jLabel9.setText("VAT(5%)");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 0, 0));
        jLabel10.setText("Invoice Amount");

        netdiscount.setBackground(new java.awt.Color(204, 204, 255));
        netdiscount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        netdiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        netdiscount.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        netdiscount.setDisabledTextColor(new java.awt.Color(255, 255, 255));
        netdiscount.setEnabled(false);
        netdiscount.setSelectedTextColor(new java.awt.Color(51, 51, 51));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 153, 51));
        jLabel14.setText("Total Discount");

        totalrate.setBackground(new java.awt.Color(255, 204, 204));
        totalrate.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalrate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalrate.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        totalrate.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalrate.setEnabled(false);

        totalvattext.setBackground(new java.awt.Color(204, 255, 204));
        totalvattext.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        totalvattext.setForeground(new java.awt.Color(204, 51, 0));
        totalvattext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalvattext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        totalvattext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalvattext.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(45, 45, 45)
                        .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalinvoiceamount, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(netdiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(totalrate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(netdiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalvattext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalinvoiceamount, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(84, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Amount", jPanel5);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setText("Curency Type");

        currencybox.setEditable(true);
        currencybox.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setText("Payment Type");

        paymenttypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Credit" }));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(currencybox, 0, 191, Short.MAX_VALUE)
                    .addComponent(paymenttypebox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currencybox, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(paymenttypebox)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(150, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Payment", jPanel8);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTabbedPane1))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 15, Short.MAX_VALUE)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jMenu1.setText("File");

        jMenuItem1.setText("Close");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("SignOut");
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
        String SearchText = (String) itemnamesearch.getSelectedItem();
        try {

            String sql = "Select id,itemName,stockunit,mrp from item where itemName='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("id");
                codetext.setText(Id);
                String unitrate = rs.getString("mrp");
                unitrateText.setText(unitrate);
                String unit = rs.getString("stockunit");
                unibox.setSelectedItem(unit);
            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        creditamounttext.setText(null);
        paidamounttext.setText(null);
        balanceduetext.setText(null);
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
        } else if ("0".equals(updarerext.getText())) {
            checkentry();
        } else {
            checkUpdate();

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void codetextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyTyped
        unitrateText.setText(null);
        itemnamesearch.setSelectedIndex(0);
    }//GEN-LAST:event_codetextKeyTyped

    private void codetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyReleased
        itemnamesearch.removeAllItems();

        String SearchText = (String) codetext.getText();
        try {

            String sql = "Select id,itemName, mrp from item where id='" + SearchText + "' OR barcode='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            if (rs.next()) {

                String name = rs.getString("itemName");
                itemnamesearch.setSelectedItem(name);
                String unitrate = rs.getString("mrp");
                unitrateText.setText(unitrate);

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {
            Item();
        } catch (SQLException ex) {
            Logger.getLogger(SalePoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_codetextKeyReleased

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (creditamounttext.getText().isEmpty() || paidamounttext.getText().isEmpty() || balanceduetext.getText().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Paid Amount Requird");
        } else {
            try {
                saleInsert();
                SaleDetailsInsert();
                Stockpdate();
                printInvoice();
                AfterExecute();
            } catch (SQLException | JRException ex) {
                Logger.getLogger(SalePoint.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void creditamounttextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_creditamounttextKeyPressed

        if (totalinvoiceamount.getText().isEmpty() || creditamounttext.getText().isEmpty()) {
        } else {

            double creditAmunt = Double.parseDouble(creditamounttext.getText());
            double paidAmount = Double.parseDouble(totalinvoiceamount.getText());
            if (evt.getKeyCode() != KeyEvent.VK_ENTER) {
            } else if (paidAmount <= creditAmunt) {
                double balanceDue = (creditAmunt - paidAmount);
                String balanceDuestring = String.format("%.2f", balanceDue);
                balanceduetext.setText(balanceDuestring);
                paidamounttext.setText(totalinvoiceamount.getText());

            } else {

            }
        }


    }//GEN-LAST:event_creditamounttextKeyPressed

    private void creditamounttextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_creditamounttextKeyTyped


    }//GEN-LAST:event_creditamounttextKeyTyped

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        int selectedRowIndex = dataTable.getSelectedRow();
        updarerext.setText("1");
        codetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        unitrateText.setText(model.getValueAt(selectedRowIndex, 3).toString());
        qtytext.setText(model.getValueAt(selectedRowIndex, 4).toString());
        unibox.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());
    }//GEN-LAST:event_dataTableMouseClicked

    private void qtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyPressed

        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
        } else if ("0".equals(updarerext.getText())) {
            checkentry();
        } else {
            checkUpdate();

        }
    }//GEN-LAST:event_qtytextKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       clear();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
      dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
            java.util.logging.Logger.getLogger(SalePoint.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField balanceduetext;
    private javax.swing.JTextField codetext;
    private javax.swing.JTextField creditamounttext;
    private javax.swing.JComboBox<String> currencybox;
    private javax.swing.JComboBox<String> customerbox;
    private javax.swing.JTable dataTable;
    private javax.swing.JTextField discounttext;
    private com.toedter.calendar.JDateChooser inputdate;
    private javax.swing.JLabel invoicetext;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField netdiscount;
    private javax.swing.JTextField paidamounttext;
    private javax.swing.JComboBox<String> paymenttypebox;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextField totalinvoiceamount;
    private javax.swing.JTextField totalrate;
    private javax.swing.JTextField totalvattext;
    private javax.swing.JComboBox<String> unibox;
    private javax.swing.JTextField unitrateText;
    private javax.swing.JLabel updarerext;
    // End of variables declaration//GEN-END:variables
}
