package fr.main.view.render.terrains;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

import fr.main.model.terrains.AbstractTerrain;
import fr.main.model.Direction;
import fr.main.model.terrains.land.*;
import fr.main.model.terrains.naval.*;

import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;
import fr.main.view.render.terrains.land.*;
import fr.main.view.render.terrains.naval.*;
import fr.main.view.render.animations.*;

public class TerrainRenderer {

  public static HashMap<TerrainLocation, Render> renderers = new HashMap<>();

  public static class Render extends Renderer {

    protected Animation anim;

    public Render () {
      anim = new Animation();
    }

    public Render (TerrainLocation location) {
    	anim = new Animation();

		LinkedList<ScaleRect> areas = new LinkedList<>();
		areas.add(location.getRect());
		AnimationState idle = new AnimationState(new SpriteList(location.getPath(), areas), 20);
		anim.put("idle", idle);
		anim.setState("idle");
	}

    public void draw (Graphics g, int x, int y) {
      anim.draw(g, x, y);
    }

  }

  public static Render getRender (AbstractTerrain terrain, TerrainLocation location) {
    if (renderers.containsKey(location))
      return renderers.get(location);

    // TODO: nice stuff with reflection
    if (location instanceof TerrainLocation.HillLocation) 			renderers.put(location, new HillRenderer((TerrainLocation.HillLocation) location));
    else if (location instanceof TerrainLocation.LowlandLocation) 	renderers.put(location, new LowlandRenderer((TerrainLocation.LowlandLocation) location));
    else if (location instanceof TerrainLocation.MountainLocation) 	renderers.put(location, new MountainRenderer((TerrainLocation.MountainLocation) location));
    else if (location instanceof TerrainLocation.BeachLocation) 	renderers.put(location, new BeachRenderer((TerrainLocation.BeachLocation) location));
    else if (location instanceof TerrainLocation.WoodLocation) 		renderers.put(location, new WoodRenderer((TerrainLocation.WoodLocation) location));
    else if (location instanceof TerrainLocation.RiverLocation) 	renderers.put(location, new RiverRenderer((TerrainLocation.RiverLocation) location));
    else if (location instanceof TerrainLocation.RoadLocation) 		renderers.put(location, new RoadRenderer((TerrainLocation.RoadLocation) location));
    else if (location instanceof TerrainLocation.BridgeLocation) 	renderers.put(location, new BridgeRenderer((TerrainLocation.BridgeLocation) location));
    else if (location instanceof TerrainLocation.ReefLocation) 		renderers.put(location, new ReefRenderer((TerrainLocation.ReefLocation) location));
    else if (location instanceof TerrainLocation.SeaLocation)		renderers.put(location, new SeaRenderer((TerrainLocation.SeaLocation) location));
	else {
		System.err.println("ERROR in TerrainRenderer");
		System.exit(10);
	}

    return renderers.get(location);
  }

  public static void render(Graphics g, Point coords, AbstractTerrain terrain, TerrainLocation location) {
    getRender(terrain, location).draw(g, coords.x, coords.y);
  }

}

