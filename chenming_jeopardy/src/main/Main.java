package main;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import Utilities.AnimationLabel;
import Utilities.JeopardyTimer;
import gamePlay.LoginWindow;



public class Main {
	public static void main(String[] args) throws Exception{
			LoginWindow login = new LoginWindow();
			login.setVisible(true);
//		timewindow tw = new timewindow();
//		tw.setVisible(true);
	}
}

//
class timewindow extends JFrame{
	private static final long serialVersionUID = 1L;
	private JLabel timerlabel;
	private Timer timer;
	public timewindow(){
		this.setSize(new Dimension(100,100));
		this.setLocation(200,200);
		this.timerlabel = new JLabel("timer", SwingConstants.CENTER);
		add(timerlabel, BorderLayout.NORTH);
		JeopardyTimer jt = new JeopardyTimer(40);
		jt.setLabel(this.timerlabel);
		this.timer = new Timer(1000, jt);
		jt.setTimer(timer);
		timer.start();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		JLabel label = new JLabel(new ImageIcon("images/clockAnimation/Clock.gif"));
//		JLabel label = new JLabel(new ImageIcon("images/clockAnimation/frame_" + "0" + "_delay-0.06s.jpg"));
//		AnimationLabel label = new AnimationLabel("clock");
//		AnimationLabel label = new AnimationLabel("fish");
//		label.setSize(new Dimension(0,0));
//		label.setVisible(true);
		// JFrame f = new JFrame("Animation");
//		add(label, BorderLayout.CENTER);
//		this.pack();	   
	}	
}