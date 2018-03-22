package fr.main.view.interfaces;

import java.awt.*;

import fr.main.view.MainFrame;
import fr.main.model.Universe;
import fr.main.model.units.AbstractUnit;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.Player;
import fr.main.model.units.Unit;
import fr.main.view.Position;
import fr.main.view.render.terrains.TerrainRenderer;

public class TerrainPanel extends InterfaceUI {

  static final Color BACKGROUNDCOLOR = new Color(0,0,0,230);
  static final Color FOREGROUNDCOLOR = Color.white;
  static final int WIDTH = 100, HEIGHT = 200, MARGIN = 10,
          HALFW = MainFrame.WIDTH / (2 * MainFrame.UNIT),
          HALFH = MainFrame.HEIGHT / (2 * MainFrame.UNIT);

  boolean leftSide;
  int x, y;

  protected final Position.Cursor cursor;
  protected final Position.Camera camera;
  protected final Universe world;

  public TerrainPanel (Position.Cursor cursor, Position.Camera camera) {
    this.cursor = cursor;
    this.camera = camera;
    world = Universe.get();
    y = MainFrame.HEIGHT - HEIGHT - MARGIN;
  }

  @Override
  protected void draw (Graphics g) {
    leftSide = cursor.getX() - camera.getX() >= HALFW && cursor.getY() - camera.getY() >= HALFH; 
    x = leftSide ? MARGIN : MainFrame.WIDTH - WIDTH - MARGIN;
    
    g.setColor (BACKGROUNDCOLOR);
    g.fillRect (x, y, WIDTH, HEIGHT);

    g.setColor (FOREGROUNDCOLOR);

    TerrainRenderer.render (g, new Point(x + 30, y + 20), cursor.position());
    g.drawString (world.getTerrain(cursor.getX(), cursor.getY()).toString(), x + 20, y + 80);



    // Units info :
    AbstractUnit unit = world.getUnit(cursor.getX(), cursor.getY());
    if(world.isVisible(cursor.getX(), cursor.getY()) && unit != null) {
      g.drawString(unit.getName(), x + 20, y + 100);
      g.drawString(unit.getPlayer().name, x + 20, y + 120);
      g.drawString(unit.getLife() + "/100", x + 20, y + 140);
      g.drawString(unit.getMoveQuantity() + "/" + unit.getMaxMoveQuantity(), x + 20, y + 160);
          
      Unit.Fuel fuel = unit.getFuel();
      g.drawString(fuel.getQuantity()+"/"+fuel.maximumQuantity, x + 20, y + 180);
    } else {
      AbstractBuilding building  = world.getBuilding(cursor.getX(), cursor.getY());
      if (building != null) {
        g.drawString(building.getName(), x + 20, y + 100);
        if (building instanceof OwnableBuilding) {
          Player p = ((OwnableBuilding)building).getOwner();
          g.drawString(p == null ? "Neutral" : p.name, x + 20, y + 120);
        }
      } else g.drawString ("No Unit", x + 20, y + 100);
    }
  }

}

