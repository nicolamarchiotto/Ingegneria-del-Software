package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDateTime;
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
	@FXML private TextField genere;
	@FXML private TextField casaEditrice;
	@FXML private TextField annoPubblicazione;
	@FXML private TextField prezzo;
	@FXML private TextArea breveDescrizione;
	
	@FXML private Button addToLibraryButton;
	@FXML private Button signOutButton;
	
	@FXML private Label errorLabel;
	
	
	/*
	 * stuff for the libroCard section
	 * 
	 * TableView objects accepts only one object, created new object UserForTableView
	 * with field contained both in User and LibroCard
	 */
	
	@FXML private TableView<UserForTableView> tableViewCards;
	@FXML private TableColumn<UserForTableView, String> idLibroCardColumn;
	@FXML private TableColumn<UserForTableView, String> idUserColumn;
	@FXML private TableColumn<UserForTableView, String> nomeUserColumn;
	@FXML private TableColumn<UserForTableView, String> cognomeUserColumn;
	@FXML private TableColumn<UserForTableView, Integer> saldoPuntiColumn;
	
	/*
	 * stuff for the order section 
	 */
	
	@FXML private TableView<OrdineForTableView> tableViewOrders;
	@FXML private TableColumn<OrdineForTableView, String> codiceOrdineColumn;
	@FXML private TableColumn<OrdineForTableView, String> idAcquirenteColumn;
	@FXML private TableColumn<OrdineForTableView, String> dataAcquistoColumn;
	@FXML private TableColumn<OrdineForTableView, String> statoColumn;
	
	
	
	
	public void addToLibraryButtonPushed(ActionEvent event) throws IOException{
		
		//public Libro(String titolo, String autori, String casaeditrice, int annopubblicazione,
			//	String genere, double prezzo, String brevedescrizione, int punti)

		if(!emptyTextField()) {
			errorLabel.setText("Tutti i campi devono essere compilati");
			return;
		}

		try {
			if(verifyDateAndPrice()) {
				
				Libro l=new Libro(titolo.getText(), toACapoMode(autori.getText(), '-'), casaEditrice.getText(), Integer.valueOf(annoPubblicazione.getText()),
						genere.getText(), Double.valueOf(prezzo.getText()), breveDescrizione.getText(),(int)Math.round(Double.valueOf(prezzo.getText())));
			
				//TODO
				/*
				 * Stampa libro da cancellare
				 * va fatto inserimento libro nel database
				 * controllare se non c'è già lo stesso libro??
				 */
				System.out.println("Stampa libro\n"+l.toString());
				
				AlertBox.display("Book added", l.getTitolo()+" was added to the library");
				setTextToEmpty();
				
			}
		}
		catch(NumberFormatException e) {
			errorLabel.setText("Anno di pubblicazione e prezzo devono essere campi numerici");
			return;
		}
	}
	
	
	private void setTextToEmpty() {
		titolo.setText("");
		autori.setText("");
		genere.setText("");
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
		if(genere.getText() == null || genere.getText().trim().isEmpty())
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
		
		return (Integer.valueOf(annoPubblicazione.getText()) instanceof Integer) && (Double.valueOf(prezzo.getText()) instanceof Double); 	
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
	private ObservableList<UserForTableView> getUser() {
		ObservableList<UserForTableView> user = FXCollections.observableArrayList();
		
		ResultSet usersFromDB = SqliteConnection.getEverythingFromTableDB("UserList");
		
		//public User(String nome, String cognome, String indirizzi, String cap, String citta, String telefono, String email,
				//String pw) {
		
		
		/*
		 * TODO
		 * chiot:ho provato a modificare funzione per copiare valori da db ma non ha funzionato,
		 * prova a rivederla tu, intanto mi sono creato dei valori locali
		 */
		
		
		
		/*
		 * try {
		 * 	while(usersFromDB.next()) {
				user.add(new User(usersFromDB.getString("nome"), usersFromDB.getString("cognome"), usersFromDB.getString("indirizzi"),
							usersFromDB.getString("cap"), usersFromDB.getString("citta"),usersFromDB.getString("telefono"), usersFromDB.getString("email"), usersFromDB.getString("pw")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 */
			
		
		/*
		 * TODO
		 * local values to eliminate, should it be..
		 * User u;
		 * LibroCard c;
		 * user.add(new UserForTableView(c.getId(), u.getEmail(), u.getCognome(), u.getNome(), c.getPunti()));
		 */
		
		
		user.add(new UserForTableView("Id1", "email1", "cogn1", "nom1", 1));
		user.add(new UserForTableView("Id2", "email2", "cogn2", "nom2", 2));
		user.add(new UserForTableView("Id3", "email3", "cogn3", "nom3", 3));
		
		return user;
	}
	
	
	//functions for the orders section
	
	private ObservableList<OrdineForTableView> getOrdini() {
		ObservableList<OrdineForTableView> orders = FXCollections.observableArrayList();
		
		/*
		 * TODO
		 * local values to eliminate, should it be..
		 * User u;
		 * Ordine o;
		 * orders.add(new OrdineForTableView(o.getId(), u.getEmail(), o.getData(), o.getStato()));
		 */
		
		
		orders.add(new OrdineForTableView("Ord1", "Acq1", LocalDateTime.now(), "In corso"));
		orders.add(new OrdineForTableView("Ord2", "Acq2", LocalDateTime.now(), "In corso"));
		orders.add(new OrdineForTableView("Ord3", "Acq3", LocalDateTime.now(), "In corso"));
		
		return orders;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		WellcomeLabel.setText("Wellcome responsabile " +respLogged.getNome());
		
		errorLabel.setText("");
		
		//code for the libroCard Section
		
		idLibroCardColumn.setCellValueFactory(new PropertyValueFactory<UserForTableView, String>("idLibroCard"));
		idUserColumn.setCellValueFactory(new PropertyValueFactory<UserForTableView, String>("idUtente"));
		cognomeUserColumn.setCellValueFactory(new PropertyValueFactory<UserForTableView, String>("cognomeId"));
		nomeUserColumn.setCellValueFactory(new PropertyValueFactory<UserForTableView, String>("nomeId"));
		saldoPuntiColumn.setCellValueFactory(new PropertyValueFactory<UserForTableView, Integer>("puntiCard"));
		
		//populates the tableView with dummy items
		//setItems must have an ObservableList as parameter, ObservableList almost like ArrayList	
		
		tableViewCards.setItems(getUser());
		
		//code for the Orders Section
		
		
		codiceOrdineColumn.setCellValueFactory(new PropertyValueFactory<OrdineForTableView, String>("codiceOrdine"));
		idAcquirenteColumn.setCellValueFactory(new PropertyValueFactory<OrdineForTableView, String>("IdAcquirente"));
		dataAcquistoColumn.setCellValueFactory(new PropertyValueFactory<OrdineForTableView, String>("dataAcquisto"));
		statoColumn.setCellValueFactory(new PropertyValueFactory<OrdineForTableView, String>("stato"));
		
		for(OrdineForTableView o: getOrdini()) {
			System.out.println(o.toString()+"\n");
		}
		
		tableViewOrders.setItems(getOrdini());
	}
	
	

}
