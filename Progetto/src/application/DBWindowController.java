package application;

import java.sql.*;

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

	private Connection connect = null;

	public DBWindowController() {
		LoginController provaPerConnector = new LoginController(); 
		this.connect = provaPerConnector.getDBConnection();
	}

	//metodo per visualizzare tutto il DB (SELECT * FROM UserList)
	public void copyButtonPushed(ActionEvent e) {
		
	        String sql = "SELECT * FROM UserList";  
	        
	        String stringaTotale = "";
	          
	        try {   
	            Statement stmt  = this.connect.createStatement();  
	            ResultSet rs    = stmt.executeQuery(sql);  
	              
	            // loop through the result set  
	            while (rs.next()) {  
	            	stringaTotale += rs.getString("email") + " - " + rs.getString("password") + "\n";  
	            }  
	            
	            listaDBcazzDB.setText(stringaTotale);
	        } catch (SQLException e1) {  
	            System.out.println(e1.getMessage());  
	        }
	}
	       
	//metodo per inserire una nuova riga nella tabella UserList
	public void insertNewRowButtonPushed(ActionEvent e) {
		String sql = "INSERT INTO UserList(email, password) VALUES('" + email.getText() + "', '" + password.getText() + "');";   
		try {   
            Statement stmt  = this.connect.createStatement();  
            stmt.executeUpdate(sql);  
        } catch (SQLException e1) {  
            System.out.println(e1.getMessage());  
        }
	}
	
	
}	