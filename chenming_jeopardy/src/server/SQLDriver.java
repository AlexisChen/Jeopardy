package server;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Driver;

import constants.SQLcmd;

public class SQLDriver implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection conn;
	public SQLDriver(){
		try{
			new Driver();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public void connect(){
		try{
			conn = DriverManager.getConnection("jdbc:mysql://localhost/Jeopardy?user=root&password=123qwe&useSSL=false");
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public void stop(){
		try{
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public String getPassword(String username){
		String psw = new String("");
		try{
			PreparedStatement ps = conn.prepareStatement(SQLcmd.getPassword);
			ps.setString(1, username);
			ResultSet result = ps.executeQuery();
			if(result.next()){
//				System.out.println("username exist, and password is: "+result.getString(1));
				psw= result.getString(1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return psw;
	}
	public void add(String username, String password){
		try{
			PreparedStatement ps = conn.prepareStatement(SQLcmd.addUser);
			ps.setString(1, username);
			ps.setString(2, password);
			ps.executeUpdate();
			System.out.println("successfully inserted username: "+ username+" password: "+password);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}//end of SQLDriver class
