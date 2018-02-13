package fr.main.view.interfaces;

import java.awt.Graphics;
import java.awt.Color;

import fr.main.model.terrains.naval.*;
import fr.main.model.terrains.land.*;
import fr.main.model.terrains.Terrain;
import fr.main.model.Universe;
import fr.main.view.Position;
import fr.main.view.MainFrame;
import fr.main.view.Controller;

public class Minimap extends InterfaceUI {

  private final Position.Camera camera;
  private final Universe world;
  private final TerrainPanel reference;
  private final int size;
  private static final Color fogColor = new Color(0,0,0,200);

  public Minimap (Position.Camera camera, TerrainPanel reference) {
    this.camera = camera;
    this.reference = reference;
    world = Universe.get();
    size = TerrainPanel.HEIGHT / world.getDimension().height;
  }

  public void draw (Graphics g) {
    int x = camera.getX(),
        y = camera.getY(),
        w = world.getDimension().width,
        h = world.getDimension().height,
        mx = reference.leftSide ? reference.x + TerrainPanel.WIDTH + TerrainPanel.MARGIN : reference.x - TerrainPanel.MARGIN - w * size,
        my = reference.y;

    // TODO: more efficient stuff
    for (int i = 0; i < w; i++)
      for (int j = 0; j < w; j++) {
        Terrain t = world.getTerrain(j, i);
        int a = mx + j * size,
            b = my + i * size;
        if (t instanceof Lowland) g.setColor(Color.green);
        else if (t instanceof Sea) g.setColor(Color.cyan);
        else if (t instanceof Reef) g.setColor(Color.blue);
        else g.setColor(Color.black);
        g.fillRect(a, b, size, size);
        if (!world.isVisible(j, i)) {
          g.setColor(fogColor);
          g.fillRect(a, b, size, size);
        }
      }

    g.setColor(Color.red);
    g.drawRect(mx + camera.getX() * size, my + camera.getY() * size,
               camera.width * size, camera.height * size);
        
  }

}
