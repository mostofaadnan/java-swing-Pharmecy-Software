/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author adnan
 */
public final class Dashboard extends javax.swing.JFrame {

    LoginFrame das = new LoginFrame();
    int userid = 0;
    public int id = 5;

    //int id;
    String loginu;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    java.sql.Date date;
    String key = "abc";

    String CompanyName;
    LicenseKey licensekey = new LicenseKey();
    private static final DecimalFormat df2 = new DecimalFormat("#.00");

    /**
     * Creates new form Dashboard
     *
     * @param LoginUser
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public Dashboard(int LoginUser) throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userid = LoginUser;
        id = LoginUser;
        licencekey();
        userInformation();
        intilize();
        userInformation();
        Useraccess(LoginUser);
        CurrentDatetime();
        hostname();
        currentDate();

        //  dayClose();
        //companytext.hide();
    }

    public Dashboard() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        licencekey();
        intilize();
        userInformation();
        currentDate();
        CurrentDatetime();

        // jLabel4.hide();
        // companytext.hide();
        // dayClose();
        //id=2;
    }

    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        // inputdate.setText(date);

    }

    private void dayClose() {
        purchase();
        creditinvoice();
        cashinvoice();
        totalinvoice();
        purchasepayment();
        totalpurchasePyment();
        expensess();
        creditrecive();
        totalcreditrecieve();
        bankbalance();
        cashbalance();

    }

    private void purchase() {
        try {
            String sql = "select sum(nettotal)as 'totalpurchase' from purchase where GRNstatus=1 AND pdate='" + date + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String Purchase = rs.getString("totalpurchase");

                if (Purchase == null) {
                    totalinvoicetext.setText("0");

                } else {
                    totalinvoicetext.setText(Purchase);

                }

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void creditinvoice() {
        try {
            String sql = "select sum(nettotal)as 'totalcredit' from sale where invoicedate='" + date + "' ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String totalcredit = rs.getString("totalcredit");

                if (totalcredit == null) {
                    TotalInvoicetext.setText("0");
                } else {
                    TotalInvoicetext.setText(totalcredit);

                }

            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void cashinvoice() {
        try {
            String sql = "select sum(nettotal)as 'totalcash' from cashsale where invoicedate='" + date + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String totalcash = rs.getString("totalcash");

                if (totalcash == null) {
                    TotalInvoicecashtext.setText("0");
                } else {

                    TotalInvoicecashtext.setText(totalcash);
                }

            }
        } catch (SQLException e) {
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

    }

    private void purchasepayment() {
        String cashtype = "Cash";
        try {
            String sql = "select sum(paidamount)as 'purchasecashpayment' from purchasepaymenthistory where paymenttype='" + cashtype + "' AND paymentdate='" + date + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String purchasecashpayment = rs.getString("purchasecashpayment");
                if (purchasecashpayment == null) {
                    purchasepaymentcash.setText("0");
                } else {
                    purchasepaymentcash.setText(purchasecashpayment);
                }

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        String banktype = "Bank";
        try {
            String sql = "select sum(paidamount)as 'purchasebankpayment' from purchasepaymenthistory where paymenttype='" + banktype + "'AND paymentdate='" + date + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String purchasebankpayment = rs.getString("purchasebankpayment");

                if (purchasebankpayment == null) {
                    purchasepaymentbank.setText("0");

                } else {
                    purchasepaymentbank.setText(purchasebankpayment);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalpurchasePyment() {
        double cashpayment = 0;
        double bankpayment = 0;
        double totalpurchasepayment = 0;
        cashpayment = Double.parseDouble(purchasepaymentcash.getText());
        bankpayment = Double.parseDouble(purchasepaymentbank.getText());
        totalpurchasepayment = cashpayment + bankpayment;
        String totalpurchasepayments = String.format("%.2f", totalpurchasepayment);
        totalpurchasepaymenttext.setText(totalpurchasepayments);

    }

    private void expensess() {
        try {
            String sql = "select sum(amount)as 'tamount' from expencess where inputdate='" + date + "'";
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

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void creditrecive() {
        String cashtype = "Cash";
        try {
            String sql = "select sum(paidamount)as 'creditcashrecieve' from invoicepaymenthistory where paymenttype='" + cashtype + "' AND paymentdate='" + date + "' ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String creditcashrecieve = rs.getString("creditcashrecieve");
                if (creditcashrecieve == null) {
                    creditcashrecievetext.setText("0");

                } else {
                    creditcashrecievetext.setText(creditcashrecieve);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        String banktype = "Bank";
        try {
            String sql = "select sum(paidamount)as 'creditbankrecieve' from invoicepaymenthistory where paymenttype='" + banktype + "'AND paymentdate='" + date + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                String creditbankrecieve = rs.getString("creditbankrecieve");
                if (creditbankrecieve == null) {

                    creditbankrecievetext.setText("0");
                } else {
                    creditbankrecievetext.setText(creditbankrecieve);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalcreditrecieve() {
        double cashrecieve = 0;
        double creditbankrecieve = 0;
        double totalcreditrecieve = 0;

        cashrecieve = Double.parseDouble(creditcashrecievetext.getText());
        creditbankrecieve = Double.parseDouble(creditbankrecievetext.getText());
        totalcreditrecieve = cashrecieve + creditbankrecieve;
        String totalcreditrecieves = String.format("%.2f", totalcreditrecieve);
        totalcredittext.setText(totalcreditrecieves);
    }

    private void cashbalance() {

        try {
            String sql = "select sum(cashin) as 'cashin',sum(cashout) as 'cashout' from cashdrower";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == true) {
                double cashin = rs.getDouble("cashin");
                double cashout = rs.getDouble("cashout");
                double bankbalance = cashin - cashout;
                String bankbalances = String.format("%.2f", bankbalance);
                cashbalancetext.setText(bankbalances);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void bankbalance() {

        try {
            String sql = "select sum(cashin) as 'cashin',sum(cashout) as 'cashout' from bankoverall ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == true) {
                double cashin = rs.getDouble("cashin");
                double cashout = rs.getDouble("cashout");
                double bankbalance = cashin - cashout;
                String bankbalances = String.format("%.2f", bankbalance);
                bankbalancetext.setText(bankbalances);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void hostname() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ipaddress = "IP Address:- " + inetAddress.getHostAddress();
        String hostname = "Host Name:- " + inetAddress.getHostName();
        hostnametext.setText(ipaddress + " " + hostname);
    }

    private void componantDeactivenavigation() {
        Component[] com = jmenunavigation.getComponents();

        for (Component com1 : com) {
            com1.setEnabled(false);
        }

    }

    private void componantdeactivepanel() {
        Component[] com = jPanel1.getComponents();

        for (Component com1 : com) {

            com1.setEnabled(false);
        }

        jButton9.setEnabled(true);

    }

    private void licencekey() throws FileNotFoundException, IOException {
        FileInputStream fis = new FileInputStream("src\\licensekey.properties");
        Properties p = new Properties();
        p.load(fis);
        String likey = (String) p.get("key");

        String company = licensekey.company(likey);
        this.setTitle(company);
        // companytext.setText(company);

    }

    private void CompanyInfo() throws SQLException {

        try {
            String sql = "Select companyname from reportdesign where id=1;";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                CompanyName = rs.getString("companyname");
                this.setTitle(CompanyName);

            } else {
                JOptionPane.showMessageDialog(null, "Please Make Sure Company Name");

            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    public void CurrentDatetime() {

        Thread clock = new Thread() {

            @Override
            public void run() {

                for (;;) {
                    Calendar cal = new GregorianCalendar();
                    int month = cal.get(Calendar.MONTH);
                    int year = cal.get(Calendar.YEAR);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    date_txt.setText("Date " + day + "/" + (month + 1) + "/" + year);

                    int second = cal.get(Calendar.SECOND);
                    int houre = cal.get(Calendar.HOUR);
                    int minit = cal.get(Calendar.MINUTE);
                    int AM_PM = cal.get(Calendar.AM_PM);
                    String amorpm = null;
                    if (AM_PM == 0) {
                        amorpm = "AM";
                    } else {
                        amorpm = "PM";

                    }

                    time_text.setText("Time: " + houre + ":" + minit + ":" + second + " " + amorpm);
                   // dayClose();
                    try {
                        sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        };

        clock.start();
    }

    public void Homeimage() {

        ImageIcon icone = new ImageIcon(this.getClass().getResource("background.jpg"));
        final Image img = icone.getImage();
        Homedesktop = new javax.swing.JDesktopPane() {

            @Override
            public void paintComponent(Graphics g) {

                g.drawImage(img, 10, 10, getWidth(), getHeight(), this);

            }

        };

    }

    public void userInformation() {

        try {

            String sql = "Select * from admin where id='" + id + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            if (rs.next()) {
                int uid = rs.getInt("id");
                String Name = rs.getString("name");
                usertext1.setText(Name);
                String UserName = rs.getString("UserName");
                unametext1.setText(UserName);

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        // usernametext.setText(LoginUserid);
        // loginu=usernametext.getText();
    }

    private void intilize() {

        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("company.png")));
    }

    private void Useraccess(int LoginUser) {

        try {

            String sql = "Select * from loginaccess where userId='" + LoginUser + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            while (rs.next()) {

                String Accessname = rs.getString("Accessname");

                int access = rs.getInt("access");

                String userpa = userpanel.getText();

                if (Accessname == null ? userpa == null : Accessname.equals(userpa)) {
                    if (access == 1) {
                        userpanel.setEnabled(true);
                        jButton8.setEnabled(true);

                    } else {
                        userpanel.setEnabled(false);
                        jButton8.setEnabled(false);

                    }
                }
                String passwordchang = passwordchange.getText();
                //   JOptionPane.showMessageDialog(null, Accessname);
                if (Accessname == null ? passwordchang == null : Accessname.equals(passwordchang)) {
                    if (access == 1) {
                        passwordchange.setEnabled(true);

                    } else {
                        passwordchange.setEnabled(false);

                    }

                }
                String logout = logouttext.getText();

                if (Accessname == null ? logout == null : Accessname.equals(logout)) {

                    if (access == 1) {
                        logouttext.setEnabled(true);

                    } else {
                        logouttext.setEnabled(false);

                    }
                }

                String category = itemcategory.getText();
                if (Accessname == null ? category == null : Accessname.equals(category)) {

                    if (access == 1) {

                        itemcategory.setEnabled(true);
                    } else {
                        itemcategory.setEnabled(false);

                    }
                }

                String item = itemlist.getText();
                if (Accessname == null ? item == null : Accessname.equals(item)) {

                    if (access == 1) {
                        itemlist.setEnabled(true);
                        itemlistbtn.setEnabled(true);

                    } else {
                        itemlist.setEnabled(false);
                        itemlistbtn.setEnabled(false);

                    }

                }

                String stock = stocktext.getText();
                if (Accessname == null ? stock == null : Accessname.equals(stock)) {

                    if (access == 1) {
                        stocktext.setEnabled(true);
                        stockbtn.setEnabled(true);

                    } else {
                        stocktext.setEnabled(false);
                        stockbtn.setEnabled(false);

                    }
                }

                String wastage = wastagetext.getText();
                if (Accessname == null ? wastage == null : Accessname.equals(wastage)) {

                    if (access == 1) {
                        wastagetext.setEnabled(true);

                    } else {
                        wastagetext.setEnabled(false);

                    }

                }

                /* String labelp = labeltext.getText();
                if (Accessname == null ? labelp == null : Accessname.equals(labelp)) {

                    if (access == 1) {
                        labeltext.setEnabled(true);

                    } else {
                        labeltext.setEnabled(false);

                    }

                }
                 */
                String supply = supplytext.getText();
                if (Accessname == null ? supply == null : Accessname.equals(supply)) {
                    if (access == 1) {
                        supplytext.setEnabled(true);

                    } else {
                        supplytext.setEnabled(false);

                    }
                }
                String supplystatement = suplytatement.getText();
                if (Accessname == null ? supplystatement == null : Accessname.equals(supplystatement)) {
                    if (access == 1) {
                        suplytatement.setEnabled(true);

                    } else {
                        suplytatement.setEnabled(false);

                    }
                }

