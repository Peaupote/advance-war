package fr.main.view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.LinkedList;

import fr.main.view.interfaces.*;

public class View extends JPanel {

  private final Controller controller;
  private final LinkedList<InterfaceUI> uiComponents;

  public View (Controller controller) {
    this.controller = controller;

    uiComponents = new LinkedList<>();
    uiComponents.add(new TerrainPanel (controller.cursor, controller.camera, controller.world)); 
    uiComponents.add(new PlayerPanel (controller.cursor, controller.camera, controller.world)); 

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
    
    for (InterfaceUI comp: uiComponents)
      comp.draw(g);
  }

}
