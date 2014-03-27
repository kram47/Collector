package collector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
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
  String urlRoot = new String();
  Queue newUrls = new LinkedList();
  
  
  /* ---------------------------------------------------------------- */
  /* -------------------- GETTER-SETTER ----------------------------- */
  
  // Constructor
  public Collector (){
  
  }
  
  public Queue getUrls () {
    return urls;
  }

  public void setUrls (Queue urls) {
    this.urls = urls;
  }
 
  
  
  
  
  /* ---------------------------------------------------------------- */
  /* ------------------------ METHODS ------------------------------- */
  
    /**
     * Read the page of the current URL
     * @param str_url
     * @return 
     */
    public String     collectPage (String str_url) {
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
          text.append(line);
          //text.append(line + "\n");
        }

        if (!this.isPageExists(text.toString()))
            this.extractUrlsFromHtml(text.toString());

      } catch (Exception e) {  System.out.println("Error : unable to get page: " + str_url + " ## Because :" + e); }

      return text.toString ();
    }


   /**
    * Store the entire page
    * @param page
    * @param name 
    */
    public void       storePages (String page, String name) {
  //    try 
  //    {
  //      File f = new File(name);
  //      FileOutputStream out = new FileOutputStream(f);
  //      OutputStreamWriter writer = new OutputStreamWriter (out);
  //      BufferedWriter output_buffer = new BufferedWriter (writer);
  //        
  //      output_buffer.write (page);
  //      output_buffer.close ();
  //    } 
  //    catch (Exception e) 
  //    {
  //      System.out.println ("Error: unable to write the page: " + name + " on hard disc!");
  //    }

    }


    /**
     * Store a sketch of the page
     * @param page
     * @param name 
     */
    public void       storeSketchPages (String page, String name){
      try 
      {      
          page = page.replaceAll("\\<div id=\"divNaoImprimir\">.*</div>", "");
          page = page.replaceAll("\\<td width=\"48%\" valign=\"top\">.*</td>", "");
          page = page.replaceAll("\\<div id=\"divImpressao\" style=\"min-height:150px;\">.*</div>", "");
          //page = page.replaceAll("\\<[^<]*>", "");
          Md5Manager md = new Md5Manager();
          String md5 = md.getMd5String(page);

          IDbManager d = DbManager.getInstance();
          String value = "VALUES(\"" + name + "\", \"HTML CONTENT BUT I HAVE THIS PROBLEM OF PONCTUATION\", \"" + md5 + "\")";
          String query = "INSERT INTO pages(page_name, page_content, page_md5) " + value + ";";

          System.out.println(query);
          d.executeUpdate(query);
      } 
      catch (Exception e) 
      {
        System.out.println ("Error: unable to write the page: " + name + " on hard disc!");
      }
    }


    /**
     * Extracts url from the page and push it in the queue called newUrls
     * @param document 
     */
    public void       extractUrlsFromHtml(String document) {
        String link = new String();

        if (document.indexOf("href=\"") == -1)
            return ;

        // We look for the pattern : href=""
        // Then we clean every pattern we found
        document = document.toLowerCase();
        Pattern p=Pattern.compile("href=\"?[^\"]*\"?");
        Matcher m=p.matcher(document);
        while(m.find())
        {
          int begin = m.group().indexOf("\"");
          link = m.group().substring(begin);
          link = link.replaceAll("\"","");

          // Converts relative links to absolute links
          if (link.charAt(0) == '/')
              link = this.relativeToAbsolute(link);

          // Get rid of css, javascript, icon, # link, empty strings, internal server like '../'
          // And then we add the link to list of newUrls
          if (link.indexOf(".css") == -1 && link.indexOf("javascript:") == -1 && link.indexOf(".ico") == -1)
             if (link != "" && link != null && link.compareTo("#") != 0 && link.indexOf("../") == -1)
             {
                 if (!this.isUrlExists(link, this.newUrls))
                     this.newUrls.add(link);
             }       
        }
    }


    /**
     * Convert a relative link to an absolute link using the current root of the wesite
     * @param url
     * @return url_root
     */
    private String    relativeToAbsolute(String url) {
        url = url.replaceFirst("/", this.urlRoot + "/");      
        return url;
    }


    /**
     * Extract the root website (without any '/') from the link
     * @param url
     * @return root
     */
    private String    getUrlRoot(String url) {
        String root = new String();

        if (url.indexOf("http://") != -1)
          {
              root = url.replace("http://", "");
              root = root.substring(0, root.indexOf("/"));
              root = root.replace("www.", "http://www.");
          }
        else if (url.indexOf("https://") != -1)
          {
              root = url.replace("https://", "");
              root = root.substring(0, root.indexOf("/"));
              root = root.replace("www.", "https://www.");
          }

        return root;
    }


    /**
     * Check if the link already exists in a queue of URLs
     * @param link, the link to check
     * @param list, a Queue with URLs
     * @return true if the link exists
     */
    private boolean   isUrlExists(String link, Queue list) {
        for (Iterator it = list.iterator(); it.hasNext();) 
            {
                String s = it.next().toString();
                if (s.compareTo(link) == 0)
                    return true;
            }
        return false;
      }


    /**
     * Check if the page already exists in the database
     * @param page, the page to check
     * @return true if the page exists
     */
    private boolean   isPageExists(String page) {
        IDbManager d = DbManager.getInstance();
        Md5Manager md = new Md5Manager();
        String query = "SELECT page_md5 FROM pages";
        ResultSet results = d.execute(query);

        try {        
              while (results.next()) {
                  String md5 = results.getString("page_md5");
                  if (md.isEqualMd5String(md5, md.getMd5String(page)) == true)
                      return true;
              }
          } catch (Exception e) { System.out.println("Error checking MD5 : " + e); }
        return false;
    }


    /**
     * Run the collector
     * The main loop is here
     * Connect to the URLs of the queue
     * Create another URLs queue (newUrls) for every website
     * Store the pages in database
     */
    public void       run() {
        int i = 0;
        while (this.urls.size() > 0)
        {
          int j = 0;
          String page;
          String newRoot = new String();
          String url = (String) this.urls.poll ();
          this.urlRoot = this.getUrlRoot(url);
          System.out.println("ROOT : " + this.urlRoot);
          String rootPage = this.collectPage(url);

          System.out.println("----------------------------------------------------------");
          while (this.newUrls.size() > 0)
          {
              url = (String) this.newUrls.poll();

              // If the root is not the same, it's another website so we store it in url Queue
              newRoot = this.getUrlRoot(url);
              if (newRoot.compareTo(this.urlRoot) != 0)
              {
                  if (!isUrlExists(newRoot, urls))
                  { 
                      this.urls.add(url);
                      System.out.println("New root : " + newRoot + " ## For the url : " + url);
                  }
                  continue;
              }
              page = this.collectPage(url);
              this.storePages(page, i + "_" + j + ".html");
              this.storeSketchPages(page, "C:\\Users\\Marc\\temp\\pages\\" + i + "_" + j++ + "_sketch.html");        
          }

          this.storePages(rootPage, i + ".html");
          this.storeSketchPages(rootPage, "C:\\Users\\Marc\\temp\\pages\\" + i++ + "_sketch.html");
        }
    }

    
  
}

