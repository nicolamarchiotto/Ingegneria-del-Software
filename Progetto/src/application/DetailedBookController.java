package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
	@FXML private ComboBox<String> combobox;
	
	private String backPage="";
	private String idOrdine="";
	private String idAcquirente;
	
	
	
	LoginController controller=new LoginController();
	private User userLogged;
	
	
	
	//to add in case of purchase
	private Libro selectedLibro;
	
	//functions as new initializer
	
	public void setBookData(Libro libro) {
		selectedLibro=libro;
		
		userLogged=controller.getUserLogged();
		
		mainTitleLabel.setText(libro.getTitolo());
		titoloLabel.setText(libro.getTitolo());
		autoreLabel.setText(libro.getAutore());
		casaEditriceLabel.setText(libro.getCasaEditrice());
		annoPubblicazioneLabel.setText(Integer.toString(libro.getAnnoPublicazione()));
		isbnLabel.setText(libro.getIsbn());
		genereLabel.setText(libro.getGenere());
		prezzoLabel.setText(String.valueOf(libro.getPrezzo()));
		breveDescrizioneLabel.setText(libro.getBreveDescrizione());
		

		combobox.getItems().addAll("1","2","3","4");
	 
		/*
		 * Inizializzazione di this.selectedLibro.copieVenduteNelSingoloOrdine a copie già inserite nel carrello
		 * problemi con l'aggiunta nel campio copie in quanto si fanno riferimenti globali alla struttura Libro da
		 * quanto ho riscontrato dopo varie smadonne
		 */
		
		for(Libro l:userLogged.getCarrello()) {
			if(this.selectedLibro.getIsbn().compareTo(l.getIsbn())==0) {
				this.selectedLibro.setCopieVenduteSingoloOrdine(l.getCopieVenduteNelSingoloOrdine());
			}
		}
		
		 
	}
	
	public void setBackPage(String name) {
		this.backPage=name;
		
		if(name.compareTo("DetailedRespOrdineScene.fxml")==0) {
			this.combobox.setVisible(false);
			this.purchaseButton.setVisible(false);
		}
		else {
			this.combobox.setVisible(true);
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
        
        controller.setUserLogged(userLogged);
        
        window.setScene(tableViewScene);
        window.show();
    }
	
	/*
	 * Verifico se il numero di copie che richiede l'utente è intero
	 * e aggiungo al libro le copie dell'ordine ma in LOCALE
	 */
	
	public void purchaseButtonPushed() {
		String c=combobox.getSelectionModel().getSelectedItem();
		Integer sup;
		try{
			sup=new Integer(c);
		}
		catch(NumberFormatException e) {
			AlertBox.display("Warning", "You must put a int numeric entry");
			return;
			
		}
		AlertBox.display("Hurray", c+" copies of\n"+ this.selectedLibro.getTitolo()+"\nwere added to your basket");
		
		this.selectedLibro.aggiungiCopieAlSingoloOrdine(sup);
		this.userLogged.addLibroToCarrello(this.selectedLibro);
		
		/*
		 * prova carrrello to string
		 */
		System.out.println("\nstampa carrello user\n" +this.userLogged.carrelloToString());
	}
  
	
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

	public void setIdOrdineEUser(String codiceOrdine, String idAcquirente) {
		this.idAcquirente=codiceOrdine;
		this.idAcquirente=idAcquirente;
		
	}

}
