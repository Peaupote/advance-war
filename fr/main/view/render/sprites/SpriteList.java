package fr.main.view.render.sprites;

import java.util.LinkedList;
import java.awt.Rectangle;
import java.awt.Image;

public class SpriteList extends LinkedList<Image> {

  private Sprite sprite;

  public SpriteList (String path, LinkedList<ScaleRect> images) {
    super();
    sprite = Sprite.get(path);

    for (ScaleRect rect: images)
      add(sprite.getImage(rect.x, rect.y, rect.width, rect.height, rect.scale));
  }

}
