package gamePlay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import server.GameHost;

public class Jeopardy implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int initialTeam = 0;
	protected int TeamNumber = 1;// keep track of the number of the team
	public String[] Team;// keep track of team names
	protected int[] TeamPoint;// keep track of the point each team has
	protected int[] teamBet;// keep track of whether each team get Final
							// Jeopardy correct
	public int CurrentTeam = -1;// keep track of which team if answering
	public ReadFile myFile;
	public int currentC, currentV;
	protected String currentQuestion;
	protected int initialanswered = 0, answeredfj = 0;
	public int answered = 0;
	private String[] myAns;
	private boolean[] chance;
	public boolean[] tofj;// to see which team is qualified for fj;
	public int fjteam = 0;// number of team qualified for fj;
	public int alreadybet = 0;	
//	private MainGamePlay mgp;
	private JTextArea jta;
	private JLabel[]vp;
	private JPanel cards;
	
	private GameHost gh;
	private int mode;
	public Jeopardy(File file ) throws FileNotFoundException {
		myFile = new ReadFile(file);
		myFile.Parse();
	}

	public boolean hasFile() {
		if (myFile.input == null)
			return false;
		else
			return true;
	}

	public void closeFile() {
		myFile.input.close();
	}
	public int getTeamPoint(int i ){
		return this.TeamPoint[i];
	}
	public void setTeamNumber(int i ){
		this.TeamNumber = i;
	}
	public int getTeamNumber(){
		return this.TeamNumber;
	}
	public void setGameHost(GameHost gh){
		this.gh = gh;
	}
	public void PlayGame(MainGamePlay mgp) {
//		this.mgp = mgp;
		this.jta = mgp.commandWindow;
		this.vp = mgp.vp;
		this.cards = mgp.cards;
		initialize();
	}
	public void PlayGame(networkFrame nf){
		this.jta = nf.commandWindow;
		this.vp = nf.vpboard.vp;
		this.cards = nf.cards;
		this.mode = 0;
		initialize();
	}
	private void initialize(){
		chance = new boolean[TeamNumber];
		myAns = new String[TeamNumber];
		TeamPoint = new int[TeamNumber];
		teamBet = new int[TeamNumber];
		tofj = new boolean[TeamNumber];
		for (int i = 0; i < TeamNumber; i++) {
			myAns[i] = new String("");
			TeamPoint[i] = 0;
			teamBet[i] = 0;
			chance[i] = true;
			tofj[i] = false;
			updatevp();
		}
		updatevp();
		Random rand = new Random();
		answered = initialanswered;
		initialTeam = rand.nextInt(TeamNumber);
		CurrentTeam = initialTeam;
		this.gh.setMode(CurrentTeam);
		gh.initialTeam = CurrentTeam;
		jta.append("Welcome to Jeopardy!");
		jta.append('\n' + "The team to go first will be " + Team[CurrentTeam]);
	}
	private void updatevp() {
		for (int i = 0; i < TeamNumber; i++) {
			vp[i].setText("$" + TeamPoint[i]);
		}
		gh.updateVP(TeamPoint);
	}
