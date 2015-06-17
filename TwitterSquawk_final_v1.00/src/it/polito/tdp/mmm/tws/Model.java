package it.polito.tdp.mmm.tws;

import it.polito.tdp.mmm.tws.TSBasicStream.TweetSource;
import it.polito.tdp.mmm.tws.filters.Filter;
import it.polito.tdp.mmm.tws.util.UtilityMethods;
import it.polito.tdp.mmm.tws.vocal.Rate;
import it.polito.tdp.mmm.tws.vocal.Speaker;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

/**
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 */
public class Model {

	//BUFFER
	private PriorityBlockingQueue<String> tweetsBufferTEST;
	
	//FILTERS
	private Filter userFilters;

	//MUTE BASIC STREAM
	private TSBasicStream bs;
	
	//AUDIO STREAM
	private TSAudioStream as;
	
	public Model() {
		
		//DEFAULT EMPTY FILTER
		this.userFilters = new Filter();
		

		this.tweetsBufferTEST = new PriorityBlockingQueue<String>();

		this.tweetsBufferTEST
				.addAll(Arrays
						.asList("day", "wasted", "gun", "shot", "agent", "provocator", "woman", "provocatrice", "heart", "target", "bird", "sky", "M", "bi", "sunlight", "swim", "moonlight", "dance", "murder", "employment", "country", "england", "skyfall", "done"));

	}

	public void run() {
		//debug
		//System.out.println("START: " + tweetsBufferTEST);
		this.readTweets();
		//debug
		//System.out.println("END: " + tweetsBufferTEST);
	}


	public Filter getFilter() {

		return this.userFilters;
	}
	
	public void addFilters(String csv){
		Set<String> set = UtilityMethods.getSetOfStringFromCSV(csv);
		for(String s : set){
			userFilters.addWord(s);
		}
	}

	public boolean filterEmpty() {
		return this.userFilters.isEmpty();
	}
	
	public void cleanFilter(){
		this.userFilters.clear();
	}

	public void readTweets() {
		while (!this.tweetsBufferTEST.isEmpty()) {
			// 1.remove old tweets
			this.tweetsBufferTEST = tweetsBufferTEST
					.parallelStream()
					.filter(s -> s.length() >= 1)
					.collect(
							Collectors.toCollection(PriorityBlockingQueue::new));
			//debug
			//System.out.println(this.tweetsBufferTEST);

			// 2.poll tweets (take is thread-safe)
			try {
				Speaker.sayString(this.tweetsBufferTEST.take().toString(),Rate.FAST);
			} catch (InterruptedException e) { e.printStackTrace();}
		}
	}
	
	public void startMuteThread(TextArea dis, TweetSource ts){
		bs = new TSBasicStream(dis, this.userFilters, ts);
		//bs.run();
		Platform.runLater(bs);
	}
	
	public void stopMuteThread(){
		bs.stopMe();
	}
	
	public void startAudioThread(TextArea dis, ImageView img, TextArea aboutBox, TweetSource ts,Rate rate){
		as = new TSAudioStream(dis, this.userFilters, img, aboutBox, ts, rate);
		Platform.runLater(as);
//		as.run();
	}
	
	public void stopAudioThread(){
		as.stopMe();
	}

	/**
	 * @return true if the twitter4j.properties file exists
	 */
	public boolean userLoggedIn() {
		File file = new File("twitter4j.properties");
		if(file.exists()){
			return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		Model m = new Model();
		m.run();
		
	}

	/**
	 * @return true if there is no Internet connection, false otherwise
	 */
	public boolean noInternetConnection() {
		try {
			return "127.0.0.1".equals(InetAddress.getLocalHost().getHostAddress().toString());
		} catch (UnknownHostException e) {e.printStackTrace();}
		return false;
	}
}
