/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package source;

import javax.swing.JOptionPane;

/**
 *
 * @author adnan
 */
public class LicenseKey {
    //ed9e25c6f4af56f667c591305abd1361
    String company=null;
    //http://www.md5.cz/
   String[] key = { 
       "5465995dcc6a09def5dbf853e3dff7df",
       "1355a6c367697938534f69db3f57fec3", 
       "ac6a50cf180eda22e99d4f7978c7ab64", 
       "b79d1f5f34d040bfaf2f82d636d27aa9",
       "02f84576b2fa77872316977d433c2187",
       "033e0f1582f9cb56d149f51bc545d023",
       "3e5c3a58ef5df6e9ab6081b2759437dd"
   };
   
    public void  LicenseKey(){
   
    
    
    }
   public String company(String key){
   if(  null != key)switch (key) {
            case "5465995dcc6a09def5dbf853e3dff7df":
                //company="Deys Pharma";
                company="National Pharmacy";
                break;
            case "1355a6c367697938534f69db3f57fec3":
                company="HARUN HABIB FOODSTUFF TRADING LLC";
                break;
            case "ac6a50cf180eda22e99d4f7978c7ab64":
                company="ABDULBASIT ABDULHOQUE FOOD STUFF TRADING LLC";
                break;
            case "b79d1f5f34d040bfaf2f82d636d27aa9":
                company="A U S H VEGETABLES & FRUITS TRADING LLC ";
                break;
            case "02f84576b2fa77872316977d433c2187":
                company="MOTI  HAMUD ALI FOODSTUFF TRADING L.L.C";
                break;
                case "033e0f1582f9cb56d149f51bc545d023":
                company="HADIA";
                break;
                case "3e5c3a58ef5df6e9ab6081b2759437dd":
                company="REEHA TRADING Co.L.L.C.";
                break;
            default:
                JOptionPane.showMessageDialog(null, "This Key Is Invalid");
                break;
        }
   return company;
   }
   
   
   
 

  
}
