/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package indexador;

import collector.IDbManager;
import collector.DbManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import screen.MyListener;
import test.Tools;


/**
 *
 * @author 492403
 */
public class Indexador {
  
    /* ---------------------------------------------------------------- */
    /* ---------------------- PROPERTIES ------------------------------ */  
    
    int             _collectionSize;
    int             _collectionMax;
    IDbManager      _db;
    List<Document>  _collection;
    InvertedIndex   _index;
    MyListener      _listener;
    
    
    /* ---------------------------------------------------------------- */
    /* ---------------------- CONSTRUCTOR  ---------------------------- */        

    public Indexador() 
    {
        this._collectionSize = -1;
        this._collection = new ArrayList<Document>();
        this._index = new InvertedIndex(); 
        this._db = DbManager.getInstance();
    }

    
    /* ---------------------------------------------------------------- */
    /* ------------------------ METHODS ------------------------------- */  
    
    private void        extractCollectionSize() throws SQLException
    {
        String query = "SELECT COUNT( * ) AS collectionSize FROM  `documents`";
        ResultSet rs = this._db.execute(query);
        if (rs.next())
            this._collectionSize = rs.getInt("collectionSize");
        
        query = "SELECT document_id FROM  `documents` ORDER BY document_id DESC LIMIT 0,1";
        rs = this._db.execute(query);
        if (rs.next())
            this._collectionMax = rs.getInt("document_id");
    }
  
