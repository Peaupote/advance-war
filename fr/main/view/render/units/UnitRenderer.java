package fr.main.view.render.units;

import java.awt.*;
import java.util.HashMap;

import fr.main.model.units.AbstractUnit;
import fr.main.model.Direction;
import fr.main.model.units.land.*;
import fr.main.model.units.air.*;
import fr.main.model.units.naval.*;

import fr.main.view.MainFrame;
import fr.main.view.render.Renderer;
import fr.main.view.render.units.air.*;
import fr.main.view.render.units.land.*;
import fr.main.view.render.units.naval.*;
import fr.main.view.render.animations.*;

public class UnitRenderer {

  protected static HashMap<AbstractUnit, Render> renderers = new HashMap<>();

  public static abstract class Render extends Renderer {

    protected Point offset;
    protected String state;
    protected Direction orientation;
    protected AbstractUnit unit;
    protected Animation anim;

    public Render (AbstractUnit unit) {
      this.unit        = unit;
      this.offset      = new Point(0, 0);
      this.orientation = Direction.RIGHT;
      this.state       = "idleRIGHT";
      this.anim        = new Animation();
    }

    public boolean moveOffset (Direction d) {
      d.move(offset);
      if (Math.abs(offset.x) == MainFrame.UNIT || Math.abs(offset.y) == MainFrame.UNIT) {
        offset.x = 0;
        offset.y = 0;
        return unit.move(d);
      }

      return true;
    }

    public void draw (Graphics g, int x, int y) {
      anim.draw(g, x + offset.x, y + offset.y);
    }

    public void setState (String state) {
      this.state = state;
      updateAnim();
    }

    public void setOrientation (Direction d) {
      this.orientation = d;
      updateAnim();
    }

    private void updateAnim () {
      anim.setState(state + orientation.toString());
    }
  }

  public static Render getRender (AbstractUnit unit) {
    if (renderers.containsKey(unit)) return renderers.get(unit);
    
    // TODO: make dynamic
    if (unit instanceof Fighter) renderers.put(unit, new FighterRenderer(unit));
    else if (unit instanceof Lander) renderers.put(unit, new LanderRenderer(unit));
    else if (unit instanceof Infantry) renderers.put(unit, new InfantryRenderer(unit));
    return renderers.get(unit);
  }

  public static void render(Graphics g, Point coords, AbstractUnit unit) {
    getRender(unit).draw (g, coords.x, coords.y);
  }

}

