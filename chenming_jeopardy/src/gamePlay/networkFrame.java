package gamePlay;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Utilities.Util;
import client.ClientPlayer;
import constants.Constants;
import server.GameHost;

public class networkFrame extends JFrame{
	public static final long serialVersionUID = 1;
	private JLabel gameProcess;
	public JTextArea commandWindow;
	private JPanel left,right;
	private JMenuBar menubar;
	private JMenu menu;
	protected JMenuItem[] options;
	protected JPanel cards;
	private JScrollPane sp;
	private int w,h;
	private int id;
	private GameHost gh;
	private ClientPlayer cp;
	private String[] names;
	protected VPPanel vpboard;
	public networkFrame(String[] names){
		super("Play the Jeopardy");
		this.names = names;
		this.vpboard = new VPPanel();
		this.vpboard.setNames(names);
		commandWindow = new JTextArea(50,50);
	}
	public void setId( int id){
		this.id = id;
	}
	public void setClientPlayer(ClientPlayer cp){
		this.cp = cp;
	}
	public void setGameHost(GameHost gh){
		this.gh = gh;
	}
	public void setCW(JTextArea jta){
		this.commandWindow = jta;
		this.commandWindow.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				networkFrame.this.gh.updateCW(commandWindow.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	public void setup(){
		initializeComponents();
		createGUI();
		addEvents();
	}
	public void setPanel(JPanel inPanel, String name){
		cards.add(inPanel,name);
	}
	public void updateCW(String s){
		this.commandWindow.setText(s);
	}
	public void updateVP(String[] vp){
		this.setVisible(false);
		int size = vp.length;
		for (int i = 0; i < size; i++) {
//			Util.ps(vp[i]);
			this.vpboard.vp[i].setText(vp[i]);
			vpboard.vp[i].repaint();
			vpboard.vp[i].revalidate();
		}
		this.setVisible(true);
	}
	private void initializeComponents(){
		String selfName;
		if(this.gh!=null){
			selfName = gh.selfName;
		}else{
			selfName = cp.playerName;
		}
		gameProcess = new JLabel("Game Process "+selfName, SwingConstants.CENTER);
		cards = new JPanel(new CardLayout());
		sp = new JScrollPane(commandWindow,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		right = new JPanel(new GridBagLayout());
	}
	private void createGUI(){
		setLocation(300,300);
		setSize(1500,1000);
		setBackground(Color.LIGHT_GRAY);
		w = this.getWidth();
		h = this.getHeight();
		
		
		setLayout(new BorderLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.47;
		
		vpboard.setPreferredSize(Constants.setsize(40, 40, w, h));
		right.add(vpboard,gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 0.06;
		gameProcess.setBackground(Constants.lightblue);
		gameProcess.setFont(Constants.setfont("tiny", w));
		gameProcess.setOpaque(true);
		right.add(gameProcess,gbc);
		
		gbc.gridy = 2;
		gbc.weighty = 0.47;
		commandWindow.setBackground(Constants.lightblue);
		commandWindow.setOpaque(true);
		commandWindow.setLineWrap(true);
		commandWindow.setWrapStyleWord(true);
		right.add(sp,gbc);

		add(cards, BorderLayout.CENTER);
		right.setPreferredSize(new Dimension(w*2/7,0));
		add(right, BorderLayout.EAST);	
		createMenu();
	}
	private void addEvents(){
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	if (JOptionPane.showConfirmDialog(null, 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		            if(id==0){
		            	gh.exitThroughMenu();
		            	System.exit(0);
		            }else {
		            	cp.exitThroughMenu();
		            	System.exit(0);
		            }
		        }
		    }
		});
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
//				adjust();
			}
		});
	}//end of add actionlistner
	private void createMenu(){	
		menubar = new JMenuBar();
		menu = new  JMenu("Menu");
		options= new JMenuItem[4];
		options[0] = new JMenuItem("Restart This Game");
		options[1] = new JMenuItem("Choose New Game File");
		options[2] = new JMenuItem("Exit Game");	
		options[3] = new JMenuItem("Logout");	
		if(id == 0) menu.add(options[0]);
		for(int i = 1; i < 4; i++){
			menu.add(options[i]);
		}
		menubar.add(menu);
		setJMenuBar(menubar);
		if(id==0){
			//restart the game
			options[0].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					networkFrame.this.gh.replayGame();
				}
			});
		}
		// choose new game file
		options[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(id==0){
					gh.newFile();
				}else if(id>0){
					cp.newFile();
				}
				String s = new String(names[id]);
				WelcomeWindow firstwindow = new WelcomeWindow(s);
				firstwindow.setVisible(true);
				dispose();
			}
		});
		// exit game
		options[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(id==0){
					gh.exitThroughMenu();
					System.exit(0);
				}else if(id>0){
					cp.exitThroughMenu();
					System.exit(0);
				}
			}
		});
		// log out
		options[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(id==0){
					gh.logoutThroughMenu();
				}else if(id>0){
					cp.logoutThroughMenu();
				}
				dispose();
				LoginWindow login = new LoginWindow();
				login.setVisible(true);
			}
		});
	}
	public void changePanel(String p){
		CardLayout cl = (CardLayout) this.cards.getLayout();
		cl.show(cards, p);
	}
	public void animationOn(int currentAnsweringTeam){
		this.vpboard.animationOn(currentAnsweringTeam);
	}
//	private void adjust(){
//		 w = this.getWidth();
//		 h = this.getHeight();
//		right.setPreferredSize(new Dimension(w*2/7,0));
//		for(int i = 0; i <4; i++){
//			vpboard.team[i].setFont(Constants.setfont("tiny", w));
//			vpboard.vp[i].setFont(Constants.setfont("tiny", w));	
//		}
//		gameProcess.setFont(Constants.setfont("tiny", w));
//		commandWindow.setFont(Constants.setfont("tiny", w));
//	}

}
