/**
 * 25 Apr 2015, 14:19:31
 */
package it.polito.tdp.mmm.tws.vocal;

import it.polito.tdp.mmm.tws.TSAudioStream;
import it.polito.tdp.mmm.tws.filters.Filter;
import it.polito.tdp.mmm.tws.tweets.TmpTweet;

import java.io.IOException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;

import twitter4j.Status;

/**
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 */
public class Speaker implements Runnable{
	
	private boolean isRunning = false;
	private PriorityBlockingQueue<TmpTweet> buffer;
	private Rate rate;
	private TSAudioStream audioStream;
	private Filter userFilter; 
	
	public Speaker(Rate rate,TSAudioStream audioStream, Filter uf){
		this.buffer = new PriorityBlockingQueue<>(50);
		this.rate = rate;
		this.audioStream = audioStream;
		this.userFilter = uf;
	}

	public static void sayStatus(Status s) {
		try {
			Filter f = new Filter();
			String statusText = f.filterStatus(s);
			Process p = Runtime.getRuntime().exec("say \"" + statusText + "\"");
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void sayString(String s) {
		try {
			Process p = Runtime.getRuntime().exec("say \"" + s + "\"");
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method vocally read a string at a given rate
	 * @param s String to be read
	 * @param rate Speech rate in words per minute
	 */
	public static void sayString(String s, Rate rate) {
		try {
			Process p = null;
			switch (rate) {
			case SLOW:
				p = Runtime.getRuntime().exec("say \"" + s + "\" --rate=160 ");
				break;
			case REGULAR:
				p = Runtime.getRuntime().exec("say \"" + s + "\"");				
				break;

			case FAST:
				p = Runtime.getRuntime().exec("say \"" + s + "\" --rate=280");
				break;
			}
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			//e.printStackTrace();
			System.err.println("Speech interrupted!");
		}
	}

	
	@Override
	public void run() {
		this.isRunning = true;
		
		//while begin
		while(isRunning){ 
			
			
			//check before cleaning
			if(!buffer.isEmpty()){
				// 1.remove old tweets
				
				//debug
				//System.out.println("buffer before Cleaning: "+this.buffer);
				
				this.buffer = buffer
						.parallelStream()
						.filter(s -> !s.isExpired())
						.collect(
								Collectors.toCollection(PriorityBlockingQueue::new));
				
				//debug
				//System.out.println("buffer after Cleaning: "+this.buffer);

				//debug
				//System.out.println("buffer before Read/Remove: "+ this.buffer +" "+this.buffer.size());

				try {

					//set picture and details
					TmpTweet nextTweet = this.buffer.take();

					this.audioStream.setGraphicalDetails(nextTweet.getUserPicture(),nextTweet.getUserDescription());

					//filter then speak
					String toBeSpoken = nextTweet.toString();

					//filter and format

					//saying Retweet when RT is present
					toBeSpoken = Filter.readableRetweet(toBeSpoken);

					if(userFilter.getRemoveLinks()){
						toBeSpoken = userFilter.removeHTTP(toBeSpoken);
					}

					if(userFilter.getRemoveHashTag()){
						//remove hashtags
						toBeSpoken = userFilter.removeHashTag(toBeSpoken);
					} else {
						//readable hashtags
						toBeSpoken = userFilter.replaceHashTag(toBeSpoken);
					}

					//speak at rate x
					Speaker.sayString(toBeSpoken,this.rate);

				} catch (InterruptedException e) {System.err.println("InterruptedException in run");}

				//debug
				//System.out.println("buffer after Read/Remove: "+ this.buffer+" "+this.buffer.size());
			}
		} //while end
	}
	
	public void stopMe(){
		this.isRunning = false;
		
		
		
	}
	
	public void addTweetToBuffer(TmpTweet e){
		this.buffer.add(e);
	}
	
	public int sizeOfBuffer(){
		return this.buffer.size();
	}

}