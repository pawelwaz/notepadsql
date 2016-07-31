/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepadsql;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author A20111
 */
public class RegisterController implements Initializable {
    @FXML private AnchorPane ap;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField repeatPassword;
    @FXML private TextField hostField;

    @FXML public void goLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            Stage thisStage = (Stage) ap.getScene().getWindow();
            thisStage.close();
        }
        catch(Exception e) {
            
        }
    }
    
    @FXML public void registerUser() {
        if(loginField.getText().length() == 0 || passwordField.getText().length() == 0 || repeatPassword.getText().length() == 0 || hostField.getText().length() == 0) {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("Wszystkie pola muszą zostać wypełnione!");
            a.showAndWait();
        }
        else if(!passwordField.getText().equals(repeatPassword.getText())) {
            Alert a = new Alert(AlertType.ERROR, "Podane hasła różnią się!");
            a.showAndWait();
        }
        else {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            String sql = "select usersid from users where login = '" + loginField.getText() + "';";
            rs = SQLHelper.doSelect(sql, hostField.getText(), conn, stmt);
            try {
                if(rs.next()) {
                    Alert a = new Alert(AlertType.ERROR, "Login jest już zajęty!");
                    a.showAndWait();
                }
                else {
                    sql = "insert into users(login, password) values('" + loginField.getText() + "', md5('" + passwordField.getText() + "'));";
                    SQLHelper.doInsert(sql, hostField.getText());
                    sql = "select usersid from users where login = '" + loginField.getText() + "';";
                    rs = SQLHelper.doSelect(sql, hostField.getText(), conn, stmt);
                    int userid = 0;
                    if(rs.next()) userid = rs.getInt("usersid");
                    sql = "insert into categories(name, description, categories_usersid) values('dom', 'notatki związane ze sprawami domowymi', " + userid + ");";
                    SQLHelper.doInsert(sql, hostField.getText());
                    sql = "insert into categories(name, description, categories_usersid) values('praca', 'notatki związane ze sprawami zawodowymi', " + userid + ");";
                    SQLHelper.doInsert(sql, hostField.getText());
                    sql = "insert into categories(name, description, categories_usersid) values('urodziny', 'daty urodzin', " + userid + ");";
                    SQLHelper.doInsert(sql, hostField.getText());
                    sql = "insert into categories(name, description, categories_usersid) values('samochód', 'notatki związane z samochodem', " + userid + ");";
                    SQLHelper.doInsert(sql, hostField.getText());
                    Alert a = new Alert(AlertType.INFORMATION, "Użytkownik został zarejestrowany");
                    a.showAndWait();
                    goLogin();
                }
            }
            catch(Exception e) {
            }
            SQLHelper.close(conn, stmt, rs);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
}
