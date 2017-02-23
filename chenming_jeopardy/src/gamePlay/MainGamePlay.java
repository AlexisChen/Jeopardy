package gamePlay;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.Serializable;

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

import constants.Constants;

public class MainGamePlay extends JFrame implements Serializable{
	public static final long serialVersionUID = 1;
	
	private JLabel[] team;
	protected JLabel[] vp;
	private JLabel gameProcess;
	protected JTextArea commandWindow;
	protected QuestionScreen qs;
	private ButtonPanel bp;
	private JPanel left,right;
	private JMenuBar menubar;
	private JMenu menu;
	protected JMenuItem[] options;
	protected JPanel cards;
	private JScrollPane sp;
	private int w,h;
	protected Jeopardy Game;
	private WelcomeWindow ww;
	
	public MainGamePlay(WelcomeWindow ww){
		
		super("Play the Jeopardy");
		this.ww = ww;
		this.Game = ww.Game;
		Game.initialanswered = ww.answered;
		initializeComponents();
		createGUI();
		addEvents();
	}
	private void initializeComponents(){
		bp = new ButtonPanel(this);
		team = new JLabel[4];
		vp = new JLabel[4];
		for(int i = 0; i<4; i++){
			team[i] = new JLabel("",SwingConstants.CENTER);
			vp[i] = new JLabel("$0", SwingConstants.CENTER);
		}
		gameProcess = new JLabel("Game Process", SwingConstants.CENTER);
		commandWindow = new JTextArea(50,50);
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
		createMenu();
		
		setLayout(new BorderLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.47;
		JPanel vpboard = new JPanel(new GridLayout(4,2));
		for(int i = 0; i <4; i++){
			team[i].setBackground(Constants.darkgray);;
			team[i].setOpaque(true);
			team[i].setForeground(Color.WHITE);
			vpboard.add(team[i]);
			vp[i].setBackground(Constants.darkgray);;
			vp[i].setOpaque(true);
			vp[i].setForeground(Color.WHITE);
			vp[i].setVisible(false);
			vpboard.add(vp[i]);
		}

		vpboard.setBackground(Constants.darkgray);
		vpboard.setOpaque(true);
		for(int i = 0; i < Game.TeamNumber;i++){
			team[i].setText(Game.Team[i]);
			team[i].setVisible(true);
			vp[i].setText("$0");
			vp[i].setVisible(true);
		}
		right.add(vpboard,gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 0.06;
		gameProcess.setBackground(Constants.lightblue);
		gameProcess.setFont(new Font("Times New Roman",Font.BOLD,24));
		gameProcess.setOpaque(true);
		right.add(gameProcess,gbc);
		
		gbc.gridy = 2;
		gbc.weighty = 0.47;
		commandWindow.setBackground(Constants.lightblue);
		commandWindow.setOpaque(true);
		commandWindow.setLineWrap(true);
		commandWindow.setWrapStyleWord(true);
		right.add(sp,gbc);
		cards.add(bp, "MGP");
		add(cards, BorderLayout.CENTER);
		right.setPreferredSize(new Dimension(w*2/7,0));
		add(right, BorderLayout.EAST);		
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
		            System.exit(0);
		        }
		    }
		});

		//restart the game
		options[0].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				Game.replay();
				CardLayout cl = (CardLayout)(cards.getLayout());
				cl.show(cards, "MGP");
				for(int i = 0; i <25; i++){
					unchoose(bp.qbutton[i],bp.enableimage);
				}
				if(qs!=null){
					qs.setVisible(false);
					qs = null;
				}
			}
		});
		//choose new game file 
		options[1].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ww.setVisible(true);
				ww.reset();
				MainGamePlay.this.dispose();
			}
		});
		//exit game
		options[2].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				System.exit(0);
			}
		});
		//log out
		options[3].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				LoginWindow login = new LoginWindow();
				login.setVisible(true);
				dispose();
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
				adjust();
			}
		});
	}
	//this
	private void createMenu(){	
		menubar = new JMenuBar();
		menu = new  JMenu("Menu");
		options= new JMenuItem[4];
		options[0] = new JMenuItem("Restart This Game");
		options[1] = new JMenuItem("Choose New Game File");
		options[2] = new JMenuItem("Exit Game");	
		options[3] = new JMenuItem("Logout");	

		for(int i = 0; i < 4; i++){
			menu.add(options[i]);
		}
		menubar.add(menu);
		setJMenuBar(menubar);
	}
	

	protected void unchoose(imageButton b, Image img){
		b.img=img;
		b.fontColor = Color.BLACK;
		b.setEnabled(true);
	}
	private void adjust(){
		 w = this.getWidth();
		 h = this.getHeight();
		right.setPreferredSize(new Dimension(w*2/7,0));
		bp.heading.setFont(Constants.setfont("small", w));
		for(int i = 0; i <4; i++){
			team[i].setFont(Constants.setfont("tiny", w));
			vp[i].setFont(Constants.setfont("tiny", w));	
		}
		gameProcess.setFont(Constants.setfont("middle", w));
		commandWindow.setFont(Constants.setfont("tiny", w));
	}
}





