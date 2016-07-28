/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepadsql;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author pawel
 */
public class NotesController extends Session implements Initializable {

    @FXML private AnchorPane apLeft;
    @FXML private AnchorPane apCenter;
    @FXML private Label loginLabel;
    
    @FXML private void goCategories() {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loginLabel.setText(loginLabel.getText() + " " + getUserName());
    }    
    
}
