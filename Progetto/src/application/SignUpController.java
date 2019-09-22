package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController implements Initializable{
	
	
	@FXML private Button GoBackButton; 	
	@FXML private Button ConfirmButton;
	
	@FXML private TextField Name;
	@FXML private TextField Surname;
	@FXML private TextField Address;
	@FXML private TextField City;
	@FXML private TextField Cap;
	@FXML private TextField TelNum;
	@FXML private TextField Email;
	@FXML private TextField Password;
	
	private List<User> UserList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ResultSet usersFromDB = DBUser.getFieldUser();
		UserList=DBUser.getUserList(usersFromDB);
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
		
		
		if(Cap.getText().length() != 5) {//il cap deve essere esattamente di 5 cifre
			AlertBox.display("Error", "CAP must be numeric and composed by five numbers");
			sup=false;
		}
		else if(TelNum.getText().length() > 11 || TelNum.getText().length() < 10) { //il numero telefonico deve essere di 10-11 caratteri
			AlertBox.display("Error", "Insert a valid telephone number");
			sup=false;
		}
		else if(Email.getText().length() < 6) {//non � possibile inserire una email con meno di 6 caratteri
			AlertBox.display("Error", "Email must be at least 6 characters long");
			sup=false;
		}
		else if(Password.getText().length() < 6) {//non � possibile inserire una password con meno di 6 caratteri
			AlertBox.display("Error", "Password must be at least 6 characters long");
			sup=false;
		}
		else {
			try {//testo se la stringa presa dal cap � effettivamente un numero o meno
				Integer.parseInt(Cap.getText());
			}
			catch(NumberFormatException e){
				AlertBox.display("Error", "Insert a valid numeric CAP");
				sup=false;
			}
			
			try {//testo se la stringa presa dal numero di telefono � effettivamente un numero o meno
				Integer.parseInt(TelNum.getText());
			}
			catch(NumberFormatException e){
				AlertBox.display("Error", "Insert a valid numeric telephone number");
				sup=false;
			}
		}
		
		return sup;
	}
	
	public void  ConfirmButtonPushed(ActionEvent event) throws IOException{
		
		if(!vefifyTextField()) {
			AlertBox.display("Error","All field must be filled");
			return;
		}
		
	
		User sup=new User(Name.getText(), Surname.getText(), Address.getText(), Cap.getText(), 
				City.getText(), TelNum.getText(), Email.getText(), Password.getText());

		//get UserList From DB	
		for(User u: UserList) {
			System.out.println(u.toString());
		}
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("LoginScene.fxml"));
		Parent tableViewParent = loader.load();
		
		LoginController controller = loader.getController();
		
		
		if(sup.verifyId(UserList)) {
				
			//Aggiunge a DB
			controller.addToUserList(sup);
			DBUser.insertUser(sup);
			controller.setUserLogged(sup);
			

			tableViewParent =  FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
	        Scene tableViewScene = new Scene(tableViewParent);
	        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	        
	        window.setOnCloseRequest(pressingTheX -> { //salvo i cambiamenti alla pressione di [X]
		 		SqliteConnection.savingOnLogOut(controller.getUserLogged()); 
		 	});
	        
	        window.setScene(tableViewScene);
	        window.show();
		}
		else {
			AlertBox.display("Error", "We are sorry, your email has already been taken");	
		}
	}
}
