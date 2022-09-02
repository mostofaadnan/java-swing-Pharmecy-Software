/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;


/**
 *
 * @author adnan
 */
public class LicenseAccess {
     LicenseKey licensekey = new LicenseKey();
     String[] keyarray = licensekey.key;
   

    
    public LicenseAccess() throws  IOException, SQLException { 
     FileInputStream fis=new FileInputStream("src\\licensekey.properties"); 
       
       
        Properties p=new Properties (); 
        p.load (fis); 
        
        if(p.get ("key")==null){
      LicenceRegistration licenseregistration=new LicenceRegistration();
    licenseregistration.setVisible(true);
            
        }else{
         String acesskey=(String) p.get ("key"); 
         ///boolean contains = Arrays.stream(keyarray).anyMatch((<any> name) -> name.equals());
        //if(contains==true){
        //Dashboard desboard=new Dashboard();
        // desboard.setVisible(true);
        
boolean found = false;
for (String element:keyarray ) {
    if ( element.equals( acesskey)) {
        found = true;
        LoginAccess desboard=new LoginAccess();
        
       
    }
}
if (!found) {
     LicenceRegistration licenseregistration=new LicenceRegistration();
    licenseregistration.setVisible(true);
}
        
        /*
        }else{
   LicenceRegistration licenseregistration=new LicenceRegistration();
    licenseregistration.setVisible(true);
        
        
        
        }

*/
        
        
        }
        
        
        
    }
    
}
