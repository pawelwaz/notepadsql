/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepadsql;

/**
 *
 * @author pawel
 */
public class EditPopup extends Session {
    private static Integer editId;
    private static boolean edit;
    
    public static boolean getEdit() {
        return edit;
    }
    
    public static Integer getEditId() {
        return editId;
    }
    
    public static void setEditId(Integer newId) {
        editId = newId;
        edit = true;
    }
    
    public static void clear() {
        edit = false;
    }
}
