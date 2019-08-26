package application;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DBWindowController {
	@FXML private TextArea listaProva; 
	@FXML private TextArea selectedItems;
	
	@FXML private TextField email;
	@FXML private TextField password;
	
	@FXML private Button clickToCopyDB;
	@FXML private Button clickToInsertDB;
	@FXML private Button clickToSelect;
	
	@FXML private RadioButton userList;
	@FXML private RadioButton bookList;
	
	@FXML private CheckBox checkBoxEmail;
	@FXML private CheckBox checkBoxPassword;
	@FXML private CheckBox checkBoxTitolo;
	@FXML private CheckBox checkBoxIsbn;
	@FXML private CheckBox checkBoxAutore;
	@FXML private CheckBox checkBoxCasaEdit;
	@FXML private CheckBox checkBoxAnnoPubb;
	@FXML private CheckBox checkBoxGenere;
	@FXML private CheckBox checkBoxPrezzo;
	@FXML private CheckBox checkBoxBreveDesc;
	@FXML private CheckBox checkBoxPosizione;
	@FXML private CheckBox checkBoxPuntiCard;

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
	            
	        listaProva.setText(stringaTotale);
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
	
	
	//metodo per selezionare colonne dalla tabella selezionata
	public void selectDataButtonPushed(ActionEvent e) {
		if(!userList.isSelected() && !bookList.isSelected()) selectedItems.setText("Si ma decidi la tabella mona");
		else{ //tabella è stata decisa
			List<String> columnList = new LinkedList<String>();
			String stringaTotale = "";
			
			if(userList.isSelected()) {
				if(checkBoxEmail.isSelected()) columnList.add("email");
				if(checkBoxPassword.isSelected()) columnList.add("password");
				
				if(columnList.isEmpty()) {
					stringaTotale = "Ma selezionami qualche colonna";
				}
				else {
					ResultSet rs = SqliteConnection.getFromTableDB("userList", columnList);
					try {
						while (rs.next()) {
							for(String singleColumn : columnList) {
								stringaTotale += rs.getString(singleColumn) + " ";
							}
							stringaTotale += "\n";
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					} 
				}
			}
			else { //bookList.isSelected() è TRUE
				if(checkBoxIsbn.isSelected()) columnList.add("isbn");
				if(checkBoxTitolo.isSelected()) columnList.add("titolo");
				if(checkBoxAutore.isSelected()) columnList.add("autore");
				if(checkBoxAnnoPubb.isSelected()) columnList.add("annoPubblicazione");
				if(checkBoxCasaEdit.isSelected()) columnList.add("casaEditrice");
				if(checkBoxGenere.isSelected()) columnList.add("genere");
				if(checkBoxPrezzo.isSelected()) columnList.add("prezzo");
				if(checkBoxBreveDesc.isSelected()) columnList.add("breveDescrizione");
				if(checkBoxPosizione.isSelected()) columnList.add("posizioneClass");
				if(checkBoxPuntiCard.isSelected()) columnList.add("puntiCarta");
				
				if(columnList.isEmpty()) {
					stringaTotale = "Ma selezionami qualche colonna";
				}
				else {
					ResultSet rs = SqliteConnection.getFromTableDB("bookList", columnList);
					try {
						while (rs.next()) {
							for(String singleColumn : columnList) {
								stringaTotale += rs.getString(singleColumn) + " ";
							}
							stringaTotale += "\n";
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					} 
				}
			}
			
			selectedItems.setText(stringaTotale);
		}
	}
	
}	