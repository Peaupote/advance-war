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
  private static int animationOffset = 0;

  private Point offset;

  static {
    new Thread(() -> {
      while (true) {
        animationOffset = (animationOffset + 1) % 5;

        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  private transient Image image;

  public LanderRenderer (Point location) {
    super (null, location);

    offset = new Point(0, 0);
  }

  public void draw (Graphics g, int x, int y) {
    if (image == null) g.fillRect (x + offset.x, y + offset.y, MainFrame.UNIT, MainFrame.UNIT);
    else g.drawImage (image, x + offset.x, y + offset.y + animationOffset - 5, MainFrame.UNIT, MainFrame.UNIT, null);
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

  public void moveOffset (Direction d) {
    d.move(offset);
    if (Math.abs(offset.x) == MainFrame.UNIT || Math.abs(offset.y) == MainFrame.UNIT) {
      offset.x = 0;
      offset.y = 0;
    }
  }

}
