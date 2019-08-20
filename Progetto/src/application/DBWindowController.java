package application;

import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class DBWindowController {
	@FXML private TextArea listaDBcazzDB;
	@FXML private Button clickToCopyDB;


	
	public void copyButtonPushed(ActionEvent e) {
		LoginController provaPerConnector = new LoginController();
		Connection connect = provaPerConnector.getDBConnection();
		
	        String sql = "SELECT * FROM UserList";  
	        
	        String stringaTotale = "";
	          
	        try {  
	            Connection conn = connect;  
	            Statement stmt  = conn.createStatement();  
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
