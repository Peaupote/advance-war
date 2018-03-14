package fr.main.view.render.terrains.land;

import java.util.LinkedList;

import fr.main.view.render.terrains.TerrainRenderer;
import fr.main.view.render.sprites.*;
import fr.main.view.render.animations.*;

public class WoodRenderer extends TerrainRenderer.Render {

  public WoodRenderer () {
    super();

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (0, 0, 16, 16, 2));
    AnimationState idle = new AnimationState(new SpriteList("./assets/terrains/forest.png", areas), 20);
    anim.put("idle", idle);
    anim.setState("idle");
  }

}

