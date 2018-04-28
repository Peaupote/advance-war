package fr.main.view.views;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import fr.main.view.components.MenuButton;
import fr.main.view.controllers.MenuController;

/**
 * Rendering main menu
 */
@SuppressWarnings("serial")
public class MenuView extends View {

    private ImageIcon bg;
    private JLabel label;
    private JButton play,
                    load,
                    edit,
                    host,
                    join,
                    exit,
                    option,
                    sound;

    protected MenuController controller;
    
    public MenuView (MenuController controller) {
        super(controller);

        bg     = new ImageIcon("./assets/screens/bd.png");
    		label  = new JLabel(bg);
    		
    		play   = new MenuButton("PLAY","./assets/button/b02.png", 350, 150);
    		load   = new MenuButton("LOAD","./assets/button/b02.png", 350, 290);
    		host   = new MenuButton("HOST","./assets/button/b02.png", 350, 430);
    		join   = new MenuButton("JOIN","./assets/button/b02.png", 350, 570);
        edit   = new MenuButton("MAP EDITOR","./assets/button/b02.png", 350, 430);
    		
    		exit   = new MenuButton("EXIT","./assets/button/b03.png", 830, 0, 20);
    		option = new MenuButton("OPTION","./assets/button/b03.png",-5, 0, 20);
    		sound  = new MenuButton("", "./assets/button/music03.png", 40, 600);


        label.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
        label.setIcon(bg);
        label.setHorizontalAlignment(0);
        
        label.add(play);
        label.add(load);
        label.add(edit);
        //label.add(host);
        //label.add(join);
        label.add(play);
        label.add(exit);
        label.add(option);
        label.add(sound);
            
        add(label);

        play.addActionListener(controller.play);
        load.addActionListener(controller.load);
        edit.addActionListener(controller.edit);
        exit.addActionListener(controller.exit);
        host.addActionListener(controller.host);
        join.addActionListener(controller.join);
        option.addActionListener(controller.option);
        controller.bm.start(true);
        controller.new Music(sound);
    }

}
