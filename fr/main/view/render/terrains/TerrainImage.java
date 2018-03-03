package fr.main.view.render.terrains;

import fr.main.model.terrains.Terrain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TerrainImage {
  private static HashMap<String, TerrainImage> instances = new HashMap<>();
  private BufferedImage image;

  public TerrainImage(String path) {
    if(!instances.containsKey(path)) instances.put(path, this);
    else {
      this.image = instances.get(path).getImage();
      return;
    }

    try {
      image = ImageIO.read(new File(path));
    } catch (IOException e) {
      System.out.println("Wrong path : " + path);
      image = null;
    }

  }

  public BufferedImage getSubImg(Location l, int width, int height) {
    if(image == null) return null;
    BufferedImage out = null;
    try {
      out = image.getSubimage(l.x, l.y, width, height);
    } catch (RasterFormatException e) {
      System.out.println("Image out of bound : origin(" + l.x + ":" + l.y + ")");
    }

    return out;
  }

  public BufferedImage getSubImg(Location l) {
    return getSubImg(l, 16, 16);
  }

  public static TerrainImage get(String path) {
    if(!instances.containsKey(path)) instances.put(path, new TerrainImage(path));
    return instances.get(path);
  }

  public BufferedImage getImage() {
    return image;
  }

  public enum Location {
    TOP_LEFT(0, 0), TOP_RIGHT(34,0), BOTTOM_LEFT(0, 34), BOTTOM_RIGHT(34, 34),
    TOP(17, 0), BOTTOM(17, 34), LEFT(0, 17), RIGHT(34, 17),
    CENTER(17, 17);
    int x, y;
    Location(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }
}
