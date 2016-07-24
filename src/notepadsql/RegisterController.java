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
        else {
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try{
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/notepadsql", "notepadsql", "notepadsql");
                stmt = conn.createStatement();
                String sql = "select usersid from users where login = '" + loginField.getText() + "' and password = md5('" + passwordField.getText() + "')";
                rs = stmt.executeQuery(sql);
               
            }
            catch(Exception e) {
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("Problem z połączeniem do serwera!");
                a.showAndWait();
            }
            finally {
                try {
                    if(rs != null) rs.close();
                    if(stmt != null) stmt.close();
                    if(conn != null) conn.close();
                }
                catch(Exception e) {
                    
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
}
