/**
 * 8 May 2015, 23:32:16
 */
package it.polito.tdp.mmm.tws.tweets;

import twitter4j.Status;

/**
 * 
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 * 
 * This is a wrapper class of the final class "twitter4j.StatusJSONImpl"
 */
public class TmpTweet implements Comparable<TmpTweet> {

	private Status status;

	// TimeToLive
	private long ttl;

	// Created at
	private long born;

	/**
	 * 
	 * @param status
	 * @param min
	 *            : time to live of the status
	 */
	public TmpTweet(Status status, long sec) {
		this.status = status;
		this.ttl = sec * 1000;
		this.born = status.getCreatedAt().getTime();
	}

	/**
	 * This method checks if the TmpTweet is still valid by comparing the
	 * elapsed time since the creation with the time-to-live set by the user.
	 * 
	 * @return true if the TmpTweet is expired
	 */
	public boolean isExpired() {
		long now = System.currentTimeMillis();
		return (now - this.born) > this.ttl;
	}

	@Override
	public int compareTo(TmpTweet t) {
		//sorting the tweets, keeping the newest as head of the priorityqueue
		return (int) -(this.born - t.born);
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TmpTweet other = (TmpTweet) obj;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if ((Long.compare(status.getId(), other.status.getId()) != 0))
			return false;
		return true;
	}

	public String toString(){
		return "@" + status.getUser().getScreenName() + " - " + status.getText();
	}

	
	public String getUserPicture() {
		return this.status.getUser().getOriginalProfileImageURL();
	}

	
	public String getUserDescription() {
		return this.status.getUser().getDescription();
	}
}
