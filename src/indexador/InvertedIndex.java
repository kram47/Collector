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
 *
 * @author 492403
 */
public class InvertedIndex {

    /* ---------------------------------------------------------------- */
    /* ---------------------- PROPERTIES ------------------------------ */
    
    Hashtable<String, Hashtable<String, Integer>>   _invertedIndex;
    List<String>                                    _wordsExceptions;

    
    /* ---------------------------------------------------------------- */
    /* ---------------------- CONSTRUCTOR  ---------------------------- */      
            
    public InvertedIndex()
    {
        _invertedIndex = new Hashtable<String, Hashtable<String, Integer>>();
        _wordsExceptions = new ArrayList<String>();
    }
    
    
    /* ---------------------------------------------------------------- */
    /* ------------------------ METHODS ------------------------------- */         
    
    public int getFrequency(String word_str, String doc_str)
    {
        int freq = -1;
        
        if (this._invertedIndex.containsKey(word_str))
            if (this._invertedIndex.get(word_str).containsKey(doc_str))
            {
                freq = this._invertedIndex.get(word_str).get(doc_str);
//                System.out.println(
//                        "The frequency of the word '" + word_str +
//                        "' in the doc '" + doc_str + 
//                        "' is : " + this._invertedIndex.get(word_str).get(doc_str));
            }
        
        return freq;
    }
    
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
    
    
    
    public void persistWords(IDbManager db) throws SQLException
    {
        ResultSet rs = db.execute("SELECT word_value FROM  `words`");
        while (rs.next())
        {
            String current_word = rs.getString("word_value");
            if (_invertedIndex.containsKey(current_word));
            {
                this._wordsExceptions.add(current_word);                        // If the word already exists in index, we add exception to not add it in the Words table.
                // System.out.print(Tools.ANSI_BLUE + "{" + current_word + "} : already in DB, ");
            }
        }       
        
        Enumeration<String> words = _invertedIndex.keys();
        String current_word;
        String query;
        
        while (words.hasMoreElements())
        {
            current_word = words.nextElement();
            if (!this._wordsExceptions.contains(current_word))
            {
                query = "INSERT INTO words(word_value) VALUES ('" + current_word + "');";
                //System.out.println(query);
                db.executeUpdate(query);
            }
        }
    }
   
    public void persistPairs(IDbManager db, Document doc) throws SQLException 
    {
        Enumeration<String> words = _invertedIndex.keys();
        
        while (words.hasMoreElements())
        {
            int word_id = -1;
            String word = words.nextElement();
            String q = "SELECT word_id FROM `words` WHERE word_value= '"+ word + "'";
            //System.out.println(Tools.ANSI_BLUE + q);
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
                    // System.out.println(Tools.ANSI_PURPLE + query);
                    db.executeUpdate(query);
                }
//                else
//                    System.out.println("The pair already exists");
                
            }
            
            
        }
    }
    
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
