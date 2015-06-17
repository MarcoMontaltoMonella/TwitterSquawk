/**
 * 22 May 2015, 23:30:31
 */
package it.polito.tdp.mmm.tws;

import it.polito.tdp.mmm.tws.filters.Filter;
import it.polito.tdp.mmm.tws.vocal.Rate;
import it.polito.tdp.mmm.tws.vocal.Speaker;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import twitter4j.UserStreamListener;

/**
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 */
public class TSAudioStream extends TSBasicStream {

	private ImageView imageShown;
	private TextArea aboutBox;
	private Speaker speaker;
	private Thread speakerThread;
	
	
	/**
	 * @param display
	 * @param userFilters
	 */
	public TSAudioStream(TextArea display, Filter userFilters, ImageView imageShown, TextArea aboutBox, TweetSource tweetsource, Rate rate) {
		super(display, userFilters, tweetsource);
		this.imageShown = imageShown;
		this.aboutBox = aboutBox;
				
		this.speaker = new Speaker(rate, this,this.userFilters);
	}
	
	
	private void startStream(){
		//listen to tweets and posts them on the display if they match
		final UserStreamListener listener = new TSUserStreamListenerWithAudio(display, userFilters,this.speaker);

		twitterStream.addListener(listener);
	}
	
	private void startSpeaker(){
//		this.speaker.run();
//		Platform.runLater(this.speaker);
		speakerThread = new Thread(this.speaker);
		speakerThread.setDaemon(true);
		speakerThread.start();
	}

	@Override
	protected void startStreamFollowings() {
		
		this.startStream();
		
		//RUN user()
		twitterStream.user();
		
		//SPEAKER
		this.startSpeaker();
	}
	
	@Override
	protected void startStreamGlobal() {
		
		this.startStream();

		//RUN sample()
		twitterStream.sample();
		
		//SPEAKER
		this.startSpeaker();
	}
	
	public void stopMe(){
		super.stopMe();
		
		//Stop the speaker too.
		this.speaker.stopMe();
		
	}
	
	//GRAPHICS
	public void setGraphicalDetails(String pictureURL, String descriptionString){
		//clear previous text
		this.aboutBox.clear();
		
		this.imageShown.setImage(new Image(pictureURL));
		this.aboutBox.setText(descriptionString);
	}
	
	
	
	
}
