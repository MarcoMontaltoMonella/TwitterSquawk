/**
 * 11 May 2015, 16:54:44
 */
package it.polito.tdp.mmm.tws.graphics;

import it.polito.tdp.mmm.tws.Model;
import it.polito.tdp.mmm.tws.TSBasicStream.TweetSource;
import it.polito.tdp.mmm.tws.oauth.Account;
import it.polito.tdp.mmm.tws.vocal.Rate;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 */
public class ViewController {

	private Model model;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;
	
	
	//TEXT AREAS
	@FXML
    private TextArea runningStreamDisplay;
	
	@FXML
    private TextArea txtAreaRunningInfo;
	
	
	
	@FXML
    private ListView<Label> filtersListView;
	
	@FXML
    private MenuItem btnShowHideTips;
	
	@FXML
	private Button btnStop;

	@FXML
	private ImageView runningPictureDisplay;
	
	
	//COMBOS
	@FXML
	private ComboBox<String> comboTTL;
	
	@FXML
    private ComboBox<Rate> comboRate;
	
	@FXML
    private ComboBox<String> comboStreamSource;

	
	
	@FXML
	private RadioButton radioDoNotReadLinks;
	
	@FXML
    private RadioButton radioDoNotReadHashtag;

	//PANES
	@FXML
	private VBox leftPaneFilters;

	@FXML
	private VBox centerPaneFilters;

	@FXML
	private AnchorPane rightPaneFilters;
	
	@FXML
    private AnchorPane centerPaneRunning;

	
	
	@FXML
	private TextField txtFTTL;
	
	@FXML
	private Circle userStatusCircle;
	
	@FXML
	private Label userStatusLabel;

	@FXML
	private RadioButton radioAudio;
	
	Node[] filterPage = new Node[3];
	Node[] runningPage = new Node[3];

	@FXML
    void doOpenTradingEconomicsWebsite(ActionEvent event) {
		ViewSupport.openBrowser("http://www.tradingeconomics.com/calendar");
    }

    @FXML
    void doSendFeedbackEmail(ActionEvent event) {
    	//email address of the feedback receiver
    	String emailAddr = "marco.montalto.93@alice.it";
    	ViewSupport.sendEmailTo(emailAddr);
    }
    
    @FXML
    void doAddFilters(ActionEvent event) {
    	//Please enter your filtering words in a comma separated format
    	//(e.g. -> hello,world,engineering,twitter)
    	TextInputDialog tid = new TextInputDialog();
    	tid.setTitle("Add filters");
    	tid.setHeaderText("Please enter your filtering words in a comma separated format\n\n(e.g. \"hello world, engineering, twitter\")");
    	Optional<String> input = tid.showAndWait();
    	if(input.isPresent() && !input.get().equals("")){
    		//debug
//    		System.out.println("PRESENT");
    		
    		//add filters to the model
    		model.addFilters(input.get());
    		//add filters to the filtersListView from the Set contained in the model
    		ArrayList<Label> filtersLabels = ViewSupport.toArrayListOfLabelsWithDoubleClickListener(model.getFilter(), filtersListView);
    		this.filtersListView.setItems(FXCollections.observableArrayList(filtersLabels));
    		
    	} else {
    		//debug
//    		System.out.println("VOID");
    	}
    }
    
    @FXML
    void doClearFilters(ActionEvent event) {
    	this.filtersListView.setItems(FXCollections.observableArrayList());
		this.model.cleanFilter();
    }
	
	
	@FXML
	void radioAudioSwitched(ActionEvent event) {
		ViewSupport.audioSwitched(radioAudio, radioDoNotReadLinks,radioDoNotReadHashtag,txtFTTL,comboTTL,comboRate);
	}

	@FXML
	private Label tip1;

	@FXML
	private Label tip2;

	@FXML
	void doShowHideTips(ActionEvent event) {
		if (tip1.isVisible() && tip2.isVisible()) {
			if(leftPaneFilters.isVisible()){
				tip1.setVisible(false);				
			}
			tip2.setVisible(false);
			
		} else {
			if(leftPaneFilters.isVisible()){
				tip1.setVisible(true);
			}
			tip2.setVisible(true);
		}
		if(btnShowHideTips.getText().equals("Show tips *")){
			this.btnShowHideTips.setText("Hide tips *");			
		} else {
			this.btnShowHideTips.setText("Show tips *");
		}
	}
	
	

