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
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import net.proteanit.sql.DbUtils;

/**
 *
 * @author adnan
 */
public class UserProcess extends javax.swing.JInternalFrame {

    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    int tree = 0;

    /**
     * Creates new form UserProcess
     *
     * @throws java.sql.SQLException
     * @throws java.io.IOException
     */
    public UserProcess() throws SQLException, IOException {
        initComponents();
        conn = Java_Connect.conectrDB();
        table_update();
        // systemin();
        // AutoCompleteDecorator.decorate(systembox);

    }

    private void checkall() {

        userpanel.setSelected(true);
        System.setSelected(true);
        saledetials.setSelected(true);
        cashinvoice.setSelected(true);
        creditinvoice.setSelected(true);
        saleretusrn.setSelected(true);
        bankinfotext.setSelected(true);
        bankaccount.setSelected(true);
        cashdrawer.setSelected(true);
        dailypurchseandsale.setSelected(true);
        creditpayment.setSelected(true);
        vatcollecion.setSelected(true);
        dayclose.setSelected(true);
        income.setSelected(true);
        purchase.setSelected(true);
        purchasedetails.setSelected(true);
        grndetaisl.setSelected(true);

        purchasereturn.setSelected(true);
        newimport.setSelected(true);
        importview.setSelected(true);
        exportview.setSelected(true);
        itemlist.setSelected(true);
        stock.setSelected(true);
        wastage.setSelected(true);
        labelprint.setSelected(true);
        purchasepayment.setSelected(true);
        chequepayemnt.setSelected(true);
        expenscc.setSelected(true);
        itemcategory.setSelected(true);
        unitmaangment.setSelected(true);
        expensesstype.setSelected(true);
        countrylist.setSelected(true);
        companyinfo.setSelected(true);
        itemreport.setSelected(true);
        salereport.setSelected(true);
        cashboxreport.setSelected(true);
        bankstatement.setSelected(true);

        grndeailsreport.setSelected(true);
        purchasereport.setSelected(true);
        instancecheck.setSelected(true);
        customerinfo.setSelected(true);
        creditstatement.setSelected(true);

        priceupdate.setSelected(true);
        supplyerinfo.setSelected(true);
        supplyerstaement.setSelected(true);
        databasebackup.setSelected(true);
        databaserestore.setSelected(true);

        stockdemand.setSelected(true);
        offerlist.setSelected(true);
        itemforcast.setSelected(true);
        suppliertarif.setSelected(true);
        supplierpurchase.setSelected(true);
        supplierpayment.setSelected(true);
        supplierforcast.setSelected(true);
        invoicecheck.setSelected(true);
        importpayment.setSelected(true);
        companydocument.setSelected(true);
        supplierdocument.setSelected(true);
        customerdocument.setSelected(true);
        databaseinfo.setSelected(true);
        supplierimport.setSelected(true);
        cashstatement.setSelected(true);
        employeelist.setSelected(true);
        sallerymanagment.setSelected(true);

    }

    private void checknull() {
        userpanel.setSelected(false);
        System.setSelected(false);
        saledetials.setSelected(false);
        cashinvoice.setSelected(false);
        creditinvoice.setSelected(false);
        saleretusrn.setSelected(false);
        bankinfotext.setSelected(false);
        bankaccount.setSelected(false);
        cashdrawer.setSelected(false);
        dailypurchseandsale.setSelected(false);
        creditpayment.setSelected(false);
        vatcollecion.setSelected(false);
        dayclose.setSelected(false);
        income.setSelected(false);
        purchase.setSelected(false);
        purchasedetails.setSelected(false);
        grndetaisl.setSelected(false);
        purchasereturn.setSelected(false);
        newimport.setSelected(false);
        importview.setSelected(false);
        exportview.setSelected(false);
        itemlist.setSelected(false);
        stock.setSelected(false);
        wastage.setSelected(false);
        labelprint.setSelected(false);
        purchasepayment.setSelected(false);
        chequepayemnt.setSelected(false);
        expenscc.setSelected(false);
        itemcategory.setSelected(false);
        unitmaangment.setSelected(false);
        expensesstype.setSelected(false);
        countrylist.setSelected(false);
        companyinfo.setSelected(false);
        itemreport.setSelected(false);
        salereport.setSelected(false);
        cashboxreport.setSelected(false);
        bankstatement.setSelected(false);
        grndeailsreport.setSelected(false);
        purchasereport.setSelected(false);
        instancecheck.setSelected(false);
        customerinfo.setSelected(false);
        creditstatement.setSelected(false);
        priceupdate.setSelected(false);
        supplyerinfo.setSelected(false);
        supplyerstaement.setSelected(false);
        databasebackup.setSelected(false);
        databaserestore.setSelected(false);
        stockdemand.setSelected(false);
        offerlist.setSelected(false);
        itemforcast.setSelected(false);
        suppliertarif.setSelected(false);
        supplierpurchase.setSelected(false);
        supplierpayment.setSelected(false);
        supplierforcast.setSelected(false);
        invoicecheck.setSelected(false);
        importpayment.setSelected(false);
        companydocument.setSelected(false);
        supplierdocument.setSelected(false);
        customerdocument.setSelected(false);
        databaseinfo.setSelected(false);
        supplierimport.setSelected(false);
        cashstatement.setSelected(false);
        employeelist.setSelected(false);
        sallerymanagment.setSelected(false);
    }

    private void clear() {
        employee_id.setText("");
        nametext.setText(null);
        usernametext.setText(null);
        password.setText(null);
        userpanel.setSelected(false);
        System.setSelected(false);
        saledetials.setSelected(false);

        if (statuscheck.isSelected()) {
            statuscheck.setSelected(false);
        }

        cashinvoice.setSelected(false);
        creditinvoice.setSelected(false);
        saleretusrn.setSelected(false);
        bankinfotext.setSelected(false);
        bankaccount.setSelected(false);
        cashdrawer.setSelected(false);
        dailypurchseandsale.setSelected(false);
        creditpayment.setSelected(false);
        vatcollecion.setSelected(false);
        dayclose.setSelected(false);
        income.setSelected(false);
        purchase.setSelected(false);
        purchasedetails.setSelected(false);
        grndetaisl.setSelected(false);
        purchasereturn.setSelected(false);
        newimport.setSelected(false);
        importview.setSelected(false);
        exportview.setSelected(false);
        itemlist.setSelected(false);
        stock.setSelected(false);
        wastage.setSelected(false);
        labelprint.setSelected(false);

        chequepayemnt.setSelected(false);
        expenscc.setSelected(false);
        itemcategory.setSelected(false);
        unitmaangment.setSelected(false);
        expensesstype.setSelected(false);
        countrylist.setSelected(false);
        companyinfo.setSelected(false);
        itemreport.setSelected(false);
        salereport.setSelected(false);
        cashboxreport.setSelected(false);
        bankstatement.setSelected(false);
        grndeailsreport.setSelected(false);
        purchasereport.setSelected(false);
        instancecheck.setSelected(false);
        customerinfo.setSelected(false);
        creditstatement.setSelected(false);
        priceupdate.setSelected(false);
        supplyerinfo.setSelected(false);
        supplyerstaement.setSelected(false);
        databasebackup.setSelected(false);
        databaserestore.setSelected(false);
        stockdemand.setSelected(false);
        offerlist.setSelected(false);
        itemforcast.setSelected(false);
        suppliertarif.setSelected(false);
        supplierpurchase.setSelected(false);
        supplierpayment.setSelected(false);
        supplierforcast.setSelected(false);
        invoicecheck.setSelected(false);
        importpayment.setSelected(false);
        companydocument.setSelected(false);
        supplierdocument.setSelected(false);
        customerdocument.setSelected(false);
        databaseinfo.setSelected(false);
        supplierimport.setSelected(false);
        cashstatement.setSelected(false);
        employeelist.setSelected(false);
        sallerymanagment.setSelected(false);

    }

    private void table_update() throws SQLException {

        try {
            String sql = "Select * from admin ";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            tbluserinfo.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        } finally {

            try {

                rs.close();
                pst.close();

            } catch (Exception e) {

            }

        }

    }

