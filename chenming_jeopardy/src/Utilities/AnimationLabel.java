package Utilities;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class AnimationLabel extends JLabel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private ImageIcon[] images;
	private int totalImages;
	private Timer animationTimer;
	private int currentImage;
	private int width, height;
	
	public AnimationLabel(String act){
		super("",SwingConstants.CENTER);
		if(act.equals("clock")){
			totalImages = 140;
		}else if(act.equals("fish")){
			totalImages = 8;
		}
		images = new ImageIcon[totalImages];
		for(int i = 0; i < totalImages;i++){
			if(act.equals("clock")){
				images[i] = new ImageIcon("images/clockAnimation/frame_" + i + "_delay-0.06s.jpg");
			}else if(act.equals("fish")){
				images[i] = new ImageIcon("images/waitingAnimation/frame_"+i+"_delay-0.1s.jpg");
			}
		}
		startAnimation();
	}
	public void set(int width, int height){
		this.width = width;
		this.height = height;
	}
	private void startAnimation(){
//		Util.ps("inside startAnimation");
		if (animationTimer == null) {
			currentImage = 0;
//			Util.ps("animationTimer = null");
			animationTimer = new Timer(1000, this);
			animationTimer.start();
		} else if (!animationTimer.isRunning()){
			animationTimer.restart();
		}	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		redo();
		
	}
	
	public void redo(){
//		Util.ps("inside paintComponent");
		Image img = images[currentImage].getImage();
//		Util.ps("width and height:");
//		Util.ps(this.getWidth()+"");
//		Util.ps(this.getHeight()+"");
		Image newimg = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH);

//		Image newimg = img.getScaledInstance(this.getWidth(), this.getHeight(),  java.awt.Image.SCALE_SMOOTH);
		ImageIcon newIcon = new ImageIcon(newimg);
		this.setIcon(newIcon);
//		this.repaint();
		this.revalidate();
	    currentImage = (currentImage + 1) % totalImages;
	}

	  public void stopAnimation() {
	    animationTimer.stop();
	  }

}
