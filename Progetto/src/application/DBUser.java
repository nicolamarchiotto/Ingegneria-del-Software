package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUser {
	
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
		return DBUser.insertUser(userList);
	}
	

	//aggiorna insieme di User
	public static void updateUser(List<User> objectList) {
		SqliteConnection.updateDB("UserList", objectList);
	}
	
	//aggiorna singolo User
	public static void updateUser(User user) {
		List<User> userList = new ArrayList<User>();
		userList.add(user);
		DBUser.updateUser(userList);
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
	
}
