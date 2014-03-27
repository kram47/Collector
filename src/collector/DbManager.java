package collector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;



/**
 * Classe repr�sentant l'ORM.
 * 	Ex�cute les preparedStatements et renvoie la clef g�n�r�e ou les resultats de la requ�te
 * 	Encapsule la connection � la BDD.
 * 
 * @author Marc CHARTON, Florian GUIHO
 *
 */
public class DbManager implements IDbManager {

	/** L'URL de la base de donn�es avec laquelle cette classe s'interface. */
	private String				url;
	/** Le nom d'utilisateur de connexion */
	private String				user;
	/** Le mot de passe de connexion */
	private String 				password;
	/** stockage de l'objet <code>connection</code> */
	private Connection			connexion;
	/** stockage de l'objet <code>dbmanager</code> */
	private static IDbManager                dbmanager;
	
	/** Encapsulation du constructeur (singleton) */
	public static IDbManager getInstance(){
		if (dbmanager == null)
			dbmanager = new DbManager("", "", "");
		return dbmanager;
	}
	
	/** Constructeur priv� (singleton) */
	private DbManager(String url, String user, String password){
		this.connexion = null;
		this.url = url;
		this.user = user;
		this.password = password;
	}
	public String   getUrl() {
		return url;
	}
	public void     setUrl(String url) {
		this.url = url;
	}
	public String   getUser() {
		return user;
	}
	public void     setUser(String user) {
		this.user = user;
	}
	public String   getPassword() {
		return password;
	}
	public void     setPassword(String password) {
		this.password = password;
	}

	/**
	 * Etablit la connexion avec la base de donn�es uniquement si la connexion n'a pas deja �t� �tablie.
	 * 
	 * @throws RuntimeException si un probl�me de connexion survient
	 */
	public void	connect() throws RuntimeException{
		if (connexion == null){
			try{
				connexion = DriverManager.getConnection(url, user, password);
			}
			catch (SQLException ex){
				throw new RuntimeException(ex.getMessage());
			}
		}
	}
	
	/**
	 * Relache la connexion.
	 * 
	 * @throws RuntimeException
	 */
	public void     close() throws RuntimeException{
		if (connexion != null){
			try{
				connexion.close();
				connexion = null;
			}
			catch (SQLException ex){
				throw new RuntimeException(ex.getMessage());
			}
		}
	}
        
        
        /**
	 * Execute une requete.
	 * 
	 * 
	 */
        public ResultSet execute(String requete)
        {
            ResultSet result = null;
            
            try {
                Statement stmt = connexion.createStatement();
                result = stmt.executeQuery(requete);
            }
            catch (Exception e) { 
                System.out.println(e);
            }
            
            return result;
        }
}
