/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package np;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import source.LicenseAccess;

/**
 *
 * @author FAHIM
 */
public class NP {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, SQLException {
        // System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // load native library of opencv
        // Process process = Runtime.getRuntime().exec("C:\\xampp\\xampp_start.exe");

        //layouthandeler();
        FileInputStream fis = new FileInputStream("src\\layout.properties");

        //  /abdulbasit?useUnicode=yes&characterEncoding=UTF-8
        //jdbc:mysql://localhost:3306
        Properties p = new Properties();
        p.load(fis);
        String laoutname = (String) p.get("laoutname");
        // String laoutname="Nimbus";
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (laoutname.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        LicenseAccess desboard = new LicenseAccess();
        //desboard.LicenseAccess();
    }

}
