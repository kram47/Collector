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
    
    /* ---------------------------------------------------------------- */
    /* ---------------------- PROPERTIES ------------------------------ */      
    
    private String      _name;
    private String      _url;
    private String      _title;
    private String      _content;
    private float       _r;
    private String[]    _words;
    

    /* ---------------------------------------------------------------- */
    /* ---------------------- CONSTRUCTOR  ---------------------------- */     
    
    public Document (String name, String[] words)
    {
        this._name = name;
        this._words = words;
    }
    
    
    /* ---------------------------------------------------------------- */
    /* --------------------- GETTER-SETTER ---------------------------- */       

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name the _name to set
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * @return the _url
     */
    public String getUrl() {
        return _url;
    }

    /**
     * @param url the _url to set
     */
    public void setUrl(String url) {
        this._url = url;
    }

    /**
     * @return the _title
     */
    public String getTitle() {
        return _title;
    }

    /**
     * @param title the _title to set
     */
    public void setTitle(String title) {
        this._title = title;
    }

    /**
     * @return the _content
     */
    public String getContent() {
        return _content;
    }

    /**
     * @param content the _content to set
     */
    public void setContent(String content) {
        this._content = content;
    }
    
    /**
     * @return the _r
     */
    public float getR() {
        return _r;
    }

    /**
     * @param r the _r to set
     */
    public void setR(float r) {
        this._r = r;
    }

    /**
     * @return the _words
     */
    public String[] getWords() {
        return _words;
    }

    /**
     * @param words the _words to set
     */
    public void setWords(String[] words) {
        this._words = words;
    }
    

    /* ---------------------------------------------------------------- */
    /* ------------------------ METHODS ------------------------------- */        
    
    
    
    
    
}