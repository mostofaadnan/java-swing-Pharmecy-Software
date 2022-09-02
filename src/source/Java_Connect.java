/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/*
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
*/
/**
 *
 * @author adnan
 */
public class Java_Connect {

    //private static String USERNAME = "root";
    //private static String PASSWORD = "123456adnan";

    //private static String CONT_STRING = "jdbc:mysql://localhost:3306/abdulbasit";

 //  private static String USERNAME =null;
 ///   private static String PASSWORD = null;

   /// private static String CONT_STRING =null;
//public static final String dbUrl = "jdbc:mysql://107.161.82.207:3306/nexttelg_nexttel";

    
    public static Connection conectrDB() throws IOException, SQLException{
     FileInputStream fis=new FileInputStream("src\\config.properties"); 
       
       //  /abdulbasit?useUnicode=yes&characterEncoding=UTF-8
       //reeha 123456adnanmim
       // data reehaper_abdulbasit
       //user reehaper_adnan
       
       //195.201.173.159
       //jdbc:mysql://localhost:3306
        Properties p=new Properties (); 
        p.load (fis); 
        Connection conn = null;
       String USERNAME= (String) p.get ("USERNAME"); 
       String PASSWORD= (String) p.get ("PASSWORD"); 
       String Database= (String) p.get ("DATABASE"); 
       String Url= (String) p.get ("CONT_STRING"); 
       String CONT_STRING=Url+Database+"?useUnicode=yes&characterEncoding=UTF-8";
       
       
       
        try {
            //          driver_class=Class.forName("com.mysql.jdbc.Driver");
            //
            try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            }catch(ClassNotFoundException | InstantiationException | IllegalAccessException e){
            
            
            }
            
            
            conn = DriverManager.getConnection(CONT_STRING, USERNAME, PASSWORD);
            //System.out.println("connectted");

            return conn;
           
        } catch (SQLException e) {
           // databasestart desboard=new databasestart();
           // desboard.databasestart();
            JOptionPane.showMessageDialog(null, "Please Make sure Database Connection String is Correct Or Start Connection Server ");
            int pa = JOptionPane.showConfirmDialog(null, "Do You Want to Propertise Database", "Database Confirmation", JOptionPane.YES_NO_OPTION);
             if (pa == 0) {
            
         //   JOptionPane.showMessageDialog(null, "Please Connect Database");
          Databaseinformation dinfo = null;

       
            dinfo = new Databaseinformation();
        dinfo.setVisible(true);
         
             }
        }
  return null;

    }

}


/*
public class Java_Connect {

    //  MYSQL_DRIVER("com.mysql.jdbc.Driver", "jdbc:mysql://%host%:%port%/%dbname%?verifyServerCertificate=%vsc%&useSSL=%usessl%&requireSSL=%requiressl%"), //

    static public Connection getSimpleDBConnectionByTextFileConfig() throws ClassNotFoundException, IOException, SQLException {
        
final String pFileName="connection.txt";
final HashMap<String, String> settings = new HashMap<>();
        { // load settings from file; could also drop this in separate method
            final List<String> lines = Files.readAllLines(Paths.get(pFileName));
            for (final String line : lines) {
                if (line == null || line.length() < 1 || line.startsWith("#")) continue; // ignore certain lines
                final String[] setting = line.trim().split(" ", 2);
                final String key = setting[0].trim();
                final String value = setting[1].trim();
                settings.put(key, value);
            }
        }

        // load driver
        final String driverName = settings.get("drivername"); // can be "com.mysql.jdbc.Driver" for default java mysql driver
        Class.forName(driverName);

        // set timeout
        final String timeout = settings.get("timeout");
        if (timeout != null && timeout.length() > 0) DriverManager.setLoginTimeout(Integer.parseInt(timeout));

        // connect
        final String url = settings.get("url"); // can be like "jdbc:mysql://%host%:%port%/%dbname%?verifyServerCertificate=%vsc%&useSSL=%usessl%&requireSSL=%requiressl%"
        return DriverManager.getConnection(url, settings.get("user"), settings.get("password"));

 Connection conn = null;

  try {
            //          driver_class=Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection(CONT_STRING, USERNAME, PASSWORD);
            //System.out.println("connectted");

            return conn;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Please Connect Database");
          
            return null;

        }


    }





}

*/