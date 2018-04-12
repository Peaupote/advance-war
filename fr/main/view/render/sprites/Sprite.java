package fr.main.view.render.sprites;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Sprite {

	private static HashMap<String, Sprite> instances = new HashMap<>();

	private BufferedImage sprite;

	Sprite(String path) {
		instances.put(path, this);

		try {
			sprite = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.err.println(path);
			e.printStackTrace();
		}
	}

	public Sprite(String name, BufferedImage image) {
		if (instances.containsKey(name)) return;
		if (image == null) System.out.println("Image null.");
		sprite = image;
		instances.put(name, this);
	}

	public static Sprite get(String path) {
		if (instances.containsKey(path)) return instances.get(path);
		return new Sprite(path);
	}

	public BufferedImage getImage(int x, int y, int w, int h) {
		if (sprite == null) return null;

		BufferedImage out = null;
		try {
			out = sprite.getSubimage(x, y, w, h);
		} catch (RasterFormatException e) {
			System.out.println(x + " : " + y + " : " + w + " : " + h);
			System.out.println(sprite.toString());
			e.printStackTrace();
		}

		return out;
	}

	public Image getImage(int x, int y, int w, int h, double scale) {
		Image img = getImage(x, y, w, h);

		if (img == null) {
			System.out.println("x : " + x + "| y : " + y + "| w : " + w + "| h : " + h);
			System.exit(11);
		}
		return img.getScaledInstance((int) (((double) w) * scale), (int) (((double) h) * scale), Image.SCALE_SMOOTH);
	}

	public Image getReverseImage(int x, int y, int w, int h, double scale, boolean vertical) {
		BufferedImage img = getImage(x, y, w, h);

		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		// TODO: find out why cant flip horizontally
		tx.translate(-img.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(img, null).getScaledInstance((int) (((double) w) * scale), (int) (((double) h) * scale), Image.SCALE_SMOOTH);
	}

	public Image getImage(ScaleRect rect) {
		if (rect.reverse == ScaleRect.Flip.NONE)
			return getImage(rect.x, rect.y, rect.width, rect.height, rect.scale);

		if (rect.reverse == ScaleRect.Flip.HORIZONTALLY)
			return getReverseImage(rect.x, rect.y, rect.width, rect.height, rect.scale, true);

		return getReverseImage(rect.x, rect.y, rect.width, rect.height, rect.scale, false);
	}

	public boolean contains(String path) {
		return instances.containsKey(path);
	}

	public static BufferedImage getSprite(String path) {
		return instances.get(path).getSprite();
	}

	public BufferedImage getSprite() {
		return sprite;
	}

	public class SpriteTuple {
		public final String path;
		public final ScaleRect rect;

		public SpriteTuple(String path, ScaleRect rect) {
			this.path = path;
			this.rect = rect;
		}

		public String getPath() {
			return path;
		}

		public ScaleRect getRect() {
			return rect;
		}
	}

}
