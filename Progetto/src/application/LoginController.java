package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	@FXML private Button dbButton;
	
	@FXML private PasswordField emailPasswordField;
	@FXML private PasswordField pwPasswordField;
	
	@FXML private Label ErrorLabel;
	
	private static User userLogged;
	
	//da cancellare, lavoreremo solo con quella del DB
	LinkedList<User> userList=new LinkedList<User>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.ErrorLabel.setText("");
		
		//ricordati di toglierlo, admin solo in locale
		User p1=new User("admin", "root");
		userList.add(p1);
	}
	
	
	//metodo di prova (RIMUOVERE) per visualizzare il DB e provare a vedere se � tutto ok
	public void DBButtonPushed(ActionEvent event) throws IOException{
		Parent tableViewParent =  FXMLLoader.load(getClass().getResource("DBWindowShow.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = new Stage(); //facendo new Stage si overlappano le finestre
        window.setScene(tableViewScene);
        window.setX(((Node)event.getSource()).getScene().getWindow().getWidth());
        window.show();
	}
	
    public void SignUpButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("SignUpScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
  
	public void LoginButtonPushed(ActionEvent event) throws IOException {
		String emailSup=emailPasswordField.getText();
		String pwSup=pwPasswordField.getText();
		
		
		//attenzione eliminare utente in eccesso
		User userSup = new User(emailSup, pwSup);
		System.out.println(userSup.toString());
		
		LinkedList<User> userList=getUserList();
		
		if(userList.contains(userSup)){
			setUserLogged(userSup);
			Parent tableViewParent =  FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
			Scene tableViewScene = new Scene(tableViewParent);
		    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		    window.setScene(tableViewScene);
		    window.show();
		}		
		else {		
			ErrorLabel.setText("id e/o pw errati");
		}	
	}
	
	
	//sarebbe possibile creare un utente di supporto nel DB solo per capire chi � loggato?
	//mettere tutti i metodi relativi al db in un unico file, anche userlogged
	
	public void setUserLogged( User other) {
		//cercare user u nella lista db e settarlo alla variabile useLogged
		LinkedList<User> l = getUserList();
		
		for(User u: l) {
			if(u.compareTo(other)==0) {
				userLogged=u;
				return;
			}
		}
	}
	
	//non sicuro dell'implementazione, sarebbe ,meglio chiederlo direttamnete al DB
	
	public User getUserLogged() {
		return userLogged;
	}
	
	//Deve aggiungere utente a lista
	public void addToUserList(User other) {
		userList.add(other);
	}
	
	//deve ritornare lista del DB
	public LinkedList<User> getUserList() {
		return userList;
	}
}
