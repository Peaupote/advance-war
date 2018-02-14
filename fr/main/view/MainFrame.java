package fr.main.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;

import java.lang.Thread;
import java.lang.InterruptedException;

import fr.main.model.Player;

/**
 * Frame for the application
 */
public class MainFrame extends JFrame {

  public static final int WIDTH = 960, HEIGHT = 704, UNIT = 32;

  public MainFrame () {
    super("Advance war");

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

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

}
