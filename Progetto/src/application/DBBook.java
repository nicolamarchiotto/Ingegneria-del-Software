package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBBook {
	
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
		return DBBook.insertLibro(bookList);
	}

	//aggiorna insieme di Libro
	public static void updateLibro(List<Libro> objectList) {
		SqliteConnection.updateDB("BookList", objectList);
	}
	
	//aggiorna singolo Libro
	public static void updateLibro(Libro book) {
		List<Libro> bookList = new ArrayList<Libro>();
		bookList.add(book);
		DBBook.updateLibro(bookList);
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
				bookList.add(new Libro(booksFromDB.getString("titolo"), booksFromDB.getString("autore"), 
						booksFromDB.getString("casaEditrice"), booksFromDB.getInt("annoPubblicazione"),
						booksFromDB.getString("isbn"), booksFromDB.getString("genere"), 
						booksFromDB.getDouble("prezzo"), booksFromDB.getString("breveDescrizione"), 0,
						booksFromDB.getInt("copieVenduteTotali"), booksFromDB.getInt("puntiCarta"), booksFromDB.getInt("disponibilita")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(bookList.isEmpty()) return null;
		else return bookList;
	}
	
	//metodo per ritornare una lista di libri DISPONIBILI
	public static List<Libro> getAvailableBooks(ResultSet booksFromDB){
		List<Libro> bookList = DBBook.getBookList(booksFromDB);
		
		if(bookList == null || bookList.isEmpty()) return null;
		
		List<Libro> booksToRemove = null;
		
		for(Libro singleBook : bookList) {
			if(singleBook.getDisponibilita() == 0) {
				if(booksToRemove == null) {
					booksToRemove = new ArrayList<Libro>();
				}
				booksToRemove.add(singleBook);
			}
		}
		
		if(booksToRemove == null) return bookList;
		
		for(Libro singleBook : booksToRemove) {
			bookList.remove(singleBook);
		}
		return bookList;
	}
	
	
}
