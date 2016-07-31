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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static notepadsql.EditPopup.getEdit;

/**
 * FXML Controller class
 *
 * @author pawel
 */
public class NoteController extends EditPopup implements Initializable {
    @FXML Label header;
    @FXML TextField title;
    @FXML TextArea description;
    @FXML AnchorPane ap;
    @FXML ComboBox cb;
    
    @FXML private void cancelGodDammit() {
        Stage thisStage = (Stage) ap.getScene().getWindow();
        thisStage.close();
    }
    
    @FXML private void saveChanges() { 
        if(title.getText().length() == 0) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Należy wprowadzić tytuł notatki");
            a.showAndWait();
        }
        else if(description.getText().length() == 0) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Należy wprowadzić treść notatki");
            a.showAndWait();
        }
        else {
            String categoryName = (String) cb.getValue();
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select categoriesid from categories where name = '" + categoryName + "' and categories_usersid = " + getUserId();
                rs = SQLHelper.doSelect(sql, getHost(), conn, stmt);
                rs.next();
                Integer categoryId = rs.getInt("categoriesid");
                if(getEdit()) {
                    sql = "update notes set title = '" + title.getText() + "', content = '" + description.getText() + "', notes_categoriesid = " + categoryId + " where notesid = " + getEditId();
                    SQLHelper.doInsert(sql, getHost());
                }
                else {
                    sql = "insert into notes(title, content, notes_categoriesid, notes_usersid) values('" + title.getText() + "', '" + description.getText() + "', " + categoryId + ", " + getUserId() + ")";
                    SQLHelper.doInsert(sql, getHost());
                }
            }
            catch(Exception e) {
            }
            finally {
                SQLHelper.close(conn, stmt, rs);
                Stage thisStage = (Stage) ap.getScene().getWindow();
                thisStage.close();
            }
        }
    }
    
    private void setCbItems() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select name from categories where categories_usersid = " + getUserId() + " order by name";
            rs = SQLHelper.doSelect(sql, getHost(), conn, stmt);
            rs.next();
            cb.getItems().add(rs.getString("name"));
            cb.setValue(rs.getString("name"));
            while(rs.next()) {
                cb.getItems().add(rs.getString("name"));
            }
        }
        catch(Exception e) {
        }
        finally {
            SQLHelper.close(conn, stmt, rs);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setCbItems();
        if(getEdit()) {
            header.setText("edycja notatki");
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                String sql = "select title, content, name from notes inner join categories on notes_categoriesid = categoriesid where notesid = " + getEditId();
                rs = SQLHelper.doSelect(sql, getHost(), conn, stmt);
                rs.next();
                title.setText(rs.getString("title"));
                description.setText(rs.getString("content"));
                cb.setValue(rs.getString("name"));
            }
            catch(Exception e) {
            }
            finally {
                SQLHelper.close(conn, stmt, rs);
            }
        }
    }    
    
}
