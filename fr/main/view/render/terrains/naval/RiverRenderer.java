package fr.main.view.render.terrains.naval;

import fr.main.model.terrains.land.River;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;

import java.awt.*;
import java.io.File;

public class RiverRenderer extends River implements Renderer {
  private String imagePath;
  private transient Image image;
  private transient static RiverRenderer instance;

  private RiverRenderer(String imagePath) {
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
      g.setColor (Color.cyan);
      g.fillRect (x, y, MainFrame.UNIT, MainFrame.UNIT);
      return;
    }

    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(image, x, y, null);
  }

  public static RiverRenderer get () {
    if (instance == null) instance = new RiverRenderer ("./assets/terrains/river.png");
    return instance;
  }
}
