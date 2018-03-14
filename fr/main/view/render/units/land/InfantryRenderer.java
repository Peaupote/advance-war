package fr.main.view.render.units.land;

import java.awt.*;
import java.util.LinkedList;

import fr.main.model.Player;
import fr.main.model.units.land.Infantry;
import fr.main.view.MainFrame;
import fr.main.view.render.units.UnitRenderer;
import fr.main.view.render.sprites.*;
import fr.main.view.render.animations.*;

public class InfantryRenderer extends Infantry implements UnitRenderer {

  private Point offset;
  private transient Image image;
  private transient Animation anim;

  public InfantryRenderer (Player player, Point location) {
    super(player, location);

    offset = new Point (0, 0);
  }

  public InfantryRenderer (Point location) {
  	super (null, location);
  	offset = new Point (0, 0);
  }

  public void draw (Graphics g, int x, int y) {
    anim.draw(g, x + offset.x, y + offset.y);
  }

  @Override
  public void update () {
    LinkedList<ScaleRect> areas = new LinkedList<>();
    areas.add(new ScaleRect (8, 95, 16, 16, 2));
    areas.add(new ScaleRect (28, 95, 16, 16, 2));
    areas.add(new ScaleRect (48, 95, 16, 16, 2));
    AnimationState idle = new AnimationState(new SpriteList("./assets/" + getColor() + "/sprites.png", areas), 20);

    areas = new LinkedList<>();
    areas.add(new ScaleRect (74, 95, 16, 16, 2));
    areas.add(new ScaleRect (95, 95, 16, 16, 2));
    areas.add(new ScaleRect (138, 95, 16, 16, 2));
    AnimationState run = new AnimationState(new SpriteList("./assets/" + getColor() + "/sprites.png", areas), 10);

    anim = new Animation();
    anim.put("idle", idle);
    anim.put("move", run);
    anim.setState("idle");
  }

  @Override
  public String getFilename () {
    return getDir() + "infantry.png";
  }

  @Override
  public void setImage (Image image) {
    this.image = image;
  }

  @Override
  public Point getOffset () {
    return offset;
  }

  public void setState (String state) {
    anim.setState (state);
  }

}

