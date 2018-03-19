package fr.main.view.render.units.land;

import java.awt.*;
import java.util.LinkedList;

import fr.main.model.Player;
import fr.main.model.units.AbstractUnit;
import fr.main.model.Direction;
import fr.main.view.MainFrame;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.render.sprites.*;
import fr.main.view.render.animations.*;

public class ReconRenderer extends UnitRenderer.Render {

  public ReconRenderer (AbstractUnit unit) {
    super (unit);

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (19, 252, 13, 15, 2));
    areas.add(new ScaleRect (41, 252, 13, 15, 2));
    AnimationState idle = new AnimationState(new SpriteList(getDir() + "sprites.png", areas), 20);

    anim.put("idleRIGHT", idle);
    anim.setState("idleRIGHT");
  }

}