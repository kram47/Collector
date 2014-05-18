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
 *
 * @author Marc
 */
public class Processador {

    /* ---------------------------------------------------------------- */
    /* ---------------------- PROPERTIES ------------------------------ */
    private String          _query;
    private float           _r_sum;
    private float           _r_square;
    private IDbManager      _db;


    /* ---------------------------------------------------------------- */
    /* ---------------------- CONSTRUCTOR  ---------------------------- */
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
    private void calculateR() {
        String[] words = Tools.splitStringByWords(getQuery());
        int nb_words = words.length;

        this._r_sum = nb_words;
        this._r_square = (float) Math.sqrt(nb_words);
    }

    public float calculateSimilarity(Document doc) {
        float numerator = doc.getR_sum() * this._r_sum;
        float denominator = doc.getR_square() * this._r_square;

        return numerator / denominator;
    }

    public float calculateSimilarity(float doc_r_sum, float doc_r_square) {
        float numerator = doc_r_sum * this._r_sum;
        float denominator = doc_r_square * this._r_square;

        return numerator / denominator;
    }

    private void process() throws SQLException
    {
        String query = "SELECT * FROM documents";
        
        System.out.println(query);        
        ResultSet documents = this._db.execute(query);
        while (documents.next())
        {
            float similarity = this.calculateSimilarity(documents.getFloat("document_r_sum"), documents.getFloat("document_r_square"));
            
            if (!Float.isNaN(similarity))
            {
                query = "UPDATE documents SET document_similarity="+ similarity +" WHERE document_id="+documents.getInt("document_id");
                System.out.println(query);
                this._db.executeUpdate(query);
            }          
        }
    }
    
    private LinkedList<Document> getBestDocuments(int nb) throws SQLException
    {
        LinkedList<Document> docs = new LinkedList<Document>();
        String query = "SELECT * FROM documents ORDER BY document_similarity DESC";
        
        System.out.println(query);        
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
    
    public LinkedList<Document> run() throws SQLException
    {
        this.process();
        
        LinkedList<Document> documents = this.getBestDocuments(5);  
        for (Document doc : documents)
        {
            System.out.println("ID : " + doc.getId());
            System.out.println("URL : " + doc.getUrl());
        }
        
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
