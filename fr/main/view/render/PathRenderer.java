package fr.main.view.render;

import java.awt.*;
import fr.main.model.units.Path;
import fr.main.view.MainFrame;
import fr.main.view.Position;

public class PathRenderer extends Path {

  private Position.Camera camera;
  public boolean visible;

  public PathRenderer (Position.Camera camera) {
    super();
    this.camera = camera;
    visible = false;
  }

  public void draw (Graphics g) {
    g.setColor(Color.red);
    Point prev = null;

    for (Point p: this) {
      if (prev != null)
       g.drawLine((prev.x - camera.getX()) * MainFrame.UNIT,
                  (prev.y - camera.getY()) * MainFrame.UNIT,
                  (p.x - camera.getX()) * MainFrame.UNIT,
                  (p.y - camera.getY()) * MainFrame.UNIT);
      prev = p;
    }
  }

}

