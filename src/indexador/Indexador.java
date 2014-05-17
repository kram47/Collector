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


/**
 *
 * @author 492403
 */
public class Indexador {
  
    /* ---------------------------------------------------------------- */
    /* ---------------------- PROPERTIES ------------------------------ */  
    
    int             _collectionSize;
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
    
    private int extractCollectionSize() throws SQLException
    {
        ResultSet rs = this._db.execute("SELECT COUNT( * ) AS collectionSize FROM  `documents`");
        System.out.println("SELECT COUNT( * ) AS collectionSize FROM  `documents`");
        if (rs.next())
            this._collectionSize = rs.getInt("collectionSize");

        return this._collectionSize;
    }
  
    private Document getNextDocument(int i) throws SQLException
    {
        Document doc = null;
        
        ResultSet rs = this._db.execute("SELECT * FROM documents WHERE document_id = " + i + ";");
        System.out.println("SELECT * FROM documents WHERE document_id = " + i + ";");
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
            
            System.out.println("current_doc.name = " + current_doc.getName() );
            System.out.println("current_doc.url = " + current_doc.getUrl() );
            System.out.println("current_doc.title = " + current_doc.getTitle() );
            System.out.println("current_doc.content = " + current_doc.getContent() );
            
            for (String word : splitDocumentByWords(current_doc))
                System.out.print(word + ", ");
            
            for (String current_word : current_doc.getWords())
            {
                _index.addAppearance(current_word, current_doc.getName());
            }
            
            _index.persistWords(this._db);
            _index.persistPairs(this._db, current_doc);
        }
    }
    
    private void calculateTF() throws SQLException
    {
        String query;
        
        for (int doc_id = 1 ; doc_id < this._collectionSize ; ++doc_id)
        {  
            ResultSet rs;
            int f_max = -1;

            /**
             * GET THE Fmax For the TF calcul
             */
            query = "SELECT pair_frequency FROM pairs WHERE pair_document_id = " + doc_id + " ORDER BY pair_frequency DESC LIMIT 0, 1";
            System.err.println("Get The Fmax : " + query);
            rs = this._db.execute(query);
            if (rs.next())
                f_max = rs.getInt("pair_frequency");
            
            /**
             * Get all words to loop on it to calculate the TF = f/f_max
             */
            query = "";
            rs = this._db.execute("SELECT * FROM pairs WHERE pair_document_id=" + doc_id);
            while (rs.next())
            {
                if (f_max != -1)
                    query += "UPDATE pairs SET pair_tf=pair_frequency/" + f_max + " WHERE pair_document_id=" + doc_id + " AND pair_word_id="+ rs.getInt("pair_word_id")+";";
            }
            System.out.println(query);
            _db.executeUpdate(query);
        }
    }    
    
    public void run () throws SQLException
    {
        this.extractCollectionSize();
        System.out.println("this._collectionSize = " + this._collectionSize);


        this.calculateFrequencies();
        this.calculateTF();
        
//        TESTS 
        _index.toString();
//        _index.getFrequency("mot39", "doc1");
//        _index.getWordDocsList("mot2");
        _index.getVocabulary();
    }

    
   
}




     