/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indexador;

import collector.IDbManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 492403
 */
public class InvertedIndex {

    /* ---------------------------------------------------------------- */
    /* ---------------------- PROPERTIES ------------------------------ */
    
    Hashtable<String, Hashtable<String, Integer>> _invertedIndex;

    
    /* ---------------------------------------------------------------- */
    /* ---------------------- CONSTRUCTOR  ---------------------------- */      
            
    public InvertedIndex()
    {
        _invertedIndex = new Hashtable<String, Hashtable<String, Integer>>();
    }
    
    
    /* ---------------------------------------------------------------- */
    /* ------------------------ METHODS ------------------------------- */         
    
    public int getFrequency(String word_str, String doc_str)
    {
        int freq = 0;
        
        if (this._invertedIndex.containsKey(word_str))
            if (this._invertedIndex.get(word_str).containsKey(doc_str))
            {
                freq = this._invertedIndex.get(word_str).get(doc_str);
                System.out.println(
                        "The frequency of the word '" + word_str +
                        "' in the doc '" + doc_str + 
                        "' is : " + this._invertedIndex.get(word_str).get(doc_str));
            }
        
        return freq;
    }
    
    public Hashtable<String, Integer> getWordDocsList(String word_str)
    {
        Hashtable<String, Integer> docs = null;
        
        if (this._invertedIndex.containsKey(word_str))
        {
            docs = this._invertedIndex.get(word_str);
            System.out.println("The docs (and frequencies) of '"+ word_str +"' are : " + docs.toString());
        }
        
        return docs;
    }
//    
//    public List getDocs()
//    {
//        List list_docs = new LinkedList<String>();
//        Hashtable<String, Integer> ht_docs;
//        Enumeration<String> docs;
//        Enumeration<String> words = _invertedIndex.keys();
//        
//        while (words.hasMoreElements())
//        {
//            ht_docs = getWordDocsList(words.nextElement());
//            docs = ht_docs.keys();
//            while (docs.hasMoreElements())
//            {
//                
//            }
//        }
//        
//        return list_docs;
//    }
    
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
    
    public String getVocabulary() 
    {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> keys = _invertedIndex.keys();
        
        sb.append("Vocabulary : ");
        sb.append("{");
        while (keys.hasMoreElements())
        {
            String key = keys.nextElement();
            sb.append(key);
            if (keys.hasMoreElements())
                sb.append(", ");
        }
        sb.append("}");
        
        System.out.println(sb.toString());
        return sb.toString();
    }
    
    
    public void persistWords(IDbManager db) throws SQLException
    {
        ResultSet rs = db.execute("SELECT word_value FROM  `words`");
        while (rs.next())
        {
            String current_word = rs.getString("word_value");
            if (_invertedIndex.containsKey(current_word));
            {
                _invertedIndex.remove(current_word);
                System.out.println("{" + current_word + "} : already exists in DB");
            }
        }       
        
        Enumeration<String> keys = _invertedIndex.keys();
        
        while (keys.hasMoreElements())
        {
            String key = keys.nextElement();
            String query = "Insert into words(word_value) values ('" + key + "');";
            // System.out.println(query);
            db.executeUpdate(query);
        }
        
    }
   
    public void persistPairs(IDbManager db, Document doc) throws SQLException {
        Enumeration<String> words = _invertedIndex.keys();
        
        while (words.hasMoreElements())
        {
            int word_id = -1;
            String word = words.nextElement();
            String q = "SELECT word_id FROM `words` WHERE word_value= '"+ word + "'";
            System.err.println(q);
            ResultSet rs = db.execute(q);
            if (rs.next())
                 word_id = rs.getInt("word_id");
            if (word_id != -1)
            {
                String query = "Insert into pairs(pair_word_id, pair_document_id, pair_frequency) values("+ word_id + ", " + doc.getId() + ", " + getFrequency(word, doc.getName()) +  ") ";
                System.err.println(query);
                db.executeUpdate(query);
            }
            
            
        }
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
