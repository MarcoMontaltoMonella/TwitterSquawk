/**
 * 14 May 2015, 16:42:45
 */
package it.polito.tdp.mmm.tws.graphics;

import it.polito.tdp.mmm.tws.filters.Filter;
import it.polito.tdp.mmm.tws.vocal.Rate;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;



/**
 * 
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 * 
 * static support class
 */
public class ViewSupport {

	/**
	 * 
	 * @param rb : radioButton
	 * @param nodes
	 */
	public static void audioSwitched(RadioButton rb, Node ... nodes) {
		if(rb.isSelected()){
			for(Node n : nodes){
				n.setDisable(false);
			}
		} else {
			for(Node n : nodes){
				n.setDisable(true);
				if(n instanceof RadioButton){
					((RadioButton) n).setSelected(false);
				}
			}
		}
	}

	/**
	 * @param comboTTL
	 * @param comboRate
	 */
	public static void setCombos(ComboBox<String> comboTTL,
			ComboBox<Rate> comboRate, ComboBox<String> comboStreamSource) {
		
		comboTTL.setItems(FXCollections.observableArrayList("min", "sec"));
		comboTTL.getSelectionModel().select("sec");
		
		comboRate.setItems(FXCollections.observableArrayList(Rate.SLOW,Rate.REGULAR,Rate.FAST));
		comboRate.getSelectionModel().select(1);
		
		//REMOVE GLOBAL FROM HERE TO 'DEACTIVATE' IT
		comboStreamSource.setItems(FXCollections.observableArrayList("followings", "global"));
		comboStreamSource.getSelectionModel().select("followings");
		
		
		
		
	}

	/**
	 * disable n given nodes
	 */
	public static void disableNodes(Node ... nodes) {
		for(Node n : nodes){
			n.setDisable(true);
		}
	}

	/**
	 * Application general informations
	 */
	public static void showAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About");
		alert.setResizable(true);
		alert.getDialogPane().setPrefSize(500, 400);
		alert.setGraphic(new ImageView(new Image("file:res/img/TWlogoSunglasses.png")));
				
		alert.setHeaderText("Twitter Squawk");
		alert.setContentText("Copyright 2015 Marco Montalto Monella \n"+
				"Licensed under the Apache License, Version 2.0 (the \"License\");\n"+
				"you may not use this file except in compliance with the License.\n"+
				"You may obtain a copy of the License at\n\n"+

    			"\thttp://www.apache.org/licenses/LICENSE-2.0\n\n"+

				"Unless required by applicable law or agreed to in writing, software\n"+
				"distributed under the License is distributed on an \"AS IS\" BASIS,\n"+
				"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n"+
				"See the License for the specific language governing permissions and \n"+
				"limitations under the License.");
		alert.showAndWait();
	}

	/**
	 * Opens the given url using the user default browser
	 * @url
	 */
	public static void openBrowser(String url) {
		if(Desktop.isDesktopSupported()){
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		
	}

	/**
	 * @param nodes to be hidden
	 */
	public static void hideAll(Node ... nodes) {
		for(Node n : nodes){
			n.setVisible(false);
		}
		
	}

	/**
	 * @param emailAddr
	 */
	public static void sendEmailTo(String emailAddr) {
		Desktop desktop;
		String userName = System.getProperty("user.name");
		
    	if(Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
    		try {
				URI mailTo = new URI("mailto:"+ emailAddr +"?subject=TwitterSquawk%20-%20"+ userName + "\'s%20feedback");
				desktop.mail(mailTo);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	} else {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setHeaderText("Your computer does not support this functionality.");
    		alert.setContentText("I am sorry for the inconvenience.\n\nThis is my email: " + emailAddr);
    		alert.showAndWait();
    	}
		
	}


	/**
	 * @param filtersListView 
	 * @param setOfWords
	 * @param model 
	 * @param mouseEvent
	 * @return
	 */
	public static ArrayList<Label> toArrayListOfLabelsWithDoubleClickListener(Filter filter, ListView<Label> filtersListView) {
		Set<String> setOfWords = filter.getSetOfWords();
		ArrayList<Label> list = new ArrayList<Label>();
		for(String s : setOfWords){
			Label lab = new Label(s);
			
			lab.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					
					if(event.getButton().equals(MouseButton.PRIMARY)){
						
						if(event.getClickCount() == 2){	
							//debug
//							System.out.println("Double clicked " + s);
							filter.removeWord(s);
							ArrayList<Label> updatedList = ViewSupport.toArrayListOfLabelsWithDoubleClickListener(filter, filtersListView);
							filtersListView.setItems(FXCollections.observableArrayList(updatedList));
							if(!filtersListView.getItems().isEmpty()){
								//After deletion, highlight first item if present.
								filtersListView.getSelectionModel().select(0);
							}
						}
					}
				}
			});
			list.add(lab);
		}
		Collections.sort(list, labelsByName);
		return list;
	}
	
	private static Comparator<Label> labelsByName = (Label l1, Label l2) ->  l1.getText().compareTo(l2.getText());

	
	public static void showAll(Node ... nodes) {
		for(Node n : nodes){
			n.setVisible(true);
		}
	}

	/**
	 * @param header
	 * @param message
	 */
	public static void showPanelMessage(AlertType at, String header, String message) {
		Alert alert = new Alert(at);
		alert.setTitle(at.name());		
		alert.setHeaderText(header);
		alert.setContentText(message);
		alert.showAndWait();
	}
	

	/**
	 * @return the inserted pin
	 */
	public static String showPinInput() {
		TextInputDialog dialog = new TextInputDialog("Insert the PIN here");
		dialog.setTitle("Security check");
		dialog.setHeaderText("Insert here the PIN:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    System.out.println("Your name: " + result.get());
		    return result.get();
		} else {
			return null;
		}
	}
}
