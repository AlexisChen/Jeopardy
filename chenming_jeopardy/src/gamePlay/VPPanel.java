package gamePlay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Utilities.AnimationLabel;
import constants.Constants;

public class VPPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JLabel[] team;
	protected JLabel[] vp;
	protected AnimationLabel[] animation;
	private String[]names;
	
	
	public VPPanel(){
		team = new JLabel[4];
		animation = new AnimationLabel[4];
		vp = new JLabel[4];
		for(int i = 0; i<4; i++){
			team[i] = new JLabel("",SwingConstants.CENTER);
			animation[i] = new AnimationLabel("clock");
			animation[i].set(40, 40);
			vp[i] = new JLabel("$0", SwingConstants.CENTER);
		}
		this.setLayout(new GridLayout(4,2));
		for(int i = 0; i <4; i++){
			team[i].setBackground(Constants.darkgray);;
			team[i].setOpaque(true);
			team[i].setForeground(Color.WHITE);
			team[i].setVisible(false);
			this.add(team[i]);
			animation[i].setBackground(Constants.darkgray);
			animation[i].setOpaque(true);			
			animation[i].setVisible(false);
			this.add(animation[i]);
			vp[i].setBackground(Constants.darkgray);;
			vp[i].setOpaque(true);
			vp[i].setForeground(Color.WHITE);
			vp[i].setVisible(false);
			this.add(vp[i]);
		}
		this.setBackground(Constants.darkgray);
		this.setOpaque(true);
	}
	public void setNames(String[] names){
		this.names = names;
		int size = names.length;
		for(int i = 0; i < size;i++){
			team[i].setText(names[i]);
			team[i].setVisible(true);
//			animation[i].setVisible(true);
			vp[i].setText("$0");
			vp[i].setVisible(true);
		}
	}
	protected void animationOn(int currentAnsweringTeam){
		for(int i = 0; i <4;i++){
			if(i==currentAnsweringTeam){
				animation[i].setVisible(true);
			}else{
				animation[i].setVisible(false);
			}
		}
	}
}


