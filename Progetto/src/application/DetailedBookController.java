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
	@FXML private Label posizioneLabel;
	//con label devi andare a capo ogni 30 caratteri	
	@FXML private Label breveDescrizioneLabel;
	@FXML private TextArea breveDescrizioneTextArea;
	
	@FXML private Button goBackButton;
	@FXML private Button purchaseButton;
	
	
	
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
	
	
	public void goBackButtonPushed(ActionEvent event) throws IOException{
		
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("HomeScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }
  
	
	public void initialize(URL arg0, ResourceBundle arg1) {
	}

}
