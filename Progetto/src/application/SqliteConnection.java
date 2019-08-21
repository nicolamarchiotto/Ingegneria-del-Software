package application;

import java.sql.*;
import java.util.List;


//classe per la gestione delle operazioni su DB
public class SqliteConnection {
	
	
	//metodo per instanziare una connessione
	public static Connection dbConnector() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connect = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\utente\\git\\Progetto-Ingegneria-Software-2019\\Progetto\\userDB.db");
			System.out.println("\nConnected to da DB!"); 
			return connect;
		}
		catch(Exception e) {
			System.out.println("\nHeya something's wrong!");
			return null;
		}
	}
	
	
	//metodo per l'inserimento di valori all'interno del DB inserendo solo certi campi, tableName contiene il nome della tabella, objectList gli oggetti veri e propri da inserire
	public static void insertIntoDB(String tableName, List<?> objectList) {
		
		Connection connect = SqliteConnection.dbConnector();
		
		String sql = "";
		if(!objectList.isEmpty()){
			if(objectList.get(0) instanceof Libro) {
				int fakeISBN = 0; //FIXME fino a quando non creeremo uno vero
				for(Object libro : objectList) {
					sql = "INSERT INTO BookList VALUES";
					Libro book = (Libro)libro;
					sql += "("    +    fakeISBN++   +/* + book.getIsbn().get() + */",\n'";
					sql += book.getTitolo() + "',\n'";
					sql += book.getAutore() + "',\n";
					sql += book.getAnnoPublicazione() + ",\n'";
					sql += book.getCasaEditrice() + "',\n'";
					sql += book.getGenere() + "',\n";
					sql += book.getPrezzo() + ",\n'";
					sql += book.getBreveDescrizione() + "',\n";
					sql += book.getPosizione() + ",\n";
					sql += book.getPunti() + ")";
					sql += ";";
					
					//FIXME System.out.println("TUTTO OK?\n" + sql);
					

					try {
						Statement stmt = connect.createStatement();
						stmt.executeUpdate(sql);
					}
					catch(SQLException e) {
						System.out.println(e.getMessage());
					}
					
					}
			}
			else if(objectList.get(0) instanceof User) {
				for(Object utente : objectList) {
					sql += "INSERT INTO UserList VALUES";
					User user = (User)utente;
					sql += "('" + user.getEmail() + "',\n'";
					sql += user.getPw() + "')";
					sql += ";";
					
					//FIXME System.out.println("TUTTO OK?\n" + sql);
					

					try {
						Statement stmt = connect.createStatement();
						stmt.executeUpdate(sql);
					}
					catch(SQLException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}
	}
}
	

		
		
	

