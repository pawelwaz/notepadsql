/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepadsql;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author pawel
 */
public class CategoryController extends EditPopup implements Initializable {
    @FXML Label header;
    @FXML TextField title;
    @FXML TextArea description;
    @FXML AnchorPane ap;
    
    @FXML private void cancelGodDammit() {
        Stage thisStage = (Stage) ap.getScene().getWindow();
        thisStage.close();
    }
    
    @FXML private void saveChanges() {
        if(title.getText().length() == 0) {
            Alert a = new Alert(AlertType.ERROR, "Należy wprowadzić nazwę kategorii!");
            a.showAndWait();
        }
        else {
            String sql = "select count(categoriesid) as amount from categories where name = '" + title.getText() + "' and categories_usersid = " + getUserId();
            boolean exists = false;
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                rs = SQLHelper.doSelect(sql, getHost(), conn, stmt);
                rs.next();
                if(rs.getInt("amount") > 0) exists = true;
            }
            catch(Exception e) {
            }
            finally {
                SQLHelper.close(conn, stmt, rs);
            }
            
            if(getEdit()) {
                sql = "update categories set name = '" + title.getText() + "', description = '" + description.getText() + "' where categoriesid = " + UIHelper.getSelectedCategory();
                SQLHelper.doInsert(sql, getHost());
                Stage thisStage = (Stage) ap.getScene().getWindow();
                thisStage.close();
            }
            else if(!exists) {
                sql = "insert into categories(name, description, categories_usersid) values('" + title.getText() + "', '" + description.getText() + "', " + getUserId() + ");";
                SQLHelper.doInsert(sql, getHost());
                Stage thisStage = (Stage) ap.getScene().getWindow();
                thisStage.close();
            }
            else {
                Alert a = new Alert(AlertType.INFORMATION, "Kategoria o takiej nazwie już istnieje");
                a.showAndWait();
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(getEdit()) {
            header.setText("edytuj kategorię");
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            String sql = "select * from categories where categoriesid = " + EditPopup.getEditId();
            rs = SQLHelper.doSelect(sql, getHost(), conn, stmt);
            try {
                rs.next();
                title.setText(rs.getString("name"));
                description.setText(rs.getString("description"));
            }
            catch(Exception e) {
            }
            finally {
                SQLHelper.close(conn, stmt, rs);
            }
        }
        else {
            header.setText("nowa kategoria");
        }
    }    
    
}
