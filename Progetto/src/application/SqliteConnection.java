package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



//classe per la gestione delle operazioni su DB
public class SqliteConnection {
	
	
	//metodo per instanziare una connessione
	public static Connection dbConnector() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connect = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\nicol\\git\\Progetto-Ingegneria-Software-2019\\Progetto\\userDB.db");
			System.out.println("\nConnected to da DB!"); 
			return connect;
		}
		catch(Exception e) {
			System.out.println("\nConnection Failed!");
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
				
				System.out.println("*****CONNESSO PER INSERIRE UN LIBRO*****");
				
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
				
				System.out.println("*****CONNESSO PER INSERIRE UN ORDINE*****");
				
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
				
				System.out.println("*****CONNESSO PER AGGIUNGERE UN LIBRO*****");
				
				System.out.println("ciao" + objectList.toString());
				for(Object libro : objectList) {
					Libro book = (Libro)libro;
					
					sql += "UPDATE BookList \nSET "; //non voglio permettere il variare i campi PRIMARY KEY O UNIQUE quindi non c'� update del campo isbn
					sql += "autore = '" + book.getAutore() + "',\n";
					sql += "annoPubblicazione = " + book.getAnnoPublicazione() + ",\n";
					sql += "casaEditrice = '" + book.getCasaEditrice() + "',\n";
					sql += "genere = '" + book.getGenere() + "',\n";
					sql += "prezzo = " + book.getPrezzo() + ",\n";
					sql += "breveDescrizione = '" + book.getBreveDescrizione() + "',\n";
					sql += "copieVenduteTotali = " + book.getCopieVendute() + ",\n";
					sql += "puntiCarta = " + book.getPunti() +"\n";
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
					sql += "cap = " + user.getCap() + ",\n";
					sql += "citta = '" + user.getCitta() + "',\n";
					sql += "telefono = " + user.getTelefono() + "\n";
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
					
					//idea di fondo: provo a fare un inserimento dove se vado in conflitto (ordine esiste gi�), non inserisco e faccio un update
					sql += "INSERT INTO DateList VALUES\n";
					sql += "('" + order.getId() + "',\n";
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
					sql += "WHERE id = '" + order.getId() + "';\n\n";
					
					sql += "INSERT INTO OrderList VALUES\n";
					sql += "('" + order.getId() + "',\n'";
					sql += order.getUserId()+ "',\n'";
					sql += SqliteConnection.bookListToISBNString(order.getLibri()) + "',\n";
					sql += order.getTotalCost() + ",\n'";
					sql += order.getPaymentType() + "',\n";
					sql += order.getSaldoPuntiOrdine() + ",\n'";
					sql += order.getStato() + "')\n";
					sql += "ON CONFLICT(id) DO "; //in caso di conflitto aggiorno l'entry nella tabella
					sql += "UPDATE SET ";
					sql += "libriOrdine = '" + SqliteConnection.bookListToISBNString(order.getLibri()) + "',\n";
					sql += "totalCost = " + order.getTotalCost() + ",\n";
					sql += "paymentType = '" + order.getPaymentType() + "',\n";
					sql += "saldoPuntiOrdine = " + order.getSaldoPuntiOrdine() + ",\n";
					sql += "stato = '" + order.getStato() + "'\n";
					sql += "WHERE id = '" + order.getId() + "' AND user = '" + order.getUserId() + "';";
					   
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
			
			//elimino infine la libroCard cos� da 
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
			sql += "DELETE FROM BookList\n";
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
			sql += "WHERE id = '" + order.getId() + "';\n\n";
			
			//elimino infine l'ordine
			sql += "DELETE FROM OrderList\n";
			sql += "WHERE id = '" + order.getId() + "';\n\n";
			
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
		if(tableName.equals("UserList")) { //LEFT OUTER JOIN pu� essere considerato una espansione di INNER JOIN
			sql += " LEFT OUTER JOIN BookCardList ON BookCardList.id = UserList.libroCard "
					+" LEFT OUTER JOIN DateList ON BookCardList.id = DateList.id;";
		}
		else if(tableName.equals("OrderList")) {
			sql += " INNER JOIN DateList ON OrderList.id = DateList.id;";
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
	
	
	//metodo per ricevere i campi delle colonne richieste di una SINGOLA TABELLA
	public static ResultSet getFromTableDB(String tableName, List<String> columnList) {
		String sql = "SELECT ";
		int iterator = 1;
		
		for(String singleColumn : columnList) {
			sql += singleColumn + (columnList.size() == iterator++ ? " " : ", "); 
		}
		
		sql += "FROM " + tableName + ";";
		
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
	public static List<Libro> isbnStringToBookList(String isbnString){
		List<Libro> bookList = new ArrayList<Libro>();
		String isbnArray[] = isbnString.split("#");
		
		if(isbnArray.length != 0) {
			String sql = "";
			for(String isbn : isbnArray) {
				sql = "SELECT * FROM BookList\n WHERE BookList.isbn = " + isbn;
				
				Statement stmt = null;
				try {
					
					System.out.println("*****CONNESSO PER TRASFORMARE L'ISBN IN BookList*****");
					
					stmt = SqliteConnection.dbConnector().createStatement();
					ResultSet booksFromDB = stmt.executeQuery(sql);
					bookList.add(new Libro(booksFromDB.getString("titolo"), booksFromDB.getString("autore"), booksFromDB.getString("casaEditrice"), booksFromDB.getInt("annoPubblicazione"), booksFromDB.getString("isbn"), booksFromDB.getString("genere"), booksFromDB.getDouble("prezzo"), booksFromDB.getString("breveDescrizione"), booksFromDB.getInt("puntiCarta")));
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
	
	//LOGICA PER LA CLASSIFICA
	/*Quando il programma parte siamo al giorno 0. Ogni 2 secondi (per esempio) viene conteggiato il passaggio di un giorno. 
	 * Un metodo in while(1) con sleep equivalente a 7 giorni chiama una funzione di updateClassifica, aggiornando quindi i valori
	 * della classifica una volta a settimana.
	 * Il responsabile pu� aggiornare la classifica quando vuole
	 */
	
	//metodo per aggiornare la classifica
	public static List<Libro> updateClassifica(){
		//TODO
		/*Classifiche saranno delle richieste al db ordinate per numero di vendita con ricerca basata su genere
		 * ogni libro ha un campo contenente la precedente posizione e quante settimane � rimasto in essa, cos� ad ogni aggiornamento � facile
		 * salvare questa informazione. 
		 * Fa eccezione la categoria  Novit�, la quale a mio parere non � un genere ma si basa sulla data di aggiunta e vale solo per la settimana
		 * in corso, tralasciando cos� problematiche di quante settimane � stato in una certa posizione.
		 * DOBBIAMO DUNQUE AGGIUNGERE UNA DATA AI LIBRI?? O BASTA UN BOOLEANO COME CAMPO, CHE VERRA' DISATTIVATO DOPO LA PRIMA SETTIMANA/PRIMO AGGIORNAMENTO CLASSIFICA DOPO L'INSERIMENTO DEL LIBRO??
		 */
		return null;
	}
	
	//metodo per fare update in fase di logOut
	public static void savingOnLogOut(User user) {
		System.out.println("\nSaving user's orders..  ");
		if(user.getOrdini() != null && user.getOrdini().size() != 0) SqliteConnection.updateOrdine(user.getOrdini());
		System.out.print("OK");

		System.out.println("\nSaving user..  ");
		if(user != null) SqliteConnection.updateUser(user);
		System.out.print("OK");
		
		System.out.println("\n------" + user.getEmail() + " SUCCESFULLY LOGGED OUT------\n");
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
	
	//cancella singolo User
	public static void deleteUser(User user) {
		SqliteConnection.deleteFromDB("UserList", user);
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
	
	//cancella singolo Libro
	public static void deleteLibro(Libro book) {
		SqliteConnection.deleteFromDB("BookList", book);
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
	
	//cancella singolo Ordine
	public static void deleteOrdine(Ordine order) {
		SqliteConnection.deleteFromDB("OrderList", order);
	}
	
	//prendi tutta la tabella Ordine
	public static ResultSet getFieldOrdine() {
		return SqliteConnection.getEverythingFromTableDB("OrderList");
	}
	
	//prendi solo i campi richiesti da la tabella Ordine
	public static ResultSet getFieldOrdine(List<String> columnList) {
		return SqliteConnection.getFromTableDB("OrderList", columnList);
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
								SqliteConnection.isbnStringToBookList(ordersFromDB.getString("libriOrdine")), 
								ordersFromDB.getDouble("totalCost"), ordersFromDB.getString("paymentType"), 
								ordersFromDB.getInt("saldoPuntiOrdine"), user.getEmail()));				
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

