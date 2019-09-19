package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	@FXML private Button updateButton;
	
	@FXML private TextField emailPasswordField;
	@FXML private PasswordField pwPasswordField;
	
	@FXML private Label ErrorLabel;
	
	private static ObservableList<Libro> libriGlobal = FXCollections.observableArrayList();
	
	private static ArrayList<HashMap<List<Libro>, List<Integer>>> vettoreMappe = new ArrayList<HashMap<List<Libro>, List<Integer>>>();
	
	private HashMap<List<Libro>, List<Integer>> classificaGenerale = null;
	private HashMap<List<Libro>, List<Integer>> classificaNovita = null;
	private HashMap<List<Libro>, List<Integer>> classificaNarrativa = null;
	private HashMap<List<Libro>, List<Integer>> classificaStoria = null;
	private HashMap<List<Libro>, List<Integer>> classificaRomanzo = null;
	private HashMap<List<Libro>, List<Integer>> classificaFantascienza = null;
	private HashMap<List<Libro>, List<Integer>> classificaRagazzi = null;
	private HashMap<List<Libro>, List<Integer>> classificaPoliziesco = null;
	private HashMap<List<Libro>, List<Integer>> classificaAltro = null;
	
	
	
	/*
	 * IMPORTANTE
	 */
	//userList locale di riferimento a tutti i file Locali
	private static User userLogged = null;
	
	private List<User> userList=new LinkedList<User>();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.libriGlobal=this.getLibriCatalogoFromDB();
		
		
		this.vettoreMappe.add(this.classificaGenerale);
		this.vettoreMappe.add(this.classificaRomanzo);
		this.vettoreMappe.add(this.classificaNarrativa);
		this.vettoreMappe.add(this.classificaRagazzi);
		this.vettoreMappe.add(this.classificaFantascienza);
		this.vettoreMappe.add(this.classificaPoliziesco);
		this.vettoreMappe.add(this.classificaStoria);
		this.vettoreMappe.add(this.classificaAltro);
		
		this.getClassifica(false);
		
		this.ErrorLabel.setText("");
		
		//aggiungo i vari user salvati nel DB
		this.userList = SqliteConnection.getUserList(SqliteConnection.getFieldUser());
		
		System.out.println("Users possibili: \n" + this.userList);
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
    
    //esegue l'aggiornamento settimanale
    public void UpdateSettimanaleButtonPushed(ActionEvent event) throws IOException
    {
    	Classifica.updateClassifica(true); //eseguo update settimanale
    }
    
    
    //tenta il login, se ha successo porta a home
	public void LoginButtonPushed(ActionEvent event) throws IOException {
		String emailSup=emailPasswordField.getText().trim();
		String pwSup=pwPasswordField.getText();
		
		User userSup = new User(emailSup, pwSup);
		
		List<User> userList=getUserList();
		
		userSup=getUserFromListDB(userList, userSup);

		if(userSup!=null){
			setUserLogged(userSup);

			System.out.println("Login effettuato con -> " + userSup.getEmail());
			
			Parent tableViewParent;
			Scene tableViewScene;
			Stage window;

			System.out.println("Valore Librocard: "+userSup.getLibroCard());
			
			
		    if(userSup.getLibroCard()!=null) {
			 	tableViewParent =  FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
				tableViewScene = new Scene(tableViewParent);
			    window = (Stage)((Node)event.getSource()).getScene().getWindow();
			    
			 	window.setOnCloseRequest(pressingTheX -> { //salvo i cambiamenti alla pressione di [X]
			 		if(!this.userLogged.getEmail().equals("#####") && this.userLogged != null) {
			 			System.out.println("Closing up.. I am " + userLogged.getEmail());
				 		SqliteConnection.savingOnLogOut(userLogged); 
			 		}
			 	});
		    }
			else {
				tableViewParent =  FXMLLoader.load(getClass().getResource("ResponsabileScene.fxml"));
				tableViewScene = new Scene(tableViewParent);
			    window = (Stage)((Node)event.getSource()).getScene().getWindow();
			}

			
		    window.setScene(tableViewScene);
		    window.show();
		}		
		else {		
			ErrorLabel.setText("id e/o pw errati");
		}	
	}
	
	public void EnterAsGuestButtonPushed(ActionEvent event) throws IOException
    {
		this.userLogged=new User("Guest", "Guest", "", "", "", "", "#####", "#####");
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
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
	
	private ObservableList<Libro> getLibriCatalogoFromDB() {
		
		ResultSet booksFromDB = SqliteConnection.getFieldLibro();
		ObservableList<Libro> libri = FXCollections.observableArrayList(SqliteConnection.getAvailableBooks(booksFromDB));
		
		return libri;
	}
	
	public ObservableList<Libro> getBookListGlobalFromLoginController() {
		return this.libriGlobal;
	}
	
	private void getClassifica(boolean cond) {
		if(classificaGenerale == null || cond==true) this.classificaGenerale = Classifica.getClassifica(null);
		
		if(classificaNovita == null || cond==true) this.classificaNovita = Classifica.getClassifica("novit�");
		
		if(classificaNarrativa == null || cond==true) this.classificaNarrativa = Classifica.getClassifica("Narrativa");
		
		if(classificaStoria == null || cond==true) this.classificaStoria = Classifica.getClassifica("Storia");
			
		if(classificaRomanzo == null || cond==true) this.classificaRomanzo = Classifica.getClassifica("Romanzo");
			
		if(classificaFantascienza == null || cond==true) this.classificaFantascienza = Classifica.getClassifica("Fantascienza");
	
		if(classificaRagazzi == null || cond==true) this.classificaRagazzi = Classifica.getClassifica("Ragazzi");
			
		if(classificaPoliziesco == null || cond==true) this.classificaPoliziesco = Classifica.getClassifica("Poliziesco");

		if(classificaAltro == null || cond==true) this.classificaAltro = Classifica.getClassifica("Altro");
	}
	
	public ArrayList<HashMap<List<Libro>, List<Integer>>> getVettoreMappeClassificaFromLoginController(){
		return this.vettoreMappe;
	}
	


	//TODO
	
	//sarebbe possibile creare un utente di supporto nel DB solo per capire chi � loggato?
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
	
	public User getUserLogged() {
		return userLogged;
	}
	
	//Deve aggiungere utente a lista
	public void addToUserList(User other) {
		userList.add(other);
	}
	
	//deve ritornare lista dal DB
	public List<User> getUserList() {
		return userList;
	}
	
	/*
	 * Aggiunge libro a carrello in userLogged, quando si far� signOut si dovr�
	 * andare a sovrascrivere quello globale con quello locale
	 * per eventuali cambiamenti/ordini effettuati
	 */
	
	public void addLibroToCarrelloAUserLoggedLocal(Libro l) {
		LoginController.userLogged.addLibroToCarrello(l);
	}
}
