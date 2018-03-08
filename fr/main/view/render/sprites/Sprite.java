package fr.main.view.render.sprites;

import java.util.HashMap;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.*;
import javax.imageio.*;

public class Sprite {

  private static HashMap<String, Sprite> instances = new HashMap<>();

  private BufferedImage sprite;

  private Sprite (String path) {
    instances.put(path, this);

    try {
      sprite = ImageIO.read(new File(path));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Sprite get (String path) {
    if (instances.containsKey(path)) return instances.get(path);
    return new Sprite(path);
  }

  public Image getImage (int x, int y, int w, int h) {
    if (sprite == null) return null;
    
    BufferedImage out = null;
    try {
      out = sprite.getSubimage(x, y, w, h);
    } catch (RasterFormatException e) {
      e.printStackTrace();
    }

    return out;
  }

  public Image getImage (int x, int y, int w, int h, int scale) {
    Image img = getImage(x, y, w, h);
    return img.getScaledInstance(w * scale, h * scale, Image.SCALE_SMOOTH);
  }

}
