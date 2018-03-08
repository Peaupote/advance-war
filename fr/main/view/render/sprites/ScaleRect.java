package fr.main.view.render.sprites;

import java.awt.Rectangle;

public class ScaleRect extends Rectangle {
  
    public final int scale;

    public ScaleRect (int x, int y, int w, int h, int scale) {
      super(x, y, w, h);
      this.scale = scale;
    }

}

