/**
 * 21 May 2015, 12:20:40
 */
package it.polito.tdp.mmm.tws;

import it.polito.tdp.mmm.tws.filters.Filter;
import javafx.scene.control.TextArea;
import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

/**
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 */
public class TSUserStreamListenerBasic implements UserStreamListener {
	
	protected TextArea display;
	protected Filter userFilters;
	
	public TSUserStreamListenerBasic(TextArea display, Filter userFilters) {
		this.display = display;
		this.userFilters = userFilters;
		//debug
		//System.out.println("TS LISTENER IN");
	}

	@Override
	public void onStatus(Status status) {

		//FIRST FILTER IF STATUS MATCHES FILTERS
		if(userFilters.statusMatches(status)){
			//debug
			//System.out.println("onStatus @"+ status.getUser().getScreenName() + " - "+ status.getText());


			display.appendText("@"
					+ status.getUser().getScreenName() + "\n"
					+ status.getText() + "\n-----------------------------\n");


		} else {
			//debug
			//System.out.println("onStatus -non matched @"+ status.getUser().getScreenName() + " - "+ status.getText());

		}
	}

	@Override
	public void onDeletionNotice(
			StatusDeletionNotice statusDeletionNotice) {
		//System.out.println("Got a status deletion notice id:"	+ statusDeletionNotice.getStatusId());
	}

	@Override
	public void onDeletionNotice(long directMessageId, long userId) {
		//System.out.println("Got a direct message deletion notice id:"	+ directMessageId);
	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		//debug
		//System.out.println("Got a track limitation notice:"+ numberOfLimitedStatuses);
	}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {
		//debug
		//System.out.println("Got scrub_geo event userId:" + userId+ " upToStatusId:" + upToStatusId);
	}

	@Override
	public void onStallWarning(StallWarning warning) {
		//debug
		//System.out.println("Got stall warning:" + warning);
	}

	@Override
	public void onFriendList(long[] friendIds) {
		//debug
		/*
		System.out.print("onFriendList");
		for (long friendId : friendIds) {
			System.out.print(" " + friendId);
		}
		System.out.println();
		*/
	}

	@Override
	public void onFavorite(User source, User target,
			Status favoritedStatus) {
		//debug
		/*
		System.out.println("onFavorite source:@"
				+ source.getScreenName() + " target:@"
				+ target.getScreenName() + " @"
				+ favoritedStatus.getUser().getScreenName() + " - "
				+ favoritedStatus.getText());
		*/
	}

	@Override
	public void onUnfavorite(User source, User target,
			Status unfavoritedStatus) {
		//debug
		/*
		System.out.println("onUnFavorite source:@"
				+ source.getScreenName() + " target:@"
				+ target.getScreenName() + " @"
				+ unfavoritedStatus.getUser().getScreenName() + " - "
				+ unfavoritedStatus.getText());
		*/
	}

	@Override
	public void onFollow(User source, User followedUser) {
		//debug
		//System.out.println("onFollow source:@" + source.getScreenName()+ " target:@" + followedUser.getScreenName());
	}

	@Override
	public void onUnfollow(User source, User followedUser) {
		//debug
		//System.out.println("onFollow source:@" + source.getScreenName()+ " target:@" + followedUser.getScreenName());
	}

	@Override
	public void onDirectMessage(DirectMessage directMessage) {
		//debug
		//System.out.println("onDirectMessage text:"+ directMessage.getText());
	}

	@Override
	public void onUserListMemberAddition(User addedMember,
			User listOwner, UserList list) {
		//debug
		/*
		System.out
				.println("onUserListMemberAddition added member:@"
						+ addedMember.getScreenName() + " listOwner:@"
						+ listOwner.getScreenName() + " list:"
						+ list.getName());
		*/
	}

	@Override
	public void onUserListMemberDeletion(User deletedMember,
			User listOwner, UserList list) {
		//debug
		/*
		System.out
				.println("onUserListMemberDeleted deleted member:@"
						+ deletedMember.getScreenName()
						+ " listOwner:@" + listOwner.getScreenName()
						+ " list:" + list.getName());
		*/
	}

	@Override
	public void onUserListSubscription(User subscriber, User listOwner,
			UserList list) {
		//debug
		/*
		System.out
				.println("onUserListSubscribed subscriber:@"
						+ subscriber.getScreenName() + " listOwner:@"
						+ listOwner.getScreenName() + " list:"
						+ list.getName());
		*/
	}

	@Override
	public void onUserListUnsubscription(User subscriber,
			User listOwner, UserList list) {
		//debug
		/*
		System.out
				.println("onUserListUnsubscribed subscriber:@"
						+ subscriber.getScreenName() + " listOwner:@"
						+ listOwner.getScreenName() + " list:"
						+ list.getName());
		*/
	}

	@Override
	public void onUserListCreation(User listOwner, UserList list) {
		//debug
		/*
		System.out
				.println("onUserListCreated  listOwner:@"
						+ listOwner.getScreenName() + " list:"
						+ list.getName());
		*/
	}

	@Override
	public void onUserListUpdate(User listOwner, UserList list) {
		//debug
		/*
		System.out
				.println("onUserListUpdated  listOwner:@"
						+ listOwner.getScreenName() + " list:"
						+ list.getName());
		*/
	}

	@Override
	public void onUserListDeletion(User listOwner, UserList list) {
		//debug
		/*
		System.out
				.println("onUserListDestroyed  listOwner:@"
						+ listOwner.getScreenName() + " list:"
						+ list.getName());
		*/
	}

	@Override
	public void onUserProfileUpdate(User updatedUser) {
		//debug
		/*
		System.out.println("onUserProfileUpdated user:@"
				+ updatedUser.getScreenName());
		*/
	}

	@Override
	public void onUserDeletion(long deletedUser) {
		//debug
		//System.out.println("onUserDeletion user:@" + deletedUser);
	}

	@Override
	public void onUserSuspension(long suspendedUser) {
		//debug
		//System.out.println("onUserSuspension user:@" + suspendedUser);
	}

	@Override
	public void onBlock(User source, User blockedUser) {
		//debug
		//System.out.println("onBlock source:@" + source.getScreenName()+ " target:@" + blockedUser.getScreenName());
	}

	@Override
	public void onUnblock(User source, User unblockedUser) {
		//debug
		/*
		System.out.println("onUnblock source:@"
				+ source.getScreenName() + " target:@"
				+ unblockedUser.getScreenName());
		*/
	}

	@Override
	public void onException(Exception ex) {
		ex.printStackTrace();
		System.err.println("onException:" + ex.getMessage());
	}
}
