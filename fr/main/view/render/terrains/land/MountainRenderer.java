package fr.main.view.render.terrains.land;

import java.util.LinkedList;
import java.awt.Graphics;

import fr.main.view.render.terrains.TerrainLocation;
import fr.main.view.render.terrains.TerrainRenderer;
import fr.main.view.render.sprites.*;
import fr.main.view.render.animations.*;

public class MountainRenderer extends TerrainRenderer.Render {

  public MountainRenderer (TerrainLocation.MountainLocation location) {
    super();

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (0, 0, 16, 20, 2));
    AnimationState normal = new AnimationState(new SpriteList(TerrainLocation.getDir() + "normal/" + location.getPath(), areas), 20);
    AnimationState snow   = new AnimationState(new SpriteList(TerrainLocation.getDir() + "snow/"   + location.getPath(), areas), 20);
    anim.put("normal", normal);
    anim.put("snow",   snow);
    anim.setState("normal");
  }

  public void draw (Graphics g, int x, int y) {
    anim.draw(g, x, y);
  }

}