    private Document    getNextDocument(int i) throws SQLException
    {
        Document doc = null;
        
        String query = "SELECT * FROM documents WHERE document_id = " + i + ";";
        ResultSet rs = this._db.execute(query);
        if (rs.next())
        {
            doc = new Document(rs.getString("document_name"), null);
            doc.setId(i);
            doc.setUrl(rs.getString("document_url"));
            doc.setTitle(rs.getString("document_title"));
            doc.setContent(rs.getString("document_content"));
        }
        return doc;
    }
    
    
    public String       getVocabulary() throws SQLException
    {
        StringBuilder sb = new StringBuilder();
        String query = "SELECT word_value FROM words;";
        ResultSet rs = this._db.execute(query);
        String word;
        
        sb.append("Vocabulary : {");
        while(rs.next())
        {
            word = rs.getString("word_value");
            sb.append("{"+word+"}");
            if(rs.next())
                sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }
    
    public static LinkedList<String>       getVocabularyList() throws SQLException
    {
        LinkedList<String> words = new LinkedList<String>();
        String query = "SELECT word_value FROM words;";
        ResultSet rs = DbManager.getInstance().execute(query);
        String word;
        
        while(rs.next())
        {
            word = rs.getString("word_value");
            words.add(word);
        }
        
        return words;
    }
    
    public static LinkedList<String>       getDocumentsList() throws SQLException
    {
        LinkedList<String> docs = new LinkedList<String>();
        String query = "SELECT document_name, document_url FROM documents;";
        ResultSet rs = DbManager.getInstance().execute(query);
        String doc;
        
        while(rs.next())
        {
            doc = rs.getString("document_name");
            doc += " - " + rs.getString("document_url");
            docs.add(doc);
        }
        
        return docs;
    }
    
    private void        calculateFrequencies() throws SQLException 
    {
        for (int i = 1 ; i <= this._collectionMax ; ++i)
        {   
            Document current_doc = getNextDocument(i);
            if (current_doc == null)
                continue;

            int percent = i * 100/this._collectionMax ;
            System.out.println("["+percent+"%] Doc : " + current_doc.getId() + " - " + current_doc.getTitle() );

            current_doc.setWords(Tools.splitStringByWords(current_doc.getContent()));
            
            for (String current_word : current_doc.getWords())
                _index.addAppearance(current_word, current_doc.getName());
            
            _index.persistWords(this._db);
            _index.persistPairs(this._db, current_doc);
            _index.flush();
        }
        System.out.println("[100%]");
    }
    
    private void        calculateTF() throws SQLException
    {
        String query;
        
        for (int doc_id = 1 ; doc_id <= this._collectionMax ; ++doc_id)
        {  
            ResultSet rs;
            int f_max = -1;

            /**
             * GET THE Fmax For the TF calcul
             */
            query = "SELECT pair_frequency FROM pairs WHERE pair_document_id = " + doc_id + " ORDER BY pair_frequency DESC LIMIT 0, 1";
            rs = this._db.execute(query);
            if (rs.next())
            {
                int percent = doc_id * 100/this._collectionMax;
                System.out.println("["+percent+"%] Doc " + doc_id);
                f_max = rs.getInt("pair_frequency");
            }
            
            /**
             * Get all words to loop on it to calculate the TF = f/f_max
             */
            rs = this._db.execute("SELECT * FROM pairs WHERE pair_document_id=" + doc_id);
            while (rs.next())
            {
                int word_id = rs.getInt("pair_word_id");
                if (f_max != -1)
                {
                    query = "UPDATE pairs SET pair_tf=pair_frequency/" + f_max + " WHERE pair_document_id=" + doc_id + " AND pair_word_id="+ word_id+";";
                    // System.out.println(query);
                    _db.executeUpdate(query);
                }
            }
        }
        System.out.println("[100%]");
    }    
    
    private void        calculateIDF() throws SQLException
    {
        String query = "SELECT * FROM words";
        ResultSet words = this._db.execute(query);
        int nb_doc_appearance, word_id;
        double idf;
        
        while(words.next())
        {
            nb_doc_appearance = -1; word_id = -1; idf = -1;
            word_id = words.getInt("word_id");
            if(word_id%50 == 0)
                System.out.println("Word : " + word_id);
            ResultSet rs = this._db.execute("SELECT COUNT(*) AS nb_doc_appearance FROM pairs WHERE pair_word_id="+word_id);
            if (rs.next())
                nb_doc_appearance = rs.getInt("nb_doc_appearance");
            idf = (double) this._collectionSize / nb_doc_appearance;
            
            idf = Math.log(idf) / Math.log(2);
            if (nb_doc_appearance != -1 && word_id != -1 && idf != -1)
                this._db.executeUpdate("UPDATE words SET word_idf="+idf +" WHERE word_id="+ word_id);
        }
    }
    
    private void        calculateWeight() throws SQLException
    {
        String query = "SELECT pair_document_id, pair_word_id FROM pairs";
        ResultSet pairs = this._db.execute(query);
        while (pairs.next())
        {
            int word_id = pairs.getInt("pair_word_id");
            int document_id = pairs.getInt("pair_document_id");
            
            ResultSet word = this._db.execute("SELECT word_idf FROM words WHERE word_id="+word_id);    
            if (word.next())
            {
                float idf = word.getFloat("word_idf");
                query = "UPDATE pairs SET pair_w=pair_tf*"+ idf +" WHERE pair_document_id="+document_id+" AND pair_word_id="+word_id;
                this._db.executeUpdate(query);
            }
        }
    }
    
    private void        calculateR() throws SQLException
    {
        String query;
        float r_sum, r_square;
        
        for(int doc_id = 1 ; doc_id <= this._collectionMax ; ++doc_id)
        {
            r_sum = r_square = 0;
            query = "SELECT pair_w FROM pairs WHERE pair_document_id="+doc_id;
            ResultSet rs = this._db.execute(query);
            
            while (rs.next())
            {
                float pair_w = rs.getFloat("pair_w");
                r_sum += pair_w;
                r_square += Math.pow(pair_w, 2);
            }
            if (r_sum != 0 && r_square != 0)
            {
                r_square = (float) Math.sqrt(r_square);
                query = "UPDATE documents SET document_r_sum="+r_sum+", document_r_square="+r_square+" WHERE document_id="+doc_id;
                this._db.executeUpdate(query);
            }
        }
    }
    
    public void         setListener(MyListener l)
    {
        this._listener = l;
    }
    
    public void         run () throws SQLException
    {
        this.extractCollectionSize();

        System.out.println("Calculate Frequencies ...");
        this.calculateFrequencies();
        if (this._listener != null)
            this._listener.onCalculateFrequenciesFinish();
        
        System.out.println("Calculate TF ...");
        this.calculateTF();
        if (this._listener != null)
            this._listener.onCalculateTFFinish();
        
        System.out.println("Calculate IDF ...");
        this.calculateIDF();
        if (this._listener != null)
            this._listener.onCalculateIDFFinish();
        
        System.out.println("Calculate Weight ...");
        this.calculateWeight();
        if (this._listener != null)
            this._listener.onCalculateWFinish();
        
        System.out.println("Calculate R ...");
        this.calculateR();
        if (this._listener != null)
            this._listener.onCalculateRFinish();
        
    }

    
    
   
}




     