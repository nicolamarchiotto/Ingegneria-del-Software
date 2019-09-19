package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ResponsabileController implements Initializable{
	
	LoginController loginController = new LoginController();
	
	User respLogged=loginController.getUserLogged();

	@FXML private Label WellcomeLabel;
	
	//stuff for the add book part
	@FXML private TextField titolo;
	@FXML private TextField autori;
	@FXML private ComboBox<String> genere;
	@FXML private TextField casaEditrice;
	@FXML private TextField annoPubblicazione;
	@FXML private TextField prezzo;
	@FXML private TextArea breveDescrizione;
	
	@FXML private Button addToLibraryButton;
	@FXML private Button signOutButton;
	
	
	
	/*
	 * stuff for the catalogoviewPart
	 */
	
	@FXML private TableView<Libro> tableViewCatalogo;
	@FXML private TableColumn<Libro, String> titoloColumnCatalogo;
	@FXML private TableColumn<Libro, String> autoreColumnCatalogo;
	@FXML private TableColumn<Libro, Integer> prezzoColumnCatalogo;
	@FXML private TableColumn<Libro, String> genereColumnCatalogo;
	
	@FXML private Button SeeDetailesButtonCatalogo;
	@FXML private Button removeFromLibraryButton;
	
	@FXML private TextField searchTextFieldCatalogo;
	@FXML private Button searchButtonCatalogo;
	@FXML private Button resetButtonCatalogo;
	
	
	/*
	 * stuff for the classifica part
	 */
	
	@FXML private TableView<Libro> tableViewClassifica;
	@FXML private TableColumn<Libro, String> titoloColumnClassifica;
	@FXML private TableColumn<Libro, String> autoreColumnClassifica;
	@FXML private TableColumn<Libro, String> genereColumnClassifica;
	@FXML private TableColumn<Libro, Integer> posizioneColumnClassifica;
	@FXML private TableColumn<Libro, Integer> settimanePosColumnClassifica;
	
	@FXML private ComboBox<String> genereComboBoxClassifica;
	@FXML private Button searchButtonClassifica;
	
	//private List<Libro> listaCatalogoLocale=new ArrayList<Libro>();
	
	private ArrayList<HashMap<List<Libro>, List<Integer>>> vettoreMappe= new ArrayList<HashMap<List<Libro>, List<Integer>>>();
	
	
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
	 * stuff for the libroCard section
	 * 
	 * TableView objects accepts only one object, created new object UserForTableView
	 * with field contained both in User and LibroCard
	 */
	
	@FXML private TableView<User> tableViewCards;
	@FXML private TableColumn<User, String> idLibroCardColumn;
	@FXML private TableColumn<User, String> idUserColumn;
	@FXML private TableColumn<User, String> nomeUserColumn;
	@FXML private TableColumn<User, String> cognomeUserColumn;
	@FXML private TableColumn<User, Integer> saldoPuntiColumn;
	
	@FXML private Button updateAdminButton;
	
	/*
	 * stuff for the order section 
	 */
	
	@FXML private TableView<Ordine> tableViewOrders;
	@FXML private TableColumn<Ordine, String> codiceOrdineColumn;
	@FXML private TableColumn<Ordine, String> idAcquirenteColumn;
	@FXML private TableColumn<Ordine, String> dataAcquistoColumn;
	@FXML private TableColumn<Ordine, String> statoColumn;
	
	@FXML private Button seeDetailesButton;
	
	
	LoginController controller=new LoginController();
	
	public void SignOutButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	/*
	 * functions stuff for the add book part
	 */
	
	public void addToLibraryButtonPushed(ActionEvent event) throws IOException{
		
		//public Libro(String titolo, String autori, String casaeditrice, int annopubblicazione,
			//	String genere, double prezzo, String brevedescrizione, int punti)

		if(!emptyTextField()) {
			AlertBox.display("Error", "Tutti i campi devono essere compilati");
			return;
		}
		
		if(genere.getValue() == null) {
			AlertBox.display("Error", "Devi selezionare un genere");
			return;
		}

		try {
			if(verifyDateAndPrice()) {
				
				Libro l=new Libro(titolo.getText(), toACapoMode(autori.getText(), '-'), casaEditrice.getText(), Integer.valueOf(annoPubblicazione.getText()),
						genere.getValue().toString(), Double.valueOf(prezzo.getText()), breveDescrizione.getText(),(int)Math.round(Double.valueOf(prezzo.getText())));
				
				LinkedList<Libro> list=new LinkedList<Libro>();
				list.add(l);

				SQLException except = SqliteConnection.insertLibro(list);
				if(except == null) {
					System.out.println("Stampa libro\n"+l.toString()+" Genere "+l.getGenere());
					
					AlertBox.display("Book added", l.getTitolo()+" was added to the library");
					setTextToEmpty();
				}
				else {
					String message;
					if(except.getErrorCode() == 19) //ESISTE GIA' NEL DB
						message = "Book already exists in this library.";
					else message = "Something went wrong.";
					
					AlertBox.display("ERROR", message);
				}
				
			}
		}
		catch(NumberFormatException e) {
			AlertBox.display("Error","Anno di pubblicazione e prezzo\ndevono essere campi numerici");
			return;
		}
	}
	
	
	private void setTextToEmpty() {
		titolo.setText("");
		autori.setText("");
		genere.setValue("Genere");
		casaEditrice.setText("");
		annoPubblicazione.setText("");
		prezzo.setText("");
		breveDescrizione.setText("");
	}


	private boolean emptyTextField() {
		boolean sup=true;
		
		if(titolo.getText() == null || titolo.getText().trim().isEmpty())
				sup=false;
		if(autori.getText() == null || autori.getText().trim().isEmpty())
			sup=false;
		if(casaEditrice.getText() == null || casaEditrice.getText().trim().isEmpty())
			sup=false;
		if(annoPubblicazione.getText() == null || annoPubblicazione.getText().trim().isEmpty())
			sup=false;
		if(prezzo.getText() == null || prezzo.getText().trim().isEmpty())
			sup=false;
		if(breveDescrizione.getText() == null || breveDescrizione.getText().trim().isEmpty())
			sup=false;
		
		return sup;
	}
	
	private boolean verifyDateAndPrice() {
		
		return (Long.valueOf(annoPubblicazione.getText()) instanceof Long) && (Double.valueOf(prezzo.getText()) instanceof Double); 	
	}
	


	/*
	 * metodo che serve per creare una stringa da:
	 * 
	 * autore 1,autore 2,autore 3 a
	 * 
	 * autore 1
	 * autore 2
	 * autore 3
	 */
	
	private String toACapoMode(String s, char divisor) {
		
		String result="";
		int i;
		int indexIniz=0;
		
		for(i=0;i<s.length();i++) {
			if(s.charAt(i)==divisor) {
				result=result+s.substring(indexIniz,i)+"\n";
				i++;
				while(s.charAt(i)==' ') {
					System.out.println("\nvalore di i :"+i);
					i++;
				}
				indexIniz=i;
			}
		}
		
		return result+s.substring(indexIniz, s.length());
	}
	
	/*
	 * functions for the catalogoView part
	 */
	
	public void searchButtonCatalogoPushed(ActionEvent event) throws IOException{
		
		ResultSet booksFromDB = SqliteConnection.getFieldLibro();
		ObservableList<Libro> libri = FXCollections.observableArrayList(SqliteConnection.getAvailableBooks(booksFromDB));
		
		
		if(this.searchTextFieldCatalogo.getText() == null || this.searchTextFieldCatalogo.getText().trim().isEmpty()) {
			AlertBox.display("Errore", "Insert something in the search textfield");
			return;
		}
		else {
			ArrayList<Libro> libriCompatibili= new ArrayList<Libro>();
			String inserimento=this.searchTextFieldCatalogo.getText();
			
			for(Libro l:libri) {
				if(l.getTitolo().toLowerCase().contains(inserimento.toLowerCase())) {
					libriCompatibili.add(l);
				}
			}
			
			if(libriCompatibili.isEmpty()) {
				AlertBox.display("Error", "Nothing was found");
				return;
			}
			
			this.tableViewCatalogo.setItems(FXCollections.observableArrayList(libriCompatibili));
		}	
	}
	
	public void resetButtonPushed() {
		this.tableViewCatalogo.setItems(this.getLibriCatalogo("Tutti"));
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
	
	public void SeeDetailesButtonCatalogoPushed(ActionEvent event) throws IOException
    {
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
				
		controller.setBackPage("ResponsabileScene.fxml");
		
		
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	//rende non più disponibile il libro
	public void RemoveFromLibraryButtonPushed(ActionEvent event) throws IOException{
		Libro l=this.tableViewCatalogo.getSelectionModel().getSelectedItem();
		if(l==null) {
			AlertBox.display("Errore", "Non è stato selezionato alcun librp da rimuovere");
			return;
		}
					
		SqliteConnection.deleteLibro(l);
		
		AlertBox.display("Success", "Libro eliminato");
		
		this.tableViewCatalogo.setItems(this.getLibriCatalogo("Tutti"));
	}
	
	/*
	 * code for the classifica section
	 */
	
	public void searchButtonClassificaPushed(ActionEvent event) throws IOException{
		if(genereComboBoxClassifica.getValue() == null) {
			AlertBox.display("Error", "Devi selezionare un genere per effettuare una ricerca");
			return;
		}
		
		String selectedGenere=genereComboBoxClassifica.getValue().toString();
		
		this.tableViewClassifica.setItems(getLibriClassifica(selectedGenere));
		
	}
	
	private ObservableList<Libro> getLibriClassifica(String genere) {
		
		int indexComboBox=genereComboBoxClassifica.getItems().indexOf(genere);
			
		HashMap<List<Libro>, List<Integer>> mappa=this.vettoreMappe.get(indexComboBox);
		
		if(mappa == null) return null; //nessun libro di questo genere
		
		
		List<Libro> classifica=Classifica.getBooksFromMap(mappa);
		List<Integer> settimane=Classifica.getWeeksFromMap(mappa);
		
		
		for(Libro l:classifica) {
			l.setPosizioneLocale(classifica.indexOf(l)+1);
			l.setSettimaneLocale(settimane.get(classifica.indexOf(l)));
		}
		
		return FXCollections.observableArrayList(classifica);
	}
	
	private void getClassifica(boolean cond) {
		
		if(classificaGenerale == null || cond==true) 
			this.classificaGenerale = Classifica.getClassifica(null);
		
		if(classificaNovita == null || cond==true) 
			this.classificaNovita = Classifica.getClassifica("novità");
		
		if(classificaNarrativa == null || cond==true) 
			this.classificaNarrativa = Classifica.getClassifica("Narrativa");
		
		if(classificaStoria == null || cond==true) 
			this.classificaStoria = Classifica.getClassifica("Storia");
			
		if(classificaRomanzo == null || cond==true) 
			this.classificaRomanzo = Classifica.getClassifica("Romanzo");
			
		if(classificaFantascienza == null || cond==true) 
			this.classificaFantascienza = Classifica.getClassifica("Fantascienza");
	
		if(classificaRagazzi == null || cond==true) 
			this.classificaRagazzi = Classifica.getClassifica("Ragazzi");
			
		if(classificaPoliziesco == null || cond==true) 
			this.classificaPoliziesco = Classifica.getClassifica("Poliziesco");

		if(classificaAltro == null || cond==true) 
			this.classificaAltro = Classifica.getClassifica("Altro");
		
		this.vettoreMappe=new ArrayList<HashMap<List<Libro>, List<Integer>>>();
		
		vettoreMappe.add(this.classificaGenerale);
		vettoreMappe.add(this.classificaRomanzo);
		vettoreMappe.add(this.classificaNarrativa);
		vettoreMappe.add(this.classificaRagazzi);
		vettoreMappe.add(this.classificaFantascienza);
		vettoreMappe.add(this.classificaPoliziesco);
		vettoreMappe.add(this.classificaStoria);
		vettoreMappe.add(this.classificaAltro);
		
	}
	
	public void UpdateAdminButtonPushed(ActionEvent event) throws IOException{
		System.out.println("\n\n--------STO AGGIORNANDO LA CLASSIFICA DA RESPONSABILE--------\n\n");
		Classifica.updateClassifica(false); //aggiornamento effettuato come responsabile
		this.getClassifica(true);
		this.visualizeAllClassifiche();
		this.tableViewClassifica.setItems(this.getLibriClassifica("Tutti"));
	}
	
	
	/*
	 * functions for the libroCard section
	 */
	
	
	private ObservableList<User> getUser() {

		List<User> userList = SqliteConnection.getUserList(SqliteConnection.getFieldUser());

		//tolgo gli amministratori dalla lista di utenti in libroCard
		for(int i = 0; i < userList.size(); i++)
			if(userList.get(i).getLibroCard() == null) {
				userList.remove(i);
				i--;
			}
		
		ObservableList<User> user =FXCollections.observableArrayList(userList);
		
		return user;
	}
	
	
	
	
	/*
	 *functions for the orders section 
	 */
	
	private ObservableList<Ordine> getOrdini() {
		
		ObservableList<Ordine> orders = FXCollections.observableArrayList(SqliteConnection.getOrderList());
		
		return orders;
	}
	
	public void SeeDetailesButtonPushed(ActionEvent event) throws IOException
    {
		
		
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("DetailedOrdineScene.fxml"));
		Parent TableViewParent=loader.load();
		

		DetailedOrdineController controller=loader.getController();
		
		//controllo se è stato selezionato qualcosa
		if(tableViewOrders.getSelectionModel().getSelectedItem() == null) {
			AlertBox.display("ERROR", "Non è stato selezionato nessun ordine");
			return;
		}
		else{
			controller.setOrderFromTableView(tableViewOrders.getSelectionModel().getSelectedItem());
			controller.setBackPage("ResponsabileScene.fxml");
			controller.setLato("Responsabile");
		}
		
		
		
		Scene tableViewScene = new Scene(TableViewParent);  
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		WellcomeLabel.setText("Wellcome responsabile " +respLogged.getNome());
		
		//stuff for the genere choiceBox
		
		genere.getItems().addAll("Romanzo", "Narrativa", "Ragazzi", "Fantascienza", "Poliziesco", "Storia", "Altro");
		
		//code for the catalogo section
		
		//set up the columns in the table
		
		
		
		
		titoloColumnCatalogo.setCellValueFactory(new PropertyValueFactory<Libro, String>("titolo"));
		autoreColumnCatalogo.setCellValueFactory(new PropertyValueFactory<Libro, String>("autore"));
		prezzoColumnCatalogo.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("prezzo"));
		genereColumnCatalogo.setCellValueFactory(new PropertyValueFactory<Libro, String>("genere"));
				
		tableViewCatalogo.setItems(getLibriCatalogo("Tutti"));	
		 
		//code for the classifica section
		
		this.getClassifica(false);
		
		this.vettoreMappe.add(this.classificaGenerale);
		this.vettoreMappe.add(this.classificaRomanzo);
		this.vettoreMappe.add(this.classificaNarrativa);
		this.vettoreMappe.add(this.classificaRagazzi);
		this.vettoreMappe.add(this.classificaFantascienza);
		this.vettoreMappe.add(this.classificaPoliziesco);
		this.vettoreMappe.add(this.classificaStoria);
		this.vettoreMappe.add(this.classificaAltro);
		

		genereComboBoxClassifica.getItems().addAll("Tutti","Romanzo", "Narrativa", "Ragazzi", "Fantascienza", "Poliziesco", "Storia", "Altro");
		
		titoloColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, String>("titolo"));
		autoreColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, String>("autore"));
		genereColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, String>("genere"));
		posizioneColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("posizioneLocale"));
		settimanePosColumnClassifica.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("settimaneLocale"));
		
		this.tableViewClassifica.setItems(this.getLibriClassifica("Tutti"));
		
		//code for the libroCard Section
		
		idLibroCardColumn.setCellValueFactory(new PropertyValueFactory<User, String>("identificativoCarta"));
		idUserColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
		cognomeUserColumn.setCellValueFactory(new PropertyValueFactory<User, String>("cognome"));
		nomeUserColumn.setCellValueFactory(new PropertyValueFactory<User, String>("nome"));
		saldoPuntiColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("puntiCard"));
		
		//populates the tableView with dummy items
		//setItems must have an ObservableList as parameter, ObservableList almost like ArrayList	
		
		tableViewCards.setItems(getUser());
		
		//code for the Orders Section
		
		
		codiceOrdineColumn.setCellValueFactory(new PropertyValueFactory<Ordine, String>("idOrdine"));
		idAcquirenteColumn.setCellValueFactory(new PropertyValueFactory<Ordine, String>("idUser"));
		dataAcquistoColumn.setCellValueFactory(new PropertyValueFactory<Ordine, String>("data"));
		statoColumn.setCellValueFactory(new PropertyValueFactory<Ordine, String>("stato"));
		
		tableViewOrders.setItems(getOrdini());
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
		
		
		libri = Classifica.getBooksFromMap(this.classificaAltro);
		weeks = Classifica.getWeeksFromMap(this.classificaAltro);
		
		System.out.println("\nCLASSIFICA ALTRO\n");
		if(libri != null)
			for(int i = 0; i < libri.size(); i++) {
				System.out.println(libri.get(i).getTitolo() + "   " + weeks.get(i));
			}
		System.out.println("\n\n");
	}
	
	

}
