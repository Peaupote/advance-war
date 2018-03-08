package fr.main.view.render.units.air;

import java.io.*;
import javax.imageio.ImageIO;
import java.awt.*;

import fr.main.model.terrains.Terrain;
import fr.main.model.units.Path;
import fr.main.model.Direction;
import fr.main.view.MainFrame;
import fr.main.view.render.units.UnitRenderer;
import fr.main.model.units.air.Fighter;

public class FighterRenderer extends Fighter implements UnitRenderer {

  private Point offset;

  private transient Image image;

  public FighterRenderer (Point location) {
    super (null, location);

    offset = new Point(0, 0);
  }

  public void draw (Graphics g, int x, int y) {
    if (image == null) g.fillRect (x + offset.x, y + offset.y, MainFrame.UNIT, MainFrame.UNIT);
    else g.drawImage (image, x + offset.x, y + offset.y - 5, MainFrame.UNIT, MainFrame.UNIT, null);
  }

  @Override
  public String getFilename() {
    return getDir() + "fighter.png";
  }

  public void setImage (Image image) {
    this.image = image;
  }

  @Override
  public Point getOffset () {
    return offset;
  }

}
