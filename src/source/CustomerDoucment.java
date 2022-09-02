/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.proteanit.sql.DbUtils;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author adnan
 */
public class CustomerDoucment extends javax.swing.JInternalFrame {
 Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    
    int suppliyerid;
    /**
     * Creates new form CustomerDoucment
     * @throws java.sql.SQLException
     */
    public CustomerDoucment() throws SQLException , IOException{
        initComponents();
       AutoCompleteDecorator.decorate(supliyerbox);
       AutoCompleteDecorator.decorate(supliyerbox1);
        conn = Java_Connect.conectrDB();
        table_update();
        suppliyer();
    }
private void suppliyer() throws SQLException {

        try {
            String sql = "Select * from customerinfo";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String name = rs.getString("customername");
                int id = rs.getInt("id");
                supliyerbox.addItem(name);
                supliyerbox1.addItem(name);
                //  supliyerbox.setSelectedIndex(id);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void table_update() throws SQLException {

        try {
            String sql = "Select id as 'Code', doctitle,(select si.customername from customerinfo si where si.id=sd.customerid) as 'Customer Name',docpath as 'Document Path' ,doctype as 'Document Type' from customerdocument sd ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    
    
       private void customertblcheck() throws SQLException {

        try {
            String sql = "Select id as 'Code', doctitle,(select si.customername from customerinfo si where si.id=sd.customerid) as 'Customer Name',docpath as 'Document Path' ,doctype as 'Document Type' from customerdocument sd where sd.customerid='"+suppliyerid+"' ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }
    private void imageupload() {
        JFileChooser choser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg", "gif", "png");
        choser.addChoosableFileFilter(filter);
        int result = choser.showSaveDialog(null);
        // choser.showOpenDialog(null);
        File f = choser.getSelectedFile();
        filename = f.getAbsolutePath();
        file_path.setText(filename);

        try {
            File image = new File(filename);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readNum; (readNum = fis.read(buf)) != -1;) {

                bos.write(buf, 0, readNum);

            }
            person_image = bos.toByteArray();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = choser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            pics_lbl.setIcon(ResizeImage(path));
        } //if the user click on save in Jfilechooser
        else if (result == JFileChooser.CANCEL_OPTION) {
            //   System.out.println("No File Select");
        }

    }

    public ImageIcon ResizeImage(String ImagePath) {
        ImageIcon MyImage = new ImageIcon(ImagePath);
        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(pics_lbl.getWidth(), pics_lbl.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }

    private Image ScaleImage(byte[] img, int w, int h) throws IOException {

        BufferedImage resizeIamge = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizeIamge.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        ByteArrayInputStream in = new ByteArrayInputStream(img);
        BufferedImage bImageFromConvert = ImageIO.read(in);
        g2.drawImage(bImageFromConvert, 0, 0, w, h, null);
        g2.dispose();
        return resizeIamge;
    }

    private void insertdata() throws SQLException {

        if (doctypebox.getSelectedIndex() > 0) {
            if (doctitle.getText().isEmpty() || file_path.getText().isEmpty()) {

                JOptionPane.showMessageDialog(null, "Please Fillup Required Field");
            } else {

                try {

                    String sql = "Insert into customerdocument(doctitle,customerid,doctype,docpath,picture) values(?,?,?,?,?)";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, doctitle.getText());
                      pst.setInt(2,suppliyerid);

                    pst.setString(3, (String) doctypebox.getSelectedItem());
                    pst.setString(4, file_path.getText());

                    pst.setBytes(5, person_image);
                    pst.execute();

                    JOptionPane.showMessageDialog(null, "Data Saved");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }

        }
    }

    private void fileupload() {

        JFileChooser choser = new JFileChooser();
        choser.showOpenDialog(null);
        File f = choser.getSelectedFile();
        String filenamehere = f.getAbsolutePath();
        file_path.setText(filenamehere);

    }

    private void clear() {
        docidtext.setText(null);
        doctitle.setText(null);
        file_path.setText(null);
        doctypebox.setSelectedIndex(0);

    }

  /*  private void updateDocument() {

        try {

            String id = docidtext.getText();

            //  byte[] image = person_image;
            String sql = "Update customerdocument set doctitle='" + doctitle.getText() + "',doctype='" + doctypebox.getSelectedItem() + "',docpath='" + file_path.getText() + "',picture=? where id='" + id + "'";
            pst = conn.prepareStatement(sql);
            pst.setBytes(1, person_image);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data Update");

        } catch (SQLException | HeadlessException e) {
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
        pics_lbl = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        doctypebox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        file_path = new javax.swing.JTextField();
        docidtext = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        doctitle = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        supliyerbox = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        searchtext = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        supliyerbox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        datatbl = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Customer Document");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pics_lbl.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(pics_lbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(548, 36, 740, 484));

        jLabel6.setText("Preview");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 10, -1, -1));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Documant Title");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 54, -1, -1));

        doctypebox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Image", "Pdf", "Word file", "Print File", "Other" }));
        jPanel3.add(doctypebox, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 83, 205, 30));

        jButton1.setBackground(new java.awt.Color(51, 153, 0));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Attach");
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(347, 155, 165, 30));

        file_path.setEditable(false);
        file_path.setEnabled(false);
        jPanel3.add(file_path, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 119, 405, 30));

        docidtext.setEditable(false);
        docidtext.setEnabled(false);
        jPanel3.add(docidtext, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 11, 118, 30));

        jButton4.setBackground(new java.awt.Color(153, 0, 0));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Delete");
        jButton4.setBorder(null);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 196, 102, 30));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Documant Id");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 18, 93, -1));

        jLabel2.setText("Doc Type");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 85, -1, 26));

        jButton2.setBackground(new java.awt.Color(204, 51, 0));
        jButton2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Execute");
        jButton2.setBorder(null);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(259, 196, 83, 30));

        jButton3.setBackground(new java.awt.Color(0, 153, 153));
        jButton3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Clear");
        jButton3.setBorder(null);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(348, 196, 56, 30));

        jLabel3.setText("Path");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 121, 93, 26));
        jPanel3.add(doctitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 47, 405, 30));

        jLabel5.setText("Customer");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(229, 11, 79, 30));

        supliyerbox.setEditable(true);
        supliyerbox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        supliyerbox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                supliyerboxPopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        jPanel3.add(supliyerbox, new org.netbeans.lib.awtextra.AbsoluteConstraints(312, 11, 200, 30));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 520, 240));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setText("Search ");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 25));

        searchtext.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchtextKeyReleased(evt);
            }
        });
        jPanel2.add(searchtext, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 11, 211, 25));

        jLabel8.setText("Customer");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, -1, 30));

        supliyerbox1.setEditable(true);
        supliyerbox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select" }));
        supliyerbox1.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
                supliyerbox1PopupMenuWillBecomeInvisible(evt);
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
            }
        });
        jPanel2.add(supliyerbox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(322, 11, 190, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 254, 522, 50));

        datatbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        datatbl.setRowHeight(30);
        datatbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datatblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(datatbl);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 307, 522, 225));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );

        setBounds(10, 0, 1332, 570);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (doctypebox.getSelectedIndex() > 0) {
            if (doctypebox.getSelectedIndex() == 1) {

                imageupload();

            } else {

                fileupload();

            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (docidtext.getText().isEmpty()) {

        } else {

            int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
            if (p == 0) {
                try {
                    String sql = "Delete from customerdocument where id=?";
                    pst = conn.prepareStatement(sql);

                    pst.setString(1, docidtext.getText());

                    pst.execute();
                    JOptionPane.showMessageDialog(null, "Data Deleted");

                } catch (SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, e);

                }

            }
            clear();

            try {
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(SuppliyerDocument.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

      
          
                try {
                    insertdata();
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerDoucment.class.getName()).log(Level.SEVERE, null, ex);
                }

          

      

        try {
            clear();
            table_update();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDoucment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        clear();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void supliyerboxPopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supliyerboxPopupMenuWillBecomeInvisible

        String suplliyer=(String) supliyerbox.getSelectedItem();
        try {
            String sql = "Select * from customerinfo where  customername='"+suplliyer+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                suppliyerid = rs.getInt("id");

                //  supliyerbox.setSelectedIndex(id);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_supliyerboxPopupMenuWillBecomeInvisible

    private void datatblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datatblMouseClicked

        try {

            int row = datatbl.getSelectedRow();
            String table_click = (datatbl.getModel().getValueAt(row, 0).toString());
            String sql = "Select * from customerdocument where id='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {
                String Id = rs.getString("id");
                docidtext.setText(Id);
                String titlehere = rs.getString("doctitle");
                doctitle.setText(titlehere);
                String doctype = rs.getString("doctype");
                doctypebox.setSelectedItem(doctype);
                String path = rs.getString("docpath");
                file_path.setText(path);
                String type = rs.getString("doctype");
                if ("Image".equals(type)) {

                    byte[] ImageData = rs.getBytes("picture");
                    BufferedImage buff_image = ImageIO.read(new ByteArrayInputStream(ImageData));
                    //format = new ImageIcon(ImageData);
                    format = new ImageIcon(ScaleImage(ImageData, pics_lbl.getWidth(), pics_lbl.getHeight()));
                    pics_lbl.setIcon(format);
                }

            }
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(null, e);

        }
        try {

            int row = datatbl.getSelectedRow();
            String table_click = (datatbl.getModel().getValueAt(row, 3).toString());
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + table_click);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_datatblMouseClicked

    private void searchtextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchtextKeyReleased
 
        String doctitlehere=searchtext.getText();
        try {
            String sql = "Select id as 'Code', doctitle,(select si.customername from customerinfo si where si.id=sd.customerid) as 'Customer Name',docpath as 'Document Path' ,doctype as 'Document Type' from customerdocument sd where sd.doctitle='"+doctitlehere+"' ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            datatbl.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }       
    }//GEN-LAST:event_searchtextKeyReleased

    private void supliyerbox1PopupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_supliyerbox1PopupMenuWillBecomeInvisible
      
        String suplliyer=(String) supliyerbox1.getSelectedItem();
        try {
            String sql = "Select * from customerinfo where  customername='"+suplliyer+"'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            //tbl_employeeinfo.setModel(DbUtils.resultSetToTableModel(rs));
            while (rs.next()) {

                suppliyerid = rs.getInt("id");

                //  supliyerbox.setSelectedIndex(id);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
        
        if(supliyerbox1.getSelectedIndex()>0){
     try {
         customertblcheck();
     } catch (SQLException ex) {
         Logger.getLogger(CustomerDoucment.class.getName()).log(Level.SEVERE, null, ex);
     }
     
        }
    }//GEN-LAST:event_supliyerbox1PopupMenuWillBecomeInvisible


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable datatbl;
    private javax.swing.JTextField docidtext;
    private javax.swing.JTextField doctitle;
    private javax.swing.JComboBox<String> doctypebox;
    private javax.swing.JTextField file_path;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel pics_lbl;
    private javax.swing.JTextField searchtext;
    private javax.swing.JComboBox<String> supliyerbox;
    private javax.swing.JComboBox<String> supliyerbox1;
    // End of variables declaration//GEN-END:variables

    String filename = null;
    int s = 0;
    byte[] person_image = null;

    private ImageIcon format = null;
}