	@FXML
	void doStart(ActionEvent event) {
		
		//debug
		//System.out.println("CONTROLLER_doStart IN");
		
		//empty filters check
		if(this.model.filterEmpty()){
			ViewSupport.showPanelMessage(AlertType.WARNING,"Please insert at least one filtering word","Otherwise no tweets will be displayed");
			return;
		}
		
		if(model.noInternetConnection()){
			this.userStatusLabel.setText("NO INTERNET CONNECTION!");
			this.userStatusCircle.setFill(Paint.valueOf("#ff0000"));
			return;
		}
		
		
		
		// hide filter_view, then show running_view
		ViewSupport.hideAll(filterPage);

//		equivalent to...
//		for(Node n : filterPage){
//			n.setVisible(false);
//		}
		
		this.tip1.setVisible(false);
		this.tip2.setVisible(false);
		ViewSupport.showAll(runningPage);
		
		//CLEAN RUNNING PANE
		this.runningStreamDisplay.clear();
		
		//TWEETS SOURCE
		TweetSource ts;
		if(comboStreamSource.getValue().equals("global")){
			//TODO WARNING!!! POSSIBLE CRASH DUE TO TOO MANY TWEETS
			ts = TweetSource.GLOBAL;
		} else {
			ts = TweetSource.FOLLOWINGS;
		}
		//clear description TextArea
		this.txtAreaRunningInfo.clear();
		
		
		if(radioAudio.isSelected()){ //AUDIO CASE
			//debug
			//System.out.println("START! [audio]");
			
			//Time to Live
			long ttl;
			if(this.txtFTTL.getText().equals("")){
				//default value: 24h
				ttl= 60 * 60 * 24;
			}else {
				ttl = Long.parseLong(this.txtFTTL.getText());
			
				if(comboTTL.getValue().compareToIgnoreCase("min")==0){
					ttl *=60;
				}
			}
			model.getFilter().setTTL(ttl);
			
			//Speech rate
			Rate rate = this.comboRate.getValue();
			
			
			/*
			 * AUDIO OPTIONS
			 */
			
			//url
			if(radioDoNotReadLinks.isSelected()){
				model.getFilter().setRemoveLinks(true);
			} else {
				model.getFilter().setRemoveLinks(false);
			}
			
			//hastag
			if(radioDoNotReadHashtag.isSelected()){
				model.getFilter().setRemoveHashTag(true);
			} else {
				model.getFilter().setRemoveHashTag(false);
			}
			
			
			model.startAudioThread(runningStreamDisplay, runningPictureDisplay, txtAreaRunningInfo,ts,rate);
			
			//Setting an empty picture at the beginning
			this.runningPictureDisplay.setImage(null);
			
			//this is a single pane in the running view
			ViewSupport.showAll(this.txtAreaRunningInfo);
			
		} else { //MUTE CASE
			//debug
			//System.out.println("START! [mute]");
			
			model.startMuteThread(this.runningStreamDisplay,ts);
			
			//Default mute picture
			this.runningPictureDisplay.setImage(new Image("file:res/img/TSmuteBird.png"));
			
			//this is a single pane in the running view
			ViewSupport.hideAll(this.txtAreaRunningInfo);
		}
		
		//debug
		//System.out.println("CONTROLLER_doStart OUT!");
	}
	
	@FXML
	void doStop(ActionEvent event) {
		ViewSupport.hideAll(runningPage);
		ViewSupport.showAll(filterPage);
		
		if(radioAudio.isSelected()){
			model.stopAudioThread();
		} else {
			model.stopMuteThread();
		}
		//optional CLEAR filterListView and model
		/*
		 * this.filtersListView.setItems(FXCollections.observableArrayList());
		 * this.model.cleanFilter();
		 * 
		 */
	}

	@FXML
	void doAboutTwitterSquawk(ActionEvent event) {
		ViewSupport.showAbout();
	}

