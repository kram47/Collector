







package test;

import indexador.Indexador;
import collector.Collector;
import collector.DbManager;
import collector.IDbManager;
import indexador.Document;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import processador.Processador;

/**
 * Main class, where we launch everything
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

        
        // Creation of the scanner to get the input
        Scanner s = new Scanner(System.in);
        System.out.println("Database :");
        System.out.println(Tools.ANSI_BLUE + "-> Words");
        System.out.println(Tools.ANSI_GREEN + "-> Documents");
        System.out.println(Tools.ANSI_PURPLE + "-> Pairs (Document, Word)");
        System.out.println("1. Collector");
        System.out.println("2. Indexador");
        System.out.println("3. Pesquisador");
        System.out.println("4. Interface");
        System.out.println("What do yo want to launch ? (Type the number)");
        String choose = s.next();
        
        
        if (choose.compareTo("1") == 0)
        {
            System.out.println("---------------------------------\n            Collector");

            Queue urls = new LinkedList();
            
            urls.add("http://www.kram47.fr");
            //urls.add("http://www.stf.jus.br/portal/principal/principal.asp");
            //urls.add("http://www.camara.gov.br");
            //urls.add("http://www.senado.gov.br/");

            Collector myColector  = new Collector();
            myColector.setUrls(urls);
            myColector.run();
        }
        else if (choose.compareTo("2") == 0)
        {
            Indexador indexador = new Indexador();
            
            System.out.println("---------------------------------\n            Indexador\n---------------------------------");
            try 
            {
                indexador.run();
            }
            catch (SQLException ex) 
            { Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex); }
        }
        else if (choose.compareTo("3") == 0)
        {
            System.out.println("(--STOP-- to quit)");
            while (true)
            {
                System.out.println("Query :");
                String query = s.next();
                if(query.compareTo("--STOP--") == 0)
                    break;
                Processador proc = new Processador(query);
                LinkedList<Document> docs = new LinkedList<Document>();

                try 
                {
                    docs = proc.run(5);

                    for (Document doc : docs)
                    {
                        System.out.println(doc.getTitle() + " - " + doc.getUrl());
                    }
                } 
                catch (SQLException ex) 
                { Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex); }
            }
        }
        else if (choose.compareTo("4") == 0)
        {
            screen.Screen.run();
        }
     
    }
}
