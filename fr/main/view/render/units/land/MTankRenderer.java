package fr.main.view.render.units.land;

import java.util.LinkedList;

import fr.main.model.units.AbstractUnit;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;
import fr.main.view.render.units.UnitRenderer;

public class MTankRenderer extends UnitRenderer.Render {

  public MTankRenderer (AbstractUnit unit) {
    super (unit);

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (455, 1043, 16, 15, 2));
    areas.add(new ScaleRect (481, 1043, 16, 15, 2));
    AnimationState idle = new AnimationState(new SpriteList(getDir() + "sprites.png", areas), 20);

    anim.put("idleRIGHT", idle);
    anim.setState("idleRIGHT");
  }

}