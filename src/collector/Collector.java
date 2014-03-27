/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package collector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.*;


/**
 *
 * @author Marc
 */
public class Collector {
  
  /* ---------------------------------------------------------------- */
  /* ---------------------- PROPERTIES ------------------------------ */
    
  Queue urls = new LinkedList();
 // String testText = new String();
  String urlRoot = new String();
  Queue newUrls = new LinkedList();
  
  
  /* ---------------------------------------------------------------- */
  /* -------------------- GETTER-SETTER ----------------------------- */
  
  
  public Collector (){
  
  }
  
  public Queue getUrls () {
    return urls;
  }

  public void setUrls (Queue urls) {
    this.urls = urls;
  }
  
//  public String getTestText() {
//    return testText;
//  }
 
  
  
  
  
  /* ---------------------------------------------------------------- */
  /* ------------------------ METHODS ------------------------------- */
  
  
  public String collectPage (String str_url) {
    StringBuilder text = new StringBuilder ();
    
    try 
    {
      URL url = new URL (str_url); 
      URLConnection url_connection = url.openConnection();
      InputStreamReader reader = new InputStreamReader (url_connection.getInputStream ());
      BufferedReader buffer = new BufferedReader (reader);

      String line;
      while ((line = buffer.readLine()) != null) 
      {
        this.extractUrls(line);
        text.append(line);
        //text.append(line + "\n");
      } 
    } 
    catch (Exception e) 
    { //System.out.println("Error : i wasn't able to get the page: " + str_url + "!\n");
      System.out.println(str_url + " ## Because :" + e); 
    }

    //this.testText = text.toString ();
    return text.toString ();
  }
  
  
 
  public void stockPages (String page, String name) {
    try 
    {
      File f = new File(name);
      FileOutputStream out = new FileOutputStream(f);
      OutputStreamWriter writer = new OutputStreamWriter (out);
      BufferedWriter output_buffer = new BufferedWriter (writer);
        
      output_buffer.write (page);
      output_buffer.close ();
    } 
    catch (Exception e) 
    {
      System.out.println ("Error: unable to write the page: " + name + " on hard disc!");
    }
  }
  
  
  public void stockSketchPages (String page, String name) {
    try 
    {
      File f = new File(name);
      FileOutputStream out = new FileOutputStream(f);
      OutputStreamWriter writer = new OutputStreamWriter (out);
      BufferedWriter output_buffer = new BufferedWriter (writer);
        
      page = page.replaceAll("\\<[^<]*>", "");
      
      output_buffer.write (page);
      output_buffer.close ();
    } 
    catch (Exception e) 
    {
      System.out.println ("Error: unable to write the page: " + name + " on hard disc!");
    }
  }
  
  
  public void extractUrls(String line) {
      String link = new String();
      
      if (line.indexOf("href=\"") == -1)
          return ;
      line = line.toLowerCase();
      Pattern p=Pattern.compile("href=\"?[^\"]*\"?");
      Matcher m=p.matcher(line);
      while(m.find()) 
      {
        int begin = m.group().indexOf("\"");
        link = m.group().substring(begin);
        link = link.replaceAll("\"","");
        
        if (link.charAt(0) == '/')
            link = this.relativeToAbsolute(link);
        
        if (link.indexOf(".css") == -1 && link.indexOf("javascript:") == -1 && link.indexOf(".ico") == -1)
           if (link != "" && link != null && link.compareTo("#") != 0 && link.indexOf("../") == -1)
           {
               if (!this.urlAlreadyExists(link))
               {
                   this.newUrls.add(link);
                   
         //          System.out.println("J'ajoute : " + link);
               }
           }       
      }
  }
  
  
  private String relativeToAbsolute(String url)
  {
//      System.out.println("En effet mon url est absolute !!");
   //   System.out.println("Avant : " + url);
      url = url.replaceFirst("/", this.urlRoot + "/");
      return url;
  }
  
  private String getUrlRoot(String url) {
      String root = new String();
      
      if (url.indexOf("http://") != -1)
        {
            root = url.replace("http://", "");
            root = root.substring(0, root.indexOf("/"));
            root = root.replace("www.", "http://www.");
        }
      
      return root;
  }
  
  
  private boolean urlAlreadyExists(String link) {
      for (Iterator it = this.newUrls.iterator(); it.hasNext();) 
          {
              String s = it.next().toString();
              if (s.compareTo(link) == 0)
                  return true;
          }
      return false;
    }
  
  
  public void run()
  {
      int i = 0;
      while (this.urls.size() > 0)
      {
        int j = 0;
        String page;
        String url = (String) this.urls.poll ();
        this.urlRoot = this.getUrlRoot(url);
        System.out.println("ROOT : " + this.urlRoot);
        String rootPage = this.collectPage(url);
                
        System.out.println("-----------------------------");
        System.out.println("-----------------------------");
        
        String newRoot = new String();
        while (this.newUrls.size() > 0)
        {
            url = (String) this.newUrls.poll();
            //System.out.println("newUrl to connect : " + url);
            
            // If the root is not the same, it's another website so we store it in url Queue
            newRoot = this.getUrlRoot(url);
            if (this.urlRoot.compareTo(newRoot) != 0)
            {
                System.out.println("Nouveau root : " + newRoot + " ## Pour l'url : " + url);
                this.urls.add(url);
                continue;
            }
            //if (j % 100 == 0) { System.out.println("[" + j + "]Il reste " + this.newUrls.size() + " elements"); }
            page = this.collectPage(url);
            this.stockSketchPages(page, "C:\\Users\\Marc\\temp\\pages\\" + i + "_" + j++ + "_sketch.html");        
          //this.stockPages(page, "C:\\Users\\Marc\\temp\\pages\\" + i + "_" + j + ".html");
            
            
        }
        
        //this.stockPages(rootPage, "C:\\Users\\Marc\\temp\\pages\\" + i + ".html");
        this.stockSketchPages(rootPage, "C:\\Users\\Marc\\temp\\pages\\" + i++ + "_sketch.html");
      }
  }

    
  
  
  
  
  
  
  
}

