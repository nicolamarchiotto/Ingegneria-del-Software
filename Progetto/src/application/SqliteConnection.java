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
				for(Object libro : objectList) {
					sql = "INSERT INTO BookList VALUES";
					Libro book = (Libro)libro;
					sql += "(" + book.getIsbn() + ",\n'";
					sql += book.getTitolo() + "',\n'";
					sql += book.getAutore() + "',\n";
					sql += book.getAnnoPublicazione() + ",\n'";
					sql += book.getCasaEditrice() + "',\n'";
					sql += book.getGenere() + "',\n";
					sql += book.getPrezzo() + ",\n'";
					sql += book.getBreveDescrizione() + "',\n";
					sql += book.getCopieVendute() + ",\n";
					sql += book.getPunti() + ");";
					
					
					Statement stmt = null;
					
					try {
						stmt = connect.createStatement();
						stmt.executeUpdate(sql);
					}
					catch(SQLException e) {
						System.out.println(e.getMessage());
					}
					finally {
						if(stmt != null)
							try {
								stmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
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
					sql += ";";
					
					Statement stmt = null;
					
					try {
						stmt = connect.createStatement();
						stmt.executeUpdate(sql);
					}
					catch(SQLException e) {
						System.out.println(e.getMessage());
					}
					finally {
						if(stmt != null)
							try {
								stmt.close();
							} catch (SQLException e) {								
								e.printStackTrace();
							}
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
					sql += SqliteConnection.bookListToISBNString(order.getLibri()) + "',\n";
					sql += order.getTotalCost() + ",\n'";
					sql += order.getPaymentType() + "',\n";
					sql += order.getSaldoPuntiOrdine() + ",\n'";
					sql += order.getStato() + "');";
					
					
					Statement stmt = null;
					
					try {
						stmt = connect.createStatement();
						stmt.executeUpdate(sql);
					}
					catch(SQLException e) {
						System.out.println(e.getMessage());
					}
					finally {
						if(stmt != null)
							try {
								stmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
					}
				}
			}
		}
	}
	
	
	//metodo per l'aggiornamento di valori all'interno del DB 
	
	public static void updateDB(String tableName, List<?> objectList) {
		
		Connection connect = SqliteConnection.dbConnector();
		
		String sql = "";
		if(!objectList.isEmpty()){
			//**********aggiornare Libri************//
			if(objectList.get(0) instanceof Libro) {
				for(Object libro : objectList) {
					Libro book = (Libro)libro;
					
					sql += "UPDATE BookList \nSET "; //non voglio permettere il variare i campi PRIMARY KEY O UNIQUE quindi non c'è update del campo isbn
					sql += "autore = '" + book.getAutore() + "',\n";
					sql += "annoPubblicazione = " + book.getAnnoPublicazione() + ",\n";
					sql += "casaEditrice = '" + book.getCasaEditrice() + "',\n";
					sql += "genere = '" + book.getGenere() + "',\n";
					sql += "prezzo = " + book.getPrezzo() + ",\n";
					sql += "breveDescrizione = '" + book.getBreveDescrizione() + "',\n";
					sql += "copieVenduteTotali = " + book.getCopieVendute() + ",\n";
					sql += "puntiCarta = " + book.getPunti() +"\n";
					sql += "WHERE isbn = " + book.getIsbn() + ";";
					
					Statement stmt = null;
					
					try {
						stmt = connect.createStatement();
						stmt.executeUpdate(sql);
					}
					catch(SQLException e) {
						System.out.println(e.getMessage());
					}
					finally {
						if(stmt != null)
							try {
								stmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
					}
				}
			}
			
			//**********aggiornare Utenti***********//
			if(objectList.get(0) instanceof User) {
				for(Object utente : objectList) {
					User user = (User)utente; //non voglio permettere il variare i campi PRIMARY KEY o UNIQUE (vedi stessa situazione in UPDATE Libri)
					//aggiorno la BookCard
					sql += "UPDATE BookCardList \nSET ";
					sql += "punti = " + user.getLibroCard().getPunti() + "\n";
					sql += "WHERE id = " + user.getLibroCard().getId() + ";\n\n";
							
					//aggiorno la EmissionDate della BookCard
					sql += "UPDATE DateList \nSET ";
					sql += "giorno = " + user.getLibroCard().getDataEmissione().getDayOfMonth() + ",\n";
					sql += "mese = " + user.getLibroCard().getDataEmissione().getMonthValue() + ",\n";
					sql += "anno = " + user.getLibroCard().getDataEmissione().getYear() + ",\n";
					sql += "ora = " + user.getLibroCard().getDataEmissione().getHour() + "\n";
					sql += "WHERE id = " + user.getLibroCard().getId() + ";\n\n";
					
					//aggiorno infine lo User
					sql += "UPDATE UserList \nSET ";
					sql += "password = '" + user.getPw() + "',\n";
					sql += "nome = '" + user.getNome() + "',\n";
					sql += "cognome = '" + user.getCognome() + "',\n";
					sql += "indirizzo = '" + user.getIndirizzi() + "',\n";
					sql += "cap = " + user.getCap() + ",\n";
					sql += "citta = '" + user.getCitta() + "',\n";
					sql += "telefono = " + user.getTelefono() + ",\n";
					sql += "WHERE email = " + user.getEmail() + " AND libroCard = " + user.getLibroCard().getId() + ";";
					
					Statement stmt = null;
					
					try {
						stmt = connect.createStatement();
						stmt.executeUpdate(sql);
					}
					catch(SQLException e) {
						System.out.println(e.getMessage());
					}
					finally {
						if(stmt != null)
							try {
								stmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
					}
				}
			}
			
			//**********aggiornare Ordini***********//
			if(objectList.get(0) instanceof Ordine) {
				for(Object ordine : objectList) {
					Ordine order = (Ordine)ordine;//non voglio permettere il variare i campi PRIMARY KEY o UNIQUE (vedi stessa situazione in UPDATE Libri)
					
					//aggiorno prima la Date relativa all'ordine
					sql += "UPDATE DateList \nSET ";
					sql += "giorno = " + order.getData().getDayOfMonth() + ",\n";
					sql += "mese = " + order.getData().getMonthValue() + ",\n";
					sql += "anno = " + order.getData().getYear() + ",\n";
					sql += "ora = " + order.getData().getHour() + "\n";
					sql += "WHERE id = " + order.getId() + ";\n\n";
					
					//aggiorno infine l'Ordine
					sql += "UPDATE OrderList \nSET ";
					sql += "libriOrdine = '" + SqliteConnection.bookListToISBNString(order.getLibri()) + "',\n";
					sql += "totalCost = " + order.getTotalCost() + ",\n";
					sql += "paymentType = '" + order.getPaymentType() + "',\n";
					sql += "saldoPuntiOrdine = " + order.getSaldoPuntiOrdine() + ",\n";
					sql += "stato = '" + order.getStato() + "',\n";
					sql += "WHERE id = " + order.getId() + " AND user = " + order.getUserId() + ";";
					
					Statement stmt = null;
					
					try {
						stmt = connect.createStatement();
						stmt.executeUpdate(sql);
					}
					catch(SQLException e) {
						System.out.println(e.getMessage());
					}
					finally {
						if(stmt != null)
							try {
								stmt.close();
							} catch (SQLException e) {
								e.printStackTrace();
							}
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
	
	
	
	
	
	
	//metodo per fare update in fase di logOut
	public static void savingOnLogOut(User user) {
		SqliteConnection.updateUser(user);
		SqliteConnection.updateOrdine(user.getOrdini());
	}
	
	
	//-----------------------//
	//------METODI USER------//
	//-----------------------//

	//inserisci User
	public static void insertUser(List<User> objectList) {
		SqliteConnection.insertIntoDB("UserList", objectList);
	}

	//inserisci singolo User
	public static void insertUser(User user) {
		List<User> userList = new ArrayList<User>();
		userList.add(user);
		SqliteConnection.insertUser(userList);
	}
	

	//aggiorna insieme di User
	public static void updateUser(List<User> objectList) {
		SqliteConnection.updateDB("UserList", objectList);
	}
	
	//aggiorna singolo User
	public static void updateUser(User user) {
		List<User> userList = new ArrayList<User>();
		userList.add(user);
		SqliteConnection.updateUser(userList);
	}
	
	//prendi tutta la tabella User
	public static ResultSet getFieldUser() {
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
	
	//inserisci insieme di Libro
	public static void insertLibro(List<Libro> objectList) {
		SqliteConnection.insertIntoDB("BookList", objectList);
	}
	
	//inserisci singolo Libro
	public static void insertLibro(Libro book) {
		List<Libro> bookList = new ArrayList<Libro>();
		bookList.add(book);
		SqliteConnection.insertLibro(bookList);
	}

	//aggiorna insieme di Libro
	public static void updateLibro(List<Libro> objectList) {
		SqliteConnection.updateDB("BookList", objectList);
	}
	
	//aggiorna singolo Libro
	public static void updateLibro(Libro book) {
		List<Libro> bookList = new ArrayList<Libro>();
		bookList.add(book);
		SqliteConnection.updateLibro(bookList);
	}
	
	//prendi tutta la tabella Libro
	public static ResultSet getFieldLibro() {
		return SqliteConnection.getEverythingFromTableDB("BookList");
	}
	
	//prendi solo i campi richiesti da la tabella Libro
	public static ResultSet getFieldLibro(List<String> columnList) {
		return SqliteConnection.getFromTableDB("BookList", columnList);
	}
	
	//metodo per ritornare una lista di libri dato un ResultSet
	public static List<Libro> getBookList(ResultSet booksFromDB){
		List<Libro> bookList = new ArrayList<Libro>();
		
		try {
			while(booksFromDB.next()) {
				
				bookList.add(new Libro(booksFromDB.getString("titolo"), booksFromDB.getString("autore"), booksFromDB.getString("casaEditrice"), booksFromDB.getInt("annoPubblicazione"),
						booksFromDB.getString("isbn"), booksFromDB.getString("genere"), booksFromDB.getDouble("prezzo"), booksFromDB.getString("breveDescrizione"), booksFromDB.getInt("copieVenduteTotali")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return bookList;
	}
	
	
	
	//-------------------------//
	//------METODI ORDINE------//
	//-------------------------//
	
	//inserisci Ordine
	public static void insertOrder(List<Ordine> objectList) {
		SqliteConnection.insertIntoDB("OrderList", objectList);
	}
	
	//inserisci singolo Ordine
	public static void insertOrder(Ordine order) {
		List<Ordine> orderList = new ArrayList<Ordine>();
		orderList.add(order);
		SqliteConnection.insertOrder(orderList);
	}
	
	//aggiorna insieme di Ordine
	public static void updateOrdine(List<Ordine> objectList) {
		SqliteConnection.updateDB("OrderList", objectList);
	}
		
	//aggiorna singolo Ordine
	public static void updateOrdine(Ordine order) {
		List<Ordine> orderList = new ArrayList<Ordine>();
		orderList.add(order);
		SqliteConnection.updateOrdine(orderList);
	}
	
	//prendi tutta la tabella Ordine
	public static ResultSet getFieldOrdine() {
		return SqliteConnection.getEverythingFromTableDB("OrderList");
	}
	
	//prendi solo i campi richiesti da la tabella Ordine
	public static ResultSet getFieldOrdine(List<String> columnList) {
		return SqliteConnection.getFromTableDB("OrderList", columnList);
	}
	
	
	//FIXME DA TESTAREEEEEEEEEEEEEEEEEEEEEEEEEEEE
	
	//metodo per ritornare una lista di ordini dato un ResultSet. Se viene dato uno User ritorno la lista di ordini relativa allo User dato.
	public static List<Ordine> getOrderList(ResultSet ordersFromDB, User user){
		List<Ordine> orderList = new ArrayList<Ordine>();
		
		try {
			if(user != null) { //prendo gli ordini dello user definito
				while(ordersFromDB.next()) {
					if(ordersFromDB.getString("user").equals(user.getEmail()))
						orderList.add(new Ordine(ordersFromDB.getString("id"), ordersFromDB.getInt("giorno"), 
								ordersFromDB.getInt("mese"), ordersFromDB.getInt("anno"), ordersFromDB.getInt("ora"), 
								SqliteConnection.isbnStringToBookList(ordersFromDB.getString("libriOrdine")), 
								ordersFromDB.getDouble("totalCost"), ordersFromDB.getString("paymentType"), 
								ordersFromDB.getInt("saldoPuntiOrdine"), user.getEmail()));				
				}
			}
			else { //prendo tutti gli ordini
				List<User> userList = SqliteConnection.getUserList(SqliteConnection.getFieldUser());
				
				while(ordersFromDB.next()) {
					for(User singleUser : userList) {
						if(singleUser.getEmail().equals(ordersFromDB.getString("user"))) {
							orderList.add(new Ordine(ordersFromDB.getString("id"), ordersFromDB.getInt("giorno"), 
									ordersFromDB.getInt("mese"), ordersFromDB.getInt("anno"), ordersFromDB.getInt("ora"), 
									SqliteConnection.isbnStringToBookList(ordersFromDB.getString("libriOrdine")), 
									ordersFromDB.getDouble("totalCost"), ordersFromDB.getString("paymentType"), 
									ordersFromDB.getInt("saldoPuntiOrdine"), singleUser.getEmail()));
							break;
						}
						else if(ordersFromDB.getString("user").equals("")) { //utente non registrato
							orderList.add(new Ordine(ordersFromDB.getString("id"), ordersFromDB.getInt("giorno"), 
									ordersFromDB.getInt("mese"), ordersFromDB.getInt("anno"), ordersFromDB.getInt("ora"), 
									SqliteConnection.isbnStringToBookList(ordersFromDB.getString("libriOrdine")), 
									ordersFromDB.getDouble("totalCost"), ordersFromDB.getString("paymentType"), 
									ordersFromDB.getInt("saldoPuntiOrdine"), ""));
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return orderList;
	}
	
	//metodo per ritornare tutti gli ordini (uso in zona Responsabile)
	public static List<Ordine> getOrderList(ResultSet ordersFromDB){
		return SqliteConnection.getOrderList(ordersFromDB, null);
	}
	
	//metodo per ritornare un ordine dato il suo id (uso per utente non registrato)
	public static Ordine getOrderByID(String id) {
		ResultSet ordersFromDB = SqliteConnection.getFieldOrdine();
		try {
			while(ordersFromDB.next()) {
				if(ordersFromDB.getString("id").equals(id)) {
					return new Ordine(ordersFromDB.getString("id"), ordersFromDB.getInt("giorno"), 
							ordersFromDB.getInt("mese"), ordersFromDB.getInt("anno"), ordersFromDB.getInt("ora"), 
							SqliteConnection.isbnStringToBookList(ordersFromDB.getString("libriOrdine")), 
							ordersFromDB.getDouble("totalCost"), ordersFromDB.getString("paymentType"), 
							ordersFromDB.getInt("saldoPuntiOrdine"), ordersFromDB.getString("user"));	
				}
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}

