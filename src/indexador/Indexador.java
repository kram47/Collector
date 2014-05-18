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
    
    private void extractCollectionSize() throws SQLException
    {
        String query = "SELECT COUNT( * ) AS collectionSize FROM  `documents`";
        ResultSet rs = this._db.execute(query);
        System.out.println(Tools.ANSI_GREEN + query);
        if (rs.next())
            this._collectionSize = rs.getInt("collectionSize");
        
        query = "SELECT document_id FROM  `documents` ORDER BY document_id DESC LIMIT 0,1";
        rs = this._db.execute(query);
        System.out.println(Tools.ANSI_GREEN + query);
        if (rs.next())
            this._collectionMax = rs.getInt("document_id");
    }
  
    private Document getNextDocument(int i) throws SQLException
    {
        Document doc = null;
        
        String query = "SELECT * FROM documents WHERE document_id = " + i + ";";
        ResultSet rs = this._db.execute(query);
        System.out.println(Tools.ANSI_GREEN + query);
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
    
    private String[] splitDocumentByWords(Document doc)
    {
        String[] words = doc.getContent().split("[\\s\\W]+");
        
        if (words != null)
            doc.setWords(words);
        return words;
    }
    
    private void calculateFrequencies() throws SQLException 
    {
        for (int i = 1 ; i < this._collectionSize ; ++i)
        {   
            Document current_doc = getNextDocument(i);
            if (current_doc == null)
                continue;

            System.out.println(Tools.ANSI_GREEN + "current_doc.id = " + current_doc.getId() );
            System.out.println(Tools.ANSI_GREEN + "current_doc.name = " + current_doc.getName() );
            System.out.println(Tools.ANSI_GREEN + "current_doc.url = " + current_doc.getUrl() );
            System.out.println(Tools.ANSI_GREEN + "current_doc.title = " + current_doc.getTitle() );

            this.splitDocumentByWords(current_doc);
            
            for (String current_word : current_doc.getWords())
            {
                _index.addAppearance(current_word, current_doc.getName());
            }
            
            _index.persistWords(this._db);
            _index.persistPairs(this._db, current_doc);
            _index.flush();
        }
    }
    
    private void calculateTF() throws SQLException
    {
        String query;
        
        for (int doc_id = 1 ; doc_id <= this._collectionMax ; ++doc_id)
        {  
            System.out.println("Doc "+doc_id);
            ResultSet rs;
            int f_max = -1;

            /**
             * GET THE Fmax For the TF calcul
             */
            query = "SELECT pair_frequency FROM pairs WHERE pair_document_id = " + doc_id + " ORDER BY pair_frequency DESC LIMIT 0, 1";
            rs = this._db.execute(query);
            if (rs.next())
                f_max = rs.getInt("pair_frequency");
            
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
    }    
    
    
    
    
    private void calculateIDF() throws SQLException
    {
        String query = "SELECT * FROM words";
        ResultSet words = this._db.execute(query);
        int nb_doc_appearance, word_id;
        double idf;
        
        while(words.next())
        {
            nb_doc_appearance = -1; word_id = -1; idf = -1;
            word_id = words.getInt("word_id");
            //System.out.printf("word{%s, %d}\n", words.getString("word_value"), word_id);
            ResultSet rs = this._db.execute("SELECT COUNT(*) AS nb_doc_appearance FROM pairs WHERE pair_word_id="+word_id);
            if (rs.next())
                nb_doc_appearance = rs.getInt("nb_doc_appearance");
            idf = (double) this._collectionSize / nb_doc_appearance;
            //System.out.printf("idf = %d / %d = %f\n", this._collectionSize, nb_doc_appearance, idf);
            
            idf = Math.log(idf) / Math.log(2);
            //System.out.printf("Math.log(idf) / Math.log(2) = %f\n", idf);
            if (nb_doc_appearance != -1 && word_id != -1 && idf != -1)
                this._db.executeUpdate("UPDATE words SET word_idf="+idf +" WHERE word_id="+ word_id);
        }
    }
    
    public void run () throws SQLException
    {
        this.extractCollectionSize();
        System.out.println("collectionSize = " + this._collectionSize);
        System.out.println("collectionMax = " + this._collectionMax);

        System.out.println("Calculate Frequencies");
        // this.calculateFrequencies();
        System.out.println("Calculate TF");
        // this.calculateTF();
        System.out.println("Calculate IDF");
        this.calculateIDF();
        
        
//        TESTS 
//        _index.toString();
//        _index.getFrequency("mot39", "doc1");
//        _index.getWordDocsList("mot2");
//        _index.getVocabulary();
    }

    
   
}




     