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

/**
 *
 * @author pawel
 */
public class SQLHelper {
    public static ResultSet doSelect(String sql, String host) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:mysql://" + host +"/notepadsql", "notepadsql", "notepadsql");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
        }
        catch(Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setContentText("Problem z połączeniem do serwera!");
            a.setContentText(e.getMessage());
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
        return rs;
    }
}
