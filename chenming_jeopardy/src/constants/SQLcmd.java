package constants;

import java.io.Serializable;

public class SQLcmd implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String getPassword = "SELECT Password FROM UserInfo WHERE Username =?";
	public final static String addUser = "INSERT INTO UserInfo(Username, Password) VALUES(?,?)";

}
