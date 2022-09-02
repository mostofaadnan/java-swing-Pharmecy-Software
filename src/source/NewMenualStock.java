/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import static mondrian.olap.fun.vba.Vba.date;

/**
 *
 * @author adnan
 */
public class NewMenualStock extends javax.swing.JInternalFrame {
  int tree = 0;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int unitid=0;
    int supid=0;
      //Dashboard dashboard = new Dashboard();
   // int userid = dashboard.id;
    int updatekey=0;
    int view=0;
    
   int updatedeletekey=0;
   int modifiaction=0;
   float minusAmount;
   int userid=0;
   //supplier
   
    double openingbalance;
    double consighnmentamnt;
    double Balancedue;
     String suplyer = null;
    /**
     * Creates new form NewMenualStock
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.sql.SQLException
     */
    public NewMenualStock() throws IOException, FileNotFoundException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        currentDate();
        parchase_code();
        unit();
        Item();
        updatedeletekey=0;
        deletebtn.setEnabled(false);
        
    }
     public NewMenualStock(String Table_Click ) throws IOException, FileNotFoundException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        userkey();
        currentDate();
        parchase_code();
        unit();
        Item();
        updatedeletekey=1;
        Dataview(Table_Click);
    }
     
      private void userkey() throws  IOException, SQLException {
        FileInputStream fis = new FileInputStream("src\\userkey.properties");
        Properties p = new Properties();
        p.load(fis);

        String userids = (String) p.get("userid");
       userid=Integer.parseInt(userids);
        // this.dispose();
        //LoginAccess desboard=new LoginAccess();
    }
       private void Dataview(String table_click) throws SQLException {
      //  int tree = 0;
        parchaseText.setText(table_click);
        try {

            String sql = "SELECT ita.itemcode as 'Code',ita.itemname as 'Item',(select name from category  cat where cat.id=ita.category) as 'Category',ita.tp as 'tradeprice',st.Qty as 'Stock',(select unitshort from unit un where un.id=ita.stockunit) as 'unit'  FROM item ita inner join stockdetails st ON ita.id=st.prcode where st.entryCode='" + table_click + "'";
            pst = conn.prepareStatement(sql);

            rs = pst.executeQuery();
            //  datatbl.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String procode = rs.getString("Code");
                String Itemname = rs.getString("Item");
                float tp = rs.getFloat("tradeprice");
                float qty = rs.getFloat("Stock");
                String unit = rs.getString("Unit");

                DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
                tree++;
                model2.addRow(new Object[]{tree, procode, Itemname,tp, qty, unit});

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        
        try {
            String sql = "Select inputdate,totalExp from manulystock ms where ms.entryCode='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
              
                Date pdate = rs.getDate("inputdate");
                parchasedate.setDate(pdate);
                String totalexp = rs.getString("totalExp");
                totalexptext.setText(totalexp);
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        // nettotaltradetext.setText(Float.toString(total_action_tp()) );
        ///nettotalsalepricetext.setText(Float.toString(total_action_mrp()));
    }
   
 private void currentDate(){
  java.util.Date now = new java.util.Date();
  java.sql.Date date = new java.sql.Date(now.getTime());
  parchasedate.setDate(date);
  
  }
     private void parchase_code() {
        try {
            int new_inv = 1;
            String NewParchaseCode = ("SE-" + new_inv + "");
            String sql = "Select id from manulystock";

            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.last()) {
                String add1 = rs.getString("id");
                //invoc_text.setText(add1);
                int inv = Integer.parseInt(add1);
                new_inv = (inv + 1);
                String ParchaseCode = Integer.toString(new_inv);
                NewParchaseCode = ("SE-" + ParchaseCode + "");
            }

            parchaseText.setText(NewParchaseCode);
        } catch (SQLException | NumberFormatException e) {
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
                int id = rs.getInt("id");
                unibox.addItem(name);
                //unibox.setSelectedIndex(id);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
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
          private void clear() {
        codetext.setText(null);
        itemnamesearch.setSelectedIndex(0);
        unitrateText.setText(null);
        qtytext.setText(null);
        unibox.setSelectedIndex(0);
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
       
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        tree++;

        float tpd = Float.parseFloat(unitrateText.getText());
        float qty = Float.parseFloat(qtytext.getText());
        
        model2.addRow(new Object[]{tree, codetext.getText(), itemnamesearch.getSelectedItem(), tpd, qty, unibox.getSelectedItem()});
       
  
       
        clear();
    }

    private void finelEntry() {
        

        if (updatekey == 0) {

            if (codetext.getText().isEmpty() || itemnamesearch.getSelectedIndex() == 0 || unitrateText.getText().isEmpty() || qtytext.getText().isEmpty() || unibox.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");

            } else {
                checkentry();

            }

        } else if (unitrateText.getText().isEmpty() || qtytext.getText().isEmpty()) {

        } else {

            DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
            int i = dataTable.getSelectedRow();
            if (i >= 0) {
               
                float tpd = Float.parseFloat(unitrateText.getText());
                float qty = Float.parseFloat(qtytext.getText());
                model.setValueAt(qty, i, 4);
                model.setValueAt(tpd, i, 3);
                clear();
                 updatekey=0;
                codetext.setText(null);
                itemnamesearch.setSelectedIndex(0);
            }

        }

    }
  
       private void manualStockInsert() throws IOException {

        try {

            String sql = "Insert into manulystock(entryCode,inputdate,totalExp,remark,inputuser) values (?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, parchaseText.getText());
            pst.setString(2, ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText());
            pst.setString(3, totalexptext.getText());
            pst.setString(4, remarktext.getText());
           
            pst.setInt(5, userid);
            pst.execute();
            parcheDetailsInsert();
            Stockupdateplus();
            JOptionPane.showMessageDialog(null, "Data Saved");
             DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
             model2.setRowCount(0);
             totalexptext.setText(null);
             
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void parcheDetailsInsert() throws SQLException, IOException {

        try {

            int rows = dataTable.getRowCount();

            // int rows1 = configtable.getRowCount();
            for (int row = 0; row < rows; row++) {
                // String coitemname = (String)sel_tbl.getValueAt(row, 0);
                String procode = (String) dataTable.getValueAt(row, 1);

                float rate = (Float) dataTable.getValueAt(row, 3);

                float qty = (Float) dataTable.getValueAt(row, 4);
               
                String unit = (String) dataTable.getValueAt(row, 5);
             
                try {

                    String sql = "Insert into stockdetails(entryCode,prcode,unitrate,qty,unit) values (?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);
                    pst.setString(1, parchaseText.getText());
                    pst.setString(2, procode);
                    pst.setFloat(3, rate);
                    pst.setFloat(4, qty);
                    pst.setString(5, unit);
                  
                    pst.execute();
                    ///    examperticipationinsert(id,examcode,exam);

                     // 
                } catch (SQLException | HeadlessException e) {
                   JOptionPane.showMessageDialog(null, e);

                }

            }
            //  loadGRN();
            //config
        } catch (NumberFormatException | HeadlessException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
         private void deleterow() {

        int[] toDelete = dataTable.getSelectedRows();
        Arrays.sort(toDelete); // be shure to have them in ascending order.
        DefaultTableModel myTableModel = (DefaultTableModel) dataTable.getModel();
        for (int ii = toDelete.length - 1; ii >= 0; ii--) {
            myTableModel.removeRow(toDelete[ii]); // beginning at the largest.
        }
        clear();
        updatekey=0;
    }
             private void Stockupdateplus() {
        // int cat=(Integer) categoryBox.getSelectedI
        
        for (int i = 0; i < dataTable.getRowCount(); i++) {
           // String cat = dataTable.getValueAt(i, 8).toString().trim();

            String s = dataTable.getValueAt(i, 1).toString().trim();
            float qty = (Float) dataTable.getValueAt(i, 4);

            Stockpdateplus(s, qty);

        }

    }

    private void Stockpdateplus(String s, float qtyint) {

        try {

            String sql = "Select Qty from Stock where procode=?";
            pst = conn.prepareStatement(sql);
            pst.setString(1, s);
            rs = pst.executeQuery();

            while (rs.next()) {

                float stock = rs.getFloat("Qty");

                float update_qty = stock + qtyint;

                String sql1 = "Update stock set qty='" + update_qty + "' where procode='" + s + "'";

                pst = conn.prepareStatement(sql1);

                pst.execute();

            }  //JOptionPane.showMessageDialog(null, add1);

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

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        qtytext = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        unitrateText = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        unibox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        totalexptext = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        parchasedate = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        parchaseText = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        remarktext = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        here = new javax.swing.JPanel();
        svbtn = new javax.swing.JButton();
        deletebtn = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Manual Stock Entry");

        jPanel2.setBackground(new java.awt.Color(67, 86, 86));

        jPanel3.setBackground(new java.awt.Color(67, 86, 86));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Product Code");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Unit ");

        codetext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        codetext.setForeground(new java.awt.Color(153, 0, 0));
        codetext.setBorder(null);
        codetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codetextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetextKeyReleased(evt);
            }
        });

        qtytext.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        qtytext.setForeground(new java.awt.Color(153, 0, 0));
        qtytext.setBorder(null);
        qtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytextKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Unit Rate");

        jButton1.setBackground(new java.awt.Color(255, 0, 0));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("ADD");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        unitrateText.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        unitrateText.setForeground(new java.awt.Color(102, 0, 0));
        unitrateText.setBorder(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Qty");

        jButton2.setBackground(new java.awt.Color(0, 153, 0));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Clear");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        unibox.setEditable(true);
        unibox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Item Name");

        itemnamesearch.setBackground(new java.awt.Color(255, 249, 248));
        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        itemnamesearch.setBorder(null);
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        itemnamesearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                itemnamesearchKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jLabel4)
                        .addComponent(jLabel11)))
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(unitrateText, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unibox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5))
        );

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Total Expencess");

        totalexptext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Date");

        parchasedate.setDateFormatString("yyyy-MM-dd ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Entry Code");

        parchaseText.setFont(new java.awt.Font("Tahoma", 2, 14)); // NOI18N
        parchaseText.setForeground(new java.awt.Color(255, 255, 255));
        parchaseText.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Remark");

        remarktext.setColumns(20);
        remarktext.setRows(5);
        remarktext.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jScrollPane2.setViewportView(remarktext);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(3, 3, 3)
                        .addComponent(parchaseText, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addGap(8, 8, 8)
                        .addComponent(parchasedate, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(totalexptext)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(totalexptext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(parchasedate, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(parchaseText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dataTable.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        dataTable.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Item Code", "Description", "Unit Rate", "Qty", "Unit"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
        dataTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dataTableKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(dataTable);
        if (dataTable.getColumnModel().getColumnCount() > 0) {
            dataTable.getColumnModel().getColumn(0).setResizable(false);
            dataTable.getColumnModel().getColumn(1).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setResizable(false);
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(300);
            dataTable.getColumnModel().getColumn(3).setResizable(false);
            dataTable.getColumnModel().getColumn(4).setResizable(false);
        }

        svbtn.setBackground(new java.awt.Color(0, 102, 51));
        svbtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        svbtn.setForeground(new java.awt.Color(255, 255, 255));
        svbtn.setText("Execute");
        svbtn.setBorder(null);
        svbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                svbtnActionPerformed(evt);
            }
        });

        deletebtn.setBackground(new java.awt.Color(153, 0, 0));
        deletebtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        deletebtn.setForeground(new java.awt.Color(255, 255, 255));
        deletebtn.setText("Delete");
        deletebtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deletebtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout hereLayout = new javax.swing.GroupLayout(here);
        here.setLayout(hereLayout);
        hereLayout.setHorizontalGroup(
            hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hereLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(svbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deletebtn, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        hereLayout.setVerticalGroup(
            hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hereLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(hereLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(svbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deletebtn, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(here, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(here, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void codetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();
            qtytext.setBackground(Color.YELLOW);

        }
    }//GEN-LAST:event_codetextKeyPressed

    private void codetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyReleased

        String SearchText = (String) codetext.getText();
        try {

            String sql = "Select id,ita.itemName as 'itemn',ita.mrp as 'mrp',ita.tp as 'tprice',stockunit,(select un.unitshort from unit un where un.id=ita.stockunit) as 'unitshort', (select st.Qty from stock st where st.procode=ita.id) as 'stock' from item ita where ita.itemcode='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            if (rs.next()) {

                String name = rs.getString("itemn");
                itemnamesearch.setSelectedItem(name);
                String unitrate = rs.getString("tprice");
                unitrateText.setText(unitrate);
                String stock = rs.getString("stock");
              //  lottext.setText(stock);
                String unit=rs.getString("unitshort");
                unibox.setSelectedItem(unit);
                // supid=rs.getInt("supliyer");
                float tp = rs.getFloat("tprice");
                float mrp = rs.getFloat("mrp");

                float profite=(mrp-tp/100*tp);
                String pro=String.format("%.2f", profite);
                //proftetext.setText(pro +"%");

            }else{

                clear();

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_codetextKeyReleased

    private void qtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        }else{

            finelEntry();
            codetext.requestFocusInWindow();
            qtytext.setBackground(Color.WHITE);

        }
    }//GEN-LAST:event_qtytextKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        finelEntry();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clear();
        updatekey=0;
        codetext.enable(true);
        itemnamesearch.enable(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
        String SearchText = (String) itemnamesearch.getSelectedItem();
        try {

            String sql = "Select ita.Itemcode as 'itaid',itemName,(select un.unitshort from unit un where un.id=ita.stockunit) as 'unitshort',ita.tp as 'tprice',mrp,(select st.Qty from stock st where st.procode=ita.id) as 'stock' from item ita where ita.itemName='" + SearchText + "'";
            pst = conn.prepareStatement(sql);
            //  pst.setString(1, SearchText);

            rs = pst.executeQuery();
            if (rs.next()) {

                String Id = rs.getString("itaid");
                codetext.setText(Id);
                String unitrate = rs.getString("tprice");
                unitrateText.setText(unitrate);

                String stock = rs.getString("stock");
              //  lottext.setText(stock);

                String unit = rs.getString("unitshort");
                unibox.setSelectedItem(unit);

                float tp = rs.getFloat("tprice");
                float mrp = rs.getFloat("mrp");

                float profite=(mrp-tp/100*tp);
                String pro=String.format("%.2f", profite);
              //  proftetext.setText(pro +"%");

                qtytext.requestFocusInWindow();
                qtytext.setBackground(Color.YELLOW);

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void itemnamesearchKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_itemnamesearchKeyTyped
      try {
          Item();
      } catch (SQLException ex) {
          Logger.getLogger(NewMenualStock.class.getName()).log(Level.SEVERE, null, ex);
      }
        
    }//GEN-LAST:event_itemnamesearchKeyTyped

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        int selectedRowIndex = dataTable.getSelectedRow();

        updatekey=1;
        codetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        unitrateText.setText(model.getValueAt(selectedRowIndex, 3).toString());
        qtytext.setText(model.getValueAt(selectedRowIndex, 4).toString());
        unibox.setSelectedItem(model.getValueAt(selectedRowIndex, 5).toString());
        qtytext.requestFocusInWindow();
        qtytext.setBackground(Color.YELLOW);

    }//GEN-LAST:event_dataTableMouseClicked

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
        deleterow();
    }//GEN-LAST:event_dataTableKeyPressed
 private void manualstockupate() {
        try {

           String date = ((JTextField) parchasedate.getDateEditor().getUiComponent()).getText();

            String sql = "Update manulystock set inputdate='" +date + "',totalExp='" + totalexptext.getText() + "' where entryCode='" + parchaseText.getText() + "'";
            pst = conn.prepareStatement(sql);

            pst.execute();
            //JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
 private void stockdetailsDelete(){
          try {
                String sql = "Delete from stockdetails where entryCode=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, parchaseText.getText());

                pst.execute();
                

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
 
 }
 private void StockloadMinus() {

        try {
            String sql1 = "Select st.Qty as 'stockqty' from stock st where st.procode=(Select pc.prcode from stockdetails pc where pc.prcode=st.procode AND pc.entryCode='" + parchaseText.getText() + "' )";
            pst = conn.prepareStatement(sql1);
            rs = pst.executeQuery();
            while (rs.next()) {
                Float updateqty = rs.getFloat("stockqty");
                String sql = "Update stock sta set sta.Qty=(Select pc.qty-'" + updateqty + "'  from stockdetails pc where pc.prcode=sta.procode AND pc.entryCode='" + parchaseText.getText() + "') where (select pc.prcode from stockdetails pc where pc.prcode=sta.procode AND pc.entryCode='" + parchaseText.getText() + "')=sta.procode";
                pst = conn.prepareStatement(sql);
                pst.execute();
            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void svbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_svbtnActionPerformed
if(updatedeletekey==0){
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
     //  model2.setRowCount(0);
       if(model2.getRowCount()==0 || totalexptext.getText().isEmpty()){
       JOptionPane.showMessageDialog(null, "Please Fillup Requirment Field");
       }else{
        try {
          manualStockInsert();
      } catch (IOException ex) {
          Logger.getLogger(NewMenualStock.class.getName()).log(Level.SEVERE, null, ex);
      }
        
       }}else{
      StockloadMinus();
      stockdetailsDelete();
      manualstockupate();
    
    try {
        parcheDetailsInsert();
    } catch (SQLException | IOException ex) {
        Logger.getLogger(NewMenualStock.class.getName()).log(Level.SEVERE, null, ex);
    }
      Stockupdateplus();
      
      this.dispose();

        }
    }//GEN-LAST:event_svbtnActionPerformed

    private void deletebtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deletebtnActionPerformed
       if(updatedeletekey==1)
        {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from manulystock where entryCode=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, parchaseText.getText());

                    pst.execute();

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }
         StockloadMinus();
        stockdetailsDelete();
            this.dispose();  
            }
        }
    }//GEN-LAST:event_deletebtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField codetext;
    private javax.swing.JTable dataTable;
    private javax.swing.JButton deletebtn;
    private javax.swing.JPanel here;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel parchaseText;
    private com.toedter.calendar.JDateChooser parchasedate;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextArea remarktext;
    private javax.swing.JButton svbtn;
    private javax.swing.JTextField totalexptext;
    private javax.swing.JComboBox<String> unibox;
    private javax.swing.JTextField unitrateText;
    // End of variables declaration//GEN-END:variables
}
