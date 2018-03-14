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

public class InfantryRenderer extends UnitRenderer.Render {

  public InfantryRenderer (AbstractUnit unit) {
    super (unit);

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (8, 95, 16, 16, 2));
    areas.add(new ScaleRect (28, 95, 16, 16, 2));
    areas.add(new ScaleRect (48, 95, 16, 16, 2));
    AnimationState idle = new AnimationState(new SpriteList("./assets/red/sprites.png", areas), 20);

    areas = new LinkedList<>();
    areas.add(new ScaleRect (74, 95, 16, 16, 2));
    areas.add(new ScaleRect (95, 95, 16, 16, 2));
    areas.add(new ScaleRect (138, 95, 16, 16, 2));
    AnimationState run = new AnimationState(new SpriteList("./assets/red/sprites.png", areas), 10);

    anim.put("idleRIGHT", idle);
    anim.put("moveRIGHT", run);
    anim.setState("idleRIGHT");
  }

}

