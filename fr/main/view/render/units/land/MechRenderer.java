package fr.main.view.render.units.land;

import java.util.LinkedList;

import fr.main.model.units.AbstractUnit;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.sound.MusicEngine;

public class MechRenderer extends UnitRenderer.Render {

  public MechRenderer (AbstractUnit unit) {
    super (unit);

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (332, 54, 16, 16, 2));
    areas.add(new ScaleRect (354, 54, 16, 16, 2));
    AnimationState idle = new AnimationState(new SpriteList(getDir() + "sprites.png", areas), 20);

    anim.put("idleRIGHT", idle);
    anim.setState("idleRIGHT");
    
    this.attack = new MusicEngine("./assets/sound/bazooka.wav");
  }

}