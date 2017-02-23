package gamePlay;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FinalJeopardy extends JPanel {

	private static final long serialVersionUID = 1L;
	private JButton[] set,submit;
	private JLabel heading, fjanswer;
	private JLabel[] name, bet;
	private JTextField[] ans;
	private JSlider[] slider;
	private Color lightBlue = new Color(0x66b2ff);
	private Color deepBlue = new Color(0x000099);
	private Color darkGray = new Color(0x404040);
	//fprivate JPanel cards;
	private JPanel parent;
	private Jeopardy Game;
	private JTextArea jta;
	private JLabel[]vp;
	
	public FinalJeopardy(QuestionScreen qssn){
		this.parent = qssn.cards;
		this.Game = qssn.Game;
		this.jta = qssn.jta;
		this.vp = qssn.vp;		
		initializeComponents(Game);
		createGUI(Game, jta);
		addEvents(parent, Game, jta, vp);
	}
	private void initializeComponents(Jeopardy Game){
		String setbet = new String("Set Bet");
		String submitanswer = new String("Submit Answer");
		set = new JButton[4];
		submit = new JButton[4];
		ans = new JTextField[4];
		name = new JLabel[4];
		slider = new JSlider[4];
		bet = new JLabel[4];
		for(int i = 0; i <4; i++){
			name[i] = new JLabel("",SwingConstants.CENTER);
			name[i].setVisible(false);
			slider[i] = new JSlider();
			slider[i].setVisible(false);
			slider[i].setEnabled(false);
			bet[i] = new JLabel("$0", SwingConstants.CENTER);
			bet[i].setVisible(false);
			set[i]= new JButton(setbet);
			set[i].setEnabled(false);
			set[i].setVisible(false);
			ans[i] = new JTextField();
			ans[i].setEnabled(false);
			ans[i].setVisible(false);
			submit[i]= new JButton(submitanswer);
			chosen(submit[i]);
			submit[i].setVisible(false);
		}
		heading = new JLabel("Final Jeopardy Round",SwingConstants.CENTER);
		fjanswer = new JLabel("And the question is...",SwingConstants.CENTER);
		
		
	}
	private void createGUI(Jeopardy Game, JTextArea jta){
		
		setBackground(darkGray);
		setLayout(new BorderLayout());
		jta.append('\n'
				+"Now that all the quesitons have been chosen, "
				+ "it's time for Final Jeopardy!");
		jta.append('\n'+"Team "+Game.Team[0]+", please select a dollar amount "
				+ "for your total that you would like to bet: ");
		heading.setBackground(deepBlue);
		heading.setOpaque(true);
		heading.setForeground(Color.WHITE);
		heading.setPreferredSize(new Dimension(10,50));
		heading.setFont(new Font("Times New Roman", Font.BOLD,24));
		heading.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, darkGray));
		add(heading,BorderLayout.NORTH);
		
		JPanel betp = new JPanel(new GridLayout(5,1));
		betp.setBackground(deepBlue);
		betp.setOpaque(true);
		JPanel[]betbar = new JPanel[4];
		for(int i = 0; i <4;i++){
			betbar[i] = new JPanel(new BorderLayout());
			betbar[i] = new JPanel(new BorderLayout());
			betbar[i].setBackground(darkGray);
			betbar[i].setOpaque(true);
			betbar[i].add(name[i],BorderLayout.WEST);
				
			JPanel rightp = new JPanel(new BorderLayout());
			betbar[i].add(slider[i],BorderLayout.CENTER);
			rightp.add(bet[i], BorderLayout.WEST);
			rightp.add(set[i],BorderLayout.EAST);
			betbar[i].add(rightp,BorderLayout.EAST);
			betbar[i].setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, darkGray));
			betp.add(betbar[i]);
		}
		fjanswer.setBackground(lightBlue);
		fjanswer.setOpaque(true);
		fjanswer.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, darkGray));
		fjanswer.setPreferredSize(new Dimension(10,50));
		betp.add(fjanswer,BorderLayout.SOUTH);
		add(betp, BorderLayout.CENTER);
		
		JPanel answerp = new JPanel(new GridLayout(2,2));
		answerp.setBackground(darkGray);
		answerp.setOpaque(true);
		answerp.setPreferredSize(new Dimension(10,100));
		JPanel[] t = new JPanel[4];
		for(int i = 0; i < 4;i++){
			t[i] = new JPanel(new BorderLayout());
			t[i].setBackground(darkGray);
			t[i].setOpaque(true);
			t[i].add(ans[i], BorderLayout.CENTER);
			submit[i].setPreferredSize(new Dimension(100,1));
			t[i].add(submit[i],BorderLayout.EAST);
			answerp.add(t[i]);
		}
		add(answerp, BorderLayout.SOUTH);
		//set the values and show the component
		for(int i = 0; i <Game.TeamNumber; i++){
			
			name[i].setForeground(Color.WHITE);
			name[i].setBackground(darkGray);
			name[i].setOpaque(true);
			name[i].setPreferredSize(new Dimension(100,1));
			name[i].setText(Game.Team[i]);
			name[i].setVisible(true);
			slider[i].setMinimum(1);
			slider[i].setMaximum(Game.TeamPoint[i]);
			slider[i].setPaintLabels(true);
			slider[i].setBackground(darkGray);
			slider[i].setOpaque(true);
			slider[i].setForeground(Color.WHITE);
			slider[i].setVisible(true);
			bet[i].setForeground(Color.WHITE);
			bet[i].setBackground(darkGray);
			bet[i].setOpaque(true);
			bet[i].setPreferredSize(new Dimension(100,10));
			bet[i].setVisible(true);
			set[i].setVisible(true);
			ans[i].setVisible(true);
			submit[i].setVisible(true);
			if(Game.tofj[i]==true){
				
				slider[i].setMajorTickSpacing(Game.TeamPoint[i]/10);
				slider[i].setMinorTickSpacing(1);	
				slider[i].setSnapToTicks(true);
				slider[i].setPaintTicks(true);				
				slider[i].setEnabled(true);
				ans[i].setText("Team"+ Game.Team[i]+", enter your answer");
				set[i].setEnabled(true);
				ans[i].setEnabled(true);
				chosen(submit[i]);
			}else{
				jta.append('\n'+ "Team "+Game.Team[i]+", you are not qualified for"
						+ "Final Jeopardy, good luck next time");
			}
		}
	}
	private void addEvents(JPanel cards, Jeopardy Game, JTextArea jta, JLabel[]vp){
		for(int i = 0 ; i <Game.TeamNumber; i++){
			slider[i].addChangeListener(new ChangeListener(){

				@Override
				public void stateChanged(ChangeEvent e) {
					// TODO Auto-generated method stub
					int index = 0;
					for(int j = 0; j <Game.TeamNumber; j++){
						if((JSlider)e.getSource()==slider[j]){
							index = j;
						}
					}
					JSlider source = (JSlider)e.getSource();
					if(!source.getValueIsAdjusting()){
						bet[index].setText("$"+(int)source.getValue());
					}
				}
				
			});
			set[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					chosen((JButton)ae.getSource());
					int index = 0;
					for(int j = 0; j <Game.TeamNumber; j++){
						if((JButton)ae.getSource()==set[j]){
							index = j;
						}
					}
					int mybet = slider[index].getValue();
					Game.setbet(index, mybet);
					jta.append('\n'
							+"Team "+Game.Team[index]+"bets $"+mybet);
					
					slider[index].setEnabled(false);
					if(Game.alreadybet==Game.fjteam){
						fjanswer.setText(Game.myFile.Question[25][2]);
						jta.append('\n'
								+"The final jeopardy question is: "+'\n'
								+Game.myFile.Question[25][2]);
						for(int j = 0; j <Game.TeamNumber; j++){
							if(Game.tofj[j]==true){
								unchoose(submit[j]);
								jta.append('\n'
									+"Team "+Game.Team[j]+", please enter your answer");
							}
						}
					}else{
						jta.append('\n'
								+"Team "+Game.Team[index+1]+", please select a dollar amount "
								+ "for your total that you would like to bet: ");
					}
					
				}
			});
			submit[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					chosen((JButton)ae.getSource());
					int index = 0;
					for(int j = 0; j <Game.TeamNumber; j++){
						if((JButton)ae.getSource()==submit[j]){
							index = j;
						}
					}
					chosen((JButton)ae.getSource());
					String answer = new String(ans[index].getText());
					Game.answerFJQuestion( index, answer);
				}
			});
		}
	}
	private void chosen(JButton b){
		b.setBackground(Color.LIGHT_GRAY);
		b.setEnabled(false);
	}
	private void unchoose(JButton b){
		b.setBackground(Color.WHITE);
		b.setEnabled(true);
	}
}
