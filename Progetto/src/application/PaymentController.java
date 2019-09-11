package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class PaymentController implements Initializable{

	
	@FXML private Button goBackButton;
	@FXML private Button signOutButton;
	@FXML private Button confirmButton;
	
	@FXML private RadioButton paypalRadioButton;
	@FXML private RadioButton creditCardRadioButton;
	@FXML private RadioButton bankStampRadioButton;
	
	@FXML private TextField idPaymentTextField;
	@FXML private PasswordField pwPaymentPwField;
	
	@FXML private Label totalCostLabel;
	@FXML private ComboBox indirizziComboBox;
	@FXML private TextField viaTextField;
	@FXML private TextField cittaTextField;
	@FXML private TextField capTextField;
	@FXML private ArrayList<TextField> vetTextField=new ArrayList<TextField>();
	
	
	private ToggleGroup paymentToggleGroup;
	
	private User userLogged;
	
	LoginController controller=new LoginController();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		this.userLogged=controller.getUserLogged();
		System.out.println(this.userLogged.carrelloToString());
		this.totalCostLabel.setText("Parziale carrello: "+this.userLogged.getTotalCostFromCarrello());
		paymentToggleGroup=new ToggleGroup();
		this.paypalRadioButton.setToggleGroup(paymentToggleGroup);
		this.creditCardRadioButton.setToggleGroup(paymentToggleGroup);
		this.bankStampRadioButton.setToggleGroup(paymentToggleGroup); 
		String sup="---Inserisci un indirizzo---";
		indirizziComboBox.getItems().add(sup);
		indirizziComboBox.setPromptText(sup);
		indirizziComboBox.getItems().addAll(this.userLogged.getIndirizziFormattati());
		this.vetTextField.add(0, viaTextField);
		this.vetTextField.add(1, cittaTextField);
		this.vetTextField.add(2, capTextField);
		
	}
	
	public void SignOutButtonPushed(ActionEvent event) throws IOException
    {
		SqliteConnection.savingOnLogOut(userLogged); //saving on logOut
		controller.setUserLogged(null); //at this point no user is logged
        Parent tableViewParent =  FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();      
    }
	
	public void indirizziComboBoxChanged() {
		String s=this.indirizziComboBox.getValue().toString();
		if(s.compareTo("---Inserisci un indirizzo---")==0) {
			for(TextField t: this.vetTextField) {
				t.setText("");
			}
			return;
		}
		int i, cell=0;
		int indexIniz=0;
		
		for(i=0;i<s.length();i++) {
			if(s.charAt(i)==',') {
				this.vetTextField.get(cell).setText(s.substring(indexIniz,i));
				i++;
				cell++;
				while(s.charAt(i)==' ') {
					i++;
				}
				indexIniz=i;
			}
		}
		this.vetTextField.get(cell).setText(s.substring(indexIniz,i));
	}
	
	
	public void goBackButtonPushed(ActionEvent event) throws IOException{
		
		controller.setUserLogged(userLogged);
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("BasketScene.fxml"));
		Parent TableViewParent=loader.load();
		
        Scene tableViewScene = new Scene(TableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }
	
	public void radioButtonChanged() {
		
		this.idPaymentTextField.setText("");
		this.pwPaymentPwField.setText("");
		
		if(this.paymentToggleGroup.getSelectedToggle().equals(paypalRadioButton)) {
			this.idPaymentTextField.setEditable(true);
			this.idPaymentTextField.setPromptText("Id paypal");
			this.pwPaymentPwField.setEditable(true);
			this.pwPaymentPwField.setPromptText("Password paypal");	
		}
		
		if(this.paymentToggleGroup.getSelectedToggle().equals(creditCardRadioButton)) {
			this.idPaymentTextField.setEditable(true);
			this.idPaymentTextField.setPromptText("Id credit card");
			this.pwPaymentPwField.setEditable(true);
			this.pwPaymentPwField.setPromptText("Password credit card");	
		}
		
		if(this.paymentToggleGroup.getSelectedToggle().equals(bankStampRadioButton)) {
			this.idPaymentTextField.setEditable(false);
			this.idPaymentTextField.setPromptText("");
			this.pwPaymentPwField.setEditable(false);
			this.pwPaymentPwField.setPromptText("");	
		}
	}
	
	public void confirmButtonPushed(ActionEvent event) throws IOException {
		
		if(checkAllFields()) {
			
			String indirizzoSpedizione=this.viaTextField.getText()+", "+this.cittaTextField.getText()+", "+this.capTextField;
			
			Ordine ordLoc=new Ordine(this.userLogged.getEmail(),this.paymentToggleGroup.getSelectedToggle().toString(),
					indirizzoSpedizione, this.userLogged.getCarrello());		
			this.userLogged.getOrdini().add(ordLoc);
			
			SqliteConnection.insertOrder(ordLoc);
			
			this.userLogged.getCarrello().removeAll(this.userLogged.getCarrello());
			
			AlertBox.display("Hurray", "Your order has benn recieved,\nthanks for choosing us!");
			try {
				goToHomePage(event);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			AlertBox.display("Error", "You have forgot some fields");
			return;
		}
	}

	private void goToHomePage(ActionEvent event) throws IOException {
		controller.setUserLogged(userLogged);
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("HomeScene.fxml"));
		Parent TableViewParent=loader.load();
		
        Scene tableViewScene = new Scene(TableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
        
	}

	private boolean checkAllFields() {
		return true;
		
	}

}
