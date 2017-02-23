package messages;

import java.io.Serializable;

public class SetBet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public int bet;
	public String name;
	public int point;
	public String fjquestion;
	public boolean[] tofj;
}
