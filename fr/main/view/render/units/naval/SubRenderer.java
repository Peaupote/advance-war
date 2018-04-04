package fr.main.view.render.units.naval;

import java.awt.*;
import java.util.LinkedList;

import fr.main.model.players.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.Direction;
import fr.main.view.MainFrame;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.render.sprites.*;
import fr.main.view.render.animations.*;

public class SubRenderer extends UnitRenderer.Render {

  public SubRenderer (AbstractUnit unit) {
    super (unit);

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (53, 7, 16, 11, 2));
    areas.add(new ScaleRect (74, 7, 16, 11, 2));
    areas.add(new ScaleRect (94, 7, 16, 11, 2));
    AnimationState idle = new AnimationState(new SpriteList(getDir() + "sea.png", areas), 20);

    anim.put("idleRIGHT", idle);
    anim.setState("idleRIGHT");
  }

}