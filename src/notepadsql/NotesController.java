/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepadsql;

import java.beans.EventHandler;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author pawel
 */
public class NotesController extends Session implements Initializable {

    @FXML private AnchorPane apLeft;
    @FXML private AnchorPane apCenter;
    @FXML private Label loginLabel;
    @FXML private AnchorPane ap;
    @FXML private ScrollPane leftScroll;
    @FXML private ScrollPane rightScroll;
    
    @FXML private void addNote() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select count(categoriesid) as amount from categories where categories_usersid = " + getUserId();
            rs = SQLHelper.doSelect(sql, getHost(), conn, stmt);
            rs.next();
            if(rs.getInt("amount") > 0) {
                EditPopup.clear();
                Parent root = FXMLLoader.load(getClass().getResource("Note.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.initModality(Modality.WINDOW_MODAL);
                Stage thisStage = (Stage) ap.getScene().getWindow();
                stage.initOwner(thisStage);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.showAndWait();
                if(UIHelper.isSelected()) displayCategory();
            }
            else {
                Alert a = new Alert(AlertType.INFORMATION, "Aby możliwe było dodawanie notatek, muszą istnieć jakieś kategorie");
                a.showAndWait();
            }
        }
        catch(Exception e) {
            
        }
        finally {
            SQLHelper.close(conn, stmt, rs);
        }
    }
    
