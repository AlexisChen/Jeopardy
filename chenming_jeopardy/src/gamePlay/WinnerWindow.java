package gamePlay;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import client.ClientPlayer;
import constants.Constants;
import server.GameHost;

public class WinnerWindow extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel h1, h2,h3,rate, averageRating;
	private JSlider rates;
	private JButton ok;
	private int w, h;
	private JPanel sliderp;
	private String m;
	private String ave;
	private JPanel cards;
	private ReadFile rf;
	private int id = -1;
	private GameHost gh;
	private ClientPlayer cp;
	private String name;
	
	public void setID(int id){
		this.id = id;
	}
	public void setGameHost(GameHost gh){
		this.gh = gh;
	}
	public void setClientPlayer(ClientPlayer cp){
		this.cp = cp;
	}
	public void setName(String s){
		this.name = name;
	}
	public String getMessage(){
		return this.m;
	}
	public String getRating(){
		return this.ave;
	}
	public WinnerWindow(String message, String rating){
		super();
		m = new String(message);
		ave = rating;
		initializeComponents();
		createGUI();
		addEvents();
	}
	public WinnerWindow(String message, JPanel cards, ReadFile rf){
		super("");
		m = new String(message);
		this.cards = cards;
		this.rf = rf;
		ave = Constants.fraction(rf.totalrating, rf.totalpeople);
		initializeComponents();
		createGUI();
		addEvents();
	}
	private void initializeComponents(){
		h1 = new JLabel("sad",SwingConstants.CENTER);
		h2 = new JLabel("There is no winners!",SwingConstants.CENTER);
		h3 = new JLabel("Please rate this game file on a scale from 1 to 5",SwingConstants.CENTER);

		averageRating = new JLabel("current average rating: "+ave,SwingConstants.CENTER);
		rates = new JSlider(JSlider.HORIZONTAL,1,5,3);
		String index = new String(rates.getValue()+"");
		rate = new JLabel(index,SwingConstants.CENTER);
		ok = new JButton("OK");
		sliderp =  new JPanel(new BorderLayout());
		if(m.equalsIgnoreCase("There is no winner")){
			h1.setText("Sad!");
		}else if(m.equalsIgnoreCase("It's a tie!")){
			h1.setText("Hummmm...");
		}else{
			h1.setText("And the winner is...");
		}
		h2.setText(m);
	}
	private void createGUI(){
		setSize(600,600);
		setLocation(300,200);
		setLayout(new GridBagLayout());
		this.getContentPane().setBackground(Constants.lightblue);
		w = this.getWidth();
		h = this.getHeight();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.2;
		gbc.fill = GridBagConstraints.BOTH;
		h1.setBackground(Constants.lightblue);
		h1.setOpaque(true);
		h1.setFont(Constants.setfont("big", w));
		add(h1, gbc);
		
		gbc.gridx = 0; 
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.2;
		gbc.fill = GridBagConstraints.BOTH;
		h2.setBackground(Constants.darkgray);
		h2.setOpaque(true);
		h2.setFont(Constants.setfont("large", w));
		h2.setForeground(Color.LIGHT_GRAY);
		add(h2, gbc);
		
		gbc.gridx = 0; 
		gbc.gridy = 3;
		gbc.weightx = 0.5;
		gbc.weighty = 0.05;
		gbc.fill = GridBagConstraints.BOTH;
		h3.setBackground(Constants.lightblue);
		h3.setOpaque(true);
		h3.setFont(Constants.setfont("big", w));
		add(h3, gbc);
		
		gbc.gridx = 0; 
		gbc.gridy = 4;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		rates.setMajorTickSpacing(1);
		rates.setMinorTickSpacing(1);
		rates.setPaintTicks(true);
		rates.setPaintLabels(true);
		rates.setSnapToTicks(true);
		sliderp.add(rates, BorderLayout.CENTER);
		rate.setBackground(Constants.darkgray);
		rate.setOpaque(true);
		rate.setFont(Constants.setfont("big", w));
		rate.setPreferredSize(new Dimension(w/10,0));
		rate.setBorder(Constants.setborder(1, 1, w, h, Constants.lightblue));
		rate.setForeground(Color.LIGHT_GRAY);
		sliderp.add(rate, BorderLayout.EAST);
		sliderp.setBackground(Constants.lightblue);
		sliderp.setOpaque(true);
		sliderp.setBorder(Constants.setborder(5, 1, w, h, Constants.lightblue));

		add(sliderp, gbc);
		
		gbc.gridx = 0; 
		gbc.gridy = 5;
		gbc.weightx = 0.5;
		gbc.weighty = 0.05;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		averageRating.setBackground(Constants.lightblue);
		averageRating.setOpaque(true);
		averageRating.setFont(Constants.setfont("big", w));
		add(averageRating, gbc);
		
		gbc.gridx = 0; 
		gbc.gridy = 6;
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.SOUTH;
		ok.setBackground(Color.LIGHT_GRAY);
		ok.setForeground(Color.WHITE);
		ok.setEnabled(false);
		ok.setOpaque(true);
		ok.setFont(Constants.setfont("big", w));
		ok.setBorder(BorderFactory.createEmptyBorder());
		ok.setPreferredSize(new Dimension(w/6,h/8));
		add(ok, gbc);
	}
	private void addEvents(){
		this.addComponentListener(new ComponentListener() {
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				adjust();
			}
		});
		rates.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				rate.setText(rates.getValue()+"");
				ok.setBackground(Constants.darkgray);
				ok.setForeground(Color.LIGHT_GRAY);
				ok.setEnabled(true);
			}
			
		});
		JFrame parent = (WinnerWindow)this;
		ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(id==-1){
					int newrate = rates.getValue();
					rf.write(newrate,1);
					CardLayout cl = (CardLayout) cards.getLayout();
					cl.show(cards, "MGP");
					parent.setVisible(false);
				}else if(id==0){
					gh.rate(rates.getValue());
					gh.finishrating();
					h2.setText("waiting for others to rate");
				}else if(id>0){
					cp.rate(rates.getValue());
					WelcomeWindow firstwindow = new WelcomeWindow(name);
					firstwindow.setVisible(true);
					dispose();
				}
				
			}
		});
	}
	public void adjust(){
		w = this.getWidth();
		h = this.getHeight();
		h1.setFont(Constants.setfont("big", w));
		h2.setFont(Constants.setfont("large", w));
		h3.setFont(Constants.setfont("big", w));
		rate.setFont(Constants.setfont("big", w));
		averageRating.setFont(Constants.setfont("big", w));
		sliderp.setBorder(Constants.setborder(5, 1, w, h, Constants.lightblue));
		ok.setFont(Constants.setfont("big", w));
		ok.setPreferredSize(new Dimension(w/6,h/8));

	}
}
