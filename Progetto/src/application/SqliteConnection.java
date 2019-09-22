package application;

import java.sql.*;
import java.util.List;



//classe per la gestione delle operazioni su DB
public class SqliteConnection {
	
//*****************************************************************************************************************//
//**********************************************                         ******************************************//
//**********************************************  METHOD FOR CONNECTING  ******************************************//
//**********************************************                         ******************************************//
//*****************************************************************************************************************//
	
	//metodo per instanziare una connessione
	public static Connection dbConnector() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connect = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\utente\\git\\Progetto-Ingegneria-Software-2019\\Progetto\\userDB.db");
			return connect;
		}
		catch(Exception e) {
			System.out.println("\nConnection Failed!");
			return null;
		}
	}
	
	
	
//*****************************************************************************************************************//
//**********************************************                         ******************************************//
//**********************************************  METHOD FOR INSERTING   *****************************************//
//**********************************************                         ******************************************//
//*****************************************************************************************************************//
	
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
						sql += user.getLibroCard().getDataEmissione().getHour() + ",\n";
						sql += user.getLibroCard().getDataEmissione().getMinute() + ");\n\n";
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
					sql += order.getData().getHour() + ",\n";
					sql += order.getData().getMinute() + ");\n\n";
					
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
	
	
	
//*****************************************************************************************************************//
//**********************************************                         ******************************************//
//**********************************************   METHOD FOR UPDATING   ******************************************//
//**********************************************                         ******************************************//
//*****************************************************************************************************************//
	
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
					sql += "ora = " + user.getLibroCard().getDataEmissione().getHour() + ",\n";
					sql += "minuto = " + user.getLibroCard().getDataEmissione().getMinute() + "\n";
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
					sql += order.getData().getHour() + ",\n";
					sql += order.getData().getMinute() + ")\n";
					sql += "ON CONFLICT(id) DO "; //in caso di conflitto aggiorno l'entry nella tabella
					sql += "UPDATE SET ";
					sql += "giorno = " + order.getData().getDayOfMonth() + ",\n";
					sql += "mese = " + order.getData().getMonthValue() + ",\n";
					sql += "anno = " + order.getData().getYear() + ",\n";
					sql += "ora = " + order.getData().getHour() + ",\n";
					sql += "minuto = " + order.getData().getMinute() + "\n";
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
	
	
//*****************************************************************************************************************//
//**********************************************                         ******************************************//
//**********************************************   METHOD FOR DELETING   ******************************************//
//**********************************************                         ******************************************//
//*****************************************************************************************************************//
	
	
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
			List<Ordine> orderList = DBOrder.getOrderList(user);
			
			
			//rendo tutti gli ordini userLess
			for(Ordine singleOrder : orderList) {
				DBOrder.deleteOrdine(singleOrder); //cancello l'entry nel DB (inutile fare solo UPDATE dato che cambio l'id)
				singleOrder.becomeUserLess();
			}
			
			//inserisco l'ordine aggiornato
			DBOrder.insertOrder(orderList);
			
			
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
	
	
	
//*****************************************************************************************************************//
//**********************************************                           ****************************************//
//**********************************************  METHOD FOR GETTING DATA  ****************************************//
//**********************************************                           ****************************************//
//*****************************************************************************************************************//
	
	
	
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
			
			//System.out.println("*****CONNESSO PER RICEVERE DA " + tableName + "*****");
			
			stmt = SqliteConnection.dbConnector().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			return rs;
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	

	
	
//**********************************************  AUXILIARY METHODS  ******************************************//
	
	//metodo per trasformare la lista di libri interna all'ordine in una stringa di isbn divisi da #
	private static String bookListToISBNString(List<Libro> bookList) {
		String isbnString = "";
		for(Libro book : bookList) {
			isbnString += book.getIsbn();
			isbnString += "#";
		}
		return isbnString.substring(0, isbnString.length() - 1); //con il -1 tolgo l'ultimo #
	}
	

	
	//metodo per fare update in fase di logOut
	public static void savingOnLogOut(User user) {
		if(user != null && user.getLibroCard() != null && !user.getEmail().equals("#####")) { //not admin, not guest and logged in
			if(user.getOrdini() != null && user.getOrdini().size() != 0) { //at least one order to save
				DBOrder.updateOrdine(user.getOrdini());
				System.out.println("\nUser's orders SAVED");
			}
			DBUser.updateUser(user);
			System.out.println("\nUser SAVED");
			System.out.println("\n------" + user.getEmail() + " SUCCESFULLY LOGGED OUT------\n");	
		}
	}
	

	
}
	


