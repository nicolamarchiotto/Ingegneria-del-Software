package application;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class LoginController implements Initializable{

	
	@FXML private Button loginButton;
	@FXML private Button signUpButton;
	@FXML private Button guestButton;
	
	@FXML private PasswordField emailPasswordField;
	@FXML private PasswordField pwPasswordField;
	
	@FXML private Label ErrorLabel;
	
	LinkedList<User> userList=new LinkedList<User>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.ErrorLabel.setText("");
		User p1=new User("ziapo", "mignolina");
		userList.add(p1);
		
		if(!userList.isEmpty())
			System.out.println(p1.toString());
		else
			System.out.println("vuota");		
	}
	
    public void SignUpButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("SignUpScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
  
	public void LoginButtonPushed() {
		String emailSup=emailPasswordField.getText();
		String pwSup=pwPasswordField.getText();
		
		
		//attenzione eliminare utente in eccesso
		User userSup = new User(emailSup, pwSup);
		System.out.println(userSup.toString());
		
		if(userList.contains(userSup)){
			ErrorLabel.setText("oke you can pass");
		}
		else {		
			ErrorLabel.setText("id e/o pw errati");
		}	
	}
	
	public LinkedList<User> getUserList() {
		return userList;
	}
}
