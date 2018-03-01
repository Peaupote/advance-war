package fr.main.view.render.terrains.land;

import fr.main.model.terrains.land.Wood;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class WoodRenderer extends Wood implements Renderer {
  private String imagePath;
  private transient Image image;
  private transient static WoodRenderer instance;

  private WoodRenderer(String imagePath) {
    this.imagePath = imagePath;
    update();
  }

    @Override
    public String getFilename () {
        return imagePath;
    }

    @Override
    public void setImage (Image image) {
        this.image = image;
    }

  public void draw (Graphics g, int x, int y) {
    if(image == null) {
      g.setColor (Color.green);
      g.fillRect (x, y, MainFrame.UNIT, MainFrame.UNIT);
      return;
    }

    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(image, x, y, MainFrame.UNIT, MainFrame.UNIT, null);
  }

  public static WoodRenderer get () {
    if (instance == null) instance = new WoodRenderer ("./assets/terrains/forest.png");
    return instance;
  }
}
