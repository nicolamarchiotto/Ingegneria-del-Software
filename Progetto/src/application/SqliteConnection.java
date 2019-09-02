package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



/*
 PER TESTARE L'INSERIMENTO DI UN ORDINE, Trasformare i vari isbn in un unica stringa e poi ripescare i libri dall'ordine, ritrasformando la stringa in lista di libri
			ResultSet booksFromDB = SqliteConnection.getEverythingFromTableDB("BookList");
			List<Libro> libri = new ArrayList<Libro>();
			
			try {
				while(booksFromDB.next()) {
					libri.add(new Libro(booksFromDB.getString("titolo"), booksFromDB.getString("autore"), booksFromDB.getString("casaEditrice"), booksFromDB.getInt("annoPubblicazione"), booksFromDB.getString("isbn"), booksFromDB.getString("genere"), booksFromDB.getDouble("prezzo"), booksFromDB.getString("breveDescrizione"), booksFromDB.getInt("puntiCarta")));
				}
				Ordine prova = new Ordine(new User("nicola", "gugole","asdaa", "37059", "verona", "347112", "nicola.gugole@gmail.com", "password"), "non lo so", libri.get(0), libri.get(1), libri.get(0));
				List<Ordine> provaa = new ArrayList<Ordine>();
				provaa.add(prova);
				SqliteConnection.insertOrder(provaa);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			List<Libro> listaLibri = SqliteConnection.isbnStringToBookList(SqliteConnection.getEveryFieldOrdine().getString("libriOrdine"));
			for(Libro l : listaLibri) System.out.println(l.toStringLong());
*/





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
			//**********inserire Libri***********//
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
					sql += book.getPunti() + ")";
					sql += ";";
					
					

					try {
						Statement stmt = connect.createStatement();
						stmt.executeUpdate(sql);
					}
					catch(SQLException e) {
						System.out.println(e.getMessage());
					}
					
					}
			}
			//**********inserire Utenti***********//
			else if(objectList.get(0) instanceof User) {
				for(Object utente : objectList) {
					User user = (User)utente;
					String libroCard = null;
					if(user.getLibroCard() != null) { //utente registrato, creo la riga nelle tabelle BookCardList e DateList
						sql += "INSERT INTO BookCardList VALUES";
						libroCard = user.getLibroCard().getId();
						sql += "('" + libroCard + "',\n";
						sql += user.getLibroCard().getPunti() + ");\n\n";

						sql += "INSERT INTO DateList VALUES";
						sql += "('" + user.getLibroCard().getId() + "',\n";
						sql += user.getLibroCard().getDataEmissione().getDayOfMonth() + ",\n";
						sql += user.getLibroCard().getDataEmissione().getMonthValue() + ",\n";
						sql += user.getLibroCard().getDataEmissione().getYear() + ",\n";
						sql += user.getLibroCard().getDataEmissione().getHour() + ");\n\n";
					}
					sql += "INSERT INTO UserList VALUES";
					sql += "('" + user.getEmail() + "',\n'";
					sql += user.getPw() + "',\n'";
					sql += user.getNome() + "',\n'";
					sql += user.getCognome() + "',\n'";
					sql += user.getIndirizzi() + "',\n";
					sql += user.getCap() + ",\n'";
					sql += user.getCitta() + "',\n";
					sql += user.getTelefono() + ",\n'";
					sql += libroCard + "')";
					//sql += salvarsi il collegamento agli ordini
					sql += ";";
					
					

					try {
						Statement stmt = connect.createStatement();
						stmt.executeUpdate(sql);
					}
					catch(SQLException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			//**********inserire Ordini***********//
			else if(objectList.get(0) instanceof Ordine) {
				for(Object ordine : objectList) {
					Ordine order = (Ordine) ordine;
					sql += "INSERT INTO DateList VALUES\n";
					sql += "('" + order.getId() + "',\n";
					sql += order.getData().getDayOfMonth() + ",\n";
					sql += order.getData().getMonthValue() + ",\n";
					sql += order.getData().getYear() + ",\n";
					sql += order.getData().getHour() + ");\n\n";
					
					sql += "INSERT INTO OrderList VALUES\n";
					sql += "('" + order.getId() + "',\n'";
					sql += order.getUserId()+ "',\n'";
					sql += SqliteConnection.bookListToISBNString(order.getLibriOrdine()) + "',\n";
					sql += order.getTotalCost() + ",\n'";
					sql += order.getPaymentType() + "',\n";
					sql += order.getSaldoPuntiOrdine() + ",\n'";
					sql += order.getStato() + "');";
					
					
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
	
	//metodo per ricevere tutti i campi di tutte le colonne di una SINGOLA TABELLA
	public static ResultSet getEverythingFromTableDB(String tableName) {
		String sql = "SELECT * FROM " + tableName;
		if(tableName.equals("UserList")) { //LEFT OUTER JOIN può essere considerato una espansione di INNER JOIN
			sql += " LEFT OUTER JOIN BookCardList ON BookCardList.id = UserList.libroCard "
					+" LEFT OUTER JOIN DateList ON BookCardList.id = DateList.id";
		}
		else if(tableName.equals("OrderList")) {
			sql += " INNER JOIN DateList ON OrderList.id = DateList.id";
		}
			
		try {
			Statement stmt = SqliteConnection.dbConnector().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	
	//metodo per ricevere i campi delle colonne richieste di una SINGOLA TABELLA
	public static ResultSet getFromTableDB(String tableName, List<String> columnList) {
		String sql = "SELECT ";
		int iterator = 1;
		
		for(String singleColumn : columnList) {
			sql += singleColumn + (columnList.size() == iterator++ ? " " : ", "); 
		}
		
		sql += "FROM " + tableName;
		
		try {
			Statement stmt = SqliteConnection.dbConnector().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	
	
	
	
	
	
	//metodo per trasformare la lista di libri interna all'ordine in una stringa di isbn divisi da #
	private static String bookListToISBNString(List<Libro> bookList) {
		String isbnString = "";
		for(Libro book : bookList) {
			isbnString += book.getIsbn();
			isbnString += "#";
		}
		return isbnString.substring(0, isbnString.length() - 1); //con il -1 tolgo l'ultimo #
	}
	
	
	//metodo per prendere una stringa di isbn separati tra loro da # e ritornare i libri equivalenti presenti all'interno del DB
	public static List<Libro> isbnStringToBookList(String isbnString){
		List<Libro> bookList = new ArrayList<Libro>();
		String isbnArray[] = isbnString.split("#");
		System.out.println("Ahah that's my shit: " + isbnArray.toString());
		
		if(isbnArray.length != 0) {
			Connection connect = SqliteConnection.dbConnector();
			String sql = "";
			for(String isbn : isbnArray) {
				sql = "SELECT * FROM BookList\n WHERE BookList.isbn = " + isbn;
				try {
					Statement stmt = SqliteConnection.dbConnector().createStatement();
					ResultSet booksFromDB = stmt.executeQuery(sql);
					bookList.add(new Libro(booksFromDB.getString("titolo"), booksFromDB.getString("autore"), booksFromDB.getString("casaEditrice"), booksFromDB.getInt("annoPubblicazione"), booksFromDB.getString("isbn"), booksFromDB.getString("genere"), booksFromDB.getDouble("prezzo"), booksFromDB.getString("breveDescrizione"), booksFromDB.getInt("puntiCarta")));
				}
				catch(SQLException e) {
					System.out.println(e.getMessage());
				}
			}
			return bookList;
		}
		return null; //caso in cui non ci sia nessun isbn
	}
	
	
	//-----------------------//
	//------METODI USER------//
	//-----------------------//

	//inserisci User
	public static void insertUser(List<User> objectList) {
		SqliteConnection.insertIntoDB("UserList", objectList);
	}
	
	//prendi tutta la tabella User
	public static ResultSet getEveryFieldUser() {
		return SqliteConnection.getEverythingFromTableDB("UserList");
	}
	
	//prendi solo i campi richiesti da la tabella User
	public static ResultSet getFieldUser(List<String> columnList) {
		return SqliteConnection.getFromTableDB("UserList", columnList);
	}
	
	//metodo per ritornare una lista di User data una ResultSet della tabella User
	public static List<User> getUserList(ResultSet usersFromDB){
		List<User> userList = new ArrayList<User>();
		try {
			while(usersFromDB.next()) {
				userList.add(new User(usersFromDB.getString("nome"), usersFromDB.getString("cognome"), usersFromDB.getString("indirizzo"), usersFromDB.getString("cap"), usersFromDB.getString("citta"), usersFromDB.getString("telefono"), usersFromDB.getString("email"), usersFromDB.getString("password"), usersFromDB.getString("libroCard"), usersFromDB.getInt("punti"), usersFromDB.getInt("giorno"), usersFromDB.getInt("mese"), usersFromDB.getInt("anno"), usersFromDB.getInt("ora")));
			}
			for(User user : userList) {
				System.out.println(user.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return userList;
	}
	
	
	//------------------------//
	//------METODI LIBRO------//
	//------------------------//
	
	//inserisci Libro
	public static void insertLibro(List<Libro> objectList) {
		SqliteConnection.insertIntoDB("BookList", objectList);
	}
	
	//prendi tutta la tabella Libro
	public static ResultSet getEveryFieldLibro() {
		return SqliteConnection.getEverythingFromTableDB("BookList");
	}
	
	//prendi solo i campi richiesti da la tabella Libro
	public static ResultSet getFieldLibro(List<String> columnList) {
		return SqliteConnection.getFromTableDB("BookList", columnList);
	}
	
	
	//-------------------------//
	//------METODI ORDINE------//
	//-------------------------//
	
	//inserisci Ordine
	public static void insertOrder(List<Ordine> objectList) {
		SqliteConnection.insertIntoDB("OrderList", objectList);
	}
	
	//prendi tutta la tabella Ordine
	public static ResultSet getEveryFieldOrdine() {
		return SqliteConnection.getEverythingFromTableDB("OrderList");
	}
	
	//prendi solo i campi richiesti da la tabella Ordine
	public static ResultSet getFieldOrdine(List<String> columnList) {
		return SqliteConnection.getFromTableDB("OrderList", columnList);
	}
	
}
	

		
		
	

