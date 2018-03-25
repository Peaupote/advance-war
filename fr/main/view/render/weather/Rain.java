package fr.main.view.render.weather;

import java.awt.*;
import java.util.*;

import fr.main.view.MainFrame;

public class Rain extends WeatherController {

  private static Color color = new Color(0x86D1E8);


  private class RainParticule extends WeatherController.Particule {

    int length;

    public void backTop() {
      super.backTop();
      length       = rand.nextInt(50) + 25;
    }

    protected void draw (Graphics g) {
      g.setColor(color);
      g.drawLine(x, y,
          x + (int)(dx * length),
          y + (int)(dy * length));
    }
  }

  public Rain (int density) {
    super(density);
  }

  protected RainParticule createParticle () {
    return new RainParticule();
  }

}
