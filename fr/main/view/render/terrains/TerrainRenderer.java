package fr.main.view.render.terrains;

import java.awt.*;
import java.util.HashMap;

import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.Direction;
import fr.main.model.terrains.land.*;
import fr.main.model.terrains.naval.*;

import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.view.render.terrains.land.*;
import fr.main.view.render.terrains.naval.*;
import fr.main.view.render.animations.*;

public class TerrainRenderer {

  public static HashMap<String, Render> renderers = new HashMap<>();

  public static class Render extends Renderer {

    protected Animation anim;

    public Render () {
      anim = new Animation();
    }

    public void draw (Graphics g, int x, int y) {
      anim.draw(g, x, y);
    }

  }

  public static Render getRender (AbstractTerrain terrain) {
    String name = terrain.getClass().toString(); 
    if (renderers.containsKey(name))
      return renderers.get(name);

    // TODO: nice stuff with reflection
    if (terrain instanceof Hill) renderers.put(name, new HillRenderer());
    else if (terrain instanceof Lowland) renderers.put(name, new LowlandRenderer());
    else if (terrain instanceof Mountain) renderers.put(name, new MountainRenderer());
    else if (terrain instanceof Beach) renderers.put(name, new BeachRenderer());
    else if (terrain instanceof Wood) renderers.put(name, new WoodRenderer());
    else if (terrain instanceof River) renderers.put(name, new RiverRenderer());
    else if (terrain instanceof Road) renderers.put(name, new RoadRenderer());
    else if (terrain instanceof Bridge) renderers.put(name, new BridgeRenderer());
    else if (terrain instanceof Reef) renderers.put(name, new ReefRenderer());
    else renderers.put(name, new SeaRenderer());

    return renderers.get(name);
  }

  public static void render(Graphics g, Point coords, AbstractTerrain terrain) {
    getRender(terrain).draw(g, coords.x, coords.y);
  }

}

