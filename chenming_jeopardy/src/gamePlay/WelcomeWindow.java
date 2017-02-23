package gamePlay;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import Utilities.Util;
import client.ClientPlayer;
import constants.Constants;
import server.GameHost;
public class WelcomeWindow extends JFrame implements Runnable{
	public static final long serialVersionUID = 1;
	private JLabel cap,subcap, choosemode, gameFile, fileName, averageRating, teamNum;
	private JLabel[] askname;
	private JButton chooseFile, startJ, clearChoice, logout,exit;
	private JSlider teamNumSlider;
	public JTextField[] name;
	protected Jeopardy Game = null;
	private JFileChooser fc;
	public  MainGamePlay mainGamePlay;
	private JCheckBox quickplay;
	public int answered = 0, teamnumber = 1;
	private int w, h;
	public JRadioButton[] playmode;
	private JTextField[] port;
	private int mode = 0;//0:not networked | 1: host game | 2:join game
	private ButtonGroup group;
	private String username;
	private GameHost gh;
	private ClientPlayer cp;
	private boolean isWaiting = false;
	public JLabel waitingl;
	
	public WelcomeWindow(String username){
		super("Welcome to Jeopardy");
		this.username = username;
		initializeComponent();
		createGUI();
		addEvents();
		new Thread(this).start();
	}
	
	private void initializeComponent(){
		cap = new JLabel("Welcome to Jeoparday!",Constants.center);
		
		subcap = new JLabel(Constants.chooseplaymode,SwingConstants.CENTER);
		choosemode = new JLabel(Constants.chooseplaymode, SwingConstants.CENTER);
		gameFile = new JLabel(Constants.choosefilename, SwingConstants.CENTER);
		fileName = new JLabel();
		averageRating = new JLabel();
		teamNum = new JLabel(Constants.chooseteamnumber,SwingConstants.CENTER);
		askname = new JLabel[4];
		name = new JTextField[4];
		for(int i = 0; i <4; i++){
			askname[i] = new JLabel("Please name Team"+(i+1), SwingConstants.CENTER);
			name[i] = new JTextField();
//			System.out.println("waht the fuck "+(name[i]==null));
		}
//		System.out.println("waht the fuck "+(name[0]==null));
		
		chooseFile = new JButton("Choose File");
		startJ = new JButton("Start Jeopardy");
		clearChoice = new JButton("Clear Choices");
		logout = new JButton("Logout");
		exit = new JButton("Exit");
		teamNumSlider = new JSlider(JSlider.HORIZONTAL,1,4,1);
		fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		fc.setFileFilter(filter);
		Game = null;
		quickplay = new JCheckBox("Quick Play");
		playmode = new JRadioButton[3];
		playmode[0] = new JRadioButton(Constants.singleplay);
		playmode[1] = new JRadioButton(Constants.hostplay);
		playmode[2] = new JRadioButton(Constants.joinplay);
		port = new JTextField[2];
		port[0] = new JTextField("port");
		port[1] = new JTextField("IP Adress");
		waitingl = new JLabel("Waiting for connection...", SwingConstants.CENTER);
		group = new ButtonGroup();
		
	}
	
