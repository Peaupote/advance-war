package fr.main.view.render.units.naval;

import java.util.LinkedList;

import fr.main.model.units.AbstractUnit;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;
import fr.main.view.render.units.UnitRenderer;

public class CruiserRenderer extends UnitRenderer.Render {

  public CruiserRenderer (AbstractUnit unit) {
    super (unit);

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (174, 7, 13, 12, 2));
    areas.add(new ScaleRect (193, 7, 13, 12, 2));
    AnimationState idle = new AnimationState(new SpriteList(getDir() + "sea.png", areas), 20);

    anim.put("idleRIGHT", idle);
    anim.setState("idleRIGHT");
  }

}