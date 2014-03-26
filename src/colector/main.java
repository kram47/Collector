/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package colector;

import java.util.AbstractQueue;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Marc
 */
public class main {
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Queue urls = new LinkedList();
        
        urls.add("http://www.stf.jus.br/portal/principal/principal.asp");
        // urls.add("http://www.camara.gov.br");
        // urls.add("http://www.senado.gov.br/");

        Colector myColector  = new Colector();
        myColector.setUrls(urls);
        
        myColector.run();
        
        
    }
}
