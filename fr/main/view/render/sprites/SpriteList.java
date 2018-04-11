package fr.main.view.render.sprites;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

import java.awt.Image;

public class SpriteList extends LinkedList<Image> {

  /**
	 * Add SpriteList
	 */
	private static final long serialVersionUID = -8016901697482659870L;
	private Sprite sprite;

	public SpriteList(String path, LinkedList<ScaleRect> images) {
		super();
		sprite = Sprite.get(path);

		for (ScaleRect rect : images)
			add(sprite.getImage(rect));
	}

	public SpriteList(LinkedList<Sprite.SpriteTuple> ss, boolean b) {
		for (Sprite.SpriteTuple s : ss)
			add(Sprite.get(s.path).getImage(s.rect));
	}

	public SpriteList(LinkedList<BufferedImage> images) {
		super();
		for (BufferedImage i : images)
			add(i);
	}

	public SpriteList(String path, ScaleRect image) {
		super();
		sprite = Sprite.get(path);
		add(sprite.getImage(image));
	}



	public SpriteList(BufferedImage i) {
		super();
		add(i);
	}

	public boolean add(Sprite.SpriteTuple s) {
		return add(Sprite.get(s.path).getImage(s.rect));
	}
}
