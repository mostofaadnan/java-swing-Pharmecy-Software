/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author adnan
 */
public class Statement extends javax.swing.JInternalFrame {
  Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    java.sql.Date date;
    int month = 0;
    String year = null;
    String year1 = null;
    String description;
double Purchase=0;
double totalcredit=0;
double totalcash=0;
double totalinvoice = 0;
double purchasecashpayment=0;
double purchasebankpayment=0;
double totalpurchasepayment = 0;
double importtotal=0;
double export=0;
double totalimportpayment=0;
double importpaymentcash=0; 
double importpaymentbank=0;
   
double totalcashpayment = 0;
double totalbankpayment = 0;
double nettotalpayment = 0;


       
double expamount=0;
double saleryexp = 0;
      

double totlexp=0;


double creditcashrecieve=0;     
double creditbankrecieve = 0;  
double totalcreditrecieve = 0;

double exportcashrecievecash=0;
double exportbankrecieve=0;
double totalexporttpayment=0;

double nettotalcashrecieve=0;

double nettotalbankrecieve= 0;
double allnettotalrecieve=0;

double bankcashin=0;
double bankcashout=0; 
double drwarcashin=0;
double drwarcashout=0;

double nettotalcashin=0;
double nettotalcashout =0;

	
	
    /**
     * Creates new form Statement
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public Statement() throws IOException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        insertdetails();
    }
    private void currentDate() {
        java.util.Date now = new java.util.Date();
        date = new java.sql.Date(now.getTime());
        //  inputdate.setDate(date);

    }

    private void dayClose() {
        purchase();
        importtotal();
        export();
        creditinvoice();
        cashinvoice();
        totalinvoice();
        purchasepayment();
        totalpurchasePyment();
        importpayment();
        totalimportPyment();
        nettotalpayment();
        expensess();
        saleryexpensess();
        totalexp();
        creditrecive();
        totalcreditrecieve();
        exportrecieve();
        totalexportPyment();
        nettotalrecieve();
        bankcashincashout();
        drwercashincashout();
        totalcashincashout();
    //   bankbalance();
      //  cashbalance();

    }
private void dataTble(){
    int tree=0;
DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
model2.setRowCount(0);
dayClose();

/*

double totalinvoice = 0;
double purchasecashpayment=0;
double purchasebankpayment=0;
double totalpurchasepayment = 0;
double importtotal=0;
double export=0;
double totalimportpayment=0;
double importpaymentcash=0; 
double importpaymentbank=0;
   
double totalcashpayment = 0;
double totalbankpayment = 0;
double nettotalpayment = 0;


       
double expamount=0;
double saleryexp = 0;
      

double totlexp=0;


double creditcashrecieve=0;     
double creditbankrecieve = 0;  
double totalcreditrecieve = 0;

double exportcashrecievecash=0;
double exportbankrecieve=0;
double totalexporttpayment=0;

double nettotalcashrecieve=0;

double nettotalbankrecieve= 0;
double allnettotalrecieve=0;

double bankcashin=0;
double bankcashout=0; 
double drwarcashin=0;
double drwarcashout=0;

double nettotalcashin=0;
double nettotalcashout =0;

*/
 tree++;
 //model2.addRow(new Object[]{tree, Purchase});


}

