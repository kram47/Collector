/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

/**
 * Package of useful tools (All methods are static)
 * @author Marc
 */
public class Tools {
    
    /* ---------------------------------------------------------------- */
    /* ---------------------- PROPERTIES ------------------------------ */  

    public static final String  ANSI_RESET =    "\u001B[0m";
    public static final String  ANSI_BLACK =    "\u001B[30m";
    public static final String  ANSI_RED =      "\u001B[31m";
    public static final String  ANSI_GREEN =    "\u001B[32m";
    public static final String  ANSI_YELLOW =   "\u001B[33m";
    public static final String  ANSI_BLUE =     "\u001B[34m";
    public static final String  ANSI_PURPLE =   "\u001B[35m";
    public static final String  ANSI_CYAN =     "\u001B[36m";
    public static final String  ANSI_WHITE =    "\u001B[37m";

    /* ---------------------------------------------------------------- */
    /* ---------------------- CONSTRUCTOR  ---------------------------- */        

    public Tools()
    {
        
    }


    /* ---------------------------------------------------------------- */
    /* --------------------- GETTER-SETTER ---------------------------- */        




    /* ---------------------------------------------------------------- */
    /* ------------------------ METHODS ------------------------------- */     

    /**
     * Replace all type of accent with the same letter without accent <br/>
     * Works with direct accent like : 'é' and HTML encoding like : '&eacute;'
     * 
     * @param page the string we want to get rid of the accent from 
     * @return 
     */
    public static String        replaceAccents(String page)
    {
        /** 
         * Replacing the HTML encoding 
         * '&eacute;' => 'é' => e
         */
        page = page.replaceAll("\\&(.)(.*?);", "$1");
        
        /**
         * Replacing directly the accents 
         * 'é' => 'e'
         */
        String[][] accents = {  {"e", "é", "è", "ê"}, 
                                {"a", "á", "à", "â", "ã"},
                                {"i", "í", "ì", "î"},
                                {"o", "ó", "ò", "ô", "õ"},
                                {"u", "ú", "ù", "û"},
                                {"c", "ç"},
                                {"n", "ñ"} };
        
        for (String[] letters : accents)
        {
            String withoutAccent = letters[0];
            for (String pattern : letters)
            {
                if (pattern != withoutAccent)
                    page = page.replaceAll(pattern, withoutAccent);
            }
        }
        
        return page;
    }

    
    /**
     * Splt a string in a list of words. <br />
     * Use the \s and \W to split (All type of spaces and non-character)
     * @param str
     * @return 
     */
    public static String[]      splitStringByWords(String str)
    {
        String[] words = str.split("[\\W\\s]+");
        
        return words;
    }


}