                String bankinfo = bankinfotext.getText();
                if (Accessname == null ? bankinfo == null : Accessname.equals(bankinfo)) {
                    if (access == 1) {
                        bankinfotext.setEnabled(true);

                    } else {
                        bankinfotext.setEnabled(false);

                    }
                }
                String bankacc = bankacounttext.getText();
                if (Accessname == null ? bankacc == null : Accessname.equals(bankacc)) {

                    if (access == 1) {
                        bankacounttext.setEnabled(true);
                        jButton5.setEnabled(true);

                    } else {
                        bankacounttext.setEnabled(false);
                        jButton5.setEnabled(false);

                    }
                }

                String cashdr = cashdrwertext.getText();
                if (Accessname == null ? cashdr == null : Accessname.equals(cashdr)) {
                    if (access == 1) {
                        cashdrwertext.setEnabled(true);
                        cashboxbtn.setEnabled(true);

                    } else {
                        cashdrwertext.setEnabled(false);
                        cashboxbtn.setEnabled(false);

                    }
                }

                String vatcolection = vatcollectiontext.getText();
                if (Accessname == null ? vatcolection == null : Accessname.equals(vatcolection)) {
                    if (access == 1) {
                        vatcollectiontext.setEnabled(true);

                    } else {
                        vatcollectiontext.setEnabled(false);

                    }
                }
                String dayclose = dayclosetext.getText();
                if (Accessname == null ? dayclose == null : Accessname.equals(dayclose)) {
                    if (access == 1) {
                        dayclosetext.setEnabled(true);

                    } else {
                        dayclosetext.setEnabled(false);

                    }
                }

                String income = incometext.getText();
                if (Accessname == null ? income == null : Accessname.equals(income)) {
                    if (access == 1) {
                        incometext.setEnabled(true);

                    } else {
                        incometext.setEnabled(false);

                    }
                }

                String creditpayment = creditpaymenttext.getText();
                if (Accessname == null ? creditpayment == null : Accessname.equals(creditpayment)) {
                    if (access == 1) {
                        creditpaymenttext.setEnabled(true);

                    } else {
                        creditpaymenttext.setEnabled(false);

                    }
                }
                String unit = uniittext.getText();
                if (Accessname == null ? unit == null : Accessname.equals(unit)) {
                    if (access == 1) {
                        uniittext.setEnabled(true);

                    } else {
                        uniittext.setEnabled(false);

                    }
                }
                String extype = expencesstypetext.getText();
                if (Accessname == null ? extype == null : Accessname.equals(extype)) {

                    if (access == 1) {
                        expencesstypetext.setEnabled(true);

                    } else {
                        expencesstypetext.setEnabled(false);

                    }
                }

                String countrylist = countrylisttext.getText();
                if (Accessname == null ? countrylist == null : Accessname.equals(countrylist)) {
                    if (access == 1) {
                        countrylisttext.setEnabled(true);

                    } else {
                        countrylisttext.setEnabled(false);

                    }
                }
                String companyinfo = companyinfotext.getText();
                if (Accessname == null ? companyinfo == null : Accessname.equals(companyinfo)) {
                    if (access == 1) {
                        companyinfotext.setEnabled(true);

                    } else {
                        companyinfotext.setEnabled(false);

                    }
                }

                String purchase = purchasetext.getText();
                if (Accessname == null ? purchase == null : Accessname.equals(purchase)) {
                    if (access == 1) {
                        purchasetext.setEnabled(true);
                        jButton6.setEnabled(true);

                    } else {
                        purchasetext.setEnabled(false);
                        jButton6.setEnabled(false);

                    }
                }
                String purchasedetails = purchasedetailstext.getText();
                if (Accessname == null ? purchasedetails == null : Accessname.equals(purchasedetails)) {
                    if (access == 1) {
                        purchasedetailstext.setEnabled(true);

                    } else {
                        purchasedetailstext.setEnabled(false);

                    }
                }
                String grn = grntext.getText();
                if (Accessname == null ? grn == null : Accessname.equals(grn)) {
                    if (access == 1) {
                        grntext.setEnabled(true);
                        jButton10.setEnabled(true);

                    } else {
                        grntext.setEnabled(false);
                        jButton10.setEnabled(false);

                    }
                }

                String purchaseretrun = purchasereturntext.getText();
                if (Accessname == null ? purchaseretrun == null : Accessname.equals(purchaseretrun)) {
                    if (access == 1) {
                        purchasereturntext.setEnabled(true);

                    } else {
                        purchasereturntext.setEnabled(false);

                    }
                }

                /*  String salepoint = salepointtext.getText();
                 if (Accessname == null ? salepoint == null : Accessname.equals(salepoint)) {
                 if (access == 1) {
                 salepointtext.setEnabled(true);
                 salepontbtn.setEnabled(true);

                 } else {
                 salepointtext.setEnabled(false);
                 salepontbtn.setEnabled(false);

                 }
                 }

                 */
                String saledetails = saledetailstext.getText();
                if (Accessname == null ? saledetails == null : Accessname.equals(saledetails)) {
                    if (access == 1) {
                        saledetailstext.setEnabled(true);

                    } else {
                        saledetailstext.setEnabled(false);

                    }
                }
                String salereturn = salereturntext.getText();
                if (Accessname == null ? salereturn == null : Accessname.equals(salereturn)) {
                    if (access == 1) {
                        salereturntext.setEnabled(true);

                    } else {
                        salereturntext.setEnabled(false);

                    }
                }

                String cashinvoice = cashinvoicetext.getText();
                if (Accessname == null ? cashinvoice == null : Accessname.equals(cashinvoice)) {
                    if (access == 1) {
                        cashinvoicetext.setEnabled(true);

                    } else {
                        cashinvoicetext.setEnabled(false);

                    }
                }

                String payment = purchasepaymenttext.getText();
                if (Accessname == null ? payment == null : Accessname.equals(payment)) {
                    if (access == 1) {
                        purchasepaymenttext.setEnabled(true);
                        jButton7.setEnabled(true);
                    } else {
                        purchasepaymenttext.setEnabled(false);
                        jButton7.setEnabled(false);

                    }
                }

                String expences = expencesstext.getText();
                if (Accessname == null ? expences == null : Accessname.equals(expences)) {
                    if (access == 1) {
                        expencesstext.setEnabled(true);

                    } else {
                        expencesstext.setEnabled(false);

                    }
                }

                String instancet = instancetext.getText();
                if (Accessname == null ? instancet == null : Accessname.equals(instancet)) {
                    if (access == 1) {
                        instancetext.setEnabled(true);

                    } else {
                        instancetext.setEnabled(false);

                    }
                }

                String customerinfo = customerinfotext.getText();
                if (Accessname == null ? customerinfo == null : Accessname.equals(customerinfo)) {
                    if (access == 1) {
                        customerinfotext.setEnabled(true);

                    } else {
                        customerinfotext.setEnabled(false);

                    }
                }

                String creditstatemen = creditstatementtext.getText();
                if (Accessname == null ? creditstatemen == null : Accessname.equals(creditstatemen)) {
                    if (access == 1) {
                        creditstatementtext.setEnabled(true);

                    } else {
                        creditstatementtext.setEnabled(false);

                    }

                }

                String itemreport = itemreporttext.getText();
                if (Accessname == null ? itemreport == null : Accessname.equals(itemreport)) {
                    if (access == 1) {
                        itemreporttext.setEnabled(true);

                    } else {
                        itemreporttext.setEnabled(false);

                    }

                }

                String salereport = salereporttext.getText();
                if (Accessname == null ? salereport == null : Accessname.equals(salereport)) {
                    if (access == 1) {
                        salereporttext.setEnabled(true);

                    } else {
                        salereporttext.setEnabled(false);

                    }

                }

                String cashboxreport = cashboxreporttext.getText();
                if (Accessname == null ? cashboxreport == null : Accessname.equals(cashboxreport)) {
                    if (access == 1) {
                        cashboxreporttext.setEnabled(true);

                    } else {
                        cashboxreporttext.setEnabled(false);

                    }

                }

                String bankstatemnt = banksatementtext.getText();
                if (Accessname == null ? bankstatemnt == null : Accessname.equals(bankstatemnt)) {
                    if (access == 1) {
                        banksatementtext.setEnabled(true);

                    } else {
                        banksatementtext.setEnabled(false);

                    }

                }

                String grnreport = grndtailstext.getText();
                if (Accessname == null ? grnreport == null : Accessname.equals(grnreport)) {
                    if (access == 1) {
                        grndtailstext.setEnabled(true);

                    } else {
                        grndtailstext.setEnabled(false);

                    }

                }

                String purchasereport = purchereportetx.getText();
                if (Accessname == null ? purchasereport == null : Accessname.equals(purchasereport)) {
                    if (access == 1) {
                        purchereportetx.setEnabled(true);

                    } else {
                        purchereportetx.setEnabled(false);

                    }

                }

                String priceupdate = priceupdatetext.getText();
                if (Accessname == null ? priceupdate == null : Accessname.equals(priceupdate)) {
                    if (access == 1) {
                        priceupdatetext.setEnabled(true);

                    } else {
                        priceupdatetext.setEnabled(false);

                    }

                }

                String databasebackup = databasebeckuptext.getText();
                if (Accessname == null ? databasebackup == null : Accessname.equals(databasebackup)) {
                    if (access == 1) {
                        databasebeckuptext.setEnabled(true);

                    } else {
                        databasebeckuptext.setEnabled(false);

                    }

                }

                String databaserestore = databaserestoretext.getText();
                if (Accessname == null ? databaserestore == null : Accessname.equals(databaserestore)) {
                    if (access == 1) {
                        databaserestoretext.setEnabled(true);

                    } else {
                        databaserestoretext.setEnabled(false);

                    }

                }

                String stockdnd = stockdemand.getText();
                if (Accessname == null ? stockdnd == null : Accessname.equals(stockdnd)) {
                    if (access == 1) {
                        stockdemand.setEnabled(true);

                    } else {
                        stockdemand.setEnabled(false);

                    }

                }
                /*
                String offerl = offerlist.getText();
                if (Accessname == null ? offerl == null : Accessname.equals(offerl)) {
                    if (access == 1) {
                        offerlist.setEnabled(true);

                    } else {
                        offerlist.setEnabled(false);

                    }

                }
                
                 */
                String itemfor = itemforca.getText();
                if (Accessname == null ? itemfor == null : Accessname.equals(itemfor)) {
                    if (access == 1) {
                        itemforca.setEnabled(true);

                    } else {
                        itemforca.setEnabled(false);

                    }

                }
                String supplierta = suppliertarif.getText();
                if (Accessname == null ? supplierta == null : Accessname.equals(supplierta)) {
                    if (access == 1) {
                        suppliertarif.setEnabled(true);

                    } else {
                        suppliertarif.setEnabled(false);

                    }

                }
                String supplierpa = supplierpurchase.getText();
                if (Accessname == null ? supplierpa == null : Accessname.equals(supplierpa)) {
                    if (access == 1) {
                        supplierpurchase.setEnabled(true);

                    } else {
                        supplierpurchase.setEnabled(false);

                    }

                }

