package fr.main.view.render.units;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;

import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.model.units.Unit;

public class UnitRenderer extends Unit implements Renderer {

  public UnitRenderer (Point location) {
    super (location);
  }

  public void draw (Graphics g, int x, int y) {
    g.setColor (Color.red);
    g.fillRect (x, y, MainFrame.UNIT, MainFrame.UNIT);
  }

}
