package fr.main.view.render.sprites;

import java.util.LinkedList;
import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class SpriteArray extends ArrayList<BufferedImage> {

  public SpriteArray (String path, LinkedList<Rectangle> images) {
    super();
    
    Sprite sprite = Sprite.get(path);
    for (Rectangle rect: images)
      add(sprite.getImage(rect.x, rect.y, rect.width, rect.height));
  }

}

