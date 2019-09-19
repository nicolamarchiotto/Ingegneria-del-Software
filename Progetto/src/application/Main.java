package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
			Scene scene = new Scene(root,600,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Libreria Digitale");
			Classifica.randomize(); //inizializzazione dei valori
			Classifica.updateClassifica(true);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		LeaderboardUpdateThread prova = new LeaderboardUpdateThread(); //thread to simulate week passing
		
		prova.start(); //starting the thread
			
		launch(args); //starting the application
		
		prova.interrupt(); //terminatin thread
	}
}
