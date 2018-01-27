package fr.main.view;

import javax.swing.JPanel;
import java.awt.Graphics;

public class View extends JPanel {

  private final Controller controller;

  public View (Controller controller) {
    this.controller = controller;
    addKeyListener(controller);
  }

  @Override
  public void paintComponent (Graphics g) {
    int x = controller.camera.getX(),
        y = controller.camera.getY();
    controller.word.draw(g, x, y);
    controller.cursor.draw(g);
  }

}
