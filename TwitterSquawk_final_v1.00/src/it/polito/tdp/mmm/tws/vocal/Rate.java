/**
 * 13 May 2015, 16:18:24
 */
package it.polito.tdp.mmm.tws.vocal;

/**
 * @author Marco Montalto Monella
 * @since TwitterSquawk 1.0
 */
public enum Rate {
	
	SLOW,
	REGULAR,
	FAST;
	
	@Override
	public String toString(){
		switch(this) {
		case SLOW: return "slow";
		case REGULAR: return "regular";
		case FAST: return "fast";
		default: throw new IllegalArgumentException();
		}
	}
	
}
