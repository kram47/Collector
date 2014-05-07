/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indexador;

/**
 *
 * @author 492403
 */
public class Document {
    
    String      _name;
    String[]    _words;
    
    public Document (String name, String[] words)
    {
        this._name = name;
        this._words = words;
    }
    
    public String getName() {
        return this._name;
    }
    public void setName(String name) {
        this._name = name;
    }
    
    public String[] getWords() {
        return this._words;
    }
    public void setWords(String[] words) {
       this._words = words;
    }
}
