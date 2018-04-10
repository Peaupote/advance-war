package fr.main.view.render.units.air;

import java.util.LinkedList;

import fr.main.model.units.AbstractUnit;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;
import fr.main.view.render.units.UnitRenderer;

public class BomberRenderer extends UnitRenderer.Render {

  public BomberRenderer (AbstractUnit unit) {
    super (unit);

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (94, 100, 16, 16, 2));
    areas.add(new ScaleRect (117, 100, 16, 16, 2));
    AnimationState idle = new AnimationState(new SpriteList(getDir() + "air.png", areas), 20);

    anim.put("idleRIGHT", idle);
    anim.setState("idleRIGHT");
  }

}