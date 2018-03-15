package fr.main.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainMenu extends JFrame implements ActionListener{
	
	private ImageIcon bg;
	private JLabel label;
	private JButton play;
	private JButton select;
	private JButton load;
	private JButton edit;
	private JButton exit;
	private JButton option;
	private JButton sound;
	static MusicEngine bm = new MusicEngine("./assets/sound/bc.wav");
	static boolean listen = true;
	
	public MainMenu() throws IOException {
		// TODO Auto-generated constructor stub
		bg = new ImageIcon("./assets/bd.png");
		label = new JLabel(bg);
		
		play =new MenuButton("PLAY","./assets/button/b02.png",350, 150);
		select = new MenuButton("SELECT","./assets/button/b02.png",350, 290);
		load = new MenuButton("LOAD","./assets/button/b02.png",350, 430);
		edit = new MenuButton("EDIT","./assets/button/b02.png",350, 590);
		
		exit = new MenuButton("EXIT","./assets/button/b03.png",830, 0,20);
		option = new MenuButton("OPTION","./assets/button/b03.png",-5, 0,20);
		sound = new RButton("×");
		sound.setBounds(40, 600, 60, 60);
		
		label.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
		label.setIcon(bg);
		label.setHorizontalAlignment(0);
		
		exit.addActionListener((ActionEvent e) -> {
			dispose();
		});
		
		sound.addActionListener((ActionEvent e) -> {
			
			if(listen) {
				sound.setText("√");
				bm.start(true);
			}else {
				sound.setText("x");
				bm.stop();
			}
			listen = !listen;
			
		});
		
		play.addActionListener((ActionEvent e) ->{
			try {
				new MainFrame();
			} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
				e1.printStackTrace();
			}
			dispose();
		});
		
		label.add(play);
		label.add(select);
		label.add(load);
		label.add(edit);
		label.add(play);
		label.add(exit);
		label.add(option);
		label.add(sound);
		this.add(label);
		
		//JFrame.setDefaultLookAndFeelDecorated(true);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
