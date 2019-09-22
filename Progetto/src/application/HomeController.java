package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class HomeController implements Initializable{

	//upper part of the scene
	@FXML private Button SignOutButton;
	@FXML private Button PersonalAreaButton;
	@FXML private Label WellcomeLabel;
	@FXML private Button BasketButton;
	
	//all the AVAILABLE books
	private ObservableList<Libro> libriGlobal = FXCollections.observableArrayList();
	
	//catalogo tab components
	@FXML private TableView<Libro> tableViewCatalogo;
	@FXML private TableColumn<Libro, String> titoloColumnCatalogo;
	@FXML private TableColumn<Libro, String> autoreColumnCatalogo;
	@FXML private TableColumn<Libro, Integer> prezzoColumnCatalogo;
	@FXML private TableColumn<Libro, String> genereColumnCatalogo;
	
	@FXML private Button SeeDetailesButtonCatalogo;
	@FXML private Button AddToBasketButtonCatalogo;
	
	@FXML private TextField searchTextFieldCatalogo;
	@FXML private Button searchButtonCatalogo;
	@FXML private Button resetButtonCatalogo;
	
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
	
	
	private ArrayList<HashMap<List<Libro>, List<Integer>>> vettoreMappe=new ArrayList<HashMap<List<Libro>, List<Integer>>>();
	
	
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
		
		ObservableList<Libro> libriCompatibili = FXCollections.observableArrayList();
		
		
		if(this.searchTextFieldCatalogo.getText() == null || this.searchTextFieldCatalogo.getText().trim().isEmpty()) {
			AlertBox.display("Errore", "Insert something in the search textfield");
			return;
		}
		else {
			String inserimento=this.searchTextFieldCatalogo.getText();
			
			for(Libro l:this.libriGlobal) {
				if(l.getTitolo().toLowerCase().contains(inserimento.toLowerCase())) {
					libriCompatibili.add(l);
				}
			}
			
			if(libriCompatibili.isEmpty()) {
				AlertBox.display("Error", "Nothing was found");
				return;
			}
			
			this.tableViewCatalogo.setItems(libriCompatibili);
		}	
	}
	
	public void resetButtonPushed() {
		this.tableViewCatalogo.setItems(this.libriGlobal);
	}
	
	public void addToBasketButtonCatalogoPushed() {
		Libro selectedLibro=this.tableViewCatalogo.getSelectionModel().getSelectedItem();
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
	
	public void SeeDetailesButtonCatalogoPushed(ActionEvent event) throws IOException
    {
		controller.setUserLogged(userLogged);
		
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("DetailedBookScene.fxml"));
		Parent TableViewParent=loader.load();
		
		Scene tableViewScene = new Scene(TableViewParent);  
		
		DetailedBookController controller=loader.getController();
		
		//controllo se è stato selezionato qualcosa
		if(tableViewCatalogo.getSelectionModel().getSelectedItem() == null) {
			AlertBox.display("ERROR", "Non è stato selezionato nessun libro");
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
		
		int indexComboBox=genereComboBoxClassifica.getItems().indexOf(selectedGenere);
		
		this.tableViewClassifica.setItems(getLibriClassifica(indexComboBox));
		
	}
	
	private ObservableList<Libro> getLibriClassifica(int posizioneGenere) {
		
		
		HashMap<List<Libro>, List<Integer>> mappa=this.vettoreMappe.get(posizioneGenere);
		
		if(mappa == null) return null; //nessun libro di questo genere
		
		
		List<Libro> classifica=Classifica.getBooksFromMap(mappa);
		List<Integer> settimane=Classifica.getWeeksFromMap(mappa);
		
		
		for(Libro l:classifica) {
			l.setPosizioneLocale(classifica.indexOf(l)+1);
			l.setSettimaneLocale(settimane.get(classifica.indexOf(l)));
		}
		
		return FXCollections.observableArrayList(classifica);
	}
	
	public void addToBasketButtonClassificaPushed() {
		Libro selectedLibro=this.tableViewClassifica.getSelectionModel().getSelectedItem();
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
	
	public void SeeDetailesButtonClassificaPushed(ActionEvent event) throws IOException
    {
		controller.setUserLogged(userLogged);
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("DetailedBookScene.fxml"));
		Parent TableViewParent=loader.load();
		
		Scene tableViewScene = new Scene(TableViewParent);  
		
		DetailedBookController controller=loader.getController();
		
		//controllo se è stato selezionato qualcosa
		if(tableViewClassifica.getSelectionModel().getSelectedItem() == null) {
			AlertBox.display("ERROR", "Non è stato selezionato nessun libro");
			return;
		}
		else controller.setBookData(tableViewClassifica.getSelectionModel().getSelectedItem());
				
		controller.setBackPage("HomeScene.fxml");
		
		
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){		
		
		//da modificare in seguito
		LoginController lc = new LoginController(); 
		userLogged = lc.getUserLogged();
		
		this.libriGlobal=lc.getBookListGlobalFromLoginController();
		
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
		
		tableViewCatalogo.setItems(this.libriGlobal);
		
		
		if(this.userLogged.getEmail().equals("#####"))
			this.PersonalAreaButton.setText("Check Orders");
		
		
		/*
		 * stuff for the classifica tab
		 */
		
		this.vettoreMappe = lc.getVettoreMappeClassificaFromLoginController();
		
		
		
		genereComboBoxClassifica.getItems().addAll("Tutti","Romanzo", "Narrativa", "Ragazzi", "Fantascienza", "Poliziesco", "Storia", "Altro");
		
		titoloColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, String>("titolo"));
		autoreColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, String>("autore"));
		genereColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, String>("genere"));
		posizioneColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("posizioneLocale"));
		settimanePosColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("settimaneLocale"));
		
		 
		this.tableViewClassifica.setItems(this.getLibriClassifica(0));
	}
	
	private void updateClassifica() {
		Classifica.updateClassifica(true);
	}
	
	/*
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
		
		
		libri = Classifica.getBooksFromMap(this.classificaAltro);
		weeks = Classifica.getWeeksFromMap(this.classificaAltro);
		
		System.out.println("\nCLASSIFICA ALTRO\n");
		if(libri != null)
			for(int i = 0; i < libri.size(); i++) {
				System.out.println(libri.get(i).getTitolo() + "   " + weeks.get(i));
			}
		System.out.println("\n\n");
	}*/

}
