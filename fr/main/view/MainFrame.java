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
import java.awt.CardLayout;
import java.awt.Container;
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
  Controller controller;
  View view;
  
  public MainFrame () throws UnsupportedAudioFileException, IOException, LineUnavailableException {
    super("Advance war");
    
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setResizable(false);

    Player[] players = new Player[] {
      new Player("P1"),
      new Player("P2")
    };

    controller = new Controller(players);
    view = new View(controller);
    
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
    
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
  
  
 

}
