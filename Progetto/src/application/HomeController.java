package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.HashMap;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class HomeController implements Initializable{

	@FXML private Button SignOutButton;
	@FXML private Button PersonalAreaButton;
	@FXML private Label WellcomeLabel;
	@FXML private Button BasketButton;
	
	//stuff for the catalogo tab
	@FXML private TableView<Libro> tableViewCatalogo;
	@FXML private TableColumn<Libro, String> titoloColumnCatalogo;
	@FXML private TableColumn<Libro, String> autoreColumnCatalogo;
	@FXML private TableColumn<Libro, Integer> prezzoColumnCatalogo;
	@FXML private TableColumn<Libro, String> genereColumnCatalogo;
	
	@FXML private Button SeeDetailesButtonCatalogo;
	@FXML private Button AddToBasketButtonCatalogo;
	
	@FXML private ComboBox<String> genereComboBoxCatalogo;
	@FXML private Button searchButtonCatalogo;
	
	//stuff for the classifica tab
	@FXML private TableView<Libro> tableViewClassifica;
	@FXML private TableColumn<Libro, String> titoloColumnClassifica;
	@FXML private TableColumn<Libro, String> autoreColumnClassifica;
	@FXML private TableColumn<Libro, String> genereColumnClassifica;
	@FXML private TableColumn<Libro, Integer> posizioneColumnClassifica;
	@FXML private TableColumn<Libro, Integer> settimanePosColumnClassifica;
	
	@FXML private Button SeeDetailesButtonClassifica;
	@FXML private Button AddToBasketButtonClassifica;
	
	@FXML private ComboBox<String> genereComboBoxClassifica;
	@FXML private Button searchButtonClassifica;
	
	
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
	
	/*
	 * functions for the catalogo tab
	 */
	
	
	public void searchButtonCatalogoPushed(ActionEvent event) throws IOException{
		if(genereComboBoxCatalogo.getValue() == null) {
			AlertBox.display("Error", "Devi selezionare un genere per effettuare una ricerca");
			return;
		}
		String selectedGenere=genereComboBoxCatalogo.getValue().toString();
		
		tableViewCatalogo.setItems(getLibriCatalogo(selectedGenere));
	}
	
	private ObservableList<Libro> getLibriCatalogo(String genere) {
		
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
	
	public void addToBasketButtonCatalogoPushed() {
		Libro selectedLibro=this.tableViewCatalogo.getSelectionModel().getSelectedItem();
		if(selectedLibro==null) {
			AlertBox.display("Error", "Nessun libro selezionato");
			return;
		}
		else if(this.userLogged.getCarrello().contains(selectedLibro)) {
			AlertBox.display("Error", "Libro gi� presente nel tuo carrello\nPer rimuoverlo vai alla sezione carrello");
			return;
		}
		else {
			AlertBox.display("Hurray", "Libro aggiunto al tuo carrello");
			this.userLogged.addLibroToCarrello(selectedLibro);
			return;
		}
		
	}
	
	public void SeeDetailesButtonCatalogoPushed(ActionEvent event) throws IOException
    {
		controller.setUserLogged(userLogged);
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("DetailedBookScene.fxml"));
		Parent TableViewParent=loader.load();
		
		Scene tableViewScene = new Scene(TableViewParent);  
		
		DetailedBookController controller=loader.getController();
		
		//controllo se � stato selezionato qualcosa
		if(tableViewCatalogo.getSelectionModel().getSelectedItem() == null) {
			AlertBox.display("ERROR", "Non � stato selezionato nessun libro");
			return;
		}
		else controller.setBookData(tableViewCatalogo.getSelectionModel().getSelectedItem());
				
		controller.setBackPage("HomeScene.fxml");
		
		
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	
	
	/*
	 * functions for the classifica tab
	 */
	
	
	public void searchButtonClassificaPushed(ActionEvent event) throws IOException{
		if(genereComboBoxClassifica.getValue() == null) {
			AlertBox.display("Error", "Devi selezionare un genere per effettuare una ricerca");
			return;
		}
		String selectedGenere=genereComboBoxClassifica.getValue().toString();
		if(selectedGenere.equals("Tutti")) {
			selectedGenere=null;
		}
		
		this.tableViewClassifica.setItems(getLibriClassifica(selectedGenere));
		
	}
	
	private ObservableList<Libro> getLibriClassifica(String genere) {
		
		HashMap<List<Libro>, List<Integer>> mappa=Classifica.getClassifica(genere);
		List<Libro> classifica=Classifica.getBooksFromMap(mappa);
		List<Integer> settimane=Classifica.getWeeksFromMap(mappa);
		
		
		for(Libro l:classifica) {
			l.setPosizioneLocale(classifica.indexOf(l));
			l.setSettimaneLocale(settimane.get(classifica.indexOf(l)));
		}
		
		return FXCollections.observableArrayList(classifica);
	}
	
	
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){		
		
		//da modificare in seguito
		LoginController lc = new LoginController(); 
		userLogged = lc.getUserLogged();
		
		WellcomeLabel.setText("Welcome " +userLogged.getNome()+", good Shopping");
		
		/*
		 * stuff for the catalogo tab
		 */
		
		//set up the columns in the table
		titoloColumnCatalogo.setCellValueFactory(new PropertyValueFactory<Libro, String>("titolo"));
		autoreColumnCatalogo.setCellValueFactory(new PropertyValueFactory<Libro, String>("autore"));
		prezzoColumnCatalogo.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("prezzo"));
		genereColumnCatalogo.setCellValueFactory(new PropertyValueFactory<Libro, String>("genere"));
		
		//setItems must have an ObservableList as parameter, ObservableList almost like ArrayList	
		
		tableViewCatalogo.setItems(getLibriCatalogo("Tutti"));
		
		genereComboBoxCatalogo.getItems().addAll("Tutti","Romanzo", "Novit�", "Narrativa", "Ragazzi", "Fantascienza", "Poliziesco", "Storia", "Altro");
		
		if(this.userLogged.getEmail().equals("#####"))
			this.PersonalAreaButton.setText("Check Orders");
		
		
		/*
		 * stuff for the classifica tab
		 */
		
		genereComboBoxClassifica.getItems().addAll("Tutti","Romanzo", "Narrativa", "Ragazzi", "Fantascienza", "Poliziesco", "Storia", "Altro");
		titoloColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, String>("titolo"));
		autoreColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, String>("autore"));
		genereColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, String>("genere"));
		posizioneColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("posizioneLocale"));
		settimanePosColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("settimaneLocale"));
		
		 
		this.tableViewClassifica.setItems(this.getLibriClassifica("Tutti"));
		
		 
		//this.updateClassifica();
		
		this.getClassifica();
		
		
		this.visualizeAllClassifiche();
	}
	
	private void updateClassifica() {
		Classifica.updateClassifica(true);
	}
	
	private void getClassifica() {
		if(classificaGenerale == null) this.classificaGenerale = Classifica.getClassifica(null);
		
		if(classificaNovita == null) this.classificaNovita = Classifica.getClassifica("novit�");
		
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
