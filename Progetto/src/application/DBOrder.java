package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBOrder {
	
	
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
		return DBOrder.insertOrder(orderList);
	}
	
	//aggiorna insieme di Ordine
	public static void updateOrdine(List<Ordine> objectList) {
		SqliteConnection.updateDB("OrderList", objectList);
	}
		
	//aggiorna singolo Ordine
	public static void updateOrdine(Ordine order) {
		List<Ordine> orderList = new ArrayList<Ordine>();
		orderList.add(order);
		DBOrder.updateOrdine(orderList);
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
				
				ResultSet ordersFromDB = DBOrder.getFieldOrdine();
				while(ordersFromDB.next()) {
					if(ordersFromDB.getString("user").equals(user.getEmail()))
						orderList.add(new Ordine(ordersFromDB.getString("id"), ordersFromDB.getInt("giorno"), 
								ordersFromDB.getInt("mese"), ordersFromDB.getInt("anno"), ordersFromDB.getInt("ora"), 
								DBOrder.isbnStringToBookList(ordersFromDB.getString("libriOrdine"), ordersFromDB.getString("stringaCopieLibri")), 
								ordersFromDB.getDouble("totalCost"), ordersFromDB.getString("paymentType"), 
								ordersFromDB.getInt("saldoPuntiOrdine"), user.getEmail(), ordersFromDB.getString("indirizzoSpedizione"), ordersFromDB.getString("stringaCopieLibri")));				
				}
			}
			else { //prendo tutti gli ordini
				List<User> userList = DBUser.getUserList(DBUser.getFieldUser());
				ResultSet ordersFromDB = DBOrder.getFieldOrdine();
				
				while(ordersFromDB.next()) {
					for(User singleUser : userList) {
						if(singleUser.getEmail().equals(ordersFromDB.getString("user"))) {
							orderList.add(new Ordine(ordersFromDB.getString("id"), ordersFromDB.getInt("giorno"), 
									ordersFromDB.getInt("mese"), ordersFromDB.getInt("anno"), ordersFromDB.getInt("ora"), 
									DBOrder.isbnStringToBookList(ordersFromDB.getString("libriOrdine"), ordersFromDB.getString("stringaCopieLibri")), 
									ordersFromDB.getDouble("totalCost"), ordersFromDB.getString("paymentType"), 
									ordersFromDB.getInt("saldoPuntiOrdine"), singleUser.getEmail(), ordersFromDB.getString("indirizzoSpedizione"), ordersFromDB.getString("stringaCopieLibri")));
							break;
						}
					}
					if(ordersFromDB.getString("user").equals("#####")) { //utente non registrato
						orderList.add(new Ordine(ordersFromDB.getString("id"), ordersFromDB.getInt("giorno"), 
								ordersFromDB.getInt("mese"), ordersFromDB.getInt("anno"), ordersFromDB.getInt("ora"), 
								DBOrder.isbnStringToBookList(ordersFromDB.getString("libriOrdine"), ordersFromDB.getString("stringaCopieLibri")), 
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
		return DBOrder.getOrderList(null);
	}
	
	//metodo per ritornare un ordine dato il suo id (uso per utente non registrato)
	public static Ordine getOrderByID(String id) {
		ResultSet ordersFromDB = DBOrder.getFieldOrdine();
		try {
			while(ordersFromDB.next()) {
				if(ordersFromDB.getString("id").equals(id)) {
					return new Ordine(ordersFromDB.getString("id"), ordersFromDB.getInt("giorno"), 
							ordersFromDB.getInt("mese"), ordersFromDB.getInt("anno"), ordersFromDB.getInt("ora"), 
							DBOrder.isbnStringToBookList(ordersFromDB.getString("libriOrdine"), ordersFromDB.getString("stringaCopieLibri")), 
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
	
}

