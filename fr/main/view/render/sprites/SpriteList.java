package fr.main.view.render.sprites;

import java.awt.Image;
import java.util.LinkedList;

public class SpriteList extends LinkedList<Image> {

  /**
	 * Add SpriteList
	 */
	private static final long serialVersionUID = -8016901697482659870L;
	private Sprite sprite;

  public SpriteList (String path, LinkedList<ScaleRect> images) {
    super();
    sprite = Sprite.get(path);

    for (ScaleRect rect: images)
      add(sprite.getImage(rect));
  }
}
