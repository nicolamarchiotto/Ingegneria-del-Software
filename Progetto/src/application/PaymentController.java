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
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class PaymentController implements Initializable{

	
	@FXML private Button goBackButton;
	@FXML private Button signOutButton;
	@FXML private Button confirmButton;
	
	@FXML private TextArea summaryTextArea;
	
	@FXML private RadioButton paypalRadioButton;
	@FXML private RadioButton creditCardRadioButton;
	@FXML private RadioButton bankStampRadioButton;
	
	@FXML private TextField idPaymentTextField;
	@FXML private PasswordField pwPaymentPwField;
	
	private ToggleGroup paymentToggleGroup;
	
	private User userLogged;
	
	LoginController controller=new LoginController();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		this.userLogged=controller.getUserLogged();
		
		paymentToggleGroup=new ToggleGroup();
		this.paypalRadioButton.setToggleGroup(paymentToggleGroup);
		this.creditCardRadioButton.setToggleGroup(paymentToggleGroup);
		this.bankStampRadioButton.setToggleGroup(paymentToggleGroup);
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

}
