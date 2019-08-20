package application;

import java.sql.*;

public class SqliteConnection {
	Connection connect = null;
	
	public static Connection dbConnector() {
		try {
			Class.forName("org.sqlite.JDBC");
			Connection connect = DriverManager.getConnection("jdbc:sqlite:D:\\Uni\\Ingegneria_del_Software\\ProgettoCondor\\userDB.db");
			System.out.println("\nConnected to da DB!"); 
			return connect;
		}
		catch(Exception e) {
			System.out.println("\nHeya something's wrong!");
			return null;
		}
	}
}
