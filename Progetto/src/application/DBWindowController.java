package application;

import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class DBWindowController {
	@FXML private TextArea listaDBcazzDB;
	@FXML private Button clickToCopyDB;
	private Connection connect = null;

	public DBWindowController() {
		LoginController provaPerConnector = new LoginController(); 
		this.connect = provaPerConnector.getDBConnection();
	}

	
	public void copyButtonPushed(ActionEvent e) {
		
	        String sql = "SELECT * FROM UserList";  
	        
	        String stringaTotale = "";
	        
	        LoginController provaPerConnector = new LoginController(); 
			this.connect = provaPerConnector.getDBConnection();
			System.out.println(connect);
	          
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
	
	
	
}	