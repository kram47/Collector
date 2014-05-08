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
    
    int             _collectionSize;
    IDbManager      _db;
    List<Document>  _collection;
    InvertedIndex   _index;
    
    public Indexador() {
        this._collectionSize = -1;
        this._collection = new ArrayList<Document>();
        this._index = new InvertedIndex(); 
        
        this._db = DbManager.getInstance();
    }
    
  public int getCollectionSize () {
    return _collectionSize;
  }
  

  public void extractPagesNumber() {
    
  }
 
  private int calculateCollectionSize()
  {
      // requete SQL pour recuperer la taille de la base (DB COLLECTEUR)
      // SELECT Count(*) FROM pages;
      this._collectionSize = 10;
      
      return this._collectionSize;
  }
  
  
  public void run (){
    
    this.calculateCollectionSize();
    this.TEST_fillCollection();
           
    
    for (int i = 0 ; i < this._collectionSize ; ++i)
    {
        Document current_doc = _collection.get(i);
        for (String current_word : current_doc.getWords())
        {
            _index.addAppearance(current_word, current_doc.getName());
        }
    }
        
    
    // TESTS 
    _index.toString();
    _index.getFrequency("mot39", "doc1");
    _index.getWordFrequencies("mot2");
    _index.getVocabulary();
  }
  
  
    private void TEST_fillCollection()
    {
        Document doc1 = new Document("doc1", new String[]{"lol", "ouech", "je", "suis", "par", "bonjour", "ma", "gueule", "joli", "tonton", "papy", "lol", "ouech", "je", "suis", "lol", "ouech", "je", "suis", "par", "bonjour", "ma", "par", "bonjour", "ma"});
        Document doc2 = new Document("doc2", new String[]{"en", "fait", "ben", "voila", "bola", "casa", "tu", "nous", "fais", "chier", "avec"});
        Document doc3 = new Document("doc3", new String[]{"ne", "on", "grave", "pas", "c´est", "mais", "con", "la", "a", "mots", "tes"});
        Document doc4 = new Document("doc4", new String[]{"t'en", "veux", "pas", "ce", "n'est", "pas", "vraiment", "de", "ta", "faute", "wesley"});
        Document doc5 = new Document("doc5", new String[]{"t'en", "veux", "pas", "ce", "n'est", "pas", "vraiment", "de", "ta", "faute", "wesley"});
        Document doc6 = new Document("doc6", new String[]{"t'en", "veux", "pas", "ce", "n'est", "pas", "vraiment", "de", "ta", "faute", "wesley"});
        Document doc7 = new Document("doc7", new String[]{"t'en", "veux", "pas", "ce", "n'est", "pas", "vraiment", "de", "ta", "faute", "wesley"});
        Document doc8 = new Document("doc8", new String[]{"t'en", "veux", "pas", "ce", "n'est", "pas", "vraiment", "de", "ta", "faute", "wesley"});
        Document doc9 = new Document("doc9", new String[]{"t'en", "veux", "pas", "ce", "n'est", "pas", "vraiment", "de", "ta", "faute", "wesley"});
        Document doc10 = new Document("doc10", new String[]{"t'en", "veux", "pas", "ce", "n'est", "pas", "vraiment", "de", "ta", "faute", "wesley"});

        _collection.add(doc1);
        _collection.add(doc2);
        _collection.add(doc3);    
        _collection.add(doc4);
        _collection.add(doc5);
        _collection.add(doc6);
        _collection.add(doc7);    
        _collection.add(doc8);
        _collection.add(doc9);
        _collection.add(doc10);
    }
  

    private void TEST_selectDocs()
    {
        // Test base
        ResultSet rs = this._db.execute("SELECT * FROM documents;");
        try {
            while (rs.next())
            {
                String lastName = rs.getString("document_name");
                String page = rs.getString("document_content");
                System.out.println(lastName + " :");
                System.out.println(page);
                System.out.println("------------------\n");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Indexador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  
}


