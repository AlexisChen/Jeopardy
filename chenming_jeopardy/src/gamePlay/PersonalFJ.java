package gamePlay;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import client.ClientPlayer;
import constants.Constants;
import server.GameHost;

public class PersonalFJ extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JMenuBar jmb;
	private JButton setBet, submitAns;
	private JLabel heading, betTitle, betAmount;
	private JLabel[] allBets;
	private JSlider betslider;
	private JLabel blueinfo;
	private JTextField input;
	private JLabel grayinfo;
	private int w, h;
	
	private int id;
	private String username;
	private String fjq;
	private GameHost gh;
	private ClientPlayer cp;
	
	public PersonalFJ(networkFrame nf){
		super(new GridBagLayout());
		this.w =nf.cards.getWidth();
		this.h = nf.cards.getWidth();
		initializeVariable();
		createGUI();
		addEvents();
	}
	
	public void setName(String s){
		username = s;
		betTitle.setText(username+"'s bet");
		input.setText(username+"'s answer");
	}
	public void setID(int id){
		this.id = id;
	}
	public void setGameHost(GameHost gh){
		this.gh = gh;
	}
	public void setClientPlayer(ClientPlayer cp){
		this.cp = cp;
	}
	public void setSlider(int high){
		betslider.setMinimum(1);
		betslider.setMaximum(high);
		betslider.setMajorTickSpacing(high/10);
		betslider.setMinorTickSpacing(1);
		betslider.repaint();
		betslider.revalidate();
		betslider.setVisible(true);
	}
	public void setFJ(String s){
		this.fjq = s;
	}
	public void setWaitingLabels(String[]names){
		int size = names.length;
		for(int i = 0; i < size-1; i++){
			int index = (id+1+i)%size;
			String s = new String("Waiting for "+names[index]+
					" to set their bet...");
			this.allBets[i].setText(s);
		}
	}
	public void setWaitingVisible(boolean[]tofj){
		int size = tofj.length;
		for(int i = 0; i < size-1; i++){
			int index = (id+1+i)%size;
			if(tofj[index]==false){
				this.allBets[i].setVisible(false);
			}
		}
	}
	//should be used later 
	public void setBet(int id, String s){
		allBets[id].setText(s);		
	}
	public void enableSubmit(){
		this.submitAns.setEnabled(true);
	}
	private void initializeVariable(){
		setBet = new JButton("Set Bet");
		submitAns = new JButton("Submit Answer");
		heading = new JLabel("Final Jeopardy Round", SwingConstants.CENTER);
		betTitle = new JLabel("", SwingConstants.CENTER);
		betAmount = new JLabel("$", SwingConstants.CENTER);
		allBets = new JLabel[3];
		for(int i = 0; i < 3; i++){
			allBets[i] = new JLabel("", SwingConstants.CENTER);
		}
		betslider = new JSlider();
		blueinfo = new JLabel("Waiting for it...",SwingConstants.CENTER);
		input = new JTextField("");
		grayinfo = new JLabel("", SwingConstants.CENTER);
	}
	private void createGUI(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 0.5;
		gbc.weighty = 0.06;
		gbc.gridx = 0;//and will be 0 always
		gbc.gridy = 0;//increasing
		
		heading.setFont(Constants.setfont("mini", w));
		heading.setForeground(Color.WHITE);
		heading.setBackground(Constants.deepblue);
		heading.setOpaque(true);
		add(heading, gbc);
		
		gbc.gridy = 1;
		JPanel sliderp = new JPanel();
		betTitle.setFont(Constants.setfont("mini", w));
		betTitle.setForeground(Color.WHITE);
		betTitle.setBackground(Color.DARK_GRAY);
		betTitle.setOpaque(true);
		betTitle.setPreferredSize(Constants.setsize(12, 8, w, h));
		sliderp.add(betTitle);
		betslider.setPaintLabels(true);
		betslider.setSnapToTicks(true);
		betslider.setForeground(Color.WHITE);
		betslider.setBackground(Color.DARK_GRAY);
		betslider.setOpaque(true);
		betslider.setVisible(false);
		betslider.setPreferredSize(Constants.setsize(55, 8, w, h));
		sliderp.add(betslider);
		betAmount.setFont(Constants.setfont("mini", w));
		betAmount.setForeground(Color.WHITE);
		betAmount.setBackground(Color.DARK_GRAY);
		betAmount.setOpaque(true);
		betAmount.setPreferredSize(Constants.setsize(12, 8, w, h));
		sliderp.add(betAmount);
		setBet.setFont(Constants.setfont("mini", w));
		setBet.setPreferredSize(Constants.setsize(10, 8, w, h));
		sliderp.setPreferredSize(Constants.setsize(100, 8, w, h));
		sliderp.setBackground(Color.DARK_GRAY);
		sliderp.setOpaque(true);
		sliderp.add(setBet);
		add(sliderp, gbc);		
		
		gbc.gridy = 2;
		for(int i = 0; i < 3; i++){
			allBets[i].setFont(Constants.setfont("mini", w));
			allBets[i].setForeground(Color.WHITE);
			allBets[i].setBackground(Color.DARK_GRAY);
			allBets[i].setOpaque(true);
			add(allBets[i], gbc);
			gbc.gridy++;
		}
		
		blueinfo.setFont(Constants.setfont("mini", w));
		blueinfo.setBackground(Constants.lightblue);
		blueinfo.setOpaque(true);
		add(blueinfo, gbc);
		
		gbc.gridy++;
		JPanel answerp = new JPanel();
		input.setPreferredSize(Constants.setsize(40, 5, w, h));
		answerp.add(input);
		submitAns.setPreferredSize(Constants.setsize(20, 5, w, h));
		submitAns.setEnabled(false);
		answerp.add(submitAns);
		answerp.setBackground(Color.DARK_GRAY);
		answerp.setOpaque(true);
		add(answerp, gbc);
		
		gbc.gridy++;
		grayinfo.setFont(Constants.setfont("mini", w));
		grayinfo.setForeground(Color.WHITE);
		grayinfo.setBackground(Color.DARK_GRAY);
		grayinfo.setOpaque(true);
		add(grayinfo, gbc);
		
		this.setBackground(Color.DARK_GRAY);
		
	}
	private void addEvents(){
		
		betslider.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				int amount = betslider.getValue();
				betAmount.setText("$"+amount);
			}
		});//end of betslider listener
		setBet.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				betslider.setEnabled(false);
				setBet.setEnabled(false);
				input.setFocusable(true);
				blueinfo.setText(fjq);
				if(id==0){
					gh.setBet(0, betslider.getValue());
				}else if(id>0){
					cp.setBet(betslider.getValue());
				}
			}
		});
		submitAns.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				grayinfo.setText("Waiting for the rest of the players to answer");
				submitAns.setEnabled(false);
				input.setEnabled(false);
				if(id==0){
					gh.submit(0, input.getText());
				}else if(id>0){
					cp.submit(input.getText());
				}
			}
		});
		
		
	}//end of adding events
}
