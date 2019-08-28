package application;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class HomeController implements Initializable{

	@FXML private TableView<Libro> tableView;
	@FXML private TableColumn<Libro, String> titoloColumn;
	@FXML private TableColumn<Libro, String> autoreColumn;
	@FXML private TableColumn<Libro, Integer> prezzoColumn;
	
	@FXML private Button SignOutButton;
	@FXML private Button PersonalAreaButton;
	@FXML private Label WellcomeLabel;
	@FXML private Button SeeDetailesButton;
	
	
	private User logged;
	
	
	private ObservableList<Libro> getLibri() {
		ObservableList<Libro> libri = FXCollections.observableArrayList();
		
		ResultSet booksFromDB = SqliteConnection.getEverythingFromTableDB("BookList");
		
		try {
			while(booksFromDB.next()) {
				libri.add(new Libro(booksFromDB.getString("titolo"), booksFromDB.getString("autore"), booksFromDB.getString("casaEditrice"), booksFromDB.getInt("annoPubblicazione"), booksFromDB.getString("isbn"), booksFromDB.getString("genere"), booksFromDB.getDouble("prezzo"), booksFromDB.getString("breveDescrizione"), booksFromDB.getInt("puntiCarta")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return libri;
	}
	
	public void SignOutButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	//change scene to detailedBookView
	
	
	public void SeeDetailesButtonPushed(ActionEvent event) throws IOException
    {
		
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("DetailedBookScene.fxml"));
		Parent TableViewParent=loader.load();
		
		Scene tableViewScene = new Scene(TableViewParent);  
		
		DetailedBookController controller=loader.getController();
		controller.setBookData(tableView.getSelectionModel().getSelectedItem());
		
		
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1){		
		
		//da modificare in seguito
		LoginController lc = new LoginController(); 
		logged = lc.getUserLogged();
		
		//System.out.println(logged.toString());
		
		WellcomeLabel.setText("Welcome " +logged.getNome()+", good Shopping");
		
		
		
		//set up the columns in the table
		titoloColumn.setCellValueFactory(new PropertyValueFactory<Libro, String>("titolo"));
		autoreColumn.setCellValueFactory(new PropertyValueFactory<Libro, String>("autore"));
		prezzoColumn.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("prezzo"));
		
		//populates the tableView with dummy items
		//setItems must have an ObservableList as parameter, ObservableList almost like ArrayList	
		
		tableView.setItems(getLibri());
		
	}

}
