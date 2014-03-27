package collector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractQueue;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Marc
 */
public class main {
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, NoSuchAlgorithmException {
        
        // Connection to the database
        IDbManager d = DbManager.getInstance();
	d.setUrl("jdbc:mysql://localhost:3306/topicos_collector");
	d.setUser("root");
	d.setPassword("");
	d.connect();

        
        
        // We fill the queue with the default value of the class
        Queue urls = new LinkedList();
        urls.add("http://www.stf.jus.br/portal/principal/principal.asp");
        // urls.add("http://www.camara.gov.br");
        // urls.add("http://www.senado.gov.br/");

        // We Launch the collector
        Collector myColector  = new Collector();
        myColector.setUrls(urls);
        myColector.run();


//        
//        ResultSet resultats = null;
//        String requete = "SELECT page_name, page_content FROM pages";
//        resultats = d.execute(requete);    
//        try {        
//            if(resultats.first() != false)
//            {
//                do{
//                 String name = resultats.getString(1);
//                 String content = resultats.getString(2);
//                 System.out.println("[" + name + "]");
//                 System.out.println("### " + content + " ###");
//                 System.out.println("---------------");
//                }while(resultats.next());
//            }
//        } catch (Exception e) { System.out.println("Erreur main : " + e); }
//        
//        
       
        
    }
}
