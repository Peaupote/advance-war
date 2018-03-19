package fr.main.view.render.terrains.land;

import java.util.LinkedList;
import java.awt.Graphics;

import fr.main.view.render.terrains.TerrainRenderer;
import fr.main.view.render.sprites.*;
import fr.main.view.render.animations.*;

public class MountainRenderer extends TerrainRenderer.Render {

  public MountainRenderer () {
    super();

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (0, 0, 16, 20, 2));
    AnimationState idle = new AnimationState(new SpriteList("./assets/terrains/mountain.png", areas), 20);
    anim.put("idle", idle);
    anim.setState("idle");
  }

  public void draw (Graphics g, int x, int y) {
    anim.draw(g, x, y);
  }

}

