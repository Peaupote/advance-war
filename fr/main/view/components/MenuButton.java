package fr.main.view.components;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class MenuButton extends JButton{
	Font font;

	public MenuButton(ImageIcon image, int x, int y){
		setBounds(x, y,image.getIconWidth(),image.getIconHeight());
		setContentAreaFilled(false);
		setOpaque(false);
	    //play.setBackground(new Color(0, 0, 255));
		setFocusPainted(false); 
		setBorder(null);
		setIcon(image);
	}

	public MenuButton(String path, int x, int y){
		this(new ImageIcon(path), x, y);
	}
	
	public MenuButton(String nom, String path, int x, int y) {
		this(path,x,y);
		
		font = new Font("Courier", Font.BOLD+Font.ITALIC, 34);
		setHorizontalTextPosition(SwingConstants.CENTER);  
		//setMargin(new Insets(0, 10, 50, 50));
		setFont(font);
	    setForeground(Color.WHITE);
		setText(nom);
	}
	
	public MenuButton(String nom, String path, int x, int y, int t) {
		this(nom, path, x, y);

		font = new Font("Courier", Font.BOLD+Font.ITALIC, t);
		setFont(font);
	}
	
	public MenuButton(String nom, String path, int x, int y, int t, Color color){
		this(nom, path, x, y, t);

		setForeground(color);
	}
}
