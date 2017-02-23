package client;

import java.io.Serializable;

import javax.swing.JPanel;

import gamePlay.MainGamePlay;
import gamePlay.networkFrame;

public class Message implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	 /*
	  * server to client:
	  * updateWaitingNumber
	  * AssignID
	  * ServerLogout
	  * SendFrame
	  * SendPanel
	  * 
	  * client to server:
	  * reportName
	  * updateWaitingNumber
	  * questionChosen
	  * WrongAns
	  * RightAns
	  */
	  private String operation;
	  private networkFrame nf;
	  private JPanel panel;
	  private int waitingNum;
	  public Message(int id, String op){
		  this.id = id;
		  this.operation = op;
	  }
	  public int getID(){
		  return this.id;
	  }
	  public String getOp(){
		  return this.operation;
	  }
	  public void setFrame(networkFrame nf){
		  this.nf = nf;
	  }
	  public void setPanel(JPanel p){
		  this.panel = p;
	  }
	  public networkFrame getFrame(){
		  return this.nf;
	  }
	  public JPanel getPanel(){
		  return this.panel;
	  }
	  public void setWaiting(int w){
		  this.waitingNum = w;
	  }
	  public int getWaiting(){
		  return this.waitingNum;
	  }
	 
}
