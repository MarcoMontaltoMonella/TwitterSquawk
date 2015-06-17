/**
 * 27 Apr 2015, 00:23:48
 */
package it.polito.tdp.mmm.tws.util;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 */
public class UtilityMethods {

	/**
	 * 
	 * @param csv String in the 'comma separated values' format.
	 * @return a set of words without duplicates.
	 */
	public static Set<String> getSetOfStringFromCSV(String csv){
		HashSet<String> set = new HashSet<String>();
		csv.trim();
		
		//first check
		if(UtilityMethods.hasOnlyCommas(csv)){
			return set;
		}
		
		StringTokenizer st = new StringTokenizer(csv,",");
			
		while(st.hasMoreTokens()){
			String token = st.nextToken().trim();
			if(!token.equals("")){
				set.add(token);
			}
		}
		
		return set;
	}
	
	/**
	 * Check if there are only commas in the given string
	 * @param string
	 * @return true if only commas are present
	 */
	private static boolean hasOnlyCommas(String s){
		String regex = "^,+$";
		Pattern p = Pattern.compile(regex);
		return p.matcher(s).matches();
	}
	
}
