package fr.main.view.render.terrains.land;

import fr.main.model.terrains.land.Road;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RoadRenderer extends Road implements Renderer{
  private String imagePath;
  private transient BufferedImage image;
  private transient static RoadRenderer instance;

  private RoadRenderer(String imagePath) {
    this.imagePath = imagePath;
    update();
  }

  @Override
  public void update() {
    try {
      image = ImageIO.read(new File(imagePath));
    } catch (IOException e) {}
  }

  public void draw (Graphics g, int x, int y) {
    if(image == null) {
      g.setColor (Color.gray);
      g.fillRect (x, y, MainFrame.UNIT, MainFrame.UNIT);
      return;
    }

    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(image, x, y, null);
  }

  public static RoadRenderer get () {
    if (instance == null) instance = new RoadRenderer ("./assets/terrains/road.png");
    return instance;
  }
}