private void insertdetails(){
int tree=0;
DefaultTableModel model2 = (DefaultTableModel) datatbl.getModel();
model2.setRowCount(0);
try{
String sql="Select p.pdate as 'pdate',p.nettotal as 'nettotal' cs.Totalinvoice as 'Totalinvoice' from purchase p,cashsale cs";
pst = conn.prepareStatement(sql);
rs = pst.executeQuery();
while(rs.next()){
String purchaseamt=rs.getString("nettotal");
String pdate=rs.getString("pdate");
String cashinvoiceamt=rs.getString("Totalinvoice");
 tree++;
 model2.addRow(new Object[]{tree,pdate,purchaseamt,cashinvoiceamt});



}

}catch(Exception e){



}


}
    private void purchase() {
        try {
            String sql = "select sum(nettotal)as 'totalpurchase' from purchase  where pdate GRNstatus=1  MONTH(pdate)='" + month + "' AND YEAR(pdate)='" + year + "' ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                Purchase = rs.getDouble("totalpurchase");
               }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void importtotal() {
        try {
            String sql = "select sum(Totalrate)as 'totalimport' from import Where pdate='" + date + "' OR MONTH(pdate)='" + month + "' AND YEAR(pdate)='" + year + "' OR YEAR(pdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
               importtotal = rs.getDouble("totalimport");
			
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void export() {
        try {
            String sql = "select sum(Totalrate)as 'totalexport' from export where pdate='" + date + "' OR MONTH(pdate)='" + month + "' AND YEAR(pdate)='" + year + "' OR YEAR(pdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                export = rs.getDouble("totalexport");


            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void creditinvoice() {
        try {
            String sql = "select sum(Totalinvoice)as 'totalcredit' from sale where invoicedate='" + date + "' OR MONTH(invoicedate)='" + month + "' AND YEAR(invoicedate)='" + year + "' OR YEAR(invoicedate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                 totalcredit = rs.getDouble("totalcredit");

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void cashinvoice() {
        try {
            String sql = "select sum(Totalinvoice)as 'totalcash' from cashsale where invoicedate='" + date + "'OR MONTH(invoicedate)='" + month + "' AND YEAR(invoicedate)='" + year + "' OR YEAR(invoicedate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
               totalcash = rs.getDouble("totalcash");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalinvoice() {
       
       
        totalinvoice = totalcredit + totalcash;


    }

    private void purchasepayment() {
        String cashtype = "Cash";
        try {
            String sql = "select sum(paidamount)as 'purchasecashpayment' from parchasepayment where paymenttype='" + cashtype + "' AND paymentdate='" + date + "'OR MONTH(paymentdate)='" + month + "' AND YEAR(paymentdate)='" + year + "' OR YEAR(paymentdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                purchasecashpayment = rs.getDouble("purchasecashpayment");
    

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
              purchasebankpayment = rs.getDouble("purchasebankpayment");
          
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalpurchasePyment() {
       

        totalpurchasepayment = purchasecashpayment + purchasebankpayment;
		
		

    }

    private void importpayment() {
        String cashtype = "Cash";
        String type = "Import";
        try {

            String sql = "select sum(paidamount)as 'importpaymentcash' from exportimportpaymentrecieve where paymenttype='" + cashtype + "' AND type='" + type + "' AND inputdate='" + date + "' OR MONTH(inputdate)='" + month + "' AND YEAR(inputdate)='" + year + "' OR YEAR(inputdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
              importpaymentcash = rs.getDouble("importpaymentcash");
	
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        String banktype = "Bank";
        try {

            String sql = "select sum(paidamount) as 'importpaymentbank' from exportimportpaymentrecieve where paymenttype='" + banktype + "' AND type='" + type + "' AND inputdate='" + date + "'OR MONTH(inputdate)='" + month + "' AND YEAR(inputdate)='" + year + "' OR YEAR(inputdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
          

		  if (rs.next()) {
                 importpaymentbank = rs.getDouble("importpaymentbank");
				
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalimportPyment() {
     
        totalimportpayment = importpaymentcash + importpaymentbank;
        

    }

    private void nettotalpayment() {
     
     totalcashpayment = purchasecashpayment + importpaymentcash;
     totalbankpayment = purchasebankpayment + importpaymentbank;
     nettotalpayment = totalcashpayment + totalbankpayment;
       }

    private void expensess() {
        try {
            String sql = "select sum(amount)as 'tamount' from expencess where inputdate='" + date + "' OR MONTH(inputdate)='" + month + "' AND YEAR(inputdate)='" + year + "' OR YEAR(inputdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
               expamount = rs.getDouble("tamount");

         
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
             saleryexp  = rs.getDouble("tamount");
             
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
  
   private void totalexp() {
    
        
        totlexp = saleryexp + expamount;
       


    }
    private void creditrecive() {
        String cashtype = "Cash";
        try {
            String sql = "select sum(paidamount)as 'creditcashrecieve' from invoicepaymenthistory where paymenttype='" + cashtype + "' AND paymentdate='" + date + "' OR MONTH(paymentdate)='" + month + "' AND YEAR(paymentdate)='" + year + "' OR YEAR(paymentdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
               creditcashrecieve = rs.getDouble("creditcashrecieve");
            	
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        String banktype = "Bank";
        try {
            String sql = "select sum(paidamount)as 'creditbankrecieve' from invoicepaymenthistory where paymenttype='" + banktype + "'AND paymentdate='" + date + "'OR MONTH(paymentdate)='" + month + "' AND YEAR(paymentdate)='" + year + "' OR YEAR(paymentdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
               creditbankrecieve = rs.getDouble("creditbankrecieve");
               
           	
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalcreditrecieve() {
       
		
		
        totalcreditrecieve = creditcashrecieve + creditbankrecieve ;
		

    }

    private void exportrecieve() {
        String cashtype = "Cash";
        String type = "Export";
        try {

            String sql = "select sum(paidamount)as 'exportrecievecash' from exportimportpaymentrecieve where paymenttype='" + cashtype + "' AND type='" + type + "' AND inputdate='" + date + "'OR MONTH(inputdate)='" + month + "' AND YEAR(inputdate)='" + year + "' OR YEAR(inputdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
               exportcashrecievecash = rs.getDouble("exportrecievecash");



            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        String banktype = "Bank";
        try {

            String sql = "select sum(paidamount)as 'exportpaymentbank' from exportimportpaymentrecieve where paymenttype='" + banktype + "' AND type='" + type + "' AND inputdate ='" + date + "'OR MONTH(inputdate)='" + month + "' AND YEAR(inputdate)='" + year + "' OR YEAR(inputdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
               exportbankrecieve = rs.getDouble("exportpaymentbank");
           

				
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalexportPyment() {
    
       
      
        totalexporttpayment = exportcashrecievecash + exportbankrecieve;
		

    }

    private void nettotalrecieve() {

        nettotalcashrecieve = creditcashrecieve + exportcashrecievecash;
		
        nettotalbankrecieve = creditbankrecieve  + exportbankrecieve;
		
	
         allnettotalrecieve = nettotalcashrecieve + nettotalbankrecieve;
        
		

    }

    private void bankcashincashout() {
        try {
            String sql = "select sum(cashin) as 'cashin',sum(cashout) as 'cashout' from bankoverall where inputdate='" + date + "' OR MONTH(inputdate)='" + month + "' AND YEAR(inputdate)='" + year + "' OR YEAR(inputdate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == true) {
               bankcashin = rs.getDouble("cashin");
               bankcashout = rs.getDouble("cashout");
            
				
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void drwercashincashout() {
        try {
            String sql = "select sum(cashin) as 'cashin',sum(cashout) as 'cashout' from cashdrower where upatedate='" + date + "' OR MONTH(upatedate)='" + month + "' AND YEAR(upatedate)='" + year + "' OR YEAR(upatedate)='" + year1 + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == true) {
               drwarcashin = rs.getDouble("cashin");
                     drwarcashout = rs.getDouble("cashout");

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void totalcashincashout() {
   

      nettotalcashin = drwarcashin + bankcashin;
	 nettotalcashout = drwarcashout + bankcashout;
       


    }
	
	/*
    private void cashbalance(){

try {
            String sql = "select sum(cashin) as 'cashin',sum(cashout) as 'cashout' from cashdrower ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == true) {
                double cashin = rs.getDouble("cashin");
                double cashout = rs.getDouble("cashout");
                double bankbalance=cashin-cashout;
                 String bankbalances = String.format("%.2f", bankbalance);
                 cashbalancetext.setText(bankbalances);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
}
private void bankbalance(){

try {
            String sql = "select sum(cashin) as 'cashin',sum(cashout) as 'cashout' from bankoverall ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == true) {
                double cashin = rs.getDouble("cashin");
                double cashout = rs.getDouble("cashout");
                double bankbalance=cashin-cashout;
                 String bankbalances = String.format("%.2f", bankbalance);
                 bankbalancetext.setText(bankbalances);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
}

*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        monthbox = new javax.swing.JComboBox<>();
        yeartext = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        overalsubmit = new javax.swing.JButton();
        jLabel59 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Statement");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1211, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        datatbl.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Sl No", "Date", "Cash Invoice", "Credit Invoice", "Total Invoice", "Purchase", "Purchase Payment", "Import Payment", "Total Payment", "Credit Recieve", "Import Recive", "Total Reicve", "Expences", "Salery Expenses", "Totlal Expenses", "Cash Drawer", "Bank", "Cash Balance", "Bank Balance"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
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
        jScrollPane1.setViewportView(datatbl);
        if (datatbl.getColumnModel().getColumnCount() > 0) {
            datatbl.getColumnModel().getColumn(0).setResizable(false);
            datatbl.getColumnModel().getColumn(1).setResizable(false);
            datatbl.getColumnModel().getColumn(2).setResizable(false);
            datatbl.getColumnModel().getColumn(3).setResizable(false);
            datatbl.getColumnModel().getColumn(4).setResizable(false);
            datatbl.getColumnModel().getColumn(5).setResizable(false);
            datatbl.getColumnModel().getColumn(6).setResizable(false);
            datatbl.getColumnModel().getColumn(7).setResizable(false);
            datatbl.getColumnModel().getColumn(8).setResizable(false);
            datatbl.getColumnModel().getColumn(9).setResizable(false);
            datatbl.getColumnModel().getColumn(10).setResizable(false);
            datatbl.getColumnModel().getColumn(11).setResizable(false);
            datatbl.getColumnModel().getColumn(12).setResizable(false);
            datatbl.getColumnModel().getColumn(13).setResizable(false);
            datatbl.getColumnModel().getColumn(14).setResizable(false);
            datatbl.getColumnModel().getColumn(15).setResizable(false);
            datatbl.getColumnModel().getColumn(16).setResizable(false);
            datatbl.getColumnModel().getColumn(17).setResizable(false);
            datatbl.getColumnModel().getColumn(18).setResizable(false);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 52, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void yeartextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_yeartextKeyPressed
       /* if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            yeartext.setBackground(Color.YELLOW);
            monthbox.setSelectedIndex(0);
            month = 0;
            year = null;
            ((JTextField) fromdatepayment1.getDateEditor().getUiComponent()).setText(null);
            date = null;
            year1 = yeartext.getText();
            dayClose();
            description = "Description:" + " Yearly Search: " + "Year: " + yeartext.getText();
         //   descriptiontext.setText(description);

        }
*/

    }//GEN-LAST:event_yeartextKeyPressed

    private void overalsubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_overalsubmitActionPerformed
/*
        if (monthbox.getSelectedIndex() == 0 || yeartext.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please Select Month AND Year");
        } else {
           // yeartext.setBackground(Color.WHITE);
            year1 = null;
            date = null;
            month = monthbox.getSelectedIndex();
            year = yeartext.getText();
            //dayClose();
            dataTble();
            description = "Description:" + "Monthly Search: " + "Month: " + monthbox.getSelectedItem() + " Year: " + yeartext.getText();
           // descriptiontext.setText(description);

        }

*/

insertdetails();
    }//GEN-LAST:event_overalsubmitActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable datatbl;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> monthbox;
    private javax.swing.JButton overalsubmit;
    private javax.swing.JTextField yeartext;
    // End of variables declaration//GEN-END:variables
}
