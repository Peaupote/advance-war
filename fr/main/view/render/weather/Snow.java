package fr.main.view.render.weather;

import java.awt.Color;
import java.awt.Graphics;

public class Snow extends WeatherRender {

  private static Color color = Color.white;

  private class SnowParticule extends WeatherRender.Particule {

    int radius;

    public void backTop() {
      super.backTop();
      radius       = rand.nextInt(10) + 5;
    }

    protected void draw (Graphics g) {
      g.setColor(color);
      g.fillOval (x, y, radius, radius);
    }
  }

  public Snow (int density) {
    super(density);
  }

  protected SnowParticule createParticle () {
    return new SnowParticule();
  }

}
