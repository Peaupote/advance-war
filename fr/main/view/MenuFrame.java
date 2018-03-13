package fr.main.view;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fr.main.App;

public class MenuFrame extends JPanel{
	
	ImageIcon bg;
	JLabel label;
	JButton play;
	JButton select;
	JButton load;
	JButton edit;

	public MenuFrame(Controller controller,View view) {
		
		bg = new ImageIcon("./assets/bd.png");
		label = new JLabel(bg);
		play = new JButton("Play");
		select = new JButton("Select");
		load = new JButton("Load");
		edit = new JButton("Edit");
		
		label.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
		label.setIcon(bg);
		label.setHorizontalAlignment(0);
		
		play.setBounds(400, 180, 100, 60);
		select.setBounds(400, 280, 100, 60);
		load.setBounds(400, 380, 100, 60);
		edit.setBounds(400, 480, 100, 60);
		
		
		play.addActionListener((ActionEvent e) ->{
			remove(this);
			setVisible(false);
			
			new Thread(() -> {
			      while (true) {
			        controller.update();
			        view.repaint();
			        try {
			          Thread.sleep(10);
			        } catch (InterruptedException e1) {
			          e1.printStackTrace();
			        }
			      }
			    }).start();
		});
		
		
		
		label.add(play);
		label.add(select);
		label.add(load);
		label.add(edit);
		this.add(label);
		
	}
	
}
