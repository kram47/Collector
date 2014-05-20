/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indexador;

/**
 * Represent the object Document in Database
 * @author 492403
 */
public class Document {
    
    /* ---------------------------------------------------------------- */
    /* ---------------------- PROPERTIES ------------------------------ */      
    
    /**
     * Document's id
     */
    private int         _id;
    
    /**
     * Document's name
     */
    private String      _name;
    
    /**
     * Document's URL
     */
    private String      _url;
    
    /**
     * Document's Title
     */
    private String      _title;
    
    /**
     * Document's content
     */
    private String      _content;
    
    /**
     * Document's r_sum -> part on the top of vectorial method function
     */
    private float       _r_sum;
    
    /**
     * Document's r_sum -> part on the bottom of vectorial method function
     */
    private float       _r_square;
    
    /**
     * Document's list of words
     */
    private String[]    _words;
    

    /* ---------------------------------------------------------------- */
    /* ---------------------- CONSTRUCTOR  ---------------------------- */     
    
    /**
     * Main Constructor 
     * @param name name of the document
     * @param words List of words
     */
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
     * @return the _r_sum
     */
    public float getR_sum() {
        return _r_sum;
    }

    /**
     * @param r_sum the _r_sum to set
     */
    public void setR_sum(float r_sum) {
        this._r_sum = r_sum;
    }

    /**
     * @return the _r_square
     */
    public float getR_square() {
        return _r_square;
    }

    /**
     * @param r_square the _r_square to set
     */
    public void setR_square(float r_square) {
        this._r_square = r_square;
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
    
    
    /**
     * @return the _id
     */
    public int getId() {
        return _id;
    }

    /**
     * @param id the _id to set
     */
    public void setId(int id) {
        this._id = id;
    }

    /* ---------------------------------------------------------------- */
    /* ------------------------ METHODS ------------------------------- */        



    
    
    
    
    
}