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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ResponsabileController implements Initializable{
	
	LoginController loginController = new LoginController();
	
	User respLogged=loginController.getUserLogged();

	@FXML private Label WellcomeLabel;
	
	//stuff for the add book part
	@FXML private TextField titolo;
	@FXML private TextField autori;
	@FXML private TextField genere;
	@FXML private TextField casaEditrice;
	@FXML private TextField annoPubblicazione;
	@FXML private TextField prezzo;
	@FXML private TextArea breveDescrizione;
	
	@FXML private Button addToLibraryButton;
	@FXML private Button signOutButton;
	
	@FXML private Label errorLabel;
	
	
	
	public void addToLibraryButtonPushed(ActionEvent event) throws IOException{
		
		//public Libro(String titolo, String autori, String casaeditrice, int annopubblicazione,
			//	String genere, double prezzo, String brevedescrizione, int posizione, int punti)

		if(!emptyTextField()) {
			errorLabel.setText("Tutti i campi devono essere compilati");
			return;
		}

		try {
			if(verifyDateAndPrice()) {
				errorLabel.setText("Right Format");
				
				//TODO
				//rivedi campo posizione
				
				Libro l=new Libro(titolo.getText(), toACapoMode(autori.getText(), '-'), casaEditrice.getText(), Integer.valueOf(annoPubblicazione.getText()),
						genere.getText(), Double.valueOf(prezzo.getText()), breveDescrizione.getText(), 666,(int)Math.round(Double.valueOf(prezzo.getText())));
			
				System.out.println("Stampa libro\n"+l.toString());
				
			}
		}
		catch(NumberFormatException e) {
			errorLabel.setText("Anno di pubblicazione e prezzo devono essere campi numerici");
			return;
		}
	}
	
	
	private boolean emptyTextField() {
		boolean sup=true;
		
		if(titolo.getText() == null || titolo.getText().trim().isEmpty())
				sup=false;
		if(autori.getText() == null || autori.getText().trim().isEmpty())
			sup=false;
		if(genere.getText() == null || genere.getText().trim().isEmpty())
			sup=false;
		if(casaEditrice.getText() == null || casaEditrice.getText().trim().isEmpty())
			sup=false;
		if(annoPubblicazione.getText() == null || annoPubblicazione.getText().trim().isEmpty())
			sup=false;
		if(prezzo.getText() == null || prezzo.getText().trim().isEmpty())
			sup=false;
		if(breveDescrizione.getText() == null || breveDescrizione.getText().trim().isEmpty())
			sup=false;
		
		return sup;
	}
	
	private boolean verifyDateAndPrice() {
		
		return (Integer.valueOf(annoPubblicazione.getText()) instanceof Integer) && (Double.valueOf(prezzo.getText()) instanceof Double); 	
	}
	


	/*
	 * metodo che serve per creare una stringa da:
	 * 
	 * autore 1,autore 2,autore 3 a
	 * 
	 * autore 1
	 * autore 2
	 * autore 3
	 */
	
	private String toACapoMode(String s, char divisor) {
		
		String result="";
		int i;
		int indexIniz=0;
		
		for(i=0;i<s.length();i++) {
			if(s.charAt(i)==divisor) {
				result=result+s.substring(indexIniz,i)+"\n";
				i++;
				while(s.charAt(i)==' ') {
					System.out.println("\nvalore di i :"+i);
					i++;
				}
				indexIniz=i;
			}
		}
		
		return result+s.substring(indexIniz, s.length());
	}
	
	public void SignOutButtonPushed(ActionEvent event) throws IOException
    {
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		WellcomeLabel.setText("Wellcome responsabile " +respLogged.getNome());
		
		errorLabel.setText("");
	}
	
	

}
