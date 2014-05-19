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
import test.Tools;

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
    public void       storePages(String page, String name, MyUrl myurl) 
    {
        String title = "";
        Pattern p=Pattern.compile("<title>([^<]+)<\\/title>");
        Matcher m=p.matcher(this.currentPage);
        if (m.find())
        {
            title = m.group().replaceAll("\\s","");
            title = title.replace("<title>","");
            title = title.replace("</title>","");
            if (title.length() > 30)
                title = title.substring(0,20) + "...";
        }

        page = page.replaceAll("\\<script[^>]*?>.*?</script>", "");
        page = page.replaceAll("\\<[^<]*>", "");
        page = page.replaceAll("\\<!--*-->", "");
        page = Tools.replaceAccents(page);
        Md5Manager md = new Md5Manager();
        String md5 = md.getMd5String(page);

        IDbManager d = DbManager.getInstance();
        String query = "INSERT INTO documents(document_name, document_url, document_title, document_content, document_md5) VALUES('" + name + "', '"+ myurl.getLink() +"', '"+title+"', ?, '" + md5 + "');";
        System.out.println(query);
        d.executeUpdatePrepared(query, page);
    }


    /**
     * Extracts url from the page and push it in the queue called newUrls
     * @param page 
     */
    public void       extractLinksCurrentPage(MyUrl myurl) {
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
          link = this.relativeToAbsolute(link, myurl);
          
          if (link.indexOf("http://") == -1 || link.indexOf("http://") > 1) { continue; }
            MyUrl currentUrl = new MyUrl(link);
          
          if (currentUrl.isFileCorrect() == true && this.urlRoot.compareTo(currentUrl.getLink()) != 0)
          {
              if (!this.isUrlExistsQueue(currentUrl.getLink(), this.newUrls) && !this.isUrlExistsQueue(currentUrl.getLink(), this.urls))
                if (this.isRootNew(currentUrl) == false)
                {
                    this.newUrls.add(currentUrl.getLink());
                    System.out.println("I add : " + currentUrl.getLink());
                }
          }
        }
    }


    /**
     * Convert a relative link to an absolute link using the current root of the wesite
     * @param url
     * @return url_root
     */
    private String    relativeToAbsolute(String link, MyUrl myurl) {
        String current_url = myurl.getLink();
        String protoc = "http://";
        
        // We get rid of the protocole and keep it to use it again after
        if (current_url.indexOf("http://") != -1)
            current_url = current_url.replace("http://", "");
        if (current_url.indexOf("https://") != -1)
        {
            current_url = current_url.replace("https://", "");
            protoc = "https://";
        }
        
        if (link != null && link.length() > 0  && link.indexOf("http") == -1 && link.indexOf("www.") == -1) 
        {
            if (link.charAt(0) == '/')
                link = link.replaceFirst("/", this.urlRoot + "/");   
            else 
            {
                 if (current_url.indexOf("/") != -1)
                 {
                    int lastSlash = current_url.lastIndexOf("/");
                    String lastPart = current_url.substring(lastSlash);
                    if (lastPart.indexOf(".") != -1)
                    {
                        current_url = current_url.substring(0, lastSlash);
                    }  
                 }
                 link = protoc + current_url + "/" + link;
            }
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
    private boolean   isPageExistsDB(String url) {
        IDbManager d = DbManager.getInstance();
        Md5Manager md = new Md5Manager();
        String query = "SELECT document_md5, document_url FROM documents";
        ResultSet results = d.execute(query);

        try {        
              while (results.next()) {
                  String document_md5 = results.getString("document_md5");
                  String document_url = results.getString("document_url");
                  if (md.isEqualMd5String(document_md5, md.getMd5String(url)) == true)
                      return true;
                  if (document_url.compareTo(url) == 0)
                  {
                      System.out.println("L'url de cette page existe en BDD, je l'ajoute pas");
                      return true;
                  }
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
        if (newRoot.indexOf("http://") == -1)
            newRoot = "http://" + newRoot;
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
          System.out.println("We gonna work on : " + myurl.getLink());
          this.urlRoot = myurl.getUrl().getProtocol() + "://" + myurl.getRoot();
          System.out.println("Root : " + this.urlRoot);
          this.currentPage = this.collectPage(myurl);
          if (!isPageExistsDB(myurl.getLink()))
          {
              extractLinksCurrentPage(myurl);
              try { this.storePages(this.currentPage, "document_" + ++i, myurl);} catch (Exception e) {System.err.println(e); }
          }
         
          int j = 0;
          String page;
          while (this.newUrls.size() > 0)
          {
              myurl = new MyUrl( (String)this.newUrls.poll() );
              System.out.println("We gonna work on : " + myurl.getLink());
              this.currentPage = this.collectPage(myurl);
              if (!isPageExistsDB(myurl.getLink()))
              {
                extractLinksCurrentPage(myurl);
                this.storePages(this.currentPage, "document_" + i + "_" + ++j, myurl);
              }
          }
        }
    }
    
  
}
