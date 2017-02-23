package gamePlay;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class imageLabel extends JLabel{
	private static final long serialVersionUID = 1L;
	Image img;
	String name;

	// Constructor
	public imageLabel(String name, Image img) {
				super(name, SwingConstants.CENTER);
				this.img = img;
				this.name = name;
			}

	@Override
	protected void paintComponent(Graphics g) {
		if (img != null) {
			int w = this.getWidth();
			int hl = this.getHeight();
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
			g.drawString(name, 0, 0);
			Font font = new Font("Times New Roman", Font.BOLD, w/7);
			g.setFont(font);
			int strwidth = g.getFontMetrics(font).stringWidth(name);
			int strheight = g.getFontMetrics(font).getHeight();
			g.drawString(name, (w - strwidth) / 2, hl-(hl -strheight)/ 2);
		}
	}
}