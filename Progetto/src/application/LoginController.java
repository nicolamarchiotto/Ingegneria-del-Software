package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable{

	
	@FXML private Button loginButton;
	@FXML private Button signUpButton;
	@FXML private Button guestButton;
	@FXML private Button dbButton;
	
	@FXML private TextField emailPasswordField;
	@FXML private PasswordField pwPasswordField;
	
	@FXML private Label ErrorLabel;
	
	private static User userLogged;
	
	//userList locale
	private List<User> userList=new LinkedList<User>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.ErrorLabel.setText("");
		
		//aggiungo i vari user salvati nel DB
		userList = SqliteConnection.getUserList(SqliteConnection.getEveryFieldUser());
	}
	
	
	//metodo di prova (RIMUOVERE) per visualizzare il DB e provare a vedere se è tutto ok
	public void DBButtonPushed(ActionEvent event) throws IOException{
		Parent tableViewParent =  FXMLLoader.load(getClass().getResource("DBWindowShow.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = new Stage(); //facendo new Stage si overlappano le finestre
        window.setScene(tableViewScene);
        window.setX(((Node)event.getSource()).getScene().getWindow().getWidth());
        window.show();
	}
	
	
	//porta a visualizzazione signUp
    public void SignUpButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("SignUpScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
    
    
    //tenta il login, se ha successo porta a home
	public void LoginButtonPushed(ActionEvent event) throws IOException {
		String emailSup=emailPasswordField.getText();
		String pwSup=pwPasswordField.getText();
		
		User userSup = new User(emailSup, pwSup);
		System.out.println(userSup.toString());
		
		List<User> userList=getUserList();
		
		userSup=getUserFromListDB(userList, userSup);
		
		if(userSup!=null){
			setUserLogged(userSup);
			
			Parent tableViewParent;

			System.out.println("Valore Librocard: "+userSup.getLibroCard());
			if(userSup.getLibroCard()!=null)
			 	tableViewParent =  FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
			else
				tableViewParent =  FXMLLoader.load(getClass().getResource("ResponsabileScene.fxml"));

			Scene tableViewScene = new Scene(tableViewParent);
		    Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		    window.setScene(tableViewScene);
		    window.show();
		}		
		else {		
			ErrorLabel.setText("id e/o pw errati");
		}	
	}
	
	private User getUserFromListDB(List<User> userList, User userSup) {
		User userNotFound=null;
		
		String idUser=userSup.getEmail();
		String pwUser=userSup.getPw();
		
		
		for(User u: userList) {
			if((idUser.compareTo(u.getEmail())==0) && (pwUser.compareTo(u.getPw())==0)) {
				return u;
			}
		}
		return userNotFound;	
	}


	//TODO
	
	//sarebbe possibile creare un utente di supporto nel DB solo per capire chi è loggato?
	//mettere tutti i metodi relativi al db in un unico file, anche userlogged
	
	public void setUserLogged(User other) {
		//cercare user u nella lista db e settarlo alla variabile useLogged
		List<User> l = getUserList();
		
		for(User u: l) {
			if(u.compareTo(other)==0) {
				userLogged=u;
				//userLogged.setListaOrdini();
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
	public List<User> getUserList() {
		return userList;
	}
}
