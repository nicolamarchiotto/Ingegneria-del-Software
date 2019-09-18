package application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

public class Classifica {
	
		//metodo per aggiornare TUTTE le classifiche
		//vero se update settimanale,falso se respondabile
		public static void updateClassifica(boolean updateSettimanale) {
			updateClassifica(null, updateSettimanale);
			updateClassifica("novità", updateSettimanale);
			updateClassifica("Narrativa", updateSettimanale);
			updateClassifica("Storia", updateSettimanale);
			updateClassifica("Romanzo", updateSettimanale);
			updateClassifica("Fantascienza", updateSettimanale);
			updateClassifica("Ragazzi", updateSettimanale);
			updateClassifica("Poliziesco", updateSettimanale);
			updateClassifica("Altro", updateSettimanale);
			
		}
		
		//metodo per aggiornare la classifica genere per genere
		public static void updateClassifica(String genere, boolean updateSettimanale){ //null per classifica globale, novità per la classifica dei libri inseriti in settimana, genere per le classifiche solo per genere
			List<Libro> bookList = null;
			
			if(genere == null) { //classifica globale
				ResultSet gettingPreviousGlobalPositions = Classifica.selectByGenre(null, "update");
				List<Integer> previousGlobalPosition = new ArrayList<Integer>(); 
				List<Integer> weeksInSamePositionGlobal = new ArrayList<Integer>();
				
				if(gettingPreviousGlobalPositions == null) return;
				
				try {
					while(gettingPreviousGlobalPositions.next()) {
						previousGlobalPosition.add(gettingPreviousGlobalPositions.getInt("precedentePosizioneClassificaGlobale"));
						weeksInSamePositionGlobal.add(gettingPreviousGlobalPositions.getInt("settimaneStessaPosizioneGlobale"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				

				bookList = SqliteConnection.getAvailableBooks(Classifica.selectByGenre(null, "update"));
				if(bookList == null) return;
				
				
				for(int i = 0; i < bookList.size(); i++) {
					if(previousGlobalPosition.get(i) == i + 1) {//posizione di questa settimana = posizione della settimana scorsa
						if(updateSettimanale)weeksInSamePositionGlobal.set(i, weeksInSamePositionGlobal.get(i) + 1); //settimana è passata
						else weeksInSamePositionGlobal.set(i, weeksInSamePositionGlobal.get(i));					//settimana non è passata
					}
					else weeksInSamePositionGlobal.set(i, 1); //posizione di questa settimana != posizione della settimana scorsa
				}
				
				Classifica.updatePosizioniClassifica(bookList, weeksInSamePositionGlobal, true, updateSettimanale);
			}
			else if(genere.equals("novità")) {//classifica delle novità
				bookList = SqliteConnection.getAvailableBooks(Classifica.selectByNovelty(1));
				List<Integer> weeksInSamePosition = new ArrayList<Integer>();
				if(bookList != null && !bookList.isEmpty()) {
					for(int i = 0; i < bookList.size(); i++) {
						weeksInSamePosition.add(1);
					}
					Classifica.removeFromNovelty(bookList, updateSettimanale);
				}
				
			}
			else {//classifica per genere
				ResultSet gettingPreviousPositions = Classifica.selectByGenre(genere, "update");
				List<Integer> previousPosition = new ArrayList<Integer>(); 
				List<Integer> weeksInSamePosition = new ArrayList<Integer>();
				
				if(gettingPreviousPositions == null) return;
				
				try {
					while(gettingPreviousPositions.next()) {
						previousPosition.add(gettingPreviousPositions.getInt("precedentePosizioneClassifica"));
						weeksInSamePosition.add(gettingPreviousPositions.getInt("settimaneStessaPosizione"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				bookList = SqliteConnection.getAvailableBooks(Classifica.selectByGenre(genere, "update"));
				
				
				if(bookList == null) return;
				
				
				for(int i = 0; i < bookList.size(); i++) {
					if(previousPosition.get(i) == (i + 1)) //posizione di questa settimana = posizione della settimana scorsa
						if(updateSettimanale) weeksInSamePosition.set(i, weeksInSamePosition.get(i) + 1); //settimana passata
						else weeksInSamePosition.set(i, weeksInSamePosition.get(i)); //settimana non passata
					else weeksInSamePosition.set(i, 1); //posizione di questa settimana != posizione della settimana scorsa
				}
				
				Classifica.updatePosizioniClassifica(bookList, weeksInSamePosition, false, updateSettimanale);
			}

			if(bookList != null) {
				System.out.println("Eh ");
				for(Libro book : bookList) {
					System.out.print(book.getTitolo() + " ");
				}
				System.out.println();
			}
		}

		//metodo per ricevere la classifica richiesta
		public static HashMap<List<Libro>, List<Integer>> getClassifica(String genere){
			if(genere == null) { //classifica globale
				ResultSet gettingPreviousGlobalPositions = Classifica.selectByGenre(null, "get");
				List<Integer> weeksInSamePositionGlobal = new ArrayList<Integer>();
				

				if(gettingPreviousGlobalPositions == null) return null;
				try {
					while(gettingPreviousGlobalPositions.next()) {
						weeksInSamePositionGlobal.add(gettingPreviousGlobalPositions.getInt("settimaneStessaPosizioneGlobale"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

				List<Libro> bookList = SqliteConnection.getAvailableBooks(Classifica.selectByGenre(null, "get"));
				
				
				if(bookList == null) return null;
				
				HashMap<List<Libro>, List<Integer>> classifica = new HashMap<List<Libro>, List<Integer>>();
				classifica.put(bookList, weeksInSamePositionGlobal);
				return classifica; 
			}
			else if(genere.equals("novità")) {//classifica delle novità
				List<Libro> bookList = SqliteConnection.getAvailableBooks(Classifica.selectByNovelty(0));
				if(bookList == null || bookList.isEmpty())
					return null;
				
				List<Integer> weeksInSamePosition = new ArrayList<Integer>();
				
				for(int i = 0; i < bookList.size(); i++){
					weeksInSamePosition.add(1);
				}
				
				HashMap<List<Libro>, List<Integer>> classifica = new HashMap<List<Libro>, List<Integer>>();
				classifica.put(bookList, weeksInSamePosition);
				return classifica;
			}
			else {//classifica per genere
				ResultSet gettingPreviousPositions = Classifica.selectByGenre(genere, "get");
				List<Integer> weeksInSamePosition = new ArrayList<Integer>();

				if(gettingPreviousPositions == null) return null;
				try {
					while(gettingPreviousPositions.next()) {
						weeksInSamePosition.add(gettingPreviousPositions.getInt("settimaneStessaPosizione"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				

				List<Libro> bookList = SqliteConnection.getAvailableBooks(Classifica.selectByGenre(genere, "get"));
				
				if(bookList == null) return null;
				
				HashMap<List<Libro>, List<Integer>> classifica = new HashMap<List<Libro>, List<Integer>>();
				classifica.put(bookList, weeksInSamePosition);
				return classifica;
			}
		}
		
		//metodo per ricevere i libri di un certo genere
		public static ResultSet selectByGenre(String genere, String getOrUpdate) {
			Connection connect = SqliteConnection.dbConnector();
			String ordering = getOrUpdate.equals("get") ? "copieVenduteSettimanaPrecedente" : "copieVenduteTotali";
			if(genere == null) { //selezione per classifica generale
				String sql = "SELECT * FROM BookList\nWHERE disponibilita = 1 AND copieVenduteSettimanaPrecedente != -1";
				sql += "\nORDER BY " + ordering + " DESC;";
				
				
				Statement stmt = null;
				
				try {
					
					System.out.println("*****CONNESSO PER RICEVERE TUTTI I GENERI*****");
						
					stmt = connect.createStatement();
					ResultSet rs = stmt.executeQuery(sql);
					return rs;
				}
				catch(SQLException e) {
					System.out.println(e.getMessage());
					return null;
				}
			}
			
			//selezione per singolo genere
			String sql = "SELECT * FROM BookList\nWHERE genere = '" + genere + "' AND disponibilita = 1 AND copieVenduteSettimanaPrecedente != -1";
			sql += "\nORDER BY " + ordering + " DESC;";
				
			Statement stmt = null;
				
			try {
					
				System.out.println("*****CONNESSO PER RICEVERE IL GENERE " + genere + "*****");
					
				stmt = connect.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				if(rs.isClosed()) return null;
				return rs;
			}
			catch(SQLException e) {
				System.out.println(e.getMessage());
				return null;
			}
		}
		
		//metodo per ricevere i libri novità (libri in 3 possibili stati: 1 -> novita totale, quando faccio update lo fisso a 0 -> novita settimanale, ovvero la novita che mi arriva facendo le get post update, nel successivo update gli 0 diventano -1 -> non più novità
		public static ResultSet selectByNovelty(int updatingOrViewing) {
			String sql = "SELECT * FROM BookList\nWHERE (novita = " + updatingOrViewing + " AND disponibilita = 1 OR novita = 2 AND disponibilita = 1) AND copieVenduteSettimanaPrecedente != -1";
			if(updatingOrViewing == 1) {
				sql += "\nORDER BY copieVenduteTotali DESC;";
				sql += "\nUPDATE BookList SET\n";
				sql += "novita = -1\n";
				sql += "WHERE novita = 0;";
			}
			else {
				sql += "\nORDER BY copieVenduteSettimanaPrecedente DESC;";
			}
			
			
				
			Statement stmt = null;
				
			try {
					
				System.out.println("*****CONNESSO PER RICEVERE LE NOVITA'*****");
					
				stmt = SqliteConnection.dbConnector().createStatement();
				stmt.executeUpdate(sql);
				ResultSet rs = stmt.executeQuery(sql);
				if(rs.isClosed()) return null;
				return rs;
			}
			catch(SQLException e) {
				System.out.println(e.getMessage());
				return null;
			}
		}
		
		
		//metodo per aggiornare i campi relativi alla classifica nella bookList
		public static void updatePosizioniClassifica(List<Libro> bookList, List<Integer> weeksInSamePosition, boolean global, boolean updateSettimanale) {
			int iterator = 0;
			String sql = "";
			
			Connection connect = SqliteConnection.dbConnector();
			
			for(Libro book : bookList) {
				if(global) {
					sql += "UPDATE BookList \nSET "; //non voglio permettere il variare i campi PRIMARY KEY O UNIQUE quindi non c'è update del campo isbn
					sql += "settimaneStessaPosizioneGlobale = " + weeksInSamePosition.get(iterator) + ",\n";
					sql += "precedentePosizioneClassificaGlobale = " + ++iterator + ",\n";
					sql += "copieVenduteSettimanaPrecedente = " + book.getCopieVendute() + ",\n"; //campo a cui accederà getClassifica(), campo dunque sul quale salvare il valore finale della settimana scorsa
					if(updateSettimanale) sql += "copieVenduteTotali = 0\n"; //updateSettimanale quindi resetto il conteggio per la settimana successiva
					sql += "WHERE isbn = '" + book.getIsbn() + "';";
				}
				else {
					sql += "UPDATE BookList \nSET "; //non voglio permettere il variare i campi PRIMARY KEY O UNIQUE quindi non c'è update del campo isbn
					sql += "settimaneStessaPosizione = " + weeksInSamePosition.get(iterator) + ",\n";
					sql += "precedentePosizioneClassifica = " + ++iterator + "\n";
					sql += "WHERE isbn = '" + book.getIsbn() + "';";
				}
				
				Statement stmt = null;
				
				try {
					stmt = connect.createStatement();
					stmt.executeUpdate(sql);
				}
				catch(SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		
		//metodo per togliere i libri dalla sezione novità (dal successivo aggiornamento della classifica)
		public static void removeFromNovelty(List<Libro> bookList, boolean updateSettimanale) {
			String sql = "";
			
			Connection connect = SqliteConnection.dbConnector();
			
			for(Libro book : bookList) {
				
				sql += "UPDATE BookList \nSET "; //non voglio permettere il variare i campi PRIMARY KEY O UNIQUE quindi non c'è update del campo isbn
				sql += "novita = " + (updateSettimanale ? 0 : 2); //0 se sono settimanale, così al prossimo update verrà tolto dalle novità. 2 se sono responsabile così al prossimo update settimanale verrà preso ancora come novità
				sql += "\nWHERE isbn = '" + book.getIsbn() + "';";
				Statement stmt = null;
				
				try {
					stmt = connect.createStatement();
					stmt.executeUpdate(sql);
				}
				catch(SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		
		//metodo per ricevere la lista di Libri interna alla mappa
		public static List<Libro> getBooksFromMap(Map<List<Libro>, List<Integer>> map) {
			if(map == null) return null;
			for(List<Libro> mapPiece : map.keySet())
				return mapPiece;
			return null;
		}
		

		//metodo per ricevere la lista di Interi interna alla mappa
		public static List<Integer> getWeeksFromMap(Map<List<Libro>, List<Integer>> map) {
			if(map == null) return null;
			for(List<Integer> mapPiece : map.values())
				 return mapPiece;
			return null;
		}
		
		
		//metodo per randomizzare le copie vendute, inizializzare le novità
		public static void randomize() {
			Connection connect = SqliteConnection.dbConnector();
			List<Libro> bookList = SqliteConnection.getAvailableBooks(SqliteConnection.getFieldLibro());
			
			Random r = new Random();
			
			for(Libro book : bookList) {
				String sql = "UPDATE BookList SET\ncopieVenduteTotali = " + r.nextInt(50) + ",\nnovita = " + r.nextInt(2) 
						+ ",\nprecedentePosizioneClassifica = -1,\n" 
						+ "settimaneStessaPosizione = -1,\n" 
						+ "precedentePosizioneClassificaGlobale = -1,\n" 
						+ "settimaneStessaPosizioneGlobale = -1,\n" 
						+ "copieVenduteSettimanaPrecedente = 0" 
						+ "\nWHERE isbn = '" + book.getIsbn() + "';";
				
				
				Statement stmt = null;
				try {
					stmt = connect.createStatement();
					stmt.executeUpdate(sql);
				}
				catch(SQLException e) {
					System.out.println(e.getMessage());
				}
			}
			
			
		}
}
