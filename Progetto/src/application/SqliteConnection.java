package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;



//classe per la gestione delle operazioni su DB
public class SqliteConnection {
	
	
	//metodo per instanziare una connessione
	public static Connection dbConnector() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connect = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\nicol\\git\\Progetto-Ingegneria-Software-2019\\Progetto\\userDB.db");
			return connect;
		}
		catch(Exception e) {
			System.out.println("\nConnection Failed!");
			return null;
		}
	}
	
	
	//metodo per l'inserimento di valori all'interno del DB inserendo solo certi campi, tableName contiene il nome della tabella, objectList gli oggetti veri e propri da inserire
	public static SQLException insertIntoDB(String tableName, List<?> objectList) {
		
		Connection connect = SqliteConnection.dbConnector();
		
		String sql = "";
		if(!objectList.isEmpty()){
			//**********inserire Libri***********//
			if(objectList.get(0) instanceof Libro) {
				
				System.out.println("*****CONNESSO PER INSERIRE UN LIBRO*****");
				
				for(Object libro : objectList) {
					sql = "INSERT INTO BookList VALUES";
					Libro book = (Libro)libro;
					sql += "(" + book.getIsbn() + ",\n\"";
					sql += book.getTitolo() + "\",\n'";
					sql += book.getAutore() + "',\n";
					sql += book.getAnnoPubblicazione() + ",\n'";
					sql += book.getCasaEditrice() + "',\n'";
					sql += book.getGenere() + "',\n";
					sql += book.getPrezzo() + ",\n\"";
					sql += book.getBreveDescrizione() + "\",\n";
					sql += book.getCopieVendute() + ",\n";
					sql += book.getPunti() + ",\n";
					sql += -1 + ",\n"; //precedentePosizioneClassifica
					sql += -1 + ",\n"; //settimaneStessaPosizione
					sql += -1 + ",\n"; //precedentePosizioneClassificaGlobale
					sql += -1 + ",\n"; //settimaneStessaPosizioneGlobale
					sql += -1 + ",\n"; //copieVenduteSettimanaPrecedente (se quando cerco qualcosa è a -1 vuol dire che non fa parte della classifica della settimana corrente)
					sql += 1 + ",\n"; //novita
					sql += book.getDisponibilita() + ");";
					
					
					Statement stmt = null;
					
					try {
						stmt = connect.createStatement();
						stmt.executeUpdate(sql);
					}
					catch(SQLException e) {
						System.out.println(e.getMessage());
						return e;
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
				
				System.out.println("*****CONNESSO PER INSERIRE UN UTENTE*****");
				
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
					sql += user.getIndirizzi() + "',\n'";
					sql += user.getCap() + "',\n'";
					sql += user.getCitta() + "',\n'";
					sql += user.getTelefono() + "',\n'";
					sql += libroCard + "')";
					sql += ";";
					
					Statement stmt = null;
					
					try {
						stmt = connect.createStatement();
						stmt.executeUpdate(sql);
					}
					catch(SQLException e) {
						System.out.println(e.getMessage());
						return e;
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
				
				System.out.println("*****CONNESSO PER INSERIRE UN ORDINE*****");
				
				for(Object ordine : objectList) {
					Ordine order = (Ordine) ordine;
					sql += "INSERT INTO DateList VALUES\n";
					sql += "('" + order.getIdOrdine() + "',\n";
					sql += order.getData().getDayOfMonth() + ",\n";
					sql += order.getData().getMonthValue() + ",\n";
					sql += order.getData().getYear() + ",\n";
					sql += order.getData().getHour() + ");\n\n";
					
					sql += "INSERT INTO OrderList VALUES\n";
					sql += "('" + order.getIdOrdine() + "',\n'";
					sql += order.getUserId()+ "',\n'";
					sql += SqliteConnection.bookListToISBNString(order.getLibri()) + "',\n'";
					sql += order.getStringaCopieLibri()+ "',\n";
					sql += order.getTotalCost() + ",\n'";
					sql += order.getPaymentType() + "',\n";
					sql += order.getSaldoPuntiOrdine() + ",\n'";
					sql += order.getStato() + "',\n'";
					sql += order.getIndirizzoSpedizione() + "');";
					
					Statement stmt = null;
					
					try {
						stmt = connect.createStatement();
						stmt.executeUpdate(sql);
					}
					catch(SQLException e) {
						System.out.println(e.getMessage());
						return e;
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
		return null;
	}
	
	
	//metodo per l'aggiornamento di valori all'interno del DB 
	public static void updateDB(String tableName, List<?> objectList) {
		
		Connection connect = SqliteConnection.dbConnector();
		
		String sql = "";
		if(!objectList.isEmpty()){
			//**********aggiornare Libri************//
			if(objectList.get(0) instanceof Libro) {
				
				System.out.println("*****CONNESSO PER AGGIORNARE UN LIBRO*****");
				
				for(Object libro : objectList) {
					Libro book = (Libro)libro;
					
					sql += "UPDATE BookList \nSET "; //non voglio permettere il variare i campi PRIMARY KEY O UNIQUE quindi non c'è update del campo isbn
					sql += "autore = '" + book.getAutore() + "',\n";
					sql += "annoPubblicazione = " + book.getAnnoPubblicazione() + ",\n";
					sql += "casaEditrice = '" + book.getCasaEditrice() + "',\n";
					sql += "genere = '" + book.getGenere() + "',\n";
					sql += "prezzo = " + book.getPrezzo() + ",\n";
					sql += "breveDescrizione = '" + book.getBreveDescrizione() + "',\n";
					sql += "copieVenduteTotali = " + book.getCopieVendute() + ",\n";
					sql += "puntiCarta = " + book.getPunti() +",\n";
					sql += "disponibilita = " + book.getDisponibilita() + "\n";
					sql += "WHERE isbn = '" + book.getIsbn() + "';";
					
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
				
				System.out.println("*****CONNESSO PER AGGIORNARE UN UTENTE*****");
				
				for(Object utente : objectList) {
					User user = (User)utente; //non voglio permettere il variare i campi PRIMARY KEY o UNIQUE (vedi stessa situazione in UPDATE Libri)
					//aggiorno la BookCard
					sql += "UPDATE BookCardList \nSET ";
					sql += "punti = " + user.getLibroCard().getPunti() + "\n";
					sql += "WHERE id = '" + user.getLibroCard().getId() + "';\n\n";
							
					//aggiorno la EmissionDate della BookCard
					sql += "UPDATE DateList \nSET ";
					sql += "giorno = " + user.getLibroCard().getDataEmissione().getDayOfMonth() + ",\n";
					sql += "mese = " + user.getLibroCard().getDataEmissione().getMonthValue() + ",\n";
					sql += "anno = " + user.getLibroCard().getDataEmissione().getYear() + ",\n";
					sql += "ora = " + user.getLibroCard().getDataEmissione().getHour() + "\n";
					sql += "WHERE id = '" + user.getLibroCard().getId() + "';\n\n";
					
					//aggiorno infine lo User
					sql += "UPDATE UserList \nSET ";
					sql += "password = '" + user.getPw() + "',\n";
					sql += "nome = '" + user.getNome() + "',\n";
					sql += "cognome = '" + user.getCognome() + "',\n";
					sql += "indirizzo = '" + user.getIndirizzi() + "',\n";
					sql += "cap = '" + user.getCap() + "',\n";
					sql += "citta = '" + user.getCitta() + "',\n";
					sql += "telefono = '" + user.getTelefono() + "'\n";
					sql += "WHERE email = '" + user.getEmail() + "' AND libroCard = '" + user.getLibroCard().getId() + "';";
					
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
				
				System.out.println("*****CONNESSO PER AGGIORNARE UN ORDINE*****");
				
				for(Object ordine : objectList) {
					Ordine order = (Ordine)ordine;//non voglio permettere il variare i campi PRIMARY KEY o UNIQUE (vedi stessa situazione in UPDATE Libri)
					
					//idea di fondo: provo a fare un inserimento dove se vado in conflitto (ordine esiste già), non inserisco e faccio un update
					sql += "INSERT INTO DateList VALUES\n";
					sql += "('" + order.getIdOrdine() + "',\n";
					sql += order.getData().getDayOfMonth() + ",\n";
					sql += order.getData().getMonthValue() + ",\n";
					sql += order.getData().getYear() + ",\n";
					sql += order.getData().getHour() + ")\n";
					sql += "ON CONFLICT(id) DO "; //in caso di conflitto aggiorno l'entry nella tabella
					sql += "UPDATE SET ";
					sql += "giorno = " + order.getData().getDayOfMonth() + ",\n";
					sql += "mese = " + order.getData().getMonthValue() + ",\n";
					sql += "anno = " + order.getData().getYear() + ",\n";
					sql += "ora = " + order.getData().getHour() + "\n";
					sql += "WHERE id = '" + order.getIdOrdine() + "';\n\n";
					
					sql += "INSERT INTO OrderList VALUES\n";
					sql += "('" + order.getIdOrdine() + "',\n'";
					sql += order.getUserId()+ "',\n'";
					sql += SqliteConnection.bookListToISBNString(order.getLibri()) + "',\n'";
					sql += order.getStringaCopieLibri()+ "',\n";
					sql += order.getTotalCost() + ",\n'";
					sql += order.getPaymentType() + "',\n";
					sql += order.getSaldoPuntiOrdine() + ",\n'";
					sql += order.getStato() + "',\n'";
					sql += order.getIndirizzoSpedizione() + "')\n";
					sql += "ON CONFLICT(id) DO "; //in caso di conflitto aggiorno l'entry nella tabella
					sql += "UPDATE SET ";
					sql += "libriOrdine = '" + SqliteConnection.bookListToISBNString(order.getLibri()) + "',\n";
					sql += "stringaCopieLibri = '" + order.getStringaCopieLibri() + "',\n";
					sql += "totalCost = " + order.getTotalCost() + ",\n";
					sql += "paymentType = '" + order.getPaymentType() + "',\n";
					sql += "saldoPuntiOrdine = " + order.getSaldoPuntiOrdine() + ",\n";
					sql += "stato = '" + order.getStato() + "',\n";
					sql += "indirizzoSpedizione = '" + order.getIndirizzoSpedizione() + "'\n";
					sql += "WHERE id = '" + order.getIdOrdine() + "' AND user = '" + order.getUserId() + "';";
					   
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
	
	
	//metodo per rimuovere una riga da una tabella del DB
	public static <T> void deleteFromDB(String tableName, T object) {
		if(object == null) //nothing to delete
			return;
		
		Connection connect = SqliteConnection.dbConnector();	
		String sql = "";
					
		if(object instanceof User) {
			User user = (User)object;
			
			//elimino la data relativa all'emissione della LibroCard dello User
			sql += "DELETE FROM DateList\n";
			sql += "WHERE id = '" + user.getIdentificativoCarta() + "';\n\n";
			
			//elimino lo user emissione
			sql += "DELETE FROM UserList\n";
			sql += "WHERE email = '" + user.getEmail() + "';\n\n";
			
			//elimino infine la libroCard così da 
			//evitare errori date le foreign card precedentemente cancellate
			sql += "DELETE FROM BookCardList\n";
			sql += "WHERE id = '" + user.getIdentificativoCarta() + "';\n\n";
			
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
			
			//NB dato che cancello l'utente dal DB, tutti i suoi ordini diventano senza padrone ("UserLess"),
			//dunque gli ordini all'interno del DB vanno aggiornati
			List<Ordine> orderList = SqliteConnection.getOrderList(user);
			
			
			//rendo tutti gli ordini userLess
			for(Ordine singleOrder : orderList) {
				SqliteConnection.deleteOrdine(singleOrder); //cancello l'entry nel DB (inutile fare solo UPDATE dato che cambio l'id)
				singleOrder.becomeUserLess();
			}
			
			//inserisco l'ordine aggiornato
			SqliteConnection.insertOrder(orderList);
			
			
		}
		else if(object instanceof Libro){
			Libro book = (Libro)object;
			
			//elimino il libro richiesto
			sql += "UPDATE BookList SET\ndisponibilita = 0\n";
			sql += "WHERE isbn = '" + book.getIsbn() + "';\n\n";
			
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
		else if(object instanceof Ordine) {
			Ordine order = (Ordine)object;
			
			//elimino prima la data relativa all'ordine
			sql += "DELETE FROM DateList\n";
			sql += "WHERE id = '" + order.getIdOrdine() + "';\n\n";
			
			//elimino infine l'ordine
			sql += "DELETE FROM OrderList\n";
			sql += "WHERE id = '" + order.getIdOrdine() + "';\n\n";
			
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
	
	
	
	
	//metodo per ricevere tutti i campi di tutte le colonne di una SINGOLA TABELLA
	public static ResultSet getEverythingFromTableDB(String tableName) {
		String sql = "SELECT * FROM " + tableName;
		if(tableName.equals("UserList")) { //LEFT OUTER JOIN può essere considerato una espansione di INNER JOIN
			sql += " LEFT OUTER JOIN BookCardList ON BookCardList.id = UserList.libroCard "
					+" LEFT OUTER JOIN DateList ON BookCardList.id = DateList.id;";
		}
		else if(tableName.equals("OrderList")) {
			sql += " INNER JOIN DateList ON OrderList.id = DateList.id;";
		}
		else if(tableName.equals("BookList")) {
			sql += "\nORDER BY titolo;";
		}
		
		Statement stmt = null;
			
		try {
			
			System.out.println("*****CONNESSO PER RICEVERE DA " + tableName + "*****");
			
			stmt = SqliteConnection.dbConnector().createStatement();
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
	public static List<Libro> isbnStringToBookList(String isbnString, String bookCopiesString){
		List<Libro> bookList = new ArrayList<Libro>();
		String isbnArray[] = isbnString.split("#");
		
		String bookCopiesArray[] = bookCopiesString.split("#");
		
		if(isbnArray.length != bookCopiesArray.length) {
			AlertBox.display("ERROR", "Oops that's embarassing, this shouldn't pop up..");
			return null;
		}
		
		if(isbnArray.length != 0) {
			String sql = "";
			int i = 0; //iteratore su bookCopiesArray
			for(String isbn : isbnArray) {
				sql = "SELECT * FROM BookList\n WHERE BookList.isbn = " + isbn;
				
				Statement stmt = null;
				try {
					
					//System.out.println("*****CONNESSO PER TRASFORMARE L'ISBN IN BookList*****");
					
					stmt = SqliteConnection.dbConnector().createStatement();
					ResultSet booksFromDB = stmt.executeQuery(sql);
					bookList.add(new Libro(booksFromDB.getString("titolo"), booksFromDB.getString("autore"), 
							booksFromDB.getString("casaEditrice"), booksFromDB.getInt("annoPubblicazione"), 
							booksFromDB.getString("isbn"), booksFromDB.getString("genere"), 
							booksFromDB.getDouble("prezzo"), booksFromDB.getString("breveDescrizione"), 
							Integer.parseInt(bookCopiesArray[i++]), booksFromDB.getInt("copieVenduteTotali"), 
							booksFromDB.getInt("puntiCarta"), booksFromDB.getInt("disponibilita")));
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
			return bookList;
		}
		return null; //caso in cui non ci sia nessun isbn
	}
	
	
	//metodo per fare update in fase di logOut
	public static void savingOnLogOut(User user) {
		if(user != null && user.getLibroCard() != null && !user.getEmail().equals("#####")) { //not admin, not guest and logged in
			if(user.getOrdini() != null && user.getOrdini().size() != 0) { //at least one order to save
				SqliteConnection.updateOrdine(user.getOrdini());
				System.out.println("\nUser's orders SAVED");
			}
			SqliteConnection.updateUser(user);
			System.out.println("\nUser SAVED");
			System.out.println("\n------" + user.getEmail() + " SUCCESFULLY LOGGED OUT------\n");	
		}
	}
	
	
	//-----------------------//	
	//------METODI USER------//
	//-----------------------//

	//inserisci User
	public static SQLException insertUser(List<User> objectList) {
		return SqliteConnection.insertIntoDB("UserList", objectList);
	}

	//inserisci singolo User
	public static SQLException insertUser(User user) {
		List<User> userList = new ArrayList<User>();
		userList.add(user);
		return SqliteConnection.insertUser(userList);
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
	
	//cancella singolo User
	public static void deleteUser(User user) {
		SqliteConnection.deleteFromDB("UserList", user);
	}
	
	//prendi tutta la tabella User
	public static ResultSet getFieldUser() {
		return SqliteConnection.getEverythingFromTableDB("UserList");
	}
	
	//metodo per ritornare una lista di User data una ResultSet della tabella User
	public static List<User> getUserList(ResultSet usersFromDB){
		List<User> userList = new ArrayList<User>();
		try {
			while(usersFromDB.next()) {
				userList.add(new User(usersFromDB.getString("nome"), usersFromDB.getString("cognome"), 
						usersFromDB.getString("indirizzo"), usersFromDB.getString("cap"), 
						usersFromDB.getString("citta"), usersFromDB.getString("telefono"), 
						usersFromDB.getString("email"), usersFromDB.getString("password"),
						usersFromDB.getString("libroCard"), usersFromDB.getInt("punti"), 
						usersFromDB.getInt("giorno"), usersFromDB.getInt("mese"), 
						usersFromDB.getInt("anno"), usersFromDB.getInt("ora")));
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
	public static SQLException insertLibro(List<Libro> objectList) {
		return SqliteConnection.insertIntoDB("BookList", objectList);
	}
	
	//inserisci singolo Libro
	public static SQLException insertLibro(Libro book) {
		List<Libro> bookList = new ArrayList<Libro>();
		bookList.add(book);
		return SqliteConnection.insertLibro(bookList);
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
	
	//cancella singolo Libro
	public static void deleteLibro(Libro book) {
		SqliteConnection.deleteFromDB("BookList", book);
	}
	
	//prendi tutta la tabella Libro
	public static ResultSet getFieldLibro() {
		return SqliteConnection.getEverythingFromTableDB("BookList");
	}
	
	//metodo per ritornare una lista di libri dato un ResultSet
	public static List<Libro> getBookList(ResultSet booksFromDB){
		List<Libro> bookList = new ArrayList<Libro>();
		if(booksFromDB == null) return null;
		try {
			while(booksFromDB.next()) {
				System.out.println(booksFromDB.getString("titolo"));
				bookList.add(new Libro(booksFromDB.getString("titolo"), booksFromDB.getString("autore"), 
						booksFromDB.getString("casaEditrice"), booksFromDB.getInt("annoPubblicazione"),
						booksFromDB.getString("isbn"), booksFromDB.getString("genere"), 
						booksFromDB.getDouble("prezzo"), booksFromDB.getString("breveDescrizione"), 0,
						booksFromDB.getInt("copieVenduteTotali"), booksFromDB.getInt("puntiCarta"), booksFromDB.getInt("disponibilita")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(bookList.isEmpty() + "   " + bookList);
		if(bookList.isEmpty()) return null;
		else return bookList;
	}
	
	//metodo per ritornare una lista di libri DISPONIBILI
	public static List<Libro> getAvailableBooks(ResultSet booksFromDB){
		List<Libro> bookList = SqliteConnection.getBookList(booksFromDB);
		
		if(bookList == null || bookList.isEmpty()) return null;
		
		for(Libro singleBook : bookList) {
			if(singleBook.getDisponibilita() == 0) {
				bookList.remove(singleBook);
			}
		}
		
		return bookList;
	}
	
	
	
	
	//-------------------------//
	//------METODI ORDINE------//
	//-------------------------//
	
	//inserisci Ordine
	public static SQLException insertOrder(List<Ordine> objectList) {
		return SqliteConnection.insertIntoDB("OrderList", objectList);
	}
	
	//inserisci singolo Ordine
	public static SQLException insertOrder(Ordine order) {
		List<Ordine> orderList = new ArrayList<Ordine>();
		orderList.add(order);
		return SqliteConnection.insertOrder(orderList);
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
	
	//cancella singolo Ordine
	public static void deleteOrdine(Ordine order) {
		SqliteConnection.deleteFromDB("OrderList", order);
	}
	
	//prendi tutta la tabella Ordine
	public static ResultSet getFieldOrdine() {
		return SqliteConnection.getEverythingFromTableDB("OrderList");
	}
	
	
	//metodo per ritornare una lista di ordini dato un ResultSet. Se viene dato uno User ritorno la lista di ordini relativa allo User dato.
	public static List<Ordine> getOrderList(User user){
		List<Ordine> orderList = new ArrayList<Ordine>();
		
		try {
			if(user != null) { //prendo gli ordini dello user definito
				
				ResultSet ordersFromDB = SqliteConnection.getFieldOrdine();
				while(ordersFromDB.next()) {
					if(ordersFromDB.getString("user").equals(user.getEmail()))
						orderList.add(new Ordine(ordersFromDB.getString("id"), ordersFromDB.getInt("giorno"), 
								ordersFromDB.getInt("mese"), ordersFromDB.getInt("anno"), ordersFromDB.getInt("ora"), 
								SqliteConnection.isbnStringToBookList(ordersFromDB.getString("libriOrdine"), ordersFromDB.getString("stringaCopieLibri")), 
								ordersFromDB.getDouble("totalCost"), ordersFromDB.getString("paymentType"), 
								ordersFromDB.getInt("saldoPuntiOrdine"), user.getEmail(), ordersFromDB.getString("indirizzoSpedizione"), ordersFromDB.getString("stringaCopieLibri")));				
				}
			}
			else { //prendo tutti gli ordini
				List<User> userList = SqliteConnection.getUserList(SqliteConnection.getFieldUser());
				System.out.println("Prendo tutti gli ordini del DB");
				ResultSet ordersFromDB = SqliteConnection.getFieldOrdine();
				
				while(ordersFromDB.next()) {
					for(User singleUser : userList) {
						if(singleUser.getEmail().equals(ordersFromDB.getString("user"))) {
							orderList.add(new Ordine(ordersFromDB.getString("id"), ordersFromDB.getInt("giorno"), 
									ordersFromDB.getInt("mese"), ordersFromDB.getInt("anno"), ordersFromDB.getInt("ora"), 
									SqliteConnection.isbnStringToBookList(ordersFromDB.getString("libriOrdine"), ordersFromDB.getString("stringaCopieLibri")), 
									ordersFromDB.getDouble("totalCost"), ordersFromDB.getString("paymentType"), 
									ordersFromDB.getInt("saldoPuntiOrdine"), singleUser.getEmail(), ordersFromDB.getString("indirizzoSpedizione"), ordersFromDB.getString("stringaCopieLibri")));
							break;
						}
					}
					if(ordersFromDB.getString("user").equals("#####")) { //utente non registrato
						orderList.add(new Ordine(ordersFromDB.getString("id"), ordersFromDB.getInt("giorno"), 
								ordersFromDB.getInt("mese"), ordersFromDB.getInt("anno"), ordersFromDB.getInt("ora"), 
								SqliteConnection.isbnStringToBookList(ordersFromDB.getString("libriOrdine"), ordersFromDB.getString("stringaCopieLibri")), 
								ordersFromDB.getDouble("totalCost"), ordersFromDB.getString("paymentType"), 
								ordersFromDB.getInt("saldoPuntiOrdine"), "#####", ordersFromDB.getString("indirizzoSpedizione"), ordersFromDB.getString("stringaCopieLibri")));
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
	public static List<Ordine> getOrderList(){
		return SqliteConnection.getOrderList(null);
	}
	
	//metodo per ritornare un ordine dato il suo id (uso per utente non registrato)
	public static Ordine getOrderByID(String id) {
		ResultSet ordersFromDB = SqliteConnection.getFieldOrdine();
		try {
			while(ordersFromDB.next()) {
				if(ordersFromDB.getString("id").equals(id)) {
					return new Ordine(ordersFromDB.getString("id"), ordersFromDB.getInt("giorno"), 
							ordersFromDB.getInt("mese"), ordersFromDB.getInt("anno"), ordersFromDB.getInt("ora"), 
							SqliteConnection.isbnStringToBookList(ordersFromDB.getString("libriOrdine"), ordersFromDB.getString("stringaCopieLibri")), 
							ordersFromDB.getDouble("totalCost"), ordersFromDB.getString("paymentType"), 
							ordersFromDB.getInt("saldoPuntiOrdine"), ordersFromDB.getString("user"), ordersFromDB.getString("indirizzoSpedizione"), ordersFromDB.getString("stringaCopieLibri"));	
				}
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//metodo per ritornare lo user di un certo ordine dato l'id dello user contenuto nell'ordine
	public static User getUserForOrdine(String userId) {
		LoginController controller=new LoginController();
		List<User> userList = controller.getUserList();
		for(User singleUser : userList)
			if(userId.equals(singleUser.getEmail()))
				return singleUser;
		return null;
	}
}

