package fr.main.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import fr.main.App;

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
	
	public MainMenu() {
		// TODO Auto-generated constructor stub
		bg = new ImageIcon("./assets/bd.png");
		//button = new ImageIcon("./assets/button.png");
		label = new JLabel(bg);
		//Font font =new Font("Courier", Font.BOLD, 30);
		play =new JButton("PLAY");
		//play.setFont(font);
		select = new JButton("Select");
		load = new JButton("Load");
		edit = new JButton("Edit");
		exit = new JButton("Exit");
		option = new JButton("Option");
		sound = new RButton("×");
		
		label.setBounds(0, 0, bg.getIconWidth(), bg.getIconHeight());
		label.setIcon(bg);
		label.setHorizontalAlignment(0);
		
		play.setBounds(400, 180, 100, 60);
		select.setBounds(400, 280, 100, 60);
		load.setBounds(400, 380, 100, 60);
		edit.setBounds(400, 480, 100, 60);
		option.setBounds(0, 0, 80, 40);
		exit.setBounds(900,0,60,40);
		sound.setBounds(30, 630, 50, 50);
		
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
		
		//play.setContentAreaFilled(false);
		//play.setFocusPainted(false); 
		play.setBorder(null);
		//play.setForeground(Color.WHITE);
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
	/**
	public static void main(String[] args) {
		new MainMenu()
	}
	**/
}
