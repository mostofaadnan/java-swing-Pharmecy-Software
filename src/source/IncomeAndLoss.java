/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author adnan
 */
public class IncomeAndLoss extends javax.swing.JInternalFrame {
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    java.sql.Date date;
    int month = 0;
    String year = null;
    String year1 = null;
    String description;
    /**
     * Creates new form IncomeAndLoss
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.sql.SQLException
     */
    public IncomeAndLoss() throws IOException, FileNotFoundException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        
        
    }
    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        //  inputdate.setDate(date);

    }
        private void dayClose() throws SQLException {
        purchase();
        creditinvoice();
        cashinvoice();
        totalinvoice();
        purchasepayment();
        totalpurchasePyment();
       
        expensess();
        saleryexpensess();
        totalexp();
        wastage();
       stock();
       income();

    }
private void purchase() {
        try {
            String sql = "select sum(nettotal)as 'totalpurchase' from purchase where GRNstatus=1 AND pdate='" + date + "' OR MONTH(pdate)='" + month + "' AND YEAR(pdate)='" + year + "' OR YEAR(pdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String Purchase = rs.getString("totalpurchase");

                if (Purchase == null) {
                    purchasetext.setText("0");

                } else {
                    purchasetext.setText(Purchase);

                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
  private void creditinvoice() {
        try {
            String sql = "select sum(Totalinvoice)as 'totalcredit',sum(totalprofite) as creditprofit from sale where invoicedate='" + date + "' OR MONTH(invoicedate)='" + month + "' AND YEAR(invoicedate)='" + year + "' OR YEAR(invoicedate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String totalcredit = rs.getString("totalcredit");

                if (totalcredit == null) {
                    TotalInvoicetext.setText("0");
                } else {
                    TotalInvoicetext.setText(totalcredit);

                }
                String creditprofit=rs.getString("creditprofit");
                if(creditprofit==null){
                 creditprofittext.setText("0");
                }else{
                 creditprofittext.setText(creditprofit);
                
                
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void cashinvoice() {
        try {
            String sql = "select sum(Totalinvoice)as 'totalcash',sum(totalprofite) as creditprofit from cashsale where invoicedate='" + date + "'OR MONTH(invoicedate)='" + month + "' AND YEAR(invoicedate)='" + year + "' OR YEAR(invoicedate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String totalcash = rs.getString("totalcash");

                if (totalcash == null) {
                    TotalInvoicecashtext.setText("0");

                } else {

                    TotalInvoicecashtext.setText(totalcash);
                }
                   String cashprofit=rs.getString("creditprofit");
                if(cashprofit==null){
                 cashprofittext.setText("0");
                }else{
                 cashprofittext.setText(cashprofit);
                
                
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalinvoice() {
        double creditinvoice = 0;
        double cashinvoice = 0;
        double totalinvoice = 0;
        creditinvoice = Double.parseDouble(TotalInvoicetext.getText());
        cashinvoice = Double.parseDouble(TotalInvoicecashtext.getText());
        totalinvoice = creditinvoice + cashinvoice;
        String totlcashins = String.format("%.2f", totalinvoice);
        totalinvoicetext.setText(totlcashins);
      
        
        double creditprofit=0;
        double cashprofit=0;
        double totalprofit=0;
        creditprofit = Double.parseDouble(creditprofittext.getText());
        cashprofit = Double.parseDouble(cashprofittext.getText());
        totalprofit = creditprofit + cashprofit;
        String totalprofits = String.format("%.2f", totalprofit);
        totalprofitext.setText(totalprofits);
        totalprofitextone.setText(totalprofits);
        
    }

    private void purchasepayment() {
        String cashtype = "Cash";
        try {
            String sql = "select sum(paidamount)as 'purchasecashpayment' from parchasepayment where paymenttype='" + cashtype + "' AND paymentdate='" + date + "'OR MONTH(paymentdate)='" + month + "' AND YEAR(paymentdate)='" + year + "' OR YEAR(paymentdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String purchasecashpayment = rs.getString("purchasecashpayment");

                if (purchasecashpayment == null) {

                    cashpaymenttext.setText("0");
                } else {

                    cashpaymenttext.setText(purchasecashpayment);
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        String banktype = "Bank";
        try {
            String sql = "select sum(paidamount)as 'purchasebankpayment' from parchasepayment where paymenttype='" + banktype + "'AND paymentdate='" + date + "'OR MONTH(paymentdate)='" + month + "' AND YEAR(paymentdate)='" + year + "' OR YEAR(paymentdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String purchasebankpayment = rs.getString("purchasebankpayment");

                if (purchasebankpayment == null) {
                    chequepayment.setText("0");

                } else {
                    chequepayment.setText(purchasebankpayment);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalpurchasePyment() {
        double cashpayment = 0;
        double bankpayment = 0;
        double totalpurchasepayment = 0;
        cashpayment = Double.parseDouble(cashpaymenttext.getText());
        bankpayment = Double.parseDouble(chequepayment.getText());
        totalpurchasepayment = cashpayment + bankpayment;
        String totalpurchasepayments = String.format("%.2f", totalpurchasepayment);
        totalpurchasepaymenttext.setText(totalpurchasepayments);

    }
   private void expensess() {
        try {
            String sql = "select sum(amount)as 'tamount' from expencess where inputdate='" + date + "' OR MONTH(inputdate)='" + month + "' AND YEAR(inputdate)='" + year + "' OR YEAR(inputdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String amount = rs.getString("tamount");

                if (amount == null) {
                    TotalExpencesstext.setText("0");

                } else {
                    TotalExpencesstext.setText(amount);

                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
  private void saleryexpensess() {
        try {
            String sql = "select sum(amount)as 'tamount' from emplyersallery where inputdate='" + date + "' OR MONTH(inputdate)='" + month + "' AND YEAR(inputdate)='" + year + "' OR YEAR(inputdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String amount = rs.getString("tamount");

                if (amount == null) {
                    saleryExpencesstext.setText("0");

                } else {
                    saleryExpencesstext.setText(amount);

                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
  
   private void totalexp() {
        double saleryexp = 0;
        double otherexp = 0;
       double totlexp=0;

        saleryexp = Double.parseDouble(saleryExpencesstext.getText());
        otherexp = Double.parseDouble(TotalExpencesstext.getText());
        totlexp = saleryexp + otherexp;
        String totalexps = String.format("%.2f", totlexp);
        totalexp.setText(totalexps);
         totalexpone.setText(totalexps);
    }
      private void wastage() {
        try {
            String sql = "select sum(totalamount)as 'tamount' from wastage where setDate='" + date + "' OR MONTH(setDate)='" + month + "' AND YEAR(setDate)='" + year + "' OR YEAR(setDate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String amount = rs.getString("tamount");

                if (amount == null) {
                    wastagetext.setText("0");
                    wastagetextone.setText("0");

                } else {
                    wastagetext.setText(amount);
                     wastagetextone.setText(amount);
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
          private void stock() throws SQLException {
       
        double totaltradeprice=0;
        double alltotaltrade=0;
        double totalsaleeprice=0;
        double alltotalsale=0;
        try {

            String sql = "Select ita.tp as 'tradeprice', ita.mrp as 'saleprice',(Select st.Qty from stock st where st.procode=ita.id) as 'Stock' from item ita";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();

            while (rs.next()) {
                
               
                double tp = rs.getDouble("tradeprice");
                double mrp = rs.getDouble("saleprice");
                float qty = rs.getFloat("Stock");
               
                if (qty < 0) {

                    totaltradeprice = 0;
                    totalsaleeprice = 0;

                } else {

                    totaltradeprice = qty * tp;
                    totalsaleeprice = qty * mrp;

                }
                
              
              alltotaltrade=alltotaltrade+totaltradeprice;
              String totaltradeprices = String.format("%.2f", alltotaltrade);
              totaltradepricetext.setText(totaltradeprices);
              
               alltotalsale=alltotalsale+totalsaleeprice;
              String alltotalsales = String.format("%.2f", alltotalsale);
              totalsalepricetext.setText(alltotalsales);
              
              double profite = alltotalsale - alltotaltrade;
              String profites = String.format("%.2f", profite);
              profittetxt.setText(profites);
            }



        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
       
    }
      private void income(){
      double profit=Double.parseDouble(totalprofitextone.getText());
      double expencess=Double.parseDouble(totalexpone.getText());
      double wastage=Double.parseDouble(wastagetextone.getText());
      double loss=expencess+wastage;
      double income=profit-loss;
       
       String status=null;
       if(income>0){
       String incomes = String.format("%.2f", income);
       incometext.setText(incomes);
       status="Your Bussiness Status is Profitable Position.In this condition your Income: "+income+"/=";
       statustext.setForeground(Color.green);
       incomelebele.setText("Income");
       }else if(income<0){
           
           String incomes = String.format("%.2f", Math.abs(income));
       incometext.setText(incomes);
       status="Your Bussiness Status is Loss Position.In this condition you have Loss: "+Math.abs(income)+"/=";
       statustext.setForeground(Color.red);
       incomelebele.setText("Loss");
       }else{
       String incomes = String.format("%.2f", income);
       incometext.setText(incomes);
       status="You have no any Income Or Loss In this Condition: "+income+"/=";
       statustext.setForeground(Color.black);
       incomelebele.setText("Income/Loss");
       }
      statustext.setText(status);
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
        reportbox = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        fromdatepayment1 = new com.toedter.calendar.JDateChooser();
        jLabel57 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        monthbox = new javax.swing.JComboBox<>();
        yeartext = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        overalsubmit = new javax.swing.JButton();
        jLabel59 = new javax.swing.JLabel();
        descriptiontext = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        purchasetext = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        chequepayment = new javax.swing.JTextField();
        cashpaymenttext = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        totalpurchasepaymenttext = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        totaltradepricetext = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        totalsalepricetext = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        profittetxt = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        TotalInvoicetext = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        TotalInvoicecashtext = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        totalinvoicetext = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cashprofittext = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        creditprofittext = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        totalprofitext = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        TotalExpencesstext = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        saleryExpencesstext = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        totalexp = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        wastagetext = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        totalprofitextone = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        totalexpone = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        wastagetextone = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        incomelebele = new javax.swing.JLabel();
        incometext = new javax.swing.JTextField();
        statustext = new javax.swing.JLabel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel12 = new javax.swing.JPanel();
        reportbox1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        fromdatepayment2 = new com.toedter.calendar.JDateChooser();
        jLabel58 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        monthbox1 = new javax.swing.JComboBox<>();
        yeartext1 = new javax.swing.JTextField();
        jLabel62 = new javax.swing.JLabel();
        overalsubmit1 = new javax.swing.JButton();
        jLabel60 = new javax.swing.JLabel();
        descriptiontext1 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        purchasetext1 = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        chequepayment1 = new javax.swing.JTextField();
        cashpaymenttext1 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        totalpurchasepaymenttext1 = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        totaltradepricetext1 = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        totalsalepricetext1 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        profittetxt1 = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        TotalInvoicetext1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        TotalInvoicecashtext1 = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        totalinvoicetext1 = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        cashprofittext1 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        creditprofittext1 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        totalprofitext1 = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        TotalExpencesstext1 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        saleryExpencesstext1 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        totalexp1 = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        wastagetext1 = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        totalprofitextone1 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        totalexpone1 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        wastagetextone1 = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel42 = new javax.swing.JLabel();
        incometext1 = new javax.swing.JTextField();
        statustext1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Income & Lose");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(67, 86, 86));

        reportbox.setBackground(new java.awt.Color(0, 204, 0));
        reportbox.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        reportbox.setForeground(new java.awt.Color(255, 255, 255));
        reportbox.setText("Print");
        reportbox.setBorder(null);
        reportbox.setEnabled(false);
        reportbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportboxActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("Today");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        fromdatepayment1.setDateFormatString("yyyy-MM-dd");

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(51, 51, 51));
        jLabel57.setText("Day");

        jButton2.setText("Submit");
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
                .addComponent(jLabel57)
                .addGap(4, 4, 4)
                .addComponent(fromdatepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fromdatepayment1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );

        monthbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "January", "February", "March", "April", "May", "June", "July", "Agust", "September", "October", "November", "December" }));

        yeartext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        yeartext.setForeground(new java.awt.Color(102, 0, 0));
        yeartext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                yeartextKeyPressed(evt);
            }
        });

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(51, 51, 51));
        jLabel61.setText("Year");

        overalsubmit.setForeground(new java.awt.Color(51, 51, 51));
        overalsubmit.setText("Submit");
        overalsubmit.setBorder(null);
        overalsubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overalsubmitActionPerformed(evt);
            }
        });

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(51, 51, 51));
        jLabel59.setText("Month");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel59)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(monthbox, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel61)
                .addGap(9, 9, 9)
                .addComponent(yeartext, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(overalsubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(monthbox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(overalsubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yeartext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        descriptiontext.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        descriptiontext.setForeground(new java.awt.Color(255, 255, 102));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reportbox, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(descriptiontext, javax.swing.GroupLayout.PREFERRED_SIZE, 1030, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(reportbox, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptiontext, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Purchase Section"));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 51));
        jLabel1.setText("Purchase");

        purchasetext.setEditable(false);
        purchasetext.setBackground(new java.awt.Color(255, 255, 255));
        purchasetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchasetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        purchasetext.setText("0");
        purchasetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        purchasetext.setEnabled(false);
        purchasetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchasetextActionPerformed(evt);
            }
        });

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Purchase Payment"));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 51));
        jLabel2.setText("Bank Payment");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 51));
        jLabel3.setText("Cash Payment");

        chequepayment.setEditable(false);
        chequepayment.setBackground(new java.awt.Color(255, 255, 255));
        chequepayment.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        chequepayment.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        chequepayment.setText("0");
        chequepayment.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        chequepayment.setEnabled(false);
        chequepayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chequepaymentActionPerformed(evt);
            }
        });

        cashpaymenttext.setEditable(false);
        cashpaymenttext.setBackground(new java.awt.Color(255, 255, 255));
        cashpaymenttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cashpaymenttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashpaymenttext.setText("0");
        cashpaymenttext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        cashpaymenttext.setEnabled(false);
        cashpaymenttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashpaymenttextActionPerformed(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 102, 51));
        jLabel30.setText("Total");

        totalpurchasepaymenttext.setEditable(false);
        totalpurchasepaymenttext.setBackground(new java.awt.Color(255, 255, 255));
        totalpurchasepaymenttext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalpurchasepaymenttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalpurchasepaymenttext.setText("0");
        totalpurchasepaymenttext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalpurchasepaymenttext.setEnabled(false);
        totalpurchasepaymenttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalpurchasepaymenttextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel30))
                .addGap(10, 10, 10)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cashpaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chequepayment, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalpurchasepaymenttext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cashpaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(chequepayment, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(totalpurchasepaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(purchasetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(purchasetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Stock"));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 51));
        jLabel4.setText("Trade Price(TP)");

        totaltradepricetext.setEditable(false);
        totaltradepricetext.setBackground(new java.awt.Color(255, 255, 255));
        totaltradepricetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totaltradepricetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totaltradepricetext.setText("0");
        totaltradepricetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totaltradepricetext.setEnabled(false);
        totaltradepricetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totaltradepricetextActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 51));
        jLabel5.setText("Market Price(MRP)");

        totalsalepricetext.setEditable(false);
        totalsalepricetext.setBackground(new java.awt.Color(255, 255, 255));
        totalsalepricetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalsalepricetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalsalepricetext.setText("0");
        totalsalepricetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalsalepricetext.setEnabled(false);
        totalsalepricetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalsalepricetextActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 51));
        jLabel6.setText("Profit");

        profittetxt.setEditable(false);
        profittetxt.setBackground(new java.awt.Color(255, 255, 255));
        profittetxt.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        profittetxt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        profittetxt.setText("0");
        profittetxt.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        profittetxt.setEnabled(false);
        profittetxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profittetxtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totaltradepricetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalsalepricetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profittetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totaltradepricetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalsalepricetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profittetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Income Section"));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 51));
        jLabel7.setText("Credit Invoice");

        TotalInvoicetext.setEditable(false);
        TotalInvoicetext.setBackground(new java.awt.Color(255, 255, 255));
        TotalInvoicetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        TotalInvoicetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalInvoicetext.setText("0");
        TotalInvoicetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TotalInvoicetext.setEnabled(false);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 102, 51));
        jLabel14.setText("Cash Invoice");

        TotalInvoicecashtext.setEditable(false);
        TotalInvoicecashtext.setBackground(new java.awt.Color(255, 255, 255));
        TotalInvoicecashtext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        TotalInvoicecashtext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalInvoicecashtext.setText("0");
        TotalInvoicecashtext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TotalInvoicecashtext.setEnabled(false);

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 102, 51));
        jLabel34.setText("Total Invoice");

        totalinvoicetext.setEditable(false);
        totalinvoicetext.setBackground(new java.awt.Color(255, 255, 255));
        totalinvoicetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalinvoicetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalinvoicetext.setText("0");
        totalinvoicetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalinvoicetext.setEnabled(false);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Profit"));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 102, 51));
        jLabel8.setText("Cash Profit");

        cashprofittext.setEditable(false);
        cashprofittext.setBackground(new java.awt.Color(255, 255, 255));
        cashprofittext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cashprofittext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashprofittext.setText("0");
        cashprofittext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        cashprofittext.setEnabled(false);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 102, 51));
        jLabel15.setText("Credit Profit");

        creditprofittext.setEditable(false);
        creditprofittext.setBackground(new java.awt.Color(255, 255, 255));
        creditprofittext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        creditprofittext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        creditprofittext.setText("0");
        creditprofittext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        creditprofittext.setEnabled(false);

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 102, 51));
        jLabel35.setText("Total Profit");

        totalprofitext.setEditable(false);
        totalprofitext.setBackground(new java.awt.Color(255, 255, 255));
        totalprofitext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalprofitext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalprofitext.setText("0");
        totalprofitext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalprofitext.setEnabled(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(19, 19, 19)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cashprofittext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(creditprofittext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalprofitext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(28, Short.MAX_VALUE)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 107, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel7Layout.createSequentialGroup()
                    .addGap(4, 4, 4)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cashprofittext)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(1, 1, 1)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(creditprofittext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(1, 1, 1)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalprofitext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(TotalInvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TotalInvoicecashtext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalinvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 64, Short.MAX_VALUE)))
                .addGap(5, 5, 5))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TotalInvoicetext)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TotalInvoicecashtext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalinvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Expenses Section"));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 102, 51));
        jLabel9.setText("Expensess");

        TotalExpencesstext.setEditable(false);
        TotalExpencesstext.setBackground(new java.awt.Color(255, 255, 255));
        TotalExpencesstext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        TotalExpencesstext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalExpencesstext.setText("0");
        TotalExpencesstext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TotalExpencesstext.setEnabled(false);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 102, 51));
        jLabel11.setText("Salery Expenses");

        saleryExpencesstext.setEditable(false);
        saleryExpencesstext.setBackground(new java.awt.Color(255, 255, 255));
        saleryExpencesstext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleryExpencesstext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        saleryExpencesstext.setText("0");
        saleryExpencesstext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        saleryExpencesstext.setEnabled(false);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 102, 51));
        jLabel12.setText("Total Expenses");

        totalexp.setEditable(false);
        totalexp.setBackground(new java.awt.Color(255, 255, 255));
        totalexp.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalexp.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalexp.setText("0");
        totalexp.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalexp.setEnabled(false);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalexp, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(saleryExpencesstext, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(TotalExpencesstext))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TotalExpencesstext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(saleryExpencesstext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalexp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(72, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Wastage Section"));

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 102, 51));
        jLabel13.setText("Product Wastage");

        wastagetext.setEditable(false);
        wastagetext.setBackground(new java.awt.Color(255, 255, 255));
        wastagetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        wastagetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        wastagetext.setText("0");
        wastagetext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        wastagetext.setEnabled(false);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel13)
                .addGap(3, 3, 3)
                .addComponent(wastagetext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wastagetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(72, Short.MAX_VALUE))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Income Section"));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 102, 51));
        jLabel16.setText("Total Profit");

        totalprofitextone.setEditable(false);
        totalprofitextone.setBackground(new java.awt.Color(255, 255, 255));
        totalprofitextone.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalprofitextone.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalprofitextone.setText("0");
        totalprofitextone.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalprofitextone.setEnabled(false);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 102, 51));
        jLabel17.setText("Total  Expenses");

        totalexpone.setEditable(false);
        totalexpone.setBackground(new java.awt.Color(255, 255, 255));
        totalexpone.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalexpone.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalexpone.setText("0");
        totalexpone.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalexpone.setEnabled(false);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 102, 51));
        jLabel18.setText("Total  Wastage");

        wastagetextone.setEditable(false);
        wastagetextone.setBackground(new java.awt.Color(255, 255, 255));
        wastagetextone.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        wastagetextone.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        wastagetextone.setText("0");
        wastagetextone.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        wastagetextone.setEnabled(false);

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        incomelebele.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        incomelebele.setForeground(new java.awt.Color(0, 102, 51));
        incomelebele.setText("Income");

        incometext.setEditable(false);
        incometext.setBackground(new java.awt.Color(255, 255, 255));
        incometext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        incometext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        incometext.setText("0");
        incometext.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        incometext.setEnabled(false);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(totalexpone, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(totalprofitextone, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(wastagetextone, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(incomelebele, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(incometext, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(5, 5, 5))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalprofitextone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalexpone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wastagetextone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(incometext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(incomelebele, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statustext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        statustext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jInternalFrame1.setBackground(new java.awt.Color(255, 255, 255));
        jInternalFrame1.setClosable(true);
        jInternalFrame1.setIconifiable(true);
        jInternalFrame1.setMaximizable(true);
        jInternalFrame1.setResizable(true);
        jInternalFrame1.setTitle("Income & Lose");

        jPanel12.setBackground(new java.awt.Color(67, 86, 86));

        reportbox1.setBackground(new java.awt.Color(0, 204, 0));
        reportbox1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        reportbox1.setForeground(new java.awt.Color(255, 255, 255));
        reportbox1.setText("Print");
        reportbox1.setBorder(null);
        reportbox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportbox1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton3.setText("Today");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        fromdatepayment2.setDateFormatString("yyyy-MM-dd");

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(51, 51, 51));
        jLabel58.setText("Day");

        jButton4.setText("Submit");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel58)
                .addGap(4, 4, 4)
                .addComponent(fromdatepayment2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fromdatepayment2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );

        monthbox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "January", "February", "March", "April", "May", "June", "July", "Agust", "September", "October", "November", "December" }));

        yeartext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        yeartext1.setForeground(new java.awt.Color(102, 0, 0));
        yeartext1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                yeartext1KeyPressed(evt);
            }
        });

        jLabel62.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(51, 51, 51));
        jLabel62.setText("Year");

        overalsubmit1.setForeground(new java.awt.Color(51, 51, 51));
        overalsubmit1.setText("Submit");
        overalsubmit1.setBorder(null);
        overalsubmit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                overalsubmit1ActionPerformed(evt);
            }
        });

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(51, 51, 51));
        jLabel60.setText("Month");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel60)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(monthbox1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel62)
                .addGap(9, 9, 9)
                .addComponent(yeartext1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(overalsubmit1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(monthbox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(overalsubmit1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yeartext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        descriptiontext1.setForeground(new java.awt.Color(255, 255, 102));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reportbox1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(descriptiontext1, javax.swing.GroupLayout.PREFERRED_SIZE, 1030, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel12Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(reportbox1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descriptiontext1, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Purchase Section"));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 102, 51));
        jLabel10.setText("Purchase");

        purchasetext1.setEditable(false);
        purchasetext1.setBackground(new java.awt.Color(255, 255, 255));
        purchasetext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchasetext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        purchasetext1.setText("0");
        purchasetext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        purchasetext1.setEnabled(false);
        purchasetext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchasetext1ActionPerformed(evt);
            }
        });

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Purchase Payment"));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 102, 51));
        jLabel20.setText("Bank Payment");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 102, 51));
        jLabel21.setText("Cash Payment");

        chequepayment1.setEditable(false);
        chequepayment1.setBackground(new java.awt.Color(255, 255, 255));
        chequepayment1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        chequepayment1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        chequepayment1.setText("0");
        chequepayment1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        chequepayment1.setEnabled(false);
        chequepayment1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chequepayment1ActionPerformed(evt);
            }
        });

        cashpaymenttext1.setEditable(false);
        cashpaymenttext1.setBackground(new java.awt.Color(255, 255, 255));
        cashpaymenttext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cashpaymenttext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashpaymenttext1.setText("0");
        cashpaymenttext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        cashpaymenttext1.setEnabled(false);
        cashpaymenttext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashpaymenttext1ActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 102, 51));
        jLabel31.setText("Total");

        totalpurchasepaymenttext1.setEditable(false);
        totalpurchasepaymenttext1.setBackground(new java.awt.Color(255, 255, 255));
        totalpurchasepaymenttext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalpurchasepaymenttext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalpurchasepaymenttext1.setText("0");
        totalpurchasepaymenttext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalpurchasepaymenttext1.setEnabled(false);
        totalpurchasepaymenttext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalpurchasepaymenttext1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21)
                    .addComponent(jLabel31))
                .addGap(10, 10, 10)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cashpaymenttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chequepayment1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalpurchasepaymenttext1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cashpaymenttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(chequepayment1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel16Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(totalpurchasepaymenttext1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(purchasetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(purchasetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Stock"));

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 102, 51));
        jLabel22.setText("Trade Price(TP)");

        totaltradepricetext1.setEditable(false);
        totaltradepricetext1.setBackground(new java.awt.Color(255, 255, 255));
        totaltradepricetext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totaltradepricetext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totaltradepricetext1.setText("0");
        totaltradepricetext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totaltradepricetext1.setEnabled(false);
        totaltradepricetext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totaltradepricetext1ActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 102, 51));
        jLabel23.setText("Market Price(MRP)");

        totalsalepricetext1.setEditable(false);
        totalsalepricetext1.setBackground(new java.awt.Color(255, 255, 255));
        totalsalepricetext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalsalepricetext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalsalepricetext1.setText("0");
        totalsalepricetext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalsalepricetext1.setEnabled(false);
        totalsalepricetext1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalsalepricetext1ActionPerformed(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 102, 51));
        jLabel24.setText("Profit");

        profittetxt1.setEditable(false);
        profittetxt1.setBackground(new java.awt.Color(255, 255, 255));
        profittetxt1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        profittetxt1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        profittetxt1.setText("0");
        profittetxt1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        profittetxt1.setEnabled(false);
        profittetxt1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profittetxt1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totaltradepricetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalsalepricetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profittetxt1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totaltradepricetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalsalepricetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profittetxt1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Income Section"));

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 102, 51));
        jLabel25.setText("Credit Invoice");

        TotalInvoicetext1.setEditable(false);
        TotalInvoicetext1.setBackground(new java.awt.Color(255, 255, 255));
        TotalInvoicetext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        TotalInvoicetext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalInvoicetext1.setText("0");
        TotalInvoicetext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TotalInvoicetext1.setEnabled(false);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 102, 51));
        jLabel26.setText("Cash Invoice");

        TotalInvoicecashtext1.setEditable(false);
        TotalInvoicecashtext1.setBackground(new java.awt.Color(255, 255, 255));
        TotalInvoicecashtext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        TotalInvoicecashtext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalInvoicecashtext1.setText("0");
        TotalInvoicecashtext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TotalInvoicecashtext1.setEnabled(false);

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 102, 51));
        jLabel36.setText("Total Invoice");

        totalinvoicetext1.setEditable(false);
        totalinvoicetext1.setBackground(new java.awt.Color(255, 255, 255));
        totalinvoicetext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalinvoicetext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalinvoicetext1.setText("0");
        totalinvoicetext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalinvoicetext1.setEnabled(false);

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Profit"));

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 102, 51));
        jLabel27.setText("Cash Profit");

        cashprofittext1.setEditable(false);
        cashprofittext1.setBackground(new java.awt.Color(255, 255, 255));
        cashprofittext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cashprofittext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashprofittext1.setText("0");
        cashprofittext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        cashprofittext1.setEnabled(false);

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 102, 51));
        jLabel28.setText("Credit Profit");

        creditprofittext1.setEditable(false);
        creditprofittext1.setBackground(new java.awt.Color(255, 255, 255));
        creditprofittext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        creditprofittext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        creditprofittext1.setText("0");
        creditprofittext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        creditprofittext1.setEnabled(false);

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 102, 51));
        jLabel37.setText("Total Profit");

        totalprofitext1.setEditable(false);
        totalprofitext1.setBackground(new java.awt.Color(255, 255, 255));
        totalprofitext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalprofitext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalprofitext1.setText("0");
        totalprofitext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalprofitext1.setEnabled(false);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addGap(19, 19, 19)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(cashprofittext1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(creditprofittext1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalprofitext1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(28, Short.MAX_VALUE)))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 107, Short.MAX_VALUE)
            .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel19Layout.createSequentialGroup()
                    .addGap(4, 4, 4)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(cashprofittext1)
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(1, 1, 1)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(creditprofittext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(1, 1, 1)
                    .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(totalprofitext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(TotalInvoicetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(TotalInvoicecashtext1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(totalinvoicetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 64, Short.MAX_VALUE)))
                .addGap(5, 5, 5))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TotalInvoicetext1)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TotalInvoicecashtext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalinvoicetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Expenses Section"));

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 102, 51));
        jLabel29.setText("Expensess");

        TotalExpencesstext1.setEditable(false);
        TotalExpencesstext1.setBackground(new java.awt.Color(255, 255, 255));
        TotalExpencesstext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        TotalExpencesstext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalExpencesstext1.setText("0");
        TotalExpencesstext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        TotalExpencesstext1.setEnabled(false);

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 102, 51));
        jLabel32.setText("Salery Expenses");

        saleryExpencesstext1.setEditable(false);
        saleryExpencesstext1.setBackground(new java.awt.Color(255, 255, 255));
        saleryExpencesstext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleryExpencesstext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        saleryExpencesstext1.setText("0");
        saleryExpencesstext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        saleryExpencesstext1.setEnabled(false);

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 102, 51));
        jLabel33.setText("Total Expenses");

        totalexp1.setEditable(false);
        totalexp1.setBackground(new java.awt.Color(255, 255, 255));
        totalexp1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalexp1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalexp1.setText("0");
        totalexp1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalexp1.setEnabled(false);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalexp1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(saleryExpencesstext1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(TotalExpencesstext1))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TotalExpencesstext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(saleryExpencesstext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalexp1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Wastage Section"));

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 102, 51));
        jLabel38.setText("Product Wastage");

        wastagetext1.setEditable(false);
        wastagetext1.setBackground(new java.awt.Color(255, 255, 255));
        wastagetext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        wastagetext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        wastagetext1.setText("0");
        wastagetext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        wastagetext1.setEnabled(false);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel38)
                .addGap(3, 3, 3)
                .addComponent(wastagetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wastagetext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Income Section"));

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(0, 102, 51));
        jLabel39.setText("Total Profit");

        totalprofitextone1.setEditable(false);
        totalprofitextone1.setBackground(new java.awt.Color(255, 255, 255));
        totalprofitextone1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalprofitextone1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalprofitextone1.setText("0");
        totalprofitextone1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalprofitextone1.setEnabled(false);

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 102, 51));
        jLabel40.setText("Total  Expenses");

        totalexpone1.setEditable(false);
        totalexpone1.setBackground(new java.awt.Color(255, 255, 255));
        totalexpone1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalexpone1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalexpone1.setText("0");
        totalexpone1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        totalexpone1.setEnabled(false);

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 102, 51));
        jLabel41.setText("Total  Wastage");

        wastagetextone1.setEditable(false);
        wastagetextone1.setBackground(new java.awt.Color(255, 255, 255));
        wastagetextone1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        wastagetextone1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        wastagetextone1.setText("0");
        wastagetextone1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        wastagetextone1.setEnabled(false);

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 102, 51));
        jLabel42.setText("Income");

        incometext1.setEditable(false);
        incometext1.setBackground(new java.awt.Color(255, 255, 255));
        incometext1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        incometext1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        incometext1.setText("0");
        incometext1.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        incometext1.setEnabled(false);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(totalexpone1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(totalprofitextone1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(wastagetextone1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel22Layout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(incometext1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(21, 21, 21))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalprofitextone1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalexpone1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(wastagetextone1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(incometext1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statustext1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        statustext1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(1, 1, 1)
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(1, 1, 1)
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(statustext1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jInternalFrame1Layout.createSequentialGroup()
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(70, 70, 70)
                .addComponent(statustext1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(1, 1, 1)
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(1, 1, 1)
                        .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(statustext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statustext, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void reportboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportboxActionPerformed
/*
        try {

            JasperDesign jd = JRXmlLoader.load(new File("")
                .getAbsolutePath()
                + "/src/Report/Dayclose.jrxml");

            //    JasperDesign jd = JRXmlLoader.load("C:\\Users\\adnan\\Documents\\NetBeansProjects\\MobileInvoice\\src\\Riport\\voiceriport.jrxml");
            HashMap para = new HashMap();

            para.put("purchase",purchasetext.getText());
            para.put("import",importtext.getText());
            para.put("export",exporttext.getText());
            para.put("creditinvoice",TotalInvoicetext.getText());
            para.put("cashinvoice",TotalInvoicecashtext.getText());
            para.put("totalinvoice",totalinvoicetext.getText());
            para.put("pcaspay",cashpaymenttext.getText());
            para.put("pbanpay",chequepayment.getText());
            para.put("ptotalpay",totalpurchasepaymenttext.getText());
            para.put("imcashpay",importpaymentcashtext.getText());
            para.put("imbankpay",importpaymentbanktext.getText());
            para.put("totalimpay",totalimportpaymenttext.getText());
            para.put("tcashpay",totalpaymenttext.getText());
            para.put("tbankpay",totalpaymentbank.getText());
            para.put("totalpay",nettotalpaymenttext.getText());
            para.put("crcasre",creditcashrecievetext.getText());
            para.put("crbancr",creditbankrecievetext.getText());
            para.put("totalcrre",totalcredittext.getText());
            para.put("excasre",exportcashrecievetext.getText());
            para.put("exbanre",exportbankrecievetext.getText());
            para.put("tocashre",totalcashrecievestext.getText());
            para.put("tobanre",totalbankrecievestext.getText());
            para.put("totalrecieve",nettotalrecievetext.getText());
            para.put("toexre",totalexportrecivetext.getText());
            para.put("bankcashout",bankcashintext.getText());
            para.put("bankcashin",bankcashouttext.getText());
            para.put("drwercashin",cashboxcashin.getText());
            para.put("drwercashout",cashboxcashouttext.getText());
            para.put("totlcashin",totalcashintext.getText());
            para.put("totlcashout",totalcashouttext.getText());
            para.put("bankbalanc",bankbalancetext.getText());
            para.put("cashbalance",cashbalancetext.getText());
            para.put("slrexp",saleryExpencesstext.getText());
            para.put("otherexp",TotalExpencesstext.getText());
            para.put("totalexp",totalexp.getText());
            para.put("description",description);

            JasperReport jr = JasperCompileManager.compileReport(jd);

            JasperPrint jp = JasperFillManager.fillReport(jr, para, conn);

            JasperViewer.viewReport(jp, false);

        } catch (NumberFormatException | JRException ex) {

            JOptionPane.showMessageDialog(null, ex);

        }
        */
    }//GEN-LAST:event_reportboxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        yeartext.setText(null);
        year1 = null;
        ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).setText(null);
        month = 0;
        year = null;
        yeartext.setBackground(Color.WHITE);
        currentDate();
        try {
            dayClose();
        } catch (SQLException ex) {
            Logger.getLogger(IncomeAndLoss.class.getName()).log(Level.SEVERE, null, ex);
        }
        description = "Description:" + " Current Date: " + date;
        descriptiontext.setText(description);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //String dates = ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).getText();
      if(((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).getText().isEmpty()){
      
      JOptionPane.showMessageDialog(null, "Please Fillup Date Value");
      }
      else{
        yeartext.setText(null);
        monthbox.setSelectedIndex(0);
        month = 0;
        year = null;
        year1 = null;
        yeartext.setBackground(Color.WHITE);
        date = new java.sql.Date(fromdatepayment1.getDate().getTime());

        try {
            dayClose();
        } catch (SQLException ex) {
            Logger.getLogger(IncomeAndLoss.class.getName()).log(Level.SEVERE, null, ex);
        }
        description = "Description:" + " Day Search: " + date;
        descriptiontext.setText(description);
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void yeartextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_yeartextKeyPressed
      
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            yeartext.setBackground(Color.YELLOW);
            monthbox.setSelectedIndex(0);
            month = 0;
            year = null;
            ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).setText(null);
            date = null;
            year1 = yeartext.getText();
            try {
                dayClose();
            } catch (SQLException ex) {
                Logger.getLogger(IncomeAndLoss.class.getName()).log(Level.SEVERE, null, ex);
            }
            description = "Description:" + " Yearly Search: " + "Year: " + yeartext.getText();
            descriptiontext.setText(description);

        }
    }//GEN-LAST:event_yeartextKeyPressed

    private void overalsubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overalsubmitActionPerformed

        if (monthbox.getSelectedIndex() == 0 || yeartext.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select Month AND Year");
        } else {
            yeartext.setBackground(Color.WHITE);
            year1 = null;
            date = null;
            month = monthbox.getSelectedIndex();
            year = yeartext.getText();
            try {
                dayClose();
            } catch (SQLException ex) {
                Logger.getLogger(IncomeAndLoss.class.getName()).log(Level.SEVERE, null, ex);
            }
            description = "Description:" + "Monthly Search: " + "Month: " + monthbox.getSelectedItem() + " Year: " + yeartext.getText();
            descriptiontext.setText(description);

        }


    }//GEN-LAST:event_overalsubmitActionPerformed

    private void purchasetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchasetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchasetextActionPerformed

    private void chequepaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chequepaymentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chequepaymentActionPerformed

    private void cashpaymenttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashpaymenttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cashpaymenttextActionPerformed

    private void totalpurchasepaymenttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalpurchasepaymenttextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalpurchasepaymenttextActionPerformed

    private void totaltradepricetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totaltradepricetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totaltradepricetextActionPerformed

    private void totalsalepricetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalsalepricetextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalsalepricetextActionPerformed

    private void profittetxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profittetxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_profittetxtActionPerformed

    private void reportbox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportbox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reportbox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void yeartext1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_yeartext1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_yeartext1KeyPressed

    private void overalsubmit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overalsubmit1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_overalsubmit1ActionPerformed

    private void purchasetext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchasetext1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchasetext1ActionPerformed

    private void chequepayment1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chequepayment1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chequepayment1ActionPerformed

    private void cashpaymenttext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashpaymenttext1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cashpaymenttext1ActionPerformed

    private void totalpurchasepaymenttext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalpurchasepaymenttext1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalpurchasepaymenttext1ActionPerformed

    private void totaltradepricetext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totaltradepricetext1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totaltradepricetext1ActionPerformed

    private void totalsalepricetext1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalsalepricetext1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalsalepricetext1ActionPerformed

    private void profittetxt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profittetxt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_profittetxt1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TotalExpencesstext;
    private javax.swing.JTextField TotalExpencesstext1;
    private javax.swing.JTextField TotalInvoicecashtext;
    private javax.swing.JTextField TotalInvoicecashtext1;
    private javax.swing.JTextField TotalInvoicetext;
    private javax.swing.JTextField TotalInvoicetext1;
    private javax.swing.JTextField cashpaymenttext;
    private javax.swing.JTextField cashpaymenttext1;
    private javax.swing.JTextField cashprofittext;
    private javax.swing.JTextField cashprofittext1;
    private javax.swing.JTextField chequepayment;
    private javax.swing.JTextField chequepayment1;
    private javax.swing.JTextField creditprofittext;
    private javax.swing.JTextField creditprofittext1;
    private javax.swing.JLabel descriptiontext;
    private javax.swing.JLabel descriptiontext1;
    private com.toedter.calendar.JDateChooser fromdatepayment1;
    private com.toedter.calendar.JDateChooser fromdatepayment2;
    private javax.swing.JLabel incomelebele;
    private javax.swing.JTextField incometext;
    private javax.swing.JTextField incometext1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JInternalFrame jInternalFrame1;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JComboBox<String> monthbox;
    private javax.swing.JComboBox<String> monthbox1;
    private javax.swing.JButton overalsubmit;
    private javax.swing.JButton overalsubmit1;
    private javax.swing.JTextField profittetxt;
    private javax.swing.JTextField profittetxt1;
    private javax.swing.JTextField purchasetext;
    private javax.swing.JTextField purchasetext1;
    private javax.swing.JButton reportbox;
    private javax.swing.JButton reportbox1;
    private javax.swing.JTextField saleryExpencesstext;
    private javax.swing.JTextField saleryExpencesstext1;
    private javax.swing.JLabel statustext;
    private javax.swing.JLabel statustext1;
    private javax.swing.JTextField totalexp;
    private javax.swing.JTextField totalexp1;
    private javax.swing.JTextField totalexpone;
    private javax.swing.JTextField totalexpone1;
    private javax.swing.JTextField totalinvoicetext;
    private javax.swing.JTextField totalinvoicetext1;
    private javax.swing.JTextField totalprofitext;
    private javax.swing.JTextField totalprofitext1;
    private javax.swing.JTextField totalprofitextone;
    private javax.swing.JTextField totalprofitextone1;
    private javax.swing.JTextField totalpurchasepaymenttext;
    private javax.swing.JTextField totalpurchasepaymenttext1;
    private javax.swing.JTextField totalsalepricetext;
    private javax.swing.JTextField totalsalepricetext1;
    private javax.swing.JTextField totaltradepricetext;
    private javax.swing.JTextField totaltradepricetext1;
    private javax.swing.JTextField wastagetext;
    private javax.swing.JTextField wastagetext1;
    private javax.swing.JTextField wastagetextone;
    private javax.swing.JTextField wastagetextone1;
    private javax.swing.JTextField yeartext;
    private javax.swing.JTextField yeartext1;
    // End of variables declaration//GEN-END:variables
}
