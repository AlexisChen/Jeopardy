package Utilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

import client.ClientPlayer;
import server.GameHost;

public class JeopardyTimer implements ActionListener{
//	private int minute;
	private int second;
	private JLabel timerLabel;
	private Timer timer;
	private GameHost gh;
	private ClientPlayer cp;
	//if mode==0: buttonpanel
	//if mode==1: questionscreen
//	private int mode;
	
	public JeopardyTimer(int second){
		this.second = second;
	}
	public void setLabel(JLabel tl){
		this.timerLabel = tl;
	}
	public void setTimer(Timer timer){
		this.timer = timer;
	}
	public void setGameHost(GameHost gh){
		this.gh = gh;
	}
	public void setClientPlayer(ClientPlayer cp){
		this.cp = cp;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		second--;
		if(second>0){
			this.timerLabel.setText(""+ second);
			timerLabel.repaint();
			timerLabel.revalidate();
//			Util.ps("here");
		}
		else{
			this.timer.stop();
			timerLabel.setText("time up");
			timerLabel.repaint();
			timerLabel.revalidate();
//			Util.ps("end");
			if(this.gh!=null){
				gh.timeUp();
			}
//			else if(this.cp!=null){
//				cp.timeUp();
//			}
		}
	}

}
