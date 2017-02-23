package gamePlay;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JButton;

public class imageButton extends JButton {
	// Serializable
	private static final long serialVersionUID = 1L;
	protected Image img;
	protected Color fontColor = Color.BLACK;
	protected String name;

	// Constructor
	public imageButton(String name, Image img) {
				super(name);
				this.img = img;
				this.name = name;
			}

	@Override
	protected void paintComponent(Graphics g) {
		if (img != null) {
			int w = this.getWidth();
			int h = this.getHeight();
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
			g.drawString(name, 0, 0);
			Font font = new Font("Times New Roman", Font.BOLD, w/5);
			g.setFont(font);
			int strwidth = g.getFontMetrics(font).stringWidth(name);
			int strheight = g.getFontMetrics(font).getHeight();
			g.setColor(fontColor);
			g.drawString(name, (w - strwidth) / 2, h-(h -strheight)/ 2);
		}
	}
	public String getName(){
		return name;
	}
}

