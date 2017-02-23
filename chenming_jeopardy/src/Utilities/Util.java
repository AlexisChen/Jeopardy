package Utilities;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.JTextField;

public class Util implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static void ps(String s){
		System.out.println(s);
	}
	public static void unfill(JTextField jtf, String s){
		jtf.setForeground(Color.LIGHT_GRAY);
		jtf.setText(s);
	}
	public static void getFocus(JTextField jtf){
		jtf.setText("");
		jtf.setForeground(Color.BLACK);
	}
}
