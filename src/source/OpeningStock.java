/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.jdesktop.swingx.prompt.PromptSupport;
import static source.StockFram.round;

/**
 *
 * @author FAHIM
 */
public class OpeningStock extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int updatekey = 0;
    int tree = 0;
    float previousStock = 0;
    String Unit;

    /**
     * Creates new form OpeningStock
     *
     * @throws java.io.IOException
     * @throws java.sql.SQLException
     */
    public OpeningStock() throws IOException, SQLException {
        initComponents();
        conn = Java_Connect.conectrDB();
        Placeholder();
        ExtractFilter();
        TableDesign();
        DataView();
    }

    private void Placeholder() {
        PromptSupport.setPrompt("Search", searchtext);
    }

    private void checkentry() {

        String s = "";
        boolean exists = false;
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            s = dataTable.getValueAt(i, 1).toString().trim();
            ///filter

            float qty = Float.parseFloat(dataTable.getValueAt(i, 5).toString());
            float applyqty = Float.parseFloat(qtytext.getText());
            float totalqty = qty + applyqty;
            if (codetext.getText().equals("")) {
                // JOptionPane.showMessageDialog(null, "Enter Invoice Details First");
            } else if (codetext.getText().equals(s)) {
                exists = true;
                dataTable.setValueAt(totalqty, i, 4);
                break;
            }
        }
        if (!exists) {
            entryData();
        } else {
            // JOptionPane.showMessageDialog(null, "This Data Already Exist.");
            clear();

        }
        codetext.requestFocusInWindow();
    }

    private void entryData() {
        float Totalqty = 0;
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        float applyqty = Float.parseFloat(qtytext.getText());
        Totalqty = applyqty + previousStock;
        tree++;
        model2.addRow(new Object[]{tree, codetext.getText(), itemnamesearch.getSelectedItem(), tptext.getText(), mrptext.getText(), Totalqty, Unit});
        clear();
    }

    private void checkUpdate() {

        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int i = dataTable.getSelectedRow();
        if (i >= 0) {
            float applyqty = Float.parseFloat(qtytext.getText());
            model.setValueAt(applyqty, i, 5);

        }
        updatekey = 0;
        clear();

    }

    private void deleterow() {

        int[] toDelete = dataTable.getSelectedRows();
        Arrays.sort(toDelete); // be shure to have them in ascending order.
        DefaultTableModel myTableModel = (DefaultTableModel) dataTable.getModel();
        for (int ii = toDelete.length - 1; ii >= 0; ii--) {
            myTableModel.removeRow(toDelete[ii]); // beginning at the largest.
        }

        clear();
    }

    private void clear() {
        codetext.setText(null);
        mrptext.setText(null);
        tptext.setText(null);
        qtytext.setText(null);
        presentqty.setText(null);
        final JTextField textfield = (JTextField) itemnamesearch.getEditor().getEditorComponent();
        textfield.setText(null);
        previousStock = 0;
        searchtext.setText(null);

    }

    private void ExtractFilter() {
        final JTextField textfield = (JTextField) itemnamesearch.getEditor().getEditorComponent();
        textfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                SwingUtilities.invokeLater(() -> {
                    if (textfield.getText().isEmpty()) {
                        itemnamesearch.removeAllItems();
                        codetext.setText(null);
                        mrptext.setText(null);
                        tptext.setText(null);
                        qtytext.setText(null);
                        previousStock = 0;

                    } else {
                        comboFilter(textfield.getText());
                    }
                });
            }

        });
    }

    public void comboFilter(String enteredText) {
        itemnamesearch.removeAllItems();
        //  itemnamesearch.addItem("");
        itemnamesearch.setSelectedItem(enteredText);
        ArrayList<String> filterArray = new ArrayList<>();
        String str1;
        try {

            String sql = "Select DISTINCT  itemName from item WHERE lower(itemName)  LIKE '%" + enteredText + "%' ORDER BY itemName ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                str1 = rs.getString("itemName");
                filterArray.add(str1);
                itemnamesearch.addItem(str1);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

        if (filterArray.size() > 0) {
            //  itemnamesearch.setModel(new DefaultComboBoxModel(filterArray.toArray()));

            itemnamesearch.setSelectedItem(enteredText);
            itemnamesearch.showPopup();

        } else {

            itemnamesearch.hidePopup();
            codetext.setText(null);
            mrptext.setText(null);
            tptext.setText(null);
            qtytext.setText(null);
            previousStock = 0;

        }
    }

    private void TableDesign() {
        JTableHeader jtblheader = dataTable.getTableHeader();
        jtblheader.setBackground(Color.red);
        jtblheader.setForeground(Color.DARK_GRAY);
        jtblheader.setFont(new Font("Segoe UI", Font.BOLD, 12));
        ((DefaultTableCellRenderer) jtblheader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        dataTable.setDefaultRenderer(Object.class, centerRenderer);

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);
        dataTable.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        dataTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        dataTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        dataTable.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        dataTable.getColumnModel().getColumn(6).setCellRenderer(leftRenderer);
        dataTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        dataTable.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
    }

    private void AfterExecution() {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
        previousStock = 0;

    }

    private void DataView() {
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);
        int tree = 1;
        double totaltradeprice;
        double totalsaleeprice;
        try {
            String sql = "SELECT ita.Itemcode as 'Code',"
                    + "ita.itemName as 'Item',"
                    + "(select name from category  cat where cat.id=ita.category) as 'Category',"
                    + "(select name from brand sup where sup.id=ita.brand_id) as 'brand',"
                    + "ita.tp as 'tradeprice',"
                    + "ita.mrp as 'saleprice',"
                    + "sum(st.Qty) as 'Stock',"
                    + "(select unitshort from unit un where un.id=ita.stockunit) as 'unit' "
                    + " FROM item ita inner join stockdetails st ON ita.itemcode=st.itemcode GROUP BY st.itemcode ORDER BY ita.id ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String procode = rs.getString("Code");
                String Itemname = rs.getString("Item");
                String category = rs.getString("category");
                String brand = rs.getString("brand");
                double tp = rs.getDouble("tradeprice");
                double mrp = rs.getDouble("saleprice");
                float qty = rs.getFloat("Stock");
                String unit = rs.getString("Unit");

                if (qty < 0) {

                    totaltradeprice = 0;
                    totalsaleeprice = 0;

                } else {

                    totaltradeprice = round((qty * tp), 2);
                    totalsaleeprice = round((qty * mrp), 2);

                }
                //   String totaltradeprices = df2.format(totaltradeprice);
                double profite = totalsaleeprice - totalsaleeprice;
                model2.addRow(new Object[]{tree, procode, Itemname, tp, mrp, qty, unit, totaltradeprice, totalsaleeprice});
                tree++;

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        nettotaltradetext.setText(Float.toString(total_action_tp()));
        nettotalsalepricetext.setText(Float.toString(total_action_mrp()));
        jLabel7.setText(Float.toString(total_action_qty()));

    }

    private void Search() {
        DefaultTableModel model2 = (DefaultTableModel) dataTable.getModel();
        model2.setRowCount(0);
        int tree = 1;
        double totaltradeprice;
        double totalsaleeprice;
        try {
            String sql = "SELECT ita.Itemcode as 'Code',"
                    + "ita.itemName as 'Item',"
                    + "(select name from category  cat where cat.id=ita.category) as 'Category',"
                    + "(select name from brand sup where sup.id=ita.brand_id) as 'brand',"
                    + "ita.tp as 'tradeprice',"
                    + "ita.mrp as 'saleprice',"
                    + "sum(st.Qty) as 'Stock',"
                    + "(select unitshort from unit un where un.id=ita.stockunit) as 'unit' "
                    + " FROM item ita inner join stockdetails st ON ita.itemcode=st.itemcode WHERE ita.itemName LIKE '%" + searchtext.getText() + "%' OR ita.Itemcode LIKE '%" + searchtext.getText() + "%'  GROUP BY st.itemcode ORDER BY ita.itemName ASC";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                String procode = rs.getString("Code");
                String Itemname = rs.getString("Item");
                String category = rs.getString("category");
                String brand = rs.getString("brand");
                double tp = rs.getDouble("tradeprice");
                double mrp = rs.getDouble("saleprice");
                float qty = rs.getFloat("Stock");
                String unit = rs.getString("Unit");

                if (qty < 0) {

                    totaltradeprice = 0;
                    totalsaleeprice = 0;

                } else {

                    totaltradeprice = round((qty * tp), 2);
                    totalsaleeprice = round((qty * mrp), 2);

                }
                //   String totaltradeprices = df2.format(totaltradeprice);
                double profite = totalsaleeprice - totalsaleeprice;
                model2.addRow(new Object[]{tree, procode, Itemname, tp, mrp, qty, unit, totaltradeprice, totalsaleeprice});
                tree++;

            }

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        nettotaltradetext.setText(Float.toString(total_action_tp()));
        nettotalsalepricetext.setText(Float.toString(total_action_mrp()));
        jLabel7.setText(Float.toString(total_action_qty()));
    }

    private float total_action_tp() {

        int rowaCount = dataTable.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());
            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 7).toString());
        }
        return (float) totaltpmrp;

    }

    private float total_action_mrp() {

        int rowaCount = dataTable.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            //totalstock=totalstock+Integer.parseInt(dataTable.getValueAt(i, 4).toString());
            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 8).toString());
        }
        return (float) totaltpmrp;

    }

    private float total_action_qty() {

        int rowaCount = dataTable.getRowCount();
        float totaltpmrp = 0;
        for (int i = 0; i < rowaCount; i++) {
            totaltpmrp = totaltpmrp + Float.parseFloat(dataTable.getValueAt(i, 5).toString());
        }
        return (float) totaltpmrp;

    }

    private void DataUpdateStock() {
        float applyqty = Float.parseFloat(qtytext.getText());
        float Totalqty = applyqty + previousStock;
        try {
            String sql = "Update stockdetails set Qty='" + Totalqty + "' where itemcode ='" + codetext.getText() + "'";
            pst = conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Stock Data Successfuly Update");
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        clear();
        DataView();
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
        jPanel21 = new javax.swing.JPanel();
        addbtn = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        mrptext = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        qtytext = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        itemnamesearch = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        codetext = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        tptext = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        presentqty = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        nettotaltradetext = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nettotalsalepricetext = new javax.swing.JLabel();
        searchtext = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Opening Stock");

        jPanel2.setBackground(new java.awt.Color(0, 51, 51));

        jPanel21.setBackground(new java.awt.Color(0, 51, 51));
        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        addbtn.setBackground(new java.awt.Color(255, 255, 255));
        addbtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addbtn.setText("Update");
        addbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addbtnjButton12ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(153, 0, 0));
        jButton16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setText("CLEAR");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(addbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        jPanel16.setBackground(new java.awt.Color(0, 51, 51));
        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("MRP");

        mrptext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        mrptext.setForeground(new java.awt.Color(153, 0, 0));
        mrptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        mrptext.setBorder(null);
        mrptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mrptextKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mrptext, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                    .addComponent(jLabel12))
                .addGap(3, 3, 3))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(mrptext, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel18.setBackground(new java.awt.Color(0, 51, 51));
        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Stock(QTY)");

        qtytext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        qtytext.setForeground(new java.awt.Color(153, 0, 0));
        qtytext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        qtytext.setBorder(null);
        qtytext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                qtytextMouseClicked(evt);
            }
        });
        qtytext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                qtytextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                qtytextqtytextKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(qtytext, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel14)
                .addGap(1, 1, 1)
                .addComponent(qtytext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel15.setBackground(new java.awt.Color(0, 51, 51));
        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Item Description");

        itemnamesearch.setEditable(true);
        itemnamesearch.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        itemnamesearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "" }));
        itemnamesearch.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                itemnamesearchPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        itemnamesearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemnamesearchMouseClicked(evt);
            }
        });
        itemnamesearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemnamesearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel10)
                .addGap(1, 1, 1)
                .addComponent(itemnamesearch, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel12.setBackground(new java.awt.Color(0, 51, 51));
        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Code");

        codetext.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        codetext.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        codetext.setBorder(null);
        codetext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codetextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codetextcodetextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codetextKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(codetext, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel11)
                .addGap(1, 1, 1)
                .addComponent(codetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel17.setBackground(new java.awt.Color(0, 51, 51));
        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("TP");

        tptext.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        tptext.setForeground(new java.awt.Color(153, 0, 0));
        tptext.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tptext.setBorder(null);
        tptext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tptextKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tptext, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                    .addComponent(jLabel13))
                .addGap(3, 3, 3))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(tptext, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanel19.setBackground(new java.awt.Color(0, 51, 51));
        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Prasent Stock");

        presentqty.setEditable(false);
        presentqty.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        presentqty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        presentqty.setBorder(null);
        presentqty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                presentqtyMouseClicked(evt);
            }
        });
        presentqty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                presentqtyKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                presentqtyqtytextKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(presentqty, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                .addGap(3, 3, 3))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jLabel15)
                .addGap(1, 1, 1)
                .addComponent(presentqty, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dataTable.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SL No", "Item Code", "Name", "TP", "MRP", "Qty", "Unit", "Total Tp", "Total MRP"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        dataTable.setRowHeight(25);
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
            dataTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        }

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total Stock");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Net Total(Trade Price):");

        nettotaltradetext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nettotaltradetext.setForeground(new java.awt.Color(255, 255, 255));
        nettotaltradetext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nettotaltradetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Net Total(Sale Price):");

        nettotalsalepricetext.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nettotalsalepricetext.setForeground(new java.awt.Color(255, 255, 255));
        nettotalsalepricetext.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nettotalsalepricetext.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        searchtext.setBackground(new java.awt.Color(0, 51, 51));
        searchtext.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        searchtext.setForeground(new java.awt.Color(255, 255, 255));
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
                .addComponent(searchtext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addGap(6, 6, 6)
                .addComponent(nettotaltradetext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel2)
                .addGap(6, 6, 6)
                .addComponent(nettotalsalepricetext, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(nettotaltradetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(nettotalsalepricetext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(7, 7, 7))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(searchtext))
                        .addContainerGap())))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addbtnjButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addbtnjButton12ActionPerformed
        if (codetext.getText().isEmpty() || mrptext.getText().isEmpty() || qtytext.getText().isEmpty() || qtytext.getText().matches("^[a-zA-Z]+$") || "0".equals(qtytext.getText())) {
            JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
        } else {
            DataUpdateStock();
        }
    }//GEN-LAST:event_addbtnjButton12ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        clear();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void mrptextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mrptextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();

        }
    }//GEN-LAST:event_mrptextKeyPressed

    private void qtytextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            if (codetext.getText().isEmpty() || mrptext.getText().isEmpty() || qtytext.getText().isEmpty() || qtytext.getText().matches("^[a-zA-Z]+$") || "0".equals(qtytext.getText())) {
                JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
            } else {
                if (codetext.getText().isEmpty() || mrptext.getText().isEmpty() || qtytext.getText().isEmpty() || qtytext.getText().matches("^[a-zA-Z]+$") || "0".equals(qtytext.getText())) {
                    JOptionPane.showMessageDialog(null, "Please Fillup Code box OR Item name Or qty Or Unit Fields");
                } else {
                    DataUpdateStock();
                }
            }

        }
    }//GEN-LAST:event_qtytextKeyPressed

    private void qtytextqtytextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_qtytextqtytextKeyReleased

    }//GEN-LAST:event_qtytextqtytextKeyReleased

    private void itemnamesearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemnamesearchMouseClicked

    }//GEN-LAST:event_itemnamesearchMouseClicked

    private void itemnamesearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemnamesearchActionPerformed

        /* if (itemnamesearch.getSelectedIndex() == 0) {
            codetext.setText(null);
            unitrateText.setText(null);
            discounttext.setText(null);
            qtytext.setText(null);

        } else {  */
        // }
    }//GEN-LAST:event_itemnamesearchActionPerformed

    private void codetextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyPressed

        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            qtytext.requestFocusInWindow();
            qtytext.setText(null);

        }
    }//GEN-LAST:event_codetextKeyPressed

    private void codetextcodetextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextcodetextKeyReleased

        if (codetext.getText().isEmpty() || codetext.getText().matches("^[a-zA-Z]+$")) {

            //itemnamesearch.setSelectedIndex(0);
            mrptext.setText(null);
            qtytext.setText(null);
            tptext.setText(null);
            codetext.requestFocusInWindow();
            //  colorbox.setSelectedIndex(0);
            //  sizetext.setText(null);

        } else {
            String Itemnameformat = codetext.getText();
            try {
                String sql = "SELECT "
                        + "ita.Itemcode as 'Code',"
                        + "ita.itemname as 'Item',"
                        + "(select name from category  cat where cat.id=ita.category) as 'Category',"
                        + "(select name from brand sup where sup.id=ita.brand_id) as 'brand',"
                        + "ita.tp as 'tp',"
                        + "ita.mrp as 'mrp',"
                        + "sum(st.Qty) as 'Stock',"
                        + "(select unitshort from unit un where un.id=ita.stockunit) as 'unit'  FROM item ita inner join stockdetails st ON ita.Itemcode=st.itemcode where ita.Itemcode='" + Itemnameformat + "'";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();
                if (rs.next() == true) {
                    String itemname = rs.getString("Item");
                    itemnamesearch.setSelectedItem(itemname);
                    String tp = rs.getString("tp");
                    tptext.setText(tp);
                    String mrp = rs.getString("mrp");
                    mrptext.setText(mrp);
                    String qty = rs.getString("Stock");
                    presentqty.setText(qty);
                    previousStock = rs.getFloat("Stock");
                    Unit = rs.getString("unit");
                    // qtytext.requestFocusInWindow();

                } else {
                    codetext.setText(null);
                    mrptext.setText(null);
                    qtytext.setText(null);
                    tptext.setText(null);
                    //  itemnamesearch.requestFocusInWindow();

                }
            } catch (SQLException ex) {

                JOptionPane.showMessageDialog(null, ex);
            }
        }
    }//GEN-LAST:event_codetextcodetextKeyReleased

    private void codetextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codetextKeyTyped

    }//GEN-LAST:event_codetextKeyTyped

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        int selectedRowIndex = dataTable.getSelectedRow();
        updatekey = 1;
        codetext.setText(model.getValueAt(selectedRowIndex, 1).toString());
        itemnamesearch.setSelectedItem(model.getValueAt(selectedRowIndex, 2).toString());
        tptext.setText(model.getValueAt(selectedRowIndex, 3).toString());
        mrptext.setText(model.getValueAt(selectedRowIndex, 4).toString());
        presentqty.setText(model.getValueAt(selectedRowIndex, 5).toString());
        Unit = model.getValueAt(selectedRowIndex, 6).toString();
        previousStock = Float.parseFloat(dataTable.getValueAt(selectedRowIndex, 5).toString());
    }//GEN-LAST:event_dataTableMouseClicked

    private void dataTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dataTableKeyPressed
        if (evt.getKeyCode() != KeyEvent.VK_ENTER) {

        } else {
            deleterow();

        }
    }//GEN-LAST:event_dataTableKeyPressed

    private void itemnamesearchPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_itemnamesearchPopupMenuWillBecomeInvisible
        String Itemnameformat = (String) itemnamesearch.getSelectedItem();
        try {
            String sql = "SELECT "
                    + "ita.Itemcode as 'Code',"
                    + "ita.itemname as 'Item',"
                    + "(select name from category  cat where cat.id=ita.category) as 'Category',"
                    + "(select name from brand sup where sup.id=ita.brand_id) as 'brand',"
                    + "ita.tp as 'tp',"
                    + "ita.mrp as 'mrp',"
                    + "sum(st.Qty) as 'Stock',"
                    + "(select unitshort from unit un where un.id=ita.stockunit) as 'unit'  FROM item ita inner join stockdetails st ON ita.Itemcode=st.itemcode where ita.itemName='" + Itemnameformat + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == true) {
                String Itemcode = rs.getString("Code");
                codetext.setText(Itemcode);
                String tp = rs.getString("tp");
                tptext.setText(tp);
                String mrp = rs.getString("mrp");
                mrptext.setText(mrp);
                String qty = rs.getString("Stock");
                presentqty.setText(qty);
                previousStock = rs.getFloat("Stock");
                Unit = rs.getString("unit");
                qtytext.requestFocusInWindow();

            } else {
                codetext.setText(null);
                mrptext.setText(null);
                qtytext.setText(null);
                tptext.setText(null);
                //  itemnamesearch.requestFocusInWindow();

            }
        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(null, ex);
        }
    }//GEN-LAST:event_itemnamesearchPopupMenuWillBecomeInvisible

    private void tptextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tptextKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tptextKeyPressed

    private void qtytextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_qtytextMouseClicked
        qtytext.setText(null);
    }//GEN-LAST:event_qtytextMouseClicked

    private void presentqtyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_presentqtyMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_presentqtyMouseClicked

    private void presentqtyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_presentqtyKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_presentqtyKeyPressed

    private void presentqtyqtytextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_presentqtyqtytextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_presentqtyqtytextKeyReleased

    private void searchtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchtextKeyReleased
        if (searchtext.getText().isEmpty()) {
            DataView();
        } else {
            Search();
        }
    }//GEN-LAST:event_searchtextKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addbtn;
    private javax.swing.JTextField codetext;
    private javax.swing.JTable dataTable;
    private javax.swing.JComboBox<String> itemnamesearch;
    private javax.swing.JButton jButton16;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField mrptext;
    private javax.swing.JLabel nettotalsalepricetext;
    private javax.swing.JLabel nettotaltradetext;
    private javax.swing.JTextField presentqty;
    private javax.swing.JTextField qtytext;
    private javax.swing.JTextField searchtext;
    private javax.swing.JTextField tptext;
    // End of variables declaration//GEN-END:variables
}
