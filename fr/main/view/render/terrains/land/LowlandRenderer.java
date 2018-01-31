package fr.main.view.render.terrains.land;

import java.awt.Graphics;
import java.awt.Color;

import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.model.terrains.land.Lowland;

public class LowlandRenderer extends Lowland implements Renderer {

  public void draw (Graphics g, int x, int y) {
    g.setColor (Color.green);
    g.fillRect (x, y, MainFrame.UNIT, MainFrame.UNIT);
  }

}

