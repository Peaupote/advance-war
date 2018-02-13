package fr.main.view.render.terrains.naval;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.model.terrains.naval.Sea;

import javax.imageio.ImageIO;

public class SeaRenderer extends Sea implements Renderer {

  private String imagePath;
  private transient BufferedImage image;
  private transient static SeaRenderer instance;

  public SeaRenderer(String imagePath) {
    this.imagePath = imagePath;
    update();
  }

  @Override
  public void update() {
    try {
      image = ImageIO.read(new File(imagePath));
    } catch (IOException e) {
    }
  }

  public void draw (Graphics g, int x, int y) {
    if(this.image == null) {
      g.setColor (Color.cyan);
      g.fillRect (x, y, MainFrame.UNIT, MainFrame.UNIT);
    }

    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(image, x, y, null);
  }

  public static SeaRenderer get() {
    if (instance == null) instance = new SeaRenderer("assets/aw_terrain_sea.png");
    return instance;
  }

}

