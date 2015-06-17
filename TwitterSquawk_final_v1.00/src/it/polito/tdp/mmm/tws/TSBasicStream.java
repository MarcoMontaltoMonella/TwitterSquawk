/**
 * 21 May 2015, 12:12:25
 */
package it.polito.tdp.mmm.tws;

import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.UserStreamListener;
import it.polito.tdp.mmm.tws.filters.Filter;
import javafx.scene.control.TextArea;

/**
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 */
public class TSBasicStream implements Runnable {
	public enum TweetSource {
		FOLLOWINGS,
		GLOBAL
	}

	protected TextArea display;
	protected Filter userFilters;
	protected TwitterStream twitterStream;
	protected TweetSource tweetsource;
	
	public TSBasicStream(TextArea display, Filter userFilters, TweetSource ts) {
		this.display = display;
		this.userFilters = userFilters;
		this.tweetsource = ts;
	}



	@Override
	public void run() {
		//debug
		//System.out.println("TSBASIC IN");
		
		
		twitterStream = new TwitterStreamFactory().getInstance();
		
		switch (tweetsource) {
		case FOLLOWINGS:
			startStreamFollowings();			
			break;
		case GLOBAL:
			startStreamGlobal();
			break;
		default:
			break;
		}
	}
	
	protected void startStreamFollowings(){
		//listen to tweets and posts them on the display if they match
		final UserStreamListener listener = new TSUserStreamListenerBasic(display, userFilters);
		
		twitterStream.addListener(listener);
		
		//RUN
		twitterStream.user();
	}
	
	protected void startStreamGlobal(){
		//listen to tweets and posts them on the display if they match
		final UserStreamListener listener = new TSUserStreamListenerBasic(display, userFilters);

		twitterStream.addListener(listener);

		//RUN
		twitterStream.sample();


	}
	
	public void stopMe(){
		//debug
		System.out.println("\n---------\nSHUTDOWN Requested!\n");
		twitterStream.shutdown();
		//debug
		//display.appendText("\n---------\nSHUTDOWN!\n");
		System.out.println("\n---------\nSHUTDOWN Completed!\n");
	}

}
