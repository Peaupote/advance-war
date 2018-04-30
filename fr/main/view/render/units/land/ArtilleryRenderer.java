package fr.main.view.render.units.land;

import java.util.LinkedList;

import fr.main.model.units.AbstractUnit;
import fr.main.view.render.animations.AnimationState;
import fr.main.view.render.sprites.ScaleRect;
import fr.main.view.render.sprites.SpriteList;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.sound.MusicEngine;

public class ArtilleryRenderer extends UnitRenderer.Render {

  public ArtilleryRenderer (AbstractUnit unit) {
    super (unit);

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (23, 718, 15, 14, 2));
    areas.add(new ScaleRect (48, 718, 15, 14, 2));
    AnimationState idle = new AnimationState(new SpriteList(getDir() + "sprites.png", areas), 20);

    anim.put("idleRIGHT", idle);
    anim.setState("idleRIGHT");
    
    this.selected = new MusicEngine("./assets/sound/SoldierLaughe.wav");
    this.attack = new MusicEngine("./assets/sound/CannonFire.wav");
  }

}