/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package indexador;

import java.util.Enumeration;
import java.util.Hashtable;

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
    
    public Hashtable<String, Integer> getWordFrequencies(String word_str)
    {
        Hashtable<String, Integer> docs = null;
        
        if (this._invertedIndex.containsKey(word_str))
        {
            docs = this._invertedIndex.get(word_str);
            System.out.println("The docs (and frequencies) of '"+ word_str +"' are : " + docs.toString());
        }
        
        return docs;
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
