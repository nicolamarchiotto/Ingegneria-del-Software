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
		
	        ResultSet resultSet = SqliteConnection.getEverythingFromTableDB("UserList");
	        
	        String stringaTotale = "";
	              
	        // loop through the result set  
	        try {
	        	ResultSetMetaData rsmd = resultSet.getMetaData(); //oggetto che contiene informazioni su nomi e numeri di colonne, tabelle, etc.
	        	int columnsNumber = rsmd.getColumnCount();
	        	while (resultSet.next()) {
	        	    for (int i = 1; i <= columnsNumber; i++) {
	        	        if (i > 1) stringaTotale += ",  ";
	        	        String columnValue = resultSet.getString(i);
	        	        stringaTotale += columnValue + " " + rsmd.getColumnName(i);
	        	    }
	        	    stringaTotale += "\n";
	        	}
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
	}
}	