package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class SampleController implements Initializable{

	
	@FXML private Button loginButton;
	@FXML private Button signUpButton;
	@FXML private Button guestButton;
	
	@FXML private PasswordField emailPasswordField;
	@FXML private PasswordField pwPasswordField;
	
	@FXML private Label ErrorLabel;
	
	List<User> userList=new LinkedList<User>();
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ErrorLabel.setText("");
		User p1=new User("ziapo", "mignolina");
		userList.add(p1);
		System.out.println(p1.toString());
	}
		
	public void LoginButtonPushed() {
		String emailSup=emailPasswordField.getText();
		String pwSup=pwPasswordField.getText();
		
		User userSup = new User(emailSup, pwSup);
		System.out.println(userSup.toString());
		
		if(userSup.equals(userList.get(0))) {
			System.out.println("sono equals");
		}
		else
			System.out.println("NON sono equals");
		
		
		if(userList.contains(userSup)){
			ErrorLabel.setText("oke you can pass");
		}
		else {		
			ErrorLabel.setText("id e/o pw errati");
		}
	}
}