public boolean formatchance = true;
	// make each team answer the question
	public int AnswerQuestion(int c, int v,  String answer) {
		int outcome = 0; //keep track of the answer's status
		String current = new String(answer);
		// check if the answer is in question mode
		if (!isQuestion(current)) {
			if (formatchance) {
				formatchance = false;
				return -2;//if the answer is badly formatted for the first time
			} else {
				formatchance = true;
				int num = Integer.parseInt(myFile.PointValue[v]);
				doIncorrect(CurrentTeam, num, false, jta, vp);
				outcome = -1;//if the team ansewred the question incorrectly
			}
		} else {
			outcome = checkAns(CurrentTeam, c * 5 + v, current, jta, vp);
		}
		CurrentTeam = (CurrentTeam + 1) % TeamNumber;
		updatevp();
		return outcome;
	}

	public void setbet(int player, int bet) {
		alreadybet++;
		teamBet[player] = bet;
	}

	public void checkfj() {
		fjteam = 0;
		for (int i = 0; i < TeamNumber; i++) {
			if (TeamPoint[i] > 0) {
				tofj[i] = true;
				fjteam++;
			}
		}
	}

	// answer the final jeopardy question
	public int answerFJQuestion(int player, String ans) {
		answeredfj++;
		if (!isQuestion(ans)) {
			doIncorrect(player, teamBet[player], true, jta, vp);
		} else {
			checkAns(player, 25, ans, jta, vp);
		}
		if (answeredfj == fjteam) {
			displayResults();
			return 1;//if all team have answered the question
		}
		return 0;//if some team haven't answer the final question yet.
	}

	// check if the answer is correct
	protected int checkAns(int player, int question, String answer, JTextArea jta, JLabel[] vp) {
		int outcome = 0; 
		if (question == 25) {

			if (answer.toLowerCase().contains(myFile.Question[question][3].toLowerCase())) {

				doCorrect(player, teamBet[player], true, jta, vp);
				outcome = 1;
			} else {
				doIncorrect(player, teamBet[player], true, jta, vp);
				outcome = -1;
			}
		} else if (answer.toLowerCase().contains(myFile.Question[question][4].toLowerCase())) {
			int temp = Integer.parseInt(myFile.Question[question][2]);
			doCorrect(player, temp, false, jta, vp);
			outcome = 1;
			
		} else {
			int temp = Integer.parseInt(myFile.Question[question][2]);
			doIncorrect(player, temp, false, jta, vp);
			outcome = -1;
		}
		return outcome;
	}

	// operate on the point values if the answer is correct
	protected void doCorrect(int player, int point, boolean fj, JTextArea jta, JLabel[] vp) {
		if (fj) {
			TeamPoint[player] += point;
			teamBet[player] = 1;
		} else {
			jta.append(
					'\n' + Team[player] + ", you go the answer right! " + "$" + point + " will be added to your score");
			TeamPoint[player] += point;
			vp[player].setText("$" + TeamPoint[player]);
		}
	}

	// operate on the point values if the answer is incorrect
	protected void doIncorrect(int player, int point, boolean fj, JTextArea jta, JLabel[] vp) {
		if (fj) {
			TeamPoint[player] -= point;
			teamBet[player] = -1;
		} else {
			jta.append('\n' + Team[player] + ", you got the answer wrong... " + "$" + point
					+ " will be deducted your score");
			TeamPoint[player] -= point;
			vp[player].setText("$" + TeamPoint[player]);
		}
	}

	public void displayResults() {
		if (fjteam == 0) {
			jta.append('\n' + "No team qualified for Final Jeopardy");
			WinnerWindow ww = new WinnerWindow("There is no winner!",cards, myFile);
			if(mode==0){
				this.gh.setWinnerW(ww);
			}else{
				ww.setVisible(true);
			}
			return;
		}
		if(fjteam!=0){
			jta.append('\n' + "The answer is: "+ myFile.Question[25][3]);
		}
		int maxpoint = TeamPoint[0];
		int maxindex = 0;
		for (int i = 0; i < TeamNumber; i++) {
			if (TeamPoint[i] > maxpoint) {
				maxpoint = TeamPoint[i];
				maxindex = i;
			}
			if (teamBet[i] == 1) {
				jta.append('\n' + "Team " + Team[i] + ", your answer is correct");
			}
			if (teamBet[i] == -1) {
				jta.append('\n' + "Team " + Team[i] + ", your answer is incorrect");
			}
		}
		jta.append('\n' + "And the winner is: ");
		checkfj();
		if(fjteam==0){
			WinnerWindow ww = new WinnerWindow("There is no winner", cards, myFile);
			if(mode==0){
				this.gh.setWinnerW(ww);
			}else{
				ww.setVisible(true);
			}
		}else if(tie()){
			WinnerWindow ww = new WinnerWindow("It's a tie!", cards, myFile);
			if(mode==0){
				this.gh.setWinnerW(ww);
			}else{
				ww.setVisible(true);
			}			
		}else{
			List<Integer> winners = new ArrayList<>();
			for (int i = maxindex; i < TeamNumber; i++) {
				if (TeamPoint[i] == maxpoint)
					winners.add(i);
			}
			
			 String winner = new String("");
			for (int i = 0; i < winners.size(); i++) {
				 winner+=(" "+Team[winners.get(i)]);
				jta.append('\n' + Team[winners.get(i)]);
			}
			WinnerWindow ww = new WinnerWindow(winner, cards, myFile);
			if(mode==0){
				this.gh.setWinnerW(ww);
			}else{
				ww.setVisible(true);
			}
		}
		updatevp();
	}

	// restart if the player type "restart"
	public void replay() {
		// reset the variables
		for (int i = 0; i < TeamNumber; i++) {
			myAns[i] = new String("");
			TeamPoint[i] = 0;
			teamBet[i] = 0;
			chance[i] = true;
			tofj[i] = false;
		}
		answered = initialanswered;
		answeredfj = 0;
		fjteam = 0;
		updatevp();
		Random rand = new Random();
		initialTeam = rand.nextInt(TeamNumber);
		CurrentTeam = initialTeam;
		jta.setText("Welcome to Jeopardy!");
		jta.append('\n' + "The team to go first will be " + Team[CurrentTeam]);
	}

	// check if an answer is posed as a question
	protected boolean isQuestion(String Ans) {
		String answer = Ans.toLowerCase();
		String[]word = answer.split(" ", 3);
		
		if(word.length<2) return false;
		if((word[0].equals("what")||word[0].equals("who")||word[0].equals("when")||word[0].equals("where"))&&(
				word[1].equals("is")||word[1].equals("are"))){
			return true;
		}
		return false;
	}
	protected boolean tie(){
		for(int i = 0; i < TeamNumber; i++){
			for(int j = i; j< TeamNumber; j++){
				if(TeamPoint[i]!=TeamPoint[j]){
					return false;
				}
			}
		}
		return true;
	}
	
}//end of class