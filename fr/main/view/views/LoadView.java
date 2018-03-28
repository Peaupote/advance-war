package fr.main.view.views;

import java.awt.*;

import fr.main.view.controllers.LoadController;
import fr.main.view.MainFrame;

public class LoadView extends View {

  private LoadController controller;
  private static int width  = 200,
                     height = 30,
                     x      = (MainFrame.WIDTH - width) / 2,
                     y      = (MainFrame.HEIGHT - height) / 2;

  public LoadView(LoadController controller) {
    super(controller);
    this.controller = controller;
  }

  public void paintComponent (Graphics g) {
    g.setColor(Color.white);
    g.fillRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);

    g.setColor(Color.black);
    g.drawRect (x, y, width, height);
    g.setColor(Color.green);
    g.fillRect (x + 1, y + 1, controller.getLoad() * (width / 100), height - 1);
  }

}
