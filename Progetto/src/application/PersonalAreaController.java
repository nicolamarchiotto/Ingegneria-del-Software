package application;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PersonalAreaController implements Initializable{

	@FXML private TabPane tabPane;
	@FXML private Tab personalDataTab;
	@FXML private Tab orderTab; 
	
	@FXML private Button goBackButton;
	@FXML private Button signOutButton;
	@FXML private Button saveChangesButton;
	
	LoginController controller=new LoginController();
	
	private User userLogged=controller.getUserLogged();
	
	/*
	 * stuff fot the personal data tab
	 */
	@FXML private TextField name;
	@FXML private TextField surname;
	@FXML private TextField address;
	@FXML private TextField city;
	@FXML private TextField cap;
	@FXML private TextField telNumber;
	@FXML private TextField email;
	@FXML private TextField pw;
	@FXML private TextField puntiLibroCard;
	@FXML private TextField dataCreazioneAccount;
	
	
	/*
	 * stuff for the manage the addresses
	 */
	@FXML private TableView<Indirizzo> tableView;
	@FXML private TableColumn<Indirizzo, String> viaColumn;
	@FXML private TableColumn<Indirizzo, String> cittaColumn;
	@FXML private TableColumn<Indirizzo, Integer> capColumn;
	@FXML private Button removeAddressButton;
	
	@FXML private TextField viaAdded;
	@FXML private TextField cittaAdded;
	@FXML private TextField capAdded;
	@FXML private Button addAddressButton;
	
	
	private ObservableList<Indirizzo> indirizziList=FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		name.setText(this.userLogged.getNome());
		surname.setText(this.userLogged.getCognome());
		address.setText(this.userLogged.getIndirizzoResidenza());
		city.setText(this.userLogged.getCittaResidenza());
		cap.setText(this.userLogged.getCapResidenza());
		telNumber.setText(this.userLogged.getTelefono());
		email.setText(this.userLogged.getEmail());
		pw.setText(this.userLogged.getPw());
		puntiLibroCard.setText(String.valueOf(this.userLogged.getLibroCard().getPunti()));
		dataCreazioneAccount.setText(this.userLogged.getLibroCard().getDataEmissione().toString());
		
		//set up the columns in the table
		viaColumn.setCellValueFactory(new PropertyValueFactory<Indirizzo, String>("via"));
		cittaColumn.setCellValueFactory(new PropertyValueFactory<Indirizzo, String>("citta"));
		capColumn.setCellValueFactory(new PropertyValueFactory<Indirizzo, Integer>("cap"));
		
		indirizziList=getIndirizzi();
		tableView.setItems(indirizziList);
	}

	public void SignOutButtonPushed(ActionEvent event) throws IOException
    {
		this.userLogged.setIndirizziDaListaDiOggettiIndirizzi(indirizziList);
		for(String s: this.userLogged.getIndirizziFormattati()) {
			System.out.println(s);
		}
		
		SqliteConnection.savingOnLogOut(userLogged); //saving on logOut
		 
		controller.setUserLogged(null); //at this point no user is logged
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	public void goBackButtonPushed(ActionEvent event) throws IOException
	{
		this.userLogged.setIndirizziDaListaDiOggettiIndirizzi(indirizziList);
		controller.setUserLogged(userLogged);
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("HomeScene.fxml"));
		Parent TableViewParent=loader.load();
		
        Scene tableViewScene = new Scene(TableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
	
	public void saveChangesButtonPushed() {
		
		try {
			if(verifyField()) {
				this.userLogged.setNome(this.name.getText());
				this.userLogged.setCognome(this.surname.getText());
				this.userLogged.setIndirizzoResidenza(this.address.getText());
				this.userLogged.setCittaResidenza(this.city.getText());
				this.userLogged.setCapResidenza(this.cap.getText());
				this.userLogged.setTelefono(this.telNumber.getText());
				this.userLogged.setPw(this.pw.getText());
				
				/*
				 * FIXME GUG
				 * aggiornare anche con qualche metodo appartenente a SqliteConncetion??
				 * Gug: ci sono i vari metodi SqliteConnection.updateUser/Libro/Ordine, tipo così:
				 * this.userLogged.setIndirizziDaListaDiOggettiIndirizzi(indirizziList); //per aggiornare la situa
				 * SqliteConnection.updateUser(this.userLogged);
				 * 
				 * Servirebbe un Save changes anche nella zona di aggiunta/rimozione indirizzi secondo me
				 */
				
				AlertBox.display("Success", "Your data have been upadted");
				//this.tableView.setItems(indirizziList);
				
			}
			else {
				AlertBox.display("Error", "All fields must be filled");
				return;
			}
		}
		catch(NumberFormatException e) {
			AlertBox.display("Error", "Cap and Telephone number\nmust be numeric inputs");
			return;
		}
		
	}

	private boolean verifyField() throws NumberFormatException{
		
		if(this.name.getText() == null || this.name.getText().trim().isEmpty())
				return false;
		if(this.surname.getText() == null || this.surname.getText().trim().isEmpty())
			return false;
		if(this.address.getText() == null || this.address.getText().trim().isEmpty())
			return false;
		if(this.city.getText() == null || this.city.getText().trim().isEmpty())
			return false;
		if(this.cap.getText() == null || this.cap.getText().trim().isEmpty())
			return false;
		if(this.telNumber.getText() == null || this.telNumber.getText().trim().isEmpty())
			return false;
		if(this.pw.getText() == null || this.pw.getText().trim().isEmpty())
			return false;
		if(!((Long.valueOf(this.cap.getText())instanceof Long) && (Long.valueOf(this.telNumber.getText())instanceof Long)))
			return false;
		
		return true;
	}
	
	private ObservableList<Indirizzo> getIndirizzi() {
		
		ObservableList<Indirizzo> supList=FXCollections.observableArrayList();
		
		List<String> listaInStringhe=this.userLogged.getIndirizziFormattati();
		for(String s:listaInStringhe) {
			supList.add(new Indirizzo((s.split(",")[0]), (s.split(",")[1]), (s.split(",")[2])));
		}
		return supList;
	}
	
	public void removeAddressButtonPushed() {
		if(tableView.getSelectionModel().getSelectedItem() == null) {
			AlertBox.display("ERROR", "Non è stato selezionato nessun indirizzo");
			return;
		}else if(tableView.getSelectionModel().getSelectedIndex()==0) {
			AlertBox.display("ERROR", "Non puoi eliminare il tuo indirizzo di residenza");
			return;
		}else {
			Indirizzo indirizzo=tableView.getSelectionModel().getSelectedItem();
			this.indirizziList.remove(indirizzo);
			this.tableView.setItems(this.indirizziList);
		}
	}
	
	public void addAddressButtonPushed() {
		try{
			if(checkAddressInput()) {
				this.indirizziList.add(new Indirizzo(this.viaAdded.getText(), this.cittaAdded.getText(), this.capAdded.getText()));
				this.tableView.setItems(this.indirizziList);
				this.viaAdded.setText("");
				this.cittaAdded.setText("");
				this.capAdded.setText("");
			}
			else {
				AlertBox.display("Error", "All fields must be filled");
				return;
			}
		}
		catch(NumberFormatException e) {
			AlertBox.display("Error", "Cap must be numeric input");
			return;
		}			
	}

	private boolean checkAddressInput() throws NumberFormatException{
		if(this.viaAdded.getText() == null || this.viaAdded.getText().trim().isEmpty())
			return false;
		if(this.cittaAdded.getText() == null || this.cittaAdded.getText().trim().isEmpty())
			return false;
		if(this.capAdded.getText() == null || this.capAdded.getText().trim().isEmpty())
			return false;
		if(!(Long.valueOf(this.capAdded.getText()) instanceof Long))
			return false;
		
		return true;
	}
	
	
}
