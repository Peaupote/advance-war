package fr.main.view.views;

import java.awt.*;
import java.awt.image.BufferedImage;

import fr.main.view.interfaces.*;
import fr.main.view.render.weather.*;
import fr.main.view.controllers.GameController;

public class GameView extends View {

  private final WeatherController weather;
  protected GameController controller;

  public GameView (GameController controller) {
    super (controller);
    this.controller = controller;
    this.weather = new WeatherController();

    // remove mouse display
    setCursor(getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), null));
  }

  @Override
  public void paintComponent (Graphics g) {
      int x = controller.camera.getX(),
          y = controller.camera.getY(),
          offsetX = controller.camera.getOffsetX(),
          offsetY = controller.camera.getOffsetY();
      controller.world.draw(g, x, y, offsetX, offsetY);
      
      if (controller.getMode() != GameController.Mode.MOVE)
        controller.unitCursor.draw(g);
      else controller.cursor.draw(g);

      if (controller.path.visible)
        controller.path.draw(g, offsetX, offsetY);
      
      weather.render(g);

      for (InterfaceUI comp: InterfaceUI.components())
          comp.render(g);
  }
}
