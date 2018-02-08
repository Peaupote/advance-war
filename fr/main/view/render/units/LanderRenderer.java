package fr.main.view.render.units;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;

import fr.main.model.terrains.Terrain;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.model.units.naval.Lander;

public class LanderRenderer extends Lander implements Renderer {

  public LanderRenderer (Point location) {
    super (null, location);
  }

  public void draw (Graphics g, int x, int y) {
    g.setColor (Color.red);
    g.fillRect (x, y, MainFrame.UNIT, MainFrame.UNIT);
  }

}
