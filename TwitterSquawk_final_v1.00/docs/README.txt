TwitterSquawk
=============

Requirements
- Operative System: OS X Mountain Lion or above
- A Twitter developer account

In order to collect tweets for analysis, you’ll need to create an account on the Twitter developer site and generate credentials for use with the Twitter API.



To create a Twitter developer account:

1. Go to https://dev.twitter.com/user/login and log in with your Twitter user name and password. If you do not yet have a Twitter account, click the Sign up link that appears under the Username field.

2. If you have not yet used the Twitter developer site, you'll be prompted to authorize the site to use your account. Click Authorize app to continue.

3. Go to the Twitter applications page at https://dev.twitter.com/apps and click Create a new application.

4. Follow the on-screen instructions. For the application Name, Description, and Website, you can specify any text — you're simply generating credentials to use with this tutorial, rather than creating a real application.

5.Twitter displays the details page for your new application. Click the ‘Keys and Access Tokens’ tab.

6. You'll see a Consumer key and Consumer secret. Make a note of these values; you'll need them later in this tutorial.




Test 
[If this is NOT your first time using TwitterSquawk and you have NOT logged out, skip points 5 to 10]

1. Import the zip file of the project into Eclipse.
2. Click ‘Run as Java Application’ on the Launch.java file present in the ‘it.polito.tdp.mmm.tws’ package.
3. The introduction window will appear.
4. Click on Go.
5. Insert Consumer Key and Consumer Secret obtained from the ‘To create a Twitter developer account’ previous step.
6. If you’ve correctly inserted the two values, an authorization page should have been open in your browser.
7. Click ‘Authorize App’.
8. The browser should now display a PIN number. Copy it and paste it inside the java application window.
9. The application will exit and your keys will be stored inside the twitter4j.properties file, now present in the project.
10. Run the application again.
11. Use the application.

To have more debug information, uncomment the ‘println’ following the ‘//debug’ line of code.

(YouTube video tutorial at: https://www.youtube.com/watch?v=-zN892KUz1s)


Try to wisely use the global tweets source.
(Tip: insert words at least 3 letters long)

KNOWN BUG:
If you insert filter words such as ‘a,b,c,d,e’, the display panel will crash due to the huge volume of tweets, and their proximity in time.

