package fr.main.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class MenuButton extends JButton{
	Font font;
	public MenuButton(String path, int x,int y) throws IOException{
		super();
		//path: the address of image
		ImageIcon imageIcon = new ImageIcon(path);
		
		setBounds(x, y,imageIcon.getIconWidth(),imageIcon.getIconHeight());
		setContentAreaFilled(false);
		setOpaque(false);
	    //play.setBackground(new Color(0, 0, 255));
		
		setFocusPainted(false); 
		setBorder(null);
		
		setIcon(imageIcon);
	}
	
	public MenuButton(String nom,String path,int x, int y) throws IOException {
		// TODO Auto-generated constructor stub
		this(path,x,y);
		
		font = new Font("Courier", Font.BOLD+Font.ITALIC, 34);
		setHorizontalTextPosition(SwingConstants.CENTER);  
		//setMargin(new Insets(0, 10, 50, 50));
		setFont(font);
	    setForeground(Color.WHITE);
		setText(nom);
	}
	
	public MenuButton(String nom,String path,int x, int y,int t) throws IOException {
		this(nom, path, x, y);
		font = new Font("Courier", Font.BOLD+Font.ITALIC, t);
		setFont(font);
	}
	
	public MenuButton(String nom,String path,int x, int y,int t,Color color) throws IOException{
		// TODO Auto-generated constructor stub
		setForeground(color);
	}
}
