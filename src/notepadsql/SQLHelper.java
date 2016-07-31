/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepadsql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 *
 * @author pawel
 */
public class SQLHelper {
    
    public static ResultSet doSelect(String sql, String host, Connection conn, Statement stmt) {
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://" + host +"/notepadsql" + "?useUnicode=true&characterEncoding=utf-8", "notepadsql", "notepadsql");
            stmt = conn.createStatement();
            stmt.execute("SET NAMES 'utf8'");
            rs = stmt.executeQuery(sql);
        }
        catch(Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Problem z połączeniem do serwera!");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
        return rs;
    }
    
    public static void doInsert(String sql, String host) {
        Connection conn = null;
        Statement stmt = null;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://" + host +"/notepadsql" + "?useUnicode=true&characterEncoding=utf-8", "notepadsql", "notepadsql");
            stmt = conn.createStatement();
            stmt.execute("SET NAMES utf8");
            stmt.executeUpdate(sql);
        }
        catch(Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Problem z połączeniem do serwera!");
            a.setContentText(e.getMessage());
            a.showAndWait();
        }
        finally {
            try {
                if(stmt != null) stmt.close();
                if(conn != null) conn.close();
            }
            catch(Exception e) {
                
            }
        }
    }
    
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if(rs != null) {
                rs.close();
                rs = null;
            }
            if(stmt != null) {
                stmt.close();
                stmt = null;
            }
            if(conn != null) {
                conn.close();
                conn = null;
            }
        }
        catch(Exception e) {
            Alert a = new Alert(AlertType.ERROR, "Błąd przy zamykaniu połączenia z bazą!");
            a.showAndWait();
        }
    }
    
}
