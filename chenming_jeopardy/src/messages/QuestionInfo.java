package messages;

import java.io.Serializable;

public class QuestionInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String username;
	public String category;
	public String pointvalue;
	public String content;
	//if enable==true: submit should be enabled
	//if enable == false: submit should be disabled
	public boolean enable;
}
