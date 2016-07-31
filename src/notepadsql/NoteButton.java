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
public class NoteButton extends Button {
    private Integer noteId;
    
    public NoteButton(String text, Integer newId) {
        super(text);
        noteId = newId;
    }
    
    public Integer getNoteId() {
        return noteId;
    }
}
