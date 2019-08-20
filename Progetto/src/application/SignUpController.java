package application;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController implements Initializable{
	
	
	@FXML private Button GoBackButton; 	
	@FXML private Button ConfirmButton;
	
	@FXML private Label SignUpErrorLabel;
	
	@FXML private TextField Name;
	@FXML private TextField Surname;
	@FXML private TextField Address;
	@FXML private TextField City;
	@FXML private TextField Cap;
	@FXML private TextField TelNum;
	@FXML private TextField Email;
	@FXML private TextField Password;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		SignUpErrorLabel.setText("");
	}
	
	public void GoBackButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	private boolean vefifyTextField() {
		boolean sup=true;
		
		if(Name.getText() == null || Name.getText().trim().isEmpty())
				sup=false;
		if(Surname.getText() == null || Surname.getText().trim().isEmpty())
			sup=false;
		if(Address.getText() == null || Address.getText().trim().isEmpty())
			sup=false;
		if(City.getText() == null || City.getText().trim().isEmpty())
			sup=false;
		if(Cap.getText() == null || Cap.getText().trim().isEmpty())
			sup=false;
		if(TelNum.getText() == null || TelNum.getText().trim().isEmpty())
			sup=false;
		if(Email.getText() == null || Email.getText().trim().isEmpty())
			sup=false;
		if(Password.getText() == null || Password.getText().trim().isEmpty())
			sup=false;
		
		return sup;
	}
	
	public void  ConfirmButtonPushed(ActionEvent event) throws IOException
    {
		if(!vefifyTextField()) {
			SignUpErrorLabel.setText("Tutti i campi devono essere compilati");
			return;
		}
		
		User sup=new User(Name.getText(), Surname.getText(), Address.getText(), City.getText(), 
				Cap.getText(), TelNum.getText(), Email.getText(), Password.getText());

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("LoginScene.fxml"));
		Parent tableViewParent = loader.load();
		
		LoginController controller = loader.getController();
		
		LinkedList<User> UserList=controller.getUserList();
		
		if(sup.verifyId(UserList)) {
			
			controller.addToUserList(sup);
			
			tableViewParent =  FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
	        Scene tableViewScene = new Scene(tableViewParent);
	        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	        window.setScene(tableViewScene);
	        window.show();
			SignUpErrorLabel.setText("id valido");
		}
		else
			SignUpErrorLabel.setText("Spiacenti la email che hai inserito � gi� stata utilizzata");
    }
}
