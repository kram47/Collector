/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package processador;

import collector.DbManager;
import collector.IDbManager;
import indexador.Document;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;
import test.Tools;

/**
 * An object to manage the parsing and computing of a given query
 * @author Marc
 */
public class Processador {

    /* ---------------------------------------------------------------- */
    /* ---------------------- PROPERTIES ------------------------------ */
    /**
     * The query to search
     */
    private String          _query;
    
    /**
     * The r_sum (Top part of vectorial method function) of the query
     */
    private float           _r_sum;
    
    /**
     * The r_square (Bottom part of vectorial method function) of the query
     */
    private float           _r_square;
    
    /**
     * Database Manager
     */
    private IDbManager      _db;


    /* ---------------------------------------------------------------- */
    /* ---------------------- CONSTRUCTOR  ---------------------------- */
    /**
     * Main constructor
     * @param query 
     */
    public Processador(String query) {
        this._db = DbManager.getInstance();
        this._query = query;
        this.calculateR();
    }


    /* ---------------------------------------------------------------- */
    /* --------------------- GETTER-SETTER ---------------------------- */
    /**
     * @return the _query
     */
    public String getQuery() {
        return _query;
    }

    /**
     * @param query the _query to set
     */
    public void setQuery(String query) {
        this._query = query;
        this.calculateR();
    }


    /* ---------------------------------------------------------------- */
    /* ------------------------ METHODS ------------------------------- */
    /**
     * Calculate the R (r_sum and r_square) of the query
     */
    private void calculateR() {
        String[] words = Tools.splitStringByWords(getQuery());
        int nb_words = words.length;

        this._r_sum = nb_words;
        this._r_square = (float) Math.sqrt(nb_words);
    }

    /**
     * Calculate the similarity of the query and the given Document
     * @param doc The document to calculate the similarity
     * @return 
     */
    public float calculateSimilarity(Document doc) {
        float numerator = doc.getR_sum() * this._r_sum;
        float denominator = doc.getR_square() * this._r_square;

        return numerator / denominator;
    }

    /**
     * Calculate the similarity of the query and the given datas (r_sum, r_square)
     * @param doc_r_sum The r_sum of a document document to calculate the similarity
     * @param doc_r_square The r_square of a document document to calculate the similarity
     * @return 
     */
    public float calculateSimilarity(float doc_r_sum, float doc_r_square) {
        float numerator = doc_r_sum * this._r_sum;
        float denominator = doc_r_square * this._r_square;

        return numerator / denominator;
    }

    /**
     * Calculate the similarity of the query with all the documents in DataBase
     * @throws SQLException 
     */
    private void process() throws SQLException
    {
        String query = "SELECT * FROM documents";
              
        ResultSet documents = this._db.execute(query);
        while (documents.next())
        {
            float similarity = this.calculateSimilarity(documents.getFloat("document_r_sum"), documents.getFloat("document_r_square"));
            
            if (!Float.isNaN(similarity))
            {
                query = "UPDATE documents SET document_similarity="+ similarity +" WHERE document_id="+documents.getInt("document_id");
                this._db.executeUpdate(query);
            }          
        }
    }
    
    /**
     * Get the 'nb' best document (With the highest similarity)
     * @param nb number of element in the list
     * @return The list with the similar documents
     * @throws SQLException 
     */
    private LinkedList<Document> getBestDocuments(int nb) throws SQLException
    {
        LinkedList<Document> docs = new LinkedList<Document>();
        String query = "SELECT * FROM documents ORDER BY document_similarity DESC";
        
        ResultSet rs = this._db.execute(query);
        while (rs.next() && nb != 0)
        {
            String name = rs.getString("document_name");
            Document current_doc = new Document(name, null);
            current_doc.setId(rs.getInt("document_id"));
            current_doc.setUrl(rs.getString("document_url"));
            current_doc.setTitle(rs.getString("document_title"));
            docs.add(current_doc);
            nb--;
        }
        return docs;
    }
    
    /**
     * Main Loop
     * @param nb_docs number of documents to return
     * @return The list of documents
     * @throws SQLException 
     */
    public LinkedList<Document> run(int nb_docs) throws SQLException
    {
        this.process();
        
        LinkedList<Document> documents = this.getBestDocuments(nb_docs);
        
        return documents;
    }
    
    /* ---------------------------------------------------------------- */
    /* ------------------------ OUTPUT -------------------------------- */
    public String toString() {
        StringBuilder ret = new StringBuilder();

        ret.append("Processador : {}");
        System.out.println(ret.toString());

        return ret.toString();
    }
}
