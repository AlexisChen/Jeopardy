package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import Utilities.JeopardyTimer;
import Utilities.Util;
import gamePlay.ButtonPanel;
import gamePlay.PersonalFJ;
import gamePlay.QuestionScreen;
import gamePlay.WelcomeWindow;
import gamePlay.WinnerWindow;
import gamePlay.networkFrame;
import messages.Answer;
import messages.MainScreenSetup;
import messages.ModeMessage;
import messages.QuestionInfo;
import messages.RatingInfo;
import messages.SetBet;
import messages.VP;

public class ClientPlayer extends Thread implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id = -1;
	public String playerName = new String ("");
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Socket s = null;
	private WelcomeWindow ww;
	private boolean stop = false;
	private String[] names;
	
	//for game
	private networkFrame nf;
	private ButtonPanel bp;
	private QuestionScreen qs;
	private PersonalFJ pfj;
	private WinnerWindow winnerw;
	
	// for assignment 5
	private Timer myTimer;
	private JeopardyTimer jt;

	public ClientPlayer(WelcomeWindow ww){
		this.ww = ww;	
	}
	public void prepare(int port, String hostname) throws IOException{
		s = new Socket(hostname, port);
		ois = new ObjectInputStream(s.getInputStream());
		oos = new ObjectOutputStream(s.getOutputStream());
		this.playerName = ww.name[0].getText();
		this.start();
	}

	private void parseMessage(Object obj){
		if(obj instanceof Message){
			Message m = (Message) obj;
			String s = m.getOp();
			System.out.println("can receive message from server");
			if(s.equals("updateWaitingNumber")){
				ww.waitingl.setText("Waiting for " + m.getWaiting() + " other teams to join");
			}else if(s.equals("AssignID")){
				this.id = m.getID();
				Util.ps("client receiving assignid message: "+ id);
				Message me = new Message(id, "reportName"+this.playerName);
				try {
					oos.writeObject(me);
					oos.flush();
					Util.ps("client send reportname message");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Util.ps("problem when sending user name from client"+ e.getMessage());
				}
			}

		}else if (obj instanceof MainScreenSetup){
			Util.ps("set up main screen command from server");
			MainScreenSetup mss = (MainScreenSetup)obj;
			this.bp = new ButtonPanel();
			bp.setID(this.id);
			bp.setClientPlayer(this);
			bp.setImage(mss.images);
			bp.setcnp(mss.categories, mss.pointvalues);
			bp.setup();
			this.names = mss.names;
			this.nf = new networkFrame(mss.names);
			nf.setId(this.id);
			nf.setClientPlayer(this);
			nf.setup();
//			nf.setCW(this.jta);
			nf.commandWindow.append("hhhhhhh can you update");
			nf.setPanel(bp, "BP");
			nf.setVisible(true);
			ww.setVisible(false);
		}else if (obj instanceof String){
			String cw = (String)obj;
			if(cw.startsWith("show")){
				Util.ps("show bp request from server");
				cw  = cw.replaceAll("show", "");
				this.nf.changePanel(cw);
			}else if(cw.startsWith("buzz")){
				//enable buzzin here;
				this.qs.enableBuzzin();
				qs.setTeamName("");
				this.startTiming(20, qs.timerLabel);
			}else if(cw.startsWith("someonebuzz")){
				cw = cw.replace("someonebuzz", "");
				qs.setTeamName(cw);
				qs.animeClock.setVisible(false);
				this.startTiming(20, qs.timerLabel);
				if(cw.equals(this.playerName)){
					qs.enableAnswer();
				}else{
					qs.hideBuzzin(cw);
				}
			}else if(cw.startsWith("chosen")){
				cw = cw.replaceAll("chosen", "");
				int index = Integer.parseInt(cw);
				bp.manuallyDisable(index);
			}else if(cw.startsWith("enablesubmit")){
				pfj.enableSubmit();
			}else if(cw.startsWith("replay")){
				
				bp.replay();
				nf.changePanel("BP");
			}else if(cw.startsWith("serverlogout")){
				Util.ps("receive message that server is logging out");
				String wl = new String("Disconnected by server, please choose another server");
				ww.serverLogout(wl);
			}else if(cw.startsWith("pop")){
				String sss = new String("one of the players has exited, click ok to restart");
				if (JOptionPane.showOptionDialog(null, sss, "Game interruptted", JOptionPane.DEFAULT_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, null, null) < 1) {
					nf.dispose();
					ww.serverLogout("");
				}
			}else if(cw.startsWith("CorrectForm")){
				qs.setFormat("Remember to pose your answer as a question");
				qs.enablesubmit(true);
				startTiming(20,qs.timerLabel);	
			}
			else{
				Util.ps("update command window request from server");
				this.nf.updateCW(cw);
			}
		}else if (obj instanceof VP){
			Util.ps("update vp request from server");
			VP nvp = (VP)obj;
			nf.setVisible(false);
			nf.updateVP(nvp.vp);
			nf.setVisible(true);
//			Util.ps("successfully update vp");
		}
		else if(obj instanceof ModeMessage){
			Util.ps("set play mode request from server");
			ModeMessage mm = (ModeMessage)obj;
			Boolean play = mm.enable;
			this.bp.setmode(play);
			this.nf.animationOn(mm.currentAnsweringTeam);
			startTiming(15,this.bp.timing);
			Util.ps("successfully set play mode");
		}
		else if (obj instanceof Boolean){
			Util.ps("disable buzzin request from server");
			this.qs.pass.setEnabled(false);
			

		}
		else if (obj instanceof QuestionInfo){
			Util.ps("set qi request from server");
			QuestionInfo qi = (QuestionInfo)obj;
			this.qs = new QuestionScreen(this.nf);
			qs.setID(this.id);
			qs.setClientPlayer(this);
			Util.ps('\n'+"this is the question info on client side: "+ qi.content);
			qs.setInfo(qi.username, qi.category,
					qi.pointvalue,qi.content);
			qs.setup();
			//here also set timers
			qs.enablesubmit(qi.enable);
			startTiming(20, qs.timerLabel);
			nf.setPanel(qs, "QS");			
			this.nf.changePanel("QS");
			Util.ps("successfully set qi on client");
		}else if(obj instanceof SetBet){	
			SetBet sb = (SetBet)obj;
			if(sb.id >= 0){
				Util.ps("set bet request from server: ");
				Util.ps("the index should set: "+sb.id);
				String s = new String(sb.name+" bet $"+sb.bet);
				pfj.setBet(sb.id, s);
			}else{
				Util.ps("receive set final jeopardy request from server");
				this.pfj = new PersonalFJ(nf);
				pfj.setID(id);
				pfj.setClientPlayer(this);
				pfj.setName(this.playerName);
				pfj.setSlider(sb.point);
				pfj.setFJ(sb.fjquestion);
				pfj.setWaitingLabels(this.names);
				pfj.setWaitingVisible(sb.tofj);
				nf.setPanel(pfj, "PFJ");
				nf.changePanel("PFJ");
				Util.ps("should see the fj window by now");
			}	
		}else if(obj instanceof RatingInfo){
			RatingInfo ri = (RatingInfo) obj;
			winnerw = new WinnerWindow(ri.message,ri.rating);
			winnerw.setID(this.id);
			winnerw.setClientPlayer(this);
			winnerw.setName(this.playerName);
			nf.changePanel("BP");
			winnerw.setVisible(true);	
		}
	}
	public void clearChoice(){
		ww.reset();
		ww.playmode[0].doClick();
		ww.setVisible(true);
	}
	public void userLogout(){
		stop = true;
		Util.ps("yes i'm logging out");
		Message m = new Message(this.id, "updateWaitingNumber");
		m.setWaiting(1);
		try {
			oos.writeObject(m);
			oos.flush();
			Util.ps("tell user I'm quiting");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Util.ps("error when user self-quiting"+e.getMessage());
		}
		Util.ps("call over from userlogout function");
		this.over();
	}
	public void exitDirectly(){
		String s = new String("clientexit"+id);
		try {
			oos.writeObject(s);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void exitThroughMenu(){
		createPop();
	}
	public void newFile(){
		createPop();
		ww.dispose();
	}
	public void logoutThroughMenu(){
		createPop();
		ww.dispose();
	}
	public void createPop(){
		String s = new String("pop"+this.id);
		try {
			oos.writeObject(s);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void answer(int c, int v){
		Util.ps("player "+this.id+"answered questino of category: "+c
				+" and of value "+v);
		int[] qInfo = new int[3];
		qInfo[0] = this.id;
		qInfo[1] = c;
		qInfo[2] = v;
		try {
			this.oos.writeObject(qInfo);
			oos.flush();
			Util.ps("successfully send the question info to server");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Util.ps("problem when sending question info to server "+ e.getMessage() );
			e.printStackTrace();
		}
	}
	public void submit(String answer){
		Answer a = new Answer();
		a.id = this.id;
		a.answer = answer;
		try {
			oos.writeObject(a);
			oos.flush();
			Util.ps("successfully tell the server that client "+ this.id
				+"answered current question with answer "+ answer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void buzzin(){
		Integer i = new Integer(this.id);
		try {
			oos.writeObject(i);
			oos.flush();
			Util.ps("successfully buzzed in");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setBet(int bet){
		SetBet sb = new SetBet();
		sb.id = this.id;
		sb.bet = bet;
		try {
			oos.writeObject(sb);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void rate(int rate){
		nf.setVisible(false);
		nf.dispose();
		String s = new String("rating"+rate);
		try {
			oos.writeObject(s);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ww.dispose();
	}
	public void startTiming(int interval, JLabel l){
		if(myTimer!=null){
			if(myTimer.isRunning()){
				myTimer.stop();
			}
		}
		this.jt = new JeopardyTimer(interval);
//		jt.setClientPlayer(this);
		this.jt.setLabel(l);
		this.myTimer = new Timer(1000, jt);
		jt.setTimer(myTimer);
		myTimer.start();		
	}
	public void run(){	
		try {
			Message me = new Message(-1, "updateWaitingNumber");
			me.setWaiting(-1);
			oos.writeObject(me);
			oos.flush();
			System.out.println("can send message to server");
			while (!stop) {
				Object m = ois.readObject();
				parseMessage(m);
			}
			Util.ps("has been stopped");
			return;
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("disconnected from server");
			return;
		}
	}
	
	public void over(){
		try {
			this.ois.close();
			this.s.close();
			System.out.println("over");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Util.ps("error when trying to close client"+ e.getMessage());
		}
	}

}
