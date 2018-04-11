package fr.main.view.render.units.air;

import java.util.LinkedList;

import fr.main.model.units.AbstractUnit;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;
import fr.main.view.render.units.UnitRenderer;

public class TCopterRenderer extends UnitRenderer.Render {

  public TCopterRenderer (AbstractUnit unit) {
    super (unit);

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (21, 98, 15, 14, 2));
    areas.add(new ScaleRect (41, 98, 15, 14, 2));
    areas.add(new ScaleRect (59, 98, 16, 14, 2));
    AnimationState idle = new AnimationState(new SpriteList(getDir() + "air.png", areas), 20);

    anim.put("idleRIGHT", idle);
    anim.setState("idleRIGHT");
  }

}