package fr.main.view;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MenuButton extends JButton{
	
	public MenuButton(String path, int x,int y) throws IOException{
		super();
		//path: the adress of image
		ImageIcon imageIcon = new ImageIcon(path);
		
		setBounds(x, y,imageIcon.getIconWidth(),imageIcon.getIconHeight());
		setContentAreaFilled(false);
		setOpaque(false);
	    //play.setBackground(new Color(0, 0, 255));
		setFocusPainted(false); 
		setBorder(null);
		//play.setFont(font);
		//play.setForeground(Color.WHITE);
		setIcon(imageIcon);
	}

}
