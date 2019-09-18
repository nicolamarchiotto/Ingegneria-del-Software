package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class HomeController implements Initializable{

	@FXML private TableView<Libro> tableView;
	@FXML private TableColumn<Libro, String> titoloColumn;
	@FXML private TableColumn<Libro, String> autoreColumn;
	@FXML private TableColumn<Libro, Integer> prezzoColumn;
	@FXML private TableColumn<Libro, String> genereColumn;
	
	@FXML private Button SignOutButton;
	@FXML private Button PersonalAreaButton;
	@FXML private Label WellcomeLabel;
	@FXML private Button SeeDetailesButton;
	@FXML private Button BasketButton;
	@FXML private Button AddToBasketButton;
	
	@FXML private ComboBox<String> genereComboBox;
	@FXML private Button searchButton;
	
	
	private HashMap<List<Libro>, List<Integer>> classificaGenerale = null;
	private HashMap<List<Libro>, List<Integer>> classificaNovita = null;
	private HashMap<List<Libro>, List<Integer>> classificaNarrativa = null;
	private HashMap<List<Libro>, List<Integer>> classificaStoria = null;
	private HashMap<List<Libro>, List<Integer>> classificaRomanzo = null;
	private HashMap<List<Libro>, List<Integer>> classificaFantascienza = null;
	private HashMap<List<Libro>, List<Integer>> classificaRagazzi = null;
	private HashMap<List<Libro>, List<Integer>> classificaPoliziesco = null;
	
	
	LoginController controller=new LoginController();
	
	private User userLogged;
	
	
	private ObservableList<Libro> getLibri(String genere) {
		
		ResultSet booksFromDB = SqliteConnection.getFieldLibro();
		
		
		ObservableList<Libro> libri = FXCollections.observableArrayList(SqliteConnection.getAvailableBooks(booksFromDB));
		
		if(genere!="Tutti") {
			for(int i=libri.size();i>0;i--) {
				if(libri.get(i-1).getGenere().compareTo(genere)!=0)
					libri.remove(i-1);
			}
		}
		return libri;
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
	
	//change scene to detailedBookView
	
	
	public void SeeDetailesButtonPushed(ActionEvent event) throws IOException
    {
		controller.setUserLogged(userLogged);
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("DetailedBookScene.fxml"));
		Parent TableViewParent=loader.load();
		
		Scene tableViewScene = new Scene(TableViewParent);  
		
		DetailedBookController controller=loader.getController();
		
		//controllo se è stato selezionato qualcosa
		if(tableView.getSelectionModel().getSelectedItem() == null) {
			AlertBox.display("ERROR", "Non è stato selezionato nessun libro");
			return;
		}
		else controller.setBookData(tableView.getSelectionModel().getSelectedItem());
				
		controller.setBackPage("HomeScene.fxml");
		
		
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	
	
	public void BasketButtonPushed(ActionEvent event) throws IOException
    {
		controller.setUserLogged(userLogged);
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("BasketScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	public void PersonalAreaButtonPushed(ActionEvent event) throws IOException
    {
		controller.setUserLogged(userLogged);
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("PersonalAreaScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	public void searchButtonPushed(ActionEvent event) throws IOException{
		if(genereComboBox.getValue() == null) {
			AlertBox.display("Error", "Devi selezionare un genere per effettuare una ricerca");
			return;
		}
		String selectedGenere=genereComboBox.getValue().toString();
		
		tableView.setItems(getLibri(selectedGenere));

	}
	
	public void addToBasketButtonPushed() {
		Libro selectedLibro=this.tableView.getSelectionModel().getSelectedItem();
		if(selectedLibro==null) {
			AlertBox.display("Error", "Nessun libro selezionato");
			return;
		}
		else if(this.userLogged.getCarrello().contains(selectedLibro)) {
			AlertBox.display("Error", "Libro già presente nel tuo carrello\nPer rimuoverlo vai alla sezione carrello");
			return;
		}
		else {
			AlertBox.display("Hurray", "Libro aggiunto al tuo carrello");
			this.userLogged.addLibroToCarrello(selectedLibro);
			return;
		}
		
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){		
		
		//da modificare in seguito
		LoginController lc = new LoginController(); 
		userLogged = lc.getUserLogged();
		
		WellcomeLabel.setText("Welcome " +userLogged.getNome()+", good Shopping");
		
		
		//set up the columns in the table
		titoloColumn.setCellValueFactory(new PropertyValueFactory<Libro, String>("titolo"));
		autoreColumn.setCellValueFactory(new PropertyValueFactory<Libro, String>("autore"));
		prezzoColumn.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("prezzo"));
		genereColumn.setCellValueFactory(new PropertyValueFactory<Libro, String>("genere"));
		
		//populates the tableView with dummy items
		//setItems must have an ObservableList as parameter, ObservableList almost like ArrayList	
		
		tableView.setItems(getLibri("Tutti"));
		
		genereComboBox.getItems().addAll("Tutti","Romanzo", "Novità", "Narrativa", "Ragazzi", "Fantascienza", "Poliziesco", "Storia", "Altro");
		
		if(this.userLogged.getEmail().equals("#####"))
			this.PersonalAreaButton.setText("Check Orders");
		 
		//this.updateClassifica();
		
		this.getClassifica();
		
		
		this.visualizeAllClassifiche();
	}
	
	private void updateClassifica() {
		Classifica.updateClassifica(true);
	}
	
	private void getClassifica() {
		if(classificaGenerale == null) this.classificaGenerale = Classifica.getClassifica(null);
		
		if(classificaNovita == null) this.classificaNovita = Classifica.getClassifica("novità");
		
		if(classificaNarrativa == null) this.classificaNarrativa = Classifica.getClassifica("Narrativa");
		
		if(classificaStoria == null) this.classificaStoria = Classifica.getClassifica("Storia");
			
		if(classificaRomanzo == null) this.classificaRomanzo = Classifica.getClassifica("Romanzo");
			
		if(classificaFantascienza == null) this.classificaFantascienza = Classifica.getClassifica("Fantascienza");
	
		if(classificaRagazzi == null) this.classificaRagazzi = Classifica.getClassifica("Ragazzi");
			
		if(classificaPoliziesco == null) this.classificaPoliziesco = Classifica.getClassifica("Poliziesco");
	}
	
	private void visualizeAllClassifiche() {
		List<Libro> libri = Classifica.getBooksFromMap(this.classificaGenerale);
		List<Integer> weeks = Classifica.getWeeksFromMap(this.classificaGenerale);
		
		System.out.println("\nCLASSIFICA GENERALE\n");
		if(libri != null)
			for(int i = 0; i < libri.size(); i++) {
				System.out.println(libri.get(i).getTitolo() + "   " + weeks.get(i));
			}
		System.out.println("\n\n");
		
		
		
		libri = Classifica.getBooksFromMap(this.classificaNovita);
		weeks = Classifica.getWeeksFromMap(this.classificaNovita);
		
		System.out.println("\nCLASSIFICA NOVITA\n");
		if(libri != null)
			for(int i = 0; i < libri.size(); i++) {
				System.out.println(libri.get(i).getTitolo() + "   " + weeks.get(i));
			}
		System.out.println("\n\n");
		
		
		libri = Classifica.getBooksFromMap(this.classificaNarrativa);
		weeks = Classifica.getWeeksFromMap(this.classificaNarrativa);
		
		System.out.println("\nCLASSIFICA NARRATIVA\n");
		if(libri != null)
			for(int i = 0; i < libri.size(); i++) {
				System.out.println(libri.get(i).getTitolo() + "   " + weeks.get(i));
			}
		System.out.println("\n\n");
		
		
		libri = Classifica.getBooksFromMap(this.classificaStoria);
		weeks = Classifica.getWeeksFromMap(this.classificaStoria);
		
		System.out.println("\nCLASSIFICA STORIA\n");
		if(libri != null)
			for(int i = 0; i < libri.size(); i++) {
				System.out.println(libri.get(i).getTitolo() + "   " + weeks.get(i));
			}
		System.out.println("\n\n");
		
		
		libri = Classifica.getBooksFromMap(this.classificaRomanzo);
		weeks = Classifica.getWeeksFromMap(this.classificaRomanzo);
		
		System.out.println("\nCLASSIFICA ROMANZO\n");
		if(libri != null)
			for(int i = 0; i < libri.size(); i++) {
				System.out.println(libri.get(i).getTitolo() + "   " + weeks.get(i));
			}
		System.out.println("\n\n");
		
		
		libri = Classifica.getBooksFromMap(this.classificaFantascienza);
		weeks = Classifica.getWeeksFromMap(this.classificaFantascienza);
		
		System.out.println("\nCLASSIFICA FANTASCIENZA\n");
		if(libri != null)
			for(int i = 0; i < libri.size(); i++) {
				System.out.println(libri.get(i).getTitolo() + "   " + weeks.get(i));
			}
		System.out.println("\n\n");
		
		
		libri = Classifica.getBooksFromMap(this.classificaRagazzi);
		weeks = Classifica.getWeeksFromMap(this.classificaRagazzi);
		
		System.out.println("\nCLASSIFICA RAGAZZI\n");
		if(libri != null)
			for(int i = 0; i < libri.size(); i++) {
				System.out.println(libri.get(i).getTitolo() + "   " + weeks.get(i));
			}
		System.out.println("\n\n");
		
		
		libri = Classifica.getBooksFromMap(this.classificaPoliziesco);
		weeks = Classifica.getWeeksFromMap(this.classificaPoliziesco);
		
		System.out.println("\nCLASSIFICA POLIZIESCO\n");
		if(libri != null)
			for(int i = 0; i < libri.size(); i++) {
				System.out.println(libri.get(i).getTitolo() + "   " + weeks.get(i));
			}
		System.out.println("\n\n");
	}

}
