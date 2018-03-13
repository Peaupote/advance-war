package fr.main.view.render.terrains;

import fr.main.model.terrains.Terrain;
import fr.main.view.AbstractImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TerrainImage extends AbstractImage {
  private static HashMap<String, TerrainImage> instances = new HashMap<>();

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

  public static TerrainImage get(String path) {
    if(!instances.containsKey(path)) instances.put(path, new TerrainImage(path));
    return instances.get(path);
  }

  public BufferedImage getImage() {
    return image;
  }
}
