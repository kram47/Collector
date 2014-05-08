package test;

import indexador.Indexador;
import collector.Collector;
import collector.DbManager;
import collector.IDbManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marc
 */
public class main {
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Connection to the database
        IDbManager d = DbManager.getInstance();
	d.setUrl("jdbc:mysql://localhost:3306/topicos_sri");
	d.connect();

        // We fill the queue with the default value of the class
//        Queue urls = new LinkedList();
//        urls.add("http://www.stf.jus.br/portal/principal/principal.asp");
//        //urls.add("http://www.camara.gov.br");
//        //urls.add("http://www.senado.gov.br/");
//
//        // We Launch the collector
//        Collector myColector  = new Collector();
//        myColector.setUrls(urls);
//        myColector.run();
          Indexador indexador = new Indexador();
          indexador.run();

          
        
              
    }
}
