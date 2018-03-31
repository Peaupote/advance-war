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

public class BattleshipRenderer extends UnitRenderer.Render {

  public BattleshipRenderer (AbstractUnit unit) {
    super (unit);

    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (119, 4, 16, 15, 2));
    areas.add(new ScaleRect (139, 4, 16, 15, 2));
    AnimationState idle = new AnimationState(new SpriteList(getDir() + "sea.png", areas), 20);

    anim.put("idleRIGHT", idle);
    anim.setState("idleRIGHT");
  }

}