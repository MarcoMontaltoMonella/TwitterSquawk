/**
 * 27 Apr 2015, 00:19:53
 */
package it.polito.tdp.mmm.tws.filters;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import twitter4j.Status;

/**
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 */
public class Filter {

	private Set<String> setOfWords;
	private long TTL;
	private boolean removeLinks;
	private boolean removeHashTag;

	public Filter() {
		this.setOfWords = Collections.synchronizedSet(new HashSet<String>());
		this.defaultSettings();
	}
	
	private void defaultSettings(){
		this.TTL = Long.MAX_VALUE;
		this.removeLinks = true;
//		this.audioActive = false;		
	}

	public String removeHTTP(String s) {
		if (s.contains("http")) {
			StringTokenizer st = new StringTokenizer(s, " ");

			StringBuilder sb = new StringBuilder();

			//debug
			//System.out.println("ORIGINAL removeHTTP:\n" + s);

			while (st.hasMoreTokens()) {
				String token = st.nextToken();

				// Removing http urls
				if (!token.contains("http")) {
					sb.append(token+" ");
				}
			}			
			s = sb.toString();
			//debug
			//System.out.println("RESULT removeHTTP:\n "+ s);
		}
		return s;
	}
	
	public static String readableRetweet(String s){
		if(s.contains("RT")){
			StringTokenizer st = new StringTokenizer(s, " ");

			StringBuilder sb = new StringBuilder();

			//debug
			//System.out.println("ORIGINAL readableRT:\n" + s);

			while (st.hasMoreTokens()) {
				String token = st.nextToken();

				if (token.equals("RT")){
					//readable retweet if present
					sb.append("Retweet ");
				}
				else{
					sb.append(token+" ");
				}
			}	
			s = sb.toString();
			//debug
			//System.out.println("RESULT readableRT:\n"+s);
		}
		return s;
	}
	
	public String replaceHashTag(String s){
		if (s.contains("#")) {
			StringTokenizer st = new StringTokenizer(s, " ");

			StringBuilder sb = new StringBuilder();

			//debug
			//System.out.println("ORIGINAL replaceHashTag:\n" + s);

			while (st.hasMoreTokens()) {
				String token = st.nextToken();

				// Replacing hashtags
				if (token.contains("#")) {
					sb.append("Hashtag ");
					sb.append(token.substring(1)+" ");
				} else {
					sb.append(token+ " ");
				}
			}
			s = sb.toString();
			
			//debug
			//System.out.println("RESULT replaceHashTag:\n"+s);
		}	
		return s;
	}
	
	
	public String removeHashTag(String s) {
		//debug
		//System.out.println("ORIGINAL removeHashTag:\n" + s);
		if (s.contains("#")) {
			StringTokenizer st = new StringTokenizer(s);
			StringBuilder sb = new StringBuilder("");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				// Removing hastag
				if (!token.contains("#")) {
					sb.append(token);
				}
			}
			
			//debug
			//System.out.println("RESULT removeHashTag:\n" + sb.toString());
			return sb.toString();
		} 
		return s;
	}

	public String hashtagWord(String s) {
		if (s.contains("#")) {
			return s.substring(1, s.length());
		}
		return s;
	}
	

	public Set<String> getSetOfWords() {
		return setOfWords;
	}

	public void addWord(String s) {
		this.setOfWords.add(s.toLowerCase());
	}

	public void removeWord(String s) {
		this.setOfWords.remove(s);
	}


	public String filterStatus(Status status) {
		String result = "@" + status.getUser().getScreenName() + " - "
				+ status.getText();	

		//In any case:
		return result;
	}
	
	
	public boolean statusMatches(Status status){
		//debug
		//System.out.println("words: "+setOfWords);
		
		//if matches returns TRUE
		for(String s : this.setOfWords){
			//NEW
			StringTokenizer st = new StringTokenizer(status.getText(), " ");
			while(st.hasMoreTokens()){
				if(st.nextToken().toLowerCase().equals(s)){
					return true;
				}
			}
			
			//OLD match if the complete tweet contains the sequence of letter of a filter word
//			if(status.getText().toLowerCase().contains(s)){
//				return true;
//			}
		}
		//else FALSE
		return false;
	}
	
	

	public void setTTL(long l) {
		this.TTL = l;
	}

	public long getTTL() {
		return this.TTL;
	}

	public boolean isEmpty() {
		return this.setOfWords.isEmpty();
	}

	public void clear() {
		this.setOfWords.clear();
		this.defaultSettings();
	}

	public void setRemoveLinks(boolean bool) {
		this.removeLinks = bool;
	}
	public boolean getRemoveLinks() {
		return this.removeLinks;
	}

	public void setRemoveHashTag(boolean bool) {
		this.removeHashTag = bool;
	}

	
	public boolean getRemoveHashTag() {
		return this.removeHashTag;
	}
}
