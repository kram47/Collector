package collector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import sun.print.BackgroundLookupListener;



/**
 *
 * @author Marc
 */
public class DbManager implements IDbManager {

  /* ---------------------------------------------------------------- */
  /* ---------------------- PROPERTIES ------------------------------ */
    
	/** L'URL de la base de donnees avec laquelle cette classe s'interface. */
	private String				url;
	/** Le nom d'utilisateur de connexion */
	private String				user;
	/** Le mot de passe de connexion */
	private String 				password;
	/** stockage de l'objet <code>connection</code> */
	private Connection			connection;
	/** stockage de l'objet <code>dbmanager</code> */
	private static IDbManager               dbmanager;

  
  /* ---------------------------------------------------------------- */
  /* ------------------ CONSTRUCTOR (Singleton) --------------------- */        
        
        
	/** Encapsulation du constructeur (singleton) */
	public static IDbManager getInstance(){
		if (dbmanager == null)
			dbmanager = new DbManager("", "", "");
		return dbmanager;
	}
        
	/** Constructeur privé (singleton) */
	private DbManager(String url, String user, String password){
		this.connection = null;
		this.url = url;
		this.user = user;
		this.password = password;
	}
        
        
        
  /* ---------------------------------------------------------------- */
  /* --------------------- GETTER-SETTER ---------------------------- */        
        
        
	public String       getUrl() {
		return url;
	}
	public void         setUrl(String url) {
		this.url = url;
	}
	public String       getUser() {
		return user;
	}
	public void         setUser(String user) {
		this.user = user;
	}
	public String       getPassword() {
		return password;
	}
	public void         setPassword(String password) {
		this.password = password;
	}
        public Connection   getConnection() {
		return connection;
	}

  
  /* ---------------------------------------------------------------- */
  /* ------------------------ METHODS ------------------------------- */        
        
        
	/**
	 * Etablit la connexion avec la base de données uniquement si la connexion n'a pas deja été établie.
	 * 
	 * @throws RuntimeException si un problème de connexion survient
	 */
	public void         connect() throws RuntimeException {
		if (connection == null){
			try{
				connection = DriverManager.getConnection(url, user, password);
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
	public void         close() throws RuntimeException {
		if (connection != null){
			try{
				connection.close();
				connection = null;
			}
			catch (SQLException ex){
				throw new RuntimeException(ex.getMessage());
			}
		}
	}
        
        
        /**
	 * Exécute une requète.
	 * 
	 * 
	 */
        public ResultSet    execute(String requete) {
            ResultSet result = null;
            
            try {
                Statement stmt = connection.createStatement();
                result = stmt.executeQuery(requete);
            }
            catch (Exception e) { 
                System.err.println(e);
            }
            
            return result;
        }
        
        /**
	 * Exécute une requète type insertInto
	 * 
	 */
        public void         executeUpdate(String requete) {
            try {
                Statement stmt = connection.createStatement();
                stmt.executeUpdate(requete);
            }
            catch (Exception e) { 
                System.err.println(e);
            }

        }
        
        /**
	 * Exécute une requète type insertInto en prepared Statement
	 * 
	 */
        public void         executeUpdatePrepared(String query, String pageHtml) {
            try {
                PreparedStatement preparedStatement = this.connection.prepareStatement(query);
                preparedStatement.setString(1, pageHtml);
                preparedStatement.executeUpdate(query);
            }
            catch (Exception e) { 
                //System.err.println(e);
                
            }

        }
}
