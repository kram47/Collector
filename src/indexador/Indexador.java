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
    
    public Indexador() 
    {
        this._collectionSize = -1;
        this._collection = new ArrayList<Document>();
        this._index = new InvertedIndex(); 
        this._db = DbManager.getInstance();
    }

    private int calculateCollectionSize()
    {
        ResultSet rs = this._db.execute("SELECT COUNT( * ) AS collectionSize FROM  `documents`");
        System.out.println("SELECT COUNT( * ) AS collectionSize FROM  `documents`");
        try 
        {
            if (rs.next())
                this._collectionSize = rs.getInt("collectionSize");
        }
        catch (SQLException ex) 
        {   Logger.getLogger(Indexador.class.getName()).log(Level.SEVERE, null, ex); }
        
        return this._collectionSize;
    }
  
    private Document getNextDocument(int i)
    {
        Document doc = null;
        
        ResultSet rs = this._db.execute("SELECT * FROM documents WHERE document_id = " + ++i + ";");
        System.out.println("SELECT * FROM documents WHERE document_id = " + i + ";");
        try 
        {
            if (rs.next())
            {
                doc = new Document(rs.getString("document_name"), null);
                doc.setUrl(rs.getString("document_url"));
                doc.setTitle(rs.getString("document_title"));
                doc.setContent(rs.getString("document_content"));
            }
        }
        catch (SQLException ex) 
        {   Logger.getLogger(Indexador.class.getName()).log(Level.SEVERE, null, ex); }
        
        return doc;
    }
    
    private String[] splitDocumentByWords(Document doc)
    {
        String[] words = doc.getContent().split("[\\s\\W]+");
        
        if (words != null)
            doc.setWords(words);
        return words;
    }
    
    private void calculateFrequencies() 
    {
        for (int i = 0 ; i < this._collectionSize ; ++i)
        {   
            Document current_doc = getNextDocument(i);
            
            System.out.println("current_doc.name = " + current_doc.getName() );
            System.out.println("current_doc.url = " + current_doc.getUrl() );
            System.out.println("current_doc.title = " + current_doc.getTitle() );
            System.out.println("current_doc.content = " + current_doc.getContent() );
            System.out.println("---------------");
            
            for (String word : splitDocumentByWords(current_doc)){
                System.out.println(word);
            }
//            for (String current_word : current_doc.getWords())
//            {
//                _index.addAppearance(current_word, current_doc.getName());
//            }
            
//            persistDocument();
//            persistWords();
//            persistIndex();
        }

    }
    
    public void run ()
    {
        this.calculateCollectionSize();
        System.out.println("this._collectionSize = " + this._collectionSize);
        this.TEST_fillCollection();

        this.calculateFrequencies();
        
//        TESTS 
        _index.toString();
//        _index.getFrequency("mot39", "doc1");
//        _index.getWordFrequencies("mot2");
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
  
}


