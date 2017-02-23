package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import Utilities.Util;
import client.Message;
import messages.Answer;
import messages.SetBet;

public class HostThread extends Thread implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private GameHost h;
	private Socket s;
	protected String clientName = new String("");
	protected int id=-1;
	public HostThread(GameHost h,Socket s){
		this.h = h;
		this.s = s;
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			this.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	protected void sendMessage(Object m){
		try {
			oos.writeObject(m);
			oos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.over();
		}	
	}
	
	protected void parseMessage(Object obj){
		if(obj instanceof Message){
			Message m = (Message) obj;
			this.h.teamID = m.getID();
			String s = m.getOp();
			if(s.equals("updateWaitingNumber")){
				int change = m.getWaiting();
				Util.ps("host receiving waiting number message: "+ change);
				if(change==1)this.id = m.getID();
				Util.ps("successfully changed id "+this.id);
				h.updateTeamToWait(change);
				System.out.println("can update team to wait in the host");
			}else if(s.startsWith("reportName")){
				this.clientName = s.replaceAll("reportName", "");
				Util.ps("host receiving reportname message: "+ s);
				this.h.nameFilled();
				if(h.teamToWait==0){
					h.startNetwork();
				}
			}
		}
		//hhh 不小心写错地方
		else if(obj instanceof int[]){
			int[]qInfo = (int[])obj;
			this.h.answer(qInfo[0],qInfo[1],qInfo[2]);
		}else if (obj instanceof Answer){
			Answer a = (Answer)obj;
			this.h.submit(a.id, a.answer);
		}else if (obj instanceof Integer){
			Integer bz = (Integer)obj;
			h.buzzin(bz);
		}else if (obj instanceof SetBet){
			SetBet sb = (SetBet) obj;
			h.setBet(sb.id, sb.bet);
		}else if (obj instanceof String){
			Util.ps("getting rating message from client");;
			String s = (String) obj;
			if(s.startsWith("pop")){
				s = s.replaceAll("pop", "");
				int id = Integer.parseInt(s);
				h.createPop(id);
			}else if(s.startsWith("clientexit")){
				s = s.replaceAll("clientexit", "");
				int userid = Integer.parseInt(s);
				this.id = userid;
				if(h.teamToWait>0){
					h.updateTeamToWait(1);
				}
			}else{
				s = s.replaceAll("rating", "");
				int rate = Integer.parseInt(s);
				Util.ps("this is the new rate "+rate);
				h.rate(rate);
			}
			
		}
		
	}
	public void run(){	
			try{
				while(true){
					if(!s.isClosed()){
						Object m = (Object)ois.readObject();
						parseMessage(m);
						System.out.println("can get message from client");
					}else{
						return;
					}
					
				}
				
			}catch(ClassNotFoundException cnfe){
				System.out.println(cnfe.getMessage());
			}catch (IOException e) {
				// TODO Auto-generated catch block
				Util.ps("unable to read from ois"+e.getMessage());				
			}
	}
	public void over(){
		Util.ps("over called by server");
		try{
			this.ois.close();
			this.oos.close();
			this.s.close();
			Util.ps("after closing s, check whether s is closed:  "+s.isClosed());
			Util.ps("closing on the server side");
		}catch(IOException ioe){
			System.out.println("ioe when trying to close the host thread: "+ ioe.getMessage());
		}
	}
}
