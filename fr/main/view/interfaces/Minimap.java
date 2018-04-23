package fr.main.view.interfaces;

import java.awt.Color;
import java.awt.Graphics;

import fr.main.model.Universe;
import fr.main.model.buildings.AbstractBuilding;
import fr.main.model.buildings.OwnableBuilding;
import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.terrains.land.LandTerrain;
import fr.main.model.terrains.naval.NavalTerrain;
import fr.main.model.units.AbstractUnit;
import fr.main.view.Position;

/**
 * In-game minimap user interface
 */
public class Minimap extends InterfaceUI {

  private final Position.Camera camera;
  private final Universe world;
  private final TerrainPanel reference;
  private final int size;
  private static final Color fogColor = new Color(0,0,0,200);

  public Minimap (Position.Camera camera, TerrainPanel reference) {
    this.camera    = camera;
    this.reference = reference;
    world          = Universe.get();
    size           = TerrainPanel.HEIGHT / world.getDimension().height;
  }

  public void draw (Graphics g) {
    @SuppressWarnings("unused")
	int x = camera.getX(),
        y = camera.getY(),
        w = world.getDimension().width,
        h = world.getDimension().height,
        mx = reference.leftSide ? reference.x + TerrainPanel.WIDTH + TerrainPanel.MARGIN : reference.x - TerrainPanel.MARGIN - w * size,
        my = reference.y;

    // TODO: more efficient stuff
    for (int i = 0; i < w; i++)
      for (int j = 0; j < w; j++) {
        int a = mx + j * size,
            b = my + i * size;
        AbstractUnit unit = world.getUnit(j, i);
        if (unit != null && (unit.getPlayer() == world.getCurrentPlayer() || world.isVisibleOpponentUnit(j, i))) {
          g.setColor(unit.getPlayer().color);
          g.fillRect(a, b, size, size);
        } else {
          AbstractBuilding building = world.getBuilding(j, i);
          if (building != null) {
            if (building instanceof OwnableBuilding) {
              OwnableBuilding ob = (OwnableBuilding)building;
              g.setColor (ob.getOwner() != null ? ob.getOwner().color : Color.white);
            } else g.setColor(Color.white);
            g.fillRect(a, b, size, size);
          } else {
            AbstractTerrain t = world.getTerrain(j, i);

            if (t instanceof LandTerrain) g.setColor(Color.green);
            else if (t instanceof NavalTerrain) g.setColor(Color.cyan);
            else g.setColor(Color.black);
            g.fillRect(a, b, size, size);
            if (!world.isVisible(j, i)) {
              g.setColor(fogColor);
              g.fillRect(a, b, size, size);
            }
          }
        }
      }

    g.setColor(Color.red);
    g.drawRect(mx + camera.getX() * size, my + camera.getY() * size,
               camera.width * size, camera.height * size);
        
  }

}
