package application;

import java.io.IOException;
import java.net.URL;
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
	
	public void ConfirmButtonPushed(){
    	
		User sup=new User(Name.getText(), Surname.getText(), Address.getText(), City.getText(), 
				Cap.getText(), TelNum.getText(), Email.getText(), Password.getText());
		
		
    }
	
	
	
	

}
