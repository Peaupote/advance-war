package fr.main.view.render.units.air;

import java.util.LinkedList;

import fr.main.model.units.AbstractUnit;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;
import fr.main.view.render.units.UnitRenderer;

public class BCopterRenderer extends UnitRenderer.Render {

  public BCopterRenderer (AbstractUnit unit) {
    super (unit);

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (18, 1, 16, 17, 2));
    areas.add(new ScaleRect (38, 1, 16, 17, 2));
    areas.add(new ScaleRect (58, 1, 16, 17, 2));
    AnimationState idleRight = new AnimationState(new SpriteList(getDir() + "air.png", areas), 20);

    areas = new LinkedList<>();
    areas.add(new ScaleRect (18, 1, 16, 17, 2, ScaleRect.Flip.VERTICALY));
    areas.add(new ScaleRect (38, 1, 16, 17, 2, ScaleRect.Flip.VERTICALY));
    areas.add(new ScaleRect (58, 1, 16, 17, 2, ScaleRect.Flip.VERTICALY));
    AnimationState idleLeft = new AnimationState(new SpriteList(getDir() + "air.png", areas), 50);

    areas = new LinkedList<>();
    areas.add(new ScaleRect(20, 20, 16, 25, 2));
    areas.add(new ScaleRect(38, 20, 16, 25, 2));
    AnimationState moveRight = new AnimationState(new SpriteList(getDir() + "air.png", areas), 15);

    areas = new LinkedList<>();
    areas.add(new ScaleRect(20, 20, 16, 25, 2, ScaleRect.Flip.VERTICALY));
    areas.add(new ScaleRect(38, 20, 16, 25, 2, ScaleRect.Flip.VERTICALY));
    AnimationState moveLeft = new AnimationState(new SpriteList(getDir() + "air.png", areas), 15);

    areas = new LinkedList<>();
    areas.add(new ScaleRect(21, 45, 19, 25, 2));
    areas.add(new ScaleRect(39, 45, 19, 25, 2));
    AnimationState idleBottom = new AnimationState(new SpriteList(getDir() + "air.png", areas), 50);

    areas = new LinkedList<>();
    areas.add(new ScaleRect(19, 72, 19, 20, 2));
    areas.add(new ScaleRect(39, 72, 19, 20, 2));
    AnimationState idleTop = new AnimationState(new SpriteList(getDir() + "air.png", areas), 50);

    anim.put("idleRIGHT", idleRight);
    anim.put("idleLEFT", idleLeft);
    anim.put("idleTOP", idleTop);
    anim.put("idleBOTTOM", idleBottom);

    anim.put("moveRIGHT", moveRight);
    anim.put("moveLEFT", moveLeft);
    anim.setState("idleRIGHT");
  }

}