    private void modificationAccsess(int userid) {
        try {

            String sql = "Insert into modificationaccsess(userid,itmedit,itmdelete,purcedit,purcdelete,imexedit,imexdelete,cshcrditedit,cshcrditdelete,prcpedit,purcpdelete,imexpedit,imexpdelete,creditpedit,creditpdelete,supedit,supdelete,cusedit,cusdelete) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);

            pst.setInt(1, userid);
            pst.setInt(2, 0);
            pst.setInt(3, 0);
            pst.setInt(4, 0);
            pst.setInt(5, 0);
            pst.setInt(6, 0);
            pst.setInt(7, 0);
            pst.setInt(8, 0);
            pst.setInt(9, 0);
            pst.setInt(10, 0);
            pst.setInt(11, 0);
            pst.setInt(12, 0);
            pst.setInt(13, 0);
            pst.setInt(14, 0);
            pst.setInt(15, 0);
            pst.setInt(16, 0);
            pst.setInt(17, 0);
            pst.setInt(18, 0);
            pst.setInt(19, 0);

            pst.execute();

        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void loginaccess(int generatedKey) throws SQLException {
        int loginaccess1, loginaccess2, loginaccess3, loginaccess4, loginaccess5, loginaccess6, loginaccess7, loginaccess8, loginaccess9,
                loginaccess10, loginaccess11, loginaccess12, loginaccess13, loginaccess14, loginaccess15, loginaccess16, loginaccess17, loginaccess18,
                loginaccess19, loginaccess20, loginaccess21, loginaccess22, loginaccess23, loginaccess24, loginaccess25,
                loginaccess26, loginaccess27, loginaccess28, loginaccess29, loginaccess30, loginaccess31, loginaccess32, loginaccess33, loginaccess34, loginaccess35,
                loginaccess36, loginaccess37, loginaccess38, loginaccess39, loginaccess40, loginaccess41, loginaccess42, loginaccess43, loginaccess44, loginaccess45,
                loginaccess46, loginaccess47, loginaccess48, loginaccess49, loginaccess50,
                loginaccess51, loginaccess52, loginaccess53, loginaccess54, loginaccess55, loginaccess56, loginaccess57,
                loginaccess58, loginaccess59, loginaccess60, loginaccess61, loginaccess62, loginaccess63, loginaccess64;

        //user-2
        if (userpanel.isSelected()) {
            loginaccess1 = 1;

        } else {
            loginaccess1 = 0;

        }
        if (System.isSelected()) {
            loginaccess2 = 1;
        } else {
            loginaccess2 = 0;

        }

        //sale-4
        if (saledetials.isSelected()) {

            loginaccess3 = 1;
        } else {
            loginaccess3 = 0;

        }
        if (cashinvoice.isSelected()) {
            loginaccess4 = 1;
        } else {
            loginaccess4 = 0;
        }

        if (creditinvoice.isSelected()) {
            loginaccess5 = 1;
        } else {
            loginaccess5 = 0;
        }

        if (saleretusrn.isSelected()) {

            loginaccess6 = 1;
        } else {
            loginaccess6 = 0;

        }
        //account-8

        if (bankinfotext.isSelected()) {

            loginaccess7 = 1;
        } else {
            loginaccess7 = 0;

        }
        if (bankaccount.isSelected()) {

            loginaccess8 = 1;
        } else {
            loginaccess8 = 0;

        }
        if (cashdrawer.isSelected()) {

            loginaccess9 = 1;
        } else {
            loginaccess9 = 0;

        }
        if (dailypurchseandsale.isSelected()) {

            loginaccess10 = 1;
        } else {
            loginaccess10 = 0;

        }
        if (creditpayment.isSelected()) {

            loginaccess11 = 1;
        } else {
            loginaccess11 = 0;

        }

        if (vatcollecion.isSelected()) {
            loginaccess12 = 1;

        } else {
            loginaccess12 = 0;
        }
        if (dayclose.isSelected()) {
            loginaccess13 = 1;

        } else {
            loginaccess13 = 0;
        }

        if (income.isSelected()) {
            loginaccess14 = 1;

        } else {
            loginaccess14 = 0;
        }

        //Operation-7
        if (purchase.isSelected()) {

            loginaccess15 = 1;
        } else {
            loginaccess15 = 0;

        }
        if (purchasedetails.isSelected()) {

            loginaccess16 = 1;
        } else {
            loginaccess16 = 0;

        }
        if (grndetaisl.isSelected()) {

            loginaccess17 = 1;
        } else {
            loginaccess17 = 0;

        }
        if (purchasereturn.isSelected()) {

            loginaccess18 = 1;
        } else {
            loginaccess18 = 0;

        }

        if (newimport.isSelected()) {

            loginaccess19 = 1;
        } else {
            loginaccess19 = 0;

        }

        if (importview.isSelected()) {

            loginaccess20 = 1;
        } else {
            loginaccess20 = 0;

        }

        if (exportview.isSelected()) {

            loginaccess21 = 1;
        } else {
            loginaccess21 = 0;

        }

        //Item Master-4
        if (itemlist.isSelected()) {

            loginaccess22 = 1;
        } else {
            loginaccess22 = 0;

        }

        if (stock.isSelected()) {

            loginaccess23 = 1;
        } else {
            loginaccess23 = 0;

        }

        if (wastage.isSelected()) {

            loginaccess24 = 1;
        } else {
            loginaccess24 = 0;

        }

        if (labelprint.isSelected()) {

            loginaccess25 = 1;
        } else {
            loginaccess25 = 0;

        }

        ///Payment
        if (purchasepayment.isSelected()) {

            loginaccess26 = 1;
        } else {
            loginaccess26 = 0;

        }

        if (chequepayemnt.isSelected()) {

            loginaccess27 = 1;
        } else {
            loginaccess27 = 0;

        }

        if (expenscc.isSelected()) {

            loginaccess28 = 1;
        } else {
            loginaccess28 = 0;

        }
        //Setup
        if (itemcategory.isSelected()) {

            loginaccess29 = 1;
        } else {
            loginaccess29 = 0;

        }
        if (unitmaangment.isSelected()) {

            loginaccess30 = 1;
        } else {
            loginaccess30 = 0;

        }
        if (expensesstype.isSelected()) {

            loginaccess31 = 1;
        } else {
            loginaccess31 = 0;

        }
        if (countrylist.isSelected()) {

            loginaccess32 = 1;
        } else {
            loginaccess32 = 0;

        }
        if (companyinfo.isSelected()) {

            loginaccess33 = 1;
        } else {
            loginaccess33 = 0;

        }

        //Report
        if (itemreport.isSelected()) {

            loginaccess34 = 1;
        } else {
            loginaccess34 = 0;

        }
        if (salereport.isSelected()) {

            loginaccess35 = 1;
        } else {
            loginaccess35 = 0;

        }
        if (cashboxreport.isSelected()) {

            loginaccess36 = 1;
        } else {
            loginaccess36 = 0;

        }

        if (bankstatement.isSelected()) {

            loginaccess37 = 1;
        } else {
            loginaccess37 = 0;

        }

        if (grndeailsreport.isSelected()) {

            loginaccess38 = 1;
        } else {
            loginaccess38 = 0;

        }
        if (purchasereport.isSelected()) {

            loginaccess39 = 1;
        } else {
            loginaccess39 = 0;

        }

        //Customer
        if (instancecheck.isSelected()) {

            loginaccess40 = 1;
        } else {
            loginaccess40 = 0;

        }

        if (customerinfo.isSelected()) {
            loginaccess41 = 1;

        } else {
            loginaccess41 = 0;
        }

        if (creditstatement.isSelected()) {
            loginaccess42 = 1;

        } else {
            loginaccess42 = 0;
        }

        if (priceupdate.isSelected()) {
            loginaccess43 = 1;

        } else {
            loginaccess43 = 0;
        }

        if (supplyerinfo.isSelected()) {
            loginaccess44 = 1;

        } else {
            loginaccess44 = 0;
        }
        if (supplyerstaement.isSelected()) {
            loginaccess45 = 1;

        } else {
            loginaccess45 = 0;
        }

        if (databasebackup.isSelected()) {
            loginaccess46 = 1;

        } else {
            loginaccess46 = 0;
        }
        if (databaserestore.isSelected()) {
            loginaccess47 = 1;

        } else {
            loginaccess47 = 0;
        }

        if (stockdemand.isSelected()) {
            loginaccess48 = 1;

        } else {
            loginaccess48 = 0;
        }

        if (offerlist.isSelected()) {
            loginaccess49 = 1;

        } else {
            loginaccess49 = 0;
        }
        if (itemforcast.isSelected()) {
            loginaccess50 = 1;

        } else {
            loginaccess50 = 0;
        }

        if (suppliertarif.isSelected()) {
            loginaccess51 = 1;

        } else {
            loginaccess51 = 0;
        }
        if (supplierpurchase.isSelected()) {
            loginaccess52 = 1;

        } else {
            loginaccess52 = 0;
        }

        if (supplierpayment.isSelected()) {
            loginaccess53 = 1;

        } else {
            loginaccess53 = 0;
        }
        if (supplierforcast.isSelected()) {
            loginaccess54 = 1;

        } else {
            loginaccess54 = 0;
        }
        if (supplierimport.isSelected()) {
            loginaccess55 = 1;

        } else {
            loginaccess55 = 0;
        }
        if (invoicecheck.isSelected()) {
            loginaccess56 = 1;

        } else {
            loginaccess56 = 0;
        }
        if (importpayment.isSelected()) {
            loginaccess57 = 1;

        } else {
            loginaccess57 = 0;
        }
        if (companydocument.isSelected()) {
            loginaccess58 = 1;

        } else {
            loginaccess58 = 0;
        }
        if (supplierdocument.isSelected()) {
            loginaccess59 = 1;

        } else {
            loginaccess59 = 0;
        }
        if (supplierdocument.isSelected()) {
            loginaccess60 = 1;

        } else {
            loginaccess60 = 0;
        }

        if (databaseinfo.isSelected()) {
            loginaccess61 = 1;

        } else {
            loginaccess61 = 0;
        }

        if (cashstatement.isSelected()) {
            loginaccess62 = 1;

        } else {
            loginaccess62 = 0;
        }

        if (employeelist.isSelected()) {
            loginaccess63 = 1;

        } else {
            loginaccess63 = 0;
        }

        if (sallerymanagment.isSelected()) {
            loginaccess64 = 1;

        } else {
            loginaccess64 = 0;
        }
        String[] queries = {
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + userpanel.getText() + "','" + loginaccess1 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + System.getText() + "','" + loginaccess2 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + saledetials.getText() + "','" + loginaccess3 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + cashinvoice.getText() + "','" + loginaccess4 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + creditinvoice.getText() + "','" + loginaccess5 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + saleretusrn.getText() + "','" + loginaccess6 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + bankinfotext.getText() + "','" + loginaccess7 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + bankaccount.getText() + "','" + loginaccess8 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + cashdrawer.getText() + "','" + loginaccess9 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + dailypurchseandsale.getText() + "','" + loginaccess10 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + creditpayment.getText() + "','" + loginaccess11 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + vatcollecion.getText() + "','" + loginaccess12 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + dayclose.getText() + "','" + loginaccess13 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + income.getText() + "','" + loginaccess14 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + purchase.getText() + "','" + loginaccess15 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + purchasedetails.getText() + "','" + loginaccess16 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + grndetaisl.getText() + "','" + loginaccess17 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + purchasereturn.getText() + "','" + loginaccess18 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + newimport.getText() + "','" + loginaccess19 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + importview.getText() + "','" + loginaccess20 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + exportview.getText() + "','" + loginaccess21 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + itemlist.getText() + "','" + loginaccess22 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + stock.getText() + "','" + loginaccess23 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + wastage.getText() + "','" + loginaccess24 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + labelprint.getText() + "','" + loginaccess25 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + purchasepayment.getText() + "','" + loginaccess26 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + chequepayemnt.getText() + "','" + loginaccess27 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + expenscc.getText() + "','" + loginaccess28 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + itemcategory.getText() + "','" + loginaccess29 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + unitmaangment.getText() + "','" + loginaccess30 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + expensesstype.getText() + "','" + loginaccess31 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + countrylist.getText() + "','" + loginaccess32 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + companyinfo.getText() + "','" + loginaccess33 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + itemreport.getText() + "','" + loginaccess34 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + salereport.getText() + "','" + loginaccess35 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + cashboxreport.getText() + "','" + loginaccess36 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + bankstatement.getText() + "','" + loginaccess37 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + grndeailsreport.getText() + "','" + loginaccess38 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + purchasereport.getText() + "','" + loginaccess39 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + instancecheck.getText() + "','" + loginaccess40 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + customerinfo.getText() + "','" + loginaccess41 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + creditstatement.getText() + "','" + loginaccess42 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + priceupdate.getText() + "','" + loginaccess43 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + supplyerinfo.getText() + "','" + loginaccess44 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + supplyerstaement.getText() + "','" + loginaccess45 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + databasebackup.getText() + "','" + loginaccess46 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + databaserestore.getText() + "','" + loginaccess47 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + stockdemand.getText() + "','" + loginaccess48 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + offerlist.getText() + "','" + loginaccess49 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + itemforcast.getText() + "','" + loginaccess50 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + suppliertarif.getText() + "','" + loginaccess51 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + supplierpurchase.getText() + "','" + loginaccess52 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + supplierpayment.getText() + "','" + loginaccess53 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + supplierforcast.getText() + "','" + loginaccess54 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + supplierimport.getText() + "','" + loginaccess55 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + invoicecheck.getText() + "','" + loginaccess56 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + importpayment.getText() + "','" + loginaccess57 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + companydocument.getText() + "','" + loginaccess58 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + supplierdocument.getText() + "','" + loginaccess59 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + customerdocument.getText() + "','" + loginaccess60 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + databaseinfo.getText() + "','" + loginaccess61 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + cashstatement.getText() + "','" + loginaccess62 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + employeelist.getText() + "','" + loginaccess63 + "')",
            "insert into loginaccess (UserId, Accessname, access) values ('" + generatedKey + "', '" + sallerymanagment.getText() + "','" + loginaccess64 + "')"

        };

        Statement statement = conn.createStatement();

        for (String query : queries) {
            statement.execute(query);
        }
    }

    @SuppressWarnings("empty-statement")
    private void insertdata() {
        try {
            int sts;
            if (statuscheck.isSelected()) {

                sts = 1;
            } else {
                sts = 0;

            }
            String sql = "Insert into admin(name,username,password,status) values(?,?,?,?)";
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, nametext.getText());
            pst.setString(2, usernametext.getText());
            pst.setString(3, password.getText());
            pst.setInt(4, sts);
            pst.execute();
            ResultSet rshere = pst.getGeneratedKeys();
            int generatedKey = 0;
            if (rshere.next()) {
                generatedKey = rshere.getInt(1);
                loginaccess(generatedKey);
                modificationAccsess(generatedKey);

            }

            JOptionPane.showMessageDialog(null, "Data Saved");
            table_update();
            clear();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void userupdate() {

        try {

            String em_id = employee_id.getText();
            String em_name = nametext.getText();
            String em_user = usernametext.getText();
            String em_pass = password.getText();

            String sql = "Update admin set  name='" + em_name + "',username='" + em_user + "',password='" + em_pass + "' where id='" + em_id + "'";

            pst = conn.prepareStatement(sql);

            pst.execute();
            JOptionPane.showMessageDialog(null, em_name + ":Data Update");
            //table_update();
            // clear();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {

            int loginaccess1, loginaccess2, loginaccess3, loginaccess4, loginaccess5, loginaccess6, loginaccess7, loginaccess8, loginaccess9,
                    loginaccess10, loginaccess11, loginaccess12, loginaccess13, loginaccess14, loginaccess15, loginaccess16, loginaccess17, loginaccess18,
                    loginaccess19, loginaccess20, loginaccess21, loginaccess22, loginaccess23, loginaccess24, loginaccess25,
                    loginaccess26, loginaccess27, loginaccess28, loginaccess29, loginaccess30, loginaccess31, loginaccess32, loginaccess33, loginaccess34, loginaccess35,
                    loginaccess36, loginaccess37, loginaccess38, loginaccess39, loginaccess40, loginaccess41, loginaccess42, loginaccess43, loginaccess44, loginaccess45, loginaccess46, loginaccess47,
                    loginaccess48, loginaccess49, loginaccess50,
                    loginaccess51, loginaccess52, loginaccess53, loginaccess54, loginaccess55, loginaccess56, loginaccess57,
                    loginaccess58, loginaccess59, loginaccess60, loginaccess61, loginaccess62, loginaccess63, loginaccess64;

            //user-2
            if (userpanel.isSelected()) {
                loginaccess1 = 1;

            } else {
                loginaccess1 = 0;

            }
            if (System.isSelected()) {
                loginaccess2 = 1;
            } else {
                loginaccess2 = 0;

            }

            //sale-4
            if (saledetials.isSelected()) {

                loginaccess3 = 1;
            } else {
                loginaccess3 = 0;

            }
            if (cashinvoice.isSelected()) {
                loginaccess4 = 1;
            } else {
                loginaccess4 = 0;
            }

            if (creditinvoice.isSelected()) {
                loginaccess5 = 1;
            } else {
                loginaccess5 = 0;
            }

            if (saleretusrn.isSelected()) {

                loginaccess6 = 1;
            } else {
                loginaccess6 = 0;

            }
            //account-8

            if (bankinfotext.isSelected()) {

                loginaccess7 = 1;
            } else {
                loginaccess7 = 0;

            }
            if (bankaccount.isSelected()) {

                loginaccess8 = 1;
            } else {
                loginaccess8 = 0;

            }
            if (cashdrawer.isSelected()) {

                loginaccess9 = 1;
            } else {
                loginaccess9 = 0;

            }
            if (dailypurchseandsale.isSelected()) {

                loginaccess10 = 1;
            } else {
                loginaccess10 = 0;

            }
            if (creditpayment.isSelected()) {

                loginaccess11 = 1;
            } else {
                loginaccess11 = 0;

            }

            if (vatcollecion.isSelected()) {
                loginaccess12 = 1;

            } else {
                loginaccess12 = 0;
            }
            if (dayclose.isSelected()) {
                loginaccess13 = 1;

            } else {
                loginaccess13 = 0;
            }

            if (income.isSelected()) {
                loginaccess14 = 1;

            } else {
                loginaccess14 = 0;
            }

            //Operation-7
            if (purchase.isSelected()) {

                loginaccess15 = 1;
            } else {
                loginaccess15 = 0;

            }
            if (purchasedetails.isSelected()) {

                loginaccess16 = 1;
            } else {
                loginaccess16 = 0;

            }
            if (grndetaisl.isSelected()) {

                loginaccess17 = 1;
            } else {
                loginaccess17 = 0;

            }
            if (purchasereturn.isSelected()) {

                loginaccess18 = 1;
            } else {
                loginaccess18 = 0;

            }

            if (newimport.isSelected()) {

                loginaccess19 = 1;
            } else {
                loginaccess19 = 0;

            }

            if (importview.isSelected()) {

                loginaccess20 = 1;
            } else {
                loginaccess20 = 0;

            }

            if (exportview.isSelected()) {

                loginaccess21 = 1;
            } else {
                loginaccess21 = 0;

            }

            //Item Master-4
            if (itemlist.isSelected()) {

                loginaccess22 = 1;
            } else {
                loginaccess22 = 0;

            }

            if (stock.isSelected()) {

                loginaccess23 = 1;
            } else {
                loginaccess23 = 0;

            }

            if (wastage.isSelected()) {

                loginaccess24 = 1;
            } else {
                loginaccess24 = 0;

            }

            if (labelprint.isSelected()) {

                loginaccess25 = 1;
            } else {
                loginaccess25 = 0;

            }

            ///Payment
            if (purchasepayment.isSelected()) {

                loginaccess26 = 1;
            } else {
                loginaccess26 = 0;

            }

            if (chequepayemnt.isSelected()) {

                loginaccess27 = 1;
            } else {
                loginaccess27 = 0;

            }

            if (expenscc.isSelected()) {

                loginaccess28 = 1;
            } else {
                loginaccess28 = 0;

            }
            //Setup
            if (itemcategory.isSelected()) {

                loginaccess29 = 1;
            } else {
                loginaccess29 = 0;

            }
            if (unitmaangment.isSelected()) {

                loginaccess30 = 1;
            } else {
                loginaccess30 = 0;

            }
            if (expensesstype.isSelected()) {

                loginaccess31 = 1;
            } else {
                loginaccess31 = 0;

            }
            if (countrylist.isSelected()) {

                loginaccess32 = 1;
            } else {
                loginaccess32 = 0;

            }
            if (companyinfo.isSelected()) {

                loginaccess33 = 1;
            } else {
                loginaccess33 = 0;

            }

            //Report
            if (itemreport.isSelected()) {

                loginaccess34 = 1;
            } else {
                loginaccess34 = 0;

            }
            if (salereport.isSelected()) {

                loginaccess35 = 1;
            } else {
                loginaccess35 = 0;

            }
            if (cashboxreport.isSelected()) {

                loginaccess36 = 1;
            } else {
                loginaccess36 = 0;

            }

            if (bankstatement.isSelected()) {

                loginaccess37 = 1;
            } else {
                loginaccess37 = 0;

            }

            if (grndeailsreport.isSelected()) {

                loginaccess38 = 1;
            } else {
                loginaccess38 = 0;

            }
            if (purchasereport.isSelected()) {

                loginaccess39 = 1;
            } else {
                loginaccess39 = 0;

            }

            //Customer
            if (instancecheck.isSelected()) {

                loginaccess40 = 1;
            } else {
                loginaccess40 = 0;

            }

            if (customerinfo.isSelected()) {
                loginaccess41 = 1;

            } else {
                loginaccess41 = 0;
            }

            if (creditstatement.isSelected()) {
                loginaccess42 = 1;

            } else {
                loginaccess42 = 0;
            }

            if (priceupdate.isSelected()) {
                loginaccess43 = 1;

            } else {
                loginaccess43 = 0;
            }

            if (supplyerinfo.isSelected()) {
                loginaccess44 = 1;

            } else {
                loginaccess44 = 0;
            }
            if (supplyerstaement.isSelected()) {
                loginaccess45 = 1;

            } else {
                loginaccess45 = 0;
            }

            if (databasebackup.isSelected()) {
                loginaccess46 = 1;

            } else {
                loginaccess46 = 0;
            }
            if (databaserestore.isSelected()) {
                loginaccess47 = 1;

            } else {
                loginaccess47 = 0;
            }

            if (stockdemand.isSelected()) {
                loginaccess48 = 1;

            } else {
                loginaccess48 = 0;
            }

            if (offerlist.isSelected()) {
                loginaccess49 = 1;

            } else {
                loginaccess49 = 0;
            }
            if (itemforcast.isSelected()) {
                loginaccess50 = 1;

            } else {
                loginaccess50 = 0;
            }

            if (suppliertarif.isSelected()) {
                loginaccess51 = 1;

            } else {
                loginaccess51 = 0;
            }
            if (supplierpurchase.isSelected()) {
                loginaccess52 = 1;

            } else {
                loginaccess52 = 0;
            }

            if (supplierpayment.isSelected()) {
                loginaccess53 = 1;

            } else {
                loginaccess53 = 0;
            }
            if (supplierforcast.isSelected()) {
                loginaccess54 = 1;

            } else {
                loginaccess54 = 0;
            }
            if (supplierimport.isSelected()) {
                loginaccess55 = 1;

            } else {
                loginaccess55 = 0;
            }
            if (invoicecheck.isSelected()) {
                loginaccess56 = 1;

            } else {
                loginaccess56 = 0;
            }
            if (importpayment.isSelected()) {
                loginaccess57 = 1;

            } else {
                loginaccess57 = 0;
            }
            if (companydocument.isSelected()) {
                loginaccess58 = 1;

            } else {
                loginaccess58 = 0;
            }
            if (supplierdocument.isSelected()) {
                loginaccess59 = 1;

            } else {
                loginaccess59 = 0;
            }
            if (customerdocument.isSelected()) {
                loginaccess60 = 1;

            } else {
                loginaccess60 = 0;
            }

            if (databaseinfo.isSelected()) {
                loginaccess61 = 1;

            } else {
                loginaccess61 = 0;
            }
            if (cashstatement.isSelected()) {
                loginaccess62 = 1;

            } else {
                loginaccess62 = 0;
            }

            if (employeelist.isSelected()) {
                loginaccess63 = 1;

            } else {
                loginaccess63 = 0;
            }

            if (sallerymanagment.isSelected()) {
                loginaccess64 = 1;

            } else {
                loginaccess64 = 0;
            }
            String userid = employee_id.getText();

            String[] queries = {
                "Update loginaccess set  	access='" + loginaccess1 + "' where Accessname='" + userpanel.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess2 + "' where Accessname='" + System.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess3 + "' where Accessname='" + saledetials.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess4 + "' where Accessname='" + cashinvoice.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess5 + "' where Accessname='" + creditinvoice.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess6 + "' where Accessname='" + saleretusrn.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess7 + "' where Accessname='" + bankinfotext.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess8 + "' where Accessname='" + bankaccount.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess9 + "' where Accessname='" + cashdrawer.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess10 + "' where Accessname='" + dailypurchseandsale.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess11 + "' where Accessname='" + creditpayment.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess12 + "' where Accessname='" + vatcollecion.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess13 + "' where Accessname='" + dayclose.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess14 + "' where Accessname='" + income.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess15 + "' where Accessname='" + purchase.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess16 + "' where Accessname='" + purchasedetails.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess17 + "' where Accessname='" + grndetaisl.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess18 + "' where Accessname='" + purchasereturn.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess19 + "' where Accessname='" + newimport.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess20 + "' where Accessname='" + importview.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess21 + "' where Accessname='" + exportview.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess22 + "' where Accessname='" + itemlist.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess23 + "' where Accessname='" + stock.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess24 + "' where Accessname='" + wastage.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess25 + "' where Accessname='" + labelprint.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess26 + "' where Accessname='" + purchasepayment.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess27 + "' where Accessname='" + chequepayemnt.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess28 + "' where Accessname='" + expenscc.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess29 + "' where Accessname='" + itemcategory.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess30 + "' where Accessname='" + unitmaangment.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess31 + "' where Accessname='" + expensesstype.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess32 + "' where Accessname='" + countrylist.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess33 + "' where Accessname='" + companyinfo.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess34 + "' where Accessname='" + itemreport.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess35 + "' where Accessname='" + salereport.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess36 + "' where Accessname='" + cashboxreport.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess37 + "' where Accessname='" + bankstatement.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess38 + "' where Accessname='" + grndeailsreport.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess39 + "' where Accessname='" + purchasereport.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess40 + "' where Accessname='" + instancecheck.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess41 + "' where Accessname='" + customerinfo.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess42 + "' where Accessname='" + creditstatement.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess43 + "' where Accessname='" + priceupdate.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess44 + "' where Accessname='" + supplyerinfo.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess45 + "' where Accessname='" + supplyerstaement.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess46 + "' where Accessname='" + databasebackup.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess47 + "' where Accessname='" + databaserestore.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess48 + "' where Accessname='" + stockdemand.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess49 + "' where Accessname='" + offerlist.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess50 + "' where Accessname='" + itemforcast.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess51 + "' where Accessname='" + suppliertarif.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess52 + "' where Accessname='" + supplierpurchase.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess53 + "' where Accessname='" + supplierpayment.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess54 + "' where Accessname='" + supplierforcast.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess55 + "' where Accessname='" + supplierimport.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess56 + "' where Accessname='" + invoicecheck.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess57 + "' where Accessname='" + importpayment.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess58 + "' where Accessname='" + companydocument.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess59 + "' where Accessname='" + supplierdocument.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess60 + "' where Accessname='" + customerdocument.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess61 + "' where Accessname='" + databaseinfo.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess62 + "' where Accessname='" + cashstatement.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess63 + "' where Accessname='" + employeelist.getText() + "' and UserId='" + userid + "'",
                "Update loginaccess set  	access='" + loginaccess64 + "' where Accessname='" + sallerymanagment.getText() + "' and UserId='" + userid + "'"};

            Statement statement = conn.createStatement();

            for (String query : queries) {
                statement.execute(query);
            }
            //  JOptionPane.showMessageDialog(null, em_name + ":Data Update");
            table_update();
            clear();

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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nametext = new javax.swing.JTextField();
        usernametext = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        employee_id = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbluserinfo = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        statuscheck = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        userpanel = new javax.swing.JCheckBox();
        System = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        cashinvoice = new javax.swing.JCheckBox();
        saledetials = new javax.swing.JCheckBox();
        creditinvoice = new javax.swing.JCheckBox();
        saleretusrn = new javax.swing.JCheckBox();
        invoicecheck = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        bankinfotext = new javax.swing.JCheckBox();
        bankaccount = new javax.swing.JCheckBox();
        cashdrawer = new javax.swing.JCheckBox();
        dailypurchseandsale = new javax.swing.JCheckBox();
        vatcollecion = new javax.swing.JCheckBox();
        dayclose = new javax.swing.JCheckBox();
        income = new javax.swing.JCheckBox();
        creditpayment = new javax.swing.JCheckBox();
        jPanel9 = new javax.swing.JPanel();
        purchase = new javax.swing.JCheckBox();
        purchasedetails = new javax.swing.JCheckBox();
        grndetaisl = new javax.swing.JCheckBox();
        purchasereturn = new javax.swing.JCheckBox();
        newimport = new javax.swing.JCheckBox();
        importview = new javax.swing.JCheckBox();
        exportview = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        itemcategory = new javax.swing.JCheckBox();
        unitmaangment = new javax.swing.JCheckBox();
        expensesstype = new javax.swing.JCheckBox();
        countrylist = new javax.swing.JCheckBox();
        companyinfo = new javax.swing.JCheckBox();
        customerdocument = new javax.swing.JCheckBox();
        companydocument = new javax.swing.JCheckBox();
        supplierdocument = new javax.swing.JCheckBox();
        jPanel11 = new javax.swing.JPanel();
        itemreport = new javax.swing.JCheckBox();
        salereport = new javax.swing.JCheckBox();
        cashboxreport = new javax.swing.JCheckBox();
        bankstatement = new javax.swing.JCheckBox();
        grndeailsreport = new javax.swing.JCheckBox();
        purchasereport = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        instancecheck = new javax.swing.JCheckBox();
        customerinfo = new javax.swing.JCheckBox();
        creditstatement = new javax.swing.JCheckBox();
        cashstatement = new javax.swing.JCheckBox();
        checkall = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        itemlist = new javax.swing.JCheckBox();
        stock = new javax.swing.JCheckBox();
        wastage = new javax.swing.JCheckBox();
        labelprint = new javax.swing.JCheckBox();
        priceupdate = new javax.swing.JCheckBox();
        stockdemand = new javax.swing.JCheckBox();
        offerlist = new javax.swing.JCheckBox();
        itemforcast = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        purchasepayment = new javax.swing.JCheckBox();
        chequepayemnt = new javax.swing.JCheckBox();
        expenscc = new javax.swing.JCheckBox();
        importpayment = new javax.swing.JCheckBox();
        jPanel15 = new javax.swing.JPanel();
        supplyerinfo = new javax.swing.JCheckBox();
        supplyerstaement = new javax.swing.JCheckBox();
        suppliertarif = new javax.swing.JCheckBox();
        supplierpurchase = new javax.swing.JCheckBox();
        supplierpayment = new javax.swing.JCheckBox();
        supplierforcast = new javax.swing.JCheckBox();
        supplierimport = new javax.swing.JCheckBox();
        jPanel16 = new javax.swing.JPanel();
        databasebackup = new javax.swing.JCheckBox();
        databaserestore = new javax.swing.JCheckBox();
        databaseinfo = new javax.swing.JCheckBox();
        jPanel17 = new javax.swing.JPanel();
        sallerymanagment = new javax.swing.JCheckBox();
        employeelist = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("User Panel");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/source/company.png"))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "User Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18), new java.awt.Color(153, 0, 51))); // NOI18N
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("User id");

        nametext.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        nametext.setForeground(new java.awt.Color(0, 102, 51));
        nametext.setToolTipText("Name");

        usernametext.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        usernametext.setForeground(new java.awt.Color(0, 102, 51));
        usernametext.setToolTipText("Username");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Name");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Username");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Password");

        employee_id.setBackground(new java.awt.Color(255, 255, 255));
        employee_id.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        password.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        password.setForeground(new java.awt.Color(0, 102, 51));

        tbluserinfo.setModel(new javax.swing.table.DefaultTableModel(
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
        tbluserinfo.setGridColor(new java.awt.Color(204, 204, 204));
        tbluserinfo.setRowHeight(30);
        tbluserinfo.setShowVerticalLines(false);
        tbluserinfo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbluserinfoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbluserinfo);

        jButton3.setBackground(new java.awt.Color(153, 0, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 153, 153));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(0, 102, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Execute");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(268, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jButton2)
                .addGap(0, 0, 0)
                .addComponent(jButton3)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        statuscheck.setText("Status");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Access", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Use Info"));

        userpanel.setBackground(new java.awt.Color(255, 255, 255));
        userpanel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        userpanel.setText("User Panel");

        System.setBackground(new java.awt.Color(255, 255, 255));
        System.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        System.setText("System");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userpanel)
                .addGap(18, 18, 18)
                .addComponent(System)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userpanel)
                    .addComponent(System))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Sale"));

        cashinvoice.setBackground(new java.awt.Color(255, 255, 255));
        cashinvoice.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cashinvoice.setText("Cash Invoice");
        cashinvoice.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cashinvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashinvoiceActionPerformed(evt);
            }
        });

        saledetials.setBackground(new java.awt.Color(255, 255, 255));
        saledetials.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        saledetials.setText("Sale Details");

        creditinvoice.setBackground(new java.awt.Color(255, 255, 255));
        creditinvoice.setText("Credit Invoice");

        saleretusrn.setBackground(new java.awt.Color(255, 255, 255));
        saleretusrn.setText("Sale Return Details");

        invoicecheck.setBackground(new java.awt.Color(255, 255, 255));
        invoicecheck.setText("Invoice Check");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saledetials)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashinvoice)
                .addGap(8, 8, 8)
                .addComponent(creditinvoice)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saleretusrn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invoicecheck)
                .addContainerGap(244, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cashinvoice)
                    .addComponent(saledetials)
                    .addComponent(creditinvoice)
                    .addComponent(saleretusrn)
                    .addComponent(invoicecheck))
                .addGap(1, 1, 1))
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Account"));

        bankinfotext.setBackground(new java.awt.Color(255, 255, 255));
        bankinfotext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bankinfotext.setText("Bank Info");

        bankaccount.setBackground(new java.awt.Color(255, 255, 255));
        bankaccount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bankaccount.setText("Bank Acount");

        cashdrawer.setBackground(new java.awt.Color(255, 255, 255));
        cashdrawer.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cashdrawer.setText("Cash Drower");

        dailypurchseandsale.setBackground(new java.awt.Color(255, 255, 255));
        dailypurchseandsale.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        dailypurchseandsale.setText("Daily Purchase & Sale Item");

        vatcollecion.setBackground(new java.awt.Color(255, 255, 255));
        vatcollecion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        vatcollecion.setText("Vat Collection");

        dayclose.setBackground(new java.awt.Color(255, 255, 255));
        dayclose.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        dayclose.setText("Day close");

        income.setBackground(new java.awt.Color(255, 255, 255));
        income.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        income.setText("Income & Loss");

        creditpayment.setBackground(new java.awt.Color(255, 255, 255));
        creditpayment.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        creditpayment.setText("Credit Payment");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bankinfotext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bankaccount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashdrawer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(vatcollecion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dayclose)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(income)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dailypurchseandsale)
                .addGap(1, 1, 1)
                .addComponent(creditpayment)
                .addGap(101, 101, 101))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bankinfotext)
                    .addComponent(bankaccount)
                    .addComponent(cashdrawer)
                    .addComponent(vatcollecion)
                    .addComponent(dayclose)
                    .addComponent(income)
                    .addComponent(dailypurchseandsale)
                    .addComponent(creditpayment))
                .addGap(1, 1, 1))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Oparation"));

        purchase.setBackground(new java.awt.Color(255, 255, 255));
        purchase.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        purchase.setText("Purchase");

        purchasedetails.setBackground(new java.awt.Color(255, 255, 255));
        purchasedetails.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        purchasedetails.setText("Purchase Detalls");

        grndetaisl.setBackground(new java.awt.Color(255, 255, 255));
        grndetaisl.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        grndetaisl.setText("GRN Details");

        purchasereturn.setBackground(new java.awt.Color(255, 255, 255));
        purchasereturn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        purchasereturn.setText("Purchase Return");

        newimport.setBackground(new java.awt.Color(255, 255, 255));
        newimport.setText("Import/Export");

        importview.setBackground(new java.awt.Color(255, 255, 255));
        importview.setText("Import View");

        exportview.setBackground(new java.awt.Color(255, 255, 255));
        exportview.setText("Export View");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(purchase)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(purchasedetails)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(grndetaisl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(purchasereturn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newimport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(importview)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportview))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(purchase)
                .addComponent(purchasedetails)
                .addComponent(grndetaisl)
                .addComponent(purchasereturn)
                .addComponent(newimport)
                .addComponent(importview)
                .addComponent(exportview))
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Category")));

        itemcategory.setBackground(new java.awt.Color(255, 255, 255));
        itemcategory.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        itemcategory.setText("Item Category");

        unitmaangment.setBackground(new java.awt.Color(255, 255, 255));
        unitmaangment.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        unitmaangment.setText("Unit Management");

        expensesstype.setBackground(new java.awt.Color(255, 255, 255));
        expensesstype.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        expensesstype.setText("Expencess Type");

        countrylist.setBackground(new java.awt.Color(255, 255, 255));
        countrylist.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        countrylist.setText("Country List");

        companyinfo.setBackground(new java.awt.Color(255, 255, 255));
        companyinfo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        companyinfo.setText("Company Info");

        customerdocument.setBackground(new java.awt.Color(255, 255, 255));
        customerdocument.setText("Customer Document");

        companydocument.setBackground(new java.awt.Color(255, 255, 255));
        companydocument.setText("Company Document");

        supplierdocument.setBackground(new java.awt.Color(255, 255, 255));
        supplierdocument.setText("Suppliyer Document");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(itemcategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(unitmaangment)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(expensesstype)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(countrylist)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(companyinfo)
                .addGap(1, 1, 1)
                .addComponent(companydocument)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(supplierdocument)
                .addGap(1, 1, 1)
                .addComponent(customerdocument)
                .addGap(41, 41, 41))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemcategory)
                    .addComponent(unitmaangment)
                    .addComponent(expensesstype)
                    .addComponent(countrylist)
                    .addComponent(companyinfo)
                    .addComponent(customerdocument)
                    .addComponent(companydocument)
                    .addComponent(supplierdocument)))
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Riport"));

        itemreport.setBackground(new java.awt.Color(255, 255, 255));
        itemreport.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        itemreport.setText("Item Report");

        salereport.setBackground(new java.awt.Color(255, 255, 255));
        salereport.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        salereport.setText("Sale Report");

        cashboxreport.setBackground(new java.awt.Color(255, 255, 255));
        cashboxreport.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cashboxreport.setText("Cash Box Report");

        bankstatement.setBackground(new java.awt.Color(255, 255, 255));
        bankstatement.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bankstatement.setText("Bank Statement");

        grndeailsreport.setBackground(new java.awt.Color(255, 255, 255));
        grndeailsreport.setText("GRN Details");

        purchasereport.setBackground(new java.awt.Color(255, 255, 255));
        purchasereport.setText("Purchase Report");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(itemreport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(salereport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cashboxreport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bankstatement)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(grndeailsreport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(purchasereport))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemreport)
                    .addComponent(salereport)
                    .addComponent(cashboxreport)
                    .addComponent(bankstatement)
                    .addComponent(grndeailsreport)
                    .addComponent(purchasereport)))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Customer Info"));

        instancecheck.setBackground(new java.awt.Color(255, 255, 255));
        instancecheck.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        instancecheck.setText("Instance Check");

        customerinfo.setBackground(new java.awt.Color(255, 255, 255));
        customerinfo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        customerinfo.setText("Customer Info");

        creditstatement.setBackground(new java.awt.Color(255, 255, 255));
        creditstatement.setText("Credit Statement");

        cashstatement.setBackground(new java.awt.Color(255, 255, 255));
        cashstatement.setText("Cash Statement");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(instancecheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(customerinfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cashstatement)
                .addGap(1, 1, 1)
                .addComponent(creditstatement)
                .addGap(1, 1, 1))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(instancecheck)
                .addComponent(customerinfo)
                .addComponent(creditstatement)
                .addComponent(cashstatement))
        );

        checkall.setBackground(new java.awt.Color(255, 255, 255));
        checkall.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        checkall.setText("Check All");
        checkall.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkallMouseClicked(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Item Master"));

        itemlist.setBackground(new java.awt.Color(255, 255, 255));
        itemlist.setText("Item List");

        stock.setBackground(new java.awt.Color(255, 255, 255));
        stock.setText("Stock");

        wastage.setBackground(new java.awt.Color(255, 255, 255));
        wastage.setText("Wastage");

        labelprint.setBackground(new java.awt.Color(255, 255, 255));
        labelprint.setText("Label Print");

        priceupdate.setBackground(new java.awt.Color(255, 255, 255));
        priceupdate.setText("Price Update");

        stockdemand.setBackground(new java.awt.Color(255, 255, 255));
        stockdemand.setText("Stock Demand");

        offerlist.setBackground(new java.awt.Color(255, 255, 255));
        offerlist.setText("Offer List");

        itemforcast.setBackground(new java.awt.Color(255, 255, 255));
        itemforcast.setText("Item Forcast");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(itemlist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(wastage)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelprint)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(priceupdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(stockdemand))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(offerlist)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(itemforcast)))
                .addGap(1, 1, 1))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemlist)
                    .addComponent(stock)
                    .addComponent(wastage)
                    .addComponent(labelprint)
                    .addComponent(priceupdate)
                    .addComponent(stockdemand))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(offerlist)
                    .addComponent(itemforcast)))
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Payment"));

        purchasepayment.setBackground(new java.awt.Color(255, 255, 255));
        purchasepayment.setText("Purchase Payment");

        chequepayemnt.setBackground(new java.awt.Color(255, 255, 255));
        chequepayemnt.setText("Credit Payment");

        expenscc.setBackground(new java.awt.Color(255, 255, 255));
        expenscc.setText("Expensess");

        importpayment.setBackground(new java.awt.Color(255, 255, 255));
        importpayment.setText("Import Export Payment/Recieve");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(purchasepayment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chequepayemnt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(expenscc))
                    .addComponent(importpayment))
                .addContainerGap(187, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(purchasepayment)
                    .addComponent(chequepayemnt)
                    .addComponent(expenscc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(importpayment)
                .addGap(0, 0, 0))
        );

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "Suppliyer Info"));

        supplyerinfo.setBackground(new java.awt.Color(255, 255, 255));
        supplyerinfo.setText("Supplier List");

        supplyerstaement.setBackground(new java.awt.Color(255, 255, 255));
        supplyerstaement.setText("Supply Statement");

        suppliertarif.setBackground(new java.awt.Color(255, 255, 255));
        suppliertarif.setText("Supplier Tarif");

        supplierpurchase.setBackground(new java.awt.Color(255, 255, 255));
        supplierpurchase.setText("Supplyier Purchase List");

        supplierpayment.setBackground(new java.awt.Color(255, 255, 255));
        supplierpayment.setText("Supplier Payment List");

        supplierforcast.setBackground(new java.awt.Color(255, 255, 255));
        supplierforcast.setText("Supplier Forcast");

        supplierimport.setBackground(new java.awt.Color(255, 255, 255));
        supplierimport.setText("Import/Export Supplier");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(supplyerinfo)
                        .addGap(1, 1, 1)
                        .addComponent(supplyerstaement)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(suppliertarif))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(supplierpurchase)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supplierpayment))
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(supplierforcast)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supplierimport))))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplyerinfo)
                    .addComponent(supplyerstaement)
                    .addComponent(suppliertarif))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierpurchase)
                    .addComponent(supplierpayment))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierforcast)
                    .addComponent(supplierimport))
                .addContainerGap())
        );

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)), "System")));

        databasebackup.setBackground(new java.awt.Color(255, 255, 255));
        databasebackup.setText("Database Backup");

        databaserestore.setBackground(new java.awt.Color(255, 255, 255));
        databaserestore.setText("Database Restore");

        databaseinfo.setBackground(new java.awt.Color(255, 255, 255));
        databaseinfo.setText("Database Information");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(databasebackup)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(databaserestore, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(databaseinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(databasebackup)
                    .addComponent(databaserestore)
                    .addComponent(databaseinfo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel17.setBackground(new java.awt.Color(255, 255, 255));
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Employee Management"));

        sallerymanagment.setBackground(new java.awt.Color(255, 255, 255));
        sallerymanagment.setText("Sallery Management");

        employeelist.setBackground(new java.awt.Color(255, 255, 255));
        employeelist.setText("Employee List");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(employeelist)
                    .addComponent(sallerymanagment, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(employeelist)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sallerymanagment)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, 0)
                        .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(checkall, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(5, 5, 5))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(checkall)
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
            .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jScrollPane2.setViewportView(jPanel3);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(statuscheck, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(employee_id, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(23, 23, 23)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(usernametext, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nametext, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(employee_id, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(nametext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel4))
                            .addComponent(usernametext, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1)
                        .addComponent(statuscheck)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE))
                .addGap(112, 112, 112))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1268, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setBounds(7, 5, 1365, 566);
    }// </editor-fold>//GEN-END:initComponents

    private void checkallMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkallMouseClicked
        if (checkall.isSelected()) {
            checkall();

        } else {
            checknull();
        }
    }//GEN-LAST:event_checkallMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (employee_id.getText().isEmpty()) {
            insertdata();

        } else {
            userupdate();

        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        clear();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int p = JOptionPane.showConfirmDialog(null, "Do you really want to delete Data", "Delete", JOptionPane.YES_NO_OPTION);
        if (p == 0) {
            try {
                String sql = "Delete from admin where id=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, employee_id.getText());

                pst.execute();
                JOptionPane.showMessageDialog(null, "Data Deleted");
                //table_update();

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            try {
                String sql = "Delete from loginaccess where UserId=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, employee_id.getText());

                pst.execute();
                /// JOptionPane.showMessageDialog(null, "Data Deleted");
                //table_update();

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }
            try {
                String sql = "Delete from modificationaccsess where userid=?";
                pst = conn.prepareStatement(sql);

                pst.setString(1, employee_id.getText());

                pst.execute();
                //  JOptionPane.showMessageDialog(null, "Data Deleted");
                //table_update();

            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e);

            }

            try {
                clear();
                table_update();
            } catch (SQLException ex) {
                Logger.getLogger(UserProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tbluserinfoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbluserinfoMouseClicked

        int row = tbluserinfo.getSelectedRow();
        String table_click = (tbluserinfo.getModel().getValueAt(row, 0).toString());

        try {

            String sql = "Select * from admin where id='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {

                String add1 = rs.getString("id");
                employee_id.setText(add1);
                String add2 = rs.getString("name");
                nametext.setText(add2);
                String add3 = rs.getString("username");
                usernametext.setText(add3);
                String add4 = rs.getString("password");
                password.setText(add4);
                int status = rs.getInt("status");
                if (status == 1) {

                    statuscheck.setSelected(true);
                } else {

                    statuscheck.setSelected(false);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

        try {

            String sql = "Select * from loginaccess where userid='" + table_click + "'";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {

                String checkname = rs.getString("Accessname");
                int access = rs.getInt("access");

                if (userpanel.getText() == null ? checkname == null : userpanel.getText().equals(checkname)) {

                    if (access == 1) {
                        userpanel.setSelected(true);
                    } else {
                        userpanel.setSelected(false);

                    }
                }

                if (System.getText() == null ? checkname == null : System.getText().equals(checkname)) {

                    if (access == 1) {
                        System.setSelected(true);
                    } else {
                        System.setSelected(false);

                    }
                }

                if (saledetials.getText() == null ? checkname == null : saledetials.getText().equals(checkname)) {

                    if (access == 1) {
                        saledetials.setSelected(true);
                    } else {
                        saledetials.setSelected(false);

                    }
                }
                if (cashinvoice.getText() == null ? checkname == null : cashinvoice.getText().equals(checkname)) {

                    if (access == 1) {
                        cashinvoice.setSelected(true);
                    } else {
                        cashinvoice.setSelected(false);

                    }
                }

                if (creditinvoice.getText() == null ? checkname == null : creditinvoice.getText().equals(checkname)) {

                    if (access == 1) {
                        creditinvoice.setSelected(true);
                    } else {
                        creditinvoice.setSelected(false);

                    }
                }

                if (saleretusrn.getText() == null ? checkname == null : creditinvoice.getText().equals(checkname)) {

                    if (access == 1) {
                        saleretusrn.setSelected(true);
                    } else {
                        saleretusrn.setSelected(false);

                    }
                }

                if (bankinfotext.getText() == null ? checkname == null : bankinfotext.getText().equals(checkname)) {

                    if (access == 1) {
                        bankinfotext.setSelected(true);
                    } else {
                        bankinfotext.setSelected(false);

                    }
                }

                if (bankaccount.getText() == null ? checkname == null : bankaccount.getText().equals(checkname)) {

                    if (access == 1) {
                        bankaccount.setSelected(true);
                    } else {
                        bankaccount.setSelected(false);

                    }
                }

                if (cashdrawer.getText() == null ? checkname == null : cashdrawer.getText().equals(checkname)) {

                    if (access == 1) {
                        cashdrawer.setSelected(true);
                    } else {
                        cashdrawer.setSelected(false);

                    }
                }

                if (dailypurchseandsale.getText() == null ? checkname == null : dailypurchseandsale.getText().equals(checkname)) {

                    if (access == 1) {
                        dailypurchseandsale.setSelected(true);
                    } else {
                        dailypurchseandsale.setSelected(false);

                    }
                }

                if (creditpayment.getText() == null ? checkname == null : creditpayment.getText().equals(checkname)) {

                    if (access == 1) {
                        creditpayment.setSelected(true);
                    } else {
                        creditpayment.setSelected(false);

                    }
                }

                if (vatcollecion.getText() == null ? checkname == null : vatcollecion.getText().equals(checkname)) {

                    if (access == 1) {
                        vatcollecion.setSelected(true);
                    } else {
                        vatcollecion.setSelected(false);

                    }
                }

                if (dayclose.getText() == null ? checkname == null : dayclose.getText().equals(checkname)) {

                    if (access == 1) {
                        dayclose.setSelected(true);
                    } else {
                        dayclose.setSelected(false);

                    }
                }

                if (income.getText() == null ? checkname == null : income.getText().equals(checkname)) {

                    if (access == 1) {
                        income.setSelected(true);
                    } else {
                        income.setSelected(false);

                    }
                }

                if (purchase.getText() == null ? checkname == null : purchase.getText().equals(checkname)) {

                    if (access == 1) {
                        purchase.setSelected(true);
                    } else {
                        purchase.setSelected(false);

                    }
                }

                if (purchasedetails.getText() == null ? checkname == null : purchasedetails.getText().equals(checkname)) {

                    if (access == 1) {
                        purchasedetails.setSelected(true);
                    } else {
                        purchasedetails.setSelected(false);

                    }
                }

                if (grndetaisl.getText() == null ? checkname == null : grndetaisl.getText().equals(checkname)) {

                    if (access == 1) {
                        grndetaisl.setSelected(true);
                    } else {
                        grndetaisl.setSelected(false);

                    }
                }

                if (purchasereturn.getText() == null ? checkname == null : purchasereturn.getText().equals(checkname)) {

                    if (access == 1) {
                        purchasereturn.setSelected(true);
                    } else {
                        purchasereturn.setSelected(false);

                    }

                }

                if (newimport.getText() == null ? checkname == null : newimport.getText().equals(checkname)) {

                    if (access == 1) {
                        newimport.setSelected(true);
                    } else {
                        newimport.setSelected(false);

                    }

                }

                if (importview.getText() == null ? checkname == null : importview.getText().equals(checkname)) {

                    if (access == 1) {
                        importview.setSelected(true);
                    } else {
                        importview.setSelected(false);

                    }

                }

                if (exportview.getText() == null ? checkname == null : exportview.getText().equals(checkname)) {

                    if (access == 1) {
                        exportview.setSelected(true);
                    } else {
                        exportview.setSelected(false);

                    }

                }

                if (itemlist.getText() == null ? checkname == null : itemlist.getText().equals(checkname)) {

                    if (access == 1) {
                        itemlist.setSelected(true);
                    } else {
                        itemlist.setSelected(false);

                    }

                }

                if (stock.getText() == null ? checkname == null : stock.getText().equals(checkname)) {

                    if (access == 1) {
                        stock.setSelected(true);
                    } else {
                        stock.setSelected(false);

                    }

                }

                if (wastage.getText() == null ? checkname == null : wastage.getText().equals(checkname)) {

                    if (access == 1) {
                        wastage.setSelected(true);
                    } else {
                        wastage.setSelected(false);

                    }

                }

                if (labelprint.getText() == null ? checkname == null : labelprint.getText().equals(checkname)) {

                    if (access == 1) {
                        labelprint.setSelected(true);
                    } else {
                        labelprint.setSelected(false);

                    }

                }

                if (purchasepayment.getText() == null ? checkname == null : purchasepayment.getText().equals(checkname)) {

                    if (access == 1) {
                        purchasepayment.setSelected(true);
                    } else {
                        purchasepayment.setSelected(false);

                    }

                }

                if (cashboxreport.getText() == null ? checkname == null : cashboxreport.getText().equals(checkname)) {

                    if (access == 1) {
                        cashboxreport.setSelected(true);
                    } else {
                        cashboxreport.setSelected(false);

                    }

                }

                if (chequepayemnt.getText() == null ? checkname == null : chequepayemnt.getText().equals(checkname)) {

                    if (access == 1) {
                        chequepayemnt.setSelected(true);
                    } else {
                        chequepayemnt.setSelected(false);

                    }

                }

                if (expenscc.getText() == null ? checkname == null : expenscc.getText().equals(checkname)) {

                    if (access == 1) {
                        expenscc.setSelected(true);
                    } else {
                        expenscc.setSelected(false);

                    }

                }

                if (itemcategory.getText() == null ? checkname == null : itemcategory.getText().equals(checkname)) {

                    if (access == 1) {
                        itemcategory.setSelected(true);
                    } else {
                        itemcategory.setSelected(false);

                    }

                }

                if (unitmaangment.getText() == null ? checkname == null : unitmaangment.getText().equals(checkname)) {

                    if (access == 1) {
                        unitmaangment.setSelected(true);
                    } else {
                        unitmaangment.setSelected(false);

                    }

                }
                if (expensesstype.getText() == null ? checkname == null : expensesstype.getText().equals(checkname)) {

                    if (access == 1) {
                        expensesstype.setSelected(true);
                    } else {
                        expensesstype.setSelected(false);

                    }

                }

                if (countrylist.getText() == null ? checkname == null : countrylist.getText().equals(checkname)) {

                    if (access == 1) {
                        countrylist.setSelected(true);
                    } else {
                        countrylist.setSelected(false);

                    }

                }

                if (companyinfo.getText() == null ? checkname == null : companyinfo.getText().equals(checkname)) {

                    if (access == 1) {
                        companyinfo.setSelected(true);
                    } else {
                        companyinfo.setSelected(false);

                    }

                }

                if (itemreport.getText() == null ? checkname == null : itemreport.getText().equals(checkname)) {

                    if (access == 1) {
                        itemreport.setSelected(true);
                    } else {
                        itemreport.setSelected(false);

                    }

                }

                if (salereport.getText() == null ? checkname == null : salereport.getText().equals(checkname)) {

                    if (access == 1) {
                        salereport.setSelected(true);
                    } else {
                        salereport.setSelected(false);

                    }

                }

                if (cashboxreport.getText() == null ? checkname == null : cashboxreport.getText().equals(checkname)) {

                    if (access == 1) {
                        cashboxreport.setSelected(true);
                    } else {
                        cashboxreport.setSelected(false);

                    }

                }

                if (bankstatement.getText() == null ? checkname == null : bankstatement.getText().equals(checkname)) {

                    if (access == 1) {
                        bankstatement.setSelected(true);
                    } else {
                        bankstatement.setSelected(false);

                    }

                }

                if (grndeailsreport.getText() == null ? checkname == null : grndeailsreport.getText().equals(checkname)) {

                    if (access == 1) {
                        grndeailsreport.setSelected(true);
                    } else {
                        grndeailsreport.setSelected(false);

                    }

                }
                if (purchasereport.getText() == null ? checkname == null : purchasereport.getText().equals(checkname)) {

                    if (access == 1) {
                        purchasereport.setSelected(true);
                    } else {
                        purchasereport.setSelected(false);

                    }

                }

                if (instancecheck.getText() == null ? checkname == null : instancecheck.getText().equals(checkname)) {

                    if (access == 1) {
                        instancecheck.setSelected(true);
                    } else {
                        instancecheck.setSelected(false);

                    }

                }

                if (customerinfo.getText() == null ? checkname == null : customerinfo.getText().equals(checkname)) {

                    if (access == 1) {
                        customerinfo.setSelected(true);
                    } else {
                        customerinfo.setSelected(false);

                    }

                }

                if (creditstatement.getText() == null ? checkname == null : creditstatement.getText().equals(checkname)) {

                    if (access == 1) {
                        creditstatement.setSelected(true);
                    } else {
                        creditstatement.setSelected(false);

                    }

                }

                if (priceupdate.getText() == null ? checkname == null : priceupdate.getText().equals(checkname)) {

                    if (access == 1) {
                        priceupdate.setSelected(true);
                    } else {
                        priceupdate.setSelected(false);

                    }

                }

                if (supplyerinfo.getText() == null ? checkname == null : supplyerinfo.getText().equals(checkname)) {

                    if (access == 1) {
                        supplyerinfo.setSelected(true);
                    } else {
                        supplyerinfo.setSelected(false);

                    }

                }

                if (supplyerstaement.getText() == null ? checkname == null : supplyerstaement.getText().equals(checkname)) {

                    if (access == 1) {
                        supplyerstaement.setSelected(true);
                    } else {
                        supplyerstaement.setSelected(false);

                    }

                }

                if (databasebackup.getText() == null ? checkname == null : databasebackup.getText().equals(checkname)) {

                    if (access == 1) {
                        databasebackup.setSelected(true);
                    } else {
                        databasebackup.setSelected(false);

                    }

                }

                if (databaserestore.getText() == null ? checkname == null : databaserestore.getText().equals(checkname)) {

                    if (access == 1) {
                        databaserestore.setSelected(true);
                    } else {
                        databaserestore.setSelected(false);

                    }

                }

                if (stockdemand.getText() == null ? checkname == null : stockdemand.getText().equals(checkname)) {

                    if (access == 1) {
                        stockdemand.setSelected(true);
                    } else {
                        stockdemand.setSelected(false);

                    }

                }
                if (offerlist.getText() == null ? checkname == null : offerlist.getText().equals(checkname)) {

                    if (access == 1) {
                        offerlist.setSelected(true);
                    } else {
                        offerlist.setSelected(false);

                    }

                }
                if (itemforcast.getText() == null ? checkname == null : itemforcast.getText().equals(checkname)) {

                    if (access == 1) {
                        itemforcast.setSelected(true);
                    } else {
                        itemforcast.setSelected(false);

                    }

                }
                if (suppliertarif.getText() == null ? checkname == null : suppliertarif.getText().equals(checkname)) {

                    if (access == 1) {
                        suppliertarif.setSelected(true);
                    } else {
                        suppliertarif.setSelected(false);

                    }

                }
                if (supplierpurchase.getText() == null ? checkname == null : supplierpurchase.getText().equals(checkname)) {

                    if (access == 1) {
                        supplierpurchase.setSelected(true);
                    } else {
                        supplierpurchase.setSelected(false);

                    }

                }

                if (supplierpayment.getText() == null ? checkname == null : supplierpayment.getText().equals(checkname)) {

                    if (access == 1) {
                        supplierpayment.setSelected(true);
                    } else {
                        supplierpayment.setSelected(false);

                    }

                }
                if (supplierforcast.getText() == null ? checkname == null : supplierforcast.getText().equals(checkname)) {

                    if (access == 1) {
                        supplierforcast.setSelected(true);
                    } else {
                        supplierforcast.setSelected(false);

                    }

                }
                if (supplierimport.getText() == null ? checkname == null : supplierimport.getText().equals(checkname)) {

                    if (access == 1) {
                        supplierimport.setSelected(true);
                    } else {
                        supplierimport.setSelected(false);

                    }

                }
                if (invoicecheck.getText() == null ? checkname == null : invoicecheck.getText().equals(checkname)) {

                    if (access == 1) {
                        invoicecheck.setSelected(true);
                    } else {
                        invoicecheck.setSelected(false);

                    }

                }
                if (importpayment.getText() == null ? checkname == null : importpayment.getText().equals(checkname)) {

                    if (access == 1) {
                        importpayment.setSelected(true);
                    } else {
                        importpayment.setSelected(false);

                    }

                }
                if (companydocument.getText() == null ? checkname == null : companydocument.getText().equals(checkname)) {

                    if (access == 1) {
                        companydocument.setSelected(true);
                    } else {
                        companydocument.setSelected(false);

                    }

                }
                if (supplierdocument.getText() == null ? checkname == null : supplierdocument.getText().equals(checkname)) {

                    if (access == 1) {
                        supplierdocument.setSelected(true);
                    } else {
                        supplierdocument.setSelected(false);

                    }

                }
                if (customerdocument.getText() == null ? checkname == null : customerdocument.getText().equals(checkname)) {

                    if (access == 1) {
                        customerdocument.setSelected(true);
                    } else {
                        customerdocument.setSelected(false);

                    }

                }

                if (databaseinfo.getText() == null ? checkname == null : databaseinfo.getText().equals(checkname)) {

                    if (access == 1) {
                        databaseinfo.setSelected(true);
                    } else {
                        databaseinfo.setSelected(false);

                    }

                }
                if (cashstatement.getText() == null ? checkname == null : cashstatement.getText().equals(checkname)) {

                    if (access == 1) {
                        cashstatement.setSelected(true);
                    } else {
                        cashstatement.setSelected(false);

                    }

                }

                if (employeelist.getText() == null ? checkname == null : employeelist.getText().equals(checkname)) {

                    if (access == 1) {
                        employeelist.setSelected(true);
                    } else {
                        employeelist.setSelected(false);

                    }

                }

                if (sallerymanagment.getText() == null ? checkname == null : sallerymanagment.getText().equals(checkname)) {

                    if (access == 1) {
                        sallerymanagment.setSelected(true);
                    } else {
                        sallerymanagment.setSelected(false);

                    }

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }//GEN-LAST:event_tbluserinfoMouseClicked

    private void cashinvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashinvoiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cashinvoiceActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox System;
    private javax.swing.JCheckBox bankaccount;
    private javax.swing.JCheckBox bankinfotext;
    private javax.swing.JCheckBox bankstatement;
    private javax.swing.JCheckBox cashboxreport;
    private javax.swing.JCheckBox cashdrawer;
    private javax.swing.JCheckBox cashinvoice;
    private javax.swing.JCheckBox cashstatement;
    private javax.swing.JCheckBox checkall;
    private javax.swing.JCheckBox chequepayemnt;
    private javax.swing.JCheckBox companydocument;
    private javax.swing.JCheckBox companyinfo;
    private javax.swing.JCheckBox countrylist;
    private javax.swing.JCheckBox creditinvoice;
    private javax.swing.JCheckBox creditpayment;
    private javax.swing.JCheckBox creditstatement;
    private javax.swing.JCheckBox customerdocument;
    private javax.swing.JCheckBox customerinfo;
    private javax.swing.JCheckBox dailypurchseandsale;
    private javax.swing.JCheckBox databasebackup;
    private javax.swing.JCheckBox databaseinfo;
    private javax.swing.JCheckBox databaserestore;
    private javax.swing.JCheckBox dayclose;
    private javax.swing.JLabel employee_id;
    private javax.swing.JCheckBox employeelist;
    private javax.swing.JCheckBox expenscc;
    private javax.swing.JCheckBox expensesstype;
    private javax.swing.JCheckBox exportview;
    private javax.swing.JCheckBox grndeailsreport;
    private javax.swing.JCheckBox grndetaisl;
    private javax.swing.JCheckBox importpayment;
    private javax.swing.JCheckBox importview;
    private javax.swing.JCheckBox income;
    private javax.swing.JCheckBox instancecheck;
    private javax.swing.JCheckBox invoicecheck;
    private javax.swing.JCheckBox itemcategory;
    private javax.swing.JCheckBox itemforcast;
    private javax.swing.JCheckBox itemlist;
    private javax.swing.JCheckBox itemreport;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JCheckBox labelprint;
    private javax.swing.JTextField nametext;
    private javax.swing.JCheckBox newimport;
    private javax.swing.JCheckBox offerlist;
    private javax.swing.JPasswordField password;
    private javax.swing.JCheckBox priceupdate;
    private javax.swing.JCheckBox purchase;
    private javax.swing.JCheckBox purchasedetails;
    private javax.swing.JCheckBox purchasepayment;
    private javax.swing.JCheckBox purchasereport;
    private javax.swing.JCheckBox purchasereturn;
    private javax.swing.JCheckBox saledetials;
    private javax.swing.JCheckBox salereport;
    private javax.swing.JCheckBox saleretusrn;
    private javax.swing.JCheckBox sallerymanagment;
    private javax.swing.JCheckBox statuscheck;
    private javax.swing.JCheckBox stock;
    private javax.swing.JCheckBox stockdemand;
    private javax.swing.JCheckBox supplierdocument;
    private javax.swing.JCheckBox supplierforcast;
    private javax.swing.JCheckBox supplierimport;
    private javax.swing.JCheckBox supplierpayment;
    private javax.swing.JCheckBox supplierpurchase;
    private javax.swing.JCheckBox suppliertarif;
    private javax.swing.JCheckBox supplyerinfo;
    private javax.swing.JCheckBox supplyerstaement;
    private javax.swing.JTable tbluserinfo;
    private javax.swing.JCheckBox unitmaangment;
    private javax.swing.JTextField usernametext;
    private javax.swing.JCheckBox userpanel;
    private javax.swing.JCheckBox vatcollecion;
    private javax.swing.JCheckBox wastage;
    // End of variables declaration//GEN-END:variables
}
