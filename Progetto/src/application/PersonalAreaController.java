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

	private boolean isDeletingAccount = false;

	@FXML private TabPane tabPane;
	@FXML private Tab personalDataTab;
	@FXML private Tab addressTab;
	@FXML private Tab orderTab; 
	
	@FXML private Button goBackButton;
	@FXML private Button signOutButton;
	@FXML private Button saveChangesButton;
	@FXML private Button deleteAccount;
	
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
	
	/*
	 * stuff for the order section
	 */
	
	@FXML private Button seeDetailesButton;
	
	@FXML private Button searchButton;
	@FXML private TextField searchTextField;
	
	@FXML private TableView<Ordine> tableViewOrder;
	@FXML private TableColumn<Ordine, String> idOrdineColumn;
	@FXML private TableColumn<Ordine, String> dataAcquistoColumn;
	@FXML private TableColumn<Ordine, String> statoColumn;
	@FXML private TableColumn<Ordine, Double> saldoOrdColumn;
	
	
	
	private ObservableList<Indirizzo> indirizziList=FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		

		//set up the columns in the order table
		idOrdineColumn.setCellValueFactory(new PropertyValueFactory<Ordine, String>("idOrdine"));
		dataAcquistoColumn.setCellValueFactory(new PropertyValueFactory<Ordine, String>("data"));
		statoColumn.setCellValueFactory(new PropertyValueFactory<Ordine, String>("stato"));
		saldoOrdColumn.setCellValueFactory(new PropertyValueFactory<Ordine, Double>("totalCost"));
		
		if(this.userLogged.getEmail().equals("#####")) {
			this.tabPane.getTabs().remove(personalDataTab);
			this.tabPane.getTabs().remove(addressTab);
			
			this.searchButton.setVisible(true);
			this.searchTextField.setVisible(true);
		}
		else {
			name.setText(this.userLogged.getNome().trim());
			surname.setText(this.userLogged.getCognome().trim());
			address.setText(this.userLogged.getIndirizzoResidenza().trim());
			city.setText(this.userLogged.getCittaResidenza().trim());
			cap.setText(this.userLogged.getCapResidenza().trim());
			telNumber.setText(this.userLogged.getTelefono().trim());
			email.setText(this.userLogged.getEmail().trim());
			pw.setText(this.userLogged.getPw().trim());
			puntiLibroCard.setText(String.valueOf(this.userLogged.getLibroCard().getPunti()));
			dataCreazioneAccount.setText(this.userLogged.getLibroCard().getDataEmissione().toString());
			
			//hide the search mechanism for the logged users
			this.searchButton.setVisible(false);
			this.searchTextField.setVisible(false);
			
			//set up the columns in the address table
			viaColumn.setCellValueFactory(new PropertyValueFactory<Indirizzo, String>("via"));
			cittaColumn.setCellValueFactory(new PropertyValueFactory<Indirizzo, String>("citta"));
			capColumn.setCellValueFactory(new PropertyValueFactory<Indirizzo, Integer>("cap"));
			
			indirizziList=getIndirizzi();
			tableView.setItems(indirizziList);
			
			tableViewOrder.setItems(FXCollections.observableArrayList(DBOrder.getOrderList(userLogged)));
		}
		
	}

	public void SignOutButtonPushed(ActionEvent event) throws IOException
    {	
		if(!this.userLogged.getEmail().equals("#####"))
			this.userLogged.setIndirizziDaListaDiOggettiIndirizzi(indirizziList);
		
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
		if(!this.userLogged.getEmail().equals("#####"))
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
				if(!verifyNonEmptyField()) return;
				this.userLogged.setNome(this.name.getText().trim());
				this.userLogged.setCognome(this.surname.getText().trim());
				this.userLogged.setIndirizzoResidenza(this.address.getText().trim());
				this.userLogged.setCittaResidenza(this.city.getText().trim());
				this.userLogged.setCapResidenza(this.cap.getText().trim());
				this.userLogged.setTelefono(this.telNumber.getText().trim());
				this.userLogged.setPw(this.pw.getText().trim());
				
				indirizziList=this.getIndirizzi();
				
				this.userLogged.setIndirizziDaListaDiOggettiIndirizzi(indirizziList); //per aggiornare la situa
				tableView.setItems(indirizziList);
				DBUser.updateUser(this.userLogged);
				
				AlertBox.display("Success", "Your data have been updated");
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
		if(!((Long.valueOf(this.cap.getText().trim())instanceof Long) && (Long.valueOf(this.telNumber.getText().trim())instanceof Long)))
			return false;
		return true;
	}
	
	private boolean verifyNonEmptyField() {
		if(this.cap.getText().trim().length() != 5) {//il cap deve essere esattamente di 5 cifre
			AlertBox.display("Error", "Cap must be numeric and composed by five numbers");
			return false;		
		}
		else if(this.telNumber.getText().trim().length() > 11 || this.telNumber.getText().length() < 10) { //il numero telefonico deve essere di 10-11 caratteri
			AlertBox.display("Error", "Insert a valid telephone number");
			return false;		
		}
		else if(this.email.getText().trim().length() < 6) {//non è possibile inserire una email con meno di 6 caratteri
			AlertBox.display("error", "Email must be at least 6 characters long");
			return false;		
		}
		else if(this.pw.getText().trim().length() < 6) {//non è possibile inserire una password con meno di 6 caratteri
			AlertBox.display("error", "Password must be at least 6 characters long");
			return false;
		}else if((Long.valueOf(this.cap.getText().trim())<0) || (Long.valueOf(this.telNumber.getText().trim())<0)){
			AlertBox.display("Error", "Cap and telephone number must be positive");
			return false;
		}
		return true;
	}
	
	private ObservableList<Indirizzo> getIndirizzi() {
		
		ObservableList<Indirizzo> supList=FXCollections.observableArrayList();
		
		List<String> listaInStringhe=this.userLogged.getIndirizziFormattati();
		for(String s:listaInStringhe) {
			supList.add(new Indirizzo((s.split(",")[0].trim()), (s.split(",")[1].trim()), (s.split(",")[2].trim())));
		}
		return supList;
	}
	
	public void removeAddressButtonPushed() {
		if(tableView.getSelectionModel().getSelectedItem() == null) {
			AlertBox.display("ERROR", "No address selected");
			return;
		}else if(tableView.getSelectionModel().getSelectedIndex()==0) {
			AlertBox.display("ERROR", "You can't remove your address of residence");
			return;
		}else {
			Indirizzo indirizzo=tableView.getSelectionModel().getSelectedItem();
			this.indirizziList.remove(indirizzo);
			this.userLogged.setIndirizziDaListaDiOggettiIndirizzi(indirizziList);
			this.tableView.setItems(this.indirizziList);
		}
	}
	
	public void addAddressButtonPushed() {
		try{
			if(checkAddressInput()) {
				if(this.capAdded.getText().trim().length() != 5) {
					AlertBox.display("Error", "CAP must be 5 number long");
					return;
				}
				if(Long.valueOf(this.capAdded.getText().trim())<0){
					AlertBox.display("Error", "Cap and telephone number must be positive");
					return;
				}
					
				this.indirizziList.add(new Indirizzo(this.viaAdded.getText().trim(), this.cittaAdded.getText().trim(), this.capAdded.getText().trim()));
				this.userLogged.setIndirizziDaListaDiOggettiIndirizzi(indirizziList);
				this.tableView.setItems(this.indirizziList); 
				this.viaAdded.setText("");
				this.cittaAdded.setText("");
				this.capAdded.setText("");
				this.controller.setUserLogged(userLogged);
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
		if(!(Long.valueOf(this.capAdded.getText().trim()) instanceof Long))
			return false;
		
		return true;
	}
	
	/*
	 * Your orders section
	 */
	
	public void seeDetailesButtonPushed(ActionEvent event) throws IOException
	{
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("DetailedOrdineScene.fxml"));
		Parent TableViewParent=loader.load();
		
		DetailedOrdineController controller=loader.getController();
		
		//controllo se è stato selezionato qualcosa
		if(tableViewOrder.getSelectionModel().getSelectedItem() == null) {
			AlertBox.display("ERROR", "No order was selected");
			return;
		}
		else{
			controller.setOrderFromTableView(tableViewOrder.getSelectionModel().getSelectedItem());
			controller.setBackPage("PersonalAreaScene.fxml");
			controller.setLato("User");
		}
		
        Scene tableViewScene = new Scene(TableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
	
	public void searchButtonPushed(ActionEvent event) throws IOException{
			
		if(this.searchTextField.getText() == null || this.searchTextField.getText().trim().isEmpty()) {
			AlertBox.display("Error", "Insert the order code");
			return;
		}
		
		Ordine ordineTrovato=DBOrder.getOrderByID(this.searchTextField.getText());
		if(ordineTrovato==null) {
			AlertBox.display("Error", "No match found");
			return;
		}
		else if(!ordineTrovato.getUserId().equals("#####"))
			AlertBox.display("Error", "This order is from a registered user, mind your own business");
		else {
			this.tableViewOrder.getItems().removeAll(this.tableViewOrder.getItems());
			this.tableViewOrder.getItems().add(ordineTrovato);
			
		}
	}
	
	public void deleteAccountButtonPushed(ActionEvent event) throws IOException{
		AlertBox.confirmAccountDelete(this);

		if(this.isDeletingAccount) {
			DBUser.deleteUser(this.userLogged);
			this.controller.setUserLogged(null);
			this.SignOutButtonPushed(event);
		}
	}
	
	public void deletingAccount() {
		this.isDeletingAccount = true;
	}
}
