/**
 * 11 May 2015, 16:59:26
 */
package it.polito.tdp.mmm.tws;

import it.polito.tdp.mmm.tws.graphics.ViewController;
import it.polito.tdp.mmm.tws.oauth.Account;

import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 */
public class Launch extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			//Introduction
			showIntro();
			
			//Login
			showLogin();
			

			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"graphics/TSgraphics.fxml"));
			BorderPane root = (BorderPane) loader.load();
			

			ViewController controller = loader.getController();
			
			Model m = new Model();
			controller.setModel(m);
			
			Scene scene = new Scene(root);
			
			
			primaryStage.setScene(scene);
			
			primaryStage.getIcons().add(new Image("file:res/img/TWlogo.png"));
			primaryStage.setTitle("Twitter Squawk");
			
			
			//Operative System name
			@SuppressWarnings("unused")
			String OS = System.getProperty("os.name");
			
			
			
			//SET DEFAULT COMBOBOXES
			controller.setup();
			
			scene.getStylesheets().add(
					getClass().getResource("graphics/stylesheet.css").toExternalForm());
			

			//OPTIONAL
			primaryStage.setResizable(false);

			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shows an introduction window with an image on it.
	 */
	private void showIntro() {
		Alert alert = new Alert(AlertType.NONE);
		
		alert.setGraphic(new ImageView(new Image("file:res/img/coverTS_noB.png")));
		
		alert.setHeaderText("Welcome!");
		    
		ButtonType goButtonType = new ButtonType("Go", ButtonData.OK_DONE);
		ButtonType exitButtonType = new ButtonType("Exit", ButtonData.CANCEL_CLOSE);
		alert.getDialogPane().getButtonTypes().addAll(goButtonType, exitButtonType);
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get()==exitButtonType){
			System.exit(1);
		}
	}
	
	/**
	 * If the twitter4j.properties file does not exist, the application will ask for the cosumerKey and the consumerSecret.
	 */
	private void showLogin(){
		File file = new File("twitter4j.properties");
		
		if(!file.exists()){
			// Create the custom dialog.
			Dialog<Pair<String, String>> dialog = new Dialog<>();
			
			dialog.setTitle("Login");
			dialog.setHeaderText("Log using your consumerKey and consumerSecret");

			// Set the button types.
			ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

			// Create the username and password labels and fields.
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));

			TextField consumerKeyField = new TextField();
			consumerKeyField.setPromptText("ConsumerKey");
			PasswordField consumerSecretField = new PasswordField();
			consumerSecretField.setPromptText("ConsumerSecret");

			grid.add(new Label("ConsumerKey:"), 0, 0);
			grid.add(consumerKeyField, 1, 0);
			grid.add(new Label("ConsumerSecret:"), 0, 1);
			grid.add(consumerSecretField, 1, 1);

			// Enable/Disable login button depending on whether a username was entered.
			Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
			loginButton.setDisable(true);

			// Do some validation (using the Java 8 lambda syntax).
			consumerKeyField.textProperty().addListener((observable, oldValue, newValue) -> {
			    loginButton.setDisable(newValue.trim().isEmpty());
			});

			dialog.getDialogPane().setContent(grid);

			
			// Request focus on the ConsumerKey field by default.
			Platform.runLater(() -> consumerKeyField.requestFocus());

			// Convert the result to a ConsumerKey-ConsumerSecret-pair when the login button is clicked.
			dialog.setResultConverter(dialogButton -> {
			    if (dialogButton == loginButtonType) {
			        return new Pair<>(consumerKeyField.getText(), consumerSecretField.getText());
			    }
			    return null;
			});

			 
			Optional<Pair<String, String>> result = dialog.showAndWait();
			if(!result.isPresent()){
				System.exit(0);
			}
			result.ifPresent(consKeyAndConsSecret -> {
				Account.login(consKeyAndConsSecret.getKey(), consKeyAndConsSecret.getValue());
			});
		}		
	}

	public static void main(String[] args) {
		launch(args);
	}

}
