package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class HomeController implements Initializable{

	@FXML private TableView<Libro> tableView;
	@FXML private TableColumn<Libro, String> titoloColumn;
	@FXML private TableColumn<Libro, String> autoreColumn;
	@FXML private TableColumn<Libro, Integer> prezzoColumn;
	
	
	private ObservableList<Libro> getLibri() {
		ObservableList<Libro> libri = FXCollections.observableArrayList();
		
		
		//public Libro(String titolo, String autori, String casaeditrice, int annopubblicazione,
			//	String isbn, String genere, double prezzo, String brevedescrizione, int posizione, int punti)
		
		Libro p1= new Libro("1984", "George Orwell", "Longman", 1949, "", "Romanzo distopico", 9.99, "", 1, 10);
		Libro p2= new Libro("Guerra e pace", "Lev Tolstoj", "Longman", 1865, "", "Romanzo storico", 19.99, "", 2, 20);
		Libro p3= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		Libro p4= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		Libro p5= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		Libro p6= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		Libro p7= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		Libro p8= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		Libro p9= new Libro("Decameron", "Giovanni Boccaccio", "Longman", 1350, "", "Raccolta di novelle", 14.99, "", 3, 15);
		
		libri.addAll(p1,p2,p3,p4,p5,p6,p7,p8,p9);
		
		return libri;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//set up the columns in the table
		titoloColumn.setCellValueFactory(new PropertyValueFactory<Libro, String>("titolo"));
		autoreColumn.setCellValueFactory(new PropertyValueFactory<Libro, String>("autore"));
		prezzoColumn.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("prezzo"));
		
		//populates the tableView with dummy items
		//setItems must have an ObservableList as parameter, ObservableList almost like ArrayList	
		//ObservableList<Libro> libri =getLibri();
		
		tableView.setItems(getLibri());
	}

}