	private void createGUI(){
		setLocation(400,100);
		setSize(500,525);
		w = this.getWidth();
		h = this.getHeight();
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;		
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		JPanel capp = new JPanel(new BorderLayout());
		cap.setFont(new Font("Times New Roman",Font.BOLD,24));
		cap.setBackground(Constants.lightblue);
		cap.setOpaque(true);
		capp.add(cap, BorderLayout.CENTER);
		quickplay.setBackground(Constants.lightblue);
		quickplay.setOpaque(true);
		capp.add(quickplay, BorderLayout.EAST);
		add(capp,gbc);
		
		gbc.gridy = 1;
		gbc.weighty = 0.1;
		subcap.setFont(new Font("Times New Roman",Font.PLAIN,12));
		subcap.setBackground(Constants.lightblue);
		subcap.setOpaque(true);
		add(subcap,gbc);
		
		gbc.gridy++;
		JPanel radiobuttonp = new JPanel(new GridLayout(1,3));
		for(JRadioButton b: playmode){
			group.add(b);
			radiobuttonp.add(b);
		}
		this.playmode[0].isSelected();
		radiobuttonp.setBackground(Constants.lightblue);
		radiobuttonp.setOpaque(true);;
		add(radiobuttonp, gbc);
		
		gbc.gridy++;
		JPanel portp = new JPanel(new GridLayout(1,2));
		for(JTextField j: port){
			j.setForeground(Color.LIGHT_GRAY);
			portp.add(j);
			j.setVisible(false);
		}
		portp.setBackground(Constants.deepblue);
		portp.setOpaque(true);
		add(portp, gbc);

		gbc.gridy ++;
		gbc.weighty = 0.03;
		teamNum.setBackground(Constants.deepblue);
		teamNum.setOpaque(true);
		teamNum.setForeground(Color.WHITE);
		add(teamNum, gbc);


		gbc.gridy ++;
		gbc.weighty = 0.05;
		teamNumSlider.setMajorTickSpacing(1);
		teamNumSlider.setMinorTickSpacing(1);
		teamNumSlider.setPaintTicks(true);
		teamNumSlider.setPaintLabels(true);
		teamNumSlider.setSnapToTicks(true);
		teamNumSlider.setForeground(new Color(0xffffff));
		teamNumSlider.setBackground(new Color(0x404040));
		teamNumSlider.setOpaque(true);
		add(teamNumSlider, gbc);

		gbc.gridy ++;//5
		gbc.weighty = 0.1;
		JPanel filechooser = new JPanel(new GridLayout(1,4));
		gameFile.setForeground(new Color (0xffffff));
		chooseFile.setForeground(new Color (0xffffff));
		chooseFile.setBackground(new Color (0x404040));
		chooseFile.setBorder(BorderFactory.createEmptyBorder());
		chooseFile.setOpaque(true);
		filechooser.setBackground(new Color(0x000099));
		filechooser.setOpaque(true);
		filechooser.add(gameFile);
		filechooser.add(chooseFile);
		fileName.setVisible(false);
		fileName.setForeground(Color.WHITE);
		fileName.setBackground(new Color(0x000099));
		fileName.setOpaque(true);
		filechooser.add(fileName);
		averageRating.setVisible(false);
		averageRating.setForeground(Color.WHITE);
		averageRating.setBackground(new Color(0x000099));
		averageRating.setOpaque(true);
		filechooser.add(averageRating);
		filechooser.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Constants.deepblue));
		add(filechooser,gbc);

		gbc.gridy ++;//6
		gbc.weighty = 0.52;
		JPanel fourbox = new JPanel (new GridLayout(2,2));
		JPanel[][]box = new JPanel[2][2];
		for(int i = 0; i<2; i++){
			int index = 0;
			for(int j = 0; j <2; j++){
				index = 2*j+i;
				box[i][j] = new JPanel(new GridLayout(2,1));
				askname[index].setForeground(Color.WHITE);
				askname[index].setBackground(Constants.darkgray);
				askname[index].setOpaque(true);
//				askname[index].setPreferredSize(new Dimension(50,50));
				askname[index].setBorder(BorderFactory.createMatteBorder(10, 30, 0, 30, Constants.deepblue));
				if(index>0)askname[index].setVisible(false);
				box[i][j].add(askname[index], BorderLayout.NORTH);
				name[index].setBackground(Constants.lightblue);
				name[index].setOpaque(true);
//				name[index].setPreferredSize(new Dimension(50,50));
				name[index].setBorder(BorderFactory.createMatteBorder(5, 30, 30, 30, Constants.deepblue));
				if(index>0)name[index].setVisible(false);
				box[i][j].setBackground(Constants.deepblue);
				box[i][j].add(name[index],BorderLayout.CENTER);
				fourbox.add(box[i][j]);
			}
		}

		fourbox.setBackground(Constants.deepblue);
		fourbox.setOpaque(true);
		add(fourbox, gbc);
		
		gbc.gridy++;
		waitingl.setBackground(Constants.deepblue);
		waitingl.setOpaque(true);
		waitingl.setForeground(Color.white);
		waitingl.setVisible(false);
		add(waitingl, gbc);

		gbc.gridy ++;//7
		gbc.weighty = 0.1;
		Border border = BorderFactory.createMatteBorder(0, 0, 10, 0, Constants.deepblue);
		JPanel buttonPanel = new JPanel();
		startJ.setBackground(Constants.darkgray);
		startJ.setForeground(new Color(0xffffff));
		startJ.setBorder(border);
		startJ.setOpaque(true);
		startJ.setPreferredSize(new Dimension(w/5,h/15));
		buttonPanel.add(startJ);
		clearChoice.setBackground(Constants.darkgray);
		clearChoice.setForeground(new Color(0xffffff));
		clearChoice.setBorder(border);
		clearChoice.setOpaque(true);
		clearChoice.setPreferredSize(new Dimension(w/5,h/15));

		buttonPanel.add(clearChoice);
		logout.setBackground(Constants.darkgray);
		logout.setForeground(new Color(0xffffff));
		logout.setBorder(border);
		logout.setOpaque(true);
		logout.setPreferredSize(new Dimension(w/5,h/15));

		buttonPanel.add(logout);
		exit.setBackground(Constants.darkgray);
		exit.setForeground(new Color(0xffffff));
		exit.setBorder(border);
		exit.setOpaque(true);
		exit.setPreferredSize(new Dimension(w/5,h/15));

		buttonPanel.add(exit);
		buttonPanel.setBackground(new Color(0x000099));
		buttonPanel.setOpaque(true);
		buttonPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Constants.deepblue));
		add(buttonPanel,gbc);
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
//		            System.exit(0);
		    		if(mode==0){
		    			System.exit(0);
		    		}else if(mode==1){
		    			gh.exitDirectly();
		    			System.exit(0);
		    		}else if(mode==2){
		    			cp.exitDirectly();
		    			System.exit(0);
		    		}
		        }
		    }
		});
		port[0].addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if(port[0].getText().equals("port")){
					port[0].setText("");
					port[0].setForeground(Color.BLACK);
				}	
			}
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(port[0].getText().equals("")){
					port[0].setForeground(Color.LIGHT_GRAY);
					port[0].setText("port");
				}
			}
		});
		port[1].addFocusListener(new FocusListener(){
			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if(port[1].getText().equals("IP Adress")){
					port[1].setText("");
					port[1].setForeground(Color.BLACK);
				}	
			}
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(port[1].getText().equals("")){
					port[1].setForeground(Color.LIGHT_GRAY);
					port[1].setText("IP Adress");
				}
			}
		});
		chooseFile.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent ae){
				int returnVal = fc.showOpenDialog(WelcomeWindow.this);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	            	File file = fc.getSelectedFile();
					String filename = file.getName();
					fileName.setText(filename);
					fileName.setVisible(true);
					try {
						if (Game != null) {
							Game.myFile.input.close();
							Game = null;
						}
						Game = new Jeopardy(file);
						if(mode==1){
							gh.setGame(Game);
						}
						averageRating.setVisible(true);
						averageRating.setText( "average rating: "+ Constants.fraction(Game.myFile.totalrating, Game.myFile.totalpeople));

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (Game.hasFile()) {
							Game.closeFile();
						}
					}
	            }

			}
		});
		teamNumSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){

				JSlider source = (JSlider) e.getSource();
				if(mode==0){
					teamnumber = (int) source.getValue();
					for (int i = 0; i < 4; i++) {
						if (i < teamnumber) {
							askname[i].setVisible(true);
							name[i].setVisible(true);
						} else {
							askname[i].setVisible(false);
							
							name[i].setVisible(false);
//							System.out.println("inside the slider"+(name[0]==null));
						}
					}
				}
				//if on host mode
				else if(mode==1){
					teamnumber = (int) source.getValue();
					WelcomeWindow.this.gh.updateTeamToWait(teamnumber-1);
				}
			}		
		});
		startJ.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				//if under single mode
				if(mode==0){
					Game.TeamNumber = teamnumber;
					Game.Team = new String[teamnumber];
					for (int i = 0; i < Game.TeamNumber; i++) {
						Game.Team[i] = new String(name[i].getText());
					}
					mainGamePlay = new MainGamePlay(WelcomeWindow.this);
					mainGamePlay.setVisible(true);
					WelcomeWindow.this.setVisible(false);
					Game.PlayGame(mainGamePlay);	
				}//end of the mode if
				else if(mode==1){
					Game.TeamNumber = teamnumber;
					Game.Team = new String[teamnumber];
					if(quickplay.isSelected()){
						Game.initialanswered = 20;
					}
					try {
						int portnum = Integer.parseInt(port[0].getText());
						disableComponents();
						gh.startWaiting(portnum);
					} catch (Exception e) {
						Constants.alert("Please enter valid port");
						return;
					}
				}
				else if(mode==2){
					try{
						int portnum = Integer.parseInt(port[0].getText());
						String ip = port[1].getText();
						disableComponents();
						cp.prepare(portnum,ip);		
					}catch(IOException ioe){
						waitingl.setText("Unable to connect to host");
						System.out.println(ioe.getMessage());
						enableComponents();
						waitingl.setVisible(true);
					}catch (Exception e) {
						Constants.alert("Please enter valid port");
						return;
					}
					
				}
			}
		});
		
		clearChoice.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				playmode[0].doClick();
				reset();
			}	
		});
		//log out of the program
		logout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (mode == 1) {
					gh.serverLogout();
					try {
						gh.ss.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						Util.ps("problem when closing the socket");
					}
					WelcomeWindow.this.gh = null;
				} else if (mode == 2) {
					cp.userLogout();
					WelcomeWindow.this.cp = null;
				}
				LoginWindow login = new LoginWindow();
				login.setVisible(true);
				dispose();
			}
		});
		//exit program
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if(mode==0){
					System.exit(0);
				}else if(mode==1){
					gh.exitDirectly();
					System.exit(0);
				}else if(mode==2){
					cp.exitDirectly();
					System.exit(0);
				}
			}
		});
		//only five questions need to be answered
		quickplay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				answered = 20;
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
		playmode[0].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				singlePlay();
			}
		});
		playmode[1].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				hostGame();
			}
		});
		playmode[2].addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				joinGame();
			}
		});
		
		
	}
	//input a new file
	
	public void reset(){
		this.cp = null;
		this.gh = null;
		mode=0;
		teamNumSlider.setValue(1);
		for (int i = 0; i < 4; i++) {
			name[i].setText("");
			if (i > 0)
				name[i].setVisible(false);
		}
		averageRating.setText("");
		averageRating.setVisible(false);
		fileName.setVisible(false);
		Game = null;
	}

	private void singlePlay(){
		gh = null;
		cp = null;
		mode = 0;
		port[0].setVisible(false);
		port[0].setForeground(Color.LIGHT_GRAY);
		port[0].setText("port");
		port[1].setVisible(false);
		port[1].setText("IP Adress");
		port[1].setForeground(Color.LIGHT_GRAY);
		waitingl.setVisible(false);
		waitingl.setText("");
		longSlider();
		visibleLabels(true);
		hideOther(false);
	}
	private void hostGame(){
		mode = 1;
		this.gh = new GameHost(this);
		cp = null;
		port[0].setVisible(true);
		port[1].setVisible(false);
		waitingl.setVisible(false);
		waitingl.setText("");
		shortSlider();
		visibleLabels(true);
		hideOther(true);
		
	}
	private void joinGame(){
		mode = 2;
		this.cp = new ClientPlayer(this);
		gh = null;
		this.startJ.setEnabled(false);
		port[0].setVisible(true);
		port[1].setVisible(true);
		waitingl.setText("");
		waitingl.setVisible(false);
		teamNumSlider.setVisible(false);
		quickplay.setVisible(false);
		hideOther(true);
		visibleLabels(false);
	}
	
	private void hideOther(boolean b){
		if(!b){
			name[0].setText("");
			for (int i = 0; i < 4; i++) {
				if (i < teamnumber) {
					askname[i].setVisible(true);
					name[i].setVisible(true);
				} else {
					askname[i].setVisible(false);
					name[i].setVisible(false);
				}
			}
		}else{
			for(int i = 1; i<4;i++){
				askname[i].setVisible(false);
				name[i].setVisible(false);
			}
			name[0].setText(username);
		}
		
	}
	private void visibleLabels(boolean b){
		this.teamNum.setVisible(b);
		this.teamNumSlider.setVisible(b);
		this.gameFile.setVisible(b);
		this.chooseFile.setVisible(b);
		this.averageRating.setVisible(b);
		this.quickplay.setVisible(b);
		this.quickplay.setEnabled(b);
		if (!b)this.startJ.setText("Join Game");
		else startJ.setText("Start Jeopardy");
	}
	private void longSlider(){
		teamNumSlider.setMinimum(1);
		teamNumSlider.setValue(1);
		this.teamNumSlider.revalidate();
		this.teamNumSlider.repaint();
		teamNumSlider.setVisible(true);
//		System.out.println("short slider");
	}
	private void shortSlider(){
		teamNumSlider.setMinimum(2);
		teamNumSlider.setValue(2);
		this.teamNumSlider.revalidate();
		this.teamNumSlider.repaint();
		teamNumSlider.setVisible(true);
//		System.out.println("short slider");
	}
	public void adjust(){
		w = this.getWidth();
		h = this.getHeight();
		cap.setFont(Constants.setfont("big", w));
		subcap.setFont(Constants.setfont("small", w));
		teamNum.setFont(Constants.setfont("small", w));
		gameFile.setFont(Constants.setfont("small", w));
		fileName.setFont(Constants.setfont("small", w));
		averageRating.setFont(Constants.setfont("small", w));
		chooseFile.setPreferredSize(Constants.setsize(18, 3, w, h));
		chooseFile.setFont(Constants.setfont("small", w));
		startJ.setPreferredSize(Constants.setsize(22, 10, w, h));
		startJ.setFont(Constants.setfont("small", w));
		clearChoice.setPreferredSize(Constants.setsize(22, 10, w, h));
		clearChoice.setFont(Constants.setfont("small", w));
		logout.setPreferredSize(Constants.setsize(22, 10, w, h));
		logout.setFont(Constants.setfont("small", w));
		exit.setPreferredSize(Constants.setsize(22, 10, w, h));
		exit.setFont(Constants.setfont("small", w));
		for(int i = 0; i<2; i++){
			int index = 0;
			for(int j = 0; j <2; j++){
				index = 2*j+i;
				askname[index].setPreferredSize(new Dimension(w/10,h/10));
				askname[index].setBorder(BorderFactory.createMatteBorder(h/50, w/19, 0, w/19, Constants.deepblue));
				name[index].setPreferredSize(new Dimension(w/10,h/10));
				name[index].setBorder(BorderFactory.createMatteBorder(h/100, w/19, h/19, w/19, Constants.deepblue));
				
			}
		}
	}
	private boolean eligibleToStart(){
		if(mode==0){
			if(Game ==null) return false;
			for(int i = 0; i < teamnumber; i++){
//				System.out.println(name[0]==null);
				if(name[i].getText().equals("")){
					return false;
				}
			}
		}
		else if(mode==1){
			if(Game ==null) return false;
			if(name[0].getText().equals(""))return false;
			if(port[0].getText().equals(""))return false;
			if(this.port[0].getForeground()==Color.LIGHT_GRAY)return false;
		}
		else if(mode==2){
			if(port[0].getText().equals(""))return false;
			if(port[1].getText().equals(""))return false;
			if(this.port[0].getForeground()==Color.LIGHT_GRAY)return false;
			if(this.port[1].getForeground()==Color.LIGHT_GRAY)return false;
			if(name[0].getText().equals(""))return false;
		}
		return true;
	}
	private void enableComponents(){
		for(int i = 0; i<3; i++){
			if(i<2)this.port[i].setEnabled(true);
			this.playmode[i].setEnabled(true);
		}
		this.name[0].setEnabled(true);
		waitingl.setVisible(false);
		isWaiting = false;
		this.startJ.setEnabled(true);
		this.clearChoice.setEnabled(true);
		Util.unfill(port[0],"port");
		Util.unfill(port[1],"IP Adress");
		if(mode==1){
			this.quickplay.setEnabled(true);
			this.teamNumSlider.setEnabled(true);
			this.chooseFile.setEnabled(true);
			waitingl.setText("Waiting for "+(this.teamnumber-1)+" other team to join");
		}
	}
	private void disableComponents(){
		if(mode==1){
			this.quickplay.setEnabled(false);
			this.teamNumSlider.setEnabled(false);
			this.chooseFile.setEnabled(false);
			waitingl.setText("Waiting for "+(this.teamnumber-1)+" other team to join");
		}
		for(int i = 0; i<3; i++){
			if(i<2)this.port[i].setEnabled(false);
			this.playmode[i].setEnabled(false);
		}
		this.name[0].setEnabled(false);
		waitingl.setVisible(true);
		isWaiting = true;
		this.startJ.setEnabled(false);
		this.clearChoice.setEnabled(false);
	}
	public void serverLogout(String s){

		cp=null;
		this.enableComponents();
		reset();
		this.playmode[0].doClick();
		this.waitingl.setText(s);
		this.waitingl.setVisible(true);
		this.setVisible(true);
		new Thread(this).run();
	}
	public void userlogout(String s){
		try {
			gh.ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Util.ps("error when closing socket");
		}
		gh = null;
		this.enableComponents();
		this.reset();
		this.playmode[0].doClick();
		this.waitingl.setText(s);
		this.waitingl.setVisible(true);
		this.setVisible(true);
		new Thread(this).run();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!isWaiting){
			if(eligibleToStart()){
				this.startJ.setEnabled(true);
				
			}else{
				this.startJ.setEnabled(false);
			}
			try {
				new Thread(this).sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}//End of the class