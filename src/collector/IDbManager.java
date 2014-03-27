package collector;

import java.sql.Connection;
import java.sql.ResultSet;

public interface IDbManager {
	public void         connect() throws RuntimeException;
	public void         close() throws RuntimeException;
        public ResultSet    execute(String requete);
        public void         executeUpdate(String requete);
	public void         setUrl(String url);
	public void         setUser(String url);
	public void         setPassword(String url);
	public String       getUrl();
	public String       getUser();
	public String       getPassword();
        public Connection   getConnection();
        
}
