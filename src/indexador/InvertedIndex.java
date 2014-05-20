/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indexador;

import collector.IDbManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.Tools;

/**
 * Content and Method of the inverted index
 * @author 492403
 */
public class InvertedIndex {

    /* ---------------------------------------------------------------- */
    /* ---------------------- PROPERTIES ------------------------------ */
    
    /**
     * Double hashtable with word, document, frequency
     */
    Hashtable<String, Hashtable<String, Integer>>   _invertedIndex;
    
    /**
     * Exception of words to not put in the database
     */
    List<String>                                    _wordsExceptions;

    
    /* ---------------------------------------------------------------- */
    /* ---------------------- CONSTRUCTOR  ---------------------------- */      
            
    /**
     * Main constructor
     */
    public InvertedIndex()
    {
        _invertedIndex = new Hashtable<String, Hashtable<String, Integer>>();
        _wordsExceptions = new ArrayList<String>();
    }
    
    
    /* ---------------------------------------------------------------- */
    /* ------------------------ METHODS ------------------------------- */         
    
    /**
     * Get the frequency of the given pair
     * @param word_str the word of the pair
     * @param doc_str the document of the pair
     * @return the frequency of the pair
     */
    public int getFrequency(String word_str, String doc_str)
    {
        int freq = -1;
        
        if (this._invertedIndex.containsKey(word_str))
            if (this._invertedIndex.get(word_str).containsKey(doc_str))
                freq = this._invertedIndex.get(word_str).get(doc_str);
        
        return freq;
    }
    
    /**
     * Add an appearance in the pair (increment the frequency)
     * @param word_str the word of the pair
     * @param doc_str the document of the pair
     * @return 0
     */
    public int addAppearance(String word_str, String doc_str)
    {
        Hashtable<String, Integer> docs = null;
        int frequency;
        
        if (this._invertedIndex.containsKey(word_str))
        {
            docs = _invertedIndex.get(word_str);
            if (docs.containsKey(doc_str))
                frequency = docs.get(doc_str) + 1;
            else
                frequency = 1;
        }
        else
        {
            docs = new Hashtable<String, Integer>();
            frequency = 1;
        }
        
        docs.put(doc_str, frequency);
        this._invertedIndex.put(word_str, docs);
        
        return 0;
    }
    
    
    /**
     * Save the words of the index in database
     * @param db the Database manager
     * @throws SQLException 
     */
    public void persistWords(IDbManager db) throws SQLException
    {
        ResultSet rs = db.execute("SELECT word_value FROM  `words`");
        while (rs.next())
        {
            String current_word = rs.getString("word_value");
            if (_invertedIndex.containsKey(current_word));
                this._wordsExceptions.add(current_word);                        // If the word already exists in index, we add exception to not add it in the Words table.
        }       
        
        Enumeration<String> words = _invertedIndex.keys();
        String current_word;
        String query;
        
        while (words.hasMoreElements())
        {
            current_word = words.nextElement();
            if (!this._wordsExceptions.contains(current_word))
                db.executeUpdate("INSERT INTO words(word_value) VALUES ('" + current_word + "');");
        }
    }
    
    
    /**
     * Save the pairs(word, document) of the index in database
     * @param db the Database manager
     * @throws SQLException 
     */   
    public void persistPairs(IDbManager db, Document doc) throws SQLException 
    {
        Enumeration<String> words = _invertedIndex.keys();
        
        while (words.hasMoreElements())
        {
            int word_id = -1;
            String word = words.nextElement();
            String q = "SELECT word_id FROM `words` WHERE word_value= '"+ word + "'";
            ResultSet rs = db.execute(q);
            if (rs.next())
                 word_id = rs.getInt("word_id");
            if (word_id != -1)
            {
                int doc_id = doc.getId();
                int pair_frequency = getFrequency(word, doc.getName());
                ResultSet rs2 = db.execute("SELECT * FROM pairs WHERE pair_document_id="+doc_id+" AND pair_word_id="+word_id+";");
                if (!rs2.next())
                {
                    String query = "INSERT INTO pairs(pair_word_id, pair_document_id, pair_frequency) VALUES("+ word_id + ", " + doc_id + ", " + pair_frequency +  ") ";
                    db.executeUpdate(query);
                }                
            }
        }
    }
    
    
    /**
     * Empty the database
     */
    public void flush()
    {
        this._invertedIndex.clear();
    }
    
    /* ---------------------------------------------------------------- */
    /* ------------------------ OUTPUT -------------------------------- */    
    
    public String toString()
    {
        StringBuilder ret = new StringBuilder();
        
        ret.append("Index : ");
        ret.append(this._invertedIndex.toString());
        
        System.out.println(ret.toString());
        
        return ret.toString();
    }


}
