package fr.main.view.render.sprites;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.DataBuffer;
import java.util.HashMap;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.*;
import javax.imageio.*;

public class Sprite {

	private static HashMap<String, Sprite> instances = new HashMap<>();

	private BufferedImage sprite;

	private Sprite(String path) {
		instances.put(path, this);

		try {
			sprite = ImageIO.read(new File(path));
		} catch (IOException e) {
      System.err.println(path);
			e.printStackTrace();
		}
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
			e.printStackTrace();
		}

		return out;
	}

	public Image getImage(int x, int y, int w, int h, double scale) {
		Image img = getImage(x, y, w, h);

		if(img == null) {
			System.out.println("x : " + x + "| y : " + y + "| w : " + w + "| h : " + h);
			System.exit(11);
		}
		return img.getScaledInstance((int) (((double)w) * scale), (int) (((double)h) * scale), Image.SCALE_SMOOTH);
	}

	public Image getReverseImage(int x, int y, int w, int h, double scale, boolean vertical) {
		BufferedImage img = getImage(x, y, w, h);

		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
    // TODO: find out why cant flip horizontally
    tx.translate(-img.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(img, null).getScaledInstance((int) (((double)w) * scale), (int) (((double)h) * scale), Image.SCALE_SMOOTH);
	}

  public Image getImage (ScaleRect rect) {
    if (rect.reverse == ScaleRect.Flip.NONE)
      return getImage (rect.x, rect.y, rect.width, rect.height, rect.scale);

    if (rect.reverse == ScaleRect.Flip.HORIZONTALLY)
      return getReverseImage(rect.x, rect.y, rect.width, rect.height, rect.scale, true);

    return getReverseImage(rect.x, rect.y, rect.width, rect.height, rect.scale, false);
  }

}
