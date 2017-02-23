package server;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import Utilities.JeopardyTimer;
import Utilities.Util;
import client.Message;
import constants.Constants;
import gamePlay.ButtonPanel;
import gamePlay.Jeopardy;
import gamePlay.PersonalFJ;
import gamePlay.QuestionScreen;
import gamePlay.VPPanel;
import gamePlay.WelcomeWindow;
import gamePlay.WinnerWindow;
import gamePlay.networkFrame;
import messages.MainScreenSetup;
import messages.ModeMessage;
import messages.QuestionInfo;
import messages.RatingInfo;
import messages.SetBet;
import messages.VP;

public class GameHost implements Runnable, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VPPanel vpboard;
	private JTextArea jta;
	private Jeopardy game;
	private String[] images = new String[3];
	
	
	public String selfName;
	protected int teamID;
	private int IDToAssign = 1;
	private int teamNumber;
	public int initialTeam;
	private int currentTeam;
	private boolean isfj = false;
	private int rated = 0;
	private int totalrate = 0;
	private boolean[] sent;
	private String msg;
//	private boolean choseBeforeTimeUp = false;
//	private boolean answeredBeforeTimeUp = false;
//	private int currentCategory;
	
	//for assignment 5
	private Timer myTimer;
	private JeopardyTimer jt;
	//if == 0: buttonPanel
	//if == 1: questionScreen
	private int timingMode;
			
	public ServerSocket ss;
	private Socket s= null;
	protected HostThread[] hostThreads;
	
	private WelcomeWindow ww;
	protected int teamToWait = 1;
	
	private networkFrame nf;
	private ButtonPanel bp;
	private QuestionScreen qs;
	private PersonalFJ pfj;
	private WinnerWindow winnerw;
	
	private Lock mLock = new ReentrantLock();
	private Condition nameFilled = mLock.newCondition();
	
	public GameHost(WelcomeWindow ww){
		this.ww = ww;	
		hostThreads = new HostThread[ww.teamnumber-1];
		this.vpboard = new VPPanel();
		this.jta = new JTextArea();

	}
	public void setGame(Jeopardy Game){
		this.game = Game;
		Util.ps("Game has been set up");
		game.setGameHost(this);
	}
	public void serverLogout(){
		for(HostThread st: hostThreads){
			if(st!=null){
				String s = new String("serverlogout");
				st.sendMessage(s);
			}
		}
	}
	public void startWaiting(int port){
		Util.ps("this is the size of array"+(ww.teamnumber-1));
		hostThreads = new HostThread[ww.teamnumber-1];
		this.teamToWait = ww.teamnumber-1;
		try {
			ss = new ServerSocket(port);
			ww.waitingl.setText("waiting for "+teamToWait+" other players to join");
			new Thread(this).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void updateTeamToWait(int n){
		teamToWait += n;
		ww.waitingl.setText("waiting for "+teamToWait+" other players to join");

		for(HostThread st: hostThreads){
			if(st!=null){
				if(st.id<0){
					Message m = new Message(0, "updateWaitingNumber");
					m.setWaiting(teamToWait);
					st.sendMessage(m);
				}
				else{
					this.removeThread(st.id);
				}
			}
		}
	}
	protected void nameFilled(){
		try{
			this.mLock.lock();
			this.nameFilled.signal();
		}finally{
			this.mLock.unlock();
		}
	}
	protected synchronized void startNetwork(){
		this.teamNumber = ww.teamnumber;
		this.sent = new boolean[teamNumber];
		for(int i = 0; i < teamNumber;i++){
			sent[i] = false;
		}
		isfj = false;
		rated = 0;
		totalrate = 0;
		IDToAssign = 1;
		this.msg = Constants.fraction(game.myFile.totalrating,game.myFile.totalpeople);
		Util.ps("this is the teamnumber"+ ww.teamnumber);
		game.Team[0] = this.ww.name[0].getText();
		this.selfName = game.Team[0];

		Util.ps("all filled");
		for(int i = 1; i<teamNumber;i++){
			game.Team[i] = new String(hostThreads[i-1].clientName);
		}
		images[0] = game.myFile.categoryImage;
		images[1] = game.myFile.enableImage;
		images[2] = game.myFile.disableImage;
		this.vpboard.setNames(game.Team);
		this.bp = new ButtonPanel();
		bp.setID(0);
		bp.setGamehost(this);
		bp.setImage(this.images);
		bp.setcnp(game.myFile.Category, game.myFile.PointValue);
		bp.setup();
		this.nf = new networkFrame(game.Team);
		nf.setId(0);
		nf.setGameHost(this);
		nf.setCW(this.jta);
		nf.setup();
		nf.setPanel(bp, "BP");
		nf.setVisible(true);
		ww.setVisible(false);
		Util.ps("ww should be invisible");
		
		
		MainScreenSetup mss = new MainScreenSetup();
		mss.names = game.Team;
		mss.images = this.images;
		mss.categories = this.game.myFile.Category;
		mss.pointvalues = this.game.myFile.PointValue;
		for(int i = 0; i<this.teamNumber-1; i++){	
			hostThreads[i].sendMessage(mss);
		}
		Util.ps("successfully send maingameplay on server side");
		game.PlayGame(nf);
		Util.ps("successfully update command window");
	}
	protected synchronized void removeThread(int id){
		hostThreads[id-1].over();
		Util.ps("successfully over the thread");
		hostThreads[id-1] = null;
		this.IDToAssign = id;
	}
	public void updateCW(String cw){
		for(int i = 0; i<this.teamNumber-1; i++){	
			hostThreads[i].sendMessage(cw);
		}
	}
	public void updateVP(int[] vp){
		Util.ps("prepare to update vp for all clients ");
		VP nvp = new VP();
		nvp.vp = new String[this.teamNumber];
		for(int i = 0; i < this.teamNumber; i++){
			nvp.vp[i] = new String("$"+vp[i]);
			Util.ps(nvp.vp[i]+"");
		}
		for(int i = 0; i<this.teamNumber-1; i++){	
			hostThreads[i].sendMessage(nvp);
		}
		Util.ps("successfully sent update messages");
	}
	public void setMode(int currentTeam){
		Util.ps("trying to set play mode for clients");
		this.currentTeam = currentTeam;
		if(currentTeam==0){
			//set itself to answering mode
			this.bp.setmode(true);
		}else{
			//set itself to waiting mode
			this.bp.setmode(false);
		}
		this.nf.animationOn(currentTeam);
		for(int i = 0; i<this.teamNumber-1; i++){	
			ModeMessage mm = new ModeMessage();
			mm.currentAnsweringTeam = currentTeam;
			if(i+1==this.currentTeam){
				mm.enable = true;
//				hostThreads[i].sendMessage(new Boolean(true));
			}else{
				mm.enable = false;
//				hostThreads[i].sendMessage(new Boolean(false));
			}
			hostThreads[i].sendMessage(mm);
		}
		Util.ps("successfully sent set play mode messages");
		//when choosing 
		timingMode = 0;
		startTiming(15,this.bp.timing);	
	}
	public void replayGame(){
		this.game.replay();
		String s = new String("replay");
		for(int i = 0; i<this.teamNumber-1; i++){	
			hostThreads[i].sendMessage(s);
		}
		bp.replay();
		nf.changePanel("BP");
	}
	public void exitDirectly(){
		for(HostThread st: hostThreads){
			if(st!=null){
				String s = new String("serverlogout");
				st.sendMessage(s);
			}
		}
	}
	public void exitThroughMenu(){
		createPop(0);
	}
	public void newFile(){
		createPop(0);
		ww.dispose();
	}
	public void logoutThroughMenu(){
		createPop(0);
		ww.dispose();
	}
	public void createPop(int id){
		String s = new String("pop");
		for(int i = 0; i<this.teamNumber-1; i++){	
			if(id!=i+1)hostThreads[i].sendMessage(s);
		}
		if(id!=0){
			String sss = new String("one of the players has exited, click ok to restart");
			if(JOptionPane.showOptionDialog(null, sss, "Game interruptted", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null)<1){
				nf.dispose();
				ww.userlogout("");
			}
		}
	}
	public void answer(int id, int c, int v){
		if(myTimer.isRunning()){
			myTimer.stop();
		}
//		this.choseBeforeTimeUp = true;
		int index = v*5+c;
		this.bp.manuallyDisable(index);
		for(int i = 0; i<this.teamNumber-1; i++){	
			hostThreads[i].sendMessage("chosen"+index);
		}
		game.currentC = c;
		game.currentV = v;
		Util.ps("player "+id+" answered question "+c+" "+v);
		this.nf.commandWindow.append('\n'+this.game.Team[currentTeam]+" chose the question in "
		+ game.myFile.Category[c]+ " worth $"+ game.myFile.PointValue[v]);;
		//set up and send question window
		this.qs = new QuestionScreen(nf);
		qs.setID(0);
		qs.setGameHost(this);
		qs.setInfo(game.Team[id], game.myFile.Category[c],
				game.myFile.PointValue[v], game.myFile.Question[c*5+v][3]);
		qs.setup();
		//when someone answer a question
		this.timingMode = 1;
		startTiming(20, qs.timerLabel);
		
		
		if(id ==0){
			qs.enablesubmit(true);
		}else {
			qs.enablesubmit(false);
		}
		for(int i = 0; i<this.teamNumber-1; i++){
			QuestionInfo qi = new QuestionInfo();
			qi.username = game.Team[id];
			qi.category = game.myFile.Category[c];
			qi.pointvalue = game.myFile.PointValue[v];
			qi.content = game.myFile.Question[c*5+v][3];
			if(i+1==id){
				qi.enable = true;
				hostThreads[i].sendMessage(qi);
			}else{
				qi.enable = false;
				hostThreads[i].sendMessage(qi);
			}
		}
		Util.ps("successfully send qs message");
//		nf.setVisible(false);
		this.nf.setPanel(qs, "QS");
		nf.changePanel("QS");
//		nf.setVisible(true);
		game.answered++;	
	}
	
	public void submit(int id, String answer){
		if(isfj){
			game.answerFJQuestion(id, answer);
			return;
		}
		Util.ps("is about to check player "+ id+"'s answer"+'\n'+answer);
		this.currentTeam = id;		
		int temp = game.AnswerQuestion(game.currentC, game.currentV, answer);
		//stop the timer
		if(myTimer.isRunning()){
			Util.ps("timer is still running");
			myTimer.stop();
		}
		//if time is up
		else{
			Util.ps("timer already stoped");
			Util.ps("this is the first temp "+temp);
//			temp = game.AnswerQuestion(game.currentC, game.currentV, answer);
			game.formatchance = true;
			jta.append('\n' + "Time's Up!");
		}
		Util.ps("this is the result of temp"+ temp);
//		this.answeredBeforeTimeUp = true;
		
		//if the team answered the question correctly
		Util.ps('\n'+""+game.answered);
		if(temp==1){
			if(game.answered ==25){
				game.checkfj();
				if(game.fjteam==0){
					game.displayResults();
				}else{
					//if host get to final Jeopardy
					preparefj();
				}
			}
			//if haven't run out of question
			else{
				if(id!=this.initialTeam){
					game.CurrentTeam = id;
				}
				jta.append('\n' + "Now it's " + game.Team[game.CurrentTeam] + "'s turn! Please choose a question.");
				this.initialTeam = game.CurrentTeam;
				nf.changePanel("BP");
//				this.nf.animationOn(game.CurrentTeam);
				String s = new String("showBP");
				for(int i = 0; i<this.teamNumber-1; i++){	
					hostThreads[i].sendMessage(s);
				}
				this.setMode(this.initialTeam);
			}
		}//end of if the question is answered incorrectly
		else if(temp == -1){
			//if none of the team got the question correct
			if(game.CurrentTeam==initialTeam){
				jta.append('\n' + "None of the teams were able to get the question right!"+
						"The answer is: "+game.myFile.Question[game.currentC*5+game.currentV][4]);
				if(game.answered ==25){
					game.checkfj();
					if(game.fjteam==0){
						game.displayResults();
					}else{
						//should play final jeopardy here
						preparefj();
					}
				}//if still have questions left
				else{
					jta.append('\n' + "Now it's " + game.Team[game.CurrentTeam] + "'s turn! Please choose a question.");
					nf.changePanel("BP");
					String s = new String("showBP");
					for(int i = 0; i<this.teamNumber-1; i++){	
						hostThreads[i].sendMessage(s);
					}
					this.setMode(this.initialTeam);
				}				
			}
			//if not all team got the question incorrect
			else{
				//enable latter teams to buzz in
				this.timingMode = 2;
				this.startTiming(20, qs.timerLabel);
				callBuzzin();
			}
		}
		//if the team answered in incorrect form
		else if(temp==-2){			
			jta.append('\n' + "Team " + game.Team[game.CurrentTeam] + " had a badly formatted answer");
			jta.append('\n' + "They will get a second chance to answer");
			if(id==0){
				qs.setFormat("Remember to pose your answer as a question");
				qs.enablesubmit(true);
			}else{
				String s = new String("CorrectForm");
				hostThreads[id-1].sendMessage(s);
			}
			this.timingMode = 1;
			startTiming(20,qs.timerLabel);	
		}
	}
	private void preparefj(){
		if(game.tofj[0]==true){
			this.pfj = new PersonalFJ(nf);
			pfj.setID(0);
			pfj.setName(game.Team[0]);
			pfj.setGameHost(this);
			pfj.setSlider(game.getTeamPoint(0));
			pfj.setFJ(game.myFile.Question[25][2]);
			pfj.setWaitingLabels(game.Team);
			pfj.setWaitingVisible(game.tofj);
			nf.setPanel(pfj, "PFJ");
			nf.changePanel("PFJ");
		}
		//if host can't get to final jeopardy
		else{
			setWinnerWindow();
		}
		for(int i = 0; i<this.teamNumber-1; i++){	
			//if the player can get to final jeopardy
			if(game.tofj[i+1]==true){
				SetBet sb = new SetBet();
				sb.id = -1;
				sb.point = game.getTeamPoint(i+1);
				sb.fjquestion = game.myFile.Question[25][2];
				sb.tofj = game.tofj;
				hostThreads[i].sendMessage(sb);
			}//if the player cannot get to final jeopardy
			else{
				
				RatingInfo ri = new RatingInfo();
				ri.message = new String("You didn't get to the final jeopardy");
				ri.rating = this.msg;
				hostThreads[i].sendMessage(ri);
				sent[i+1] = true;
			}

		}
		Util.ps("successfully send the windows to others");
	}
	public void callBuzzin(){
		qs.enableBuzzin();
		qs.setTeamName("");
		String b = new String("buzz");
		for(int i = 0; i<this.teamNumber-1; i++){	
			hostThreads[i].sendMessage(b);
		}
	}
	public synchronized void buzzin(int id){
		Util.ps("player "+id+" buzzed in");
		jta.append('\n'+game.Team[id]+" buzzed in");
		qs.setTeamName(game.Team[id]);
		String b = new String("someonebuzz"+game.Team[id]);
		if(id==0){
			qs.enableAnswer();
		}else{
			this.qs.hideBuzzin(game.Team[id]+"");
		}
		for(int i = 0; i<this.teamNumber-1; i++){	
			hostThreads[i].sendMessage(b);
		}
		this.timingMode = 1;
		startTiming(15,qs.timerLabel);	
	}
	public synchronized void setBet(int id, int bet){
		game.setbet(id, bet);
		jta.append('\n'+"Team "+game.Team[id]+
				"bets $"+bet);
		String s = new String(game.Team[id]+" bet $"+bet);
		if(id!=0&&game.tofj[0]==true)this.pfj.setBet(id-1, s);
		for(int i = 0; i<this.teamNumber-1; i++){	
			if(id!=i+1&&game.getTeamPoint(i)>0&&game.tofj[i+1]==true){
				SetBet sb = new SetBet();
				int index = (id+this.teamNumber-i-2)%this.teamNumber;
				sb.id = index;
				sb.name = game.Team[id];
				sb.bet = bet;
				sb.tofj = game.tofj;
				hostThreads[i].sendMessage(sb);
			}
		}
		if(game.alreadybet==game.fjteam){
			Util.ps("this is the fj already bet"+ game.alreadybet);
			Util.ps("this is the fj teamnumber"+game.fjteam);
			jta.append('\n'
					+"The final jeopardy question is: "+'\n'
					+game.myFile.Question[25][2]);
			if(game.tofj[0]==true){
				pfj.enableSubmit();
			}
			String ss = new String("enablesubmit");
			for(int i = 0; i<this.teamNumber-1; i++){	
				if(game.tofj[i+1]==true){
					hostThreads[i].sendMessage(ss);
				}
			}
			isfj = true;
		}
	}
	public void setWinnerWindow(){
		sent[0]=true;
		winnerw = new WinnerWindow("You didn't get to the final jeopardy", this.msg);
		winnerw.setID(0);
		winnerw.setGameHost(this);
		winnerw.setName(game.Team[0]);
		nf.changePanel("BP");
		winnerw.setVisible(true);
		
	}
	public void setWinnerW(WinnerWindow winnerw){
		//if host's winner window has already been set
		if(sent[0]==true){
			
		}else{
			this.winnerw = winnerw;
			winnerw.setID(0);
			winnerw.setGameHost(this);
			nf.changePanel("BP");
			winnerw.setVisible(true);
			
		}
		RatingInfo ri = new RatingInfo();
		ri.message = winnerw.getMessage();
		ri.rating = winnerw.getRating();
		for(int i = 0; i < teamNumber-1; i++){
			if(sent[i+1]==false){
				hostThreads[i].sendMessage(ri);
			}
		}
	}
	public void rate(int rate){
		Util.ps("getting rating message from client");
		this.rated++;
		this.totalrate+=rate;
		if(rated==teamNumber){
			Util.ps("this is the total rating"+ totalrate);
			Util.ps("this is the total people"+ rated);
			game.myFile.write(totalrate,teamNumber);	
			winnerw.dispose();
			WelcomeWindow firstwindow = new WelcomeWindow(game.Team[0]);
			firstwindow.setVisible(true);
			ww.dispose();
		}
	}
	public void startTiming(int interval, JLabel j){
		if(myTimer != null){
			if(myTimer.isRunning()){
				myTimer.stop();
			}
		}
		this.jt = new JeopardyTimer(interval);
//		jt.setClientPlayer(this);
		jt.setGameHost(this);
		this.jt.setLabel(j);
		this.myTimer = new Timer(1000, jt);
		jt.setTimer(myTimer);
		myTimer.start();		
	}

	public void timeUp() {
		Util.ps("time is up");
		
		//if time up during bp 
		if(timingMode==0){
			Util.ps("time up during bp");
//			if (!this.choseBeforeTimeUp) {
				game.CurrentTeam = (game.CurrentTeam+1)%(game.Team.length);
				this.initialTeam = game.CurrentTeam;
				Util.ps("current team is increased");
				this.setMode(game.CurrentTeam);
				this.jta.append('\n' + "Now it's " + game.Team[game.CurrentTeam] + "'s turn! Please choose a question.");
				return;
//			}else{
//				this.choseBeforeTimeUp = false;
//			}
		}
		//if time up during questionScreen
		else if(timingMode ==1){
			Util.ps("time up during qestion");
//			if(!answeredBeforeTimeUp){
//				
//			}else{
//				answeredBeforeTimeUp = false;
//			}
			if(game.CurrentTeam==0){
				this.qs.pass.setEnabled(false);
			}else{
				Boolean b = false;
				hostThreads[game.CurrentTeam-1].sendMessage(b);
			}
			submit(game.CurrentTeam, "what is ");
		}
		//if time up during buzzin 
		else if(timingMode ==2){
			Util.ps("time up during buzz in ");
			this.jta.append('\n' + "No team buzz in to answer");
			
			if(game.answered ==25){
				game.checkfj();
				if(game.fjteam==0){
					game.displayResults();
				}else{
					//if host get to final Jeopardy
					preparefj();
				}
			}
			//if haven't run out of question
			else{
				this.initialTeam = (this.initialTeam+1)%(game.Team.length);
				game.CurrentTeam = initialTeam;
				jta.append('\n' + "Now it's " + game.Team[game.CurrentTeam] + "'s turn! Please choose a question.");
				nf.changePanel("BP");
//				this.nf.animationOn(game.CurrentTeam);
				String s = new String("showBP");
				for(int i = 0; i<this.teamNumber-1; i++){	
					hostThreads[i].sendMessage(s);
				}
				this.setMode(this.initialTeam);
			}

		}
		
	}
	public void finishrating(){
		nf.dispose();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(teamToWait!=0){
			if(ss.isClosed()) return;
			try {
				s = ss.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Util.ps("problem when reading from ss"+e.getMessage());
			}
			if(s!=null){
				HostThread ht = new HostThread(this, s);
				hostThreads[this.IDToAssign-1]=ht;
				Message m = new Message(this.IDToAssign,"AssignID");
				ht.sendMessage(m);
				this.IDToAssign++;
				s = null;
			}
		}//exit when teamToWait == 0
	}
}
