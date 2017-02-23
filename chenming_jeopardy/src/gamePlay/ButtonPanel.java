package gamePlay;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import client.ClientPlayer;
import constants.Constants;
import server.GameHost;

public class ButtonPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JLabel heading; 
	public  JLabel timing;
	protected imageButton[] qbutton;
	private imageLabel[]c;
	protected Image categoryi, enableimage, disableimage;
	private MainGamePlay mgp;
	private Jeopardy Game;
	private int w;
	private String[] images = new String[3];
	private String[] categories = new String[5];
	private String[] pointvalues = new String[5];
	private int id = -1;
	private GameHost gh;
	private ClientPlayer cp;
	public ButtonPanel(){
		super(new GridBagLayout());
		w = 1500;
	}
	public void setID(int i){
		this.id = i;
	}
	public void setGamehost(GameHost gh){
		this.gh = gh;
	}
	public void setClientPlayer(ClientPlayer cp){
		this.cp = cp;
	}
	public void setImage(String[] images){
		this.images = images;
	}
	public void setcnp(String[] categories, String[]pointvalues){
		this.categories = categories;
		this.pointvalues = pointvalues;
	}
	public void setup(){
		initializeComponents();
		createGUI();
		addEvents();
	}
	public ButtonPanel(MainGamePlay mgp){
		super(new GridBagLayout());
		this.mgp = mgp;
		this.Game = mgp.Game;
		w = mgp.getWidth();
		images[0] = Game.myFile.categoryImage;
		images[1] = Game.myFile.enableImage;
		images[2] = Game.myFile.disableImage;
		this.categories = Game.myFile.Category;
		this.pointvalues = Game.myFile.PointValue;
		setup();		
	}
	private void initializeComponents(){
		try {
			categoryi = ImageIO.read(new File(images[0]));
			enableimage = ImageIO.read(new File(images[1])); 
			disableimage = ImageIO.read(new File(images[2])); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		heading = new JLabel("Jeopardy :",SwingConstants.RIGHT);
		timing = new JLabel(":",SwingConstants.LEFT);

		qbutton = new imageButton[25];
		for(int i = 0; i <25; i++){
			qbutton[i] = new imageButton("$"+this.pointvalues[i/5], enableimage);
		}
		c = new imageLabel[5];
		for(int i = 0; i <5; i++){
			c[i] = new imageLabel(this.categories[i],categoryi);
		}
	}
	private void createGUI(){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 0.06;
		JPanel head = new JPanel();
		heading.setBackground(Constants.lightblue);
		heading.setFont(Constants.setfont("small", w));
		heading.setOpaque(true);
		heading.setBorder(BorderFactory.createEmptyBorder());
		timing.setBackground(Constants.lightblue);
		timing.setFont(Constants.setfont("small", w));
		timing.setOpaque(true);
		timing.setBorder(BorderFactory.createEmptyBorder());
		head.setBackground(Constants.lightblue);
		head.setOpaque(true);
		head.add(heading);
		head.add(timing);
		add(head, gbc);
		
		gbc.gridy = 1;
		JPanel categories = new JPanel(new GridLayout(1,5));
		for(int i = 0; i <5; i++){
			c[i].setForeground(Color.BLACK);
			c[i].setBorder( new LineBorder(Constants.deepblue, 1));
			categories.add(c[i]);
		}
		this.add(categories, gbc);
		gbc.gridy = 2;
		gbc.weighty = 0.88;
		JPanel questions = new JPanel(new GridLayout(5,5));
		for(int i = 0; i <25; i++){
			qbutton[i].setForeground(Color.BLACK);
			qbutton[i].setBorder( new LineBorder(Constants.deepblue, 1));
			questions.add(qbutton[i]);
		}
		this.add(questions, gbc);	
	}
	private void addEvents(){
		for(int i = 0 ; i <25; i++){
			qbutton[i].addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent ae){	
					chosen((imageButton)ae.getSource(), disableimage);
					int index = 0;
					for(int j = 0; j <25; j++){
						if (qbutton[j]==(imageButton)ae.getSource()){
							index = j;
						}
					}
					int c = index%5;
					int v = index/5;
					//play as unnetworked
					if(id==-1){
						mgp.commandWindow.append('\n'+Game.Team[Game.CurrentTeam]+" chose the question in "
								+ Game.myFile.Category[c]+ " worth $"+ Game.myFile.PointValue[v]);
						mgp.qs = new QuestionScreen(c, v, mgp);
						Game.answered++;
						mgp.cards.add(mgp.qs,"QS");
						CardLayout cl = (CardLayout)(mgp.cards.getLayout());
						cl.show(mgp.cards, "QS");
					}else if(id==0){
						gh.answer(0,c,v);
					}else if(id>0){
						cp.answer(c, v);
					}
				}
			});
		}
	}
	public void manuallyDisable(int index){
		chosen(this.qbutton[index], this.disableimage);
	}
	public void chosen(imageButton b, Image img){
		b.img = img;
		b.fontColor = Color.LIGHT_GRAY;
		b.setEnabled(false);
	}
	public void unchoose(imageButton b){
		b.img = this.enableimage;
		b.fontColor = Color.BLACK;
		b.setEnabled(true);
		
	}
	public void setmode(boolean choose){
		if(choose){
			for(int i = 0; i < 25; i++){
				if(qbutton[i].img==enableimage){
					this.qbutton[i].setEnabled(true);
				}
			}
		}else{
			for(int i = 0; i < 25; i++){
				if(qbutton[i].img==enableimage){
					this.qbutton[i].setEnabled(false);
				}
			}
		}
	}
	public void replay(){
		for(int i = 0; i < 25; i++){
			unchoose(this.qbutton[i]);
		}
	}

}
