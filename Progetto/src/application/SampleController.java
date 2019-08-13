package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SampleController implements Initializable{
	
	@FXML private Label provaLabel;
	@FXML private Button firstButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		provaLabel.setText("ciao");
	}
		
}
