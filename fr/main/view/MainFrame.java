package fr.main.view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;

import java.lang.Thread;
import java.lang.InterruptedException;

/**
 * Frame for the application
 */
public class MainFrame extends JFrame {

  public static final int WIDTH = 500, HEIGHT = 500, UNIT = 100;

  public MainFrame () {
    super("Advance war");

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setResizable(false);

    Controller controller = new Controller();
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
          Thread.sleep(5);
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
