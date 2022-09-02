/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.awt.HeadlessException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author adnan
 */
public class LoginAccess {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    public LoginAccess() throws SQLException, IOException {

        conn = Java_Connect.conectrDB();
        try {
            String sql = "Select id from admin";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next() == false) {

                JOptionPane.showMessageDialog(null, "Please Admin Information");
                AdminInfo admin = new AdminInfo();
                admin.setVisible(true);
            } else {
                LoginFrame desboard = new LoginFrame();
                desboard.setVisible(true);

            }
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

}
