package fr.main.view;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import fr.main.model.Player;
import fr.main.view.controllers.*;
import fr.main.view.views.View;

/**
 * Frame for the application
 */
public class MainFrame extends JFrame {

    public static final int WIDTH = 960,
                            HEIGHT = 704,
                            UNIT = 32;

    private static int timer = 0;
    private static MainFrame instance;
    Controller controller;
    View view;
    
    public MainFrame ()
        throws UnsupportedAudioFileException, IOException,
               LineUnavailableException {
        super("Advance war");
        instance = this;
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        setController(new MenuController());

        // main loop
        
        new Thread(() -> {
            while (true) {
                timer = timer == Integer.MAX_VALUE ? 0 : timer + 1;

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
    
    public static int getTimer () {
        return timer;
    }

    public void setController (Controller controller) {
      this.controller = controller;
      this.view       = controller.makeView();
      timer           = 0;
        
      setContentPane(view);
      // set view to listen key events
      setFocusable(false);
      view.setFocusable(true);

      // set view content
      view.setPreferredSize(new Dimension(WIDTH, HEIGHT));
      pack();
    }

    public static void setScene (Controller controller) {
      instance.setController (controller);
    }

}
