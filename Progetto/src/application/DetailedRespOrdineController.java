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

public class DetailedRespOrdineController implements Initializable{

	@FXML private Label titleLabel;
	@FXML private Button goBackButton;
	@FXML private TextArea sumTextArea;
	
	@FXML private TableView<Libro> tableView;
	@FXML private TableColumn<Libro, String> titoloColumn;
	@FXML private TableColumn<Libro, String> autoreColumn;
	@FXML private TableColumn<Libro, Integer> prezzoColumn;
	@FXML private TableColumn<Libro, Integer> numeroCopieColumn;
	
	private OrdineForTableView localOrder;
	
	public void setOrderFromTableView(OrdineForTableView order) {
		localOrder=order;
		
		this.sumTextArea.setText(getSummary());

		tableView.setItems(getLibriFromOrder());
		
		this.titleLabel.setText("Codice Ordine: "+this.localOrder.getCodiceOrdine());
		
		titoloColumn.setCellValueFactory(new PropertyValueFactory<Libro, String>("titolo"));
		autoreColumn.setCellValueFactory(new PropertyValueFactory<Libro, String>("autore"));
		prezzoColumn.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("prezzo"));
		numeroCopieColumn.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("copieVendute"));
		
	}
	
	private ObservableList<Libro> getLibriFromOrder() {
		ObservableList<Libro> books = FXCollections.observableArrayList();
		
		for(Libro l: localOrder.getLibri()) {
			books.add(l);
		}
		
		return books;
	}
	
	public void goBackButtonPushed(ActionEvent event) throws IOException{
		
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("ResponsabileScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
	}

	private String getSummary() {
		String sup;
		
		sup="Costo totale dell'ordine: "+this.localOrder.getPrezzoTot()+"\nData di effettuazione ordine: "+this.localOrder.getDataAcquisto()+""
				+ "\nStato dell'ordine: " +this.localOrder.getStato()+"\nId acquirente: "+this.localOrder.getIdAcquirente();
		
		return sup;
	}
}