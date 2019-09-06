package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class BasketController implements Initializable{


	@FXML private AnchorPane anchorPane=new AnchorPane();
	@FXML private GridPane gridPane=new GridPane();
	
	int numbersOfGridRows=0;
	
	private User userLogged;
	private ArrayList<Libro> carrelloUser;
	private ArrayList<Label> labelArray=new ArrayList<Label>();
	private ArrayList<Button> buttonArray=new ArrayList<Button>();
	private ArrayList<ComboBox<String>> comboboxArray=new ArrayList<ComboBox<String>>();
	
	LoginController controller=new LoginController();
	
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		userLogged=controller.getUserLogged();
		carrelloUser=userLogged.getCarrello();
		numbersOfGridRows=carrelloUser.size();
		creaGridPane();
	}

	private void creaGridPane() {
		
		GridPane gridPaneLocal=new GridPane();
		
		ColumnConstraints column = new ColumnConstraints(300);
		gridPaneLocal.getColumnConstraints().add(column);
	    ColumnConstraints column2 = new ColumnConstraints(80);
	    gridPaneLocal.getColumnConstraints().add(column2);
	    gridPaneLocal.getColumnConstraints().add(column2);
	     
		
	    gridPaneLocal.add(new Label("Titolo"), 0, 0);
	    gridPaneLocal.add(new Label("Q.tà"), 1, 0);
		
		for(int i=1;i<=numbersOfGridRows;i++) {			
			Label label=new Label(carrelloUser.get(i-1).getTitolo());
			ComboBox<String> combobox=new ComboBox<String>();
			combobox.getItems().addAll("1","2","3");
			combobox.setEditable(true);
			combobox.setPrefSize(30, 20);
			combobox.setId("combobox"+String.valueOf(i-1));
			combobox.setValue("1");
			Button button=new Button();
			button.setText("Remove");
			button.setId("button"+String.valueOf(i-1));
			button.setOnAction(e -> {
				try {
					removeLibroFromCarrello(button.getId());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
			this.labelArray.add(label);
			this.comboboxArray.add(combobox);
			this.buttonArray.add(button);
			
			gridPaneLocal.add(labelArray.get(i-1), 0, i);
			gridPaneLocal.add(comboboxArray.get(i-1), 1, i);
			gridPaneLocal.add(buttonArray.get(i-1), 2, i);
			
			}
		this.gridPane=gridPaneLocal;
		this.gridPane.setVisible(true);
		
		this.anchorPane.getChildren().add(this.gridPane);
	}


	private void removeLibroFromCarrello(String id) throws IOException {
		int number=Character.getNumericValue(id.charAt(id.length()-1));
		System.out.println("Valore del Bottone: "+number);
		this.carrelloUser.remove(number);
		this.numbersOfGridRows--;
		System.out.println(this.carrelloUser.toString());
		this.anchorPane.getChildren().remove(gridPane);
		creaGridPane();
		
	}

}
