package gamePlay;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import server.SQLDriver;

public class LoginWindow extends JFrame implements Runnable{
	private static final long serialVersionUID = 1L;
	
	private JLabel h1,h2,h3;
	private JTextField username,password;
	private JButton login, createAccount;
	private Color lightblue;
	private String currentUser, currentPsw;
	private SQLDriver mDriver;
	private boolean stop = false;
	
	public LoginWindow(){
		super("");
		initializeComponents();
		createGUI();
		addEvents();
		new Thread(this).start();
	}
	public void initializeComponents(){
		
		h1 = new JLabel("login or create an account to play",SwingConstants.CENTER);
		h2 = new JLabel("Jeopardy!",SwingConstants.CENTER);
		h3 = new JLabel("",SwingConstants.CENTER);
		username = new JTextField();
		password = new JTextField();
		login =  new JButton("Login");
		createAccount = new JButton("Create Account");
		lightblue = new Color(0x66b2ff);
		mDriver = new SQLDriver();
		mDriver.connect();
	}
	public void createGUI(){
		
		setLocation(300,200);
		setSize(600,600);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		getContentPane().setBackground( lightblue );
		int w = this.getWidth();
		int h = this.getHeight();
		Font font1 = new Font("Times New Roman", Font.BOLD, w/24);
		Font font2 = new Font("Times New Roman", Font.BOLD, w/17);
		Font font3 = new Font("Times New Roman", Font.BOLD, w/30);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		//gbc.ipady = 10;
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		h1.setFont(font1);
		h1.setBackground(lightblue);
		h1.setOpaque(true);
		h1.setBorder(BorderFactory.createMatteBorder(h/30, 0, 0, 0, lightblue));
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(h1,gbc);	
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.ipady = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		h2.setFont(font2);
		h2.setBackground(lightblue);
		h2.setOpaque(true);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(h2,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.ipady = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		h3.setFont(font3);
		h3.setBackground(lightblue);
		h3.setOpaque(true);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(h3,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		username.setBorder(BorderFactory.createMatteBorder(h/20, w/6, h/20, w/6, lightblue));
		username.setForeground(Color.LIGHT_GRAY);
		username.setText("username");
		add(username,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.weightx = 1;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		password.setForeground(Color.LIGHT_GRAY);
		password.setText("password");
		password.setBorder(BorderFactory.createMatteBorder(h/20, w/6, h/20, w/6, lightblue));
		add(password,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.weightx = 0.5;
		gbc.weighty = 0.1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTH;
		JPanel bp = new JPanel();
		login.setForeground(Color.LIGHT_GRAY);
		login.setBackground(Color.DARK_GRAY);
		login.setOpaque(true);
		login.setBorder(null);
		login.setEnabled(false);
		login.setPreferredSize(new Dimension(w*3/9,h/10));
		bp.add(login);
		createAccount.setForeground(Color.LIGHT_GRAY);
		createAccount.setBackground(Color.DARK_GRAY);
		createAccount.setOpaque(true);
		createAccount.setBorder(null);
		createAccount.setEnabled(false);
		createAccount.setPreferredSize(new Dimension(w*3/9,h/10));
		bp.add(createAccount);
		bp.setBackground(lightblue);
		add(bp,gbc);
		
	}
	public void addEvents(){
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	stop = true;
		    	System.exit(0);
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
		login.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				currentUser = username.getText();
				currentPsw = password.getText();	
				if(!mDriver.getPassword(currentUser).equals(currentPsw)){
					showNoCombinition();
				}else{
					startGame();
				}
			}
		});
		createAccount.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				currentUser = username.getText();
				currentPsw = password.getText();	
				if(!mDriver.getPassword(currentUser).equals("")){
					showUsernameTaken();
				}else{
					mDriver.add(currentUser,currentPsw);
					startGame();
				}
			}
		});
		username.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if(username.getText().equals("username")){
					username.setText("");
					username.setForeground(Color.BLACK);
				}	
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(username.getText().equals("")){
					username.setForeground(Color.LIGHT_GRAY);
					username.setText("username");
				}
			}
			
		});
		password.addFocusListener(new FocusListener(){

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				if (password.getText().equals("password")) {
					password.setText("");
					password.setForeground(Color.BLACK);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if(password.getText().equals("")){
					password.setForeground(Color.LIGHT_GRAY);
					password.setText("password");
				}
			}
			
		});
	}
	public void adjust(){
		int w = this.getWidth();
		int h = this.getHeight();
		int w1 = (int)(w*3/9);
		int hh = (int)(h/10);
		Dimension d1 = new Dimension(w1,hh);
		username.setBorder(BorderFactory.createMatteBorder(h/20, w/6, h/20, w/6, lightblue));
		password.setBorder(BorderFactory.createMatteBorder(h/20, w/6, h/20, w/6, lightblue));
		login.setPreferredSize(d1);
		createAccount.setPreferredSize(d1);
		Font font1 = new Font("Times New Roman", Font.BOLD, w/24);
		Font font2 = new Font("Times New Roman", Font.BOLD, w/17);
		Font font3 = new Font("Times New Roman", Font.BOLD, w/30);
		h1.setFont(font1);
		h1.setBorder(BorderFactory.createMatteBorder(h/30, 0, 0, 0, lightblue));
		h2.setFont(font2);
		h3.setFont(font3);

	}
	public void showNoCombinition(){
		h3.setText("this username and password combinition does not exist");
		h3.setVisible(true);
	}
	
	public void showUsernameTaken(){
		h3.setText("this username already exists");
		h3.setVisible(true);
	}
	
	public void checkfilled(){
		if(!username.getText().isEmpty()&&!password.getText().isEmpty()
				&&username.getForeground()==Color.BLACK
				&&password.getForeground()==Color.BLACK){
			login.setEnabled(true);
			createAccount.setEnabled(true);
		}else{
			login.setEnabled(false);
			createAccount.setEnabled(false);
		}
	}
	private void startGame(){
		WelcomeWindow firstwindow = new WelcomeWindow(this.currentUser);
		firstwindow.setVisible(true);
		stop = true;
		dispose();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!stop){
			checkfilled();
		}
	}
	
}
