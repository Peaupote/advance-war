package fr.main.view.render.sprites;

import java.awt.Rectangle;

@SuppressWarnings("serial")
public class ScaleRect extends Rectangle {

	public enum Flip {
        NONE,
        HORIZONTALLY,
        VERTICALY
    }

    public final double scale;
    public final Flip reverse;

    public ScaleRect (int x, int y, int w, int h, double scale) {
        this(x, y, w, h, scale, Flip.NONE);
    }

    public ScaleRect (int x, int y, int w, int h, double scale, Flip reverse) {
        super(x, y, w, h);
        this.scale = scale;
        this.reverse = reverse;
    }

    public ScaleRect (int x, int y) {
        this(x, y, Flip.NONE);
    }

    public ScaleRect(int x, int y, int w, int h) {
    	this(x, y, w, h, 2);
  	}

    public ScaleRect (int x, int y, Flip reverse) {
        this(x, y, 16, 16, 2, reverse);
    }
}

