package fr.main.view.render.sprites;

import java.util.LinkedList;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class SpriteList extends LinkedList<BufferedImage> {

  private Sprite sprite;

  public SpriteList (String path, LinkedList<Rectangle> images) {
    super();
    sprite = Sprite.get(path);

    for (Rectangle rect: images)
      add(sprite.getImage(rect.x, rect.y, rect.width, rect.height));
  }

}
