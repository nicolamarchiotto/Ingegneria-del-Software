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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PersonalAreaController implements Initializable{

	@FXML private TabPane tabPane;
	@FXML private Tab personalDataTab;
	@FXML private Tab orderTab; 
	
	@FXML private Button goBackButton;
	@FXML private Button signOutButton;
	
	
	/*
	 * stuff fot the personal data tab
	 */
	@FXML private TextField name;
	@FXML private TextField surname;
	@FXML private TextField address;
	@FXML private TextField city;
	@FXML private TextField cap;
	@FXML private TextField telNumber;
	@FXML private TextField email;
	@FXML private TextField pw;
	
	LoginController controller=new LoginController();
	
	private User userLogged=controller.getUserLogged();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		name.setText(this.userLogged.getNome());
		surname.setText(this.userLogged.getCognome());
		address.setText(this.userLogged.getIndirizzoResidenza());
		city.setText(this.userLogged.getCittaResidenza());
		cap.setText(this.userLogged.getCapResidenza());
		telNumber.setText(this.userLogged.getTelefono());
		email.setText(this.userLogged.getEmail());
		pw.setText(this.userLogged.getPw());
	}
	
	public void SignOutButtonPushed(ActionEvent event) throws IOException
    {
		SqliteConnection.savingOnLogOut(userLogged); //saving on logOut
		controller.setUserLogged(null); //at this point no user is logged
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	public void goBackButtonPushed(ActionEvent event) throws IOException{
		
		controller.setUserLogged(userLogged);
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("HomeScene.fxml"));
		Parent TableViewParent=loader.load();
		
        Scene tableViewScene = new Scene(TableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
	
	public void saveChangesButtonPushed() {
		if(verifyField()) {
			
		}
	}

	private boolean verifyField() {
		// TODO Auto-generated method stub
		return false;
	}
}
