package fr.main.view.render.units.land;

import java.awt.*;

import fr.main.model.units.land.Infantry;
import fr.main.view.MainFrame;
import fr.main.view.render.units.UnitAnimationManager;
import fr.main.view.render.units.UnitRenderer;

public class InfantryRenderer extends Infantry implements UnitRenderer {

  public static String filename = "machine-gun.png";

  private Point offset;
  private transient Image image;

  public InfantryRenderer (Point location) {
    super(null, location);

    offset = new Point (0, 0);
  }

  public void draw (Graphics g, int x, int y) {
    if (image == null) g.fillRect (x + offset.x, y + offset.y, MainFrame.UNIT, MainFrame.UNIT);
    else g.drawImage (image, x + offset.x, y + offset.y + UnitAnimationManager.getOffset() - 5, MainFrame.UNIT, MainFrame.UNIT, null);
  }

  @Override
  public String getFilename () {
    return getDir() + filename;
  }

  @Override
  public void setImage (Image image) {
    this.image = image;
  }

  @Override
  public Point getOffset () {
    return offset;
  }

}

