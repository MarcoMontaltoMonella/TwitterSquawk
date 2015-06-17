/**
 * 22 May 2015, 19:32:55
 */
package it.polito.tdp.mmm.tws;

import it.polito.tdp.mmm.tws.filters.Filter;
import it.polito.tdp.mmm.tws.tweets.TmpTweet;
import it.polito.tdp.mmm.tws.vocal.Speaker;
import javafx.scene.control.TextArea;
import twitter4j.Status;

/**
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 */
public class TSUserStreamListenerWithAudio extends TSUserStreamListenerBasic {

	
	private Speaker speaker;
	
	/**
	 * @param display
	 * @param userFilters
	 * @param tweetsBuffer
	 * @param imageShown
	 * @param aboutBox
	 */
	public TSUserStreamListenerWithAudio(TextArea display,
			Filter userFilters, Speaker spe) {
		super(display, userFilters);
		this.speaker = spe;
	}
	
	@Override
	public void onStatus(Status status) {
		//FIRST FILTER IF STATUS MATCHES FILTERS
		if(userFilters.statusMatches(status)){
			
			//debug
			/*
			System.out.println("onStatus @"
					+ status.getUser().getScreenName() + " - "
					+ status.getText());
			*/

			
			

			display.appendText("@"
					+ status.getUser().getScreenName() + "\n"
					+ status.getText() + "\n-------------\n");

			
			
			
			//Adding a TmpTweet and the TTL set by the user
			TmpTweet tt = new TmpTweet(status,userFilters.getTTL());
			
			//debug
			//System.out.println("Added status. Buffer size: "+this.speaker.sizeOfBuffer());
			
			this.speaker.addTweetToBuffer(tt);
		}
	}
}