	/**
	 * Opens a tutorial video via the user's browser.
	 */
	@FXML
	void doWatchTutorial(ActionEvent event) {
		ViewSupport.openBrowser("https://youtu.be/-zN892KUz1s");
	}
	
	@FXML
    void doLogout(ActionEvent event) {
		//debug
		//System.out.println("LOGOUT");
		boolean loggedOutSuccess = Account.logout();
		if(loggedOutSuccess){
			ViewSupport.showPanelMessage(AlertType.INFORMATION, "You have been successfully logged out", "Hope you enjoyed TwitterSquawk!");
			System.exit(0);
		} else {
			ViewSupport.showPanelMessage(AlertType.ERROR, "You were not logged in!", "");
			System.exit(-1);
		}
    }

	@FXML
	void initialize() {
		 assert tip1 != null : "fx:id=\"tip1\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert radioDoNotReadHashtag != null : "fx:id=\"radioDoNotReadHashtag\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert userStatusCircle != null : "fx:id=\"userStatusCircle\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert centerPaneRunning != null : "fx:id=\"centerPaneRunning\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert radioAudio != null : "fx:id=\"radioAudio\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert tip2 != null : "fx:id=\"tip2\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert btnShowHideTips != null : "fx:id=\"btnShowHideTips\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert comboStreamSource != null : "fx:id=\"comboStreamSource\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert runningStreamDisplay != null : "fx:id=\"runningStreamDisplay\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert txtAreaRunningInfo != null : "fx:id=\"txtAreaRunningInfo\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert txtFTTL != null : "fx:id=\"txtFTTL\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert rightPaneFilters != null : "fx:id=\"rightPaneFilters\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert radioDoNotReadLinks != null : "fx:id=\"radioDoNotReadLinks\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert comboRate != null : "fx:id=\"comboRate\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert leftPaneFilters != null : "fx:id=\"leftPaneFilters\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert btnStop != null : "fx:id=\"btnStop\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert filtersListView != null : "fx:id=\"filtersListView\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert runningPictureDisplay != null : "fx:id=\"runningPictureDisplay\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert centerPaneFilters != null : "fx:id=\"centerPaneFilters\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert userStatusLabel != null : "fx:id=\"userStatusLabel\" was not injected: check your FXML file 'TSgraphics.fxml'.";
		 assert comboTTL != null : "fx:id=\"comboTTL\" was not injected: check your FXML file 'TSgraphics.fxml'.";

	}

	public void setModel(Model m) {
		this.model = m;
	}

	public void setup() {
		ViewSupport.setCombos(comboTTL,comboRate,comboStreamSource);
		ViewSupport.disableNodes(radioDoNotReadLinks,radioDoNotReadHashtag,txtFTTL,comboTTL,comboRate);
		
		//filterPage nodes
		filterPage[0] = leftPaneFilters;
		filterPage[1] = centerPaneFilters;
		filterPage[2] = rightPaneFilters;
		
		//runningPage nodes
		runningPage[0] = runningStreamDisplay; 
		runningPage[1] = centerPaneRunning;
		runningPage[2] = btnStop;
		
		ViewSupport.hideAll(runningPage);
		
		//MENU ITEM SHORTCUT
		this.btnShowHideTips.setAccelerator(KeyCombination.keyCombination("H"));
		
		//LOCK USER INPUT TO DIGITS ONLY AND MAX_LENGTH
		final int MAX_LENGTH = 2;
		
		txtFTTL.textProperty().addListener(new ChangeListener<String>() {
		    @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        if (!newValue.matches("\\d*") || txtFTTL.getText().length() > MAX_LENGTH) {
		        	txtFTTL.setText(oldValue);
		        	//BELL
//		        	java.awt.Toolkit.getDefaultToolkit().beep();
		        }
		    }
		});
		
		//USER STATUS
		if(model.userLoggedIn()){
			this.userStatusLabel.setText("Logged in");
			this.userStatusCircle.setFill(Paint.valueOf("#00ff00"));
		} else {
			this.userStatusLabel.setText("Logged off");
			this.userStatusCircle.setFill(Paint.valueOf("#ff0000"));
		}
		
	}
	
	@FXML
    void quitTwitterSquawk(ActionEvent event) {
		System.exit(0);
    }

}
