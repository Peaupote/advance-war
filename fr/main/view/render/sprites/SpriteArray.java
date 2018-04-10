package fr.main.view.render.sprites;

import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;

public class SpriteArray extends ArrayList<Image> {

	/**
	 * Add SpriteArray UID
	 */
	private static final long serialVersionUID = -8302059632789547463L;

	public SpriteArray(String path, LinkedList<ScaleRect> images) {
		super();

		Sprite sprite = Sprite.get(path);
		for (ScaleRect rect : images)
      add(sprite.getImage(rect));
	}

}

