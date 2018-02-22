package fr.main.view;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Thread;
import java.lang.InterruptedException;

import fr.main.model.Player;

/**
 * Frame for the application
 */
public class MainFrame extends JFrame {

  public static final int WIDTH = 960, HEIGHT = 704, UNIT = 32;
  
/*  boolean listenBackgroundMusic = false; 
  public BackgroundMusic bm;
  public MusicEngine bm2;
  
  //Components about the background music
  public JButton buttonMusic;
  public JMenuBar menu;
  public JMenu setting;
  public JMenuItem bmItem;
*/  

  public MainFrame () throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    super("Advance war");
    
    // For adding a menu bar above the game windows. For instance, we just add a menu 'setting' for open/close the backgroundmusic
/*    menu = new JMenuBar();
    bm = new BackgroundMusic();
    bm2 = new MusicEngine("assets/bc.wav");
    setting = new JMenu("Setting");
    bmItem = new JMenuItem("Open/Close Music");
    
    bmItem.setBounds(10, 10, 70, 30);
    bmItem.addActionListener((ActionEvent e) ->{
    	
    	listenBackgroundMusic = !listenBackgroundMusic;
    	if(listenBackgroundMusic) {
    		bm2.play_thread(true);
    		bmItem.setText("On playing");
    	}else {
    		bm2.stop_thread();
    		bmItem.setText("Stopped");
    	}
                                                    });
    
    setting.add(bmItem);
    menu.add(setting);
    //change it to 'true', we can play the music, and you can add new music in the filder assets. 
*/   

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setResizable(false);

    Player[] players = new Player[] {
      new Player("P1"),
      new Player("P2")
    };

    Controller controller = new Controller(players);
    View view = new View(controller);

    // set view to listen key events
    setFocusable(false);
    view.setFocusable(true);

    // set view content
    view.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setContentPane(view);

    // main loop
    new Thread(() -> {
      while (true) {
        controller.update();
        view.repaint();
        
        
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
    
//    this.setJMenuBar(menu);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

}
