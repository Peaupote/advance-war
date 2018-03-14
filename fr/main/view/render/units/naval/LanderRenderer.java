package fr.main.view.render.units.naval;

import java.io.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.util.LinkedList;

import fr.main.model.terrains.Terrain;
import fr.main.model.units.Path;
import fr.main.model.Direction;
import fr.main.view.MainFrame;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.render.sprites.*;
import fr.main.view.render.animations.*;
import fr.main.model.units.naval.Lander;

public class LanderRenderer extends Lander implements UnitRenderer {

  private Point offset;

  private transient Animation anim;

  public LanderRenderer (Point location) {
    super (null, location);

    offset = new Point(0, 0);
  }

  public void draw (Graphics g, int x, int y) {
    anim.draw(g, x + offset.x, y + offset.y);
  }

  public void update () {
    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect(9, 3, 16, 16, 2));
    areas.add(new ScaleRect(27, 3, 16, 16, 2));
    AnimationState idle = new AnimationState (new SpriteList("./assets/red/sea.png", areas), 30);

    areas = new LinkedList<>();
    areas.add(new ScaleRect(7, 60, 18, 13, 2));
    areas.add(new ScaleRect(27, 60, 18, 13, 2));
    AnimationState move = new AnimationState (new SpriteList("./assets/red/sea.png", areas), 30);

    anim = new Animation ();
    anim.put("idle", idle);
    anim.put("move", move);
    anim.setState("idle");
  }

  public void setState (String state) {
    anim.setState(state);
  }

  @Override
  public String getFilename() {
    return getDir() + "lander.png";
  }

  public void setImage (Image image) {
  }

  @Override
  public Point getOffset () {
    return offset;
  }

  public void setOrientation (Direction d) {}

}
