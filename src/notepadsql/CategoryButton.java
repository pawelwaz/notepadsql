/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepadsql;

import javafx.scene.control.Button;

/**
 *
 * @author pawel
 */
public class CategoryButton extends Button {
    private Integer catId;
    
    public CategoryButton(Integer newId) {
        super();
        catId = newId;
    }
    
    public Integer getCatId() {
        return catId;
    }
}
