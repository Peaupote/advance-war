package fr.main.view.render.terrains.land;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.model.terrains.land.Lowland;

import javax.imageio.ImageIO;

public class LowlandRenderer extends Lowland implements Renderer {

  //  private transient boolean imageChecked = false;
  private String imagePath;
  private transient BufferedImage image;

  public LowlandRenderer(String imagePath) {
    this.imagePath = imagePath;
    update();
  }

  @Override
  public void update() {
    try {
      image = ImageIO.read(new File(imagePath));
    } catch (IOException e) {
      System.out.println("At 'setImage' IOException");
    }
  }

  public void draw (Graphics g, int x, int y) {
//    if(!imageChecked && image == null) {
//      setImage(imagePath);
//      imageChecked = true;
//      System.out.println("Image checked");
//    }

    if(image == null) {
      g.setColor (Color.green);
      g.fillRect (x, y, MainFrame.UNIT, MainFrame.UNIT);
      return;
    }

    Graphics2D g2d = (Graphics2D) g;
    g2d.drawImage(image, x, y, null);
  }

}

