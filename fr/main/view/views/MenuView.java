package fr.main.view.views;

import java.io.IOException;
import javax.swing.*;
import fr.main.view.components.*;
import fr.main.view.controllers.*;

/**
 * Rendering main menu
 */
public class MenuView extends View {

	/**
	 * Add MenuView UID
	 */
	private static final long serialVersionUID = -2757623237690415972L;
	private ImageIcon bg;
	private JLabel label;
	private JButton play,
                    load,
                    host,
                    join,
                    exit,
                    option,
                    sound;

  protected MenuController controller;
	
	public MenuView (MenuController controller) {
    super(controller);
		// TODO Auto-generated constructor stub
    try {
		bg = new ImageIcon("./assets/screens/bd.png");
		label = new JLabel(bg);
		
		play   = new MenuButton("PLAY","./assets/button/b02.png", 350, 150);
		load   = new MenuButton("LOAD","./assets/button/b02.png", 350, 290);
		host   = new MenuButton("HOST","./assets/button/b02.png", 350, 430);
		join   = new MenuButton("JOIN","./assets/button/b02.png", 350, 570);
		
		exit   = new MenuButton("EXIT","./assets/button/b03.png", 830, 0, 20);
		option = new MenuButton("OPTION","./assets/button/b03.png",-5, 0, 20);
		sound  = new MenuButton("", "./assets/button/music02.png", 40, 600);
		
    } catch (IOException e) {
      System.err.println(e);
    }
		
	label.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
	label.setIcon(bg);
	label.setHorizontalAlignment(0);
	
	label.add(play);
	label.add(load);
	label.add(host);
	label.add(join);
	label.add(play);
	label.add(exit);
	label.add(option);
	label.add(sound);
		
    add(label);

    play.addActionListener(controller.play);
    load.addActionListener(controller.load);
    exit.addActionListener(controller.exit);
    host.addActionListener(controller.host);
    join.addActionListener(controller.join);
    controller.new Music(sound); 
	}

}
