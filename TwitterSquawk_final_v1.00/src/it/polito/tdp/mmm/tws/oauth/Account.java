/**
 * 25 Apr 2015, 14:25:05
 */
package it.polito.tdp.mmm.tws.oauth;

import java.io.File;

/**
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 */
public class Account {
	
	
	public static void login(String consumerKey, String consumerSecret){
		File file = new File("twitter4j.properties");
		if(!file.exists()){
			GetAccessToken.run(consumerKey, consumerSecret);
		}	
	}
	
	public static boolean logout(){
		File file = new File("twitter4j.properties");
		if(file.exists()){
			file.delete();
			return true;
		}
		return false;
	}
}
