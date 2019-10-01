package application;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

	public static void display(String title, String message) {
		Stage window = new Stage();
	
	    //Block events to other windows
	    window.initModality(Modality.APPLICATION_MODAL);
	    window.setTitle(title);
	    window.setMinWidth(250);
	    window.setMinHeight(120);
	    
	    Label label = new Label();
	    label.setTextAlignment(TextAlignment.CENTER);
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
	
	public static void confirmAccountDelete(PersonalAreaController personalArea) {
		Stage window = new Stage();
	
	    //Block events to other windows
	    window.initModality(Modality.APPLICATION_MODAL);
	    window.setTitle("Are you sure about that?");
	    window.setMinWidth(250);
	    window.setMinHeight(120);
	    
	    Label label = new Label();
	    label.setTextAlignment(TextAlignment.CENTER);
	    label.setText("Are you sure you want to\ndelete your account?");
	    Button yesButton = new Button("Yes");
	    Button noButton = new Button("No");
	    yesButton.setMinWidth(80);
	    noButton.setMinWidth(80);
	    yesButton.setLayoutX(301);

	    yesButton.setOnAction(e -> {
	    	personalArea.deletingAccount();
	    	window.close();
	    });
	    noButton.setMinWidth(80);
	    noButton.setOnAction(e -> window.close());
	    
	    BorderPane buttons = new BorderPane();
	    buttons.setRight(noButton);
	    buttons.setLeft(yesButton);
	    buttons.setPadding(new Insets(10));
	
	    VBox layout = new VBox(10);
	    layout.setAlignment(Pos.CENTER);
	    layout.getChildren().addAll(label, buttons);
	    layout.setSpacing(20);
	
	    //Display window and wait for it to be closed before returning
	    Scene scene = new Scene(layout);
	    window.setScene(scene);
	    window.showAndWait();
	}
}