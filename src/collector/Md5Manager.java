package collector;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Marc
 */
public class Md5Manager {
    MessageDigest   md;
    
    public          Md5Manager() {    }
    
    /**
     * Get the MD5 from string
     * Convert the MD5 into a string
     * @param str to get the MD5 from
     * @return md5
     * @throws NoSuchAlgorithmException 
     */
    public String   getMd5String(String str) throws NoSuchAlgorithmException {        
        this.md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte byteData[] = md.digest();
        
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        return sb.toString();
    }
    
    /**
     * Get the MD5 from string
     * @param str to get the MD5 from
     * @return md5
     * @throws NoSuchAlgorithmException 
     */
    public byte[]   getMd5(String str) throws NoSuchAlgorithmException {        
        this.md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte byteData[] = md.digest();
        
        return byteData;
    }
    
    /**
     * Compare two MD5s
     * @param md5_1
     * @param md5_2
     * @return true if the two MD5 are equals
     */
    public boolean  isEqualMd5String(String md5_1, String md5_2) {
        if (md5_1.compareTo(md5_2) == 0)
            return true;
        return false;
    }
}
