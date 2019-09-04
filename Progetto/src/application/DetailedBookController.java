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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class DetailedBookController implements Initializable{

	@FXML private Label mainTitleLabel;
	@FXML private Label titoloLabel;
	@FXML private Label autoreLabel;
	@FXML private Label casaEditriceLabel;
	@FXML private Label annoPubblicazioneLabel;
	@FXML private Label isbnLabel;
	@FXML private Label genereLabel;
	@FXML private Label prezzoLabel;
	//con label devi andare a capo ogni 20 caratteri
	@FXML private Label breveDescrizioneLabel;
	
	@FXML private Button goBackButton;
	@FXML private Button purchaseButton;
	
	private String backPage="";
	private String idOrdine="";
	private String idAcquirente;
	
	@FXML private ListView listview;
	
	
	//to add in case of purchase
	private Libro selectedLibro;
	
	public void setBookData(Libro libro) {
		selectedLibro=libro;
		
		mainTitleLabel.setText(libro.getTitolo());
		titoloLabel.setText(libro.getTitolo());
		autoreLabel.setText(libro.getAutore());
		casaEditriceLabel.setText(libro.getCasaEditrice());
		annoPubblicazioneLabel.setText(Integer.toString(libro.getAnnoPublicazione()));
		isbnLabel.setText(libro.getIsbn());
		genereLabel.setText(libro.getGenere());
		prezzoLabel.setText(String.valueOf(libro.getPrezzo()));
		breveDescrizioneLabel.setText(libro.getBreveDescrizione());
	 
		 
	}
	
	public void setBackPage(String name) {
		this.backPage=name;
		
		if(name.compareTo("DetailedRespOrdineScene.fxml")==0) {
			this.purchaseButton.setVisible(false);
		}
		else {
			this.purchaseButton.setVisible(true);
		}
	}
	
	
	public void goBackButtonPushed(ActionEvent event) throws IOException{
		
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource(this.backPage));
		Parent TableViewParent=loader.load();
		
        Scene tableViewScene = new Scene(TableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        //codice in caso da responsabile si volesse ritornare a alla DetailedRespOrdineScene
        if(this.backPage.compareTo("DetailedRespOrdineScene.fxml")==0) {
        	
        	DetailedRespOrdineController controller=loader.getController();
        	/*
        	 *metodo per ottenere ordine da DB 
        	 */
        	//OrdineForTableView order=new OrdineForTableView(getUserFromDB(this.idAcquirente),getOrdineFromDB(this.idOrdine));
        	//controller.setOrderFromTableView(order);
        	
        	this.idAcquirente="";
        	this.idOrdine="";
        }
        
        window.setScene(tableViewScene);
        window.show();
    }
  
	
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

	public void setIdOrdineEUser(String codiceOrdine, String idAcquirente) {
		this.idAcquirente=codiceOrdine;
		this.idAcquirente=idAcquirente;
		
	}

}
