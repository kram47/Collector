/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package collector;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Encapsulation of an URL
 * @author Marc
 */
public class MyUrl {
    
  /* ---------------------------------------------------------------- */
  /* ---------------------- PROPERTIES ------------------------------ */
    
  private String link = new String();  
  private URL url; 
    
    
  /* ---------------------------------------------------------------- */
  /* ---------------------- CONSTRUCTOR  ---------------------------- */        
    
    /**
     * Constructor of the class
     * @param _link Access link of the URL
     */
    public MyUrl (String _link) {
        this.link = _link;
        try {
            if (_link.indexOf("www") == 0)
            {
                _link = "http://" + _link;
            }
            this.url = new URL(_link);
        } catch (Exception e) { System.err.println(_link + "\n[MYURL-Constructor]" + e); }
    }
    
  /* ---------------------------------------------------------------- */
  /* --------------------- GETTER-SETTER ---------------------------- */        

    public String getLink () {
        return this.link;
    }
    
    public void setLink (String link) {
        this.link = link;
    }
    
    public URL getUrl () {
        return this.url;
    }
    
    public void setUrl (URL _url) {
        this.url = _url;
    }
    
  /* ---------------------------------------------------------------- */
  /* ------------------------ METHODS ------------------------------- */            
    
//    public String    relativeToAbsolute(String root) {
//        String url = link.replaceFirst("/", root + "/");      
//        return url;
//    }
    
    public String   getFile()
    {
        return url.getFile();
    }
    
    
    /**
     * Extract the root website (without any '/') from the link
     * @param url
     * @return root
     */
    public String    getRoot() {
        return url.getHost();
    }
    
    public String   getInfo()
    {
        StringBuilder ret = new StringBuilder();

        try {
            ret.append("Host : ").append(url.getHost());
            ret.append("\nFile : ").append(url.getFile());
            ret.append("\nURI : ").append(url.toURI());
            ret.append("\nProtocole : ").append(url.getProtocol());
            
        } catch (Exception e) { System.err.append("[MY_URL-URI] " + e); }
        
        return ret.toString();
    }
    
    /**
     * Get rid of css, javascript, icon, # link, empty strings, internal server like '../'
     */ 
    public boolean isFileCorrect()
    {
        String[] extensions = { "css", "scss", "sass", "js", 
                                "ico", "jpg", "jpeg", "bmp", "png", "psd", "jpf", "pdf",
                                "mp3", "mp4", "wav", "flac", "ogg", "m4a", "aac", "m3u",
                                "m2k", "avi", "mkv", "wmp", "mov"};
        
        for (String ext : extensions)
            if (link.indexOf("." + ext) != -1)
                return false;
        
        if (link.equals("") || link == null)
            return false;
        if (link.indexOf("#") != -1 || link.indexOf("../") != -1 || link.indexOf("javascript") != -1 || link.indexOf("mailto:") != -1)
            return false;
        return true;
    }
    
    
    
    
    
}
