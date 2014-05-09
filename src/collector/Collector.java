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
    
  Queue     urls = new LinkedList();
  String    urlRoot = new String();
  String    currentPage = new String ();
  
  Queue     newUrls = new LinkedList();
  
  
  
  
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
    public String     collectPage (MyUrl myurl) {
      StringBuilder page = new StringBuilder ();

      try 
      {
        URLConnection url_connection = myurl.getUrl().openConnection();
        InputStreamReader reader = new InputStreamReader (url_connection.getInputStream ());
        BufferedReader buffer = new BufferedReader (reader);

        String line;
        while ((line = buffer.readLine()) != null) 
          page.append(line);

      } catch (Exception e) {  System.err.println("Error : unable to collect page: " + myurl.getLink() + " ## Because :" + e); }

      return page.toString ();
    }



    /**
     * Store a sketch of the page
     * @param page
     * @param name 
     */
    public void       storePages (String page, String name, MyUrl myurl) {
        
        page = page.replaceAll("\\<div id=\"divNaoImprimir\">.*</div>", "");
        page = page.replaceAll("\\<td width=\"48%\" valign=\"top\">.*</td>", "");
        page = page.replaceAll("\\<div id=\"divImpressao\" style=\"min-height:150px;\">.*</div>", "");
        page = page.replaceAll("\\<[^<]*>", "");
        Md5Manager md = new Md5Manager();
        String md5 = md.getMd5String(page);

        IDbManager d = DbManager.getInstance();
        String query = "INSERT INTO documents(document_name, document_url, document_title, document_content, document_md5) VALUES('" + name + "', '"+ myurl.getLink() +"', '', ?, '" + md5 + "');";
        d.executeUpdatePrepared(query, page);
    }


    /**
     * Extracts url from the page and push it in the queue called newUrls
     * @param page 
     */
    public void       extractLinksCurrentPage() {
        String link = new String();

        this.currentPage = this.currentPage.toLowerCase();
        if (this.currentPage.indexOf("href=\"") == -1)
            return ;
        
        // We look for the pattern : href=""
        // Then we clean every pattern we found
        Pattern p=Pattern.compile("href=\"?[^\"]*\"?");
        Matcher m=p.matcher(this.currentPage);
        while(m.find())
        {
          int begin = m.group().indexOf("\"");
          link = m.group().substring(begin);
          link = link.replaceAll("\"","");
          link = this.relativeToAbsolute(link);
          
          if (link.indexOf("http://") == -1 || link.indexOf("http://") > 1) { continue; }
            MyUrl currentUrl = new MyUrl(link);
          
          if (!this.isUrlExistsQueue(currentUrl.getLink(), this.newUrls) && currentUrl.isFileCorrect() == true)
          {
              if (this.isRootNew(currentUrl) == false)
              {
                  this.newUrls.add(currentUrl.getLink());
                  //System.out.println("jai ajoute : " + currentUrl.getLink());
              }
          }
        }
    }


    /**
     * Convert a relative link to an absolute link using the current root of the wesite
     * @param url
     * @return url_root
     */
    private String    relativeToAbsolute(String link) {
        if (link != null && link.length() > 0 && link.charAt(0) == '/') 
        {
            link = link.replaceFirst("/", this.urlRoot + "/");
        }      
        return link;
    }

    
    /**
     * Check if the link already exists in a queue of URLs
     * @param link, the link to check
     * @param list, a Queue with URLs
     * @return true if the link exists
     */
    private boolean   isUrlExistsQueue(String link, Queue list) {
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
    private boolean   isPageExistsDB(String page) {
        IDbManager d = DbManager.getInstance();
        Md5Manager md = new Md5Manager();
        String query = "SELECT document_md5 FROM documents";
        ResultSet results = d.execute(query);

        try {        
              while (results.next()) {
                  String md5 = results.getString("document_md5");
                  if (md.isEqualMd5String(md5, md.getMd5String(page)) == true)
                      return true;
              }
          } catch (Exception e) { System.err.println("Error checking MD5 : " + e); }
        return false;
    }


    /**
     * If the root is not the than the current root : 
     *   - it's another website 
     *   - we store it in url Queue
     * @param link
     * @return true if it's a new root
     */
    private boolean   isRootNew(MyUrl myurl) {
        String newRoot = new String();
        
        newRoot = myurl.getRoot();
        if (newRoot.compareTo(this.urlRoot) != 0)
        {
            if (newRoot.indexOf("www2") != -1 || newRoot.indexOf("www3") != -1 || newRoot.indexOf("www4") != -1)
                return false;
            else if (!isUrlExistsQueue(newRoot, urls))
            { 
                this.urls.add(newRoot);
                System.out.println("New website : " + newRoot + " ## Extracted from : " + myurl.getLink());
                return true;
            }
        }
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
          System.out.println("---------------------------------");
          MyUrl myurl = new MyUrl( (String)this.urls.poll() );
          this.urlRoot = myurl.getUrl().getProtocol() + "://" + myurl.getRoot();
          this.currentPage = this.collectPage(myurl);
          if (!isPageExistsDB(myurl.getLink()))
          {
              extractLinksCurrentPage();
               try { this.storePages(this.currentPage, "document_" + ++i, myurl);} catch (Exception e) {System.err.println(e); }
          }
         
          int j = 0;
          String page;
          while (this.newUrls.size() > 0)
          {
              myurl = new MyUrl( (String)this.newUrls.poll() );
              this.currentPage = this.collectPage(myurl);
              if (!isPageExistsDB(myurl.getLink()))
              {
                extractLinksCurrentPage();
                this.storePages(this.currentPage, "document_" + i + "_" + ++j, myurl);
              }
          }
        }
    }

    
  
}




//    /**
//     * Extract the root website (without any '/') from the link
//     * @param url
//     * @return root
//     */
//    private String      getUrlRoot(String url) {
//        String root = new String();
//
//        if (url.indexOf("http://") != -1)
//          {
//              root = url.replace("http://", "");
//              root = root.substring(0, root.indexOf("/"));
//              root = root.replace("www.", "http://www.");
//          }
//        else if (url.indexOf("https://") != -1)
//          {
//              root = url.replace("https://", "");
//              root = root.substring(0, root.indexOf("/"));
//              root = root.replace("www.", "https://www.");
//          }
//
//        return root;
//    }