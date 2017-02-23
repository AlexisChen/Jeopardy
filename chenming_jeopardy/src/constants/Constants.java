package constants;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.Serializable;
import java.io.Serializable;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Constants implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Color lightblue =  new Color(0x66b2ff);
	public static final Color darkgray = new Color(0x404040);
	public static final Color deepblue = new Color(0x000099);
	
	public static final String chooseplaymode = new String("Choose whether you are joining "
			+ "or hosting a game or playing non-networked.");
	public static final String singleplay = new String("Not Networked");
	public static final String hostplay = new String("Host Game");
	public static final String joinplay = new String("Join Game");
	public static final String chooseteamnumber = new String("Please choose the umber of teams "
				+ "that will be playing on the slider below");
	public static final String choosefilename = new String("Please choose a game file.");
	
	public static String categoryImage = new String("");
	public static String enableImage = new String("");
	public static String disableImage = new String ("");
	
	public static final int center = SwingConstants.CENTER;
	public static Font setfont(String size, int w){
		switch(size){
		case "large": return new Font("Times New Roman", Font.BOLD, w/17);
		case "big": return new Font("Times New Roman", Font.BOLD, w/24);
		case "middle": return new Font("Times New Roman", Font.BOLD, w/30);
		case "small": return new Font("Times New Roman", Font.BOLD, w/35);
		case "tiny":return new Font("Times New Roman", Font.PLAIN, w/40);
		case "mini": return new Font("Times New Roman", Font.PLAIN, w/60);
		default: return null;
		}
	}
	public static Border setborder(int percentagew, int percentageh, int w, int h, Color color){
		int lr = w*percentagew/100;
		int tb = w*percentageh/100;
		return BorderFactory.createMatteBorder(tb, lr, tb, lr, color);		
	}
	public static Dimension setsize(int percentagew, int percentageh, int w, int h){
		int lr = w*percentagew/100;
		int tb = w*percentageh/100;
		return new Dimension(lr, tb);		
	}
	public static String fraction(int numerator, int denominator){
		int max = numerator, min = denominator;
		int mod = max%min;
		while(mod!=0){
			//int temp = mod;
			max = min;
			min = mod;
			mod = max%min;
		}
		int n = numerator/min;
		int d = denominator/min;
		if(d==1)return n+"";
		return n+"/"+d;
	}
	public static void alert(String s){
		JOptionPane.showMessageDialog(new JFrame(),s);
	}
}
