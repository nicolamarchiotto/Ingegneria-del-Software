package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
	@FXML private Button removeFromLibraryButton;
	@FXML private Button updateAdminButton;
	
	@FXML private Label errorLabel;
	
	
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
	
	/*
	 * stuff for the order section 
	 */
	
	@FXML private TableView<Ordine> tableViewOrders;
	@FXML private TableColumn<Ordine, String> codiceOrdineColumn;
	@FXML private TableColumn<Ordine, String> idAcquirenteColumn;
	@FXML private TableColumn<Ordine, String> dataAcquistoColumn;
	@FXML private TableColumn<Ordine, String> statoColumn;
	
	@FXML private Button seeDetailesButton;
	
	//FIXME
	LoginController controller=new LoginController();
	
	public void addToLibraryButtonPushed(ActionEvent event) throws IOException{
		
		//public Libro(String titolo, String autori, String casaeditrice, int annopubblicazione,
			//	String genere, double prezzo, String brevedescrizione, int punti)

		if(!emptyTextField()) {
			errorLabel.setText("Tutti i campi devono essere compilati");
			return;
		}
		
		if(genere.getValue() == null) {
			errorLabel.setText("Devi selezionare un genere");
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
		errorLabel.setText("");
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
	
	public void SignOutButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	//functions for the libroCard section
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
	
	
	//functions for the orders section
	
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
	
	//rende non più disponibile il libro
	public void RemoveFromLibraryButtonPushed(ActionEvent event) throws IOException{
		Libro l=new Libro(titolo.getText(), toACapoMode(autori.getText(), '-'), casaEditrice.getText(), Integer.valueOf(annoPubblicazione.getText()),
				genere.getValue().toString(), Double.valueOf(prezzo.getText()), breveDescrizione.getText(),(int)Math.round(Double.valueOf(prezzo.getText())));
		
		SqliteConnection.deleteLibro(l);
	}
	
	public void UpdateAdminButtonPushed(ActionEvent event) throws IOException{
		Classifica.updateClassifica(false); //aggiornamento effettuato come responsabile
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		WellcomeLabel.setText("Wellcome responsabile " +respLogged.getNome());
		
		//stuff for the genere choiceBox
		
		errorLabel.setText("");
		
		genere.getItems().addAll("Romanzo", "Novità", "Narrativa", "Ragazzi", "Fantascienza", "Poliziesco", "Storia", "Altro");
		 
		
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
	
	

}
