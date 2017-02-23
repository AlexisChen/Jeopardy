package gamePlay;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Utilities.AnimationLabel;
import client.ClientPlayer;
import constants.Constants;
import server.GameHost;

public class QuestionScreen extends JPanel{

	private static final long serialVersionUID = 1L;
	private String myName;
    private int myCategory, myValuePoint;
	private JLabel teamname, category, valuepoint,question;
	public JLabel format;
	public JTextField answer;
	private JButton submit;
	public JButton pass;
	private FinalJeopardy fj;
	private int initialTeam;//keep track of which team got the quesiton wrong and which team we are currently at
	private int w, h;
	private JPanel answerp;
	protected Jeopardy Game;
	protected JTextArea jta;
	protected JLabel[]vp;
	protected JPanel cards;
	private String[] qinfo = new String[3];
	private int id = -1;
	private GameHost gh;
	private ClientPlayer cp;
	
	//for assignment5
	public JLabel timerLabel;
	public AnimationLabel animeClock;
	private AnimationLabel animeFish;
//	private boolean showTimer = false;
	
	public QuestionScreen(networkFrame nf){
		this.cards = nf.cards;
		w = cards.getWidth();
		h = cards.getHeight(); 
//		showTimer = true;
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
	public void setInfo(String player,String category, String pv, String q){
		myName = player;
		qinfo[0] = category;
		qinfo[1] = pv;
		qinfo[2] = q;
	}
	//should call add events here
	public void setup(){
		initializeComponents();
		createGUI();
		addEvents();
	}

	public void enablesubmit(boolean enable) {
		this.submit.setEnabled(enable);
		this.answer.setEnabled(enable);
		this.answer.setFocusable(enable);
		animeClock.setVisible(false);
		teamname.setVisible(true);
		if(enable){
			this.format.setText("Answer within 20 seconds");
//			format.repaint();
//			format.revalidate();
			animeFish.setVisible(false);
		}
		//if not turn to answer
		else{
			animeFish.setVisible(true);
		}
	}
	
//	public void changeHead(){
//		animeClock.setVisible(false);
//		teamName.setVisible(true)
//	}

	public void addNetEvents(){
		submit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				submit.setEnabled(false);
				answer.setEnabled(false);
				if(id==0){
					gh.submit(0, answer.getText());
				}else if(id>0){
					cp.submit(answer.getText());
				}
			}
		});
		format.setVisible(true);
		pass.setText("Buss In");
		pass.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				pass.setEnabled(false);
				pass.setVisible(false);
				if(id==0){
					gh.buzzin(0);
				}else if(id>0){
					cp.buzzin();
				}
			}
		});
	}
	public void setTeamName(String s){
		this.teamname.setText(s);
	}
	public void setFormat(String s){
		this.format.setText(s);
	}

	public QuestionScreen( int c, int v, MainGamePlay mgp){
		this.Game = mgp.Game;
		this.jta = mgp.commandWindow;
		this.vp = mgp.vp;
		this.cards = mgp.cards;
		w = cards.getWidth();
		h = cards.getHeight(); 
		Game.currentQuestion = new String(Game.myFile.Question[c*5+v][3]);
		myName = new String(Game.Team[Game.CurrentTeam]);
		myCategory = c;
		myValuePoint = v;	
		qinfo[0] = new String(Game.myFile.Category[myCategory]);
		qinfo[1] = new String(Game.myFile.PointValue[myValuePoint]);
		qinfo[2] = new String(Game.myFile.Question[c*5+v][3]);
		setup();
	}
	
	private void initializeComponents(){
		timerLabel = new JLabel("", SwingConstants.CENTER);
		animeClock = new AnimationLabel("clock");
		animeClock.set(40,40);
		animeFish = new AnimationLabel("fish");
		animeFish.set(80, 80);
		teamname = new JLabel(myName, SwingConstants.CENTER);
		category = new JLabel(qinfo[0], SwingConstants.CENTER);
		valuepoint = new JLabel("$"+qinfo[1], SwingConstants.CENTER);
		question = new JLabel("<html>"+qinfo[2]+"</html>");		
		format = new JLabel("", SwingConstants.CENTER);
		answer = new JTextField();
		submit = new JButton("Submit Answer");
		pass = new JButton("Pass");
		answerp = new JPanel(new BorderLayout());
		fj = null;
	}
	private void createGUI(){
		setBackground(Constants.darkgray);
		setOpaque(true);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 0;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridy = 0;
		gbc.weighty = 0.1;
		JPanel heading = new JPanel(new GridLayout(1,5));
		heading.setBackground(Constants.deepblue);
		heading.setOpaque(true);
		teamname.setForeground(Color.WHITE);
		teamname.setFont(Constants.setfont("small", w));
		category.setForeground(Color.WHITE);
		category.setFont(Constants.setfont("small", w));
		valuepoint.setForeground(Color.WHITE);
		valuepoint.setFont(Constants.setfont("small", w));
//		timerLabel.setVisible(false);
		timerLabel.setForeground(Color.BLUE);
		timerLabel.setFont(Constants.setfont("small", w));
		animeClock.setVisible(false);
		heading.add(timerLabel);
		heading.add(animeClock);
		heading.add(teamname);
		heading.add(category);
		heading.add(valuepoint);
		add(heading, gbc);
		
		gbc.gridy = 1;
		JPanel formatP = new JPanel(new BorderLayout());
		formatP.setBackground(Color.darkGray);
		formatP.setOpaque(true);
		format.setBackground(Constants.darkgray);
		format.setOpaque(true);
		format.setForeground(Color.WHITE);
		format.setVisible(false);
		formatP.add(format, BorderLayout.CENTER);
		animeFish.setVisible(false);
		formatP.add(animeFish, BorderLayout.WEST);
		add(formatP, gbc);
		
		gbc.gridy = 2;
		gbc.weighty = 0.6;
		question.setForeground(Color.BLACK);
		question.setBackground(Constants.lightblue);
		question.setOpaque(true);
		question.setVerticalAlignment(JLabel.TOP);
		question.setBorder(Constants.setborder(10, 1, w, h, Constants.darkgray));
		question.setFont(Constants.setfont("middle", w));
		add(question,gbc);
		
		gbc.gridy = 3;
		gbc.weighty = 0.1;
		answerp.setBackground(Constants.darkgray);
		answerp.setOpaque(true);
		answerp.setBorder(Constants.setborder(10, 1, w, h, Constants.darkgray));
		answerp.add(answer,BorderLayout.CENTER);
		answer.setPreferredSize(Constants.setsize(60, 10, w, h));
		answerp.add(submit, BorderLayout.EAST);
		add(answerp, gbc);

		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.NONE;
		pass.setBackground(Color.WHITE);
		pass.setOpaque(true);
		pass.setBorder(BorderFactory.createEmptyBorder());
		pass.setVisible(false);
		add(pass, gbc);	
	}
	
	public void addEvents(){

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
		answer.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				pass.setEnabled(false);
				pass.setBackground(Color.LIGHT_GRAY);
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
		if(id==-1){
			addNonNetEvents();
		}else{
			addNetEvents();
		}
	}
	
	private void addNonNetEvents(){
		submit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String ans = new String( answer.getText());
				if(!pass.isVisible()) initialTeam = Game.CurrentTeam;	
				int nowTeam = Game.CurrentTeam;
				int temp = Game.AnswerQuestion(myCategory, myValuePoint,ans);
				//if the team answered the question correctly
				if(temp==1){
					
					CardLayout cl = (CardLayout)(cards.getLayout());
					if(Game.answered ==25){
						Game.checkfj();
						if(Game.fjteam==0){
							Game.displayResults();
						}else{
							Game.CurrentTeam = nowTeam;
							fj = new FinalJeopardy(QuestionScreen.this);
							cards.add(fj, "FJ");
							cl = (CardLayout)(cards.getLayout());
							cl.show(cards, "FJ");
						}
					}else{
						if(nowTeam!=initialTeam)Game.CurrentTeam = nowTeam;
						jta.append('\n' + "Now it's " + Game.Team[Game.CurrentTeam] + "'s turn! Please choose a question.");
						cl.show(cards, "MGP");
					}	
					//if the team answered the question incorrectly
				}else if(temp ==-1){
					answer.setText("");
					if(Game.CurrentTeam==initialTeam){
						jta.append('\n' + "None of the teams were able to get the question right!"+
					"The answer is: "+Game.myFile.Question[myCategory*5+myValuePoint][4]);
						CardLayout cl = (CardLayout)(cards.getLayout());
						if(Game.answered ==25){
							Game.checkfj();
							if(Game.fjteam==0){
								Game.displayResults();
							}else{
								fj = new FinalJeopardy(QuestionScreen.this);
								cards.add(fj, "FJ");
								cl = (CardLayout)(cards.getLayout());
								cl.show(cards, "FJ");
							}
						}else{
							jta.append('\n' + "Now it's " + Game.Team[Game.CurrentTeam] + "'s turn! Please choose a question.");
							cl.show(cards, "MGP");
						}		
					}else{
						pass.setEnabled(true);
						pass.setBackground(Color.WHITE);

						teamname.setText(Game.Team[Game.CurrentTeam]);
						format.setText("It's Team "+Game.Team[Game.CurrentTeam]+"'s turn to try to answer the question");
						jta.append('\n' + "It's Team "+Game.Team[Game.CurrentTeam]+"'s turn to try to answer the question");
						if(!format.isVisible())format.setVisible(true);
						if(!pass.isVisible())pass.setVisible(true);
					}
					
					//if the team answered in incorrect form
				}else if(temp ==-2){
					jta.append('\n' + "Team " + Game.Team[Game.CurrentTeam] + " had a badly formatted answer");
					jta.append('\n' + "They will get a second chance to answer");
					if(!format.isVisible()){
						format.setText("Remember to pose your answer as a question");
						format.setVisible(true);
					}

				}
			}
		});
		pass.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				jta.append('\n' + Game.Team[Game.CurrentTeam]+" choose to pass the question.");
				Game.CurrentTeam =( Game.CurrentTeam+1)%Game.TeamNumber;
				
				answer.setText("");
				if(Game.CurrentTeam==initialTeam){
					jta.append('\n' + "None of the teams were able to get the question right!"+
							"The answer is: "+Game.myFile.Question[myCategory*5+myValuePoint][4]);
					CardLayout cl = (CardLayout)(cards.getLayout());
					if(Game.answered ==25){
						Game.checkfj();
						if(Game.fjteam==0){
							Game.displayResults();
						}else{
							fj = new FinalJeopardy(QuestionScreen.this);
							cards.add(fj, "FJ");
							cl = (CardLayout)(cards.getLayout());
							cl.show(cards, "FJ");
						}
					}else{
						cl.show(cards, "MGP");
					}		
				}else{
					jta.append('\n' + "Now it's "+Game.Team[Game.CurrentTeam]+"'s turn to answer the question.");
					teamname.setText(Game.Team[Game.CurrentTeam]);
					format.setText("It's Team "+Game.Team[Game.CurrentTeam]+"'s turn to try to answer the question");
				}
			}
		});
	}
	public void enableBuzzin(){
		this.animeClock.setVisible(true);
		this.teamname.setVisible(false);
		if(pass.isEnabled()){
			pass.setVisible(true);
			format.setText("buzzin to answer");
		}else{
			format.setText("You can not buzz in again. "
					+ "Waiting for another team to buzz in.");
			answer.setFocusable(false);
			answer.setEnabled(false);
			submit.setEnabled(false);
		}
		
//		pass.setEnabled(true);
		
	}
	public void hideBuzzin(String s){
			format.setText(s+" buzzed in to answer");
			pass.setVisible(false);
			animeClock.setVisible(false);
			teamname.setText(s);
			teamname.setVisible(true);
	}
	public void enableAnswer(){
		format.setText("Please answer the question");
		answer.setFocusable(true);
		answer.setEnabled(true);
		submit.setEnabled(true);
		pass.setVisible(false);
		animeClock.setVisible(false);
		teamname.setVisible(true);;
	}
	private void adjust(){
		 w = this.getWidth();
		h = this.getHeight();
		teamname.setFont(Constants.setfont("small", w));
		category.setFont(Constants.setfont("small", w));
		valuepoint.setFont(Constants.setfont("small", w));
		question.setBorder(Constants.setborder(10, 1, w, h, Constants.darkgray));
		question.setFont(Constants.setfont("middle", w));
		answerp.setBorder(Constants.setborder(10, 1, w, h, Constants.darkgray));
		answer.setPreferredSize(Constants.setsize(60, 10, w, h));

	}

}
