package fr.main.view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;

import fr.main.view.interfaces.*;

public class View extends JPanel {

  private final Controller controller;

  public View (Controller controller) {
    this.controller = controller;

    addKeyListener(controller);
    addMouseMotionListener(controller);
  }

  @Override
  public void paintComponent (Graphics g) {
    int x = controller.camera.getX(),
        y = controller.camera.getY(),
        offsetX = controller.camera.getOffsetX(),
        offsetY = controller.camera.getOffsetY();
    controller.world.draw(g, x, y, offsetX, offsetY);
    controller.cursor.draw(g);
    
    for (InterfaceUI comp: controller.uiComponents)
      comp.render(g);
  }

}
