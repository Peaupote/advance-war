package fr.main.view.render.units;

import java.io.*;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Image;

import fr.main.model.terrains.Terrain;
import fr.main.model.units.Path;
import fr.main.model.Direction;
import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.model.units.naval.Lander;

public class LanderRenderer extends Lander implements UnitRenderer {

  private static String filename = "lander.png";

  private Point offset;

  private transient Image image;

  public LanderRenderer (Point location) {
    super (null, location);

    offset = new Point(0, 0);
  }

  public void draw (Graphics g, int x, int y) {
    if (image == null) g.fillRect (x + offset.x, y + offset.y, MainFrame.UNIT, MainFrame.UNIT);
    else g.drawImage (image, x + offset.x, y + offset.y + UnitAnimationManager.getOffset() - 5, MainFrame.UNIT, MainFrame.UNIT, null);
  }

  @Override
  public void update() {
    try {
      // TODO: make real stuff here
      String color = getPlayer().id == 1 ? "red" : "blue";
      image = ImageIO.read(new File ("./assets/" + color + "/" + filename));

    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  @Override
  public Point getOffset () {
    return offset;
  }

}
