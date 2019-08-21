package application;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DBWindowController {
	@FXML private TextArea listaDBcazzDB; 
	@FXML private Button clickToCopyDB;
	@FXML private TextField email;
	@FXML private TextField password;
	@FXML private Button clickToInsertDB;

	//metodo per visualizzare tutto il DB (SELECT * FROM UserList)
	public void copyButtonPushed(ActionEvent e) {
		
	        ResultSet rs = SqliteConnection.getEverythingFromTableDB("UserList");
	        
	        String stringaTotale = "";
	              
	        // loop through the result set  
	        try {
				while (rs.next()) stringaTotale += rs.getString("email") + " - " + rs.getString("password") + "\n";
			} catch (SQLException e1) {
				e1.printStackTrace();
			}    
	            
	        listaDBcazzDB.setText(stringaTotale);
	}
	       
	//metodo per inserire una nuova riga nella tabella UserList
	public void insertNewRowButtonPushed(ActionEvent e) {
		List<User> provaInserimento = new LinkedList<User>();
		provaInserimento.add(new User(email.getText(), password.getText()));
		SqliteConnection.insertIntoDB("UserList", provaInserimento);
		
		
		
		/*ObservableList<Libro> libri = FXCollections.observableArrayList();
		
		
		
		
		Libro p1= new Libro("1984", "George Orwell", "Longman", 1949, "", "Romanzo distopico", 9.99, "", 1, 10);
		Libro p2= new Libro("Guerra e pace", "Lev Tolstoj", "Longman", 1865, "", "Romanzo storico", 19.99, "", 2, 20);
		Libro p3= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		Libro p4= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		Libro p5= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		Libro p6= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		Libro p7= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		Libro p8= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		Libro p9= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		
		libri.addAll(p1,p2,p3,p4,p5,p6,p7,p8,p9);
		
		
		//FIXME ho provato qui perché HomeController è non funzionante e non voglio mettere mano su qualcosa che stai facendo
		SqliteConnection.insertIntoDB("BookList", libri);*/
	}
	
	
}	