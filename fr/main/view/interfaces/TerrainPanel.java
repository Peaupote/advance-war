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
import fr.main.view.render.buildings.BuildingRenderer;
import fr.main.view.render.sprites.Sprite;

/**
 * User interface showing informations about the terrain, unit or building under the cursor
 */
public class TerrainPanel extends InterfaceUI {

  static final Color BACKGROUNDCOLOR = new Color(0,0,0,230);
  static final Color FOREGROUNDCOLOR = Color.white;
  static final int WIDTH = 100, HEIGHT = 200, MARGIN = 10,
          HALFW = MainFrame.WIDTH / (2 * MainFrame.UNIT),
          HALFH = MainFrame.HEIGHT / (2 * MainFrame.UNIT);

  /**
   * Unit icons
   */
  private static Sprite sp = Sprite.get("./assets/small_sprites.png");
  public static final Image lifeImage = sp.getImage(0, 8, 8, 8, 2),
                            munitionsImage = sp.getImage(0, 16, 8, 7, 2),
                            fuelImage = sp.getImage(0, 23, 8, 8, 2),
                            moveImage = sp.getImage(0, 0, 8, 8, 2);

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



    // Units info :
    AbstractUnit unit = world.getUnit(cursor.getX(), cursor.getY());
    AbstractBuilding building  = world.getBuilding(cursor.getX(), cursor.getY());
    if(unit != null &&
        (unit.getPlayer() == world.getCurrentPlayer() ||
         world.isVisibleOpponentUnit(cursor.getX(), cursor.getY()))) { // show unit
      g.drawString(unit.getName(), x + 15, y + 100);
      g.drawString(unit.getPlayer().name, x + 15, y + 120);

      g.drawImage(lifeImage, x + 15, y + 130, null);
      g.drawString(unit.getLife() + "/100", x + 35, y + 140);

      g.drawImage(moveImage, x + 15, y + 150, null);
      g.drawString(unit.getMoveQuantity() + "/" + unit.getMaxMoveQuantity(), x + 35, y + 160);
          
      Unit.Fuel fuel = unit.getFuel();
      g.drawImage(fuelImage, x + 15, y + 170, null);
      g.drawString(fuel.getQuantity()+"/"+fuel.maximumQuantity, x + 35, y + 180);
    } else {
      if (building != null) { // show building
        g.drawString(building.getName(), x + 15, y + 100);
        if (building instanceof OwnableBuilding) {
          Player p = ((OwnableBuilding)building).getOwner();
          g.drawString(p == null ? "Neutre" : p.name, x + 15, y + 120);
        }
      } else { // no unit nor building
        g.drawString ("No Unit", x + 15, y + 100);
        TerrainRenderer.render (g, new Point(x + 30, y + 20), cursor.position());
        g.drawString (world.getTerrain(cursor.getX(), cursor.getY()).toString(), x + 20, y + 80);
      }
    } 
    
    // Terrain or building image
    Point img = new Point(x + 30, y + 20);
    if (building == null) {
      TerrainRenderer.render (g, img, cursor.position());
      g.drawString (world.getTerrain(cursor.getX(), cursor.getY()).toString(), x + 20, y + 80);
    } else BuildingRenderer.render(g, img, building); 
  }

}

