/**
 * 21 Apr 2015, 17:06:53
 */
package it.polito.tdp.mmm.tws.oauth;

import it.polito.tdp.mmm.tws.graphics.ViewSupport;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javafx.scene.control.Alert.AlertType;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


/**
 * @author Yusuke Yamamoto - yusuke at mac.com 
 * (edited by Marco Montalto Monella)
 * @since Twitter4J 2.1.7
 */
public class GetAccessToken {
    /**
     * Usage: java  twitter4j.examples.oauth.GetAccessToken [consumer key] [consumer secret]
     *
     */
    public static void run(String consumerKey, String consumerSecret) {
        File file = new File("twitter4j.properties");
        Properties prop = new Properties();
        InputStream is = null;
        OutputStream os = null;
        try {
            if (file.exists()) {
                is = new FileInputStream(file);
                prop.load(is);
            }
            
            prop.setProperty("oauth.consumerKey", consumerKey);
            prop.setProperty("oauth.consumerSecret", consumerSecret);
            os = new FileOutputStream("twitter4j.properties");
            prop.store(os, "twitter4j.properties");
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(-1);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ignore) {
                }
            }
        }
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            RequestToken requestToken = twitter.getOAuthRequestToken();
            
            //debug
            /*
            System.out.println("Got request token.");
            System.out.println("Request token: " + requestToken.getToken());
            System.out.println("Request token secret: " + requestToken.getTokenSecret());
            */
            AccessToken accessToken = null;

            //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (null == accessToken) {
            	//debug
            	/*
                System.out.println("Open the following URL and grant access to your account:");
                System.out.println(requestToken.getAuthorizationURL());
                */
                try {
                    Desktop.getDesktop().browse(new URI(requestToken.getAuthorizationURL()));
                } catch (UnsupportedOperationException ignore) {
                } catch (IOException ignore) {
                } catch (URISyntaxException e) {
                    throw new AssertionError(e);
                }
                
                //pin in console
                /*
                System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                String pin = br.readLine();
                */
                
                //pin in separate window
                String pin = ViewSupport.showPinInput();
                
                try {
                    if (pin.length() > 0 && pin != null) {
                        accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                    } else {
                    	if(pin != null){
                    		accessToken = twitter.getOAuthAccessToken(requestToken);
                    	} else {
                    		System.exit(-1);
                    	}
                    }
                } catch (TwitterException te) {
                    if (401 == te.getStatusCode()) {
                        System.err.println("Unable to get the access token.");
                    } else {
                        te.printStackTrace();
                    }
                }
            }
            //debug
            /*
            System.out.println("Got access token.");
            System.out.println("Access token: " + accessToken.getToken());
            System.out.println("Access token secret: " + accessToken.getTokenSecret());
			*/
            try {
                prop.setProperty("oauth.accessToken", accessToken.getToken());
                prop.setProperty("oauth.accessTokenSecret", accessToken.getTokenSecret());
                os = new FileOutputStream(file);
                prop.store(os, "twitter4j.properties");
                os.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(-1);
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException ignore) {
                    }
                }
            }
            //debug
            //System.out.println("Successfully stored access token to " + file.getAbsolutePath() + ".");
            
            ViewSupport.showPanelMessage(AlertType.INFORMATION, "Successfully stored access tokens", "You will be logged in at the next launch of the app.");
            System.exit(0);
            
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get accessToken: " + te.getMessage());
            System.exit(-1);
        } 
        /*
        //console input exception
        catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to read the system input.");
            System.exit(-1);
        }
        */
    }
}
