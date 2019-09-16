package application;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class DetailedOrdineController implements Initializable{

	@FXML private Label titleLabel;
	@FXML private Button goBackButton;
	@FXML private Button seeDetailesButton;
	@FXML private Button signOutButton;
	@FXML private TextArea sumTextArea;
	
	
	@FXML private TableView<Libro> tableView;
	@FXML private TableColumn<Libro, String> titoloColumn;
	@FXML private TableColumn<Libro, String> autoreColumn;
	@FXML private TableColumn<Libro, Integer> prezzoColumn;
	@FXML private TableColumn<Libro, Integer> numeroCopieColumn;
	
	private String backpage="";
	private String lato="";
	
	private Ordine localOrder;
	
	public void setOrderFromTableView(Ordine order) {
		localOrder=order;
		
		this.sumTextArea.setText(getSummary());

		tableView.setItems(getLibriFromOrder());
		
		this.titleLabel.setText("Codice Ordine: "+this.localOrder.getIdOrdine());
		
		titoloColumn.setCellValueFactory(new PropertyValueFactory<Libro, String>("titolo"));
		autoreColumn.setCellValueFactory(new PropertyValueFactory<Libro, String>("autore"));
		prezzoColumn.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("prezzo"));
		numeroCopieColumn.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("copieVenduteNelSingoloOrdine"));
		
	}
	
	private ObservableList<Libro> getLibriFromOrder() {
		ObservableList<Libro> books = FXCollections.observableArrayList();
		
		for(Libro l: localOrder.getLibri()) {
			books.add(l);
		}
		return books;
	}
	
	public void setBackPage(String backpage) {
		this.backpage=backpage;
	}
	
	public void goBackButtonPushed(ActionEvent event) throws IOException{
		
		if(this.lato.compareTo("User")==0)
			this.backpage="PersonalAreaScene.fxml";
		else
			this.backpage="ResponsabileScene.fxml";
		
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource(this.backpage));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
	
	public void SeeDetailesButtonPushed(ActionEvent event) throws IOException
    {
		
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
		
		controller.setBackPage("DetailedOrdineScene.fxml");
		controller.setLato(this.lato);
		
		//metodo che mi serve per ripopolare la tabella in caso di return to backPage
		controller.setIdOrdineForRespView(this.localOrder.getIdOrdine());
		
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	public void SignOutButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	
	/*
	 *initialize è sempre eseguito come primo metodo,
	 *il settaggio delle colonne deve essere fatto dopo aver ottenuto l'oggetto Order
	 *dalla pagina responsabile, vedi funzione setOrderFromTableView
	 */
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
	}

	private String getSummary() {
		String sup;
		
		sup="Costo totale dell'ordine: "+this.localOrder.getTotalCost()+"\nData di effettuazione ordine: "+this.localOrder.getData()+""
				+ "\nStato dell'ordine: " +this.localOrder.getStato()+"\nId acquirente: "+this.localOrder.getUserId()+"\nIndirizzo di spedizione: "+this.localOrder.getIndirizzoSpedizione();
		
		return sup;
	}

	public void setLato(String lato) {
		this.lato=lato;		
	}
}
