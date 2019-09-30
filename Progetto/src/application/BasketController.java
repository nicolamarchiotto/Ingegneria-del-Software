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
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class BasketController implements Initializable{


	@FXML private AnchorPane anchorPane=new AnchorPane();
	@FXML private GridPane gridPane=new GridPane();
	@FXML private ScrollPane sp=new ScrollPane();
	
	@FXML private Button signOutButton;
	@FXML private Button goBackButton;
	@FXML private Button goToPaymentButton;
	
	int numbersOfGridRows=0;
	
	private User userLogged;
	private ArrayList<Libro> carrelloUser;
	private ArrayList<Label> labelArray=new ArrayList<Label>();
	private ArrayList<Button> buttonArray=new ArrayList<Button>();
	private ArrayList<ComboBox<String>> comboboxArray=new ArrayList<ComboBox<String>>();
	private ArrayList<Label> priceArray=new ArrayList<Label>();
	
	
	LoginController controller=new LoginController();
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		userLogged=controller.getUserLogged();
		carrelloUser=userLogged.getCarrello();
		numbersOfGridRows=carrelloUser.size();
		this.sp.setContent(anchorPane);
		creaGridPane();
		
		for(int i=0;i<this.carrelloUser.size();i++) {
			System.out.println("Titolo: "+this.carrelloUser.get(i).getTitolo()+" LabelId: "+this.labelArray.get(i).getId()+" ComboBox: "+this.comboboxArray.get(i).getId());
		}
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
	
	public void goBackButtonPushed(ActionEvent event) throws IOException{
		
		controller.setUserLogged(userLogged);
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("HomeScene.fxml"));
		Parent TableViewParent=loader.load();
		
        Scene tableViewScene = new Scene(TableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
    }

	private void creaGridPane() {
		
		GridPane gridPaneLocal=new GridPane();
		Label carrelloVuotoLabel=new Label("Your basket is empty, go back to Home");
		carrelloVuotoLabel.setStyle("-fx-font-weight: bold");
	
		carrelloVuotoLabel.setLayoutX(50);
		carrelloVuotoLabel.setLayoutY(50);
		
		if(this.carrelloUser.isEmpty()) {

			this.anchorPane.getChildren().add(carrelloVuotoLabel);
			return;
		}
		else {
			this.anchorPane.getChildren().remove(carrelloVuotoLabel);
		}
		
		ColumnConstraints column = new ColumnConstraints(300);
		gridPaneLocal.getColumnConstraints().add(column);
	    ColumnConstraints column2 = new ColumnConstraints(80);
	    gridPaneLocal.getColumnConstraints().add(column2);
	    gridPaneLocal.getColumnConstraints().add(column2);
	    gridPaneLocal.getColumnConstraints().add(column2);
	    RowConstraints row=new RowConstraints(50);
	    gridPaneLocal.setLayoutX(20);
	    gridPaneLocal.setLayoutX(20);
	    
	     
		Label firstRowLabel=new Label("Title");
		firstRowLabel.setStyle("-fx-font-weight: bold");
	    gridPaneLocal.add(firstRowLabel, 0, 0);
	    
	    firstRowLabel=new Label("Q.ty");
	    firstRowLabel.setStyle("-fx-font-weight: bold");
	    gridPaneLocal.add(firstRowLabel, 1, 0);
	    
	    firstRowLabel=new Label("Price");
	    firstRowLabel.setStyle("-fx-font-weight: bold");	    
	    gridPaneLocal.add(firstRowLabel, 2, 0);
	    
	    gridPaneLocal.getRowConstraints().add(row);
		
		
		for(int i=0;i<numbersOfGridRows;i++) {		
			gridPaneLocal.getRowConstraints().add(row);
			Label label=new Label(carrelloUser.get(i).getTitolo().trim());
			Label price=new Label(String.valueOf(carrelloUser.get(i).getPrezzo()));
			label.setId("label"+String.valueOf(i));
			ComboBox<String> combobox=new ComboBox<String>();
			combobox.getItems().addAll("1","2","3");
			combobox.setEditable(true);
			combobox.setMinSize(60, 25);
			combobox.setMaxSize(60, 25);
			combobox.setId("combobox"+String.valueOf(i));
			combobox.setValue("1");
			Button button=new Button();
			button.setMinHeight(25);
			button.setText("Remove");
			button.setId("button"+String.valueOf(i));
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
			this.priceArray.add(price);
			
			gridPaneLocal.add(labelArray.get(i), 0, i+1);
			gridPaneLocal.add(priceArray.get(i), 1, i+1);
			gridPaneLocal.add(comboboxArray.get(i), 2, i+1);
			gridPaneLocal.add(buttonArray.get(i), 3, i+1);
			
			}
		this.gridPane=gridPaneLocal;
		this.anchorPane.setMinSize(0, 50*(this.carrelloUser.size()+1));
		
		this.anchorPane.getChildren().add(this.gridPane);
	}


	private void removeLibroFromCarrello(String id) throws IOException {
		int number=Character.getNumericValue(id.charAt(id.length()-1));
		this.carrelloUser.remove(number);
		this.comboboxArray.remove(number);
		this.labelArray.remove(number);
		this.numbersOfGridRows--;
		this.anchorPane.getChildren().remove(gridPane);
		creaGridPane();
		
	}
	
	public void goToPaymentButtonPushed(ActionEvent event) throws IOException{
		
		if(this.carrelloUser.isEmpty()) {
			AlertBox.display("Error", "Your basket is empty,\nsorry you can gift us money :)");
			return;
		}
		
		for(int i=0;i<this.carrelloUser.size();i++) {
			Integer intero;
			try {
				intero=Integer.valueOf(this.comboboxArray.get(i).getValue());
			}
			catch(NumberFormatException e){
				AlertBox.display("Error", "The number of the copies must be numeric");
				return;
			}
			
			this.carrelloUser.get(i).setCopieVenduteSingoloOrdine(intero);
		}
		
		controller.setUserLogged(userLogged);
		
		FXMLLoader loader=new FXMLLoader();
		loader.setLocation(getClass().getResource("PaymentScene.fxml"));
		Parent TableViewParent=loader.load();
		
        Scene tableViewScene = new Scene(TableViewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(tableViewScene);
        window.show();
		
	}

}