                String supplierpay = supplierpayment.getText();
                if (Accessname == null ? supplierpay == null : Accessname.equals(supplierpay)) {
                    if (access == 1) {
                        supplierpayment.setEnabled(true);

                    } else {
                        supplierpayment.setEnabled(false);

                    }
                }
                String supplierfor = supplierforcast.getText();
                if (Accessname == null ? supplierfor == null : Accessname.equals(supplierfor)) {
                    if (access == 1) {
                        supplierforcast.setEnabled(true);

                    } else {
                        supplierforcast.setEnabled(false);

                    }

                }

                String invoice = invoicecheck.getText();

                if (Accessname == null ? invoice == null : Accessname.equals(invoice)) {
                    if (access == 1) {
                        invoicecheck.setEnabled(true);

                    } else {
                        invoicecheck.setEnabled(false);

                    }

                }

                String companydoc = companydocu.getText();
                if (Accessname == null ? companydoc == null : Accessname.equals(companydoc)) {
                    if (access == 1) {
                        companydocu.setEnabled(true);

                    } else {
                        companydocu.setEnabled(false);

                    }

                }

                String supplierdoc = supplierdocu.getText();
                if (Accessname == null ? supplierdoc == null : Accessname.equals(supplierdoc)) {
                    if (access == 1) {
                        supplierdocu.setEnabled(true);

                    } else {
                        supplierdocu.setEnabled(false);

                    }

                }

                String customerdoc = customerdocu.getText();
                if (Accessname == null ? customerdoc == null : Accessname.equals(customerdoc)) {
                    if (access == 1) {
                        customerdocu.setEnabled(true);

                    } else {
                        customerdocu.setEnabled(false);

                    }

                }

                String databaseinfo = datainfo.getText();
                if (Accessname == null ? databaseinfo == null : Accessname.equals(databaseinfo)) {
                    if (access == 1) {
                        datainfo.setEnabled(true);

                    } else {
                        datainfo.setEnabled(false);

                    }

                }

                String employee = employeetext.getText();
                if (Accessname == null ? employee == null : Accessname.equals(employee)) {
                    if (access == 1) {
                        employeetext.setEnabled(true);

                    } else {
                        employeetext.setEnabled(false);

                    }

                }

