package fr.main.view.render.terrains.land;

import java.util.LinkedList;

import fr.main.view.render.terrains.TerrainRenderer;
import fr.main.view.render.sprites.*;
import fr.main.view.render.animations.*;

public class LowlandRenderer extends TerrainRenderer.Render {

  public LowlandRenderer () {
    super();

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (17, 17, 16, 16, 2));
    AnimationState idle = new AnimationState(new SpriteList("./assets/terrains/rivers1.png", areas), 20);
    anim.put("idle", idle);
    anim.setState("idle");
  }

}

