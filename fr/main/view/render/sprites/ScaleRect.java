package fr.main.view.render.sprites;

import java.awt.Rectangle;

public class ScaleRect extends Rectangle {

    public final int scale;
    public final boolean reverse;

    public ScaleRect (int x, int y, int w, int h, int scale) {
      this(x, y, w, h, scale, false);
    }

    public ScaleRect (int x, int y, int w, int h, int scale, boolean reverse) {
		super(x, y, w, h);
		this.scale = scale;
		this.reverse = reverse;
	}

    public ScaleRect (int x, int y) {
    	this(x, y, false);
	}

	public ScaleRect (int x, int y, boolean reverse) {
    	this(x, y, 16, 16, 2, reverse);
	}
}

