package fr.main.view.render.sprites;

import java.util.LinkedList;
import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.Image;

public class SpriteArray extends ArrayList<Image> {

	public SpriteArray(String path, LinkedList<ScaleRect> images) {
		super();

		Sprite sprite = Sprite.get(path);
		for (ScaleRect rect : images)
      add(sprite.getImage(rect));
	}

}