    @FXML private void addCategory() {
        try {
            EditPopup.clear();
            Parent root = FXMLLoader.load(getClass().getResource("Category.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            Stage thisStage = (Stage) ap.getScene().getWindow();
            stage.initOwner(thisStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            loadCategories();
        }
        catch(Exception e) {
            
        }
    }
    
    private void editCategory(ActionEvent e) {
        try {
            EditPopup.setEditId(UIHelper.getSelectedCategory());
            Parent root = FXMLLoader.load(getClass().getResource("Category.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            Stage thisStage = (Stage) ap.getScene().getWindow();
            stage.initOwner(thisStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            loadCategories();
            displayCategory();
        }
        catch(Exception ex) {
            
        }
    }
    
    private void clearCenter() {
        apCenter.getChildren().clear();
        Label apCenterHeader = new Label();
        apCenterHeader.setText("Nie wybrano żadnej kategorii");
        apCenterHeader.setFont(new Font("Impact", 18));
        apCenterHeader.setPadding(new Insets(10, 10, 10, 10));
        apCenterHeader.setTextFill(Color.web("#035679"));
        apCenter.getChildren().add(apCenterHeader);
    }
    
    private void deleteCategory(ActionEvent e) {
        Alert a = new Alert(AlertType.CONFIRMATION, "Czy na pewno usunąć kategorię wraz ze wszystkimi związanymi z nią notatkami?");
        Optional<ButtonType> result = a.showAndWait();
        if(result.get() == ButtonType.OK) {
            String sql = "delete from notes where notes_categoriesid = " + UIHelper.getSelectedCategory();
            SQLHelper.doInsert(sql, getHost());
            sql = "delete from categories where categoriesid = " + UIHelper.getSelectedCategory();
            SQLHelper.doInsert(sql, getHost());
            loadCategories();
            EditPopup.clear();
            UIHelper.deselectCategory();
            clearCenter();
        }
    }
    
    private void deleteNote(ActionEvent e) {
        Alert a = new Alert(AlertType.CONFIRMATION, "Czy na pewno usunąć notatkę?");
        Optional<ButtonType> result = a.showAndWait();
        if(result.get() == ButtonType.OK) {
            Integer noteId = ((NoteButton) e.getSource()).getNoteId();
            String sql = "delete from notes where notesid = " + noteId;
            SQLHelper.doInsert(sql, getHost());
            displayCategory();
        }
    }
    
    private void editNote(ActionEvent e) {
         try {
            EditPopup.setEditId(((NoteButton) e.getSource()).getNoteId());
            Parent root = FXMLLoader.load(getClass().getResource("Note.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            Stage thisStage = (Stage) ap.getScene().getWindow();
            stage.initOwner(thisStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            displayCategory();
        }
        catch(Exception ex) {
            
        }
    }
    
    private void displayNotes(Double layoutY) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "select * from notes where notes_categoriesid = " + UIHelper.getSelectedCategory() + " and notes_usersid = " + getUserId();
            rs = SQLHelper.doSelect(sql, getHost(), conn, stmt);
            VBox vb = new VBox(0);
            vb.setLayoutY(layoutY);
            vb.setLayoutX(10);
            while(rs.next()) {
                HBox hb = new HBox(10);
                hb.setPadding(new Insets(10, 10, 10, 10));
                hb.setStyle("-fx-background-color: #035679;");
                Label noteTitle = new Label(rs.getString("title"));
                noteTitle.setFont(new Font("Impact", 18));
                noteTitle.setTextFill(Color.web("#FFFFFF"));
                noteTitle.setStyle("-fx-background-color: #035679;");
                hb.getChildren().add(noteTitle);
                
                NoteButton noteEdit = new NoteButton("Edytuj notatkę", rs.getInt("notesid"));
                noteEdit.setOnAction(this::editNote);
                hb.getChildren().add(noteEdit);
                NoteButton noteDelete = new NoteButton("Usuń notatkę", rs.getInt("notesid"));
                noteDelete.setOnAction(this::deleteNote);
                hb.getChildren().add(noteDelete);
                
                vb.getChildren().add(hb);
                
                Label noteContent = new Label(rs.getString("content"));
                noteContent.setMaxWidth(600.0);
                noteContent.setWrapText(true);
                noteContent.setFont(new Font("System", 14));
                noteContent.setPadding(new Insets(10, 10, 10, 10));
                noteContent.setTextFill(Color.web("#FFFFFF"));
                noteContent.setStyle("-fx-background-color: #035679;");
                noteContent.setMinWidth(600.0);
                vb.getChildren().add(noteContent);
                
                AnchorPane separator = new AnchorPane();
                separator.setMinHeight(20.0);
                vb.getChildren().add(separator);
            }
            apCenter.getChildren().add(vb);
            rightScroll.setContent(apCenter);
        }
        catch(Exception e) {
        }
        finally {
            SQLHelper.close(conn, stmt, rs);
        }
    }
    
    private void displayCategory() {
        apCenter.getChildren().clear();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select name, description from categories where categoriesid = " + UIHelper.getSelectedCategory();
        rs = SQLHelper.doSelect(sql, getHost(), conn, stmt);
        try {
            rs.next();
            Label apCenterHeader = new Label();
            apCenterHeader.setText("Notatki w kategorii " + rs.getString("name"));
            apCenterHeader.setFont(new Font("Impact", 18));
            apCenterHeader.setPadding(new Insets(10, 10, 10, 10));
            apCenterHeader.setTextFill(Color.web("#035679"));
            apCenter.getChildren().add(apCenterHeader);
            
            Label apCenterDesc = new Label();
            if(rs.getString("description").length() > 0) apCenterDesc.setText("(" + rs.getString("description") + ")");
            apCenterDesc.setFont(new Font("System", 12));
            apCenterDesc.setPadding(new Insets(10, 10, 10, 10));
            apCenterDesc.setTextFill(Color.web("#035679"));
            apCenterDesc.setLayoutY(20);
            apCenter.getChildren().add(apCenterDesc);
            
            Double layoutY = 35.0;
            if(rs.getString("description").length() > 0) layoutY = 50.0;
            
            Button catEditButton = new Button("Edytuj kategorię");
            catEditButton.setLayoutX(10);
            catEditButton.setLayoutY(layoutY);
            catEditButton.setOnAction(this::editCategory);
            apCenter.getChildren().add(catEditButton);
            
            Button catDelButton = new Button("Usuń kategorię");
            catDelButton.setLayoutX(120);
            catDelButton.setLayoutY(layoutY);
            catDelButton.setOnAction(this::deleteCategory);
            apCenter.getChildren().add(catDelButton);
            
            displayNotes(layoutY + 40.0);
        }
        catch(Exception ex) {
        }
        finally {
            SQLHelper.close(conn, stmt, rs);
        }
    }
    
    private void selectCategory(ActionEvent e) {
        Integer catId = ((CategoryButton) e.getSource()).getCatId();
        UIHelper.selectCategory(catId);
        displayCategory();
    }
    
    private void loadCategories() {
        apLeft.getChildren().clear();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "select * from categories where categories_usersid = " + getUserId() + " order by name";
        rs = SQLHelper.doSelect(sql, getHost(), conn, stmt);
        Integer y = 0;
        try {
            while(rs.next()) {
                CategoryButton b = new CategoryButton(rs.getInt("categoriesid"));
                b.setText(rs.getString("name"));
                b.setLayoutX(0);
                b.setLayoutY(y);
                y += 30;
                b.setMinWidth(228);
                b.setOnAction(this::selectCategory);
                apLeft.getChildren().add(b);
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
        rightScroll.setFitToWidth(true);
        loginLabel.setText(loginLabel.getText() + " " + getUserName());
        loadCategories();
        clearCenter();
    }    
    
}