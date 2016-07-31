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
public class UIHelper {
    private static boolean categorySelected;
    private static Integer categoryId;
    
    public static void selectCategory(Integer id) {
        categoryId = id;
        categorySelected = true;
    }
    
    public static Integer getSelectedCategory() {
        return categoryId;
    }
    
    public static void deselectCategory() {
        categorySelected = false;
    }
    
    public static boolean isSelected() {
        return categorySelected;
    }
}
