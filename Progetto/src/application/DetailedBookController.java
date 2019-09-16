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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	@FXML private Label breveDescrizioneLabel;
	@FXML private Label disponibilita;
	
	@FXML private Button goBackButton;
	@FXML private Button signOutButton;
	@FXML private Button basketButton;
	@FXML private Button purchaseButton;
	
	private String backPage="";
	private String idOrdine="";
	private String idAcquirente;
	
	
	
	
	
	LoginController controller=new LoginController();
	private User userLogged;
	
	private Libro selectedLibro;
	private boolean bookAlreadyInBasket;
	
	//functions as new initializer
	
	public void setBookData(Libro libro) {
		selectedLibro=libro;
		
		userLogged=controller.getUserLogged();
		
		mainTitleLabel.setText(libro.getTitolo());
		titoloLabel.setText(libro.getTitolo());
		autoreLabel.setText(libro.getAutore());
		casaEditriceLabel.setText(libro.getCasaEditrice());
		annoPubblicazioneLabel.setText(Integer.toString(libro.getAnnoPubblicazione()));
		isbnLabel.setText(libro.getIsbn());
		genereLabel.setText(libro.getGenere());
		prezzoLabel.setText(String.valueOf(libro.getPrezzo()));
		breveDescrizioneLabel.setText(libro.getBreveDescrizionePortataACapoOgniTotCaratteri(45));
		
		if(userLogged.getCarrello().contains(this.selectedLibro)) {
			this.purchaseButton.setText("Remove book from your basket");
			bookAlreadyInBasket=true;
		}
		else {
			this.purchaseButton.setText("Add book to your basket");	 
			bookAlreadyInBasket=false;
		}
		
		if(libro.getDisponibilita() == 0)
			this.disponibilita.setVisible(true);
	}
	
	public void setBackPage(String name) {
		this.backPage=name;
		
		if(name.compareTo("DetailedRespOrdineScene.fxml")==0) {
			this.purchaseButton.setVisible(false);
			this.basketButton.setVisible(false);
		}
		else {
			this.purchaseButton.setVisible(true);
			this.basketButton.setVisible(true);
		}
	}
	
	public void SignOutButtonPushed(ActionEvent event) throws IOException
    {
		if(this.backPage.compareTo("DetailedRespOrdineScene.fxml")!=0) SqliteConnection.savingOnLogOut(userLogged); //saving on logOut if i'm not an admin
		
		controller.setUserLogged(null); //at this point no user is logged
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      	
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
		
		String messagge="";
		
		if(this.bookAlreadyInBasket) {
			messagge="\nwas removed from your basket";
			this.bookAlreadyInBasket=false;
			this.purchaseButton.setText("Add to your basket");	
		}
		else {
			messagge="\nwas added to your basket";
			this.purchaseButton.setText("Remove from your basket");
			this.bookAlreadyInBasket=true;
		}
			
		AlertBox.display("Hurray ", this.selectedLibro.getTitolo()+messagge);
		
		if(this.bookAlreadyInBasket==true) {
			this.userLogged.addLibroToCarrello(this.selectedLibro);
		}
		else
			this.userLogged.removeLibroFromCarrello(this.selectedLibro);
		
		/*
		 * prova carrello to string
		 */
		System.out.println("\nstampa carrello user\n" +this.userLogged.carrelloToString());
	}
  
	public void BasketButtonPushed(ActionEvent event) throws IOException
    {
		controller.setUserLogged(userLogged);
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("BasketScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
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