                String salary = salarytext.getText();
                if (Accessname == null ? salary == null : Accessname.equals(salary)) {
                    if (access == 1) {
                        salarytext.setEnabled(true);

                    } else {
                        salarytext.setEnabled(false);

                    }

                }

            }

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

        jSeparator41 = new javax.swing.JSeparator();
        jSeparator60 = new javax.swing.JSeparator();
        jSeparator70 = new javax.swing.JSeparator();
        Homedesktop = new javax.swing.JDesktopPane();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        TotalInvoicecashtext = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        TotalInvoicetext = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        totalinvoicetext = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        purchasepaymentcash = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        purchasepaymentbank = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        totalpurchasepaymenttext = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cashbalancetext = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        bankbalancetext = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        creditcashrecievetext = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        creditbankrecievetext = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        totalcredittext = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        TotalExpencesstext = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        itemlistbtn = new javax.swing.JButton();
        stockbtn = new javax.swing.JButton();
        salepontbtn = new javax.swing.JButton();
        cashboxbtn = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        time_text = new javax.swing.JLabel();
        date_txt = new javax.swing.JLabel();
        itemlistbtn1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        usertext1 = new javax.swing.JLabel();
        unametext1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        hostnametext = new javax.swing.JLabel();
        jmenunavigation = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        userpanel = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        passwordchange1 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        logouttext = new javax.swing.JMenuItem();
        itemmenu = new javax.swing.JMenu();
        itemlist = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem22 = new javax.swing.JMenuItem();
        jSeparator75 = new javax.swing.JPopupMenu.Separator();
        priceupdatetext = new javax.swing.JMenuItem();
        jSeparator42 = new javax.swing.JPopupMenu.Separator();
        stocktext = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        wastagetext = new javax.swing.JMenuItem();
        jSeparator53 = new javax.swing.JPopupMenu.Separator();
        stockdemand = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        itemforca = new javax.swing.JMenuItem();
        jSeparator57 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator23 = new javax.swing.JPopupMenu.Separator();
        jMenuItem27 = new javax.swing.JMenuItem();
        jSeparator45 = new javax.swing.JPopupMenu.Separator();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        supplytext = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        suplytatement = new javax.swing.JMenuItem();
        jSeparator47 = new javax.swing.JPopupMenu.Separator();
        suppliertarif = new javax.swing.JMenuItem();
        jSeparator46 = new javax.swing.JPopupMenu.Separator();
        supplierpurchase = new javax.swing.JMenuItem();
        jSeparator48 = new javax.swing.JPopupMenu.Separator();
        supplierpayment = new javax.swing.JMenuItem();
        jSeparator49 = new javax.swing.JPopupMenu.Separator();
        supplierforcast = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        bankinfotext = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        bankacounttext = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        cashdrwertext = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItem12 = new javax.swing.JMenuItem();
        jSeparator71 = new javax.swing.JPopupMenu.Separator();
        vatcollectiontext = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        dayclosetext = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        incometext = new javax.swing.JMenuItem();
        jSeparator65 = new javax.swing.JPopupMenu.Separator();
        jMenuItem16 = new javax.swing.JMenuItem();
        jSeparator21 = new javax.swing.JPopupMenu.Separator();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        itemcategory = new javax.swing.JMenuItem();
        jSeparator14 = new javax.swing.JPopupMenu.Separator();
        uniittext = new javax.swing.JMenuItem();
        jSeparator15 = new javax.swing.JPopupMenu.Separator();
        jMenuItem14 = new javax.swing.JMenuItem();
        jSeparator73 = new javax.swing.JPopupMenu.Separator();
        jMenuItem26 = new javax.swing.JMenuItem();
        jSeparator13 = new javax.swing.JPopupMenu.Separator();
        expencesstypetext = new javax.swing.JMenuItem();
        jSeparator16 = new javax.swing.JPopupMenu.Separator();
        countrylisttext = new javax.swing.JMenuItem();
        jSeparator17 = new javax.swing.JPopupMenu.Separator();
        companyinfotext = new javax.swing.JMenuItem();
        jSeparator39 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        jMenuItem11 = new javax.swing.JMenuItem();
        jSeparator52 = new javax.swing.JPopupMenu.Separator();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator67 = new javax.swing.JPopupMenu.Separator();
        jMenuItem8 = new javax.swing.JMenuItem();
        jSeparator68 = new javax.swing.JPopupMenu.Separator();
        jMenuItem9 = new javax.swing.JMenuItem();
        jSeparator69 = new javax.swing.JPopupMenu.Separator();
        jMenuItem10 = new javax.swing.JMenuItem();
        jSeparator44 = new javax.swing.JPopupMenu.Separator();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator74 = new javax.swing.JPopupMenu.Separator();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        purchasetext = new javax.swing.JMenuItem();
        jSeparator18 = new javax.swing.JPopupMenu.Separator();
        purchasedetailstext = new javax.swing.JMenuItem();
        jSeparator19 = new javax.swing.JPopupMenu.Separator();
        grntext = new javax.swing.JMenuItem();
        jSeparator20 = new javax.swing.JPopupMenu.Separator();
        purchasereturntext = new javax.swing.JMenuItem();
        jSeparator22 = new javax.swing.JPopupMenu.Separator();
        jMenuItem17 = new javax.swing.JMenuItem();
        jSeparator50 = new javax.swing.JPopupMenu.Separator();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jSeparator24 = new javax.swing.JPopupMenu.Separator();
        saledetailstext = new javax.swing.JMenu();
        jMenuItem28 = new javax.swing.JMenuItem();
        jSeparator27 = new javax.swing.JPopupMenu.Separator();
        jMenuItem18 = new javax.swing.JMenuItem();
        jSeparator59 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator43 = new javax.swing.JPopupMenu.Separator();
        jMenuItem19 = new javax.swing.JMenuItem();
        jSeparator61 = new javax.swing.JPopupMenu.Separator();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator28 = new javax.swing.JPopupMenu.Separator();
        jMenuItem13 = new javax.swing.JMenuItem();
        jSeparator25 = new javax.swing.JPopupMenu.Separator();
        salereturntext = new javax.swing.JMenuItem();
        jSeparator26 = new javax.swing.JPopupMenu.Separator();
        cashinvoicetext = new javax.swing.JMenuItem();
        jSeparator66 = new javax.swing.JPopupMenu.Separator();
        invoicecheck = new javax.swing.JMenuItem();
        jSeparator72 = new javax.swing.JPopupMenu.Separator();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        purchasepaymenttext = new javax.swing.JMenuItem();
        jSeparator62 = new javax.swing.JPopupMenu.Separator();
        creditpaymenttext = new javax.swing.JMenuItem();
        jSeparator64 = new javax.swing.JPopupMenu.Separator();
        expencesstext = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        itemreporttext = new javax.swing.JMenuItem();
        jSeparator30 = new javax.swing.JPopupMenu.Separator();
        salereporttext = new javax.swing.JMenuItem();
        jSeparator31 = new javax.swing.JPopupMenu.Separator();
        cashboxreporttext = new javax.swing.JMenuItem();
        jSeparator32 = new javax.swing.JPopupMenu.Separator();
        banksatementtext = new javax.swing.JMenuItem();
        jSeparator33 = new javax.swing.JPopupMenu.Separator();
        grndtailstext = new javax.swing.JMenuItem();
        jSeparator34 = new javax.swing.JPopupMenu.Separator();
        purchereportetx = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        instancetext = new javax.swing.JMenuItem();
        jSeparator35 = new javax.swing.JPopupMenu.Separator();
        customerinfotext = new javax.swing.JMenuItem();
        jSeparator36 = new javax.swing.JPopupMenu.Separator();
        creditstatementtext = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        employeetext = new javax.swing.JMenuItem();
        jSeparator55 = new javax.swing.JPopupMenu.Separator();
        salarytext = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        companydocu = new javax.swing.JMenuItem();
        jSeparator37 = new javax.swing.JPopupMenu.Separator();
        supplierdocu = new javax.swing.JMenuItem();
        jSeparator38 = new javax.swing.JPopupMenu.Separator();
        customerdocu = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        databasebeckuptext = new javax.swing.JMenuItem();
        jSeparator40 = new javax.swing.JPopupMenu.Separator();
        databaserestoretext = new javax.swing.JMenuItem();
        jSeparator51 = new javax.swing.JPopupMenu.Separator();
        passwordchange = new javax.swing.JMenuItem();
        jSeparator29 = new javax.swing.JPopupMenu.Separator();
        datainfo = new javax.swing.JMenuItem();
        jMenu13 = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Deshboard");
        setExtendedState(6);

        Homedesktop.setBackground(new java.awt.Color(255, 255, 255));
        Homedesktop.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        Homedesktop.setAutoscrolls(true);
        Homedesktop.setEnabled(false);
        Homedesktop.setNextFocusableComponent(Homedesktop);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logo-sm.png"))); // NOI18N
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPanel2.setBackground(new java.awt.Color(0, 51, 51));

        jPanel3.setBackground(new java.awt.Color(0, 102, 102));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Cash Invoice");

        TotalInvoicecashtext.setEditable(false);
        TotalInvoicecashtext.setBackground(new java.awt.Color(0, 51, 51));
        TotalInvoicecashtext.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        TotalInvoicecashtext.setForeground(new java.awt.Color(255, 255, 255));
        TotalInvoicecashtext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalInvoicecashtext.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TotalInvoicecashtext, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TotalInvoicecashtext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(0, 102, 102));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Credit Invoice");

        TotalInvoicetext.setEditable(false);
        TotalInvoicetext.setBackground(new java.awt.Color(0, 51, 51));
        TotalInvoicetext.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        TotalInvoicetext.setForeground(new java.awt.Color(255, 255, 255));
        TotalInvoicetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalInvoicetext.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TotalInvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TotalInvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(0, 102, 102));
        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Total Invoice");

        totalinvoicetext.setEditable(false);
        totalinvoicetext.setBackground(new java.awt.Color(0, 51, 51));
        totalinvoicetext.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        totalinvoicetext.setForeground(new java.awt.Color(255, 255, 255));
        totalinvoicetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalinvoicetext.setText("0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(totalinvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalinvoicetext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(0, 102, 102));
        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Purchase");

        jTextField4.setEditable(false);
        jTextField4.setBackground(new java.awt.Color(0, 51, 51));
        jTextField4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jTextField4.setForeground(new java.awt.Color(255, 255, 255));
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField4.setText("0");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(0, 102, 102));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Purchase Payment");

        purchasepaymentcash.setEditable(false);
        purchasepaymentcash.setBackground(new java.awt.Color(0, 51, 51));
        purchasepaymentcash.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        purchasepaymentcash.setForeground(new java.awt.Color(255, 255, 255));
        purchasepaymentcash.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        purchasepaymentcash.setText("0");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Cash");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Bank");

        purchasepaymentbank.setEditable(false);
        purchasepaymentbank.setBackground(new java.awt.Color(0, 51, 51));
        purchasepaymentbank.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        purchasepaymentbank.setForeground(new java.awt.Color(255, 255, 255));
        purchasepaymentbank.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        purchasepaymentbank.setText("0");
        purchasepaymentbank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchasepaymentbankActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Total Payment");

        totalpurchasepaymenttext.setEditable(false);
        totalpurchasepaymenttext.setBackground(new java.awt.Color(0, 51, 51));
        totalpurchasepaymenttext.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        totalpurchasepaymenttext.setForeground(new java.awt.Color(255, 255, 255));
        totalpurchasepaymenttext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalpurchasepaymenttext.setText("0");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(purchasepaymentcash, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addComponent(purchasepaymentbank, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(totalpurchasepaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(2, 2, 2))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(purchasepaymentcash, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(purchasepaymentbank, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalpurchasepaymenttext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jPanel10.setBackground(new java.awt.Color(0, 102, 102));
        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Cash Drawer");

        cashbalancetext.setEditable(false);
        cashbalancetext.setBackground(new java.awt.Color(0, 51, 51));
        cashbalancetext.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        cashbalancetext.setForeground(new java.awt.Color(255, 255, 255));
        cashbalancetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cashbalancetext.setText("0");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cashbalancetext)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel11.setBackground(new java.awt.Color(0, 102, 102));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Bank Balance");

        bankbalancetext.setEditable(false);
        bankbalancetext.setBackground(new java.awt.Color(0, 51, 51));
        bankbalancetext.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        bankbalancetext.setForeground(new java.awt.Color(255, 255, 255));
        bankbalancetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bankbalancetext.setText("0");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bankbalancetext)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bankbalancetext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel12.setBackground(new java.awt.Color(0, 102, 102));
        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Stock Amount");

        jTextField9.setEditable(false);
        jTextField9.setBackground(new java.awt.Color(0, 51, 51));
        jTextField9.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jTextField9.setForeground(new java.awt.Color(255, 255, 255));
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField9.setText("0");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField9)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(0, 102, 102));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Credit Recieved");

        creditcashrecievetext.setEditable(false);
        creditcashrecievetext.setBackground(new java.awt.Color(0, 51, 51));
        creditcashrecievetext.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        creditcashrecievetext.setForeground(new java.awt.Color(255, 255, 255));
        creditcashrecievetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        creditcashrecievetext.setText("0");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Cash");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Bank");

        creditbankrecievetext.setEditable(false);
        creditbankrecievetext.setBackground(new java.awt.Color(0, 51, 51));
        creditbankrecievetext.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        creditbankrecievetext.setForeground(new java.awt.Color(255, 255, 255));
        creditbankrecievetext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        creditbankrecievetext.setText("0");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Total Recieved");

        totalcredittext.setEditable(false);
        totalcredittext.setBackground(new java.awt.Color(0, 51, 51));
        totalcredittext.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        totalcredittext.setForeground(new java.awt.Color(255, 255, 255));
        totalcredittext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalcredittext.setText("0");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(creditcashrecievetext, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17)
                            .addComponent(creditbankrecievetext, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(totalcredittext, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(creditcashrecievetext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(creditbankrecievetext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalcredittext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jPanel13.setBackground(new java.awt.Color(0, 102, 102));
        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Expenses");

        TotalExpencesstext.setEditable(false);
        TotalExpencesstext.setBackground(new java.awt.Color(0, 51, 51));
        TotalExpencesstext.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        TotalExpencesstext.setForeground(new java.awt.Color(255, 255, 255));
        TotalExpencesstext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        TotalExpencesstext.setText("0");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TotalExpencesstext)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TotalExpencesstext, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton1.setText("Load");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        Homedesktop.setLayer(filler1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Homedesktop.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        Homedesktop.setLayer(jPanel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout HomedesktopLayout = new javax.swing.GroupLayout(Homedesktop);
        Homedesktop.setLayout(HomedesktopLayout);
        HomedesktopLayout.setHorizontalGroup(
            HomedesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomedesktopLayout.createSequentialGroup()
                .addGroup(HomedesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HomedesktopLayout.createSequentialGroup()
                        .addGap(247, 247, 247)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        HomedesktopLayout.setVerticalGroup(
            HomedesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HomedesktopLayout.createSequentialGroup()
                .addGroup(HomedesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HomedesktopLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(135, 135, 135)
                        .addComponent(filler1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(81, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        itemlistbtn.setBackground(new java.awt.Color(255, 255, 255));
        itemlistbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/product.png"))); // NOI18N
        itemlistbtn.setToolTipText("Item Master");
        itemlistbtn.setBorder(null);
        itemlistbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemlistbtnActionPerformed(evt);
            }
        });

        stockbtn.setBackground(new java.awt.Color(255, 255, 255));
        stockbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/stock.png"))); // NOI18N
        stockbtn.setToolTipText("Stock");
        stockbtn.setBorder(null);
        stockbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockbtnActionPerformed(evt);
            }
        });

        salepontbtn.setBackground(new java.awt.Color(255, 255, 255));
        salepontbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/salehistory.png"))); // NOI18N
        salepontbtn.setToolTipText("Sale Point");
        salepontbtn.setBorder(null);
        salepontbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salepontbtnActionPerformed(evt);
            }
        });

        cashboxbtn.setBackground(new java.awt.Color(255, 255, 255));
        cashboxbtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cashbox.png"))); // NOI18N
        cashboxbtn.setToolTipText("Cash Box");
        cashboxbtn.setBorder(null);
        cashboxbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashboxbtnActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/bank.png"))); // NOI18N
        jButton5.setToolTipText("Bank Master");
        jButton5.setBorder(null);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/if_purchase_45130.png"))); // NOI18N
        jButton6.setBorder(null);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(255, 255, 255));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/payment.png"))); // NOI18N
        jButton7.setToolTipText("Payment");
        jButton7.setBorder(null);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(255, 255, 255));
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/report.png"))); // NOI18N
        jButton10.setToolTipText("GRN Details");
        jButton10.setBorder(null);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 255, 255));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/customer.png"))); // NOI18N
        jButton8.setToolTipText("User Master");
        jButton8.setBorder(null);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(255, 255, 255));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/logout.png"))); // NOI18N
        jButton9.setToolTipText("Logout");
        jButton9.setBorder(null);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        time_text.setBackground(new java.awt.Color(204, 0, 0));
        time_text.setFont(new java.awt.Font("DS-Digital", 1, 18)); // NOI18N
        time_text.setForeground(new java.awt.Color(0, 51, 51));
        time_text.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        date_txt.setBackground(new java.awt.Color(204, 0, 0));
        date_txt.setFont(new java.awt.Font("DS-Digital", 1, 18)); // NOI18N
        date_txt.setForeground(new java.awt.Color(0, 51, 51));
        date_txt.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        itemlistbtn1.setBackground(new java.awt.Color(255, 255, 255));
        itemlistbtn1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/barcode.png"))); // NOI18N
        itemlistbtn1.setToolTipText("Item Master");
        itemlistbtn1.setBorder(null);
        itemlistbtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemlistbtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(salepontbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(itemlistbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(itemlistbtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(stockbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(cashboxbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(388, 388, 388)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(date_txt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(time_text, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(time_text, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(date_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(stockbtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(salepontbtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(cashboxbtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemlistbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(itemlistbtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        usernametext2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        usernametext2.setText("User Name:");
        jPanel6.add(usernametext2, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 73, 25));

        usernametext3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        usernametext3.setText("Name:");
        jPanel6.add(usernametext3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 39, 25));

        usertext1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel6.add(usertext1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, 230, 25));

        unametext1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel6.add(unametext1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 0, 180, 25));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("User Panel:");
        jPanel6.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, 25));

        hostnametext.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        hostnametext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel6.add(hostnametext, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 0, 580, 25));

        jmenunavigation.setBackground(new java.awt.Color(255, 255, 255));

        jMenu1.setText("File");

        userpanel.setBackground(new java.awt.Color(255, 255, 255));
        userpanel.setText("User Panel");
        userpanel.setBorder(null);
        userpanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userpanelActionPerformed(evt);
            }
        });
        jMenu1.add(userpanel);
        jMenu1.add(jSeparator1);

        passwordchange1.setBackground(new java.awt.Color(255, 255, 255));
        passwordchange1.setText("Password Change");
        passwordchange1.setBorder(null);
        passwordchange1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordchange1ActionPerformed(evt);
            }
        });
        jMenu1.add(passwordchange1);
        jMenu1.add(jSeparator2);

        logouttext.setBackground(new java.awt.Color(255, 255, 255));
        logouttext.setText("Log Out");
        logouttext.setBorder(null);
        logouttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logouttextActionPerformed(evt);
            }
        });
        jMenu1.add(logouttext);

        jmenunavigation.add(jMenu1);

        itemmenu.setText("Item Master");
        itemmenu.setPreferredSize(new java.awt.Dimension(71, 25));

        itemlist.setText("Item List");
        itemlist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemlistActionPerformed(evt);
            }
        });
        itemmenu.add(itemlist);
        itemmenu.add(jSeparator3);

        jMenuItem22.setText("New Item");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        itemmenu.add(jMenuItem22);
        itemmenu.add(jSeparator75);

        priceupdatetext.setText("Item Update");
        priceupdatetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                priceupdatetextActionPerformed(evt);
            }
        });
        itemmenu.add(priceupdatetext);
        itemmenu.add(jSeparator42);

        stocktext.setText("Stock");
        stocktext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stocktextActionPerformed(evt);
            }
        });
        itemmenu.add(stocktext);
        itemmenu.add(jSeparator4);

        wastagetext.setText("Wastage");
        wastagetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wastagetextActionPerformed(evt);
            }
        });
        itemmenu.add(wastagetext);
        itemmenu.add(jSeparator53);

        stockdemand.setText("Stock Demand");
        stockdemand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockdemandActionPerformed(evt);
            }
        });
        itemmenu.add(stockdemand);
        itemmenu.add(jSeparator5);

        itemforca.setText("Item Forcast");
        itemforca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemforcaActionPerformed(evt);
            }
        });
        itemmenu.add(itemforca);
        itemmenu.add(jSeparator57);

        jMenuItem5.setText("Opening Stock");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        itemmenu.add(jMenuItem5);
        itemmenu.add(jSeparator23);

        jMenuItem27.setText("Item Operation");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        itemmenu.add(jMenuItem27);
        itemmenu.add(jSeparator45);

        jMenuItem24.setText("Exp Date Check");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        itemmenu.add(jMenuItem24);

        jmenunavigation.add(itemmenu);

        jMenu3.setText("Supplier");

        supplytext.setText("Supplier List");
        supplytext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplytextActionPerformed(evt);
            }
        });
        jMenu3.add(supplytext);
        jMenu3.add(jSeparator6);

        suplytatement.setText("Supply Statement");
        suplytatement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suplytatementActionPerformed(evt);
            }
        });
        jMenu3.add(suplytatement);
        jMenu3.add(jSeparator47);

        suppliertarif.setText("Supplier Tarif");
        suppliertarif.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppliertarifActionPerformed(evt);
            }
        });
        jMenu3.add(suppliertarif);
        jMenu3.add(jSeparator46);

        supplierpurchase.setText("Supplyier Purchase List");
        supplierpurchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierpurchaseActionPerformed(evt);
            }
        });
        jMenu3.add(supplierpurchase);
        jMenu3.add(jSeparator48);

        supplierpayment.setText("Supplier Payment List");
        supplierpayment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierpaymentActionPerformed(evt);
            }
        });
        jMenu3.add(supplierpayment);
        jMenu3.add(jSeparator49);

        supplierforcast.setText("Supplier Forcast");
        jMenu3.add(supplierforcast);

        jmenunavigation.add(jMenu3);

        jMenu4.setText("Account");

        bankinfotext.setText("Bank Info");
        bankinfotext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bankinfotextActionPerformed(evt);
            }
        });
        jMenu4.add(bankinfotext);
        jMenu4.add(jSeparator7);

        bankacounttext.setText("Bank Acount");
        bankacounttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bankacounttextActionPerformed(evt);
            }
        });
        jMenu4.add(bankacounttext);
        jMenu4.add(jSeparator8);

        cashdrwertext.setText("Ladger Book");
        cashdrwertext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashdrwertextActionPerformed(evt);
            }
        });
        jMenu4.add(cashdrwertext);
        jMenu4.add(jSeparator9);

        jMenuItem12.setText("Mobile Banking");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem12);
        jMenu4.add(jSeparator71);

        vatcollectiontext.setText("Vat Collection");
        vatcollectiontext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vatcollectiontextActionPerformed(evt);
            }
        });
        jMenu4.add(vatcollectiontext);
        jMenu4.add(jSeparator10);

        dayclosetext.setText("Day close");
        dayclosetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dayclosetextActionPerformed(evt);
            }
        });
        jMenu4.add(dayclosetext);
        jMenu4.add(jSeparator11);

        incometext.setText("Income & Expenses");
        incometext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incometextActionPerformed(evt);
            }
        });
        jMenu4.add(incometext);
        jMenu4.add(jSeparator65);

        jMenuItem16.setText("All accounts");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem16);
        jMenu4.add(jSeparator21);

        jMenuItem21.setText("Monthly Closing");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem21);

        jmenunavigation.add(jMenu4);

        jMenu5.setText("Setup");

        itemcategory.setText("Item Category");
        itemcategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemcategoryActionPerformed(evt);
            }
        });
        jMenu5.add(itemcategory);
        jMenu5.add(jSeparator14);

        uniittext.setText("Unit Management");
        uniittext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uniittextActionPerformed(evt);
            }
        });
        jMenu5.add(uniittext);
        jMenu5.add(jSeparator15);

        jMenuItem14.setText("Brand");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem14);
        jMenu5.add(jSeparator73);

        jMenuItem26.setText("Dos Type");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem26);
        jMenu5.add(jSeparator13);

        expencesstypetext.setText("Expencess Type");
        expencesstypetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expencesstypetextActionPerformed(evt);
            }
        });
        jMenu5.add(expencesstypetext);
        jMenu5.add(jSeparator16);

        countrylisttext.setText("Country List");
        countrylisttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countrylisttextActionPerformed(evt);
            }
        });
        jMenu5.add(countrylisttext);
        jMenu5.add(jSeparator17);

        companyinfotext.setText("Company Info");
        companyinfotext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyinfotextActionPerformed(evt);
            }
        });
        jMenu5.add(companyinfotext);
        jMenu5.add(jSeparator39);

        jMenuItem3.setText("Number Format ");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem3);
        jMenu5.add(jSeparator12);

        jMenuItem11.setText("Report Design");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem11);
        jMenu5.add(jSeparator52);

        jMenuItem4.setText("Cash Payment Setup");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem4);
        jMenu5.add(jSeparator67);

        jMenuItem8.setText("Mobile Banking Type");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem8);
        jMenu5.add(jSeparator68);

        jMenuItem9.setText("Color Management");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem9);
        jMenu5.add(jSeparator69);

        jMenuItem10.setText("POS Setting");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem10);
        jMenu5.add(jSeparator44);

        jMenuItem7.setText("Item Size");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem7);
        jMenu5.add(jSeparator74);

        jMenuItem23.setText("Generic Setup");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem23);

        jmenunavigation.add(jMenu5);

        jMenu6.setText("Operation");

        purchasetext.setText("Purchase");
        purchasetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchasetextActionPerformed(evt);
            }
        });
        jMenu6.add(purchasetext);
        jMenu6.add(jSeparator18);

        purchasedetailstext.setText("Purchase Detalls");
        purchasedetailstext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchasedetailstextActionPerformed(evt);
            }
        });
        jMenu6.add(purchasedetailstext);
        jMenu6.add(jSeparator19);

        grntext.setText("GRN Details");
        grntext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grntextActionPerformed(evt);
            }
        });
        jMenu6.add(grntext);
        jMenu6.add(jSeparator20);

        purchasereturntext.setText("Purchase Return");
        purchasereturntext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchasereturntextActionPerformed(evt);
            }
        });
        jMenu6.add(purchasereturntext);
        jMenu6.add(jSeparator22);

        jMenuItem17.setText("Purchase Check");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem17);
        jMenu6.add(jSeparator50);

        jMenuItem25.setText("Item Update");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem25);

        jmenunavigation.add(jMenu6);

        jMenu7.setText("Sale Section");
        jMenu7.add(jSeparator24);

        saledetailstext.setText("Sale Details");

        jMenuItem28.setText("Cash Sale");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        saledetailstext.add(jMenuItem28);
        saledetailstext.add(jSeparator27);

        jMenuItem18.setText("Credit Invoice");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        saledetailstext.add(jMenuItem18);
        saledetailstext.add(jSeparator59);

        jMenuItem2.setText("Group By payment(Cash Sale)");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        saledetailstext.add(jMenuItem2);
        saledetailstext.add(jSeparator43);

        jMenuItem19.setText("Sale Profit");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        saledetailstext.add(jMenuItem19);
        saledetailstext.add(jSeparator61);

        jMenuItem6.setText("Booking");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        saledetailstext.add(jMenuItem6);
        saledetailstext.add(jSeparator28);

        jMenuItem13.setText("Void Sale");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        saledetailstext.add(jMenuItem13);

        jMenu7.add(saledetailstext);
        jMenu7.add(jSeparator25);

        salereturntext.setText("Sale Return Details");
        salereturntext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salereturntextActionPerformed(evt);
            }
        });
        jMenu7.add(salereturntext);
        jMenu7.add(jSeparator26);

        cashinvoicetext.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        cashinvoicetext.setText("Retail Sale");
        cashinvoicetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashinvoicetextActionPerformed(evt);
            }
        });
        jMenu7.add(cashinvoicetext);
        jMenu7.add(jSeparator66);

        invoicecheck.setText("Invoice Check");
        invoicecheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoicecheckActionPerformed(evt);
            }
        });
        jMenu7.add(invoicecheck);
        jMenu7.add(jSeparator72);

        jMenuItem15.setText("Total Sale");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem15);

        jmenunavigation.add(jMenu7);

        jMenu8.setText("Payment");

        purchasepaymenttext.setText("Purchase Payment");
        purchasepaymenttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchasepaymenttextActionPerformed(evt);
            }
        });
        jMenu8.add(purchasepaymenttext);
        jMenu8.add(jSeparator62);

        creditpaymenttext.setText("Payment Recieved");
        creditpaymenttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditpaymenttextActionPerformed(evt);
            }
        });
        jMenu8.add(creditpaymenttext);
        jMenu8.add(jSeparator64);

        expencesstext.setText("Expensess");
        expencesstext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expencesstextActionPerformed(evt);
            }
        });
        jMenu8.add(expencesstext);

        jmenunavigation.add(jMenu8);

        jMenu9.setText("Report");

        itemreporttext.setText("Item Report");
        itemreporttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemreporttextActionPerformed(evt);
            }
        });
        jMenu9.add(itemreporttext);
        jMenu9.add(jSeparator30);

        salereporttext.setText("Sale Report");
        salereporttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salereporttextActionPerformed(evt);
            }
        });
        jMenu9.add(salereporttext);
        jMenu9.add(jSeparator31);

        cashboxreporttext.setText("Cash Box Report");
        cashboxreporttext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashboxreporttextActionPerformed(evt);
            }
        });
        jMenu9.add(cashboxreporttext);
        jMenu9.add(jSeparator32);

        banksatementtext.setText("Bank Statement");
        banksatementtext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                banksatementtextActionPerformed(evt);
            }
        });
        jMenu9.add(banksatementtext);
        jMenu9.add(jSeparator33);

        grndtailstext.setText("GRN Details");
        grndtailstext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grndtailstextActionPerformed(evt);
            }
        });
        jMenu9.add(grndtailstext);
        jMenu9.add(jSeparator34);

        purchereportetx.setText("Purchase Report");
        purchereportetx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchereportetxActionPerformed(evt);
            }
        });
        jMenu9.add(purchereportetx);

        jmenunavigation.add(jMenu9);

        jMenu10.setText("Customer");

        instancetext.setText("instant  Credit Statement Check");
        instancetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instancetextActionPerformed(evt);
            }
        });
        jMenu10.add(instancetext);
        jMenu10.add(jSeparator35);

        customerinfotext.setText("Customer Info");
        customerinfotext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerinfotextActionPerformed(evt);
            }
        });
        jMenu10.add(customerinfotext);
        jMenu10.add(jSeparator36);

        creditstatementtext.setText("Credit Statement");
        creditstatementtext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditstatementtextActionPerformed(evt);
            }
        });
        jMenu10.add(creditstatementtext);

        jmenunavigation.add(jMenu10);

        jMenu14.setText("Employee");

        employeetext.setText("Employee List");
        employeetext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeetextActionPerformed(evt);
            }
        });
        jMenu14.add(employeetext);
        jMenu14.add(jSeparator55);

        salarytext.setText("Sallery Management");
        salarytext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salarytextActionPerformed(evt);
            }
        });
        jMenu14.add(salarytext);

        jmenunavigation.add(jMenu14);

        jMenu11.setText("Document");

        companydocu.setText("Company Document");
        companydocu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companydocuActionPerformed(evt);
            }
        });
        jMenu11.add(companydocu);
        jMenu11.add(jSeparator37);

        supplierdocu.setText("Suppliyer Document");
        supplierdocu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierdocuActionPerformed(evt);
            }
        });
        jMenu11.add(supplierdocu);
        jMenu11.add(jSeparator38);

        customerdocu.setText("Customer Document");
        customerdocu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerdocuActionPerformed(evt);
            }
        });
        jMenu11.add(customerdocu);

        jmenunavigation.add(jMenu11);

        jMenu12.setText("System");

        databasebeckuptext.setText("Database Backup");
        databasebeckuptext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                databasebeckuptextActionPerformed(evt);
            }
        });
        jMenu12.add(databasebeckuptext);
        jMenu12.add(jSeparator40);

        databaserestoretext.setText("Database Restore");
        databaserestoretext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                databaserestoretextActionPerformed(evt);
            }
        });
        jMenu12.add(databaserestoretext);
        jMenu12.add(jSeparator51);

        passwordchange.setText("System");
        passwordchange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordchangeActionPerformed(evt);
            }
        });
        jMenu12.add(passwordchange);
        jMenu12.add(jSeparator29);

        datainfo.setText("Database Information");
        datainfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datainfoActionPerformed(evt);
            }
        });
        jMenu12.add(datainfo);

        jmenunavigation.add(jMenu12);

        jMenu13.setText("Help");

        jMenuItem20.setText("License Key");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem20);

        jMenuItem1.setText("About");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem1);

        jmenunavigation.add(jMenu13);

        setJMenuBar(jmenunavigation);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Homedesktop)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Homedesktop)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void userpanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userpanelActionPerformed
        UserProcess Up = null;
        try {
            Up = new UserProcess();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(Up);
        Up.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Up.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = Up.getSize();
        Up.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        Up.moveToFront();
    }//GEN-LAST:event_userpanelActionPerformed

    private void itemlistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemlistActionPerformed
        ItemList ietmforlist = null;
        try {
            ietmforlist = new ItemList();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(ietmforlist);
        ietmforlist.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        ietmforlist.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = ietmforlist.getSize();
        ietmforlist.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        ietmforlist.moveToFront();
    }//GEN-LAST:event_itemlistActionPerformed

    private void stocktextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stocktextActionPerformed
        StockFram stock = null;

        try {
            stock = new StockFram();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(stock);
        stock.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        stock.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = stock.getSize();
        stock.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        stock.moveToFront();
    }//GEN-LAST:event_stocktextActionPerformed

    private void wastagetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wastagetextActionPerformed
        WastageStock wsstock = null;

        try {
            wsstock = new WastageStock();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(wsstock);
        wsstock.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        wsstock.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = wsstock.getSize();
        wsstock.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        wsstock.moveToFront();
    }//GEN-LAST:event_wastagetextActionPerformed

    private void supplytextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplytextActionPerformed
        SupplyerList Supinfo = null;
        try {
            Supinfo = new SupplyerList();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(Supinfo);
        Supinfo.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Supinfo.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = Supinfo.getSize();
        Supinfo.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        Supinfo.moveToFront();
    }//GEN-LAST:event_supplytextActionPerformed

    private void suplytatementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suplytatementActionPerformed
        SupplyeStatement Supstate = null;

        try {
            Supstate = new SupplyeStatement();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(Supstate);
        Supstate.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Supstate.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = Supstate.getSize();
        Supstate.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        Supstate.moveToFront();
    }//GEN-LAST:event_suplytatementActionPerformed

    private void bankinfotextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bankinfotextActionPerformed
        BankInfo bankinfo = null;

        try {
            bankinfo = new BankInfo();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(bankinfo);
        bankinfo.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = bankinfo.getSize();
        bankinfo.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        bankinfo.moveToFront();
    }//GEN-LAST:event_bankinfotextActionPerformed

    private void bankacounttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bankacounttextActionPerformed
        Bank bankacc = null;

        try {
            bankacc = new Bank();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(bankacc);
        bankacc.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        bankacc.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = bankacc.getSize();

        bankacc.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        bankacc.moveToFront();
    }//GEN-LAST:event_bankacounttextActionPerformed

    private void itemlistbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemlistbtnActionPerformed
        ItemList ietmforlist = null;
        try {
            ietmforlist = new ItemList();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(ietmforlist);
        ietmforlist.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = ietmforlist.getSize();
        ietmforlist.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        ietmforlist.moveToFront();
    }//GEN-LAST:event_itemlistbtnActionPerformed

    private void cashdrwertextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashdrwertextActionPerformed
        CashBox cashbox = null;

        try {
            cashbox = new CashBox();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(cashbox);
        cashbox.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        cashbox.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = cashbox.getSize();
        cashbox.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        cashbox.moveToFront();
    }//GEN-LAST:event_cashdrwertextActionPerformed

    private void vatcollectiontextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vatcollectiontextActionPerformed
        VatCollection vatcollection = null;

        try {
            vatcollection = new VatCollection();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(vatcollection);
        vatcollection.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        vatcollection.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = vatcollection.getSize();
        vatcollection.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        vatcollection.moveToFront();
    }//GEN-LAST:event_vatcollectiontextActionPerformed

    private void dayclosetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dayclosetextActionPerformed

        Daycloses itemcat = null;

        try {
            itemcat = new Daycloses();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(itemcat);
        itemcat.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = itemcat.getSize();
        itemcat.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        itemcat.moveToFront();


    }//GEN-LAST:event_dayclosetextActionPerformed

    private void purchasetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchasetextActionPerformed

        Purchase purchase = null;
        try {
            purchase = new Purchase();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(purchase);
        purchase.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        purchase.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = purchase.getSize();
        purchase.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        purchase.moveToFront();

        /*

         PurchaseEntry pointofsale = null;

         try {
         pointofsale = new PurchaseEntry();
         } catch (SQLException | IOException ex) {
         Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
         }
         pointofsale.setVisible(true);
         */

    }//GEN-LAST:event_purchasetextActionPerformed

    private void purchasedetailstextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchasedetailstextActionPerformed

        PurchaseDetails purchaedetails = null;
        try {
            purchaedetails = new PurchaseDetails();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(purchaedetails);
        purchaedetails.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        purchaedetails.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = purchaedetails.getSize();
        purchaedetails.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        purchaedetails.moveToFront();

    }//GEN-LAST:event_purchasedetailstextActionPerformed

    private void grntextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grntextActionPerformed

        GRNframe grnframe = null;
        try {
            grnframe = new GRNframe();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(grnframe);
        grnframe.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        grnframe.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = grnframe.getSize();
        grnframe.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        grnframe.moveToFront();

    }//GEN-LAST:event_grntextActionPerformed

    private void purchasereturntextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchasereturntextActionPerformed
        PurchaseReturn prchaseretrn = null;

        try {
            prchaseretrn = new PurchaseReturn();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(prchaseretrn);
        prchaseretrn.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        prchaseretrn.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = prchaseretrn.getSize();
        prchaseretrn.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        prchaseretrn.moveToFront();
    }//GEN-LAST:event_purchasereturntextActionPerformed

    private void salereturntextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salereturntextActionPerformed

        SaleReturn invoicereturn = null;

        try {
            invoicereturn = new SaleReturn();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(invoicereturn);
        invoicereturn.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        invoicereturn.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = invoicereturn.getSize();
        invoicereturn.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        invoicereturn.moveToFront();

    }//GEN-LAST:event_salereturntextActionPerformed

    private void expencesstextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expencesstextActionPerformed
        Expences expencess = null;

        try {
            expencess = new Expences();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(expencess);
        expencess.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        expencess.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = expencess.getSize();
        expencess.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        expencess.moveToFront();
    }//GEN-LAST:event_expencesstextActionPerformed

    private void itemreporttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemreporttextActionPerformed
        ItemReportFrame itemreport = null;

        try {
            itemreport = new ItemReportFrame();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(itemreport);
        itemreport.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();

        Dimension jInternalFrameSize = itemreport.getSize();
        itemreport.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        itemreport.moveToFront();
    }//GEN-LAST:event_itemreporttextActionPerformed

    private void salereporttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salereporttextActionPerformed
        Salereportframe salereport = null;

        try {
            salereport = new Salereportframe();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(salereport);
        salereport.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = salereport.getSize();
        salereport.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        salereport.moveToFront();
    }//GEN-LAST:event_salereporttextActionPerformed

    private void cashboxreporttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashboxreporttextActionPerformed
        CashBoxReport cashreport = null;

        try {
            cashreport = new CashBoxReport();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(cashreport);
        cashreport.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = cashreport.getSize();
        cashreport.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        cashreport.moveToFront();
    }//GEN-LAST:event_cashboxreporttextActionPerformed

    private void banksatementtextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_banksatementtextActionPerformed
        /*        BankstatementReport bankreport = null;

         try {
         bankreport = new BankstatementReport();
         } catch (IOException | SQLException ex) {
         Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
         }

         Homedesktop.add(bankreport);
         bankreport.setVisible(true);
        
         */
    }//GEN-LAST:event_banksatementtextActionPerformed

    private void grndtailstextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grndtailstextActionPerformed
        GRNReportframe grnreport = null;

        try {
            grnreport = new GRNReportframe();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(grnreport);
        grnreport.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = grnreport.getSize();
        grnreport.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        grnreport.moveToFront();
    }//GEN-LAST:event_grndtailstextActionPerformed

    private void purchereportetxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchereportetxActionPerformed
        PurchaseReport purchasereport = null;

        try {
            purchasereport = new PurchaseReport();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(purchasereport);
        purchasereport.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = purchasereport.getSize();
        purchasereport.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        purchasereport.moveToFront();
    }//GEN-LAST:event_purchereportetxActionPerformed

    private void instancetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instancetextActionPerformed
        creditistancecgeck creditcechk = null;

        try {
            creditcechk = new creditistancecgeck();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(creditcechk);
        creditcechk.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = creditcechk.getSize();
        creditcechk.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        creditcechk.moveToFront();
    }//GEN-LAST:event_instancetextActionPerformed

    private void customerinfotextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerinfotextActionPerformed
        Customerlist customerlist = null;

        try {
            customerlist = new Customerlist();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(customerlist);
        customerlist.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        // customerlist.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = customerlist.getSize();
        customerlist.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        customerlist.moveToFront();
    }//GEN-LAST:event_customerinfotextActionPerformed

    private void creditstatementtextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditstatementtextActionPerformed

        CustomerCreditStatemeny credstatement = null;

        try {
            credstatement = new CustomerCreditStatemeny();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (credstatement.isVisible()) {

            credstatement.setVisible(false);
        } else {
            Homedesktop.add(credstatement);

            credstatement.setVisible(true);
        }

        Dimension desktopSize = Homedesktop.getSize();
        credstatement.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = credstatement.getSize();
        credstatement.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        credstatement.moveToFront();

    }//GEN-LAST:event_creditstatementtextActionPerformed

    private void companydocuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companydocuActionPerformed
        Companydocument companydocument = null;

        try {
            companydocument = new Companydocument();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(companydocument);
        companydocument.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        companydocument.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = companydocument.getSize();
        companydocument.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        companydocument.moveToFront();

    }//GEN-LAST:event_companydocuActionPerformed

    private void stockbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockbtnActionPerformed
        StockFram stokf = null;

        try {
            stokf = new StockFram();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(stokf);
        stokf.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        stokf.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = stokf.getSize();
        stokf.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        stokf.moveToFront();
    }//GEN-LAST:event_stockbtnActionPerformed

    private void salepontbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salepontbtnActionPerformed
        InvoicePharmacy pointofsale = null;
        try {
            pointofsale = new InvoicePharmacy();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        pointofsale.setVisible(true);
    }//GEN-LAST:event_salepontbtnActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        Bank ban = null;

        try {
            ban = new Bank();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(ban);
        ban.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        ban.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = ban.getSize();
        ban.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        ban.moveToFront();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        NewPurchase purchase = null;
        try {
            purchase = new NewPurchase();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(purchase);
        purchase.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        purchase.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = purchase.getSize();
        purchase.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        purchase.moveToFront();


    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        Payment pay = null;

        try {
            pay = new Payment();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(pay);
        pay.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        pay.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = pay.getSize();
        pay.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        pay.moveToFront();

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        GRNframe gr = null;

        try {
            gr = new GRNframe();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(gr);
        gr.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        gr.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = gr.getSize();
        gr.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        gr.moveToFront();
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        UserProcess upr = null;
        try {
            upr = new UserProcess();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(upr);
        upr.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        upr.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = upr.getSize();
        upr.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        upr.moveToFront();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
//       componantDeactivenavigation();
        //     componantdeactivepanel();

        LoginFrame loginframe = null;

        try {
            loginframe = new LoginFrame();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        loginframe.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void logouttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logouttextActionPerformed
        LoginFrame loginframe = null;

        try {
            loginframe = new LoginFrame();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        loginframe.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_logouttextActionPerformed

    private void passwordchange1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordchange1ActionPerformed
        ChangePassword passordchange = null;

        try {
            passordchange = new ChangePassword(id);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(passordchange);
        passordchange.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = passordchange.getSize();
        passordchange.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        passordchange.moveToFront();
    }//GEN-LAST:event_passwordchange1ActionPerformed

    private void supplierdocuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierdocuActionPerformed
        SuppliyerDocument supplyerdocument = null;

        try {
            supplyerdocument = new SuppliyerDocument();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(supplyerdocument);
        supplyerdocument.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        supplyerdocument.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = supplyerdocument.getSize();
        supplyerdocument.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        supplyerdocument.moveToFront();

    }//GEN-LAST:event_supplierdocuActionPerformed

    private void customerdocuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerdocuActionPerformed
        CustomerDoucment customerdocument = null;

        try {
            customerdocument = new CustomerDoucment();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(customerdocument);
        customerdocument.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        customerdocument.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = customerdocument.getSize();
        customerdocument.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        customerdocument.moveToFront();
    }//GEN-LAST:event_customerdocuActionPerformed

    private void databasebeckuptextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_databasebeckuptextActionPerformed
        DatabaseBackup databasebackup = null;

        try {
            databasebackup = new DatabaseBackup();
        } catch (IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(databasebackup);
        databasebackup.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = databasebackup.getSize();
        databasebackup.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        databasebackup.moveToFront();
    }//GEN-LAST:event_databasebeckuptextActionPerformed

    private void databaserestoretextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_databaserestoretextActionPerformed
        DatabaseRestore databaserestore = null;

        try {
            databaserestore = new DatabaseRestore();
        } catch (IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(databaserestore);
        databaserestore.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = databaserestore.getSize();
        databaserestore.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        databaserestore.moveToFront();
    }//GEN-LAST:event_databaserestoretextActionPerformed

    private void priceupdatetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_priceupdatetextActionPerformed
        newMedicine newmedicine = null;

        try {
            newmedicine = new newMedicine();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(newmedicine);
        newmedicine.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = newmedicine.getSize();
        newmedicine.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        newmedicine.moveToFront();
    }//GEN-LAST:event_priceupdatetextActionPerformed

    private void cashboxbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashboxbtnActionPerformed
        CashBox cashb = null;

        try {
            cashb = new CashBox();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(cashb);
        cashb.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        cashb.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = cashb.getSize();
        cashb.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        cashb.moveToFront();
    }//GEN-LAST:event_cashboxbtnActionPerformed

    private void invoicecheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoicecheckActionPerformed
        InvoiceCheck invo = null;

        try {
            invo = new InvoiceCheck();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(invo);
        invo.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        invo.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = invo.getSize();
        invo.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        invo.moveToFront();
    }//GEN-LAST:event_invoicecheckActionPerformed

    private void incometextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incometextActionPerformed
        IncomeExpenss income = null;

        try {
            income = new IncomeExpenss();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(income);
        income.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        //income.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = income.getSize();
        income.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        income.moveToFront();
    }//GEN-LAST:event_incometextActionPerformed

    private void suppliertarifActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppliertarifActionPerformed
        SupplyerTarif supplyier = null;
        try {
            supplyier = new SupplyerTarif();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(supplyier);
        supplyier.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        supplyier.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = supplyier.getSize();
        supplyier.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        supplyier.moveToFront();
    }//GEN-LAST:event_suppliertarifActionPerformed

    private void supplierpurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierpurchaseActionPerformed
        SupplyerpurchaseList supplierpurchaselist = null;

        try {
            supplierpurchaselist = new SupplyerpurchaseList();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(supplierpurchaselist);
        supplierpurchaselist.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        supplierpurchaselist.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = supplierpurchaselist.getSize();
        supplierpurchaselist.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        supplierpurchaselist.moveToFront();
    }//GEN-LAST:event_supplierpurchaseActionPerformed

    private void supplierpaymentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierpaymentActionPerformed
        SupplierpaymentList supplierpaymentlist = null;

        try {
            supplierpaymentlist = new SupplierpaymentList();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(supplierpaymentlist);
        supplierpaymentlist.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        supplierpaymentlist.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = supplierpaymentlist.getSize();
        supplierpaymentlist.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        supplierpaymentlist.moveToFront();
    }//GEN-LAST:event_supplierpaymentActionPerformed

    private void itemforcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemforcaActionPerformed
        ItemForCast itemforcast = null;

        try {
            itemforcast = new ItemForCast();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(itemforcast);
        itemforcast.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        itemforcast.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = itemforcast.getSize();
        itemforcast.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        itemforcast.moveToFront();
    }//GEN-LAST:event_itemforcaActionPerformed

    private void stockdemandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockdemandActionPerformed
        StockDemand itemforcast = null;

        try {
            itemforcast = new StockDemand();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(itemforcast);
        itemforcast.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        itemforcast.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = itemforcast.getSize();
        itemforcast.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        itemforcast.moveToFront();
    }//GEN-LAST:event_stockdemandActionPerformed

    private void creditpaymenttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditpaymenttextActionPerformed
        CreditPaymentList creditpaymentlist = null;
        try {
            creditpaymentlist = new CreditPaymentList();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(creditpaymentlist);
        creditpaymentlist.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        creditpaymentlist.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = creditpaymentlist.getSize();
        creditpaymentlist.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        creditpaymentlist.moveToFront();
    }//GEN-LAST:event_creditpaymenttextActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        CreditSale creditsale = null;
        try {
            creditsale = new CreditSale();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(creditsale);
        creditsale.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        creditsale.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = creditsale.getSize();
        creditsale.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        creditsale.moveToFront();
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        ProfitSale profitsale = null;
        try {
            profitsale = new ProfitSale();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(profitsale);
        profitsale.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        profitsale.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = profitsale.getSize();
        profitsale.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        profitsale.moveToFront();
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void purchasepaymenttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchasepaymenttextActionPerformed
        Payment payment = null;

        try {
            payment = new Payment();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(payment);
        payment.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        payment.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = payment.getSize();
        payment.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        payment.moveToFront();
    }//GEN-LAST:event_purchasepaymenttextActionPerformed

    private void passwordchangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordchangeActionPerformed
        SystemOnPlatForm system = null;
        try {
            system = new SystemOnPlatForm();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(system);
        system.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = system.getSize();
        system.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        system.moveToFront();
    }//GEN-LAST:event_passwordchangeActionPerformed

    private void datainfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_datainfoActionPerformed

        Databaseinformation dinfo = null;

        try {
            dinfo = new Databaseinformation();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
        dinfo.setVisible(true);
    }//GEN-LAST:event_datainfoActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        LicienseFrame dinfo = null;

        try {
            dinfo = new LicienseFrame();
        } catch (IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        dinfo.setVisible(true);
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        About offerl = null;

        offerl = new About();

        Homedesktop.add(offerl);
        offerl.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = offerl.getSize();
        offerl.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        offerl.moveToFront();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        SalewithBank creditsale = null;
        try {
            creditsale = new SalewithBank();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(creditsale);
        creditsale.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        creditsale.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = creditsale.getSize();
        creditsale.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        creditsale.moveToFront();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void employeetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeetextActionPerformed
        EmplyeeList emplyeelist = null;

        try {
            emplyeelist = new EmplyeeList();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(emplyeelist);
        emplyeelist.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        emplyeelist.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = emplyeelist.getSize();
        emplyeelist.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        emplyeelist.moveToFront();
    }//GEN-LAST:event_employeetextActionPerformed

    private void salarytextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salarytextActionPerformed
        Sellery sallery = null;

        try {
            sallery = new Sellery();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(sallery);
        sallery.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        sallery.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = sallery.getSize();
        sallery.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        sallery.moveToFront();
    }//GEN-LAST:event_salarytextActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        OpeningStock manuelstock = null;
        try {
            manuelstock = new OpeningStock();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(manuelstock);
        manuelstock.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        manuelstock.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = manuelstock.getSize();
        manuelstock.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        manuelstock.moveToFront();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void cashinvoicetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashinvoicetextActionPerformed
        InvoicePharmacy pointofsale = null;

        try {
            pointofsale = new InvoicePharmacy();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        pointofsale.setVisible(true);
    }//GEN-LAST:event_cashinvoicetextActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        Booking booking = null;

        booking = new Booking();

        Homedesktop.add(booking);
        booking.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        booking.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = booking.getSize();
        booking.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        booking.moveToFront();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        MobileBanking cashbox = null;

        try {
            cashbox = new MobileBanking();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(cashbox);
        cashbox.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        cashbox.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = cashbox.getSize();
        cashbox.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        cashbox.moveToFront();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void itemlistbtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemlistbtn1ActionPerformed
        BarcodeMaker barmaker = null;

        try {
            //  barmaker = new BarcodeMaker();
            barmaker = new BarcodeMaker();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(barmaker);
        barmaker.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = barmaker.getSize();
        barmaker.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        barmaker.moveToFront();
    }//GEN-LAST:event_itemlistbtn1ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        VoidSale profitsale = null;
        try {
            profitsale = new VoidSale();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(profitsale);
        profitsale.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        profitsale.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = profitsale.getSize();
        profitsale.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        profitsale.moveToFront();
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        TotalSale itemcat = null;

        try {
            itemcat = new TotalSale();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(itemcat);
        itemcat.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = itemcat.getSize();
        itemcat.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        itemcat.moveToFront();
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        AllIn itemcat = null;

        try {
            itemcat = new AllIn();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(itemcat);
        itemcat.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = itemcat.getSize();
        itemcat.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        itemcat.moveToFront();
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        monthlyClose dayclose = null;
        try {
            dayclose = new monthlyClose();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(dayclose);
        dayclose.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        dayclose.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = dayclose.getSize();
        dayclose.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        dayclose.moveToFront();
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        newMedicine newmedicine = null;

        try {
            newmedicine = new newMedicine();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(newmedicine);
        newmedicine.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = newmedicine.getSize();
        newmedicine.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        newmedicine.moveToFront();
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        PurchaseView purchase = null;
        try {
            purchase = new PurchaseView();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(purchase);
        purchase.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        purchase.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = purchase.getSize();
        purchase.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        purchase.moveToFront();
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        ItemInvoicePurchaseHistory ietmforlist = null;
        try {
            ietmforlist = new ItemInvoicePurchaseHistory();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(ietmforlist);
        ietmforlist.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        ietmforlist.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = ietmforlist.getSize();
        ietmforlist.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        ietmforlist.moveToFront();
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        CashSale cashsale = null;
        try {
            cashsale = new CashSale();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(cashsale);
        cashsale.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        cashsale.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = cashsale.getSize();
        cashsale.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        cashsale.moveToFront();
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void purchasepaymentbankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchasepaymentbankActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchasepaymentbankActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        medicineGenric brand = null;
        try {
            brand = new medicineGenric();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(brand);
        brand.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = brand.getSize();
        brand.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        brand.moveToFront();
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        Size salesetting = null;
        try {
            salesetting = new Size();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(salesetting);
        salesetting.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = salesetting.getSize();
        salesetting.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        salesetting.moveToFront();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        Salesetting salesetting = null;
        try {
            salesetting = new Salesetting();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(salesetting);
        salesetting.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = salesetting.getSize();
        salesetting.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        salesetting.moveToFront();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        ColorManagement color = null;
        try {
            color = new ColorManagement();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(color);
        color.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = color.getSize();
        color.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        color.moveToFront();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        MobileBanktype mobilebank = null;
        try {
            mobilebank = new MobileBanktype();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(mobilebank);
        mobilebank.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = mobilebank.getSize();
        mobilebank.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        mobilebank.moveToFront();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        CashPaymentSetup cashpaymentsetup = null;

        try {
            cashpaymentsetup = new CashPaymentSetup();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(cashpaymentsetup);
        cashpaymentsetup.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = cashpaymentsetup.getSize();
        cashpaymentsetup.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        cashpaymentsetup.moveToFront();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        ReportDesign reportdesign = null;

        try {
            reportdesign = new ReportDesign();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(reportdesign);
        reportdesign.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = reportdesign.getSize();
        reportdesign.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        reportdesign.moveToFront();
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        StarNumber startnumber = null;
        try {
            startnumber = new StarNumber();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(startnumber);
        startnumber.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = startnumber.getSize();
        startnumber.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        startnumber.moveToFront();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void companyinfotextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyinfotextActionPerformed
        Company company = null;
        try {
            company = new Company();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(company);
        company.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = company.getSize();
        company.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        company.moveToFront();
    }//GEN-LAST:event_companyinfotextActionPerformed

    private void countrylisttextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countrylisttextActionPerformed
        Country countrylist = null;
        try {
            countrylist = new Country();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(countrylist);
        countrylist.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = countrylist.getSize();
        countrylist.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        countrylist.moveToFront();
    }//GEN-LAST:event_countrylisttextActionPerformed

    private void expencesstypetextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expencesstypetextActionPerformed
        ExpencessType expensetype = null;

        try {
            expensetype = new ExpencessType();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(expensetype);
        expensetype.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = expensetype.getSize();
        expensetype.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        expensetype.moveToFront();
    }//GEN-LAST:event_expencesstypetextActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        MedicineForms brand = null;
        try {
            brand = new MedicineForms();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(brand);
        brand.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = brand.getSize();
        brand.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        brand.moveToFront();
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        Brand brand = null;
        try {
            brand = new Brand();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(brand);
        brand.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = brand.getSize();
        brand.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        brand.moveToFront();
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void uniittextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uniittextActionPerformed
        UnitManagement Unitmanagement = null;

        try {
            Unitmanagement = new UnitManagement();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(Unitmanagement);
        Unitmanagement.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = Unitmanagement.getSize();
        Unitmanagement.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        Unitmanagement.moveToFront();
    }//GEN-LAST:event_uniittextActionPerformed

    private void itemcategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemcategoryActionPerformed
        ItemCategory itemcat = null;

        try {
            itemcat = new ItemCategory();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(itemcat);
        itemcat.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        Dimension jInternalFrameSize = itemcat.getSize();
        itemcat.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        itemcat.moveToFront();
    }//GEN-LAST:event_itemcategoryActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        Expdate ietmforlist = null;
        try {
            ietmforlist = new Expdate();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        Homedesktop.add(ietmforlist);
        ietmforlist.setVisible(true);

        Dimension desktopSize = Homedesktop.getSize();
        ietmforlist.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = ietmforlist.getSize();
        ietmforlist.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        ietmforlist.moveToFront();
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        ItemUpdate purchase = null;
        try {
            purchase = new ItemUpdate();
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        Homedesktop.add(purchase);
        purchase.setVisible(true);
        Dimension desktopSize = Homedesktop.getSize();
        purchase.setSize(desktopSize.width, desktopSize.height);
        Dimension jInternalFrameSize = purchase.getSize();
        purchase.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);
        purchase.moveToFront();
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       dayClose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane Homedesktop;
    private javax.swing.JTextField TotalExpencesstext;
    private javax.swing.JTextField TotalInvoicecashtext;
    private javax.swing.JTextField TotalInvoicetext;
    private javax.swing.JMenuItem bankacounttext;
    private javax.swing.JTextField bankbalancetext;
    private javax.swing.JMenuItem bankinfotext;
    private javax.swing.JMenuItem banksatementtext;
    private javax.swing.JTextField cashbalancetext;
    private javax.swing.JButton cashboxbtn;
    private javax.swing.JMenuItem cashboxreporttext;
    private javax.swing.JMenuItem cashdrwertext;
    private javax.swing.JMenuItem cashinvoicetext;
    private javax.swing.JMenuItem companydocu;
    private javax.swing.JMenuItem companyinfotext;
    private javax.swing.JMenuItem countrylisttext;
    private javax.swing.JTextField creditbankrecievetext;
    private javax.swing.JTextField creditcashrecievetext;
    private javax.swing.JMenuItem creditpaymenttext;
    private javax.swing.JMenuItem creditstatementtext;
    private javax.swing.JMenuItem customerdocu;
    private javax.swing.JMenuItem customerinfotext;
    private javax.swing.JMenuItem databasebeckuptext;
    private javax.swing.JMenuItem databaserestoretext;
    private javax.swing.JMenuItem datainfo;
    private javax.swing.JLabel date_txt;
    private javax.swing.JMenuItem dayclosetext;
    private javax.swing.JMenuItem employeetext;
    private javax.swing.JMenuItem expencesstext;
    private javax.swing.JMenuItem expencesstypetext;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JMenuItem grndtailstext;
    private javax.swing.JMenuItem grntext;
    private javax.swing.JLabel hostnametext;
    private javax.swing.JMenuItem incometext;
    private javax.swing.JMenuItem instancetext;
    private javax.swing.JMenuItem invoicecheck;
    private javax.swing.JMenuItem itemcategory;
    private javax.swing.JMenuItem itemforca;
    private javax.swing.JMenuItem itemlist;
    private javax.swing.JButton itemlistbtn;
    private javax.swing.JButton itemlistbtn1;
    private javax.swing.JMenu itemmenu;
    private javax.swing.JMenuItem itemreporttext;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator13;
    private javax.swing.JPopupMenu.Separator jSeparator14;
    private javax.swing.JPopupMenu.Separator jSeparator15;
    private javax.swing.JPopupMenu.Separator jSeparator16;
    private javax.swing.JPopupMenu.Separator jSeparator17;
    private javax.swing.JPopupMenu.Separator jSeparator18;
    private javax.swing.JPopupMenu.Separator jSeparator19;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator20;
    private javax.swing.JPopupMenu.Separator jSeparator21;
    private javax.swing.JPopupMenu.Separator jSeparator22;
    private javax.swing.JPopupMenu.Separator jSeparator23;
    private javax.swing.JPopupMenu.Separator jSeparator24;
    private javax.swing.JPopupMenu.Separator jSeparator25;
    private javax.swing.JPopupMenu.Separator jSeparator26;
    private javax.swing.JPopupMenu.Separator jSeparator27;
    private javax.swing.JPopupMenu.Separator jSeparator28;
    private javax.swing.JPopupMenu.Separator jSeparator29;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator30;
    private javax.swing.JPopupMenu.Separator jSeparator31;
    private javax.swing.JPopupMenu.Separator jSeparator32;
    private javax.swing.JPopupMenu.Separator jSeparator33;
    private javax.swing.JPopupMenu.Separator jSeparator34;
    private javax.swing.JPopupMenu.Separator jSeparator35;
    private javax.swing.JPopupMenu.Separator jSeparator36;
    private javax.swing.JPopupMenu.Separator jSeparator37;
    private javax.swing.JPopupMenu.Separator jSeparator38;
    private javax.swing.JPopupMenu.Separator jSeparator39;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator40;
    private javax.swing.JSeparator jSeparator41;
    private javax.swing.JPopupMenu.Separator jSeparator42;
    private javax.swing.JPopupMenu.Separator jSeparator43;
    private javax.swing.JPopupMenu.Separator jSeparator44;
    private javax.swing.JPopupMenu.Separator jSeparator45;
    private javax.swing.JPopupMenu.Separator jSeparator46;
    private javax.swing.JPopupMenu.Separator jSeparator47;
    private javax.swing.JPopupMenu.Separator jSeparator48;
    private javax.swing.JPopupMenu.Separator jSeparator49;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator50;
    private javax.swing.JPopupMenu.Separator jSeparator51;
    private javax.swing.JPopupMenu.Separator jSeparator52;
    private javax.swing.JPopupMenu.Separator jSeparator53;
    private javax.swing.JPopupMenu.Separator jSeparator55;
    private javax.swing.JPopupMenu.Separator jSeparator57;
    private javax.swing.JPopupMenu.Separator jSeparator59;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JSeparator jSeparator60;
    private javax.swing.JPopupMenu.Separator jSeparator61;
    private javax.swing.JPopupMenu.Separator jSeparator62;
    private javax.swing.JPopupMenu.Separator jSeparator64;
    private javax.swing.JPopupMenu.Separator jSeparator65;
    private javax.swing.JPopupMenu.Separator jSeparator66;
    private javax.swing.JPopupMenu.Separator jSeparator67;
    private javax.swing.JPopupMenu.Separator jSeparator68;
    private javax.swing.JPopupMenu.Separator jSeparator69;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JSeparator jSeparator70;
    private javax.swing.JPopupMenu.Separator jSeparator71;
    private javax.swing.JPopupMenu.Separator jSeparator72;
    private javax.swing.JPopupMenu.Separator jSeparator73;
    private javax.swing.JPopupMenu.Separator jSeparator74;
    private javax.swing.JPopupMenu.Separator jSeparator75;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JMenuBar jmenunavigation;
    private javax.swing.JMenuItem logouttext;
    private javax.swing.JMenuItem passwordchange;
    private javax.swing.JMenuItem passwordchange1;
    private javax.swing.JMenuItem priceupdatetext;
    private javax.swing.JMenuItem purchasedetailstext;
    private javax.swing.JTextField purchasepaymentbank;
    private javax.swing.JTextField purchasepaymentcash;
    private javax.swing.JMenuItem purchasepaymenttext;
    private javax.swing.JMenuItem purchasereturntext;
    private javax.swing.JMenuItem purchasetext;
    private javax.swing.JMenuItem purchereportetx;
    private javax.swing.JMenuItem salarytext;
    private javax.swing.JMenu saledetailstext;
    private javax.swing.JButton salepontbtn;
    private javax.swing.JMenuItem salereporttext;
    private javax.swing.JMenuItem salereturntext;
    private javax.swing.JButton stockbtn;
    private javax.swing.JMenuItem stockdemand;
    private javax.swing.JMenuItem stocktext;
    private javax.swing.JMenuItem suplytatement;
    private javax.swing.JMenuItem supplierdocu;
    private javax.swing.JMenuItem supplierforcast;
    private javax.swing.JMenuItem supplierpayment;
    private javax.swing.JMenuItem supplierpurchase;
    private javax.swing.JMenuItem suppliertarif;
    private javax.swing.JMenuItem supplytext;
    private javax.swing.JLabel time_text;
    private javax.swing.JTextField totalcredittext;
    private javax.swing.JTextField totalinvoicetext;
    private javax.swing.JTextField totalpurchasepaymenttext;
    private javax.swing.JLabel unametext1;
    private javax.swing.JMenuItem uniittext;
    public final javax.swing.JLabel usernametext2 = new javax.swing.JLabel();
    public final javax.swing.JLabel usernametext3 = new javax.swing.JLabel();
    private javax.swing.JMenuItem userpanel;
    private javax.swing.JLabel usertext1;
    private javax.swing.JMenuItem vatcollectiontext;
    private javax.swing.JMenuItem wastagetext;
    // End of variables declaration//GEN-END:variables

}
