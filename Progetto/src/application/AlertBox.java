package application;


import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AlertBox {

	public static void display(String title, String message) {
		Stage window = new Stage();
	
	    //Block events to other windows
	    window.initModality(Modality.APPLICATION_MODAL);
	    window.setTitle(title);
	    window.setMinWidth(250);
	    window.setMinHeight(120);
	    
	    Label label = new Label();
	    label.setText(message);
	    Button okButton = new Button("Ok");
	    okButton.setMinWidth(80);
	    okButton.setOnAction(e -> window.close());
	
	    VBox layout = new VBox(10);
	    layout.getChildren().addAll(label, okButton);
	    layout.setAlignment(Pos.CENTER);
	    layout.setSpacing(20);
	
	    //Display window and wait for it to be closed before returning
	    Scene scene = new Scene(layout);
	    window.setScene(scene);
	    window.showAndWait();
	}
}