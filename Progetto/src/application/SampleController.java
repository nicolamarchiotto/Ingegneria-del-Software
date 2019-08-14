package application;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class SampleController implements Initializable{

	//hbsdjb
	//hhhhh
	//hbhjbs
	
	@FXML private Button LoginButton;
	@FXML private Button SignUpButton;
	@FXML private Button GuestButton;
	
	@FXML private PasswordField IdPasswordField;
	@FXML private PasswordField PwPasswordField;
	
	@FXML private Label ErrorLabel;
	
	private List<User> userList=new LinkedList<User>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
		
	public void LoginButtonPushed() {
		String idSup=IdPasswordField.getText();
		String pwSup=PwPasswordField.getText();
		
		User userSup = new User(idSup, pwSup);
		
		if(!userList.contains(userSup)){
			ErrorLabel.setText("id e/o pw errati");
		}
	}
}
