package collector;

import java.sql.ResultSet;

public interface IDbManager {
	public void connect() throws RuntimeException;
	public void close() throws RuntimeException;
        public ResultSet execute(String requete);
	public void setUrl(String url);
	public void setUser(String url);
	public void setPassword(String url);
	public String getUrl();
	public String getUser();
	public String getPassword();
        
}
