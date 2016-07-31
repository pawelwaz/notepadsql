/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepadsql;

import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.*;
import javafx.stage.Modality;

/**
 *
 * @author A20111
 */
public class LoginController extends Session implements Initializable {
    @FXML public AnchorPane ap;
    @FXML public TextField hostField;
    @FXML public TextField loginField;
    @FXML public PasswordField passwordField;
    
    @FXML
    private void logUser() {
        if(loginField.getText().length() == 0 || passwordField.getText().length() == 0 || hostField.getText().length() == 0) {
            Alert a = new Alert(AlertType.ERROR);
            a.setContentText("Wszystkie dane logowania muszą zostać podane!");
            a.showAndWait();
        }
        else {
            String sql = "select usersid from users where login = '" + loginField.getText() + "' and password = md5('" + passwordField.getText() + "')";
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = SQLHelper.doSelect(sql, hostField.getText(), conn, stmt);
            
            try {
                if(rs.next()) {
                    setUserName(loginField.getText());
                    setUserId(rs.getInt("usersid"));
                    setHost(hostField.getText());
                    SQLHelper.close(conn, stmt, rs);
                    goNotes();
                }
                else {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Błędne dane logowania!");
                    a.showAndWait();
                }
            }
            catch(Exception e) {
            }
            finally {
                SQLHelper.close(conn, stmt, rs);
            }
        }
    }
    
    private void goNotes() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Notes.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            Stage thisStage = (Stage) ap.getScene().getWindow();
            thisStage.close();
        }
        catch(Exception e) {
            
        }
    }
    
    @FXML
    private void goRegister() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
            Stage thisStage = (Stage) ap.getScene().getWindow();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.showAndWait();
            thisStage.close();
        }
        catch(Exception e) {
            
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
